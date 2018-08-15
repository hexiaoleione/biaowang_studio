package com.hex.express.iwant.activities;

import android.app.AlertDialog.Builder;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Message;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alipay.sdk.app.PayTask;
import com.google.gson.Gson;
import com.hex.express.iwant.R;
import com.hex.express.iwant.alipay.AlipayUtils;
import com.hex.express.iwant.alipay.PayResult;
import com.hex.express.iwant.bean.BaseBean;
import com.hex.express.iwant.bean.DepotBean;
import com.hex.express.iwant.bean.DiscountBean;
import com.hex.express.iwant.bean.LogisBean;
import com.hex.express.iwant.bean.RegisterBean;
import com.hex.express.iwant.bean.WechatBean;
import com.hex.express.iwant.constance.CollectionKey;
import com.hex.express.iwant.constance.MCUrl;
import com.hex.express.iwant.constance.MsgConstants;
import com.hex.express.iwant.constance.PreferenceConstants;
import com.hex.express.iwant.http.AsyncHttpUtils;
import com.hex.express.iwant.http.UrlMap;
import com.hex.express.iwant.iWantApplication;
import com.hex.express.iwant.utils.MessageUtils;
import com.hex.express.iwant.utils.PreferencesUtils;
import com.hex.express.iwant.utils.ToastUtil;
import com.hex.express.iwant.views.DialogUtils;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.tencent.mm.sdk.modelpay.PayReq;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;

import butterknife.Bind;
import butterknife.ButterKnife;
/**
 * 
 * @author huyichuan
 *  物流公司报价详情
 */
public class LogCompanyActivity extends BaseActivity{

	@Bind(R.id.btnLeft)
	ImageView btnLeft;
	@Bind(R.id.spec_huoname)
	TextView spec_huoname;
	@Bind(R.id.spec_zhong)
	TextView spec_zhong;
	@Bind(R.id.spec_tiji)
	TextView spec_tiji;
	@Bind(R.id.spec_jiazhi)//价值
	TextView spec_jiazhi;
	@Bind(R.id.spec_quhuo)
	TextView spec_quhuo;
	@Bind(R.id.spec_songhuo)
	TextView spec_songhuo;
	@Bind(R.id.spec_fahuodizhi)
	TextView spec_fahuodizhi;
	@Bind(R.id.spec_dadaodizhi)
	TextView spec_dadaodizhi;
	@Bind(R.id.spec_shouhuoname)
	TextView spec_shouhuoname;
	@Bind(R.id.spec_lianxifangshi)//
	TextView spec_lianxifangshi;
	@Bind(R.id.spec_fahuoname)
	TextView spec_fahuoname;
	@Bind(R.id.spec_falianxifangshi)//
	TextView spec_falianxifangshi;
	@Bind(R.id.spec_fahuotime)//
	TextView spec_fahuotime;
	@Bind(R.id.spec_dadaotime)//
	TextView spec_dadaotime;
	@Bind(R.id.spec_beizhu)//
	TextView spec_beizhu;
	@Bind(R.id.spec_beizhus)//
	EditText spec_beizhus;
	@Bind(R.id.spec_edt_yuan)//报价多少元
	EditText spec_edt_yuan;
	@Bind(R.id.deta_but)//报价
	Button deta_but;
	@Bind(R.id.spec_huoaddse)//货场地址
	EditText spec_huoaddse;
	@Bind(R.id.add_receiver) 
	ImageView add_receiver;
	
	@Bind(R.id.editText1)//上门取货费
	EditText editText1;
	@Bind(R.id.editText2)//送货上门费
	EditText editText2;
	@Bind(R.id.text_total)//总价
	TextView text_total;
	@Bind(R.id.spec_jian)//
	TextView spec_jian;
	@Bind(R.id.huo)//
	RelativeLayout huo;
	
	@Bind(R.id.spec_carcold)
	TextView spec_carcold;
	
	
	
	@Bind(R.id.r1)
	RelativeLayout r1;// 上门取货费
	@Bind(R.id.r2)
	RelativeLayout r2;//送货上门费
	@Bind(R.id.r3)
	RelativeLayout r3;//运费
	
	
	@Bind(R.id.lysonghuo)
	LinearLayout lysonghuo;
	@Bind(R.id.lycoldcar)
	LinearLayout lycoldcar;
	@Bind(R.id.tems)
	TextView tems;

	
	private boolean qu;
	private boolean song;
	private LogisBean lobean;
	int  cargoTotal,takeCargoMoney,sendCargoMoney;
	
