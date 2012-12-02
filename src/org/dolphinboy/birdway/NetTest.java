package org.dolphinboy.birdway;

import java.util.Date;

import org.dolphinboy.birdway.entity.GpsData;
import org.dolphinboy.birdway.net.HttpClientService;

import android.test.AndroidTestCase;

public class NetTest extends AndroidTestCase {
	private static final String TAG = "NetTest";
	
	public void upload() throws Exception {
		GpsData gpsdata = new GpsData();
		gpsdata.setId(934);
		gpsdata.setLongitude(114.05417705);
		gpsdata.setLatitude(22.51397736);
		gpsdata.setAltitude(73.5999984741211);
		gpsdata.setBear(32.3f);
		gpsdata.setSpeed(10.23f);
		gpsdata.setGpstime((new Date()).getTime());
		gpsdata.setRecordtime((new Date()).getTime());
		gpsdata.setProvider(0);
		gpsdata.setState(1);
		
		String token = "504ca79689fdda581f000001";
		HttpClientService.smartSendByGet(token, gpsdata);
	}
	
}