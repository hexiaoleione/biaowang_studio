package com.hex.express.iwant.activities;

import org.apache.http.Header;

import com.google.gson.Gson;
import com.hex.express.iwant.R;
import com.hex.express.iwant.bean.LoOfferBean;
import com.hex.express.iwant.bean.MyloginBean;
import com.hex.express.iwant.constance.MCUrl;
import com.hex.express.iwant.constance.PreferenceConstants;
import com.hex.express.iwant.http.AsyncHttpUtils;
import com.hex.express.iwant.http.UrlMap;
import com.hex.express.iwant.utils.MyBitmapUtils;
import com.hex.express.iwant.utils.PreferencesUtils;
import com.hex.express.iwant.views.MarqueeTextView;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import android.content.ComponentName;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import butterknife.Bind;
import butterknife.ButterKnife;
/**
 * 
 * @author huyichuan
 *  查看自己发的物流
 *
 */
public class LogistioffActivity extends BaseActivity{
	
	@Bind(R.id.deta_fabutime)//发货时间
	TextView deta_fabutime;	
	@Bind(R.id.huoname)//货物名字
	TextView huoname;	
	@Bind(R.id.deta_zhong)//重量
	TextView deta_zhong;
	@Bind(R.id.btnRight)//
	TextView btnRight;
	
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
	@Bind(R.id.btnLeft)//次数
	ImageView btnLeft;
//	@Bind(R.id.deta_pingjias)//评价
//	TextView deta_pingjia;
//	@Bind(R.id.deta_farens)//法人
//	TextView deta_faren;
	@Bind(R.id.deta_addse_liu)//电话图标
	TextView deta_addse_liu;
	@Bind(R.id.deta_ad)//
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
	@Bind(R.id.deta_logoffer_userphone)//手机号
	TextView deta_logoffer_userphone;
	@Bind(R.id.detafahuoname)//手
	TextView detafahuoname;
	@Bind(R.id.deta_falianxifangshi)//手机号
	TextView deta_falianxifangshi;
	@Bind(R.id.deta_toubao)//手机号
	TextView deta_toubao;
	@Bind(R.id.camy_btime)//
	TextView camy_btime;
	@Bind(R.id.huowuzhonglei)//
	TextView huowuzhonglei;
	@Bind(R.id.chengbaoleibie)//
	TextView chengbaoleibie;
	
	
	@Bind(R.id.deta_buts)//支付按钮
	Button deta_buts;
	@Bind(R.id.my_chongxuan)//支
	Button my_chongxuan;
	@Bind(R.id.my_xiugai)//修改
	Button my_xiugai;
	@Bind(R.id.coamaddse)
	RelativeLayout coamaddse;
	
	//是否买保险
			@Bind(R.id.checkbox_deta)
			CheckBox checkbox_lo;
			@Bind(R.id.edt_pricesdeta)
			EditText edt_price;
			@Bind(R.id.v_ppsdeta)
			View v_pp;
			@Bind(R.id.deta_btnagin)//修改
			Button deta_btnagin;
			
			@Bind(R.id.deta_carcold)//
			TextView deta_carcold;
			@Bind(R.id.deta_jianshu)//
			TextView deta_jianshu;
			
			@Bind(R.id.tem)//
			TextView tem;
			@Bind(R.id.recoldcar)//冷链
			RelativeLayout	recoldcar;
			@Bind(R.id.resonghuo)//正常
			LinearLayout	resonghuo;
			
			 @Bind(R.id.deta_baojiasang1)//上门
				TextView deta_baojiasang1;
				@Bind(R.id.deta_baojiasong1)//送货
				TextView deta_baojiasong1;
				@Bind(R.id.deta_baojiayun1)//运费
				TextView deta_baojiayun1;
				@Bind(R.id.deta_baojiazong1)//总价
				TextView deta_baojiazong1;
			
