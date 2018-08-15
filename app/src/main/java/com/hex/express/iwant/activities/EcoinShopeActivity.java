package com.hex.express.iwant.activities;

import com.hex.express.iwant.R;
import com.hex.express.iwant.iWantApplication;
import com.hex.express.iwant.views.TitleBarView;

import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;

/**
 * e币商城
 * 
 * @author zheng
 * 
 */
public class EcoinShopeActivity extends BaseActivity {

	private TitleBarView tbv_show;
	private WebView webv_exchangenote;
	private String url = "http://www.efamax.com/express/ebshop/ecoin.html";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_ecoinexchangenote);
		iWantApplication.getInstance().addActivity(this);
//		getData();
		initView();
//		initData();
//		setOnClick();
	}

	@Override
	public void onWeightClick(View v) {

	}

	@Override
	public void initView() {
		// 标题
		tbv_show = (TitleBarView) findViewById(R.id.tbv_show);
		this.tbv_show.setTitleText(R.string.ecoinshope);
		// webview
		webv_exchangenote = (WebView) findViewById(R.id.webv_exchangenote);
		webv_exchangenote.loadUrl(url);
		webv_exchangenote.setWebViewClient(new WebViewClient(){
            @Override	
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
          
        });
	}

	@Override
	public void initData() {
		// 设置JS交互数据
		webv_exchangenote.getSettings().setJavaScriptEnabled(true);
		webv_exchangenote.getSettings().setSupportZoom(true);
		webv_exchangenote.getSettings().setBuiltInZoomControls(true);
		webv_exchangenote.loadUrl(url);
	}

	@Override
	public void setOnClick() {
		// 设置webview的点击事件
		webv_exchangenote.setWebViewClient(new WebViewClient() {
			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				view.loadUrl(url);
				return true;
			}
		});
	}

	@Override
	public void getData() {

	}

}
