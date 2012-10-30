package org.dolphinboy.birdway.comps;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;

public class ServiceHandler extends Handler {

	public ServiceHandler(Looper looper) {
		super(looper);
	}

	@Override
	public void dispatchMessage(Message msg) {
		super.dispatchMessage(msg);
	}

	@Override
	public void handleMessage(Message msg) {
		
		super.handleMessage(msg);
	}

	@Override
	public boolean sendMessageAtTime(Message msg, long uptimeMillis) {
		return super.sendMessageAtTime(msg, uptimeMillis);
	}
	
}
