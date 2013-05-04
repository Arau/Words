package com.idi.arau;

import java.util.HashMap;
import java.util.Map;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class UserController {
	
	private static UserController userControllerObject;	
	private DatabaseHelper dbHelper; 
	private Map<String, Integer> info;
	
	private UserController(Context context) {
		DbProvider provider = DbProvider.getInstance(context);
		dbHelper = provider.getDatabaseHelper();
		info = new HashMap<String, Integer>();
	}
	
	public static UserController getInstance(Context context) {
		if (userControllerObject == null) {						
			userControllerObject = new UserController(context);			
		}		
		return userControllerObject;
	}
	
	public void deleteUser(String user) {
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		String sql = "DELETE FROM users WHERE username = ?;";
		try {
			Cursor c = db.rawQuery(sql, new String[] {user});
			c.getCount();
			info.remove(user);
			c.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		db.close();
	}
	
	public void setMaxScore(String user, int points) {
		ContentValues values = new ContentValues();
		values.put(DatabaseHelper.COLUMN_USER, user);
		values.put(DatabaseHelper.COLUMN_SCORE, points);

		int score = getMaxScore(user);
		
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		try {
			if (score == -1) {
				db.insert(DatabaseHelper.TABLE_USERS, null, values);
				this.info.put(user, score);
			}
			else if (score < points) { 
				db.update(DatabaseHelper.TABLE_USERS, values, "username=?", new String[] {user});
				this.info.put(user, score);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}		
		db.close();
	}
	
	public Map<String, Integer> getRanking() {
		SQLiteDatabase db = dbHelper.getReadableDatabase();		
		try {
			Cursor cursor = db.rawQuery(
					"SELECT username, score " +
					"FROM users " +
					"ORDER BY score DESC", 
							new String[]{});
			
			if (cursor.getCount() != 0) {
				cursor.moveToFirst();
				while (!cursor.isAfterLast()) {
					info.put(cursor.getString(0), cursor.getInt(1));
					cursor.moveToNext();
				}
			}
			cursor.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		db.close();
		return info;
	}

	private int getMaxScore(String username) {
		if (this.info.get(username) == null) 
			return getDatabaseScore(username);
		else 
			return this.info.get(username);
	}
	
	
	private int getDatabaseScore(String username) {
		int score = -1;
		SQLiteDatabase db = dbHelper.getReadableDatabase();
		try {			
			Cursor cursor = db.rawQuery("SELECT score FROM users WHERE username = ?", new String[] {username});
			if (cursor.getCount() == 1) {
				cursor.moveToFirst();
				score = cursor.getInt(0);				
			}
			cursor.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		db.close();
		return score;
	}
}
