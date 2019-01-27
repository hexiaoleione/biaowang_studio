package com.hex.express.iwant.activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.hex.express.iwant.R;
import com.hex.express.iwant.bean.DepositBean;
import com.hex.express.iwant.bean.MyAlipayInfoBean;
import com.hex.express.iwant.constance.MCUrl;
import com.hex.express.iwant.constance.PreferenceConstants;
import com.hex.express.iwant.http.AsyncHttpUtils;
import com.hex.express.iwant.http.UrlMap;
import com.hex.express.iwant.iWantApplication;
import com.hex.express.iwant.mypasspay.DepositPassActivity;
import com.hex.express.iwant.utils.PreferencesUtils;
import com.hex.express.iwant.utils.ToastUtil;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;

import java.text.DecimalFormat;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 提现界面
 *
 * @author SCHT-50
 */
public class DepositActivity extends BaseActivity {
    @Bind(R.id.btnLeft)
    ImageView btnLeft;
    @Bind(R.id.btnRight)
    ImageView btnRight;
    @Bind(R.id.text_money)
    TextView money;
    @Bind(R.id.text_waitmoney)
    TextView text_waitmoney;
    @Bind(R.id.edit_aliPayNickName)
    EditText edit_aliPayNickName;
    @Bind(R.id.edit_aliPayAccoount)
    EditText edit_aliPayAccoount;
    @Bind(R.id.edit_money)
    EditText editMoney;
    @Bind(R.id.btn_submit)
    Button btnSubmit;
    public DepositBean bean;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deposit);
        iWantApplication.getInstance().addActivity(this);
        ButterKnife.bind(this);
        initData();
        setOnClick();
        initView();
    }

    @Override
    public void onWeightClick(View v) {
        // TODO Auto-generated method stub

    }

    @Override
    public void initView() {
        // TODO Auto-generated method stub
        editMoney.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                String text = s.toString();
                if (text.contains(".")) {
                    int index = text.indexOf(".");
                    if (index + 3 < text.length()) {
                        text = text.substring(0, index + 3);
                        editMoney.setText(text);
                        editMoney.setSelection(text.length());
                    }
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
                // TODO Auto-generated method stub

            }

            @Override
            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub

            }
        });
    }

    @Override
    public void initData() {
//		tvb_show.setTitleText("支付宝提现");
        getAlipayInfo();
        getrequstBalance();

    }

    /**
     * 获取提现余额
     */
    private void getrequstBalance() {
        Log.e("json", UrlMap.getUrl(MCUrl.WITHDRAWMYLEFT, "userId",
                String.valueOf(PreferencesUtils.getInt(getApplicationContext(), PreferenceConstants.UID))));
        AsyncHttpUtils.doGet(
                UrlMap.getUrl(MCUrl.WITHDRAWMYLEFT, "userId",
                        String.valueOf(PreferencesUtils.getInt(getApplicationContext(), PreferenceConstants.UID))),
                null, null, null, new AsyncHttpResponseHandler() {

                    @Override
                    public void onFailure(int arg0, Header[] arg1, byte[] arg2, Throwable arg3) {

                    }

                    @Override
                    public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
                        // TODO Auto-generated method stub
                        bean = new Gson().fromJson(new String(arg2), DepositBean.class);
                        DecimalFormat df = new DecimalFormat("######0.00");
                        money.setText(String.valueOf(df.format(bean.data.get(0).withdrawableMoney)));
                        text_waitmoney.setText(bean.data.get(0).waitMoney);
                    }

                });
    }

    // /**
    // * 提现申请
    // */
    // private void PostHttpRequst() {
    //
    // JSONObject obj = new JSONObject();
    // try {
    // obj.put("applyMoney",
    // Double.parseDouble(editMoney.getText().toString()));
    // obj.put("aliPayNickName", edit_aliPayNickName.getText().toString());
    // obj.put("aliPayAccount", edit_aliPayAccoount.getText().toString());
    // obj.put("userId", Integer.valueOf(PreferencesUtils.getInt(
    // getApplicationContext(), PreferenceConstants.UID)));
    // } catch (JSONException e) {
    // e.printStackTrace();
    // }
    // AsyncHttpUtils.doPostJson(DepositActivity.this, MCUrl.WITHDRAW,
    // obj.toString(), new AsyncHttpResponseHandler() {
    //
    // @Override
    // public void onFailure(int arg0, Header[] arg1, byte[] arg2,
    // Throwable arg3) {
    // // dialog.dismiss();
    // }
    //
    // @Override
    // public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
    // Log.e("json", new String(arg2));
    // // dialog.dismiss();
    // BaseBean bean = new Gson().fromJson(new String(arg2),
    // BaseBean.class);
    // if (bean.isSuccess()) {
    // AlertDialog.Builder ad = new Builder(DepositActivity.this);
    // ad.setTitle("温馨提示");
    // ad.setMessage("提现成功，我们将会在3-5个工作日内打到您的账户");
    // ad.setPositiveButton("确认", new DialogInterface.OnClickListener() {
    // public void onClick(DialogInterface dialog, int which) {
    // finish();
    // Intent intent = new Intent(DepositActivity.this,
    // MainActivity.class);
    // startActivity(intent);
    //
    // ToastUtil.shortToast(getApplicationContext(), "提现成功");
    // }
    // });
    // ad.create().show();
    //
    // } else {
    // ToastUtil.shortToast(DepositActivity.this,
    // bean.getMessage());
    // }
    //
    //
    // }
    //
    // });
    //
    // }

    // 点击事件:
    @Override
    public void setOnClick() {
        btnSubmit.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!TextUtils.isEmpty(edit_aliPayNickName.getText())
                        && !TextUtils.isEmpty(edit_aliPayAccoount.getText())) {


                    if (!editMoney.getText().toString().equals("0") && !editMoney.getText().toString().equals("")) {

                        double wantMoney = Double.parseDouble(editMoney.getText().toString());
                        double withdrawableMoney = bean.data.get(0).withdrawableMoney;
                        String tips ="提现有手续费（费率见右上角，点问号），您的费用不足，请减少提现金额再试。";
                        if (withdrawableMoney >= wantMoney) {


                            if (withdrawableMoney < 100 && wantMoney > withdrawableMoney * 0.98) {
                                ToastUtil.shortToast(DepositActivity.this, tips);
                                return;
                            } else if (withdrawableMoney >= 100 && withdrawableMoney < 1000 && wantMoney > withdrawableMoney * 0.99) {
                                ToastUtil.shortToast(DepositActivity.this, tips);
                                return;
                            } else if (withdrawableMoney >= 1000 && withdrawableMoney < 2000 && wantMoney > withdrawableMoney * 0.995) {
                                ToastUtil.shortToast(DepositActivity.this, tips);
                                return;
                            } else if (withdrawableMoney >= 2000 && withdrawableMoney < 5000 && wantMoney > withdrawableMoney * 0.997) {
                                ToastUtil.shortToast(DepositActivity.this, tips);
                                return;
                            } else if (withdrawableMoney >= 5000 && wantMoney > withdrawableMoney * 0.998) {
                                ToastUtil.shortToast(DepositActivity.this, tips);
                                return;
                            }
                            // dialog.show();
                            // 跳转到设定支付密码界面或者支付密码:
                            if (PreferencesUtils.getString(getApplicationContext(), PreferenceConstants.PAYPASSWORD) != null) {
                                // 输入支付密码:

                                // 提现操作需要的参数:
                                // obj.put("applyMoney",
                                // Double.parseDouble(editMoney.getText().toString()));
                                // obj.put("aliPayNickName",
                                // edit_aliPayNickName.getText().toString());
                                // obj.put("aliPayAccount",
                                // edit_aliPayAccoount.getText().toString());
                                // obj.put("userId",
                                // Integer.valueOf(PreferencesUtils.getInt(
                                // getApplicationContext(),
                                // PreferenceConstants.UID)));

                                startActivity(new Intent(getApplicationContext(), DepositPassActivity.class)
                                        .putExtra("applyMoney", editMoney.getText().toString())
                                        .putExtra("aliPayNickName", edit_aliPayNickName.getText().toString())
                                        .putExtra("aliPayAccount", edit_aliPayAccoount.getText().toString()));
                            } else {
                                // 跳转到设定支付密码:
                                startActivity(new Intent(getApplicationContext(), SetupPasswordActivity.class));
                            }

                            // PostHttpRequst();//提现申请
                        } else {
                            ToastUtil.shortToast(DepositActivity.this, tips);
                        }

                    } else {

                        ToastUtil.shortToast(DepositActivity.this, "请输入提现金额");
                    }
                } else {
                    ToastUtil.shortToast(DepositActivity.this, "请填全资料信息");
                }

            }
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
                String url = "http://www.efamax.com/mobile/moneyPolicy.html";
                Intent intent = new Intent();
                intent.putExtra("url", url);
                intent.setClass(DepositActivity.this, HAdvertActivity.class);//公司
                startActivity(intent);

            }
        });

    }

    @Override
    public void getData() {
        // TODO Auto-generated method stub

    }

    /**
     * 获取到默认的支付宝账户和昵称等信息:
     */
    private void getAlipayInfo() {
        RequestParams params = new RequestParams();
        Log.e("json", UrlMap.getUrl(MCUrl.ALIPAYINFO, "userId",
                String.valueOf(PreferencesUtils.getInt(getApplicationContext(), PreferenceConstants.UID))));
        AsyncHttpUtils.doGet(
                UrlMap.getUrl(MCUrl.ALIPAYINFO, "userId",
                        String.valueOf(PreferencesUtils.getInt(getApplicationContext(), PreferenceConstants.UID))),
                null, null, params, new AsyncHttpResponseHandler() {

                    @Override
                    public void onFailure(int arg0, Header[] arg1, byte[] arg2, Throwable arg3) {

                    }

                    @Override
                    public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
                        // TODO Auto-generated method stub
                        Log.e("json", new String(arg2));
                        MyAlipayInfoBean bean = new Gson().fromJson(new String(arg2), MyAlipayInfoBean.class);
                        if (!bean.data.equals("") && bean.data.size() > 0) {
                            if (!bean.data.get(0).aliPayNickName.equals("")) {
                                edit_aliPayNickName.setText(bean.data.get(0).aliPayNickName);

                            }
                            if (!bean.data.get(0).equals("")) {
                                edit_aliPayAccoount.setText(bean.data.get(0).aliPayAccount);
                            }
                        } else {
                            ToastUtil.shortToast(DepositActivity.this, bean.getMessage());
                        }
                    }

                });
    }

}
