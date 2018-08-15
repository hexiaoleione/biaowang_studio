package com.hex.express.iwant.newactivity;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.gson.Gson;
import com.hex.express.iwant.R;
import com.hex.express.iwant.iWantApplication;
import com.hex.express.iwant.activities.BaseActivity;
import com.hex.express.iwant.activities.HAdvertActivity;
import com.hex.express.iwant.activities.NewMyWalletActivity;
import com.hex.express.iwant.bean.BaseBean;
import com.hex.express.iwant.bean.DeposBean;
import com.hex.express.iwant.bean.RegisBean;
import com.hex.express.iwant.constance.MCUrl;
import com.hex.express.iwant.constance.PreferenceConstants;
import com.hex.express.iwant.http.AsyncHttpUtils;
import com.hex.express.iwant.http.UrlMap;
import com.hex.express.iwant.mypasspay.DepositPassActivity;
import com.hex.express.iwant.newsmain.NewMainActivity;
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
import android.widget.LinearLayout;
import android.widget.TextView;
import butterknife.Bind;
import butterknife.ButterKnife;
/**
 * 
 * 退交押金界面
*/

public class DepositToActivity  extends BaseActivity{
	
	@Bind(R.id.btnLeft)
	ImageView btnleft;//返回键=
	@Bind(R.id.btnRight)
	TextView btnRight;
	@Bind(R.id.money)
	TextView money;
	@Bind(R.id.yay)
	TextView yay;
	
	
	
	
	@Bind(R.id.ly_n)
	LinearLayout ly_n;
	@Bind(R.id.ly_p)
	LinearLayout ly_p;
	
	@Bind(R.id.edit_aliPayNickName)
	EditText edit_aliPayNickName;
	@Bind(R.id.edit_aliPayAccoount)
	EditText edit_aliPayAccoount;
	
	@Bind(R.id.btntui)
	Button btntui;
	@Bind(R.id.btnsumt)
	Button btnsumt;
	@Bind(R.id.btntuizhong)
	Button btntuizhong;
	DeposBean bean;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.deposintto_activity);
		iWantApplication.getInstance().addActivity(this);
		ButterKnife.bind(DepositToActivity.this);
		getData();
		initView();
		initData();
		
		getrequstBalancese();
		setOnClick();
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
				 String url="http://www.efamax.com/mobile/explain/marginExplain.html";
				    Intent intent=new Intent();
					intent.putExtra("url", url);
					intent.setClass(DepositToActivity.this, HAdvertActivity.class);//公司
					startActivity(intent);
			}
		});
		btntui.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				ly_n.setVisibility(View.VISIBLE);
				ly_p.setVisibility(View.VISIBLE);
				btnsumt.setVisibility(View.VISIBLE);	
				btntui.setVisibility(View.GONE);
				btntuizhong.setVisibility(View.GONE);
			}
		});
		btnsumt.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub driver/driverRefund
				PostHttpRequst();
			}
		});
	}

	@Override
	public void getData() {
		// TODO Auto-generated method stub
		
	}
	private void getrequstBalancese() {
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
						 bean = new Gson().fromJson(
								new String(arg2), DeposBean.class); 
						if (bean.getErrCode()==0) {
							money.setText("¥  "+bean.getData().get(0).money+"  元");
							// 镖师押金     0 默认      1    已充值    2  退款中   3  已退款
							if (bean.getData().get(0).driverMoney.equals("1")) {
								
								ly_n.setVisibility(View.GONE);
								ly_p.setVisibility(View.GONE);
								btnsumt.setVisibility(View.GONE);
								btntui.setVisibility(View.VISIBLE);
								btntuizhong.setVisibility(View.GONE);
							}else if (bean.getData().get(0).driverMoney.equals("2")) {
								ly_n.setVisibility(View.GONE);
								ly_p.setVisibility(View.GONE);
								btnsumt.setVisibility(View.GONE);
								btntui.setVisibility(View.GONE);
								btntuizhong.setVisibility(View.VISIBLE);
								yay.setText("退款中");
							}
						
						}else {
							ToastUtil.shortToast(DepositToActivity.this,bean.getMessage());
						}
					}

				});
	}
	/**
	 * 提现申请
	 */
	private void PostHttpRequst() {
			JSONObject obj = new JSONObject();
			if ("".equals(edit_aliPayNickName.getText().toString()) || "".equals(edit_aliPayAccoount.getText().toString())) {
				ToastUtil.shortToast(DepositToActivity.this, "请填写信息");
				return;
			}
			try {
				// 提现操作需要的参数:
				obj.put("applyMoney", bean.getData().get(0).money);
				obj.put("aliPayNickName", edit_aliPayNickName.getText().toString());
				obj.put("aliPayAccount", edit_aliPayAccoount.getText().toString());
				obj.put("userId", Integer.valueOf(PreferencesUtils.getInt(
						getApplicationContext(), PreferenceConstants.UID)));
				Log.e("shuju", obj.toString());
			} catch (JSONException e) {
				e.printStackTrace();
			}
			AsyncHttpUtils.doPostJson(DepositToActivity.this, MCUrl.driverRefund,
					obj.toString(), new AsyncHttpResponseHandler() {

						@Override
						public void onFailure(int arg0, Header[] arg1,
								byte[] arg2, Throwable arg3) {
							Log.e("json", new String(arg2));
						}
						@Override
						public void onSuccess(int arg0, Header[] arg1,
								byte[] arg2) {
							Log.e("json", new String(arg2));
						
							BaseBean bean = new Gson().fromJson(
									new String(arg2), BaseBean.class);
							if (bean.isSuccess()) {							
//												Intent intent = new Intent(DepositToActivity.this, NewMainActivity.class);	
//												startActivity(intent);
//												
								ToastUtil.shortToast(DepositToActivity.this,bean.getMessage());
								finish();
								
							} else {
								ToastUtil.shortToast(DepositToActivity.this,bean.getMessage());
								finish();
							}
						}

					});
		}


}
