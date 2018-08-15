package com.hex.express.iwant.activities;

import java.io.File;
import java.text.DecimalFormat;
import java.util.Set;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;
import org.madmatrix.zxing.android.CaptureActivity;

import com.google.gson.Gson;
import com.hex.express.iwant.R;
import com.hex.express.iwant.iWantApplication;
import com.hex.express.iwant.bean.BaseBean;
import com.hex.express.iwant.bean.CourierLevelBean;
import com.hex.express.iwant.bean.RegisterBean;
import com.hex.express.iwant.constance.MCUrl;
import com.hex.express.iwant.constance.PreferenceConstants;
import com.hex.express.iwant.homfragment.HomSubFragment1;
import com.hex.express.iwant.http.AsyncHttpUtils;
import com.hex.express.iwant.http.UrlMap;
import com.hex.express.iwant.newmain.MainTab;
import com.hex.express.iwant.utils.AppUtils;
import com.hex.express.iwant.utils.Logger;
import com.hex.express.iwant.utils.PreferencesUtils;
import com.hex.express.iwant.utils.ToastUtil;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.TagAliasCallback;

public class NewCenterActivity extends BaseActivity {

	@Bind(R.id.btnRight)
	 ImageView btnRight;
	@Bind(R.id.btnLeft)
	 ImageView btnLeft;
	@Bind(R.id.img_head)
	ImageView img_head;
	@Bind(R.id.txt_username)
	TextView txt_username;
	
	
	@Bind(R.id.saomiao)  //扫描
	LinearLayout saomiao;
	@Bind(R.id.btn_share)  //分享
	LinearLayout btn_share;
	
	@Bind(R.id.ll_wallet)  //钱包
	LinearLayout ll_wallet;
	@Bind(R.id.ll_shunfeng)
	LinearLayout ll_shunfeng;//人人代理
	@Bind(R.id.ll_realname)
	LinearLayout ll_realname;//角色
	@Bind(R.id.ll_function)
	LinearLayout ll_function;//功能介绍
	@Bind(R.id.ll_feedback)
	LinearLayout ll_feedback;//意见反馈
	@Bind(R.id.ll_about)
	LinearLayout ll_about;//关于
	@Bind(R.id.ll_tui)
	LinearLayout ll_tui;//退出
	
	@Bind(R.id.ll_shutdown)
	TextView ll_shutdown;// 退出
	@Bind(R.id.level)
	ImageView level;
	@Bind(R.id.level_rating)
	RatingBar level_rating;
	
