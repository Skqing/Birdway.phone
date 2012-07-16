package org.dolphinboy.birdway.service;

import org.birdway.mobile.R;
import org.dolphinboy.birdway.BirdwayActivity;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;

public class BirdwayService extends IntentService {
//	private ServiceHandler handler;
	public BirdwayService() {
		super("BirdwayService");
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		
		shwoNotification();
	}

	private void shwoNotification() {
		Notification notification = new Notification(R.drawable.ic_launcher, "GPS for Birdway……", System.currentTimeMillis());
		Intent intent = new Intent(this, BirdwayActivity.class);
		PendingIntent contentIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
		notification.setLatestEventInfo(this, "BirdwayService", "working……", contentIntent);
		NotificationManager nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
		nm.notify(R.layout.main, notification);
	}
}
