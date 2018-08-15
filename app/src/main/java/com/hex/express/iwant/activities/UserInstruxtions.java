package com.hex.express.iwant.activities;

import butterknife.Bind;
import butterknife.ButterKnife;

import com.hex.express.iwant.R;
import com.hex.express.iwant.views.TitleBarView;

import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
/**
 * 用户使用说明
 * @author SCHT-50
 *
 */
public class UserInstruxtions extends BaseActivity {
	@Bind(R.id.webview)
	WebView webView;
	@Bind(R.id.tbv_show)
	TitleBarView tbv_show;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_function);
		ButterKnife.bind(UserInstruxtions.this);tbv_show.setTitleText("使用说明");
		webView.loadUrl("http://www.efamax.com/mobile/user_rules.html");
		// webview.setWebViewClient(new webViewClient());
		webView.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
            
        });
	}
	public UserInstruxtions() {
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
