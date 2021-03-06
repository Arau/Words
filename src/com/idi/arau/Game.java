package com.idi.arau;

import java.util.Random;

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
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.PopupWindow;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.TextView;
import android.widget.Toast;

public class Game extends Activity implements OnClickListener {

	private ManagerGame manager;

	private static final int DIALOG_GAMEOVER_ID = 1;
	private static final int DIALOG_FINISH_GAME = 2;
	private static final int DIALOG_HELP = 3;

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
		view.killGameThread();
		view = null;
		mSensorManager.unregisterListener(mSensorListener);
		stopMusic();
	};

	@Override
	protected void onResume() {
		super.onResume();

		definePrefs();		
		toggleMusic();
		fixWordIndex();

		mSensorManager.registerListener(mSensorListener,
				mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
				SensorManager.SENSOR_DELAY_UI);
		
		layout = defineLayout();
		view = new ViewGame(this, viewToGame);
		layout.addView(view);
		setContentView(layout);
	}

	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.menu, menu);
		return true;
	}

	// DIALOG
	@Override
	protected Dialog onCreateDialog(int id) {
		Dialog dialog = new Dialog(this);

		switch (id) {
		case DIALOG_GAMEOVER_ID:
			showScore(dialog);
			break;
		case DIALOG_FINISH_GAME:
			showScore(dialog);
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
		default:			
			finish();
		}
	}

	// /////////////////////////////////////////////////////////////////////////////
	// /// PUBLIC

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

	// INTERFACE IMPLEMENTATION
	ViewToGame viewToGame = new ViewToGame() {

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
			subtractScore();
			
			if (pref.getBoolean("answer", true)) {
				showSolution();
			} else
				showGameOverDialog();
		}
		
		@Override
		public void inflateMenu() {			
			openOptionsMenu();
		}
	};

	// /////////////////////////////////////////////////////////////////////////////
	// /// PRIVATE

	private LinearLayout defineLayout() {
		LinearLayout layout = new LinearLayout(this);
		layout.setOrientation(LinearLayout.VERTICAL);
		layout.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,
				LayoutParams.MATCH_PARENT));
		return layout;
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
		i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		i.putExtra("level", this.level);
		startActivity(i);
	}

	protected void goToStart() {
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

		Typeface font = Typeface.createFromAsset(getAssets(), "gloriahallelujah.ttf");
		TextView txt = (TextView) popup.getContentView().findViewById(R.id.solution);
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
		setContentView(layout);
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
	
	private void subtractScore() {
		int aux = 0;
		int score = pref.getInt("score", aux);
		Random rand = new Random();			
		int num = rand.nextInt(1000 - 900 + 1) + 900;
		score -= num;
		pref.edit().putInt("score", score).commit();
	}
	
	private void definePrefs() {
		pref = PreferenceManager.getDefaultSharedPreferences(this);
		int maxScore = this.manager.getWordsLength(level) * 1000;
		pref.edit().putInt("score", maxScore).commit();
	}
	
	private void showScore(final Dialog dialog) {
		dialog.setContentView(R.layout.finish_game_dialog);
		dialog.setTitle("Score");
		dialog.setCancelable(false);
		
		int aux = 0;
		final int score = pref.getInt("score", aux);
		
		TextView pointsText = (TextView) dialog.findViewById(R.id.points);
		pointsText.setText("" + score);
		
		Button goStrt = (Button) dialog.findViewById(R.id.accept);
		goStrt.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				EditText userInput = (EditText) dialog.findViewById(R.id.username);
				CharSequence username = userInput.getText();
				if (username == "")
					Toast.makeText(Game.this, "Please, define user", Toast.LENGTH_SHORT).show();
				else {
					saveMaxScore(username, score);

					// Go main activity
					finish();
				}
			}
			
			private void saveMaxScore(CharSequence username, int score) {
				UserController userController = UserController.getInstance(Game.this);
				userController.setMaxScore(username.toString().trim(), score);
			}
			
		});
	}
}
