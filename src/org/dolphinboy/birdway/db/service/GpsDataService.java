package org.dolphinboy.birdway.db.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.dolphinboy.birdway.db.dao.DBOpenHelper;
import org.dolphinboy.birdway.entity.GpsData;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;



public class GpsDataService {  //考虑把所有的SQL语句定义为静态变量
	private static final String TAG = "GpsDataService";
	
	public static final String DATA_STATE_NOSENT = "0";
	public static final String DATA_STATE_SENT = "1";
	
	private DBOpenHelper dbOpenHelper;
	
	public GpsDataService(Context context) {
		dbOpenHelper = new DBOpenHelper(context);
//		DBOpenHelper dbOpenHelper.getReadableDatabase();
	}

	public void save(GpsData gpsdata) {
		StringBuilder sql = new StringBuilder();
		sql.append("insert into t_gpsdata (id_, latitude, longitude, altitude, accuracy, bear, speed, gpstime, ")
			.append("recordtime, provider, state) values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
		Object[] objary = new Object[11];
		objary[0] = null;
		objary[1] = gpsdata.getLatitude();
		objary[2] = gpsdata.getLongitude();
		objary[3] = gpsdata.getAltitude();
		objary[4] = gpsdata.getAccuracy();
		objary[5] = gpsdata.getBear();
		objary[6] = gpsdata.getSpeed();
		objary[7] = gpsdata.getGpstime();
		objary[8] = gpsdata.getRecordtime();
		objary[9] = gpsdata.getProvider();
		objary[10] = gpsdata.getState();
		
		try {
			dbOpenHelper.getWritableDatabase().execSQL(sql.toString(), objary);
		} catch (Exception e) {
			Log.i(TAG, "---保存数据出错---");
		}
	}
	
	/**
	 * 查询分页数据
	 * @return
	 */
	public List<GpsData> find(Integer offset, Integer maxresult) {
		StringBuilder sql = new StringBuilder();
		sql.append("select id_, latitude, longitude, altitude, accuracy, bear, speed, gpstime, ")
			.append("recordtime, provider, state from t_gpsdata limit ?, ?");
		String[] params = new String[]{offset.toString(), maxresult.toString()};
		Cursor cursor = dbOpenHelper.getWritableDatabase().rawQuery(sql.toString(), params);
		List<GpsData> gpsdatalist = new ArrayList<GpsData>();
		while(cursor.moveToNext()) {
			GpsData gpsdata = new GpsData();
			gpsdata.setId(cursor.getInt(cursor.getColumnIndex("id_")));
			gpsdata.setLatitude(cursor.getDouble(cursor.getColumnIndex("latitude")));
			gpsdata.setLongitude(cursor.getDouble(cursor.getColumnIndex("longitude")));
			gpsdata.setAltitude(cursor.getDouble(cursor.getColumnIndex("altitude")));
			gpsdata.setBear(cursor.getFloat(cursor.getColumnIndex("bear")));
			gpsdata.setSpeed(cursor.getFloat(cursor.getColumnIndex("speed")));
			gpsdata.setGpstime(cursor.getLong(cursor.getColumnIndex("gpstime")));
			gpsdata.setRecordtime(cursor.getLong(cursor.getColumnIndex("recordtime")));
			gpsdata.setProvider(cursor.getInt(cursor.getColumnIndex("provider")));
			gpsdata.setState(cursor.getInt(cursor.getColumnIndex("uped")));
			
			gpsdatalist.add(gpsdata);
		};
		if (gpsdatalist.size() == 0) {
			gpsdatalist = null;
			return null;
		}
		return gpsdatalist;
	}
	/**
	 * 查询所有数据
	 * @return
	 */
	public List<GpsData> findAll() {
		StringBuilder sql = new StringBuilder();
		sql.append("select id_, latitude, longitude, altitude, accuracy, bear, speed, gpstime, ")
			.append("recordtime, provider, state from t_gpsdata");
		Cursor cursor = dbOpenHelper.getReadableDatabase().rawQuery(sql.toString(), null);
		List<GpsData> gpsdatalist = new ArrayList<GpsData>();
		while(cursor.moveToNext()) {
			GpsData gpsdata = new GpsData();
			gpsdata.setId(cursor.getInt(cursor.getColumnIndex("id_")));
			gpsdata.setLatitude(cursor.getDouble(cursor.getColumnIndex("latitude")));
			gpsdata.setLongitude(cursor.getDouble(cursor.getColumnIndex("longitude")));
			gpsdata.setAltitude(cursor.getDouble(cursor.getColumnIndex("altitude")));
			gpsdata.setBear(cursor.getFloat(cursor.getColumnIndex("bear")));
			gpsdata.setSpeed(cursor.getFloat(cursor.getColumnIndex("speed")));
			gpsdata.setGpstime(cursor.getLong(cursor.getColumnIndex("gpstime")));
			gpsdata.setRecordtime(cursor.getLong(cursor.getColumnIndex("recordtime")));
			gpsdata.setProvider(cursor.getInt(cursor.getColumnIndex("provider")));
			gpsdata.setState(cursor.getInt(cursor.getColumnIndex("state")));
			
			gpsdatalist.add(gpsdata);
		};
		if (gpsdatalist.size() == 0) {
			gpsdatalist = null;
			return null;
		}
		cursor.close();
		return gpsdatalist;
	}
	
	/**
	 * 获取要上传的数据
	 * @param offset
	 * @param maxresult
	 * @return
	 */
	public String[] getSending(Integer offset, Integer maxresult) {
		StringBuilder sql = new StringBuilder();
		sql.append("select id_, latitude, longitude, altitude, accuracy, bear, speed, gpstime, ")
			.append("recordtime, provider, state from t_gpsdata limit ?, ? order by id_ desc");
		String[] params = new String[]{offset.toString(), maxresult.toString()};
		StringBuilder ids = new StringBuilder();
		StringBuilder jsonstr = new StringBuilder();
		jsonstr.append("[");
		Cursor cursor = dbOpenHelper.getWritableDatabase().rawQuery(sql.toString(), params);
		while(cursor.moveToNext()) {
			ids.append(cursor.getString(cursor.getColumnIndex("id_"))).append(",");
			jsonstr.append("{\"id\":").append(cursor.getInt(cursor.getColumnIndex("id_"))).append("},")
			.append("{\"lat\":").append(cursor.getDouble(cursor.getColumnIndex("latitude"))).append("},")
			.append("{\"lon\":").append(cursor.getDouble(cursor.getColumnIndex("longitude"))).append("},")
			.append("{\"alt\":").append(cursor.getDouble(cursor.getColumnIndex("altitude"))).append("},")
			.append("{\"ber\":").append(cursor.getFloat(cursor.getColumnIndex("bear"))).append("},")
			.append("{\"spd\":").append(cursor.getFloat(cursor.getColumnIndex("speed"))).append("},")
			.append("{\"gpt\":").append(cursor.getLong(cursor.getColumnIndex("gpstime"))).append("},")
			.append("{\"ret\":").append(cursor.getString(cursor.getColumnIndex("recordtime"))).append("},")
			.append("{\"pro\":").append(cursor.getString(cursor.getColumnIndex("provider"))).append("},")
			.append("{\"sta\":").append(cursor.getInt(cursor.getColumnIndex("state"))).append("},");
		};
		ids.deleteCharAt(ids.lastIndexOf(","));
		jsonstr.deleteCharAt(ids.lastIndexOf(","));
		jsonstr.append("]");
		String[] obj = new String[]{ids.toString(), jsonstr.toString()};
		return obj;
	}
	
	/**
	 * 取得数据库记录总条数
	 * @return
	 */
	public long getCount(String state) {
		Log.i(TAG, "getCount-->");
		String sql = null;
		String[] params = null;
		if (state == null) {
			sql = "select count(*) from t_gpsdata";
		} else {
			sql = "select count(*) from t_gpsdata where state = ?";
			params = new String[]{state};
		}
		Cursor cursor = dbOpenHelper.getWritableDatabase().rawQuery(sql, params);
		cursor.moveToFirst();
		long count = cursor.getLong(0);
		Log.i(TAG, "--Count:"+count);
		return count;
	}
	
	/**
	 * 根据ID删除数据
	 * @param id
	 */
	public void delete(int id) {
		
	}
	/**
	 * 删除一段时间的数据
	 * @param startdate
	 * @param enddate
	 */
	public void delete(Date startdate, Date enddate) {
		
	}
}
