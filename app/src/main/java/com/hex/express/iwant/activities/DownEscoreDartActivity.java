package com.hex.express.iwant.activities;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URISyntaxException;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.google.gson.Gson;
import com.hex.express.iwant.R;
import com.hex.express.iwant.iWantApplication;
import com.hex.express.iwant.adapters.SelectListAdapter;
import com.hex.express.iwant.adapters.SelectListAdapter.OnDelBtnClickListener;
import com.hex.express.iwant.adapters.SelectListAdapter.OnItemClickListener;
import com.hex.express.iwant.bean.BaseBean;
import com.hex.express.iwant.bean.HaiBean;
import com.hex.express.iwant.bean.IconBean;
import com.hex.express.iwant.bean.LogisBean;
import com.hex.express.iwant.bean.RegisterBean;
import com.hex.express.iwant.constance.MCUrl;
import com.hex.express.iwant.constance.MsgConstants;
import com.hex.express.iwant.constance.PreferenceConstants;
import com.hex.express.iwant.http.AsyncHttpUtils;
import com.hex.express.iwant.http.UrlMap;
import com.hex.express.iwant.mypasspay.DownWindPassActivity;
import com.hex.express.iwant.utils.AppUtils;
import com.hex.express.iwant.utils.DataTools;
import com.hex.express.iwant.utils.Logger;
import com.hex.express.iwant.utils.PreferencesUtils;
import com.hex.express.iwant.utils.StringUtil;
import com.hex.express.iwant.utils.ToastUtil;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import android.R.bool;
import android.R.integer;
import android.R.string;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Bitmap.Config;
import android.graphics.PorterDuff.Mode;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Message;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.LinearLayout.LayoutParams;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 【镖件详情】
 * 
 * @author SCHT-50
 * 
 */
public class DownEscoreDartActivity extends BaseActivity implements OnClickListener,  OnItemClickListener, OnDelBtnClickListener, OnDismissListener  {
	public static DownEscoreDartActivity instance;//实例，在其他页面销毁此Activity的时候进行调用。
	private View vPopupWindow_recceivedConfirm, vPopupWindow_deliveredConfirm,vPopupWindow_report,vPopupWindow_complain;
	private PopupWindow popupWindow_recceivedConfirm;
	private PopupWindow popupWindow_deliveredConfirm;
	private PopupWindow popupWindow_report;
	private PopupWindow popupWindow_complain;
	private Button btnCancel, btnOK;
	@Bind(R.id.text_title)
	TextView text_title;
	@Bind(R.id.tv_cancle) // 取消订单
	TextView tv_cancle;
	// 发件人
	@Bind(R.id.text_time)
	TextView text_time;
	@Bind(R.id.text_name)
	TextView text_name;
//	@Bind(R.id.text_phone)
//	TextView text_phone;
	@Bind(R.id.text_address)
	TextView text_address;
	// 收件人
	@Bind(R.id.text_name_to)
	TextView text_name_to;
//	@Bind(R.id.text_phone_to)
//	TextView text_phone_to;
	@Bind(R.id.text_address_to)
	TextView text_address_to;
	@Bind(R.id.btn_submit)
	TextView btn_submit;
	@Bind(R.id.btn_escort)
	TextView btn_escort;
	@Bind(R.id.btn_escortno)
	TextView btn_escortno;
	
	@Bind(R.id.iv_flag)
	ImageView iv_flag;

	@Bind(R.id.text_weights)
	TextView text_weights;
	// 货物备注；
	@Bind(R.id.tv_matRemark)
	TextView tv_matRemark;
	@Bind(R.id.text_goodName)
	TextView text_goodName;
	@Bind(R.id.text_goodMoney)
	TextView text_goodMoney;
	@Bind(R.id.text_goodMessage)
	TextView text_goodMessage;
	@Bind(R.id.text_goodMessagese)
	TextView text_goodMessagese;
	
	@Bind(R.id.carchang)
	TextView carchang;
	@Bind(R.id.tijis)
	TextView tijis;
	
	@Bind(R.id.louts)
	LinearLayout loutsLayout;
	@Bind(R.id.text_finishTime_to)
	TextView text_finishTime_to;
	@Bind(R.id.tv_limtime)
	TextView tv_limtime;
	
	@Bind(R.id.dart_xuanze)  //投诉和货物违规
	LinearLayout dart_xuanze;
	@Bind(R.id.dart_ok)  //确定到达
	RelativeLayout dart_ok;
	@Bind(R.id.btn_tousu)// 投诉按钮
	Button btn_tousu;
	@Bind(R.id.btn_weigui)//违规按钮
	TextView btn_weigui;
	@Bind(R.id.btn_jiuwei)//就位按钮
	TextView btn_jiuwei;
	@Bind(R.id.replaceMoney)//代收
	TextView replaceMoney;
	@Bind(R.id.cartype)//代收
	TextView cartype;
	@Bind(R.id.usertime)//shijian
	TextView usertime;
	@Bind(R.id.text_juli)//shijian
	TextView text_juli;
	@Bind(R.id.text_jian)//shijian
	TextView text_jian;
	
	
	@Bind(R.id.btn_ifReplace)// shouk按钮
	Button btn_ifReplace;
	
	@Bind(R.id.qujian)
	ImageView qujian;
	@Bind(R.id.songjian)
	ImageView songjian;
	
	@Bind(R.id.lyoucarb)
	LinearLayout lyoucarb;
	
	
	private ImageView btn_image;
	private Button	btn_immediately;
	private Button	btn_no_immediatel;
	
	private TextView exit;
	private TextView exit_re;
	private EditText	edit_comp;
	private ImageView	btn_comp_image;
	private Button	btn_comp_sumit;
	
	private LinearLayout ll_setChecked;
	private CheckBox cb_checkedReceive;
	private Button btn_received;
	private boolean isFinished = false;// 是否押镖完成,false表示可点击，证明押镖未完成状态；true表示不可点击，证明是押镖完成；
	// 当前位置经纬度
	private double latitude;
	private double longitude;
	private String saddse;
	private String city;
	private LocationClient client;
	
	private Bitmap head;
	private String fileName = path + "dart.png";
	private static String path = "/sdcard/myHead/";// sd路径
	private Map map = new HashMap<String, String>();
	private Map map_file = new HashMap<String, File>();
	private Bitmap head1;
	private String fileName1 = path + "comp.png";
	private String result;
	private String result1;
	private String icon="";
	private String icon1="";
	private boolean flag;
	private boolean frist = false;
	private IconBean bean;
	/**
	 * 【SendEscortFragment】界面传来的值
	 */
	private String recId;
	private String publishTime;
	private String personName;
	private String mobile;
	private String address;
	private String personNameTo;
	private String mobileTo;
	private String addressTo;
	private String matRemark;
	private String status;// 镖件的状态 0-预发布成功(未支付) 1-支付成功(已支付) 2(已抢单) 3 已取件 4
							// 订单取消()镖师 5 成功 6 删除 7 已评价 8订单取消（用户）

	private String password;
	private String readyTime;
	private RelativeLayout mInputLayout;
	private ImageButton mArrow;
	private TextView mInput;
	private PopupWindow mSelectWindow;
	private LayoutInflater mInflater;
	private ArrayList<String> mAccounts;
	
