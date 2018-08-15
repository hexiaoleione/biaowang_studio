package com.hex.express.iwant.activities;

import org.apache.http.Header;

import com.google.gson.Gson;
import com.hex.express.iwant.R;
import com.hex.express.iwant.iWantApplication;
import com.hex.express.iwant.bean.AdvertBean;
import com.hex.express.iwant.constance.MCUrl;
import com.hex.express.iwant.constance.PreferenceConstants;
import com.hex.express.iwant.http.AsyncHttpUtils;
import com.hex.express.iwant.http.UrlMap;
import com.hex.express.iwant.utils.AppUtils;
import com.hex.express.iwant.utils.MyBitmapUtils;
import com.hex.express.iwant.utils.PreferencesUtils;
import com.hex.express.iwant.views.MarqueeTextView;
import com.loopj.android.http.AsyncHttpResponseHandler;

import android.R.integer;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.RelativeLayout;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import butterknife.Bind;
import butterknife.ButterKnife;
/**
 * 
 * @author huyichuan
 *  个人物流详情
 */
public class MydetailedActivity extends BaseActivity{
	
	
	
	@Bind(R.id.deta_fabutime)//发货时间
	TextView deta_fabutime;	
	@Bind(R.id.huoname)//货物名字
	TextView huoname;	
	@Bind(R.id.deta_zhong)//重量
	TextView deta_zhong;	
	@Bind(R.id.deta_tiji)//体积
	TextView deta_tiji;	
	@Bind(R.id.deta_fahuodizhi)//发货地址
	MarqueeTextView deta_fahuodizhi;	
	@Bind(R.id.quhuo)//取货
	TextView quhuo;	
	@Bind(R.id.deta_dadaodizhi)//送货地址
	MarqueeTextView deta_dadaodizhi;	
	@Bind(R.id.songhuo)//送货
	TextView songhuo;
	@Bind(R.id.deta_lianxifangshi)//shoujihao
	TextView deta_lianxifangshi;	
	@Bind(R.id.deta_shouhuoname)//收货人
	TextView deta_shouhuoname;	
	@Bind(R.id.deta_fahuotime)//发货时间
	TextView deta_fahuotime;
	@Bind(R.id.deta_dadaotime)//达到时间
	TextView deta_dadaotime;
	@Bind(R.id.deta_beizhu)//备注信息
	TextView deta_beizhu;
	@Bind(R.id.deta_img_headse)//头像
	ImageView offer_img_head;
	@Bind(R.id.deta_logoffer_usernames)//公司名字
	TextView deta_logoffer_username;
	@Bind(R.id.deta_logoffer_times)//时间
	TextView deta_logoffer_time;
	@Bind(R.id.jiazhi)//报价
	TextView jiazhi;
	@Bind(R.id.deta_faname)//发件
	TextView deta_faname;
	@Bind(R.id.deta_falianxifangshi)//发件人电话
	TextView deta_falianxifangshi;
//	@Bind(R.id.deta_farens)//法人
//	TextView deta_faren;
//	@Bind(R.id.deta_phones)//电话图标
//	TextView deta_phone;
	@Bind(R.id.deta_ad)//联系方式
	TextView deta_ad;
	@Bind(R.id.deta_addses)//地址
	MarqueeTextView deta_addse;
	@Bind(R.id.deta_addse_tus)// 地址图标
	TextView deta_addse_tu;
	@Bind(R.id.deta_baojias)//报价
	TextView deta_baojia;
	@Bind(R.id.deta_baojiasang)//上门
	TextView deta_baojiasang;
	@Bind(R.id.deta_baojiasong)//送货
	TextView deta_baojiasong;
	@Bind(R.id.deta_baojiayun)//运费
	TextView deta_baojiayun;
	@Bind(R.id.deta_baojiazong)//总价
	TextView deta_baojiazong;
	@Bind(R.id.deta_jian)//
	TextView deta_jian;
	@Bind(R.id.deta_carcold)//
	TextView deta_carcold;
	
	@Bind(R.id.tem)//
	TextView tem;
	@Bind(R.id.recoldcar)//冷链
	RelativeLayout	recoldcar;
	@Bind(R.id.resonghuo)//正常
	RelativeLayout	resonghuo;
	
	@Bind(R.id.deta_buts)//支付按钮
	Button deta_buts;
	@Bind(R.id.readds)
	RelativeLayout readdss;
	//是否买保险 
			@Bind(R.id.checkbox_deta)
			CheckBox checkbox_lo;
			@Bind(R.id.edt_pricesdeta)
			EditText edt_price;
			@Bind(R.id.v_ppsdeta)
			View v_pp;
			private boolean chox=false; 
			PopupWindow window02;
			private String premium;
			 int whether;
			 
