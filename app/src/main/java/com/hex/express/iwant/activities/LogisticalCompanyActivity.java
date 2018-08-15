package com.hex.express.iwant.activities;

import com.hex.express.iwant.R;

import android.os.Bundle;
import android.view.View;
import butterknife.ButterKnife;
/**
 * 
 * @author huyichuan
 * 物流公司详情
 */
public class LogisticalCompanyActivity extends BaseActivity{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_down_mylogistical);
		ButterKnife.bind(LogisticalCompanyActivity.this);
		initView();
		initData();
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