	HaiBean haibean1;
	LogisBean Logisbean;
	boolean carnumber=false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.sactivity_downe_newdart);//activity_downescort_newdart activity_downescort_dart
		ButterKnife.bind(this);
		mInputLayout=(RelativeLayout) findViewById(R.id.input_layout);
		mArrow = (ImageButton) findViewById(R.id.input_arrow);
		mInput = (TextView) findViewById(R.id.input_et);
		initData();
		initView();
		setOnClickListener();
		setOnClick();
	}

	@Override
	public void onWeightClick(View v) {
		// TODO Auto-generated method stub

	}

	@Override
	public void initView() {
		// TODO Auto-generated method stub
		showPopupWindow_receivedConfirm();
		showvPopupWindow_deliveredConfirm();
		
//		dart_xuanze.setVisibility(visibility);
	}

	@OnClick({ R.id.btnLeft, R.id.tv_cancle, R.id.img_phone, R.id.img_phone_to, R.id.img_address, R.id.btn_submit,
			R.id.btn_escort })
	public void MyOnclick(View view) {
		switch (view.getId()) {
		case R.id.btnLeft:
			finish();// 销毁此页面,并去启动上一个页面的相应的Fragment
//			startActivity(
//					new Intent(getApplicationContext(), MyDownwindActivity.class).putExtra("loadIndex", "Escort"));
			Log.e("--------", "-----------");
			break;
		case R.id.tv_cancle:// 取消订单
			Log.e("recId是——————————————————————", recId + "=========");
			// GET方法
			Builder ad = new Builder(DownEscoreDartActivity.this);
			ad.setTitle("温馨提示");
			ad.setMessage("取消订单将罚款补偿给用户");
			ad.setPositiveButton("确认", new OnClickListener() {
				@Override
				public void onClick(DialogInterface arg0, int arg1) {

					dialog.show();//DiverCance  DRIVERREFUNDREIMBURSE
					AsyncHttpUtils.doSimGet(UrlMap.getTwo(MCUrl.DiverCance, "recId", recId,"type","1"),
							new AsyncHttpResponseHandler() {

								@Override
								public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
									// TODO Auto-generated method stub
									dialog.dismiss();
									BaseBean bean = new Gson().fromJson(new String(arg2), BaseBean.class);
									// Log.e("bean________",
									// bean.getData().get(arg0).toString());
									if (bean.getErrCode() == 0) {// 如果取消订单成功
										ToastUtil.shortToast(getApplicationContext(), "取消订单成功！");
										finish();// 取消订单成功后即销毁此页面,并去启动上一个页面的Fragment
//										startActivity(new Intent(getApplicationContext(), MyDownwindActivity.class)
//												.putExtra("loadIndex", "Escort"));

									} else if (bean.getErrCode() == -1) {
										ToastUtil.shortToast(getApplicationContext(), bean.getMessage());
									} else if (bean.getErrCode() == -2) {
										ToastUtil.shortToast(getApplicationContext(), bean.getMessage());
									} else {
										ToastUtil.shortToast(getApplicationContext(), bean.getMessage());
									}
								}

								@Override
								public void onFailure(int arg0, Header[] arg1, byte[] arg2, Throwable arg3) {
									dialog.dismiss();
								}
							});
				}
			});
			ad.setNegativeButton("取消", new OnClickListener() {
				@Override
				public void onClick(DialogInterface arg0, int arg1) {
					arg0.dismiss();
				}
			});
			ad.create().show();

		

			// ToastUtil.shortToast(DownEscoreDartActivity.this, "取消订单成功");
			break;
		case R.id.img_phone:// 发件人电话
			AppUtils.intentDial(DownEscoreDartActivity.this, mobile);
//			showReplacewu();
			break;
		case R.id.img_phone_to:// 收件人电话
			AppUtils.intentDial(DownEscoreDartActivity.this, mobileTo);
			break;
		case R.id.img_address:// 地图定位
			break;

		case R.id.btn_submit:// 确认收货---------------------------------------02
			if (carnumber) {
				if (!mInput.getText().toString().equals("")) {
					popupWindow_recceivedConfirm.setAnimationStyle(R.style.mypopwindow_anim_style);
					popupWindow_recceivedConfirm.showAtLocation(view, Gravity.CENTER, 0, 0);
				}else {
					ToastUtil.shortToast(DownEscoreDartActivity.this, "请填写车牌号");
				}
			}else {
				popupWindow_recceivedConfirm.setAnimationStyle(R.style.mypopwindow_anim_style);
				popupWindow_recceivedConfirm.showAtLocation(view, Gravity.CENTER, 0, 0);
			}
		
			break;
		case R.id.btn_escort:// 押镖完成---------------------------------------01
			// popupWindow_deliveredConfirm.setAnimationStyle(R.style.mypopwindow_anim_style);
			// popupWindow_deliveredConfirm.showAtLocation(view, Gravity.CENTER,
			// 0, 0);
			if (latitude==5e-324 || latitude==4.9E-324) {
					ToastUtil.shortToast(DownEscoreDartActivity.this, "请开启定位");
				return;
				
			}
			if (!isFinished) {// 押镖未完成且交易码已发送
				Intent intent = new Intent(getApplicationContext(), DownWindPassActivity.class);
				intent.putExtra("recId", recId);// int recId;//镖件主键
				intent.putExtra("publishTime", publishTime);// String publishTime;//发布时间
				intent.putExtra("personName", personName);// String personName;//发件人
				intent.putExtra("mobile", mobile);// String mobile;//发件人手机号
				intent.putExtra("address", address);// String address;//发件地址
				intent.putExtra("personNameTo", personNameTo);// String personNameTo;//收件人
				intent.putExtra("mobileTo", mobileTo);// String mobileTo;//收件人手机号
				intent.putExtra("addressTo", addressTo);// String addressTo;//收件地址
				intent.putExtra("matRemark", matRemark);// String matRemark;//物品备注
				if (latitude==5e-324 || latitude==4.9E-324) {
					intent.putExtra("arriveLatitude", 0.0);// St
					intent.putExtra("arriveLongitude", 0.0);//
					
			}else {
				intent.putExtra("arriveLatitude", latitude);// St
				intent.putExtra("arriveLongitude", longitude);//
			}
				 
//				startActivity(intent);
				startActivityForResult(intent, 113);
//				finish();
			} else {
				// ToastUtil.shortToast(getApplicationContext(),
				// "请在用户取货后点击押镖完成");
			}

			break;
//				case R.id.btn_escortno:
//					
//					break;
		default:
			break;
		}
	}

	// 点击“确定取货”所弹出的PopupWindow;

	private void showPopupWindow_receivedConfirm() {

		vPopupWindow_recceivedConfirm = LayoutInflater.from(this).inflate(R.layout.popupwindow_tips, null);
		ll_setChecked = (LinearLayout) vPopupWindow_recceivedConfirm.findViewById(R.id.ll_setChecked);
		cb_checkedReceive = (CheckBox) vPopupWindow_recceivedConfirm.findViewById(R.id.cb_checkToReceive);
		btn_received = (Button) vPopupWindow_recceivedConfirm.findViewById(R.id.btn_recceivedOK);
		popupWindow_recceivedConfirm = new PopupWindow(vPopupWindow_recceivedConfirm, LayoutParams.MATCH_PARENT,
				LayoutParams.MATCH_PARENT, true);
		// 设置背景 使返回键生效（PopupWindow会拦截触摸事件，包括返回键和菜单键）
		popupWindow_recceivedConfirm.setBackgroundDrawable(new ColorDrawable(0x99999999));

		vPopupWindow_recceivedConfirm.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				if (vPopupWindow_recceivedConfirm.isShown()) {
					popupWindow_recceivedConfirm.dismiss();
				}
				return false;
			}
		});

	}

	// 点击“押镖完成”所弹出的PopupWindow;
	private void showvPopupWindow_deliveredConfirm() {

		vPopupWindow_deliveredConfirm = LayoutInflater.from(this).inflate(R.layout.popupwindow_enterpassword, null);
		btnCancel = (Button) vPopupWindow_deliveredConfirm.findViewById(R.id.btn_cancel);
		btnOK = (Button) vPopupWindow_deliveredConfirm.findViewById(R.id.btn_ok);

		popupWindow_deliveredConfirm = new PopupWindow(vPopupWindow_deliveredConfirm, LayoutParams.MATCH_PARENT,
				LayoutParams.MATCH_PARENT, true);
		// 设置背景 使返回键生效（PopupWindow会拦截触摸事件，包括返回键和菜单键）
		popupWindow_deliveredConfirm.setBackgroundDrawable(new ColorDrawable(0x99999999));

		vPopupWindow_deliveredConfirm.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				if (vPopupWindow_deliveredConfirm.isShown()) {
					popupWindow_deliveredConfirm.dismiss();
				}
				return false;
			}
		});

	}

	@Override
	public void initData() {
		// 上一个Activity：【SendEscortFragment】【DownWindPassActivity】
		
		mInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		mAccounts = new ArrayList<String>();
//		if (!"".equals(getIntent().getStringExtra("tyy")) ) {
//			if ( getIntent().getStringExtra("tyy").equals("1")) {
//			Intent intent=new Intent();
//			intent.putExtra("type", "1");
//			setResult(RESULT_OK, intent);
//			finish();
//			}
//		}
		getHttprequst();
		DecimalFormat df = new DecimalFormat("######0.0000");
		Intent intent = getIntent();
		recId = intent.getStringExtra("recId");
		publishTime = intent.getStringExtra("publishTime");
		personName = intent.getStringExtra("personName");
		mobile = intent.getStringExtra("mobile");
		address = intent.getStringExtra("address");
		personNameTo = intent.getStringExtra("personNameTo");
		mobileTo = intent.getStringExtra("mobileTo");
		addressTo = intent.getStringExtra("addressTo");
		matRemark = intent.getStringExtra("matRemark");
		
//		if(!"".equals(getIntent().getStringExtra("carLength"))){
//			if(getIntent().getStringExtra("carLength").equals("1")){
//				carchang.setText("车长需求：无");
//			}else if(getIntent().getStringExtra("carLength").equals("2")){
//				carchang.setText("车长需求：1.8米");
//			}else if (getIntent().getStringExtra("carLength").equals("3")) {
//				carchang.setText("车长需求：2.7米");
//			}else if (getIntent().getStringExtra("carLength").equals("4")) {
//				carchang.setText("车长需求：4.2米");
//			}
//		}else {
			carchang.setVisibility(View.GONE);
//		}
		
		if(!"".equals(getIntent().getStringExtra("useTime"))){
			usertime.setText("取货时间："+getIntent().getStringExtra("useTime"));
		}else {
			usertime.setVisibility(View.GONE);
		}
//		if(!"".equals(getIntent().getStringExtra("length"))){
//			tijis.setText("单件规格："+getIntent().getStringExtra("length"));
		
		if (getIntent().getStringExtra("matVolume").equals("")) {
			tijis.setText("要求车型：无要求");
		}else {
			tijis.setText("要求车型："+getIntent().getStringExtra("matVolume"));
		}
		
//		}else {
//			tijis.setText("");
//		}
//	ToastUtil.shortToast(DownEscoreDartActivity.this, ""+getIntent().getStringExtra("distance"));
		text_jian.setText("件数："+getIntent().getStringExtra("cargoSize")+"件");//件数
		if (!"".equals(getIntent().getStringExtra("distance"))) {
			String disString=getIntent().getStringExtra("distance");
			text_juli.setText("运送距离："+(float)(Integer.parseInt(disString))/1000+"公里");
		}
		
		
//		if (!getIntent().getStringExtra("carType").equals("")) {
//			if (getIntent().getStringExtra("carType").equals("smallTruck")) {
//				cartype.setText("要求车型：小货车");
//			}else if(getIntent().getStringExtra("carType").equals("middleTruck")){
//				cartype.setText("要求车型：中货车");
//			}else if(getIntent().getStringExtra("carType").equals("smallMinibus")){
//				cartype.setText("要求车型：小面包车");
//			}else if(getIntent().getStringExtra("carType").equals("middleMinibus")){
//				cartype.setText("要求车型：中面包车");
//			}else {
//				cartype.setVisibility(View.GONE);
////			}
//		}else {
			cartype.setVisibility(View.GONE);
//		}
		
		if (intent.getStringExtra("ifReplaceMoney").equals("true") 
			) {
			replaceMoney.setVisibility(View.VISIBLE);
			replaceMoney.setText("代收款："+intent.getStringExtra("replaceMoney")+"元");
//			ToastUtil.shortToast(DownEscoreDartActivity.this, "1s"+intent.getStringExtra("replaceMoney"));
		}else {
//			ToastUtil.shortToast(DownEscoreDartActivity.this, "2s"+intent.getStringExtra("replaceMoney"));
			replaceMoney.setVisibility(View.GONE);
		}
//		if (!intent.getStringExtra("length").equals("0")) {
			text_goodMessage.setText(intent.getStringExtra("length"));
//		}else {
//			text_goodMessage.setVisibility(View.GONE);
//			text_goodMessagese.setVisibility(View.GONE);
//		}
			
			if ("Y".equals(getIntent().getStringExtra("whether"))) {
				try {
				int  i=5000;
				int  a = 0;
				String string=getIntent().getStringExtra("premium");
				String[] strs=string.split("\\.");
						     a = Integer.parseInt(strs[0]);
						     if (subj(i,a)>0) {
						    	 lyoucarb.setVisibility(View.GONE);
								}else {
									lyoucarb.setVisibility(View.VISIBLE);
									carnumber=true;
								}
						} catch (NumberFormatException e) {
						    e.printStackTrace();
						}
				
			}
		
//		text_goodMoney.setText("运费:"+df.format(Double.parseDouble(intent.getStringExtra("transferMoney")))+" 元");
			text_goodMoney.setText("运费:"+intent.getStringExtra("transferMoney")+" 元");
		text_goodName.setText(intent.getStringExtra("matName"));
		// 后台返回的状态：2，3，5，7
		status = intent.getStringExtra("status");// 状态 0-预发布成功(未支付) 1-支付成功(已支付)
													// 2(已抢单) 3 已取件 4 订单取消()镖师 5
													// 成功 6 镖师删除 7 已评价 8订单取消（用户）
		readyTime= intent.getStringExtra("readyTime");//是否就位
		isFinished = intent.getBooleanExtra("isFinished", false);
//		Log.e("1111111：", intent.getStringExtra("finishTime") + "------------------");
		if(intent.getStringExtra("limitTime")!=null){
//			loutsLayout.setVisibility(View.VISIBLE);
			text_finishTime_to.setText(""+intent.getStringExtra("limitTime").toString());
		}
//		if(intent.getStringExtra("limitTime")!=null){
//			tv_limtime.setVisibility(View.VISIBLE);
//			tv_limtime.setText("用户期望到达时间："+intent.getStringExtra("limitTime"));
//		}
		
		
//		text_title.setText("单件详情");
//		if (getIntent().getStringExtra("limitTime").equals("")) {
			text_time.setText(getIntent().getStringExtra("useTime"));
//		}else {
//			text_time.setText(getIntent().getStringExtra("limitTime"));
//		}
		
		text_name.setText(personName);
//		text_phone.setText(mobile);
		text_address.setText(address);
		if (getIntent().getStringExtra("matWeight").equals("5")) {
			text_weights.setText("总重量: ≤"+getIntent().getStringExtra("matWeight")+" 公斤");
		}else {
			text_weights.setText("总重量:"+getIntent().getStringExtra("matWeight")+" 公斤");
		}
		
		text_name_to.setText(personNameTo);
//		text_phone_to.setText(mobileTo);
		text_address_to.setText(addressTo);
//		tv_matRemark.setVisibility(View.GONE);
		if (!matRemark.equals("")) {
			tv_matRemark.setText("备注：" + matRemark);
		}else {
			tv_matRemark.setVisibility(View.GONE);
		}
		
		Log.e("1111111111status", status + "");
		if (status.equals("10") || status.equals("11") || status.equals("5") || status.equals("7") || isFinished) {// 如果此镖件已完成或者已评价
			btn_submit.setBackgroundResource(R.drawable.bg_gray_rounded_stroke);
			btn_submit.setClickable(false);// 不可点击
			btn_submit.setTextColor(0xffa4a4a4);// 设置字体颜色
			btn_escort.setBackgroundResource(R.drawable.bg_gray_rounded_stroke);
			btn_escort.setTextColor(0xffa4a4a4);// 设置字体颜色
			btn_escort.setClickable(false);// 不可点击
//			btn_escortno.setClickable(false);// 不可点击
			iv_flag.setVisibility(View.GONE);// 设置为押镖完成的状态
			tv_cancle.setVisibility(View.INVISIBLE);
			btn_submit.setVisibility(View.GONE);
			dart_xuanze.setVisibility(View.GONE);
			dart_ok.setVisibility(View.GONE);
			mInput.setEnabled(false);
			mArrow.setClickable(false);
		}
		// 初始化【押镖完成】按钮不可点击；
		// 是否按押镖完成,false表示不可点击，证明押镖完成状态，且取货码已经发送；true表示可点击，证明是押镖未完成；
		// if(isFinished == true){
		// btn_escort.setBackgroundResource(R.drawable.bg_gray_rounded_stroke);
		// btn_escort.setTextColor(0xffa4a4a4);//设置字体颜色
		// btn_escort.setClickable(false);//不可点击
		// iv_flag.setVisibility(View.INVISIBLE);//设置为押镖完成的状态
		// }
		else if (status.equals("2")) {// 如果抢单，未取件；
			btn_escort.setBackgroundResource(R.drawable.bg_gray_rounded_stroke);
			btn_escort.setTextColor(0xffa4a4a4);// 设置字体颜色
			btn_escort.setClickable(false);// 不可点击
//			dart_xuanze.setVisibility(View.VISIBLE);
			dart_ok.setVisibility(View.GONE);
			tv_cancle.setVisibility(View.VISIBLE);
		}else
		if (status.equals("3")) {// 如果已取件，即短信取货吗发送成功；
			Log.e("11111", intent.getStringExtra("ifReplaceMoney"));
			if (!intent.getStringExtra("ifReplaceMoney").equals("true") ) {
				if (!intent.getStringExtra("ifTackReplace").equals("true")) {
					btn_submit.setBackgroundResource(R.drawable.bg_gray_rounded_stroke);
					btn_submit.setClickable(false);// 不可点击
					btn_submit.setTextColor(0xffa4a4a4);// 设置字体颜色
					// btn_escort.setBackgroundResource(R.drawable.bg_gray_rounded_stroke);
					// btn_escort.setTextColor(0xffa4a4a4);//设置字体颜色
					btn_submit.setVisibility(View.GONE);
					btn_escort.setClickable(true);// 可点击
					btn_escortno.setClickable(true);// 可点击
					dart_xuanze.setVisibility(View.GONE);
					dart_ok.setVisibility(View.VISIBLE);
					tv_cancle.setVisibility(View.INVISIBLE);
					mInput.setEnabled(false);
					mArrow.setClickable(false);
				}
//				ToastUtil.shortToast(DownEscoreDartActivity.this, "11"+intent.getStringExtra("ifTackReplace"));
				
			}else {
				if (intent.getStringExtra("ifTackReplace").equals("true")) {
					btn_submit.setBackgroundResource(R.drawable.bg_gray_rounded_stroke);
					btn_submit.setClickable(false);// 不可点击
					btn_submit.setTextColor(0xffa4a4a4);// 设置字体颜色
					// btn_escort.setBackgroundResource(R.drawable.bg_gray_rounded_stroke);
					// btn_escort.setTextColor(0xffa4a4a4);//设置字体颜色
					btn_submit.setVisibility(View.GONE);
					btn_escort.setClickable(true);// 可点击
					dart_xuanze.setVisibility(View.GONE);
					dart_ok.setVisibility(View.VISIBLE);
					tv_cancle.setVisibility(View.INVISIBLE);
				}else {
					btn_submit.setVisibility(View.GONE);
					dart_xuanze.setVisibility(View.GONE);
//					ToastUtil.shortToast(DownEscoreDartActivity.this, "22"+intent.getStringExtra("ifTackReplace"));
					tv_cancle.setVisibility(View.GONE);
					dart_ok.setVisibility(View.GONE);
					btn_ifReplace.setVisibility(View.VISIBLE);
				}
				
			}
			
		}
		else if (status.equals("9")) {
			dart_xuanze.setVisibility(View.GONE);
			btn_escort.setVisibility(View.GONE);
			btn_submit.setVisibility(View.GONE);
			tv_cancle.setVisibility(View.INVISIBLE);
		}else
		if (status.equals("4") || status.equals("8")) {
			if (getIntent().getStringExtra("ifAgree").equals("1")) {
				btn_tousu.setVisibility(View.GONE);
				tv_cancle.setVisibility(View.GONE);
				dart_xuanze.setVisibility(View.GONE);
				btn_escort.setVisibility(View.GONE);
				btn_submit.setVisibility(View.GONE);
			}else {
//				btn_tousu.setVisibility(View.VISIBLE);
				tv_cancle.setVisibility(View.GONE);
				dart_xuanze.setVisibility(View.GONE);
				btn_escort.setVisibility(View.GONE);
				btn_submit.setVisibility(View.GONE);
			}
			
			
		}
		if(!readyTime.equals("")){
			btn_jiuwei.setClickable(false);
			btn_jiuwei.setBackgroundResource(R.drawable.bg_gray_rounded_stroke);
			btn_jiuwei.setText("已就位");
			btn_jiuwei.setTextColor(getResources().getColor(R.color.grayview));
		}else {
			btn_jiuwei.setClickable(true);
		}
		

		client = new LocationClient(getApplicationContext());
		initLocation();
		client.registerLocationListener(new BDLocationListener() {

			@Override
			public void onReceiveLocation(BDLocation arg0) {
				if (arg0 == null) {
					ToastUtil.shortToast(DownEscoreDartActivity.this, "定位失败，请检查定位设置");
					latitude = 0.0;
					longitude = 0.0;
					return;
				} else {
					if (latitude==5e-324 || latitude==4.9e-324){
						ToastUtil.shortToast(DownEscoreDartActivity.this, "定位失败，请检查定位设置");
						latitude = 0.0;
						longitude = 0.0;
					}else {
						latitude = arg0.getLatitude();
						longitude = arg0.getLongitude();
					
					}
					saddse= arg0.getAddrStr();
				}
				
				Log.e("jpppp", latitude + ":::::::::" + longitude);
			}
		});
		// 初始化定位
		// 打开GPS
		client.start();

	}
	public int subj(int a,int b){
		int resultSub=a-b;
		return resultSub;
	}
