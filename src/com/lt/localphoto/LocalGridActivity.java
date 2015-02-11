package com.lt.localphoto;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.google.ads.AdRequest;
import com.google.ads.AdSize;
import com.google.ads.AdView;
import com.lt.aircraftwallpaper.AppPagerActivity;
import com.lt.aircraftwallpaper.BaseActivity;
import com.lt.aircraftwallpaper.Extra;
import com.lt.aircraftwallpaper.R;
import com.twotoasters.jazzylistview.JazzyGridView;
import com.twotoasters.jazzylistview.JazzyHelper;

/**
 * 
 * @author adamin
 * 
 */
public class LocalGridActivity extends BaseActivity {
	private ArrayList<String> lists;
	AssetManager assetManager;
	private AdView adView;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.localgrid);
		JazzyGridView gridview = (JazzyGridView) findViewById(R.id.gridv);
		assetManager = getAssets();
		LinearLayout adlayout = (LinearLayout) findViewById(R.id.adlaout7);
		adView = new AdView(this, AdSize.BANNER, "a1536a09095cdce");
		adlayout.addView(adView);
		AdRequest adRequest = new AdRequest();
		adView.loadAd(adRequest);
		try {
			lists = (ArrayList<String>) getdata();
		} catch (IOException e) {
			e.printStackTrace();
		}
		gridview.setTransitionEffect(JazzyHelper.FLIP);
		gridview.setAdapter(new ItemAdapter(lists));
		gridview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				startImageGalleryActivity(position);
			}
		});

	}

	private List<String> getdata() throws IOException {
		String dirPath = "im";
		String photoName = null;
		List<String> lists = new ArrayList<String>();

		// 使用list()方法获取某文件夹下所有文件的名字
		String[] photos = assetManager.list(dirPath);
		for (int i = 0; i < photos.length; i++) {
			photoName = photos[i];
			String str = dirPath + "/" + photoName;
			lists.add(str);
		}
		return lists;

	}

	class ItemAdapter extends BaseAdapter {
		private List<String> images;

		public ItemAdapter(List<String> lists) {
			super();
			this.images = lists;
		}

		@Override
		public Object getItem(int position) {
			return position;
		}

		@Override
		public int getCount() {
			return images.size();
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {

			ImageView image = null;
			if (convertView == null) {
				image = (ImageView) getLayoutInflater().inflate(
						R.layout.local_item_grid, parent, false);
			} else {
				image = (ImageView) convertView;
			}
			// image.setTag(position);
			asynLoadBitmap(image, position);// 异步去加载图片
			return image;
		}

		private void asynLoadBitmap(ImageView image, int position) {
			InputStream is = null;
			try {
				is = assetManager.open(images.get(position));
				BitmapFactory.Options options = new BitmapFactory.Options();
				options.inJustDecodeBounds = false;
				options.inSampleSize = 5;
				Bitmap btp = BitmapFactory.decodeStream(is, null, options);
				image.setImageBitmap(btp);
			} catch (IOException e) {
				e.printStackTrace();
			}

		}

		// View view;
		// if (convertView == null) {
		// view = getLayoutInflater().inflate(R.layout.local_item_grid,
		// null);
		// ImageView image = (ImageView) view.findViewById(R.id.imageapp);
		// InputStream is = null;
		// try {
		// is = assetManager.open(images.get(position));
		// BitmapFactory.Options options=new BitmapFactory.Options();
		// options.inJustDecodeBounds = false;
		// options.inSampleSize = 5;
		// Bitmap btp =BitmapFactory.decodeStream(is,null,options);
		// image.setImageBitmap(btp);
		//
		// } catch (IOException e) {
		// e.printStackTrace();
		// }
		// try {
		// is.close();
		// } catch (IOException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// }
		//
		// } else {
		// view = convertView;
		// }
		// return view;
	}

	private void startImageGalleryActivity(int position) {
		Intent intent = new Intent(LocalGridActivity.this,
				AppPagerActivity.class);
		intent.putExtra(Extra.IMAGE_POSITION, position);
		intent.putStringArrayListExtra("list", lists);
		startActivity(intent);
	}

}
