package com.EasyPeasy.buymybook;


import java.util.concurrent.ExecutionException;

import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

/*
 * flow of this activity:
 * 1. launch with welcome message - saying "get ready to scan your book"
 * 2. launch scannerManager - scan and we'll get (isbn,format)
 * 3. query our db for book info - not done
 * 4. display result - either error, or result screen(top=info from server, bottom = questions)
 * 		questions = how much do you want to sell it for, condition, take a picture
 * 5. user press sell, load in to server(not done) and local db(not done).
 */
public class PostActivity extends MainActivity implements OnClickListener{
	final Context context = this;
	
 //UI elements
	//landing page
	private ImageButton scanBtn;
	
	//enter info page
	String json;
	String title;
	String author;
	private TextView title_info;
	private TextView author_info;
	private Button confirmBtn;
	
	ArrayAdapter<String> condition_adapter;
	private EditText my_price;
	private Spinner condition_spinner;
	private EditText my_subject;
	private EditText my_coursenum;
	private EditText my_comment;
	String[] conditions = new String[]{
			"As New",
	        "Very Good",
	        "Good",
	        "Fair",
	        "Poor",
	        "Decomposed"
	};
	/*
	private TextView info;
	private Button confirmBtn;
	private EditText priceTag;
	private Spinner spinner;
	*/
	
	//result page
	
	//data elements for posting
	String isbn_13;
	String price;
	String condition;
	int isActive;
	String subject;
	String catalog_number;
	String comments;
	
	
	/*if kahlim gets stuff done */
	String first_name;
	String last_name;
	String phone;
	String email;
	private DBHelper dbHelper;
	
	@SuppressLint("NewApi")
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.dieAfterFinish=true;
		//System.out.println("started onCreate for PostActivity");
		
		//set up UI
		setContentView(R.layout.activity_post);
		scanBtn = (ImageButton)findViewById(R.id.launch_scanner_button);
		scanBtn.setOnClickListener(this);
		this.setupDrawer(savedInstanceState);
		
		dbHelper = new DBHelper(this);
		
	}

	/**
	 * Set up the {@link android.app.ActionBar}, if the API is available.
	 */
	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	private void setupActionBar() {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			getActionBar().setDisplayHomeAsUpEnabled(true);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.post, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			// This ID represents the Home or Up button. In the case of this
			// activity, the Up button is shown. Use NavUtils to allow users
			// to navigate up one level in the application structure. For
			// more details, see the Navigation pattern on Android Design:
			//
			// http://developer.android.com/design/patterns/navigation.html#up-vs-back
			//
			NavUtils.navigateUpFromSameTask(this);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onClick(View v) { // DON'T CHANGE THIS -YI
		
		switch (v.getId()) {
		
		case R.id.launch_scanner_button:
			System.out.println("i should scan something....");
			scanBtn.setBackgroundResource(R.drawable.scan_button_contact);
			
			Intent intent = new Intent(this, ScannerManager.class);
			startActivityForResult(intent, 1);
			overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left);
			
			break;
		case R.id.post_enterinfo_confirm:
			
			
			//1. fake a post to the server
			String url = "http://buymybookapp.com/api/listings/post?";
			price=my_price.getText().toString();
			condition=condition_spinner.getSelectedItem().toString();
			if (condition.equals("As New")) {
				condition="5";
			} else if (condition.equals("Very Good")) {
				condition="4";
			} else if (condition.equals("Good")) {
				condition="3";
			} else if (condition.equals("Fair")) {
				condition="2";
			} else if (condition.equals("Poor")) {
				condition="1";
			} else {
				condition="0";
			}
			catalog_number=my_coursenum.getText().toString();
			subject=my_subject.getText().toString();
			
			SharedPreferences settings = getSharedPreferences("MyLoginInfo", 0);
			
			first_name=settings.getString("fbFirstName", null);
			last_name=settings.getString("fbLastName", null);
			phone=settings.getString("fbTextNum", null);
			email=settings.getString("fbEmail", null);
			
			String comment =my_comment.getText().toString();
			comment = comment.replace(" ", "%20");
			url = url +
					"isbn_13="+isbn_13 +
					"&listing_price="+price+
					"&condition="+condition+
					"&catalog_number="+catalog_number+
					"&subject="+subject+
					"&comments="+comment+
					"&first_name="+first_name+
					"&last_name="+last_name+
					"&phone_number="+phone+
					"&email="+email;
			System.out.println(" I'm going to fake a post! "+url);
			
			CommunicationClass c = new CommunicationClass(url);
			String return_json=null;
			
			try {
				return_json = c.new DownloadJSON(this,"post").execute(url).get();
				
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ExecutionException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			JSONObject jsonObj=null;
			try {
				jsonObj = new JSONObject(return_json);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			String listing_id=null;
			try {
				listing_id =jsonObj.getJSONObject("data").getJSONObject("insert_obj").getString("id");
				String status=jsonObj.getJSONObject("status").getString("status");
				System.out.println("posted to server! status was:"+status);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			//2. post to local db
			dbHelper.insert(listing_id, title, isbn_13, author, price, condition+"");
			//3. toast
			Toast toast = Toast.makeText(
					getApplicationContext(),
					title+" has been posted!",
					Toast.LENGTH_SHORT);
			toast.show();
			this.finish();
		}
		
	}
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		if ( resultCode == RESULT_OK && requestCode == 1 )
		{
			//1. get isbn from scanner
			isbn_13=data.getStringExtra("isbn");
			System.out.println("scanner returned, isbn is "+isbn_13);
			
			//2. talk to server to get book info
				//9780201314526=cs246
			String url="http://buymybookapp.com/api/search/get_book/" + isbn_13;
			CommunicationClass c = new CommunicationClass(url);
			String return_json=null;
			
			try {
				return_json = c.new DownloadJSON(this,"post").execute(url).get();
				
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ExecutionException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			JSONObject jsonObj=null;
			try {
				jsonObj = new JSONObject(return_json);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			try {
				title = jsonObj.getJSONObject("data").getJSONObject("book").getString("title");
				author = jsonObj.getJSONObject("data").getJSONObject("book").getString("authors");
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		
			//3. set layout screen for following page : crappy onCreate
			setContentView(R.layout.activity_post_enterinfo);
			//enterinfo UI
			title_info = (TextView)findViewById(R.id.post_enterinfo_title);
			author_info = (TextView)findViewById(R.id.post_enterinfo_author);
			confirmBtn = (Button)findViewById(R.id.post_enterinfo_confirm);	
	        confirmBtn.setOnClickListener(this);
	        
			title_info.setText("Found Your Book:"+"\n"+title+"\n");
			author_info.setText("By: "+ author+"\n\n"+"Tell Us More about your book:"+"\n");

			my_price=(EditText)findViewById(R.id.post_scan_price);
			my_subject=(EditText)findViewById(R.id.post_subject);
			my_coursenum=(EditText)findViewById(R.id.post_number);
			my_comment=(EditText)findViewById(R.id.post_comment);
			condition_spinner=(Spinner)findViewById(R.id.post_scan_spinner);
			condition_adapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, conditions);
			condition_spinner.setAdapter(condition_adapter);
			
			Bundle dummie = new Bundle();
			this.setupDrawer(dummie);
		} else if (resultCode == RESULT_OK && requestCode == 2) {
			//die
		}
	}
	/**
     * Diplaying fragment view for selected nav drawer list item
     * */

	

}
