package com.EasyPeasy.buymybook;

import java.util.ArrayList;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.widget.DrawerLayout;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.EasyPeasy.buymybook.adapter.NavDrawerListAdapter;
import com.EasyPeasy.buymybook.model.NavDrawerItem;
/*
 * THIS IS AN ABSTRACT CLASS!
 * It's used so that we can have drawers with SOME of our fragments
 *  note: we should only use this for fragments that
 *  		require drawers. Most fragments should not
 *  		need a drawer
 *  						-Yi
 */
public class MyFragmentActivity extends FragmentActivity {
	final Context contenxt = this;
	
	//drawer menu items
	
	private DrawerLayout mDrawerLayout;
	private ListView mDrawerList;
	private ActionBarDrawerToggle mDrawerToggle;
			// nav drawer title
	private CharSequence mDrawerTitle;
	    	// used to store app title
	private CharSequence mTitle;
	    	// slide menu items
	private String[] navMenuTitles;
	private TypedArray navMenuIcons;
	 
	private ArrayList<NavDrawerItem> navDrawerItems;
	private NavDrawerListAdapter adapter;
	
	protected boolean dieAfterFinish = true;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_my_fragment);
		//install drawer
		this.setupDrawer(savedInstanceState);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.my, menu);
		return true;
	}
	
	/**
     * When using the ActionBarDrawerToggle, you must call it during
     * onPostCreate() and onConfigurationChanged()...
     */
 
    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }
    
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Pass any configuration change to the drawer toggles
        mDrawerToggle.onConfigurationChanged(newConfig);
    }
    
    @Override
	public void onBackPressed() {
    	if(dieAfterFinish) {
			super.onBackPressed();
		    overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left);
		} else {
			//main page, don't do anything.
		}
	}
    
    protected void setupDrawer(Bundle savedInstanceState) {
		mTitle = mDrawerTitle = getTitle();
		 
        // load slide menu items
        navMenuTitles = getResources().getStringArray(R.array.nav_drawer_items);
        // nav drawer icons from resources
        navMenuIcons = getResources()
                .obtainTypedArray(R.array.nav_drawer_icons);
 
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.list_slidermenu);
 
        navDrawerItems = new ArrayList<NavDrawerItem>();
 
        // adding nav drawer items to array
        // SELL
        navDrawerItems.add(new NavDrawerItem(navMenuTitles[0], navMenuIcons.getResourceId(0, -1)));
        // BUY
        navDrawerItems.add(new NavDrawerItem(navMenuTitles[1], navMenuIcons.getResourceId(1, -1)));
        // PROFILE
        navDrawerItems.add(new NavDrawerItem(navMenuTitles[2], navMenuIcons.getResourceId(2, -1)));
        // LOGOUT
        navDrawerItems.add(new NavDrawerItem(navMenuTitles[3], navMenuIcons.getResourceId(3, -1)));
        // Communities, Will add a counter here  (??)
 
        // Recycle the typed array
        navMenuIcons.recycle();
        // install listeners for drawer
        
        mDrawerList.setOnItemClickListener(new SlideMenuClickListener());
        
        // setting the nav drawer list adapter
        adapter = new NavDrawerListAdapter(getApplicationContext(),
                navDrawerItems);
        mDrawerList.setAdapter(adapter);
 
        // enabling action bar app icon and behaving it as toggle button
        //getActionBar().setDisplayHomeAsUpEnabled(true);
        //getActionBar().setHomeButtonEnabled(true);
 
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
                R.drawable.ic_drawer, //nav menu toggle icon
                R.string.app_name, // nav drawer open - description for accessibility
                R.string.app_name // nav drawer close - description for accessibility
        ){
            public void onDrawerClosed(View view) {
                //getActionBar().setTitle(mTitle);
                // calling onPrepareOptionsMenu() to show action bar icons
                //invalidateOptionsMenu();
            }
 
            public void onDrawerOpened(View drawerView) {
                //getActionBar().setTitle(mDrawerTitle);
                // calling onPrepareOptionsMenu() to hide action bar icons
                //invalidateOptionsMenu();
            }
        };
        mDrawerLayout.setDrawerListener(mDrawerToggle);
 
        if (savedInstanceState == null) {
            // on first time display view for first nav item
            displayView(99);
        }else {
        	//what screen should we display!?!?! D:
        }
	}
    
    /**
     * Diplaying fragment view for selected nav drawer list item
     * */
    private void displayView(int position) {
    	
    	//launching new activity
    	mDrawerLayout.closeDrawers();
    	Intent intent;
        // update the main content by replacing fragments
        switch (position) {
        case 0: //SELL
        	intent = new Intent(this, PostActivity.class);
			startActivity(intent);
			overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left);
			
	        //decide how to finish the activity
			if(dieAfterFinish) {
				finish();
			}
            break;
        case 1: //SEARCH
        	//	CARL ADD SEARCH HERE!!!!
        	intent = new Intent(this, ManualSearchActivity.class);
			startActivity(intent);
			overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left);
			
	        //decide how to finish the activity
			if(dieAfterFinish) {
				finish();
			}
            break;
        case 2: //MY PROFILE
        	intent = new Intent(this, ProfileActivity.class);
        	startActivity(intent);
        	overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left);
     
        	this.finishActivity();
            break;
        case 3: // LOGOUT
    		Toast toast = Toast.makeText(
    			getApplicationContext(),
    			"Toasty!",
    			Toast.LENGTH_SHORT);
    		toast.show();
        	break;
        default:
            break;
        }
    }

    //helper to decide how to finish - helper for displayView()
    private void finishActivity() {
    	if(dieAfterFinish) {
            	finish();
            }
    }
    
    /**
	 * Slide menu item click listener
	 * */
	private class SlideMenuClickListener implements
			ListView.OnItemClickListener {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			// display view for selected nav drawer item
			displayView(position);
		}
	}


}
