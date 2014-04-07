package com.EasyPeasy.buymybook;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.text.style.ImageSpan;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.EasyPeasy.buymybook.adapter.NavDrawerListAdapter;
import com.EasyPeasy.buymybook.model.NavDrawerItem;
import com.facebook.Session;


public class MainActivity extends Activity implements OnClickListener{
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
    
    	//UI instance variables
	//private ImageButton scanBtn;
	private TextView greeting, instruction;
    protected boolean dieAfterFinish = false;
    
	//local db stuff
	private DBHelper dbHelper;
	
	private String fbUserId;
	private String fbUserName;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		fbUserId = getIntent().getStringExtra("userId");
		fbUserName = getIntent().getStringExtra("userName");
	
		setContentView(R.layout.activity_main);
		
		//install drawer
		this.setupDrawer(savedInstanceState);
		
		//instantiate UI items
		//scanBtn = (ImageButton)findViewById(R.id.scan_button);
		greeting=(TextView)findViewById(R.id.greeting);
		instruction=(TextView)findViewById(R.id.instruction);
		
		//scanBtn.setOnClickListener(this);
		//scanBtn.setBackgroundResource(R.drawable.scan_button);
		//set up greeting message (static right now)
		this.setupGreeting();
		//set up instructions
		this.setupInstruction();
		//set up local db
		dbHelper = new DBHelper(this);
		
		if(false){
			dbHelper.clearAll();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	@Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // toggle nav drawer on selecting action bar app icon/title
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        // Handle action bar actions click
        switch (item.getItemId()) {
        case R.id.action_settings:
            return true;
        default:
            return super.onOptionsItemSelected(item);
        }
    }
	 
    /***
     * Called when invalidateOptionsMenu() is triggered
     */
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        // if nav drawer is opened, hide the action items
        boolean drawerOpen = mDrawerLayout.isDrawerOpen(mDrawerList);
        menu.findItem(R.id.action_settings).setVisible(!drawerOpen);
        return super.onPrepareOptionsMenu(menu);
    }
 
    @Override
    public void setTitle(CharSequence title) {
        mTitle = title;
        //getActionBar().setTitle(mTitle);
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
	public void onClick(View v) { // DON'T CHANGE THIS -YI
		/*
		switch (v.getId()) {
		
		case R.id.scan_button:
			System.out.println("i should scan something....");
			scanBtn.setBackgroundResource(R.drawable.scan_button_contact);
			
			Intent intent = new Intent(this, PostActivity.class);
			startActivity(intent);
			overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left);

			break;
		}
		*/
	}
	@Override
	public void onResume() {
		super.onResume();
		//scanBtn.setBackgroundResource(R.drawable.scan_button);
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
	
	
	
	
	/// helpers for set up
	
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
     * Displaying fragment view for selected nav drawer list item
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
    		intent.putExtra("userId", fbUserId);
    		intent.putExtra("userName", fbUserName);
        	startActivity(intent);
        	overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left);
        	
        	// decide how to finish the activity
        	if (dieAfterFinish) {
        		finish();
        	}
            break;
        case 3: // LOGOUT
    		callFacebookLogout(getApplicationContext());
    		finish();
    		intent = new Intent(this, LoginActivity.class);
    		startActivity(intent);
    		overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left);
    		
    		if (dieAfterFinish) {
        		finish();
        	}
    		
        	break;
        default:
            break;
        }
    }
    
    public static void callFacebookLogout(Context context) {
    	Session session = Session.getActiveSession();
        if (session != null) {
            if (!session.isClosed()) {
                session.closeAndClearTokenInformation();
                //clear your preferences if saved
            }
        } else {
        	session = new Session(context);
            Session.setActiveSession(session);
            session.closeAndClearTokenInformation();
            //clear your preferences if saved
        }
    }
	
	private void setupGreeting() {
		
		Drawable booker = this.getResources().getDrawable(R.drawable.ic_booker_default);
		booker.setBounds(0, 0, booker.getIntrinsicWidth(), booker.getIntrinsicHeight()); 
		ImageSpan b = new ImageSpan(booker);
		
		SpannableStringBuilder builder = new SpannableStringBuilder();
		builder.append(" ");
		builder.append(this.getText(R.string.main_greeting));
		builder.setSpan(b, 0, 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		greeting.setText(builder);
	}
	private void setupInstruction() {
		SpannableStringBuilder builder = new SpannableStringBuilder();
		
		Drawable camera = this.getResources().getDrawable(R.drawable.ic_action_camera);
		camera.setBounds(0, 0, camera.getIntrinsicWidth(), camera.getIntrinsicHeight()); // <---- Very important otherwise your image won't appear
		ImageSpan c = new ImageSpan(camera);
		Drawable swipe = this.getResources().getDrawable(R.drawable.ic_swipe_menu);
		swipe.setBounds(0, 0, swipe.getIntrinsicWidth(), swipe.getIntrinsicHeight());
		ImageSpan s = new ImageSpan(swipe);
		Drawable look = this.getResources().getDrawable(R.drawable.ic_action_search);
		look.setBounds(0, 0, look.getIntrinsicWidth(), look.getIntrinsicHeight());
		ImageSpan l = new ImageSpan(look);
		
		builder.append(" ");
		builder.append(this.getText(R.string.main_inst_sell));
		int lengthOfPart1 = builder.length();
		builder.append(" ");
		builder.append(this.getText(R.string.main_inst_swipe));
		int lengthOfPart2 = builder.length();
		builder.append(" ");
		builder.append(this.getText(R.string.main_inst_look));
		
		builder.setSpan(c, 0, 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		builder.setSpan(s,  lengthOfPart1,  lengthOfPart1+1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		builder.setSpan(l, lengthOfPart2, lengthOfPart2+1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		
		builder.setSpan(new ForegroundColorSpan(Color.parseColor("#16a085")), 4, 8, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		builder.setSpan(new ForegroundColorSpan(Color.parseColor("#e67e22")), 18, 23, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		builder.setSpan(new ForegroundColorSpan(Color.parseColor("#8e44ad")), 49, 53, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		
		instruction.setText(builder);
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
