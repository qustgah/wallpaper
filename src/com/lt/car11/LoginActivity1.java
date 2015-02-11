package com.lt.car11;

import com.lt.car11.R; 
import com.wallpaper.updatemanager.UpdateManager;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
public class LoginActivity1 extends Activity{
	//延时两秒
	private final int SPLASH_DISPLAY_LENGTH=5000;
	private ImageButton  imgbtn=null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//设置无标题
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		//设置全屏显示
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN );
		//关联布局文件
		setContentView(R.layout.login1);
		UpdateManager um = new UpdateManager(LoginActivity1.this);
		um.checkUpdate();
		
		  
//		new CountDownTimer(2000,1000){
//			@Override
//			public void onTick(long millisUntilFinished) {
//			}
//			@Override
//			public void onFinish() {
//				Intent intent = new Intent();
//				intent.setClass(LoginActivity1.this, tabhostmainactivity.class);
//				startActivity(intent);
//				int VERSION=Integer.parseInt(android.os.Build.VERSION.SDK);
//				if(VERSION >= 5){
//					LoginActivity1.this.overridePendingTransition(R.anim.alpha_in, R.anim.alpha_out);
//				}
//				finish();
//				}
//			}.start();  
	}
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		return super.onKeyDown(keyCode, event);

	}
	



}