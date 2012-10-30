package org.dolphinboy.birdway.task;

import android.app.Activity;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

public class OrientationTask extends AsyncTask {
	private static final String TAG = "OrientationTask";
	
	private TaskCallBack callBk = null;
	private Activity context = null;
	
	private SensorManager sensormanager;
	private Sensor sensor_orientation;
	private boolean TIME_OUT = false;  //是否超时
	private boolean DATA_CONNTECTED = false;  //是否连接到数据
	private long TIME_DURATION = 5000;
	
	private OrientationHandler orientation_handler = null;
	
	public OrientationTask(Activity context, TaskCallBack callBk, long time_out) {
		this.callBk = callBk;
		this.context = context;
		this.TIME_DURATION = time_out;
		InitOrientation();
	}
	private void InitOrientation() {
		sensormanager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
		sensor_orientation = sensormanager.getDefaultSensor(Sensor.TYPE_ORIENTATION);
		sensormanager.registerListener(sensorListener, sensor_orientation, SensorManager.SENSOR_DELAY_FASTEST);
	}
	
	@Override
	protected Object doInBackground(Object... params) {
		
		return null;
	}
	
	/**
	 * 传感器监听器
	 */
	private SensorEventListener sensorListener = new SensorEventListener() {
		public void onSensorChanged(SensorEvent event) {
			Log.i(TAG, "onSensorChanged event!");
			float x = event.values[SensorManager.DATA_X];
			float y = event.values[SensorManager.DATA_Y];
			float z = event.values[SensorManager.DATA_Z];
			//计算值数据得到描述性方向信息
			
			DATA_CONNTECTED = true;
			Message msg = orientation_handler.obtainMessage();
			msg.what = 0;
			msg.obj = event;
			orientation_handler.sendMessage(msg);
			
//			str_orientation = x;
			Log.i(TAG, "x="+x+","+"y="+y+","+"z="+z);
		}
		
		public void onAccuracyChanged(Sensor sensor, int accuracy) {
			
		}
	};
	
	private class OrientationHandler extends Handler {
		@Override
		public void handleMessage(Message msg) {
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
}