	PayReq req;
	private String orderInfo;
	private String result;
	final IWXAPI msgApi = WXAPIFactory.createWXAPI(this, null);
	private MyReceiver reMyrecive;
	private Object needPayMoney;
	private DiscountBean bean;
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		iWantApplication.getInstance().addActivity(this);
		//activity_logistical_xiangqing   activity_logistical_xiqing
		setContentView(R.layout.activity_newlogis_xiqing);// activity_newlogis_xiqing  activity_logistical_xiqing
		ButterKnife.bind(LogCompanyActivity.this);
		req = new PayReq();
		msgApi.registerApp(CollectionKey.APP_ID);
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
		reMyrecive = new MyReceiver();
		IntentFilter filter = new IntentFilter();
		filter.addAction("recharge");
		registerReceiver(reMyrecive, filter);
		getDiscount();
		DefaultComAdd();
		if (getIntent().getStringExtra("way")!=null) {
			if (getIntent().getStringExtra("way").equals("2")) {
				deta_but.setVisibility(View.GONE);
			}
		}
		
		
		// TODO Auto-generated method stub
//		spec__img_head.setImageURI("");
		spec_fahuoname.setText("发件人："+getIntent().getStringExtra("sendName"));
		spec_falianxifangshi.setText(""+getIntent().getStringExtra("sendMobile"));
//		spec_logoffer_time.setText(getIntent().getStringExtra("publishTime"));
//		spec_logoffer_addes.setText("发货次数："+getIntent().getStringExtra("sendNumber"));
//		spec_logoffer_phone.setText(getIntent().getStringExtra(""));
//		spec_juli.setText(getIntent().getStringExtra(""));
		spec_huoname.setText("货物名称："+getIntent().getStringExtra("cargoName"));
//		if (getIntent().getStringExtra("cargoWeight").equals("5")) {
//			spec_zhong.setText("总重量：≤"+getIntent().getStringExtra("cargoWeight")+"公斤");
//		}else {
			spec_zhong.setText("总重量："+getIntent().getStringExtra("cargoWeight")+"");
//		}
		spec_tiji.setText("总体积："+getIntent().getStringExtra("cargoVolume"));
//		spec_tiji.setText("单件规格： 长："+getIntent().getStringExtra("length")+" 厘米 "+" 宽："+getIntent().getStringExtra("wide")+" 厘米 "+" 高："+getIntent().getStringExtra("high")+" 厘米 ");
//		spec_jiazhi.setText("货物价值："+getIntent().getStringExtra("cargoCost")+"元");
//		spec_quhuo.setText(getIntent().getStringExtra(""));
//		spec_songhuo.setText(getIntent().getStringExtra(""));
		spec_fahuodizhi.setText("始发地："+getIntent().getStringExtra("startPlace"));
		spec_dadaodizhi.setText("目的地："+getIntent().getStringExtra("entPlace"));
		spec_shouhuoname.setText("收货人："+getIntent().getStringExtra("takeName"));
		spec_lianxifangshi.setText(""+getIntent().getStringExtra("takeMobile"));
		spec_fahuotime.setText("发货时间："+getIntent().getStringExtra("takeTime"));
		spec_dadaotime.setText("要求到达时间："+getIntent().getStringExtra("arriveTime"));
	//	appontSpace
		if ("cold".equals(getIntent().getStringExtra("carType"))) {
			spec_carcold.setText("需求："+getIntent().getStringExtra("carName"));
		}
//		if (!"".equals(getIntent().getStringExtra("cargoNumber"))) {
			spec_jian.setText("件数："+getIntent().getStringExtra("cargoNumber")+"件");
//		}else {
//			spec_jian.setVisibility(View.GONE);
//		}
			if ("cold".equals(getIntent().getStringExtra("carType"))) {
				lycoldcar.setVisibility(View.VISIBLE);
				lysonghuo.setVisibility(View.GONE);
				tems.setText("温度要求："+getIntent().getStringExtra("tem"));
				r1.setVisibility(View.GONE);
				r2.setVisibility(View.GONE);
			}else {
				r1.setVisibility(View.VISIBLE);
				r2.setVisibility(View.VISIBLE);
				lycoldcar.setVisibility(View.GONE);
				lysonghuo.setVisibility(View.VISIBLE);
			}
		if (!"".equals(getIntent().getStringExtra("appontSpace"))) {
			spec_beizhu.setText("指定园区："+getIntent().getStringExtra("appontSpace"));
		}else {
			spec_beizhu.setText("");
		}
		
		if(getIntent().getBooleanExtra("takeCargo",false)){
			spec_quhuo.setText("物流公司上门取货");
			huo.setVisibility(View.GONE);
			spec_huoaddse.setVisibility(View.GONE);
//			ToastUtil.shortToast(LogCompanyActivity.this, ""+getIntent().getBooleanExtra("takeCargo",true));
		}else {
			spec_quhuo.setText("发货人送到货场");
			r1.setVisibility(View.GONE);
			huo.setVisibility(View.VISIBLE);
			spec_huoaddse.setVisibility(View.VISIBLE);
//			ToastUtil.shortToast(LogCompanyActivity.this, ""+getIntent().getBooleanExtra("takeCargo",true));
		}
		if(getIntent().getBooleanExtra("sendCargo",true)){
			spec_songhuo.setText("物流公司送货上门");
		}else {
			spec_songhuo.setText("收件人自提");
			r2.setVisibility(View.GONE);
		}
		if ("cold".equals(getIntent().getStringExtra("carType"))) {
			r1.setVisibility(View.GONE);
			r2.setVisibility(View.GONE);
		}
		