private boolean isAppInstall( Context context, String pakgnmane){
	PackageInfo packageInfo;
	try {
		packageInfo=context.getPackageManager().getPackageInfo(pakgnmane, 0);
		
	} catch (PackageManager.NameNotFoundException e) {
		// TODO: handle exception
		packageInfo=null;
		e.printStackTrace();
		
	}
	if (packageInfo==null) {
		return false;
	}else {
		return true;
	}
	
}
public void onDownArrowClicked(View view)
{
	if (mAccounts.size() != 0)
	{
		mArrow.setBackgroundResource(R.drawable.arrow_up);
		showAccountChoiceWindow();
	}
}

private void showAccountChoiceWindow()
{
	View view = mInflater.inflate(R.layout.input_selectlist, null);
	RelativeLayout contentview = (RelativeLayout) view.findViewById(R.id.input_select_listlayout);
	ListView userlist = (ListView) view.findViewById(R.id.input_select_list);
	userlist.setDividerHeight(0);

	SelectListAdapter adapter = new SelectListAdapter(this, mAccounts);
//	adapter.setOnItemClickListener(this);
	adapter.setOnDelBtnClickListener(this);
	adapter.setOnItemClickListener(this);
	userlist.setAdapter(adapter);

	mSelectWindow = new PopupWindow(contentview, mInputLayout.getMeasuredWidth(), LayoutParams.WRAP_CONTENT, true);
	mSelectWindow.setBackgroundDrawable(getResources().getDrawable(R.drawable.bg_input_bottom_normal));
	int heig = DownEscoreDartActivity.this.getWindowManager().getDefaultDisplay().getHeight();
//	mSelectWindow.setHeight(heig*3/5);
	if (haibean1.data.size()>6) {
		mSelectWindow.setHeight(heig*3/5);
	}
	// 实例化一个ColorDrawable颜色为半透明
				 ColorDrawable dw = new ColorDrawable(R.color.transparent01);
				mSelectWindow.setBackgroundDrawable(dw);
	mSelectWindow.setOutsideTouchable(true);
	mSelectWindow.setFocusable(true);
	mSelectWindow.setOnDismissListener(this);
//	mSelectWindow.showAsDropDown(mInputLayout, 0, 0);
	int[] location = new int[2];
	mInputLayout.getLocationOnScreen(location);
	mSelectWindow.showAtLocation(mInputLayout, Gravity.NO_GRAVITY, location[0], location[1]-mSelectWindow.getHeight());
	mArrow.setBackgroundResource(R.drawable.arrow_up);
}

