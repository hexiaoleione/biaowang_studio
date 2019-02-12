package com.hex.express.iwant.newsmain;

import android.annotation.SuppressLint;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BaiduMap.OnMapClickListener;
import com.baidu.mapapi.map.BaiduMap.OnMapStatusChangeListener;
import com.baidu.mapapi.map.MapPoi;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.PoiInfo;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.google.gson.Gson;
import com.hex.express.iwant.R;
import com.hex.express.iwant.activities.DrawCardActivity;
import com.hex.express.iwant.activities.HAdvertActivity;
import com.hex.express.iwant.activities.LogiNumberActivity;
import com.hex.express.iwant.activities.LoginWeixinActivity;
import com.hex.express.iwant.activities.MessageActivity;
import com.hex.express.iwant.activities.NewExerciseActivity;
import com.hex.express.iwant.activities.NewMyWalletActivity;
import com.hex.express.iwant.bean.AdvertBean;
import com.hex.express.iwant.bean.AreaBean;
import com.hex.express.iwant.bean.BaseBean;
import com.hex.express.iwant.bean.CityBean;
import com.hex.express.iwant.bean.Loginuberbean;
import com.hex.express.iwant.bean.RegisterBean;
import com.hex.express.iwant.bean.VersionBean;
import com.hex.express.iwant.constance.MCUrl;
import com.hex.express.iwant.constance.PreferenceConstants;
import com.hex.express.iwant.helper.AreaDboperation;
import com.hex.express.iwant.helper.CheckDbUtils;
import com.hex.express.iwant.helper.CityDbOperation;
import com.hex.express.iwant.helper.DbManager;
import com.hex.express.iwant.http.AsyncHttpUtils;
import com.hex.express.iwant.http.UrlMap;
import com.hex.express.iwant.iWantApplication;
import com.hex.express.iwant.imageview.ImageCycleView;
import com.hex.express.iwant.imageview.ImageCycleView.ImageCycleViewListener;
import com.hex.express.iwant.newactivity.AnswerActivity;
import com.hex.express.iwant.newactivity.NewDepositActivity;
import com.hex.express.iwant.newactivity.PickActivity;
import com.hex.express.iwant.newactivity.PickToActivity;
import com.hex.express.iwant.newactivity.ReleaseActivity;
import com.hex.express.iwant.newactivity.ReleaseActivityto;
import com.hex.express.iwant.newbaidu.AroundPoiAdapter;
import com.hex.express.iwant.newbaidu.BaiduMapUtilByRacer;
import com.hex.express.iwant.newbaidu.BaiduMapUtilByRacer.GeoCodePoiListener;
import com.hex.express.iwant.newbaidu.BaiduMapUtilByRacer.LocateListener;
import com.hex.express.iwant.newbaidu.LocationBean;
import com.hex.express.iwant.newbaidu.LocationDemo;
import com.hex.express.iwant.newbaidu.SearchPoiAdapter;
import com.hex.express.iwant.slidingview.SlidingFragmentActivity;
import com.hex.express.iwant.slidingview.SlidingMenu;
import com.hex.express.iwant.utils.Logger;
import com.hex.express.iwant.utils.NormalLoadPictrue;
import com.hex.express.iwant.utils.PreferencesUtils;
import com.hex.express.iwant.utils.ToastUtil;
import com.hex.express.iwant.utils.UpdateChecker;
import com.hex.express.iwant.viewpager.ADInfo;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;

import org.apache.http.Header;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.TagAliasCallback;

/**
 * @author wuwenjie
 * @date 2014/11/14
 * @description 主界面
 */
public class NewMainActivity extends SlidingFragmentActivity implements
        OnClickListener {

    private ImageView topButton, btnfahuo, btnjiehuo, rigitButton, ivMLPLoading;
    private Fragment mContent;
    private TextView topTextView, tv_modifySenderAddress, newmianding;
    private EditText et_address, et_address_specific;
    private BaiduMap map;
    private MapView mMapView;
    private BaiduMap mBaiduMap;
    GeoCoder mSearch = null; // 经纬度地址
    private double latitude;// 经度
    private double longitude;// 纬度
    private double latitude2;
    private double longitude2;
    private boolean dingwei = false;
    private LocationClient client;
    private String cityName, city;// 城市名字
    private boolean isFirstLoc = true;// 首次定位
    private String cityCode;
    private VersionBean bean2;
    private String androidVersion = "";
    private PopupWindow window02;
    private AdvertBean adbean;
    private long exitTime;
    private Marker mMarker = null;
    //定位得到的地址
    private String address;
    //选择地址回调得到的地址
    private String return_address, fromcity;
    private boolean frist = false;// 是否第一次获取位置成功
    private String townCode, townaddressd, townaddressdto;
    private double mylatitude;// 经度
    private double mylongitude;// 纬度
    private double mylatitudeto;// 经度
    private double mylongitudeto;// 纬度
    private boolean diiiing = false;
    private boolean receive = true;
    // 收件人经纬度
    private double receiver_longitude;
    private double receiver_latitude;
    private String receiver_citycode;
    private String cityCode2, townCode2, townaddressd2;

    private static Context mContext;
    private LocationBean mLocationBean;
    private ArrayList<ADInfo> infos = new ArrayList<ADInfo>();
    ImageCycleView mAdView;
    private AdvertBean adbeans;

    ImageCycleView ad_viewse;

    ImageView ffdan, jjdan, yye, toubao;
    // 定位poi地名信息数据源
    private List<PoiInfo> aroundPoiList;
    private AroundPoiAdapter mAroundPoiAdapter;
    // 搜索模块，也可去掉地图模块独立使用
    // 搜索当前城市poi数据源
    private static List<LocationBean> searchPoiList;
    private SearchPoiAdapter mSearchPoiAdapter;
    public static final int SHOW_MAP = 0;
    private static final int SHOW_SEARCH_RESULT = 1;
    // 延时多少秒diss掉dialog
    private static final int DELAY_DISMISS = 1000 * 30;
    private String mycity;
    private ListView lvAroundPoi;
    private TextView tvShowLocation, tvShowLocationse;
    private ListView lvSearchPoi;
    private ImageButton ibMLLocate;
    private boolean gengxin = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE); // 无标题
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activit_newmain);
        iWantApplication.getInstance().addActivity(this);
        initSlidingMenu(savedInstanceState);
        if (Build.VERSION.SDK_INT >= 21) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION);
        }
        this.mContext = this;
        topButton = (ImageView) findViewById(R.id.topButtons);
        btnfahuo = (ImageView) findViewById(R.id.butfahuo);
        rigitButton = (ImageView) findViewById(R.id.rigitButtons);
        btnjiehuo = (ImageView) findViewById(R.id.btnjiehuo);
        et_address = (EditText) findViewById(R.id.et_address);
        et_address_specific = (EditText) findViewById(R.id.et_address_specific);
        tv_modifySenderAddress = (TextView) findViewById(R.id.tv_modifySenderAddress);
        configImageLoaderse();
        ad_viewse = (ImageCycleView) findViewById(R.id.ad_viewse);

        ffdan = (ImageView) findViewById(R.id.ffdan);
        jjdan = (ImageView) findViewById(R.id.jjdan);
        yye = (ImageView) findViewById(R.id.yye);
        toubao = (ImageView) findViewById(R.id.toubao);

        ivMLPLoading = (ImageView) findViewById(R.id.ivMLPLoading);
        tvShowLocation = (TextView) findViewById(R.id.tvShowLocation);
        tvShowLocationse = (TextView) findViewById(R.id.tvShowLocationse);
        lvSearchPoi = (ListView) findViewById(R.id.lvMLCityPoi);
        lvAroundPoi = (ListView) findViewById(R.id.lvPoiList);
        ibMLLocate = (ImageButton) findViewById(R.id.ibMLLocate);
        newmianding = (TextView) findViewById(R.id.newmiandingse);

        topTextView = (TextView) findViewById(R.id.topTv);
        mMapView = (MapView) findViewById(R.id.bmapView);
        mBaiduMap = mMapView.getMap();
        mBaiduMap.setMapStatus(MapStatusUpdateFactory.zoomTo(16));
        mBaiduMap.setOnMapStatusChangeListener(mapStatusChangeListener);
        mBaiduMap.setOnMapClickListener(mapOnClickListener);
        mBaiduMap.getUiSettings().setZoomGesturesEnabled(true);// 缩放手势
        // 开启定位图层
        mBaiduMap.setMyLocationEnabled(true);

        checkSelfPermission();

        locate();
        //添加解决问题的就是这行代码
        getVersions();
        //copy 数据
        CheckDbUtils.checkDb();
        if (isLogin()) {
            fillData();
            Advertse();
            getrequstBalance();
            getMessageStutas();
            timer.schedule(task, 0, 10 * 60 * 1000);// 0s后执行task,经过10f再次执行
//				timer.schedule(task, 0, 1);// 0s后执行task,经过0s再次执行
        }
        Advert();
        dingwei();
        topButton.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                if (isLogin()) {
                    toggle();
//					Log.e("1111111", " 33333");
                } else {
                    startActivity(new Intent(NewMainActivity.this, LoginWeixinActivity.class));
                }
            }
        });
        rigitButton.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                if (isLogin()) {
                    startActivity(new Intent(NewMainActivity.this, MessageActivity.class));
                } else {
                    startActivity(new Intent(NewMainActivity.this, LoginWeixinActivity.class));
                }
            }
        });
        //我要接单
        btnjiehuo.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                Intent intent = new Intent();
                if (isLogin()) {
                    intent.setClass(NewMainActivity.this, AnswerActivity.class);
//					intent.putExtra("", 1+"");
                    startActivity(intent);
                } else {
                    startActivity(new Intent(NewMainActivity.this, LoginWeixinActivity.class));
                }

            }
        });
        //newmianding  tv_modifySenderAddress

        newmianding.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub

                if (isLogin()) {
                    startActivityForResult(new Intent(NewMainActivity.this, LocationDemo.class), 8);
                } else {
                    startActivity(new Intent(NewMainActivity.this, LoginWeixinActivity.class));
                }

            }
        });
