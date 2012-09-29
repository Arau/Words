package com.idi.arau;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ProgressBar;

public class Game extends Activity  implements OnClickListener {

	private TimeThread timeThread;
	private static final int DIALOG_GAMEOVER_ID = 1;
	private static final int TIME_X_WORD = 10;
	Dialog gameOverDialog = null;

	Button playAgain;
	Button goStart;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);

		LinearLayout layout = defineLayout();
		ProgressBar timeBar = defineProgressTimeBar();

		layout.addView(timeBar);
		layout.addView(new ViewGame(this));
		setContentView(layout);

		timeInit(timeBar);

	}

	private LinearLayout defineLayout() {
		LinearLayout layout = new LinearLayout(this);
		layout.setOrientation(LinearLayout.VERTICAL);
		layout.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT,
				LayoutParams.FILL_PARENT));
		return layout;
	}

	private ProgressBar defineProgressTimeBar() {
		int progressBarStyle = android.R.attr.progressBarStyleHorizontal;		
		ProgressBar timeBar = new ProgressBar(this, null, progressBarStyle);
		timeBar.setProgressDrawable(getResources().getDrawable(R.drawable.time_bar_def));
		
	
		
		return timeBar;
	}

	private void timeInit(ProgressBar timeBar) {
		timeThread = new TimeThread(this, timeBar, TIME_X_WORD);
		timeThread.setRunning(true);
		timeThread.start();
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

		switch (id) {
		case DIALOG_GAMEOVER_ID:
			dialog.setContentView(R.layout.game_over_dialog);
			Button playAgain = (Button) dialog.findViewById(R.id.playAgain);
			playAgain.setOnClickListener(this);
			Button goStart = (Button) dialog.findViewById(R.id.goStart);
			goStart.setOnClickListener(this);
			gameOverDialog = dialog;
			break;

		default:
			dialog = null;
		}
		return dialog;
	}


	protected void playAgain() {
		Log.v("mmm", "adasdas");
	}

	protected void goToStart() {
		finish();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.playAgain:
			gameOverDialog.dismiss();
			gameOverDialog = null;	
			playAgain();
			break;
		case R.id.goStart:
			goToStart();
			break;		
		}		
	}
}
