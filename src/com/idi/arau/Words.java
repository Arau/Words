package com.idi.arau;

import java.io.File;
import java.util.List;

import android.app.Activity;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Typeface;
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
			
			// Level 0
			ModelWord apple = new ModelWord("apple", R.drawable.apple,0);
			ModelWord basket = new ModelWord("basket", R.drawable.basket,0);
			ModelWord boat = new ModelWord("boat", R.drawable.boat,0);
			ModelWord bottle = new ModelWord("bottle", R.drawable.bottle,0);
			ModelWord coins = new ModelWord("coins", R.drawable.coins,0);
			ModelWord engine = new ModelWord("engine", R.drawable.engine,0);
			ModelWord orange = new ModelWord("orange", R.drawable.orange,0);			
			ModelWord pineapple = new ModelWord("pineapple", R.drawable.pineapple,0);
			ModelWord tie = new ModelWord("tie", R.drawable.tie,0);
			ModelWord wheel = new ModelWord("wheel", R.drawable.wheel,0);
			
			// Level 1
			ModelWord dumbbells = new ModelWord("dumbbells", R.drawable.dumbbells,1);
			ModelWord dustbin = new ModelWord("dustbin", R.drawable.dustbin,1);
			ModelWord helmet = new ModelWord("helmet", R.drawable.helmet,1);
			ModelWord screwdriver = new ModelWord("screwdriver", R.drawable.screwdriver,1);
			ModelWord seagull = new ModelWord("seagull", R.drawable.seagull,1);
			ModelWord snail = new ModelWord("snail", R.drawable.snail,1);			

			data.addWord(apple);						
			data.addWord(basket);
			data.addWord(boat);
			data.addWord(bottle);
			data.addWord(coins);
			data.addWord(engine);
			data.addWord(orange);
			data.addWord(pineapple);
			data.addWord(tie);
			data.addWord(wheel);
			
			data.addWord(dumbbells);
			data.addWord(dustbin);
			data.addWord(helmet);
			data.addWord(screwdriver);
			data.addWord(seagull);
			data.addWord(snail);
			
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
		Typeface font = Typeface.createFromAsset(getAssets(), "gloriahallelujah.ttf");
		EditText txt = (EditText) findViewById(R.id.editText1);
		txt.setTypeface(font);
		txt.setText(text);
	}

	private void play(int level) {
		Intent i = new Intent(this, Game.class);		
		i.putExtra("level", level);
		startActivity(i);
	}		
}
