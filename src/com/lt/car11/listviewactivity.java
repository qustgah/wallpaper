package com.lt.car11;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;  

import cache.AsyncImageLoader.ImageCallback;
import cache.NetUtil;

import com.google.ads.AdRequest;
import com.google.ads.AdSize;
import com.google.ads.AdView;
import com.twotoasters.jazzylistview.JazzyHelper;
import com.twotoasters.jazzylistview.JazzyListView;
import com.wallpaper.bean.picture;
import com.wallpaper.dao.CommonDao;

import android.app.ListActivity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SimpleAdapter;
import android.widget.AbsListView.OnScrollListener;
import android.widget.SimpleAdapter.ViewBinder;
import android.widget.TextView;
import android.widget.Toast;
public class listviewactivity extends ListActivity {
	protected static final int GETDATA = 1;
	private long exitTime = 0;
	// 网络资源
	List<Map<String, Object>> data;
	private LinearLayout ll_loading;//正在加载模块
	private boolean isScrolling = false;//是否正在滚动
	SimpleAdapter adapter; 
	ArrayList<picture> imagesurls;
	private JazzyListView lv_main_category;
	Intent intent;
	private AdView adView;
	private boolean isloading = false;//判断是否正在加载中
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// 设置全屏显示
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.listview);
		ll_loading = (LinearLayout) this.findViewById(R.id.ll_main_progress);
		lv_main_category = (JazzyListView) this.findViewById(R.id.listcategory);
		lv_main_category.setTransitionEffect(JazzyHelper.FLIP);
		data = new ArrayList<Map<String, Object>>();
		LinearLayout adlayout = (LinearLayout) findViewById(R.id.adlaout3);
		adView = new AdView(this, AdSize.BANNER, "a1536a09095cdce");
		adlayout.addView(adView);
		AdRequest adRequest = new AdRequest();
		adView.loadAd(adRequest);
		getData();
		
		
		lv_main_category.setOnScrollListener(new OnScrollListener() {
			//三种不同状态
			@Override
			//判断是否滑动过，优先级最高
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				switch (scrollState) {
				case OnScrollListener.SCROLL_STATE_FLING:
					isScrolling = true;
					break;
				case OnScrollListener.SCROLL_STATE_IDLE:
					isScrolling = false;
					//获取屏幕上第一个显示的位置
					int startindex = lv_main_category.getFirstVisiblePosition();
					//获取
					int count = lv_main_category.getChildCount();
					for (int i = 0; i < count; i++) {
						int currentpostion = startindex + i;
						final Map<String, Object> book = (Map<String, Object>) lv_main_category.getItemAtPosition(currentpostion);
						final View viewchildren = lv_main_category.getChildAt(i);
						ImageView iv_icon = (ImageView) viewchildren.findViewById(R.id.photo);
						Drawable drawable = NetUtil.asyncImageLoader.loadDrawable((String)book.get("caddr"),
								new ImageCallback() {
									public void imageLoaded(Drawable imageDrawable,
											String imageUrl) {
										ImageView imageViewByTag = (ImageView) lv_main_category
												.findViewWithTag(imageUrl);
										if (imageViewByTag != null) {
											imageViewByTag.setImageDrawable(imageDrawable);
										}
									}
								});
						if (drawable != null) {
							iv_icon.setImageDrawable(drawable);
						} else {
							iv_icon.setImageResource(R.drawable.image_for_empty_url);
						}
					}
					break;
				case OnScrollListener.SCROLL_STATE_TOUCH_SCROLL:
					isScrolling = true;
					break;
				}
			}
			//是否已到最底
			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,int visibleItemCount, int totalItemCount) {
			}
		}); 
		lv_main_category.setOnItemClickListener(new AdapterView.OnItemClickListener() {
					@Override
					public void onItemClick(AdapterView<?> parent, View view,int position, long id) {
						TextView category = (TextView) view.findViewById(R.id.cid);
						// 种类的id
						final String cid = category.getText().toString();
						intent = new Intent(listviewactivity.this,ImageListActivity.class);
						intent.putExtra("cat", cid);
						startActivity(intent);
					}
				});
	} 
	
	private void getData(){
		new AsyncTask<Void, String, List<Map<String, Object>>>() {
			protected List<Map<String, Object>> doInBackground(Void... params) {
				List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
 
				try {
					list = CommonDao.getList(
							getResources().getString(R.string.catURL), null,
							null);
					 String urlString=getResources().getString(R.string.catURL);
					list=CommonDao.getList(urlString, null, null);

				} catch (Exception e) {
					publishProgress("Failed to get the type, please in wifi connection...");
					e.printStackTrace();
				}
				return list;
			}
			protected void onPreExecute() {
				ll_loading.setVisibility(View.VISIBLE);
				super.onPreExecute(); 
			} 
			protected void onPostExecute(List<Map<String, Object>> result) {
				data = result;
				ll_loading.setVisibility(View.GONE);
				adapter = new SimpleAdapter(listviewactivity.this, data,
						R.layout.list_item, new String[] { "cid", "cname",
								"caddr", "cdescrip" }, new int[] { R.id.cid,
								R.id.category, R.id.photo, R.id.describe }){
					@Override
					public Object getItem(int position) {
						 return data.get(position);
					}
				};
				adapter.setViewBinder(new ListViewBinder());
				lv_main_category.setAdapter(adapter);
				super.onPostExecute(result);
			}
			protected void onProgressUpdate(String... values) {
				Toast.makeText(listviewactivity.this, values[0],Toast.LENGTH_SHORT).show();
				super.onProgressUpdate(values);
			}
		}.execute();
	}
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK
				&& event.getAction() == KeyEvent.ACTION_DOWN) {
			if ((System.currentTimeMillis() - exitTime) > 2000) {
				Toast.makeText(getApplicationContext(),
						"Again according to exit the program.",
						Toast.LENGTH_SHORT).show();
				exitTime = System.currentTimeMillis();
			} else {
				finish();
				System.exit(0);
			}
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
	private class ListViewBinder implements ViewBinder {  
        @Override  
        public boolean setViewValue(View view, Object data,  
                String textRepresentation) {  
            if((view instanceof ImageView) && (data instanceof String)) {
                ImageView imageView = (ImageView) view;
                imageView.setTag((String)data);
                if(isloading){
                	imageView.setImageResource(R.drawable.image_for_empty_url); 
                }else{
                	Drawable drawable =NetUtil.asyncImageLoader.loadDrawable((String)data,
    						new ImageCallback() {
    							public void imageLoaded(Drawable imageDrawable,String data) {
    								ImageView imageViewByTag = (ImageView) lv_main_category.findViewWithTag(data);
    								if (imageViewByTag != null) {
    									imageViewByTag.setImageDrawable(imageDrawable);
    								}
    							}
    						});
    				if (drawable != null) {
    					imageView.setImageDrawable(drawable);
    				} else {
    					imageView.setImageResource(R.drawable.image_for_empty_url);
    				}
                }
                return true;  
            }  
            return false;  
        }  
    }

}
