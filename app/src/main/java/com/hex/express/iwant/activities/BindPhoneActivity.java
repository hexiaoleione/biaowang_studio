package com.hex.express.iwant.activities;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.TagAliasCallback;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.wechat.friends.Wechat;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.google.gson.Gson;
import com.hex.express.iwant.R;
import com.hex.express.iwant.iWantApplication;
import com.hex.express.iwant.bean.AreaBean;
import com.hex.express.iwant.bean.BaseBean;
import com.hex.express.iwant.bean.CityBean;
import com.hex.express.iwant.bean.RegisterBean;
import com.hex.express.iwant.constance.CommonConstants;
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
import com.hex.express.iwant.utils.DataTools;
import com.hex.express.iwant.utils.PreferencesUtils;
import com.hex.express.iwant.utils.StringUtil;
import com.hex.express.iwant.utils.ToastUtil;
import com.hex.express.iwant.views.LoadingProgressDialog;
import com.hex.express.iwant.views.TitleBarView;
import com.loopj.android.http.AsyncHttpResponseHandler;

public class BindPhoneActivity extends BaseActivity {
    private Timer timer;
    private TimerTask task;
    private int currentTime;
    private TitleBarView tbv_show;
    private EditText edt_registephonenumber;
    private EditText edt_registecode;
    private EditText edt_introducer;
    private Button btn_getsmscode;
    private Button btn_nextstep;
    private String phone_number;
    private String code;
    private String city, cityCode, townCode, townaddressd;

    private LocationClient client;//百度地图的Client
    private double latitude;
    private double longitude;

