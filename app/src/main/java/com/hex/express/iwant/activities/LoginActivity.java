package com.hex.express.iwant.activities;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.LocationClientOption.LocationMode;
import com.google.gson.Gson;
import com.hex.express.iwant.R;
import com.hex.express.iwant.iWantApplication;
import com.hex.express.iwant.bean.AreaBean;
import com.hex.express.iwant.bean.BaseBean;
import com.hex.express.iwant.bean.CityBean;
import com.hex.express.iwant.bean.PublicCourierBean;
import com.hex.express.iwant.bean.RegisterBean;
import com.hex.express.iwant.bean.Unidbean;
import com.hex.express.iwant.constance.MCUrl;
import com.hex.express.iwant.constance.MsgConstants;
import com.hex.express.iwant.constance.PreferenceConstants;
import com.hex.express.iwant.helper.AreaDboperation;
import com.hex.express.iwant.helper.CheckDbUtils;
import com.hex.express.iwant.helper.CityDbOperation;
import com.hex.express.iwant.helper.DbManager;
import com.hex.express.iwant.http.AsyncHttpUtils;
import com.hex.express.iwant.http.UrlMap;
import com.hex.express.iwant.newmain.MainTab;
import com.hex.express.iwant.newsmain.NewMainActivity;
import com.hex.express.iwant.utils.DataTools;
import com.hex.express.iwant.utils.Logger;
import com.hex.express.iwant.utils.MD5Util;
import com.hex.express.iwant.utils.PreferencesUtils;
import com.hex.express.iwant.utils.ToastUtil;
import com.hex.express.iwant.views.DialogUtils;
import com.hex.express.iwant.views.LoadingProgressDialog;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.mob.tools.utils.UIHandler;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler.Callback;
import android.provider.Settings;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.TagAliasCallback;
import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.PlatformDb;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.tencent.qq.QQ;
import cn.sharesdk.wechat.friends.Wechat;

/**
 * 登录界面
 * 
 * @author Eric
 * 
 */
