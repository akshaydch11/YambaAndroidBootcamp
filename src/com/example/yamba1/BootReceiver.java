package com.example.yamba1;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.preference.PreferenceManager;
import android.util.Log;

public class BootReceiver extends BroadcastReceiver{

	@Override
	public void onReceive(Context context, Intent arg1) {

		long interval = Long.parseLong(
				PreferenceManager.getDefaultSharedPreferences(context).getString("delay", "900000"));
		
		PendingIntent operation = PendingIntent.getService(context, -1, 
				new Intent(YambaApp.ACTION_REFRESH),PendingIntent.FLAG_UPDATE_CURRENT);
		
		AlarmManager alarmManager = (AlarmManager) context
				.getSystemService(Context.ALARM_SERVICE);

		 alarmManager.cancel(operation);
		if(interval > 0) {
		alarmManager.setInexactRepeating(AlarmManager.RTC,
				System.currentTimeMillis(), interval, operation);
		}
		// context.startService(new Intent(context, UpdaterService.class));
		Log.d("Bootreceiver", "onReceive delay :" + interval);

	}

}
