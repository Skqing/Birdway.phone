package org.dolphinboy.birdway.asynwork;

import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import org.dolphinboy.birdway.entity.GpsData;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

public class LocationTask extends AsyncTask {
	private static final String TAG = "LocationTask";
	
	private boolean time_out = false;  //是否超时
	private boolean data_conntected = false;  //是否取得数据
	private long time_duration = 5000;  //此异步任务超时
	
	private TaskCallBack callBk = null;
	private Context context = null;
	
	private LocationManager locationManager = null;
	private Location location = null;
	
	private LocationHandler location_handler = null;
	
	
	public LocationTask(Context context, TaskCallBack callBk, long time_out) {
		this.callBk = callBk;
		this.context = context;
		this.time_duration = time_out;
		InitLocation();
	}

	private void InitLocation() {
		locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
		location_handler = new LocationHandler();
		boolean lmenable = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
		if (lmenable) {
			//添加位置监听器
			locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 8*1000, 30, locationListener);
			Log.i(TAG, "add location listener!");
		} else {
			time_out = true;
			Toast.makeText(context, "GPS已关闭，请开启GPS！", Toast.LENGTH_LONG).show();
		}
	}
	
	@Override
	protected Object doInBackground(Object... params) {
		Log.i(TAG, "run doInBackground!");
		while (!time_out && !data_conntected) {
			//选择数据来源
			location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
			if (location == null) {
				location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
			}
			//获得上一次数据
			if (location != null && callBk != null) {
				Message msg = location_handler.obtainMessage();
				msg.what = 0;
				msg.obj = parseGpsData(location);
				location_handler.sendMessage(msg);
				break;
			}
		}
		return null;
	}
    
	/**
	 * 监听位置信息的变化情况 
	 */
    private final LocationListener locationListener = new LocationListener()
    {
        //位置信息变化时触发
		public void onLocationChanged(Location location) {
			Log.i(TAG, "onLocationChanged event!");
			//通过GPS获取位置，新的位置信息放在location中
			data_conntected = true;
			Message msg = location_handler.obtainMessage();
			msg.what = 0;
			msg.obj = parseGpsData(location);
			location_handler.sendMessage(msg);
		}
        //GPS禁用时触发
		public void onProviderDisabled(String provider) {
			Toast.makeText(context, "GPS已关闭，请开启GPS！", Toast.LENGTH_LONG).show();
			Log.i(TAG, "onProviderDisabled!");
		}
        //GPS开启时触发
		public void onProviderEnabled(String provider) {
			Log.i(TAG, "onProviderEnabled!");
		}
        //GPS状态变化时触发
		public void onStatusChanged(String provider, int status, Bundle extras) {
			Log.i(TAG, "onStatusChanged!");
			switch (status) {
            //GPS状态为可见时
            case LocationProvider.AVAILABLE:
                Log.i(TAG, "当前GPS状态为可见状态");
                break;
            //GPS状态为服务区外时
            case LocationProvider.OUT_OF_SERVICE:
                Log.i(TAG, "当前GPS状态为服务区外状态");
                break;
            //GPS状态为暂停服务时
            case LocationProvider.TEMPORARILY_UNAVAILABLE:
                Log.i(TAG, "当前GPS状态为暂停服务状态");
                break;
            }
		}
    };
    
	private class LocationHandler extends Handler {
		@Override
		public void handleMessage(Message msg) {
			Log.i(TAG, "LocationHandler-->handleMessage");
			super.handleMessage(msg);
			if(callBk == null)
				return;
			switch (msg.what) {
			case 0:
				callBk.gpsConnected(msg.obj);
				break;
			case 1:
				callBk.gpsConnectedTimeOut();
				break;
			}
		}

		@Override
		public void dispatchMessage(Message msg) {
			super.dispatchMessage(msg);
		}

		@Override
		public boolean sendMessageAtTime(Message msg, long uptimeMillis) {
			return super.sendMessageAtTime(msg, uptimeMillis);
		}
	}
	
	@Override
	protected void onPreExecute() {
		Log.i(TAG, "run onPreExecute!");
		super.onPreExecute();
		Timer timer = new Timer();
		timer.schedule(new TimerTask() {
			@Override
			public void run() {
				time_out = true;
			}
		}, time_duration);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	protected void onPostExecute(Object result) {
		Log.i(TAG, "run onPostExecute!");
//		locationManager.removeUpdates(locationListener);
//		Log.i(TAG, "remove location listener!");
		// 获取超时
		if (time_out && callBk != null)
			location_handler.sendEmptyMessage(1);
		super.onPostExecute(result);
	}
	
	/**
	 * 把location转换为GpsData
	 * @param location
	 * @return
	 */
	public GpsData parseGpsData(Location location) {
		GpsData gpsdata = new GpsData();  //###这个对象是否定义为类变量？
		gpsdata.setLongitude(location.getLongitude());
		gpsdata.setLatitude(location.getLatitude());
		gpsdata.setAltitude(location.getAltitude());
		gpsdata.setAccuracy(location.getAccuracy());
		gpsdata.setBear(location.getBearing());
		gpsdata.setSpeed(location.getSpeed());
		gpsdata.setGpstime(location.getTime());  //这个时间要格式化 一下，能够和服务器端的JS包括mongodb数据库中的数据类型一致，并且考虑国际化
		gpsdata.setRecordtime((new Date()).getTime());  //这个时间要格式化 一下，能够和服务器端的JS包括mongodb数据库中的数据类型一致，并且考虑国际化
		
		if ("gps".equals(location.getProvider().toLowerCase())) {
			gpsdata.setProvider(GpsData.PROVIDER_GPS);
		} else if ("net".equals(location.getProvider().toLowerCase())) {
			gpsdata.setProvider(GpsData.PROVIDER_NET);
		} else if ("apn".equals(location.getProvider().toLowerCase())) {
			gpsdata.setProvider(GpsData.PROVIDER_APN);
		}
		
		return gpsdata;
    }
}