		if (getIntent().getBooleanExtra("ifQuotion", false)) {
			spec_beizhus.setText(getIntent().getStringExtra("luMessage"));
			spec_huoaddse.setText(getIntent().getStringExtra("yardAddress"));	
			String	cte=getIntent().getStringExtra("transferMoney");
			String[] stre=cte.split("\\.");
			text_total.setText(""+Integer.parseInt(stre[0]));
			deta_but.setBackgroundResource(R.drawable.xiugaibaojia_new);
			if (getIntent().getStringExtra("cargoTotal")!=null && !getIntent().getStringExtra("cargoTotal").equals("0.0")) {
				String	ct=getIntent().getStringExtra("cargoTotal");
				String[] strs=ct.split("\\.");
				spec_edt_yuan.setText(""+Integer.parseInt(strs[0]));
			}
			if (getIntent().getStringExtra("takeCargoMoney")!=null && !getIntent().getStringExtra("takeCargoMoney").equals("0.0")) {
				String	ct=getIntent().getStringExtra("takeCargoMoney");
				String[] strs=ct.split("\\.");
				editText1.setText(""+Integer.parseInt(strs[0]));
			}
			if (getIntent().getStringExtra("sendCargoMoney")!=null && !getIntent().getStringExtra("sendCargoMoney").equals("0.0")) {
				String	ct=getIntent().getStringExtra("sendCargoMoney");
				String[] strs=ct.split("\\.");
				editText2.setText(""+Integer.parseInt(strs[0]));
			}
		}
		
