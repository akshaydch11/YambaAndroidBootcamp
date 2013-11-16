package com.example.yamba1;

import winterwell.jtwitter.Twitter;
import winterwell.jtwitter.TwitterException;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Debug;
import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class StatusActivity extends Activity implements LocationListener {
	static final String TAG = "StatusActivity";
	static final String PROVIDER = LocationManager.GPS_PROVIDER; 
	EditText editStatus;
	LocationManager locationManager;
	Location location;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		//Debug.startMethodTracing("Yamba.trace");
		setContentView(R.layout.status);

		editStatus = (EditText) findViewById(R.id.edit_status);
		
		locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
		location = locationManager.getLastKnownLocation(PROVIDER);
	}

	@Override
	protected void onPause() {
		super.onPause();
		locationManager.removeUpdates(this);
	}



	@Override
	protected void onResume() {
		super.onResume();
		locationManager.requestLocationUpdates(PROVIDER, 30000, 10000, this);
	}



	@Override
	protected void onStop() {
		super.onStop();
		//Debug.stopMethodTracing();
	}

	public void onClick(View v) {
		String statusText = editStatus.getText().toString();

		new PostToTwitter().execute(statusText);
		Log.d(TAG, "onClicked with Status " + statusText);

	}

	class PostToTwitter extends AsyncTask<String, Void, String> {

		/* new thread created here*/
		@Override
		protected String doInBackground(String... params) {
			try {
				Twitter twitter = new Twitter("student", "password");
				twitter.setAPIRootUrl("http://yamba.marakana.com/api");
				twitter.setStatus(params[0]);
				Log.d(TAG, "Successfully posted : " + params[0]);
				return "Successfully posted: " + params[0];
			} catch (TwitterException e) {
				Log.e(TAG, "Died ", e);
				return "Failed post: " + params[0];
			}
		}
		/* after posting gets message*/
		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			Toast.makeText(StatusActivity.this,
					"Successfully posted: " + result, Toast.LENGTH_SHORT)
					.show();

		}
	}

	@Override
	public void onLocationChanged(Location l) {
		location = l;
		Log.d(TAG, "onLocationChanged: " + location.toString());
		
	}

	@Override
	public void onProviderDisabled(String arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onProviderEnabled(String arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onStatusChanged(String arg0, int arg1, Bundle arg2) {
		// TODO Auto-generated method stub
		
	}

	
	



}
