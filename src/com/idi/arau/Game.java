package com.idi.arau;

import com.idi.arau.R;
import com.idi.arau.R.drawable;
import com.idi.arau.R.id;
import com.idi.arau.R.layout;
import com.idi.arau.R.menu;
import com.idi.arau.R.raw;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.PopupWindow;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.ProgressBar;
import android.widget.TextView;

public class Game extends Activity implements OnClickListener {

	private TimeThread timeThread;
	private ManagerGame manager;

	private static final int DIALOG_GAMEOVER_ID = 1;
	private static final int DIALOG_FINISH_GAME = 2;
	private static final int DIALOG_HELP = 3;
	private int timePerWord;

	private Dialog gameOverDialog = null;
	private Dialog finishDialog = null;
	private Dialog helpDialog = null;

	private ProgressBar timeBar;
	private LinearLayout layout;

	private ViewGame view;
	private MediaPlayer mp = null;
	private SharedPreferences pref;

	Button playAgain;
	Button goStart;

	private int level;
	private SensorManager mSensorManager;
	private ShakeEventListener mSensorListener;

	// /////////////////////////////////////////////////////////////////////////////
	// /// EVENTS

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.level = defineLevel();
		this.manager = ManagerGame.getInstanceManager(this);
		this.manager.refresh();
		initSensor();
		manager.setLevel(this.level);
		manager.restartIndex();
	}

	private void initSensor() {
		mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
		mSensorListener = new ShakeEventListener();
		mSensorListener
				.setOnShakeListener(new ShakeEventListener.OnShakeListener() {

					public void onShake() {
						if (pref.getBoolean("shake", true)) {
							resetViewFromActivity();
						}
					}
				});
	}

	private int defineLevel() {
		Bundle extras = getIntent().getExtras();
		if (extras != null) {
			int value = extras.getInt("level");
			return value;
		}
		return 0;
	}

	@Override
	protected void onPause() {
		super.onPause();
		killTimeThread();
		view.killGameThread();
		view = null;
		mSensorManager.unregisterListener(mSensorListener);
		stopMusic();
	};

	@Override
	protected void onResume() {
		super.onResume();
		pref = PreferenceManager.getDefaultSharedPreferences(this);
		toggleMusic();
		fixWordIndex();
		mSensorManager.registerListener(mSensorListener,
				mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
				SensorManager.SENSOR_DELAY_UI);
		layout = defineLayout();
		this.timeBar = defineProgressTimeBar();
		layout.addView(timeBar);
		view = new ViewGame(this, viewToGame);
		layout.addView(view);
		setContentView(layout);
		timeInit(timeBar);
		view.setTimeThread(timeThread, timePerWord);
		//
		// String s = "musica: " + pref.getBoolean("music", true);
		// Toast.makeText(this, s, Toast.LENGTH_LONG).show();
	}

	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.menu, menu);
		// timeThread.setRunning(false);
		return true;
	}

	// DIALOG
	@Override
	protected Dialog onCreateDialog(int id) {
		Dialog dialog = new Dialog(this);

		switch (id) {
		case DIALOG_GAMEOVER_ID:
			dialog.setContentView(R.layout.game_over_dialog);
			Button playAgain = (Button) dialog.findViewById(R.id.playAgain);
			playAgain.setOnClickListener(this);
			Button goStart = (Button) dialog.findViewById(R.id.goStart);
			dialog.setCancelable(false);
			goStart.setOnClickListener(this);
			gameOverDialog = dialog;
			break;
		case DIALOG_FINISH_GAME:
			dialog.setContentView(R.layout.finish_game_dialog);
			Button playAgn = (Button) dialog.findViewById(R.id.playAgn);
			playAgn.setOnClickListener(this);
			Button goStrt = (Button) dialog.findViewById(R.id.goStrt);
			dialog.setCancelable(false);
			goStrt.setOnClickListener(this);
			finishDialog = dialog;
			break;
		case DIALOG_HELP:
			dialog.setContentView(R.layout.help_dialog);
			Button exit = (Button) dialog.findViewById(R.id.exitHelp);
			exit.setOnClickListener(this);
			dialog.setCancelable(false);
			helpDialog = dialog;
			break;

		default:
			dialog = null;
		}
		return dialog;
	}

	@Override
	protected void onSaveInstanceState(Bundle savedInstance) {
		super.onSaveInstanceState(savedInstance);
		int wordIndex = this.manager.getIndex();
		savedInstance.putInt("wordIndex", wordIndex);
	}

	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		super.onRestoreInstanceState(savedInstanceState);
		int wordIndex = savedInstanceState.getInt("wordIndex");
		this.manager.setIndex(wordIndex);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.restart:
			playAgain();
			return true;
		case R.id.main_menu:
			finish();
			return true;
		case R.id.toggle_music:
			startStopMusic();
			toggleMusic();
			break;
		case R.id.help:
			onHelp();
			return true;
		}
		return false;
	}

	// IMPLEMENT onClick Dialog buttons
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.playAgain:
			dismiss(gameOverDialog);
			playAgain();
			break;
		case R.id.goStart:
			dismiss(gameOverDialog);
			goToStart();
			break;
		case R.id.playAgn:
			dismiss(finishDialog);
			playAgain();
			break;
		case R.id.goStrt:
			dismiss(finishDialog);
			goToStart();
			break;
		case R.id.exitHelp:
			// pause game
			dismiss(helpDialog);
			// play game
			break;
		default:
			finish();
		}
	}

	// /////////////////////////////////////////////////////////////////////////////
	// /// PUBLIC

	public void timeInit(ProgressBar timeBar) {
		if (this.level == 1)
			timePerWord = 40;
		else
			timePerWord = 25;
		timeThread = new TimeThread(this, timeBar, timePerWord);
		timeThread.setRunning(true);
		timeThread.start();
	}

	public void onTimeOut() {
		if (timeThread.isTimeOut()) {
			if (pref.getBoolean("answer", true)) {
				this.runOnUiThread(showSolutionPopUp);
			} else
				this.runOnUiThread(showGameOverDialog);
		}
	}

	public void toggleMusic() {
		if (musicShouldBeOn()) {
			if (mp == null) {
				mp = MediaPlayer.create(this, R.raw.song);
				mp.setLooping(true);
			}
			mp.start();
		} else {
			if (mp != null)
				mp.pause();
		}
	}

	public void restartTime() {
		timeInit(timeBar);
	}

	public void killTimeThread() {
		timeThread.setRunning(false);
		timeThread.interrupt();
	}

	// INTERFACE IMPLEMENTATION
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
			// okPopup();
			resetViewFromActivity();
		}

		@Override
		public void finishGame() {
			showFinishDialog();
		}

		@Override
		public void gameOver() {
			if (pref.getBoolean("answer", true)) {
				showSolution();
			} else
				showGameOverDialog();
		}
	};

	// /////////////////////////////////////////////////////////////////////////////
	// /// PRIVATE

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

	private Runnable showGameOverDialog = new Runnable() {
		public void run() {
			showDialog(DIALOG_GAMEOVER_ID);
		}
	};

	private Runnable showFinishDialog = new Runnable() {
		public void run() {
			showDialog(DIALOG_FINISH_GAME);
		}
	};

	private Runnable showHelpDialog = new Runnable() {

		@Override
		public void run() {
			showDialog(DIALOG_HELP);
		}
	};

	private Runnable showSolutionPopUp = new Runnable() {
		@Override
		public void run() {
			showSolution();
		}
	};

	private Runnable showOkPopup = new Runnable() {
		@Override
		public void run() {
			showOk();
		}
	};

	private void showFinishDialog() {
		this.runOnUiThread(showFinishDialog);
	}

	private void showGameOverDialog() {
		this.runOnUiThread(showGameOverDialog);
	}

	private void onHelp() {
		this.runOnUiThread(showHelpDialog);
	}

	private void okPopup() {
		this.runOnUiThread(showOkPopup);
	}

	protected void playAgain() {
		Intent i = new Intent(this, Game.class);
		i.putExtra("level", this.level);
		startActivity(i);
	}

	protected void goToStart() {
		killTimeThread();
		view.killGameThread();
		finish();
	}

	private View inflatePopupLayout() {
		LinearLayout viewGroup = (LinearLayout) this.findViewById(R.id.popup);
		LayoutInflater layoutInflater = (LayoutInflater) this
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		return layoutInflater.inflate(R.layout.popup_layout, viewGroup);
	}

	private void showSolution() {

		View layout = inflatePopupLayout();

		int width = (view.getWidth() * 5) / 7;
		int height = (view.getHeight() * 1) / 7;

		final PopupWindow popup = new PopupWindow(this);
		popup.setHeight(height);
		popup.setWidth(width);

		popup.setContentView(layout);
		popup.setFocusable(true);

		Typeface font = Typeface.createFromAsset(getAssets(),
				"gloriahallelujah.ttf");
		TextView txt = (TextView) popup.getContentView().findViewById(
				R.id.solution);
		txt.setTypeface(font);

		String word = this.view.getStringCurrentWord();
		txt.setText(word);
		int textPositionX = (width / 2)
				- (int) txt.getPaint().measureText(word) / 2;
		int textPositionY = (height / 2) - (txt.getHeight() / 2);		
		txt.setPadding(textPositionX, 0, 0, 0);

		// Clear the default translucent background
		popup.setBackgroundDrawable(new BitmapDrawable());
		int x = (view.getWidth() - width) - ((view.getWidth() - width) / 2);
		int y = (view.getHeight() - height) - (height / 4);
		popup.showAtLocation(layout, Gravity.NO_GRAVITY, x, y);

		popup.setOnDismissListener(new OnDismissListener() {

			@Override
			public void onDismiss() {
				resetViewFromActivity();
			}
		});
	}

	private View inflateOkLayout() {
		LinearLayout viewGroup = (LinearLayout) this.findViewById(R.id.okpopup);
		LayoutInflater layoutInflater = (LayoutInflater) this
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		return layoutInflater.inflate(R.layout.ok_popup, viewGroup);
	}

	private void showOk() {
		View layout = inflatePopupLayout();

		int width = view.getWidth() / 2;
		int height = view.getHeight() / 2;

		final PopupWindow popup = new PopupWindow(this);
		popup.setHeight(height);
		popup.setWidth(width);

		popup.setContentView(layout);
		popup.setFocusable(true);

		// Clear the default translucent background
		popup.setBackgroundDrawable(new BitmapDrawable());
		int x = (view.getWidth() - width) - ((view.getWidth() - width) / 2);
		int y = (view.getHeight() - height) - ((view.getHeight() - height) / 2);

		popup.setBackgroundDrawable(this.getResources().getDrawable(R.id.ok));
		popup.showAtLocation(layout, Gravity.NO_GRAVITY, x, y);

		popup.setOnDismissListener(new OnDismissListener() {

			@Override
			public void onDismiss() {
				resetViewFromActivity();
			}
		});
	}

	private void resetViewFromActivity() {
		layout.removeView(view);
		view = new ViewGame(this, viewToGame);
		layout.addView(view);

		killTimeThread();
		restartTime();

		setContentView(layout);
		view.setTimeThread(timeThread, timePerWord);
	}

	private void dismiss(Dialog dialog) {
		dialog.dismiss();
		dialog = null;
	}

	private boolean musicShouldBeOn() {
		return pref.getBoolean("music", false);
	}

	private void stopMusic() {
		if (mp != null)
			mp.pause();
	}

	private void startStopMusic() {
		SharedPreferences.Editor editor = pref.edit();
		if (mp != null) {
			if (mp.isPlaying())
				// mp.pause();
				editor.putBoolean("music", false);
			else
				// mp.start();
				editor.putBoolean("music", true);
		} else {
			mp = MediaPlayer.create(this, R.raw.song);
			mp.setLooping(true);
			editor.putBoolean("music", true);
			// mp.start();
		}
		editor.commit();
	}

	private void fixWordIndex() {
		int wordIndex = manager.getIndex();
		if (wordIndex > 0)
			this.manager.setIndex(wordIndex - 1);
	}		
}
