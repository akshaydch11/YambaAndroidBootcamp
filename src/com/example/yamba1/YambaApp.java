package com.example.yamba1;

import java.util.List;

import winterwell.jtwitter.Twitter;
import winterwell.jtwitter.Twitter.Status;
import winterwell.jtwitter.TwitterException;
import android.app.Application;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.preference.PreferenceManager;
import android.util.Log;

public class YambaApp extends Application implements OnSharedPreferenceChangeListener{
	static 	final String TAG = "YambaApp";
	public static final String ACTION_NEW_STATUS = "com.example.yamba.NEW_STATUS";
	public static final String ACTION_REFRESH = "com.example.yamba.RefreshService";
	public static final String ACTION_REFRESH_ALARM = "com.example.yamba.RefreshAlarm";
	Twitter twitter;
	SharedPreferences sharedprefs;


	@Override
	public void onCreate() {
		super.onCreate();

		//twitter 
		//twitter = new Twitter("student", "password");
		//twitter.setAPIRootUrl("http://yamba.marakana.com/api");

		// shared prefs
		sharedprefs = PreferenceManager.getDefaultSharedPreferences(this);
		sharedprefs.registerOnSharedPreferenceChangeListener(this);

		//statusData = new StatusData(this);

		Log.d(TAG, "onCreate");
	}

	public Twitter getTwitter() {
		if(twitter == null){
			String username = sharedprefs.getString("username", "student");
			String password = sharedprefs.getString("password", "password");
			String server = sharedprefs.getString("server", "http://yamba.marakana.com/api");

			twitter = new Twitter(username, password);
			twitter.setAPIRootUrl(server);

		}

		return twitter;
	}

	static final Intent refreshAlarm = new Intent(ACTION_REFRESH_ALARM);
	@Override
	public void onSharedPreferenceChanged(SharedPreferences arg0, String arg1) {
		twitter = null;
		sendBroadcast(refreshAlarm);
		Log.d(TAG, "onSharedPreferenceChanged() for key : " + arg1);

	}
	long lastTimeStampSeen = -1;
	public int pullandInsert() {
		Log.d(TAG, "pullandInsert()");
		int count = 0;
		long biggestTimeStamp = -1;
		try {
			List<Status> timeline = getTwitter().getPublicTimeline();
			for (Status status : timeline) {
				getContentResolver().insert(StatusProvider.CONTENT_URI, 
						StatusProvider.statusToValues(status));
				//statusData.insert(status);
				//if(biggestTimeStamp == -1) biggestTimeStamp = status.createdAt.getTime();
				
				if(status.createdAt.getTime() > lastTimeStampSeen )
				{
					count++;
					biggestTimeStamp = (status.createdAt.getTime() > biggestTimeStamp) 
							? status.createdAt.getTime() :biggestTimeStamp;
					lastTimeStampSeen = status.createdAt.getTime();
					
					Log.d(TAG, String.format("%s , %s count %s", status.user,
							status.text, count));
				}
				//Log.d(TAG, String.format("%s , %s", status.user,
					//	status.text));
			}
		} catch (TwitterException e) {
			Log.e(TAG, "Failed to pull from twitter");
			e.printStackTrace();
		}
		if(count > 0) {
			sendBroadcast(new Intent(ACTION_NEW_STATUS).putExtra("count", count));
		}

		return count;
	}

	
	
}
