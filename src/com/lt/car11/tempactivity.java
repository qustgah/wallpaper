package com.lt.car11;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class tempactivity extends Activity {
Button btn=null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.temp);
		Button btn=(Button)findViewById(R.id.tempbtn);
	    btn.setOnClickListener(new Button.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent =new Intent(tempactivity.this,tabhostmainactivity.class);
				startActivity(intent);
			}
		});
		
	}
	

}