public class LoginActivity extends BaseActivity implements
		PlatformActionListener, Callback {
	private static final int MSG_USERID_FOUND = 1;
	private static final int MSG_LOGIN = 2;
	private static final int MSG_AUTH_CANCEL = 3;
	private static final int MSG_AUTH_ERROR = 4;
	private static final int MSG_AUTH_COMPLETE = 5;
	private long exitTime;
	private EditText edt_username;
	private EditText edt_userpassword;
	private TextView findpassword;
	private TextView hurryregiste;
	private Button btn_login;
	private ImageButton imgbtn_qq;
	private ImageButton imgbtn_wx;
	private ImageButton imgbtn_xl;
	// 接口
	private static String url = iWantApplication.BASE_URL + "login.action?";
	private String deviceId;
	LoadingProgressDialog dialog;
	private Platform plat;
	private int login_type;
	Unidbean unidbean;
	
//	private LocationClient client;//百度地图的Client
//	
//	private double latitude;
//	private double longitude;
	private LocationClient client;//百度地图的Client
	private double latitude;
	private double longitude;
	private String code;
	private String 	city,cityCode,townCode,townaddressd;

	//注册的实体类
	RegisterBean bean;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		iWantApplication.getInstance().addActivity(this);
		ButterKnife.bind(this);
		deviceId = DataTools.getDeviceId(getBaseContext());
		
		initView();
		initData();
		setOnClick();
	}
	@OnClick(R.id.btnLeft)
	public void onMyClick(View view){
		switch (view.getId()) {
		case R.id.btnLeft:
//			Intent intent=new Intent(LoginActivity.this,MainActivity.class);
			Intent intent=new Intent(LoginActivity.this,LoginWeixinActivity.class);
			startActivity(intent);
			break;

			
		default:
			break;
		}
	}
	@Override
	public void onWeightClick(View v) {
		final Intent intent = new Intent();
		switch (v.getId()) {
		case R.id.findpassword:// 找回密码
			// ToastUtil.shortToast(LoginActivity.this, "找回密码被点击");
			intent.setClass(LoginActivity.this, FindPassWordActivity.class);
			startActivity(intent);
			LoginActivity.this.finish();
			break;
		case R.id.hurryregiste:// 快速注册
			// ToastUtil.shortToast(LoginActivity.this, "快速注册被点击");
			if (latitude==4.9E-324 || longitude==4.9E-324) {  
	         	Builder ad = new Builder(LoginActivity.this);
	         	  ad.setTitle("温馨提示");  
		           ad.setMessage("为方便您的使用，请您在手机权限管理中允许软件定位");  
				ad.setNegativeButton("确认", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface arg0, int arg1) {
						arg0.dismiss();
//						ToastUtil.longToast(LoginActivity.this, "");
//						Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);  
//	                    startActivityForResult(intent, 0); // 设置完成后返回到原来的界面  
					}
				});
//				ad.setPositiveButton("取消", new DialogInterface.OnClickListener() {
//					@Override
//					public void onClick(DialogInterface arg0, int arg1) {
//						arg0.dismiss();
//						
//					}
//				});
				ad.create().show();
				
	        }else {
//	        	initGPS();
	        	intent.setClass(LoginActivity.this, RegisteInfoActivity.class);
				startActivity(intent);
				LoginActivity.this.finish();
			
	        }
			break;
		case R.id.btn_login:// 登录
			final String username = edt_username.getText().toString().trim();
			final String password = edt_userpassword.getText().toString()
					.trim();
			if (TextUtils.isEmpty(username) || TextUtils.isEmpty(password)) {
				ToastUtil.shortToast(getBaseContext(), "用户名或密码不能为空");
			} else {
				JSONObject obj = new JSONObject();
				try {
					obj.put("mobile", username);
					obj.put("password", MD5Util.MD5Encode(password));
					obj.put("deviceId", deviceId);
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				// LoadingProgressDialog.getInstance(this).show();
				dialog.show();
				AsyncHttpUtils.doPostJson(getApplicationContext(), MCUrl.LOGIN,
						obj.toString(), new AsyncHttpResponseHandler() {

							@Override
							public void onSuccess(int arg0, Header[] arg1,
									byte[] arg2) {
								dialog.dismiss();
								Log.e("code", arg0 + "" + new String(arg2));
							  bean = new Gson().fromJson(
										new String(arg2), RegisterBean.class);
								Log.e("errcode", bean.getErrCode() + "");
								switch (bean.getErrCode()) {
								case -5:
									PreferencesUtils.putString(getApplicationContext(), PreferenceConstants.REPEAT, username);
									showMyDialog(bean.getMessage(),JudgeActivity.class,new Intent());
									break;
								case -3:
									ToastUtil.shortToast(
											getApplicationContext(),
											bean.getMessage());
									break;
								case -2:
									ToastUtil.shortToast(
											getApplicationContext(),
											bean.getMessage());
									break;
								case -1:
									ToastUtil.shortToast(
											getApplicationContext(),
											bean.getMessage());
									break;
								case 0:
									PreferencesUtils.putString(getApplicationContext(), PreferenceConstants.PASSWORD, bean.getData().get(0).userCode);
									PreferencesUtils.putInt(getApplicationContext(), PreferenceConstants.UID, bean.data.get(0).userId);	
									PreferencesUtils.putString(getApplicationContext(), PreferenceConstants.USERTYPE,bean.getData().get(0).userType);
//									PreferencesUtils.putString(getApplicationContext(), PreferenceConstants.CITYCODE,bean.getData().get(0).cityCode);
									sendEmptyBackgroundMessage(MsgConstants.MSG_01);
									PreferencesUtils.putBoolean(getApplicationContext(),PreferenceConstants.ISLOGIN, true);
									PreferencesUtils.putString(getApplicationContext(), PreferenceConstants.WLID, bean.getData().get(0).wlid);
									PreferencesUtils.putString(getApplicationContext(), PreferenceConstants.USERNAME, bean.getData().get(0).userName);
									PreferencesUtils.putBoolean(getApplicationContext(), PreferenceConstants.COMPLETE, bean.getData().get(0).completed);
									PreferencesUtils.putString(getApplicationContext(), PreferenceConstants.IDCARD, bean.getData().get(0).idCard);
									PreferencesUtils.putString(getApplicationContext(), PreferenceConstants.ICON_PATH,bean.getData().get(0).idCardPath);
									PreferencesUtils.putString(getApplicationContext(), PreferenceConstants.MOBILE,bean.getData().get(0).mobile);
									PreferencesUtils.putString(getApplicationContext(), PreferenceConstants.PAYPASSWORD,bean.getData().get(0).payPassword);
								
									Log.e("+++++++++++++", PreferencesUtils.getString(getApplicationContext(), PreferenceConstants.USERTYPE)+"559999955");
									 PreferencesUtils.putString(getApplicationContext(),
									 PreferenceConstants.REALMANAUTH, bean.data.get(0).realManAuth);
										//海南用户标识
										PreferencesUtils.putString(getApplicationContext(), PreferenceConstants.AgreementType,bean.getData().get(0).agreementType);
									//商户标识
										PreferencesUtils.putString(getApplicationContext(), PreferenceConstants.shopType,bean.getData().get(0).shopType);
									 
//									 Intent intent = new Intent(LoginActivity.this, MainActivity.class);
//									 Intent intent = new Intent(LoginActivity.this, MainTab.class);
									 Intent intent=new Intent(LoginActivity.this,NewMainActivity.class);
										intent.putExtra("type", "1");
										startActivity(intent);
									finish();
//									//注册环信：
//									
//									new Thread(new Runnable() {
//									    public void run() {
//									      try {
//									         // 调用sdk注册方法
//									         EMChatManager.getInstance().createAccountOnServer(bean.getData().get(0).userId+"", password);
//									      } catch (final EaseMobException e) {
//									      //注册失败
//											int errorCode=e.getErrorCode();
//											if(errorCode==EMError.NONETWORK_ERROR){
//											    Toast.makeText(getApplicationContext(), "网络异常，请检查网络！", Toast.LENGTH_SHORT).show();
//											}else if(errorCode==EMError.USER_ALREADY_EXISTS){
//											    Toast.makeText(getApplicationContext(), "用户已存在！", Toast.LENGTH_SHORT).show();
//											}else if(errorCode==EMError.UNAUTHORIZED){
//												Toast.makeText(getApplicationContext(), "注册失败，无权限！", Toast.LENGTH_SHORT).show();
//											}else{
//												Toast.makeText(getApplicationContext(), "注册失败: " + e.getMessage(), Toast.LENGTH_SHORT).show();
//									      }
//									   }
//									    }
//									}).start();
//									
//									//登录环信；
//									
//									EMChatManager.getInstance().login(bean.getData().get(0).userId+"", password,new EMCallBack() {//回调
//										@Override
//										public void onSuccess() {
//											runOnUiThread(new Runnable() {
//												public void run() {
//													EMGroupManager.getInstance().loadAllGroups();
//													EMChatManager.getInstance().loadAllConversations();
//													Log.d("main", "登录聊天服务器成功！");		
//												}
//											});
//										}
//									 
//										@Override
//										public void onProgress(int progress, String status) {
//									 
//										}
//									 
//										@Override
//										public void onError(int code, String message) {
//											Log.d("main", "登录聊天服务器失败！");
//										}
//									});
//									
									JSONObject obj = new JSONObject();
									
									try {
										// obj.put("userId",
										// PreferencesUtils.getInt(getApplicationContext(),
										// PreferenceConstants.UID));
										obj.put("userId", PreferencesUtils.getInt(
												getApplicationContext(), PreferenceConstants.UID));
										obj.put("userType", PreferencesUtils.getString(getApplicationContext(), PreferenceConstants.USERTYPE));
										obj.put("latitude", latitude);
										obj.put("longitude", longitude);
									} catch (JSONException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}
									AsyncHttpUtils.doPostJson(getApplicationContext(), MCUrl.UPLOADMYLOCATION,
											obj.toString(), new AsyncHttpResponseHandler() {

												@Override
												public void onSuccess(int arg0, Header[] arg1,
														byte[] arg2) {
													Log.e("___00000000000000_", new String(arg2));
													dialog.dismiss();
													if (arg2 == null || arg2.length == 0) {
//														ToastUtil.shortToast(getApplicationContext(),
//																"获取位置失败");
														Log.e("__111111111111111__", "获取位置失败");
														return;
													}
													BaseBean bean = new Gson().fromJson(
															new String(arg2), BaseBean.class);
													if (bean.isSuccess() == true) {
//														ToastUtil.shortToast(getApplicationContext(),
//																bean.getMessage());
														Log.e("__222222222222___", bean.getMessage());
														
														finish();
													} else {
//														ToastUtil.shortToast(getApplicationContext(),
//																bean.getMessage());
														Log.e("_33333333333333___", bean.getMessage());
													}
												}

												@Override
												public void onFailure(int arg0, Header[] arg1,
														byte[] arg2, Throwable arg3) {
													dialog.dismiss();
//													ToastUtil.shortToast(getApplicationContext(),
//															"获取位置失败");
													BaseBean bean = new Gson().fromJson(
															new String(arg2), BaseBean.class);
													Log.e("_4444444444444444__", bean.getMessage());
													// TODO Auto-generated method stub
													Log.e("ooo1", arg0 + "");
												}
											});
									
									finish();
									break;
								default:
									break;
								}
							}

							@Override
							public void onFailure(int arg0, Header[] arg1,
									byte[] arg2, Throwable arg3) {
								Logger.e("11111111   login");
								// LoadingProgressDialog.getInstance(LoginActivity.this).dismiss();
								ToastUtil.shortToast(getApplicationContext(),
										"网络请求出错，请检查网络");
							}
						});
			}
			break;
		case R.id.imgbtn_qq:// QQ登录
			dialog=new LoadingProgressDialog(LoginActivity.this);
			dialog.show();		
			plat = ShareSDK.getPlatform(LoginActivity.this, QQ.NAME);
			authorize(plat);
			break;
		case R.id.imgbtn_wx:// 微信登录
			if (latitude==4.9E-324 || longitude==4.9E-324) {  
	         	Builder ad = new Builder(LoginActivity.this);
	         	  ad.setTitle("温馨提示");  
		           ad.setMessage("为方便您的使用，请您在手机权限管理中允许软件定位");  
				ad.setNegativeButton("确认", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface arg0, int arg1) {
						arg0.dismiss();
//						ToastUtil.longToast(LoginActivity.this, "");
//						Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);  
//	                    startActivityForResult(intent, 0); // 设置完成后返回到原来的界面  
					}
				});
