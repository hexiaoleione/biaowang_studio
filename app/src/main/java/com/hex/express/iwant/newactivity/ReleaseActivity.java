package com.hex.express.iwant.newactivity;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.google.gson.Gson;
import com.hex.express.iwant.R;
import com.hex.express.iwant.activities.BaseActivity;
import com.hex.express.iwant.bean.AreaBean;
import com.hex.express.iwant.bean.Beancar;
import com.hex.express.iwant.bean.CityBean;
import com.hex.express.iwant.bean.DownSpecialBean;
import com.hex.express.iwant.bean.RegisterBean;
import com.hex.express.iwant.bean.ReleaseBean;
import com.hex.express.iwant.bean.ShopBean;
import com.hex.express.iwant.constance.MCUrl;
import com.hex.express.iwant.constance.PreferenceConstants;
import com.hex.express.iwant.helper.AreaDboperation;
import com.hex.express.iwant.helper.CheckDbUtils;
import com.hex.express.iwant.helper.CityDbOperation;
import com.hex.express.iwant.helper.DbManager;
import com.hex.express.iwant.http.AsyncHttpUtils;
import com.hex.express.iwant.http.UrlMap;
import com.hex.express.iwant.iWantApplication;
import com.hex.express.iwant.newbaidu.LocationDemo;
import com.hex.express.iwant.utils.DataTools;
import com.hex.express.iwant.utils.Logger;
import com.hex.express.iwant.utils.PreferencesUtils;
import com.hex.express.iwant.utils.ToastUtil;
import com.hex.express.iwant.views.TimePickerView;
import com.hex.express.iwant.witget.DatePicker;
import com.hex.express.iwant.witget.TimePicker;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ReleaseActivity extends BaseActivity {


    @Bind(R.id.tv_modifyRecieverAddress)
    TextView tv_modifyRecieverAddress;

    @Bind(R.id.et_add_address)
    TextView et_add_address;
    @Bind(R.id.et_add_address_specific)
    EditText et_add_address_specific;
    @Bind(R.id.et_weightpner)
    Spinner et_weightpner;
    @Bind(R.id.et_jian)
    Spinner et_jian;
    @Bind(R.id.et_car)
    Spinner et_car;

    @Bind(R.id.et_address)
    TextView et_address;
    @Bind(R.id.tv_modifySenderAddress)
    TextView tv_modifySenderAddress;
    @Bind(R.id.et_address_specific)
    EditText et_address_specific;


    //	@Bind(R.id.et_chang)
//	EditText et_chang;
//	@Bind(R.id.et_kuan)
//	EditText et_kuan;
//	@Bind(R.id.et_gao)
//	EditText et_gao;
    @Bind(R.id.et_time)
    TextView et_time;


    @Bind(R.id.xianshi1)
    Button xianshi1;
    @Bind(R.id.xianshi)
    Button xianshi;
    @Bind(R.id.wuliu)
    Button wuliu;
    @Bind(R.id.shunfen)
    Button shunfen;
    @Bind(R.id.submit)
    Button submit;

    @Bind(R.id.sa)
    LinearLayout sa;
    @Bind(R.id.re_shunfeng)
    RelativeLayout re_shunfeng;
    @Bind(R.id.btn_xianshi)
    RelativeLayout btn_xianshi;
    @Bind(R.id.re_wuliu)
    RelativeLayout re_wuliu;
    @Bind(R.id.zhiyouwuliu)
    RelativeLayout zhiyouwuliu;

    @Bind(R.id.text_mshun)
    TextView text_mshun;
    @Bind(R.id.text_mxian1)
    TextView text_mxian1;
    @Bind(R.id.text_mxian)
    TextView text_mxian;
    @Bind(R.id.imshun)
    ImageView imshun;
    @Bind(R.id.imxian)
    ImageView imimxian;
    @Bind(R.id.imwu)
    ImageView imwu;
    @Bind(R.id.topButton)
    ImageView topButton;


    @Bind(R.id.te_juli)
    TextView te_juli;
    @Bind(R.id.fanshi)
    TextView fanshi;

    @Bind(R.id.zhongjiann)
    TextView zhongjiann;
    @Bind(R.id.zhongcar)
    TextView zhongcar;
    @Bind(R.id.zhongt)
    TextView zhongt;

    @Bind(R.id.Rl_all)
    LinearLayout Rl_all;

    @Bind(R.id.zhongc)
    TextView zhongc;
    @Bind(R.id.text_mwulius)
    TextView text_mwulius;

    @Bind(R.id.text_mwuliu)
    TextView text_mwuliu;
    @Bind(R.id.ssdde)
    Button ssdde;


    private String fet_address;
    private String ftownCode;
    private String fcityCode;
    private double fromLatitude;// 经度
    private double fromLongitude;// 纬度
    private String fromcity;
    private boolean fromreceive;


    private double latitude;
    private double longitude;
    private String city;
    private double mylatitude;// 经度
    private double mylongitude;// 纬度
    private LocationClient client;
    private String cityCode;
    private boolean frist = false;// 是否第一次获取位置成功
    private boolean ding = false;
    private boolean receive;

    Beancar carbeans;
    private boolean fromchange1 = true;
    private boolean change1 = true;

    private boolean change2 = true;
    private boolean change3 = true;
    private boolean change4 = true;
    private boolean change5 = true;
    private boolean change6 = true;

    // 收件人经纬度
    private double receiver_longitude;
    private double receiver_latitude;
    private String receiver_citycode;
    private String townCode, townaddressd;
    private String cityCode2, townCode2, townaddressd2;
    int kes = 0;
    int kesse = 0;
    int cars = 0;
    TimePickerView pvTime;
    DownSpecialBean bean;

    private Calendar calendar;
    private DatePicker dp_test;
    private TimePicker tp_test;
    private TextView tv_ok, tv_cancel;    //确定、取消button
    private Button btn_naozhong;
    private PopupWindow pw;
    private String selectDate, selectTime;
    //选择时间与当前时间，用于判断用户选择的是否是以前的时间
    private String zerotime = "00";
    private int currentHour, currentMinute, currentDay, selectHour, selectMinute, selectDay, maxDayOfMonth;
    private int moyu;
    //整体布局
    boolean timeb = true;
    boolean dadada = false;
    String weatherId = "0";
    String timether = "0";
    int temp = 0;
    ReleaseBean releaseBean;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.release_activity);
        iWantApplication.getInstance().addActivity(this);
        ButterKnife.bind(this);
        Cartype();
        initView();
        initData();
        setOnClick();
        getrequstBalance();
        weather();
//		 obj.put("fromLatitude", ""+getIntent().getDoubleExtra("fromLatitude", 0.0));
//			obj.put("fromLongitude", getIntent().getDoubleExtra("fromLongitude", 0.0));
//			obj.put("cityCode", getIntent().getStringExtra("cityCode"));
//			obj.put("townCode", getIntent().getStringExtra("townCode"));
//			obj.put("address", getIntent().getStringExtra("et_address"));
//		 ToastUtil.shortToast(ReleaseActivity.this, "citycode"+ getIntent().getStringExtra("cityCode"));

        Display display = getWindow().getWindowManager().getDefaultDisplay();
        DisplayMetrics dm = new DisplayMetrics();
        display.getMetrics(dm);
