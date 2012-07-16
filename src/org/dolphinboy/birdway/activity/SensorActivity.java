package org.dolphinboy.birdway.activity;

import android.app.Activity;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;

public class SensorActivity extends Activity{
	private String orientation;  //方向传感器的值
	private String accelerometer;  //重力传感器的值
	private SensorManager sensorManager;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		//获取传感器管理器
		sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
	}
	@Override
	protected void onPause() {
		sensorManager.unregisterListener(sensorEventListener);
		super.onPause();
	}
	@Override
	protected void onResume() {
		Sensor orientationSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION);
		sensorManager.registerListener(sensorEventListener, orientationSensor, SensorManager.SENSOR_DELAY_NORMAL);
		
		Sensor accelerometerSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
		sensorManager.registerListener(sensorEventListener, accelerometerSensor, SensorManager.SENSOR_DELAY_NORMAL);
		
		super.onResume();
	}
	
	
	private SensorEventListener sensorEventListener = new SensorEventListener() {
		public void onSensorChanged(SensorEvent event) {  //得到传感器实时的数值
			int type = event.sensor.getType();
			switch (type) {
				case Sensor.TYPE_ORIENTATION:
					float x = event.values[SensorManager.DATA_X];
					float y = event.values[SensorManager.DATA_Y];
					float z = event.values[SensorManager.DATA_Z];
					break;
				case Sensor.TYPE_ACCELEROMETER:
				
				default:
					break;
		}
			
			
			
		}
		
		public void onAccuracyChanged(Sensor sensor, int accuracy) {
			
		}
    };
}
