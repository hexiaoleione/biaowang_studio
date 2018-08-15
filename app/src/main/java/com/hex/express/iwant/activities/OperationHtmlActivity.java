package com.hex.express.iwant.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import butterknife.Bind;
import butterknife.ButterKnife;
import com.hex.express.iwant.R;
import com.hex.express.iwant.views.TitleBarView;

public class OperationHtmlActivity extends BaseActivity {
	@Bind(R.id.webview)
	WebView webView;
	@Bind(R.id.tbv_show)
	TitleBarView tbv_show;
	String url;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_operahtm);
		ButterKnife.bind(this);
		tbv_show.setTitleText("操作详解");
		tbv_show.setLeftBtnOnclickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				finish();
			}
		});
		WebSettings settings = webView.getSettings();
		settings.setJavaScriptEnabled(true);
		url=getIntent().getStringExtra("url");
		webView.loadUrl(url);
//		webView.loadUrl("http://202.108.103.154:9090/HT_interfacePlatform/PdfAction?policyNo=B017136012017021125&insuranceCode=3601");
		
		// webview.setWebViewClient(new webViewClient());
		webView.setWebViewClient(new WebViewClient() {
			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				if (url.startsWith("tel:")) {
					Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
					startActivity(intent);
				} else if (url.startsWith("http:") || url.startsWith("https:")) {
					view.loadUrl(url);
				}
				return true;
			}

		});
	}
	public boolean dispatchKeyEvent(KeyEvent event) {
		if (event.getKeyCode() == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN
				&& event.getRepeatCount() == 0) {
			finish();// 销毁此页面,并去启动上一个页面的相应的Fragment
		}
		return super.dispatchKeyEvent(event);
	}
	@Override 
	protected void onDestroy() {
		super.onDestroy();
		if(webView!=null){
			webView.destroy();
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
