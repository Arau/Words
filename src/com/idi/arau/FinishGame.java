package com.idi.arau;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

public class FinishGame extends Activity {

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
				playAgain();								
			}
		});
	}


	protected void playAgain() {
		//
	}

	
	
	
//	private void toast() {
//		Toast t = Toast.makeText(this, "press", Toast.LENGTH_SHORT);
//		t.show();
//		ImageView v = new ImageView(this);
//		setContentView(v);
//		Drawable d = this.getResources().getDrawable(R.drawable.apple);
//		v.setImageDrawable(d);
//	}

}