//			Double
        int mWidth = dm.widthPixels;
        LayoutParams lp = (LayoutParams) ssdde.getLayoutParams();
        lp.width = mWidth / 7;
    }

    /**
     * 获取
     */
    private void getrequstBalance() {
        RequestParams params = new RequestParams();
        AsyncHttpUtils.doGet(
                UrlMap.getUrl(MCUrl.BALANCE, "id",
                        String.valueOf(PreferencesUtils.getInt(ReleaseActivity.this, PreferenceConstants.UID))),
                null, null, params, new AsyncHttpResponseHandler() {

                    @Override
                    public void onFailure(int arg0, Header[] arg1, byte[] arg2, Throwable arg3) {
                    }

                    @Override
                    public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
                        // TODO Auto-generated method stub
                        Logger.e("json", "" + new String(arg2));

                        final RegisterBean bean = new Gson().fromJson(new String(arg2), RegisterBean.class);

                        if (bean.getErrCode() == 0) {
                            if ("1".equals(bean.getData().get(0).shopType)) {
                                RequestParams params2 = new RequestParams();

                                AsyncHttpUtils.doGet(
                                        UrlMap.getUrl(MCUrl.getChapman, "userId",
                                                String.valueOf(PreferencesUtils.getInt(getApplicationContext(), PreferenceConstants.UID))),
                                        null, null, params2, new AsyncHttpResponseHandler() {
                                            //									@Override
                                            public void onFailure(int arg0, Header[] arg1, byte[] arg2, Throwable arg3) {
                                            }

                                            @Override
                                            public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
                                                if (arg2 == null)
                                                    return;
                                                Log.e("json1111 ", "" + new String(arg2));
                                                ShopBean shopbean = new Gson().fromJson(new String(arg2), ShopBean.class);
                                                if (shopbean.getErrCode() == 0) {

                                                    et_address_specific.setText(shopbean.data.get(0).address);

                                                }
                                            }
                                        });
                            } else {
                            }
//							if ("2".equals(getIntent().getStringExtra("nochange"))) {
//								et_name.setText( getIntent().getStringExtra("personName"));
//								et_tel.setText( getIntent().getStringExtra("mobile"));
//							}else {
//								 et_name.setText(bean.data.get(0).userName);
//								 et_tel.setText(bean.data.get(0).mobile);
//							}

                        }

                    }

                });
    }

    @Override
    public void onWeightClick(View v) {
        // TODO Auto-generated method stub

    }

    @Override
    public void initView() {
        // TODO Auto-generated method stub
//		pvTime.show();
        client = new LocationClient(ReleaseActivity.this);
        initLocation();
        client.registerLocationListener(new BDLocationListener() {

            @Override
            public void onReceiveLocation(BDLocation arg0) {
                if (arg0 == null) {
                    ToastUtil.shortToast(ReleaseActivity.this, "定位失败，请检查设置");
                    latitude = 0.0;
                    longitude = 0.0;
                    return;
                } else {
                    latitude = arg0.getLatitude();
                    longitude = arg0.getLongitude();
//				townaddressd=arg0.getDistrict();
                    Log.e("jpppp", latitude + ":::::::::" + longitude);
                }
            }
        });
        // 初始化定位
        // 打开GPS
        client.start();

        if ("sss".equals(getIntent().getStringExtra("status"))) {
            et_address.setText(getIntent().getStringExtra("et_address"));
            fet_address = getIntent().getStringExtra("et_address");
            ftownCode = getIntent().getStringExtra("townCode");
            fcityCode = getIntent().getStringExtra("cityCode");
            fromcity = getIntent().getStringExtra("fromcity");
            fromLatitude = getIntent().getDoubleExtra("fromLatitude", 0.0);
            fromLongitude = getIntent().getDoubleExtra("fromLongitude", 0.0);
        } else {
            et_address.setText(getIntent().getStringExtra("address"));
            fet_address = getIntent().getStringExtra("et_address");
            ftownCode = getIntent().getStringExtra("townCode");
            fcityCode = getIntent().getStringExtra("cityCode");
            if ("".equals(getIntent().getStringExtra("cargoVolume"))) {
                fromLatitude = Double.valueOf(getIntent().getStringExtra("fromLatitude")).doubleValue();
                fromLongitude = Double.valueOf(getIntent().getStringExtra("fromLongitude")).doubleValue();
            }

            et_add_address.setText(getIntent().getStringExtra("addressTo"));
        }


//		ToastUtil.longToast(ReleaseActivity.this, "ftownCode"+ftownCode);
//		ToastUtil.longToast(ReleaseActivity.this, "fcityCode"+fcityCode);
//		ToastUtil.longToast(ReleaseActivity.this, "fromLatitude"+fromLatitude);
//		ToastUtil.longToast(ReleaseActivity.this, "fromLongitude"+fromLongitude);
        topButton.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                finish();
            }
        });
    }

    @Override
    public void initData() {
        // TODO Auto-generated method stub
        tv_modifySenderAddress.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                startActivityForResult(new Intent(ReleaseActivity.this, LocationDemo.class), 15);
                submit.setVisibility(View.VISIBLE);
                sa.setVisibility(View.GONE);
                te_juli.setVisibility(View.GONE);
                fanshi.setVisibility(View.GONE);
            }
        });
        shunfen.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                Intent intent = new Intent();
                intent.putExtra("latitude", latitude);
                intent.putExtra("longitude", longitude);
                intent.putExtra("fromLatitude", fromLatitude);
                intent.putExtra("fromLongitude", fromLongitude);
                intent.putExtra("cityCode", fcityCode);
                intent.putExtra("townCode", ftownCode);
                intent.putExtra("address", et_address.getText().toString() + et_address_specific.getText().toString());
                intent.putExtra("addressTo", et_add_address.getText().toString() + et_add_address_specific.getText().toString());
//				obj.put("addressTo", et_address.getText().toString());
                intent.putExtra("cityCodeTo", receiver_citycode);
                intent.putExtra("toLatitude", receiver_latitude);
                intent.putExtra("toLongitude", receiver_longitude);
                intent.putExtra("matWeight", sunmit(5, kes));//重量
//				intent.putExtra("length", et_chang.getText().toString());// length
//				intent.putExtra("wide", et_kuan.getText().toString());
//				intent.putExtra("high", et_gao.getText().toString());
//				intent.putExtra("limitTime", et_time.getText().toString());
                if ("0".equals(timether)) {
                    //获取当前时间一个小时之后的
                    Calendar now = Calendar.getInstance();
                    now.add(Calendar.HOUR, 1);
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                    String dateStr = sdf.format(now.getTimeInMillis());
                    et_time.setText(dateStr);
                    intent.putExtra("limitTime", et_time.getText().toString());
                } else {
                    intent.putExtra("limitTime", et_time.getText().toString());
                }
                intent.putExtra("nochange", "1");
                intent.putExtra("cargoSize", sunmit(1, kesse));
                intent.putExtra("nochangedi", "1");
                intent.putExtra("carType", carbeans.data.get(cars).carType);
                intent.putExtra("matVolume", carbeans.data.get(cars).carName);
                intent.putExtra("weatherId", weatherId);
                intent.putExtra("temp", temp);

                intent.setClass(ReleaseActivity.this, LogisewindActivity.class);
                try {
                    intent.putExtra("insureUrl", releaseBean.data.get(0).insureUrl);

                } catch (Exception e) {
                    // TODO: handle exception
                }
//				startActivity(intent);
                startActivityForResult(intent, 10);
//				ToastUtil.shortToast(ReleaseActivity.this, "dd"+et_chang.getText().toString());
                submit.setVisibility(View.VISIBLE);
                sa.setVisibility(View.GONE);
                te_juli.setVisibility(View.GONE);
                fanshi.setVisibility(View.GONE);
            }
        });
        xianshi.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub

                Intent intent = new Intent();

                intent.putExtra("latitude", latitude);
                intent.putExtra("longitude", longitude);
                intent.putExtra("fromLatitude", fromLatitude);
                intent.putExtra("fromLongitude", fromLongitude);
                intent.putExtra("cityCode", fcityCode);
                intent.putExtra("townCode", ftownCode);
                intent.putExtra("address", et_address.getText().toString() + et_address_specific.getText().toString());
                intent.putExtra("addressTo", et_add_address.getText().toString() + et_add_address_specific.getText().toString());
//				obj.put("addressTo", et_address.getText().toString());
                intent.putExtra("cityCodeTo", receiver_citycode);
                intent.putExtra("toLatitude", receiver_latitude);
                intent.putExtra("toLongitude", receiver_longitude);
                intent.putExtra("matWeight", sunmit(5, kes));//重量
                intent.putExtra("nochangedi", "1");
                intent.putExtra("nochange", "1");
//				intent.putExtra("length", et_chang.getText().toString());// length
//				intent.putExtra("wide", et_kuan.getText().toString());
//				intent.putExtra("high", et_gao.getText().toString());if
                if ("0".equals(timether)) {
                    //获取当前时间一个小时之后的
                    Calendar now = Calendar.getInstance();
                    now.add(Calendar.HOUR, 1);
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                    String dateStr = sdf.format(now.getTimeInMillis());
                    et_time.setText(dateStr);
                    intent.putExtra("limitTime", et_time.getText().toString());
                } else {
                    intent.putExtra("limitTime", et_time.getText().toString());
                }


                intent.putExtra("cargoSize", sunmit(1, kesse));
                intent.putExtra("carType", carbeans.data.get(cars).carType);
                intent.putExtra("matVolume", carbeans.data.get(cars).carName);
                intent.putExtra("weatherId", weatherId);
                intent.putExtra("temp", temp);
                try {
                    intent.putExtra("insureUrl", releaseBean.data.get(0).insureUrl);

                } catch (Exception e) {
                    // TODO: handle exception
                }

                intent.setClass(ReleaseActivity.this, LogisewindtimlitActivity.class);
//				startActivity(intent);
                startActivityForResult(intent, 11);
                submit.setVisibility(View.VISIBLE);
                sa.setVisibility(View.GONE);
                te_juli.setVisibility(View.GONE);
                fanshi.setVisibility(View.GONE);
            }
        });

        wuliu.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                Intent intent = new Intent();

                intent.putExtra("latitude", latitude);
                intent.putExtra("longitude", longitude);
                intent.putExtra("fromLatitude", fromLatitude);
                intent.putExtra("fromLongitude", fromLongitude);
                intent.putExtra("cityCode", fcityCode);
                intent.putExtra("townCode", ftownCode);
                intent.putExtra("address", et_address.getText().toString() + et_address_specific.getText().toString());
                intent.putExtra("addressTo", et_add_address.getText().toString() + et_add_address_specific.getText().toString());
//				obj.put("addressTo", et_address.getText().toString());
                intent.putExtra("cityCodeTo", receiver_citycode);
                intent.putExtra("toLatitude", receiver_latitude);
                intent.putExtra("toLongitude", receiver_longitude);
                intent.putExtra("matWeight", sunmit(5, kes));//重量
                intent.putExtra("nochange", "1");
                intent.putExtra("nochangedi", "1");
//				intent.putExtra("length", et_chang.getText().toString());// length
//				intent.putExtra("wide", et_kuan.getText().toString());
//				intent.putExtra("high", et_gao.getText().toString());
                intent.putExtra("limitTime", et_time.getText().toString());
                intent.putExtra("cargoSize", sunmit(1, kesse));
                intent.putExtra("carType", carbeans.data.get(cars).carType);
                intent.putExtra("matVolume", carbeans.data.get(cars).carName);
                intent.putExtra("cargoVolume", "");
                intent.setClass(ReleaseActivity.this, LoginseticsActivity.class);
