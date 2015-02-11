package com.lt.aircraftwallpaper;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.zip.Inflater;

import uk.co.senab.photoview.PhotoView;
import android.R.bool;
import android.app.ProgressDialog;
import android.app.WallpaperManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Looper;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.webkit.DateSorter;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;
import cache.NetUtil;
import cache.AsyncImageLoader.ImageCallback;

import com.google.ads.AdRequest;
import com.google.ads.AdSize;
import com.google.ads.AdView;
import com.lt.aircraftwallpaper.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageLoadingListener;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.polites.android.GestureImageView;
import com.wallpaper.bean.picture;
import com.wallpaper.dao.CommonDao;
 
public class ImagePagerActivity extends BaseActivity {

	private ViewPager pager;
	private ProgressDialog mSaveDialog = null; 
	private DisplayImageOptions options; 
	private ImageButton loadbutton;
	private ImageButton setwallbtn;
	private ImageButton leftbutton;
	private ImageButton rightbutton;
	private List<picture> imageUrls ;
	private String fileName;  
	private int pagerPosition; 
	private final static String ALBUM_PATH  = Environment.getExternalStorageDirectory() + "/LovePaper/download/";  
	private AdView adView;  
	private boolean isloading = false;//判断是否加载中
	public ImagePagerAdapter ip;
	String page ;
	String cid ;
	private boolean ishas = true;
	boolean picloading = false;
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//将系统时间作为文件名字  
		final DateFormat time=DateFormat.getDateTimeInstance(DateFormat.FULL, DateFormat.FULL);
		pager= new HackyViewPager(this);
		setContentView(R.layout.ac_image_pager);  
		LinearLayout adlayout=(LinearLayout)findViewById(R.id.adlaout6);
		adView=new AdView(this,AdSize.BANNER,"a1536a09095cdce");
		adlayout.addView(adView);
		AdRequest adRequest=new AdRequest();

		adView.loadAd(adRequest);
		Bundle bundle = getIntent().getExtras();
		imageUrls =getIntent().getParcelableArrayListExtra(Extra.IMAGES);
		 cid = getIntent().getStringExtra("cid");
		 page = getIntent().getStringExtra("page");
		//获取每个图片的position
		pagerPosition = bundle.getInt(Extra.IMAGE_POSITION, 0);
		System.out.println("获得position的位置："+pagerPosition);
		options = new DisplayImageOptions.Builder()
		.showImageForEmptyUri(R.drawable.image_for_empty_url)
		.cacheOnDisc()
		.imageScaleType(ImageScaleType.EXACT)
		.build();
		pager = (ViewPager) findViewById(R.id.paper);
		ip = new ImagePagerAdapter(imageUrls);
		pager.setAdapter(ip);
		pager.setCurrentItem(pagerPosition);
		pager.setOnPageChangeListener(new OnPageChangeListener() {
			
			public void onPageSelected(int arg0) { 
				pagerPosition = arg0;
			}
			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
			}
			public void onPageScrollStateChanged(int arg0) {
				
				if(pagerPosition == imageUrls.size() -1&&arg0==1){
					getMoreData();
				}
			}
		});
		
		loadbutton=(ImageButton)findViewById(R.id.downloadbtn);
	    setwallbtn=(ImageButton)findViewById(R.id.setwall); 
	    leftbutton=(ImageButton)findViewById(R.id.left); 
	    rightbutton=(ImageButton)findViewById(R.id.right); 
	    //需要设置监听
	    leftbutton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				pager.arrowScroll(v.FOCUS_LEFT);
					System.out.println("左边position的位置："+pagerPosition);
					pagerPosition=pagerPosition-1;
					if(pagerPosition<=0)
					{
						pagerPosition=0;
						pager.setCurrentItem(0);
					}			 
					else{
						pager.setCurrentItem(pagerPosition,true);
					}
			}
		});
	    leftbutton.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				 if(event.getAction() == MotionEvent.ACTION_DOWN){         
	                 v.setBackgroundResource(R.drawable.left1);     
	         }else if(event.getAction() == MotionEvent.ACTION_UP){ 
	        	 v.setBackgroundResource(R.drawable.left);     
	         }     
			return false;
			}
		});
	    rightbutton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
 
				pager.arrowScroll(v.FOCUS_RIGHT);
				
				//pager.setCurrentItem(pagerPosition-1,true);
				pagerPosition++;
				System.out.println("右边position的位置："+pagerPosition);
				 if(pagerPosition>=0&&pagerPosition<=imageUrls.size()-1){
					 pager.setCurrentItem(pagerPosition);	
				}
				else if(pagerPosition>imageUrls.size()-1){
					getMoreData();
				} 
			}
		});
	    rightbutton.setOnTouchListener(new OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if (event.getAction() == MotionEvent.ACTION_DOWN) {
					v.setBackgroundResource(R.drawable.right1);
				} else if (event.getAction() == MotionEvent.ACTION_UP) {
					v.setBackgroundResource(R.drawable.right);
				}
				return false;
			}
		});
	    loadbutton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
