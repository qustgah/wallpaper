package com.lt.aircraftwallpaper;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Gallery;
import android.widget.ImageView;

import com.lt.aircraftwallpaper.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

/**
 * 
 * @author adamin
 *
 */
public class ImageGalleryActivity extends BaseActivity {
	private String[] imageUrls;

	private Gallery gallery;

	private DisplayImageOptions options;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.ac_image_gallery);
		Bundle bundle = getIntent().getExtras();
         imageUrls = bundle.getStringArray(Extra.IMAGES);
		int galleryPosition = bundle.getInt(Extra.IMAGE_POSITION, 0);

		options = new DisplayImageOptions.Builder()
			.showImageForEmptyUri(R.drawable.image_for_empty_url)
			.showStubImage(R.drawable.stub_image)
			.cacheInMemory()
			.cacheOnDisc()
			.build();

		gallery = (Gallery) findViewById(R.id.gallery);
		gallery.setAdapter(new ImagePagerAdapter());
		gallery.setSelection(galleryPosition);
		gallery.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position,
					long id) {
				startImagePagerActivity(position);
				// TODO Auto-generated method stub
				
			}

			
		});
		
	}
	private void startImagePagerActivity(int position) {
		Intent intent = new Intent(this, ImagePagerActivity.class);
		intent.putExtra(Extra.IMAGES, imageUrls);
		intent.putExtra(Extra.IMAGE_POSITION, position);
		startActivity(intent);
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void onStop() {
		imageLoader.stop();
		super.onStop();
	}

	private class ImagePagerAdapter extends BaseAdapter {

		
//		private LayoutInflater inflater;
//
//		ImagePagerAdapter(String[] images) {
//			this.images = images;
//			inflater = getLayoutInflater();
//		}

		@Override
		public int getCount() {
			return imageUrls.length;
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
			ImageView imageView = (ImageView) convertView;
			if (imageView == null) {
				imageView = (ImageView)getLayoutInflater().inflate(R.layout.item_gallery_image, parent, false);
			}//≥ı ºªØ
			imageLoader.init(ImageLoaderConfiguration.createDefault(ImageGalleryActivity.this));
			imageLoader.displayImage(imageUrls[position], imageView, options);
			return imageView;
		}
	}
}