		if (spec_huoaddse.getText().toString().equals("")) {
			spec_huoaddse.setVisibility(View.GONE);
		}
//		spec_huoaddse.setFocusable(false);
	}

	@Override
	public void initData() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setOnClick() {
		// TODO Auto-generated method stub
		btnLeft.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intent1 = new Intent();
//				intent1.putExtra("types", "1");
				LogCompanyActivity.this.setResult(RESULT_OK, intent1);
				finish();
			}
		});
		add_receiver.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(LogCompanyActivity.this, DepotActivity.class);
			 	  
			 		startActivityForResult(intent, 1);
			}
		});
		
		deta_but.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				
				
				if (getIntent().getBooleanExtra("ifQuotion", false)) {
//					quotation/update
					//运费
					if (spec_edt_yuan.getText().toString().equals("")) {
						cargoTotal=0;
					}else {
						String ct= spec_edt_yuan.getText().toString();
						String[] strs=ct.split("\\.");
						cargoTotal=	Integer.parseInt(strs[0]);
					}
				//上门取货费
					if (editText1.getText().toString().equals("")) {
						takeCargoMoney=0;
					}else {
						String tm= editText1.getText().toString();
						String[] strs=tm.split("\\.");
						takeCargoMoney=	Integer.parseInt(strs[0]);
					}
				//送货上门费
					if (editText2.getText().toString().equals("")) {
						sendCargoMoney=0;
					}else {
						String sm=editText2.getText().toString();
						String[] strs=sm.split("\\.");
						sendCargoMoney=	Integer.parseInt(strs[0]);
					}
					text_total.setText(""+subadd(cargoTotal,takeCargoMoney,sendCargoMoney));
					//修改报价
					addPostResultre();
//					getData();
				}else {
					//运费
					if (spec_edt_yuan.getText().toString().equals("")) {
						cargoTotal=0;
					}else {
						String ct= spec_edt_yuan.getText().toString();
						cargoTotal=	Integer.parseInt(ct);
					}
				//上门取货费
					if (editText1.getText().toString().equals("")) {
						takeCargoMoney=0;
					}else {
						String tm= editText1.getText().toString();
						takeCargoMoney=	Integer.parseInt(tm);
					}
				//送货上门费
					if (editText2.getText().toString().equals("")) {
						sendCargoMoney=0;
					}else {
						String sm=editText2.getText().toString();
						sendCargoMoney=	Integer.parseInt(sm);
					}
					text_total.setText(""+subadd(cargoTotal,takeCargoMoney,sendCargoMoney));
//					Log.e("111122", ""+getIntent().getStringExtra("transferMoney"));
					tijiao();
				}
				
			}
		});
		
		text_total.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				// TODO Auto-generated method stub
			
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
		            int after) {
				// TODO Auto-generated method stub
			}
			
			@Override
			public void afterTextChanged(Editable arg0) {
				// TODO Auto-generated method stub
			}
		});
		
	}
	public void tijiao() {
		// TODO Auto-generated method stub
		Builder ad = new Builder(LogCompanyActivity.this);
		ad.setTitle("温馨提示");
		ad.setMessage("参与报价，用户选择后，对接成功将收取平台使用费。您确定要报价吗？");
		ad.setPositiveButton("确认", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface arg0, int arg1) {
				addPostResult();
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


//	public void getData() {
//		// TODO Auto-generated method stub
//		Intent intent = new Intent(LogCompanyActivity.this, LogisPayActivity.class);
//	 	   intent.putExtra("evaluationStatus","10");//余额支付 的价格	
////	 		intent.putExtra("insureCost", String.valueOf(df.format(bean.data.get(0).insureCost)));
//	 		intent.putExtra("evaluationScore","20");//其它支付的价格
//	 		intent.putExtra("billCode", "20161110");//物流单号
//	 		intent.putExtra("way", "2");
//	 		intent.putExtra("transferMoney", "2");
////	 		intent.putExtra("transferMoney", Integer.parseInt(spec_edt_yuan.getText().toString()));
//	 		startActivityForResult(intent, 10);
//		
//	}
	private void addPostResult() {
		JSONObject obj = new JSONObject();
		if( spec_edt_yuan.getText().toString().equals("")
				){
			ToastUtil.shortToast(getApplicationContext(), "请填写完资料");
			return;
		}
		if(getIntent().getBooleanExtra("takeCargo",false)){
			
//			ToastUtil.shortToast(LogCompanyActivity.this, ""+getIntent().getBooleanExtra("takeCargo",true));
		}else {
			if( spec_huoaddse.getText().toString().equals("")
					){
				ToastUtil.shortToast(getApplicationContext(), "请填写完资料");
				return;
			}
			
		}
		
//	int stringta=editText1.setText(""+getIntent().getStringExtra("takeCargoMoney"));
		try {
			obj.put("WLBId", getIntent().getIntExtra("recId", 0));//备注
			obj.put("userId",
					String.valueOf(PreferencesUtils.getInt(getApplicationContext(), PreferenceConstants.UID)));
			obj.put("luMessage", spec_beizhus.getText().toString());//备注
			obj.put("yardAddress", spec_huoaddse.getText().toString());//货场地址
			obj.put("transferMoney", text_total.getText().toString());//总价
			obj.put("takeCargoMoney", takeCargoMoney);//取货费
			obj.put("sendCargoMoney", sendCargoMoney);//送货费
			obj.put("cargoTotal", cargoTotal);///运费
		} catch (JSONException e) {
			e.printStackTrace();
		}
		dialog.show();
		Log.e("查看数据修报价。。。", obj.toString());
		AsyncHttpUtils.doPostJson(LogCompanyActivity.this, MCUrl.COMPANY, obj.toString(),
				new AsyncHttpResponseHandler() {
					@Override
					public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
						Log.e("11111111111 wwww   ", new String(arg2));
						dialog.dismiss();
						lobean = new Gson().fromJson(new String(arg2), LogisBean.class);
//						Log.e("oppop", bean.data.toString());
						if(lobean.getErrCode()==0){
							
//							sendEmptyUiMessage(MsgConstants.MSG_02);
//						Intent intent = new Intent(LogCompanyActivity.this, LogisPayActivity.class);
//					 	   intent.putExtra("evaluationStatus",bean.getData().get(0).playMoneyMin);//余额支付 的价格	
//					 		intent.putExtra("evaluationScore", bean.getData().get(0).playMoneyMax);//其它支付的价格
//					 		intent.putExtra("billCode", getIntent().getStringExtra("billCode"));//物流单号
//					 		intent.putExtra("way", "2");
//					 		intent.putExtra("transferMoney", Integer.parseInt(spec_edt_yuan.getText().toString()));
//					 		startActivityForResult(intent, 10);
//						ToastUtil.shortToast(getApplicationContext(), lobean.getMessage());
							Intent intent1 = new Intent();
							setResult(RESULT_OK, intent1);
							finish();
					}
						ToastUtil.shortToast(getApplicationContext(), lobean.getMessage());
					}

					@Override
					public void onFailure(int arg0, Header[] arg1, byte[] arg2, Throwable arg3) {
						dialog.dismiss();
					}
				});

	}
	/**
	 * 修改报价
	 */
	private void addPostResultre() {
		JSONObject obj = new JSONObject();
		
		try {
			obj.put("WLBId", getIntent().getIntExtra("recId", 0));//备注
			obj.put("userId",
					String.valueOf(PreferencesUtils.getInt(getApplicationContext(), PreferenceConstants.UID)));
			obj.put("luMessage",spec_beizhus.getText().toString());//备注spec_beizhus.getText().toString()  getIntent().getStringExtra("luMessage")
			obj.put("transferMoney",  text_total.getText().toString());//报价元
			obj.put("yardAddress",   spec_huoaddse.getText().toString());//货场地址
			obj.put("takeCargoMoney", editText1.getText().toString());//取货费 editText1.setText(""+getIntent().getStringExtra("takeCargoMoney"))
			obj.put("sendCargoMoney", editText2.getText().toString());//送货费
			obj.put("cargoTotal",  spec_edt_yuan.getText().toString());///运费
			
		} catch (JSONException e) {
			e.printStackTrace();
		}
		dialog.show();
		Log.e("查看数据", obj.toString());
		AsyncHttpUtils.doPostJson(LogCompanyActivity.this, MCUrl.COMPANYUP, obj.toString(),
				new AsyncHttpResponseHandler() {
					@Override
					public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
						Log.e("11111111111 wwww   ", new String(arg2));
						dialog.dismiss();
						LogisBean bean = new Gson().fromJson(new String(arg2), LogisBean.class);
//						Log.e("oppop", bean.data.toString());
						if(bean.getErrCode()==0){
//						ToastUtil.shortToast(getApplicationContext(), bean.getMessage());
							Intent intent = new Intent();
							LogCompanyActivity.this.setResult(RESULT_OK, intent);
							finish();

					}else {
						ToastUtil.shortToast(getApplicationContext(), bean.getMessage());
					}
					}

					@Override
					public void onFailure(int arg0, Header[] arg1, byte[] arg2, Throwable arg3) {
						dialog.dismiss();
					}
				});

	}
	
	private void DefaultComAdd(){
		RequestParams params=new RequestParams();
		AsyncHttpUtils.doGet(
				UrlMap.getUrl(MCUrl.DefaultComAdd, "userId",
						String.valueOf(PreferencesUtils.getInt(getApplicationContext(), PreferenceConstants.UID))),
				null, null, params, new AsyncHttpResponseHandler() {
			@Override
			public void onFailure(int arg0, Header[] arg1, byte[] arg2, Throwable arg3) {
				
			}
			@Override
			public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
				
				Log.e("111DefaultComAdd ", new String(arg2));
				DepotBean		bean = new Gson().fromJson(new String(arg2),
						DepotBean.class);
				if (bean.errCode==0) {
					if (bean.getData().size()>0) {// getIntent().getStringExtra("status").equals("1")
						if (getIntent().getBooleanExtra("ifQuotion", false)
								) {
//							spec_huoaddse.setVisibility(View.VISIBLE);
						}else {
							spec_huoaddse.setText(bean.getData().get(0).locationAddress+bean.getData().get(0).address);
//							spec_huoaddse.setVisibility(View.VISIBLE);
						}
					
					}
				
				}
				
				
			}
		});
	}
	
	@Override
	protected void onActivityResult(int arg0, int arg1, Intent arg2) {
		// TODO Auto-generated method stub
		super.onActivityResult(arg0, arg1, arg2);
		switch (arg0) {
		case 10:
			finish();
			break;
		case 1:
			if (arg1 == RESULT_OK) {
				String add= arg2.getStringExtra("locationAddress");
				String add1= arg2.getStringExtra("address");
				spec_huoaddse.setText(add+add1);
				spec_huoaddse.setVisibility(View.VISIBLE);
			}
			break;
		default:
			break;
		}
	}
	/**
	 * 获取钱包余额
	 */
	private void getrequstBalance() {
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
						RegisterBean bean = new Gson().fromJson(
						new String(arg2), RegisterBean.class); 
						DecimalFormat df = new DecimalFormat("######0");
					int i=Integer.parseInt(df.format(bean.data.get(0).balance));
					int j=Integer.parseInt(lobean.getData().get(0).playMoneyMin);
					   //钱包余额钱=0提示余额支付减免40
						if (i==0) {
							showwind(1);
							//钱包大于余额钱直接去支付
						}else  if (i>=j) {
							showin();
//							AlertDialog.Builder ad = new Builder(LogCompanyActivity.this);
//							ad.setTitle("温馨提示");
//							ad.setMessage("支付平台使用费"+lobean.getData().get(0).playMoneyMin+"元");
//							ad.setNegativeButton("确认", new DialogInterface.OnClickListener() {
//								@Override
//								public void onClick(DialogInterface arg0, int arg1) {
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
//							Intent intent = new Intent(LogCompanyActivity.this, LogisPayActivity.class);
//						 	   intent.putExtra("evaluationStatus",lobean.getData().get(0).playMoneyMin);//余额支付 的价格	
//						 		intent.putExtra("evaluationScore", lobean.getData().get(0).playMoneyMax);//其它支付的价格
//						 		intent.putExtra("billCode", getIntent().getStringExtra("billCode"));//物流单号
//						 		intent.putExtra("way", "2");
//						 		intent.putExtra("transferMoney", Integer.parseInt(text_total.getText().toString()));
//						 		intent.putExtra("takeCargoMoney", takeCargoMoney);
//						 		intent.putExtra("sendCargoMoney", sendCargoMoney);
//						 		intent.putExtra("cargoTotal", cargoTotal);
//						 		startActivityForResult(intent, 10);
							//钱包if(M<10)去充值
						}else if (i<j) {
							showwindow(2);
						}
//						wa_validBalance.setText(df.format(bean.data.get(0).balance));
					}

				});
	}
	/**
	 *余额为0 弹窗
	 * 
	 */
	public void showwindow(int item) {
		final	PopupWindow window02;
		TextView lc_conte,pin;
		Button lc_sumit,lc_exti,lc_sumitzhi;
		 final Intent intent=new Intent() ;
		// 利用layoutInflater获得View
		LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View view = inflater.inflate(R.layout.logcompanpopwind, null);
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
		window02.showAtLocation(LogCompanyActivity.this.findViewById(R.id.deta_but), Gravity.CENTER, 0, 0);
		lc_exti=(Button) view.findViewById(R.id.lc_exti);
		lc_conte=(TextView) view.findViewById(R.id.lc_conte);
		pin=(TextView) view.findViewById(R.id.pin);
		lc_sumit=(Button) view.findViewById(R.id.lc_sumit);
//		lc_sumitzhi=(Button) view.findViewById(R.id.lc_sumitzhi);//logcompanpopwind
		if (item==1) {
			int j=Integer.parseInt(lobean.getData().get(0).playMoneyMin);
			int s=Integer.parseInt(lobean.getData().get(0).playMoneyMax);
			subduticon(s,j);
			lc_conte.setText("您的余额不足,请您去充值！余额支付减免"+subduticon(s,j)+"哟~");
		}else {
			lc_conte.setText("需支付"+lobean.getData().get(0).playMoneyMin+"元平台使用费,您余额不足，请您进行充值！");
		}
		pin.setText("2.使用余额支付收取平台使用费"+lobean.getData().get(0).playMoneyMin+"元");
		
		lc_sumit.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(LogCompanyActivity.this, RechargeActivity.class);
				intent.putExtra("way", "2");
				intent.putExtra("billCode", getIntent().getStringExtra("billCode"));//物流单号
//				startActivityForResult(intent, 10);
				startActivity(intent);
				finish();
			}
		});
