package com.hex.express.iwant.activities;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.alipay.sdk.app.PayTask;
import com.google.gson.Gson;
import com.hex.express.iwant.R;
import com.hex.express.iwant.alipay.AlipayUtils;
import com.hex.express.iwant.alipay.PayResult;
import com.hex.express.iwant.bean.BaseBean;
import com.hex.express.iwant.bean.DiscountBean;
import com.hex.express.iwant.bean.WechatBean;
import com.hex.express.iwant.constance.CollectionKey;
import com.hex.express.iwant.constance.MCUrl;
import com.hex.express.iwant.constance.MsgConstants;
import com.hex.express.iwant.constance.PreferenceConstants;
import com.hex.express.iwant.http.AsyncHttpUtils;
import com.hex.express.iwant.http.UrlMap;
import com.hex.express.iwant.newsmain.NewMainActivity;
import com.hex.express.iwant.utils.MessageUtils;
import com.hex.express.iwant.utils.PreferencesUtils;
import com.hex.express.iwant.utils.ToastUtil;
import com.hex.express.iwant.views.DialogUtils;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.tencent.mm.sdk.modelpay.PayReq;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 确认发布支付界面
 *
 * @author SCHT-50
 */
public class DownWindPayActivity extends BaseActivity {
    @Bind(R.id.pay_moneys)
    TextView pay_money;
    @Bind(R.id.pay_money_baos)//保险费
            TextView pay_money_bao;
    @Bind(R.id.pay_money_yuns)//运险费
            TextView pay_money_yun;
    @Bind(R.id.down_togg)//选择现金卷开关
            ToggleButton down_togg;
    @Bind(R.id.down_daifu)//选择代付开关
            ToggleButton down_daifu;


    @Bind(R.id.pay_ext)//关闭
            TextView pay_ext;
    @Bind(R.id.pay_wu)//方式支付
            TextView pay_wu;
    @Bind(R.id.pay_distance)//方式支付
            TextView pay_distance;
    @Bind(R.id.fangshi)//方式支付
            TextView fangshi;

    @Bind(R.id.pay_money_daishou)//dai
            TextView pay_money_daishou;
    @Bind(R.id.pay_money_das)//dai
            TextView pay_money_das;

    @Bind(R.id.pay_juan)//现金卷
            TextView pay_juan;
    @Bind(R.id.pay_zhifubao)//支付宝
            ImageView pay_zhifubao;
    @Bind(R.id.pay_wenxin)//微信
            ImageView pay_wenxin;
    @Bind(R.id.pay_surplus)//余额
            ImageView pay_surplus;
    @Bind(R.id.btn_sumit)//余额
            Button btn_sumit;

    @Bind(R.id.bbb)//现金卷
            TextView bbb;

    @Bind(R.id.vise)//现金卷
            View vise;


    @Bind(R.id.pay_daifu)//代付按钮
            ImageView pay_daifu;



    @Bind(R.id.btnLeft)//
            ImageView btnLeft;


    @Bind(R.id.payreout_juan)//
            RelativeLayout payreout_juan;
    @Bind(R.id.re_dai)//
            RelativeLayout re_dai;


    private String billCode;
    private String money;

