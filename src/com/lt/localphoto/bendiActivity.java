package com.lt.localphoto;

import java.io.File;
import java.io.FileInputStream; 
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import com.lt.aircraftwallpaper.Extra;
import com.lt.aircraftwallpaper.LocalImagePagerActivity;
import com.lt.aircraftwallpaper.R;

import android.app.Activity; 
import android.content.Intent; 
import android.graphics.Bitmap;
import android.graphics.BitmapFactory; 
import android.os.Bundle;
import android.os.Environment; 
import android.view.View; 
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout; 

import com.google.ads.AdRequest;
import com.google.ads.AdSize;
import com.google.ads.AdView; 
import com.polites.android.GestureImageView;

public class bendiActivity extends Activity {

	public  static int RESULT_LOAD_IMAGE = 1;
	private AdView adView;
	public  static GridView gridview;
	private String localpicturepath  = Environment.getExternalStorageDirectory() + "/LovePaper/download/";
	public  static List<String> piclist ;
	public static myGridAdapter myAdapter;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//设置无标题
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.bendishow);
		//添加广告条
		LinearLayout adlayout=(LinearLayout)findViewById(R.id.adlaout4);
		adView=new AdView(this,AdSize.BANNER,"a1536a09095cdce");
		adlayout.addView(adView);
		AdRequest adRequest=new AdRequest(); 
		adView.loadAd(adRequest);  
		
		gridview = (GridView) findViewById(R.id.localpicture);
		piclist = getData(localpicturepath);
		myAdapter = new myGridAdapter(piclist);
		gridview.setAdapter(myAdapter); 
		gridview.setOnItemClickListener(new OnItemClickListener() { 
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {  
				Intent intent = new Intent(bendiActivity.this,LocalImagePagerActivity.class); 
				ArrayList list = new ArrayList();
				list.add(piclist);
				intent.putExtra(Extra.IMAGES, list);
				intent.putExtra(Extra.IMAGE_POSITION, position);
				startActivity(intent);
//				finish();
			}
		}); 
	}
	@Override
	protected void onResume() {
		super.onResume();
	}
	
	@Override
	public void finish() { 
		super.finish();
	} 
	public static List<String> getData(String path){
		List <String> list = new ArrayList<String>();
		File filedir = new File(path);
		if(!filedir.exists()){
			filedir.mkdirs();
		}
		File [] files = filedir.listFiles();
		for(File file : files){
			 try {
				 String path1 = file.getPath();
				 String path2 = file.getAbsolutePath(); 
				 File file2 = new File(path1); 
				list.add(path1); 
			}catch ( Exception e) { 
				System.out.println(e.getMessage().toString());
			} 
		}
		return list;
	}
	public class myGridAdapter extends BaseAdapter{
		List<String> mylist ;
		public myGridAdapter(List<String> list) { 
			this.mylist=list;
		}
		@Override
		public int getCount() { 
			return mylist.size();
		}
		@Override
		public Object getItem(int position) {
			return position;
		}
		@Override
		public long getItemId(int position) {
			return position;
		}
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View view;
			ImageView imageView = null;
			if(convertView==null){
				view = getLayoutInflater().inflate(R.layout.item_list_image, null);
				imageView = (ImageView) view.findViewById(R.id.image);
			}else{
				view = convertView;
				imageView = (ImageView) view.findViewById(R.id.image);
			} 
			File file = new File(mylist.get(position));
			InputStream is;
			try {
				is = new FileInputStream(file);
				Bitmap bitmap = BitmapFactory.decodeStream(is);
				imageView.setImageBitmap(bitmap);
				is.close();
			} catch (Exception e){ 
				System.out.println(e.getMessage().toString());
			} 
			return view;
		} 
	}
	 
}