    // private String smsCode;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registerinfo);
        initView();
        initData();
        setOnClick();
        getData();
    }

    private void resetCurrentTime() {
        currentTime = CommonConstants.GET_CODE_TIME;
    }

    public void handleUiMessage(Message msg) {
        super.handleUiMessage(msg);
        switch (msg.what) {
            case MsgConstants.MSG_01:
                if (currentTime == 0) {
                    resetCurrentTime();
                    btn_getsmscode.setText("获取验证码");
                    task.cancel();
                } else
                    btn_getsmscode.setText(currentTime-- + "秒");
                // btn_getsmscode.setClickable(false);
                break;
            case MsgConstants.MSG_02:
                Set tag = new HashSet();
                tag.add("user");
                JPushInterface.setAliasAndTags(getApplicationContext(), PreferencesUtils.getInt(getApplicationContext(), PreferenceConstants.UID) + "", tag, new TagAliasCallback() {
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

		/*
         * case MsgConstants.MSG_02: etCode.setText(""); currentTime = 0; break;
		 */
        }
    }

    private RegisterBean bean;
    private LoadingProgressDialog dialog;

    @Override
    public void onWeightClick(View v) {
        switch (v.getId()) {
            case R.id.btn_getsmscode:// 获取验证码
                String username = edt_registephonenumber.getText().toString()
                        .trim();
                if (!StringUtil.isMobileNO(username) || (username.length() != 11)) { // 判断是否是正确的手机号码
                    //	Log.e("bbb",use)
                    ToastUtil.shortToastByRec(BindPhoneActivity.this,
                            R.string.mobileisnotvolid);
                    return;
                } else if (currentTime == CommonConstants.GET_CODE_TIME) {
                    PreferencesUtils.putString(getApplicationContext(),
                            PreferenceConstants.MOBILE, username);
                    task = new TimerTask() {

                        @Override
                        public void run() {
                            sendEmptyUiMessage(MsgConstants.MSG_01);
                        }
                    };
                    timer.schedule(task, 0, 1000);
                    final String smsCode = edt_registecode.getText().toString()
                            .trim();
                    Log.e("sms", "codddddd");
                    AsyncHttpUtils.doSimGet(UrlMap.getUrl(MCUrl.LOGINAUTH,
                            PreferenceConstants.MOBILE, username),
                            new AsyncHttpResponseHandler() {

                                @Override
                                public void onSuccess(int arg0, Header[] arg1,
                                                      byte[] arg2) {
                                    Log.e("验证码是：", arg2 + "");
                                    if (arg1 == null)
                                        return;
                                    BaseBean bean = new Gson().fromJson(new String(arg2), BaseBean.class);
                                    if (bean.getErrCode() == 0) {
//									code = bean.getData().get(0).getCode() + "";
                                    }
                                    if (bean.getErrCode() == 0) {
                                        ToastUtil.shortToast(getApplicationContext(), "验证码发送成功");
                                    } else {
                                        ToastUtil.shortToast(getApplicationContext(), bean.getMessage());
                                    }
                                }

                                @Override
                                public void onFailure(int arg0, Header[] arg1,
                                                      byte[] arg2, Throwable arg3) {
                                    Log.e("ccc", arg0 + "");
                                }
                            });
                }
                break;
//		case R.id.btn_nextstep:// 下一步
//			
//			break;
        }
    }

    @Override
    public void initView() {
        // 标题
        tbv_show = (TitleBarView) findViewById(R.id.tbv_show);
        this.tbv_show.setTitleText("绑定手机号");
        // 手机号和验证码
        edt_registephonenumber = (EditText) findViewById(R.id.edt_registephonenumber);
        edt_registecode = (EditText) findViewById(R.id.edt_registecode);
        edt_introducer = (EditText) findViewById(R.id.edt_introducer);
        // 按钮
        btn_getsmscode = (Button) findViewById(R.id.btn_getsmscode);
        btn_nextstep = (Button) findViewById(R.id.btn_nextstep);
        // 设置edittext的监听
    }

    private String deviceId;
    private String openId;
    private String token;
    private String nickName;
    private String sex;
    private String headImageUrl;

    @Override
    public void initData() {
        deviceId = DataTools.getDeviceId(getBaseContext());
        btn_nextstep.setText("完成");
        openId = getIntent().getStringExtra("openId");
        token = getIntent().getStringExtra("accessToken");
        nickName = getIntent().getStringExtra("nickName");
        sex = getIntent().getStringExtra("sex");
        headImageUrl = getIntent().getStringExtra("headImageUrl");
        timer = new Timer();
        // edt_registephonenumber.getText().toString()
        resetCurrentTime();
    }

    @Override
    public void setOnClick() {
        btn_getsmscode.setOnClickListener(this);
        btn_nextstep.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                btn_nextstep.setVisibility(View.GONE);
                if (latitude == 4.9E-324 || longitude == 4.9E-324) {
                    Builder ad = new Builder(BindPhoneActivity.this);
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
                    sumit();
                }


            }
        });
    }

    private void sumit() {

        dialog = new LoadingProgressDialog(BindPhoneActivity.this);

        String userName = edt_registephonenumber.getText().toString()
                .trim();
        String recommendMobile = edt_introducer.getText().toString().trim();
        final String smsCode = edt_registecode.getText().toString().trim();
        if (cityCode.length() < 1) {
            ToastUtil.shortToast(BindPhoneActivity.this, "请开启定位");
            return;
        }
        if (deviceId.length() < 1) {
            ToastUtil.shortToast(BindPhoneActivity.this, "请允许获取手机设备信息");
            return;
        }

        if (!smsCode.equals("") && smsCode.length() == 4) {
            JSONObject obj = new JSONObject();
            if (recommendMobile.equals(userName)) {
                ToastUtil.shortToast(BindPhoneActivity.this, "推荐人不能是自己");
            } else {
                try {
                    obj.put("recommendMobile", recommendMobile);
                    obj.put("openId", openId);
                    obj.put("accessToken", token);
                    obj.put("nickName", nickName);
                    obj.put("sex", sex);
                    obj.put("headImageUrl", headImageUrl);
                    obj.put("mobile", userName);
                    obj.put("code", smsCode);
                    obj.put("deviceId", deviceId);
                    obj.put("cityCode", cityCode);
                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
            btn_nextstep.setClickable(false);
            btn_nextstep.setVisibility(View.GONE);
            dialog.show();
            AsyncHttpUtils.doPostJson(getApplicationContext(), MCUrl.LOGINBINDING, obj.toString(), new AsyncHttpResponseHandler() {
                @Override
                public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
                    btn_nextstep.setClickable(true);
                    btn_nextstep.setVisibility(View.VISIBLE);
                    // TODO Auto-generated method stub
                    if (arg2 == null)
                        return;
                    RegisterBean bean = new Gson().fromJson(new String(arg2), RegisterBean.class);
                    if (bean.getErrCode() == 0 && bean.data != null && bean.data.size() != 0) {
                        dialog.dismiss();
                        PreferencesUtils.putString(getApplicationContext(), PreferenceConstants.PASSWORD, bean.getData().get(0).userCode);
                        PreferencesUtils.putInt(getApplicationContext(), PreferenceConstants.UID, bean.data.get(0).userId);
                        PreferencesUtils.putString(getApplicationContext(), PreferenceConstants.USERTYPE, bean.getData().get(0).userType);
                        sendEmptyBackgroundMessage(MsgConstants.MSG_02);
                        PreferencesUtils.putBoolean(getApplicationContext(), PreferenceConstants.ISLOGIN, true);
                        PreferencesUtils.putString(getApplicationContext(), PreferenceConstants.WLID, bean.getData().get(0).wlid);
                        PreferencesUtils.putString(getApplicationContext(), PreferenceConstants.MOBILE, bean.getData().get(0).mobile);
                        PreferencesUtils.putString(getApplicationContext(), PreferenceConstants.USERNAME, bean.getData().get(0).userName);
                        PreferencesUtils.putBoolean(getApplicationContext(), PreferenceConstants.COMPLETE, bean.getData().get(0).completed);
                        PreferencesUtils.putString(getApplicationContext(), PreferenceConstants.IDCARD, bean.getData().get(0).idCard);
                        PreferencesUtils.putString(getApplicationContext(), PreferenceConstants.ICON_PATH, bean.getData().get(0).idCardPath);
                        PreferencesUtils.putString(getApplicationContext(),
                                PreferenceConstants.REALMANAUTH, bean.data.get(0).realManAuth);

                        //海南用户标识
                        PreferencesUtils.putString(getApplicationContext(), PreferenceConstants.AgreementType, bean.getData().get(0).agreementType);
                        //商户标识
                        PreferencesUtils.putString(getApplicationContext(), PreferenceConstants.shopType, bean.getData().get(0).shopType);

//						PreferencesUtils.putString(getApplicationContext(), PreferenceConstants.USERTYPE,bean.getData().get(0).userType);
                        // PreferencesUtils.putString(getApplicationContext(),
                        // PreferenceConstants.USERNAME, value)
//						startActivity(new Intent(
//								BindPhoneActivity.this,
//								MainActivity.class));

                        JSONObject obj = new JSONObject();
                        try {
                            obj.put("userId", PreferencesUtils.getInt(
                                    getApplicationContext(), PreferenceConstants.UID));
                            obj.put("userType", PreferencesUtils.getString(getApplicationContext(), PreferenceConstants.USERTYPE));
                            obj.put("latitude", latitude);
                            obj.put("longitude", longitude);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        AsyncHttpUtils.doPostJson(getApplicationContext(), MCUrl.UPLOADMYLOCATION,
                                obj.toString(), new AsyncHttpResponseHandler() {

                                    @Override
                                    public void onSuccess(int arg0, Header[] arg1,
                                                          byte[] arg2) {
                                        Log.e("_000000000000000___", new String(arg2));
                                        dialog.dismiss();
                                        if (arg2 == null || arg2.length == 0) {
                                            return;
                                        }
                                        BaseBean bean = new Gson().fromJson(
                                                new String(arg2), BaseBean.class);
                                        Intent intent = new Intent(BindPhoneActivity.this,
                                                RoleAuthenticationActivity.class);
                                        // 跳转注册3/3
//												intent.putExtra("flag", true);
                                        intent.putExtra("tiaoguo", "1");
                                        startActivity(intent);
                                    }

                                    @Override
                                    public void onFailure(int arg0, Header[] arg1,
                                                          byte[] arg2, Throwable arg3) {
                                        dialog.dismiss();
                                    }
                                });


                        finish();
                    } else {
                        Log.e("111   ", bean.getMessage());
                        ToastUtil.shortToast(getApplicationContext(), bean.getMessage());
                    }
                }

                @Override
                public void onFailure(int arg0, Header[] arg1, byte[] arg2, Throwable arg3) {
                    dialog.dismiss();
                    btn_nextstep.setClickable(true);
                    btn_nextstep.setVisibility(View.VISIBLE);
                }
            });
        } else {
            btn_nextstep.setClickable(true);
            btn_nextstep.setVisibility(View.VISIBLE);
            ToastUtil.shortToast(getApplication(), "请输入正确的验证码");
        }
    }

    @Override
    public void getData() {
        Intent intent = getIntent();
        client = new LocationClient(BindPhoneActivity.this);
        initLocation();
        client.registerLocationListener(new BDLocationListener() {

            @Override
            public void onReceiveLocation(BDLocation arg0) {
                if (arg0 == null) {
                    ToastUtil.shortToast(BindPhoneActivity.this, "定位失败，请检查设置");
                    return;
                } else {
                    latitude = arg0.getLatitude();
                    longitude = arg0.getLongitude();
                    city = arg0.getCity();
                    townaddressd = arg0.getDistrict();
                    getCityCode();
//				townaddressd=arg0.getDistrict();
//				Log.e("jpppp", latitude + ":::::::::" + longitude);
                }
            }
        });
        // 初始化定位
        // 打开GPS
        client.start();
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
//			ToastUtil.shortToast(getActivity(), "请输入完整信息");
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
        if (timer != null)
            timer.cancel();
        if (task != null)
            task.cancel();
//		client.stop();
    }
}
