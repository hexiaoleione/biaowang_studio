package com.hex.express.iwant.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.hex.express.iwant.R;
import com.hex.express.iwant.bean.DeposBean;
import com.hex.express.iwant.bean.GuardBean;
import com.hex.express.iwant.bean.RegisBean;
import com.hex.express.iwant.constance.MCUrl;
import com.hex.express.iwant.constance.PreferenceConstants;
import com.hex.express.iwant.http.AsyncHttpUtils;
import com.hex.express.iwant.http.UrlMap;
import com.hex.express.iwant.iWantApplication;
import com.hex.express.iwant.newactivity.DepositToActivity;
import com.hex.express.iwant.newactivity.DepositsActivity;
import com.hex.express.iwant.newactivity.InsuranceActivity;
import com.hex.express.iwant.newactivity.InsuranceOffActivity;
import com.hex.express.iwant.newactivity.InsuranceOnActivity;
import com.hex.express.iwant.utils.PreferencesUtils;
import com.hex.express.iwant.utils.ToastUtil;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;

import java.text.DecimalFormat;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class NewMyWalletActivity extends BaseActivity{

	@Bind(R.id.btnLeft)
	ImageView btnleft;//返回键
	@Bind(R.id.btnRight)
	TextView btnRight;
	@Bind(R.id.wa_validBalance)
	TextView wa_validBalance;// 显示金额
	
	@Bind(R.id.wa_deposit)
	LinearLayout wa_deposit;// 体现
	
	@Bind(R.id.wa_transfer)
	LinearLayout wa_transfer;// 转账
	
	@Bind(R.id.wa_chong)
	LinearLayout wa_chong;// 充值
	@Bind(R.id.ll_ebi)
	LinearLayout ll_ebi;// 积分
	@Bind(R.id.ll_xianjin)
	LinearLayout ll_xianjin;// 现金券
	@Bind(R.id.ll_tuiguang)
	LinearLayout ll_tuiguang;//
	@Bind(R.id.ll_biaoshi)
	LinearLayout ll_biaoshi;//
	@Bind(R.id.ll_dengluj)
	LinearLayout ll_dengluj;//
	
	@Bind(R.id.ll_yajin)
	LinearLayout ll_yajin;//
	
	@Bind(R.id.zuihou)
	LinearLayout zuihou;//
	
	
	@Bind(R.id.imgbiao)
	ImageView imgbiao;//
	
	@Bind(R.id.imgzuihou)
	ImageView imgzuihou;//
	
	@Bind(R.id.textbiao)
	TextView textbiao;//
	@Bind(R.id.yiwaixiant)
	TextView yiwaixiant;//
	
	DeposBean beanse;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.newallet_activity);//newwallet_activity newmywallet_activity
		iWantApplication.getInstance().addActivity(this);
		ButterKnife.bind(NewMyWalletActivity.this);
		getData();
		initView();
		initData();
		setOnClick();
		getrequs();
		getrequstBalance();
		//交押金
		getrequstBalancese();