//				Toast.makeText(ImagePagerActivity.this, "loading..", 2000).show();
				mSaveDialog = ProgressDialog.show(ImagePagerActivity.this, "loading", "loading，wait...", true);
			   new Thread(new Runnable() {
				@Override
				public void run() {
					Looper.prepare();
                	try {
                		picloading = true;
                		String addr ;
            			if(imageUrls.get(pagerPosition)==null){
            				addr = "";
            			}else{
            				addr=imageUrls.get(pagerPosition).getPaddr();
            			}
        				URL	url = new URL(addr);
        					HttpURLConnection conn = (HttpURLConnection)url.openConnection();
        	                conn.connect();
                            conn.setConnectTimeout(5000);
        	                InputStream in = conn.getInputStream();
        	                Bitmap map = BitmapFactory.decodeStream(in);
        	                File dirFile = new File(ALBUM_PATH);  
        	                if(!dirFile.exists()){  
        	                    dirFile.mkdir();  
        	                }
        	                Date now =new Date(); 
        	        		fileName=time.format(now)+".jpg";
        	                File myCaptureFile = new File(ALBUM_PATH + fileName);  
        	                Toast.makeText(ImagePagerActivity.this, "load success!", Toast.LENGTH_LONG).show();
        	                mSaveDialog.dismiss();
        	                BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(myCaptureFile));  
        	                map.compress(Bitmap.CompressFormat.JPEG, 80, bos);  
        	                bos.flush();  
        	                bos.close();  
        	                }
                	catch (MalformedURLException e) {
    					e.printStackTrace();
    				} catch (IOException e) {
    					e.printStackTrace(); 
    				}
                	Looper.loop();
                	
                	} 
			}).start();
		}
		});
	    loadbutton.setOnTouchListener(new OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				 if(event.getAction() == MotionEvent.ACTION_DOWN){         
	                 v.setBackgroundResource(R.drawable.ded1);     
	         }else if(event.getAction() == MotionEvent.ACTION_UP){     
	                 v.setBackgroundResource(R.drawable.ded);     
	         }     
				return false;
			}
		});
     setwallbtn.setOnTouchListener(new OnTouchListener() {
		
		@Override
		public boolean onTouch(View v, MotionEvent event) {
			 if(event.getAction() == MotionEvent.ACTION_DOWN){     
                 v.setBackgroundResource(R.drawable.ses1);     
         }else if(event.getAction() == MotionEvent.ACTION_UP){     
                 v.setBackgroundResource(R.drawable.ses);     
         }     
         return false;     
		}
	});
	 setwallbtn.setOnClickListener(new OnClickListener() {  
			@Override
			public void onClick(View v) {
				mSaveDialog = ProgressDialog.show(ImagePagerActivity.this, "loading", "loading image...", true);
				new Thread(new Runnable(){
	                @Override
	                public void run() {
	                	Looper.prepare();
	                	try {
	                		String addr ;
	            			if(imageUrls.get(pagerPosition)==null){
	            				addr = "";
	            			}else{
	            				addr=imageUrls.get(pagerPosition).getPaddr();
	            			}
	        				URL	url = new URL(addr);
	        					HttpURLConnection conn = (HttpURLConnection)url.openConnection();
	        	                conn.connect(); 
	        	                InputStream in = conn.getInputStream();
	        	                Bitmap map = BitmapFactory.decodeStream(in);
	        	                File dirFile = new File(ALBUM_PATH);  
	        	                if(!dirFile.exists()){  
	        	                    dirFile.mkdir();  
	        	                }
	        	                Date now =new Date(); 
	        	        		fileName=time.format(now)+".jpg";
	        	                File myCaptureFile = new File(ALBUM_PATH + fileName);  
	        	                Toast.makeText(ImagePagerActivity.this, "load success!", Toast.LENGTH_LONG).show();
	        	                mSaveDialog.dismiss(); 
	        	                BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(myCaptureFile));  
	        	                map.compress(Bitmap.CompressFormat.JPEG, 80, bos);  
	        	                bos.flush();  
	        	                bos.close();  
	        	                WallpaperManager wallpaperManager = WallpaperManager.getInstance(ImagePagerActivity.this);
	        	                		Resources res = ImagePagerActivity.this.getResources();
	        	                		try {
	        	                		wallpaperManager.setBitmap(map);
	        	                		} catch (IOException e) {
	        	                		e.printStackTrace();
	        	                		}
	        	               Toast.makeText(ImagePagerActivity.this, "set success!", Toast.LENGTH_LONG).show();
	        				}catch (Exception e) {
	        					e.printStackTrace(); 
	        				}
	                	Looper.loop();
	                }
	            }).start();
			}
		});
	} 
	public void getMoreData(){ 
			if(isloading){
				Toast.makeText(ImagePagerActivity.this, "loading.....", Toast.LENGTH_SHORT);
				return ;
			}
			if(!ishas){
				Toast.makeText(ImagePagerActivity.this, "NO PICTRUES", Toast.LENGTH_SHORT);
				return;
			}
			else{
				new AsyncTask<Void, String, List<Map<String, Object>>>() {
					protected List<Map<String, Object>> doInBackground(Void... params) {
						List<Map<String, Object>> list = null;
						try { 
							list = CommonDao.getList(getResources().getString(R.string.picURL),page,cid);
						} catch (Exception e) {
							e.printStackTrace();
						}
						return list;
					}
					protected void onPreExecute() { 
						isloading = true;
						Toast.makeText(ImagePagerActivity.this,"loading....", Toast.LENGTH_SHORT).show();
						super.onPreExecute();
					}
					protected void onPostExecute(List<Map<String, Object>> result) {
						List<Map<String, Object>> picurls = new ArrayList<Map<String,Object>>();
						picurls = result; 
						for (Map<String, Object> map : picurls) {
							picture pic = new picture();
							pic.setPid(String.valueOf(map.get("pid")));
							pic.setPaddr(String.valueOf(map.get("paddr")));
							pic.setCid(String.valueOf(map.get("cid")));
							pic.setPname(String.valueOf(map.get("pname"))); 
							imageUrls.add(pic);
						}
						ip.notifyDataSetChanged();
						page = String.valueOf((Integer.valueOf(page)+1));
						isloading = false;
						if(result.size()<10||result==null){
							ishas = false;
						} 
						super.onPostExecute(result);
					} 
					protected void onProgressUpdate(String... values) {
						Toast.makeText(ImagePagerActivity.this, values[0], Toast.LENGTH_SHORT).show();
						super.onProgressUpdate(values);
					}
				}.execute();
			}  
	}
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		return super.onOptionsItemSelected(item);
	}
	private class ImagePagerAdapter extends PagerAdapter {

		private List<picture>  images; 
		ImagePagerAdapter(List<picture> images) {
			this.images = images; 
		}
		@Override
		public void destroyItem(View container, int position, Object object) {
			((ViewPager) container).removeView((View) object);
		} 
		@Override
		public int getCount() {
			return images.size();
		} 
		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			View view = View.inflate(ImagePagerActivity.this, R.layout.item_pager_image, null);
			final GestureImageView imageView = (GestureImageView) view.findViewById(R.id.image);
			final ProgressBar spinner = (ProgressBar) view.findViewById(R.id.loading); 
			imageLoader.init(ImageLoaderConfiguration.createDefault(ImagePagerActivity.this));
			String addr ;
			if(images.get(position)==null){
				position = 1;
			} 
			addr=images.get(position).getPaddr(); 
			imageLoader.displayImage(addr, imageView, options, new ImageLoadingListener() {
				@Override
		  		public void onLoadingStarted() {
					spinner.setVisibility(View.VISIBLE);
				} 
				@Override
				public void onLoadingFailed(FailReason failReason) {
					String message = null;
					switch (failReason) {
						case IO_ERROR:
							message = "Input/Output error";
							break;
						case OUT_OF_MEMORY:
							message = "Out Of Memory error";
							break;
						case UNKNOWN:
							message = "Unknown error";
							break;
					}
					Toast.makeText(ImagePagerActivity.this, message, Toast.LENGTH_SHORT).show();

					spinner.setVisibility(View.GONE);
					imageView.setImageResource(android.R.drawable.ic_delete);
				} 
				@Override
				public void onLoadingComplete() {
					spinner.setVisibility(View.GONE);
					Animation anim = AnimationUtils.loadAnimation(ImagePagerActivity.this, R.anim.fade_in);
					imageView.setAnimation(anim);
					anim.start();
				}
				@Override
				public void onLoadingCancelled() {
				}
			});
			((ViewPager) container).addView(view, 0);
			return view;
		}  
		@Override
		public boolean isViewFromObject(View view, Object object) {
			return view.equals(object);
		}  
	} 
}
 
 
 
 
 
 
