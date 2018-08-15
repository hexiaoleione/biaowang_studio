package com.hex.express.iwant.activities;
/**
 * 我的物流界面
 */
import java.util.ArrayList;
import java.util.List;

import com.hex.express.iwant.R;
import com.hex.express.iwant.iWantApplication;
import com.hex.express.iwant.fragments.ExpressFragment;
import com.hex.express.iwant.fragments.OrderMytransportFragment;
import com.hex.express.iwant.fragments.ReceiptMycourierFragment;
import com.hex.express.iwant.fragments.FreightFragment;
import com.hex.express.iwant.fragments.ReceiptMytransportFragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

public class MyTransportActivity extends BaseActivity {
	private ImageView btn_publice;
	private ImageView btn_order;
	private ViewPager viewPager;
	private List<Fragment> list;

	protected void onCreate(Bundle savedInstanceState) {
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
		btn_order=(ImageView) findViewById(R.id.order);
		btn_publice=(ImageView) findViewById(R.id.publice);
		viewPager=(ViewPager) findViewById(R.id.viewpager);

	}

	@Override
	public void initData() {
		list = new ArrayList<Fragment>();
		ReceiptMytransportFragment reFragment=new ReceiptMytransportFragment();
		OrderMytransportFragment OMFragment=new OrderMytransportFragment();
		list.add(reFragment);
		list.add(OMFragment);
		viewPager.setCurrentItem(0);
		myAdapter mAdapter=new myAdapter(getSupportFragmentManager());
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
