package org.dolphinboy.birdway.activity;

import org.dolphinboy.birdway.R;
import org.dolphinboy.birdway.entity.GpsData;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;

public class SensorInforActivity extends Activity {
	private GPSReceiver gpsreceiver;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.sensor);
		
		gpsreceiver = new GPSReceiver();  
        IntentFilter filter = new IntentFilter();  
        filter.addAction("birdway.gpsreceiver");  
        //注册  
        registerReceiver(gpsreceiver, filter); 
        
        
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		unregisterReceiver(gpsreceiver);  
	}

	@Override
	protected void onStart() {
		super.onStart();
		
		
	}
	
	private class GPSReceiver extends BroadcastReceiver {  
        @Override  
        public void onReceive(Context context, Intent intent) {  
            Bundle bundle = intent.getExtras();  
            GpsData gpsdata = (GpsData) bundle.get("gpsdata");  
        }  
    }
}