			 @Bind(R.id.deta_baojiasang1)//上门
				TextView deta_baojiasang1;
				@Bind(R.id.deta_baojiasong1)//送货
				TextView deta_baojiasong1;
				@Bind(R.id.deta_baojiayun1)//运费
				TextView deta_baojiayun1;
				@Bind(R.id.deta_baojiazong1)//总价
				TextView deta_baojiazong1;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.mylogi_detailedse);//mylogi_detailedse  mylogi_detailed
		ButterKnife.bind(MydetailedActivity.this);
		initView();
		setOnClick();
	}

	@Override
	public void onWeightClick(View v) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void initView() {
		// TODO Auto-generated method stub
		if (getIntent().getStringExtra("status").equals("2")) {
			deta_buts.setVisibility(View.GONE);
		}
//		if (getIntent().getStringExtra("status").equals("8")) {
//			deta_buts.setText("担保交易");
//			
//		}
		deta_fahuodizhi.setFocusable(false);
		deta_dadaodizhi.setFocusable(false);
		huoname.setText("物品名称："+getIntent().getStringExtra("cargoName"));
		deta_zhong.setText("总重量："+getIntent().getStringExtra("cargoWeight")+"");
		deta_fabutime.setText(getIntent().getStringExtra("publishTimes"));
		deta_tiji.setText("总体积："+getIntent().getStringExtra("cargoVolume"));
//		deta_tiji.setText("货物体积： "+" 长"+getIntent().getStringExtra("length")+" 厘米"+" 宽"+getIntent().getStringExtra("wide")+" 厘米"+" 高"+getIntent().getStringExtra("high")+" 厘米");
//		String	c1t=getIntent().getStringExtra("cargoCost");
//		String[] str1s=c1t.split("\\.");
//		jiazhi.setText("货物价值："+str1s[0]+"元");
//		jiazhi.setText("货物价值："+getIntent().getStringExtra("cargoCost")+"元");
//		intent.putExtra("carType", getIntent().getStringExtra("carType"));
//		intent.putExtra("carName", getIntent().getStringExtra("carName"));
		if ("cold".equals(getIntent().getStringExtra("carType"))) {
			recoldcar.setVisibility(View.VISIBLE);	
			resonghuo.setVisibility(View.GONE);
			tem.setText("温度要求："+getIntent().getStringExtra("tem"));
		}else {
			recoldcar.setVisibility(View.GONE);
			resonghuo.setVisibility(View.VISIBLE);
		}
		
		if ("cold".equals(getIntent().getStringExtra("carType"))) {
			deta_carcold.setText("需求："+getIntent().getStringExtra("carName"));
		}
		deta_fahuodizhi.setText(getIntent().getStringExtra("startPlace"));
		deta_dadaodizhi.setText(getIntent().getStringExtra("entPlace"));
		deta_faname.setText("发货人："+getIntent().getStringExtra("sendPerson"));
		deta_falianxifangshi.setText("电话："+getIntent().getStringExtra("sendPhone"));
		deta_shouhuoname.setText("收货人："+getIntent().getStringExtra("takeName"));
		deta_lianxifangshi.setText("电话："+getIntent().getStringExtra("takeMobile"));
//		deta_fahuotime.setText("发货时间："+getIntent().getStringExtra("takeTime"));
		deta_dadaotime.setText("要求到达时间："+getIntent().getStringExtra("arriveTime"));
		if (!"".equals(getIntent().getStringExtra("cargoSize"))) {
			deta_jian.setText("件数："+getIntent().getStringExtra("cargoSize")+"件");
		}else {
			deta_jian.setVisibility(View.GONE);
		}
		//intent.putExtra("appontSpace",  getIntent().getStringExtra("appontSpace"));
		if (!"".equals(getIntent().getStringExtra("appontSpace"))) {
			deta_beizhu.setText("指定园区："+getIntent().getStringExtra("appontSpace"));
		}else {
			deta_beizhu.setVisibility(View.GONE);
		}
		
//		deta_zhong.setText(getIntent().getStringExtra("cargoWeight"));
		if (getIntent().getBooleanExtra("takeCargo", false)) {
			quhuo.setText("物流公司上门取货");
		}else {
			quhuo.setText("发货人发货人送到货场");
		}
		if (getIntent().getBooleanExtra("sendCargo", false)) {
			songhuo.setText("物流公司送货上门");
		}else {
			songhuo.setText("收件人自提");
		}
		
//		songhuo.setText(getIntent().getStringExtra("cargoWeight"));
//		offer_img_head.setText(getIntent().getStringExtra("matImageUrl"));
		if (!getIntent().getStringExtra("matImageUrl").equals("")) {
			new MyBitmapUtils().display(offer_img_head,
					getIntent().getStringExtra("matImageUrl"));
			offer_img_head.setBackgroundDrawable(null);
		}else {
			offer_img_head.setBackgroundResource(R.drawable.tubgs);
		}
		
		deta_logoffer_username.setText("名称："+getIntent().getStringExtra("companyName"));
		deta_logoffer_time.setText(getIntent().getStringExtra("publishTime"));
//		deta_faren.setText("公司法人："+getIntent().getStringExtra("companyName"));
//		deta_lianxi.setText("联系方式："+getIntent().getStringExtra("mobile"));
		if (PreferencesUtils.getString(getApplicationContext(), PreferenceConstants.WLID).equals("2")) {
	     	readdss.setVisibility(View.GONE);
		}
		if (!getIntent().getStringExtra("address").equals("")) {
			
			deta_addse.setText(""+getIntent().getStringExtra("address"));
		}else {
			deta_addse.setVisibility(View.GONE);
			deta_ad.setVisibility(View.GONE);
		}
		
		deta_addse_tu.setText("留言："+getIntent().getStringExtra("luMessage"));
//		deta_baojia.setText("报价："+getIntent().getStringExtra("transferMoney"));  +"送费货上门："+getIntent().getStringExtra("sendCargoMoney")+"元"
//+"运费："+getIntent().getStringExtra("cargoTotal")+"="+"总价："+getIntent().getStringExtra("transferMoney")+"元"
//		deta_baojiasang.setText("上门取货费："+getIntent().getStringExtra("takeCargoMoney")+"元");
		if ("cold".equals(getIntent().getStringExtra("carType"))) {

			String	ct=getIntent().getStringExtra("cargoTotal");
			String[] strs=ct.split("\\.");
			deta_baojiasang.setText(strs[0]+"元");
			deta_baojiasang1.setText("运费：");
			String	ct1=getIntent().getStringExtra("transferMoney");
			String[] strs1=ct1.split("\\.");
			deta_baojiasong.setText(strs1[0]+"元");
			deta_baojiasong1.setText("合计：");
//			deta_baojiasong.setText("送货上门费："+getIntent().getStringExtra("sendCargoMoney")+"元");
//			String	ct2=getIntent().getStringExtra("cargoTotal");
//			String[] strs2=ct2.split("\\.");
//			deta_baojiayun.setText(strs2[0]+"元");
			deta_baojiayun1.setVisibility(View.GONE);
			deta_baojiayun.setVisibility(View.GONE);
//			deta_baojiayun.setText("运费："+getIntent().getStringExtra("cargoTotal")+"元");
//			String	ct3=getIntent().getStringExtra("transferMoney");
//			String[] strs3=ct3.split("\\.");
//			deta_baojiazong.setText(strs3[0]+"元");
			deta_baojiazong1.setVisibility(View.GONE);
			deta_baojiazong.setVisibility(View.GONE);
		}else {

			String	ct=getIntent().getStringExtra("takeCargoMoney");
			String[] strs=ct.split("\\.");
			deta_baojiasang.setText(strs[0]+"元");
			String	ct1=getIntent().getStringExtra("sendCargoMoney");
			String[] strs1=ct1.split("\\.");
			deta_baojiasong.setText(strs1[0]+"元");
//			deta_baojiasong.setText("送货上门费："+getIntent().getStringExtra("sendCargoMoney")+"元");
			String	ct2=getIntent().getStringExtra("cargoTotal");
			String[] strs2=ct2.split("\\.");
			deta_baojiayun.setText(strs2[0]+"元");
//			deta_baojiayun.setText("运费："+getIntent().getStringExtra("cargoTotal")+"元");
			String	ct3=getIntent().getStringExtra("transferMoney");
			String[] strs3=ct3.split("\\.");
			deta_baojiazong.setText(strs3[0]+"元");
		}
		
//		String	ct=getIntent().getStringExtra("takeCargoMoney");
//		String[] strs=ct.split("\\.");
//		deta_baojiasang.setText(strs[0]+"元");
//		String	ct1=getIntent().getStringExtra("sendCargoMoney");
//		String[] strs1=ct1.split("\\.");
//		deta_baojiasong.setText(strs1[0]+"元");
////		deta_baojiasong.setText("送货上门费："+getIntent().getStringExtra("sendCargoMoney")+"元");
//		String	ct2=getIntent().getStringExtra("cargoTotal");
//		String[] strs2=ct2.split("\\.");
//		deta_baojiayun.setText(strs2[0]+"元");
////		deta_baojiayun.setText("运费："+getIntent().getStringExtra("cargoTotal")+"元");
//		String	ct3=getIntent().getStringExtra("transferMoney");
//		String[] strs3=ct3.split("\\.");
//		deta_baojiazong.setText(strs3[0]+"元");
////		deta_baojiazong.setText("总价："+getIntent().getStringExtra("transferMoney")+"元");
	}

	@Override
	public void initData() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setOnClick() {
		// TODO Auto-generated method stub
//		deta_phone.setOnClickListener(new OnClickListener() {
//			@Override
//			public void onClick(View arg0) {
//				// TODO Auto-generated method stub
//				//拨打电话☎️
//				AppUtils.intentDial(MydetailedActivity.this, getIntent().getStringExtra("mobile"));
//			}
//		});
		checkbox_lo.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
				// TODO Auto-generated method stub
				if (arg1) {
					chox=arg1;
					edt_price.setVisibility(View.VISIBLE);
				   	v_pp.setVisibility(View.VISIBLE);
				   	premium=  	edt_price.getText().toString();
				   	whether=1;
					showPaywindow();
				}else {
					edt_price.setVisibility(View.GONE);
				   	v_pp.setVisibility(View.GONE);
				   	whether=0;
					chox=arg1;
				}
			}
		});
		
		deta_buts.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(MydetailedActivity.this, TradingcertaintyActivity.class);
					intent.putExtra("userId", getIntent().getIntExtra("userId", 0));//
					intent.putExtra("WLBId", getIntent().getIntExtra("recId", 0));//
			 		intent.putExtra("billCode", getIntent().getStringExtra("billCode"));//物流单号
			 		intent.putExtra("takeCargoMoney", getIntent().getStringExtra("takeCargoMoney"));//上门取货费
			 		intent.putExtra("sendCargoMoney", getIntent().getStringExtra("sendCargoMoney"));//送货上门费
					intent.putExtra("cargoTotal", getIntent().getStringExtra("cargoTotal"));//运费
			 		intent.putExtra("takeCargo", getIntent().getBooleanExtra("takeCargo", false));//是否上门取货
			 		intent.putExtra("carType", getIntent().getStringExtra("carType"));//冷链
			 		
			 		intent.putExtra("way", "3");
			 		intent.putExtra("wayde","3");//
			 		intent.putExtra("transferMoney", getIntent().getStringExtra("transferMoney"));
			 		Log.e("1111122w  ", ""+ getIntent().getIntExtra("recId", 0));
