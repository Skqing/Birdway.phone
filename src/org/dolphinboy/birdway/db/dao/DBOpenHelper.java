package org.dolphinboy.birdway.db.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBOpenHelper extends SQLiteOpenHelper {
	private static final String dbname = "birdway.db";
	private static final int dbversion = 1;
//	protected SQLiteDatabase dbwrite = null;
//	protected SQLiteDatabase dbread = null;
	
	public DBOpenHelper(Context context) {
		super(context, dbname, null, dbversion);
//		try {
//			dbwrite = getReadableDatabase();
//		} catch (Exception e) {
//			dbwrite = null;
//		}
//		dbread = getReadableDatabase();
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		String sql = "create table bd_gps(id_ inteter primary key aotuincrement," +
				" latitude real, longitude real, gpstime varchar(20))";
		String[] args = null;
		db.execSQL(sql, args);
		
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// 数据库版本改变时才会被调用，也就是在应用升级的时候才可能会有用
		
	}
	
	

}