//				startActivity(intent);
                startActivityForResult(intent, 12);
                submit.setVisibility(View.VISIBLE);
                sa.setVisibility(View.GONE);
                te_juli.setVisibility(View.GONE);
                fanshi.setVisibility(View.GONE);
            }
        });
//		zhiyouwuliu.setVisibility(View.VISIBLE);
//		sa.setVisibility(View.GONE); 
        zhiyouwuliu.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                Intent intent = new Intent();
                intent.putExtra("latitude", latitude);
                intent.putExtra("longitude", longitude);
                intent.putExtra("fromLatitude", fromLatitude);
                intent.putExtra("fromLongitude", fromLongitude);
                intent.putExtra("cityCode", fcityCode);
                intent.putExtra("townCode", ftownCode);
                intent.putExtra("address", et_address.getText().toString() + et_address_specific.getText().toString());
                intent.putExtra("addressTo", et_add_address.getText().toString() + et_add_address_specific.getText().toString());
//				obj.put("addressTo", et_address.getText().toString());
                intent.putExtra("cityCodeTo", receiver_citycode);
                intent.putExtra("toLatitude", receiver_latitude);
                intent.putExtra("toLongitude", receiver_longitude);
                intent.putExtra("nochangedi", "1");
                intent.putExtra("matWeight", sunmit(5, kes));//重量
                intent.putExtra("nochange", "1");
//				intent.putExtra("length", et_chang.getText().toString());// length
//				intent.putExtra("wide", et_kuan.getText().toString());
//				intent.putExtra("high", et_gao.getText().toString());
                intent.putExtra("limitTime", et_time.getText().toString());
                intent.putExtra("cargoSize", sunmit(1, kesse));
                intent.putExtra("carType", carbeans.data.get(cars).carType);
                intent.putExtra("matVolume", carbeans.data.get(cars).carName);
                intent.putExtra("cargoVolume", "");

                intent.setClass(ReleaseActivity.this, LoginseticsActivity.class);
                startActivityForResult(intent, 12);
                submit.setVisibility(View.VISIBLE);
                sa.setVisibility(View.GONE);
                te_juli.setVisibility(View.GONE);
                fanshi.setVisibility(View.GONE);
            }
        });


        submit.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
