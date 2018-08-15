package com.hex.express.iwant.activities;

import java.io.File;
import java.text.DecimalFormat;
import java.util.HashSet;
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
import com.hex.express.iwant.bean.CrowdCheckBean;
import com.hex.express.iwant.bean.RegisterBean;
import com.hex.express.iwant.bean.UserLevelBean;
import com.hex.express.iwant.constance.MCUrl;
import com.hex.express.iwant.constance.PreferenceConstants;
import com.hex.express.iwant.http.AsyncHttpUtils;
import com.hex.express.iwant.http.UrlMap;
import com.hex.express.iwant.utils.DataTools;
import com.hex.express.iwant.utils.PreferencesUtils;
import com.hex.express.iwant.utils.ToastUtil;
import com.hex.express.iwant.views.LoadingProgressDialog;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.sina.weibo.sdk.api.share.Base;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.PopupWindow.OnDismissListener;
import butterknife.Bind;
import butterknife.ButterKnife;
import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.TagAliasCallback;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.onekeyshare.OnekeyShare;

/**
 * 用户中心
 * 
 * @author Eric
 * 
 */
public class UserCenterActivity extends BaseActivity {
	private ImageView img_head,saomiao;
	private TextView txt_username;
	private TextView txt_balance;
	private ImageView btn_trake;
	private LinearLayout ll_card;
	private LinearLayout ll_wallet;
	private LinearLayout ll_ecoin;
	private LinearLayout ll_myexp;
	private LinearLayout ll_shunfeng;// 我的顺丰
	// private LinearLayout ll_mytasks;//我的任务
	private LinearLayout ll_realname;
	private LinearLayout youhuiquan;
	private ImageView btnRight;
	private ImageView btnLeft;
	private LinearLayout btn_share;
	private LinearLayout ll_about;
	private LinearLayout ll_feedback;
	private LinearLayout ll_shutdown;
	private DialogInterface.OnClickListener listener;
	private RegisterBean bean;
	private CrowdCheckBean bean2;
	private NetworkInfo info;
	@Bind(R.id.level)
	ImageView level;
	
	@Bind(R.id.ll_textlevel)
	LinearLayout ll_textlevel;
	LoadingProgressDialog dialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_usercenter);
		iWantApplication.getInstance().addActivity(this);
		ButterKnife.bind(UserCenterActivity.this);
		getData();
		initView();
		initData();
		setOnClick();
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		return super.onTouchEvent(event);
	}
