package com.hex.express.iwant.activities;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;

import org.apache.http.Header;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import butterknife.Bind;
import butterknife.ButterKnife;

import com.google.gson.Gson;
import com.hex.express.iwant.R;
import com.hex.express.iwant.iWantApplication;
import com.hex.express.iwant.activities.MyCourierActivity.myAdapter;
import com.hex.express.iwant.bean.BaseBean;
import com.hex.express.iwant.bean.RegisterBean;
import com.hex.express.iwant.constance.MCUrl;
import com.hex.express.iwant.constance.PreferenceConstants;
import com.hex.express.iwant.http.AsyncHttpUtils;
import com.hex.express.iwant.http.UrlMap;
import com.hex.express.iwant.utils.MyBitmapUtils;
import com.hex.express.iwant.utils.PreferencesUtils;
import com.hex.express.iwant.utils.ToastUtil;
import com.hex.express.iwant.views.TitleBarView;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

public class EcoinActivity extends BaseActivity {
	@Bind(R.id.tbv_show)
	TitleBarView tbv_show;
	@Bind(R.id.txtMyEocin)
	TextView txtMyEocin;
	@Bind(R.id.txtHisEcoin)
	TextView txtHisEcoin;
	@Bind(R.id.txtExchange)
	TextView txtExchange;
	@Bind(R.id.txtEcoinMall)
	TextView txtEcoinMall;
	@Bind(R.id.txtHisEcoinmm)
	TextView txtHisEcoinmm;
	
	@Bind(R.id.imgAdvertising)
	ImageView imgAdvertising;
	@Bind(R.id.ress)
	RelativeLayout ress;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_ecoin);
		iWantApplication.getInstance().addActivity(this);
		ButterKnife.bind(this);
		getData();
		initView();
		initData();
		setOnClick();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		getHttpMoney();
	}
	@Override
	public void onWeightClick(View v) {
		final Intent intent = new Intent();
		switch (v.getId()) {
		case R.id.txtHisEcoin:// 历史积分
			intent.setClass(EcoinActivity.this, EcoinHistory.class);
			startActivity(intent);
			break;
		case R.id.txtExchange:// 兑换说明
			intent.setClass(EcoinActivity.this, EcoinExchangeNoteActivity.class);
			startActivity(intent);
			break;
		case R.id.ress:// 积分商城
			intent.setClass(EcoinActivity.this, EcoinShopeActivity.class);
			startActivity(intent);
			break;
		case R.id.txtHisEcoinmm:// 购买积分
//			intent.setClass(EcoinActivity.this, IntegralActivity.class);
			intent.setClass(EcoinActivity.this, CardActivityse.class);
//			startActivity(intent);
			startActivityForResult(intent, 1);
			break;
		default:
			break;
		}
	}

	@Override
	public void initView() {

	}

	@Override
	public void initData() {
		this.tbv_show.setTitleText(R.string.ecointitle);
		dialog.show();
		getHttpMoney();
		SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyyMMdd");
		String date = sDateFormat.format(new java.util.Date());
		Log.e("UUUUUUUUUUUU", UrlMap.getUrl(
				"http://www.efamax.com/mobile_document/ecoin/business.png",
				"date", date));
		new MyBitmapUtils().display(imgAdvertising, UrlMap.getUrl(
				"http://www.efamax.com/mobile_document/ecoin/business.png",
				"date", date));
	}

	private void getHttpMoney() {
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
						dialog.dismiss();
					}

					@Override
					public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
						// TODO Auto-generated method stub
						dialog.dismiss();
						Log.e("json", "" + new String(arg2));
						RegisterBean bean = new Gson().fromJson(
								new String(arg2), RegisterBean.class);
						txtMyEocin.setText(String.valueOf(bean.data.get(0).ecoin)+"");
					}

				});

	}

	@Override
	public void setOnClick() {
		txtHisEcoin.setOnClickListener(this);
		txtExchange.setOnClickListener(this);
		txtEcoinMall.setOnClickListener(this);
		ress.setOnClickListener(this);
		txtHisEcoinmm.setOnClickListener(this);
	}

	@Override
	public void getData() {

	}
	/**
	 * 接收选择现金价参数
	 */
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		Log.e("requestCode  ", "          "+requestCode);
		Log.e("resultCode  ", "          "+resultCode);
		Log.e("11111data  ", "          "+data);
		switch (requestCode) {
		case 1:
			if (requestCode == 1 && data != null) {
				Bundle bundle = data.getBundleExtra("bundle2");
				double num3 = 0.0;
				double num4 = 0.0;
				DecimalFormat df = new DecimalFormat("###.00");  
				bundle.getString("strResult");
				Log.e("11111 userCouponId  ", "          "+bundle.getString("strResult"));
				Log.e("11111 money  ", "          "+bundle.getString("money"));
			 getrequstBalancese(bundle.getString("strResult"));
			}
			
			break;
		}
		}
	/**
	 * 提交
	 */
	private void getrequstBalancese(String i) {
		RequestParams params = new RequestParams();
	
		AsyncHttpUtils.doGet(UrlMap.getTwo(MCUrl.exchangeCoupon, "userId", String
				.valueOf(PreferencesUtils.getInt(getApplicationContext(),
						PreferenceConstants.UID)),"couponId",""+i), null, null, params,
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
							 ToastUtil.shortToast(EcoinActivity.this, bean2.getMessage());
//							 finish();
							 getHttpMoney();
						}else {
							 ToastUtil.shortToast(EcoinActivity.this, bean2.getMessage());
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
}
