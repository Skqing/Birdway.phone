package org.dolphinboy.birdway.comps;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.util.Log;
import android.widget.Toast;

import com.baidu.mapapi.BMapManager;
import com.baidu.mapapi.MKEvent;
import com.baidu.mapapi.MKGeneralListener;

public class BirdwayAppalication extends Application {
	private static final String TAG = "BirdwayAppalication";
	static BirdwayAppalication birdwayapp;  //为什幺要定义这个变量，并且是静态的？
	private SharedPreferences pres = null;  //把参数加载到内存
	public static final String bmapkey = "9EBF2E26C758A5D44FEA1F1E9C6A38514EE1E35B";  //这个参数应该有多重情况保证它的存在，并且可变
	//百度MapAPI的管理类
	private BMapManager bmapmanager;
/**
	在Application里生成BMapManager对象并初使化，在程序退出时调用destory，在需要

	使用sdk功能的activity的onCreate里调用start, onDestroy调用stop，或者

	onResume/onPause分别调用start和stop。

	locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 

	100, locationListener);
	这个方法中第二个和第三个参数的具体意义
	//假如provider被用户停止，（比如关闭GPS等）,更新会停止。并且

	onProviderDisabled方法(监听器中一个需要复写的方法)会被调用，只要provider再

	次变为可用状态，onProviderEnable方法会被调用，并且更新操作立刻开始。
	//通知(更新)的频率可以通过使用minTime(最小更新时间 单位：毫秒)和minDistance

	（单位：米）参数，如果minTime大于0，LocationManager能够在minTime时间内休息

	来保存电量，如果minDistance大于0，每变化这个距离就会进行一次更新，如果希望

	尽可能频繁的更新的数据，则把两个参数均设置为0.
	//后台的服务应该注意，设置一个合理的minTime使得设备一直保持GPS或者WIFI的时

	候不耗费太多的电量。不推荐使用6000ms的minTime值
	//使用该方法的线程必须时开启消息循环的线程，例如被调用activity的主线程（可

	以参考Android线程机制，默认新建的线程是没有开启消息循环的，主线程开启消息循
	环，可以参考SDK的Looper类）
*/
	@Override
	public void onCreate() {
		Log.v(TAG, "onCreate");
		birdwayapp = this;
		bmapmanager = new BMapManager(this);
		bmapmanager.init(BirdwayAppalication.bmapkey, new BMapGeneralListener());
		this.pres = this.getSharedPreferences("birdway", Context.MODE_PRIVATE);
		super.onCreate();
	}

	@Override
	public void onTerminate() {
		// TODO Auto-generated method stub
		super.onTerminate();
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		// TODO Auto-generated method stub
		super.onConfigurationChanged(newConfig);
	}

	@Override
	public void onLowMemory() {
		// TODO Auto-generated method stub
		super.onLowMemory();
	}
	
	// 常用事件监听，用来处理通常的网络错误，授权验证错误等
	static class BMapGeneralListener implements MKGeneralListener {
//		@Override
		public void onGetNetworkState(int iError) {
			Log.d(TAG, "onGetNetworkState error is "+ iError);
			Toast.makeText(BirdwayAppalication.birdwayapp.getApplicationContext(), ":-( 网络连接出错啦!", Toast.LENGTH_LONG).show();
		}

//		@Override
		public void onGetPermissionState(int iError) {
			Log.d(TAG, "onGetPermissionState error is "+ iError);
			if (iError == MKEvent.ERROR_PERMISSION_DENIED) {  //这个提示应该发送到服务器
				Toast.makeText(BirdwayAppalication.birdwayapp.getApplicationContext(), "此应用的Key授权失败，请联系开发人员!", Toast.LENGTH_LONG).show();
//				BMapApiDemoApp.mDemoApp.m_bKeyRight = false;
			}
		}
	}
	
	public void setBmapmanager(BMapManager bmapmanager) {
		this.bmapmanager = bmapmanager;
	}

	public BMapManager getBmapmanager() {
		if (bmapmanager == null) {
			bmapmanager = new BMapManager(this);
			bmapmanager.init(BirdwayAppalication.bmapkey, new BMapGeneralListener());
		}
		return bmapmanager;
	}

	public SharedPreferences getPres() {
		return pres;
	}

	public void setPres(SharedPreferences pres) {
		this.pres = pres;
	}
}
