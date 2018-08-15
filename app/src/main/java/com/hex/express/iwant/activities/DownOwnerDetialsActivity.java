package com.hex.express.iwant.activities;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.Header;

import com.daimajia.androidanimations.library.Techniques;
import com.google.gson.Gson;
import com.hex.express.iwant.R;
import com.hex.express.iwant.iWantApplication;
import com.hex.express.iwant.adapters.SendOwnerAdapter;
import com.hex.express.iwant.bean.BaseBean;
import com.hex.express.iwant.bean.DownSpecialBean;
import com.hex.express.iwant.constance.MCUrl;
import com.hex.express.iwant.constance.PreferenceConstants;
import com.hex.express.iwant.http.AsyncHttpUtils;
import com.hex.express.iwant.http.UrlMap;
import com.hex.express.iwant.newactivity.ReleaseActivity;
import com.hex.express.iwant.newsmain.NewMainActivity;
import com.hex.express.iwant.utils.MyBitmapUtils;
import com.hex.express.iwant.utils.PreferencesUtils;
import com.hex.express.iwant.utils.StringUtil;
import com.hex.express.iwant.utils.ToastUtil;
import com.hex.express.iwant.views.TitleBarView;
import com.loopj.android.http.AsyncHttpResponseHandler;

import android.R.integer;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import butterknife.Bind;
import butterknife.ButterKnife;
/**
 * 顺风发布详情
 * @author huyichuan
 *
 */
public class DownOwnerDetialsActivity extends BaseActivity {
//	@Bind(R.id.iv_headle)
//	ImageView iv_headle;
	@Bind(R.id.tbv_show)
	TitleBarView tbv_show;
	@Bind(R.id.text_goodName)
	TextView text_goodName;
//	@Bind(R.id.text_goodWeight)
//	TextView text_goodWeight;
//	@Bind(R.id.text_goodSize1)
//	TextView text_goodSize1;
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
//	@Bind(R.id.text_carchang1)
//	TextView text_carchang1;
//	@Bind(R.id.text_tijis)
//	TextView text_tijis1;
//	@Bind(R.id.text_usertime1)
//	TextView text_usertime1;
	@Bind(R.id.text_re1)
	TextView text_re1;
//	@Bind(R.id.text_re)
//	TextView text_re;
	
	
	
	@Bind(R.id.own_ok)//投诉同意
	Button own_ok;
	@Bind(R.id.own_no)//投诉不同意
	Button own_no;
	@Bind(R.id.detil_huo)
	RelativeLayout detil_huo;
	@Bind(R.id.lyagain)
	LinearLayout	lyagain;
	@Bind(R.id.btnagin)
	Button btnagin;
	@Bind(R.id.bezhu)
    TextView	bezhu;
	
	//定义Action常量  
    protected static final String ACTION = "com.android.broadcast.RECEIVER_ACTION";  
	private String status;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_owner_newdetial);//activity_owner_newdetial   activity_owner_detial
		iWantApplication.getInstance().addActivity(this);
		ButterKnife.bind(DownOwnerDetialsActivity.this);
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
		tbv_show.setTitleText("发布详情");
		
//		if (!"".equals(getIntent().getStringExtra("matImageUrl"))) {
//			new MyBitmapUtils().display(iv_headle, getIntent().getStringExtra("matImageUrl"));
//		}else {
//			iv_headle.setBackgroundResource(R.drawable.wu_cou);
//		}
		text_goodName.setText("物品名称: " + getIntent().getStringExtra("matName"));
		if (getIntent().getStringExtra("matWeight").equals("5")) {
			text_goodWeight1.setText( "总重量：≤"+getIntent().getStringExtra("matWeight") + " 公斤 ");
		}else {
			text_goodWeight1.setText( "总重量："+getIntent().getStringExtra("matWeight") + " 公斤 ");
		}
		
		
//		if (!"".equals(getIntent().getStringExtra("money"))) {
//			text_usertime1.setText(getIntent().getStringExtra("money")+"元");
//		}else {
//			text_usertime1.setVisibility(View.INVISIBLE);
//			text_re.setVisibility(View.INVISIBLE);
//		}
		//+"镖费："+getIntent().getStringExtra("money")
		
		try {
			double num1 = 0.0;
			double num2 = 0.0;
			num1 = Double.valueOf(getIntent().getStringExtra("money"));//money
			num2 = Double.valueOf(getIntent().getStringExtra("insureCost"));
			DecimalFormat df = new DecimalFormat("###.00");  
			text_re1.setText("运费："+df.format(StringUtil.sub(num1,num2))+" 元");
		} catch (Exception e) {
			// TODO: handle exception
		}
		
		
		if (!getIntent().getStringExtra("insureCost").equals("") && !getIntent().getStringExtra("premium").equals("")) {
			text_goodSize1.setText("保费:"+getIntent().getStringExtra("insureCost")+"元");//+"(保额"+getIntent().getStringExtra("premium")+"元）"
		}else {
//			text_goodSize.setVisibility(View.INVISIBLE);
			text_goodSize1.setVisibility(View.INVISIBLE);
		}
		if (!"".equals(getIntent().getStringExtra("money"))) {
////			text_usertime.setText("取货ོ时间："+getIntent().getStringExtra("useTime"));
//			text_re1.setText("运费："+getIntent().getStringExtra("money")+" 元");
		}
			text_goodjian1.setText("件数："+getIntent().getStringExtra("cargoSize")+" 件");
		
