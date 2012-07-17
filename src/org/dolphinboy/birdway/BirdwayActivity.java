package org.dolphinboy.birdway;

import org.birdway.mobile.R;
import org.dolphinboy.birdway.service.GPSService;

import android.app.Activity;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class BirdwayActivity extends Activity {
	private Button enterbut = null;
	private Button loginbut = null;
	private Context context = this; 
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        enterbut = (Button) findViewById(R.id.enterbut);
        enterbut.setOnClickListener(enterbutclicklistner);
        
        loginbut = (Button) findViewById(R.id.loginbut);
        loginbut.setOnClickListener(loginbutclicklistner); 
    }
    
    
    @Override
	protected void onStart() {
		super.onStart();
		NotificationManager nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
		nm.cancel(R.layout.main);
	}

	private OnClickListener enterbutclicklistner = new OnClickListener() {
		public void onClick(View v) {
			Intent intentservice = new Intent(context, GPSService.class);
			startService(intentservice);
		}
    };
    private OnClickListener loginbutclicklistner = new OnClickListener() {
		public void onClick(View v) {
			Intent intentservice = new Intent(context, GPSService.class);
			startService(intentservice);
		}
    };
    
    private OnClickListener onclicklistener = new OnClickListener() {
		public void onClick(View v) {
			int id = v.getId();
			switch (id) {
				case R.id.loginbut:
					
					break;
	
				default:
					break;
			}
			//Toast.makeText(BirdwayActivity.this, "click the button!", Toast.LENGTH_LONG).show();
		}
    };
}
