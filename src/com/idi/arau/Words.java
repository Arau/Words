package com.idi.arau;

import java.io.File;
import java.util.List;

import android.app.Activity;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class Words extends Activity {
	private Button button;
	private static final int EASY = 0;
	private static final int HARD = 1;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		fillDataBase();
		button = (Button) findViewById(R.id.easy);
		button.setOnClickListener(new OnClickListener() {

			public void onClick(View view) {
				play(EASY);
			}
		});
		
		button = (Button) findViewById(R.id.hard);
		button.setOnClickListener(new OnClickListener() {

			public void onClick(View view) {
				play(HARD);
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
			ModelWord apple = new ModelWord("apple", R.drawable.apple,0);
			ModelWord orange = new ModelWord("orange", R.drawable.orange,0);
			ModelWord pineapple = new ModelWord("pineapple", R.drawable.pineapple,0);
			ModelWord dumbbells = new ModelWord("dumbbells", R.drawable.dumbbells,1);

			data.addWord(apple);
			data.addWord(orange);
			data.addWord(pineapple);
			data.addWord(dumbbells);
			
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
		List<ModelWord> wordsZ = data.readAllWords(0);
		List<ModelWord> wordsO = data.readAllWords(1);
		String text = "";
		for (ModelWord word : wordsZ) {
			text = text + " " + word.getWord() + " " + word.getResource() + " ";
		}
		
		for (ModelWord word : wordsO) {
			text = text + " " + word.getWord() + " " + word.getResource() + " ";
		}
		
		data.close();
		EditText txt = (EditText) findViewById(R.id.editText1);
		txt.setText(text);
	}

	private void play(int level) {
		Intent i = new Intent(this, Game.class);		
		i.putExtra("level", level);
		startActivity(i);
	}		
}
