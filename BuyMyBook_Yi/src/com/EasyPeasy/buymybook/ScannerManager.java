package com.EasyPeasy.buymybook;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

public class ScannerManager extends Activity {
	private String isbn;
	private String format;
	private int status=-2; 
	/*
	 * 0=isbn found
	 * -1=error result
	 * -2=terminated early/not ready
	 */
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		IntentIntegrator scanIntegrator = new IntentIntegrator(this);
		scanIntegrator.initiateScan();
		
		//setContentView(R.layout.activity_scanner_manager);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.scanner_manager, menu);
		return true;
	}

	public void onActivityResult(int requestCode, int resultCode, Intent intent) {
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

			System.out.println("scannerManager: FORMAT: " + scanFormat);
			System.out.println("scannerManager: CONTENT: "+ scanContent);
			
			//set variables - probably don't need this :/ -yi
			isbn=scanContent;
			format=scanFormat;
			status=0;
			
			//sent result back to my mom
			Intent ret = new Intent();
			ret.putExtra("isbn",isbn);
			ret.putExtra("format",format);
			setResult(RESULT_OK, ret); 
			
		} else {
			//invalid scan data or scan canceled
			status=-1;
			Toast toast = Toast.makeText(
					getApplicationContext(), 
					"No scan data received!",
					Toast.LENGTH_SHORT);
			toast.show();
		}
		
		//always autofinish!
		finish();
	}


}
