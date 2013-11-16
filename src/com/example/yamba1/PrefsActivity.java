package com.example.yamba1;

import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.util.Log;

public class PrefsActivity extends PreferenceActivity{

	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.d("PrefsAct", "onCreate1");
		addPreferencesFromResource(R.xml.prefs);
		Log.d("PrefsAct", "onCreate");
	}

}
