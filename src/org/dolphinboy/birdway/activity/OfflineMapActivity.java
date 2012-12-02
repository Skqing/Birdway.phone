package org.dolphinboy.birdway.activity;

import org.dolphinboy.birdway.comps.BirdwayAppalication;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

import com.baidu.mapapi.MKOLUpdateElement;
import com.baidu.mapapi.MKOfflineMap;
import com.baidu.mapapi.MKOfflineMapListener;

public class OfflineMapActivity extends Activity implements MKOfflineMapListener {
	private static final String TAG = "OfflineMapActivity";
	//百度地图管理器
	
	//离线地图管理器
	private MKOfflineMap mkoffine = null;
	
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//初始化百度地图管理器
		BirdwayAppalication app = (BirdwayAppalication)this.getApplication();
		
		//不知道离线地图的初始化放在哪里好
		mkoffine = new MKOfflineMap();
		mkoffine.init(app.getBmapmanager(), new MKOfflineMapListener() {
			@Override
			public void onGetOfflineMapState(int type, int state) {
				switch (type) {
				case MKOfflineMap.TYPE_DOWNLOAD_UPDATE:
					MKOLUpdateElement update = mkoffine.getUpdateInfo(state);
					break;
			    case MKOfflineMap.TYPE_NEW_OFFLINE:
			          Log.d(TAG, String.format("add offlinemap num:%d", state));
			          break;
			    case MKOfflineMap.TYPE_VER_UPDATE:
			    	  Log.d(TAG, String.format("new offlinemap ver"));
			    	  break;
				}
			}
		});
		int scansum = mkoffine.scan();
		
	}

	@Override
	public void onGetOfflineMapState(int arg0, int arg1) {
		
	}

}
