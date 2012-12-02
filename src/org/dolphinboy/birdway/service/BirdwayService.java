package org.dolphinboy.birdway.service;

import java.util.Date;

import org.dolphinboy.birdway.R;
import org.dolphinboy.birdway.activity.BaiduMapActivity;
import org.dolphinboy.birdway.asynwork.LocationTask;
import org.dolphinboy.birdway.asynwork.TaskCallBack;
import org.dolphinboy.birdway.comps.BirdwayAppalication;
import org.dolphinboy.birdway.db.service.GpsDataService;
import org.dolphinboy.birdway.entity.GpsData;
import org.dolphinboy.birdway.net.HttpClientService;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

/**
 * 软件主服务
 * @author DolphinBoy
 * @date
 * @time 
 */
public class BirdwayService extends Service {
	private static final String TAG = "BirdwayService";
	
	private Notification notification = null;
	private PendingIntent pendingintent = null;
	private NotificationManager nm = null;
	
	private String token = null;
//	private ServiceHandler handler;
//	private Handler handler;
	GpsDataService gpsDataService;
	
	public BirdwayService() {
		super();
		Log.i(TAG, "run BirdwayService!");
		
	}

	@Override
	public void onCreate() {
		super.onCreate();
		Log.i(TAG, "run onCreate!");
		BirdwayAppalication app = (BirdwayAppalication)this.getApplication();  //取得全局变量
		this.token = app.getPres().getString("token", "504ca79689fdda581f000001");
		
		//异步方向传感器
//		OrientationTask orientationTask = new OrientationTask(this, new OrigenTaskCallBack(), 10 * 1000);  
		
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
		gpsDataService = new GpsDataService(BirdwayService.this);
		
		LocationTask locationtask = new LocationTask(this, new LocationTaskCallBack(), 60 * 1000);
		locationtask.execute();
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
//		locationManager.removeUpdates(locationListener);
		nm.cancel(R.id.loginbut_id);
		super.onDestroy();
	}
	
	//--------监听器部分--------//
	
    //--------EOF--------//

	
	/**
	 * GPS传感器回调类的实现
	 * @author DolphinBoy
	 * @date 2012-11-5
	 */
	private class LocationTaskCallBack implements TaskCallBack {
		@Override
		public void gpsConnected(Object data) {
			dealGpsData((GpsData) data);
		}

		@Override
		public void gpsConnectedTimeOut() {
			Log.i(TAG, "------获取GPS信息超时------");
		}
	};
    
	/**
	 * 方向传感器的回调类的实现
	 * @author DolphinBoy
	 * @date 2012-11-5
	 */
	private class OrigenTaskCallBack implements TaskCallBack {
		@Override
		public void gpsConnected(Object data) {
			Object[] origen = (Object[]) data;
			Log.i(TAG, "x="+origen[0]+","+"y="+origen[1]+","+"z="+origen[2]);
		}

		@Override
		public void gpsConnectedTimeOut() {
			Log.i(TAG, "------获取方向信息出错------");
		}
	};
	
//    private class NetworkThread implements Runnable {   
//        public void run() {  
//             while (!Thread.currentThread().isInterrupted()) {    
//                  Message message = new Message();   
//                  message.what = SENDGPSDATA;
//                  message.obj = "数据对象";
//                  BirdwayService.this.serviceHandler.sendMessage(message);   
//                  try {   
//                       Thread.sleep(100);    
//                  } catch (InterruptedException e) {   
//                       Thread.currentThread().interrupt();   
//                  }   
//             }   
//        }   
//    } 
//    
//    private Handler serviceHandler = new Handler() {
//		@Override
//		public void handleMessage(Message msg) {
//			super.handleMessage(msg);
//			switch (msg.what) {
//			case SENDGPSDATA:
//				
//				break;
//			}
//		}
//
//		@Override
//		public void dispatchMessage(Message msg) {
//			super.dispatchMessage(msg);
//		}
//
//		@Override
//		public boolean sendMessageAtTime(Message msg, long uptimeMillis) {
//			return super.sendMessageAtTime(msg, uptimeMillis);
//		}
//    };
	/**
	 * 对采集到的数据进行处理，发送或保存
	 * @param gpsdata
	 */
    public void dealGpsData(GpsData gpsdata) {
    	Log.i(TAG, "采集到数据：");
    	Log.i(TAG, "采集时间："+gpsdata.getRecordtime());
		Log.i(TAG, "经度："+gpsdata.getLatitude());
		Log.i(TAG, "纬度："+gpsdata.getLongitude());
		
    	updateNotification(gpsdata);  //首先更新Notification
    	
    	try {  //要开线程或用异步任务，或者基于事件机制，总之不能阻塞，并且要保证其及时性
			int bakcode = HttpClientService.smartSendByGet(token, gpsdata);
			if (bakcode != 200) {
				//错误代码处理
			}
		} catch (Exception e) {
			Log.i(TAG, "------发送数据出错------");
		}
    	
 		try {  //要开线程或用异步任务，总之不能阻塞
 			Log.i(TAG, "gpsDataService.save-->");
 			gpsDataService.save(gpsdata);  //保存数据到数据库
 			Log.i(TAG, "数据已保存!");
 		} catch (Exception e) {
 			Log.i(TAG, "保存数据出错!");
 		}
     }
    /**
     * 更新Notification的信息
     */
    private void updateNotification(GpsData gpsdata) {
    	StringBuilder notisb = new StringBuilder();
    	notisb.append("经度：").append(gpsdata.getLongitude())
    		.append("纬度：").append(gpsdata.getLatitude())
    		.append("高度：").append(gpsdata.getAltitude());
    	
    	notification.setLatestEventInfo(this, (new Date()).toLocaleString()+"更新", notisb.toString(), pendingintent);
    	nm.notify("birdway", R.id.loginbut_id, notification);  //如果重复出现 notification ，则去掉标识符参数 "birdway" 试试
    }
    /**
     * 发送数据，属于异步实时发送，不需要缓存或延迟
     * @param obj
     */
  	private void sendData(GpsData gpsdata) {
  		Log.i(TAG, "begin SendData-->");
  		
//  			Log.i(TAG, "获取用户token!");
//  			HashMap<String, String> user = new HashMap<String,String>();
//  			user.put("username", "dolphinboy");
//  			user.put("password", "longxin");
//  			String usertoken = "504ca79689fdda581f000001";
//  			try {
//  				Log.i(TAG, "begin sendPostBackData!");
//  				usertoken = HttpClientService.sendPostBackData(user, "UTF-8");
//  				Log.i(TAG, "获取用户token成功!");
//  			} catch (Exception e) {
//  				
//  				Log.i(TAG, "------获取用户token出错------");
//  			}
//  			Editor editor = pres.edit();
//  			editor.putString(token, usertoken);
//  			editor.commit();
//  			try {
//  				Log.i(TAG, "begin smartSendByGet!");
//  				HttpClientService.smartSendByGet(token, gpsdata);
//  			} catch (Exception e) {
//  				Log.i(TAG, "------上传数据出错------");
//  			}
  		
  		
  	}
  	
  	/**
  	 * 保存数据：如果内存充足则先缓存再保存，否则直接保存
  	 * @param gpsdata
  	 */
  	public void saveData(GpsData gpsdata) {
  		
  	}

    /**
     * 把方向传感器的值转换为描述信息
     * @param origen
     * @return
     */
    private String parseOrigen(Object[] origen) {
    	String origen_str = "未知";
    	
    	return origen_str;
    }
}