//		lc_sumit.setOnClickListener(new OnClickListener() {
//			
//			@Override
//			public void onClick(View arg0) {
//				// TODO Auto-generated method stub
//				window02.dismiss();
//			}
//		});
		lc_exti.setOnClickListener(new OnClickListener() {
			
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
	/**
	 *余额支付
	 */
	public void showin() {
		final	PopupWindow window02;
		TextView lc_conte;
		Button lc_sumit,lc_exti,lc_sumitzhi;
		 final Intent intent=new Intent() ;
		// 利用layoutInflater获得View
		LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View view = inflater.inflate(R.layout.logcompanpopwindping, null);
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
		window02.showAtLocation(LogCompanyActivity.this.findViewById(R.id.deta_but), Gravity.CENTER, 0, 0);
		lc_exti=(Button) view.findViewById(R.id.lc_exti);
		lc_conte=(TextView) view.findViewById(R.id.lc_conte);
		lc_sumit=(Button) view.findViewById(R.id.lc_sumit);
		lc_sumitzhi=(Button) view.findViewById(R.id.lc_sumitzhi);
			int j=Integer.parseInt(lobean.getData().get(0).playMoneyMin);
			lc_conte.setText("支付平台使用费"+lobean.getData().get(0).playMoneyMin+"元");
			lc_sumitzhi.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				getaddsurp();
			}
		});
			lc_sumit.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					window02.dismiss();
				}
			});
		lc_exti.setOnClickListener(new OnClickListener() {
			
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
	/**
	 * 第三方弹窗
	 */
	public void showwind(int item) {
		final	PopupWindow window02;
		TextView lc_conte,lc_title,textView2;
		Button lc_sumitc,lc_exti,lc_sumitsan;
		final ImageView pay_zhifubao;
		final ImageView pay_wenxin;
		 final Intent intent=new Intent() ;
		// 利用layoutInflater获得View
		LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View view = inflater.inflate(R.layout.logcompanpopwinds, null);
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
		window02.showAtLocation(LogCompanyActivity.this.findViewById(R.id.deta_but), Gravity.CENTER, 0, 0);
		lc_exti=(Button) view.findViewById(R.id.lc_exti);
		lc_conte=(TextView) view.findViewById(R.id.lc_conte);
		lc_title=(TextView) view.findViewById(R.id.lc_title);
		textView2=(TextView) view.findViewById(R.id.textView2);
		lc_sumitc=(Button) view.findViewById(R.id.lc_sumitc);
		lc_sumitsan=(Button) view.findViewById(R.id.lc_sumitsan);
		pay_wenxin=(ImageView) view.findViewById(R.id.pay_wenxin);
		pay_zhifubao=(ImageView) view.findViewById(R.id.pay_zhifubao);
		int j=Integer.parseInt(lobean.getData().get(0).playMoneyMin);
		int s=Integer.parseInt(lobean.getData().get(0).playMoneyMax);
		subduticon(s,j);
			lc_conte.setText("2.使用余额支付收取平台使用费"+j+"元");
			lc_title.setText(lobean.getData().get(0).playMoneyMax+"元  (平台使用费)");
			textView2.setText("3.使用第三方支付收取平台使用费"+lobean.getData().get(0).playMoneyMax+"元");
		//充值
			lc_sumitc.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(LogCompanyActivity.this, RechargeActivity.class);
				intent.putExtra("way", "2");
				intent.putExtra("billCode", getIntent().getStringExtra("billCode"));//物流单号
				startActivityForResult(intent, 10);
			}
		});
			//第三方支付
			lc_sumitsan.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					pay_wenxin.setVisibility(View.VISIBLE);
					pay_zhifubao.setVisibility(View.VISIBLE);
					
				}
			});
			pay_wenxin.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					
					getWxChat("2");
				}
			});
			pay_zhifubao.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					getAllpay("B");
				
				}
			});
		lc_exti.setOnClickListener(new OnClickListener() {
			
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
	
	/**
	 * 余额支付的接口   物流公司
	 */
	public void getaddsurp() {
		if (getIntent().getStringExtra("billCode").equals("") || lobean.getData().get(0).playMoneyMin.equals("")) {
			ToastUtil.shortToast(getApplicationContext(), "该订单有问题");
			return;
		}
		JSONObject obj = new JSONObject();
		Log.e("111111111compay", UrlMap.getfour(MCUrl.BALANCEPAYCOMPAY, "userId", String
				.valueOf(PreferencesUtils.getInt(getApplicationContext(),
						PreferenceConstants.UID)), "billCode", getIntent().getStringExtra("billCode") ,
				"counterFee", lobean.getData().get(0).playMoneyMin,"transferMoney",Integer.parseInt(text_total.getText().toString())+""));
		RequestParams params = new RequestParams();
		dialog.show();
//		AsyncHttpUtils.doPut(getApplicationContext(), MCUrl.BALANCEPAY, obj.toString(), null,
//		new AsyncHttpResponseHandler() {
//		AsyncHttpUtils.doGet(UrlMap.getfour(MCUrl.BALANCEPAYCOMPAY, "userId", String
//				.valueOf(PreferencesUtils.getInt(getApplicationContext(),
//						PreferenceConstants.UID)), "billCode", billCode ,
//				"counterFee", money,"transferMoney",transferMoney+""), null, null, params,
//				new AsyncHttpResponseHandler() {
		        AsyncHttpUtils.doGet(UrlMap.getseven(MCUrl.BALANCEPAYCOMPAY, "userId", String
				.valueOf(PreferencesUtils.getInt(getApplicationContext(),
						PreferenceConstants.UID)), "billCode", getIntent().getStringExtra("billCode") ,
				"counterFee", lobean.getData().get(0).playMoneyMin,"transferMoney",Integer.parseInt(text_total.getText().toString())+"",
				"takeCargoMoney",takeCargoMoney+"","sendCargoMoney",sendCargoMoney+"","cargoTotal",cargoTotal+""), null, null, params,
				new AsyncHttpResponseHandler() {
					@Override
					public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
						// TODO Auto-generated method
						Intent intent=new Intent();
						dialog.dismiss();
						if (arg2 == null)
							return;
						Log.e("compaymsg", new String(arg2));
						BaseBean bean = new Gson().fromJson(new String(arg2), BaseBean.class);
						if (bean.getErrCode() == 0) {
							ToastUtil.shortToast(LogCompanyActivity.this, bean.getMessage());
//							sendBroadcast(new Intent().setAction("freight").putExtra("flag", "owner"));//启动SendFreightActivity界面	
//							intent.setClass(LogCompanyActivity.this, LogistcaInforseActivity.class);//公司
//							startActivity(intent);
							Intent intent1 = new Intent();
							setResult(RESULT_OK, intent1);
							finish();
						}
						if (bean.getErrCode() == -2) {
							DialogUtils.createAlertDialogTwo(LogCompanyActivity.this, "", bean.getMessage(), 0, "去充值",
									"更换支付方式", new DialogInterface.OnClickListener() {
										@Override
										public void onClick(DialogInterface dialog, int which) {

											startActivity(new Intent(LogCompanyActivity.this, RechargeActivity.class));
										}

									}).show();
						}
					}

					@Override
					public void onFailure(int arg0, Header[] arg1, byte[] arg2, Throwable arg3) {
						// TODO Auto-generated method
						// stub
						dialog.dismiss();
					}
				});
	}
	
	/**
	 * 支付宝支付
	 */
	public void getAllpay(String ty) {
		// if (bean.data.get(0).discount != null) {
		// needPayMoney = Double.parseDouble(money)
		// * Double.parseDouble(bean.data.get(0).discount);
		// }
		if (getIntent().getStringExtra("billCode").equals("") || lobean.getData().get(0).playMoneyMax.equals("")) {
			ToastUtil.shortToast(getApplicationContext(), "该订单有问题");
			return;
		}
		String useridString=String.valueOf(PreferencesUtils.getInt(getApplicationContext(),PreferenceConstants.UID));
		orderInfo = AlipayUtils.getOrderInfo("物流线上支付", null + lobean.getData().get(0).playMoneyMax + "元", lobean.getData().get(0).playMoneyMax + "",
				"http://www.efamax.com:8888/appservice-1.0-SNAPSHOT/jsp/alipay/receiveNotify.jsp", null, null,
				getIntent().getStringExtra("billCode")+"B"+useridString);
		Log.e("pp", orderInfo);
		sendEmptyBackgroundMessage(MsgConstants.MSG_01);

	}

	/**
	 * 微信支付
	 */
	public void getWxChat(String type) {
//		if (getIntent().getStringExtra("billCode").equals("") || lobean.getData().get(0).playMoneyMax.equals("")) {
//			ToastUtil.shortToast(getApplicationContext(), "该订单有问题");
//			return;
//		}
		JSONObject obj = new JSONObject();
		try {
			obj.put("billCode", getIntent().getStringExtra("billCode"));
			obj.put("matName", null);
			obj.put("matType", null);
			obj.put("insuranceFee", null);
			obj.put("insureMoney", null);
			obj.put("shipMoney", null);
			obj.put("userCouponId", null);
			obj.put("price", lobean.getData().get(0).playMoneyMax);
			obj.put("type", type);
			obj.put("weight", null);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		Log.e("obj", obj.toString());
//		dialog.show();
//		AsyncHttpUtils.doPut(getApplicationContext(), MCUrl.WPRE, obj.toString(), null,
//				new AsyncHttpResponseHandler() {
		RequestParams params = new RequestParams();
		dialog.show();
//		AsyncHttpUtils.doPut(getApplicationContext(), MCUrl.BALANCEPAY, obj.toString(), null,
//		new AsyncHttpResponseHandler() {
		String useridString=String.valueOf(PreferencesUtils.getInt(getApplicationContext(),PreferenceConstants.UID));
		AsyncHttpUtils.doGet(UrlMap.getThreeUrl(MCUrl.WPRE, "billCode",getIntent().getStringExtra("billCode")+"B"+useridString,
				"price", lobean.getData().get(0).playMoneyMax,
				"type", type), null, null, params,
				new AsyncHttpResponseHandler() {
					@Override
					public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
						dialog.dismiss();
						if (arg2 == null)
							return;
						WechatBean bean = new Gson().fromJson(new String(arg2), WechatBean.class);
						Log.e("lllllllll}}}}}}]]]", new String(arg2));
						if (bean.getErrCode()==0) {
						req.appId = bean.data.get(0).appId;
						req.partnerId = bean.data.get(0).partnerId;
						req.prepayId = bean.data.get(0).prepayid;
						req.nonceStr = bean.data.get(0).nonceStr;
						req.packageValue = bean.data.get(0).package_;
						req.sign = bean.data.get(0).sign;
						req.timeStamp = bean.data.get(0).timestamp;
						req.extData = "app data";
						msgApi.registerApp(CollectionKey.APP_ID);
						msgApi.sendReq(req);
						}else {
							ToastUtil.shortToast(LogCompanyActivity.this, bean.getMessage());
						}
					}

					@Override
					public void onFailure(int arg0, Header[] arg1, byte[] arg2, Throwable arg3) {
						dialog.dismiss();
					}
				});
	}
	@Override
	public void handleBackgroundMessage(Message msg) {
		super.handleBackgroundMessage(msg);
		switch (msg.what) {
		case MsgConstants.MSG_01:
			PayTask task = new PayTask(this);
			result = task.pay(orderInfo);
			Log.e("mm", result);
			sendEmptyUiMessage(MsgConstants.MSG_01);
			break;
		default:
			break;
		}
	}

	@Override
	public void handleUiMessage(Message msg) {
		super.handleUiMessage(msg);
		switch (msg.what) {
		case MsgConstants.MSG_01:
			PayResult payResult = new PayResult(result);
			String resultStatus = payResult.getResultStatus();
			Log.e("su", resultStatus + payResult.getResult());
			if (TextUtils.equals(resultStatus, "9000")) {
				getQueryResult(getIntent().getStringExtra("billCode"));
			} else {
				// 判断resultStatus 为非“9000”则代表可能支付失败
				// “8000”代表支付结果因为支付渠道原因或者系统原因还在等待支付结果确认，最终交易是否成功以服务端异步通知为准（小概率状态）
				if (TextUtils.equals(resultStatus, "8000")) {
					Toast.makeText(LogCompanyActivity.this, "支付结果确认中", Toast.LENGTH_SHORT).show();

				} else {
					// 其他值就可以判断为支付失败，包括用户主动取消支付，或者系统返回的错误
					Toast.makeText(LogCompanyActivity.this, "支付失败", Toast.LENGTH_SHORT).show();

				}
			}
			break;
		case MsgConstants.MSG_02:
			getrequstBalance();
			break;
		default:
			break;
		}
	}

	/**
	 * 接收微信支付成功返回的状态
	 * 
	 * @author
	 *
	 */
	public class MyReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			if (intent.getAction().equals("recharge")) {
				getQueryResult(getIntent().getStringExtra("billCode"));
			}

		}

	}

	// 获取支付宝支付折扣
	public void getDiscount() {
		AsyncHttpUtils.doGet(UrlMap.getUrl(MCUrl.ALIPAYDISCOUNT, "uid", PreferenceConstants.UID), null, null, null,
				new AsyncHttpResponseHandler() {
					@Override
					public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
						Log.e("disconut", new String(arg2));
						bean = new Gson().fromJson(new String(arg2), DiscountBean.class);

					}

					@Override
					public void onFailure(int arg0, Header[] arg1, byte[] arg2, Throwable arg3) {
						// TODO Auto-generated method stub

					}
				});
	}

	// 获取支付宝和微信支付的结果
		public void getQueryResult(String billCode) {
//			AsyncHttpUtils.doGet(UrlMap.getUrl(MCUrl.AndUserId, "billCode", billCode+"B"), null, null, null,
//					new AsyncHttpResponseHandler() {
			String useridString=String.valueOf(PreferencesUtils.getInt(getApplicationContext(),PreferenceConstants.UID));
			AsyncHttpUtils.doGet(UrlMap.getTwo(MCUrl.AndUserId, "userId", String
					.valueOf(PreferencesUtils.getInt(getApplicationContext(),
							PreferenceConstants.UID)),"billCode", billCode+"B"+useridString), null, null, null,
					new AsyncHttpResponseHandler() {
						@Override
						public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
							Log.e("msdjkg", new String(arg2));
							BaseBean bean = new Gson().fromJson(new String(arg2), BaseBean.class);
							if (bean.isSuccess()) {
								MessageUtils.alertMessageCENTER(getApplicationContext(), "支付成功");
//								sendBroadcast(new Intent().setAction("freight").putExtra("flag", "owner"));//启动SendFreightActivity界面	
								Intent intent = new Intent();
								setResult(RESULT_OK, intent);
								finish();
							} else {
								MessageUtils.alertMessageCENTER(getApplicationContext(), bean.getMessage());
							}
						}

						@Override
						public void onFailure(int arg0, Header[] arg1, byte[] arg2, Throwable arg3) {
							MessageUtils.alertMessageCENTER(getApplicationContext(), "支付失败");

						}
					});
		}

		@Override
		protected void onDestroy() {
			// TODO Auto-generated method stub
			super.onDestroy();
			unregisterReceiver(reMyrecive);
		}
	
	public int  subduticon(int a,int b){
		int resultSub=a-b;
		return resultSub;
	}
	public int  subadd(int a,int b,int c){
		int resultSum=a+b+c;
		return resultSum;
	}
	@Override
	public void getData() {
		// TODO Auto-generated method stub
		
	}

}
