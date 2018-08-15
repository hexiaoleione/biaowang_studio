package com.hex.express.iwant.activities;

import butterknife.Bind;
import butterknife.ButterKnife;

import com.hex.express.iwant.R;
import com.hex.express.iwant.views.TitleBarView;

import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageView;

public class ComActivity extends BaseActivity {
	@Bind(R.id.webview) WebView webview;
	@Bind(R.id.btn_Left)
	ImageView btn_Left;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_function);
		ButterKnife.bind(this);
//		tbv_show.setTitleText("公司介绍");
	btn_Left.setOnClickListener(new  View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				finish();
			}
		});
		
		webview.loadUrl("http://www.efamax.com/mobile_document/about/company.html");
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
