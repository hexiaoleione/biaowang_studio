package com.hex.express.iwant.activities;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.TextView;

import com.google.gson.Gson;
import com.hex.express.iwant.R;
import com.hex.express.iwant.R.id;
import com.hex.express.iwant.bean.SpreadBean;
import com.hex.express.iwant.constance.MCUrl;
import com.hex.express.iwant.constance.PreferenceConstants;
import com.hex.express.iwant.http.AsyncHttpUtils;
import com.hex.express.iwant.http.UrlMap;
import com.hex.express.iwant.iWantApplication;
import com.hex.express.iwant.utils.PreferencesUtils;
import com.hex.express.iwant.views.TitleBarView;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;

import butterknife.Bind;
import butterknife.ButterKnife;

public class SpreadActivity extends BaseActivity {

    @Bind(id.tbv_show)
    TitleBarView tvb_show;
    @Bind(id.webview)
    WebView webView;
    @Bind(id.tuiren)
    TextView tuiren;
    @Bind(id.tuimoney)
    TextView tuimoney;
    @Bind(id.btntui)
    Button btntui;
    SpreadBean bean;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.spread_activity);
        iWantApplication.getInstance().addActivity(this);
        ButterKnife.bind(this);
        initData();
        initView();
        getrequstBalance();
    }

    @Override
    public void onWeightClick(View v) {
        // TODO Auto-generated method stub

    }

    @Override
    public void initView() {
        // TODO Auto-generated method stub

    }

    @Override
    public void initData() {
        // TODO Auto-generated method stub
        tvb_show.setTitleText("推广收益");
        webView.loadUrl("http://www.efamax.com/mobile_document/extend.html");
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

    /**
     * 获取钱包余额
     */
    private void getrequstBalance() {
        RequestParams params = new RequestParams();
//		Log.e("json", UrlMap.getUrl(MCUrl.BALANCE, "id", String
//				.valueOf(PreferencesUtils.getInt(getApplicationContext(),
//						PreferenceConstants.UID))));
        AsyncHttpUtils.doGet(UrlMap.getUrl(MCUrl.getExtend, "userId", String
                        .valueOf(PreferencesUtils.getInt(getApplicationContext(),
                                PreferenceConstants.UID))), null, null, params,
                new AsyncHttpResponseHandler() {

                    @Override
                    public void onFailure(int arg0, Header[] arg1, byte[] arg2,
                                          Throwable arg3) {
                    }

                    @Override
                    public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
                        // TODO Auto-generated method stub
                        Log.e("json", "" + new String(arg2));
                        bean = new Gson().fromJson(
                                new String(arg2), SpreadBean.class);
                        if (bean.getErrCode() == 0) {
                            tuiren.setText("推广人数：" + bean.getData().get(0).high);
                            tuimoney.setText("推广收益：" + bean.getData().get(0).price + "元");
                            Log.e("11111  ", "  " + bean.getData().get(0).length);
                            btntui.setOnClickListener(new OnClickListener() {
                                @Override
                                public void onClick(View arg0) {
                                    // TODO Auto-generated method stub
                                    Log.e("11111  ", "  " + Integer.parseInt(bean.getData().get(0).length));
                                    int a = Integer.parseInt(bean.getData().get(0).high);
                                    int b = Integer.parseInt(bean.getData().get(0).length);
                                    Log.e("11111  ", "  " + jina(a, b));
                                    if (jina(a, b) > 0) {
                                        Intent intent = new Intent();
                                        // TODO s class not find
//										intent.setClass(SpreadActivity.this, s.class);
//										startActivity(intent);
                                    } else {
                                        showwindow();
                                    }


                                }
                            });
                        }

                    }

                });
    }

    public int jina(int a, int b) {
        int res = a - b;
        return res;
    }

    /**
     */

    public void showwindow() {

        final TextView te;
        Button btn_recceivedOKs;
        ImageView adv_bg;
        final Intent intent = new Intent();
        // 利用layoutInflater获得View
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.popwindow_spea, null);
        // 下面是两种方法得到宽度和高度 getWindow().getDecorView().getWidth()
        final PopupWindow window02 = new PopupWindow(view, WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.MATCH_PARENT);
        // 设置popWindow弹出窗体可点击，这句话必须添加，并且是true
        window02.setFocusable(true);
        // 实例化一个ColorDrawable颜色为半透明
        // ColorDrawable dw = new ColorDrawable(R.color.transparent01);
        ColorDrawable dw = new ColorDrawable(getResources().getColor(android.R.color.white));
        window02.setBackgroundDrawable(dw);
        window02.setOutsideTouchable(false);// 这是点击外部不消失
        // 设置popWindow的显示和消失动画
        window02.setAnimationStyle(R.style.mypopwindow_anim_style);
        // 在底部显示
        window02.showAtLocation(SpreadActivity.this.findViewById(id.btntui), Gravity.CENTER, 0, 0);
        btn_recceivedOKs = (Button) view.findViewById(id.btn_recceivedOKs);
        te = (TextView) view.findViewById(id.te);
        te.setText("" + bean.getData().get(0).matName);
//		relativeLayout1.setim
//		Log.e("11111url", ""+adbean.getData().get(0).advertiseImageUrl);
//		
        btn_recceivedOKs.setOnClickListener(new OnClickListener() {

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
                window02.dismiss();
            }
        });

    }


}
