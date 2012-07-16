package org.dolphinboy.birdway.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class BirdwayReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {

		System.out.println("小子，你又邪恶了，偷拍被我逮到了吧！");
		//准备接收拍照事件，设置是否给 照片记录GPS信息，或手动记录并自动或手动上传
		
	}

}
