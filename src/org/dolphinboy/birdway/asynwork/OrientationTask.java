package org.dolphinboy.birdway.asynwork;

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
	
	private boolean data_conntected = false;  //是否连接到数据
	private long time_duration = 5000;
	
	private TaskCallBack callBk = null;
	private Context context = null;
	
	private SensorManager sensormanager;
	private Sensor sensor_orientation;
	
	private OrientationHandler orientation_handler = null;
	
	public OrientationTask(Context context, TaskCallBack callBk, long time_out) {
		this.callBk = callBk;
		this.context = context;
		this.time_duration = time_out;
		//初始化方向传感器
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
			
			data_conntected = true;
			Message msg = orientation_handler.obtainMessage();
			msg.what = 0;
			msg.obj = new Object[]{x, y, z};
			orientation_handler.sendMessage(msg);
			
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
