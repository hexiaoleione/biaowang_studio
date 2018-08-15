package com.hex.express.iwant.activities;

import com.hex.express.iwant.R;
import com.hex.express.iwant.views.TitleBarView;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.webkit.WebSettings.LayoutAlgorithm;
import butterknife.Bind;
import butterknife.ButterKnife;

public class CourierInstrucActivity  extends BaseActivity {
	@Bind(R.id.webview)
	WebView webView;
	@Bind(R.id.tbv_show)
	TitleBarView tbv_show;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_function);
		ButterKnife.bind(CourierInstrucActivity.this);
		tbv_show.setTitleText("人人代理详解问答");
		webView.loadUrl("http://www.efamax.com/mobile/Everyone_promotion.html   ");
		WebSettings settings = webView.getSettings();
		settings.setJavaScriptEnabled(true);
		settings.setLayoutAlgorithm(LayoutAlgorithm.SINGLE_COLUMN);
//		webView.loadUrl("http://www.efamax.com/mobile_document/discount_courier.html");//快递员的现金政策
		// webview.setWebViewClient(new webViewClient());
		webView.setWebViewClient(new WebViewClient(){
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
	}

	public CourierInstrucActivity() {
		// TODO Auto-generated constructor stub
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

