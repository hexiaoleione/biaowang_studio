package com.hex.express.iwant.activities;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.Handler.Callback;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.google.gson.Gson;
import com.hex.express.iwant.R;
import com.hex.express.iwant.iWantApplication;
import com.hex.express.iwant.bean.AreaBean;
import com.hex.express.iwant.bean.CityBean;
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
import com.hex.express.iwant.utils.PreferencesUtils;
import com.hex.express.iwant.utils.ToastUtil;
import com.hex.express.iwant.viewpager.ADInfo;
import com.hex.express.iwant.viewpager.CycleViewPager;
import com.hex.express.iwant.viewpager.CycleViewPager.ImageCycleViewListener;
import com.hex.express.iwant.views.DialogUtils;
import com.hex.express.iwant.views.GifView;
import com.hex.express.iwant.views.LoadingProgressDialog;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.mob.tools.utils.UIHandler;
import com.hex.express.iwant.viewpager.ViewFactory;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Message;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import butterknife.ButterKnife;
import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.PlatformDb;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.wechat.friends.Wechat;

public class LoginWeixinActivity extends BaseActivity implements PlatformActionListener, Callback {
    private static final int MSG_USERID_FOUND = 1;
    private static final int MSG_LOGIN = 2;
    private static final int MSG_AUTH_CANCEL = 3;
    private static final int MSG_AUTH_ERROR = 4;
    private static final int MSG_AUTH_COMPLETE = 5;
    private String deviceId;
    private List<ImageView> views = new ArrayList<ImageView>();
    private List<ADInfo> infos = new ArrayList<ADInfo>();
    private CycleViewPager cycleViewPager;
    private TextView btnLeft, btnRight;
    private GifView imgbtn_wx;
    private String[] imageUrls = {"http://www.efamax.com/images/lunbo_0.jpg",
            "http://www.efamax.com/images/lunbo_1.jpg",
            "http://www.efamax.com/images/lunbo_2.jpg"};
    private static String url = iWantApplication.BASE_URL + "login.action?";
    LoadingProgressDialog dialog;
    private Platform plat;
    Unidbean unidbean;

    private LocationClient client;//百度地图的Client
    private double latitude;
    private double longitude;
    private String code;
    private String city, cityCode, townCode, townaddressd;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loginweixin);
        iWantApplication.getInstance().addActivity(this);
        ButterKnife.bind(this);

        deviceId = DataTools.getDeviceId(getBaseContext());
        ShareSDK.initSDK(this);
        initView();
        configImageLoader();
        initialize();
//		initView();

    }

    @Override
    public void onWeightClick(View v) {
        // TODO Auto-generated method stub

    }

    @Override
    public void initView() {

        client = new LocationClient(LoginWeixinActivity.this);
        initLocation();
        client.registerLocationListener(new BDLocationListener() {

            @Override
            public void onReceiveLocation(BDLocation arg0) {
                if (arg0 == null) {
                    ToastUtil.shortToast(LoginWeixinActivity.this, "定位失败，请检查设置");
                    return;
                } else {
//					ToastUtil.shortToast(LoginWeixinActivity.this, "定设置"+ arg0.getLatitude());
                    latitude = arg0.getLatitude();
                    longitude = arg0.getLongitude();
                    city = arg0.getCity();
                    townaddressd = arg0.getDistrict();
                    getCityCode();
//				townaddressd=arg0.getDistrict();
                    Log.e("jpppp", latitude + ":::::::::" + longitude);
                }
            }
        });
        // 初始化定位
        // 打开GPS
        client.start();

//	     if(isWeixinAvilible(getApplicationContext())){
//	    	 Intent intent=new Intent();
//	    	 intent.setClass(LoginWeixinActivity.this,LoginActivity.class);
//				startActivity(intent);
////				finish();
        imgbtn_wx = (GifView) findViewById(R.id.imgbtn_wxs);
        dialog = new LoadingProgressDialog(this);

        imgbtn_wx.setMovieResource(R.drawable.weixinbar200s);

        btnLeft = (TextView) findViewById(R.id.btnLeft);
        btnLeft.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
//				Intent intent=new Intent(LoginWeixinActivity.this,MainActivity.class);
//				Intent intent=new Intent(LoginWeixinActivity.this,MainTab.class);
                Intent intent = new Intent(LoginWeixinActivity.this, NewMainActivity.class);

                intent.putExtra("type", "2");
                startActivity(intent);
//				finish();


            }
        });
        btnRight = (TextView) findViewById(R.id.btnRight);
        btnRight.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                Intent intent = new Intent(LoginWeixinActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();


            }
        });

        imgbtn_wx.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
