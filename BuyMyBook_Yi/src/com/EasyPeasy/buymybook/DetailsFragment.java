package com.EasyPeasy.buymybook;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.text.method.KeyListener;
import android.util.Log;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Telephony;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.content.Context;
/*
 * xml file: fragment_results_search.xml
 */
public class DetailsFragment extends Fragment {
	private final String tag = "DetailsFragment";
	//private OnItemSelectedListener listener;
	private EditText editPhoneNumber;
	private EditText editTextNumber;
	private EditText editEmailAddress;
	
	private KeyListener originalPhoneKeyListener;
	private KeyListener originalTextKeyListener;
	private KeyListener originalEmailKeyListener;
	
	private ImageView callme;
	private ImageView textme;
	private ImageView emailme;
	
	private TextView titleText;
	private TextView authorText;
	private TextView priceText;
	private TextView conditionText;
	private TextView commentsText;
	private TextView nameText;
	
	public DetailsFragment(){
		;
	}
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		
	}
	
	public String getCondition(int cond){
		String condition;
		 switch(cond){
     	case 0:
     		condition = new String("Decomposed");
     		break;
     	case 1:
     		condition = new String("Poor");
     		break;
     	case 2:
     		condition = new String("Fair");
     		break;
     	case 3:
     		condition = new String("Good");
     		break;
     	case 4:
     		condition = new String("Very Good");
     		break;
     	case 5:
     		condition = new String("As New");
     		break;
     	default:
     		condition = new String("N/A");
     		break;
     		
     }//switch
		 
		 return condition;
	}
	
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState){
		View view = inflater.inflate(R.layout.fragment_results_details,container,false);
		
		editTextNumber = (EditText)view.findViewById(R.id.message);
		editEmailAddress = (EditText)view.findViewById(R.id.email);
		
	
		emailme = (ImageView)view.findViewById(R.id.email_img);
		textme = (ImageView)view.findViewById(R.id.message_img);
		
	
		originalTextKeyListener = editTextNumber.getKeyListener();
		originalEmailKeyListener = editEmailAddress.getKeyListener();
		
		//Put name, join date, phone, text, and email
		titleText = (TextView) view.findViewById(R.id.title);
		authorText = (TextView) view.findViewById(R.id.author);
		priceText = (TextView) view.findViewById(R.id.price);
		conditionText = (TextView) view.findViewById(R.id.condition);
		commentsText = (TextView) view.findViewById(R.id.commentsText);
		nameText = (TextView) view.findViewById(R.id.nameText);
	
		TextView txt = (TextView) view.findViewById(R.id.search_manual_title);
		String firstname = getArguments().getString("firstname");
		String lastname = getArguments().getString("lastname");
		String title = getArguments().getString("title");
		String author = getArguments().getString("author");
		String price = getArguments().getString("price");
		String condition = getArguments().getString("condition");
		String comments = getArguments().getString("comments");
		String contactNum = getArguments().getString("contactNum");
		String contactEmail = getArguments().getString("contactEmail");
		condition = getCondition(Integer.parseInt(condition)).toString();
		Log.d(tag,"in detailsfrag: email: "+ contactEmail + " : " + contactNum);
		titleText.setText(title);
		authorText.setText(author);
		priceText.setText(price);
		conditionText.setText(condition);
		if(contactNum != null){
			editTextNumber.setText(contactNum);
		} else{
			editTextNumber.setText("5195460284");
		}
		if(contactEmail != null && !contactEmail.equals("null")){
			editEmailAddress.setText(contactEmail);
		} else {
			editEmailAddress.setText("cecabusa@uwaterloo.ca");
		}
		if(firstname != null && !firstname.equals("null") && lastname != null && !lastname.equals("null")){
			String name = firstname + " " + lastname;
			nameText.setText(name);
		}
		else{
			nameText.setText("CoolGuy Greg");
		}
		
		if(comments != null){
			commentsText.setText(comments);
		}
		
		/*String result = new String(title.toString()+author.toString()+price.toString()+condition.toString());
		txt.setText(result);
		*/
		
		// Allows the ability to send a text message
		textme.setOnClickListener(new View.OnClickListener() {
			// Need because Build.VERSION if statement uses KitKat specific stuff
			@SuppressLint("NewApi")
			@Override
			public void onClick(View v) {
				String smsText = "Hi there, I saw your posting for " 
						+ titleText.getText().toString() + " by "
						+ authorText.getText().toString() + " that you are selling for $"
						+ priceText.getText().toString() + " on Buy My Book!"
						+ " I'm interested in it";
				String smsNumber = editTextNumber.getText().toString();
				if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) { //At least KitKat
					String defaultSmsPackageName = Telephony.Sms.getDefaultSmsPackage(v.getContext().getApplicationContext());
					Intent sendIntent = new Intent(
						Intent.ACTION_SENDTO, 
						Uri.parse("smsto:" + Uri.encode(smsNumber))
					);
					sendIntent.putExtra("sms_body", smsText);

					// If the user doesn't have a default app that supports SMS
					if (defaultSmsPackageName != null) {
						sendIntent.setPackage(defaultSmsPackageName);
					}
					startActivity(sendIntent);
				} else { // Stuff for pre-KitKat
					Intent sendIntent = new Intent(Intent.ACTION_VIEW);
					sendIntent.setData(Uri.parse("sms:"));
					sendIntent.putExtra("sms_body", smsText);
					sendIntent.putExtra("address", smsNumber);
					startActivity(sendIntent);
				}
			}
		});
		
		// Allows the ability to send an email
		emailme.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				String aEmailList[] = { editEmailAddress.getText().toString()};
						
				final Intent intent = new Intent(android.content.Intent.ACTION_SEND);
			    intent.setType("plain/text");
			    intent.putExtra(android.content.Intent.EXTRA_EMAIL, aEmailList);
			    intent.putExtra(android.content.Intent.EXTRA_SUBJECT, 
			    		"Reply to your " + titleText.getText().toString() + " ad on Buy My Book!");
			    intent.putExtra(android.content.Intent.EXTRA_TEXT, 
			    		"Hi there, I saw your posting for " 
						+ titleText.getText().toString() + " by "
						+ authorText.getText().toString() + " that you are selling for $"
						+ priceText.getText().toString() + " on Buy My Book!"
						+ " I'm interested in it");
			    startActivity(intent);
			}
		});
		
		
		
		
		
		return view;
		
	}
	
	@Override
    public void onAttach(Activity activity) {
      super.onAttach(activity);
      
      
      /*
      if (activity instanceof OnItemSelectedListener) {
        listener = (OnItemSelectedListener) activity;
      } else {
        throw new ClassCastException(activity.toString()
            + " must implemenet MyListFragment.OnItemSelectedListener");
      }
      */
    }
	
	
}
