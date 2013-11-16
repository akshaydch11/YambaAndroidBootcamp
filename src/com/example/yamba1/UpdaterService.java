package com.example.yamba1;

import java.util.List;

import winterwell.jtwitter.Twitter;
import winterwell.jtwitter.Twitter.Status;
import winterwell.jtwitter.TwitterException;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

public class UpdaterService extends Service {

	static final String TAG = "UpdateService: ";
	Twitter twitter;
	static final int DELAY = 30;
	boolean running = false;

	@Override
	public void onCreate() {
		super.onCreate();
		//twitter = new Twitter("student", "password");
		//twitter.setAPIRootUrl("http://yamba.marakana.com/api");
		Log.d(TAG, "onCreate");
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		running = true;
 
		new Thread() {
			public void run() {
				while(running) {
					try {
/*						List<Status> timeline = twitter.getPublicTimeline();
						for (Status status : timeline) {
							((YambaApp)getApplication()).statusData.insert(status);
							Log.d(TAG, String.format("%s , %s", status.user,
									status.text));
						} */
						((YambaApp)getApplication()).pullandInsert();
						int delay = ((YambaApp) getApplication()).sharedprefs.getInt("delay", DELAY);
						Thread.sleep(delay * 1000);
					/*} catch (TwitterException e) {
						Log.e(TAG, "Twiiter exception");
						e.printStackTrace();*/
					} catch (InterruptedException e) {
						Log.e(TAG, "UpdaterService Interrupted");
						e.printStackTrace();
					}
				}
			}
		}.start();
		Log.d(TAG, "onStartCommand");
		return super.onStartCommand(intent, flags, startId);
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		running = false;
		Log.d(TAG, "onDestroy");
	}


	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

}
