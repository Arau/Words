package com.idi.arau;

import android.content.Context;

public class DbProvider {

	private static DbProvider object;
	private DatabaseHelper dbHelper;
	private Context context;
	
	private DbProvider(Context context) {
		this.context = context;
	}
	
	public static DbProvider getInstance(Context context) {
		if (object == null)
			object = new DbProvider(context);
		return object;
	}
	
	public DatabaseHelper getDatabaseHelper() {
		if (dbHelper == null)
			dbHelper = new DatabaseHelper(context);
		return dbHelper;
	}
}