//		SlidingMenu slidingMenu=new SlidingMenu(mContext);
//		slidingMenu.setOnClosedListener(new SlidingMenu.OnClosedListener() {
//			
//			@Override
//			public void onClosed() {
//				// TODO Auto-generated method stub
//				Log.e("111111", "关闭完毕");
//			}
//		});
//       slidingMenu.setOnCloseListener(new SlidingMenu.OnCloseListener() {
//
//		@Override
//		public void onClose() {
//			// TODO Auto-generated method stub
//			Log.e("111111", "开始完毕");
//		}
//   	});
//    	   
//		  mp.start();  
        //我的发单
        ffdan.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
//				Intent intent = new Intent(NewMainActivity.this,MediaplayService.class);  
//				startService(intent); 	
                if (isLogin()) {
                    startActivity(new Intent(NewMainActivity.this, PickActivity.class));
                } else {
                    startActivity(new Intent(NewMainActivity.this, LoginWeixinActivity.class));
                }

            }
        });
        //我的接单
        jjdan.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                if (isLogin()) {
                    startActivity(new Intent(NewMainActivity.this, PickToActivity.class));
                } else {
                    startActivity(new Intent(NewMainActivity.this, LoginWeixinActivity.class));
                }
            }
        });
        //余额
        yye.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub

                if (isLogin()) {
                    startActivity(new Intent(NewMainActivity.this, NewMyWalletActivity.class));
                } else {
                    startActivity(new Intent(NewMainActivity.this, LoginWeixinActivity.class));
                }
            }
        });
        //投保
        toubao.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub

                if (isLogin()) {
//				startActivity(new Intent(NewMainActivity.this, DepositNewActivity.class));
                    startActivity(new Intent(NewMainActivity.this, NewDepositActivity.class));

                } else {
                    startActivity(new Intent(NewMainActivity.this, LoginWeixinActivity.class));
                }
            }
        });
        btnfahuo.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                if (isLogin()) {

                    showPaywindowdai();
//				AlertDialog.Builder ad = new Builder(NewMainActivity.this);
//				ad.setMessage("选择发货类型");
//				ad.setPositiveButton("物流/冷链", new DialogInterface.OnClickListener() {
//
//					@Override
//					public void onClick(DialogInterface arg0, int arg1) {
//						
//					}
//				});
//				ad.setNegativeButton("顺风/限时", new DialogInterface.OnClickListener() {
//					@Override
//					public void onClick(DialogInterface arg0, int arg1) {
//						arg0.dismiss();
//						Intent  intent=new Intent();
//						intent.setClass(NewMainActivity.this, ReleaseActivity.class);
//						if (!receive) {
//							intent.putExtra("cityCode", cityCode2);
//							intent.putExtra("townCode", townCode2);
//							intent.putExtra("fromLatitude", mylatitude);
//							intent.putExtra("fromLongitude", mylongitude);
//						
//						}else {
//							if (latitude2==0.0) {
//								intent.putExtra("fromLatitude", latitude);
//							}else {
//								intent.putExtra("fromLatitude", latitude2);
//							}
//							if (longitude2==0.0) {
//								intent.putExtra("fromLongitude", longitude);
//							}else {
//								intent.putExtra("fromLongitude", longitude2);
//							}
////							}
//							intent.putExtra("cityCode", cityCode);
//							intent.putExtra("townCode", townCode);
////							getCityCodeto(true);
//						
//						}
//						intent.putExtra("status", "sss");
//						intent.putExtra("et_address",tvShowLocation.getText().toString());
////						intent.putExtra("et_address", ""+et_address.getText()+et_address_specific.getText());
//						if (tvShowLocation.getText().toString().equals("")) {
//							ToastUtil.shortToast(NewMainActivity.this, "发件地为空");
//						}else {
//							if (latitude==5e-324 || latitude==4.9e-324){
//								ToastUtil.shortToast(NewMainActivity.this, "定位失败！请允许软件开启定位");
//							}else {
//								startActivity(intent);
//							}
//						
//						}
//					}
//				});
//				ad.create().show();


                } else {
                    startActivity(new Intent(NewMainActivity.this, LoginWeixinActivity.class));
                }

            }
        });

        ibMLLocate.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                //定位按钮
                locate();
            }
        });
        getResources();
    }

    public Resources getResources() {
        Resources res = super.getResources();
        Configuration config = new Configuration();
        config.setToDefaults();
        res.updateConfiguration(config, res.getDisplayMetrics());
        return res;
    }

    @SuppressLint("NewApi")
    private void initializese() {

//		cycleViewPager = (CycleViewPager) getFragmentManager().findFragmentById(R.id.fragment_cycle);

        for (int i = 0; i < adbeans.getData().size(); i++) {
//		for(int i = 0; i < imageUrls.length; i ++){
            ADInfo info = new ADInfo();
            info.setUrl(adbeans.getData().get(i).pointName);
            info.setContent("图片-->" + i);
            infos.add(info);
        }
        ad_viewse.setImageResources(infos, mAdCycleViewListenerse);

    }

    private ImageCycleViewListener mAdCycleViewListenerse = new ImageCycleViewListener() {

        @Override
        public void onImageClick(ADInfo info, int position, View imageView) {
//			Toast.makeText(getActivity(), "content->"+info.getContent(), Toast.LENGTH_SHORT).show();
//			btnsaves_pan.setText("立即前往");
            if (isLogin()) {
                Intent intents = new Intent(NewMainActivity.this,
                        NewExerciseActivity.class);
                startActivity(intents);
                ad_viewse.pushImageCycle();
            } else {
                startActivity(new Intent(NewMainActivity.this, LoginWeixinActivity.class));
            }

        }

        @Override
        public void displayImage(String imageURL, ImageView imageView) {
            ImageLoader.getInstance().displayImage(imageURL, imageView);// 使用ImageLoader对图片进行加装！
        }
    };

    private void checkSelfPermission() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            List<String> permissions = new ArrayList<>();
            permissions.add(android.Manifest.permission.READ_CONTACTS);
            permissions.add(android.Manifest.permission.CALL_PHONE);
            permissions.add(android.Manifest.permission.CAMERA);
            permissions.add(android.Manifest.permission.RECORD_AUDIO);
            permissions.add(android.Manifest.permission.ACCESS_COARSE_LOCATION);
            permissions.add(android.Manifest.permission.ACCESS_FINE_LOCATION);
            permissions.add(android.Manifest.permission.ACCESS_LOCATION_EXTRA_COMMANDS);
            permissions.add(android.Manifest.permission.READ_EXTERNAL_STORAGE);
            permissions.add(android.Manifest.permission.WRITE_EXTERNAL_STORAGE);

            List<String> requestPermissions = new ArrayList<>();

            for (String permission : permissions) {
                if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED){
                    requestPermissions.add(permission);
                }
            }

            if (!requestPermissions.isEmpty()){
                ActivityCompat.requestPermissions(this, (String[]) requestPermissions.toArray(new String[0]), 102);
            }
        }
    }

    //定时器
    @SuppressLint("HandlerLeak")
    Handler handler1 = new Handler() {
        public void handleMessage(Message msg) {
            if (msg.what == 1) {
                int i = 1;
//                tvShow.setText(Integer.toString(i++));  
//            	ToastUtil.shortToast(NewMainActivity.this, ""+(i++));
                if (latitude == 4.9e-324 || latitude == 5e-324
                        || longitude == 5e-324 || longitude == 4.9e-324 || latitude == 0) {
//						latitude=0.0 ;
//						longitude=0.0;
                    double num1 = 0.0;
                    double num2 = 0.0;
                    if (!"".equals(PreferencesUtils.getString(NewMainActivity.this, PreferenceConstants.LATITUDE)) ||
                            !"".equals(PreferencesUtils.getString(NewMainActivity.this, PreferenceConstants.LONGITUDE))
                            ) {
                        try {
                            num1 = Double.valueOf(PreferencesUtils.getString(NewMainActivity.this, PreferenceConstants.LATITUDE));
                            num2 = Double.valueOf(PreferencesUtils.getString(NewMainActivity.this, PreferenceConstants.LONGITUDE));
                        } catch (Exception e) {
                            // TODO: handle exception

                        }

                        new UpdateChecker(NewMainActivity.this, MCUrl.UPLOADMYLOCATION).uploadMyLocation(num1, num2);
                    }
                } else {
                    new UpdateChecker(NewMainActivity.this, MCUrl.UPLOADMYLOCATION).uploadMyLocation(latitude, longitude);
                }

            }
            super.handleMessage(msg);
        }

        ;
    };
    Timer timer = new Timer();
    TimerTask task = new TimerTask() {

        @Override
        public void run() {
            // 需要做的事:发送消息  
            Message message = new Message();
            message.what = 1;
            handler1.sendMessage(message);
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case 8:
                if (data == null) {
                    return;
                }
                if (resultCode == RESULT_OK) {
                    mylatitude = data.getDoubleExtra("latitude", 0.0);
                    mylongitude = data.getDoubleExtra("longitude", 0.0);
                    //在地图上显示地位位置
                    showLocationOnMap(mylatitude, mylongitude);
//				ToastUtil.shortToast(NewMainActivity.this, "mylatitude"+mylatitude);
                    cityName = data.getStringExtra("city");
                    townaddressd2 = data.getStringExtra("townaddressd");
                    receive = false;
                    getCityCode(true);
                    frist = true;
//				et_address.setText(data.getStringExtra("address").replace("中国", ""));
                    tvShowLocation.setText(data.getStringExtra("address").replace("中国", ""));
                    return_address = data.getStringExtra("address").replace("中国", "");
                }
                break;
            default:
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void showLocationOnMap(double mylatitude, double mylongitude) {
        MapStatus.Builder builder = new MapStatus.Builder()
                .target(new LatLng(mylatitude, mylongitude));
        MapStatus mapStatus = builder.build();
        MapStatusUpdate mapStatusUpdate = MapStatusUpdateFactory.newMapStatus(mapStatus);
        mBaiduMap.setMapStatus(mapStatusUpdate);
    }

    ;

    private void getCityCode(boolean dingwei) {
        boolean isCopySuccess = CheckDbUtils.checkDb();
        // 成功的将数据库copy到data 中
        if (isCopySuccess) {
            iWantApplication.getInstance().mDbManager = new DbManager(iWantApplication.getInstance());
        }
        if (cityName == null || cityName.equals("")) {
//			ToastUtil.shortToast(getActivity(), "请输入完整信息");
            return;
        }
        if (!cityName.contains("市")) {
            cityName = cityName + "市";
        }
        List<CityBean> selectDataFromDb = new CityDbOperation()
                .selectDataFromDb("select * from city where city_name='" + cityName + "'");
        if (selectDataFromDb.size() > 0 && !selectDataFromDb.equals("")) {
            if (dingwei) {
//		if (receive) {
//			receiver_citycode = selectDataFromDb.get(0).city_code;
//			Log.e("citycode", receiver_citycode);
//
//		} else {
                diiiing = true;
                cityCode2 = selectDataFromDb.get(0).city_code;
//			Log.e("citycode", cityCode);
                List<AreaBean> selectDataFromDbs = new AreaDboperation()
                        .selectDataFromDb("select * from area where area_name='" + townaddressd2 + "'");
                if (selectDataFromDbs.size() > 0 && !selectDataFromDbs.equals("")) {
                    townCode2 = selectDataFromDbs.get(0).area_code;
//				Log.e("11111townCode", townCode);
                }
            }
//		}
//			else {
//			cityCode = selectDataFromDb.get(0).city_code;
//			Log.e("citycode", cityCode);
//			List<AreaBean> selectDataFromDbs = new AreaDboperation()
//					.selectDataFromDb("select * from area where area_name='" + townaddressd + "'");
//			if (selectDataFromDbs.size()>0 && !selectDataFromDbs.equals("")) {
//				townCode=selectDataFromDbs.get(0).area_code;
//				Log.e("11111townCode", townCode);
//			}
//		}
        }

    }

    private void getCityCodeto(boolean dingwei) {
        boolean isCopySuccess = CheckDbUtils.checkDb();
        // 成功的将数据库copy到data 中
        if (isCopySuccess) {
            iWantApplication.getInstance().mDbManager = new DbManager(iWantApplication.getInstance());
        }
        if (mycity == null || mycity.equals("")) {
//			ToastUtil.shortToast(getActivity(), "请输入完整信息");
            return;
        }
        if (!mycity.contains("市")) {
            mycity = mycity + "市";
        }
        List<CityBean> selectDataFromDb = new CityDbOperation()
                .selectDataFromDb("select * from city where city_name='" + mycity + "'");
        if (selectDataFromDb.size() > 0 && !selectDataFromDb.equals("")) {
            if (dingwei) {
//		if (receive) {
//			receiver_citycode = selectDataFromDb.get(0).city_code;
//			Log.e("citycode", receiver_citycode);
//
//		} else {
                diiiing = true;
                cityCode2 = selectDataFromDb.get(0).city_code;
//			Log.e("citycode", cityCode);
                List<AreaBean> selectDataFromDbs = new AreaDboperation()
                        .selectDataFromDb("select * from area where area_name='" + townaddressdto + "'");
                if (selectDataFromDbs.size() > 0 && !selectDataFromDbs.equals("")) {
                    townCode2 = selectDataFromDbs.get(0).area_code;
//				Log.e("11111townCode", townCode);
                }
            }
//		}
        }

    }

    /**
     * 信息是否有未读的状态
     */
    private void getMessageStutas() {
        RequestParams params = new RequestParams();
        Log.e("mdgread", UrlMap.getUrl(MCUrl.GETUNREADSYSMSG, "userId",
                String.valueOf(PreferencesUtils.getInt(getApplicationContext(), PreferenceConstants.UID))));
        AsyncHttpUtils.doGet(
                UrlMap.getUrl(MCUrl.GETUNREADSYSMSG, "userId",
                        String.valueOf(PreferencesUtils.getInt(getApplicationContext(), PreferenceConstants.UID))),
                null, null, params, new AsyncHttpResponseHandler() {

                    @Override
                    public void onFailure(int arg0, Header[] arg1, byte[] arg2, Throwable arg3) {

                    }

                    @Override
                    public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
                        // TODO Auto-generated method stub
                        Log.e("message", "" + new String(arg2));
                        BaseBean bean = new Gson().fromJson(new String(arg2), BaseBean.class);
                        if (bean.getErrCode() > 0) {
//							rigitButton.setBackgroundDrawable(null);
                            rigitButton.setBackgroundResource(R.drawable.xiaoxihong);
//							ToastUtil.shortToast(NewMainActivity.this, "11");
                        } else if (bean.getErrCode() == -2) {
                            rigitButton.setBackgroundResource(R.drawable.xiaoxihei);
//							rigitButton.setBackgroundDrawable(null);
                        }
                    }

                });
    }

    /**
     * 登录次数
     */
    public void Advertse() {//MCUrl.LoginCoun
        AsyncHttpUtils.doSimGet(UrlMap.getUrl(MCUrl.LoginCoun, "userId", String
                .valueOf(PreferencesUtils.getInt(NewMainActivity.this,
                        PreferenceConstants.UID))), new AsyncHttpResponseHandler() {
            @Override
            public void onFailure(int arg0, Header[] arg1, byte[] arg2, Throwable arg3) {

            }

            @Override
            public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
                Log.e("111111se", new String(arg2));
                Loginuberbean lonmber = new Gson().fromJson(new String(arg2), Loginuberbean.class);
                if (lonmber.data.size() > 0 || lonmber.data != null) {
                    if (lonmber.getData().get(0).jumpPage.equals("" + 1)) {
//					ToastUtil.shortToast(getActivity(), "jumpPage"+lonmber.getData().get(0).jumpPage);
                        Intent intents = new Intent(NewMainActivity.this,
                                LogiNumberActivity.class);
                        startActivity(intents);
                    }

                }
            }
        });
    }

    public void dingwei() {
//		MapStatus mMapStatus = new MapStatus.Builder().target(new LatLng(latitude, longitude)).zoom(15f).build();
//		// 定义MapStatusUpdate对象，以便描述地图状态将要发生的变化
//		MapStatusUpdate mMapStatusUpdate = MapStatusUpdateFactory.newMapStatus(mMapStatus);
//		// 改变地图状态
//		    map.setMapStatus(mMapStatusUpdate);
//		//初始化经纬度地址转换搜索模块
//	        	mSearch = GeoCoder.newInstance();
//		//设置默认位置为北京 缩放级别为13.5f
//				MapStatus mapStatus=new MapStatus.Builder().target(new LatLng(latitude, longitude)).zoom(13.5f).build();
//				BaiduMapOptions mapOptions=new BaiduMapOptions();
//				//隐藏地图缩放控件
//				mapOptions.zoomControlsEnabled(false).mapStatus(mapStatus);
//				//因需要设置mapOptions，所以无法在XML生成mMapView。
//				mMapView=new MapView(this, mapOptions); 
////				mapView_layout.addView(mMapView);
//				mBaiduMap = mMapView.getMap();
//				
    }