    private String insureCostString;
    PayReq req;
    private String orderInfo;
    private String result;
    final IWXAPI msgApi = WXAPIFactory.createWXAPI(this, null);
    private MyReceiver reMyrecive;
    private Object needPayMoney;
    private DiscountBean bean;
    private String moneys;
    String way;
    String bundles;
    String uhuid;
    String distance;
    boolean isCheckeds;
    boolean isCheckedse;
    boolean many = false;
    double num1 = 0.0;
    double num2 = 0.0;
    double dis = 0.0;
    int type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_downwindpayse);//activity_downwindpays  activity_downwindpayse
        ButterKnife.bind(DownWindPayActivity.this);
        initData();
        req = new PayReq();
        msgApi.registerApp(CollectionKey.APP_ID);
    }

    @Override
    public void onWeightClick(View v) {
        // TODO Auto-generated method stub

    }

    @Override
    public void initView() {
        // TODO Auto-generated method stub

    }

    @OnClick({R.id.lout_showdown, R.id.lout_show, R.id.pay_surplus, R.id.pay_zhifubao, R.id.pay_wenxin
            , R.id.btn_sumit, R.id.pay_daifu})
    public void MyOnClick(View view) {
        Intent intent = new Intent();
        switch (view.getId()) {
            case R.id.lout_showdown:// 关闭activity
                finish();

                intent.putExtra("type", "1");
                setResult(RESULT_OK, intent);
                break;
            case R.id.lout_show:// 关闭activity
                finish();
                intent.putExtra("type", "1");
                setResult(RESULT_OK, intent);
                break;
            case R.id.pay_surplus://余额支付
                type = 1;
//			if (isCheckeds) {
//				pay_zhifubao.setBackgroundResource(R.drawable.zhifubao_noclick_07);
//				pay_wenxin.setBackgroundResource(R.drawable.weixin_noclick_05);
//				pay_surplus.setBackgroundResource(R.drawable.click_03);
//			}else {
                pay_surplus.setBackgroundResource(R.drawable.play_yuerzhifuon);
                pay_wenxin.setBackgroundResource(R.drawable.play_weixin);
                pay_zhifubao.setBackgroundResource(R.drawable.play_zhifubao);
                pay_daifu.setBackgroundResource(R.drawable.play_daifu);
//			}

                break;
            case R.id.pay_zhifubao://支付宝支付
                type = 2;
//			if (isCheckeds) {
//				pay_zhifubao.setBackgroundResource(R.drawable.zhifubao_noclick_07);
//				pay_wenxin.setBackgroundResource(R.drawable.weixin_noclick_05);
//				pay_surplus.setBackgroundResource(R.drawable.p_yuerzhifu);
//			}else {
                pay_surplus.setBackgroundResource(R.drawable.play_yuerzhifu);
                pay_wenxin.setBackgroundResource(R.drawable.play_weixin);
                pay_zhifubao.setBackgroundResource(R.drawable.play_zhifubaoon);
                pay_daifu.setBackgroundResource(R.drawable.play_daifu);
//			}
                break;
            case R.id.pay_wenxin://微信支付
                type = 3;
//			if (isCheckeds) {
//				pay_zhifubao.setBackgroundResource(R.drawable.zhifubao_noclick_07);
//				pay_wenxin.setBackgroundResource(R.drawable.weixin_noclick_05);
//				pay_surplus.setBackgroundResource(R.drawable.p_yuerzhifu);
//			}else {
                pay_surplus.setBackgroundResource(R.drawable.play_yuerzhifu);
                pay_wenxin.setBackgroundResource(R.drawable.play_weixinon);
                pay_zhifubao.setBackgroundResource(R.drawable.play_zhifubao);
                pay_daifu.setBackgroundResource(R.drawable.play_daifu);
//			}
                break;
            case R.id.pay_daifu://微信支付
                type = 5;
//			if (isCheckeds) {
//				pay_zhifubao.setBackgroundResource(R.drawable.zhifubao_noclick_07);
//				pay_wenxin.setBackgroundResource(R.drawable.weixin_noclick_05);
//				pay_surplus.setBackgroundResource(R.drawable.p_yuerzhifu);
//			}else {
                pay_surplus.setBackgroundResource(R.drawable.play_yuerzhifu);
                pay_wenxin.setBackgroundResource(R.drawable.play_weixin);
                pay_zhifubao.setBackgroundResource(R.drawable.play_zhifubao);
                pay_daifu.setBackgroundResource(R.drawable.play_daifuon);
//			}
                break;
            case R.id.btn_sumit://支付
                getyouhui();


                break;
            default:
                break;
        }
        setResult(RESULT_OK, intent);

    }

    /**
     * 支付宝支付
     */
    public void getAllpay() {
        // if (bean.data.get(0).discount != null) {
        // needPayMoney = Double.parseDouble(money)
        // * Double.parseDouble(bean.data.get(0).discount);
        // }
        if (billCode.equals("") || money.equals("")) {
            ToastUtil.shortToast(getApplicationContext(), "该订单有问题");
            return;
        }
//		ToastUtil.shortToast(DownWindPayActivity.this, pay_money.getText().toString());
//		money= pay_money.getText().toString();
//		double num21 = Double.valueOf(pay_money.getText().toString());
//		DecimalFormat df = new DecimalFormat("###.00");  
//		money=df.format(num21) ;
        money = pay_money.getText().toString();
        orderInfo = AlipayUtils.getOrderInfo("运费线上支付", null + money + "元", money + "",
                "http://www.efamax.com:8888/appservice-1.0-SNAPSHOT/jsp/alipay/receiveNotify.jsp", null, null,
                billCode);
        Log.e("pp", orderInfo);
        sendEmptyBackgroundMessage(MsgConstants.MSG_01);

    }

    /**
     * 微信支付
     */
    public void getWxChat() {
        if (billCode.equals("") || money.equals("")) {
            ToastUtil.shortToast(getApplicationContext(), "该订单有问题");
            return;
        }
        money = pay_money.getText().toString();
        JSONObject obj = new JSONObject();
        try {
            obj.put("billCode", billCode);
            obj.put("matName", null);
            obj.put("matType", null);
            obj.put("insuranceFee", null);
            obj.put("insureMoney", null);
            obj.put("shipMoney", null);
            obj.put("userCouponId", null);
//			obj.put("needPayMoney", "0.01");
            obj.put("needPayMoney", money);
//			obj.put("needPayMoney", pay_money.getText().toString());

            obj.put("weight", null);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.e("obj", obj.toString());
//		ToastUtil.shortToast(DownWindPayActivity.this, pay_money.getText().toString());
        dialog.show();
        AsyncHttpUtils.doPut(getApplicationContext(), MCUrl.EXPRESSPAYWECHAT, obj.toString(), null,
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

    @Override
    public void handleBackgroundMessage(Message msg) {
        super.handleBackgroundMessage(msg);
        switch (msg.what) {
            case MsgConstants.MSG_01:

                PayTask task = new PayTask(this);
                result = task.pay(orderInfo);
                Log.e("mm", result);
                sendEmptyUiMessage(MsgConstants.MSG_01);
                new Timer().schedule(new TimerTask() {
                    public void run() {
                        sendEmptyUiMessage(MsgConstants.MSG_01);

                    }
                }, 1000);
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
                    getQueryResult(billCode);
                } else {
                    // 判断resultStatus 为非“9000”则代表可能支付失败
                    // “8000”代表支付结果因为支付渠道原因或者系统原因还在等待支付结果确认，最终交易是否成功以服务端异步通知为准（小概率状态）
                    if (TextUtils.equals(resultStatus, "8000")) {
                        Toast.makeText(DownWindPayActivity.this, "支付结果确认中", Toast.LENGTH_SHORT).show();

                    } else {
                        // 其他值就可以判断为支付失败，包括用户主动取消支付，或者系统返回的错误
                        Toast.makeText(DownWindPayActivity.this, "支付失败", Toast.LENGTH_SHORT).show();

                    }
                }
                break;

            default:
                break;
        }
    }

    /**
     * 余额支付的接口
     */
    public void getaddsurplus() {
        if (billCode.equals("") || money.equals("")) {
            ToastUtil.shortToast(getApplicationContext(), "该订单有问题");
            return;
        }
        JSONObject obj = new JSONObject();
        try {
            obj.put("billCode", billCode);
            obj.put("matName", null);
            obj.put("matType", null);
            obj.put("userId",
                    String.valueOf(PreferencesUtils.getInt(DownWindPayActivity.this, PreferenceConstants.UID)));
            obj.put("insuranceFee", null);
            obj.put("insureMoney", null);
            obj.put("shipMoney", null);
            obj.put("needPayMoney", money);
            obj.put("weight", null);
            obj.put("userCouponId", uhuid);
            // if (cb_gift.isChecked()) {
            // obj.put("userCouponId", userCouponId);
            // Log.e("ic", userCouponId + "");
            // } else {
            // obj.put("userCouponId", 0);
            // Log.e("ic", "fff");
            // }

        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        Log.e("1111   余额obj", obj.toString());
        dialog.show();
        AsyncHttpUtils.doPut(getApplicationContext(), MCUrl.BALANCE_MONEY, obj.toString(), null,
                new AsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
                        // TODO Auto-generated method
                        // stub
                        dialog.dismiss();
                        if (arg2 == null)
                            return;
                        Log.e("msg", new String(arg2));
                        BaseBean bean = new Gson().fromJson(new String(arg2), BaseBean.class);
                        if (bean.getErrCode() == 0) {
                            ToastUtil.shortToast(DownWindPayActivity.this, bean.getMessage());
//							sendBroadcast(new Intent().setAction("freight").putExtra("flag", "owner"));//启动SendFreightActivity界面	
                            Intent intent = new Intent();
                            intent.putExtra("type", "2");
                            setResult(RESULT_OK, intent);
                            finish();
                        }
                        if (bean.getErrCode() == -2) {
                            DialogUtils.createAlertDialogTwo(DownWindPayActivity.this, "", bean.getMessage(), 0, "去充值",
                                    "更换支付方式", new DialogInterface.OnClickListener() {

                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {

                                            startActivity(new Intent(DownWindPayActivity.this, RechargeActivity.class));
                                        }

                                    }).show();
                        }
                    }

                    @Override
                    public void onFailure(int arg0, Header[] arg1, byte[] arg2, Throwable arg3) {
                        // TODO Auto-generated method
                        // stub
                        dialog.dismiss();
                    }
                });
    }

    @Override
    public void initData() {
        int i, j, h = 0;
        reMyrecive = new MyReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction("recharge");
        registerReceiver(reMyrecive, filter);
        getDiscount();
        if ("".equals(getIntent().getStringExtra("payUserId"))) {

            pay_daifu.setVisibility(View.VISIBLE);
        } else {
            pay_daifu.setVisibility(View.GONE);
        }
        if (!"".equals(getIntent().getStringExtra("tyy"))) {
            fangshi.setText("" + getIntent().getStringExtra("tyy"));
        }
        if (!"".equals(getIntent().getStringExtra("distance"))) {
            dis = Double.valueOf(getIntent().getStringExtra("distance").toString());
//		    DecimalFormat dfd = new DecimalFormat("###.00");  
            pay_distance.setText("(" + dis / 1000 + "km)");
        }
        if (!"".equals(getIntent().getStringExtra("daishou"))) {
            pay_money_daishou.setText("" + getIntent().getStringExtra("daishou") + "元");
            pay_money_das.setVisibility(View.VISIBLE);
        } else {
            pay_money_das.setVisibility(View.GONE);
        }


        String carType = getIntent().getStringExtra("carType");

        if (!"sanlun".equalsIgnoreCase(carType) && !"else".equalsIgnoreCase(carType)) {
            pay_surplus.setVisibility(View.GONE);
        }


//		money = getIntent().getStringExtra("money");
        billCode = getIntent().getStringExtra("billCode");
        if ("0.0".equals(getIntent().getStringExtra("insureCost"))) {
            pay_money_bao.setVisibility(View.GONE);
            bbb.setVisibility(View.GONE);
            vise.setVisibility(View.GONE);
        }
        insureCostString = getIntent().getStringExtra("insureCost");
        try {

            num1 = Double.valueOf(getIntent().getStringExtra("money").toString());
//			num1=	getIntent().getDoubleExtra("money", 0.0);
            num2 = Double.valueOf(insureCostString.toString());
            DecimalFormat df = new DecimalFormat("###.00");
            if (!"".equals(getIntent().getStringExtra("baofei"))) {
                pay_money_bao.setText("" + num2 + " 元" + "(保额" + getIntent().getStringExtra("baofei") + "元)");
            } else {
                pay_money_bao.setText("" + num2 + " 元");
            }

            pay_money_yun.setText("" + df.format(sub(num1, num2)) + " 元");
            if (bundles != null) {
                money = moneys;
            } else {
//				pay_money.setText(df.format(num1) );
//				money=df.format(num1) ;
                pay_money.setText(getIntent().getStringExtra("money").toString());
                money = getIntent().getStringExtra("money").toString();
//				ToastUtil.shortToast(DownWindPayActivity.this, "money3  "+df.format(num1));
//				ToastUtil.shortToast(DownWindPayActivity.this, "money4  "+money);
            }


//			  Log.e("  11111   ",""+ sub(num1,num2));
//			    Log.e("22222   ",""+ num2);
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }

        Log.e("money", money);
        Log.e("insureCostString", insureCostString);
        btnLeft.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                Intent intent = new Intent();
                intent.putExtra("type", "1");
                setResult(RESULT_OK, intent);
                finish();
            }
        });

        down_togg.setOnCheckedChangeListener(new OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // TODO Auto-generated method stub
                if (isChecked) {
                    isCheckeds = isChecked;
                    Intent intent = new Intent(DownWindPayActivity.this, CardActivitys.class);
//						intent.putExtra("type", getIntent().getIntExtra("type", 0));
                    startActivityForResult(intent, 1);
//					ToastUtil.shortToast(DownWindPayActivity.this, "开 "+isChecked);
                    pay_zhifubao.setBackgroundResource(R.drawable.play_zhifubao);
                    pay_wenxin.setBackgroundResource(R.drawable.play_weixin);
                } else {
                    isCheckeds = isChecked;
//					ToastUtil.shortToast(DownWindPayActivity.this, "关 "+isChecked); 
                    pay_zhifubao.setBackgroundResource(R.drawable.play_zhifubao);
                    pay_wenxin.setBackgroundResource(R.drawable.play_weixin);
                    if (!isCheckeds) {
                        num1 = Double.valueOf(getIntent().getStringExtra("money").toString());
//						num1=getIntent().getDoubleExtra("money", 0.0);
                        DecimalFormat df = new DecimalFormat("###.00");
                        pay_money.setText(df.format(num1));
                        payreout_juan.setVisibility(View.GONE);
                        many = false;
                    }
                }
            }
        });
        down_daifu.setOnCheckedChangeListener(new OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // TODO Auto-generated method stub
                if (isChecked) {
                    isCheckedse = isChecked;
//					Intent intent = new Intent(DownWindPayActivity.this, CardActivitys.class)
//						intent.putExtra("type", getIntent().getIntExtra("type", 0));
//						startActivityForResult(intent, 1);
                    Intent intent2 = new Intent();
                    intent2.putExtra("type", "3");
                    setResult(RESULT_OK, intent2);
                    finish();
                } else {
                    isCheckedse = isChecked;
//					ToastUtil.shortToast(DownWindPayActivity.this, "关 "+isChecked); 
//					pay_zhifubao.setBackgroundResource(R.drawable.p_zhifubao);
//					pay_wenxin.setBackgroundResource(R.drawable.p_weixin);
                }
            }
        });
        pay_daifu.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
