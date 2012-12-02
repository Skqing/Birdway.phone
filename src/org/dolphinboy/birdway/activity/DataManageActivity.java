package org.dolphinboy.birdway.activity;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.dolphinboy.birdway.R;
import org.dolphinboy.birdway.db.service.GpsDataService;
import org.dolphinboy.birdway.entity.GpsData;
import org.dolphinboy.birdway.utils.FileManager;

import android.app.Activity;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class DataManageActivity extends Activity {
	private static final String TAG = "DataManageActivity";
	private static final int ONCE_NUM = 1000; //每次发送多少条数据
	private GpsDataService gpsDataService = null;
	private TextView allVolTextView = null;
	
	private Button emptyBut = null;
	private Button exportBut = null;
	private Button syncBut = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.datamanage);
		
		allVolTextView = (TextView) findViewById(R.id.dav_tv_id);
		
		emptyBut = (Button) findViewById(R.id.dm_empty_id);
		exportBut = (Button) findViewById(R.id.dm_export_id);
		exportBut.setOnClickListener(exportButClickListener);
		syncBut = (Button) findViewById(R.id.dm_sync_id);
		syncBut.setOnClickListener(syncButClickListener);
		gpsDataService = new GpsDataService(DataManageActivity.this);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

	@Override
	protected void onStart() {
		super.onStart();
		Long allvol = gpsDataService.getCount(null);
		String allvalue = allVolTextView.getText() + ":"+ allvol.toString();
		allVolTextView.setText(allvalue);
	}
	
	private OnClickListener exportButClickListener = new OnClickListener() {
		public void onClick(View v) {
			List<GpsData> gpsList = gpsDataService.findAll();
			if (gpsList != null) {
			//判断sdcard是否存在于手机上，并且可以进行读写访问
				if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
					try {
						FileManager.saveToJSONFile(gpsList);
						Toast.makeText(DataManageActivity.this, "数据成功保存到内存卡!", Toast.LENGTH_LONG).show();
					} catch (Exception e) {
						Toast.makeText(DataManageActivity.this, "保存数据出错!", Toast.LENGTH_LONG).show();
						Log.e(TAG, e.toString());
					}
				} else {
						Toast.makeText(DataManageActivity.this, "内存卡不存在，请先安装内存卡", Toast.LENGTH_LONG).show();
//					Log.i(TAG, "内存卡不存在,将保存到手机内存中!");
//					try {
//						FileManager.saveToJSONFile(gpsList, DataManageActivity.this);
//						Toast.makeText(DataManageActivity.this, "数据成功保存到手机内存!", Toast.LENGTH_LONG).show();
//					} catch (Exception e) {
//						Toast.makeText(DataManageActivity.this, "保存到手机内存出错!", Toast.LENGTH_LONG).show();
//						Log.e(TAG, e.toString());
//					}
				}
			} else {
				Toast.makeText(DataManageActivity.this, "暂时还没有收集到数据!", Toast.LENGTH_LONG).show();
			}
		}
    };
    
    private OnClickListener syncButClickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			
		}
    	
    };
    
    
	public void send() {  //用线程呢，还是用循环呢？
		Map<String, String[]> senddata = new HashMap<String, String[]>();
		
		long count = gpsDataService.getCount(GpsDataService.DATA_STATE_NOSENT);
		
		//还是用多线程吧，先查询出一共又多少条数据，比如N条
		//规定每次发送1000条
		//需要开的线程数为：T=N/1000，有余数则多加一个线程
		//每个线程负责确定的起始位置的数据
		//如果超过1万条数据也就意味着要开10个以上的线程，这样负载太大，要分为多次
		
		while (count>100) {
			String[] data = gpsDataService.getSending(1, 1);
			String uuid = UUID.randomUUID().toString();
			senddata.put(uuid, data);
			
		}
		count -= 1000;
		long thrsum = (count%ONCE_NUM==0)?(count/ONCE_NUM):(count/ONCE_NUM+1);
		for (int i=0; i<thrsum; i++) {
		}
		
		//线程开始
		class sendThread extends Thread {  //如果用多线程，网络层就不能用静态变量和方法了
			public void run() {
				
		    }
		}
	}
}