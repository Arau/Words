package com.idi.arau;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ProgressBar;
import android.widget.Toast;

public class Game extends Activity {

	private TimeThread timeThread;
	static final int DIALOG_GAMEOVER_ID = 1;
	Dialog gameOverDialog = null;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);

		LinearLayout layout = new LinearLayout(this);
		layout.setOrientation(LinearLayout.VERTICAL);

		layout.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT,
				LayoutParams.FILL_PARENT));

		// LayoutInflater inflater = getLayoutInflater();
		// ProgressBar timeBar = (ProgressBar )
		// inflater.inflate(R.layout.time_bar_def, null);

		ProgressBar timeBar = new ProgressBar(this, null,
				android.R.attr.progressBarStyleHorizontal);

		layout.addView(timeBar);
		layout.addView(new ViewGame(this));
		setContentView(layout);

		// Time
		timeThread = new TimeThread(this, timeBar, 50);

		timeThread.setRunning(true);
		timeThread.start();
	}

	@Override
	public void onPause() {
		super.onPause();

	}

	public void onTimeOut() {
		this.runOnUiThread(showGameOverDialog);
	}

	private Runnable showGameOverDialog = new Runnable() {
		public void run() {
			showDialog(DIALOG_GAMEOVER_ID);
		}
	};

	@Override
	protected Dialog onCreateDialog(int id) {
		Dialog dialog = new Dialog(this);
		
		switch(id) {
		case DIALOG_GAMEOVER_ID:
			gameOver();

		}
		return dialog;
	}
	private void gameOver() {
		Toast toast = Toast.makeText(this, "Game Over", Toast.LENGTH_SHORT);
		toast.show();
	}
}
