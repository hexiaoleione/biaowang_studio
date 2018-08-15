package com.hex.express.iwant.newactivity;

import org.apache.http.Header;

import com.google.gson.Gson;
import com.hex.express.iwant.R;
import com.hex.express.iwant.iWantApplication;
import com.hex.express.iwant.activities.BaseActivity;
import com.hex.express.iwant.activities.DownOwnerDetialsActivity;
import com.hex.express.iwant.bean.MyloginBeanlo;
import com.hex.express.iwant.constance.MCUrl;
import com.hex.express.iwant.constance.PreferenceConstants;
import com.hex.express.iwant.http.AsyncHttpUtils;
import com.hex.express.iwant.http.UrlMap;
import com.hex.express.iwant.utils.MyBitmapUtils;
import com.hex.express.iwant.utils.PreferencesUtils;
import com.hex.express.iwant.utils.ToastUtil;
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
public class LogoffActivitybao extends BaseActivity{
	
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
	@Bind(R.id.btnLeft)//次数
	ImageView btnLeft;
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
	@Bind(R.id.deta_faname)//手
	TextView deta_faname;
	@Bind(R.id.deta_falianxifangshi)//手机号
	TextView deta_falianxifangshi;
	
	@Bind(R.id.deta_jian)//手机号
	TextView deta_jian;
	@Bind(R.id.deta_agin)//再来一单
	TextView deta_agin;
	
	@Bind(R.id.deta_carcold)//
	TextView deta_carcold;
	

	@Bind(R.id.tem)//
	TextView tem;
	@Bind(R.id.recoldcar)//冷链
	RelativeLayout	recoldcar;
	@Bind(R.id.resonghuo)//正常
	RelativeLayout	resonghuo;
	@Bind(R.id.lystb)//投保
	LinearLayout lystb;
	@Bind(R.id.lyagin)//再来
	LinearLayout lyagin;
	

	@Bind(R.id.huowujia)//
	TextView huowujia;

	@Bind(R.id.huozhong)//
	TextView huozhong;

	@Bind(R.id.chengxian)//
	TextView chengxian;

	@Bind(R.id.chepan)//
	TextView chepan;

	@Bind(R.id.menybao)//
	TextView menybao;
	@Bind(R.id.baodanhao)//
	TextView baodanhao;
	
	@Bind(R.id.bntbaodan)//
	Button bntbaodan;
	
	
	
	int recId;
	MyloginBeanlo loff;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_mylogbao);//activity_mylogisti
		iWantApplication.getInstance().addActivity(this);
		ButterKnife.bind(LogoffActivitybao.this);
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
		
		deta_fahuodizhi.setFocusable(false);
		deta_dadaodizhi.setFocusable(false);
		huoname.setText("物品名称："+loff.getData().get(0).cargoName);
//		if (loff.getData().get(0).weight.equals("5")) {
//			deta_zhong.setText("总重量：≤"+loff.getData().get(0).cargoWeight+"");
//		}else {
			deta_zhong.setText("总重量："+loff.getData().get(0).cargoWeight+"");
//		}
	
		deta_fabutime.setText(loff.getData().get(0).publishTime);
		deta_tiji.setText("总体积："+loff.getData().get(0).cargoVolume);