//			else {
//			text_usertime.setVisibility(View.INVISIBLE);
//			text_re1.setVisibility(View.INVISIBLE);
//		}
		if (getIntent().getStringExtra("limitTime").equals("")) {
			lout1.setVisibility(View.GONE);
		}else {
			text_time_to.setText(getIntent().getStringExtra("limitTime"));
		}
		if (getIntent().getStringExtra("useTime").equals("")) {
//			lout10.setVisibility(View.GONE);
		}else {
			text_time_to.setText(getIntent().getStringExtra("useTime"));
		}
		
//			text_tijis.setText("单件规格："+getIntent().getStringExtra("length"));
//		if (getIntent().getStringExtra("matVolume").equals("")) {
//			text_tijis.setText("要求车型：无要求");
//		}else {
			text_tijis.setText("车型要求："+getIntent().getStringExtra("matVolume"));
//		}
		
//		else {
//			text_tijis1.setVisibility(View.INVISIBLE);
//			text_tijis.setVisibility(View.INVISIBLE);
//		}
//		if (!"".equals(getIntent().getStringExtra("carLength"))) {
//			if(getIntent().getStringExtra("carLength").equals("1")){
//				text_carchang1.setText("无");
//			}else if(getIntent().getStringExtra("carLength").equals("2")){
//				text_carchang1.setText("1.8米");
//			}else if (getIntent().getStringExtra("carLength").equals("3")) {
//				text_carchang1.setText("2.7米");
//			}else if (getIntent().getStringExtra("carLength").equals("4")) {
//				text_carchang1.setText("4.2米");
//			}
//		}else {
//			text_carchang1.setVisibility(View.GONE);
//			text_carchang.setVisibility(View.GONE);
//		}
		text_name.setText(getIntent().getStringExtra("personName"));
		text_phone.setText(getIntent().getStringExtra("mobile"));
		text_address.setText(getIntent().getStringExtra("address"));
		text_name_to.setText(getIntent().getStringExtra("personNameTo"));
		text_phone_to.setText(getIntent().getStringExtra("mobileTo"));
		text_address_to.setText(getIntent().getStringExtra("addressTo"));
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
//			}
//		}else {
//			cartype.setVisibility(View.GONE);
//		}
		
//		text_remak.setVisibility(View.GONE);
		if (!getIntent().getStringExtra("matRemark").equals("")) {
			bezhu.setText("备注: "+getIntent().getStringExtra("matRemark"));
		}else {
//			text_remak1.setVisibility(View.GONE);
		}
		if (getIntent().getStringExtra("ifReplaceMoney").equals("true") ) {
			replaceMoney1.setVisibility(View.VISIBLE);
			replaceMoney1.setText("代收款:"+getIntent().getStringExtra("replaceMoney")+"元");
		}else {
			replaceMoney1.setVisibility(View.INVISIBLE);
		}
		if(getIntent().getStringExtra("money").equals("")){
			text_money.setVisibility(View.GONE);
		}
		Log.e("11111", "sd  "+getIntent().getStringExtra("money"));
//		text_money.setText("费用："+getIntent().getStringExtra("money"));
		status=getIntent().getStringExtra("status");
				if (!status.equals("9")) {
					detil_huo.setVisibility(View.GONE);
					lyagain.setVisibility(View.GONE);
				}else {
					lyagain.setVisibility(View.GONE);
					
				}
