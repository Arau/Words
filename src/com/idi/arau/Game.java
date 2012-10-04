package com.idi.arau;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ProgressBar;

public class Game extends Activity implements OnClickListener {

	private TimeThread timeThread;
	private static final int DIALOG_GAMEOVER_ID = 1;
	private static final int TIME_X_WORD = 20;
	private Dialog gameOverDialog = null;
	private ProgressBar timeBar;
	private ManagerGame manager;
	private LinearLayout layout;
	private ViewGame view;
	Button playAgain;
	Button goStart;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);

		layout = defineLayout();
		this.timeBar = defineProgressTimeBar();

		this.manager = ManagerGame.getInstanceManager(this);
		manager.restartIndex();

		layout.addView(timeBar);
		view = new ViewGame(this, viewToGame);
		layout.addView(view);
		timeInit(timeBar);
		setContentView(layout);
		view.setTimeThread(timeThread, TIME_X_WORD);		
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
		timeBar.setProgressDrawable(getResources().getDrawable(
				R.drawable.time_bar_def));
		return timeBar;
	}

	public void timeInit(ProgressBar timeBar) {
		timeThread = new TimeThread(this, timeBar, TIME_X_WORD);
		timeThread.setRunning(true);
		timeThread.start();
	}

	public void onTimeOut() {
		if (timeThread.isTimeOut()) {
			this.runOnUiThread(showGameOverDialog);
		}

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
		Intent i = new Intent(this, Game.class);
		startActivity(i);
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

	public void restartTime() {
		timeInit(timeBar);
	}

	public void killTimeThread() {
		timeThread.setRunning(false);
		timeThread.interrupt();		
	}

	private void resetViewFromActivity() {
		layout.removeView(view);
		view = new ViewGame(this, viewToGame);
		layout.addView(view);
		
		killTimeThread();
		restartTime();
		
		setContentView(layout);
		view.setTimeThread(timeThread, TIME_X_WORD);
	}		
	
	ViewToGame viewToGame = new ViewToGame() {

		@Override
		public void restartTime() {
			timeInit(timeBar);
		}

		@Override
		public void killOldThread() {
			killTimeThread();		
		}

		@Override
		public void resetView() {
			resetViewFromActivity();
		}		
	};
}
