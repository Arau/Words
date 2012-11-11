package com.idi.arau;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class WordsDataSource {

	private SQLiteDatabase database;
	private DatabaseHelper dbHelper;
	private String[] allColumns = { DatabaseHelper.COLUMN_ID,
									DatabaseHelper.COLUMN_WORD, 
									DatabaseHelper.COLUMN_PICTURE,
									DatabaseHelper.COLUMN_LEVEL };

	public WordsDataSource(Context context) {
		dbHelper = new DatabaseHelper(context);
	}

	public void open() throws SQLException {
		database = dbHelper.getReadableDatabase();
	}

	public void close() {
		dbHelper.close();
	}

	public int addWord(ModelWord word) {
		ContentValues values = new ContentValues();
		values.put(DatabaseHelper.COLUMN_WORD, word.getWord());
		values.put(DatabaseHelper.COLUMN_PICTURE, word.getResource());
		long insertId = database.insert(DatabaseHelper.TABLE_WORDS, null,
				values);
		Cursor cursor = database.query(DatabaseHelper.TABLE_WORDS, allColumns,
				DatabaseHelper.COLUMN_ID + " = " + insertId, null, null, null,
				null);
		int modifyedRows = cursor.getCount();
		cursor.close();
		return modifyedRows;
	}

	public List<ModelWord> readAllWords() {
		List<ModelWord> words = new ArrayList<ModelWord>();
		try {

			Cursor cursor = database.query(DatabaseHelper.TABLE_WORDS,
					allColumns, null, null, null, null, null);
			cursor.moveToFirst();
			while (!cursor.isAfterLast()) {
				ModelWord word = cursorToModelWord(cursor);
				words.add(word);
				cursor.moveToNext();
			}
			cursor.close();
		} catch (Exception e) {
			// TODO: handle exception
		}
		return words;
	}

	private ModelWord cursorToModelWord(Cursor cursor) {
		ModelWord model = new ModelWord();
		model.setId(cursor.getLong(0));
		model.setWord(cursor.getString(1));
		model.setResource(cursor.getInt(2));
		return model;
	}
}
