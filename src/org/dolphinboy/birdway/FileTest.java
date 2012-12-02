package org.dolphinboy.birdway;

import java.util.List;

import org.dolphinboy.birdway.db.service.GpsDataService;
import org.dolphinboy.birdway.entity.GpsData;
import org.dolphinboy.birdway.utils.FileManager;

import android.os.Environment;
import android.test.AndroidTestCase;
import android.util.Log;
import android.widget.Toast;

public class FileTest extends AndroidTestCase {
	private static final String TAG = "DBTest";
	
	public void saveFileToSD() {
		GpsDataService gpsDataService = new GpsDataService(this.getContext());
		List<GpsData> gpsList = gpsDataService.findAll();
		if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
			if (gpsList != null) {
				try {
					FileManager.saveToJSONFile(gpsList);
					Toast.makeText(this.getContext(), "数据成功保存到内存卡!", Toast.LENGTH_LONG).show();
				} catch (Exception e) {
					Toast.makeText(this.getContext(), "保存数据出错!", Toast.LENGTH_LONG).show();
					Log.e(TAG, e.toString());
				}
			} else {
				Toast.makeText(this.getContext(), "暂时还没有收集到数据!", Toast.LENGTH_LONG).show();
			}
		}
	}
}
