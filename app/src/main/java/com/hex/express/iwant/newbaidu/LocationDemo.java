package com.hex.express.iwant.newbaidu;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BaiduMap.OnMapClickListener;
import com.baidu.mapapi.map.BaiduMap.OnMapStatusChangeListener;
import com.baidu.mapapi.map.BaiduMapOptions;
import com.baidu.mapapi.map.MapPoi;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.PoiInfo;
import com.baidu.mapapi.search.poi.PoiResult;
import com.hex.express.iwant.R;
import com.hex.express.iwant.iWantApplication;
import com.hex.express.iwant.iflytek.FucUtil;
import com.hex.express.iwant.iflytek.IatSettings;
import com.hex.express.iwant.iflytek.JsonParser;
import com.hex.express.iwant.newbaidu.BaiduMapUtilByRacer.GeoCodePoiListener;
import com.hex.express.iwant.newbaidu.BaiduMapUtilByRacer.LocateListener;
import com.hex.express.iwant.newbaidu.BaiduMapUtilByRacer.PoiSearchListener;
import com.hex.express.iwant.utils.ToastUtil;
import com.iflytek.cloud.ErrorCode;
import com.iflytek.cloud.InitListener;
import com.iflytek.cloud.LexiconListener;
import com.iflytek.cloud.RecognizerListener;
import com.iflytek.cloud.RecognizerResult;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechRecognizer;
import com.iflytek.cloud.SpeechUtility;
import com.iflytek.cloud.ui.RecognizerDialog;
import com.iflytek.cloud.ui.RecognizerDialogListener;
import com.iflytek.cloud.util.ContactManager;
import com.iflytek.cloud.util.ContactManager.ContactListener;
import com.iflytek.sunflower.FlowerCollector;
import com.tencent.bugly.crashreport.CrashReport;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * 此demo用来展示如何结合定位SDK实现定位，并使用MyLocationOverlay绘制定位位置 同时展示如何使用自定义图标绘制并点击时弹出泡泡
 */
public class LocationDemo extends Activity implements OnClickListener {
    private static Context mContext;
    private LocationBean mLocationBean;
    // 定位poi地名信息数据源
    private List<PoiInfo> aroundPoiList;
    private AroundPoiAdapter mAroundPoiAdapter;
    // 搜索模块，也可去掉地图模块独立使用
    private Marker mMarker = null;
    // 搜索当前城市poi数据源
    private static List<LocationBean> searchPoiList;
    private SearchPoiAdapter mSearchPoiAdapter;
    private BaiduMap mBaiduMap;
    // 控件
    private MapView mMapView;
    private EditText etMLCityPoi;
    private TextView tvShowLocation, tvShowLocationse, tetyuyin;
    private LinearLayout llMLMain;
    private ListView lvAroundPoi, lvSearchPoi;
    private ImageView ivMLPLoading, btnLeft;
    private Button btMapZoomIn, btMapZoomOut;
    private ImageButton ibMLLocate;
    private Button lo_sumit, btnyuyin;
    // 標識
    public static final int SHOW_MAP = 0;
    private static final int SHOW_SEARCH_RESULT = 1;
    // 延时多少秒diss掉dialog
    private static final int DELAY_DISMISS = 1000 * 30;
    private String mycity, townaddressd;
    private double mylatitude;
    private double mylongitude;

    private static String TAG = LocationDemo.class.getSimpleName();
    //语音
    // 语音听写对象
    private SpeechRecognizer mIat;
    // 语音听写UI
    private RecognizerDialog mIatDialog;
    // 用HashMap存储听写结果
    private HashMap<String, String> mIatResults = new LinkedHashMap<String, String>();

    private EditText mResultText;
    private Toast mToast;
    private SharedPreferences mSharedPreferences;
    // 引擎类型
    private String mEngineType = SpeechConstant.TYPE_CLOUD;
    // 语记安装助手类

    private boolean mTranslateEnable = true;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.mapview_location_poi);
        iWantApplication.getInstance().addActivity(this);
//		SDKInitializer.initialize(this.getApplicationContext());
//        SDKInitializer.initialize(getApplicationContext());
        this.mContext = this;