//				Intent intent2=new Intent();
//				intent2.putExtra("type", "3");
//				setResult(RESULT_OK, intent2);
//				finish();
                shoudairen();
            }
        });
        pay_ext.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                Intent intent = new Intent();
                intent.putExtra("type", "1");
                setResult(RESULT_OK, intent);
                finish();
            }
        });
    }

    /**
     * 接收微信支付成功返回的状态
     *
     * @author
     */
    public class MyReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals("recharge")) {
                getQueryResult(billCode);
            }

        }

    }

    // 获取支付宝支付折扣
    public void getDiscount() {
        AsyncHttpUtils.doGet(UrlMap.getUrl(MCUrl.ALIPAYDISCOUNT, "uid", PreferenceConstants.UID), null, null, null,
                new AsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
                        Log.e("disconut", new String(arg2));
                        bean = new Gson().fromJson(new String(arg2), DiscountBean.class);

                    }

                    @Override
                    public void onFailure(int arg0, Header[] arg1, byte[] arg2, Throwable arg3) {
                        // TODO Auto-generated method stub

                    }
                });
    }

    // 获取支付宝和微信支付的结果
    public void getQueryResult(String billCode) {
        AsyncHttpUtils.doGet(UrlMap.getUrl(MCUrl.MONEYINLOGRESULT, "billCode", billCode), null, null, null,
                new AsyncHttpResponseHandler() {

                    @Override
                    public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
                        Log.e("msdjkg", new String(arg2));
                        BaseBean bean = new Gson().fromJson(new String(arg2), BaseBean.class);
                        if (bean.isSuccess()) {
                            MessageUtils.alertMessageCENTER(getApplicationContext(), "支付成功");
//							sendBroadcast(new Intent().setAction("freight").putExtra("flag", "owner"));//启动SendFreightActivity界面	
                            Intent intent = new Intent();
                            intent.putExtra("type", "2");
                            setResult(RESULT_OK, intent);
                            finish();
                        } else {
                            MessageUtils.alertMessageCENTER(getApplicationContext(), bean.getMessage());
                        }
                    }

                    @Override
                    public void onFailure(int arg0, Header[] arg1, byte[] arg2, Throwable arg3) {
                        MessageUtils.alertMessageCENTER(getApplicationContext(), "支付失败");

                    }
                });
    }

    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
        unregisterReceiver(reMyrecive);
    }

    @Override
    public void setOnClick() {
        // TODO Auto-generated method stub

    }

    @Override
    public void getData() {
        // TODO Auto-generated method stub

    }

    /**
     * * 两个Double数相减 *
     *
     * @param v1 *
     * @param v2 *
     * @return Double
     */
    public static Double sub(Double v1, Double v2) {
        BigDecimal b1 = new BigDecimal(v1.toString());
        BigDecimal b2 = new BigDecimal(v2.toString());
        return new Double(b1.subtract(b2).doubleValue());
    }

    /**
     * 接收选择现金价参数
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);
        Log.e("requestCode  ", "          " + requestCode);
        Log.e("resultCode  ", "          " + resultCode);
        Log.e("11111data  ", "          " + data);
        switch (requestCode) {
            case 1:
                if (requestCode == 1 && data != null) {
                    Bundle bundle = data.getBundleExtra("bundle2");
                    double num3 = 0.0;
                    double num4 = 0.0;
                    DecimalFormat df = new DecimalFormat("###.00");
                    bundle.getString("strResult");
                    Log.e("11111 strResult  ", "          " + bundle.getString("strResult"));
                    Log.e("11111 strResult  ", "          " + bundle.getString("money"));
                    if (bundle.getString("strResult") != null) {
                        uhuid = bundle.getString("strResult");
                    }
                    if (bundle.getString("money") != null) {
                        payreout_juan.setVisibility(View.VISIBLE);
                        pay_juan.setText("" + bundle.getString("money") + " 现金券");
                        bundles = bundle.getString("money");
                        num3 = Double.valueOf(bundles);
                        Log.e(num1 + " " + "11111", "" + num3);
                        String number = df.format(sub(num1, num3));
                        num4 = Double.valueOf(number);
                        double a = 0.0;
//						   double b = 1.0; 
                        BigDecimal data1 = new BigDecimal(a);
                        BigDecimal data2 = new BigDecimal(num4);
                        int result = data1.compareTo(data2);
                        if (result >= 0) {
                            pay_money.setText("0.00");

                            many = true;
                        } else {
                            many = false;
                            pay_money.setText(df.format(sub(num1, num3)));
                        }

                        moneys = pay_money.getText().toString();
                    }
//			 
                }

                break;
        }
    }

    public void getyouhui() {
        if (!isCheckeds) {
            uhuid = "";
        }
//		if (type==5) {
//			Intent intent2=new Intent();
//			intent2.putExtra("type", "3");
//			setResult(RESULT_OK, intent2);
//			finish();
//		}else {


        AsyncHttpUtils.doGet(UrlMap.getTwo(MCUrl.coupon, "recId", "" + getIntent().getIntExtra("recId", 0), "userCouponId", uhuid), null, null, null,
                new AsyncHttpResponseHandler() {

                    @Override
                    public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
                        Log.e("msdjkg", new String(arg2));
                        BaseBean bean = new Gson().fromJson(new String(arg2), BaseBean.class);
                        if (bean.getErrCode() == 0) {

//							Log.e("111", msg);
//							MessageUtils.alertMessageCENTER(getApplicationContext(), bean.getMessage());
                            if (type == 1) {
//								ToastUtil.shortToast(DownWindPayActivity.this, "余额支付");
                                getaddsurplus();
                            } else if (type == 2) {
                                if (many) {
                                    ToastUtil.shortToast(DownWindPayActivity.this, "请用余额支付");
                                } else {
//									ToastUtil.shortToast(DownWindPayActivity.this, "支付包");
                                    getAllpay();
                                }
                            } else if (type == 3) {
                                if (many) {
                                    ToastUtil.shortToast(DownWindPayActivity.this, "请用余额支付");
                                } else {
//									ToastUtil.shortToast(DownWindPayActivity.this, "微信支付");
                                    getWxChat();
                                }
                            }
                        } else {

                            MessageUtils.alertMessageCENTER(getApplicationContext(), bean.getMessage());
                        }
                    }

                    @Override
                    public void onFailure(int arg0, Header[] arg1, byte[] arg2, Throwable arg3) {
                        MessageUtils.alertMessageCENTER(getApplicationContext(), "支付失败");

                    }
                });
//		}
    }

    private void shoudairen() {

        LayoutInflater inflater = LayoutInflater.from(this);
        final View textEntryView = inflater.inflate(
                R.layout.popwidndow_daifu, null);
        final EditText edtInput = (EditText) textEntryView.findViewById(R.id.edtInput);
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(false);
        builder.setTitle("请输入代付人手机号");
        builder.setView(textEntryView);
        builder.setPositiveButton("确认",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
//                        setTitle(edtInput.getText());  
                        getHttpRequst(edtInput.getText().toString());
                    }
                });
        builder.setNegativeButton("取消",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        setTitle("");
                    }
                });
        builder.show();

    }

    /**
     * 获取网络数据
     */
    @SuppressWarnings("unchecked")
    private void getHttpRequst(String phone) {
        if ((phone.length() != 11)) {
            ToastUtil.shortToast(DownWindPayActivity.this, "请输入正确的手机号码");
            return;
        }
        String url = UrlMap.getTwo(MCUrl.replace, "recId", "" + getIntent().getIntExtra("recId", 0),
                "mobile", phone);
//	dialog.show();
        AsyncHttpUtils.doSimGet(url, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
                Log.e("sdds ", new String(arg2));
//			dialog.dismiss();
                BaseBean baseBean = new Gson().fromJson(new String(arg2), BaseBean.class);
                if (baseBean.getErrCode() == 0) {
                    ToastUtil.shortToast(DownWindPayActivity.this, baseBean.getMessage());
                    Intent intent = new Intent();
                    intent.setClass(DownWindPayActivity.this, NewMainActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    ToastUtil.shortToast(DownWindPayActivity.this, baseBean.getMessage());
                }

            }

            @Override
            public void onFailure(int arg0, Header[] arg1, byte[] arg2, Throwable arg3) {
            }
        });
    }

    public boolean dispatchKeyEvent(KeyEvent event) {
        if (event.getKeyCode() == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN
                && event.getRepeatCount() == 0) {
            Intent intent = new Intent();
            intent.putExtra("type", "1");
            setResult(RESULT_OK, intent);
            finish();
        }
        return super.dispatchKeyEvent(event);
    }
}
