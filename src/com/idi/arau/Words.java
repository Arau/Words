package com.idi.arau;

import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class Words extends Activity {
	private Button button;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		
		button = (Button) findViewById(R.id.button1);
		button.setOnClickListener(new OnClickListener() {
						
			public void onClick(View view) {
				play();				
			}
		});
		
		button = (Button) findViewById(R.id.button2);
		button.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View view) {
				finish();
			}
		});
		
		button = (Button) findViewById(R.id.button3);
		button.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				interactDB();
			}
		});
	}

	protected void interactDB() {
		WordsDataSource data = new WordsDataSource(this);
		data.open();
		
		
		ModelWord m = new ModelWord("word",R.drawable.apple);		
		ModelWord w = new ModelWord("paraula",R.drawable.cinc);
		data.addWord(m);
		data.addWord(w);
		
		
		List<ModelWord> words = data.readAllWords();
		Toast t = new Toast(this);
		String text = "";
		for(ModelWord word: words){
			text = text + " " + word.getWord() + " "+ word.getResource() + " ||| ";
		}		
		EditText txt = (EditText) findViewById(R.id.editText1);
		txt.setText(text);
	}

	private void play() {
		Intent i = new Intent(this, Game.class);
		startActivity(i);
	}
}