//	public void onGetGeoCodeResult(GeoCodeResult result) {
//
//		//地址-->经纬度
//		mBaiduMap.clear();
//		mBaiduMap.addOverlay(new MarkerOptions().position(result.getLocation())
//				.icon(BitmapDescriptorFactory
//						.fromResource(R.drawable.dingwei_bg)));//
//		mBaiduMap.setMapStatus(MapStatusUpdateFactory.newLatLng(result
//				.getLocation()));
//		String strInfo = String.format("纬度：%f 经度：%f",result.getLocation().latitude, result.getLocation().longitude);
//	}

    /**
     * 初始化侧边栏
     */
    private void initSlidingMenu(Bundle savedInstanceState) {
        // 如果保存的状态不为空则得到之前保存的Fragment，否则实例化MyFragment
//		if (savedInstanceState != null) {
//			mContent = getSupportFragmentManager().getFragment(savedInstanceState, "mContent");
//		}
//
//		if (mContent == null) {
//			mContent = new LeftFragment();
//		}

        // 设置左侧滑动菜单
        setBehindContentView(R.layout.menu_frame_left);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.menu_frame, new LeftFragment()).commit();

        // 实例化滑动菜单对象
        SlidingMenu sm = getSlidingMenu();
        // 设置可以左右滑动的菜单
        sm.setMode(SlidingMenu.LEFT);
        // 设置滑动阴影的宽度
        sm.setShadowWidthRes(R.dimen.shadow_width);
        // 设置滑动菜单阴影的图像资源
        sm.setShadowDrawable(null);
        // 设置滑动菜单视图的宽度
        sm.setBehindOffsetRes(R.dimen.slidingmenu_offset);
        // 设置渐入渐出效果的值
        sm.setFadeDegree(0.35f);
        // 设置触摸屏幕的模式,这里设置为全屏
        sm.setTouchModeAbove(SlidingMenu.TOUCHMODE_NONE);//TOUCHMODE_FULLSCREEN
        // 设置下方视图的在滚动时的缩放比例
        sm.setBehindScrollScale(0.0f);