//  @Override
//  	protected void onResume() {
//	// TODO Auto-generated method stub
//	super.onResume();
//	getData();
//	initView();
//	initData();
//  	}
	@Override
	public void onWeightClick(View v) {
		final Intent intent = new Intent();
		switch (v.getId()) {
		case R.id.ll_card:// 我的股份
//			ToastUtil.shortToast(UserCenterActivity.this, "此功能暂未开放");
			 if (info == null || !info.isConnected()) {
			 ToastUtil.shortToast(UserCenterActivity.this, "网络已断开");
			 } else {
			 if (bean2.getErrCode() == 0) {
			 intent.setClass(UserCenterActivity.this,
			 CrowdFundingActivity.class);
			 startActivity(intent);
			 } else {
			 intent.setClass(UserCenterActivity.this,
			 CrowdWelcomeActivity.class);
			 startActivity(intent);
			 }
			 }
			break;
		case R.id.ll_wallet:// 钱包
			// ToastUtil.shortToast(getBaseContext(), "钱包被点击,代表跳转钱包页面");
			 intent.setClass(UserCenterActivity.this, MyWalletActivity.class);
//			intent.setClass(UserCenterActivity.this, UserWalletActivity.class);
			startActivity(intent);
			break;
		case R.id.ll_ecoin:// 我的E币
			// ToastUtil.shortToast(UserCenterActivity.this, "此功能暂未开放");
			// intent.setClass(UserCenterActivity.this,
			// SendFreightActivity.class);
			intent.setClass(UserCenterActivity.this, EcoinActivity.class);
			startActivity(intent);
			break;
		// case R.id.ll_myexp:// 我的快递
		// // ToastUtil.shortToast(getBaseContext(), "我的快递被点击,代表跳转我的快递页面");
		// intent.setClass(UserCenterActivity.this, MyCourierActivity.class);
		// startActivity(intent);
		// break;
		case R.id.ll_shunfeng://
//			ToastUtil.shortToast(UserCenterActivity.this, "此功能暂未开放");
//			 intent.setClass(UserCenterActivity.this,
//			 MyDownwindActivity.class);
//			 startActivity(intent);

			 intent.setClass(UserCenterActivity.this,
					 MyExtentionActivity.class);//我的推广界面
			 startActivity(intent);
			break;
		// case R.id.ll_mytasks:// 我的任务
		// ToastUtil.shortToast(UserCenterActivity.this, "此功能暂未开放");
		// // ToastUtil.shortToast(getBaseContext(), "我的任务被点击,代表跳转我的任务页面");
		// // intent.setClass(UserCenterActivity.this,
		// // MyMissionActivity.class);
		// // startActivity(intent);
		// break;
		case R.id.ll_realname:// 实名认证
			// ToastUtil.shortToast(getBaseContext(), "实名认证被点击,代表跳转实名认证页面");
//			ToastUtil.shortToast(UserCenterActivity.this, "您是海南特约用户，不能进行此操作。");
//			showPaywindow();
			intent.putExtra("tiaoguo", "2");
			intent.setClass(UserCenterActivity.this, RoleAuthenticationActivity.class);
			startActivity(intent);
			break;
		// case R.id.ll_courier:// 快递员认证
		// // ToastUtil.shortToast(getBaseContext(), "暂未开放");
		// if (PreferencesUtils.getString(getApplicationContext(),
		// PreferenceConstants.USERTYPE).equals("1")) {
		// intent.setClass(UserCenterActivity.this,
		// CourierIdentificationActivity.class);
		// startActivity(intent);
		// } else if (PreferencesUtils.getString(getApplicationContext(),
		// PreferenceConstants.USERTYPE).equals("2")) {
		// intent.setClass(UserCenterActivity.this,
		// MyExtentionActivity.class);
		// startActivity(intent);
		// }
		//
		// break;
		case R.id.youhuiquan: // 现金券
			intent.setClass(UserCenterActivity.this, CardActivity.class);
			startActivity(intent);
//			showPaywindow();
			break;
		/*
		 * case R.id.btn_logoff:// 切换账户 intent.setClass(UserCenterActivity.this,
		 * LoginActivity.class); // startActivity(new
		 * Intent(UserCenterActivity.this, // LoginActivity.class));
		 * startActivity(intent);
		 * PreferencesUtils.clear(getApplicationContext());
		 * UserCenterActivity.this.finish(); break;
		 */
		case R.id.btn_recharge:// 充值
			// 充值之前先去完善个人资料
			if (!"Y".equals(PreferencesUtils.getString(getApplicationContext(), PreferenceConstants.REALMANAUTH))) {
				Builder ad = new Builder(UserCenterActivity.this);
				ad.setTitle("温馨提示");
				ad.setMessage("根据《中华人民共和国反恐怖主义法》规定，发快递须实名制，若您所填信息不真实或不完善，请尽快修改和完善，感谢您的配合。");
				ad.setNegativeButton("确认", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface arg0, int arg1) {
						startActivity(new Intent(UserCenterActivity.this,
								RegisterSetImageAndNameActivity.class).putExtra("tiaoguo", "2"));
					}
				});
				ad.setPositiveButton("取消", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface arg0, int arg1) {
						arg0.dismiss();
					}
				});
				ad.create().show();
			} else {
				intent.setClass(UserCenterActivity.this, RechargeActivity.class);
				startActivity(intent);
			}

			break;
		case R.id.img_head:// 个人信息
			// ToastUtil.shortToast(UserCenterActivity.this, "此功能暂未开放");
			intent.setClass(UserCenterActivity.this, RegisterSetImageAndNameActivity.class);
			
			intent.putExtra("tiaoguo", "2");
			startActivity(intent);
			break;
		case R.id.btnRight:// 信息
			startActivity(new Intent(UserCenterActivity.this, MessageActivity.class));
			break;
		case R.id.ll_about:// 关于
			startActivity(new Intent(UserCenterActivity.this, AboutActivity.class));
			break;
		case R.id.ll_feedback:// 意见反馈
			startActivity(new Intent(UserCenterActivity.this, FeedBackActivity.class));
			break;
		case R.id.ll_shutdown:// 退出登录
			Builder ad = new Builder(UserCenterActivity.this);
			ad.setTitle("退出登录");
			ad.setMessage("确认是否退出登录？");
			ad.setPositiveButton("确认", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int which) {
					finish();
					PreferencesUtils.clear(getApplicationContext());
					fileDelete("/sdcard/myHead/head.png");
					startActivity(new Intent(UserCenterActivity.this, MainActivity.class));
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
				}
			});
			ad.setNegativeButton("取消", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int which) {
					dialog.dismiss();
				}
			});
			ad.create().show();
			break;
		case R.id.btnLeft:// 返回
