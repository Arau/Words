package com.idi.arau;

import java.io.File;
import java.util.List;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.IBinder;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class Words extends Activity {
	private Button button;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		fillDataBase();
		button = (Button) findViewById(R.id.button1);
		button.setOnClickListener(new OnClickListener() {

			public void onClick(View view) {
				play();
			}
		});

		button = (Button) findViewById(R.id.preferences);
		button.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {
				goPreferences();
			}
		});

		button = (Button) findViewById(R.id.button3);
		button.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				dumpDataBase();
			}
		});
	}

	@Override
	public void onPause() {
		super.onPause();
		this.finishActivity(0);
	}

	protected void goPreferences() {
		Intent i = new Intent(this, Preferences.class);
		startActivity(i);
	}

//	public void showPreferences() {
//		SharedPreferences pref = getSharedPreferences(
//				"com.arau.asteroides_preferences", MODE_PRIVATE);
//		String s = "musica: " + pref.getBoolean("musica", true)
//				+ ", gráficos: " + pref.getString("graficos", "?")
//				+ ", fragmentos: " + pref.getString("fragmentos", "?")
//				+ ", multijugador: " + pref.getBoolean("multijugador", true)
//				+ ", numJugadors: " + pref.getString("numJugadores", "?")
//				+ ", conexiones: " + pref.getString("conexiones", "?");
//
//		Toast.makeText(this, s, Toast.LENGTH_LONG).show();
//	}

	private void fillDataBase() {
		Resources res = getResources();
		boolean existDB = checkDBStock(new ContextWrapper(this),
				res.getString(R.string.dataBaseName));
		if (!existDB) {
			WordsDataSource data = new WordsDataSource(this);
			data.open();
			ModelWord m = new ModelWord("apple", R.drawable.apple);
			ModelWord w = new ModelWord("orange", R.drawable.orange);
			ModelWord z = new ModelWord("pineapple", R.drawable.pineapple);

			data.addWord(m);
			data.addWord(w);
			data.addWord(z);

			data.close();
		}
	}

	private boolean checkDBStock(ContextWrapper context, String dbName) {
		File dbFile = context.getDatabasePath(dbName);
		return dbFile.exists();
	}

	protected void dumpDataBase() {
		WordsDataSource data = new WordsDataSource(this);
		data.open();
		List<ModelWord> words = data.readAllWords();
		String text = "";
		for (ModelWord word : words) {
			text = text + " " + word.getWord() + " " + word.getResource() + " ";
		}
		data.close();
		EditText txt = (EditText) findViewById(R.id.editText1);
		txt.setText(text);
	}

	private void play() {
		Intent i = new Intent(this, Game.class);
		startActivity(i);
	}		
}
