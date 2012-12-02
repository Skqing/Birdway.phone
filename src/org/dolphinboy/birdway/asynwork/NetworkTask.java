package org.dolphinboy.birdway.asynwork;

import android.app.Activity;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;

public class NetworkTask extends AsyncTask {
private static final String TAG = "NetworkTask";
	
	private TaskCallBack callBk = null;
	private Activity context = null;
	
	private SensorManager sensormanager;
	private Sensor sensor_orientation;
	private boolean TIME_OUT = false;  //是否超时
	private boolean DATA_CONNTECTED = false;  //是否连接到数据
	private long TIME_DURATION = 5000;
	
	private NetworkTask network_handler = null;
	
	public NetworkTask(Activity context, TaskCallBack callBk, long time_out) {
		this.callBk = callBk;
		this.context = context;
		this.TIME_DURATION = time_out;
	}
	@Override
	protected Object doInBackground(Object... params) {
		
		return null;
	}
	private class NetworkHandler extends Handler {
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
