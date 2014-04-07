package com.EasyPeasy.buymybook;
import com.EasyPeasy.buymybook.CustomSearchListAdaptor;
import com.EasyPeasy.buymybook.CommunicationClass.DownloadJSON;



import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
/*
 * xml file: fragment_results_detail
 */
public class SearchResultsFragment extends Fragment{
	final String tag = "SearchResultsFragment";
	String result = null;

	/*
	 *Called before onCreateView() of the fragment
	 */
	public SearchResultsFragment(){
		super();
		;
	}
	private ArrayList populateBooks(){
		ArrayList results = new ArrayList();
		for(int i = 0 ; i< 20; i++){
			String title="Clear Thinking In a Blurry World";
			String author="Tim Kenyon";
			String price="100";
			String condition="1";
			SearchListItem item = new SearchListItem(title,author,price,condition);
			results.add(item);
			title = "Introduction to Algorithmns";
			author = "Cormen";
			price = "50";
			condition = "5";
			item =new SearchListItem(title,author,price,condition);
			results.add(item);
		}
		return results;
	}
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		//Log.d(tag,"onCreate");
		//setContentView(R.layout.activity_search_manual);
		result = getArguments() != null ? getArguments().getString("json") : null;
		//Log.d(tag,"results: "+ result);
	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState){
		Log.d(tag,"onCreateView");
		
		
		
		View view = inflater.inflate(R.layout.fragment_results_search,container,false);
		/*String scanContent = new String("9780176251949");
	   String url="http://buymybookapp.com/api/search/get_book/"+scanContent;
		//CommunicationClass c = new CommunicationClass(url);
		String getUrl = new String();
		/*try {
			getUrl = c.new DownloadJSON(view.getContext(),"image_get").execute(url).get();
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (ExecutionException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		Log.d(tag,"geturl: " + getUrl.toString());
		String imageUrl = null;
		if(getUrl != null){
			try {
				JSONObject jsonString = new JSONObject(getUrl);
				jsonString = jsonString.getJSONObject("data");
				jsonString = jsonString.getJSONObject("book");
				imageUrl= jsonString.getString("amazon_small_image");

				//Log.d(tag,"imageurl: " + imageUrl.toString());
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}*/
		//String imageUrl = null;
		JSONArray endResult = new JSONArray();
		/*
		 * Problem: listings vs single item
		 * Will Alex return a listing? or just one data item
		 * 
		 */
		JSONObject data = new JSONObject();
		if(result != null){
			try{
				JSONObject jsonString = new JSONObject(result);
				data = jsonString.getJSONObject("data");
				//Log.d(tag,"got data");
				//endResult = data.getJSONArray("book");
				//endResult.put(data);
				//Log.d(tag,"got book");
				endResult = data.getJSONArray("listings");
				//Log.d(tag,"got end result");
				//Log.d(tag,"endResult size: "+endResult.length());
				
			}
			catch(JSONException e){
				Log.d(tag,"Exception: "+e.toString());
			}
		}//if
		//endResult.put(book);
		//Log.d(tag,"endResult size: "+endResult.length());
	//	Log.d(tag,"endResult: "+endResult.toString());
	
		//String imageURL = endResult.getJSONObject(0).getString();
		ArrayList image_details;
		image_details = parseJSONResult(endResult);
		//grab url from listings
		String isbn = null;
		try {
			isbn = endResult.getJSONObject(0).getString("isbn_13");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		String url="http://buymybookapp.com/api/search/get_book/"+isbn;
		CommunicationClass c = new CommunicationClass();
		String imgUrl = null;
		try {
			//grabs imgUrl
			imgUrl = c.new DownloadJSON(view.getContext(),"image_get").execute(url).get();
			JSONObject json = new JSONObject(imgUrl);
			imgUrl = json.getJSONObject("data").getJSONObject("book").getString("amazon_small_image");
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		Log.d(tag,"IMGURL: "+ imgUrl);
		if(image_details == null){
			image_details = populateBooks();
			Log.d(tag,"image_details null: ");
		}
		else{
			Log.d(tag,"imagedetails not null");
		}
			//fake input
			//image_details = populateBooks();
		   final ListView lv1 = (ListView) view.findViewById(R.id.search_manual_listview);
		   if(lv1 != null){
			   lv1.setAdapter(new CustomSearchListAdaptor(getActivity(), image_details,imgUrl));
			   lv1.setOnItemClickListener(new OnItemClickListener() {
		        	/*
		        	 * Click listener for each item view
		        	 * @see android.widget.AdapterView.OnItemClickListener#onItemClick(android.widget.AdapterView, android.view.View, int, long)
		        	 */
		            @Override
		            public void onItemClick(AdapterView<?> a, View v, int position, long id) {
		                Object o = lv1.getItemAtPosition(position);
		                SearchListItem newsData = (SearchListItem) o;
		               // Toast.makeText(getActivity(), "Selected :" + " " + newsData, Toast.LENGTH_LONG).show();            
		                detailsFragment(newsData);
		            }
		 
		        });
		   }
		   else{
			   Log.d(tag,"searchresultfragment, adapter null");
		   }	       
		   //lv1.setAdapter(new CustomSearchListAdaptor(inflater, image_details));
		return view;
	}

	public void detailsFragment(SearchListItem newsData){
		FragmentActivity f;
		Fragment fragment = null;
		fragment =  new DetailsFragment();
		f = getActivity();
			if (fragment != null) {
				Bundle args = new Bundle();
				args.putString("title", newsData.getTitle());
				args.putString("author", newsData.getAuthor());
				args.putString("price", newsData.getPrice());
				args.putString("condition", newsData.getCondition());
				args.putString("comments", newsData.getComments());
				args.putString("contactNum", newsData.getNum());
				args.putString("contactEmail", newsData.getEmail());
				args.putString("firstname", newsData.firstname);
				args.putString("lastname", newsData.lastname);
				Log.d(tag, "detailfragmet(): " + newsData.firstname + newsData.lastname + " : " + newsData.getEmail()+" : "+ newsData.getNum());
				fragment.setArguments(args);
				f.getSupportFragmentManager().beginTransaction().replace(R.id.frame_container, fragment).addToBackStack(null).commit();
			}
	}
		
	private ArrayList parseJSONResult(JSONArray jsonArray){
			
			ArrayList results = new ArrayList();
			
			try{
				for(int i = 0 ; i < jsonArray.length();i++){
					JSONObject childObject = jsonArray.getJSONObject(i);
					String title = childObject.getString("book_title");
				String author = childObject.getString("authors");
				String comments = childObject.getString("comments");
	
				//String price = "1";
				//String condition = "1";
				
				String price = childObject.getString("listing_price");
				String condition = childObject.getString("condition");
				String phone = childObject.getString("phone_number");
				String email = childObject.getString("email");
				//Log.d(tag,"parseJsonResult: "+title+author);
				SearchListItem item = new SearchListItem(title,author,price,condition);
				if(comments != null && !comments.equals("null")) item.setComments(comments);
				if(phone != null && !phone.equals("null")) item.setNum(phone);
				if(email != null && !email.equals("null")) item.setEmail(email);
				item.firstname = childObject.getString("first_name");
				item.lastname = childObject.getString("last_name");
				Log.d(tag," parsejson: name: " +item.firstname + item.lastname + " email: " +phone + ":"+email);
				//if(comments != null && !comments.equals("null")) item.setComments(comments);
				//item.setUrl(url);
				results.add(item);
			}//for
		}//try
		catch(JSONException e){
			Log.e(tag,"Exception in parseJSONResult: "+e.toString());
		}
		return results;
	}

}