//				if (!status.equals("0") || !status.equals("1") || !status.equals("2") || !status.equals("3")
//						|| !status.equals("4")  || !status.equals("9")) {
//					lyagain.setVisibility(View.VISIBLE);
//				}else {
//					lyagain.setVisibility(View.GONE);
//				}
//				
		
	}

	@Override
	public void setOnClick() {
		// TODO Auto-generated method stub
		own_ok.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				getmegse();
			}
		});
		own_no.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				getmegsetwo();
			}
		});
		
		btnagin.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				
				Intent  intent=new Intent();
				intent.setClass(DownOwnerDetialsActivity.this, ReleaseActivity.class);
				intent.putExtra("matImageUrl", getIntent().getStringExtra("matImageUrl"));
				intent.putExtra("matName", getIntent().getStringExtra("matName"));
				intent.putExtra("matWeight", getIntent().getStringExtra("matWeight"));
				intent.putExtra("length", getIntent().getStringExtra("length"));
				intent.putExtra("publishTime", getIntent().getStringExtra("publishTime"));
				intent.putExtra("personName", getIntent().getStringExtra("personName"));
				intent.putExtra("mobile", getIntent().getStringExtra("mobile"));
				intent.putExtra("address", getIntent().getStringExtra("address"));
				intent.putExtra("personNameTo", getIntent().getStringExtra("personNameTo"));
				intent.putExtra("mobileTo", getIntent().getStringExtra("mobileTo"));
				intent.putExtra("addressTo", getIntent().getStringExtra("addressTo"));
				intent.putExtra("matRemark", getIntent().getStringExtra("matRemark"));
				intent.putExtra("category", getIntent().getStringExtra("category"));
				intent.putExtra("money", getIntent().getStringExtra("money"));
				intent.putExtra("status", getIntent().getStringExtra("status"));
				intent.putExtra("recId", getIntent().getIntExtra("recId", 0));
				intent.putExtra("limitTime", getIntent().getStringExtra("limitTime"));
				intent.putExtra("finishTime", getIntent().getStringExtra("finishTime"));
				intent.putExtra("replaceMoney", getIntent().getStringExtra("replaceMoney"));
				intent.putExtra("ifReplaceMoney", getIntent().getStringExtra("ifReplaceMoney"));
				intent.putExtra("ifTackReplace", getIntent().getStringExtra("ifTackReplace"));
				intent.putExtra("carType", getIntent().getStringExtra("carType"));
				intent.putExtra("insureCost", getIntent().getStringExtra("insureCost"));
				intent.putExtra("premium", getIntent().getStringExtra("premium"));
				intent.putExtra("carLength", getIntent().getStringExtra("carLength"));
				intent.putExtra("matVolume", getIntent().getStringExtra("matVolume"));
				intent.putExtra("useTime", getIntent().getStringExtra("useTime"));
				intent.putExtra("cargoSize",  getIntent().getStringExtra("cargoSize"));
				intent.putExtra("cityCode",   getIntent().getStringExtra("cityCode"));
				intent.putExtra("fromLatitude",   getIntent().getStringExtra("fromLatitude"));
				intent.putExtra("fromLongitude",   getIntent().getStringExtra("fromLongitude"));
				intent.putExtra("cityCodeTo",   getIntent().getStringExtra("cityCodeTo"));
				intent.putExtra("toLatitude",   getIntent().getStringExtra("toLatitude"));
				intent.putExtra("toLongitude",   getIntent().getStringExtra("toLongitude"));
				intent.putExtra("cityCode",   getIntent().getStringExtra("cityCode"));
				intent.putExtra("whether",   getIntent().getStringExtra("whether"));
				intent.putExtra("townCode",   getIntent().getStringExtra("townCode"));
				//物流有的
				intent.putExtra("cargoVolume",   "");
				intent.putExtra("appontSpace",    "");
				
				startActivity(intent);
				finish();
			}
		});
	}
	private void getmegse(){
		String url = UrlMap.getTwo(MCUrl.CustomerChoose, "recId",
				String.valueOf(getIntent().getIntExtra("recId", 0)), "ifAgree",
				String.valueOf(1));
		dialog.show();
		AsyncHttpUtils.doSimGet(url, new AsyncHttpResponseHandler() {
			@Override
			public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
//				Log.e("11111111", new String(arg2));
				dialog.dismiss();
				BaseBean baseBean=new Gson().fromJson(new String(arg2), BaseBean.class);
				if (baseBean.getErrCode()==0) {
					summit();
					finish();
				}else {
					ToastUtil.shortToast(DownOwnerDetialsActivity.this, baseBean.getMessage());
				}
				
			}
			@Override
			public void onFailure(int arg0, Header[] arg1, byte[] arg2, Throwable arg3) {
				dialog.dismiss();
			}
		});
	}
private void getmegsetwo(){
	String url = UrlMap.getTwo(MCUrl.CustomerChoose, "recId",
		String.valueOf(getIntent().getIntExtra("recId", 0)), "ifAgree",
		String.valueOf(0));
dialog.show();
AsyncHttpUtils.doSimGet(url, new AsyncHttpResponseHandler() {
	@Override
	public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
//		Log.e("11111111", new String(arg2));
		dialog.dismiss();
		BaseBean baseBean=new Gson().fromJson(new String(arg2), BaseBean.class);
		if (baseBean.getErrCode()==0) {
			summit();
			finish();
		}else {
			ToastUtil.shortToast(DownOwnerDetialsActivity.this, baseBean.getMessage());
		}
		
	}
	@Override
	public void onFailure(int arg0, Header[] arg1, byte[] arg2, Throwable arg3) {
		dialog.dismiss();
	}
});
	
}
	@Override
	public void getData() {
		// TODO Auto-generated method stub
	}
	private void summit(){
		Intent broadcast = new Intent(ACTION);
		LocalBroadcastManager.getInstance(this).sendBroadcast(broadcast);
	}
}