//				ad.setPositiveButton("取消", new DialogInterface.OnClickListener() {
//					@Override
//					public void onClick(DialogInterface arg0, int arg1) {
//						arg0.dismiss();
//						
//					}
//				});
				ad.create().show();
				
	        }else {
			dialog=new LoadingProgressDialog(LoginActivity.this);
			dialog.show();
			plat = ShareSDK.getPlatform(LoginActivity.this, Wechat.NAME);
			authorize(plat);
	        }
			break;
		}
	}
	@Override
	protected void onResume() {
		super.onResume();
		
	}

	/**
	 * 主界面点击2次退出APP
	 */
	@Override
	public boolean dispatchKeyEvent(KeyEvent event) {
		if (event.getKeyCode() == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
			exit();// 退出
			return true;
		}
		return super.dispatchKeyEvent(event);
	}

	public void exit() {
		if ((System.currentTimeMillis() - exitTime) > 2000) {
			ToastUtil.shortToastByRec(getApplicationContext(), R.string.exitapp);
			exitTime = System.currentTimeMillis();
		} else {
			// finish();
			iWantApplication.getInstance().exit();
			System.exit(0);
		}
	}
	public void showMyDialog(String message,final Class clazz,final Intent intent){
		DialogUtils
		.createAlertDialogTwo(
				LoginActivity.this,
				"温馨提示",
				message,
				0,
				true,
				false,
				"确认",
				"取消",
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(
							DialogInterface dialog,
							int which){
						intent.setClass(LoginActivity.this, clazz);
						startActivity(intent);
					}
				},
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(
							DialogInterface dialog,
							int which) {

					}
				}).show();
}
	@Override
	public void initView() {
		
		client = new LocationClient(LoginActivity.this);
		initLocation();
		client.registerLocationListener(new BDLocationListener() {

			@Override
			public void onReceiveLocation(BDLocation arg0) {
				if (arg0 == null) {
					ToastUtil.shortToast(LoginActivity.this, "定位失败，请检查设置");
					return;
				} else {
//					ToastUtil.shortToast(LoginWeixinActivity.this, "定设置"+ arg0.getLatitude());
				latitude = arg0.getLatitude();
				longitude = arg0.getLongitude();
				city=arg0.getCity();
				townaddressd=arg0.getDistrict();
				getCityCode();
//				townaddressd=arg0.getDistrict();
				Log.e("jpppp", latitude + ":::::::::" + longitude);
				}
			}
		});
		// 初始化定位
		// 打开GPS
		client.start();
		
//     if(isWeixinAvilible(getApplicationContext())){
//    	 Intent intent=new Intent();
//    	 intent.setClass(LoginActivity.this,LoginWeixinActivity.class);
//			startActivity(intent);
////			finish();
//		}
		// 主题
		// 用户名密码
		edt_username = (EditText) findViewById(R.id.edt_username);
		edt_userpassword = (EditText) findViewById(R.id.edt_userpassword);
		// 注册和登录及找回密码
		findpassword = (TextView) findViewById(R.id.findpassword);
		hurryregiste = (TextView) findViewById(R.id.hurryregiste);
		btn_login = (Button) findViewById(R.id.btn_login);
		// 第三方登录按钮
		imgbtn_qq = (ImageButton) findViewById(R.id.imgbtn_qq);
		imgbtn_wx = (ImageButton) findViewById(R.id.imgbtn_wx);
		
	}
	 /**
     * 判断微信是否可用
     * 
     * @param context
     * @return
     */
	public static boolean isWeixinAvilible(Context context) {
        final PackageManager packageManager = context.getPackageManager();// 获取packagemanager
        List<PackageInfo> pinfo = packageManager.getInstalledPackages(0);// 获取所有已安装程序的包信息
        if (pinfo != null) {
            for (int i = 0; i < pinfo.size(); i++) {
                String pn = pinfo.get(i).packageName;
                if (pn.equals("com.tencent.mm")) {
                    return true;
                }
            }
        }

        return false;
    }

    /**
     * 判断qq是否可用
     * 
     * @param context
     * @return
     */
    public static boolean isQQClientAvailable(Context context) {
        final PackageManager packageManager = context.getPackageManager();
        List<PackageInfo> pinfo = packageManager.getInstalledPackages(0);
        if (pinfo != null) {
            for (int i = 0; i < pinfo.size(); i++) {
                String pn = pinfo.get(i).packageName;
                if (pn.equals("com.tencent.mobileqq")) {
                    return true;
                }
            }
        }
        return false;
    }
	@Override
	public void initData() {
		dialog = new LoadingProgressDialog(this);
		ShareSDK.initSDK(this);
	}

	@Override
	public void setOnClick() {
		// 登录 注册 找回密码
		findpassword.setOnClickListener(this);
		hurryregiste.setOnClickListener(this);
		btn_login.setOnClickListener(this);
		// 第三方登录
		imgbtn_qq.setOnClickListener(this);
		imgbtn_wx.setOnClickListener(this);
	}

	@Override
	public void getData() {
		// TODO Auto-generated method stub

	}

	@Override
	public void handleBackgroundMessage(Message msg) {
		// TODO Auto-generated method stub
		super.handleBackgroundMessage(msg);
		switch (msg.what) {
		case MsgConstants.MSG_01:
			Set<String> tag=new HashSet<String>();
			if (PreferencesUtils.getString(getApplicationContext(), PreferenceConstants.USERTYPE).equals("1")) {
				tag.add("user");			
			}else if (PreferencesUtils.getString(getApplicationContext(), PreferenceConstants.USERTYPE).equals("2")) {
				tag.add("courier");
			}else if (PreferencesUtils.getString(getApplicationContext(), PreferenceConstants.USERTYPE).equals("3")) {
				tag.add("driver");
			}
			JPushInterface.setAliasAndTags(getApplicationContext(), String.valueOf(PreferencesUtils.getInt(getApplicationContext(), PreferenceConstants.UID)),tag,new TagAliasCallback() {
				@Override
				public void gotResult(int i, String s, Set<String> set) {
					if (i == 0) {
						Log.d("Jpush", s);
					} else {
						Log.d("Jpush", "error code is " + i);
					}
				}
			});
			break;

		default:
			break;
		}
	}
	private void login(final PlatformDb platDB,String unionid) {
		// 通过DB获取各种数据
		Log.e("userId", platDB.getUserId() + "");
		Log.e("token", platDB.getToken());
		JSONObject obj = new JSONObject();
		try {
			obj.put("openId", platDB.getUserId());
			obj.put("accessToken", platDB.getToken());
			obj.put("nickName", platDB.getUserName());
			obj.put("sex", platDB.getUserGender());
			obj.put("headImageUrl", platDB.getUserIcon());
			obj.put("unionId", unionid);
			obj.put("deviceId",deviceId);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Log.e("ee", obj.toString());
		AsyncHttpUtils.doPostJson(getApplicationContext(), MCUrl.THIRDLOGIN,
				obj.toString(), new AsyncHttpResponseHandler() {

					@Override
					public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
						dialog.dismiss();
						// TODO Auto-generated method stub
						if (arg2 == null) {
							ToastUtil.shortToast(getApplicationContext(),
									"登录失败");
						}
						RegisterBean bean = new Gson().fromJson(
								new String(arg2), RegisterBean.class);
						if (bean != null&&bean.data.size()!=0)
						{
							if(bean.getErrCode()==0){
							PreferencesUtils.putString(getApplicationContext(), PreferenceConstants.PASSWORD, bean.getData().get(0).userCode);
							PreferencesUtils.putInt(getApplicationContext(), PreferenceConstants.UID, bean.data.get(0).userId);		
							PreferencesUtils.putString(getApplicationContext(), PreferenceConstants.USERTYPE,bean.getData().get(0).userType);
							sendEmptyBackgroundMessage(MsgConstants.MSG_01);
							PreferencesUtils.putBoolean(
									getApplicationContext(),
									PreferenceConstants.ISLOGIN, true);
							PreferencesUtils.putBoolean(
									getApplicationContext(),
									PreferenceConstants.ThIRDLOGIN, true);
							
							PreferencesUtils.putString(getApplicationContext(), PreferenceConstants.HeadPath, bean.getData().get(0).headPath);
							PreferencesUtils.putString(getApplicationContext(), PreferenceConstants.USERNAME, bean.getData().get(0).userName);
							PreferencesUtils.putBoolean(getApplicationContext(), PreferenceConstants.COMPLETE, bean.getData().get(0).completed);
							PreferencesUtils.putString(getApplicationContext(), PreferenceConstants.IDCARD, bean.getData().get(0).idCard);
							PreferencesUtils.putString(getApplicationContext(), PreferenceConstants.ICON_PATH,bean.getData().get(0).idCardPath);
							PreferencesUtils.putString(getApplicationContext(), PreferenceConstants.MOBILE,bean.getData().get(0).mobile);
							PreferencesUtils.putString(getApplicationContext(), PreferenceConstants.PAYPASSWORD,bean.getData().get(0).payPassword);
							 PreferencesUtils.putString(getApplicationContext(),
							 PreferenceConstants.REALMANAUTH, bean.data.get(0).realManAuth);
								//海南用户标识
								PreferencesUtils.putString(getApplicationContext(), PreferenceConstants.AgreementType,bean.getData().get(0).agreementType);
							//商户标识
								PreferencesUtils.putString(getApplicationContext(), PreferenceConstants.shopType,bean.getData().get(0).shopType);
							 
//							 Intent intent = new Intent(LoginActivity.this, MainTab.class);
//							 Intent intent = new Intent(LoginActivity.this, MainActivity.class);
							 Intent intent=new Intent(LoginActivity.this,NewMainActivity.class);
								intent.putExtra("type", "1");
								startActivity(intent);
							finish();
							}
							if(bean.getErrCode()==-5){
								Intent intent=new Intent();
								intent.putExtra("openId", platDB.getUserId());
								intent.putExtra("accessToken", platDB.getToken());
								intent.putExtra("nickName", platDB.getUserName());
								intent.putExtra("sex", platDB.getUserGender());
								intent.putExtra("headImageUrl", platDB.getUserIcon());
								intent.putExtra("mobile", bean.data.get(0).mobile);
								//ThirdBean bean=new Gson().fromJson(new , classOfT)
								showMyDialog("请验证手机号",JudgeSmsActivity.class,intent);
							}
						}
							if(bean.getErrCode()==-3){
								Intent intent=new Intent();
								intent.putExtra("openId", platDB.getUserId());
								intent.putExtra("accessToken", platDB.getToken());
								intent.putExtra("nickName", platDB.getUserName());
								intent.putExtra("sex", platDB.getUserGender());
								intent.putExtra("headImageUrl", platDB.getUserIcon());
								showMyDialog("为了您的账号安全请绑定手机号！",BindPhoneActivity.class,intent);
							}			
					}

					@Override
					public void onFailure(int arg0, Header[] arg1, byte[] arg2,
							Throwable arg3) {
						// TODO Auto-generated method stub
						ToastUtil.shortToast(getApplicationContext(),
								"登录授权失败");
					}
				});
	}

	private void authorize(Platform plat) {
		if (plat == null)
			return;
		plat.removeAccount(true);
		ShareSDK.removeCookieOnAuthorize(true);
		plat.setPlatformActionListener(this);
		plat.SSOSetting(false);
		plat.showUser(null);
		// plat.authorize();
	}

	public void onComplete(Platform platform, int action,
			HashMap<String, Object> res) {
		if (action == Platform.ACTION_USER_INFOR) {
			/*runOnUiThread(new Runnable() {
				
				@Override
				public void run() {
					// TODO Auto-generated method stub
					dialog=new LoadingProgressDialog(LoginActivity.this);
					dialog.show();
					finish();
				}
			});*/
			UIHandler.sendEmptyMessage(MSG_AUTH_COMPLETE, this);
		}
	}

	public void onError(Platform platform, int action, Throwable t) {
		if (action == Platform.ACTION_USER_INFOR) {
			Log.e("ee", t.getMessage());
			UIHandler.sendEmptyMessage(MSG_AUTH_ERROR, this);
		}
		t.printStackTrace();
	}

	public void onCancel(Platform platform, int action) {
		if (action == Platform.ACTION_USER_INFOR) {
			UIHandler.sendEmptyMessage(MSG_AUTH_CANCEL, this);
		}
	}

	public boolean handleMessage(Message msg) {
		switch (msg.what) {
		case MSG_USERID_FOUND:
			Toast.makeText(this, "found", Toast.LENGTH_SHORT).show();
			break;
		case MSG_AUTH_CANCEL: {
			Toast.makeText(this, "cancle", Toast.LENGTH_SHORT).show();
			System.out.println("-------MSG_AUTH_CANCEL--------");
		}
			break;
		case MSG_AUTH_ERROR: {
			Toast.makeText(this, "error", Toast.LENGTH_SHORT).show();
			System.out.println("-------MSG_AUTH_ERROR--------");
		}
			break;
		case MSG_AUTH_COMPLETE: {
			/*if ("QZone".equals(plat.getName())) {
				login_type = 1;
			}*/
			unind(plat.getDb());
			
			System.out.println("--------MSG_AUTH_COMPLETE-------");
		}
		
			break;
		}
		return false;

	}
	private void unind(final PlatformDb platDB) {
		// 通过DB获取各种数据
		Log.e("userId", platDB.getUserId() + "");
//		ToastUtil.longToast(LoginActivity.this, platDB.getUserId() + "");
		Log.e("token", platDB.getToken());
		JSONObject obj = new JSONObject();
		try {
			obj.put("openId", platDB.getUserId());
			obj.put("accessToken", platDB.getToken());
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		RequestParams pa=new RequestParams();
//		pa.add("openId", platDB.getUserId());
//		pa.add("accessToken", platDB.getToken());
		Log.e("ee", obj.toString());
		String uString="https://api.weixin.qq.com/sns/userinfo";
		AsyncHttpUtils.doGet( UrlMap.getTwo(uString,"openId", platDB.getUserId(),"accessToken", platDB.getToken()),null,null,
				pa, new AsyncHttpResponseHandler() {

					@Override
					public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
						dialog.dismiss();
						// TODO Auto-generated method stub
						Log.e("111111   ",""+arg2);
						if (arg2 == null) {}
						unidbean = new Gson().fromJson(
								new String(arg2), Unidbean.class);
						unidbean.getUnionid();
						login(plat.getDb(),unidbean.getUnionid());
					}
					@Override
					public void onFailure(int arg0, Header[] arg1, byte[] arg2,
							Throwable arg3) {
						// TODO Auto-generated
					}
				});
	}
	 private void initLocation() {
			LocationClientOption option = new LocationClientOption();
			option.setLocationMode(com.baidu.location.LocationClientOption.LocationMode.Hight_Accuracy);// 可选，默认高精度，设置定位模式，高精度，低功耗，仅设备
			option.setCoorType("bd09ll");// 可选，默认gcj02，设置返回的定位结果坐标系
			int span = 0;
			option.setScanSpan(span);// 可选，默认0，即仅定位一次，设置发起定位请求的间隔需要大于等于1000ms才是有效的
			option.setIsNeedAddress(true);// 可选，设置是否需要地址信息，默认不需要
			option.setOpenGps(false);// 可选，默认false,设置是否使用gps
			option.setLocationNotify(false);// 可选，默认false，设置是否当gps有效时按照1S1次频率输出GPS结果
			option.setIsNeedLocationDescribe(true);// 可选，默认false，设置是否需要位置语义化结果，可以在BDLocation.getLocationDescribe里得到，结果类似于“在北京天安门附近”
			option.setIsNeedLocationPoiList(true);// 可选，默认false，设置是否需要POI结果，可以在BDLocation.getPoiList里得到
			option.setIgnoreKillProcess(false);// 可选，默认true，定位SDK内部是一个SERVICE，并放到了独立进程，设置是否在stop的时候杀死这个进程，默认不杀死
			option.SetIgnoreCacheException(false);// 可选，默认false，设置是否收集CRASH信息，默认收集
			option.setEnableSimulateGps(false);// 可选，默认false，设置是否需要过滤gps仿真结果，默认需要
			client.setLocOption(option);
		}
	 private void getCityCode() {
			boolean isCopySuccess = CheckDbUtils.checkDb();
			// 成功的将数据库copy到data 中
			if (isCopySuccess) {
				iWantApplication.getInstance().mDbManager = new DbManager(iWantApplication.getInstance());
			}
			if (city == null || city.equals("")) {
//				ToastUtil.shortToast(getActivity(), "请输入完整信息");
				return;
			}
			if (!city.contains("市")) {
				city = city + "市";
			}
			List<CityBean> selectDataFromDb = new CityDbOperation()
					.selectDataFromDb("select * from city where city_name='" + city + "'");
			if (selectDataFromDb.size() > 0 && !selectDataFromDb.equals("")) {
				cityCode = selectDataFromDb.get(0).city_code;
				Log.e("citycode", cityCode);
				List<AreaBean> selectDataFromDbs = new AreaDboperation()
						.selectDataFromDb("select * from area where area_name='" + townaddressd + "'");
				if (selectDataFromDbs.size()>0 && !selectDataFromDbs.equals("")) {
					townCode=selectDataFromDbs.get(0).area_code;
					Log.e("11111townCode", townCode);
			}
			}
		}

		@Override
		protected void onDestroy() {
			super.onDestroy();
			ShareSDK.stopSDK(this);
			client.stop();
		}
		 private void initGPS() {  
		        LocationManager locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);  
		        // 判断GPS模块是否开启，如果没有则开启  
		        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