//				Intent  intent=new Intent();
//				intent.setClass(ReleaseActivity.this, LoginseticsActivity.class);
////				startActivity(intent);
//				startActivityForResult(intent, 12);

                if ("sss".equals(getIntent().getStringExtra("status"))) {

//				fet_address=getIntent().getStringExtra("et_address");
//				ftownCode=getIntent().getStringExtra("townCode");
//				fcityCode=getIntent().getStringExtra("cityCode");
//				fromLatitude=getIntent().getDoubleExtra("fromLatitude", 0.0);
//				fromLongitude=getIntent().getDoubleExtra("fromLongitude", 0.0);
                    if ("".equals(et_address.getText().toString()) || "".equals(ftownCode)
                            || "".equals(fcityCode) || fromLatitude == 0.0
                            || fromLongitude == 0.0
                            ) {//|| et_weight.getText().toString().equals("")
                        ToastUtil.shortToast(ReleaseActivity.this, "发件地位置不准确");
                        return;
                    }
                    if ("cold".equals(carbeans.data.get(cars).carType)) {
                        Intent intent = new Intent();
                        intent.putExtra("latitude", latitude);
                        intent.putExtra("longitude", longitude);
                        intent.putExtra("fromLatitude", fromLatitude);
                        intent.putExtra("fromLongitude", fromLongitude);
                        intent.putExtra("cityCode", fcityCode);
                        intent.putExtra("townCode", ftownCode);
                        intent.putExtra("address", et_address.getText().toString() + et_address_specific.getText().toString());
                        intent.putExtra("addressTo", et_add_address.getText().toString() + et_add_address_specific.getText().toString());
//					obj.put("addressTo", et_address.getText().toString());
                        intent.putExtra("cityCodeTo", receiver_citycode);
                        intent.putExtra("toLatitude", receiver_latitude);
                        intent.putExtra("toLongitude", receiver_longitude);
                        intent.putExtra("nochangedi", "1");
                        intent.putExtra("matWeight", sunmit(5, kes));//重量
                        intent.putExtra("nochange", "1");
//					intent.putExtra("length", et_chang.getText().toString());// length
//					intent.putExtra("wide", et_kuan.getText().toString());
//					intent.putExtra("high", et_gao.getText().toString());
                        intent.putExtra("limitTime", et_time.getText().toString());
                        intent.putExtra("cargoSize", sunmit(1, kesse));
                        intent.putExtra("carType", carbeans.data.get(cars).carType);
                        intent.putExtra("matVolume", carbeans.data.get(cars).carName);
                        intent.putExtra("cargoVolume", "");
                        intent.putExtra("cccc", "cd");
//					intent.setClass(ReleaseActivity.this, LoginseticsActivity.class);
                        intent.setClass(ReleaseActivity.this, LoginseticsActivityto.class);
                        startActivityForResult(intent, 12);
                    } else {
                        addPostResult();
//					Intent intent = new Intent(ReleaseActivity.this, DownWindPayActivity.class);
//					intent.putExtra("money","11");
//					intent.putExtra("recId", 1);
//					intent.putExtra("payUserId", "");
////					intent.putExtra("insureCost", String.valueOf(df.format(bean.data.get(0).insureCost)));
//					intent.putExtra("insureCost", "22");
//					intent.putExtra("billCode", "d3333");
//					intent.putExtra("distance","33");
//					intent.putExtra("baofei", "44");
//					intent.putExtra("daishou","");
//					intent.putExtra("tyy","专程送");
//					startActivityForResult(intent, 10);

                    }

                } else {
                    if (et_time.getText().toString().equals("")
                            ) {//|| et_weight.getText().toString().equals("")
                        ToastUtil.shortToast(ReleaseActivity.this, "时间未选择");
                        return;
                    }
                    Intent intent = new Intent();

                    if (!fromchange1) {
                        intent.putExtra("latitude", latitude);
                        intent.putExtra("longitude", longitude);

                        intent.putExtra("fromLatitude", fromLatitude);
                        intent.putExtra("fromLongitude", fromLongitude);
                        intent.putExtra("cityCode", fcityCode);
                        intent.putExtra("townCode", ftownCode);
                        intent.putExtra("address", et_address.getText().toString() + et_address_specific.getText().toString());
                        intent.putExtra("nochangefa", "1");

                    } else {
                        intent.putExtra("latitude", latitude);
                        intent.putExtra("longitude", longitude);

                        intent.putExtra("fromLatitude", getIntent().getStringExtra("fromLatitude"));
                        intent.putExtra("fromLongitude", getIntent().getStringExtra("fromLongitude"));
                        intent.putExtra("cityCode", getIntent().getStringExtra("cityCode"));
                        intent.putExtra("townCode", getIntent().getStringExtra("townCode"));
                        intent.putExtra("address", getIntent().getStringExtra("address"));
                        intent.putExtra("nochangefa", "2");
                    }
                    //地址
                    if (!change1) {
                        intent.putExtra("addressTo", et_add_address.getText().toString() + et_add_address_specific.getText().toString());
                        intent.putExtra("cityCodeTo", receiver_citycode);
                        intent.putExtra("toLatitude", receiver_latitude);
                        intent.putExtra("toLongitude", receiver_longitude);
                        intent.putExtra("nochangedi", "1");
                    } else {
                        intent.putExtra("addressTo", getIntent().getStringExtra("addressTo"));
                        intent.putExtra("cityCodeTo", getIntent().getStringExtra("cityCodeTo"));
                        intent.putExtra("toLatitude", getIntent().getStringExtra("toLatitude"));
                        intent.putExtra("toLongitude", getIntent().getStringExtra("toLongitude"));
                        intent.putExtra("nochangedi", "2");
                    }

                    //重量
                    if (change2 == false) {
                        if (kes == 0) {
                            intent.putExtra("matWeight", sunmit(5, kes));//重量
                        } else {
                            intent.putExtra("matWeight", sunmit(5, kes - 1));//重量
                        }

                        intent.putExtra("nochange1", "1");
                    } else {
                        intent.putExtra("matWeight", getIntent().getStringExtra("matWeight"));
                        intent.putExtra("nochange1", "2");
                    }

                    //j、件数1
                    if (change3 == false) {
                        if (kesse == 0) {
                            intent.putExtra("cargoSize", sunmit(1, kesse));
                        } else {
                            intent.putExtra("cargoSize", sunmit(1, kesse - 1));
                        }

                        intent.putExtra("nochange2", "1");
                    } else {
                        intent.putExtra("cargoSize", getIntent().getStringExtra("cargoSize"));
                        intent.putExtra("nochange3", "2");
                    }
                    //车型
                    if (change4 == false) {
                        if (cars == 0) {
//							intent.putExtra("cargoSize", sunmit(1, kesse));
                            intent.putExtra("carType", carbeans.data.get(cars).carType);
                            intent.putExtra("matVolume", carbeans.data.get(cars).carName);
                        } else {
//							intent.putExtra("cargoSize", sunmit(1, kesse-1));
                            intent.putExtra("carType", carbeans.data.get(cars - 1).carType);
                            intent.putExtra("matVolume", carbeans.data.get(cars - 1).carName);
                        }

                        intent.putExtra("nochange4", "1");

                    } else {
//						ToastUtil.shortToast(ReleaseActivity.this, "222"+change4);
                        intent.putExtra("carType", getIntent().getStringExtra("carType"));
                        intent.putExtra("matVolume", getIntent().getStringExtra("matVolume"));
                        intent.putExtra("nochange4", "2");
                    }
                    intent.putExtra("nochange", "2");

                    intent.putExtra("limitTime", et_time.getText().toString());
                    intent.putExtra("personName", getIntent().getStringExtra("personName"));
                    intent.putExtra("mobile", getIntent().getStringExtra("mobile"));
                    intent.putExtra("personNameTo", getIntent().getStringExtra("personNameTo"));
                    intent.putExtra("mobileTo", getIntent().getStringExtra("mobileTo"));
//					intent.putExtra("limitTime", getIntent().getStringExtra("limitTime"));
                    intent.putExtra("replaceMoney", getIntent().getStringExtra("replaceMoney"));
                    intent.putExtra("ifReplaceMoney", getIntent().getStringExtra("ifReplaceMoney"));
                    intent.putExtra("ifTackReplace", getIntent().getStringExtra("ifTackReplace"));
                    intent.putExtra("insureCost", getIntent().getStringExtra("insureCost"));
                    intent.putExtra("premium", getIntent().getStringExtra("premium"));
                    intent.putExtra("matName", getIntent().getStringExtra("matName"));
                    intent.putExtra("matRemark", getIntent().getStringExtra("matRemark"));

                    intent.putExtra("whether", getIntent().getStringExtra("whether"));
                    intent.putExtra("ifReplaceMoney", getIntent().getStringExtra("ifReplaceMoney"));


                    if ("".equals(getIntent().getStringExtra("cargoVolume"))) {

                        if ("".equals(getIntent().getStringExtra("limitTime"))) {
//						intent.putExtra("latitude", latitude);
//						intent.putExtra("longitude", longitude);
//
//						intent.putExtra("fromLatitude", getIntent().getStringExtra("fromLatitude"));
//						intent.putExtra("fromLongitude", getIntent().getStringExtra("fromLongitude"));
                            intent.setClass(ReleaseActivity.this, LogisewindActivity.class);
                        } else {
//						ToastUtil.shortToast(ReleaseActivity.this, "限时");

                            intent.setClass(ReleaseActivity.this, LogisewindtimlitActivity.class);
                        }

                    } else {
                        if ("cold".equals(getIntent().getStringExtra("carType"))) {
                            intent.putExtra("latitude", getIntent().getStringExtra("fromLatitude"));
                            intent.putExtra("longitude", getIntent().getStringExtra("fromLongitude"));
                            intent.putExtra("cargoVolume", getIntent().getStringExtra("cargoVolume"));
                            intent.putExtra("appontSpace", getIntent().getStringExtra("appontSpace"));
                            intent.putExtra("takeCargo", getIntent().getBooleanExtra("takeCargo", true));
                            intent.putExtra("sendCargo", getIntent().getBooleanExtra("sendCargo", true));
                            intent.putExtra("tem", getIntent().getStringExtra("tem"));

                            intent.putExtra("cccc", "c");
                            intent.setClass(ReleaseActivity.this, LoginseticsActivityto.class);
                        } else {
//							ToastUtil.shortToast(ReleaseActivity.this, "顺风 "+getIntent().getStringExtra("carType"));
                            intent.putExtra("latitude", getIntent().getStringExtra("fromLatitude"));
                            intent.putExtra("longitude", getIntent().getStringExtra("fromLongitude"));
                            intent.putExtra("cargoVolume", getIntent().getStringExtra("cargoVolume"));
                            intent.putExtra("appontSpace", getIntent().getStringExtra("appontSpace"));
                            intent.putExtra("takeCargo", getIntent().getBooleanExtra("takeCargo", true));
                            intent.putExtra("sendCargo", getIntent().getBooleanExtra("sendCargo", true));

                            intent.setClass(ReleaseActivity.this, LoginseticsActivity.class);
                        }
//						
                    }
                    startActivityForResult(intent, 11);
                }

            }
        });

    }

    @Override
    public void setOnClick() {
        // TODO Auto-generated method stub
        //获取当前时间一个小时之后的
        Calendar now = Calendar.getInstance();
        now.add(Calendar.HOUR, 1);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        String dateStr = sdf.format(now.getTimeInMillis());
        et_time.setText(dateStr);
        timether = "0";
        et_time.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
//				pvTime.show();
//				submit.setVisibility(View.VISIBLE);
                calendar = Calendar.getInstance();
                View view = View.inflate(ReleaseActivity.this, R.layout.dialog_select_time, null);
                if (calendar.get(Calendar.DAY_OF_MONTH) < 10 && (calendar.get(Calendar.MONTH) + 1) < 10) {
                    selectDate = calendar.get(Calendar.YEAR) + "-" + "0" + (calendar.get(Calendar.MONTH) + 1) + "-"
                            + "0" + calendar.get(Calendar.DAY_OF_MONTH);
                } else if ((calendar.get(Calendar.DAY_OF_MONTH) < 10)) {
                    selectDate = calendar.get(Calendar.YEAR) + "-" + (calendar.get(Calendar.MONTH) + 1) + "-"
                            + "0" + calendar.get(Calendar.DAY_OF_MONTH);
                } else if ((calendar.get(Calendar.MONTH) + 1) < 10) {
                    selectDate = calendar.get(Calendar.YEAR) + "-" + "0" + (calendar.get(Calendar.MONTH) + 1) + "-"
                            + calendar.get(Calendar.DAY_OF_MONTH);
                } else {
                    selectDate = calendar.get(Calendar.YEAR) + "-" + (calendar.get(Calendar.MONTH) + 1) + "-"
                            + calendar.get(Calendar.DAY_OF_MONTH);
                }
                //选择时间与当前时间的初始化，用于判断用户选择的是否是以前的时间，如果是，弹出toss提示不能选择过去的时间
                moyu = (calendar.get(Calendar.MONTH) + 1);
                selectDay = currentDay = calendar.get(Calendar.DAY_OF_MONTH);
                maxDayOfMonth = Calendar.getInstance().getActualMaximum(Calendar.DAY_OF_MONTH);
//				    int maxDayOfMonth = Calendar.getInstance().getActualMaximum(Calendar.DAY_OF_MONTH);
                selectMinute = currentMinute = calendar.get(Calendar.MINUTE) + 5;
                selectHour = currentHour = calendar.get(Calendar.HOUR_OF_DAY) + 1;
//			        if ((currentMinute+35)>55){
//	                    selectHour = currentHour + 1;
//	                        selectMinute = (currentMinute + 35 - 60) >= ((currentMinute + 35 - 60) / 10 * 10 + 5) ? ((currentMinute + 35 - 60) / 10 + 1) * 10 + 5 : ((currentMinute + 35 - 60) / 10) * 10 + 5;
////	                    selectTime = selectHour + ":" + selectMinute;//zerotime
//	                        selectTime = selectHour + ":" + ((selectMinute < 10) ? ("0" + selectMinute) : selectMinute);//zerotime
//	                }else {
//	                    selectMinute = (currentMinute + 35) >= ((currentMinute + 35) / 10 * 10 + 5) ? ((currentMinute + 35) / 10 + 1) * 10 + 5 : ((currentMinute + 35) / 10) * 10 + 5;
////	                    selectTime = selectHour + ":" + selectMinute;//zerotime
//	                    selectTime = selectHour + ":" + ((selectMinute < 10) ? ("0" + selectMinute) : selectMinute);//zerotime
//	                }
                if ((currentMinute) > 59) {
                    selectHour = currentHour + 1;
//	                        selectMinute = (currentMinute + 5 - 60) >= ((currentMinute + 5 - 60) / 10 * 10 + 5) ? ((currentMinute + 5 - 60) / 10 + 1) * 10 + 5 : ((currentMinute + 5 - 60) / 10) * 10 + 5;
                    selectMinute = (currentMinute - 60) >= ((currentMinute - 60) / 5 * 5) ? ((currentMinute - 60) / 5) * 5 : ((currentMinute - 60) / 5) * 5;
                    if (selectHour > 23) {
                        selectHour = 0;
                        selectDay += 1;
                        if (selectDay <= maxDayOfMonth) {
                            selectDate = calendar.get(Calendar.YEAR) + "-" + (calendar.get(Calendar.MONTH) + 1) + "-"
                                    + selectDay;
                        } else {
                            selectDay = selectDay - maxDayOfMonth;
                            selectDate = calendar.get(Calendar.YEAR) + "-" + (calendar.get(Calendar.MONTH) + 2) + "-"
                                    + selectDay;
                        }

                    }
//	                    if (selectHour==24) {
//	                    	selectDay+=1;
//	                    	selectHour=0;
//						}
//	                    Toast.makeText(getApplicationContext(), ""+selectMinute +"  hs "+ currentMinute, Toast.LENGTH_LONG).show();
                    selectTime = selectHour + ":" + ((selectMinute < 10) ? ("0" + selectMinute) : selectMinute);//zerotime
                } else {
                    selectMinute = ((currentMinute) >= ((currentMinute) / 5 * 5) ? ((currentMinute) / 5) * 5 : ((currentMinute) / 5) * 5);
//	                    selectMinute = (currentMinute + 5) >= ((currentMinute + 5) / 10 * 10 + 5) ? ((currentMinute + 5) / 10 + 1) * 10 + 5 : ((currentMinute + 5) / 10) * 10 + 5;
                    if (selectMinute > 59) {
                        selectMinute -= 60;
                        selectHour += 1;
                    }
                    if (selectHour > 23) {
                        selectHour = 0;
                        selectDay += 1;
                        if (selectDay <= maxDayOfMonth) {
                            selectDate = calendar.get(Calendar.YEAR) + "-" + (calendar.get(Calendar.MONTH) + 1) + "-"
                                    + selectDay;
                        } else {
                            selectDay = selectDay - maxDayOfMonth;
                            selectDate = calendar.get(Calendar.YEAR) + "-" + (calendar.get(Calendar.MONTH) + 2) + "-"
                                    + selectDay;
                        }

                    }
                    selectTime = selectHour + ":" + ((selectMinute < 10) ? ("0" + selectMinute) : selectMinute);//zerotime
                }


                dp_test = (DatePicker) view.findViewById(R.id.dp_test);
                tp_test = (TimePicker) view.findViewById(R.id.tp_test);
                tv_ok = (TextView) view.findViewById(R.id.tv_ok);
                tv_cancel = (TextView) view.findViewById(R.id.tv_cancel);
                //设置滑动改变监听器
                dp_test.setOnChangeListener(dp_onchanghelistener);
                tp_test.setOnChangeListener(tp_onchanghelistener);
                pw = new PopupWindow(view, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, true);
//				//设置这2个使得点击pop以外区域可以去除pop
//				pw.setOutsideTouchable(true);
//				pw.setBackgroundDrawable(new BitmapDrawable());

                //出现在布局底端
//				pw.showAtLocation(Rl_all, 0, 0,  Gravity.END);
                ColorDrawable dw = new ColorDrawable(android.R.color.white);
                pw.setBackgroundDrawable(dw);
                pw.setOutsideTouchable(false);// 这是点击外部不消失
                // 设置popWindow的显示和消失动画
                pw.setAnimationStyle(R.style.mypopwindow_anim_style);
                pw.showAtLocation(ReleaseActivity.this.findViewById(R.id.fanshi), Gravity.END, 1, 1);

                //点击确定
                tv_ok.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View arg0) {


                        int curDay = currentDay;
                        int curHour = currentHour;
                        int curMinute = currentMinute - 5;
                        if (curMinute > 59) {
                            curMinute -= 60;
                            curHour += 1;
                        }
                        if (curHour >= 23) {
                            curHour -= 23;
                            if ((curDay += 1) <= maxDayOfMonth) {
                                curDay += 1;//这里最好再判断一下本月的最大天数，判断是否比较月份
                            } else {
                                curDay = ((curDay += 1) - maxDayOfMonth);
                            }

                        }

//	                  	   Toast.makeText(getApplicationContext(), selectMinute + " " + curMinute + "a", Toast.LENGTH_LONG).show();
                        if (selectDay == curDay) {    //在当前日期情况下可能出现选中过去时间的情况
                            if (selectHour < curHour) {
                                Toast.makeText(getApplicationContext(), "不能选择过去的时间\n请重新选择", Toast.LENGTH_LONG).show();
//	                                Toast.makeText(getApplicationContext(), selectHour +"  s "+ curHour, Toast.LENGTH_LONG).show();
                            } else if ((selectHour == curHour) && (selectMinute < curMinute)) {

//	                                Toast.makeText(getApplicationContext(), selectHour +"  s1 "+ curMinute, Toast.LENGTH_LONG).show();
                                Toast.makeText(getApplicationContext(), "不能选择过去的时间\n 请重新选择", Toast.LENGTH_LONG).show();

                            }
//	                            else if ((selectHour > curHour) && (selectMinute < curMinute)) {
//	                                Toast.makeText(getApplicationContext(), "不能选择过去的时间\n  请重新选择", Toast.LENGTH_LONG).show();
//	                           	
//	                            } 
                            else {
//	                            	if (selectHour>23) {
////	  	                      		  Toast.makeText(getApplicationContext(), "不能选择过去的时间\n        请重选择日期", Toast.LENGTH_LONG).show();
//	  	                      	     	selectHour=0;
//	  	                      		  selectTime = selectHour + ":" + ((selectMinute < 10) ? ("0" + selectMinute) : selectMinute);//zerotime
//	  	                      		  selectDay+=1;
//	  	                      		  if (selectDay>30) {
//	  	                      			if (!dadada) {
//	  	                      				 Toast.makeText(getApplicationContext(), "获取月份日期失败！请重选择月份日期", Toast.LENGTH_LONG).show();
//	  	                      				pw.dismiss();
//	  	                      				 return;
//	  	                      			
//	  									}
//	  								}
//	  	                      		 if(calendar.get(Calendar.DAY_OF_MONTH)<10 && (calendar.get(Calendar.MONTH) + 1)<10){
//	  	           					  selectDate = calendar.get(Calendar.YEAR) + "-" +"0"+(calendar.get(Calendar.MONTH) + 1) + "-"
//	  	           				                +"0"+selectDay;
//	  	           					}else  if ((calendar.get(Calendar.DAY_OF_MONTH)<10)) {
//	  	           						  selectDate = calendar.get(Calendar.YEAR) + "-"  +(calendar.get(Calendar.MONTH) + 1) + "-"
//	  	           								  +"0"+selectDay;
//	  	           					}else if ((calendar.get(Calendar.MONTH) + 1)<10) {
//	  	           						selectDate = calendar.get(Calendar.YEAR) + "-" +"0"+ (calendar.get(Calendar.MONTH) + 1) + "-"
//	  	           				                +selectDay;
//	  	           					}else {
//	  	           						  selectDate = calendar.get(Calendar.YEAR) + "-" + (calendar.get(Calendar.MONTH) + 1) + "-"
//	  	           					                +selectDay;
//	  	           					}
                                DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                                try {
                                    Date date = new Date();
                                    DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                                    String time = format.format(date);
                                    Date d1 = df.parse(selectDate + " " + selectTime);
                                    Date d2 = df.parse(time);
                                    long diff = d1.getTime() - d2.getTime();//这样得到的差值是微秒级别
                                    long days = diff / (1000 * 60 * 60 * 24);
                                    long hours = (diff - days * (1000 * 60 * 60 * 24)) / (1000 * 60 * 60);
                                    long minutes = (diff - days * (1000 * 60 * 60 * 24) - hours * (1000 * 60 * 60)) / (1000 * 60);
//	                        		  System.out.println(""+days+"天"+hours+"小时"+minutes+"分");
                                    if (hours <= 0) {
                                        ToastUtil.shortToast(ReleaseActivity.this, "不能选择过去的时间\n请重新选择。");
                                        et_time.setText("");
//	                            	 return;
                                    } else {
//	                                	  et_time.setText(selectDate+" "+selectTime);
                                        if (selectDay <= maxDayOfMonth && selectHour < 24) {
                                            et_time.setText(selectDate + " " + selectTime);
                                            timether = "1";
                                        } else {

                                            Toast.makeText(getApplicationContext(), "获取月份日期失败！请重选择月份日期", Toast.LENGTH_LONG).show();
                                        }
                                    }
                                } catch (Exception e) {
                                }


                                pw.dismiss();
//						}
                            }
                        } else {
                            if (selectHour > 23) {
//	                      		  Toast.makeText(getApplicationContext(), "不能选择过去的时间\n        请重选择日期", Toast.LENGTH_LONG).show();
                                selectHour = 0;
                                selectTime = selectHour + ":" + ((selectMinute < 10) ? ("0" + selectMinute) : selectMinute);//zerotime

                                if (selectDay <= maxDayOfMonth) {
                                    selectDay += 1;
                                } else {
                                    selectDay = (selectDay - maxDayOfMonth);
                                }
//	     	                       Toast.makeText(getApplicationContext(), selectDay+"请重选择月份日期.", Toast.LENGTH_LONG).show();
                                if (selectDay > maxDayOfMonth) {
                                    if (!dadada) {
                                        Toast.makeText(getApplicationContext(), "获取月份日期失败！请重选择月份日期.", Toast.LENGTH_LONG).show();
                                        pw.dismiss();
                                        return;
                                    }
                                }
                                if (calendar.get(Calendar.DAY_OF_MONTH) < 10 && (calendar.get(Calendar.MONTH) + 1) < 10) {
                                    selectDate = calendar.get(Calendar.YEAR) + "-" + "0" + (calendar.get(Calendar.MONTH) + 1) + "-"
                                            + "0" + selectDay;
                                } else if ((calendar.get(Calendar.DAY_OF_MONTH) < 10)) {
                                    selectDate = calendar.get(Calendar.YEAR) + "-" + (calendar.get(Calendar.MONTH) + 1) + "-"
                                            + "0" + selectDay;
                                } else if ((calendar.get(Calendar.MONTH) + 1) < 10) {
                                    selectDate = calendar.get(Calendar.YEAR) + "-" + "0" + (calendar.get(Calendar.MONTH) + 1) + "-"
                                            + selectDay;
                                } else {
                                    selectDate = calendar.get(Calendar.YEAR) + "-" + (calendar.get(Calendar.MONTH) + 1) + "-"
                                            + selectDay;
                                }
//	                      		et_time.setText(selectDate +" "+ selectTime);
                                DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                                try {
                                    Date date = new Date();
                                    DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                                    String time = format.format(date);
                                    Date d1 = df.parse(selectDate + " " + selectTime);
                                    Date d2 = df.parse(time);
                                    long diff = d1.getTime() - d2.getTime();//这样得到的差值是微秒级别
                                    long days = diff / (1000 * 60 * 60 * 24);
                                    long hours = (diff - days * (1000 * 60 * 60 * 24)) / (1000 * 60 * 60);
                                    long minutes = (diff - days * (1000 * 60 * 60 * 24) - hours * (1000 * 60 * 60)) / (1000 * 60);
//                        		  System.out.println(""+days+"天"+hours+"小时"+minutes+"分");
                                    if (hours <= 0) {
                                        ToastUtil.shortToast(ReleaseActivity.this, "不能选择过去的时间，\n请重新选择");
//                            	 return;
                                        et_time.setText("");
                                    } else {
                                        if (selectDay <= maxDayOfMonth && selectHour < 24) {
                                            et_time.setText(selectDate + " " + selectTime);
                                            timether = "1";
                                        } else {
                                            Toast.makeText(getApplicationContext(), "获取月份日期失败！请重选择月份日期。", Toast.LENGTH_LONG).show();
                                        }

                                    }
                                } catch (Exception e) {
                                }
                            } else {
//									et_time.setText(selectDate +" "+ selectTime);
                                DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                                try {
                                    Date date = new Date();
                                    DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                                    String time = format.format(date);

                                    Date d1 = df.parse(selectDate + " " + selectTime);
                                    Date d2 = df.parse(time);
                                    long diff = d1.getTime() - d2.getTime();//这样得到的差值是微秒级别
                                    long days = diff / (1000 * 60 * 60 * 24);
                                    long hours = (diff - days * (1000 * 60 * 60 * 24)) / (1000 * 60 * 60);
                                    long minutes = (diff - days * (1000 * 60 * 60 * 24) - hours * (1000 * 60 * 60)) / (1000 * 60);
//	                        		  System.out.println(""+days+"天"+hours+"小时"+minutes+"分");
                                    if (hours <= 0) {
                                        ToastUtil.shortToast(ReleaseActivity.this, "不能选择过去的时间\n 请重新选择!");
//	                            	 return;
                                        et_time.setText("");
                                    } else {
//	                                	  et_time.setText(selectDate+" "+selectTime);

                                        if (selectDay <= maxDayOfMonth && selectHour < 24) {
                                            et_time.setText(selectDate + " " + selectTime);
                                            timether = "1";
                                        } else {
                                            Toast.makeText(getApplicationContext(), "获取月份日期失败，请重选择月份日期！", Toast.LENGTH_LONG).show();
                                        }
                                    }
                                } catch (Exception e) {
                                }
                            }
//							et_time.setText(selectDate+" "+selectTime);
                            pw.dismiss();
                        }
                    }
                });

                //点击取消
                tv_cancel.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View arg0) {
                        pw.dismiss();
                    }
                });
            }
        });