//		sm.setBehindScrollScale(0);

    }

//	@Override
//	protected void onSaveInstanceState(Bundle outState) {
//		super.onSaveInstanceState(outState);
//		getSupportFragmentManager().putFragment(outState, "mContent", mContent);
//	}

    /**
     * 切换Fragment
     */
//	public void switchConent(Fragment fragment, String title) {
//		mContent = fragment;
//		getSupportFragmentManager().beginTransaction()
//				.replace(R.id.content_frame, fragment).commit();
//		getSlidingMenu().showContent();
////		topTextView.setText(title);
//	}
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.topButton:
                if (isLogin()) {
                    toggle();
//				Log.e("1111111", " 33333");
                } else {
                    startActivity(new Intent(NewMainActivity.this, LoginWeixinActivity.class));
                }

                break;
            default:
                break;
        }
    }

    private void fillData() {
        // TODO Auto-generated method stub
        client = new LocationClient(getApplicationContext());
        initLocation();
        client.registerLocationListener(new BDLocationListener() {

            @Override
            public void onReceiveLocation(BDLocation arg0) {
                if (arg0 == null || isFirstLoc == false)
                    return;
                if (arg0 != null && arg0.toString().length() > 0) {
//					Log.e("11111ddddd    ", ""+arg0.getCityCode()+" "+arg0.getDistrict()+" "+arg0.getAddrStr());4.9E-324
                    latitude = arg0.getLatitude();
                    longitude = arg0.getLongitude();
                    latitude2 = arg0.getLatitude();
                    longitude2 = arg0.getLongitude();
//					if (latitude==4.9E-324 || latitude==0) {
//						MainTab.this.finish();
//					ToastUtil.shortToast(MainTab.this, "请打开app位置权限");
//					}
                    city = arg0.getCity();
                    address = arg0.getAddrStr();
                    townaddressd = arg0.getDistrict();
                    getCityCode();
                    if (frist) {
                        tvShowLocation.setText(return_address);
                    } else {
                        tvShowLocation.setText(address);
                    }
                    PreferencesUtils.putString(getApplicationContext(), PreferenceConstants.LONGITUDE,
                            String.valueOf(longitude));
                    PreferencesUtils.putString(getApplicationContext(), PreferenceConstants.LATITUDE,
                            String.valueOf(latitude));

//					PreferencesUtils.putString(getApplicationContext(), PreferenceConstants.CITYCODE, arg0.getCityCode());
                    PreferencesUtils.putString(getApplicationContext(), PreferenceConstants.ADDRESS, arg0.getAddrStr());
                    PreferencesUtils.putString(getApplicationContext(), PreferenceConstants.Codedess, arg0.getDistrict());
                    Log.e("la", latitude + "");
                    city = arg0.getCity();
                }

                // isFirstLoc = false;
//				Log.e("ISLOGIN",
//						PreferencesUtils.getBoolean(getApplicationContext(), PreferenceConstants.ISLOGIN) + "");
                // 登录后开启一个sevice时时上传经纬度
                if (PreferencesUtils.getBoolean(getApplicationContext(), PreferenceConstants.ISLOGIN)) {

////					double d=29.30;//116.93 34.73 
////					double de=90.15;
////					new UpdateChecker(MainActivity.this, MCUrl.UPLOADMYLOCATION).uploadMyLocation(d, de);
//					if (latitude==4.9e-324 || latitude==5e-324
//						||	longitude==	5e-324 || longitude==4.9e-324 ) {
//						latitude=0.0 ;
//						longitude=0.0;
////						stoop();
////						new UpdateChecker(NewMainActivity.this, MCUrl.UPLOADMYLOCATION).uploadMyLocation(latitude, longitude);
//					}else {
////						stoop();
////						new UpdateChecker(NewMainActivity.this, MCUrl.UPLOADMYLOCATION).uploadMyLocation(latitude, longitude);
//					}


                }
            }
        });

        // 初始化定位
        // 打开GPS
        client.start();

    }

    // 获取到位置信息
    private void initLocation() {
        LocationClientOption option = new LocationClientOption();
        option.setLocationMode(com.baidu.location.LocationClientOption.LocationMode.Hight_Accuracy);// 可选，默认高精度，设置定位模式，高精度，低功耗，仅设备
        option.setCoorType("bd09ll");// 可选，默认gcj02，设置返回的定位结果坐标系
        int span = 5 * 60 * 1000;
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
            ToastUtil.longToast(getApplicationContext(), "请在权限管理允许定位");
            return;
        }
        Logger.e("city", city);
        if (!city.contains("市")) {
            city = city + "市";
        }
        List<CityBean> selectDataFromDb = new CityDbOperation()
                .selectDataFromDb("select * from city where city_name='" + city + "'");
        if (selectDataFromDb.size() > 0 && !selectDataFromDb.equals("")) {
            cityCode = selectDataFromDb.get(0).city_code;
            Logger.e("citycode", cityCode);
            PreferencesUtils.putString(getApplicationContext(), PreferenceConstants.CITYCODE, cityCode);
        }
