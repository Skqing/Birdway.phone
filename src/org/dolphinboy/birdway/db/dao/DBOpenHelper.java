package org.dolphinboy.birdway.db.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBOpenHelper extends SQLiteOpenHelper {
	private static final String dbname = "birdway.db";
	private static final int dbversion = 1;
	
	public DBOpenHelper(Context context) {
		super(context, dbname, null, dbversion);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		StringBuilder initsql = new StringBuilder();
		initsql.append("CREATE TABLE IF NOT EXISTS t_gpsdata(id_ INTEGER PRIMARY KEY AUTOINCREMENT, ")
			.append("latitude REAL, longitude REAL, altitude REAL, accuracy REAL, bear REAL, ")
			.append("speed REAL, gpstime INTEGER, recordtime INTEGER, provider varchar(10), state INTEGER)");
		db.execSQL(initsql.toString());
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// 数据库版本改变时才会被调用，也就是在应用升级的时候才可能会有用
		
	}
	
	

}
