package com.example.yamba1;

import android.app.Activity;
import android.app.ListActivity;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.SimpleCursorAdapter;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter.ViewBinder;
import android.widget.TextView;

public class TimelineActivity extends ListActivity implements LoaderCallbacks<Cursor>{
	//TextView textOut;
	static final String TAG = "TimelineActivity";
	static final String[] FROM = {StatusProvider.C_USER, StatusProvider.C_CREATED_AT,StatusProvider.C_TEXT};
	static final int[] TO = {R.id.text_user,R.id.text_created_At,R.id.text_text};
	//ListView list;
	Cursor cursor;
	SimpleCursorAdapter adapter;
	TimelineReceiver receiver;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//setContentView(R.layout.timeline);
		getLoaderManager().initLoader(-1, null, this);
		//list = (ListView) findViewById(R.id.list);
		//textOut = (TextView) findViewById(R.id.text_out);
		//cursor = getContentResolver().query(StatusProvider.CONTENT_URI, null,
			//	null, null, StatusProvider.C_CREATED_AT + " DESC");
		//cursor = ((YambaApp)getApplication()).StatusProvider.query();

		//adapter = new SimpleCursorAdapter(this, R.layout.row, cursor, FROM, TO, 0);
		adapter = new SimpleCursorAdapter(this, R.layout.row, cursor, FROM, TO);
		adapter.setViewBinder(VIEW_BINDER);
		
		setTitle(R.string.timeline);
		setListAdapter(adapter);
		
		/*while(cursor.moveToNext()) {
			String user = cursor.getString(cursor.getColumnIndex(StatusProvider.C_USER));
			String text = cursor.getString(cursor.getColumnIndex(StatusProvider.C_TEXT));
			textOut.append(String.format("\n%s : %s",	user, text));
			
			
		}*/
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		unregisterReceiver(receiver);
	}

	@Override
	protected void onResume() {
		super.onResume();
		if (receiver == null) 
			receiver = new TimelineReceiver();
		registerReceiver(receiver, new IntentFilter(YambaApp.ACTION_NEW_STATUS));
	}

	static final ViewBinder VIEW_BINDER = new ViewBinder() {
		
		@Override
		public boolean setViewValue(View view, Cursor cursor, int columnIndex) {
			if(view.getId() != R.id.text_created_At) return false;
			
			long time = cursor.getLong(cursor.getColumnIndex(StatusProvider.C_CREATED_AT));
			CharSequence realtiveTime = DateUtils.getRelativeTimeSpanString(time);
			
			((TextView)view).setText(realtiveTime);
			
			return true;
		}
	};
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.menu, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		Intent intent = new Intent(this, UpdaterService.class);
		Intent intentRefresh = new Intent(this, RefreshService.class);
		Intent intentPref = new Intent(this, PrefsActivity.class);
		
		switch(item.getItemId()) {
		
		case R.id.item_start_service:
			startService(intent);
			Log.d(TAG, "item.start");
			return true;
		case R.id.item_stop_service:
			stopService(intent);
			Log.d(TAG, "item.stop");
			return true;
		case R.id.item_refresh:
			startService(intentRefresh);
			Log.d(TAG, "item.refresh");
			return true;
		case R.id.item_prefs:
			startActivity(intentPref);
			Log.d(TAG, "item.prefs");
			return true;
		case R.id.item_status:
			startActivity(new Intent(this, StatusActivity.class));
			Log.d(TAG, "item.status");
			return true;
		default:
			return false;
		
		}
	}

	
	class TimelineReceiver extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {
			cursor = getContentResolver().query(StatusProvider.CONTENT_URI, null,
					null, null, StatusProvider.C_CREATED_AT + " DESC");
			//cursor = ((YambaApp)getApplication()).statusData.query();
			adapter.changeCursor(cursor);
			Log.d(TAG, " TimelineReceiver onReceive changeCursor with count : "
					+ intent.getIntExtra("count", 0));
			
		}
		
	}


	@Override
	public Loader<Cursor> onCreateLoader(int id, Bundle args) {
		
		return new CursorLoader(this, StatusProvider.CONTENT_URI, null, null,
				null, StatusProvider.C_CREATED_AT + " DESC");
	}

	@Override
	public void onLoadFinished(Loader<Cursor> arg0, Cursor arg1) {
		
		
	}

	@Override
	public void onLoaderReset(Loader<Cursor> arg0) {
		
		
	}
}
