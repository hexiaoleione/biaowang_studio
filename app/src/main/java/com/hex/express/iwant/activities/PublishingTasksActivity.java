package com.hex.express.iwant.activities;

import com.hex.express.iwant.iWantApplication;

import android.os.Bundle;
import android.view.View;
/**
 * 发布任务界面
 * @author Eric
 *
 */
public class PublishingTasksActivity extends BaseActivity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		iWantApplication.getInstance().addActivity(this);
	}

	@Override
	public void onWeightClick(View v) {

	}

	@Override
	public void initView() {

	}

	@Override
	public void initData() {

	}

	@Override
	public void setOnClick() {

	}

	@Override
	public void getData() {
		// TODO Auto-generated method stub
		
	}
}
