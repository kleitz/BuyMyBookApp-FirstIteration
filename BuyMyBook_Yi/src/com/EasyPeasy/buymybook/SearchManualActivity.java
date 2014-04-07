package com.EasyPeasy.buymybook;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.Bundle;
import android.app.Activity;
import android.app.ListActivity;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;


/*
 * This activity is the result
 * xmlfile:activity_search_manual.xml
 */
public class SearchManualActivity extends Activity {
	private String tag = "SearchManualActivity";
	
	//adapter for the listview
	SimpleAdapter adapter;
	
	List<Map<String, String>> listings = new ArrayList<Map<String,String>>();
	

	/*
	 * Function to fake listings
	 */
	/*
	private ArrayList populateBooks(){
		ArrayList results = new ArrayList();
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
		return results;
	}
	*/
	/*
	 * Function that creates a listing for the with given Key and Value
	 */
	private HashMap<String,String> createListing(String key, String value){
		HashMap<String,String> data = new HashMap<String,String>();
		data.put(key,value);
		return data;
	}
	
	private ArrayList parseJSONResult(JSONArray jsonArray){
		
		ArrayList results = new ArrayList();
		
		try{
			for(int i = 0 ; i < jsonArray.length();i++){
				JSONObject childObject = jsonArray.getJSONObject(i);
				String title = childObject.getString("title");
				String author = childObject.getString("authors");
				String price = "1";
				String condition = "1";
				
				//String price = childObject.getString("listing_price");
				//String condition = childObject.getString("condition");
			
				SearchListItem item = new SearchListItem(title,author,price,condition);
				
				results.add(item);
			}//for
		}//try
		catch(JSONException e){
			Log.e(tag,"Exception in parseJSONResult: "+e.toString());
		}
		return results;
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
	
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_search_manual);
		Log.d(tag,"onCreate SearchManualActivity");
		/*
		 * 
		 * If we have json to parse, parse it
		 * 
		 */
		Bundle extras = getIntent().getExtras();
		String result = extras.getString("json");
		Log.d(tag,"extras: "+result);
		JSONArray endResult = new JSONArray();
		/*
		 * Problem: listings vs single item
		 * Will Alex return a listing? or just one data item
		 * 
		 */
		JSONObject book = new JSONObject();
		JSONObject data = new JSONObject();
		if(result != null){
			try{
				JSONObject jsonString = new JSONObject(result);
				data = jsonString.getJSONObject("data");
				Log.d(tag,"got data");
				endResult = data.getJSONArray("book");
				//endResult.put(data);
				Log.d(tag,"got book");
				//endResult = book.getJSONArray("listings");
				Log.d(tag,"got end result");
				Log.d(tag,"endResult size: "+endResult.length());
				
			}
			catch(JSONException e){
				Log.d(tag,"Exception: "+e.toString());
			}
		}//if
		//endResult.put(book);
		Log.d(tag,"endResult size: "+endResult.length());
		Log.d(tag,"endResult: "+endResult.toString());
	
		ArrayList image_details;
		image_details = parseJSONResult(endResult);

        final ListView lv1 = (ListView) findViewById(R.id.search_manual_listview);
        lv1.setAdapter(new CustomSearchListAdaptor(this, image_details));
        lv1.setOnItemClickListener(new OnItemClickListener() {
        	/*
        	 * Click listener for each item view
        	 * @see android.widget.AdapterView.OnItemClickListener#onItemClick(android.widget.AdapterView, android.view.View, int, long)
        	 */
            @Override
            public void onItemClick(AdapterView<?> a, View v, int position, long id) {
                Object o = lv1.getItemAtPosition(position);
                SearchListItem newsData = (SearchListItem) o;
                Toast.makeText(SearchManualActivity.this, "Selected :" + " " + newsData, Toast.LENGTH_LONG).show();
               
            }
 
        });
	
	}
public void detailsFragment(){
	
}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		//getMenuInflater().inflate(R.menu.search_manual, menu);
		return true;
	}
	

}
