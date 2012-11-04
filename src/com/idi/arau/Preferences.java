package com.idi.arau;

import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.widget.CheckBox;


public class Preferences extends PreferenceActivity {
	CheckBox box;	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.settings);				
	}
}
