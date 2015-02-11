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
		//�����ޱ���
				requestWindowFeature(Window.FEATURE_NO_TITLE);
				//����ȫ����ʾ
				getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN );
				//���������ļ�
		setContentView(R.layout.ac_home);
    //��ȡͼƬ�����ַ
		String[] heavyImages = getResources().getStringArray(R.array.heavy_images);
		String[] lightImages = getResources().getStringArray(R.array.light_images);
		String[] beautyImages = getResources().getStringArray(R.array.beauty_images);
//����һ�����飬���ȵ�����������ͼƬ����ĳ���
		imageUrls = new String[heavyImages.length + lightImages.length];
		imageUrls2 = new String[beautyImages.length];
		//˧���б�
		List<String> urls = new ArrayList<String>();
		//�б�����heavyImages�б�
		urls.addAll(Arrays.asList(heavyImages));
		//�б�����lightImages�б�
		urls.addAll(Arrays.asList(lightImages));
		//list.toarray Ȼ��ת�ͳ�String����
		imageUrls = (String[]) urls.toArray(new String[0]);
		//��Ů�б�
		List<String> urls2 = new ArrayList<String>();
		//�б�����heavyImages�б�
		urls2.addAll(Arrays.asList(beautyImages));
		//list.toarray Ȼ��ת�ͳ�String����
		imageUrls2 = (String[]) urls2.toArray(new String[0]);
	}
//xml��onclick
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