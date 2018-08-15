package com.hex.express.iwant.activities;

import com.hex.express.iwant.R;
import com.hex.express.iwant.newmain.MainTab;
import com.hex.express.iwant.newsmain.NewMainActivity;
import com.hex.express.iwant.views.TitleBarView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.Bind;
import butterknife.ButterKnife;

public class WeHwedActivity extends BaseActivity {
	@Bind(R.id.webview)
	WebView webView;
	@Bind(R.id.btnLeft)
	ImageView btnLeft;
	private TextView btn_go;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_weh);
		ButterKnife.bind(this);
		webView.loadUrl("http://www.efamax.com/mobile/Preferential_notice.html");
		// webview.setWebViewClient(new webViewClient());
   btnLeft.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				SharedPreferences sp = getSharedPreferences("data", MODE_PRIVATE);
				SharedPreferences.Editor edit = sp.edit();
				edit.putBoolean("isLogin", false);
				edit.commit();
//				Intent  intent=new Intent(mActivity,MainActivity.class);
//				Intent  intent=new Intent(mActivity,MainTab.class);
				Intent  intent=new Intent(mActivity,NewMainActivity.class);
//				startActivity(intent);
				finish();
			}
		});
		
//		btn_go = (TextView) findViewById(R.id.btn_go);
//		btn_go.setVisibility(View.VISIBLE);
//		btn_go.setText("镖王晋级镖师");
//		btn_go.setOnClickListener(new OnClickListener() {
//			
//			@Override
//			public void onClick(View v) {
//				Intent intent = new Intent();
//				// TODO Auto-generated method stub
////				
////				checkName = getIntent().getStringExtra("checkName");
////				checkIdCard = getIntent().getStringExtra("checkIdCard");
////				chenkPath = getIntent().getStringExtra("chenkPath");
////				startActivity(new Intent(getApplicationContext(), RoleAuthenticationActivity.class));
//				if (PreferencesUtils.getString(getApplicationContext(), PreferenceConstants.USERTYPE).equals("2")
//						|| PreferencesUtils.getString(getApplicationContext(), PreferenceConstants.USERTYPE)
//								.equals("3")) {
//					ToastUtil.shortToast(H5ModelActivity.this, "您已具备镖师资格");
//				} else {
//					intent.putExtra("IDENTIFY_CODE", "2");
//					startActivity(intent.setClass(getApplicationContext(), PrefectActivity.class));
//					finish();
//				}
//			}
//		});
		
		
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