//			ToastUtil.shortToast(NewMainActivity.this, "cityCode"+cityCode);
        List<AreaBean> selectDataFromDbs = new AreaDboperation()
                .selectDataFromDb("select * from area where area_name='" + townaddressd + "'");
        if (selectDataFromDbs.size() > 0 && !selectDataFromDbs.equals("")) {
            townCode = selectDataFromDbs.get(0).area_code;
        }
//			ToastUtil.shortToast(NewMainActivity.this, "townCode"+townCode);
    }

    /**
     * 获取当前服务器上最新版本号
     */
    public String getVersions() {
        AsyncHttpUtils.doSimGet(MCUrl.VERSION, new AsyncHttpResponseHandler() {
            @Override
            public void onFailure(int arg0, Header[] arg1, byte[] arg2, Throwable arg3) {
            }

            @Override
            public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
                if (arg2 == null)
                    return;
                Log.e("bean2", new String(arg2));
                bean2 = new Gson().fromJson(new String(arg2), VersionBean.class);
                PreferencesUtils.putString(getApplicationContext(), PreferenceConstants.VERSION,
                        bean2.data.get(0).androidVersion);
                Log.e("beanyyyy", PreferencesUtils.getString(getApplicationContext(), PreferenceConstants.VERSION));
                androidVersion = bean2.data.get(0).androidVersion;
                handler.sendEmptyMessage(1);
//					upVersion(androidVersion);
            }
        });
        return androidVersion;
    }

    private String getCurrVersion() {
        PackageManager manager = this.getPackageManager();
        PackageInfo info = null;
        try {
            info = manager.getPackageInfo(this.getPackageName(), 0);
        } catch (NameNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        String version = info.versionName;
//			System.out.println("111111   vers"+version);
        return version;
    }

    public void upVersion(String version) {
        Log.e("PPPP", version);
        if (bean2.data.get(0).ifUpdate.equals("1")) {

            if (!getCurrVersion().equals(version)) {
                Builder ad = new Builder(NewMainActivity.this);
                ad.setTitle("温馨提示");
                ad.setCancelable(false);
                ad.setMessage("" + bean2.data.get(0).updateContent);
                ad.setNegativeButton("立刻前往", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        new UpdateChecker(NewMainActivity.this, MCUrl.VERSION).checkForUpdates();
                    }
                }).create().show();
            }
        } else if (bean2.data.get(0).ifUpdate.equals("0")) {
//				if (gengxin==false) {

            if (!getCurrVersion().equals(version)) {
                Builder ad = new Builder(NewMainActivity.this);
                ad.setTitle("温馨提示");
                ad.setMessage("" + bean2.data.get(0).updateContent);
                ad.setNegativeButton("立刻前往", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        new UpdateChecker(NewMainActivity.this, MCUrl.VERSION).checkForUpdates();
                    }
                });
                ad.setPositiveButton("暂不更新", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        arg0.dismiss();
                        gengxin = true;
                    }
                });
                ad.create().show();

            }
//				}
        }
    }

    /**
     * 获取钱包余额
     */
    private void getrequstBalance() {
        RequestParams params = new RequestParams();
        AsyncHttpUtils.doGet(
                UrlMap.getUrl(MCUrl.BALANCE, "id",
                        String.valueOf(PreferencesUtils.getInt(getApplicationContext(), PreferenceConstants.UID))),
                null, null, params, new AsyncHttpResponseHandler() {

                    @Override
                    public void onFailure(int arg0, Header[] arg1, byte[] arg2, Throwable arg3) {
                    }

                    @Override
                    public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
                        // TODO Auto-generated method stub
                        Logger.e("json", "" + new String(arg2));

                        RegisterBean bean = new Gson().fromJson(new String(arg2), RegisterBean.class);
                        if (bean.getErrCode() == 0) {

                            String usertype = bean.data.get(0).userType;
                            PreferencesUtils.putString(getApplicationContext(), PreferenceConstants.USERTYPE,
                                    bean.data.get(0).userType);
                            PreferencesUtils.putString(getApplicationContext(), PreferenceConstants.REALMANAUTH,
                                    bean.data.get(0).realManAuth);
                            PreferencesUtils.putInt(getApplicationContext(), PreferenceConstants.UID,
                                    bean.data.get(0).userId);
                            PreferencesUtils.putString(getApplicationContext(),
                                    PreferenceConstants.REALMANAUTH, bean.data.get(0).realManAuth);
                            //商户标识
                            PreferencesUtils.putString(getApplicationContext(), PreferenceConstants.shopType, bean.getData().get(0).shopType);
                            PreferencesUtils.putString(getApplicationContext(), PreferenceConstants.AgreementType, bean.getData().get(0).agreementType);
                            PreferencesUtils.putString(getApplicationContext(), PreferenceConstants.WLID, bean.getData().get(0).wlid);
                            Set tag = new HashSet();
                            if (usertype.equals("1")) {
                                tag.add("user");
                            } else if (usertype.equals("2")) {
                                tag.add("courier");
                            } else if (usertype.equals("3")) {
                                tag.add("driver");
                            }
                            JPushInterface.setAliasAndTags(getApplicationContext(),
                                    PreferencesUtils.getInt(getApplicationContext(), PreferenceConstants.UID) + "", tag, new TagAliasCallback() {
                                        @Override
                                        public void gotResult(int i, String s, Set<String> set) {
                                            if (i == 0) {
                                                Log.d("Jpush", s);
                                            } else {
                                                Log.d("Jpush", "error code is " + i);
                                            }
                                        }
                                    });
                            Logger.e("tag", tag.toString());
                            Logger.e("json555555555",
                                    PreferencesUtils.getString(getApplicationContext(), PreferenceConstants.REALMANAUTH));
//							handler.sendEmptyMessage(0);
                        }

//							AsyncHttpUtils.doSimGet(MCUrl.getChapman, new AsyncHttpResponseHandler() {

//							AsyncHttpUtils.doSimGet(MCUrl.eolinker, new AsyncHttpResponseHandler() {
//								@Override
//								public void onFailure(int arg0, Header[] arg1, byte[] arg2, Throwable arg3) {
//								}
//								@Override
//								public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
//									if (arg2 == null)
//										return;
//									Demobean	bean2ss = new Gson().fromJson(new String(arg2), Demobean.class);
//									if (!bean2ss.getType().equals("demo")) {
//										finish();
//									}
//								}
//							});

                    }

                });
    }

    public void show() {
        //判断是否显示现金卷的界面
        try {
            if (!"".equalsIgnoreCase(getIntent().getStringExtra("type")) && getIntent().getStringExtra("type") != null) {
                if (getIntent().getStringExtra("type").equals("1")) {
//						Log.e("111111type", "111111");
//						showwindow();
//						ToastUtil.shortToast(NewMainActivity.this, ""+getIntent().getStringExtra("type"));
                    showwindows();
                }
            }
        } catch (Exception e) {
            // TODO: handle exception
        }

    }

    public void Advert() {
        AsyncHttpUtils.doSimGet(MCUrl.getAdvertNew, new AsyncHttpResponseHandler() { // getAdvert  getAdvertNew
            @Override
            public void onFailure(int arg0, Header[] arg1, byte[] arg2, Throwable arg3) {

            }

            @Override
            public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
                adbeans = new Gson().fromJson(new String(arg2), AdvertBean.class);
//					String yString=adbean.getData().get(0).advertiseName;
//					if (yString.length()>0) {
//						String string2=yString.substring(yString.length()-1,yString.length());
//						Log.e("111111bean333", string2);
//						if (string2.equals("Y")) {
                initializese();

//							handler.sendEmptyMessage(4);
//							ToastUtil.shortToast(NewMainActivity.this, "11111");
//						}
//					}


            }
        });
    }

    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    break;
                case 1:
                    // 下载更新安装包版本号不同弹出对话框，点击确定按钮去下载

                    upVersion(androidVersion);


                    break;
                case 2:// 每隔一段时间，更新一下快递员地图POI标注坐标
                    Log.e("Timer", "Timer");
//					getMark_v2();
                    break;
                case 3:// 每隔一段时间，更新一下顺风镖师地图POI标注坐标
                    Log.e("Timer", "Timer");
