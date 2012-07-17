package org.dolphinboy.birdway.service;

import java.util.Date;
import java.util.Iterator;

import org.birdway.mobile.R;
import org.dolphinboy.birdway.activity.ShowInfoActivity;
import org.dolphinboy.birdway.entity.GPSData;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.location.Criteria;
import android.location.GpsSatellite;
import android.location.GpsStatus;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.Settings;
import android.text.format.DateFormat;
import android.util.Log;
import android.widget.Toast;

public class GPSService extends IntentService {
	private NotificationManager nm;
	private Notification notification;
	
	private GPSData gpsdata;
	private LocationManager locationManager;
	//2000ms
    private static final long minTime = 5*1000;
    //最小变更距离 10m
    private static final float minDistance = 0;
	
    private Criteria criteria;
    private String provider;
    private Location location;
    private GpsStatus gpsstatus;
    
    private Context context = this;
    
    
    private static final String TAG = "GPSService";
    
	public GPSService() {
		super("GPSService");
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		Log.i(TAG, "GPSService's onHandleIntent!");
		initLocation();
//		int ontime = 0;
//		if (gpsdata != null) {
//			ontime = (int)gpsdata.getSpeed();
//		}
		
//		shwoNotification(Color.RED, ontime, 2);
	}
	
	private void shwoNotification(int color, int ontime, int offtime) {
		Log.i(TAG, "shwoNotification start!");
		notification = new Notification(R.drawable.ic_launcher, "GPS for Birdway……", System.currentTimeMillis());
		Intent intent = new Intent(this, ShowInfoActivity.class);
		PendingIntent contentIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
		notification.setLatestEventInfo(this, "BirdwayService", "经度:"+gpsdata.getLongitude()+"纬度:"+gpsdata.getLatitude(), contentIntent);
		nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
		notification.ledARGB = color;
		notification.ledOnMS = ontime;  //显示时间
		notification.ledOffMS = offtime;  //关闭时间
		notification.flags = notification.flags | Notification.FLAG_SHOW_LIGHTS;
		nm.notify(R.layout.showinfo, notification);
		Log.i(TAG, "shwoNotification OK!");
	}
	
	private void initLocation()
	{
		locationManager = (LocationManager) getSystemService(this.LOCATION_SERVICE);
		if (locationManager.isProviderEnabled(locationManager.GPS_PROVIDER))
		{
			//查找到服务信息    位置数据标准类  
	        criteria = new Criteria();
	        //查询精度:高 
	        criteria.setAccuracy(Criteria.ACCURACY_FINE);
	        //是否查询海拔:是 
	        criteria.setAltitudeRequired(true);
	        //是否查询方位角:是 
	        criteria.setBearingRequired(true);
	        //是否允许付费 
	        criteria.setCostAllowed(true);
	        //电量要求:低 
	        criteria.setPowerRequirement(Criteria.POWER_LOW);
	        //是否查询速度:是 
	        criteria.setSpeedRequired(true);
			
			provider = locationManager.getBestProvider(criteria, true);
			//设置监听器，自动更新的最小时间为间隔N秒(1秒为1*1000，这样写主要为了方便)或最小位移变化超过N米      
	        //实时获取位置提供者provider中的数据，一旦发生位置变化 立即通知应用程序locationListener  
	        locationManager.requestLocationUpdates(provider, minTime, minDistance, locationListener);
	        //监听卫星，statusListener为响应函数  
	        locationManager.addGpsStatusListener(statusListener);
		} else {
			Toast.makeText(this, "GPS已关闭，请开启GPS！", Toast.LENGTH_SHORT).show(); 
		}
		Log.i(TAG, "initLocation OK!");
	}
	
