package com.EasyPeasy.buymybook;

import android.content.Intent;
import android.os.Bundle; 
import android.support.v4.app.FragmentActivity;
import android.view.KeyEvent;

public class LoginActivity extends FragmentActivity {
	private LoginFragment loginFragment;
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);

	    if (savedInstanceState == null) {
	        // Add the fragment on initial activity setup
	    	loginFragment = new LoginFragment();
	    	getSupportFragmentManager() // this used to be getSupportFragmentManager
	        .beginTransaction()
	        .add(android.R.id.content, loginFragment)
	        .commit();
	    } else {
	        // Or set the fragment from restored state info
	    	loginFragment = (LoginFragment) getSupportFragmentManager() // this used to be getSupportFragmentManager
	        .findFragmentById(android.R.id.content);
	    }
	    
	    if (getIntent().getBooleanExtra("EXIT", false)) {
            finish();
            return;
        }
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
	    if ((keyCode == KeyEvent.KEYCODE_BACK)) {
	    	Intent intent = new Intent(this, LoginActivity.class);
	    	intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
	    	intent.putExtra("EXIT", true);
	    	moveTaskToBack(true);
	    	startActivity(intent);
	    }
	    return super.onKeyDown(keyCode, event);
	}
}