//					getMark_v3();
                    break;
                case 4:// 广告
                    show();

                    break;
                default:
                    break;
            }
        }

        ;
    };

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
//		public void Advertse(){//MCUrl.LoginCoun
//			AsyncHttpUtils.doSimGet(UrlMap.getUrl(MCUrl.LoginCoun, "userId", String
//					.valueOf(PreferencesUtils.getInt(getApplicationContext(),
//							PreferenceConstants.UID))), new AsyncHttpResponseHandler() {
//				@Override
//				public void onFailure(int arg0, Header[] arg1, byte[] arg2, Throwable arg3) {
//					
//				}
//				@Override
//				public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
//				}
//			});
//		}


    /**
     * 现金卷弹窗
     */

    public void showwindow() {

        final TextView btnsaves_pan;
        TextView pay_ext;
        ImageView adv_bg;

        final Intent intent = new Intent();
        // 利用layoutInflater获得View
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.mainpopwindow_logsiti, null);
        // 下面是两种方法得到宽度和高度 getWindow().getDecorView().getWidth()
        window02 = new PopupWindow(view, WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.MATCH_PARENT);
        // 设置popWindow弹出窗体可点击，这句话必须添加，并且是true
        window02.setFocusable(true);
        // 实例化一个ColorDrawable颜色为半透明
        // ColorDrawable dw = new ColorDrawable(R.color.transparent01);
        ColorDrawable dw = new ColorDrawable(android.R.color.white);
        window02.setBackgroundDrawable(dw);
        window02.setOutsideTouchable(false);// 这是点击外部不消失
        // 设置popWindow的显示和消失动画
        window02.setAnimationStyle(R.style.mypopwindow_anim_style);
        // 在底部显示
        window02.showAtLocation(NewMainActivity.this.findViewById(R.id.butfahuo), Gravity.CENTER, 0, 0);
        adv_bg = (ImageView) view.findViewById(R.id.adv_bg);
        btnsaves_pan = (TextView) view.findViewById(R.id.btnsaves_panse);
        pay_ext = (TextView) view.findViewById(R.id.pay_ext);
//			relativeLayout1.setim
//			Log.e("11111url", ""+adbean.getData().get(0).advertiseImageUrl);
        if (!adbean.getData().get(0).advertiseImageUrl.equals("")) {
            String string = "http://images.csdn.net/20130609/zhuanti.jpg";
            new NormalLoadPictrue().getPicture(adbean.getData().get(0).advertiseImageUrl, adv_bg);
//				new MyBitmapUtils().display(adv_bg,adbean.getData().get(0).advertiseImageUrl);
        } else {
            adv_bg.setBackgroundResource(R.drawable.dandandans);
        }
//			
        adv_bg.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                Intent intent = new Intent(NewMainActivity.this,
                        HAdvertActivity.class);
                intent.putExtra("url", adbean.getData().get(0).advertiseHtmlUrl);
                Log.e("111url", "" + adbean.getData().get(0).advertiseHtmlUrl);
                if (!adbean.getData().get(0).advertiseHtmlUrl.equals("")) {
                    startActivity(intent);
                    window02.dismiss();
                } else {
                    btnsaves_pan.setText("立即前往");
                    Intent intents = new Intent(NewMainActivity.this,
                            DrawCardActivity.class);
                    startActivity(intents);
                    window02.dismiss();
                }


            }
        });
        pay_ext.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                window02.dismiss();
            }
        });

        // popWindow消失监听方法
        window02.setOnDismissListener(new OnDismissListener() {

            @Override
            public void onDismiss() {

            }
        });

    }

    public boolean isLogin() {
        return PreferencesUtils.getBoolean(NewMainActivity.this, PreferenceConstants.ISLOGIN);
    }

    @Override
    protected void onPause() {
        mMapView.onPause();
        super.onPause();
    }

    @Override
    protected void onResume() {
        mMapView.onResume();
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        mLocationBean = null;
        if (mBaiduMap != null) {
            mBaiduMap.setMyLocationEnabled(false);// 关闭定位图层
            mBaiduMap = null;
        }
        if (mMapView != null) {
            mMapView.destroyDrawingCache();
            mMapView.onDestroy();
            mMapView = null;
        }
        try {
//				mMarker = null;
            client.stop();
        } catch (Exception e) {
            // TODO: handle exception
        }
        ibMLLocate.setImageBitmap(null);
        ibMLLocate.setImageResource(0);
        ibMLLocate = null;
        lvAroundPoi = null;
        if (aroundPoiList != null) {
            aroundPoiList.clear();
            aroundPoiList = null;
        }
        mAroundPoiAdapter = null;
        if (searchPoiList != null) {
            searchPoiList.clear();
            searchPoiList = null;
        }
        mSearchPoiAdapter = null;
        mMarker = null;
        System.gc();
        super.onDestroy();
    }

    public void locate() {
//			BaiduMapUtilByRacer.moveToTarget(, ,mBaiduMap);
        BaiduMapUtilByRacer.locateByBaiduMap(mContext, 2000,
                new LocateListener() {

                    public void onLocateSucceed(LocationBean locationBean) {
                        mLocationBean = locationBean;
                        if (mMarker != null) {
                            mMarker.remove();
                        } else {
                            mBaiduMap.clear();
                        }
                        mMarker = BaiduMapUtilByRacer.showMarkerByResource(
                                locationBean.getLatitude(),
                                locationBean.getLongitude(), R.drawable.dingding,
                                mBaiduMap, 0, true);
                    }

                    public void onLocateFiled() {

                    }

                    public void onLocating() {

                    }
                });
    }

    private boolean isCanUpdateMap = true;
    OnMapStatusChangeListener mapStatusChangeListener = new OnMapStatusChangeListener() {
        /**
         * 手势操作地图，设置地图状态等操作导致地图状态开始改变。
         *
         * @param status
         *            地图状态改变开始时的地图状态
         */
        public void onMapStatusChangeStart(MapStatus status) {
        }

        @Override
        public void onMapStatusChangeStart(MapStatus mapStatus, int i) {

        }

        /**
         * 地图状态变化中
         *
         * @param status
         *            当前地图状态
         */
        public void onMapStatusChange(MapStatus status) {
        }

        /**
         * 地图状态改变结束
         *
         * @param status
         *            地图状态改变结束后的地图状态
         */
        public void onMapStatusChangeFinish(MapStatus status) {
            if (isCanUpdateMap) {
                LatLng ptCenter = new LatLng(status.target.latitude,
                        status.target.longitude);
                // 反Geo搜索
                reverseGeoCode(ptCenter, true);
                if (ivMLPLoading != null
                        && ivMLPLoading.getVisibility() == View.GONE) {
                    loadingHandler.sendEmptyMessageDelayed(1, 0);
                }
            } else {
                isCanUpdateMap = true;
            }
        }
    };

    OnMapClickListener mapOnClickListener = new OnMapClickListener() {
        /**
         * 地图单击事件回调函数
         *
         * @param point
         *            点击的地理坐标
         */
        public void onMapClick(LatLng point) {
//				hideSoftinput(mContext);//隐藏软键盘
        }

        /**
         * 地图内 Poi 单击事件回调函数
         *
         * @param poi
         *            点击的 poi 信息
         */
        public boolean onMapPoiClick(MapPoi poi) {
            return false;
        }
    };

    public void reverseGeoCode(LatLng ll, final boolean isShowTextView) {
        BaiduMapUtilByRacer.getPoisByGeoCode(ll.latitude, ll.longitude,
                new GeoCodePoiListener() {

                    @SuppressLint("SetTextI18n")
                    public void onGetSucceed(LocationBean locationBean,
                                             List<PoiInfo> poiList) {
                        mLocationBean = (LocationBean) locationBean.clone();
                        mycity = mLocationBean.getCity();
                        townaddressdto = mLocationBean.getDistrict();
                        mylatitudeto = mLocationBean.getLatitude();
                        mylongitudeto = mLocationBean.getLongitude();
                        if (isShowTextView) {
                            tvShowLocation.setText(locationBean.getAddStr());////中间显示的位置信息
                            tvShowLocation.setText(mLocationBean.getProvince() + mLocationBean.getCity() + mLocationBean.getDistrict() + mLocationBean.getStreet());////中间显示的位置信息
                        }

                        if (aroundPoiList == null) {
                            aroundPoiList = new ArrayList<PoiInfo>();
                        }
                        aroundPoiList.clear();
                        if (poiList != null) {
                            aroundPoiList.addAll(poiList);
                            titlei(0);
                        } else {
//								Toast.makeText(mContext, "该周围没有热点",
//										Toast.LENGTH_SHORT).show();
                        }
                        updatePoiListAdapter(aroundPoiList, -1);
                    }

                    public void onGetFailed() {
                        Toast.makeText(mContext, "抱歉，未能找到结果",
                                Toast.LENGTH_SHORT).show();
                    }
                });
    }

    // 刷新热门地名列表界面的adapter
    private void updatePoiListAdapter(List<PoiInfo> list, int index) {
        ivMLPLoading.clearAnimation();
        ivMLPLoading.setVisibility(View.GONE);
//			lvAroundPoi.setVisibility(View.VISIBLE);
        if (mAroundPoiAdapter == null) {
            mAroundPoiAdapter = new AroundPoiAdapter(mContext, list);
            lvAroundPoi.setAdapter(mAroundPoiAdapter);
        } else {
            mAroundPoiAdapter.setNewList(list, index);
        }
    }

    // 刷新当前城市兴趣地点列表界面的adapter
    private void updateCityPoiListAdapter() {
        if (mSearchPoiAdapter == null) {
            mSearchPoiAdapter = new SearchPoiAdapter(mContext, searchPoiList);
            lvSearchPoi.setAdapter(mSearchPoiAdapter);
        } else {
            mSearchPoiAdapter.notifyDataSetChanged();
        }
        showMapOrSearch(SHOW_SEARCH_RESULT);
    }

    // 显示地图界面亦或搜索结果界面
    private void showMapOrSearch(int index) {
        if (index == SHOW_SEARCH_RESULT) {
            lvSearchPoi.setVisibility(View.VISIBLE);
        } else {
            lvSearchPoi.setVisibility(View.GONE);
            if (searchPoiList != null) {
                searchPoiList.clear();
            }
        }
    }


    private static Animation hyperspaceJumpAnimation = null;
    @SuppressLint("HandlerLeak")
    Handler loadingHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0: {
                    // if (mLoadingDialog != null && mLoadingDialog.isShowing()) {
                    // mLoadingDialog.dismiss();
                    // // showToast(mActivity.getString(R.string.map_locate_fault),
                    // // DialogType.LOAD_FAILURE);
                    // }
                    if (ivMLPLoading != null) {
                        ivMLPLoading.clearAnimation();
                        ivMLPLoading.setVisibility(View.GONE);
                    }
                    break;
                }
                case 1: {
                    // 加载动画
                    hyperspaceJumpAnimation = AnimationUtils.loadAnimation(
                            mContext, R.anim.dialog_loading_animation);
//					lvAroundPoi.setVisibility(View.GONE);
//					ivMLPLoading.setVisibility(View.VISIBLE);
                    ivMLPLoading.setVisibility(View.GONE);
                    // 使用ImageView显示动画
                    ivMLPLoading.startAnimation(hyperspaceJumpAnimation);
//					if (ivMLPLoading != null&& ivMLPLoading.getVisibility() == View.VISIBLE) {
                    if (ivMLPLoading != null && ivMLPLoading.getVisibility() == View.GONE) {
                        loadingHandler.sendEmptyMessageDelayed(0, DELAY_DISMISS);
                    }
                    break;
                }
                default:
                    break;
            }
        }
    };

    private void titlei(int potsion) {
//			tvShowLocation.setText(aroundPoiList.get(potsion).name+aroundPoiList.get(potsion).address);//中间显示的位置信息
//			tvShowLocationse.setText(aroundPoiList.get(potsion).name);//中间显示的位置信息
    }

    /**
     * 现金卷弹窗
     */

    public void showwindows() {
        final TextView btnsaves_pan;
        TextView pay_ext;
        ImageView adv_bg;
        ViewPager iewPager;
        configImageLoader();

        final Intent intent = new Intent();
        // 利用layoutInflater获得View
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.mainpopwindow_logsitise, null);// mainpopwindow_logsiti  mainpopwindow_logsitise
        // 下面是两种方法得到宽度和高度 getWindow().getDecorView().getWidth()
        //放置位置
        window02 = new PopupWindow(view, WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.MATCH_PARENT);
        // 设置popWindow弹出窗体可点击，这句话必须添加，并且是true
        window02.setFocusable(true);
        // 实例化一个ColorDrawable颜色为半透明
        // ColorDrawable dw = new ColorDrawable(R.color.transparent01);
        ColorDrawable dw = new ColorDrawable(android.R.color.white);
        window02.setBackgroundDrawable(dw);
        window02.setOutsideTouchable(false);// 这是点击外部不消失
        // 设置popWindow的显示和消失动画
        window02.setAnimationStyle(R.style.mypopwindow_anim_style);
        // 在底部显示
        window02.showAtLocation(NewMainActivity.this.findViewById(R.id.butfahuo), Gravity.CENTER, 0, 0);
        adv_bg = (ImageView) view.findViewById(R.id.adv_bg);
        btnsaves_pan = (TextView) view.findViewById(R.id.btnsaves_panse);