public void onDelBtnClicked(int position)
{
	mSelectWindow.dismiss();
	String inputAccount = mInput.getText().toString();
	if (inputAccount == mAccounts.remove(position))
	{
		String nextAccount = "";

		if (mAccounts.size() != 0)
			nextAccount = mAccounts.get(0) + "";

		mInput.setText(nextAccount);
	}
	
	detegetHttprequst(haibean1.getData().get(position).recId);
//	Toast.makeText(this, "删除账号成功", Toast.LENGTH_SHORT).show();
}

public void onItemClicked(int position)
{
	mSelectWindow.dismiss();
	mInput.setText(mAccounts.get(position) + "");
}

public void onDismiss()
{
	mArrow.setBackgroundResource(R.drawable.arrow_down);
}
/**
 * 获取车牌
 */
private void getHttprequst() {
	RequestParams params = new RequestParams();
	AsyncHttpUtils.doGet(UrlMap.getUrl(MCUrl.HaiCarNumber, "userId", String
			.valueOf(PreferencesUtils.getInt(getApplicationContext(),
					PreferenceConstants.UID))), null, null, params,
			new AsyncHttpResponseHandler() {
				@Override
				public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
					Log.e("json", new String(arg2));
					haibean1 = new Gson().fromJson(new String(arg2),
							HaiBean.class);
//					dialog.dismiss();
					for (int i = 0; i < haibean1.data.size(); i++)
					{
						mAccounts.add(haibean1.data.get(i).carNum);
					}
					if (mAccounts.size()>0) {
						mInput.setText(mAccounts.get(0) + "");
					}
					} 

				@Override
				public void onFailure(int arg0, Header[] arg1, byte[] arg2,
						Throwable arg3) {
					dialog.dismiss();
//					ToastUtil.shortToast(HaiLogisticalActivity.this, "网络请求加载失败");

				}
			});

}


/**
 * 删除车牌
 */
private void detegetHttprequst( int recId) {
	RequestParams params = new RequestParams();
	AsyncHttpUtils.doGet(UrlMap.getUrl(MCUrl.HaiDeleteCarNum, "recId", ""+recId), null, null, params,
			new AsyncHttpResponseHandler() {
				@Override
				public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
					BaseBean		bean = new Gson().fromJson(new String(arg2),
							BaseBean.class);
					if (bean.getErrCode()!=0) {
						ToastUtil.shortToast(DownEscoreDartActivity.this, bean.getMessage());
					}
				}
				@Override
				public void onFailure(int arg0, Header[] arg1, byte[] arg2,
						Throwable arg3) {
					dialog.dismiss();
					ToastUtil.shortToast(DownEscoreDartActivity.this, "网络请求加载失败");

				}
			});

}

/**
 * 判断是否安装目标应用
 * @param packageName 目标应用安装后的包名
 * @return 是否已安装目标应用
 */
private boolean isInstallByread(String packageName) {
    return new File("/data/data/" + packageName).exists();
}
	@Override
	public void setOnClick() {
		// TODO Auto-generated method stubbtn_escortno
		btn_escortno.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
//				AlertDialog.Builder ad = new Builder(DownEscoreDartActivity.this);
//				ad.setTitle("温馨提示");
//				ad.setMessage("如72小时内无用户投诉，此单运费划入余额");
//				ad.setPositiveButton("确认", new DialogInterface.OnClickListener() {
//
//					@Override
//					public void onClick(DialogInterface arg0, int arg1) {
//						trueTake();
//					}
//				});
//				
//				ad.setNegativeButton("取消", new DialogInterface.OnClickListener() {
//					@Override
//					public void onClick(DialogInterface arg0, int arg1) {
//						arg0.dismiss();
//						
//					}
//				});
//				ad.create().show();
				showReplacewu();
				
			}
		});
		
		qujian.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
					String fla=getIntent().getStringExtra("fromLatitude");
					String flo=getIntent().getStringExtra("fromLongitude");
					String number = fla;
					String intNumber = number.substring(0,number.indexOf(".")+3);
					String number2 = flo;
					String intNumber2 = number2.substring(0,number2.indexOf(".")+3);
//					ToastUtil.shortToast(DownEscoreDartActivity.this, ""+intNumber2);
					String tfla=getIntent().getStringExtra("toLatitude");
					String tflo=getIntent().getStringExtra("toLongitude");
				showPopupmaP(fla,flo,address);
			
			    }
		});
		
		songjian.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				String fla=getIntent().getStringExtra("fromLatitude");
				String flo=getIntent().getStringExtra("fromLongitude");
				String number = fla;
				String intNumber = number.substring(0,number.indexOf(".")+3);
				String number2 = flo;
				String intNumber2 = number2.substring(0,number2.indexOf(".")+3);
//				ToastUtil.shortToast(DownEscoreDartActivity.this, ""+intNumber2);
				String tfla=getIntent().getStringExtra("toLatitude");
				String tflo=getIntent().getStringExtra("toLongitude");
			showPopupmaP(tfla,tflo,addressTo);
			}
		});
		btn_tousu.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				showPopup_complain();
			}
		});
		btn_weigui.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				
				showPopup_report();
			}
		});
		btn_jiuwei.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if (latitude==5e-324 || latitude==4.9E-324) {
					ToastUtil.shortToast(DownEscoreDartActivity.this, "请开启定位");
				return;
				
			}
				getmegsejiu();
			}
		});
		btn_ifReplace.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
			
				showReplaceMoney();
			}
		});
	}
	public void getReplace(){
		
		AsyncHttpUtils.doSimGet(UrlMap.getUrl(MCUrl.ReplayMoney, "recId", recId + ""),

				new AsyncHttpResponseHandler() {

					@Override
					public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
						// TODO Auto-generated method stub
						BaseBean bean = new Gson().fromJson(new String(arg2), BaseBean.class);
						if (bean.getErrCode()==0) {
							Intent intent=new Intent();
							intent.putExtra("type", "1");
							setResult(RESULT_OK, intent);
							finish();
						}else {
							ToastUtil.shortToast(DownEscoreDartActivity.this, bean.getMessage());
						}
					}

					@Override
					public void onFailure(int arg0, Header[] arg1, byte[] arg2, Throwable arg3) {
						Log.e("ccc", arg0 + "");
					}
				});
	}

	/**
	 * 
	 * 镖师确定取货发送确认密码;
	 */
	public void getData1() {
		// TODO Auto-generated method stub
		if (latitude==5e-324 || latitude==4.9E-324) {
			ToastUtil.shortToast(DownEscoreDartActivity.this, "请开启定位");
		
	  }else {
		
	
		Log.e("-----------------", "-----" + UrlMap.getUrl(MCUrl.DRIVERDETAIL, "recId", recId));
	
		// GET方法
		AsyncHttpUtils.doSimGet(UrlMap.getfive(MCUrl.DRIVERISTASK, "recId", recId + "","orderDeviceId",DataTools.getDeviceId(DownEscoreDartActivity.this),"tackLatitude",latitude+"","tackLongitude",longitude+""
				,"carNumber",mInput.getText().toString()),

				new AsyncHttpResponseHandler() {

					@Override
					public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
						// TODO Auto-generated method stub
						BaseBean bean = new Gson().fromJson(new String(arg2), BaseBean.class);
						Log.e("bean________", new String(arg2) + "ooooooooo");
						if (bean.getErrCode() == 0) {// 如果获取成功
                      
							ToastUtil.shortToast(getApplicationContext(), "取货短信发送成功！");
							btn_submit.setBackgroundResource(R.drawable.bg_gray_rounded_stroke);
							btn_submit.setTextColor(0xffa4a4a4);// 设置字体颜色
							btn_submit.setClickable(false);
							btn_escort.setBackgroundResource(R.drawable.selector_orange_to_gray_rouded_stroke);
							btn_escort.setTextColor(Color.WHITE);
							btn_escort.setClickable(true);
							Intent intent=new Intent();
							intent.putExtra("type", "1");
							setResult(RESULT_OK, intent);
							finish();
//							 finish();
						} else if (bean.getErrCode() == -1) {
							ToastUtil.shortToast(getApplicationContext(), bean.getMessage());
						} else if (bean.getErrCode() == -2) {
							ToastUtil.shortToast(getApplicationContext(), bean.getMessage());
						} else {
							ToastUtil.shortToast(getApplicationContext(), bean.getMessage());
						}
					}

					@Override
					public void onFailure(int arg0, Header[] arg1, byte[] arg2, Throwable arg3) {
						Log.e("ccc", arg0 + "");
					}
				});
	}
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub

		switch (v.getId()) {
		case R.id.btn_ok:// 取货完成的确定按钮
			Log.e("11111checkDeviceId", DataTools.getDeviceId(DownEscoreDartActivity.this));
			// GET方法获取数据
			AsyncHttpUtils.doSimGet(UrlMap.getThreeUrl(MCUrl.DRIVERTRUETASK, "recId", recId, "dealPassword", password,"checkDeviceId",DataTools.getDeviceId(DownEscoreDartActivity.this)),

					new AsyncHttpResponseHandler() {
						@Override
						public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
							// TODO Auto-generated method stub
							BaseBean bean = new Gson().fromJson(new String(arg2), BaseBean.class);
							// Log.e("bean________",
							// bean.getData().get(arg0).toString());
							if (bean.getErrCode() == 0) {// 操作成功
//								finish();
								Intent intent=new Intent();
								intent.putExtra("type", "1");
								setResult(RESULT_OK, intent);
								finish();
								ToastUtil.shortToast(getApplicationContext(), "送单成功！");

							} else if (bean.getErrCode() == -1) {
								ToastUtil.shortToast(getApplicationContext(), bean.getMessage());
							} else if (bean.getErrCode() == -2) {
								ToastUtil.shortToast(getApplicationContext(), bean.getMessage());
							} else {
								ToastUtil.shortToast(getApplicationContext(), bean.getMessage());
							}
						}

						@Override
						public void onFailure(int arg0, Header[] arg1, byte[] arg2, Throwable arg3) {
							Log.e("ccc", arg0 + "");
						}
					});

			popupWindow_deliveredConfirm.dismiss();
			break;
		case R.id.btn_cancel:// 押镖完成，弹出的输入密码弹框的取消按钮
			popupWindow_deliveredConfirm.dismiss();
			break;
		case R.id.ll_setChecked:// 点击设置镖件验收状态

			if (cb_checkedReceive.isChecked()) {
				cb_checkedReceive.setChecked(false);
			} else {
				cb_checkedReceive.setChecked(true);
			}

			break;
		case R.id.btn_recceivedOK:

			// 判断镖件验收状态和取货按钮；
			if (cb_checkedReceive.isChecked()) {
				
				getData1();// 镖师确定取货发送确认密码
				popupWindow_recceivedConfirm.dismiss();
			} else {
				ToastUtil.shortToast(getApplicationContext(), "请验收后再取货！");
			}

			break;

		default:
			break;
		}

	}

	private void setOnClickListener() {

		btnOK.setOnClickListener(this);
		btnCancel.setOnClickListener(this);
		btn_received.setOnClickListener(this);
		ll_setChecked.setOnClickListener(this);

	}

	@Override
	public void onClick(DialogInterface dialog, int which) {
		// TODO Auto-generated method stub

	}

	@Override
	public void getData() {
		// TODO Auto-generated method stub

		
	}
	private void trueTake(){
		RequestParams params = new RequestParams();
		
		AsyncHttpUtils.doGet(
				UrlMap.getfour(MCUrl.trueTake, "recId",recId,
						"checkDeviceId",DataTools.getDeviceId(DownEscoreDartActivity.this),"arriveLatitude",""+latitude,"arriveLongitude",""+longitude
						),
				null, null, params, new AsyncHttpResponseHandler() {
					@Override
					public void onFailure(int arg0, Header[] arg1, byte[] arg2, Throwable arg3) {
					}
					@Override
					public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
						// TODO Auto-generated method stub
						Log.e("11json", "" + new String(arg2));
						BaseBean baseBean=new Gson().fromJson(new String(arg2), BaseBean.class);
						if (baseBean.getErrCode()==0) {
							ToastUtil.shortToast(DownEscoreDartActivity.this, baseBean.getMessage());
							Intent intent=new Intent();
							intent.putExtra("type", "1");
							setResult(RESULT_OK, intent);
							finish();
						}else {
//							Intent intent=new Intent();
//							intent.putExtra("type", "1");
//							setResult(RESULT_OK, intent);
//							finish();
							ToastUtil.shortToast(DownEscoreDartActivity.this, baseBean.getMessage());
						}


					}

				});
	}
	
	private void showPopup_complain(){
//		private ImageView	btn_comp_image;
//		private Button	btn_comp_sumit;
		vPopupWindow_complain = LayoutInflater.from(this).inflate(R.layout.popupwindow_complain, null);
//		ll_report = (LinearLayout) vPopupWindow_report.findViewById(R.id.ll_setChecked);
		btn_comp_image = (ImageView) vPopupWindow_complain.findViewById(R.id.btn_comp_image);
		edit_comp = (EditText) vPopupWindow_complain.findViewById(R.id.edit_comp);
		btn_comp_sumit = (Button) vPopupWindow_complain.findViewById(R.id.btn_comp_sumit);
		exit = (TextView) vPopupWindow_complain.findViewById(R.id.exit);
		popupWindow_complain = new PopupWindow(vPopupWindow_complain, LayoutParams.MATCH_PARENT,
				LayoutParams.MATCH_PARENT, true);
		popupWindow_complain.setAnimationStyle(R.style.mypopwindow_anim_style);
		popupWindow_complain.showAtLocation(DownEscoreDartActivity.this.findViewById(R.id.btn_escort), Gravity.CENTER, 0, 0);
		// 设置背景 使返回键生效（PopupWindow会拦截触摸事件，包括返回键和菜单键）
		popupWindow_complain.setBackgroundDrawable(new ColorDrawable(0x99999999));
		//上传照片
		btn_comp_image.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				showPopwindowcomp();
			}
		});
		//提交
		btn_comp_sumit.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				getmegsetousu();
