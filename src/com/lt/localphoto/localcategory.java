package com.lt.localphoto;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.SimpleAdapter;

import com.lt.aircraftwallpaper.R;
import com.twotoasters.jazzylistview.JazzyGridView;

public class localcategory extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.localcategory); 
		JazzyGridView gridview =(JazzyGridView)findViewById(R.id.localcat);
		  //生成动态数组，并且转入数据
        ArrayList<HashMap<String, Object>> lstImageItem = new ArrayList<HashMap<String, Object>>();
        HashMap<String, Object> map = new HashMap<String, Object>(); 
        HashMap<String, Object> map1 = new HashMap<String, Object>(); 
        HashMap<String, Object> map2 = new HashMap<String, Object>(); 
        HashMap<String, Object> map3 = new HashMap<String, Object>(); 
        HashMap<String, Object> map4 = new HashMap<String, Object>(); 
        HashMap<String, Object> map5 = new HashMap<String, Object>(); 
        map.put("localimg", R.drawable.br1);
        map.put("name", "方程式赛");
        map1.put("localimg", R.drawable.br1);
        map1.put("name", "方程式赛");
        map2.put("localimg", R.drawable.br1);
        map2.put("name", "方程式赛");
        map3.put("localimg", R.drawable.br1);
        map3.put("name", "方程式赛");
        map4.put("localimg", R.drawable.br1);
        map4.put("name", "方程式赛");
        map5.put("localimg", R.drawable.br1);
        map5.put("name", "方程式赛");
        lstImageItem.add(map5);
        lstImageItem.add(map4);
        lstImageItem.add(map3);
        lstImageItem.add(map2);
        lstImageItem.add(map1);
        lstImageItem.add(map);
        SimpleAdapter adapter=new SimpleAdapter(localcategory.this, lstImageItem, R.layout.item_pager, new String[]{"localimg","name"}, new int[]{R.id.localItemImage,R.id.localItemText});
        gridview.setAdapter(adapter);
        gridview.setOnItemClickListener(new  OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				switch(position){
				case 0:
					Intent intent=new Intent();
					intent.setClass(localcategory.this, LocalGridActivity.class);
					startActivity(intent);
					break;
				}
				
			}
		});
	}

}
