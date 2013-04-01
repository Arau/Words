package com.idi.arau;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class WordsDataSource {

	private static WordsDataSource dataInstance;	
	private DatabaseHelper dbHelper;
	private String[] allColumns = { DatabaseHelper.COLUMN_ID,
			DatabaseHelper.COLUMN_WORD, DatabaseHelper.COLUMN_PICTURE, DatabaseHelper.COLUMN_LEVEL};

	private WordsDataSource(Context context) {
		dbHelper = new DatabaseHelper(context);
	}	
	
	public static WordsDataSource getInstance(Context context) {
		if (dataInstance == null) {
			dataInstance = new WordsDataSource(context);
		}
		return dataInstance;
	}

	public void addWords(List<ModelWord> words) {
		SQLiteDatabase database = dbHelper.getWritableDatabase();
		for (ModelWord word: words) {
			addWord(word, database);
		}
		database.close();
	}
		
	private void addWord(ModelWord word, SQLiteDatabase database) {		
		ContentValues values = new ContentValues();
		values.put(DatabaseHelper.COLUMN_WORD, word.getWord());
		values.put(DatabaseHelper.COLUMN_PICTURE, word.getResource());
		values.put(DatabaseHelper.COLUMN_LEVEL, word.getLevel());
		long insertId = database.insert(DatabaseHelper.TABLE_WORDS, null,
				values);						
	}

	public List<ModelWord> readAllWords() {
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		List<ModelWord> words = new ArrayList<ModelWord>();
		try {			
			Cursor cursor = db.query(DatabaseHelper.TABLE_WORDS,
					allColumns, null, null, null, null, null);
			cursor.moveToFirst();
			while (!cursor.isAfterLast()) {
				ModelWord word = cursorToModelWord(cursor);
				words.add(word);		
				cursor.moveToNext();
			}
			cursor.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		db.close();
		return words;
	}
	
	private ModelWord cursorToModelWord(Cursor cursor) {
		ModelWord model = new ModelWord();
		model.setId(cursor.getLong(0));
		model.setWord(cursor.getString(1));
		model.setResource(cursor.getInt(2));
		model.setLevel(cursor.getInt(3));
		return model;
	}
}

