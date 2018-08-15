package com.hex.express.iwant.activities;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.TextView;

import com.google.gson.Gson;
import com.hex.express.iwant.R;
import com.hex.express.iwant.R.color;
import com.hex.express.iwant.R.id;
import com.hex.express.iwant.bean.BaseBean;
import com.hex.express.iwant.bean.GuardBean;
import com.hex.express.iwant.constance.MCUrl;
import com.hex.express.iwant.constance.PreferenceConstants;
import com.hex.express.iwant.http.AsyncHttpUtils;
import com.hex.express.iwant.http.UrlMap;
import com.hex.express.iwant.iWantApplication;
import com.hex.express.iwant.utils.PreferencesUtils;
import com.hex.express.iwant.utils.ToastUtil;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;

import butterknife.Bind;
import butterknife.ButterKnife;

public class GuardActivity extends BaseActivity{

	@Bind(id.btnRight)
	TextView btnRight;
	@Bind(id.btnLeft)
	ImageView btnLeft;
	@Bind(id.webview)
	WebView webView;
	@Bind(id.tuiren)
	TextView tuiren;
	@Bind(id.tuimoney)
	TextView tuimoney;
	
	@Bind(id.thisweek)
	TextView thisweek;
	@Bind(id.thisall)
	TextView thisall;
	
	
	@Bind(id.btntui)
	Button btntui;
	GuardBean bean;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.guard_activity);
		iWantApplication.getInstance().addActivity(this);
		ButterKnife.bind(this);
		initData();
		initView();
