package com.idi.arau;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

	private static final String DATABASE_NAME = "wordsDB.db";

	public static final String TABLE_WORDS = "words";
	public static final String COLUMN_ID = "_id";
	public static final String COLUMN_WORD = "word";
	public static final String COLUMN_PICTURE = "picture";
	public static final String COLUMN_LEVEL = "level";

	private static final String DATABASE_CREATE = "CREATE TABLE " + TABLE_WORDS
			+ " (" + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
			+ COLUMN_WORD + " TEXT, " + COLUMN_PICTURE + " TEXT," + COLUMN_LEVEL + " INTEGER );";
	
	//+ COLUMN_WORD + " TEXT, " + COLUMN_PICTURE + " TEXT);";

	public DatabaseHelper(Context context) {
		super(context, DATABASE_NAME, null, 1);		
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(DATABASE_CREATE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		android.util.Log.v("Words",
				"Upgrading database, which will destroy all old data");
		db.execSQL("DROP TABLE IF EXISTS words");
		onCreate(db);
	}
}
