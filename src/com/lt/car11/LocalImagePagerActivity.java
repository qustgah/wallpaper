package com.lt.car11;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream; 
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream; 
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List; 

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.app.WallpaperManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.os.Looper;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.ads.AdRequest;
import com.google.ads.AdSize;
import com.google.ads.AdView;
import com.lt.car11.R;
import com.lt.localphoto.bendiActivity;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.polites.android.GestureImageView;
 
public class LocalImagePagerActivity extends BaseActivity {

	private static ViewPager pager;
	private ProgressDialog mSaveDialog = null; 
	private DisplayImageOptions options; 
	private ImageButton loadbutton;
	private ImageButton setwallbtn;
	private ImageButton leftbutton;
	private ImageButton rightbutton;
	private static ArrayList<String> localpic;
	private String fileName; 
	private static int pagerPosition; 
	private final static String ALBUM_PATH  = Environment.getExternalStorageDirectory() + "/LovePaper/download/";  
	private AdView adView; 
	public static ImagePagerAdapter imagePagerAdapter;
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.local_image_pager);
		//��ϵͳʱ����Ϊ�ļ�����
		Date now =new Date();
		DateFormat time=DateFormat.getDateTimeInstance(DateFormat.FULL, DateFormat.FULL);
		fileName=time.format(now)+".jpg"; 
		Bundle bundle = getIntent().getExtras(); 
		pagerPosition = bundle.getInt(Extra.IMAGE_POSITION, 0);
		ArrayList list = new ArrayList();
		list = (ArrayList) bundle.getSerializable(Extra.IMAGES);
		localpic = (ArrayList<String>) list.get(0);
		pager= new HackyViewPager(this);
		
		  
		LinearLayout adlayout=(LinearLayout)findViewById(R.id.adlaout6);
		
		adView=new AdView(this,AdSize.BANNER,"a1536a09095cdce");
		adlayout.addView(adView);
		AdRequest adRequest=new AdRequest(); 
		adView.loadAd(adRequest);
		
		//��ȡÿ��ͼƬ��position
		
      
		options = new DisplayImageOptions.Builder()
			.showImageForEmptyUri(R.drawable.image_for_empty_url)
			.cacheOnDisc()
			.imageScaleType(ImageScaleType.EXACT)
			.build();
		pager = (ViewPager) findViewById(R.id.paper);
		imagePagerAdapter = new ImagePagerAdapter(localpic);
		pager.setAdapter(imagePagerAdapter);
		pager.setCurrentItem(pagerPosition);
		pager.setOnPageChangeListener(new OnPageChangeListener() {
			
			@Override
			public void onPageSelected(int arg0) {
				pagerPosition = arg0;
			}
			
			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
				
			}
			
			@Override
			public void onPageScrollStateChanged(int arg0) {
				
			}
		});
		
		loadbutton=(ImageButton)findViewById(R.id.deletebtn);
	    setwallbtn=(ImageButton)findViewById(R.id.setwall); 
	    leftbutton=(ImageButton)findViewById(R.id.left); 
	    rightbutton=(ImageButton)findViewById(R.id.right); 
	    //��Ҫ���ü���
	    leftbutton.setOnClickListener(new OnClickListener() {
			@Override
 
			public void onClick(View v) {
			
				pager.arrowScroll(v.FOCUS_LEFT);
					System.out.println("���position��λ�ã�"+pagerPosition);
					pagerPosition=pagerPosition-1;
					if(pagerPosition<=0)
					{
						pagerPosition=0;
						pager.setCurrentItem(0);
					}			 
					else{
				pager.setCurrentItem(pagerPosition,true);}}
	 
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
				pagerPosition++;
				System.out.println("�ұ�position��λ�ã�"+pagerPosition);
				 if(pagerPosition>=0&&pagerPosition<=localpic.size()-1){
					 pager.setCurrentItem(pagerPosition);	
				}
				else if(pagerPosition>localpic.size()-1){
					pagerPosition=localpic.size()-1;
					Toast.makeText(LocalImagePagerActivity.this,"NO pictures", 2000).show();
				} 
			}
		});
	    rightbutton.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				 if(event.getAction() == MotionEvent.ACTION_DOWN){         
	                 v.setBackgroundResource(R.drawable.right1);     
	         }else if(event.getAction() == MotionEvent.ACTION_UP){     
	                 v.setBackgroundResource(R.drawable.right);     
	         }     				return false;
			}
		});
	    loadbutton.setOnClickListener(new OnClickListener() { 
			@Override
			public void onClick(View v) { 
				AlertDialog dialog = new AlertDialog.Builder(LocalImagePagerActivity.this).setTitle("delete picture").setMessage("confirm��")
						.setPositiveButton("YES", new DialogInterface.OnClickListener(){
							@Override
							public void onClick(DialogInterface dialog, int which) {
								File file = new File(localpic.get(pagerPosition));
								file.delete();  
								Intent intent = new Intent(LocalImagePagerActivity.this, bendiActivity.class);
								startActivity(intent);
								finish();
							}
						})
						.setNegativeButton("NO",  new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int which) { 
							}
				}).show();  
			}
		});
	    loadbutton.setOnTouchListener(new OnTouchListener() {
			public boolean onTouch(View v, MotionEvent event) {
				 if(event.getAction() == MotionEvent.ACTION_DOWN){         
	                 v.setBackgroundResource(R.drawable.deleting);     
	         }else if(event.getAction() == MotionEvent.ACTION_UP){     
	                 v.setBackgroundResource(R.drawable.fredelete);     
	         }     
			return false;
			}
		});
     setwallbtn.setOnTouchListener(new OnTouchListener() { 
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
				new Thread(new Runnable(){
	                @Override
	                public void run() {
	                	Looper.prepare();
						try {
							File file = new File(localpic.get(pagerPosition));
							InputStream is = new FileInputStream(file);
							Bitmap map = BitmapFactory.decodeStream(is);
							is.close();
							File dirFile = new File(ALBUM_PATH);
							if (!dirFile.exists()) {
								dirFile.mkdir();
							}
							File myCaptureFile = new File(ALBUM_PATH + fileName);
							BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(myCaptureFile));
							map.compress(Bitmap.CompressFormat.JPEG, 80, bos);
							bos.flush();
							bos.close();
							WallpaperManager wallpaperManager = WallpaperManager.getInstance(LocalImagePagerActivity.this);
							Resources res = LocalImagePagerActivity.this.getResources();
							try {
								wallpaperManager.setBitmap(map);
							}catch (IOException e) {
								e.printStackTrace();
							}
							Toast.makeText(LocalImagePagerActivity.this,
									"Set wallpaper success!", Toast.LENGTH_LONG)
									.show();
						} catch (Exception e) {
							e.printStackTrace();
						}
	                	Looper.loop();
	                }
	            }).start();
			}
		});
	} 
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		return super.onOptionsItemSelected(item);
	}
	private class ImagePagerAdapter extends PagerAdapter {
		private List<String>  images; 
		ImagePagerAdapter(List<String> imgs) { 
			this.images = imgs ;
		}
		@Override
		public void destroyItem(View container, int position, Object object) {
			((ViewPager) container).removeView((View) object);
		} 
		public int getCount() {
			return images.size();
		} 
		
		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			View view = View.inflate(LocalImagePagerActivity.this,R.layout.local_item_pager_image, null);
			final GestureImageView imageView = (GestureImageView) view.findViewById(R.id.image);
			final ProgressBar spinner = (ProgressBar) view.findViewById(R.id.loading); 
			if(images.get(position)==null){
				position = 0;
			}
			File file = new File(images.get(position));
			InputStream is = null;
			try {
				is = new FileInputStream(file);
				Bitmap bitmap = BitmapFactory.decodeStream(is);
				imageView.setImageBitmap(bitmap);
			} catch (Exception e) { 
				System.out.println(e.getMessage().toString());
			} 
			try {
				is.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			((ViewPager) container).addView(view, 0);
			return view;
		}  
		@Override
		public boolean isViewFromObject(View view, Object object) {
			return view.equals(object);
		} 
	} 
}
 
 
 
 
 
 