	int recId;
	LoOfferBean loff;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_mylogistise);//activity_mylogisti
		ButterKnife.bind(LogistioffActivity.this);
		initView();
		setOnClick();
		getData();
	}

	@Override
	public void onWeightClick(View v) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void initView() {
		// TODO Auto-generated method stub
		if (getIntent().getIntExtra("recId", 0)!=0) {
			recId=getIntent().getIntExtra("recId", 0);
		}
		getmessage();
		
	}

	@Override
	public void initData() {
		// TODO Auto-generated method stub
		
		deta_buts.setVisibility(View.GONE);
		deta_fahuodizhi.setFocusable(false);
		deta_dadaodizhi.setFocusable(false);
//		deta_addse.setFocusable(false);
		huoname.setText("物品名称："+loff.getData().get(0).cargoName);
//		if (loff.getData().get(0).weight.equals("5")) {
//			deta_zhong.setText("总重量：≤"+loff.getData().get(0).weight+"公斤");
//		}else {
			deta_zhong.setText("总重量："+loff.getData().get(0).cargoWeight+"");
//		}
		if ("cold".equals(loff.getData().get(0).carType)) {
			recoldcar.setVisibility(View.VISIBLE);	
			resonghuo.setVisibility(View.GONE);
			tem.setText("温度要求："+loff.getData().get(0).tem);
		}else {
			recoldcar.setVisibility(View.GONE);
			resonghuo.setVisibility(View.VISIBLE);
		}
		if ("cold".equals(loff.getData().get(0).carType)) {
			deta_carcold.setText("需求："+loff.getData().get(0).carName);
		}
		deta_fabutime.setText(loff.getData().get(0).publishTime);
		deta_tiji.setText("总体积："+loff.getData().get(0).cargoVolume);
//		deta_tiji.setText("货物体积："+" 长："+loff.getData().get(0).length+" 厘米"+" 宽："+loff.getData().get(0).wide+" 厘米"+" 高："+loff.getData().get(0).high+" 厘米");
		String	c1t=loff.getData().get(0).cargoCost;
		String[] str1s=c1t.split("\\.");
		if (str1s[0].equals("0")) {
			jiazhi.setVisibility(View.GONE);	
		}
		jiazhi.setText("货物价值："+str1s[0]+"元");
		if ("cold".equals(loff.getData().get(0).carType)) {
			deta_carcold.setText("需求："+loff.getData().get(0).carName);
		}
//		jiazhi.setText("货物价值："+loff.getData().get(0).cargoCost+"元");
		deta_fahuodizhi.setText(loff.getData().get(0).startPlace);
		deta_dadaodizhi.setText(loff.getData().get(0).entPlace);
		deta_shouhuoname.setText("收货人："+loff.getData().get(0).takeName);
		deta_lianxifangshi.setText("电话："+loff.getData().get(0).takeMobile);
		detafahuoname.setText("发件人："+loff.getData().get(0).sendPerson);
		deta_falianxifangshi.setText("电话： "+loff.getData().get(0).sendPhone);
//		deta_fahuotime.setText("发货时间："+loff.getData().get(0).takeTime);
		deta_dadaotime.setText("到达时间："+loff.getData().get(0).arriveTime);
		if (!"".equals(loff.getData().get(0).appontSpace)) {
			deta_beizhu.setText("指定园区："+loff.getData().get(0).appontSpace);
		}else {
			deta_beizhu.setText("");
		}
		deta_jianshu.setText("件数："+loff.getData().get(0).cargoSize+" 件");
		
		if (loff.getData().get(0).insureCost==0.0) {
			deta_toubao.setVisibility(View.GONE);
		}
		deta_toubao.setText("投保费用："+loff.getData().get(0).insureCost+"元");
		camy_btime.setText("报价时间："+loff.getData().get(0).reportTime);
		if (loff.getData().get(0).category.equals("1")) {
			huowuzhonglei.setText("物品种类：常规货物");
		}else if (loff.getData().get(0).category.equals("2")) {
			huowuzhonglei.setText("物品种类：蔬菜");
		}else if (loff.getData().get(0).category.equals("3")) {
			huowuzhonglei.setText("物品种类：水果");
		}else if (loff.getData().get(0).category.equals("4")) {
			huowuzhonglei.setText("物品种类：牲畜及禽鱼");
		}else {
			huowuzhonglei.setVisibility(View.GONE);
		}
		if (loff.getData().get(0).insurance.equals("1")) {
			chengbaoleibie.setText("承包类型：基本险");
		}else if (loff.getData().get(0).insurance.equals("2")) {
			chengbaoleibie.setText("承包类型：综合险");
		}else {
			chengbaoleibie.setVisibility(View.GONE);
		}
//		if (!loff.getData().get(0).category.equals("")) {
//			huowuzhonglei.setText("货物种类："+loff.getData().get(0).category);
//		}else {
//			huowuzhonglei.setVisibility(View.GONE);
//		}
//		if (!loff.getData().get(0).insurance.equals("")) {
//			chengbaoleibie.setText("承保类别："+loff.getData().get(0).insurance);
//		}else {
//			chengbaoleibie.setVisibility(View.GONE);
//		}
		
//	if(loff.getData().get(0).status.equals("3")){
		btnRight.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if (!"".equals(loff.getData().get(0).pdfURL)) {
					
					 Intent intent = new Intent();
					intent.setAction(Intent.ACTION_VIEW);
					intent.setData(Uri.parse(loff.getData().get(0).pdfURL));
					// 注意此处的判断intent.resolveActivity()可以返回显示该Intent的Activity对应的组件名
					// 官方解释 : Name of the component implementing an activity that can display the intent
					if (intent.resolveActivity(getPackageManager()) != null) {    
					   ComponentName componentName = intent.resolveActivity(getPackageManager()); 
					  // 打印Log   ComponentName到底是什么
//					  Log.e("1111", "componentName = " + bean.data.get(0).advertiseHtmlUrl);
					  startActivity(Intent.createChooser(intent, "请选择浏览器"));
					} else {    
					  Toast.makeText(getApplicationContext(), "没有匹配的程序", Toast.LENGTH_SHORT).show();
					}
				}else {
					  Toast.makeText(getApplicationContext(), "暂无下载地址", Toast.LENGTH_SHORT).show();

				}	
			}
		});