//			startActivity(new Intent(UserCenterActivity.this, MainActivity.class));
			finish();
			break;
		case R.id.btn_share:// 分享
			// showShare();
			startActivity(new Intent(UserCenterActivity.this, ShareActivity.class));
			break;
		}
	}

	@Override
	public void initView() {
		// 头像和用户名
		img_head = (ImageView) findViewById(R.id.img_head);
		txt_username = (TextView) findViewById(R.id.txt_username);
		// 余额
		txt_balance = (TextView) findViewById(R.id.txt_balance);
		// 充值按钮
		btn_trake = (ImageView) findViewById(R.id.btn_recharge);
		// 现金券 钱包 E币
		ll_card = (LinearLayout) findViewById(R.id.ll_card);
		ll_wallet = (LinearLayout) findViewById(R.id.ll_wallet);
		ll_ecoin = (LinearLayout) findViewById(R.id.ll_ecoin);
		// 订单管理
		// ll_myexp = (LinearLayout) findViewById(R.id.ll_myexp);
		ll_shunfeng = (LinearLayout) findViewById(R.id.ll_shunfeng);
		// ll_mytasks = (LinearLayout) findViewById(R.id.ll_mytasks);//我的任务
		// 认证大厅
		ll_realname = (LinearLayout) findViewById(R.id.ll_realname);
		youhuiquan = (LinearLayout) findViewById(R.id.youhuiquan);
		// 注销按钮
		// btn_logoff = (Button) findViewById(R.id.btn_logoff);
		btnRight = (ImageView) findViewById(R.id.btnRight);
		btnLeft = (ImageView) findViewById(R.id.btnLeft);
		// 设置
		ll_about = (LinearLayout) findViewById(R.id.ll_about);
		ll_feedback = (LinearLayout) findViewById(R.id.ll_feedback);
		ll_shutdown = (LinearLayout) findViewById(R.id.ll_shutdown);
		// 分享
		btn_share = (LinearLayout) findViewById(R.id.btn_share);
		saomiao= (ImageView) findViewById(R.id.saomiao);
	}

	@Override
	public void initData() {
		getLevel();
		getMessageStutas();
		ll_textlevel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// startActivity(new Intent(mActivity,
				// PreferentialActivity.class));

			}
		});
		ConnectivityManager manager = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
		info = manager.getActiveNetworkInfo();
		getrequstBalance();
		getrequsCrowd();
		saomiao.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				startActivityForResult(new Intent(UserCenterActivity.this, CaptureActivity.class), 1);	
			}
		});
	}

	/**
	 * 获取用户等级和评分
	 */
	private void getLevel() {
		AsyncHttpUtils.doGet(
				UrlMap.getUrl(MCUrl.USERCREDIT, "userId",
						String.valueOf(PreferencesUtils.getInt(getApplicationContext(), PreferenceConstants.UID))),
				null, null, null, new AsyncHttpResponseHandler() {
					private UserLevelBean bean3;
					@Override
					public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
						Log.e("USERCREDIT", new String(arg2));
						bean3 = new Gson().fromJson(new String(arg2), UserLevelBean.class);
						if (bean3.data.get(0).creditLV == 1) {
							level.setBackgroundResource(R.drawable.v1);
						} else if (bean3.data.get(0).creditLV == 2) {
							level.setBackgroundResource(R.drawable.v2);
						} else if (bean3.data.get(0).creditLV == 3) {
							level.setBackgroundResource(R.drawable.v3);
						}

					}

					@Override
					public void onFailure(int arg0, Header[] arg1, byte[] arg2, Throwable arg3) {
						// TODO Auto-generated method stub

					}
				});

	}

	/**
	 * 判断用户是否是众筹用户
	 */
	private void getrequsCrowd() {
		
		RequestParams params = new RequestParams();

		Log.e("json", MCUrl.CROWDCHECK + "/"
				+ String.valueOf(PreferencesUtils.getInt(getApplicationContext(), PreferenceConstants.UID)));

		AsyncHttpUtils.doGet(
				MCUrl.CROWDCHECK + "/"
						+ String.valueOf(PreferencesUtils.getInt(getApplicationContext(), PreferenceConstants.UID)),
				null, null, params, new AsyncHttpResponseHandler() {

					@Override
					public void onFailure(int arg0, Header[] arg1, byte[] arg2, Throwable arg3) {
						// ToastUtil.shortToast(baseContext, string)

					}

					@Override
					public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
						Log.e("jdflkdfjf", new String(arg2));
						bean2 = new Gson().fromJson(new String(arg2), CrowdCheckBean.class);

					}
				});
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
						txt_balance.setText(df.format(bean.data.get(0).balance));
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

	@Override
	public void setOnClick() {
		// 头像点击
		img_head.setOnClickListener(this);
		// 我的资产
		ll_card.setOnClickListener(this);
		ll_wallet.setOnClickListener(this);
		ll_ecoin.setOnClickListener(this);
		// 订单管理
		// ll_myexp.setOnClickListener(this);
		ll_shunfeng.setOnClickListener(this);
		// ll_mytasks.setOnClickListener(this);//我的任务
		// 认证中心
		ll_realname.setOnClickListener(this);
		youhuiquan.setOnClickListener(this);
		// 充值注销按钮
		btn_trake.setOnClickListener(this);
		btnLeft.setOnClickListener(this);
		btnRight.setOnClickListener(this);
		// 设置
		ll_about.setOnClickListener(this);
		ll_feedback.setOnClickListener(this);
		ll_shutdown.setOnClickListener(this);
		// 分享
		btn_share.setOnClickListener(this);
	}

	@Override
	public void getData() {
		dialog = new LoadingProgressDialog(this);
//		Log.e("11111DataTools  ", ""+DataTools.getDeviceId(UserCenterActivity.this));
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

	private void showShare() {
		ShareSDK.initSDK(this);
		OnekeyShare oks = new OnekeyShare();
		Log.e("ccc", "sss");
		// 关闭sso授权
		oks.disableSSOWhenAuthorize();
		// 分享时Notification的图标和文字 2.5.9以后的版本不调用此方法
		// oks.setNotification(R.drawable.ic_launcher,
		// getString(R.string.app_name));
		// title标题，印象笔记、邮箱、信息、微信、人人网和QQ空间使用
		oks.setTitle("我推荐镖王app");
		// text是分享文本，所有平台都需要这个字段
		oks.setText("来自我要的分享");

		// imagePath是图片的本地路径，Linked-In以外的平台都支持此参数
		oks.setImageUrl("http://www.efamax.com/images/5.png");// 确保SDcard下面存在此张图片
		// url仅在微信（包括好友和朋友圈）中使用
		oks.setUrl("https://itunes.apple.com/cn/app/e-fa/id1031426530?mt=8");
		// comment是我对这条分享的评论，仅在人人网和QQ空间使用
		// site是分享此内容的网站名称，仅在QQ空间使用
		oks.setSite(getString(R.string.app_name));
		// siteUrl是分享此内容的网站地址，仅在QQ空间使用
		oks.setSiteUrl("https://itunes.apple.com/cn/app/e-fa/id1031426530?mt=8");
		oks.setTitleUrl("https://itunes.apple.com/cn/app/e-fa/id1031426530?mt=8");
		// 启动分享GUI
		oks.show(this);
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
			AsyncHttpUtils.doPostJson(UserCenterActivity.this, MCUrl.THINKCHANGE, obj.toString(),
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
			ToastUtil.shortToast(UserCenterActivity.this, "二维码不正确");
		}

	}
	/**
	 * 提示信息
	 */
	private void showPaywindow() {
		final PopupWindow	window02;
		TextView btnsaves_pan,tet_tishi;
		// 利用layoutInflater获得View
		LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View view = inflater.inflate(R.layout.popwidndow_hainan, null);
		// 下面是两种方法得到宽度和高度 getWindow().getDecorView().getWidth()
		window02 = new PopupWindow(view, WindowManager.LayoutParams.MATCH_PARENT,
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
		window02.showAtLocation(UserCenterActivity.this.findViewById(R.id.ll_feedback), Gravity.CENTER, 0, 0);
		btnsaves_pan=(TextView) view.findViewById(R.id.btnsaves_pan);
		tet_tishi=(TextView) view.findViewById(R.id.tet_tishi);
		btnsaves_pan.setOnClickListener(new OnClickListener() {
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

			}
		});

	}
}
