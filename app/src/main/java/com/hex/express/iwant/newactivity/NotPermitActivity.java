package com.hex.express.iwant.newactivity;

import org.apache.http.Header;

import com.google.gson.Gson;
import com.hex.express.iwant.R;
import com.hex.express.iwant.iWantApplication;
import com.hex.express.iwant.activities.BaseActivity;
import com.hex.express.iwant.activities.NotPermittPayActivity;
import com.hex.express.iwant.activities.NotPermittedActivity;
import com.hex.express.iwant.bean.BaseBean;
import com.hex.express.iwant.bean.LoOfferBean;
import com.hex.express.iwant.constance.MCUrl;
import com.hex.express.iwant.constance.PreferenceConstants;
import com.hex.express.iwant.http.AsyncHttpUtils;
import com.hex.express.iwant.http.UrlMap;
import com.hex.express.iwant.utils.PreferencesUtils;
import com.hex.express.iwant.utils.ToastUtil;
import com.hex.express.iwant.views.TitleBarView;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import butterknife.Bind;
import butterknife.ButterKnife;

public class NotPermitActivity extends BaseActivity{
	
	
	@Bind(R.id.tbv_show)
	TitleBarView tbv_show;
//	@Bind(R.id.text_goodName)
//	TextView text_goodName;
//	@Bind(R.id.text_goodWeight)
//	TextView text_goodWeight;
	@Bind(R.id.text_goodName)
	TextView text_goodName;
	@Bind(R.id.text_remak1)
	TextView text_remak1;
	
	@Bind(R.id.text_goodWeight1)
	TextView text_goodWeight1;
	@Bind(R.id.text_goodSize1)
	TextView text_goodSize1;
	
	// 发件人
	@Bind(R.id.text_time)
	TextView text_time;
	@Bind(R.id.text_name)
	TextView text_name;
	@Bind(R.id.text_phone)
	TextView text_phone;
	@Bind(R.id.text_address)
	TextView text_address;
	// 收件人
	@Bind(R.id.text_name_to)
	TextView text_name_to;
	@Bind(R.id.text_phone_to)
	TextView text_phone_to;
	@Bind(R.id.text_address_to)
	TextView text_address_to;
	@Bind(R.id.text_money)
	TextView text_money;
	@Bind(R.id.lout1)
	LinearLayout lout1;
	@Bind(R.id.lout10)
	RelativeLayout lout10;
	@Bind(R.id.text_time_to)
	TextView text_time_to;
	@Bind(R.id.replaceMoney1)
	TextView replaceMoney1;
	@Bind(R.id.text_goodjian1)
	TextView text_goodjian1;
	@Bind(R.id.cartype)
	TextView cartype;
	@Bind(R.id.text_carchang)
	TextView text_carchang;
	@Bind(R.id.text_tijis)
	TextView text_tijis;
	@Bind(R.id.text_usertime)
	TextView text_usertime;
	
	@Bind(R.id.editText1)//上门取货费
	EditText editText1;
	@Bind(R.id.editText2)//送货上门费
	EditText editText2;
	@Bind(R.id.spec_edt_yuan)//送货上门费
	EditText spec_edt_yuan;
	
	@Bind(R.id.editText22)////总价
	EditText editText22;
	
	@Bind(R.id.text_total)//
	TextView text_total;
	@Bind(R.id.text_compname)//公司名称
	TextView text_compname;
	@Bind(R.id.text_huochangadse)//货场地址
	TextView text_huochangadse;
	@Bind(R.id.text_liuyan)//留言
	TextView text_liuyan;
	@Bind(R.id.text_re1)//
	TextView text_re1;
	@Bind(R.id.sumit)//
	Button sumit;
	
	@Bind(R.id.text_goodcold)//
	TextView text_goodcold;
	
	
	@Bind(R.id.r1)
	RelativeLayout r1;// 上门取货费
	@Bind(R.id.r2)
	RelativeLayout r2;//送货上门费
	@Bind(R.id.r3)
	RelativeLayout r3;//运费
	LoOfferBean loff;
	int recId;
@Override
protected void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	setContentView(R.layout.notpermi_activity);
	iWantApplication.getInstance().addActivity(this);
	ButterKnife.bind(NotPermitActivity.this);
	getData();
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
		if (getIntent().getIntExtra("recId", 0)!=0) {
			recId=getIntent().getIntExtra("recId", 0);
			getMessages();
		}else {
			ToastUtil.shortToast(NotPermitActivity.this, "网络请求失败！");
		}
		
	}

	@Override
	public void initData() {
		// TODO Auto-generated method stub
		text_time_to.setText(""+loff.data.get(0).arriveTime);
		text_tijis.setText("总体积："+loff.getData().get(0).cargoVolume);
//		text_tijis.setText("单件规格：  长："+loff.data.get(0).length+" 宽："+loff.data.get(0).wide+" 高："+loff.data.get(0).high);
		text_goodWeight1.setText("总重量："+loff.data.get(0).cargoWeight);
		if ("cold".equals(loff.data.get(0).carType)) {
			
			text_goodcold.setText("需求："+loff.data.get(0).carName);
		}
		text_goodjian1.setText("件数："+loff.data.get(0).cargoSize);
		if (!loff.data.get(0).tem.equals("")) {
			
		}
		if (loff.data.get(0).takeCargo.equals("true")) {
			text_re1.setText("物流公司上门取货");
		}else {
			text_re1.setText("用户送到货场");
		}
		if (loff.data.get(0).sendCargo.equals("true")) {
			text_goodSize1.setText("物流公司送货上门");
		}else {
			text_goodSize1.setText("收件人自提");
		}

		text_goodName.setText("货物名称："+loff.data.get(0).cargoName);
		text_name.setText(""+loff.data.get(0).sendName);
		text_phone.setText(""+loff.data.get(0).sendMobile);
		text_address.setText(""+loff.data.get(0).startPlace);
		
		text_name_to.setText(""+loff.data.get(0).takeName);
		text_phone_to.setText(""+loff.data.get(0).takeMobile);
		text_address_to.setText(""+loff.data.get(0).entPlace);
		text_compname.setText(""+loff.data.get(0).companyName);
		
		text_huochangadse.setText(""+loff.data.get(0).yardAddress);
		text_liuyan.setText(""+loff.data.get(0).luMessage);
		
		editText1.setText(""+loff.data.get(0).takeCargoMoney);
		editText2.setText(""+loff.data.get(0).sendCargoMoney);
		spec_edt_yuan.setText(""+loff.data.get(0).cargoTotal);
		editText22.setText(""+loff.data.get(0).transferMoney);
		
		
		sumit.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(NotPermitActivity.this, NotPermittPayActivity.class);
			 	   intent.putExtra("playMoneyMin",getIntent().getStringExtra("playMoneyMin"));//余额支付 的价格	
			 		intent.putExtra("playMoneyMax", getIntent().getStringExtra("playMoneyMax"));//其它支付的价格
			 		intent.putExtra("billCode", getIntent().getStringExtra("billCode"));//物流单号
			 		startActivity(intent);
			 		finish();
			}
		});
	}

	@Override
	public void setOnClick() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void getData() {
		// TODO Auto-generated method stub
		
	}
	private void getMessages(){
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
