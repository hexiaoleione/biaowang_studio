package com.hex.express.iwant.activities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.alipay.sdk.app.PayTask;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.google.gson.Gson;
import com.hex.express.iwant.R;
import com.hex.express.iwant.alipay.AlipayUtils;
import com.hex.express.iwant.alipay.PayResult;
import com.hex.express.iwant.bean.BaseBean;
import com.hex.express.iwant.bean.RechargeBean;
import com.hex.express.iwant.bean.WechatBean;
import com.hex.express.iwant.constance.CollectionKey;
import com.hex.express.iwant.constance.MCUrl;
import com.hex.express.iwant.constance.MsgConstants;
import com.hex.express.iwant.constance.PreferenceConstants;
import com.hex.express.iwant.http.AsyncHttpUtils;
import com.hex.express.iwant.http.UrlMap;
import com.hex.express.iwant.iWantApplication;
import com.hex.express.iwant.utils.MessageUtils;
import com.hex.express.iwant.utils.PreferencesUtils;
import com.hex.express.iwant.utils.ToastUtil;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.tencent.mm.sdk.constants.Build;
import com.tencent.mm.sdk.modelpay.PayReq;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class RechargeActivity extends BaseActivity {
    private String orderInfo;
    private String result;
    @Bind(R.id.tv_detailContent)
    TextView tv_detailContent;

    @Bind(R.id.btnRight)
    TextView btnRight;
    @Bind(R.id.btnLeft)
    ImageView btnLeft;
    @Bind(R.id.money50)
    CheckBox money50;
    @Bind(R.id.money100)
    CheckBox money100;
    @Bind(R.id.money200)
    CheckBox money200;
    @Bind(R.id.money500)
    CheckBox money500;
    //	@Bind(R.id.money500)
//	CheckBox money500;
    @Bind(R.id.money1000)
    CheckBox money1000;
//	@Bind(R.id.recommend)
//	EditText  ;

    @Bind(R.id.checkbox_bs)
    CheckBox checkbox_bs;

    @Bind(R.id.re_te)
    TextView re_te;
    @Bind(R.id.btn_weixin)
    Button btn_weixin;
    @Bind(R.id.btn_alipay)
    Button btn_alipay;
    String text = null;
    ArrayList<CheckBox> mList;
    private String money;
    @Bind(R.id.webview)
    WebView webView;
    PayReq req;
    private RechargeBean bean;
    boolean choxdai = false;
    boolean first = false;
    final IWXAPI msgApi = WXAPIFactory.createWXAPI(this, null);
    private SimpleDateFormat sDateFormat;
    private String trandNum;
    int num = 0;
    private Myrecive reMyrecive;
    private String billCode;
    private String url;// 图片地址；

    private LocationClient client;//百度地图的Client
    private double latitude;
    private double longitude;
    private String 	city,cityCode,townCode,townaddressd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recharge);
        iWantApplication.getInstance().addActivity(this);
        ButterKnife.bind(this);
        initData();
        req = new PayReq();
        msgApi.registerApp(CollectionKey.APP_ID);
        if (PreferencesUtils.getString(getApplicationContext(), PreferenceConstants.AgreementType).equals("1") ||
                PreferencesUtils.getString(getApplicationContext(), PreferenceConstants.AgreementType).equals("2")) {
            ToastUtil.longToast(RechargeActivity.this, "您是海南特约用户不能充值！");
            first = false;
            btn_weixin.setClickable(false);
            btn_alipay.setClickable(false);
        }

        initView();
    }


    public void initView() {

        client = new LocationClient(RechargeActivity.this);
        initLocation();
        client.registerLocationListener(new BDLocationListener() {

            @Override
            public void onReceiveLocation(BDLocation arg0) {
                if (arg0 == null) {
                    ToastUtil.shortToast(RechargeActivity.this, "定位失败，请检查设置");
                    return;
                } else {
//					ToastUtil.shortToast(LoginWeixinActivity.this, "定设置"+ arg0.getLatitude());
                    latitude = arg0.getLatitude();
                    longitude = arg0.getLongitude();
                    city=arg0.getCity();
                    townaddressd=arg0.getDistrict();
                    Log.e("jpppp", latitude + ":::::::::" + longitude);
                    client.stop();
                    updatelocation();
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


    @Override
    public void onWeightClick(View v) {
        // TODO Auto-generated method stub

    }

    // R.id.money1000
    @OnClick({R.id.money50, R.id.money100, R.id.money200, R.id.money500, R.id.money1000, R.id.btn_alipay,
            R.id.btn_weixin, R.id.tv_detailContent})
    public void myClick(View view) {
        switch (view.getId()) {

            case R.id.tv_detailContent:// 现金详情

                startActivity(new Intent(RechargeActivity.this, PreferentialActivity.class));

                break;
            case R.id.money50:
                // text = money30.getText().toString();
//			money50.setChecked(true);
//			money100.setChecked(false);
//			money200.setChecked(false);
//			money500.setChecked(false);
//			money1000.setChecked(false);
//			money50.setTextColor(getResources().getColor(R.color.red));
//			money100.setTextColor(getResources().getColor(R.color.gray));
//			money200.setTextColor(getResources().getColor(R.color.gray));
//			money500.setTextColor(getResources().getColor(R.color.gray));
//			money1000.setTextColor(getResources().getColor(R.color.gray));
                break;
            case R.id.money100:
                money50.setChecked(false);
                money100.setChecked(true);
                money200.setChecked(false);
                money500.setChecked(false);
                money1000.setChecked(false);
                // money1000.setChecked(false);
                // text = money50.getText().toString();
                money100.setTextColor(getResources().getColor(R.color.red));
                money50.setTextColor(getResources().getColor(R.color.gray));
                money200.setTextColor(getResources().getColor(R.color.gray));
                money500.setTextColor(getResources().getColor(R.color.gray));
                money1000.setTextColor(getResources().getColor(R.color.gray));
                // money1000.setTextColor(getResources().getColor(R.color.gray));

                break;
            case R.id.money200:
                money50.setChecked(false);
                money100.setChecked(false);
                money200.setChecked(true);
                money500.setChecked(false);
                money1000.setChecked(false);
                // money1000.setChecked(false);
                // text = money100.getText().toString();
                money200.setTextColor(getResources().getColor(R.color.red));
                money50.setTextColor(getResources().getColor(R.color.gray));
                money100.setTextColor(getResources().getColor(R.color.gray));
                money1000.setTextColor(getResources().getColor(R.color.gray));
                money500.setTextColor(getResources().getColor(R.color.gray));
                // money1000.setTextColor(getResources().getColor(R.color.gray));
                break;
            case R.id.money500:
                money200.setChecked(false);
                money50.setChecked(false);
                money100.setChecked(false);
                money500.setChecked(true);
                money1000.setChecked(false);
                // money1000.setChecked(false);
                // text = money300.getText().toString();
                money500.setTextColor(getResources().getColor(R.color.red));
                money200.setTextColor(getResources().getColor(R.color.gray));
                money50.setTextColor(getResources().getColor(R.color.gray));
                money100.setTextColor(getResources().getColor(R.color.gray));
                money1000.setTextColor(getResources().getColor(R.color.gray));
                // money1000.setTextColor(getResources().getColor(R.color.gray));
                break;
            case R.id.money1000:
                money200.setChecked(false);
                money50.setChecked(false);
                money100.setChecked(false);
                money500.setChecked(false);
                money1000.setChecked(true);
                // money1000.setChecked(false);
                // text = money500.getText().toString();
                money1000.setTextColor(getResources().getColor(R.color.red));
                money200.setTextColor(getResources().getColor(R.color.gray));
                money50.setTextColor(getResources().getColor(R.color.gray));
                money100.setTextColor(getResources().getColor(R.color.gray));
                money500.setTextColor(getResources().getColor(R.color.gray));
                // money1000.setTextColor(getResources().getColor(R.color.gray));
                break;
            // case R.id.money1000:
            // money30.setChecked(false);
            // money50.setChecked(false);
            // money100.setChecked(false);
            // money300.setChecked(false);
            // money500.setChecked(false);
            // money1000.setChecked(true);
            // // text = money1000.getText().toString();
            // money1000.setTextColor(getResources().getColor(R.color.orange1));
            // money30.setTextColor(getResources().getColor(R.color.gray));
            // money50.setTextColor(getResources().getColor(R.color.gray));
            // money100.setTextColor(getResources().getColor(R.color.gray));
            // money300.setTextColor(getResources().getColor(R.color.gray));
            // money500.setTextColor(getResources().getColor(R.color.gray));
            // break;
            // case R.id.edit_other:
            // break;
            case R.id.btn_alipay:// 支付宝支付
                // //statu=AlipayUtils.pay(this, "朕的快递",
                // "从北京来的快递价值0.01元",0.01+"",23+"");
                // /*Log.e("statu",statu+"");
                // if(statu==1){
                // //sendEmptyBackgroundMessage(MsgConstants.MSG_01);
                // }*/

                // 打开GPS
                client.start();

                for (int i = 0; i < mList.size(); i++) {
                    if (mList.get(i).isChecked()) {
                        text = mList.get(i).getText().toString();
                        money = text.substring(0, text.indexOf("元"));
                        if (mList.get(i).equals(money100)) {
                            money100.setChecked(true);
//						ToastUtil.shortToast(RechargeActivity.this, text.substring(0, text.indexOf("元")));
                        }
                    }
                }
                sDateFormat = new SimpleDateFormat("yyyyMMddhhmmss");
                String date = sDateFormat.format(new java.util.Date());
                Log.e("json", date);
                if (choxdai) {
                    if (first) {

                        MessageUtils.alertMessageCENTER(getApplicationContext(), "正在唤醒支付宝支付");
                        btn_alipay.setClickable(false);
                        if (!TextUtils.isEmpty(money)) {
                            Log.e("json", "" + money);
//					}
                            if (getIntent().getStringExtra("way") != null && !getIntent().getStringExtra("way").equals("")) {
                                if (getIntent().getStringExtra("way").equals("2")) {
                                    String useridString = String.valueOf(PreferencesUtils.getInt(getApplicationContext(), PreferenceConstants.UID));
                                    String billCode = getIntent().getStringExtra("billCode") + "D" + useridString;
//                   money String useridString=String.valueOf(PreferencesUtils.getInt(getApplicationContext(),PreferenceConstants.UID));
                                    orderInfo = AlipayUtils.getOrderInfo("物流线上充值支付", null + money + "元", money + "",
                                            "http://www.efamax.com:8888/appservice-1.0-SNAPSHOT/jsp/alipay/receiveNotify.jsp", null, null,
                                            getIntent().getStringExtra("billCode") + "D" + useridString);
                                    trandNum = billCode;
                                }
                            } else {
//						Log.e("11111112zhifubaoyuanx1", "yuanx");
                                orderInfo = AlipayUtils.getOrderInfo("钱包充值", "镖王充值", // 推荐手机号码
                                        money + "", // 充值金额
                                        "http://www.efamax.com:8888/appservice-1.0-SNAPSHOT/jsp/alipay/receiveNotify.jsp",
                                        null, null, PreferencesUtils.getInt(getApplicationContext(), PreferenceConstants.UID)
                                                + "CZ" + date);
                                trandNum = PreferencesUtils.getInt(getApplicationContext(), PreferenceConstants.UID) + "CZ" + date;
                            }

                            Log.e("pp", orderInfo);
                            btn_alipay.setClickable(true);
                        } else {
                            ToastUtil.shortToast(RechargeActivity.this, "请输入金额");
                        }
                        sendEmptyBackgroundMessage(MsgConstants.MSG_01);

                    } else {
                        ToastUtil.shortToast(RechargeActivity.this, "每天只能充值一次");
                    }
                } else {
                    ToastUtil.shortToast(RechargeActivity.this, "请阅读并同意镖王的说明");
                }

                break;
            case R.id.btn_weixin:
                // MessageUtils.alertMessageCENTER(getApplicationContext(),
                // "此功能暂未开放");
                if (choxdai) {

                    // 打开GPS
                    client.start();

                    if (first) {
                        MessageUtils.alertMessageCENTER(getApplicationContext(), "正在唤醒微信支付");
                        if (!checkWeiXinVersion()) {
                            ToastUtil.shortToast(getApplicationContext(), "微信版本不支持，请下载最新微信版本");
                            return;
                        }
                        for (int i = 0; i < mList.size(); i++) {
                            if (mList.get(i).isChecked()) {
                                text = mList.get(i).getText().toString();
                                money = text.substring(0, text.indexOf("元"));
                                if (mList.get(i).equals(money100)) {
                                    money100.setChecked(true);
                                }

                            }
                        }

                        if (!TextUtils.isEmpty(money)) {
                            if (getIntent().getStringExtra("way") != null && !getIntent().getStringExtra("way").equals("")) {

                                if (getIntent().getStringExtra("way").equals("2")) {
//	                    	Log.e("11111111weix", billCode);

                                    String billCode = getIntent().getStringExtra("billCode");
//	                    	ToastUtil.shortToast(RechargeActivity.this, "物流过来的"+billCode);
                                    JSONObject obj = new JSONObject();
                                    try {
                                        obj.put("billCode", billCode);
                                        obj.put("price", money);//money
                                        obj.put("type", "5");
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                    Log.e("obj", obj.toString());

                                    dialog.show();
                                    RequestParams params = new RequestParams();
                                    String useridString = String.valueOf(PreferencesUtils.getInt(getApplicationContext(), PreferenceConstants.UID));
//	                		AsyncHttpUtils.doPostJson(getApplicationContext(), MCUrl.WPRE, obj.toString(), null,
//	                				new AsyncHttpResponseHandler() {
                                    AsyncHttpUtils.doGet(UrlMap.getThreeUrl(MCUrl.WPRE, "billCode", billCode + "D" + useridString,
                                            "price", money, "type", "5"), null, null, params,
                                            new AsyncHttpResponseHandler() {
                                                @Override
                                                public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
                                                    dialog.dismiss();
                                                    if (arg2 == null)
                                                        return;
                                                    WechatBean bean = new Gson().fromJson(new String(arg2), WechatBean.class);
                                                    Log.e("lllllllll}}}}}}]]]", new String(arg2));
                                                    req.appId = bean.data.get(0).appId;
                                                    req.partnerId = bean.data.get(0).partnerId;
                                                    req.prepayId = bean.data.get(0).prepayid;
                                                    req.nonceStr = bean.data.get(0).nonceStr;
                                                    req.packageValue = bean.data.get(0).package_;
                                                    req.sign = bean.data.get(0).sign;
                                                    req.timeStamp = bean.data.get(0).timestamp;
                                                    req.extData = "app data";
                                                    msgApi.registerApp(CollectionKey.APP_ID);
                                                    msgApi.sendReq(req);
                                                }

                                                @Override
                                                public void onFailure(int arg0, Header[] arg1, byte[] arg2, Throwable arg3) {
                                                    dialog.dismiss();
                                                }
                                            });
                                }
                            } else {
                                Log.e("11111112weixyuanx1", "yuanx");
                                JSONObject obj = new JSONObject();
                                try {
                                    obj.put("userId", PreferencesUtils.getInt(mActivity, PreferenceConstants.UID));
                                    obj.put("rechargeMoney", money);
//						obj.put("recommandMobile", null);
                                } catch (Exception e) {
                                    // TODO: handle exception
                                }
                                btn_weixin.setClickable(false);
                                dialog.show();
                                //logistics/task//pay/wechat/pre
                                AsyncHttpUtils.doPostJson(getApplicationContext(), MCUrl.WECHATRECHARGEPRE, obj.toString(),
                                        new AsyncHttpResponseHandler() {

                                            @Override
                                            public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
                                                dialog.dismiss();
                                                if (arg2 == null)
                                                    return;
                                                WechatBean bean = new Gson().fromJson(new String(arg2), WechatBean.class);
                                                Log.e("lllllllll}}}}}}]]]", new String(arg2));
                                                req.appId = bean.data.get(0).appId;
                                                req.partnerId = bean.data.get(0).partnerId;
                                                req.prepayId = bean.data.get(0).prepayid;
                                                req.nonceStr = bean.data.get(0).nonceStr;
                                                req.packageValue = bean.data.get(0).package_;
                                                req.sign = bean.data.get(0).sign;
                                                req.timeStamp = bean.data.get(0).timestamp;
                                                req.extData = "app data";
                                                billCode = bean.data.get(0).billCode;
                                                // SharedPreferences sp =
                                                // getSharedPreferences("wei",
                                                // MODE_PRIVATE);
                                                // SharedPreferences.Editor edit =
                                                // sp.edit();
                                                // edit.putString("billCodetwo",
                                                // billCodetwo);
                                                // edit.commit();
                                                // PreferencesUtils.putString(
                                                // getApplicationContext(),
                                                // "billCodetwo", billCodetwo);
                                                msgApi.registerApp(CollectionKey.APP_ID);
                                                msgApi.sendReq(req);
                                                btn_weixin.setClickable(true);

                                            }

                                            @Override
                                            public void onFailure(int arg0, Header[] arg1, byte[] arg2, Throwable arg3) {
                                                btn_weixin.setClickable(true);
                                                dialog.dismiss();
                                            }
                                        });
                            }
                        }
                    } else {
                        ToastUtil.shortToast(RechargeActivity.this, "每天只能充值一次");
                    }

                } else {
                    ToastUtil.shortToast(RechargeActivity.this, "请阅读并同意镖王的说明");
                }
                break;
            default:
                break;
        }

    }

    public void  updatelocation(){
        JSONObject obj = new JSONObject();

        try {
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
                        if (arg2.length == 0) {
//														ToastUtil.shortToast(getApplicationContext(),
//																"获取位置失败");
                            Log.e("__111111111111111__", "获取位置失败");
                            return;
                        }
                        BaseBean bean = new Gson().fromJson(
                                new String(arg2), BaseBean.class);
                        if (bean.isSuccess()) {
//														ToastUtil.shortToast(getApplicationContext(),
//																bean.getMessage());
                            Log.e("__222222222222___", bean.getMessage());

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

    }

    public class Myrecive extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals("recharge")) {
                getQueryrechargeResult();
            }

        }

    }

    public void getChange() {
        AsyncHttpUtils.doGet(
                UrlMap.getUrl(MCUrl.RACHARGECHANGE, "userId",
                        String.valueOf(PreferencesUtils.getInt(getApplicationContext(), PreferenceConstants.UID))),
                null, null, null, new AsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
                        Log.e("change", new String(arg2));
                        bean = new Gson().fromJson(new String(arg2), RechargeBean.class);
                        if (bean.data != null && !bean.data.isEmpty()) {
                            first = bean.data.get(0).ifAvalid;

                        }
                    }

                    @Override
                    public void onFailure(int arg0, Header[] arg1, byte[] arg2, Throwable arg3) {
                        // TODO Auto-generated method stub

                    }
                });
    }

    // 获取微信充值的结果
    public void getQueryrechargeResult() {
        if (getIntent().getStringExtra("way") != null && !getIntent().getStringExtra("way").equals("")) {
            if (getIntent().getStringExtra("way").equals("2")) {
                String useridString = String
                        .valueOf(PreferencesUtils.getInt(getApplicationContext(),
                                PreferenceConstants.UID));
                String billCodes = getIntent().getStringExtra("billCode");
                AsyncHttpUtils.doGet(UrlMap.getTwo(MCUrl.AndUserId, "userId", String
                                .valueOf(PreferencesUtils.getInt(getApplicationContext(),
                                        PreferenceConstants.UID)), "billCode", billCodes + "D" + useridString), null, null, null,
                        new AsyncHttpResponseHandler() {
                            @Override
                            public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
                                Log.e("msdjkg", new String(arg2));
                                if (!billCode.equals("")) {
                                    MessageUtils.alertMessageCENTER(RechargeActivity.this, "充值成功");
                                    finish();
                                }
                            }

                            @Override
                            public void onFailure(int arg0, Header[] arg1, byte[] arg2, Throwable arg3) {
                                finish();
                            }
                        });
            }
        } else {
            AsyncHttpUtils.doGet(UrlMap.getUrl(MCUrl.MONEYINLOGRESULT, "billCode", billCode), null, null, null,
                    new AsyncHttpResponseHandler() {
                        @Override
                        public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
                            Log.e("msdjkg", new String(arg2));
                            BaseBean bean = new Gson().fromJson(new String(arg2), BaseBean.class);
                            if (bean.getErrCode() == 0) {
                                MessageUtils.alertMessageCENTER(RechargeActivity.this, "充值成功！");
                                finish();
                            } else {
                                MessageUtils.alertMessageCENTER(RechargeActivity.this, "" + bean.getMessage());
                                finish();
                            }
//        						if (!billCode.equals("")) {
//        							MessageUtils.alertMessageCENTER(RechargeActivity.this, "充值成功");
//        							finish();
//        						}

                        }

                        @Override
                        public void onFailure(int arg0, Header[] arg1, byte[] arg2, Throwable arg3) {
                            finish();
                        }
                    });
        }

    }

    /**
     * 检查微信版本是否支持支付
     *
     * @return
     */
    private boolean checkWeiXinVersion() {
        boolean isPaySupported = msgApi.getWXAppSupportAPI() >= Build.PAY_SUPPORTED_SDK_INT;
        return isPaySupported;
    }

    @Override
    public void handleBackgroundMessage(Message msg) {
        super.handleBackgroundMessage(msg);
        switch (msg.what) {
            case MsgConstants.MSG_01:
                PayTask task = new PayTask(this);
                Log.e("mm", "orderInfo----->" + orderInfo);
                result = task.pay(orderInfo);
                Log.e("mm", "result----->" + result);
                sendEmptyUiMessage(MsgConstants.MSG_01);
                break;

            default:
                break;
        }
    }

    @Override
    public void handleUiMessage(Message msg) {
        super.handleUiMessage(msg);
        switch (msg.what) {
            case MsgConstants.MSG_01:
                PayResult payResult = new PayResult(result);
                String resultStatus = payResult.getResultStatus();
                Log.e("su", resultStatus + payResult.getResult());
                if (TextUtils.equals(resultStatus, "9000")) {
                    getQueryResult(trandNum);
                    Toast.makeText(RechargeActivity.this, "支付成功", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    // 判断resultStatus 为非“9000”则代表可能支付失败
                    // “8000”代表支付结果因为支付渠道原因或者系统原因还在等待支付结果确认，最终交易是否成功以服务端异步通知为准（小概率状态）
                    if (TextUtils.equals(resultStatus, "8000")) {
                        Toast.makeText(RechargeActivity.this, "支付结果确认中", Toast.LENGTH_SHORT).show();

                    } else {
                        // 其他值就可以判断为支付失败，包括用户主动取消支付，或者系统返回的错误
                        Toast.makeText(RechargeActivity.this, "支付失败", Toast.LENGTH_SHORT).show();

                    }
                }
                break;

            default:
                break;
        }
    }

    // 获取支付宝和微信支付的结果
    public void getQueryResult(String billCode) {
        String billCodesString;
        if (getIntent().getStringExtra("way") != null && !getIntent().getStringExtra("way").equals("")) {
            if (getIntent().getStringExtra("way").equals("2")) {
                billCodesString = billCode + "D";
                String useridStrings = String.valueOf(PreferencesUtils.getInt(getApplicationContext(), PreferenceConstants.UID));
                String billCodes = getIntent().getStringExtra("billCode") + "D" + useridStrings;
                String useridString = String.valueOf(PreferencesUtils.getInt(getApplicationContext(), PreferenceConstants.UID));
                AsyncHttpUtils.doGet(UrlMap.getTwo(MCUrl.AndUserId, "userId", String
                                .valueOf(PreferencesUtils.getInt(getApplicationContext(),
                                        PreferenceConstants.UID)), "billCode", billCodes), null, null, null,
                        new AsyncHttpResponseHandler() {
                            @Override
                            public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
                                Log.e("msdjkg", new String(arg2));
                                BaseBean bean = new Gson().fromJson(new String(arg2), BaseBean.class);
                                if (bean.isSuccess()) {
                                    getChange();
                                    MessageUtils.alertMessageCENTER(getApplicationContext(), "充值成功");
                                } else {
                                    MessageUtils.alertMessageCENTER(getApplicationContext(), bean.getMessage());
                                    getChange();
                                }
                            }

                            @Override
                            public void onFailure(int arg0, Header[] arg1, byte[] arg2, Throwable arg3) {
                                MessageUtils.alertMessageCENTER(getApplicationContext(), "充值失败");
                                getChange();
                            }
                        });
            } else {
                billCodesString = billCode;
            }
        } else {
            AsyncHttpUtils.doGet(UrlMap.getUrl(MCUrl.MONEYINLOGRESULT, "billCode", billCode), null, null, null,
                    new AsyncHttpResponseHandler() {
                        @Override
                        public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
                            Log.e("msdjkg", new String(arg2));
                            BaseBean bean = new Gson().fromJson(new String(arg2), BaseBean.class);
                            if (bean.isSuccess()) {
                                getChange();
                                MessageUtils.alertMessageCENTER(getApplicationContext(), "充值成功");
                            } else {
                                MessageUtils.alertMessageCENTER(getApplicationContext(), bean.getMessage());
                                getChange();
                            }
                        }

                        @Override
                        public void onFailure(int arg0, Header[] arg1, byte[] arg2, Throwable arg3) {
                            MessageUtils.alertMessageCENTER(getApplicationContext(), "充值失败");
                            getChange();
                        }
                    });
        }

    }

    @Override
    public void initData() {
        reMyrecive = new Myrecive();
        IntentFilter filter = new IntentFilter();
        filter.addAction("recharge");
        registerReceiver(reMyrecive, filter);
        checkbox_bs.setOnCheckedChangeListener(new OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
                // TODO Auto-generated method stub
                if (arg1) {
                    choxdai = arg1;
                } else {
                    choxdai = arg1;
                }
            }
// 			}
        });
        btnLeft.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                finish();
            }
        });
        btnRight.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                Intent intent = new Intent();
                intent.putExtra("url", "http://www.efamax.com/mobile/explain/rechargeExplain.html ");
                intent.setClass(RechargeActivity.this, HAdvertActivity.class);
                startActivity(intent);
            }
        });
