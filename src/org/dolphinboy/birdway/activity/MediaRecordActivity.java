package org.dolphinboy.birdway.activity;

import java.io.File;
import java.io.IOException;

import android.app.Activity;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

public class MediaRecordActivity extends Activity {
	MediaRecorder recorder;
	private File audofile;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
	}

	private OnClickListener onClickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			recorder = new MediaRecorder();
			recorder.setAudioSource(MediaRecorder.AudioSource.DEFAULT);  //声音来源
			recorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);  //输出格式
			recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);  //编码格式
			recorder.setMaxFileSize(10 * 1024);  //设置文件最大字节数
			
			String filename = "";
			audofile = new File(getCacheDir(), filename+".3gp");  //实例化文件
			recorder.setOutputFile("");  //设置缓存文件路径
			
			try {
				recorder.prepare();
			} catch (IllegalStateException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}  //预期准备
			recorder.start();  //开始录制
			recorder.stop();  //暂停录制
			
		}
		
	};

	@Override
	protected void onDestroy() {
		super.onDestroy();
		recorder.release();  //释放
		//清除缓存文件
	}

	
}
