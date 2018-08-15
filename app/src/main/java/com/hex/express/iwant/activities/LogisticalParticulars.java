package com.hex.express.iwant.activities;

import com.hex.express.iwant.R;

import android.os.Bundle;
import android.view.View;
import butterknife.ButterKnife;
/**
 * 
 * @author huyichuan
 * 个人物流详情
 *
 */
public class LogisticalParticulars extends BaseActivity{
	private int num;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.myquote_item);
		ButterKnife.bind(LogisticalParticulars.this);
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
