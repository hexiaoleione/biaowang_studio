package com.hex.express.iwant.activities;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import com.hex.express.iwant.R;
import com.hex.express.iwant.iWantApplication;
import com.hex.express.iwant.fragments.OrderMymissionFragment;
import com.hex.express.iwant.fragments.ReceiptMymissionFragment;

/**
 * 我的任务界面
 * 
 * @author SCHT-50
 * 
 */
public class MyMissionActivity extends BaseActivity {
	private ImageButton btn_publice;
	private ImageButton btn_order;
	private ViewPager viewPager;
	private List<Fragment> list;

	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_mycourier);
		iWantApplication.getInstance().addActivity(this);
		initView();
		initData();
		setOnClick();
	}

	@Override
	public void onWeightClick(View v) {
		switch (v.getId()) {
		case R.id.publice:
			viewPager.setCurrentItem(0);
			break;
		case R.id.order:
			viewPager.setCurrentItem(1);
			break;
		}
	}

	@Override
	public void initView() {
		btn_order = (ImageButton) findViewById(R.id.order);
		btn_publice = (ImageButton) findViewById(R.id.publice);
		viewPager = (ViewPager) findViewById(R.id.viewpager);

	}

	@Override
	public void initData() {
		list = new ArrayList<Fragment>();
		ReceiptMymissionFragment rFragment = new ReceiptMymissionFragment();
		OrderMymissionFragment oFragment = new OrderMymissionFragment();
		list.add(rFragment);
		list.add(oFragment);
		viewPager.setCurrentItem(0);
		myAdapter mAdapter = new myAdapter(getSupportFragmentManager());
		viewPager.setAdapter(mAdapter);

	}

	@Override
	public void setOnClick() {
		// TODO Auto-generated method stub
		btn_order.setOnClickListener(this);
		btn_publice.setOnClickListener(this);

	}

	@Override
	public void getData() {
		// TODO Auto-generated method stub

	}

	class myAdapter extends FragmentPagerAdapter {

		public myAdapter(android.support.v4.app.FragmentManager fm) {
			super(fm);
			// TODO Auto-generated constructor stub
		}

		@Override
		public Fragment getItem(int arg0) {
			// TODO Auto-generated method stub
			return list.get(arg0);
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return list.size();
		}
	}

}
