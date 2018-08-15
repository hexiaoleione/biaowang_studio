package com.hex.express.iwant.newactivity;

import org.apache.http.Header;

import com.google.gson.Gson;
import com.hex.express.iwant.R;
import com.hex.express.iwant.iWantApplication;
import com.hex.express.iwant.activities.BaseActivity;
import com.hex.express.iwant.activities.DepositActivity;
import com.hex.express.iwant.activities.HAdvertActivity;
import com.hex.express.iwant.activities.LogiNumberActivity;
import com.hex.express.iwant.bean.GuardBean;
import com.hex.express.iwant.bean.InsuranceBean;
import com.hex.express.iwant.bean.Loginuberbean;
import com.hex.express.iwant.constance.MCUrl;
import com.hex.express.iwant.constance.PreferenceConstants;
import com.hex.express.iwant.http.AsyncHttpUtils;
import com.hex.express.iwant.http.UrlMap;
import com.hex.express.iwant.newsmain.NewMainActivity;
import com.hex.express.iwant.utils.PreferencesUtils;
import com.hex.express.iwant.utils.ToastUtil;
import com.loopj.android.http.AsyncHttpResponseHandler;

import android.R.integer;
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

/**
 * 
 * @author huyichuan
 * 人身保险
 *
 */
public class InsuranceActivity extends BaseActivity{
	@Bind(R.id.btnLeft)
	ImageView btnleft;//返回键
	@Bind(R.id.btnRight)
	TextView btnRight;
	@Bind(R.id.btntuinsu)
	Button btntuinsu;// 
	@Bind(R.id.btntuinsure)
	Button btntuinsure;// 
	@Bind(R.id.yayre)
	TextView yayre;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.loginsurance_activity);//loginsurance_activity
		iWantApplication.getInstance().addActivity(this);
		 ButterKnife.bind(this);
		 initView();
		 initData();
		 setOnClick();
		 driveInfo();
	}
	
	
	@Override
	public void onWeightClick(View v) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void initView() {
		
		// TODO Auto-generated method stub
		btnleft.setOnClickListener(new OnClickListener() {
			
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
				
				   String url="http://www.efamax.com/mobile/explain/driverSafeExplain.html";
				    Intent intent=new Intent();
					intent.putExtra("url", url);
					intent.setClass(InsuranceActivity.this, HAdvertActivity.class);//公司
					startActivity(intent);
			}
		});
		//driver/driveSafe

		
		btntuinsu.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
//				Advertse(1);
				  Intent intent=new Intent();
					intent.setClass(InsuranceActivity.this, InsuranceseActivity.class);//公司
					startActivity(intent);
					finish();
			}
		});
		btntuinsure.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
//				Advertse(2);
				Advertsed();
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
	public void driveInfo(){//MCUrl.  driver/driveInfo
		AsyncHttpUtils.doSimGet(UrlMap.getUrl(MCUrl.driveInfo, "userId", String
				.valueOf(PreferencesUtils.getInt(InsuranceActivity.this,
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
					if ("2".equals(guardBean.getData().get(0).ifBuyInsure)) {
						yayre.setVisibility(View.GONE);
						btntuinsure.setVisibility(View.GONE);
					}
				}else {
					ToastUtil.shortToast(InsuranceActivity.this, guardBean.getMessage());
				}
			}
		});
	}
	//driver/driveInfo
	public void Advertse(int   ifBuyInsure){//MCUrl.LoginCoun
		AsyncHttpUtils.doSimGet(UrlMap.getTwo(MCUrl.driveSafe, "userId", String
				.valueOf(PreferencesUtils.getInt(InsuranceActivity.this,
						PreferenceConstants.UID)),"ifBuyInsure",""+ifBuyInsure), new AsyncHttpResponseHandler() {
			@Override
			public void onFailure(int arg0, Header[] arg1, byte[] arg2, Throwable arg3) {
				
			}
			@Override
			public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
				Log.e("111111se", new String(arg2));
				InsuranceBean lonmber = new Gson().fromJson(new String(arg2), InsuranceBean.class);
				if (lonmber.getErrCode()==0) {
//					finish();
					
				}else {
					ToastUtil.shortToast(InsuranceActivity.this, lonmber.getMessage());
				}
			}
		});
	}
	public void Advertsed(){//MCUrl.LoginCoun
		AsyncHttpUtils.doSimGet(UrlMap.getfour(MCUrl.driveSafe, "userId", String
				.valueOf(PreferencesUtils.getInt(InsuranceActivity.this,
						PreferenceConstants.UID)),"ifBuyInsure",""+2,"userName","","idCard",""), new AsyncHttpResponseHandler() {
			@Override
			public void onFailure(int arg0, Header[] arg1, byte[] arg2, Throwable arg3) {
				
			}
			@Override
			public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
				Log.e("111111se", new String(arg2));
				InsuranceBean lonmber = new Gson().fromJson(new String(arg2), InsuranceBean.class);
				if (lonmber.getErrCode()==0) {
					finish();
				}else {
					ToastUtil.shortToast(InsuranceActivity.this, lonmber.getMessage());
				}
			}
		});
	}
}
