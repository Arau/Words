package com.idi.arau;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

public class finishGame extends Activity {
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);		
		setContentView(R.layout.finish);		
		Toast toast = Toast.makeText(this, "Finished game", Toast.LENGTH_SHORT);
		toast.show();		
	}
}
