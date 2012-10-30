package org.dolphinboy.birdway.db.service;

import java.util.List;

import org.dolphinboy.birdway.db.dao.DBOpenHelper;
import org.dolphinboy.birdway.entity.BaseEntity;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;

public class BaseService<T extends BaseEntity> {
	private DBOpenHelper dbOpenHelper;
	protected SQLiteDatabase dbwrite;
	protected SQLiteDatabase dbread;
	
	public BaseService() { 
		
	}
	public BaseService(Context context) {
		this.dbOpenHelper = new DBOpenHelper(context);
//		dbwrite = dbOpenHelper.getWritableDatabase();
//		dbread = dbOpenHelper.getReadableDatabase();
	}

	public void save(T entity) throws SQLiteException {
		String sql = "insert into bd_gps (latitude, longitude, gpstime) values (?, ?, ?)";
//		db.execSQL(sql, new String[]{entity})
	}
	public void save(List<T> entitys) throws SQLiteException {
		
	}
	
}