//	}else {
//		btnRight.setVisibility(View.GONE);
//	}
		
//		deta_zhong.setText(getIntent().getStringExtra("cargoWeight"));
		if (loff.getData().get(0).takeCargo.equals("true")) {
			quhuo.setText("物流公司上门取货");
		}else {
			quhuo.setText("发货人送到货场");
		}
		if (loff.getData().get(0).sendCargo.equals("true")) {
			songhuo.setText("物流公司送货上门");
		}else {
			songhuo.setText("收货人自提");
		}
		
//		songhuo.setText(getIntent().getStringExtra("cargoWeight"));
//		offer_img_head.setText(getIntent().getStringExtra("matImageUrl"));
		if (loff.getData().get(0).matImageUrl!=null || !loff.getData().get(0).matImageUrl.equals("")) {
			new MyBitmapUtils().display(offer_img_head,
					loff.getData().get(0).matImageUrl);
			   offer_img_head.setBackgroundDrawable(null);
//			   offer_img_head.setBackgroundResource(R.drawable.tubgs);
		}else {
			offer_img_head.setBackgroundResource(R.drawable.tubgs);
			
		}
		deta_logoffer_username.setText("运货方: "+loff.getData().get(0).companyName);
		deta_logoffer_time.setText(loff.getData().get(0).reportTime);
//		deta_faren.setText("公司法人："+getIntent().getStringExtra("companyName"));
		deta_logoffer_userphone.setText("联系方式："+loff.getData().get(0).companyMobile);
		if (PreferencesUtils.getString(getApplicationContext(), PreferenceConstants.WLID).equals("2")) {
			coamaddse.setVisibility(View.GONE);
		}
		if (loff.getData().get(0).yardAddress.equals("")) {
			deta_addse.setVisibility(View.GONE);
			deta_ad.setVisibility(View.GONE);
		}else {
			deta_addse.setVisibility(View.VISIBLE);
			deta_ad.setVisibility(View.VISIBLE);
			deta_addse.setText(""+loff.getData().get(0).yardAddress);
		}
			
			deta_addse_liu.setText("留言："+loff.getData().get(0).luMessage);