//		deta_tiji.setText("货物体积："+" 长："+loff.getData().get(0).length+" 厘米"+" 宽："+loff.getData().get(0).wide+" 厘米"+" 高："+loff.getData().get(0).high+" 厘米");
//		String	c1t=loff.getData().get(0).cargoCost;
//		String[] str1s=c1t.split("\\.");
//		if (str1s[0].equals("0")) {
//			jiazhi.setVisibility(View.GONE);	
//		}
//		jiazhi.setText("货物价值："+str1s[0]+"元");
//		jiazhi.setText("货物价值："+loff.getData().get(0).cargoCost+"元");
		if ("cold".equals(loff.getData().get(0).carType)) {
			recoldcar.setVisibility(View.VISIBLE);
			resonghuo.setVisibility(View.GONE);
			tem.setText("温度要求："+loff.getData().get(0).tem);
		}else {
			recoldcar.setVisibility(View.GONE);
//			resonghuo.setVisibility(View.VISIBLE);
		}
		if ("cold".equals(loff.getData().get(0).carType)) {
			deta_carcold.setText("需求："+loff.getData().get(0).carName);
		}
		deta_jian.setText("件数："+loff.getData().get(0).cargoSize+" 件");
		deta_fahuodizhi.setText(loff.getData().get(0).startPlace);
		deta_dadaodizhi.setText(loff.getData().get(0).entPlace);
		deta_shouhuoname.setText("收货人："+loff.getData().get(0).takeName);
		deta_lianxifangshi.setText("电话： "+loff.getData().get(0).takeMobile);
		deta_faname.setText("发件人："+loff.getData().get(0).sendPerson);
		deta_falianxifangshi.setText("电话： "+loff.getData().get(0).sendPhone);
//		deta_fahuotime.setText("发货时间："+loff.getData().get(0).takeTime);
		deta_dadaotime.setText("要求到达时间："+loff.getData().get(0).arriveTime);
		if (!"".equals(loff.getData().get(0).appontSpace)) {
			deta_beizhu.setText("指定园区："+loff.getData().get(0).appontSpace);
		}else {
			deta_beizhu.setText("");
		}
		if (loff.getData().get(0).status.equals("11") ) {
			bntbaodan.setVisibility(View.GONE);
		}else {
			bntbaodan.setVisibility(View.VISIBLE);
		}
		if (loff.getData().get(0).status.equals("10") || loff.getData().get(0).status.equals("11") 
				|| loff.getData().get(0).status.equals("12") ) {
			lyagin.setVisibility(View.GONE);
			lystb.setVisibility(View.VISIBLE);
			if (loff.getData().get(0).status.equals("12") ) {
				baodanhao.setVisibility(View.VISIBLE);
			}else {
				baodanhao.setVisibility(View.GONE);
			}
			baodanhao.setText("保单号："+loff.getData().get(0).remark+"");
			huowujia.setText("货物价值："+loff.getData().get(0).cargoCost+"元");
			if (loff.getData().get(0).category.equals("1")) {
				huozhong.setText("货物种类：常规货物");
			}else if (loff.getData().get(0).category.equals("2")) {
				huozhong.setText("货物种类：蔬菜");
			}else if (loff.getData().get(0).category.equals("3")) {
				huozhong.setText("货物种类：水果");
			}else if (loff.getData().get(0).category.equals("4")) {
				huozhong.setText("货物种类：牲畜及禽鱼");
			}
			if (loff.getData().get(0).insurance.equals("1")) {
				chengxian.setText("承保类型：基本险");
			}else if (loff.getData().get(0).insurance.equals("2")) {
				chengxian.setText("承保类型：综合险");
			}
		
			
			chepan.setText("车牌号："+loff.getData().get(0).carNumImg);
			menybao.setText("投保费用："+loff.getData().get(0).insureCost+"元");
//			btnRight.setText("下载保单");
			
		}else {
			btnRight.setText("");
			lyagin.setVisibility(View.GONE);
			lystb.setVisibility(View.GONE);
		}
