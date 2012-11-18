package com.idi.arau;

import android.os.Bundle;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceClickListener;
import android.preference.PreferenceActivity;
import android.widget.SeekBar;


public class Preferences extends PreferenceActivity {
	SeekBar volume;
	Preference dialogPreference;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		//SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(this); 		
		//Log.v("aaaaaaaaaaaaa", "aaaaaaaaaaaaa " + pref.getInt("volume", 50));		
		
		addPreferencesFromResource(R.xml.settings);
		
		dialogPreference = (Preference) getPreferenceScreen().findPreference("volume_preference");
		dialogPreference.setOnPreferenceClickListener(new OnPreferenceClickListener() {
			
			@Override
			public boolean onPreferenceClick(Preference preference) {
				showVolumeDialog();
				return false;
			}
		});
	}
	
	private void showVolumeDialog() {
		
	}
}