//		deta_baojia.setText("报价："+getIntent().getStringExtra("transferMoney"));  +"送费货上门："+getIntent().getStringExtra("sendCargoMoney")+"元"
//+"运费："+getIntent().getStringExtra("cargoTotal")+"="+"总价："+getIntent().getStringExtra("transferMoney")+"元"
		if ("cold".equals(loff.getData().get(0).carType)) {
			

			String	ct=loff.getData().get(0).cargoTotal;
			String[] strs=ct.split("\\.");
			deta_baojiasang.setText(strs[0]+"元");
			deta_baojiasang1.setText("运费：");
			
			String	ct1=loff.getData().get(0).transferMoney;
			String[] strs1=ct1.split("\\.");
			deta_baojiasong.setText(strs1[0]+"元");
			deta_baojiasong1.setText("合计：");
			
			String	ct2=loff.getData().get(0).cargoTotal;
			String[] strs2=ct2.split("\\.");
			deta_baojiayun.setText(strs2[0]+"元");
			deta_baojiayun.setVisibility(View.GONE);
			deta_baojiayun1.setVisibility(View.GONE);
			String	ct3=loff.getData().get(0).transferMoney;
			String[] strs3=ct3.split("\\.");
			deta_baojiazong.setText(strs3[0]+"元");
			deta_baojiazong.setVisibility(View.GONE);
			deta_baojiazong1.setVisibility(View.GONE);
		}else {
			
		
		String	ct=loff.getData().get(0).takeCargoMoney;
		String[] strs=ct.split("\\.");
		deta_baojiasang.setText(strs[0]+"元");
		
		String	ct1=loff.getData().get(0).sendCargoMoney;
		String[] strs1=ct1.split("\\.");
		deta_baojiasong.setText(strs1[0]+"元");
		
		String	ct2=loff.getData().get(0).cargoTotal;
		String[] strs2=ct2.split("\\.");
		deta_baojiayun.setText(strs2[0]+"元");
		
		String	ct3=loff.getData().get(0).transferMoney;
		String[] strs3=ct3.split("\\.");
		deta_baojiazong.setText(strs3[0]+"元");
		}
//		deta_baojiasong.setText("送货上门费："+loff.getData().get(0).sendCargoMoney+"元");
//		deta_baojiayun.setText("运费："+loff.getData().get(0).cargoTotal+"元");
//		deta_baojiazong.setText("总价："+loff.getData().get(0).transferMoney+"元");
		
		
//		if (loff.getData().get(0).status.equals("4") || loff.getData().get(0).status.equals("5") ) {
//			deta_btnagin.setVisibility(View.VISIBLE);
//			
//		}
	}

	@Override
	public void setOnClick() {
		// TODO Auto-generated method stub
		if (getIntent().getStringExtra("status").equals("7") || getIntent().getStringExtra("status").equals("8")) {
			my_xiugai.setVisibility(View.VISIBLE);
		}
		if (getIntent().getStringExtra("status").equals("8")) {
			my_chongxuan.setVisibility(View.VISIBLE);
		}
		btnLeft.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				finish();
			}
		});
		my_xiugai.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(LogistioffActivity.this, TradingcertaintyActivity.class);
				intent.putExtra("userId", loff.getData().get(0).userToId);//+loff.getData().get(0).recId
				intent.putExtra("WLBId", loff.getData().get(0).recId);//
		 		intent.putExtra("billCode",loff.getData().get(0).billCode);//物流单号
		 		intent.putExtra("wayde","1");//
		 		intent.putExtra("takeCargoMoney", loff.getData().get(0).takeCargoMoney);//上门取货费
		 		intent.putExtra("sendCargoMoney", loff.getData().get(0).sendCargoMoney);//送货上门费
				intent.putExtra("cargoTotal", loff.getData().get(0).cargoTotal);//运费
		 		intent.putExtra("insureCost", loff.getData().get(0).cargoCost);//保费
		 		intent.putExtra("transferMoney",loff.getData().get(0).transferMoney);
		 		intent.putExtra("takeCargo",loff.getData().get(0).takeCargo);
		 		intent.putExtra("carType", loff.getData().get(0).carType);
				intent.putExtra("carName", loff.getData().get(0).carName);
				intent.putExtra("tem", loff.getData().get(0).tem);
			    startActivity(intent);
			    finish();
			}
		}); 
		
		my_chongxuan.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(LogistioffActivity.this, LogistiOfferActivity.class);
				intent.putExtra("cargoName", loff.getData().get(0).cargoName);
				intent.putExtra("cargoWeight",loff.getData().get(0).cargoWeight);
				intent.putExtra("cargoVolume", loff.getData().get(0).cargoVolume);
				intent.putExtra("startPlace", loff.getData().get(0).startPlace);
				intent.putExtra("entPlace", loff.getData().get(0).entPlace);
				intent.putExtra("takeName", loff.getData().get(0).takeName);
				intent.putExtra("takeMobile", loff.getData().get(0).takeMobile);
				intent.putExtra("publishTime", loff.getData().get(0).publishTime);
				intent.putExtra("takeCargo", loff.getData().get(0).takeCargo);
				intent.putExtra("sendCargo", loff.getData().get(0).sendCargo);
				intent.putExtra("remark", loff.getData().get(0).remark);
				intent.putExtra("takeTime", loff.getData().get(0).takeTime);
				intent.putExtra("arriveTime", loff.getData().get(0).arriveTime);
				intent.putExtra("recId", loff.getData().get(0).recId);
				intent.putExtra("billCode", loff.getData().get(0).billCode);
				intent.putExtra("whether","2");
