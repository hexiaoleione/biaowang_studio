package com.hex.express.iwant.newactivity;

import java.util.HashSet;
import java.util.Set;

import org.apache.http.Header;

import com.google.gson.Gson;
import com.hex.express.iwant.R;
import com.hex.express.iwant.iWantApplication;
import com.hex.express.iwant.activities.BaseActivity;
import com.hex.express.iwant.activities.CommercialActivity;
import com.hex.express.iwant.activities.HAdvertActivity;
import com.hex.express.iwant.activities.RechargeActivity;
import com.hex.express.iwant.bean.InsuranceBean;
import com.hex.express.iwant.bean.RegisterBean;
import com.hex.express.iwant.constance.MCUrl;
import com.hex.express.iwant.constance.PreferenceConstants;
import com.hex.express.iwant.http.AsyncHttpUtils;
import com.hex.express.iwant.http.UrlMap;
import com.hex.express.iwant.utils.IDUtils;
import com.hex.express.iwant.utils.Logger;
import com.hex.express.iwant.utils.PreferencesUtils;
import com.hex.express.iwant.utils.ToastUtil;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.Bind;
import butterknife.ButterKnife;
import cn.jpush.android.api.JPushInterface;

public class InsuranceseActivity  extends BaseActivity{
	@Bind(R.id.btnLeft)
	ImageView btnleft;//返回键
	@Bind(R.id.btnRight)
	TextView btnRight;
	@Bind(R.id.btntuinsu)
	Button btntuinsu;// 

	@Bind(R.id.et_name)
	EditText et_name;// 

	@Bind(R.id.et_idcard)
	EditText et_idcard;// 
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.loginsurancese_activity);//loginsurance_activity
		iWantApplication.getInstance().addActivity(this);
		 ButterKnife.bind(this);
		 initView();
		 initData();
		 setOnClick();
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
		
	}

	@Override
	public void setOnClick() {
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
					intent.setClass(InsuranceseActivity.this, HAdvertActivity.class);//公司
					startActivity(intent);
			}
		});
		btntuinsu.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Builder ad = new Builder(InsuranceseActivity.this);
				ad.setTitle("温馨提示");
				ad.setMessage("购买后保险将在5-7个工作日内生效生效后开始扣费");
				
				ad.setPositiveButton("确认", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface arg0, int arg1) {
//						getHttpMessages(true, false, 1, false);
						Advertse();
					}
				});
				ad.setNegativeButton("取消", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface arg0, int arg1) {
						arg0.dismiss();
						
					}
				});
				ad.create().show();
			
			}
		});
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
		AsyncHttpUtils.doGet(
				UrlMap.getUrl(MCUrl.BALANCE, "id",
						String.valueOf(PreferencesUtils.getInt(getApplicationContext(), PreferenceConstants.UID))),
				null, null, params, new AsyncHttpResponseHandler() {

					@Override
					public void onFailure(int arg0, Header[] arg1, byte[] arg2, Throwable arg3) {
					}
					@Override
					public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
						// TODO Auto-generated method stub
						Logger.e("json", "" + new String(arg2));
						
						RegisterBean bean = new Gson().fromJson(new String(arg2), RegisterBean.class);
						if (bean.getErrCode()==0) {
							try {
								et_name.setText(bean.data.get(0).userName);
								et_idcard.setText(""+bean.data.get(0).idCard);
							} catch (Exception e) {
								// TODO: handle exception
							}
							
						
					}
				
						
					}

				});
	}
	public void Advertse(){//MCUrl.LoginCoun
		if ("".equals(et_name.getText().toString())) {
			ToastUtil.shortToast(InsuranceseActivity.this, "姓名不能为空");
			return;
		}
		if ("".equals(et_idcard.getText().toString())) {
			ToastUtil.shortToast(InsuranceseActivity.this, "身份证不能为空");
			return;
		}
		String  idcard=et_idcard.getText().toString();
		if (IDUtils.IDCardValidate(idcard.toUpperCase()).equals("false")) {
			ToastUtil.shortToast(InsuranceseActivity.this, "请输入正确的身份证号");
			return;
		}
		AsyncHttpUtils.doSimGet(UrlMap.getfour(MCUrl.driveSafe, "userId", String
				.valueOf(PreferencesUtils.getInt(InsuranceseActivity.this,
						PreferenceConstants.UID)),"ifBuyInsure",""+1,"userName",et_name.getText().toString(),"idCard",et_idcard.getText().toString()), new AsyncHttpResponseHandler() {
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
					ToastUtil.shortToast(InsuranceseActivity.this, lonmber.getMessage());
				}
			}
		});
	}

}