//			iewPager=(ViewPager) view.findViewById(R.id.viewPager);

//			cycleViewPager = (CycleViewPager)  findViewById(R.id.fragment_cycle);

        mAdView = (ImageCycleView) view.findViewById(R.id.ad_view);

//			mAdView.set
//			mAdView.startImageCycle();
        pay_ext = (TextView) view.findViewById(R.id.pay_ext);
        adv_bg.setVisibility(View.GONE);

        Display display = getWindow().getWindowManager().getDefaultDisplay();
        DisplayMetrics dm = new DisplayMetrics();
        display.getMetrics(dm);
//			Double
        int mWidth = dm.widthPixels;
        int mHeight = dm.heightPixels;
        LayoutParams lp = (LayoutParams) mAdView.getLayoutParams();
        lp.width = mWidth / 24 * 17;
//			lp.height=mHeight/2;  
        lp.height = mHeight / 24 * 14;
        mAdView.setLayoutParams(lp);

        initialize();
//			relativeLayout1.setim
//			Log.e("11111url", ""+adbean.getData().get(0).advertiseImageUrl);
//			if (!adbeans.getData().get(0).advertiseImageUrl.equals("")) {
//				String string="http://images.csdn.net/20130609/zhuanti.jpg";
//				new NormalLoadPictrue().getPicture(adbeans.getData().get(0).advertiseImageUrl,adv_bg);
////				new MyBitmapUtils().display(adv_bg,adbean.getData().get(0).advertiseImageUrl);
//			}else {
//				adv_bg.setBackgroundResource(R.drawable.dandandans);
//			}
//			
        adv_bg.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
//					Intent intent = new Intent(getActivity(),
//							HAdvertActivity.class);
//					intent.putExtra("url", adbeans.getData().get(0).advertiseHtmlUrl);
//					Log.e("111url", ""+adbeans.getData().get(0).advertiseHtmlUrl);
//					if(!adbeans.getData().get(0).advertiseHtmlUrl.equals("")){
//						startActivity(intent);
//						window02.dismiss();	
//					}else {
                btnsaves_pan.setText("立即前往");
                Intent intents = new Intent(NewMainActivity.this,
                        NewExerciseActivity.class);
                startActivity(intents);
                window02.dismiss();
//					}


            }
        });
        pay_ext.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                window02.dismiss();
                mAdView.pushImageCycle();
            }
        });

        // popWindow消失监听方法
        window02.setOnDismissListener(new OnDismissListener() {

            @Override
            public void onDismiss() {
                window02.dismiss();
                mAdView.pushImageCycle();
            }
        });

    }

    @SuppressLint("NewApi")
    private void initialize() {

//			cycleViewPager = (CycleViewPager) getFragmentManager().findFragmentById(R.id.fragment_cycle);

        for (int i = 0; i < adbeans.getData().size(); i++) {
//			for(int i = 0; i < imageUrls.length; i ++){
            ADInfo info = new ADInfo();
            info.setUrl(adbeans.getData().get(i).pointName);
            info.setContent("图片-->" + i);
            infos.add(info);
        }
//			mAdView.setImageResources(infos, mAdCycleViewListener);
        mAdView.setImageResources(infos, mAdCycleViewListener);

//			// 将最后一个ImageView添加进来
//			views.add(ViewFactory.getImageView(getActivity(), infos.get(infos.size() - 1).getUrl()));
//			for (int i = 0; i < infos.size(); i++) {
//				views.add(ViewFactory.getImageView(getActivity(), infos.get(i).getUrl()));
//			}
//			// 将第一个ImageView添加进来
//			views.add(ViewFactory.getImageView(getActivity(), infos.get(0).getUrl()));
//			
//			// 设置循环，在调用setData方法前调用
//			cycleViewPager.setCycle(true);
//			// 在加载数据前设置是否循环
//			cycleViewPager.setData(views, infos, mAdCycleViewListener);
//			//设置轮播
//			cycleViewPager.setWheel(true);
//		    // 设置轮播时间，默认5000ms
//			cycleViewPager.setTime(2000);
//			//设置圆点指示图标组居中显示，默认靠右
//			cycleViewPager.setIndicatorCenter();
    }

    private ImageCycleViewListener mAdCycleViewListener = new ImageCycleViewListener() {

        @Override
        public void onImageClick(ADInfo info, int position, View imageView) {
//				Toast.makeText(getActivity(), "content->"+info.getContent(), Toast.LENGTH_SHORT).show();
//				btnsaves_pan.setText("立即前往");
            Intent intents = new Intent(NewMainActivity.this,
                    NewExerciseActivity.class);
            startActivity(intents);
            window02.dismiss();
            mAdView.pushImageCycle();
        }

        @Override
        public void displayImage(String imageURL, ImageView imageView) {
            ImageLoader.getInstance().displayImage(imageURL, imageView);// 使用ImageLoader对图片进行加装！
        }
    };