//		sa.setVisibility(View.GONE);

//		ToastUtil.shortToast(ReleaseActivity.this, 	getIntent().getStringExtra("status"));
//		Log.e("111", "11");
        List<String> data_list;
        data_list = new ArrayList<String>();
        if (!"sss".equals(getIntent().getStringExtra("status"))) {
            if (getIntent().getStringExtra("matWeight").equals("5")) {
                data_list.add("≤5公斤");
            } else {
                data_list.add(getIntent().getStringExtra("matWeight") + " 公斤 ");
            }
        } else {
            data_list.add("≤5公斤");
        }

        for (int i = 6; i <= 10000; i++) {
            data_list.add(i + "公斤");
        }
        final String[] myItems = getResources().getStringArray(
                R.array.zhong);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(ReleaseActivity.this,
                android.R.layout.simple_spinner_item, data_list);
        adapter.setDropDownViewResource(R.layout.drop_down_item);
//		if (!"".equals(getIntent().getStringExtra("status"))) {
//			zhongc.setText(getIntent().getStringExtra("matWeight")+"cc");
//			et_weightpner.setVisibility(View.INVISIBLE);
//				}else {
//					et_weightpner.setVisibility(View.VISIBLE);
//					zhongc.setVisibility(View.GONE);
//				}
        et_weightpner.setAdapter(adapter);
        et_weightpner.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                // TODO Auto-generated method stub
                et_weightpner.setVisibility(View.VISIBLE);
                zhongc.setVisibility(View.GONE);
                submit.setVisibility(View.VISIBLE);
                sa.setVisibility(View.GONE);
                te_juli.setVisibility(View.GONE);
                fanshi.setVisibility(View.GONE);