//		tvShowLocation.setTextColor(R.color.black);
        ibMLLocate = (ImageButton) findViewById(R.id.ibMLLocate);
        etMLCityPoi = (EditText) findViewById(R.id.etMLCityPoi);
        tvShowLocation = (TextView) findViewById(R.id.tvShowLocation);
        tvShowLocationse = (TextView) findViewById(R.id.tvShowLocationse);
        lvAroundPoi = (ListView) findViewById(R.id.lvPoiList);
        lvSearchPoi = (ListView) findViewById(R.id.lvMLCityPoi);
        ivMLPLoading = (ImageView) findViewById(R.id.ivMLPLoading);
        btnLeft = (ImageView) findViewById(R.id.btnLeft);
        btMapZoomIn = (Button) findViewById(R.id.btMapZoomIn);
        btMapZoomOut = (Button) findViewById(R.id.btMapZoomOut);
        llMLMain = (LinearLayout) findViewById(R.id.llMLMain);
        lo_sumit = (Button) findViewById(R.id.lo_sumit);
        btnyuyin = (Button) findViewById(R.id.btnyuyin);
        tetyuyin = (TextView) findViewById(R.id.tetyuyin);
        // 地图初始化
        mMapView = (MapView) findViewById(R.id.mMapView);
        BaiduMapUtilByRacer.goneMapViewChild(mMapView, true, true);
        // 隐藏缩放控制按钮
        mMapView.showZoomControls(false);
        mBaiduMap = mMapView.getMap();
        mBaiduMap.setMapStatus(MapStatusUpdateFactory.zoomTo(16));
        mBaiduMap.setOnMapStatusChangeListener(mapStatusChangeListener);
        mBaiduMap.setOnMapClickListener(mapOnClickListener);
        mBaiduMap.getUiSettings().setZoomGesturesEnabled(true);// 缩放手势
        // 开启定位图层
        mBaiduMap.setMyLocationEnabled(true);

        initLayout();
        // 初始化识别无UI识别对象
        // 使用SpeechRecognizer对象，可根据回调消息自定义界面；
        mIat = SpeechRecognizer.createRecognizer(this, mInitListener);

        // 初始化听写Dialog，如果只使用有UI听写功能，无需创建SpeechRecognizer
        // 使用UI听写功能，请根据sdk文件目录下的notice.txt,放置布局文件和图片资源
        mIatDialog = new RecognizerDialog(LocationDemo.this, mInitListener);

        mSharedPreferences = getSharedPreferences(IatSettings.PREFER_NAME,
                Activity.MODE_PRIVATE);
        mToast = Toast.makeText(this, "", Toast.LENGTH_SHORT);


        locate();
        iniEvent();


    }


    // 显示地图界面亦或搜索结果界面
    private void showMapOrSearch(int index) {
        if (index == SHOW_SEARCH_RESULT) {
            llMLMain.setVisibility(View.GONE);
            lvSearchPoi.setVisibility(View.VISIBLE);
        } else {
            lvSearchPoi.setVisibility(View.GONE);
            llMLMain.setVisibility(View.VISIBLE);
            if (searchPoiList != null) {
                searchPoiList.clear();
            }
        }
        mIat.stopListening();
    }

    public void locate() {
//		BaiduMapUtilByRacer.moveToTarget(, ,mBaiduMap);
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
                                locationBean.getLongitude(), R.drawable.point,
                                mBaiduMap, 0, true);
                    }

                    public void onLocateFiled() {

                    }

                    public void onLocating() {

                    }
                });
    }

    public void getPoiByPoiSearch() {
        try {
            BaiduMapUtilByRacer.getPoiByPoiSearch(mLocationBean.getCity(),
                    etMLCityPoi.getText().toString().trim(), 0,
                    new PoiSearchListener() {

                        public void onGetSucceed(List<LocationBean> locationList,
                                                 PoiResult res) {
                            if (etMLCityPoi.getText().toString().trim().length() > 0) {
                                if (searchPoiList == null) {
                                    searchPoiList = new ArrayList<LocationBean>();
                                }
                                searchPoiList.clear();
                                searchPoiList.addAll(locationList);
                                updateCityPoiListAdapter();
                            }
                        }

                        public void onGetFailed() {
                            Toast.makeText(mContext, "抱歉，未能找到结果",
                                    Toast.LENGTH_SHORT).show();
                        }
                    });
        } catch (Exception e) {
            // TODO: handle exception
        }
    }

    public void reverseGeoCode(LatLng ll, final boolean isShowTextView) {
        BaiduMapUtilByRacer.getPoisByGeoCode(ll.latitude, ll.longitude,
                new GeoCodePoiListener() {

                    public void onGetSucceed(LocationBean locationBean,
                                             List<PoiInfo> poiList) {
                        mLocationBean = (LocationBean) locationBean.clone();
//						Toast.makeText(
//								mContext,
//								mLocationBean.getProvince() + "-"//省名
//										+ mLocationBean.getCity() + "-"//市名
//										+ mLocationBean.getDistrict() + "-"//区县
//										+ mLocationBean.getStreet() + "-"//街道
//										+ mLocationBean.getStreetNum(),
//								Toast.LENGTH_SHORT).show();
                        // mBaiduMap.setMapStatus(MapStatusUpdateFactory
                        // .newLatLng(new LatLng(locationBean
                        // .getLatitude(), locationBean
                        // .getLongitude())));
//						tvShowLocation.setText(mLocationBean.getProvince()+mLocationBean.getCity()+mLocationBean.getDistrict()+mLocationBean.getStreet());////中间显示的位置信息
//						ToastUtil.shortToast(LocationDemo.this, "city"+mLocationBean.getCity()+"dis"+mLocationBean.getDistrict()+"street"+mLocationBean.getStreet());
                        mycity = mLocationBean.getCity();
                        townaddressd = mLocationBean.getDistrict();
                        mylatitude = mLocationBean.getLatitude();
                        mylongitude = mLocationBean.getLongitude();
                        if (isShowTextView) {
                            tvShowLocation.setText(locationBean.getAddStr());////中间显示的位置信息
//							tvShowLocation.setText(mLocationBean.getProvince()+mLocationBean.getCity()+mLocationBean.getDistrict()+mLocationBean.getStreet());////中间显示的位置信息
                        }

                        if (aroundPoiList == null) {
                            aroundPoiList = new ArrayList<PoiInfo>();
                        }
                        aroundPoiList.clear();
                        if (poiList != null) {
                            aroundPoiList.addAll(poiList);
                            titlei(0);
                        } else {
//							Toast.makeText(mContext, "该周围没有热点",
//									Toast.LENGTH_SHORT).show();
                        }
                        updatePoiListAdapter(aroundPoiList, -1);
                    }

                    public void onGetFailed() {
                        Toast.makeText(mContext, "抱歉，未能找到结果",
                                Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void iniEvent() {
//		btnyuyin.setOnClickListener(new OnClickListener() {
//			
//			@Override
//			public void onClick(View arg0) {
//				// TODO Auto-generated method stub
//				mIatResults.clear();
//				etMLCityPoi.setText(null);// 清空显示内容
//				// 设置参数
//				setParam();
////				if (true) {
////					mIatDialog.setListener(mRecognizerDialogListener);
////					mIatDialog.show();
////					showTip("请开始说话…");
////				} else {
////				ret = mIat.startListening(mRecognizerListener);
////				if (ret != ErrorCode.SUCCESS) {
////					showTip("听写失败!" );
////				} else {
////					showTip("请开始说话…");
////				}
////				}
//			}
//		});

        etMLCityPoi.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (etMLCityPoi.getText().toString().trim().length() > 0) {
                    getPoiByPoiSearch();
                }
            }
        });
        etMLCityPoi.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence cs, int start, int before,
                                      int count) {
                if (cs.toString().trim().length() > 0) {
                    getPoiByPoiSearch();
                } else {
                    if (searchPoiList != null) {
                        searchPoiList.clear();
                    }
                    showMapOrSearch(SHOW_MAP);
                    hideSoftinput(mContext);
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        ibMLLocate.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                locate();
            }
        });
        btMapZoomIn.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                isCanUpdateMap = false;
                BaiduMapUtilByRacer.zoomInMapView(mMapView);
            }
        });
        btMapZoomOut.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                isCanUpdateMap = false;
                BaiduMapUtilByRacer.zoomOutMapView(mMapView);
            }
        });
        //下面list周围信息
        lvAroundPoi.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {
                isCanUpdateMap = false;
                BaiduMapUtilByRacer.moveToTarget(
                        aroundPoiList.get(arg2).location.latitude,
                        aroundPoiList.get(arg2).location.longitude, mBaiduMap);
                tvShowLocation.setText(aroundPoiList.get(arg2).address);//中间显示的位置信息
                tvShowLocationse.setText(aroundPoiList.get(arg2).name);//中间显示的位置信息

                titlei(arg2);
            }
        });
        lvSearchPoi.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {
                // Geo搜索
                // mGeoCoder.geocode(new GeoCodeOption().city(
                // searchPoiList.get(arg2).getCity()).address(
                // searchPoiList.get(arg2).getLocName()));
                hideSoftinput(mContext);
                isCanUpdateMap = false;
                BaiduMapUtilByRacer.moveToTarget(searchPoiList.get(arg2)
                                .getLatitude(), searchPoiList.get(arg2).getLongitude(),
                        mBaiduMap);
//				tvShowLocation.setText(searchPoiList.get(arg2).getLocName());////中间显示的位置信息
                tvShowLocation.setText(searchPoiList.get(arg2).getAddStr());////中间显示的位置信息
                // 反Geo搜索
                reverseGeoCode(new LatLng(
                        searchPoiList.get(arg2).getLatitude(), searchPoiList
                        .get(arg2).getLongitude()), false);
                if (ivMLPLoading != null
                        && ivMLPLoading.getVisibility() == View.GONE) {
                    loadingHandler.sendEmptyMessageDelayed(1, 0);
                }
                showMapOrSearch(SHOW_MAP);
            }
        });
        btnLeft.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                finish();
            }
        });
        lo_sumit.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                Intent intent = new Intent();
                intent.putExtra("city", mycity);
                intent.putExtra("townaddressd", townaddressd);
                intent.putExtra("address", tvShowLocation.getText().toString() + tvShowLocationse.getText().toString());
                intent.putExtra("latitude", mylatitude);
                intent.putExtra("longitude", mylongitude);
                setResult(RESULT_OK, intent);
                finish();
            }
        });
    }

    private void titlei(int potsion) {
        tvShowLocation.setText(aroundPoiList.get(potsion).address);//中间显示的位置信息
        tvShowLocationse.setText(aroundPoiList.get(potsion).name);//中间显示的位置信息
    }

    /**
     * 判定输入汉字
     *
     * @param c
     * @return
     */
    public boolean isChinese(char c) {
        Character.UnicodeBlock ub = Character.UnicodeBlock.of(c);
        if (ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS
                || ub == Character.UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS
                || ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A
                || ub == Character.UnicodeBlock.GENERAL_PUNCTUATION
                || ub == Character.UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION
                || ub == Character.UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS) {
            return true;
        }
        return false;
    }

    @Override
    protected void onPause() {
        mMapView.onPause();
        // 开放统计 移动数据统计分析
        FlowerCollector.onPause(LocationDemo.this);
        super.onPause();
    }

    @Override
    protected void onResume() {
        mMapView.onResume();
        FlowerCollector.onResume(LocationDemo.this);
        super.onResume();
    }

    @Override
    public void onBackPressed() {
        if (llMLMain.getVisibility() == View.GONE) {
            showMapOrSearch(SHOW_MAP);
        } else {
            this.finish();
        }
    }

    ;

    OnMapClickListener mapOnClickListener = new OnMapClickListener() {
        /**
         * 地图单击事件回调函数
         *
         * @param point
         *            点击的地理坐标
         */
        public void onMapClick(LatLng point) {
            hideSoftinput(mContext);
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
    private static Animation hyperspaceJumpAnimation = null;
    Handler loadingHandler = new Handler() {
        @Override
        public void handleMessage(android.os.Message msg) {
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
                    lvAroundPoi.setVisibility(View.GONE);
//				ivMLPLoading.setVisibility(View.VISIBLE);
                    ivMLPLoading.setVisibility(View.GONE);
                    // 使用ImageView显示动画
                    ivMLPLoading.startAnimation(hyperspaceJumpAnimation);
//				if (ivMLPLoading != null&& ivMLPLoading.getVisibility() == View.VISIBLE) {
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

    /**
     * 隐藏软键盘
     */
    private void hideSoftinput(Context mContext) {
        InputMethodManager manager = (InputMethodManager) mContext
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        if (manager.isActive()) {
            manager.hideSoftInputFromWindow(etMLCityPoi.getWindowToken(), 0);
        }
    }

    // 刷新热门地名列表界面的adapter
    private void updatePoiListAdapter(List<PoiInfo> list, int index) {
        ivMLPLoading.clearAnimation();
        ivMLPLoading.setVisibility(View.GONE);
//		lvAroundPoi.setVisibility(View.VISIBLE);
        try {
            if (mAroundPoiAdapter == null) {
                mAroundPoiAdapter = new AroundPoiAdapter(mContext, list);
                lvAroundPoi.setAdapter(mAroundPoiAdapter);
            } else {
                mAroundPoiAdapter.setNewList(list, index);
            }
        } catch (Exception e) {
            // TODO: handle exception
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

    @Override
    protected void onDestroy() {
        mLocationBean = null;
        lvAroundPoi = null;
        lvSearchPoi = null;
        btMapZoomIn.setBackgroundResource(0);
        btMapZoomIn = null;
        btMapZoomOut.setBackgroundResource(0);
        btMapZoomOut = null;
        ibMLLocate.setImageBitmap(null);
        ibMLLocate.setImageResource(0);
        ibMLLocate = null;
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
        if (mBaiduMap != null) {
            mBaiduMap.setMyLocationEnabled(false);// 关闭定位图层
            mBaiduMap = null;
        }
        if (mMapView != null) {
            mMapView.destroyDrawingCache();
            mMapView.onDestroy();
            mMapView = null;
        }
        if (etMLCityPoi != null) {
            etMLCityPoi.setBackgroundResource(0);
            etMLCityPoi = null;
        }
        mMarker = null;
        if (null != mIat) {
            // 退出时释放连接
            mIat.cancel();
            mIat.destroy();
        }
        super.onDestroy();
        System.gc();
    }

    /**
     * 初始化Layout。
     */
    private void initLayout() {
        findViewById(R.id.btnyuyin).setOnClickListener(LocationDemo.this);

        findViewById(R.id.tetyuyin).setOnClickListener(LocationDemo.this);
        findViewById(R.id.iat_recognize_stream).setOnClickListener(LocationDemo.this);
        findViewById(R.id.iat_upload_contacts).setOnClickListener(LocationDemo.this);
        findViewById(R.id.iat_upload_userwords).setOnClickListener(LocationDemo.this);
        findViewById(R.id.iat_stop).setOnClickListener(LocationDemo.this);
        findViewById(R.id.iat_cancel).setOnClickListener(LocationDemo.this);
//		findViewById(R.id.image_iat_set).setOnClickListener(IatDemo.this);
        // 选择云端or本地
        RadioGroup group = (RadioGroup) findViewById(R.id.radioGroup);
        group.setOnCheckedChangeListener(new OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (null == mIat) {
                    // 创建单例失败，与 21001 错误为同样原因，参考 http://bbs.xfyun.cn/forum.php?mod=viewthread&tid=9688
                    showTip("创建对象失败，请确认 libmsc.so 放置正确，且有调用 createUtility 进行初始化");
                    return;
                }

                switch (checkedId) {
                    case R.id.iatRadioCloud:
                        mEngineType = SpeechConstant.TYPE_CLOUD;
                        findViewById(R.id.iat_upload_contacts).setEnabled(true);
                        findViewById(R.id.iat_upload_userwords).setEnabled(true);
                        break;
                    case R.id.iatRadioLocal:
                        mEngineType = SpeechConstant.TYPE_LOCAL;
                        findViewById(R.id.iat_upload_contacts).setEnabled(false);
                        findViewById(R.id.iat_upload_userwords).setEnabled(false);
                        /**
                         * 选择本地听写 判断是否安装语记,未安装则跳转到提示安装页面
                         */
//					if (!SpeechUtility.getUtility().checkServiceInstalled()) {
//						mInstaller.install();
//					} else {
//						String result = FucUtil.checkLocalResource();
//						if (!TextUtils.isEmpty(result)) {
//							showTip(result);
//						}
//					}
                        break;
                    case R.id.iatRadioMix:
                        mEngineType = SpeechConstant.TYPE_MIX;
                        findViewById(R.id.iat_upload_contacts).setEnabled(false);
                        findViewById(R.id.iat_upload_userwords).setEnabled(false);
                        /**
                         * 选择本地听写 判断是否安装语记,未安装则跳转到提示安装页面
                         */
//					if (!SpeechUtility.getUtility().checkServiceInstalled()) {
//						mInstaller.install();
//					} else {
//						String result = FucUtil.checkLocalResource();
//						if (!TextUtils.isEmpty(result)) {
//							showTip(result);
//						}
//					}
                        break;
                    default:
                        break;
                }
            }
        });
    }

    int ret = 0; // 函数调用返回值

    @Override
    public void onClick(View view) {
        if (null == mIat) {
            // 创建单例失败，与 21001 错误为同样原因，参考 http://bbs.xfyun.cn/forum.php?mod=viewthread&tid=9688
            ToastUtil.shortToast(LocationDemo.this, "创建对象失败，请确认 libmsc.so 放置正确，且有调用 ujuj 进行初始化");
            return;
        }

        switch (view.getId()) {
            // 进入参数设置页面
//		case R.id.image_iat_set:
////			Intent intents = new Intent(IatDemo.this, IatSettings.class);
////			startActivity(intents);
//			break;
            // 开始听写
            // 如何判断一次听写结束：OnResult isLast=true 或者 onError
            case R.id.tetyuyin:
                // 移动数据分析，收集开始听写事件
                FlowerCollector.onEvent(LocationDemo.this, "iat_recognize");

                etMLCityPoi.setText(null);// 清空显示内容
                mIatResults.clear();
                // 设置参数
                setParam();
//			boolean isShowDialog = mSharedPreferences.getBoolean(
//					getString(R.string.pref_key_iat_show), true);
                // 显示听写对话框
                try {
                    if (true) {
                        mIatDialog.setListener(mRecognizerDialogListener);
                        mIatDialog.show();
                        showTip("请开始说话…");
                    } else {
                        // 不显示听写对话框
                        ret = mIat.startListening(mRecognizerListener);
                        if (ret != ErrorCode.SUCCESS) {
                            showTip("听写失败");
                        } else {
                            showTip("请开始说话…");
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            // 音频流识别
            case R.id.iat_recognize_stream:
                etMLCityPoi.setText(null);// 清空显示内容
                mIatResults.clear();
                // 设置参数
                setParam();
                // 设置音频来源为外部文件
                mIat.setParameter(SpeechConstant.AUDIO_SOURCE, "-1");
                // 也可以像以下这样直接设置音频文件路径识别（要求设置文件在sdcard上的全路径）：
                // mIat.setParameter(SpeechConstant.AUDIO_SOURCE, "-2");
                // mIat.setParameter(SpeechConstant.ASR_SOURCE_PATH, "sdcard/XXX/XXX.pcm");
                ret = mIat.startListening(mRecognizerListener);
                if (ret != ErrorCode.SUCCESS) {
                    showTip("识别失败,");
                } else {
                    byte[] audioData = FucUtil.readAudioFile(LocationDemo.this, "iattest.wav");

                    if (null != audioData) {
                        showTip(getString(R.string.text_begin_recognizer));
                        // 一次（也可以分多次）写入音频文件数据，数据格式必须是采样率为8KHz或16KHz（本地识别只支持16K采样率，云端都支持），位长16bit，单声道的wav或者pcm
                        // 写入8KHz采样的音频时，必须先调用setParameter(SpeechConstant.SAMPLE_RATE, "8000")设置正确的采样率
                        // 注：当音频过长，静音部分时长超过VAD_EOS将导致静音后面部分不能识别。
                        // 音频切分方法：FucUtil.splitBuffer(byte[] buffer,int length,int spsize);
                        mIat.writeAudio(audioData, 0, audioData.length);
                        mIat.stopListening();
                    } else {
                        mIat.cancel();
                        showTip("读取音频流失败");
                    }
                }
                break;
            // 停止听写
            case R.id.iat_stop:
                mIat.stopListening();
                showTip("停止听写");
                break;
            // 取消听写
            case R.id.iat_cancel:
                mIat.cancel();
                showTip("取消听写");
                break;
            // 上传联系人
            case R.id.iat_upload_contacts:
                showTip(getString(R.string.text_upload_contacts));
                ContactManager mgr = ContactManager.createManager(LocationDemo.this,
                        mContactListener);
                mgr.asyncQueryAllContactsName();
                break;
            // 上传用户词表
            case R.id.iat_upload_userwords:
                showTip(getString(R.string.text_upload_userwords));
                String contents = FucUtil.readFile(LocationDemo.this, "userwords", "utf-8");
                etMLCityPoi.setText(contents);
                // 指定引擎类型
                mIat.setParameter(SpeechConstant.ENGINE_TYPE, SpeechConstant.TYPE_CLOUD);
                mIat.setParameter(SpeechConstant.TEXT_ENCODING, "utf-8");
                ret = mIat.updateLexicon("userword", contents, mLexiconListener);
                if (ret != ErrorCode.SUCCESS)
                    showTip("上传热词失败");
                break;
            default:
                break;
        }
    }

    /**
     * 初始化监听器。
     */
    private InitListener mInitListener = new InitListener() {

        @Override
        public void onInit(int code) {
            Log.d(TAG, "SpeechRecognizer init() code = " + code);
            if (code != ErrorCode.SUCCESS) {
                showTip("初始化失败");
            }
        }
    };

    /**
     * 上传联系人/词表监听器。
     */
    private LexiconListener mLexiconListener = new LexiconListener() {

        @Override
        public void onLexiconUpdated(String lexiconId, SpeechError error) {
            if (error != null) {
                showTip(error.toString());
            } else {
                showTip("传成功");
            }
        }
    };

    /**
     * 听写监听器。
     */
    private RecognizerListener mRecognizerListener = new RecognizerListener() {

        @Override
        public void onBeginOfSpeech() {
            // 此回调表示：sdk内部录音机已经准备好了，用户可以开始语音输入
            showTip("开始说话");
        }

        @Override
        public void onError(SpeechError error) {
            // Tips：
            // 错误码：10118(您没有说话)，可能是录音机权限被禁，需要提示用户打开应用的录音权限。
            // 如果使用本地功能（语记）需要提示用户开启语记的录音权限。
            if (mTranslateEnable && error.getErrorCode() == 14002) {
                showTip(error.getPlainDescription(true) + "\n请确认是否已开通翻译功能");
            } else {
                showTip(error.getPlainDescription(true));
            }
        }

        @Override
        public void onEndOfSpeech() {
            // 此回调表示：检测到了语音的尾端点，已经进入识别过程，不再接受语音输入
            showTip("结束说话");
        }

        @Override
        public void onResult(RecognizerResult results, boolean isLast) {
            Log.d(TAG, results.getResultString());
            if (mTranslateEnable) {
                printTransResult(results);
            } else {
                printResult(results);
            }

            if (isLast) {
                // TODO 最后的结果
            }
        }

        @Override
        public void onVolumeChanged(int volume, byte[] data) {
            showTip("当前正在说话，音量大小：" + volume);
            Log.d(TAG, "返回音频数据：" + data.length);
        }

        @Override
        public void onEvent(int eventType, int arg1, int arg2, Bundle obj) {
            // 以下代码用于获取与云端的会话id，当业务出错时将会话id提供给技术支持人员，可用于查询会话日志，定位出错原因
            // 若使用本地能力，会话id为null
            //	if (SpeechEvent.EVENT_SESSION_ID == eventType) {
            //		String sid = obj.getString(SpeechEvent.KEY_EVENT_SESSION_ID);
            //		Log.d(TAG, "session id =" + sid);
            //	}
        }
    };

    private void printResult(RecognizerResult results) {
        String text = JsonParser.parseIatResult(results.getResultString());

        String sn = null;
        // 读取json结果中的sn字段
        try {
            JSONObject resultJson = new JSONObject(results.getResultString());
            sn = resultJson.optString("sn");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        mIatResults.put(sn, text);

        StringBuffer resultBuffer = new StringBuffer();
        for (String key : mIatResults.keySet()) {
            resultBuffer.append(mIatResults.get(key));
        }

        etMLCityPoi.setText(resultBuffer.toString());
        etMLCityPoi.setSelection(etMLCityPoi.length());
    }

    /**
     * 听写UI监听器
     */
    private RecognizerDialogListener mRecognizerDialogListener = new RecognizerDialogListener() {
        public void onResult(RecognizerResult results, boolean isLast) {
            if (mTranslateEnable) {
                printTransResult(results);
            } else {
                printResult(results);
            }

        }

        /**
         * 识别回调错误.
         */
        public void onError(SpeechError error) {
            if (mTranslateEnable && error.getErrorCode() == 14002) {
                showTip(error.getPlainDescription(true) + "\n请确认是否已开通翻译功能");
            } else {
                showTip(error.getPlainDescription(true));
            }
        }

    };

    /**
     * 获取联系人监听器。
     */
    private ContactListener mContactListener = new ContactListener() {

        @Override
        public void onContactQueryFinish(final String contactInfos, boolean changeFlag) {
            // 注：实际应用中除第一次上传之外，之后应该通过changeFlag判断是否需要上传，否则会造成不必要的流量.
            // 每当联系人发生变化，该接口都将会被回调，可通过ContactManager.destroy()销毁对象，解除回调。
            // if(changeFlag) {
            // 指定引擎类型
            runOnUiThread(new Runnable() {
                public void run() {
                    etMLCityPoi.setText(contactInfos);
                }
            });

            mIat.setParameter(SpeechConstant.ENGINE_TYPE, SpeechConstant.TYPE_CLOUD);
            mIat.setParameter(SpeechConstant.TEXT_ENCODING, "utf-8");
            ret = mIat.updateLexicon("contact", contactInfos, mLexiconListener);
            if (ret != ErrorCode.SUCCESS) {
                showTip("上传联系人失败");
            }
        }
    };

    private void showTip(final String str) {
        mToast.setText(str);
        mToast.show();
    }

    /**
     * 参数设置
     *
     * @return
     */
    public void setParam() {
        // 清空参数
        mIat.setParameter(SpeechConstant.PARAMS, null);

        // 设置听写引擎
        mIat.setParameter(SpeechConstant.ENGINE_TYPE, mEngineType);
        // 设置返回结果格式
        mIat.setParameter(SpeechConstant.RESULT_TYPE, "json");

        this.mTranslateEnable = mSharedPreferences.getBoolean(this.getString(R.string.pref_key_translate), false);
//		mTranslateEnable=true;
        if (mTranslateEnable) {
            Log.i(TAG, "translate enable");
            mIat.setParameter(SpeechConstant.ASR_SCH, "1");
            mIat.setParameter(SpeechConstant.ADD_CAP, "translate");
            mIat.setParameter(SpeechConstant.TRS_SRC, "its");
        }

        String lag = mSharedPreferences.getString("iat_language_preference",
                "mandarin");
        if (lag.equals("en_us")) {
            // 设置语言
            mIat.setParameter(SpeechConstant.LANGUAGE, "en_us");
            mIat.setParameter(SpeechConstant.ACCENT, null);

            if (mTranslateEnable) {
                mIat.setParameter(SpeechConstant.ORI_LANG, "en");
                mIat.setParameter(SpeechConstant.TRANS_LANG, "cn");
            }
        } else {
            // 设置语言
            mIat.setParameter(SpeechConstant.LANGUAGE, "zh_cn");
            // 设置语言区域
            mIat.setParameter(SpeechConstant.ACCENT, lag);

            if (mTranslateEnable) {
                mIat.setParameter(SpeechConstant.ORI_LANG, "cn");
                mIat.setParameter(SpeechConstant.TRANS_LANG, "en");
            }
        }

        // 设置语音前端点:静音超时时间，即用户多长时间不说话则当做超时处理
        mIat.setParameter(SpeechConstant.VAD_BOS, mSharedPreferences.getString("iat_vadbos_preference", "4000"));

        // 设置语音后端点:后端点静音检测时间，即用户停止说话多长时间内即认为不再输入， 自动停止录音
        mIat.setParameter(SpeechConstant.VAD_EOS, mSharedPreferences.getString("iat_vadeos_preference", "1000"));

        // 设置标点符号,设置为"0"返回结果无标点,设置为"1"返回结果有标点
        mIat.setParameter(SpeechConstant.ASR_PTT, mSharedPreferences.getString("iat_punc_preference", "0"));

        // 设置音频保存路径，保存音频格式支持pcm、wav，设置路径为sd卡请注意WRITE_EXTERNAL_STORAGE权限
        // 注：AUDIO_FORMAT参数语记需要更新版本才能生效
        mIat.setParameter(SpeechConstant.AUDIO_FORMAT, "wav");
        mIat.setParameter(SpeechConstant.ASR_AUDIO_PATH, Environment.getExternalStorageDirectory() + "/msc/iat.wav");
    }

    private void printTransResult(RecognizerResult results) {
        String trans = JsonParser.parseTransResult(results.getResultString(), "dst");
        String oris = JsonParser.parseTransResult(results.getResultString(), "src");

        if (TextUtils.isEmpty(trans) || TextUtils.isEmpty(oris)) {
            showTip("解析结果失败，请确认是否已开通翻译功能。");
        } else {
            etMLCityPoi.setText("原始语言:\n" + oris + "\n目标语言:\n" + trans);
        }

    }


}
