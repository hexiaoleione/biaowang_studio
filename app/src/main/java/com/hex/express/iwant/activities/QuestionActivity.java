package com.hex.express.iwant.activities;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.ConsoleMessage;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageView;

import com.google.gson.Gson;
import com.hex.express.iwant.R;
import com.hex.express.iwant.bean.BaseBean;
import com.hex.express.iwant.constance.MCUrl;
import com.hex.express.iwant.constance.PreferenceConstants;
import com.hex.express.iwant.http.AsyncHttpUtils;
import com.hex.express.iwant.utils.PreferencesUtils;
import com.hex.express.iwant.utils.ToastUtil;
import com.hex.express.iwant.views.LoadingProgressDialog;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import butterknife.Bind;
import butterknife.ButterKnife;

public class QuestionActivity extends BaseActivity {
    @Bind(R.id.webview)
    WebView webview;
    @Bind(R.id.btn_Left)
    ImageView btn_Left;

    @SuppressLint({"SetJavaScriptEnabled", "JavascriptInterface", "AddJavascriptInterface"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_function);
        ButterKnife.bind(this);
        btn_Left.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                finish();
            }
        });


        WebSettings webSettings = webview.getSettings();
        webSettings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK); //设置缓存
        webSettings.setJavaScriptEnabled(true);//设置能够解析Javascript
        webSettings.setDomStorageEnabled(true);
        webview.addJavascriptInterface(new JsInterface(), "Android");

        webview.setWebChromeClient(new WebChromeClient() {
            @Override
            public boolean onConsoleMessage(ConsoleMessage consoleMessage) {
                Log.i("console", "[" + consoleMessage.messageLevel() + "] " + consoleMessage.message() + "(" + consoleMessage.sourceId() + ":" + consoleMessage.lineNumber() + ")");
                return super.onConsoleMessage(consoleMessage);
            }

            @Override
            public void onConsoleMessage(String message, int lineNumber, String sourceID) {
                Log.d("webview", message + " -- From line " + lineNumber + " of " + sourceID);
            }
        });

        webview.loadUrl("http://www.efamax.com/mobile_document/sanchuan.html");
    }

    private class JsInterface {
        /**
         * js中通过window.Android.methodsName("参数") 可以调用此方法并且把js中input中的值作为参数传入，
         * 但这是在点击js中的按钮得到的，若实现点击java中的按钮得到，需要方法 clickView(View v)
         *
         * @param param
         */
        @JavascriptInterface
        public void methodsName(final String param) {
            Log.i("JsInterface", param);

            final LoadingProgressDialog jsDialog = new LoadingProgressDialog(getApplicationContext());

            JSONObject obj = new JSONObject();
            try {
                obj.put("answerDriverId", String.valueOf(PreferencesUtils.getInt(getApplicationContext(), PreferenceConstants.UID)));
                obj.put("answerScore", "100");
            } catch (JSONException e) {
                e.printStackTrace();
            }

            Log.e("1111111ss", obj.toString());
//            参数示例要求：{"answerDriverId":"97974","answerScore":"100"}
//            保存成功返回: { "errCode": 0, "success": true, "message": "保存成功!"}
//            保存失败返回: { "errCode": -1, " success ": false, "message": "保存失败!"}

            AsyncHttpUtils.doPostJson(QuestionActivity.this, MCUrl.saveAnswerResult, obj.toString(),
                    new AsyncHttpResponseHandler() {
                        @Override
                        public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
                            Log.e("oppo", new String(arg2));
                            jsDialog.dismiss();
                            BaseBean beans = new Gson().fromJson(new String(arg2), BaseBean.class);
                            int errCode = beans.getErrCode();
                            String message = beans.getMessage();

                            if (errCode == 0) {
                                finish();
                            } else if (errCode == -1) {
                                ToastUtil.shortToast(getApplicationContext(), message);
                            }

                        }

                        @Override
                        public void onFailure(int arg0, Header[] arg1, byte[] arg2, Throwable arg3) {
                            jsDialog.dismiss();
                        }
                    });
        }
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

    }

    @Override
    public void setOnClick() {
        // TODO Auto-generated method stub

    }

    @Override
    public void getData() {
        // TODO Auto-generated method stub

    }

}