//			 		startActivityForResult(intent, 10);
//				Intent intent = new Intent(MydetailedActivity.this, TradingcertaintyActivity.class);
//			 		if (!getIntent().getStringExtra("status").equals("8")) {
						Advert();
//					}
				startActivity(intent);
				finish();
				
			}
		});
		
	}

	@Override
	public void getData() {
		// TODO Auto-generated method stub
		
	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		Log.e("11111111  ", resultCode+" ssss   "+data);
		switch (requestCode) {
		case 10:
			finish();
			break;
		}
	}
	/**
	 * 显示投保提示信息
	 */
	private void showPaywindow() {
		
		TextView btnsaves_pan;
		// 利用layoutInflater获得View
		LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View view = inflater.inflate(R.layout.popwidndow_baojia, null);
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
		window02.showAtLocation(MydetailedActivity.this.findViewById(R.id.deta_buts), Gravity.CENTER, 0, 0);
		btnsaves_pan=(TextView) view.findViewById(R.id.btnsaves_pan);
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

	public void Advert(){
		AsyncHttpUtils.doSimGet(UrlMap.getTwo(MCUrl.Chose, "recId", ""+getIntent().getIntExtra("recId", 0), "userId", getIntent().getIntExtra("userId", 0)+"") , new AsyncHttpResponseHandler() {
			@Override
			public void onFailure(int arg0, Header[] arg1, byte[] arg2, Throwable arg3) {
				
			}
			@Override
			public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
				
			}
		});
	}

}
