package com.hex.express.iwant.activities;

import java.text.DecimalFormat;

import org.apache.http.Header;

import com.google.gson.Gson;
import com.hex.express.iwant.R;
import com.hex.express.iwant.iWantApplication;
import com.hex.express.iwant.bean.CourierWalletBean;
import com.hex.express.iwant.constance.MCUrl;
import com.hex.express.iwant.constance.PreferenceConstants;
import com.hex.express.iwant.http.AsyncHttpUtils;
import com.hex.express.iwant.http.UrlMap;
import com.hex.express.iwant.utils.PreferencesUtils;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 
 * 【我的钱包】——【用户】
 * @author han
 *
 */
public class UserWalletActivity extends BaseActivity {
	@Bind(R.id.text_title)
	TextView text_title;

	@Bind(R.id.tv_yestodayRecieveMoney)
	TextView tv_yestodayRecieveMoney;

	@Bind(R.id.tv_validBalance)
	TextView tv_validBalance;
	
	@Bind(R.id.tv_sevendayPercent)
	TextView tv_sevendayPercent;
	
	@Bind(R.id.tv_interestEveryTenThousand)
	TextView tv_interestEveryTenThousand;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_user_wallet);
		ButterKnife.bind(UserWalletActivity.this);
		iWantApplication.getInstance().addActivity(this);
		ButterKnife.bind(this);
		getData();
		initData();
	}

	@Override
	public void onWeightClick(View v) {
		// TODO Auto-generated method stub

	}

	@Override
	public void initView() {
		// TODO Auto-generated method stub

	}

	@OnClick({ R.id.back_userWallet, R.id.btn_userWalletDetail, R.id.img_help, R.id.btn_deposit, R.id.btn_transfer })
	public void MyOnlick(View view) {
		Intent intent = new Intent();
		switch (view.getId()) {
		case R.id.back_userWallet://返回键；
			finish();
			break;
		case R.id.btn_userWalletDetail://我的钱包流水详情；
			startActivity(intent.setClass(UserWalletActivity.this, MyWalletActivity.class));
			break;
		case R.id.img_help:
			startActivity(intent.setClass(UserWalletActivity.this, PreferentialActivity.class));
			break;
		case R.id.btn_deposit:
			startActivity(intent.setClass(UserWalletActivity.this, DepositActivity.class));
			break;
		case R.id.btn_transfer:
			startActivity(intent.setClass(UserWalletActivity.this, TransferActivity.class));
			break;

		default:
			break;
		}
	}

	@Override
	public void initData() {
		text_title.setText("我的钱包");

	}

	@Override
	public void setOnClick() {
		// TODO Auto-generated method stub

	}

	@Override
	public void getData() {
		// TODO Auto-generated method stub
		getUserWalletInfo();
	}
	
	/**
	 * 获取钱包信息；
	 */
	private void getUserWalletInfo() {
		RequestParams params = new RequestParams();
		Log.e("json", UrlMap.getUrl(MCUrl.WALLETINFO, "userId", String
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
						CourierWalletBean bean = new Gson().fromJson(
								new String(arg2), CourierWalletBean.class); 
						DecimalFormat df = new DecimalFormat("######0.00");
//						text_blance.setText(df.format(bean.data.get(0).balance));

						tv_yestodayRecieveMoney.setText(df.format(Double.parseDouble(bean.data.get(0).yestodayReceiveMoney)));
						tv_validBalance.setText(df.format(Double.parseDouble(bean.data.get(0).validBalance)));
						tv_sevendayPercent.setText(bean.data.get(0).sevendayPercent);
						tv_interestEveryTenThousand.setText(bean.data.get(0).interestEveryTenThousand);
						
					}

				});
	}
	

}
