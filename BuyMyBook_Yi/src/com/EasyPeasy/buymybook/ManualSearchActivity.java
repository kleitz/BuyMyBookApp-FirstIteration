package com.EasyPeasy.buymybook;

import java.util.concurrent.ExecutionException;

import com.EasyPeasy.buymybook.CommunicationClass.DownloadJSON;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.text.Editable;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
//this file is for inputting stuff
//xml file: activity_manual_Search.xml

public class ManualSearchActivity extends MainActivity {
	final Context context = this;
	String TAG = new String("ManualSearchActivity");
	private EditText textTitle,textAuthor,textISBN, textCourseNum;
	/*
	 * Spinner stuff
	 */
	Spinner courseCode,courseNum,term;
	ArrayAdapter<String> spinnerCourseAdapter,termAdapter;
	String[] subjects = new String[]{"ACC","ACTSC","AFM","AHS","AMATH","ANTH","APPLS","ARBUS",
			"ARCH","ARTS","BE","BET","BIOL","BUS","CHE","CHEM","CHINA","CIVE","CLAS","CM",
			"CMW","CO","COGSCI","COMM","CROAT","CS","CT","DAC","DEI","DRAMA","DUTCH",
			"EARTH","EASIA","ECE","ECON","ENBUS","ENGL","ENVE","ENVS","ERS","ESL","FINE",
			"FR","GBDA","GEMCC","GENE","GEOE","GEOG","GER","GERON","GGOV","GRK","HIST",
			"HLTH","HRM","HSG","HUMSC","IAIN","INDEV","INTEG","INTST","ISS","ITAL",
			"ITALST","JAPAN","JS","KIN","KOREA","LAT","LED","LS","MATBUS","MATH","MCT",
			"ME","MEDVL","MNS","MSCI","MTE","MTHEL","MUSIC","NANO","NE","OPTOM","PACS",
			"PHARM","PHIL","PHS","PHYS","PLAN","PMATH","PORT","PS","PSCI","PSYCH","REC",
			"REES","RS","RUSS","SCBUS","SCI","SDS","SE","SI","SMF","SOC","SOCWK","SPAN",
			"SPCOM","SPD","STAT","STV","SUSM","SWK","SWREN","SYDE","TOUR","TS","UNIV",
			"VCULT","WS"};
	String[] terms = new String[]{"1141","1145","1149"};
	
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_manual_search);
		
		//set drawer menu - from yi
		this.dieAfterFinish=true;
		this.setupDrawer(savedInstanceState);
		
		textTitle = (EditText)findViewById(R.id.search_manual_booktitle);
		textAuthor = (EditText)findViewById(R.id.search_manual_bookauthor);
		textISBN = (EditText)findViewById(R.id.search_manual_bookisbn);
		
		courseCode = (Spinner)findViewById(R.id.spinnerCourse);
		textCourseNum = (EditText)findViewById(R.id.textCourseNum);
		term = (Spinner)findViewById(R.id.spinnerTerm);
		
		spinnerCourseAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,subjects);
		termAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,terms);
		courseCode.setAdapter(spinnerCourseAdapter);
		term.setAdapter(termAdapter);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	public void test(View view){
		
	}
	
	public void submitResults(View view){
		String title = textTitle.getText().toString();
		String author = textAuthor.getText().toString();
		// Get rid of hyphens in ISBN
		String isbn = textISBN.getText().toString().replace("-","");
		
		String chosenSubject = courseCode.getSelectedItem().toString();
		String chosenSubjectNum = textCourseNum.getText().toString();
		/*Log.d(TAG,"chosenSUbject:" +chosenSubject);
		Intent intent = new Intent(this,SearchManualActivity.class);
		intent.putExtra("SUBMITVAL_TITLE", title);
		intent.putExtra("SUBMITVAL_AUTHOR", author);
		intent.putExtra("SUBMITVAL_ISBN", isbn);*/
		//Intent resultsIntent = new Intent(this,SearchResultsActivity.class);
		//String scanContent = new String("9787887031990"); //fake it
		//String scanContent = new String("9780176251949");
		//String url="http://buymybookapp.com/api/search/get_book/"+scanContent;
		//String scanContent = new String("9780176251949");
		String url = null;
		if(chosenSubject != null && chosenSubjectNum != null && chosenSubjectNum.length() == 3 ){
			Log.d(TAG,"chose subject: "+ chosenSubject +":"+chosenSubjectNum + " len: "+chosenSubjectNum.length());
			url ="http://buymybookapp.com/api/search/get_listings_by_course/"+chosenSubject+"/"+chosenSubjectNum;
		}
		else if(isbn != null && isbn.length()>0){
			Log.d(TAG,"chose isbn"+isbn);
			url="http://buymybookapp.com/api/search/get_listings_by_book/"+isbn;
		}
		else{
			Log.d(TAG,"chose else case");
		}
		//String url="http://buymybookapp.com/api/search/get_listings_by_book/"+isbn;
		//communication class does all the work in getting results
		if(url != null){
			Log.d(TAG,"url to execute: "+ url);
			CommunicationClass c = new CommunicationClass(url);
			String results = null;
		
			c.new DownloadJSON(this,"search").execute(url);
		}
		else{
			Toast.makeText(view.getContext(), "Invalid search params, try CS136", Toast.LENGTH_LONG).show();  
		}
	/*
		if(results != null){
			Log.d(TAG,"results null");
			intent.putExtra("json", results);
		}
		
		
		startActivity(intent);*/
		//This activity should die now -Yi
		if(dieAfterFinish) {
			finish();
		}
	}
	/*
	 * Called by the scan ISBN button to scan an ISBN into the textfield
	 * This function will hopefully provide functionality to populate book information when you scan it 
	 */
	public void scanISBN(View view){
		IntentIntegrator scanIntegrator = new IntentIntegrator(this);
		scanIntegrator.initiateScan();
	}
	
	public void testFragment(View view){
		Log.d(TAG,"testFragment");
		Intent intent = new Intent(this,SearchResultsActivity.class);
		startActivity(intent);
	}
	
	public void onActivityResult(int requestCode, int resultCode, Intent intent){
		//retrieve result of scanning - instantiate ZXing object
		IntentResult scanningResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);
		
		//check we have a valid result
		if(scanningResult != null 
				&& scanningResult.getContents() != null
				&& scanningResult.getFormatName() != null
		) {
			//get content from Intent Result
			String scanContent = scanningResult.getContents();
			//get format name of data scanned
			String scanFormat = scanningResult.getFormatName();
		
			textISBN.setText(scanContent);
		} else {
			//invalid scan data or scan canceled
			Toast toast = Toast.makeText(
					getApplicationContext(), 
					"No scan data received!",
					Toast.LENGTH_SHORT);
			toast.show();
		}
	}

}
