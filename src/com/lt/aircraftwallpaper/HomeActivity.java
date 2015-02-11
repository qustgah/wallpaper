package com.lt.aircraftwallpaper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.lt.localphoto.bendiActivity;
import com.lt.aircraftwallpaper.R;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
public class HomeActivity extends BaseActivity {

	private String[] imageUrls;
	private String[] imageUrls2;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//设置无标题
				requestWindowFeature(Window.FEATURE_NO_TITLE);
				//设置全屏显示
				getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN );
				//关联布局文件
		setContentView(R.layout.ac_home);
    //获取图片数组地址
		String[] heavyImages = getResources().getStringArray(R.array.heavy_images);
		String[] lightImages = getResources().getStringArray(R.array.light_images);
		String[] beautyImages = getResources().getStringArray(R.array.beauty_images);
//声明一个数组，长度等于上面两组图片数组的长度
		imageUrls = new String[heavyImages.length + lightImages.length];
		imageUrls2 = new String[beautyImages.length];
		//帅哥列表
		List<String> urls = new ArrayList<String>();
		//列表后加入heavyImages列表
		urls.addAll(Arrays.asList(heavyImages));
		//列表后加入lightImages列表
		urls.addAll(Arrays.asList(lightImages));
		//list.toarray 然后转型成String数组
		imageUrls = (String[]) urls.toArray(new String[0]);
		//美女列表
		List<String> urls2 = new ArrayList<String>();
		//列表后加入heavyImages列表
		urls2.addAll(Arrays.asList(beautyImages));
		//list.toarray 然后转型成String数组
		imageUrls2 = (String[]) urls2.toArray(new String[0]);
	}
//xml的onclick
	public void onImageListClick(View view) {
		Intent intent = new Intent(this, ImageListActivity.class);
		intent.putExtra(Extra.IMAGES, imageUrls);
		startActivity(intent);
	}

	public void onImageGridClick(View view) {
		Intent intent = new Intent(this, ImageGridActivity.class);
		intent.putExtra(Extra.IMAGES, imageUrls2);
		startActivity(intent);
	}

	public void onImagePagerClick(View view) {
		Intent intent = new Intent(this, ImagePagerActivity.class);
		intent.putExtra(Extra.IMAGES, imageUrls);
		startActivity(intent);
	}

	public void onImageGalleryClick(View view) {
		Intent intent = new Intent(this, ImageGalleryActivity.class);
		intent.putExtra(Extra.IMAGES, imageUrls);
		startActivity(intent);
	}
	public void onLocalImageClick(View view){
		Intent intent =new Intent(this,bendiActivity.class);
		startActivity(intent);
	}
}