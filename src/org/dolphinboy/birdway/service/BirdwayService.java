package org.dolphinboy.birdway.service;

import java.util.Date;

import org.dolphinboy.birdway.R;
import org.dolphinboy.birdway.activity.BaiduMapActivity;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

/**
 * 软件主服务
 * @author DolphinBoy
 * @date
 * @time 
 */
public class BirdwayService extends Service {
	private static final String TAG = "BirdwayService";
	public static final int SENDGPSDATA = 1001;
//	private SensorListener sensorlistener;
	private LocationManager locationManager;
	private String provider;
	
	private Notification notification;
	private PendingIntent pendingintent;
	private NotificationManager nm;
//	private ServiceHandler handler;
	private Handler handler;
	private StringBuilder str_orientation;  //用文字描述方向信息
	Object sensor_data[];  //组合采集到的数据用于显示到Notification
	
	public BirdwayService() {
		super();
		Log.i(TAG, "run BirdwayService!");
		
	}

	@Override
	public void onCreate() {
		super.onCreate();
		Log.i(TAG, "run onCreate!");
		
		locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		Log.i(TAG, "locationManager:=:"+locationManager);
		boolean lmenable = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
		if (lmenable) {
			locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
		} else {
			Toast.makeText(this, "GPS已关闭，请开启GPS！", Toast.LENGTH_LONG).show();
		}
//		if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER))
//		{
//	        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5*1000, 3, locationListener);
//		} else if (locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
//			locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 5*1000, 3, locationListener);
//		} else {
//			Toast.makeText(this, "GPS已关闭，请开启GPS！", Toast.LENGTH_LONG).show();
//		}
		
		//显示notification
		notification = new Notification(R.drawable.logo, "Birdway服务开始运行...", System.currentTimeMillis());
		Intent intent = new Intent(this, BaiduMapActivity.class);
		pendingintent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
		nm = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
		notification.setLatestEventInfo(this, (new Date()).toLocaleString()+"更新", "数据获取中……", pendingintent);
		nm.notify("birdway", R.id.loginbut_id, notification);
		//消息传输
//		Looper looper = Looper.getMainLooper();
//		handler = new ServiceHandler(looper);
		Log.i(TAG, "onCreate End!");
		
		//监控网络
		//ConnectivityManager connectivity = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		//监控WiFi
		//final WifiManager wifimanager = (WifiManager) getSystemService(Context.WIFI_SERVICE);
		
	}
	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		Log.i(TAG, "run onStartCommand!");  //activity中的startService(intent);方法会重复调用这个方法
//		return super.onStartCommand(intent, flags, startId);
		return Service.START_STICKY;
	}

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}
	
	@Override
	public void onDestroy() {
		Log.i(TAG, "run onDestroy!");  //activity中的stopService(intent);方法会重复调用这个方法
//		NotificationManager nm = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
		locationManager.removeUpdates(locationListener);
		nm.cancel(R.id.loginbut_id);
		super.onDestroy();
	}
	
	//--------监听器部分--------//
	
	/**
	 * 监听位置信息的变化情况 
	 */
    private final LocationListener locationListener = new LocationListener()
    {
    	/**
         * 位置信息变化时触发
         */
		public void onLocationChanged(Location location) {
			Log.i(TAG, "onLocationChanged event!");
			//通过GPS获取位置，新的位置信息放在location中，调用updateToNewLocation函数显示位置信息
			location.getLatitude();
			if (sensor_data == null) {
				sensor_data = new Object[]{location.getLongitude(), 
						location.getLatitude(), location.getAltitude(), str_orientation};
			}
			else {
				sensor_data[0] = location.getLongitude();
				sensor_data[1] = location.getLatitude();
				sensor_data[2] = location.getAltitude();
				sensor_data[3] = str_orientation;
			}
			updateNotification(sensor_data);
//			NetworkTask networktask = new NetworkTask(BirdwayService.this, );
			Message message = new Message();   
            message.what = SENDGPSDATA;
            message.obj = sensor_data;
            BirdwayService.this.serviceHandler.sendMessage(message);
		}
		/**
         * GPS禁用时触发
         */
		public void onProviderDisabled(String provider) {
			Toast.makeText(BirdwayService.this, "GPS已关闭，请开启GPS！", Toast.LENGTH_LONG).show();
			Log.i(TAG, "onProviderDisabled!");
		}
		/**
         * GPS开启时触发
         */
		public void onProviderEnabled(String provider) {
			Log.i(TAG, "onProviderEnabled!");
		}
		/**
         * GPS状态变化时触发
         */
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
//			updateToNewLocation(null);
		}
    };
    //--------EOF--------//
    /**
     * 更新Notification的信息
     */
    private void updateNotification(Object data[]) {
    	StringBuilder sb = new StringBuilder();
    	if (data[0] != null) {
        	sb.append("经度：").append(data[0]);
    	} else {
    		sb.append("经度：").append("未知");
    	}
    	if (data[0] != null) {
        	sb.append("纬度：").append(data[1]);
    	} else {
    		sb.append("纬度：").append("未知");
    	}
    	if (data[0] != null) {
        	sb.append("高度：").append(data[2]);
    	} else {
    		sb.append("高度：").append("未知");
    	}
    	if (data[0] != null) {
        	sb.append("方向：").append(data[3]);
    	} else {
    		sb.append("方向：").append("未知");
    	}
    	
    	notification.setLatestEventInfo(this, (new Date()).toLocaleString()+"更新", sb.toString(), pendingintent);
    	nm.notify("birdway", R.id.loginbut_id, notification);  //如果重复出现 notification ，则去掉标识符参数 "birdway" 试试
    	
    	
    }
	//--------内部类部分--------//
	
    private class NetworkThread implements Runnable {   
        public void run() {  
             while (!Thread.currentThread().isInterrupted()) {    
                  Message message = new Message();   
                  message.what = SENDGPSDATA;
                  message.obj = "数据对象";
                  BirdwayService.this.serviceHandler.sendMessage(message);   
                  try {   
                       Thread.sleep(100);    
                  } catch (InterruptedException e) {   
                       Thread.currentThread().interrupt();   
                  }   
             }   
        }   
   } 
    
    Handler serviceHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case SENDGPSDATA:
				
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
    };
}