//		imgzuihou.setVisibility(View.GONE);
//		imgbiao.setBackgroundResource(R.drawable.new_dians);
//		textbiao.setVisibility(View.INVISIBLE);
	}
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		getrequstBalance();
	}
	@OnClick({R.id.btnLeft,R.id.btnRight,R.id.wa_deposit,R.id.wa_transfer,R.id.wa_chong,R.id.ll_ebi,R.id.ll_xianjin,
		R.id.ll_tuiguang,R.id.ll_biaoshi,R.id.ll_dengluj,R.id.zuihou})
	public void onMyClick(View v) {
		Intent intent = new Intent();
		switch (v.getId()) {
		case R.id.btnLeft:// 返回
			finish();
			break;
		case R.id.btnRight:// 
			startActivity(intent.setClass(NewMyWalletActivity.this, MyWalletlistActivity.class));
			break;
		case R.id.wa_deposit:// 提现
//			if (PreferencesUtils.getString(NewMyWalletActivity.this, PreferenceConstants.USERTYPE).equals("1")) {
//				if (PreferencesUtils.getString(NewMyWalletActivity.this, PreferenceConstants.WLID).equals("1")||PreferencesUtils.getString(NewMyWalletActivity.this, PreferenceConstants.WLID).equals("")) {
//					AlertDialog.Builder ad = new Builder(NewMyWalletActivity.this);
//					ad.setTitle("温馨提示");
//					ad.setMessage("只有镖师或物流公司（司机）才能提现");
//					ad.setNegativeButton("去认证", new DialogInterface.OnClickListener() {
//
//						@Override
//						public void onClick(DialogInterface arg0, int arg1) {
//							Intent intents=new Intent();
//							intents.putExtra("tiaoguo", "2");
//							intents.setClass(NewMyWalletActivity.this, RoleAuthenticationActivity.class);
//							startActivity(intents);
//						}
//					});
//					ad.setPositiveButton("取消", new DialogInterface.OnClickListener() {
//						@Override
//						public void onClick(DialogInterface arg0, int arg1) {
//							arg0.dismiss();
//							
//						}
//					});
//					ad.create().show();
//				}
//			}
			startActivity(intent.setClass(NewMyWalletActivity.this, DepositActivity.class));
			break;
		case R.id.wa_transfer:// 转账
//			startActivity(intent.setClass(NewMyWalletActivity.this, TransferActivity.class));
		case R.id.wa_chong:// 
			startActivity(intent.setClass(NewMyWalletActivity.this, RechargeActivity.class));
			break;
		case R.id.ll_ebi:// ebi
			startActivity(intent.setClass(NewMyWalletActivity.this, EcoinActivity.class));
			break;
		case R.id.ll_xianjin:// xianj1
			startActivity(intent.setClass(NewMyWalletActivity.this, CardActivity.class));
			break;
		case R.id.ll_tuiguang:// 推广人数
//			startActivity(intent.setClass(NewMyWalletActivity.this, SpreadActivity.class));
			startActivity(intent.setClass(NewMyWalletActivity.this, GuardActivity.class));
//			startActivity(intent.setClass(NewMyWalletActivity.this, LogiNumberActivity.class));
//			ToastUtil.shortToast(NewMyWalletActivity.this, "该功能已关闭！");
			break;
//		case R.id.ll_biaoshi:// 镖师收益
//			startActivity(intent.setClass(NewMyWalletActivity.this, GuardActivity.class));
//			break;
		case R.id.ll_dengluj:// 登录收益
			startActivity(intent.setClass(NewMyWalletActivity.this, LogiNumberActivity.class));
//			startActivity(intent.setClass(NewMyWalletActivity.this, InsuranceActivity.class));
			
			break;
		case R.id.zuihou:// 登录收益
//			startActivity(intent.setClass(NewMyWalletActivity.this, LogiNumberActivity.class));
//			startActivity(intent.setClass(NewMyWalletActivity.this, InsuranceActivity.class));
			driveInfo();
			break;
			
			
		}
	}
	/**
	 * 获取钱包余额
	 * 
	 */
	private void getrequstBalance() {
		RequestParams params = new RequestParams();
		Log.e("json", UrlMap.getUrl(MCUrl.BALANCE, "id", String
				.valueOf(PreferencesUtils.getInt(getApplicationContext(),
						PreferenceConstants.UID))));
		AsyncHttpUtils.doGet(UrlMap.getUrl(MCUrl.WALLETINFO, "userId", String
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
						Log.e("json", "" + new String(arg2));
						RegisBean bean = new Gson().fromJson(
								new String(arg2), RegisBean.class); 
						if (bean.getErrCode()==0) {
							
						try {
							DecimalFormat df = new DecimalFormat("######0.00");
//							wa_validBalance.setText(df.format(bean.data.get(0).balance));validBalance
							wa_validBalance.setText(df.format(bean.data.get(0).validBalance));
						} catch (Exception e) {
							// TODO: handle exception
						}
						
						}
					}

				});
	}
	private void getrequstBalancese() {
		RequestParams params = new RequestParams();
		Log.e("json", UrlMap.getUrl(MCUrl.BALANCE, "id", String
				.valueOf(PreferencesUtils.getInt(getApplicationContext(),
						PreferenceConstants.UID))));
		AsyncHttpUtils.doGet(UrlMap.getUrl(MCUrl.BALANCE, "id", String
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
						Log.e("json", "" + new String(arg2));
						RegisBean bean = new Gson().fromJson(
								new String(arg2), RegisBean.class); 
						if (bean.getErrCode()==0) {
//							
							if (!bean.getData().get(0).userType.equals("3") && !bean.getData().get(0).userType.equals("2")) {
								imgzuihou.setVisibility(View.GONE);
								imgbiao.setBackgroundResource(R.drawable.new_dians);
								textbiao.setText("");
								 ll_yajin.setOnClickListener(new OnClickListener() {
										
										@Override
										public void onClick(View arg0) {
											// TODO Auto-generated method stub
//											driveInfo();
										}
									});
							}else {
	               ll_yajin.setOnClickListener(new OnClickListener() {
									
									@Override
									public void onClick(View arg0) {
										// TODO Auto-generated method stub
										Intent intent=new Intent();
										if (beanse.getErrCode()==0) {
											
										
										// 镖师押金     0 默认      1    已充值    2  退款中   3  已退款
										if (beanse.getData().get(0).driverMoney.equals("0") || beanse.getData().get(0).driverMoney.equals("3") ) {
											startActivity(intent.setClass(NewMyWalletActivity.this, DepositsActivity.class));
										}else if (beanse.getData().get(0).driverMoney.equals("1") || beanse.getData().get(0).driverMoney.equals("2")) {
											startActivity(intent.setClass(NewMyWalletActivity.this, DepositToActivity.class));	
										}
										
										}else {
											ToastUtil.shortToast(NewMyWalletActivity.this, beanse.getMessage());
										}
									}
								});
							}
						
						}
					}

				});
	}
	private void getrequs() {
		RequestParams params = new RequestParams();
	
		AsyncHttpUtils.doGet(UrlMap.getUrl(MCUrl.driverMoney, "userId", String
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
						Log.e("json", "" + new String(arg2));
						beanse = new Gson().fromJson(
								new String(arg2), DeposBean.class); 
						if (beanse.getErrCode()==0) {
							// 镖师押金     0 默认      1    已充值    2  退款中   3  已退款
							
							if (!"1".equals(beanse.getData().get(0).driverMoney)) {
								zuihou.setVisibility(View.GONE);
							}else {
								zuihou.setVisibility(View.VISIBLE);
//								imgzuihou.setVisibility(View.GONE);
//								imgbiao.setBackgroundResource(R.drawable.new_dians);
//								textbiao.setVisibility(View.INVISIBLE);
//								yiwaixiant.setVisibility(View.INVISIBLE);
							}
						}else {
//							imgzuihou.setVisibility(View.GONE);
//							imgbiao.setBackgroundResource(R.drawable.new_dians);
//							textbiao.setVisibility(View.INVISIBLE);
//							yiwaixiant.setVisibility(View.INVISIBLE);
						}
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
		wa_transfer.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				startActivity(intent.setClass(NewMyWalletActivity.this, TransferActivity.class));
			}
		});
	}
	public void driveInfo(){//MCUrl.  driver/driveInfo
		AsyncHttpUtils.doSimGet(UrlMap.getUrl(MCUrl.driveInfo, "userId", String
				.valueOf(PreferencesUtils.getInt(NewMyWalletActivity.this,
						PreferenceConstants.UID))), new AsyncHttpResponseHandler() {
			@Override
			public void onFailure(int arg0, Header[] arg1, byte[] arg2, Throwable arg3) {
				
			}
			@Override
			public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
				Log.e("111111se", new String(arg2));
				GuardBean guardBean = new Gson().fromJson(new String(arg2), GuardBean.class);
				if (guardBean.getErrCode()==0) {
//					finish();
					if (!"1".equals(guardBean.getData().get(0).ifBuyInsure)) {
						
						Intent intent=new Intent();
						startActivity(intent.setClass(NewMyWalletActivity.this, InsuranceActivity.class));
					}else {
						getDriverSafe();	
					}
				}else {
					ToastUtil.shortToast(NewMyWalletActivity.this, guardBean.getMessage());
				}
			}
		});
	}
	public void getDriverSafe(){//MCUrl.  driver/driveInfo
		AsyncHttpUtils.doSimGet(UrlMap.getUrl(MCUrl.getDriverSafe, "userId", String
				.valueOf(PreferencesUtils.getInt(NewMyWalletActivity.this,
						PreferenceConstants.UID))), new AsyncHttpResponseHandler() {
			@Override
			public void onFailure(int arg0, Header[] arg1, byte[] arg2, Throwable arg3) {
				
			}
			@Override
			public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
				Log.e("111111se", new String(arg2));
				GuardBean guardBean = new Gson().fromJson(new String(arg2), GuardBean.class);
				if (guardBean.getErrCode()==0) {
//					finish();
					if ("1".equals(guardBean.getData().get(0).ifPass) || "0".equals(guardBean.getData().get(0).ifPass)) {
						Intent intent=new Intent();
						startActivity(intent.setClass(NewMyWalletActivity.this, InsuranceOffActivity.class));
//						startActivity(intent.setClass(NewMyWalletActivity.this, InsuranceOnActivity.class));
					}else if ("2".equals(guardBean.getData().get(0).ifPass)) {
						Intent intent=new Intent();
						startActivity(intent.setClass(NewMyWalletActivity.this, InsuranceOnActivity.class));
					}
				}else {
//					Intent intent=new Intent();
//					startActivity(intent.setClass(NewMyWalletActivity.this, InsuranceActivity.class));
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
