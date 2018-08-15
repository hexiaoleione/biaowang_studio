package com.hex.express.iwant.activities;

import com.hex.express.iwant.R;
import com.hex.express.iwant.constance.PreferenceConstants;
import com.hex.express.iwant.utils.PreferencesUtils;
import com.hex.express.iwant.views.MarqueeTextView;
import com.hex.express.iwant.views.TitleBarView;

import android.content.ComponentName;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import butterknife.Bind;
import butterknife.ButterKnife;

public class HainanLogistioffActivity extends BaseActivity{
	
	@Bind(R.id.huoname)
	TextView huoname;
	@Bind(R.id.deta_fahuodizhi)
	MarqueeTextView deta_fahuodizhi;
	@Bind(R.id.deta_dadaodizhi)
	MarqueeTextView deta_dadaodizhi;
	@Bind(R.id.huowulei)
	TextView huowulei;
	@Bind(R.id.baoxianleibie)
	TextView baoxianleibie;
	@Bind(R.id.deta_jiazhi)
	TextView deta_jiazhi;
	@Bind(R.id.deta_zhong)
	TextView deta_zhong;
	@Bind(R.id.deta_baodan)
	TextView deta_baodan;
	
	
	@Bind(R.id.deta_tiji)
	TextView deta_tiji;
	@Bind(R.id.deta_shouhuoname)
	TextView deta_shouhuoname;
	@Bind(R.id.deta_lianxifangshi)
	TextView deta_lianxifangshi;
	@Bind(R.id.deta_baofei)
	TextView deta_baofei;
	
