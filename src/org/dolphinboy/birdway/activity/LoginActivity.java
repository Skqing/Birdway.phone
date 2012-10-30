package org.dolphinboy.birdway.activity;

import org.dolphinboy.birdway.R;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

/**
 * @description
 * @author DolphinBoy
 * @date 
 * @time 
 */
public class LoginActivity extends Activity {
	private static final String TAG = "LoginActivity";
	private Button loginbut = null;
	private Button singupbut = null;
	private EditText username = null;
	private EditText password = null;
	private CheckBox savepw = null;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        
        loginbut = (Button) findViewById(R.id.loginbut_id);
        loginbut.setOnClickListener(loginbutclicklistner); 
        
        singupbut = (Button) findViewById(R.id.singup_id);
		singupbut.setOnClickListener(singupbutclicklistner);
		
        username = (EditText) findViewById(R.id.username_id);
        password = (EditText) findViewById(R.id.password_id);
        
        savepw = (CheckBox) findViewById(R.id.savepw_id);
    }
    
    
    @Override
	protected void onPause() {
		super.onPause();
		//注意处理这个方法，也就是其他activity覆盖到当前activity时当前activity该怎么做
		
	}


	@Override
	protected void onStart() {
		super.onStart();
//		NotificationManager nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
//		nm.cancel(R.layout.login);
	}
    
	private OnClickListener loginbutclicklistner = new OnClickListener() {
		public void onClick(View v) {
			//记录用户登录参数，注：密码是要加密的，并且要实现“记住密码登录”的功能
//			SharedPreferences preferences = getSharedPreferences("birdway", Context.MODE_PRIVATE);
//			Editor editor = preferences.edit();
//			Log.i(TAG, "savepw:"+savepw.getText().toString());
//			editor.putString("savepw", savepw.getText().toString());
//			editor.putString("username", username.getText().toString());
//			if (savepw.getText().toString() == "true") {
//				editor.putString("password", password.getText().toString());
//			}
//			editor.commit();
			
			//显示地图窗口
			Intent intent = new Intent(LoginActivity.this, BaiduMapActivity.class);
			startActivity(intent);
			
			//启动后台服务
//			Intent intents = new Intent(context, BirdwayService.class);
//			startService(intents);
		}
    };
    
    private OnClickListener singupbutclicklistner = new OnClickListener() {
		public void onClick(View v) {
			Intent intent = new Intent(LoginActivity.this, SingupActivity.class);
			startActivity(intent);
		}
    };
//    private OnClickListener onClickListener = new OnClickListener() {
//		public void onClick(View v) {
//			int id = v.getId();
//			switch (id) {
//				case R.id.loginbut_id:
//					
//					break;
//				case R.id.singup_id:
//					finish();  //关闭此activity
//					break;
//				default:
//					break;
//			}
//			//Toast.makeText(BirdwayActivity.this, "click the button!", Toast.LENGTH_LONG).show();
//		}
//    };
}