//		deta_agin.setOnClickListener(new OnClickListener() {
//			
//			@Override
//			public void onClick(View arg0) {
//				// TODO Auto-generated method stub
//				Intent  intent=new Intent();
//				intent.setClass(LogoffActivitybao.this, ReleaseActivity.class);
//				intent.putExtra("matName", loff.getData().get(0).cargoName);
//				intent.putExtra("matWeight", loff.getData().get(0).weight);
//				intent.putExtra("personName", loff.getData().get(0).sendPerson);
//				intent.putExtra("mobile",    loff.getData().get(0).sendPhone);
//				intent.putExtra("address",  loff.getData().get(0).startPlace);
//				intent.putExtra("personNameTo",loff.getData().get(0).takeName);
//				intent.putExtra("mobileTo",  loff.getData().get(0).takeMobile);
//				intent.putExtra("addressTo", loff.getData().get(0).entPlace);
//				intent.putExtra("matRemark", "");
////				intent.putExtra("category", getIntent().getStringExtra("category"));
////				intent.putExtra("money", getIntent().getStringExtra("money"));
//				intent.putExtra("status", loff.getData().get(0).status);
////				intent.putExtra("recId", getIntent().getIntExtra("recId", 0));
//				intent.putExtra("limitTime", loff.getData().get(0).arriveTime);
////				intent.putExtra("finishTime", getIntent().getStringExtra("finishTime"));
//				intent.putExtra("replaceMoney", "");
//				intent.putExtra("ifReplaceMoney", "");
//				intent.putExtra("ifTackReplace", "");
//				intent.putExtra("carType", loff.getData().get(0).carType);
//				intent.putExtra("insureCost", "");
//				intent.putExtra("premium", "");
////				intent.putExtra("carLength", getIntent().getStringExtra("carLength"));
//				intent.putExtra("matVolume", loff.getData().get(0).carName);
////				intent.putExtra("useTime", getIntent().getStringExtra("useTime"));
//				intent.putExtra("cargoSize",  loff.getData().get(0).cargoSize);
//				intent.putExtra("cityCode",   loff.getData().get(0).startPlaceCityCode);
//				intent.putExtra("fromLatitude",    loff.getData().get(0).latitude);
//				intent.putExtra("fromLongitude",    loff.getData().get(0).longitude);
//				intent.putExtra("cityCodeTo",    loff.getData().get(0).entPlaceCityCode);
//				intent.putExtra("toLatitude",    loff.getData().get(0).latitudeTo);
//				intent.putExtra("toLongitude",    loff.getData().get(0).longitudeTo);
//				intent.putExtra("townCode",    loff.getData().get(0).startPlaceTownCode);
//				
//				
//				intent.putExtra("cargoVolume",    loff.getData().get(0).cargoVolume);
//				intent.putExtra("appontSpace",    loff.getData().get(0).appontSpace);
//				intent.putExtra("takeCargo",    loff.getData().get(0).takeCargo);
//				intent.putExtra("sendCargo",    loff.getData().get(0).sendCargo);
//				intent.putExtra("tem",    loff.getData().get(0).tem);
//				startActivity(intent);
//				finish();
//			}
//		});
		
		
//	if(loff.getData().get(0).status.equals("3")){
		bntbaodan.setOnClickListener(new OnClickListener() {
			
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
		if (loff.getData().get(0).takeCargo) {
			quhuo.setText("物流公司上门取货");
		}else {
			quhuo.setText("发货人送到货场");
		}
//		ToastUtil.shortToast(LogoffActivity.this, " sendCargo "+loff.getData().get(0).sendCargo.equals("true"));
		if (loff.getData().get(0).sendCargo) {
			songhuo.setText("物流公司送货上门");
		}else {
			songhuo.setText("收货人自提");
		}
		
//		deta_baojia.setText("报价："+getIntent().getStringExtra("transferMoney"));  +"送费货上门："+getIntent().getStringExtra("sendCargoMoney")+"元"
//+"运费："+getIntent().getStringExtra("cargoTotal")+"="+"总价："+getIntent().getStringExtra("transferMoney")+"元"
		
		
//		deta_baojiasong.setText("送货上门费："+loff.getData().get(0).sendCargoMoney+"元");
//		deta_baojiayun.setText("运费："+loff.getData().get(0).cargoTotal+"元");
//		deta_baojiazong.setText("总价："+loff.getData().get(0).transferMoney+"元");
	}

	@Override
	public void setOnClick() {
		// TODO Auto-generated method stub
		btnLeft.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				finish();
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
			AsyncHttpUtils.doGet(UrlMap.getUrl(MCUrl.info, "recId",recId+""), null, null, params,
					new AsyncHttpResponseHandler() {
						@Override
						public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
							Log.e("json", new String(arg2));
							dialog.dismiss();
							 loff = new Gson().fromJson(new String(arg2),
									 MyloginBeanlo.class);
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