//				intent.putExtra("premium", loff.getData().get(0).premium);
				intent.putExtra("status", loff.getData().get(0).status);
				intent.putExtra("sendPerson", loff.getData().get(0).sendPerson);
				intent.putExtra("sendPhone", loff.getData().get(0).sendPhone);
				startActivity(intent);
			}
		});
		//再来一单
		deta_btnagin.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(LogistioffActivity.this, LogistiOfferActivity.class);
				intent.putExtra("cargoName", loff.getData().get(0).cargoName);
				intent.putExtra("cargoWeight",loff.getData().get(0).cargoWeight);
				intent.putExtra("cargoVolume", loff.getData().get(0).cargoVolume);
				intent.putExtra("startPlace", loff.getData().get(0).startPlace);
				intent.putExtra("entPlace", loff.getData().get(0).entPlace);
				intent.putExtra("takeName", loff.getData().get(0).takeName);
				intent.putExtra("takeMobile", loff.getData().get(0).takeMobile);
				intent.putExtra("publishTime", loff.getData().get(0).publishTime);
				intent.putExtra("takeCargo", loff.getData().get(0).takeCargo);
				intent.putExtra("sendCargo", loff.getData().get(0).sendCargo);
				intent.putExtra("remark", loff.getData().get(0).remark);
				intent.putExtra("takeTime", loff.getData().get(0).takeTime);
				intent.putExtra("arriveTime", loff.getData().get(0).arriveTime);
				intent.putExtra("recId", loff.getData().get(0).recId);
				intent.putExtra("billCode", loff.getData().get(0).billCode);
				intent.putExtra("whether","2");
//				intent.putExtra("premium", loff.getData().get(0).premium);
				intent.putExtra("status", loff.getData().get(0).status);
				intent.putExtra("sendPerson", loff.getData().get(0).sendPerson);
				intent.putExtra("sendPhone", loff.getData().get(0).sendPhone);

				intent.putExtra("fromLatitude", loff.getData().get(0).latitude);
				intent.putExtra("fromLongitude", loff.getData().get(0).longitude);
				intent.putExtra("cityCode", loff.getData().get(0).startPlaceCityCode);
				intent.putExtra("cityCodeTo", loff.getData().get(0).entPlaceCityCode);
				intent.putExtra("payTime", loff.getData().get(0).payTime);
				intent.putExtra("sendPhone", loff.getData().get(0).whether);
				intent.putExtra("toLatitude", loff.getData().get(0).latitudeTo);
				intent.putExtra("toLongitude", loff.getData().get(0).longitudeTo);
				intent.putExtra("endPlaceName", loff.getData().get(0).endPlaceName);
				intent.putExtra("cargoSize", loff.getData().get(0).cargoSize);

				
				
				startActivity(intent);
			}
		});
		
	}

	@Override
	public void getData() {
		// TODO Auto-generated method stub
		
	}
		public void getmessage(){
			dialog.show();
			RequestParams params = new RequestParams();
			AsyncHttpUtils.doGet(UrlMap.getUrl(MCUrl.QuotationInfo, "recId",recId+""), null, null, params,
					new AsyncHttpResponseHandler() {
						@Override
						public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
							Log.e("json", new String(arg2));
							dialog.dismiss();
							 loff = new Gson().fromJson(new String(arg2),
									LoOfferBean.class);
							if (loff.errCode==0) {
								initData();
							}
						}

						@Override
						public void onFailure(int arg0, Header[] arg1, byte[] arg2, Throwable arg3) {
							// TODO Auto-generated method stub
							dialog.dismiss();
						}


					});
		}
}