//				submit.setVisibility(View.VISIBLE);
//					if (!"".equals(getIntent().getStringExtra("status"))) {
//						if (position-1<0) {
//							position=0;
//						}
//						kes=position-1;
//					}else {
                kes = position;
//					}
//				ToastUtil.shortToast(ReleaseActivity.this,"position"+sunmit(5, position));
                submit.setVisibility(View.VISIBLE);

                if (position == 0) {
                    if (!"sss".equals(getIntent().getStringExtra("status"))) {
                        change2 = true;
//							ToastUtil.shortToast(ReleaseActivity.this, "122"+change4);
                    }
                } else {
                    change2 = false;
                }
//				sa.setVisibility(View.GONE);
//				ToastUtil.shortToast(ReleaseActivity.this, "change2 "+change2);
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub

            }
        });
        List<String> data_list1;
        data_list1 = new ArrayList<String>();
        if (!"sss".equals(getIntent().getStringExtra("status"))) {
            data_list1.add("" + getIntent().getStringExtra("cargoSize") + "件");
        }
//		data_list1.add("5公斤及以下");
        for (int i = 1; i <= 1000; i++) {
            data_list1.add(i + "件");
        }

        final String[] myItems1 = getResources().getStringArray(
                R.array.jianshu);
        ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(ReleaseActivity.this,
                android.R.layout.simple_spinner_item, data_list1);
        adapter1.setDropDownViewResource(R.layout.drop_down_item);
        et_jian.setAdapter(adapter1);
        et_jian.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                // TODO Auto-generated method stub
                submit.setVisibility(View.VISIBLE);
                sa.setVisibility(View.GONE);
                te_juli.setVisibility(View.GONE);
                fanshi.setVisibility(View.GONE);
//				submit.setVisibility(View.VISIBLE);
                kesse = position;
                submit.setVisibility(View.VISIBLE);
                if (position == 0) {
                    if (!"sss".equals(getIntent().getStringExtra("status"))) {
                        change3 = true;
//						ToastUtil.shortToast(ReleaseActivity.this, "122"+change4);
                    }
                } else {
                    change3 = false;
                }
//					ToastUtil.shortToast(ReleaseActivity.this, "1223"+change4);

//				sa.setVisibility(View.GONE);
//				Log.e("11111", "position"+sunmit(5, position));
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub

            }
        });


        // 时间选择器
        pvTime = new TimePickerView(ReleaseActivity.this, TimePickerView.Type.MONTH_DAY_HOUR_MIN);
        // 控制时间范围
