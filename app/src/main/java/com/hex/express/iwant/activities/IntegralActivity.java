package com.hex.express.iwant.activities;

import java.text.DecimalFormat;

import org.apache.http.Header;

import com.google.gson.Gson;
import com.hex.express.iwant.R;
import com.hex.express.iwant.iWantApplication;
import com.hex.express.iwant.bean.BaseBean;
import com.hex.express.iwant.bean.Integ;
import com.hex.express.iwant.bean.RegisBean;
import com.hex.express.iwant.constance.MCUrl;
import com.hex.express.iwant.constance.PreferenceConstants;
import com.hex.express.iwant.http.AsyncHttpUtils;
import com.hex.express.iwant.http.UrlMap;
import com.hex.express.iwant.utils.PreferencesUtils;
import com.hex.express.iwant.utils.ToastUtil;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import android.R.integer;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.Bind;
import butterknife.ButterKnife;

public class IntegralActivity extends BaseActivity{

	
	@Bind(R.id.yijifen)
	TextView yijifen;
	@Bind(R.id.shijifen)
	TextView shijifen;
	@Bind(R.id.btnLeft)
	ImageView btnLeft;
	@Bind(R.id.btn_yi)
	
	Button btn_yi;
	@Bind(R.id.btn_shi)
	Button btn_shi;
	Integ bean;
	int money;
	int count;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_integral);
		iWantApplication.getInstance().addActivity(this);
		ButterKnife.bind(this);
		initView();
		initData();
		setOnClick();
	}
	@Override
	public void onWeightClick(View v) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void initView() {
		// TODO Auto-generated method stub
		getrequstBalance();
		btnLeft.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				finish();
			}
		});
		btn_yi.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				 if (bean.errCode==0) {
				getrequstBalancese(1);
				 }else {
						ToastUtil.shortToast(IntegralActivity.this, bean.message);
					}
			}
		});
		btn_shi.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				 if (bean.errCode==0) {
						getrequstBalancese(2);
						 }else {
								ToastUtil.shortToast(IntegralActivity.this, bean.message);
							}
			}
		});
		
	}
	/**
	 * 获取
	 */
	private void getrequstBalance() {
		RequestParams params = new RequestParams();
		Log.e("json", UrlMap.getUrl(MCUrl.BALANCE, "id", String
				.valueOf(PreferencesUtils.getInt(getApplicationContext(),
						PreferenceConstants.UID))));
		AsyncHttpUtils.doGet(UrlMap.getUrl(MCUrl.buyEcoinRule, "", ""), null, null, params,
				new AsyncHttpResponseHandler() {

					@Override
					public void onFailure(int arg0, Header[] arg1, byte[] arg2,
							Throwable arg3) {
					}
					@Override
					public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
						// TODO Auto-generated method stub
						Log.e("json", "" + new String(arg2));
						 bean = new Gson().fromJson(
								new String(arg2), Integ.class); 
						 if (bean.errCode==0) {
							 yijifen.setText(bean.data.get(0).countMin+"积分"); 
							 shijifen.setText(bean.data.get(0).countMax+"积分"); 
							 btn_yi.setText(bean.data.get(0).stringMin); 
							 btn_shi.setText(bean.data.get(0).stringMax); 
							
						}else {
							ToastUtil.shortToast(IntegralActivity.this, bean.message);
						}
					}

				});
	}

	/**
	 * 提交
	 */
	private void getrequstBalancese(int i) {
		RequestParams params = new RequestParams();
	
		if (i==1) {
			money=bean.data.get(0).moneyMin;
			count=bean.data.get(0).countMin;
		}else if(i==2){
			money=bean.data.get(0).moneyMax;
			count=bean.data.get(0).countMax;
		}
		AsyncHttpUtils.doGet(UrlMap.getThreeUrl(MCUrl.buyEcoin, "userId", String
				.valueOf(PreferencesUtils.getInt(getApplicationContext(),
						PreferenceConstants.UID)),"money",""+money,"count",""+count), null, null, params,
				new AsyncHttpResponseHandler() {

					@Override
					public void onFailure(int arg0, Header[] arg1, byte[] arg2,
							Throwable arg3) {
					}
					@Override
					public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
						// TODO Auto-generated method stub
						Log.e("json", "" + new String(arg2));
					BaseBean	 bean2 = new Gson().fromJson(
								new String(arg2), BaseBean.class); 
						 if (bean2.getErrCode()==0) {
							 ToastUtil.shortToast(IntegralActivity.this, bean2.getMessage());
							 finish();
						}else {
							 ToastUtil.shortToast(IntegralActivity.this, bean2.getMessage());
//							AlertDialog.Builder ad = new Builder(IntegralActivity.this);
//							ad.setTitle("温馨提示");
//							ad.setMessage(bean2.getMessage());
//							ad.setNegativeButton("确认", new DialogInterface.OnClickListener() {
//
//								@Override
//								public void onClick(DialogInterface arg0, int arg1) {
//									Intent intent = new Intent();
//									startActivity(intent.setClass(IntegralActivity.this, RechargeActivity.class));
//									finish();
//								}
//							});
//							ad.setPositiveButton("取消", new DialogInterface.OnClickListener() {
//								@Override
//								public void onClick(DialogInterface arg0, int arg1) {
//									arg0.dismiss();
//									
//								}
//							});
//							ad.create().show();
//							

						}
					}

				});
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
