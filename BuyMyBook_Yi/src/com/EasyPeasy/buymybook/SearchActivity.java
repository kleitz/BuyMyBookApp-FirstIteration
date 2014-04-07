package com.EasyPeasy.buymybook;


import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;

/*
 * flow of this activity:
 * 1. launch with welcome message - saying "get ready to scan your book"
 * 2. launch scannerManager - scan and we'll get (isbn,format)
 * 3. query our db for book info - not done
 * 4. display result - either error, or result screen(top=info from server, bottom = questions)
 * 		questions = how much do you want to sell it for, condition, take a picture
 * 5. user press sell, load in to server(not done) and local db(not done).
 */
public class SearchActivity extends MainActivity implements OnClickListener{
	final Context context = this;

    //UI elements
	
	@SuppressLint("NewApi")
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.dieAfterFinish=true;
		//System.out.println("started onCreate for PostActivity");
		
		
		
		//call scanner here
		Intent intent = new Intent(this, ScannerManager.class);
		//startActivity(intent);
		startActivityForResult(intent, 1);
		
		//set up UI
		setContentView(R.layout.activity_post);
		this.setupDrawer(savedInstanceState);
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
		getMenuInflater().inflate(R.menu.search, menu);
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
	public void onClick(View v) {
		// TODO Auto-generated method stub
		
	}
	
	/**
     * Diplaying fragment view for selected nav drawer list item
     * */

	

}
