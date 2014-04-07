package com.EasyPeasy.buymybook;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.EasyPeasy.buymybook.CommunicationClass.FacebookStuff;
import com.facebook.*;
import com.facebook.model.*;
import com.facebook.widget.LoginButton;
import com.facebook.widget.ProfilePictureView;

public class LoginFragment extends Fragment {
	
	private static final String TAG = "LoginFragment";
	private UiLifecycleHelper uiHelper;

    boolean phoneNumCorrect = false;
    boolean textNumCorrect = false;
    boolean newEmailCorrect = false;
    boolean newUser = false;
    View view = null;
    
    String fbFirstName = null;
    String fbLastName = null;
    String fbEmail = null;
    String fbPhoneNum = null;
    String fbTextNum = null;
    String fbUserId = null;
    String fbUserName = null;
    String fbAccessToken = null;
    
    final String PREFS_NAME = "MyLoginInfo";
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    uiHelper = new UiLifecycleHelper(getActivity(), callback);
	    uiHelper.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, 
	        ViewGroup container, 
	        Bundle savedInstanceState
	) {
	    view = inflater.inflate(R.layout.activity_login, container, false);
	    
	    LoginButton authButton = (LoginButton) view.findViewById(R.id.authButton);
	    authButton.setFragment(this);
	    // We would like the user's basic info (name, user id, picture) and their email address
	    /*
	     * "basic_info" is implied with every request. It's best practice to include it as per Facebook SDK documentation
	     * "basic_info" can retrieve: id, name, first_name, last_name, link, user name, gender, locale, age_range and other public info
	     * "email" retrieves the user's primary email address.
	     * "publish_actions" lets the app post content/comment/likes to a user's stream (might be useful feature in future)
	     */
	    authButton.setReadPermissions(Arrays.asList("basic_info", "email"));
	    
	    Button newPage = (Button) view.findViewById(R.id.goToMain);
	    newPage.setOnClickListener(new View.OnClickListener() {
			
	    	// Go to main page when user clicks on the appropriate button
			@Override
			public void onClick(View v) {
				if (newUser) {
					// Do stuff if you are a new user
				}
				saveLoginInfo();
				
				Intent intent = new Intent(getActivity(), MainActivity.class);
				intent.putExtra("userId", fbUserId);
				intent.putExtra("userName", fbUserName);
				getActivity().startActivity(intent);
			}
		});
	    
	    return view;
	}
	
	private Session.StatusCallback callback = new Session.StatusCallback() {
	    @Override
	    public void call(Session session, SessionState state, Exception exception) {
	        onSessionStateChange(session, state, exception);
	    }
	};
	
	// Does stuff when the user is logged in and when the user is not logged in
	private void onSessionStateChange(Session session, SessionState state, Exception exception) {
        final TextView welcome = (TextView) view.findViewById(R.id.welcome);
        final TextView name = (TextView) view.findViewById(R.id.name);
        final TextView informUser = (TextView) view.findViewById(R.id.informUserForMoreInfo);
        final EditText phoneNum = (EditText) view.findViewById(R.id.phoneNum);
        final EditText textNum = (EditText) view.findViewById(R.id.textNum);
        final EditText newEmail = (EditText) view.findViewById(R.id.newEmail);
        final Button goToMain = (Button) view.findViewById(R.id.goToMain);
        final ProfilePictureView profilePic = (ProfilePictureView) view.findViewById(R.id.profilePicture);
        final Pattern EMAIL_ADDRESS_PATTERN = Pattern.compile(
        	"[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256}" +
            "\\@" +
            "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}" +
            "(" +
            "\\." +
            "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25}" +
            ")+"
        );
        
	    if (state.isOpened()) {
	        Log.i(TAG, "Logged in...");
	        fbAccessToken = session.getAccessToken().toString();
	        Request.newMeRequest(session, new Request.GraphUserCallback() {
      		  // callback after Graph API response with user object
      		  @Override
      		  public void onCompleted(GraphUser user, Response response) {
      		    if (user != null) {
      		    	/*
      		    	 * user ID: user.getId()
      		    	 * First Name: user.getFirstName()
      		    	 * Last Name: user.getLastName()
      		    	 * Email: user.asMap().get("email").toString()
      		    	 * FB URL: user.getLink();
      		    	 * Access Token: session.getAccessToken()
      		    	 */
      		    	//welcome.setText(getResources().getString(R.string.welcome_back));
      			    loadLoginInfo();
      			    Log.i("onCompleted", "user not null");
      		    	fbUserId = user.getId();
      		    	fbUserName = user.getLink();
      		    	fbFirstName = user.getFirstName();
      		    	fbLastName = user.getLastName();
      		    	if (fbEmail == null) {
      		    		fbEmail = user.asMap().get("email").toString();
      		    	}
      		    	name.setText(fbFirstName + " " + fbLastName);
      		    	
      		    	phoneNum.setText(fbPhoneNum);
      		    	textNum.setText(fbTextNum);
      		    	newEmail.setText(fbEmail);
      		    	// Facebook Profile Picture -- http://graph.facebook.com/id/picture
      		    	// For Booker Book, it is http://graph.facebook.com/100008045347915/picture
      		    	profilePic.setProfileId(fbUserId);
      		    } else {
      		    	return;
      		    }
      		  }
      		}).executeAsync();
	        loadLoginInfo();
	        Log.i(TAG, "Still logged in...");
	        name.setVisibility(View.VISIBLE);
	        profilePic.setVisibility(View.VISIBLE);
	        	newUser = false;
		        welcome.setText(getResources().getString(R.string.welcome_new_user));
		        
		        Log.i("PRECHECK", "fbPhoneNum: " + fbPhoneNum );
        		Log.i("PRECHECK", "fbTextNum: " + fbTextNum );
        		Log.i("PRECHECK", "fbEmail: " + fbEmail );

        		phoneNum.setText(fbPhoneNum);
	        	textNum.setText(fbTextNum);
	        	newEmail.setText(fbEmail);
	        	
		        if (fbPhoneNum == null ||
		        		fbTextNum == null ||
		        		fbEmail == null) {
		        	informUser.setText(getResources().getString(R.string.inform_user));
		        	informUser.setVisibility(View.VISIBLE);
		        	
	        		Log.i("phoneNum", "fbPhoneNum: " + fbPhoneNum );
	        		Log.i("textNum", "fbTextNum: " + fbTextNum );
	        		Log.i("email", "fbEmail: " + fbEmail );
		        	if (fbPhoneNum != null) {
		        		Log.i("THE CHECKER", "fbPhoneNum not null");
		        		phoneNumCorrect = true;
		        	}
		        	if (fbTextNum != null) {
		        		Log.i("THE CHECKER", "fbTextNum not null");
		        		textNumCorrect = true;
		        	}
		        	if (fbEmail != null) {
		        		Log.i("THE CHECKER", "fbEmail not null");
		        		newEmailCorrect = true;
		        	}
		        } else { // user has everything we need
		        	//informUser.setVisibility(View.INVISIBLE);
		        	//goToMain.setVisibility(View.VISIBLE);
		        	
		        	Intent intent = new Intent(getActivity(), MainActivity.class);
					intent.putExtra("userId", fbUserId);
					intent.putExtra("userName", fbUserName);
					getActivity().startActivity(intent);
		        }
		        
	        	phoneNum.setVisibility(View.VISIBLE);
	        	textNum.setVisibility(View.VISIBLE);
	        	newEmail.setVisibility(View.VISIBLE); 
	        	//goToMain.setVisibility(View.VISIBLE);		// TESTING PURPOSES
	    } else if (state.isClosed()) {
	        Log.i(TAG, "Logged out...");
	        welcome.setText(getResources().getString(R.string.welcome));
	        name.setVisibility(View.INVISIBLE);
	        informUser.setVisibility(View.INVISIBLE);
	        phoneNum.setVisibility(View.INVISIBLE);
        	textNum.setVisibility(View.INVISIBLE);
        	newEmail.setVisibility(View.INVISIBLE);
	        goToMain.setVisibility(View.INVISIBLE);
	        profilePic.setVisibility(View.INVISIBLE);
	    }
	    
	    phoneNum.setOnEditorActionListener(
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
			    		    getActivity().getApplicationContext(),
				    		"You must enter 10 digits for the phone number",
				    		Toast.LENGTH_SHORT);
				    	toast.show();
				    	phoneNumCorrect = false;
				    	goToMain.setVisibility(View.INVISIBLE);
				    	//informUser.setVisibility(View.VISIBLE);
				    	return false;
			    	} else {
			    		Toast toast = Toast.makeText(
			    			getActivity().getApplicationContext(),
			    			"Thanks for providing a valid phone number",
				    		Toast.LENGTH_SHORT);
				    	toast.show();
				    	phoneNum.clearFocus();
				    	phoneNumCorrect = true;
				    	fbPhoneNum = phoneNum.getText().toString();
				    	
				    	if (phoneNumCorrect && textNumCorrect && newEmailCorrect) {
				    		informUser.setVisibility(View.INVISIBLE);
				    		goToMain.setVisibility(View.VISIBLE);
				    	}
				    	//hideKeyboard(v, 1);
				    	return true;
			    	}
			    } else {
			    	return false; // pass on to other listeners. 
			    }
			}
		});
	    
	    textNum.setOnEditorActionListener(
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
			    		    getActivity().getApplicationContext(),
				    		"You must enter 10 digits for the texting number",
				    		Toast.LENGTH_SHORT);
				    	toast.show();
				    	textNumCorrect = false;
				    	goToMain.setVisibility(View.INVISIBLE);
				    	//informUser.setVisibility(View.VISIBLE);
				    	return false;
			    	} else {
			    		Toast toast = Toast.makeText(
			    			getActivity().getApplicationContext(),
				    		"Thanks for providing a valid texting number",
				    		Toast.LENGTH_SHORT);
				    	toast.show();
				    	textNum.clearFocus();
				    	textNumCorrect = true;
				    	
				    	fbTextNum = textNum.getText().toString();
				    	if (phoneNumCorrect && textNumCorrect && newEmailCorrect) {
				    		informUser.setVisibility(View.INVISIBLE);
				    		goToMain.setVisibility(View.VISIBLE);
				    	}
				    	//hideKeyboard(v, 2);
				    	return true;
			    	}
			    } else {
			    	return false; // pass on to other listeners. 
			    }
			}
		});
	    
	    newEmail.setOnEditorActionListener(
			    new EditText.OnEditorActionListener() {
			@Override
			public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
				if (actionId == EditorInfo.IME_ACTION_SEND
			            || actionId == EditorInfo.IME_ACTION_DONE
			            || actionId == EditorInfo.IME_ACTION_GO
			            || event.getKeyCode() == KeyEvent.KEYCODE_ENTER) {
			    	
			    	// Check if user entered 
			    	if (! EMAIL_ADDRESS_PATTERN.matcher(newEmail.getText()).matches()) {
			    		Toast toast = Toast.makeText(
			    		    getActivity().getApplicationContext(),
				    		"You must enter a valid email address",
				    		Toast.LENGTH_SHORT);
				    	toast.show();
				    	newEmailCorrect = false;
				    	goToMain.setVisibility(View.INVISIBLE);
				    	//informUser.setVisibility(View.VISIBLE);
				    	return false;
			    	} else {
			    		Toast toast = Toast.makeText(
			    			getActivity().getApplicationContext(),
			    			"Thanks for providing a valid email address",
				    		Toast.LENGTH_SHORT);
				    	toast.show();
				    	newEmail.clearFocus();
				    	newEmailCorrect = true;
				    	fbEmail = newEmail.getText().toString();
				    	if (phoneNumCorrect && textNumCorrect && newEmailCorrect) {
				    		informUser.setVisibility(View.INVISIBLE);
				    		goToMain.setVisibility(View.VISIBLE);
				    	}
				    	//hideKeyboard(v, 2);
				    	return true;
			    	}
			    } else {
			    	return false; // pass on to other listeners. 
			    }
			}
		});
	}
	
	@Override
	public void onResume() {
	    super.onResume();
	    
	    // For scenarios where the main activity is launched and user
	    // session is not null, the session state change notification
	    // may not be triggered. Trigger it if it's open/closed.
	    Session session = Session.getActiveSession();
	    if (session != null &&
	           (session.isOpened() || session.isClosed()) ) {
	        onSessionStateChange(session, session.getState(), null);
	    }
	    uiHelper.onResume();
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
	    super.onActivityResult(requestCode, resultCode, data);
	    uiHelper.onActivityResult(requestCode, resultCode, data);
	}

	@Override
	public void onPause() {
	    super.onPause();
	    uiHelper.onPause();
	}

	@Override
	public void onDestroy() {
	    super.onDestroy();
	    uiHelper.onDestroy();
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
	    super.onSaveInstanceState(outState);
	    uiHelper.onSaveInstanceState(outState);
	}
	
	public void loadLoginInfo() {
		SharedPreferences settings = this.getActivity().getSharedPreferences(PREFS_NAME, 0);
		
		//this.getActivity().getApplicationContext().getSharedPreferences(PREFS_NAME, 0).edit().clear().commit();
				
		fbUserId = settings.getString("fbUserId", null);
		Log.i("loadLoginInfo", "fbUserId: " + fbUserId);
		fbUserName = settings.getString("fbUserName", null);
		Log.i("loadLoginInfo", "fbUserName: " + fbUserName);
		fbFirstName = settings.getString("fbFirstName", null);
		Log.i("loadLoginInfo", "fbFirstName: " + fbFirstName);
		fbLastName = settings.getString("fbLastName", null);
		Log.i("loadLoginInfo", "fbLastName: " + fbLastName);
		fbEmail = settings.getString("fbEmail", null);
		Log.i("loadLoginInfo", "fbEmail: " + fbEmail);
		fbPhoneNum = settings.getString("fbPhoneNum", null);
		Log.i("loadLoginInfo", "fbPhoneNum: " + fbPhoneNum);
		fbTextNum = settings.getString("fbTextNum", null);
		Log.i("loadLoginInfo", "fbTextNum: " + fbTextNum);
		fbAccessToken = settings.getString("fbAccessToken", null);
		Log.i("loadLoginInfo", "fbAccessToken: " + fbAccessToken);
		
	}
	
	public void saveLoginInfo() {
		SharedPreferences settings = this.getActivity().getSharedPreferences(PREFS_NAME, 0);
		SharedPreferences.Editor editor = settings.edit();
		editor.putString("fbUserId", fbUserId);
		editor.putString("fbUserName", fbUserName);
		editor.putString("fbFirstName", fbFirstName);
		editor.putString("fbLastName", fbLastName);
		editor.putString("fbEmail", fbEmail);
		editor.putString("fbPhoneNum", fbPhoneNum);
		editor.putString("fbTextNum", fbTextNum);
		editor.putString("fbAccessToken", fbAccessToken);
		
		// Commit the edits
		editor.commit();
	}
}