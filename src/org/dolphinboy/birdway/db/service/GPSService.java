package org.dolphinboy.birdway.db.service;

import org.dolphinboy.birdway.db.dao.DBOpenHelper;

import android.content.Context;



public class GPSService extends DBOpenHelper {
	public GPSService(Context context) {
		super(context);
	}

	public void save() {
		String sql = "insert into bd_gps (latitude, longitude, gpstime) values (?, ?, ?)";
		getWritableDatabase().execSQL(sql, new String[]{"1","2","3"});
	}
}
