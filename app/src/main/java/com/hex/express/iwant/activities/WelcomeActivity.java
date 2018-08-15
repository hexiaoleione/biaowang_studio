package com.hex.express.iwant.activities;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.http.Header;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import butterknife.Bind;
import butterknife.ButterKnife;

import com.google.gson.Gson;
import com.hex.express.iwant.R;
import com.hex.express.iwant.iWantApplication;
import com.hex.express.iwant.bean.AdvertiseBean;
import com.hex.express.iwant.constance.MCUrl;
import com.hex.express.iwant.constance.PreferenceConstants;
import com.hex.express.iwant.http.AsyncHttpUtils;
import com.hex.express.iwant.http.UrlMap;
import com.hex.express.iwant.newmain.MainTab;
import com.hex.express.iwant.newsmain.MainseActivity;
import com.hex.express.iwant.newsmain.NewMainActivity;
import com.hex.express.iwant.utils.DataTools;
import com.hex.express.iwant.utils.PreferencesUtils;
import com.hex.express.iwant.views.TitleBarView;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

/**
 * 欢迎界面
 * 
 * @author Eric
 * 
 */
public class WelcomeActivity extends Activity {
	DisplayImageOptions options; // DisplayImageOptions是用于设置图片显示的类
	@Bind(R.id.imgLogo)
	ImageView imgLogo;
	@Bind(R.id.imgLogo2)
	ImageView imgLogo2;
	@Bind(R.id.btn_vault)
	Button btn_vault;
	@Bind(R.id.ll_webview)
	LinearLayout ll_webview;
	@Bind(R.id.webview)
	WebView webView;
	@Bind(R.id.ll_advertise)
	FrameLayout ll_advertise;
	@Bind(R.id.btn_Left)
	ImageView btn_Left;
	@Bind(R.id.btn_Right)
	TextView btn_Right;
	@Bind(R.id.wel_neme)
	TextView wel_neme;
	
//	@Bind(R.id.tbv_show)
//	TitleBarView tbv_show;
	private AdvertiseBean bean;
	boolean ischick = false;
	boolean isfis = false;
	boolean flag = false;
	private Handler handler;
	private int time = 3;
	private String url;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_welcome);
		iWantApplication.getInstance().addActivity(this);
		ButterKnife.bind(WelcomeActivity.this);
		handler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				// TODO Auto-generated method stub
				super.handleMessage(msg);
				switch (msg.what) {
				case 1:
					ConnectivityManager connectivityManager = (ConnectivityManager) WelcomeActivity.this
							.getSystemService(Context.CONNECTIVITY_SERVICE);
					NetworkInfo activeNetInfo = connectivityManager
							.getActiveNetworkInfo();
					if (activeNetInfo != null
							&& activeNetInfo.getType() == ConnectivityManager.TYPE_WIFI) {
						SharedPreferences sp = getSharedPreferences("data", MODE_PRIVATE);
						boolean isFirst = sp.getBoolean("isFirst", true);
						if (isFirst) {
							SharedPreferences.Editor edit = sp.edit();
							edit.putBoolean("isFirst", false);
							edit.commit();
							enterHome();
						}else {						
							getHttpImage();
//							enterHome();
						}
					} else {

						imgLogo2.setBackgroundResource(R.drawable.icon_introduce_1);
						enterHome();
					}
					break;

				case 2:
					if (!ischick) {
						
						enterHome();
					}

					break;
				case 3:
					time--;
//					btn_vault.setText("(跳过" + time + ")");
					
					if (time >= 0) {
						btn_vault.setText(time+"秒跳过");
						handler.sendEmptyMessageDelayed(3, 1000);
					} else if (!ischick) {
					
						enterHome();
					}

					break;
				}

			}
		};
		initData();
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		handler.removeCallbacksAndMessages(null);
		handler = null;
	}

	@SuppressWarnings("deprecation")
	private void initData() {
		// 使用DisplayImageOptions.Builder()创建DisplayImageOptions
		options = new DisplayImageOptions.Builder()
				.showStubImage(R.drawable.icon_introduce_1) // 设置图片下载期间显示的图片
				.showImageForEmptyUri(R.drawable.icon_introduce_1) // 设置图片Uri为空或是错误的时候显示的图片
				.showImageOnFail(R.drawable.icon_introduce_1) // 设置图片加载或解码过程中发生错误显示的图片
				.cacheInMemory(true) // 设置下载的图片是否缓存在内存中
				.cacheOnDisc(true) // 设置下载的图片是否缓存在SD卡中 
				.resetViewBeforeLoading(true)// 设置图片在下载前是否重置，复位 
				.build(); // 创建配置过得DisplayImageOption对象
		handler.sendEmptyMessageDelayed(1, 2000);
		btn_vault.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(WelcomeActivity.this,
						NewMainActivity.class);
//				Intent intent = new Intent(WelcomeActivity.this,
//						MainTab.class);
//				Intent intent = new Intent(WelcomeActivity.this,
//						MainActivity.class);
				intent.putExtra("type", "1");
				startActivity(intent);
				finish();
			}
		});
		btn_Left.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(WelcomeActivity.this, NewMainActivity.class);
				// Intent intent = new Intent(WelcomeActivity.this,
				// MainTab.class);

				// Intent intent = new Intent(WelcomeActivity.this,
				// MainActivity.class);
				intent.putExtra("type", "1");
				startActivity(intent);
				finish();
			}
		});
		btn_Right.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				final Intent intent = new Intent();
				intent.setAction(Intent.ACTION_VIEW);
				intent.setData(Uri.parse(bean.data.get(0).advertiseHtmlUrl));
				// 注意此处的判断intent.resolveActivity()可以返回显示该Intent的Activity对应的组件名
				// 官方解释 : Name of the component implementing an activity that can display the intent
				if (intent.resolveActivity(getPackageManager()) != null) {    
				  final ComponentName componentName = intent.resolveActivity(getPackageManager()); 
				  // 打印Log   ComponentName到底是什么
//				  Log.e("1111", "componentName = " + bean.data.get(0).advertiseHtmlUrl);
				  startActivity(Intent.createChooser(intent, "请选择浏览器"));
				} else {    
				  Toast.makeText(getApplicationContext(), "没有匹配的程序", Toast.LENGTH_SHORT).show();
				}
			}
		});
	}
	private void getHttpImage() {
		//DataTools.getDeviceId(WelcomeActivity.this)
		Log.e("11111111mon   ", DataTools.getDeviceId(WelcomeActivity.this));
		 url = UrlMap.getTwo(MCUrl.ADVERTISE, "userId", String
				.valueOf(PreferencesUtils.getInt(getApplicationContext(),
						PreferenceConstants.UID)), "deviceId", DataTools.getDeviceId(WelcomeActivity.this));
		AsyncHttpUtils.doGet(url, null, null, null,
				new AsyncHttpResponseHandler() {

					@Override
					public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
						Log.e("11111111mon", new String(arg2));
						bean = new Gson().fromJson(new String(arg2),
								AdvertiseBean.class);
						ImageLoader loader = ImageLoader.getInstance();
						loader.init(ImageLoaderConfiguration
								.createDefault(WelcomeActivity.this));

						loader.displayImage(bean.data.get(0).advertiseImageUrl,
								imgLogo2, options, new ImageLoadingListener() {

									@Override
									public void onLoadingComplete(String arg0,
											View arg1, Bitmap arg2) {
										imgLogo.setVisibility(View.GONE);
										ll_advertise
												.setVisibility(View.VISIBLE);
										handler.sendEmptyMessageDelayed(2, 3000);
										handler.sendEmptyMessageDelayed(3, 1000);
										ll_advertise
												.setOnClickListener(new OnClickListener() {

													@Override
													public void onClick(View v) {
												
															webView.loadUrl(bean.data
																	.get(0).advertiseHtmlUrl);
															webView.setWebViewClient(new WebViewClient(){
																@Override
																public boolean shouldOverrideUrlLoading(
																		WebView view,
																		String url) {
																	// TODO Auto-generated method stub
																	view.loadUrl(url);
																	return true;
																}
															});
															ll_webview
																	.setVisibility(View.VISIBLE);
															wel_neme.setText(bean.data.get(0).advertiseName);
															ll_advertise
																	.setVisibility(View.GONE);
															ischick = true;
													}
												});
									}

									@Override
									public void onLoadingCancelled(String arg0,
											View arg1) {
										// TODO Auto-generated method stub

									}

									@Override
									public void onLoadingFailed(String arg0,
											View arg1, FailReason arg2) {
										enterHome();

									}

									@Override
									public void onLoadingStarted(String arg0,
											View arg1) {
									

									}
								});

					}

					@Override
					public void onFailure(int arg0, Header[] arg1, byte[] arg2,
							Throwable arg3) {
						// TODO Auto-generated method stub
						enterHome();
					}
				});

	}

	/**
	 * 点击返回键跳到主界面
	 */
	@Override
	public boolean dispatchKeyEvent(KeyEvent event) {
		if (event.getKeyCode() == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
//			Intent intent = new Intent(this, MainTab.class);
			Intent intent = new Intent(this, NewMainActivity.class);
			intent.putExtra("type", "1");
			startActivity(intent);
			return true;
		}
		return super.dispatchKeyEvent(event);
	}
	// 跳转主菜单页面
	private void enterHome() {
		  Date now = new Date(); 
		   SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");//日期格式
		   String hehe = dateFormat.format(now); 
		   SimpleDateFormat dfs = new SimpleDateFormat("HH:mm:ss");     
		   Date begin = null;
		   Date end = null ;
		try {
			begin = dfs.parse(hehe);
	        end = dfs.parse("23:50:50");
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}     
		  long between=(end.getTime()-begin.getTime())/1000;//除以1000是为了转换成秒     
//		  if (between>0) {
//			  SharedPreferences sps = getSharedPreferences("isFid", MODE_PRIVATE);
//			  boolean isFi = sps.getBoolean("isFi", true);
//			  if (isFi) {
//				  SharedPreferences.Editor edit = sps.edit();
//					edit.putBoolean("isFi", false);
//					edit.commit();
//				  startActivity(new Intent(this, WeHwedActivity.class));
//					finish();
//			}else {
			  SharedPreferences sp = getSharedPreferences("data", MODE_PRIVATE);
				boolean isLogin = sp.getBoolean("isLogin", true);
				// 开启页面
				if (isLogin == true) {
					startActivity(new Intent(this, IntroduceActivity.class));
				} else {
//					Intent intent = new Intent(this, MainActivity.class);
					Intent intent = new Intent(this, NewMainActivity.class);//NewMainActivity  MainseActivity
//					Intent intent = new Intent(this, MainTab.class);
					intent.putExtra("type", "1");
					startActivity(intent);
					// 开启服务
					// 关闭
					finish();
				}
//		  }
//		  }else {
//			  SharedPreferences sps = getSharedPreferences("isFid", MODE_PRIVATE);
//			  SharedPreferences.Editor edit = sps.edit();
//				edit.putBoolean("isFi", true);
//				edit.commit();
////			
//		}

		
	
	}

	/*
	 * @Override public void onWeightClick(View v) { initView(); setOnClick();
	 * 
	 * }
	 * 
	 * @Override public void initView() {
	 * 
	 * }
	 * 
	 * @Override public void initData() {
	 * 
	 * }
	 * 
	 * @Override public void setOnClick() {
	 * 
	 * }
	 * 
	 * @Override public void getData() {
	 * 
	 * }
	 */
}
