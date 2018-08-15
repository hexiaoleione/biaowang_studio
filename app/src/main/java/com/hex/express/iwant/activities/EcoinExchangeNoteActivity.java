package com.hex.express.iwant.activities;

import butterknife.Bind;
import butterknife.ButterKnife;

import com.hex.express.iwant.R;
import com.hex.express.iwant.views.TitleBarView;

import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;

public class EcoinExchangeNoteActivity extends BaseActivity {
	@Bind(R.id.webview)
	WebView webView;
	@Bind(R.id.btn_Left)
	ImageView btn_Left;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_function);
		ButterKnife.bind(this);
//		tbv_show.setTitleText("积分说明");
		btn_Left.setOnClickListener(new View .OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				finish();
			}
		});
		webView.loadUrl("http://www.efamax.com/mobile_document/ecoin/rewardCourierEcoin.html");
		webView.setWebViewClient(new WebViewClient(){
            @Override	
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
          
        });
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