//				dialog=new LoadingProgressDialog(LoginWeixinActivity.this);
//				dialog.show();
//				plat = ShareSDK.getPlatform(LoginWeixinActivity.this, Wechat.NAME);
//				authorize(plat); 
//				initGPS();
//				ToastUtil.shortToast(LoginWeixinActivity.this, ""+latitude);
                if (latitude == 4.9E-324 || longitude == 4.9E-324) {
                    Builder ad = new Builder(LoginWeixinActivity.this);
                    ad.setTitle("温馨提示");
                    ad.setMessage("为方便您的使用，请您在手机权限管理中允许软件定位");
                    ad.setNegativeButton("确认", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface arg0, int arg1) {

                            arg0.dismiss();
//							ToastUtil.longToast(LoginWeixinActivity.this, "");
//							Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);  
//		                    startActivityForResult(intent, 0); // 设置完成后返回到原来的界面  
                        }
                    });
//					ad.setPositiveButton("取消", new DialogInterface.OnClickListener() {
//						@Override
//						public void onClick(DialogInterface arg0, int arg1) {
//							arg0.dismiss();
//							
//						}
//					});
                    ad.create().show();

                } else {
//		        	initGPS();
                    dialog = new LoadingProgressDialog(LoginWeixinActivity.this);
                    dialog.show();
                    plat = ShareSDK.getPlatform(LoginWeixinActivity.this, Wechat.NAME);
                    authorize(plat);
                }

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
        plat.authorize();
    }

    @Override
    public void initData() {
        // TODO Auto-generated method stub

    }

    @Override
    public void setOnClick() {
        // TODO Auto-generated method stub

    }

    @Override
    public void getData() {
        // TODO Auto-generated method stub

    }

    private void login(final PlatformDb platDB, String unionid) {
        // 通过DB获取各种数据
        Log.e("userId", platDB.getUserId() + "");
        Log.e("token", platDB.getToken());
        Log.e("11111111111111unionid", unionid);
//		ToastUtil.longToast(LoginWeixinActivity.this, "111111111122    "+unionid);
        JSONObject obj = new JSONObject();
        try {
            obj.put("openId", platDB.getUserId());
            obj.put("accessToken", platDB.getToken());
            obj.put("nickName", platDB.getUserName());
            obj.put("sex", platDB.getUserGender());
            obj.put("headImageUrl", platDB.getUserIcon());
            obj.put("unionId", unionid);
            obj.put("deviceId", deviceId);
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
                        if (bean != null && bean.data.size() != 0) {
                            if (bean.getErrCode() == 0) {
                                PreferencesUtils.putString(getApplicationContext(), PreferenceConstants.PASSWORD, bean.getData().get(0).userCode);
                                PreferencesUtils.putInt(getApplicationContext(), PreferenceConstants.UID, bean.data.get(0).userId);
                                PreferencesUtils.putString(getApplicationContext(), PreferenceConstants.USERTYPE, bean.getData().get(0).userType);
                                sendEmptyBackgroundMessage(MsgConstants.MSG_01);
                                PreferencesUtils.putBoolean(
                                        getApplicationContext(),
                                        PreferenceConstants.ISLOGIN, true);
                                PreferencesUtils.putBoolean(
                                        getApplicationContext(),
                                        PreferenceConstants.ThIRDLOGIN, true);
//							PreferencesUtils.putString(getApplicationContext(), PreferenceConstants.CITYCODE,bean.getData().get(0).cityCode);
                                PreferencesUtils.putString(getApplicationContext(), PreferenceConstants.HeadPath, bean.getData().get(0).headPath);
                                PreferencesUtils.putString(getApplicationContext(), PreferenceConstants.WLID, bean.getData().get(0).wlid);
                                PreferencesUtils.putString(getApplicationContext(), PreferenceConstants.USERNAME, bean.getData().get(0).userName);
                                PreferencesUtils.putBoolean(getApplicationContext(), PreferenceConstants.COMPLETE, bean.getData().get(0).completed);
                                PreferencesUtils.putString(getApplicationContext(), PreferenceConstants.IDCARD, bean.getData().get(0).idCard);
                                PreferencesUtils.putString(getApplicationContext(), PreferenceConstants.ICON_PATH, bean.getData().get(0).idCardPath);
                                PreferencesUtils.putString(getApplicationContext(), PreferenceConstants.MOBILE, bean.getData().get(0).mobile);
                                PreferencesUtils.putString(getApplicationContext(), PreferenceConstants.PAYPASSWORD, bean.getData().get(0).payPassword);
                                PreferencesUtils.putString(getApplicationContext(),
                                        PreferenceConstants.REALMANAUTH, bean.data.get(0).realManAuth);
                                PreferencesUtils.putString(getApplicationContext(), PreferenceConstants.AgreementType, bean.getData().get(0).agreementType);
                                //商户标识
                                PreferencesUtils.putString(getApplicationContext(), PreferenceConstants.shopType, bean.getData().get(0).shopType);

//							startActivity(new Intent(LoginWeixinActivity.this,
//									MainActivity.class));
//							Intent intent = new Intent(LoginWeixinActivity.this, MainActivity.class);
//							Intent intent = new Intent(LoginWeixinActivity.this, MainTab.class);
                                Intent intent = new Intent(LoginWeixinActivity.this, NewMainActivity.class);
                                intent.putExtra("type", "1");
                                startActivity(intent);
                                finish();
                            }
                            if (bean.getErrCode() == -5) {
                                Intent intent = new Intent();
                                intent.putExtra("openId", platDB.getUserId());
                                intent.putExtra("accessToken", platDB.getToken());
                                intent.putExtra("nickName", platDB.getUserName());
                                intent.putExtra("sex", platDB.getUserGender());
                                intent.putExtra("headImageUrl", platDB.getUserIcon());
                                intent.putExtra("mobile", bean.data.get(0).mobile);
                                //ThirdBean bean=new Gson().fromJson(new , classOfT)
                                showMyDialog("请验证手机号", JudgeSmsActivity.class, intent);
                            }
                        }
                        if (bean.getErrCode() == -3) {
                            Intent intent = new Intent();
                            intent.putExtra("openId", platDB.getUserId());
                            intent.putExtra("accessToken", platDB.getToken());
                            intent.putExtra("nickName", platDB.getUserName());
                            intent.putExtra("sex", platDB.getUserGender());
                            intent.putExtra("headImageUrl", platDB.getUserIcon());
                            showMyDialog("为了您的账号安全请绑定手机号", BindPhoneActivity.class, intent);
                        }
                        if (bean.getErrCode() == -2) {
                            ToastUtil.shortToast(
                                    getApplicationContext(),
                                    bean.getMessage());
                        }
                        if (bean.getErrCode() == -1) {
                            ToastUtil.shortToast(
                                    getApplicationContext(),
                                    bean.getMessage());
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

    public void showMyDialog(String message, final Class clazz, final Intent intent) {
        DialogUtils
                .createAlertDialogTwo(
                        LoginWeixinActivity.this,
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
                                    int which) {
                                intent.setClass(LoginWeixinActivity.this, clazz);
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

    @SuppressLint("NewApi")
    private void initialize() {

        cycleViewPager = (CycleViewPager) getFragmentManager()
                .findFragmentById(R.id.fragment_cycle_viewpager_content);

        for (int i = 0; i < imageUrls.length; i++) {
            ADInfo info = new ADInfo();
            info.setUrl(imageUrls[i]);
            info.setContent("图片-->" + i);
            infos.add(info);
        }

        // 将最后一个ImageView添加进来
        views.add(ViewFactory.getImageView(this, infos.get(infos.size() - 1).getUrl()));
        for (int i = 0; i < infos.size(); i++) {
            views.add(ViewFactory.getImageView(this, infos.get(i).getUrl()));
        }
        // 将第一个ImageView添加进来
        views.add(ViewFactory.getImageView(this, infos.get(0).getUrl()));

        // 设置循环，在调用setData方法前调用
        cycleViewPager.setCycle(true);

        // 在加载数据前设置是否循环
        cycleViewPager.setData(views, infos, mAdCycleViewListener);
        //设置轮播
        cycleViewPager.setWheel(true);

        // 设置轮播时间，默认5000ms
        cycleViewPager.setTime(2000);
        //设置圆点指示图标组居中显示，默认靠右
        cycleViewPager.setIndicatorCenter();
    }

    private ImageCycleViewListener mAdCycleViewListener = new ImageCycleViewListener() {

        @Override
        public void onImageClick(ADInfo info, int position, View imageView) {
            if (cycleViewPager.isCycle()) {
                position = position - 1;
//				Toast.makeText(LoginWeixinActivity.this,
//						"position-->" + info.getContent(), Toast.LENGTH_SHORT)
//						.show();
            }

        }

    };

    /**
     * 配置ImageLoder
     */
    private void configImageLoader() {
        // 初始化ImageLoader
        @SuppressWarnings("deprecation")
        DisplayImageOptions options = new DisplayImageOptions.Builder().showStubImage(R.drawable.huandeng_wu700) // 设置图片下载期间显示的图片
                .showImageForEmptyUri(R.drawable.huandeng_shun700) // 设置图片Uri为空或是错误的时候显示的图片
                .showImageOnFail(R.drawable.huandeng_kuai700) // 设置图片加载或解码过程中发生错误显示的图片
                .cacheInMemory(true) // 设置下载的图片是否缓存在内存中
                .cacheOnDisc(true) // 设置下载的图片是否缓存在SD卡中
                // .displayer(new RoundedBitmapDisplayer(20)) // 设置成圆角图片
                .build(); // 创建配置过得DisplayImageOption对象

        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(getApplicationContext()).defaultDisplayImageOptions(options)
                .threadPriority(Thread.NORM_PRIORITY - 2).denyCacheImageMultipleSizesInMemory()
                .discCacheFileNameGenerator(new Md5FileNameGenerator()).tasksProcessingOrder(QueueProcessingType.LIFO).build();
        ImageLoader.getInstance().init(config);
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
        Log.e("token", platDB.getToken());
        JSONObject obj = new JSONObject();
        try {
            obj.put("openId", platDB.getUserId());
            obj.put("accessToken", platDB.getToken());

        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        RequestParams pa = new RequestParams();
//		pa.add("openId", platDB.getUserId());
//		pa.add("accessToken", platDB.getToken());
        Log.e("ee", obj.toString());
        String uString = "https://api.weixin.qq.com/sns/userinfo";
        AsyncHttpUtils.doGet(UrlMap.getTwo(uString, "openid", platDB.getUserId(), "access_token", platDB.getToken()), null, null,
                pa, new AsyncHttpResponseHandler() {

                    @Override
                    public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
                        dialog.dismiss();
                        // TODO Auto-generated method stub
                        Log.e("111111   ", "" + arg2);
                        if (arg2 == null) {
                        }
                        unidbean = new Gson().fromJson(
                                new String(arg2), Unidbean.class);
                        unidbean.getUnionid();
//						ToastUtil.longToast(LoginWeixinActivity.this, "1111111111    "+unidbean.getUnionid());
                        login(plat.getDb(), unidbean.getUnionid());
                    }

                    @Override
                    public void onFailure(int arg0, Header[] arg1, byte[] arg2,
                                          Throwable arg3) {
                        // TODO Auto-generated
                    }
                });
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

    private void initGPS() {
        LocationManager locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        // 判断GPS模块是否开启，如果没有则开启
        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
//	            Toast.makeText(LoginWeixinActivity.this, "请打开GPS", Toast.LENGTH_SHORT).show();  
            final Builder dialog = new Builder(this);
            dialog.setTitle("温馨提示");
            dialog.setMessage("为方便您的使用，请您打开GPS");
            dialog.setPositiveButton("设置", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface arg0, int arg1) {
                    // 转到手机设置界面，用户设置GPS
                    Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
//	                    Toast.makeText(LoginWeixinActivity.this, "打开后直接点击返回键即可，若不打开返回下次将再次出现", Toast.LENGTH_SHORT).show();  
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
//	        	   Toast.makeText(LoginWeixinActivity.this, "请打开GS", Toast.LENGTH_SHORT).show();  
            dialog = new LoadingProgressDialog(LoginWeixinActivity.this);
            dialog.show();
            plat = ShareSDK.getPlatform(LoginWeixinActivity.this, Wechat.NAME);
            authorize(plat);
        }
    }

    public static final boolean isOPen(final Context context) {
        LocationManager locationManager
                = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        // 通过GPS卫星定位，定位级别可以精确到街（通过24颗卫星定位，在室外和空旷的地方定位准确、速度快）
        boolean gps = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        // 通过WLAN或移动网络(3G/2G)确定的位置（也称作AGPS，辅助GPS定位。主要用于在室内或遮盖物（建筑群或茂密的深林等）密集的地方定位）
        boolean network = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        if (gps || network) {
            return true;
        }

        return false;
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
            if (selectDataFromDbs.size() > 0 && !selectDataFromDbs.equals("")) {
                townCode = selectDataFromDbs.get(0).area_code;
                Log.e("11111townCode", townCode);
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        client.stop();
    }
}