//		 Calendar calendar = Calendar.getInstance();
//		 pvTime.setRange(calendar.get(Calendar.YEAR) - 20,
//		 calendar.get(Calendar.YEAR));
        pvTime.setTime(new Date());
        pvTime.setCyclic(false);
        pvTime.setCancelable(true);
        // 时间选择后回调
        pvTime.setOnTimeSelectListener(new TimePickerView.OnTimeSelectListener() {

            @Override
            public void onTimeSelect(Date date) {
                submit.setVisibility(View.VISIBLE);
                Date now = new Date();
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");//日期格式
                String hehe = dateFormat.format(now);
                SimpleDateFormat dfs = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                Date begin = null;
                Date end = null;
                try {
                    begin = dfs.parse(hehe);
                    end = dfs.parse(getTimes(date));
                } catch (ParseException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
//				Log.e("111shijian", ""+getTime(date));
                long between = (end.getTime() - begin.getTime()) / 1000;//除以1000是为了转换成秒
                submit.setVisibility(View.VISIBLE);
                sa.setVisibility(View.GONE);
                te_juli.setVisibility(View.GONE);
                fanshi.setVisibility(View.GONE);
//				  if (between>1800) {
//					  et_time.setText(getTimes(date));
//				     }else {
//					ToastUtil.shortToast(ReleaseActivity.this, "请选择30分钟以后的时间！");
//				}
//				
            }
        });
//		et_gao.setOnClickListener(new OnClickListener() {
//			
//			@Override
//			public void onClick(View arg0) {
//				// TODO Auto-generated method stub
//				submit.setVisibility(View.VISIBLE);
////				sa.setVisibility(View.GONE);
//				sa.setVisibility(View.GONE);
//				te_juli.setVisibility(View.GONE);
//				fanshi.setVisibility(View.GONE);
//			}
//		});
//et_kuan.setOnClickListener(new OnClickListener() {
//			
//			@Override
//			public void onClick(View arg0) {
//				// TODO Auto-generated method stub
//				submit.setVisibility(View.VISIBLE);
////				sa.setVisibility(View.GONE);
//				sa.setVisibility(View.GONE);
//				te_juli.setVisibility(View.GONE);
//				fanshi.setVisibility(View.GONE);
//				
//			}
//		});
//et_chang.setOnClickListener(new OnClickListener() {
//	
//	@Override
//	public void onClick(View arg0) {
//		// TODO Auto-generated method stub
//		submit.setVisibility(View.VISIBLE);
//		sa.setVisibility(View.GONE);
//		te_juli.setVisibility(View.GONE);
//		fanshi.setVisibility(View.GONE);
////		sa.setVisibility(View.GONE);
//	}
//});

    }

    //listeners
    DatePicker.OnChangeListener dp_onchanghelistener = new DatePicker.OnChangeListener() {
        @Override
        public void onChange(int year, int month, int day, int day_of_week) {
            selectDay = day;
            dadada = true;
//				selectDate = year + "年" + month + "月" + day + "日" + DatePicker.getDayOfWeekCN(day_of_week);
//				selectDate = year + "-" + month + "-" + day + " " ;//+ DatePicker.getDayOfWeekCN(day_of_week)
            if (day < 10 && month < 10) {
                selectDate = year + "-" + "0" + month + "-" + "0" + day + " ";
            } else if (day < 10) {
                selectDate = year + "-" + month + "-" + "0" + day + " ";
            } else if (month < 10) {
                selectDate = year + "-" + "0" + month + "-" + day + " ";
            } else {
                selectDate = year + "-" + month + "-" + day + " ";
            }
        }
    };
    TimePicker.OnChangeListener tp_onchanghelistener = new TimePicker.OnChangeListener() {
        @Override
        public void onChange(int hour, int minute) {
//				selectTime = hour + ":" + ((minute < 10)?("0"+minute):minute) + "";
//				selectHour = hour;
//			    selectMinute = minute + 5;
//				if (currentMinute> 0 && currentMinute< 10) {
//	        		if (minute<35) {
//	        			Toast.makeText(getApplicationContext(), "不能选择过去的时间", Toast.LENGTH_LONG).show();
//	        			tv_ok.setVisibility(View.GONE);
//					}else {
//						tv_ok.setVisibility(View.VISIBLE);
//					}
//				}
////	        		else if (currentMinute> 10 && currentMinute< 20) {
////					if (minute<45) {
////	        			Toast.makeText(getApplicationContext(), "不能选择过去的时间", Toast.LENGTH_LONG).show();
////	        			tv_ok.setVisibility(View.GONE);
////					}else {
////						tv_ok.setVisibility(View.VISIBLE);
////					}
////				}else if (currentMinute> 20 && currentMinute< 40) {
////					if (minute<45) {
////	        			Toast.makeText(getApplicationContext(), "不能选择过去的时间", Toast.LENGTH_LONG).show();
////	        			tv_ok.setVisibility(View.GONE);
////					}else {
////						tv_ok.setVisibility(View.VISIBLE);
////					}
////				}
//				else if (currentMinute> 40 &&  currentMinute< 59) {
//					if (minute<25) {
//						tv_ok.setVisibility(View.GONE);
//	        			Toast.makeText(getApplicationContext(), "不能选择过去的时间", Toast.LENGTH_LONG).show();
//					}else {
//						tv_ok.setVisibility(View.VISIBLE);
//					}
//				}else {
//					tv_ok.setVisibility(View.VISIBLE);

            selectTime = hour + ":" + ((minute < 10) ? ("0" + minute) : minute) + "";
            selectHour = hour;
            selectMinute = minute;
//				}
            timeb = false;
        }
    };

    public void Cartype() {

        AsyncHttpUtils.doSimGet(MCUrl.carType, new AsyncHttpResponseHandler() {
            @Override
            public void onFailure(int arg0, Header[] arg1, byte[] arg2, Throwable arg3) {

            }

            @Override
            public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
                Log.e("11111111", new String(arg2));
                carbeans = new Gson().fromJson(new String(arg2), Beancar.class);

                indse();
            }
        });

    }

    public void indse() {
        List<Object> data_list11;
        data_list11 = new ArrayList<Object>();
        if (!"sss".equals(getIntent().getStringExtra("status"))) {
//			if ("".equals(getIntent().getStringExtra("cargoVolume"))) {
            data_list11.add("" + getIntent().getStringExtra("matVolume"));
//			}

        }
//		data_list1.add("5公斤及以下");
//		Log.e("1111111size   ",carbeans.data.size()+"");
        for (int i = 0; i < carbeans.getData().size(); i++) {
            data_list11.add(carbeans.data.get(i).carName);
        }
//		Log.e("1111111sdata_list   ",data_list11.size()+"");
//		ToastUtil.shortToast(ReleaseActivity.this, data_list11.size()+"");
        final String[] myItems11 = getResources().getStringArray(
                R.array.jianshu);
        ArrayAdapter<Object> adapter11 = new ArrayAdapter<Object>(ReleaseActivity.this,
                android.R.layout.simple_spinner_item, data_list11);
        adapter11.setDropDownViewResource(R.layout.drop_down_item);
        et_car.setAdapter(adapter11);
        et_car.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                // TODO Auto-generated method stub
                submit.setVisibility(View.VISIBLE);
                sa.setVisibility(View.GONE);
                te_juli.setVisibility(View.GONE);
                fanshi.setVisibility(View.GONE);
                cars = position;
                if (position == 0) {
                    if (!"sss".equals(getIntent().getStringExtra("status"))) {
                        change4 = true;
//					ToastUtil.shortToast(ReleaseActivity.this, "122"+change4);
                    }
                } else {
                    change4 = false;
//				ToastUtil.shortToast(ReleaseActivity.this, "1223"+change4);
                }

//				sa.setVisibility(View.GONE);
//				Log.e("11111", "parent"+parent +"  view "+view+" position"+position+"  id"+id);
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub
                Log.e("11111", "arg0  " + arg0);
            }
        });
    }

    @Override
    public void getData() {
        // TODO Auto-generated method stub

    }

    @OnClick({R.id.et_add_address, R.id.et_add_address_specific, R.id.tv_modifyRecieverAddress, R.id.sa, R.id.re_shunfeng,
            R.id.btn_xianshi, R.id.re_wuliu, R.id.zhiyouwuliu})
    public void MyOnClick(View view) {
        switch (view.getId()) {

            case R.id.et_add_address://地址
//		startActivity(new Intent(getActivity(), H5ModelActivity.class));
//		startActivityForResult(new Intent(ReleaseActivity.this, LocationDemo.class), 8);
                submit.setVisibility(View.VISIBLE);
                sa.setVisibility(View.GONE);
                te_juli.setVisibility(View.GONE);
                fanshi.setVisibility(View.GONE);
//			change1=false;
//		break;
            case R.id.tv_modifyRecieverAddress://位置
//		startActivity(new Intent(getActivity(), H5ModelActivity.class));
                startActivityForResult(new Intent(ReleaseActivity.this, LocationDemo.class), 8);
                submit.setVisibility(View.VISIBLE);
                sa.setVisibility(View.GONE);
                te_juli.setVisibility(View.GONE);
                fanshi.setVisibility(View.GONE);

                break;
            case R.id.re_shunfeng://发布顺风
//		startActivity(new Intent(ReleaseActivity.this, LogisewindActivity.class));

                break;
            case R.id.btn_xianshi://发布限时
//		startActivity(new Intent(ReleaseActivity.this, LogisewindtimlitActivity.class));
                break;
            case R.id.re_wuliu://发布物流
//		startActivity(new Intent(ReleaseActivity.this, LoginseticsActivity.class));
                break;
            case R.id.zhiyouwuliu://只有物流
                break;
            default:
                break;
        }
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case 8:
                if (data == null) {
                    return;
                }
                if (resultCode == RESULT_OK) {
                    receiver_latitude = data.getDoubleExtra("latitude", 0);
                    receiver_longitude = data.getDoubleExtra("longitude", 0);
                    city = data.getStringExtra("city");
                    receive = true;
                    getCityCode(true);
                    et_add_address.setText(data.getStringExtra("address").replace("中国", ""));
                    change1 = false;
                }
                break;
            case 15:
                if (data == null) {
                    return;
                }
                if (resultCode == RESULT_OK) {
                    fromLatitude = data.getDoubleExtra("latitude", 0);
                    fromLongitude = data.getDoubleExtra("longitude", 0);
                    fromcity = data.getStringExtra("city");
                    fromreceive = true;
                    getCityCodefrom(true);
                    weather();
                    et_address.setText(data.getStringExtra("address").replace("中国", ""));
                    fromchange1 = false;
                }
                break;
            case 10:
//				finish();
//				if ( data.getStringExtra("type").equals("1")) {
//					finish();
//				}
                break;
            case 11:
//				finish();
//				if ( data.getStringExtra("type").equals("1")) {
//					finish();
//				}
                break;
            case 12:
//				finish();
//				if ( data.getStringExtra("type").equals("1")) {
//					finish();
//				}
                break;
            default:
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    ;

    private void getCityCodefrom(boolean dingwei) {
        boolean isCopySuccess = CheckDbUtils.checkDb();
        // 成功的将数据库copy到data 中
        if (isCopySuccess) {
            iWantApplication.getInstance().mDbManager = new DbManager(iWantApplication.getInstance());
        }
        if (fromcity == null || fromcity.equals("")) {
//			ToastUtil.shortToast(getActivity(), "请输入完整信息");
            return;
        }
        if (!fromcity.contains("市")) {
            fromcity = fromcity + "市";
        }
        List<CityBean> selectDataFromDb = new CityDbOperation()
                .selectDataFromDb("select * from city where city_name='" + fromcity + "'");
        if (selectDataFromDb.size() > 0 && !selectDataFromDb.equals("")) {
            if (dingwei) {
                if (receive) {
                    fcityCode = selectDataFromDb.get(0).city_code;
                    Log.e("citycode", receiver_citycode);

                } else {
                    ding = true;
                    fcityCode = selectDataFromDb.get(0).city_code;
//			Log.e("citycode", cityCode);
                    List<AreaBean> selectDataFromDbs = new AreaDboperation()
                            .selectDataFromDb("select * from area where area_name='" + townaddressd2 + "'");
                    if (selectDataFromDbs.size() > 0 && !selectDataFromDbs.equals("")) {
                        ftownCode = selectDataFromDbs.get(0).area_code;
//				Log.e("11111townCode", townCode);
                    }
                }
            } else {
                fcityCode = selectDataFromDb.get(0).city_code;
                Log.e("citycode", cityCode);
                List<AreaBean> selectDataFromDbs = new AreaDboperation()
                        .selectDataFromDb("select * from area where area_name='" + townaddressd + "'");
                if (selectDataFromDbs.size() > 0 && !selectDataFromDbs.equals("")) {
                    ftownCode = selectDataFromDbs.get(0).area_code;
                    Log.e("11111townCode", townCode);
                }
            }
        }
//		if (townaddressd.equals("")) {
//			townaddressd = PreferencesUtils.getString(getActivity(), PreferenceConstants.Codedess);
//		}
    }

    private void getCityCode(boolean dingwei) {
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
            if (dingwei) {
                if (receive) {
                    receiver_citycode = selectDataFromDb.get(0).city_code;
                    Log.e("citycode", receiver_citycode);

                } else {
                    ding = true;
                    cityCode2 = selectDataFromDb.get(0).city_code;
//			Log.e("citycode", cityCode);
                    List<AreaBean> selectDataFromDbs = new AreaDboperation()
                            .selectDataFromDb("select * from area where area_name='" + townaddressd2 + "'");
                    if (selectDataFromDbs.size() > 0 && !selectDataFromDbs.equals("")) {
                        townCode2 = selectDataFromDbs.get(0).area_code;
//				Log.e("11111townCode", townCode);
                    }
                }
            } else {
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
//		if (townaddressd.equals("")) {
//			townaddressd = PreferencesUtils.getString(getActivity(), PreferenceConstants.Codedess);
//		}
    }

    //选择时间
    public static String getTimes(Date date) {
        SimpleDateFormat format = new SimpleDateFormat("MM-dd HH:mm");
//		SimpleDateFormat formats = new SimpleDateFormat("yyyy");
        Date now = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy");//日期格式
        String hehe = dateFormat.format(now);
        return hehe + "-" + format.format(date);
//		   return dateFormat.format(now);
    }

    private void weather() {
        RequestParams params = new RequestParams();
//		Log.e("json", UrlMap.getUrl(MCUrl.BALANCE, "id", String
//				.valueOf(PreferencesUtils.getInt(getApplicationContext(),
//						PreferenceConstants.UID))));
        String uu = "http://v.juhe.cn/weather/index";
        AsyncHttpUtils.doGet(UrlMap.getfour(uu, "cityname", fromcity, "dtype", "json", "format", "1", "key", "09c8829c4d25fe3cbd75ddf5ab394a9c"), null, null, params,
                new AsyncHttpResponseHandler() {

                    @Override
                    public void onFailure(int arg0, Header[] arg1, byte[] arg2,
                                          Throwable arg3) {
                    }

                    @Override
                    public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
                        // TODO Auto-generated method stub
                        Log.e("111223json   ", "" + new String(arg2));
                        try {
                            JSONObject feedobj = new JSONObject(new String(arg2));
                            JSONObject feedobj1 = new JSONObject(feedobj.getString("result"));
//							Log.e("1111112  ", feedobj1.getString("today"));
                            JSONObject feedobj12 = new JSONObject(feedobj1.getString("today"));
                            JSONObject feedobj13 = new JSONObject(feedobj12.getString("weather_id"));

                            weatherId = feedobj13.getString("fa");
                            JSONObject feedobjs12 = new JSONObject(feedobj1.getString("sk"));

                            temp = Integer.parseInt(feedobjs12.getString("temp"));
                            Log.e("1111113  ", feedobjs12.getString("temp"));
                        } catch (JSONException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }

//						BaseBean bean = new Gson().fromJson(
//								new String(arg2), GuardBean.class); 
//						if (bean.getErrCode()==0) {
//							
//							
//						}

                    }

                });
    }

    int su = 0;

    /**
     * 发布按钮提交数据
     */
    private void addPostResult() {
        JSONObject obj = new JSONObject();


        if ("".equals(et_time.getText().toString())
                ) {//|| et_weight.getText().toString().equals("")
            ToastUtil.shortToast(ReleaseActivity.this, "时间未选择");
            return;
        }


        if ("".equals(et_add_address.getText().toString()) || "".equals(receiver_citycode)) {
            ToastUtil.shortToast(ReleaseActivity.this, "请重新选择收件地址信息");
            return;
        }
        try {
            obj.put("userId",
                    String.valueOf(PreferencesUtils.getInt(ReleaseActivity.this, PreferenceConstants.UID)));
            obj.put("latitude", latitude);
            obj.put("longitude", longitude);
//			if ("sss".equals(getIntent().getStringExtra("status"))) {
            obj.put("fromLatitude", fromLatitude);
            obj.put("fromLongitude", fromLongitude);
            obj.put("cityCode", fcityCode);
            obj.put("townCode", ftownCode);
            obj.put("address", et_address.getText().toString() + et_address_specific.getText().toString());
//			}else {
//				obj.put("fromLatitude", getIntent().getStringExtra("fromLatitude"));
//				obj.put("fromLongitude", getIntent().getStringExtra("fromLongitude"));
//				obj.put("cityCode", getIntent().getStringExtra("cityCode"));
//				obj.put("townCode", getIntent().getStringExtra("townCode"));
//				obj.put("address", getIntent().getStringExtra("et_address"));
//			}


            obj.put("addressTo", et_add_address.getText().toString() + et_add_address_specific.getText().toString());
//			obj.put("addressTo", et_address.getText().toString());
            obj.put("cityCodeTo", receiver_citycode);
            obj.put("toLatitude", receiver_latitude);
            obj.put("toLongitude", receiver_longitude);
            obj.put("matWeight", sunmit(5, kes));//重量
//			if (et_chang.getText().toString()=="") {
//				obj.put("high", ""+et_chang.getText().toString());
//			}else {

//			}
//			obj.put("high", ""+et_chang.getText().toString());
//			obj.put("wide", ""+et_kuan.getText().toString());
//			obj.put("length", ""+et_gao.getText().toString());
            obj.put("limitTime", et_time.getText().toString());
            obj.put("cargoSize", sunmit(1, kesse));
            obj.put("carType", carbeans.data.get(cars).carType);
            obj.put("matVolume", carbeans.data.get(cars).carName);
            obj.put("weatherId", Integer.parseInt(weatherId));
            obj.put("temp", temp);


            obj.put("publshDeviceId", DataTools.getDeviceId(ReleaseActivity.this));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        su = sunmit(5, kes);
        if (su == 5) {
            ssdde.setVisibility(View.INVISIBLE);
            re_shunfeng.setVisibility(View.VISIBLE);
        } else {
            ssdde.setVisibility(View.INVISIBLE);
            re_shunfeng.setVisibility(View.VISIBLE);
        }
//		ToastUtil.shortToast(ReleaseActivity.this, ""+su);
//		ToastUtil.shortToast(ReleaseActivity.this, getIntent().getStringExtra("weatherId"));
        dialog.show();
        Logger.e("查看数据", obj.toString());
//		ToastUtil.longToast(ReleaseActivity.this, obj.toString());
        AsyncHttpUtils.doPostJson(ReleaseActivity.this, MCUrl.getPriceByType, obj.toString(),
                new AsyncHttpResponseHandler() {

                    @Override
                    public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
                        Logger.e("111111111ssoppo  ", new String(arg2));
                        dialog.dismiss();
                        releaseBean = new Gson().fromJson(new String(arg2), ReleaseBean.class);
                        if (releaseBean.getErrCode() == 0) {
                            su = 0;
                            submit.setVisibility(View.GONE);
//						  Logger.e("1111111oppop", bean.data.toString());
                            text_mshun.setText(releaseBean.data.get(0).dowPrice);
                            text_mxian.setText(releaseBean.data.get(0).limitPrice);
                            text_mwuliu.setText("" + releaseBean.data.get(0).logisPrice);

                            if (releaseBean.data.get(0).showType.equals("1")) {
                                sa.setVisibility(View.VISIBLE);
                                zhiyouwuliu.setVisibility(View.GONE);
                                te_juli.setVisibility(View.VISIBLE);
                                fanshi.setVisibility(View.VISIBLE);
                                fanshi.setText("" + releaseBean.data.get(0).weatherMessage);
                                DecimalFormat df = new DecimalFormat("######0.00");
                                te_juli.setText("运送总距离：" + df.format(releaseBean.data.get(0).distance) + "公里");
                            } else {
                                if (releaseBean.data.get(0).showType.equals("2")) {
                                    zhiyouwuliu.setVisibility(View.VISIBLE);
                                    sa.setVisibility(View.GONE);
                                } else {
                                    sa.setVisibility(View.GONE);
                                    te_juli.setVisibility(View.VISIBLE);
                                    fanshi.setVisibility(View.GONE);
                                    zhiyouwuliu.setVisibility(View.VISIBLE);
                                }

                            }

                        }
                    }

                    @Override
                    public void onFailure(int arg0, Header[] arg1, byte[] arg2, Throwable arg3) {
                        dialog.dismiss();
                    }
                });

    }

    public int sunmit(int a, int b) {
        int sum = a + b;
        return sum;
    }
}
