package com.hex.express.iwant.activities;

import com.hex.express.iwant.R;
import com.hex.express.iwant.iWantApplication;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import butterknife.Bind;
import butterknife.ButterKnife;

public class AttestationActivity extends BaseActivity{

	@Bind(R.id.cerid_person)
	Button cerid_person;//个人
	@Bind(R.id.cerid_company)
	Button cerid_company;//公司
	@Bind(R.id.pesro_lin)
	LinearLayout pesro_lin;//个人的显示界面
	@Bind(R.id.compan_li)
	LinearLayout compan_li;//公司的显示界面
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		iWantApplication.getInstance().addActivity(this);
		setContentView(R.layout.activity_attestation);
		ButterKnife.bind(AttestationActivity.this);
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
		cerid_person.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				pesro_lin.setVisibility(View.VISIBLE);
				
			}
		});
		cerid_company.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				pesro_lin.setVisibility(View.GONE);
				
			}
		});
	}

	@Override
	public void initData() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setOnClick() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void getData() {
		// TODO Auto-generated method stub
		
	}

}
