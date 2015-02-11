package com.wallpaper.updatemanager;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL; 

import com.lt.aircraftwallpaper.LoginActivity1;
import com.lt.aircraftwallpaper.R;
import com.lt.aircraftwallpaper.tabhostmainactivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.os.CountDownTimer;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ProgressBar; 

/**
 * APK更新管理类
 * @author Royal
 */
public class UpdateManager {
	// 上下文对象
	private Context mContext;
	// 更新版本信息对象
	private VersionInfo info = null;
	// 下载进度条
	private ProgressBar progressBar;
	// 是否终止下载
	private boolean isInterceptDownload = false;
	// 进度条显示数值
	private int progress = 0;
	private String path = "http://www.iiijiaba.com/version.xml";
	/**
	 * 参数为Context(上下文activity)的构造函数
	 * @param context
	 */
	public UpdateManager(Context context) {
		this.mContext = context;
	}
	public void checkUpdate() {
		// 从服务端获取版本信息 
		new Thread(new Runnable() {
			URL url = null;
			HttpURLConnection urlConn = null;

			@Override
			public void run() {
				Looper.prepare();
				try { 
					url = new URL(path);
					// 使用HttpURLConnection打开连接
					urlConn = (HttpURLConnection) url.openConnection();
				} catch (Exception e) {
					new CountDownTimer(2000,1000){
						@Override
						public void onTick(long millisUntilFinished) {
						}
						@Override
						public void onFinish() {
							Intent intent = new Intent();
							intent.setClass(mContext, tabhostmainactivity.class);
							mContext.startActivity(intent);
							int VERSION=Integer.parseInt(android.os.Build.VERSION.SDK);
							if(VERSION >= 5){
								((Activity) mContext).overridePendingTransition(R.anim.alpha_in, R.anim.alpha_out);
							}
							((Activity) mContext).finish();
							}
					}.start();
				}
				try {
					// 读取服务端version.xml的内容(流)
					info = XMLParserUtil.getUpdateInfo(urlConn.getInputStream());
					urlConn.disconnect();
					if (info != null) {
						try {
							// 获取当前软件包信息
							PackageInfo pi = mContext.getPackageManager()
									.getPackageInfo(mContext.getPackageName(),
											PackageManager.GET_CONFIGURATIONS);
							// 当前软件版本号
							int versionCode = pi.versionCode;
							if (versionCode < info.getVersionCode()) {
								// 如果当前版本号小于服务端版本号,则弹出提示更新对话框
								showUpdateDialog();
							}else{
								new CountDownTimer(2000,1000){
									@Override
									public void onTick(long millisUntilFinished) {
									}
									@Override
									public void onFinish() {
										Intent intent = new Intent();
										intent.setClass(mContext, tabhostmainactivity.class);
										mContext.startActivity(intent);
										int VERSION=Integer.parseInt(android.os.Build.VERSION.SDK);
										if(VERSION >= 5){
											((Activity) mContext).overridePendingTransition(R.anim.alpha_in, R.anim.alpha_out);
										}
										((Activity) mContext).finish();
										}
								}.start();
							}
						} catch (NameNotFoundException e) {
							e.printStackTrace();
						}
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
				Looper.loop();
			}
		}).start();
	}
	/**
	 * 从服务端获取版本信息
	 */
	private void getVersionInfoFromServer() { 
		
		 
	}
	/**
	 * 提示更新对话框
	 * @param info
	 *            版本信息对象
	 */
	private void showUpdateDialog() {
		Builder builder = new Builder(mContext);
		builder.setTitle("version updating");
		builder.setMessage(info.getDisplayMessage());
		builder.setPositiveButton("Download", new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				// 弹出下载框
				showDownloadDialog();
			}
		});
		builder.setNegativeButton("No", new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				
				new CountDownTimer(2000,1000){
					@Override
					public void onTick(long millisUntilFinished) {
					}
					@Override
					public void onFinish() {
						Intent intent = new Intent();
						intent.setClass(mContext, tabhostmainactivity.class);
						mContext.startActivity(intent);
						int VERSION=Integer.parseInt(android.os.Build.VERSION.SDK);
						if(VERSION >= 5){
							((Activity) mContext).overridePendingTransition(R.anim.alpha_in, R.anim.alpha_out);
						}
						((Activity) mContext).finish();
						}
				}.start();
			}
		}); 
		 AlertDialog dialog = builder.create();    
		 dialog.show(); 
	} 
	/**
	 * 弹出下载框
	 */
	private void showDownloadDialog() {
		Builder builder = new Builder(mContext);
		builder.setTitle("updating...");
		final LayoutInflater inflater = LayoutInflater.from(mContext);
		View v = inflater.inflate(R.layout.update_progress, null);
		progressBar = (ProgressBar) v.findViewById(R.id.pb_update_progress);
		builder.setView(v);
		builder.setNegativeButton("cancel", new OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				// 终止下载
				isInterceptDownload = true;
				new CountDownTimer(2000,1000){
					@Override
					public void onTick(long millisUntilFinished) {
					}
					@Override
					public void onFinish() {
						Intent intent = new Intent();
						intent.setClass(mContext, tabhostmainactivity.class);
						mContext.startActivity(intent);
						int VERSION=Integer.parseInt(android.os.Build.VERSION.SDK);
						if(VERSION >= 5){
							((Activity) mContext).overridePendingTransition(R.anim.alpha_in, R.anim.alpha_out);
						}
						((Activity) mContext).finish();
						}
				}.start();
			}
		});
		builder.create().show();
		// 下载apk
		downloadApk();
	}

	/**
	 * 下载apk
	 */
	private void downloadApk() {
		// 开启另一线程下载
		Thread downLoadThread = new Thread(downApkRunnable);
		downLoadThread.start();
	} 
	/**
	 * 从服务器下载新版apk的线程
	 */
	private Runnable downApkRunnable = new Runnable() {
		@Override
		public void run() { 
			if (!android.os.Environment.getExternalStorageState().equals(
					android.os.Environment.MEDIA_MOUNTED)) {
				// 如果没有SD卡
				Builder builder = new Builder(mContext);
				builder.setTitle("Reminder");
				builder.setMessage("The current equipment without the SD card, can't download data");
				builder.setPositiveButton("confirm", new OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
						new CountDownTimer(2000,1000){
							@Override
							public void onTick(long millisUntilFinished) {
							}
							@Override
							public void onFinish() {
								Intent intent = new Intent();
								intent.setClass(mContext, tabhostmainactivity.class);
								mContext.startActivity(intent);
								int VERSION=Integer.parseInt(android.os.Build.VERSION.SDK);
								if(VERSION >= 5){
									((Activity) mContext).overridePendingTransition(R.anim.alpha_in, R.anim.alpha_out);
								}
								((Activity) mContext).finish();
								}
						}.start();
					}
				});
				builder.show();
				return;
			} else {
				try {
					// 服务器上新版apk地址
					URL url = new URL(info.getDownloadURL());
					HttpURLConnection conn = (HttpURLConnection) url
							.openConnection();
					conn.connect();
					int length = conn.getContentLength();
					InputStream is = conn.getInputStream();
					File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath()
							+ "/updateApkFile/");
					if (!file.exists()) {
						// 如果文件夹不存在,则创建
						file.mkdir();
					}
					// 下载服务器中新版本软件（写文件）
					String apkFile = Environment.getExternalStorageDirectory()
							.getAbsolutePath()
							+ "/updateApkFile/"
							+ info.getApkName();
					File ApkFile = new File(apkFile);
					FileOutputStream fos = new FileOutputStream(ApkFile);
					int count = 0;
					byte buf[] = new byte[1024];
					do {
						int numRead = is.read(buf);
						count += numRead;
						// 更新进度条
						progress = (int) (((float) count / length) * 100);
						handler.sendEmptyMessage(1);
						if (numRead <= 0) {
							// 下载完成通知安装
							handler.sendEmptyMessage(0);
							break;
						}
						fos.write(buf, 0, numRead);
						// 当点击取消时，则停止下载
					} while (!isInterceptDownload);
				} catch (Exception e) { 
					new CountDownTimer(2000,1000){
						@Override
						public void onTick(long millisUntilFinished) {
						}
						@Override
						public void onFinish() {
							Intent intent = new Intent();
							intent.setClass(mContext, tabhostmainactivity.class);
							mContext.startActivity(intent);
							int VERSION=Integer.parseInt(android.os.Build.VERSION.SDK);
							if(VERSION >= 5){
								((Activity) mContext).overridePendingTransition(R.anim.alpha_in, R.anim.alpha_out);
							}
							((Activity) mContext).finish();
							}
					}.start();
				}
			} 
		}
	};
	/**
	 * 声明一个handler来跟进进度条
	 */
	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 1:
				// 更新进度情况
				progressBar.setProgress(progress);
				break;
			case 0:
//				progressBar.setVisibility(View.INVISIBLE); 
				// 安装apk文件
				installApk();
				break;
			default:
				break;
			}
		};
	};
	/**
	 * 安装apk
	 */
	private void installApk() {
		// 获取当前sdcard存储路径
		File apkfile = new File(Environment.getExternalStorageDirectory()
				.getAbsolutePath() + "/updateApkFile/" + info.getApkName());
		if (!apkfile.exists()) {
			return;
		}
		Intent i = new Intent(Intent.ACTION_VIEW);
		// 安装，如果签名不一致，可能出现程序未安装提示
		i.setDataAndType(Uri.fromFile(new File(apkfile.getAbsolutePath())),
				"application/vnd.android.package-archive");
		mContext.startActivity(i);
	}
}
