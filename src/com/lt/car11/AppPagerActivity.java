package com.lt.car11;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import android.app.WallpaperManager;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Looper;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.ads.AdRequest;
import com.google.ads.AdSize;
import com.google.ads.AdView;
import com.polites.android.GestureImageView;

/**
 * 
 * @author adamin
 * 
 */
public class AppPagerActivity extends BaseActivity {

	private ViewPager pager;
	private ImageButton setwallbtn;
	private ImageButton leftbutton;
	private ImageButton rightbutton;
	private ArrayList<String> imageUrls;
	private int pagerPosition;
	private AdView adView;
	AssetManager assetManager;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.app_image_paper);
		assetManager = getAssets();
		
		LinearLayout adlayout = (LinearLayout) findViewById(R.id.appadlaout6);
		adView = new AdView(this, AdSize.BANNER, "a1536a09095cdce");
		adlayout.addView(adView);
		AdRequest adRequest = new AdRequest();
		adView.loadAd(adRequest);
		
		Bundle bundle = getIntent().getExtras();
		imageUrls=bundle.getStringArrayList("list");
		pagerPosition = bundle.getInt(Extra.IMAGE_POSITION, 0);
		System.out.println("获得position的位置：" + pagerPosition);
		pager = (ViewPager) findViewById(R.id.apppaper);
		pager.setAdapter(new AppImagePagerAdapter(imageUrls));
		
		pager.setCurrentItem(pagerPosition);
		setwallbtn = (ImageButton) findViewById(R.id.appsetwall);
		leftbutton = (ImageButton) findViewById(R.id.appleft);
		rightbutton = (ImageButton) findViewById(R.id.appright);
		// //需要设置监听
		leftbutton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				pager.arrowScroll(v.FOCUS_LEFT);
				System.out.println("左边position的位置：" + pagerPosition);
				pagerPosition = pagerPosition - 1;
				if (pagerPosition <= 0) {
					pagerPosition = 0;
					pager.setCurrentItem(0);
					Toast.makeText(AppPagerActivity.this, "NO pictures", 2000)
							.show();
				} else {
					pager.setCurrentItem(pagerPosition, true);

				}
			}
		});
		leftbutton.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if (event.getAction() == MotionEvent.ACTION_DOWN) {
					v.setBackgroundResource(R.drawable.left1);
				} else if (event.getAction() == MotionEvent.ACTION_UP) {
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
				System.out.println("右边position的位置：" + pagerPosition);
				if (pagerPosition >= 0 && pagerPosition <= imageUrls.size() - 1) {
					pager.setCurrentItem(pagerPosition);
				} else if (pagerPosition > imageUrls.size() - 1) {
					pagerPosition = imageUrls.size() - 1;
					Toast.makeText(AppPagerActivity.this, "NO pictures", 2000)
							.show();
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
		setwallbtn.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if (event.getAction() == MotionEvent.ACTION_DOWN) {
					v.setBackgroundResource(R.drawable.ses1);
				} else if (event.getAction() == MotionEvent.ACTION_UP) {
					v.setBackgroundResource(R.drawable.ses);
				}
				return false;
			}
		});
		setwallbtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				new Thread(new Runnable() {
					@Override
					public void run() {
						Looper.prepare(); 
						InputStream is=null;
						try {
							is=assetManager.open(imageUrls.get(pagerPosition));
							Bitmap bitmap=BitmapFactory.decodeStream(is);
							is.close();
							WallpaperManager wallpaperManager = WallpaperManager.getInstance(AppPagerActivity.this);
							wallpaperManager.setBitmap(bitmap);
							Toast.makeText(AppPagerActivity.this, "set success!", Toast.LENGTH_SHORT).show();
						} catch (IOException e) {
							// TODO Auto-generated catch block
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

	private class AppImagePagerAdapter extends PagerAdapter {
		private ArrayList<String> images; 
		public AppImagePagerAdapter(ArrayList<String> images) {
			this.images = images;
		}
		@Override
		public int getCount() {
			return images.size();
		}
		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0.equals(arg1);
		}
		@Override
		public Object instantiateItem(View container, int position) {
			View view = View.inflate(AppPagerActivity.this,R.layout.app_paper_item, null);
			final GestureImageView imageView = (GestureImageView) view.findViewById(R.id.appimage);
			if (images.get(position) == null) {
				position = 0;
			}
			InputStream is = null;
			try {
				is = assetManager.open(images.get(position));
				Bitmap bitmap = BitmapFactory.decodeStream(is);
				imageView.setImageBitmap(bitmap);
				is.close();
				
			} catch ( Exception e) {
				e.printStackTrace();
			} 
			
			((ViewPager) container).addView(view, 0);
			return view;
		}
		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			((ViewPager) container).removeView((View) object);
		}
	}
}
