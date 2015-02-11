package com.lt.aircraftwallpaper;

import com.google.ads.AdRequest;
import com.google.ads.AdSize;
import com.google.ads.AdView;
import com.lt.localphoto.LocalGridActivity;
import com.lt.localphoto.bendiActivity;
import com.lt.localphoto.localcategory;
import com.lt.aircraftwallpaper.R;

import android.app.Activity;
import android.app.TabActivity;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TabHost.TabSpec;
import android.widget.TabHost;
import android.widget.TextView;

public class tabhostmainactivity  extends TabActivity implements OnCheckedChangeListener{

	private TabHost tabHost;
	private AdView adView;
	private static final String HOME = "home";
	private static final String HOT = "cat";
	private static final String TJ = "loc";
	private RadioGroup rGroup;
	private TabSpec tabSpec;
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN );
		setContentView(R.layout.tabhost2);
        init();  //初始化view
        setLister(); //绑定事件
	}
	private void setLister() {
		rGroup.setOnCheckedChangeListener(this);
	}

	private void init() {
		rGroup = (RadioGroup) findViewById(R.id.radioground);
		tabHost=this.getTabHost();
		tabSpec=tabHost.newTabSpec(HOME).setIndicator(HOME).setContent(new Intent(this,LocalGridActivity.class));
		tabHost.addTab(tabSpec);
		tabSpec=tabHost.newTabSpec(HOT).setIndicator(HOT).setContent(new Intent(this,listviewactivity.class));
		tabHost.addTab(tabSpec);
		tabSpec=tabHost.newTabSpec(TJ).setIndicator(TJ).setContent(new Intent(this,bendiActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
		tabHost.addTab(tabSpec);
		
	}
	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId) {
		switch(checkedId){
			case R.id.browse:
				tabHost.setCurrentTab(0);
				break;
			case R.id.catg:
				tabHost.setCurrentTab(1);
				break;
			case R.id.loc:
				tabHost.setCurrentTab(2);
				break;
		}
	}
	@Override
	protected void onResume() {
		super.onResume();
	}
}
