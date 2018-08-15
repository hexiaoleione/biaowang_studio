package com.hex.express.iwant.activities;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageButton;
import butterknife.Bind;
import butterknife.ButterKnife;

import com.hex.express.iwant.R;
import com.hex.express.iwant.iWantApplication;
import com.hex.express.iwant.fragments.OrderMycourierFragment;
import com.hex.express.iwant.fragments.ReceiptMycourierFragment;
import com.hex.express.iwant.utils.ToastUtil;
import com.hex.express.iwant.views.LoadingProgressDialog;
import com.hex.express.iwant.views.MyViewpager;
import com.hex.express.iwant.views.TitleBarView;

/**
 * 我的快递界面
 * 
 * @author SCHT-50
 * 
 */
public class MyCourierActivity extends BaseActivity {
	@Bind(R.id.publice)
	ImageButton btn_publice;
	@Bind(R.id.order)
	ImageButton btn_order;
	@Bind(R.id.viewpager)
	MyViewpager viewPager;
	private List<Fragment> list;
	LoadingProgressDialog dialog;
	@Bind(R.id.tbv_show)
	TitleBarView tvb;
	boolean flag=false;

	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_mycourier);
		iWantApplication.getInstance().addActivity(this);
		ButterKnife.bind(this);
		initView();
		initData();
		setOnClick();
	}

	@Override
	public void onWeightClick(View v) {
		switch (v.getId()) {
		case R.id.publice:
			if (flag) {
				btn_publice.setBackgroundResource(R.drawable.orengerfabu);
				btn_order.setBackgroundResource(R.drawable.blackrshoujian);
			}
			flag=false;
			viewPager.setCurrentItem(0);
			break;
		case R.id.order:
			if (!flag) {
				btn_order.setBackgroundResource(R.drawable.ordershoujian);
				btn_publice.setBackgroundResource(R.drawable.fabu);
			}
			flag=true;  
			viewPager.setCurrentItem(1);
			break;
		}
	}

	@Override
	public void initView() {
		tvb.setTitleText("我的快递");

	}

	@Override
	public void initData() {
		 dialog = new LoadingProgressDialog(MyCourierActivity.this);
		list = new ArrayList<Fragment>();
		ReceiptMycourierFragment fabulatFragment = new ReceiptMycourierFragment();//【我的快递】→【我的发布】
		OrderMycourierFragment fragment = new OrderMycourierFragment();//【我的快递】→【我的收件】
		list.add(fabulatFragment);
		list.add(fragment);
		myAdapter mAdapter = new myAdapter(getSupportFragmentManager());
		viewPager.setAdapter(mAdapter);
		
	}
	
	@Override
	public void setOnClick() {
		// TODO Auto-generated method stub
		btn_order.setOnClickListener(this);
		btn_publice.setOnClickListener(this);

	}
	public void show() {
		dialog.show();
	}
	public void dissmiss() {
		dialog.dismiss();
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
