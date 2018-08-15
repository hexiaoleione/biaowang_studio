package com.hex.express.iwant.activities;

import butterknife.Bind;
import butterknife.ButterKnife;

import com.hex.express.iwant.R;
import com.hex.express.iwant.iWantApplication;
import com.hex.express.iwant.http.UrlMap;
import com.hex.express.iwant.views.TitleBarView;
import com.hex.express.iwant.views.TitleViewBarView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
/**
 * 查询快递
 * @author SCHT-50
 *
 */
public class ExpressCheckActivity extends BaseActivity {
	@Bind(R.id.web)
	WebView webView;
	@Bind(R.id.tbv_show)
	TitleViewBarView tvb_show;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_express_check);
		iWantApplication.getInstance().addActivity(this);
		ButterKnife.bind(this);
		initData();
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
		tvb_show.setTitleText("查询快递");
		final String stringExtra = getIntent().getStringExtra("expName");
		final String expNo = getIntent().getStringExtra("expNo");
		String url = "http://m.kuaidi100.com/index_all.html";
		webView.setEnabled(true);
		WebSettings webSettings = webView.getSettings();
		webSettings.setJavaScriptEnabled(true);
		webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);
		webView.loadUrl(UrlMap
				.getTwo(url, "type", stringExtra, "postid", expNo));
		webView.setWebViewClient(new WebViewClient(){
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

}
