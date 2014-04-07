package com.EasyPeasy.buymybook;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Telephony;
import android.support.v4.app.NavUtils;
import android.text.method.KeyListener;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.widget.ProfilePictureView;

public class ProfileActivity extends MainActivity {
	final Context context = this;
	final String tag = "ProfileActivity";
	
	private EditText editPhoneNumber;
	private EditText editTextNumber;
	private EditText editEmailAddress;
	
	private TextView firstName;
	private TextView lastName;
	
	private KeyListener originalPhoneKeyListener;
	private KeyListener originalTextKeyListener;
	private KeyListener originalEmailKeyListener;
	
	private String userFirstName;
	private String userLastName;
	private String userEmail;
	private String userPhoneNum;
	private String userTextNum;
	private String fbUserId;
	private String fbUserName;
	
	//db stuff
	private DBHelper dbHelper;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_profile);
		this.dieAfterFinish=true;
		
		loadUserInfo();
		
		editPhoneNumber = (EditText)findViewById(R.id.phone);
		editTextNumber = (EditText)findViewById(R.id.message);
		editEmailAddress = (EditText)findViewById(R.id.email);
		
		firstName = (TextView)findViewById(R.id.first_name);
		lastName = (TextView)findViewById(R.id.last_name);
		
		originalPhoneKeyListener = editPhoneNumber.getKeyListener();
		originalTextKeyListener = editTextNumber.getKeyListener();
		originalEmailKeyListener = editEmailAddress.getKeyListener();
		
		//Put name, join date, phone, text, and email
		((TextView)findViewById(R.id.first_name)).setText(userFirstName);
		((TextView)findViewById(R.id.last_name)).setText(userLastName);
		editPhoneNumber.setText(userPhoneNum);
		editTextNumber.setText(userTextNum);
		editEmailAddress.setText(userEmail);
		((TextView)findViewById(R.id.books_selling_text)).setText("Books that you are selling");
		
		((ProfilePictureView)findViewById(R.id.profile_pic)).setProfileId(fbUserId);
		
		dbHelper = new DBHelper(this);
		
		ArrayList<SearchListItem> myArrayOfBooks = new ArrayList<SearchListItem>();
		
		//gets data from local db
		Cursor c = null;
		
		try {
			c = dbHelper.cursorSelectAll();
		} catch (Exception e) {
			// Exception
		}
		
		if (c != null && c.getCount() > 0) {
			c.moveToFirst();
			
			while (!c.isAfterLast()) {
				String listing_id=c.getString(0); //listing ID
				String title=c.getString(1); //title
				String author=c.getString(2);
				String price=c.getString(3);
				String condition=c.getString(4);
				
				SearchListItem item = new SearchListItem(
						title,
						author,
						price,
						condition);
				System.out.println("in ProfileActiving loading book "+title);
				myArrayOfBooks.add(item);
				c.moveToNext();
			}
		}
		
		editPhoneNumber.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				editPhoneNumber.setKeyListener(originalPhoneKeyListener);
			}
		});
		
		editPhoneNumber.setOnEditorActionListener(
			    new EditText.OnEditorActionListener() {
			@Override
			public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
				if (actionId == EditorInfo.IME_ACTION_SEND
			            || actionId == EditorInfo.IME_ACTION_DONE
			            || actionId == EditorInfo.IME_ACTION_GO
			            || event.getKeyCode() == KeyEvent.KEYCODE_ENTER) {
			    	
			    	// Check if user entered 
			    	if (v.length() != 10) {
			    		Toast toast = Toast.makeText(
				    		getApplicationContext(),
				    		"You must enter 10 digits for the phone number",
				    		Toast.LENGTH_SHORT);
				    	toast.show();
				    	return false;
			    	} else {
			    		Toast toast = Toast.makeText(
				    		getApplicationContext(),
				    		"Phone number has been updated",
				    		Toast.LENGTH_SHORT);
				    	toast.show();
				    	editPhoneNumber.clearFocus();
				    	hideKeyboard(v);
				    	return true;
			    	}
			    } else {
			    	return false; // pass on to other listeners. 
			    }
			}
		});
		
		editTextNumber.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				editTextNumber.setKeyListener(originalTextKeyListener);
			}
		});
		
		editTextNumber.setOnEditorActionListener(
			    new EditText.OnEditorActionListener() {
			@Override
			public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
				if (actionId == EditorInfo.IME_ACTION_SEND
			            || actionId == EditorInfo.IME_ACTION_DONE
			            || actionId == EditorInfo.IME_ACTION_GO
			            || event.getKeyCode() == KeyEvent.KEYCODE_ENTER) {
			    	
			    	// Check if user entered 
			    	if (v.length() != 10) {
			    		Toast toast = Toast.makeText(
				    		getApplicationContext(),
				    		"You must enter 10 digits for the texting number",
				    		Toast.LENGTH_SHORT);
				    	toast.show();
				    	return false;
			    	} else {
			    		Toast toast = Toast.makeText(
				    		getApplicationContext(),
				    		"Texting number has been updated",
				    		Toast.LENGTH_SHORT);
				    	toast.show();
				    	editTextNumber.clearFocus();
				    	hideKeyboard(v);
				    	return true;
			    	}
			    } else {
			    	return false; // pass on to other listeners. 
			    }
			}
		});
	
		editEmailAddress.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				editEmailAddress.setKeyListener(originalEmailKeyListener);
			}
		});
	
		editEmailAddress.setOnEditorActionListener(
			    new EditText.OnEditorActionListener() {
			@Override
			public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
				if (actionId == EditorInfo.IME_ACTION_SEND
			            || actionId == EditorInfo.IME_ACTION_DONE
			            || actionId == EditorInfo.IME_ACTION_GO
			            || event.getKeyCode() == KeyEvent.KEYCODE_ENTER) {
			    	Toast toast = Toast.makeText(
				   		getApplicationContext(),
				    	"Email address has been updated",
				    	Toast.LENGTH_SHORT);
				   	toast.show();
				   	editEmailAddress.clearFocus();
				   	hideKeyboard(v);
				   	return true;
			    } else {
			    	return false; // pass on to other listeners. 
			    }
			}
		});
	
		Log.i("profileActivity", "time for drawer setup");
		//install drawer
		//this.setupDrawer(savedInstanceState);
		Log.i("profileActivity", "Drawer setup worked");
		
		final ListView lv1 = (ListView) findViewById(R.id.my_books);
		if(lv1 != null){
			lv1.setAdapter(new CustomPeronalListingAdapter(this, myArrayOfBooks));
			lv1.setItemsCanFocus(true);
			lv1.setFocusable(false);
			lv1.setFocusableInTouchMode(false);
			lv1.setClickable(true);
			lv1.setOnItemClickListener(new OnItemClickListener() {
		    /*
		     * Click listener for each item view
		     * @see android.widget.AdapterView.OnItemClickListener#onItemClick(android.widget.AdapterView, android.view.View, int, long)
		     */
		    @Override
		    public void onItemClick(AdapterView<?> a, View v, int position, long id) {
		    	Object o = lv1.getItemAtPosition(position);
		        SearchListItem newsData = (SearchListItem) o;
		        Toast nre = Toast.makeText(
		        	getApplicationContext(), 
		            "selected :" + " " + newsData.getTitle(), 
		            Toast.LENGTH_SHORT);
		        nre.show();
		    }

		});
		   }
		   else{
			   Log.d(tag,"searchresultfragment, adapter null");
		   }	       
	} // onCreate end
	
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
		getMenuInflater().inflate(R.menu.profile, menu);
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
	
	// When user presses the profile pic, it will go to the Facebook profile
    public void goToFacebook(View view) {
    	try { // Try launching through Facebook app first
    	    context.getPackageManager().getPackageInfo("com.facebook.katana", 0);
    	    String URI = "fb://profile/" + fbUserId;
    	    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(URI));
    	    startActivity(intent);
    	} catch (Exception e) { // Fallback to web browser if Facebook app doesn't exist or failed to launch
    		String URI = "https://www.facebook.com/" + fbUserName;
    		Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(URI));
    	   	startActivity(intent);
    	}
    }
    
	private void hideKeyboard(View view) {
	    InputMethodManager manager = (InputMethodManager) view.getContext()
	            .getSystemService(INPUT_METHOD_SERVICE);
	    if (manager != null)
	        manager.hideSoftInputFromWindow(view.getWindowToken(), 0);
	}

	public void loadUserInfo() {
		SharedPreferences settings = getSharedPreferences("MyLoginInfo", 0);
		fbUserId = settings.getString("fbUserId", null);
		Log.i("loadLoginInfo", "fbUserId: " + fbUserId);
		fbUserName = settings.getString("fbUserName", null);
		Log.i("loadLoginInfo", "fbUserName: " + fbUserName);
		userFirstName = settings.getString("fbFirstName", null);
		Log.i("loadLoginInfo", "fbFirstName: " + firstName);
		userLastName = settings.getString("fbLastName", null);
		Log.i("loadLoginInfo", "fbLastName: " + lastName);
		userEmail = settings.getString("fbEmail", null);
		Log.i("loadLoginInfo", "fbEmail: " + userEmail);
		userPhoneNum = settings.getString("fbPhoneNum", null);
		Log.i("loadLoginInfo", "fbPhoneNum: " + userPhoneNum);
		userTextNum = settings.getString("fbTextNum", null);
		Log.i("loadLoginInfo", "fbTextNum: " + userTextNum);
	}
}