//		            Toast.makeText(LoginWeixinActivity.this, "请打开GPS", Toast.LENGTH_SHORT).show();  
		            final Builder dialog = new Builder(this);
		            dialog.setTitle("温馨提示");  
		            dialog.setMessage("为方便您的使用，请您打开GPS");  
		            dialog.setPositiveButton("设置", new DialogInterface.OnClickListener() {
		                @Override  
		                public void onClick(DialogInterface arg0, int arg1) {  
		                    // 转到手机设置界面，用户设置GPS  
		                    Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);  
//		                    Toast.makeText(LoginWeixinActivity.this, "打开后直接点击返回键即可，若不打开返回下次将再次出现", Toast.LENGTH_SHORT).show();  
		                    startActivityForResult(intent, 0); // 设置完成后返回到原来的界面  
		                }  
		            });  
		            dialog.setNeutralButton("取消", new DialogInterface.OnClickListener() {
		                @Override  
		                public void onClick(DialogInterface arg0, int arg1) {  
		                    arg0.dismiss();  
		                }  
		            });  
		            dialog.show();  
		        } else {  
		        	Intent intent=new Intent();
		        	intent.setClass(LoginActivity.this, RegisteInfoActivity.class);
					startActivity(intent);
					LoginActivity.this.finish();
		        	
		        }  
		    }  
}