//				popupWindow_complain.dismiss();
			}
		});
		exit.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				popupWindow_complain.dismiss();
			}
		});
		popupWindow_complain.setOnDismissListener(new OnDismissListener() {

			@Override
			public void onDismiss() {
				popupWindow_complain.dismiss();
			}
		});
		
		
	}
	/**
	 * t投诉
	 */
	private void getmegsetousu(){
		JSONObject obj = new JSONObject();
		if (edit_comp.getText().toString().equals("")) {
			ToastUtil.shortToast(getApplicationContext(), "请输入投诉内容");
			return;
		}
//		if (!frist) {
//			ToastUtil.shortToast(getApplicationContext(), "上传照片");
//			return;
//		}
		try {
			obj.put("userId",
					String.valueOf(PreferencesUtils.getInt(getApplicationContext(), PreferenceConstants.UID)));
			obj.put("toUserId", getIntent().getStringExtra("toUserId"));
			obj.put("type", "2");
			obj.put("details",edit_comp.getText().toString());
			obj.put("toRecId",recId);
			obj.put("imageUrl", icon1);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		dialog.show();
		Log.e("查看数据", obj.toString());
		//PublishNew    LOGISTICS
		AsyncHttpUtils.doPostJson(DownEscoreDartActivity.this, MCUrl.Accusations, obj.toString(),
				new AsyncHttpResponseHandler() {
			@Override
			public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
				dialog.dismiss();
				popupWindow_complain.dismiss();
				BaseBean baseBean=new Gson().fromJson(new String(arg2), BaseBean.class);
				if (baseBean.getErrCode()==0) {
				}else {
					ToastUtil.shortToast(DownEscoreDartActivity.this, baseBean.getMessage());
				}
			}
			@Override
			public void onFailure(int arg0, Header[] arg1, byte[] arg2, Throwable arg3) {
				dialog.dismiss();
			}
		});
	}
	private void getmegsejiu(){
		String url = UrlMap.getThreeUrl(MCUrl.OnReady, "recId",
				recId, "readyLatitude",
				""+latitude,"readyLongitude",""+longitude);
	    	dialog.show();
		AsyncHttpUtils.doSimGet(url, new AsyncHttpResponseHandler() {
			@Override
			public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
				dialog.dismiss();
				BaseBean baseBean=new Gson().fromJson(new String(arg2), BaseBean.class);
				if (baseBean.getErrCode()==0) {
					btn_jiuwei.setClickable(false);
					btn_jiuwei.setBackgroundResource(R.drawable.bg_gray_rounded_stroke);
					btn_jiuwei.setTextColor(getResources().getColor(R.color.grayview));
				}else {
					btn_jiuwei.setClickable(true);
					ToastUtil.shortToast(DownEscoreDartActivity.this, baseBean.getMessage());
				}
			}
			@Override
			public void onFailure(int arg0, Header[] arg1, byte[] arg2, Throwable arg3) {
				dialog.dismiss();
			}
		});
	}
	/**
	 * h货物违规
	 */
	private void showPopup_report(){
		vPopupWindow_report = LayoutInflater.from(this).inflate(R.layout.popupwindow_report, null);
//		ll_report = (LinearLayout) vPopupWindow_report.findViewById(R.id.ll_setChecked);
		btn_image = (ImageView) vPopupWindow_report.findViewById(R.id.btn_image);
		btn_immediately = (Button) vPopupWindow_report.findViewById(R.id.btn_immediately);
		exit_re = (TextView) vPopupWindow_report.findViewById(R.id.exit_re);
		popupWindow_report = new PopupWindow(vPopupWindow_report, LayoutParams.MATCH_PARENT,
				LayoutParams.MATCH_PARENT, true);
		popupWindow_report.setAnimationStyle(R.style.mypopwindow_anim_style);
		popupWindow_report.showAtLocation(DownEscoreDartActivity.this.findViewById(R.id.btn_escort), Gravity.CENTER, 0, 0);
		// 设置背景 使返回键生效（PopupWindow会拦截触摸事件，包括返回键和菜单键）
		popupWindow_report.setBackgroundDrawable(new ColorDrawable(0x99999999));
		
			btn_immediately.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				getmegseshang(1);
			}
		});
			btn_image.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					showPopwindow();
				}
			});
			exit_re.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				popupWindow_report.dismiss();
			}
		});

		popupWindow_report.setOnDismissListener(new OnDismissListener() {

			@Override
			public void onDismiss() {
				popupWindow_report.dismiss();
			}
		});
	}
	private void getmegseshang(int i){
//		if ("".equals(icon)) {
//			ToastUtil.shortToast(DownEscoreDartActivity.this, string);
//			return;
//		}
		String url;
//		if (i==1) {
			 url = UrlMap.getTwo(MCUrl.RemoveDow, "recId",recId, "illegalImageUrl",""+icon);
//		}else {
//			 url = UrlMap.getTwo(MCUrl.RemoveDow, "recId",recId, "illegalImageUrl","");
//		}
		
	    	dialog.show();
		AsyncHttpUtils.doSimGet(url, new AsyncHttpResponseHandler() {
			@Override
			public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
				dialog.dismiss();
				popupWindow_report.dismiss();
				BaseBean baseBean=new Gson().fromJson(new String(arg2), BaseBean.class);
				if (baseBean.getErrCode()==0) {
					dart_xuanze.setVisibility(View.GONE);
//					finish();
					Intent intent=new Intent();
					intent.putExtra("type", "1");
					setResult(RESULT_OK, intent);
					finish();
				}else {
					ToastUtil.shortToast(DownEscoreDartActivity.this, baseBean.getMessage());
				}
			}
			@Override
			public void onFailure(int arg0, Header[] arg1, byte[] arg2, Throwable arg3) {
				dialog.dismiss();
			}
		});
	}

	// //返回键
	// @Override
	// public boolean onKeyDown(int keyCode,KeyEvent event) {
	// if (keyCode == KeyEvent.KEYCODE_BACK&&event.getAction() ==
	// KeyEvent.ACTION_DOWN){
	// //这里重写返回键
	// finish();//取消订单成功后即销毁此页面,并去启动上一个页面的Fragment
	// startActivity(new Intent(getApplicationContext(),
	// SendFreightActivity.class).putExtra("loadIndex", "Escort"));
	// Log.e("--------", "00000000000");
	//
	// return true;
	// }
	// return super.onKeyDown(keyCode, event);
	// }
	//

	/**
	 * FragmentActivity的重写返回键；
	 */

