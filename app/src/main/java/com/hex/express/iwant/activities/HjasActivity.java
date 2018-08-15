package com.hex.express.iwant.activities;

import com.hex.express.iwant.R;
import com.hex.express.iwant.views.TitleBarView;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.Bind;
import butterknife.ButterKnife;

public class HjasActivity extends BaseActivity {
	@Bind(R.id.webview)
	WebView webView;
	@Bind(R.id.btnLeft)
	ImageView btnLeft;
	@Bind(R.id.ok)
	Button ok;
	@Bind(R.id.off)
	Button off;
	private TextView btn_go;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_hjs);
		ButterKnife.bind(this);
//		tbv_show.setTitleText("镖王");
		
		String  uString="http://www.efamax.com/mobile/manual/inSureRuleAndroid.html";
//		Log.e("111", ""+uString);
		
		WebSettings settings = webView.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setUseWideViewPort(true);
        settings.setDomStorageEnabled(true);
        settings.setAppCacheMaxSize(1024 * 8);
        String appCachePath = getCacheDir().getAbsolutePath();
        settings.setAppCachePath(appCachePath);
        settings.setAllowFileAccess(true);
        settings.setAppCacheEnabled(true);
        settings.setJavaScriptCanOpenWindowsAutomatically(true);
        settings.setCacheMode(WebSettings.LOAD_NO_CACHE);
        
        webView.loadUrl(uString);
//        webView.getSettings().setJavaScriptEnabled(true);
//        // 设置可以支持缩放
        webView.getSettings().setSupportZoom(false);
//        // 设置出现缩放工具
        webView.getSettings().setBuiltInZoomControls(false);
//        //扩大比例的缩放
        webView.getSettings().setUseWideViewPort(false);
//        //控制WebView 自适应屏幕
//        webView.getSettings().setUseWideViewPort(true);
//        webView.getSettings().setLoadWithOverviewMode(true);
//        String urlString =uString;
//        webView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
//        webView.setWebViewClient(new WebViewClient() {
//        	
//            public boolean shouldOverrideUrlLoading(WebView view, String url) {
//                Uri uri = Uri.parse(url);
//
//               uri.getQueryParameter("price"); //获取数据
//               Log.e("11111", ""+uri.getQueryParameter("price"));
//		       	return false;
//
//            }
//            public void onPageStarted(WebView view, String url, Bitmap favicon) {
////                super.onPageStarted(view, url, favicon);
//
//            }
//
//            public void onPageFinished(WebView view, String url) {
//
//            }
//        });
        
//	
		ok.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
//				Intent intent=new Intent();
//				intent.putExtra("type", "1");
//				setResult(RESULT_OK, intent);
				finish();
			}
		});
		off.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intent=new Intent();
				intent.putExtra("type", "2");
				setResult(RESULT_OK, intent);
				finish();
			}
		});
		btnLeft.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intent=new Intent();
				intent.putExtra("type", "2");
				setResult(RESULT_OK, intent);
				finish();
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
	@Override
	public boolean dispatchKeyEvent(KeyEvent event) {
		if (event.getKeyCode() == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
			Intent intent=new Intent();
			intent.putExtra("type", "2");
			setResult(RESULT_OK, intent);
			finish();
			return true;
		}
		return super.dispatchKeyEvent(event);
	}
}