//		tvb_show.setTitleText("充值");
//		tvb_show.getRightBtn().setText("说明");
//		tvb_show.getRightBtn().setTextColor(getResources().getColor(R.color.white));
//		tvb_show.setRightBtnOnclickListener(new OnClickListener() {
//			
//			@Override
//			public void onClick(View arg0) {
//				// TODO Auto-generated method stub
//				Intent intent=new Intent();
//				intent.putExtra("url", "http://www.efamax.com/mobile/explain/rechargeExplain.html ");
//				intent.setClass(RechargeActivity.this, HAdvertActivity.class);
//				startActivity(intent);
//			}
//		});
        re_te.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                Intent intent = new Intent();
                intent.putExtra("url", "http://www.efamax.com/mobile/explain/rechargeExplain.html ");
                intent.setClass(RechargeActivity.this, HAdvertActivity.class);
                startActivity(intent);
            }
        });
//		if ("1".equals((PreferencesUtils.getString(getApplicationContext(), PreferenceConstants.USERTYPE)))) {
//
//			tv_detailContent.setVisibility(View.VISIBLE);
////			url = "http://www.efamax.com/mobile_document/discount.html";// 用户的充值界面图片信息
//			url = "http://www.efamax.com/mobile_document/discount_courier.html";
//		} else if ("2".equals((PreferencesUtils.getString(getApplicationContext(), PreferenceConstants.USERTYPE)))) {
//			tv_detailContent.setVisibility(View.INVISIBLE);
//			url = "http://www.efamax.com/mobile_document/discount_courier.html";// 快递员的充值界面图片信息
//		}
        showWebview();
        mList = new ArrayList<CheckBox>();
        mList.add(money100);
        mList.add(money200);
//		mList.add(money50);
        mList.add(money1000);
        mList.add(money500);
        // mList.add(money1000);
        getChange();

    }

    // 现金广告
    private void showWebview() {
//		url = "http://www.efamax.com/mobile_document/discount.html";
        url = "http://www.efamax.com/mobile_document/discount_1.html";
        webView.loadUrl(url);
        webView.setBackgroundColor(2);
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                // TODO Auto-generated method stub
                view.loadUrl(url);
                return true;

            }
        });

    }

    @Override
    public void setOnClick() {
        // TODO Auto-generated method stub

    }

    @Override
    public void getData() {
        // TODO Auto-generated method stub

    }

    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
        unregisterReceiver(reMyrecive);
    }

}
