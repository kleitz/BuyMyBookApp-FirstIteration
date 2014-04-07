package com.EasyPeasy.buymybook;


import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Menu;
import android.widget.SimpleAdapter;

/*
 * Carl - talk to me if you need answers on MyFragmentActivity
 * 	-yi
 */
public class SearchResultsActivity extends MyFragmentActivity {
	final String tag = "SearchResultsActivity";
	SimpleAdapter adapter;
	
	List<Map<String, String>> listings = new ArrayList<Map<String,String>>();
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_search_results);
		Bundle extras = getIntent().getExtras();
		String result = null;
		Log.d(tag,"got bundle");
		if(getIntent().getStringExtra("json") != null){
			result = new String(extras.getString("json"));
		}
		else{
			Log.d(tag,"null pointer");
			//delete later
			result = "dsfsd";
		}
		
		//install drawer menu
		this.setupDrawer(savedInstanceState);
		/*
		 * Launches the result fragment upon creation
		 */
		Fragment fragment = null;
		fragment =  new SearchResultsFragment();
			if (fragment != null) {
				Bundle args = new Bundle();
				args.putString("json", result);
				fragment.setArguments(args);
				getSupportFragmentManager().beginTransaction().add(R.id.frame_container, fragment).addToBackStack(null).commit();
				
			
			}
		
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.search_results, menu);
		return true;
	}

	/*
	 * Added support for fragment
	 */
	@Override
	public void onBackPressed() {
	   //getSupportFragmentManager();
		Log.d(tag,"onbackpressed: "+this.getSupportFragmentManager().getBackStackEntryCount());
		//if our backstack count is 1 then we only have the activity
	   if( this.getSupportFragmentManager().getBackStackEntryCount() > 1 ){
		   Log.d(tag,"popbackstack");
           this.getSupportFragmentManager().popBackStack();
	   }//if
	   else{
		   Log.d(tag,"super backpressed");
		   //pop the activity
		   this.getSupportFragmentManager().popBackStack();
		   //call super method to go back an activity
		   super.onBackPressed();
	   }
	   overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left);
	}

}
