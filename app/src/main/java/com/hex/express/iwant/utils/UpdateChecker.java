package com.hex.express.iwant.utils;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.ResultReceiver;
import android.util.Log;

import com.google.gson.Gson;
import com.hex.express.iwant.bean.BaseBean;
import com.hex.express.iwant.bean.VersionBean;
import com.hex.express.iwant.constance.PreferenceConstants;
import com.hex.express.iwant.http.AsyncHttpUtils;
import com.hex.express.iwant.service.DownLoaderService;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.zip.GZIPInputStream;
public class UpdateChecker{
    private static final String TAG = "UpdateChecker";
    private Context mContext;
    //检查版本信息的线程
    private Thread mThread;
    //版本对比地址
    private String mCheckUrl;
    //下载apk的对话框
    private ProgressDialog mProgressDialog;
    VersionBean bean;
    private File apkFile;
    private String androidApkPath;
//    public void setCheckUrl(String url) {
//        mCheckUrl = url;
//    }
                                                                                                                  
    public UpdateChecker(Context context,String url) {
        mContext = context;
        mCheckUrl = url;
        // instantiate it within the onCreate method
        mProgressDialog = new ProgressDialog(context);
        mProgressDialog.setMessage("正在下载");
        mProgressDialog.setIndeterminate(false);
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        mProgressDialog.setCancelable(false);
        mProgressDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                                                                                                                              
            }
        });
        mProgressDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                // TODO Auto-generated method stub
                                                                                                                              
            }
        });
    }
	/**
	 * 上传自己的坐标位置；
	 */
	int i=0;
	public  void uploadMyLocation(double latitude,double longitude ) {
	
		final JSONObject obj = new JSONObject();
if (i>6) {
	return;
}
		try {
			obj.put("userId",
					String.valueOf(PreferencesUtils.getInt(mContext.getApplicationContext(), PreferenceConstants.UID)));
			obj.put("userType", PreferencesUtils.getString(mContext.getApplicationContext(), PreferenceConstants.USERTYPE));
			obj.put("latitude", latitude);
			obj.put("longitude", longitude);
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
//			Log.e("11111111123 latitude  ", ""+obj.toString());
//			Log.e("11111111123 longitude  ", ""+longitude);
//		new Timer().schedule(new TimerTask(){
//			  public void run() {
				  Log.e("11111111123 longitude  ", "ssssssss");
					AsyncHttpUtils.doPostJson(mContext, mCheckUrl, obj.toString(),
							new AsyncHttpResponseHandler() {

								@Override
								public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
									Log.e("__________________", new String(arg2));
									
									if (arg2 == null || arg2.length == 0) {
										ToastUtil.shortToast(mContext, "获取位置失败");
										return;
									}
									BaseBean bean = new Gson().fromJson(new String(arg2), BaseBean.class);
									if (bean.isSuccess() == true) {
										i++;
										Log.e("11111111 ", i+"");
//										ToastUtil.shortToast(mContext, bean.getMessage());

									} else {
//										ToastUtil.shortToast(mContext, bean.getMessage());
									}
								}
								@Override
								public void onFailure(int arg0, Header[] arg1, byte[] arg2, Throwable arg3) {
									ToastUtil.shortToast(mContext, "定位失败");
									// TODO Auto-generated method stub
									Log.e("ooo1", arg0 + "");
								}
							});


//			}}, 60000);
		
	}                                                                                              
    public void checkForUpdates() {
        if(mCheckUrl == null) {
            //throw new Exception("checkUrl can not be null");
            return;
        }
        AsyncHttpUtils.doSimGet(mCheckUrl, new AsyncHttpResponseHandler() {
			@Override
			public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
				// TODO Auto-generated method stub
				Log.e("gdktttttd", new String(arg2));
				if (arg2 == null)
					return;
				bean = new Gson().fromJson(new String(arg2), VersionBean.class);
				Log.e("bean", bean.data.get(0).androidApkPath);
				if (!getCurrVersion().equals(bean.data.get(0).androidVersion)) {
//					DialogUtils.createAlertDialogTwo(mContext, "发现新版本",
//							"版本号为" + bean.data.get(0).androidVersion + "/n" + bean.data.get(0).updateContent, 0,
//							true, false, "确认", "取消", new DialogInterface.OnClickListener() {
//
//								@Override
//								public void onClick(DialogInterface dialog, int which) {
//									androidApkPath = bean.data.get(0).androidApkPath;
//									downLoadApk();
//								}
//							}, new DialogInterface.OnClickListener() {
//
//								@Override
//								public void onClick(DialogInterface dialog, int which) {
//									// TODO Auto-generated
//									// method
//									// stub
//
//								}
//							}).show();
					androidApkPath = bean.data.get(0).androidApkPath;
					Log.e("111111s1", ""+androidApkPath);
					downLoadApk();
				}
			}

			@Override
			public void onFailure(int arg0, Header[] arg1, byte[] arg2, Throwable arg3) {
				// TODO Auto-generated method stub
				Log.e("ccc", arg0 + "");
			}
		});
                                                                                                                      
    }
    private String getCurrVersion() {
		PackageManager manager = mContext.getPackageManager();
		PackageInfo info = null;
		try {
			info = manager.getPackageInfo(mContext.getPackageName(), 0);
		} catch (NameNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String version = info.versionName;
		Log.e("versionttttt", version);
		return version;
	}
    protected String sendPost() {
        HttpURLConnection uRLConnection = null;
        InputStream is = null;
        BufferedReader buffer = null;
        String result = null;
        try {
            URL url = new URL(mCheckUrl);
            uRLConnection = (HttpURLConnection) url.openConnection();
            uRLConnection.setDoInput(true);
            uRLConnection.setDoOutput(true);
            uRLConnection.setRequestMethod("POST");
            uRLConnection.setUseCaches(false);
            uRLConnection.setConnectTimeout(10 * 1000);
            uRLConnection.setReadTimeout(10 * 1000);
            uRLConnection.setInstanceFollowRedirects(false);
            uRLConnection.setRequestProperty("Connection", "Keep-Alive");
            uRLConnection.setRequestProperty("Charset", "UTF-8");
            uRLConnection.setRequestProperty("Accept-Encoding", "gzip, deflate");
            uRLConnection.setRequestProperty("Content-Type", "application/json");
            uRLConnection.connect();
            is = uRLConnection.getInputStream();
            String content_encode = uRLConnection.getContentEncoding();
            if (null != content_encode && !"".equals(content_encode) && content_encode.equals("gzip")) {
                is = new GZIPInputStream(is);
            }
            buffer = new BufferedReader(new InputStreamReader(is));
            StringBuilder strBuilder = new StringBuilder();
            String line;
            while ((line = buffer.readLine()) != null) {
                strBuilder.append(line);
            }
            result = strBuilder.toString();
        } catch (Exception e) {
            Log.e(TAG, "http post error", e);
        } finally {
            if(buffer!=null){
                try {
                    buffer.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if(is!=null){
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if(uRLConnection!=null){
                uRLConnection.disconnect();
            }
        }
        return result;
    }
                                                                                                                  
                                                                                                                  
    public void downLoadApk() {
        String apkUrl = androidApkPath;
        String dir = mContext.getExternalFilesDir( "apk").getAbsolutePath();
        File folder = Environment.getExternalStoragePublicDirectory(dir);
        if(folder.exists() && folder.isDirectory()) {
             //do nothing
        }else {
            folder.mkdirs();
        }
        String filename = apkUrl.substring(apkUrl.lastIndexOf("/"),apkUrl.length());
        String destinationFilePath =  dir + "/" + filename;
        apkFile = new File(destinationFilePath);
        mProgressDialog.show();
        Intent intent = new Intent(mContext, DownLoaderService.class);
        intent.putExtra("url", apkUrl);
        intent.putExtra("dest", destinationFilePath);
        intent.putExtra("receiver", new DownloadReceiver(new Handler()));
        mContext.startService(intent);
    }
                                                                                                                  
    private class DownloadReceiver extends ResultReceiver{
        public DownloadReceiver(Handler handler) {
            super(handler);
        }
        @Override
        protected void onReceiveResult(int resultCode, Bundle resultData) {
            super.onReceiveResult(resultCode, resultData);
            if (resultCode == DownLoaderService.UPDATE_PROGRESS) {
                int progress = resultData.getInt("progress");
                mProgressDialog.setProgress(progress);
                if (progress == 100) {
                    mProgressDialog.dismiss();
                    //如果没有设置SDCard写权限，或者没有sdcard,apk文件保存在内存中，需要授予权限才能安装
                    String[] command = {"chmod","777",apkFile.toString()};
                    try{
                        ProcessBuilder builder = new ProcessBuilder(command);
                        builder.start();
                        Intent intent = new Intent(Intent.ACTION_VIEW);
                        intent.setDataAndType(Uri.fromFile(apkFile), "application/vnd.android.package-archive");
                 		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        mContext.startActivity(intent);
                    }catch (Exception e){
                                                                                                                                      
                    }
                }
            }
        }
    }
}