//	@Override
//	public boolean dispatchKeyEvent(KeyEvent event) {
//		if (event.getKeyCode() == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN
//				&& event.getRepeatCount() == 0) {
////			 ToastUtil.shortToast(getApplicationContext(),
////			 "这是测试FragmentActivity的重写返回键");
//			// 具体的操作代码
//			finish();// 销毁此页面,并去启动上一个页面的相应的Fragment
//			startActivity(
//					new Intent(getApplicationContext(), MyDownwindActivity.class).putExtra("loadIndex", "Escort"));
//			Log.e("--------", "00000000000");
//		}
//		return super.dispatchKeyEvent(event);
//	}
	private void initLocation() {
		LocationClientOption option = new LocationClientOption();
		option.setLocationMode(com.baidu.location.LocationClientOption.LocationMode.Hight_Accuracy);// 可选，默认高精度，设置定位模式，高精度，低功耗，仅设备
		option.setCoorType("bd09ll");// 可选，默认gcj02，设置返回的定位结果坐标系
		int span = 0;
		option.setScanSpan(span);// 可选，默认0，即仅定位一次，设置发起定位请求的间隔需要大于等于1000ms才是有效的
		option.setIsNeedAddress(true);// 可选，设置是否需要地址信息，默认不需要
		option.setOpenGps(false);// 可选，默认false,设置是否使用gps
		option.setLocationNotify(true);// 可选，默认false，设置是否当gps有效时按照1S1次频率输出GPS结果
		option.setIsNeedLocationDescribe(true);// 可选，默认false，设置是否需要位置语义化结果，可以在BDLocation.getLocationDescribe里得到，结果类似于“在北京天安门附近”
		option.setIsNeedLocationPoiList(true);// 可选，默认false，设置是否需要POI结果，可以在BDLocation.getPoiList里得到
		option.setIgnoreKillProcess(false);// 可选，默认true，定位SDK内部是一个SERVICE，并放到了独立进程，设置是否在stop的时候杀死这个进程，默认不杀死
		option.SetIgnoreCacheException(false);// 可选，默认false，设置是否收集CRASH信息，默认收集
		option.setEnableSimulateGps(false);// 可选，默认false，设置是否需要过滤gps仿真结果，默认需要
		client.setLocOption(option);
	}
	/**
	 * 显示投诉popupWindow
	 */
	private void showPopwindowcomp(){
		// 利用layoutInflater获得View
		LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View view = inflater.inflate(R.layout.pop_window_lauoutitem, null);
		// 下面是两种方法得到宽度和高度 getWindow().getDecorView().getWidth()
		final PopupWindow window = new PopupWindow(view, WindowManager.LayoutParams.MATCH_PARENT,
				WindowManager.LayoutParams.WRAP_CONTENT);
		// 设置popWindow弹出窗体可点击，这句话必须添加，并且是true
		window.setFocusable(true);
		// 实例化一个ColorDrawable颜色为半透明
		ColorDrawable dw = new ColorDrawable(R.color.transparent);
		window.setBackgroundDrawable(dw); 
		window.setOutsideTouchable(false);// 这是点击外部不消失
		// 设置popWindow的显示和消失动画
		window.setAnimationStyle(R.style.mypopwindow_anim_style);
		// 在底部显示
		window.showAtLocation(DownEscoreDartActivity.this.findViewById(R.id.btn_escort), Gravity.BOTTOM, 0, 0);

		// 这里检验popWindow里的button是否可以点击
		TextView tv_camera = (TextView) view.findViewById(R.id.tv_camera);
		TextView tv_show = (TextView) view.findViewById(R.id.tv_show);
		tv_show.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				startActivity(new Intent(DownEscoreDartActivity.this, IdCardActivity.class));
				window.dismiss();
			}
		});
		tv_camera.setClickable(true);
		// 点击是拍照
		tv_camera.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				window.dismiss();
				Intent intent2 = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
				intent2.putExtra(MediaStore.EXTRA_OUTPUT,
						Uri.fromFile(new File(Environment.getExternalStorageDirectory(), "comp.png")));
				// intent2.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
				startActivityForResult(intent2, 22);// 采用ForResult打开
			}
		});
		// 从相册弄
		TextView tv_photofromalbum = (TextView) view.findViewById(R.id.tv_photofromalbum);
		tv_photofromalbum.setClickable(true);
		tv_photofromalbum.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				window.dismiss();
				Intent intent1 = new Intent(Intent.ACTION_PICK, null);
				intent1.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
				// intent1.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
				startActivityForResult(intent1, 11);
			}
		});
		TextView tv_cancel = (TextView) view.findViewById(R.id.tv_cancel);
		tv_cancel.setClickable(true);
		tv_cancel.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				window.dismiss();
			}
		});
		// popWindow消失监听方法
		window.setOnDismissListener(new OnDismissListener() {

			@Override
			public void onDismiss() {
				

			}
		});

	}
	/**
	 * 显示货物违规popupWindow
	 */
	private void showPopwindow() {
		// 利用layoutInflater获得View
		LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View view = inflater.inflate(R.layout.pop_window_lauoutitem, null);
		// 下面是两种方法得到宽度和高度 getWindow().getDecorView().getWidth()
		final PopupWindow window = new PopupWindow(view, WindowManager.LayoutParams.MATCH_PARENT,
				WindowManager.LayoutParams.WRAP_CONTENT);
		// 设置popWindow弹出窗体可点击，这句话必须添加，并且是true
		window.setFocusable(true);
		// 实例化一个ColorDrawable颜色为半透明
		ColorDrawable dw = new ColorDrawable(R.color.transparent);
		window.setBackgroundDrawable(dw); 
		window.setOutsideTouchable(false);// 这是点击外部不消失
		// 设置popWindow的显示和消失动画
		window.setAnimationStyle(R.style.mypopwindow_anim_style);
		// 在底部显示
		window.showAtLocation(DownEscoreDartActivity.this.findViewById(R.id.btn_escort), Gravity.BOTTOM, 0, 0);

		// 这里检验popWindow里的button是否可以点击
		TextView tv_camera = (TextView) view.findViewById(R.id.tv_camera);
		TextView tv_show = (TextView) view.findViewById(R.id.tv_show);
		tv_show.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				startActivity(new Intent(DownEscoreDartActivity.this, IdCardActivity.class));
				window.dismiss();
			}
		});
		tv_camera.setClickable(true);
		// 点击是拍照
		tv_camera.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				window.dismiss();
				Intent intent2 = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
				intent2.putExtra(MediaStore.EXTRA_OUTPUT,
						Uri.fromFile(new File(Environment.getExternalStorageDirectory(), "dart.png")));
				// intent2.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
				startActivityForResult(intent2, 2);// 采用ForResult打开
			}
		});
		// 从相册弄
		TextView tv_photofromalbum = (TextView) view.findViewById(R.id.tv_photofromalbum);
		tv_photofromalbum.setClickable(true);
		tv_photofromalbum.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				window.dismiss();
				Intent intent1 = new Intent(Intent.ACTION_PICK, null);
				intent1.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
				// intent1.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
				startActivityForResult(intent1, 1);
			}
		});
		TextView tv_cancel = (TextView) view.findViewById(R.id.tv_cancel);
		tv_cancel.setClickable(true);
		tv_cancel.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				window.dismiss();
			}
		});
		// popWindow消失监听方法
		window.setOnDismissListener(new OnDismissListener() {

			@Override
			public void onDismiss() {

			}
		});

	}
	/**
	 * 显示ReplaceMoney
	 */
	private void showReplaceMoney() {
		// 利用layoutInflater获得View
		LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View view = inflater.inflate(R.layout.pop_window_remoney, null);
		// 下面是两种方法得到宽度和高度 getWindow().getDecorView().getWidth()
		final PopupWindow window = new PopupWindow(view, WindowManager.LayoutParams.MATCH_PARENT,
				WindowManager.LayoutParams.MATCH_PARENT);
		// 设置popWindow弹出窗体可点击，这句话必须添加，并且是true
		window.setFocusable(true);
		// 实例化一个ColorDrawable颜色为半透明
		ColorDrawable dw = new ColorDrawable(android.R.color.white);
		window.setBackgroundDrawable(dw); 
		window.setOutsideTouchable(false);// 这是点击外部不消失
		// 设置popWindow的显示和消失动画
		window.setAnimationStyle(R.style.mypopwindow_anim_style);
		// 在底部显示
		window.showAtLocation(DownEscoreDartActivity.this.findViewById(R.id.btn_escort), Gravity.BOTTOM, 0, 0);

		// 这里检验popWindow里的button是否可以点击
		Button tv_show = (Button) view.findViewById(R.id.btnsaves_pan);
		TextView extis= (TextView) view.findViewById(R.id.extis);
		tv_show.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				getReplace();
			}
		});
		extis.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				window.dismiss();
			}
		});
		// popWindow消失监听方法
		window.setOnDismissListener(new OnDismissListener() {

			@Override
			public void onDismiss() {

			}
		});

	}
	/**
	 * 显示ReplaceMoney
	 */
	private void showReplacewu() {
		// 利用layoutInflater获得View  如72小时内无用户投诉，此单运费划入余额
		LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View view = inflater.inflate(R.layout.pop_window_remoneywu, null);
		// 下面是两种方法得到宽度和高度 getWindow().getDecorView().getWidth()
		final PopupWindow window = new PopupWindow(view, WindowManager.LayoutParams.MATCH_PARENT,
				WindowManager.LayoutParams.MATCH_PARENT);
		// 设置popWindow弹出窗体可点击，这句话必须添加，并且是true
		window.setFocusable(true);
		// 实例化一个ColorDrawable颜色为半透明
		ColorDrawable dw = new ColorDrawable(android.R.color.white);
		window.setBackgroundDrawable(dw); 
		window.setOutsideTouchable(false);// 这是点击外部不消失
		// 设置popWindow的显示和消失动画
		window.setAnimationStyle(R.style.mypopwindow_anim_style);
		// 在底部显示
		window.showAtLocation(DownEscoreDartActivity.this.findViewById(R.id.btn_escort), Gravity.BOTTOM, 0, 0);

		// 这里检验popWindow里的button是否可以点击
		TextView queding = (TextView) view.findViewById(R.id.queding);
		TextView quxiao= (TextView) view.findViewById(R.id.quxiao);
		queding.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (latitude==5e-324 || latitude==4.9E-324) {
					ToastUtil.shortToast(DownEscoreDartActivity.this, "请开启定位");
				return;
				
			}
				trueTake();
			}
		});
		quxiao.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				window.dismiss();
			}
		});
		// popWindow消失监听方法
		window.setOnDismissListener(new OnDismissListener() {

			@Override
			public void onDismiss() {

			}
		});

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
//		ToastUtil.shortToast(DownEscoreDartActivity.this, "1111111  "+data);
		switch (requestCode) {
		// 如果是直接从相册获取
		case 1:
			if (resultCode == RESULT_OK) {
				cropPhoto(data.getData());// 裁剪图片
			}
			break;
		// 如果是调用相机拍照时
		case 2:
			if (resultCode == RESULT_OK) {
				File temp = new File(Environment.getExternalStorageDirectory() + "/dart.png");
				cropPhoto(Uri.fromFile(temp));// 裁剪图片
			}
			break;

		// 取得裁剪后的图片
		case 3:
//			if (resultCode == RESULT_OK) {
			Log.e("1111tututuutut  ", ""+data);
////			}
			if (data != null) {
				Bundle extras = data.getExtras();
				head = extras.getParcelable("data");
				Log.e("img.1111111"  ,""+head);
				if (head != null) {
					/**
					 * 上传服务器代码 //TODO 实现头衔上传
					 */
					Logger.e("img.1111111");
					setPicToView(head);
					map.put("userId",
							String.valueOf(PreferencesUtils.getInt(getApplicationContext(), PreferenceConstants.UID)));
					map.put("fileName", "idcard");
					map_file.put("file", new File(fileName));
					// String result="";
					
					sendEmptyBackgroundMessage(MsgConstants.MSG_01);
					btn_image.setBackgroundDrawable(null);
					btn_image.setImageBitmap(head);
				}
			}
			break;
		case 11:
			if (resultCode == RESULT_OK) {
				cropPhototwo(data.getData());// 裁剪图片
			}
			break;
		// 如果是调用相机拍照时
		case 22:
			if (resultCode == RESULT_OK) {
				File temp = new File(Environment.getExternalStorageDirectory() + "/comp.png");
				cropPhototwo(Uri.fromFile(temp));// 裁剪图片
			}
			break;

		// 取得裁剪后的图片
		case 33:
//			if (resultCode == RESULT_OK) {
			Log.e("1111tututuutut  ", ""+data);
////			}
			if (data != null) {
				Bundle extras = data.getExtras();
				head1 = extras.getParcelable("data");
				Log.e("img.1111111"  ,""+head1);
				if (head1 != null) {
					/**
					 * 上传服务器代码 //TODO 实现头衔上传
					 */
					Logger.e("img.1111111");
					setPicToViewtwo(head1);
					map.put("userId",
							String.valueOf(PreferencesUtils.getInt(getApplicationContext(), PreferenceConstants.UID)));
					map.put("fileName1", "idcard");
					map_file.put("file", new File(fileName1));
					// String result="";
					sendEmptyBackgroundMessage(MsgConstants.MSG_02);
					btn_comp_image.setBackgroundDrawable(null);
					btn_comp_image.setImageBitmap(head);
				}
			}
			break;
		case 113:
				if (data!=null) {
					if (data.getStringExtra("type").equals("1")) {
//						finish();	
						Intent intent=new Intent();
						intent.putExtra("type", "1");
						setResult(RESULT_OK, intent);
						finish();
					
				}
				}
			break;
		default:
			break;
		}
	
	};
	/***
	 * 裁剪图片方法实现
	 * 
	 * @param uri
	 */
	public void cropPhoto(Uri uri) {
		Intent intent = new Intent("com.android.camera.action.CROP");
		intent.setDataAndType(uri, "image/*");
		intent.putExtra("crop", "true");
		// aspectX aspectY 是宽高的比例
		intent.putExtra("aspectX", 1.5);
		intent.putExtra("aspectY", 1);
		// outputX outputY 是裁剪图片宽高
		intent.putExtra("outputX", 350);
		intent.putExtra("outputY", 350);
		intent.putExtra("return-data", true);
		startActivityForResult(intent, 3);
	}
	/***
	 * 裁剪图片方法实现
	 * 
	 * @param uri
	 */
	public void cropPhototwo(Uri uri) {
		Intent intent = new Intent("com.android.camera.action.CROP");
		intent.setDataAndType(uri, "image/*");
		intent.putExtra("crop", "true");
		// aspectX aspectY 是宽高的比例
		intent.putExtra("aspectX", 1.5);
		intent.putExtra("aspectY", 1);
		// outputX outputY 是裁剪图片宽高
		intent.putExtra("outputX", 350);
		intent.putExtra("outputY", 350);
		intent.putExtra("return-data", true);
		startActivityForResult(intent, 33);
	}
	
	@Override
	public void handleBackgroundMessage(Message msg) {
		super.handleBackgroundMessage(msg);
		switch (msg.what) {
		case MsgConstants.MSG_01:
			try {
				result = post(MCUrl.Illegal, map, map_file);
				sendEmptyUiMessage(MsgConstants.MSG_01);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			break;
		case MsgConstants.MSG_02:
			try {
				result1 = post(MCUrl.Accusation, map, map_file);
				sendEmptyUiMessage(MsgConstants.MSG_02);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			break;
		default:
			break;
		}
	}
	@Override
	public void handleUiMessage(Message msg) {
		// TODO Auto-generated method stub
		super.handleUiMessage(msg);
		switch (msg.what) {
		case MsgConstants.MSG_01:
			Log.e("result", result);
			IconBean bean = new Gson().fromJson(result, IconBean.class);
			if (bean.getErrCode()==0) {
			if (bean.data.size() != 0) {
				Log.e("filePath", bean.data.get(0).filePath);
				ToastUtil.shortToast(DownEscoreDartActivity.this, "上传成功");
				icon = bean.data.get(0).filePath;
//				PreferencesUtils.putString(getApplicationContext(), PreferenceConstants.ICON_PATH, icon);
//				loader.displayImage(icon, com_img, options);
				btn_image.setBackgroundDrawable(null);
				btn_image.setImageBitmap(head);
			}
			} else if (bean.getErrCode()==-2) {
				
				ToastUtil.shortToast(DownEscoreDartActivity.this, "请勿重复上传");
			}else if (bean.getErrCode()==-3) {
				ToastUtil.shortToast(DownEscoreDartActivity.this, "请勿重复上传");
			}
			break;
		case MsgConstants.MSG_02:
			Log.e("result", result1);
			IconBean bean1 = new Gson().fromJson(result1, IconBean.class);
			if (bean1.getErrCode()==0) {
			if (bean1.data.size() != 0) {
				frist=true;
				Log.e("filePath", bean1.data.get(0).filePath);
				ToastUtil.shortToast(DownEscoreDartActivity.this, "上传成功");
				icon1 = bean1.data.get(0).filePath;
//				PreferencesUtils.putString(getApplicationContext(), PreferenceConstants.ICON_PATH, icon);
//				loader.displayImage(icon, com_img, options);
				btn_comp_image.setBackgroundDrawable(null);
				btn_comp_image.setImageBitmap(head1);
			}
			} else if (bean1.getErrCode()==-2) {
				
				ToastUtil.shortToast(DownEscoreDartActivity.this, "请勿重复上传");
			}else if (bean1.getErrCode()==-3) {
				ToastUtil.shortToast(DownEscoreDartActivity.this, "请勿重复上传");
			}
			break;
		default:
			break;
		}
	}
	
	/**
	 * 保存裁剪之后的图片数据
	 * 
	 * @param head2
	 */

	private void setPicToView(Bitmap mBitmap) {
		String sdStatus = Environment.getExternalStorageState();
		if (!sdStatus.equals(Environment.MEDIA_MOUNTED)) { // 检测sd是否可用
			return;
		}
		FileOutputStream b = null;
		File file = new File(path);
		file.mkdirs();// 创建文件夹
		try {
			b = new FileOutputStream(fileName);
			mBitmap.compress(Bitmap.CompressFormat.PNG, 100, b);// 把数据写入文件
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} finally {
			try {
				// 关闭流
				b.flush();
				b.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	/**
	 * 保存裁剪之后的图片数据
	 * 
	 * @param head2
	 */

	private void setPicToViewtwo(Bitmap mBitmap) {
		String sdStatus = Environment.getExternalStorageState();
		if (!sdStatus.equals(Environment.MEDIA_MOUNTED)) { // 检测sd是否可用
			return;
		}
		FileOutputStream b = null;
		File file = new File(path);
		file.mkdirs();// 创建文件夹
		fileName = path + "comp.png";
		try {
			b = new FileOutputStream(fileName);
			mBitmap.compress(Bitmap.CompressFormat.PNG, 100, b);// 把数据写入文件
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} finally {
			try {
				// 关闭流
				b.flush();
				b.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 通过拼接的方式构造请求内容，实现参数传输以及文件传输
	 * 
	 * @param actionUrl
	 *            访问的服务器URL
	 * @param params
	 *            普通参数
	 * @param files
	 *            文件参数
	 * @return
	 * @return
	 * @throws IOException
	 */
	public static String post(String actionUrl, Map<String, String> params, Map<String, File> files)
			throws IOException {

		StringBuilder sb2 = new StringBuilder();

		String BOUNDARY = java.util.UUID.randomUUID().toString();
		String PREFIX = "--", LINEND = "\r\n";
		String MULTIPART_FROM_DATA = "multipart/form-data";
		String CHARSET = "UTF-8";

		URL uri = new URL(actionUrl);
		HttpURLConnection conn = (HttpURLConnection) uri.openConnection();
		conn.setReadTimeout(5 * 1000); // 缓存的最长时间
		conn.setDoInput(true);// 允许输入
		conn.setDoOutput(true);// 允许输出
		conn.setUseCaches(false); // 不允许使用缓存
		conn.setRequestMethod("POST");
		conn.setRequestProperty("connection", "keep-alive");
		conn.setRequestProperty("Charsert", "UTF-8");
		conn.setRequestProperty("Content-Type", MULTIPART_FROM_DATA + ";boundary=" + BOUNDARY);

		// 首先组拼文本类型的参数
		StringBuilder sb = new StringBuilder();
		for (Map.Entry<String, String> entry : params.entrySet()) {
			sb.append(PREFIX);
			sb.append(BOUNDARY);
			sb.append(LINEND);
			sb.append("Content-Disposition: form-data; name=\"" + entry.getKey() + "\"" + LINEND);
			sb.append("Content-Type: text/plain; charset=" + CHARSET + LINEND);
			sb.append("Content-Transfer-Encoding: 8bit" + LINEND);
			sb.append(LINEND);
			sb.append(entry.getValue());
			sb.append(LINEND);
		}

		DataOutputStream outStream = new DataOutputStream(conn.getOutputStream());
		outStream.write(sb.toString().getBytes());
		InputStream in = null;
		// 发送文件数据
		if (files != null) {
			for (Map.Entry<String, File> file : files.entrySet()) {
				StringBuilder sb1 = new StringBuilder();
				sb1.append(PREFIX);
				sb1.append(BOUNDARY);
				sb1.append(LINEND);
				// name是post中传参的键 filename是文件的名称
				sb1.append(
						"Content-Disposition: form-data; name=\"file\"; filename=\"" + file.getKey() + "\"" + LINEND);
				sb1.append("Content-Type: application/octet-stream; charset=" + CHARSET + LINEND);
				sb1.append(LINEND);
				outStream.write(sb1.toString().getBytes());
				InputStream is = new FileInputStream(file.getValue());
				byte[] buffer = new byte[1024];
				int len = 0;
				while ((len = is.read(buffer)) != -1) {
					outStream.write(buffer, 0, len);
				}
				is.close();
				outStream.write(LINEND.getBytes());
			}

			// 请求结束标志
			byte[] end_data = (PREFIX + BOUNDARY + PREFIX + LINEND).getBytes();
			outStream.write(end_data);
			outStream.flush();
			// 得到响应码
			int res = conn.getResponseCode();
			if (res == 200) {
				in = conn.getInputStream();
				int ch;

				while ((ch = in.read()) != -1) {
					sb2.append((char) ch);
				}
			}
			outStream.close();
			conn.disconnect();
		}
		return sb2.toString();
	}

	public InputStream getStream(File file) {
		// 第2步、通过子类实例化父类对象
		// File f= new File("d:" + File.separator + "test.txt") ; // 声明File对象
		// 第2步、通过子类实例化父类对象
		InputStream input = null; // 准备好一个输入的对象
		try {
			input = new FileInputStream(file); // 通过对象多态性，进行实例化
			// 第3步、进行读操作
			// byte b[] = new byte[input..available()] ; 跟使用下面的代码是一样的
			byte b[] = new byte[(int) file.length()]; // 数组大小由文件决定
			int len = input.read(b); // 读取内容
			// 第4步、关闭输出流

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				input.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return input;
	}

	private boolean fileIsExist(String path) {
		File file = new File(path);
		if (file.exists()) {
			return true;
		}
		return false;
	}

	private void fileDelete(String path) {

		if (fileIsExist(path)) {
			File file = new File(path);
			file.delete();
		}
	}
	public Bitmap getRoundedCornerBitmap(Bitmap bitmap, float roundPx) {

		Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Config.ARGB_8888);
		Canvas canvas = new Canvas(output);

		final int color = 0xff424242;
		final Paint paint = new Paint();
		final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
		final RectF rectF = new RectF(rect);

		paint.setAntiAlias(true);
		canvas.drawARGB(0, 0, 0, 0);
		paint.setColor(color);
		canvas.drawRoundRect(rectF, roundPx, roundPx, paint);

		paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
		canvas.drawBitmap(bitmap, rect, rect, paint);

		return output;
	}
	
	private void showPopupmaP( final String litude ,final String flotitude ,final String adds ){
		TextView gaode;
		TextView baidu;
		TextView tengxu;
		TextView exit;
//		private Button	btn_comp_sumit;
		vPopupWindow_complain = LayoutInflater.from(this).inflate(R.layout.mypopwindow_map, null);
//		ll_report = (LinearLayout) vPopupWindow_report.findViewById(R.id.ll_setChecked);
		exit = (TextView) vPopupWindow_complain.findViewById(R.id.exit);
		gaode = (TextView) vPopupWindow_complain.findViewById(R.id.gaode);
		baidu = (TextView) vPopupWindow_complain.findViewById(R.id.baidu);
		tengxu = (TextView) vPopupWindow_complain.findViewById(R.id.tengxu);
		popupWindow_complain = new PopupWindow(vPopupWindow_complain, LayoutParams.MATCH_PARENT,
				LayoutParams.MATCH_PARENT, true);
		popupWindow_complain.setAnimationStyle(R.style.mypopwindow_anim_style);
		popupWindow_complain.showAtLocation(DownEscoreDartActivity.this.findViewById(R.id.btn_escort), Gravity.CENTER, 0, 0);
		// 设置背景 使返回键生效（PopupWindow会拦截触摸事件，包括返回键和菜单键）
		popupWindow_complain.setBackgroundDrawable(new ColorDrawable(0x99999999));
		//高德
		gaode.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				   /**
			     * 我的位置BY高德
			     */
				String fla=getIntent().getStringExtra("fromLatitude");
				String flo=getIntent().getStringExtra("fromLongitude");
				String number = fla;
				String intNumber = number.substring(0,number.indexOf(".")+3);
				String number2 = flo;
				String intNumber2 = number2.substring(0,number2.indexOf(".")+3);
//				ToastUtil.shortToast(DownEscoreDartActivity.this, ""+intNumber2);
				String tfla=getIntent().getStringExtra("toLatitude");
				String tflo=getIntent().getStringExtra("toLongitude");
				String tnumber = tfla;
				String tintNumber = tnumber.substring(0,tnumber.indexOf(".")+3);
				String tnumber2 = tflo;
				String tintNumber2 = tnumber2.substring(0,tnumber2.indexOf(".")+3);
			        try {
			             Intent intent = Intent.getIntent("androidamap://route?sourceApplication=softname&slat="+latitude+"&slon="+longitude+"&sname="+address+"&dlat="+litude+"&dlon="+flotitude+"&dname="+addressTo+"&dev=0&m=0&t=1");
//			        	 Intent intent = Intent.getIntent("intent://map/direction?origin="+address+"&destination="+addressTo+"&mode=driving&src="+DownEscoreDartActivity.this+"|iWant#Intent;scheme=bdapp;package=com.baidu.BaiduMap;end");
			        	if(isInstallByread("com.autonavi.minimap")){//com.baidu.BaiduMap com.autonavi.minimap
			                startActivity(intent);
//			                Log.e(TAG, "高德地图客户端已经安装") ;
			            }else {
//			                Log.e(TAG, "没有安装高德地图客户端") ;
			            	ToastUtil.shortToast(DownEscoreDartActivity.this, "没有安装高德地图客户端");
			            }
			        } catch (URISyntaxException e) {
			            e.printStackTrace();
			        }
				popupWindow_complain.dismiss();
			}
		});
		//百度
		baidu.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				   /**
			     * 我的位置BY高德
			     */
				String fla=getIntent().getStringExtra("fromLatitude");
				String flo=getIntent().getStringExtra("fromLongitude");
				String number = fla;
				String intNumber = number.substring(0,number.indexOf(".")+3);
				String number2 = flo;
				String intNumber2 = number2.substring(0,number2.indexOf(".")+3);
//				ToastUtil.shortToast(DownEscoreDartActivity.this, ""+intNumber2);
				String tfla=getIntent().getStringExtra("toLatitude");
				String tflo=getIntent().getStringExtra("toLongitude");
				String tnumber = tfla;
				String tintNumber = tnumber.substring(0,tnumber.indexOf(".")+3);
				String tnumber2 = tflo;
				String tintNumber2 = tnumber2.substring(0,tnumber2.indexOf(".")+3);
			        try {
//			             Intent intent = Intent.getIntent("androidamap://route?sourceApplication=softname&slat="+intNumber+"&slon="+intNumber2+"&sname="+address+"&dlat="+tintNumber+"&dlon="+tintNumber2+"&dname="+addressTo+"&dev=0&m=0&t=1");
			        	 Intent intent = Intent.getIntent("intent://map/direction?origin="+saddse+"&destination="+adds+"&mode=driving&src="+DownEscoreDartActivity.this+"|iWant#Intent;scheme=bdapp;package=com.baidu.BaiduMap;end");
			        	if(isInstallByread("com.baidu.BaiduMap")){//com.baidu.BaiduMap com.autonavi.minimap
			                startActivity(intent);
			            }else {
			            	ToastUtil.shortToast(DownEscoreDartActivity.this, "没有安装百度地图客户端");
			            }
			        } catch (URISyntaxException e) {
			            e.printStackTrace();
			        }
				popupWindow_complain.dismiss();
			}
		});
		//腾讯
		tengxu.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				   /**
			     * 我的位置BY高德
			     */
				String fla=getIntent().getStringExtra("fromLatitude");
				String flo=getIntent().getStringExtra("fromLongitude");
				String number = fla;
				String intNumber = number.substring(0,number.indexOf(".")+3);
				String number2 = flo;
				String intNumber2 = number2.substring(0,number2.indexOf(".")+3);
//				ToastUtil.shortToast(DownEscoreDartActivity.this, ""+intNumber2);
				String tfla=getIntent().getStringExtra("toLatitude");
				String tflo=getIntent().getStringExtra("toLongitude");
				String tnumber = tfla;
				String tintNumber = tnumber.substring(0,tnumber.indexOf(".")+3);
				String tnumber2 = tflo;
				String tintNumber2 = tnumber2.substring(0,tnumber2.indexOf(".")+3);
			        try {
//			             Intent intent = Intent.getIntent("androidamap://route?sourceApplication=softname&slat="+intNumber+"&slon="+intNumber2+"&sname="+address+"&dlat="+tintNumber+"&dlon="+tintNumber2+"&dname="+addressTo+"&dev=0&m=0&t=1");
			        	 Intent intent = Intent.getIntent("qqmap://map/routeplan?type=drive&from="+address+"&fromcoord="+latitude+","+longitude+"&to="+addressTo+"&tocoord="+litude+","+flotitude+"");
			        		//移动端启动腾讯地图App，并显示从出发点[天坛南门] 到 [目的地坐标(国家大剧院)] 的驾车路线规划
			        	 if(isInstallByread("com.tencent.map")){//com.baidu.BaiduMap com.autonavi.minimap
			                startActivity(intent);
//			                Log.e(TAG, "高德地图客户端已经安装") ;
			            }else {
//			                Log.e(TAG, "没有安装高德地图客户端") ;
			            	ToastUtil.shortToast(DownEscoreDartActivity.this, "没有安装腾讯地图客户端");
			            }
			        } catch (URISyntaxException e) {
			            e.printStackTrace();
			        }
				popupWindow_complain.dismiss();
			}
		});
		exit.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				popupWindow_complain.dismiss();
			}
		});
		popupWindow_complain.setOnDismissListener(new OnDismissListener() {

			@Override
			public void onDismiss() {
				popupWindow_complain.dismiss();
			}
		});
		
		
	}

	// 点击空白区域关闭输入法
//		public boolean onTouchEvent(MotionEvent event) {
//			InputMethodManager inputMethodManager = (InputMethodManager)getActivity(). getSystemService(Context.INPUT_METHOD_SERVICE);
//			inputMethodManager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(),
//					InputMethodManager.HIDE_NOT_ALWAYS);
//			return super.onTouchEvent(event);
//		}
}
