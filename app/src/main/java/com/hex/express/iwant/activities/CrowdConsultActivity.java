package com.hex.express.iwant.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebSettings.LayoutAlgorithm;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import butterknife.Bind;
import butterknife.ButterKnife;

import com.hex.express.iwant.R;
import com.hex.express.iwant.iWantApplication;
import com.hex.express.iwant.views.TitleBarView;

/**
 * 融资咨询页
 * 
 * @author SCHT-50
 * 
 */
public class CrowdConsultActivity extends BaseActivity {
	@Bind(R.id.webview)
	WebView webView;
	@Bind(R.id.tbv_show)
	TitleBarView tbv_show;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_consult);
		ButterKnife.bind(CrowdConsultActivity.this);
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

	@SuppressLint("SetJavaScriptEnabled")
	@Override
	public void initData() {
		tbv_show.setTitleText("");
		WebSettings settings = webView.getSettings();
		settings.setJavaScriptEnabled(true);
		settings.setLayoutAlgorithm(LayoutAlgorithm.SINGLE_COLUMN);
		webView.setWebViewClient(new WebViewClient() {
			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				   if (url.startsWith("tel:")){
	                   Intent intent = new Intent(Intent.ACTION_VIEW,
	                           Uri.parse(url));
	                   startActivity(intent);
	                   } else if(url.startsWith("http:") || url.startsWith("https:")) {
	                       view.loadUrl(url);
	                   }
				return true;
			}
		

		});
		webView.loadUrl("http://www.efamax.com/stockholder.html");

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
