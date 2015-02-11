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
 * APK���¹�����
 * @author Royal
 */
public class UpdateManager {
	// �����Ķ���
	private Context mContext;
	// ���°汾��Ϣ����
	private VersionInfo info = null;
	// ���ؽ�����
	private ProgressBar progressBar;
	// �Ƿ���ֹ����
	private boolean isInterceptDownload = false;
	// ��������ʾ��ֵ
	private int progress = 0;
	private String path = "http://www.iiijiaba.com/version.xml";
	/**
	 * ����ΪContext(������activity)�Ĺ��캯��
	 * @param context
	 */
	public UpdateManager(Context context) {
		this.mContext = context;
	}
	public void checkUpdate() {
		// �ӷ���˻�ȡ�汾��Ϣ 
		new Thread(new Runnable() {
			URL url = null;
			HttpURLConnection urlConn = null;

			@Override
			public void run() {
				Looper.prepare();
				try { 
					url = new URL(path);
					// ʹ��HttpURLConnection������
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
					// ��ȡ�����version.xml������(��)
					info = XMLParserUtil.getUpdateInfo(urlConn.getInputStream());
					urlConn.disconnect();
					if (info != null) {
						try {
							// ��ȡ��ǰ�������Ϣ
							PackageInfo pi = mContext.getPackageManager()
									.getPackageInfo(mContext.getPackageName(),
											PackageManager.GET_CONFIGURATIONS);
							// ��ǰ����汾��
							int versionCode = pi.versionCode;
							if (versionCode < info.getVersionCode()) {
								// �����ǰ�汾��С�ڷ���˰汾��,�򵯳���ʾ���¶Ի���
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
	 * �ӷ���˻�ȡ�汾��Ϣ
	 */
	private void getVersionInfoFromServer() { 
		
		 
	}
	/**
	 * ��ʾ���¶Ի���
	 * @param info
	 *            �汾��Ϣ����
	 */
	private void showUpdateDialog() {
		Builder builder = new Builder(mContext);
		builder.setTitle("version updating");
		builder.setMessage(info.getDisplayMessage());
		builder.setPositiveButton("Download", new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				// �������ؿ�
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
	 * �������ؿ�
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
				// ��ֹ����
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
		// ����apk
		downloadApk();
	}

	/**
	 * ����apk
	 */
	private void downloadApk() {
		// ������һ�߳�����
		Thread downLoadThread = new Thread(downApkRunnable);
		downLoadThread.start();
	} 
	/**
	 * �ӷ����������°�apk���߳�
	 */
	private Runnable downApkRunnable = new Runnable() {
		@Override
		public void run() { 
			if (!android.os.Environment.getExternalStorageState().equals(
					android.os.Environment.MEDIA_MOUNTED)) {
				// ���û��SD��
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
					// ���������°�apk��ַ
					URL url = new URL(info.getDownloadURL());
					HttpURLConnection conn = (HttpURLConnection) url
							.openConnection();
					conn.connect();
					int length = conn.getContentLength();
					InputStream is = conn.getInputStream();
					File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath()
							+ "/updateApkFile/");
					if (!file.exists()) {
						// ����ļ��в�����,�򴴽�
						file.mkdir();
					}
					// ���ط��������°汾�����д�ļ���
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
						// ���½�����
						progress = (int) (((float) count / length) * 100);
						handler.sendEmptyMessage(1);
						if (numRead <= 0) {
							// �������֪ͨ��װ
							handler.sendEmptyMessage(0);
							break;
						}
						fos.write(buf, 0, numRead);
						// �����ȡ��ʱ����ֹͣ����
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
	 * ����һ��handler������������
	 */
	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 1:
				// ���½������
				progressBar.setProgress(progress);
				break;
			case 0:
//				progressBar.setVisibility(View.INVISIBLE); 
				// ��װapk�ļ�
				installApk();
				break;
			default:
				break;
			}
		};
	};
	/**
	 * ��װapk
	 */
	private void installApk() {
		// ��ȡ��ǰsdcard�洢·��
		File apkfile = new File(Environment.getExternalStorageDirectory()
				.getAbsolutePath() + "/updateApkFile/" + info.getApkName());
		if (!apkfile.exists()) {
			return;
		}
		Intent i = new Intent(Intent.ACTION_VIEW);
		// ��װ�����ǩ����һ�£����ܳ��ֳ���δ��װ��ʾ
		i.setDataAndType(Uri.fromFile(new File(apkfile.getAbsolutePath())),
				"application/vnd.android.package-archive");
		mContext.startActivity(i);
	}
}