//		getrequstBalance();
		getrequs();
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
		btnLeft.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				finish();
				
			}
		});
		btnRight.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub

				Intent intent = new Intent(GuardActivity.this,HAdvertActivity.class);					
					intent.putExtra("url", "http://www.efamax.com/mobile/explain/driverExplain.html");
						startActivity(intent);
			}
		});
		webView.loadUrl("http://www.efamax.com/mobile_document/driver.html");
		webView.setBackgroundColor(2);
		webView.setWebViewClient(new WebViewClient() {
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
	/**
	 * 获取钱包余额
	 */
	private void getrequs() {
		RequestParams params = new RequestParams();
		Log.e("json", UrlMap.getUrl(MCUrl.driveInfo, "userId", String
				.valueOf(PreferencesUtils.getInt(getApplicationContext(),
						PreferenceConstants.UID))));
		AsyncHttpUtils.doGet(UrlMap.getUrl(MCUrl.driveInfo, "userId", String
				.valueOf(PreferencesUtils.getInt(getApplicationContext(),
						PreferenceConstants.UID))), null, null, params,
				new AsyncHttpResponseHandler() {

					@Override
					public void onFailure(int arg0, Header[] arg1, byte[] arg2,
							Throwable arg3) {
					}
					@Override
					public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
						// TODO Auto-generated method stub
						Log.e("1112json", "" + new String(arg2));
						 bean = new Gson().fromJson(
								new String(arg2), GuardBean.class); 
						if (bean.getErrCode()==0) {
//							tuiren.setText("接单次数: "+bean.data.get(0).driverCount);
//							tuimoney.setText("奖励金额: "+bean.data.get(0).activityMoney+"元");
							 thisweek.setText(bean.data.get(0).activityCount);
							 thisall.setText(bean.data.get(0).driverRouteCount);
						}else {
							ToastUtil.shortToast(GuardActivity.this, bean.getMessage());
						}
					}

				});
	}
	/**
	 * 获取钱包余额
	 */
	private void getrequstBalance() {
		RequestParams params = new RequestParams();
//		Log.e("json", UrlMap.getUrl(MCUrl.BALANCE, "id", String
//				.valueOf(PreferencesUtils.getInt(getApplicationContext(),
//						PreferenceConstants.UID))));
		AsyncHttpUtils.doGet(UrlMap.getUrl(MCUrl.driverActivity, "userId", String
				.valueOf(PreferencesUtils.getInt(getApplicationContext(),
						PreferenceConstants.UID))), null, null, params,
				new AsyncHttpResponseHandler() {

					@Override
					public void onFailure(int arg0, Header[] arg1, byte[] arg2,
							Throwable arg3) {
					}
					@Override
					public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
						// TODO Auto-generated method stub
						Log.e("1112json", "" + new String(arg2));
						 bean = new Gson().fromJson(
								new String(arg2), GuardBean.class); 
						if (bean.getErrCode()==0) {
							tuiren.setText("接单次数: "+bean.data.get(0).driverCount);
							tuimoney.setText("奖励金额: "+bean.data.get(0).activityMoney+"元");
							if (bean.data.get(0).ifReciveMoney) {
								btntui.setText("已提现");
								btntui.setBackgroundColor(color.huise);
							}
							btntui.setOnClickListener(new OnClickListener() {
								@Override
								public void onClick(View arg0) {
									// TODO Auto-generated method stub
									if (bean.data.get(0).buttunSwitch) {
										getrequstBala();
									}else {
										ToastUtil.shortToast(GuardActivity.this, bean.getMessage());
									}
								}
							});
							
						}else {
							ToastUtil.shortToast(GuardActivity.this, bean.getMessage());
							tuiren.setVisibility(View.GONE);
							
						}
						
					}

				});
	}
	/**
	 * 获取钱包余额
	 */
	private void getrequstBala() {
		RequestParams params = new RequestParams();
//		Log.e("json", UrlMap.getUrl(MCUrl.BALANCE, "id", String
//				.valueOf(PreferencesUtils.getInt(getApplicationContext(),
//						PreferenceConstants.UID))));
		AsyncHttpUtils.doGet(UrlMap.getUrl(MCUrl.driverActivityReceive, "userId", String
				.valueOf(PreferencesUtils.getInt(getApplicationContext(),
						PreferenceConstants.UID))), null, null, params,
				new AsyncHttpResponseHandler() {

					@Override
					public void onFailure(int arg0, Header[] arg1, byte[] arg2,
							Throwable arg3) {
					}
					@Override
					public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
						// TODO Auto-generated method stub
						Log.e("111223json", "" + new String(arg2));
						BaseBean bean = new Gson().fromJson(
								new String(arg2), GuardBean.class); 
						if (bean.getErrCode()==0) {
							btntui.setText("已提现");
							btntui.setBackgroundColor(color.huise);
							ToastUtil.shortToast(GuardActivity.this, bean.getMessage());
						}else {
							ToastUtil.shortToast(GuardActivity.this, bean.getMessage());
							
						}
						
					}

				});
	}
	public int jina(int a ,int b){
		int res=a-b;
		return res;
	}
	/**
	 */
	
	public void showwindow() {
	
		final TextView te;
		Button btn_recceivedOKs;
		ImageView adv_bg;
		 final Intent intent=new Intent() ;
		// 利用layoutInflater获得View
		LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View view = inflater.inflate(R.layout.popwindow_spea, null);
		// 下面是两种方法得到宽度和高度 getWindow().getDecorView().getWidth()
		final PopupWindow	window02 = new PopupWindow(view, WindowManager.LayoutParams.MATCH_PARENT,
				WindowManager.LayoutParams.MATCH_PARENT);
		// 设置popWindow弹出窗体可点击，这句话必须添加，并且是true
		window02.setFocusable(true);
		// 实例化一个ColorDrawable颜色为半透明
		// ColorDrawable dw = new ColorDrawable(R.color.transparent01);
		ColorDrawable dw = new ColorDrawable(android.R.color.white);
		window02.setBackgroundDrawable(dw);
		window02.setOutsideTouchable(false);// 这是点击外部不消失
		// 设置popWindow的显示和消失动画
		window02.setAnimationStyle(R.style.mypopwindow_anim_style);
		// 在底部显示
		window02.showAtLocation(GuardActivity.this.findViewById(id.btntui), Gravity.CENTER, 0, 0);
		btn_recceivedOKs=(Button) view.findViewById(id.btn_recceivedOKs);
		te=(TextView) view.findViewById(id.te);
//		te.setText(""+bean.getData().get(0).matName);
//		relativeLayout1.setim
//		Log.e("11111url", ""+adbean.getData().get(0).advertiseImageUrl);
//		
		btn_recceivedOKs.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				window02.dismiss();
			}
		});
		
		// popWindow消失监听方法
		window02.setOnDismissListener(new OnDismissListener() {

			@Override
			public void onDismiss() {
				window02.dismiss();
			}
		});

	}


}