	private void openGPSSettings() {
		//获取位置管理服务
		locationManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
		while(locationManager.isProviderEnabled(android.location.LocationManager.GPS_PROVIDER)){
			Toast.makeText(this, "请开启GPS！", Toast.LENGTH_SHORT).show();       
            Intent locationintent = new Intent(Settings.ACTION_SECURITY_SETTINGS);       
//            startActivityForResult(locationintent, 0); //此为设置完成后返回到获取界面 
		}
	}
	
	
	//定位监听类负责监听位置信息的变化情况  
    private final LocationListener locationListener = new LocationListener()
    {
    	/**
         * 位置信息变化时触发
         */
		public void onLocationChanged(Location location) {
			Log.i(TAG, "onLocationChanged event!");
			//通过GPS获取位置，新的位置信息放在location中，调用updateToNewLocation函数显示位置信息
			updateToNewLocation(location);
		}
		/**
         * GPS禁用时触发
         */
		public void onProviderDisabled(String provider) {
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
    //卫星状态监听
    private final GpsStatus.Listener statusListener= new GpsStatus.Listener() {
    	public void onGpsStatusChanged(int event) {
            switch (event) {
            //第一次定位
            case GpsStatus.GPS_EVENT_FIRST_FIX:
                Log.i(TAG, "第一次定位");
                break;
            //卫星状态改变
            case GpsStatus.GPS_EVENT_SATELLITE_STATUS:
                Log.i(TAG, "卫星状态改变");
                //获取当前状态
                gpsstatus=locationManager.getGpsStatus(null);
                //获取卫星颗数的默认最大值
                int maxSatellites = gpsstatus.getMaxSatellites();
                //创建一个迭代器保存所有卫星 
                Iterator<GpsSatellite> iters = gpsstatus.getSatellites().iterator();
                int count = 0;     
                while (iters.hasNext() && count <= maxSatellites) {     
                    GpsSatellite s = iters.next();     
                    count++;     
                }   
                System.out.println("搜索到："+count+"颗卫星");
                break;
            //定位启动
            case GpsStatus.GPS_EVENT_STARTED:
                Log.i(TAG, "定位启动");
                break;
            //定位结束
            case GpsStatus.GPS_EVENT_STOPPED:
                Log.i(TAG, "定位结束");
                break;
            }
    	};
    };
    
  //位置更新
    private void updateToNewLocation(Location location) {
    	Log.i(TAG, "updateToNewLocation start!");
    	if (location != null) {
    		float bear = location.getBearing();   //偏离正北方的度数
    		float latitude = (int) (location.getLatitude() * 1E6) ; //维度
    		float longitude= (int)( location.getLongitude() * 1E6); //经度
    		float speed = (float)location.getSpeed(); //速度
    		long gpstime = location.getTime();  //时间 
    		
    		System.out.println("bear:"+bear);
    		System.out.println("latitude:"+latitude);
    		System.out.println("longitude:"+longitude);
    		System.out.println("speed:"+speed);
    		
    		gpsdata.setBear(bear);
    		gpsdata.setLatitude(location.getLatitude());
    		gpsdata.setLongitude(location.getLongitude());
    		gpsdata.setSpeed(location.getSpeed());
			gpsdata.setAltitude(location.getAltitude());  //海拔
			Date date = new Date(); //利用Date进行时间的转换
    		date.setTime(gpstime + 28800000);// UTC时间,转北京时间+8小时
			gpsdata.setGpstime(DateFormat.format("yyyy-MM-dd kk:mm:ss", date).toString());
			date = null;
			
			//更新状态条
			shwoNotification(Color.RED, (int)gpsdata.getSpeed(), 2);
			
			//发送GPS数据广播  
            Intent intent = new Intent();  
            intent.setAction("birdway.gpsreceiver");  
            intent.putExtra("gpsdata", gpsdata);  
            sendBroadcast(intent); 
            
            //这里传递数据给远程服务器
    		
            
    	} else {
    		Toast.makeText(this, "无法获取地理信息", Toast.LENGTH_SHORT).show();
    	}
    	Log.i(TAG, "updateToNewLocation end!");
    }
    
    
    
    @Override
	public void onCreate() {
		super.onCreate();
	}
	@Override
	public void onStart(Intent intent, int startId) {
		super.onStart(intent, startId);
	}
	@Override
	public IBinder onBind(Intent intent) {
		return super.onBind(intent);
	}
	@Override
	public void onDestroy() {
		super.onDestroy();
	}
}