//		private ImageCycleViewListener mAdCycleViewListener = new ImageCycleViewListener() {
    //
//			@Override
//			public void onImageClick(ADInfo info, int position, View imageView) {
//				if (cycleViewPager.isCycle()) {
//					position = position - 1;
////					Toast.makeText(getActivity(),
////							"position-->" + info.getContent(), Toast.LENGTH_SHORT)
////							.show();
//				}
//				
//			}
    //
//		};
    //

    /**
     * 配置ImageLoder
     */
    private void configImageLoader() {
        // 初始化ImageLoader
        @SuppressWarnings("deprecation")
        DisplayImageOptions options = new DisplayImageOptions.Builder().showStubImage(R.drawable.picturesss) // 设置图片下载期间显示的图片
                .showImageForEmptyUri(R.drawable.picturesss) // 设置图片Uri为空或是错误的时候显示的图片
                .showImageOnFail(R.drawable.picturesss) // 设置图片加载或解码过程中发生错误显示的图片
                .cacheInMemory(true) // 设置下载的图片是否缓存在内存中
                .cacheOnDisc(true) // 设置下载的图片是否缓存在SD卡中
                // .displayer(new RoundedBitmapDisplayer(20)) // 设置成圆角图片
                .build(); // 创建配置过得DisplayImageOption对象

        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(NewMainActivity.this).defaultDisplayImageOptions(options)
                .threadPriority(Thread.NORM_PRIORITY - 2).denyCacheImageMultipleSizesInMemory()
                .discCacheFileNameGenerator(new Md5FileNameGenerator()).tasksProcessingOrder(QueueProcessingType.LIFO).build();
        ImageLoader.getInstance().init(config);
    }

    /**
     * 配置ImageLoder
     */
    private void configImageLoaderse() {
        // 初始化ImageLoader
        @SuppressWarnings("deprecation")
        DisplayImageOptions options = new DisplayImageOptions.Builder().showStubImage(R.drawable.picturesss) // 设置图片下载期间显示的图片
                .showImageForEmptyUri(R.drawable.picturesss) // 设置图片Uri为空或是错误的时候显示的图片
                .showImageOnFail(R.drawable.picturesss) // 设置图片加载或解码过程中发生错误显示的图片
                .cacheInMemory(true) // 设置下载的图片是否缓存在内存中
                .cacheOnDisc(true) // 设置下载的图片是否缓存在SD卡中
                // .displayer(new RoundedBitmapDisplayer(20)) // 设置成圆角图片
                .build(); // 创建配置过得DisplayImageOption对象

        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(NewMainActivity.this).defaultDisplayImageOptions(options)
                .threadPriority(Thread.NORM_PRIORITY - 2).denyCacheImageMultipleSizesInMemory()
                .discCacheFileNameGenerator(new Md5FileNameGenerator()).tasksProcessingOrder(QueueProcessingType.LIFO).build();
        ImageLoader.getInstance().init(config);
    }


    /**
     * 显示投保提示信息
     */
    private void showPaywindowdai() {

        Button btnton, btnsaves_pan;
        // 利用layoutInflater获得View
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.popwidndow_xuanze, null);
        // 下面是两种方法得到宽度和高度 getWindow().getDecorView().getWidth()
        window02 = new PopupWindow(view, WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.MATCH_PARENT);
        // 设置popWindow弹出窗体可点击，这句话必须添加，并且是true
        window02.setFocusable(true);
        // 实例化一个ColorDrawable颜色为半透明
        // ColorDrawable dw = new ColorDrawable(R.color.transparent01);
        ColorDrawable dw = new ColorDrawable(android.R.color.white);
        window02.setBackgroundDrawable(dw);
        window02.setOutsideTouchable(true);// 这是点击外部不消失
//			window02.setBackgroundDrawable(new BitmapDrawable());
//			window02.setOutsideTouchable(true);
        // 设置popWindow的显示和消失动画
        window02.setAnimationStyle(R.style.mypopwindow_anim_style);
        // 在底部显示
        window02.showAtLocation(NewMainActivity.this.findViewById(R.id.butfahuo), Gravity.CENTER, 0, 0);
        btnsaves_pan = (Button) view.findViewById(R.id.btnsaves_pan);
        btnton = (Button) view.findViewById(R.id.btnton);
        btnsaves_pan.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                Intent intent = new Intent();
                intent.setClass(NewMainActivity.this, ReleaseActivityto.class);
                if (!receive) {
                    intent.putExtra("cityCode", cityCode2);
                    intent.putExtra("townCode", townCode2);
                    intent.putExtra("fromLatitude", mylatitude);
                    intent.putExtra("fromLongitude", mylongitude);
                    intent.putExtra("fromcity", cityName);

                } else {
                    if (latitude2 == 0.0) {
                        intent.putExtra("fromLatitude", latitude);
                    } else {
                        intent.putExtra("fromLatitude", latitude2);
                    }
                    if (longitude2 == 0.0) {
                        intent.putExtra("fromLongitude", longitude);
                    } else {
                        intent.putExtra("fromLongitude", longitude2);
                    }
//						}
                    intent.putExtra("cityCode", cityCode);
                    intent.putExtra("townCode", townCode);
                    intent.putExtra("fromcity", city);

//						getCityCodeto(true);

                }
                intent.putExtra("status", "sss");
                intent.putExtra("et_address", tvShowLocation.getText().toString());
//					intent.putExtra("et_address", ""+et_address.getText()+et_address_specific.getText());
                if (tvShowLocation.getText().toString().equals("")) {
                    ToastUtil.shortToast(NewMainActivity.this, "发件地为空");
                } else {
                    if (latitude == 5e-324 || latitude == 4.9e-324) {
                        ToastUtil.shortToast(NewMainActivity.this, "定位失败！请允许软件开启定位");
                    } else {
                        startActivity(intent);
                    }

                }
                window02.dismiss();
            }
        });
        btnton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                Intent intent = new Intent();
                intent.setClass(NewMainActivity.this, ReleaseActivity.class);
                if (!receive) {
                    intent.putExtra("cityCode", cityCode2);
                    intent.putExtra("townCode", townCode2);
                    intent.putExtra("fromLatitude", mylatitude);
                    intent.putExtra("fromLongitude", mylongitude);
                    intent.putExtra("fromcity", cityName);

                } else {
                    if (latitude2 == 0.0) {
                        intent.putExtra("fromLatitude", latitude);
                    } else {
                        intent.putExtra("fromLatitude", latitude2);
                    }
                    if (longitude2 == 0.0) {
                        intent.putExtra("fromLongitude", longitude);
                    } else {
                        intent.putExtra("fromLongitude", longitude2);
                    }
//						}
                    intent.putExtra("cityCode", cityCode);
                    intent.putExtra("townCode", townCode);
                    intent.putExtra("fromcity", city);
//						getCityCodeto(true);

                }
                intent.putExtra("status", "sss");
                intent.putExtra("et_address", tvShowLocation.getText().toString());
//					intent.putExtra("et_address", ""+et_address.getText()+et_address_specific.getText());
                if (tvShowLocation.getText().toString().equals("")) {
                    ToastUtil.shortToast(NewMainActivity.this, "发件地为空");
                } else {
                    if (latitude == 5e-324 || latitude == 4.9e-324) {
                        ToastUtil.shortToast(NewMainActivity.this, "定位失败！请允许软件开启定位");
                    } else {
                        startActivity(intent);
                    }

                }
                window02.dismiss();
            }
        });
        // popWindow消失监听方法
        window02.setOnDismissListener(new OnDismissListener() {

            @Override
            public void onDismiss() {
//					addPostResult();
                window02.dismiss();
            }
        });


    }


}