	private RegisterBean bean;
	private NetworkInfo info;
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		iWantApplication.getInstance().addActivity(this);
		setContentView(R.layout.newcenter_activity);//
		ButterKnife.bind(NewCenterActivity.this);
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
		
	}

	@Override
	public void initData() {
		// TODO Auto-generated method stub
		getLevel();
		getMessageStutas();
		ConnectivityManager manager = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
		info = manager.getActiveNetworkInfo();
		getrequstBalance();
//		getrequsCrowd();
	}

	@Override
	public void setOnClick() {
		// TODO Auto-generated method stub
		level_rating.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View arg0, MotionEvent arg1) {
			return true;
			}
			});
	}
	@OnClick({R.id.btnRight,R.id.btnLeft, R.id.img_head, R.id.saomiao, R.id.btn_share ,R.id.ll_wallet,R.id.ll_shunfeng, R.id.ll_realname, R.id.ll_function, R.id.ll_feedback,
		R.id.ll_about,R.id.ll_tui,R.id.ll_phone,R.id.ll_caozuo})
	public void MyOnClick(View view) {
		Intent intent = null;
		Bundle bundle = new Bundle();
		switch (view.getId()) {
		case R.id.btnRight:// 
			startActivity(new Intent(NewCenterActivity.this, MessageActivity.class));
			break;
		case R.id.btnLeft://
			finish();
			break;
		case R.id.img_head://添加照片
			startActivity(new Intent(NewCenterActivity.this,
					RegisterSetImageAndNameActivity.class).putExtra("tiaoguo", "2"));
//			ceshi();
			break;
		case R.id.saomiao:// 
			startActivityForResult(new Intent(NewCenterActivity.this, CaptureActivity.class), 1);	
			break;
		case R.id.btn_share:// 
			startActivity(new Intent(NewCenterActivity.this, ShareActivity.class));
//			startActivityForResult(intent, 7);
			break;
		case R.id.ll_wallet:// 钱包  NewMyWalletActivity   MyWalletActivity
			startActivity(new Intent(NewCenterActivity.this, NewMyWalletActivity.class));
			break;
		case R.id.ll_shunfeng:// 人人代理
			startActivity(new Intent(NewCenterActivity.this, MyExtentionActivity.class));
			break;
		case R.id.ll_realname:// 实名认证
			Intent intents=new Intent();
			intents.putExtra("tiaoguo", "2");
			intents.setClass(NewCenterActivity.this, RoleAuthenticationActivity.class);
			startActivity(intents);
//			startActivity(new Intent(NewCenterActivity.this, RoleAuthenticationActivity.class));
			break;
		case R.id.ll_function:// 功能介绍
			
//			startActivity(new Intent(NewCenterActivity.this, FunctionActivity.class));
			break;
		case R.id.ll_feedback:// 意见反馈
			startActivity(new Intent(NewCenterActivity.this, FeedBackActivity.class));
			break;
		case R.id.ll_about:// 关于
			startActivity(new Intent(NewCenterActivity.this, AboutActivity.class));
			break;
		case R.id.ll_caozuo:// 操作指南
			startActivity(new Intent(NewCenterActivity.this, OperationActivity.class));
			break;
		case R.id.ll_phone:// 理赔电话
			AppUtils.intentDial(NewCenterActivity.this, "95509");
			break;
		case R.id.ll_tui:// 
			Builder ad = new Builder(NewCenterActivity.this);
			ad.setTitle("退出登录");
			ad.setMessage("确认是否退出登录？");
			ad.setPositiveButton("确认", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int which) {
					PreferencesUtils.clear(getApplicationContext());
//					PreferencesUtils.clearuid(getApplicationContext());
					fileDelete("/sdcard/myHead/head.png");
					startActivity(new Intent(NewCenterActivity.this, LoginWeixinActivity.class));
					ToastUtil.shortToast(getApplicationContext(), "退出成功");
					JPushInterface.setAliasAndTags(getApplicationContext(), "", null,new TagAliasCallback() {
						@Override
						public void gotResult(int i, String s, Set<String> set) {
							if (i == 0) {
								Log.d("Jpush", s);
							} else {
								Log.d("Jpush", "error code is " + i);
							}
						}
					});
					finish();
				}
			});
			ad.setNegativeButton("取消", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int which) {
					dialog.dismiss();
				}
			});
			ad.create().show();
			break;
		default:
			break;
		}
	}

	@Override
	public void getData() {
		// TODO Auto-generated method stub
		
	}

	private void fileDelete(String path) {
		if (fileIsExist(path)) {
			File file = new File(path);
			file.delete();
		}
	}

	private boolean fileIsExist(String path) {
		File file = new File(path);
		if (file.exists()) {
			return true;
		}
		return false;
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
						Log.e("json", "" + new String(arg2));
						bean = new Gson().fromJson(new String(arg2), RegisterBean.class);
						if (bean.getData() == null || bean.getData().size() == 0)
							return;
						// double类型保留小数点位数
						DecimalFormat df = new DecimalFormat("######0.00");
//						txt_balance.setText(df.format(bean.data.get(0).balance));
						if (!bean.data.get(0).userName.equals("")&&!bean.data.get(0).userName.equals("null")) {
							txt_username.setText(bean.data.get(0).userName);
						} else {
							txt_username.setText(
									PreferencesUtils.getString(getApplicationContext(), PreferenceConstants.MOBILE));
						}
						PreferencesUtils.putString(getApplicationContext(), PreferenceConstants.HeadPath, bean.getData().get(0).headPath);
						PreferencesUtils.putString(getApplicationContext(), PreferenceConstants.ICON_PATH,bean.data.get(0).idCardPath);
					    PreferencesUtils.putString(getApplicationContext(), PreferenceConstants.USERTYPE,bean.getData().get(0).userType);
					    PreferencesUtils.putString(getApplicationContext(), PreferenceConstants.WLID, bean.getData().get(0).wlid);
						 PreferencesUtils.putString(getApplicationContext(), PreferenceConstants.AgreementType,bean.getData().get(0).agreementType);

					    if (!bean.data.get(0).headPath.equals("") && bean.data.get(0).headPath!=null) {
								loader.displayImage(bean.data.get(0).headPath, img_head, options);
								// new MyBitmapUtils().display(img_head,
								// bean.data.get(0).headPath);
							} else {
								img_head.setBackgroundResource(R.drawable.ren3);
							}

					}

				});
	}
	/**
	 * 信息是否有未读的状态
	 */
	private void getMessageStutas() {
		RequestParams params = new RequestParams();
		Log.e("mdgread", UrlMap.getUrl(MCUrl.GETUNREADSYSMSG, "userId",
				String.valueOf(PreferencesUtils.getInt(getApplicationContext(), PreferenceConstants.UID))));
		AsyncHttpUtils.doGet(
				UrlMap.getUrl(MCUrl.GETUNREADSYSMSG, "userId",
						String.valueOf(PreferencesUtils.getInt(getApplicationContext(), PreferenceConstants.UID))),
				null, null, params, new AsyncHttpResponseHandler() {

					@Override
					public void onFailure(int arg0, Header[] arg1, byte[] arg2, Throwable arg3) {

					}

					@Override
					public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
						// TODO Auto-generated method stub
						Log.e("message", "" + new String(arg2));
						BaseBean bean = new Gson().fromJson(new String(arg2), BaseBean.class);
						if (bean.getErrCode() > 0) {
							btnRight.setBackgroundResource(R.drawable.info_show);
						} else if (bean.getErrCode() == -2) {
							btnRight.setBackgroundResource(R.drawable.mess);
						}
					}

				});
	}
	/**
	 * 获取用户等级和评分
	 */
	private void getLevel() {
		AsyncHttpUtils.doGet(
				UrlMap.getUrl(MCUrl.COURIERSCOREANDLV, "userId",
						String.valueOf(PreferencesUtils.getInt(getApplicationContext(), PreferenceConstants.UID))),
				null, null, null, new AsyncHttpResponseHandler() {

					private CourierLevelBean bean3;

					@Override
					public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
						Log.e("111111111   ", new String(arg2));
						bean3 = new Gson().fromJson(new String(arg2), CourierLevelBean.class);
						if (bean3.data.size() > 0 && bean3.data != null) {
							if (bean3.data.get(0).allowanceLevel == 1) {
								level.setBackgroundResource(R.drawable.v1);
							} else if (bean3.data.get(0).allowanceLevel == 2) {
								level.setBackgroundResource(R.drawable.v2);
							} else if (bean3.data.get(0).allowanceLevel == 3) {
								level.setBackgroundResource(R.drawable.v3);
							}
							level_rating.setRating(bean3.data.get(0).pickupScore / 2);
						}

					}

					@Override
					public void onFailure(int arg0, Header[] arg1, byte[] arg2, Throwable arg3) {
						// TODO Auto-generated method stub

					}
				});

	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		Log.e("requestCode     ",""+requestCode);
		Log.e("resultCode     ",""+resultCode);
		Log.e("data     ",""+data);
		switch (requestCode) {
		
		case 1:
			if (requestCode == 1 && data != null) {
				Bundle bundle = data.getExtras();
				
				Log.e("111111111     ",""+bundle.getString("result"));
				Log.e("111111111     ",""+bundle.getString("errCode"));
				Log.e("111111111     ",""+bundle.getBundle("success"));
//				codeId = bundle.getString("codeId");
//				phone= bundle.getString("phone");
			     if(!bundle.getString("result").equals("") && bundle.getString("result")!=null){
			    	 getLlsurplus(bundle.getString("result"));
			     }
				
			}
			break;
		default:
			break;
		}
	}
	/**
	 * 二维码登录
	 */
	private void getLlsurplus(String codeid) {
//		if (!tv_needPayMoney.getText().toString().equals("")) {
		String str = codeid;   
		boolean isNum = str.matches("[0-9]+");  
		if (isNum) {
	     	dialog.show();
			JSONObject obj = new JSONObject();
			try {
				obj.put("codeId", codeid);
				obj.put("idCard", PreferencesUtils.getString(getApplicationContext(), PreferenceConstants.IDCARD));
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			Log.e("1111111 mlist    ", obj.toString());
			AsyncHttpUtils.doPostJson(NewCenterActivity.this, MCUrl.THINKCHANGE, obj.toString(),
					new AsyncHttpResponseHandler() {
						@Override
						public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
							Log.e("josnnd", new String(arg2));
							dialog.dismiss();
							BaseBean bean = new Gson().fromJson(new String(arg2), BaseBean.class);
								ToastUtil.shortToast(getApplicationContext(), bean.getMessage());
						}
						@Override
						public void onFailure(int arg0, Header[] arg1, byte[] arg2, Throwable arg3) {
							// TODO Auto-generated method stub
						}
					});
		} else {
			ToastUtil.shortToast(NewCenterActivity.this, "二维码不正确");
		}

	}
}
