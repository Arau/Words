package com.idi.arau;

import com.idi.arau.R;
import com.idi.arau.R.xml;

import android.os.Bundle;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceClickListener;
import android.preference.PreferenceFragment;
import android.widget.SeekBar;


public class PrefsFragment extends PreferenceFragment {
	SeekBar volume;
	Preference dialogPreference;
	
	@Override
	 public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);		
		addPreferencesFromResource(R.xml.settings);				
	}		
}