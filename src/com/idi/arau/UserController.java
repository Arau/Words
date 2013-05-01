package com.idi.arau;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class UserController {
	
	private static UserController userControllerObject;	
	private DatabaseHelper dbHelper; 
	private int score;
	
	private UserController(Context context) {
		this.score = -1;
		DbProvider provider = DbProvider.getInstance(context);
		dbHelper = provider.getDatabaseHelper();
	}
	
	public static UserController getInstance(Context context) {
		if (userControllerObject == null) {						
			userControllerObject = new UserController(context);			
		}		
		return userControllerObject;
	}
	
	public int getMaxScore(String username) {				
		if (this.score == -1) 
			getDatabaseScore(username);
		return score;
	}
	
	public void setMaxScore(String user, int score) {
		this.score = score;
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put(DatabaseHelper.COLUMN_USER, user);
		values.put(DatabaseHelper.COLUMN_SCORE, score);
		db.insert(DatabaseHelper.TABLE_USERS, null, values);
		db.close();
	}
	
	private void getDatabaseScore(String username) {
		SQLiteDatabase db = dbHelper.getReadableDatabase();
		
		try {			
			Cursor cursor = db.rawQuery("SELECT score WHERE username = ?", new String[] {username}); 
			cursor.moveToFirst();
			this.score = cursor.getInt(0);
			cursor.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		db.close();
	}
}
