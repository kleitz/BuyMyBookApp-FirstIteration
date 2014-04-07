package com.EasyPeasy.buymybook;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;



public class CommunicationClass{
		//verification before request
	private String tag = "CommClass";
	//most commonly used for setting up and starting a progress dialog
	InputStream inputStream = null;
	String result = ""; //result of JSON
	HttpResponse httpResponse2;
	int typeCall; //0 for post, 1 for scan
	private boolean done = false;
	/*
	public CommunicationClass(){ // should make the ctor a nop later - only run on demand
		String uri = new String("http://buymybookapp.com/api/test/test2");
        new DownloadJSON().execute(uri , null, null);
	}
	*/
	public CommunicationClass(){
		
	}
	public CommunicationClass(String url){
		String uri = new String(url);
		
       // new DownloadJSON().execute(uri , null, null);
	}
	public CommunicationClass(String url, int code){
		String uri = new String(url);
		
        //new DownloadJSON().execute(uri , null, null);
	}
	
	public class FacebookStuff extends AsyncTask<String, Void, String> {
		HttpClient client;
		HttpPost post;
		HttpResponse response;
		
		FacebookStuff(HttpPost post) {
			this.client = null;
			this.post = post;
			this.response = null;
		}
		
		// Not sure what this does exactly...
		protected String doInBackground(String... urls) {
            HttpClient client = new DefaultHttpClient();
            String json = "";
            try {
                String line = "";
                HttpGet request = new HttpGet(urls[0]);
                HttpResponse response = client.execute(request);
                BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
                while ((line = rd.readLine()) != null) {
                    json += line + System.getProperty("line.separator");
                }
            } catch (IllegalArgumentException e1) {
                e1.printStackTrace();
            } catch (IOException e2) {
                e2.printStackTrace();
            }
            return json;
        }
		
		public HttpResponse Login() {
			try {
				HttpClient httpclient = new DefaultHttpClient();
				this.response = httpclient.execute(this.post);
			} catch (ClientProtocolException e) {
				// TODO: catch
		    } catch (IOException e) {
		    	// TODO: catch
		    } catch (Exception e) {
		    	// TODO: another exception that I missed
		    }
				return this.response;
		}
	}
	
	public class DownloadJSON extends AsyncTask<String, Void, String> {
		JSONObject jsonObj;
		Context context;
		String typeDownload;
		DownloadJSON() {
		}
		
		DownloadJSON(Context context,String type) {
			this.context= context.getApplicationContext();
			this.typeDownload = type.toString();
			Log.d(tag,"Constructing, type: "+type);
		}
		protected String doInBackground(String... urls) {
            HttpClient client = new DefaultHttpClient();
            String json = "";
            try {
                String line = "";
                HttpGet request = new HttpGet(urls[0]);
                HttpResponse response = client.execute(request);
                BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
                while ((line = rd.readLine()) != null) {
                    json += line + System.getProperty("line.separator");
                }
            } catch (IllegalArgumentException e1) {
                e1.printStackTrace();
            } catch (IOException e2) {
                e2.printStackTrace();
            }
            return json;
        }

        protected void onProgressUpdate(Void... progress) {

        }

        protected void onPostExecute(String result) {
        	Log.d(tag, "ONPOSTEXECUTE");
        	Log.d(tag,"jsonON: "+ result);
        	try {
        		jsonObj = null;
				jsonObj = new JSONObject(result);
				
				if (typeDownload.equals("post")) { // show post confimration activity
					/*Log.d(tag,"if case");
					Intent intent = new Intent(this.context, PostScanConfirmationActivity.class);
					intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					
					intent.putExtra("json", result.toString());
					
					context.startActivity(intent);*/
				} else if(typeDownload.equals("image_get")){
						//Log.d(tag,"image_Get: "+result.toString());
				} else {
					//Log.d(tag,"else case");
					// carl, but your result code here!
					
					Intent intent = new Intent(this.context, SearchResultsActivity.class);
					intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					
					intent.putExtra("json", result.toString());
					//Log.d(tag,"Passing json to SearchResults: "+result.toString());
					context.startActivity(intent);
					
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        	
        	catch(Exception e){
        		Log.d(tag,"Exception: "+e.toString());
        	}
        }//onPostExecute
    }//Download JSON
	
	
	public class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
	    ImageView bmImage;

	    public DownloadImageTask(ImageView bmImage) {
	        this.bmImage = bmImage;
	    }

	    protected Bitmap doInBackground(String... urls) {
	        String urldisplay = urls[0];
	        Bitmap mIcon11 = null;
	        try {
	            InputStream in = new java.net.URL(urldisplay).openStream();
	            mIcon11 = BitmapFactory.decodeStream(in);
	        } catch (Exception e) {
	            Log.e("Error", e.getMessage());
	            e.printStackTrace();
	        }
	        return mIcon11;
	    }

	    protected void onPostExecute(Bitmap result) {
	        bmImage.setImageBitmap(result);
	    }
	}
	
}//CommunicationClass
