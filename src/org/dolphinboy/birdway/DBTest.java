package org.dolphinboy.birdway;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.dolphinboy.birdway.db.service.GpsDataService;
import org.dolphinboy.birdway.entity.GpsData;

import android.test.AndroidTestCase;
import android.util.Log;

public class DBTest extends AndroidTestCase {
	private static final String TAG = "DBTest";
	
	public void testSave() throws Exception {
		GpsDataService gpsDataService = new GpsDataService(this.getContext());
		GpsData gpsdata = new GpsData();
		gpsdata.setLatitude(23.51397736);
		gpsdata.setLongitude(114.05417705);
		gpsdata.setAltitude(73.5999984741211);
		gpsdata.setAccuracy(10.232f);
		gpsdata.setBear(10.4f);
		gpsdata.setSpeed(3.3f);
		gpsdata.setGpstime(12232422l);
		gpsdata.setRecordtime((new Date()).getTime());
		gpsdata.setProvider(0);
		gpsdata.setState(0);
		
		gpsDataService.save(gpsdata);
	}
	
	public void testfindAll() throws Exception {
		GpsDataService gpsDataService = new GpsDataService(this.getContext());
		List<GpsData> gpsdatalist = gpsDataService.findAll();
		if (gpsdatalist != null && gpsdatalist.size() > 0) {
			for (int i=0; i<gpsdatalist.size(); i++) {
				GpsData gpsdata = gpsdatalist.get(i);
				Log.i(TAG, "UUID："+UUID.randomUUID());
				Log.i(TAG, "ID："+gpsdata.getId());
				Log.i(TAG, "经度："+gpsdata.getLatitude());
				Log.i(TAG, "纬度："+gpsdata.getLongitude());
			}
		}
	}
}
