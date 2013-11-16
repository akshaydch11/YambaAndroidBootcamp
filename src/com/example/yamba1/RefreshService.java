package com.example.yamba1;

import java.util.List;

import winterwell.jtwitter.Twitter;
import winterwell.jtwitter.Twitter.Status;
import winterwell.jtwitter.TwitterException;
import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

public class RefreshService extends IntentService{
	static final String TAG = "RefreshService";
	//Twitter twitter;
	public RefreshService() {
		super(TAG);

	}

	@Override
	public void onCreate() {
		super.onCreate();
		//twitter = new Twitter("student", "password");
		//twitter.setAPIRootUrl("http://yamba.marakana.com/api");
		Log.d(TAG, "onCreate");
	}

	
	@Override
	protected void onHandleIntent(Intent intent) {
/*		StatusData statusData = ((YambaApp)getApplication()).statusData;
		try {
			List<Status> timeline = ((YambaApp)getApplication()).getTwitter().getPublicTimeline();

			for (Status status : timeline) {
				statusData.insert(status);
				Log.d(TAG, String.format("%s , %s", status.user,
						status.text));
			}
		} catch (TwitterException e) {
			e.printStackTrace();
		}*/
		((YambaApp)getApplication()).pullandInsert();
		
		Log.d(TAG, "onHandleIntent");
		
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		Log.d(TAG, "onDestroy");
	}

}
