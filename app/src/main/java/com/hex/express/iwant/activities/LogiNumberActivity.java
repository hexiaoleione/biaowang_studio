package com.hex.express.iwant.activities;

import java.text.DecimalFormat;

import org.apache.http.Header;

import com.google.gson.Gson;
import com.hex.express.iwant.R;
import com.hex.express.iwant.iWantApplication;
import com.hex.express.iwant.R.color;
import com.hex.express.iwant.R.id;
import com.hex.express.iwant.bean.BaseBean;
import com.hex.express.iwant.bean.GuardBean;
import com.hex.express.iwant.bean.RegisBean;
import com.hex.express.iwant.bean.RewardInfoBean;
import com.hex.express.iwant.bean.SpreadBean;
import com.hex.express.iwant.constance.MCUrl;
import com.hex.express.iwant.constance.PreferenceConstants;
import com.hex.express.iwant.http.AsyncHttpUtils;
import com.hex.express.iwant.http.UrlMap;
import com.hex.express.iwant.newsmain.NewMainActivity;
import com.hex.express.iwant.utils.NormalLoadPictrue;
import com.hex.express.iwant.utils.PreferencesUtils;
import com.hex.express.iwant.utils.ToastUtil;
import com.hex.express.iwant.views.TitleBarView;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import android.R.integer;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.PopupWindow.OnDismissListener;
import butterknife.Bind;
import butterknife.ButterKnife;

public class LogiNumberActivity extends BaseActivity{

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
	@Bind(id.btntui)
	Button btntui;
	RewardInfoBean bean;
	GuardBean beans;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.lognumber_activity);
		iWantApplication.getInstance().addActivity(this);
		ButterKnife.bind(this);
		initData();
		initView();
		getrequstBalance();
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

				Intent intent = new Intent(LogiNumberActivity.this,HAdvertActivity.class);					
					intent.putExtra("url", "http://www.efamax.com/mobile/explain/loginReward.html");
						startActivity(intent);
			}
		});
		webView.loadUrl("http://www.efamax.com/mobile_document/loginReward.html");
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
	private void getrequstBalance() {
		RequestParams params = new RequestParams();
//		Log.e("json", UrlMap.getUrl(MCUrl.BALANCE, "id", String
//				.valueOf(PreferencesUtils.getInt(getApplicationContext(),
//						PreferenceConstants.UID))));
		AsyncHttpUtils.doGet(UrlMap.getUrl(MCUrl.loginRewardInfo, "userId", String
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
								new String(arg2), RewardInfoBean.class); 
						if (bean.getErrCode()==0) {
							tuiren.setText("奖励总金额: "+bean.data.get(0).rewardCount);
							if (!bean.data.get(0).ifReward) {
//								btntui.setText("已领取");
//								btntui.setBackgroundColor(color.huise);
								btntui.setBackgroundResource(R.drawable.lognumberqiangw);
							}
							btntui.setOnClickListener(new OnClickListener() {
								@Override
								public void onClick(View arg0) {
									// TODO Auto-generated method stub
//									if (bean.data.get(0).ifReward) {
										getrequstBala();
								
//									}else {
//										ToastUtil.shortToast(LogiNumberActivity.this, "暂不可领取");
//									}
								}
							});
							
						}else {
							ToastUtil.shortToast(LogiNumberActivity.this, bean.getMessage());
							tuiren.setVisibility(View.GONE);
							
						}
						
					}

				});
	}
	/**
	 * 获取
	 */
	private void getrequstBala() {
		RequestParams params = new RequestParams();
//		Log.e("json", UrlMap.getUrl(MCUrl.BALANCE, "id", String
//				.valueOf(PreferencesUtils.getInt(getApplicationContext(),
//						PreferenceConstants.UID))));
		AsyncHttpUtils.doGet(UrlMap.getUrl(MCUrl.getLoginReward, "userId", String
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
						beans = new Gson().fromJson(
								new String(arg2), GuardBean.class); 
						if (beans.getErrCode()==0) {
//							btntui.setText("已领取");
							showwindow();
							btntui.setBackgroundResource(R.drawable.lognumberqiangw);
							
//							ToastUtil.shortToast(LogiNumberActivity.this, beans.getMessage());
//							getrequstBalance();
//							finish();
						}else {
							ToastUtil.shortToast(LogiNumberActivity.this, beans.getMessage());
							
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
	
		final TextView teyuan;
		Button btn_recceivedOKs,btnex;
		 final Intent intent=new Intent() ;
		// 利用layoutInflater获得View
		LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View view = inflater.inflate(R.layout.popwindow_lognumbers, null);
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
		window02.showAtLocation(LogiNumberActivity.this.findViewById(id.btntui), Gravity.CENTER, 0, 0);
		btn_recceivedOKs=(Button) view.findViewById(id.btn_recceivedOKs);
		btnex=(Button) view.findViewById(id.btnex);
		teyuan=(TextView) view.findViewById(id.teyuan);
		teyuan.setText(""+beans.getData().get(0).recordMoney);
//		relativeLayout1.setim
//		Log.e("11111url", ""+adbean.getData().get(0).advertiseImageUrl);
//		
		btn_recceivedOKs.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				window02.dismiss();
				startActivity(new Intent(LogiNumberActivity.this, NewMainActivity.class));
			}
		});
		btnex.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				window02.dismiss();
				getrequstBalance();
			}
		});
		// popWindow消失监听方法
		window02.setOnDismissListener(new OnDismissListener() {

			@Override
			public void onDismiss() {
				window02.dismiss();
				getrequstBalance();
			}
		});

	}


}
