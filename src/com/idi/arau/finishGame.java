package com.idi.arau;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

public class finishGame extends Activity {

	Button button;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.finish);
		Toast toast = Toast.makeText(this, "Finished game", Toast.LENGTH_SHORT);
		toast.show();
		button = (Button) findViewById(R.id.button1);
		button.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				toast();
			}
		});
	}

	private void toast() {
		Toast t = Toast.makeText(this, "press", Toast.LENGTH_SHORT);
		t.show();
	}

}