	@Bind(R.id.deta_fahuoname)
	TextView deta_fahuoname;
	@Bind(R.id.deta_falianxifangshi)
	TextView deta_falianxifangshi;
	@Bind(R.id.deta_carnumber)
	TextView deta_carnumber;
	@Bind(R.id.btnLeft)//返回
	ImageView btnLeft;
	@Bind(R.id.btnRight)//返回
	TextView btnRight;
	@Bind(R.id.btn_pi)//pihai
	Button btn_pi;
	
//	@Bind(R.id.mylis_show)
//	TitleBarView mylis_show;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.hainanlogistioff);
		ButterKnife.bind(HainanLogistioffActivity.this);
		initView();
		initData();
		setOnClick();
	}


	@Override
	public void initView() {
		// TODO Auto-generated method stub
//		mylis_show.setTitleText("物流详情");
		huoname.setText("货物名称："+getIntent().getStringExtra("cargoName"));
		deta_fahuodizhi.setText(""+getIntent().getStringExtra("startPlace"));
		deta_dadaodizhi.setText(""+getIntent().getStringExtra("entPlace"));
		huowulei.setText("货物类别："+getIntent().getStringExtra("category"));
		baoxianleibie.setText("保险类别："+getIntent().getStringExtra("insurance"));
		
		if (PreferencesUtils.getString(HainanLogistioffActivity.this, PreferenceConstants.AgreementType).equals("2")) {
			deta_baofei.setVisibility(View.GONE);
		}else {
			deta_baofei.setVisibility(View.GONE);
			deta_baofei.setText("货物保费："+getIntent().getDoubleExtra("insureCost", 0)+" 元");
		}
		deta_jiazhi.setText("货物价值(保额)："+getIntent().getStringExtra("cargoCost")+" 元");
		if ("吨".equals(getIntent().getStringExtra("cargoWeight")) || "千克".equals(getIntent().getStringExtra("cargoWeight"))) {
			deta_zhong.setVisibility(View.GONE);
		}else {
			deta_zhong.setText("货物重量："+getIntent().getStringExtra("cargoWeight"));
		}
		if ("".equals(getIntent().getStringExtra("cargoVolume"))) {
			deta_tiji.setVisibility(View.INVISIBLE);
		}else {
			deta_tiji.setText("货物件数："+getIntent().getStringExtra("cargoVolume")+"件");
		}
		
		deta_shouhuoname.setText("收货人姓名："+getIntent().getStringExtra("takeName"));
		deta_lianxifangshi.setText(""+getIntent().getStringExtra("takeMobile"));
		deta_fahuoname.setText("发货人姓名："+getIntent().getStringExtra("sendName"));
		deta_falianxifangshi.setText(""+getIntent().getStringExtra("sendMobile"));
		deta_carnumber.setText("车牌号："+getIntent().getStringExtra("carNumImg"));
		if (!"".equals(getIntent().getStringExtra("remark"))) {
			deta_baodan.setText("保单号："+getIntent().getStringExtra("remark"));
		}else {
			deta_baodan.setVisibility(View.GONE);
		}
		
		btn_pi.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intent=new Intent();
				intent.putExtra("cargoName", getIntent().getStringExtra("cargoName"));
				intent.putExtra("recId",  getIntent().getIntExtra("recId", 0));
				intent.putExtra("resendNamecId",  getIntent().getStringExtra("sendName"));
				intent.putExtra("sendMobile",  getIntent().getStringExtra("sendMobile"));
				
				intent.putExtra("cargoWeight",  getIntent().getStringExtra("cargoWeight"));
				intent.putExtra("cargoVolume",  getIntent().getStringExtra("cargoVolume"));
				intent.putExtra("entPlace",  getIntent().getStringExtra("entPlace"));
				intent.putExtra("startPlace",  getIntent().getStringExtra("startPlace"));
				intent.putExtra("takeName",  getIntent().getStringExtra("takeName"));
				intent.putExtra("takeMobile",  getIntent().getStringExtra("takeMobile"));
				intent.putExtra("category",  getIntent().getStringExtra("category"));
				intent.putExtra("insurance", getIntent().getStringExtra("insurance"));
				intent.putExtra("cargoCost", getIntent().getStringExtra("cargoCost"));
				intent.putExtra("insureCost",  getIntent().getStringExtra("insureCost"));
				intent.putExtra("sendName",  getIntent().getStringExtra("sendName"));
				intent.putExtra("sendMobile",  getIntent().getStringExtra("sendMobile"));
				intent.putExtra("carNumImg",  getIntent().getStringExtra("carNumImg"));
				intent.putExtra("pdfURL",  getIntent().getStringExtra("pdfURL"));
				intent.putExtra("remark", getIntent().getStringExtra("remark"));
				intent.setClass(HainanLogistioffActivity.this, HnLogActivity.class);//
				startActivity(intent);
				finish();
			}
		});
	}

	@Override
	public void onWeightClick(View v) {
		// TODO Auto-generated method stub
		
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
				finish();
			}
		});
		btnRight.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if (!"".equals(getIntent().getStringExtra("pdfURL"))) {
				
				 Intent intent = new Intent();
				intent.setAction(Intent.ACTION_VIEW);
				intent.setData(Uri.parse(getIntent().getStringExtra("pdfURL")));
				// 注意此处的判断intent.resolveActivity()可以返回显示该Intent的Activity对应的组件名
				// 官方解释 : Name of the component implementing an activity that can display the intent
				if (intent.resolveActivity(getPackageManager()) != null) {    
				   ComponentName componentName = intent.resolveActivity(getPackageManager()); 
				  // 打印Log   ComponentName到底是什么
//				  Log.e("1111", "componentName = " + bean.data.get(0).advertiseHtmlUrl);
				  startActivity(Intent.createChooser(intent, "请选择浏览器"));
				} else {    
				  Toast.makeText(getApplicationContext(), "没有匹配的程序", Toast.LENGTH_SHORT).show();
				}
			}else {
				  Toast.makeText(getApplicationContext(), "暂无下载地址", Toast.LENGTH_SHORT).show();

			}	
			}
		});
	}

	@Override
	public void getData() {
		// TODO Auto-generated method stub
		
	}

}
