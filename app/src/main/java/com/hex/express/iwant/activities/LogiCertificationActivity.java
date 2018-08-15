package com.hex.express.iwant.activities;

import java.util.ArrayList;

import com.hex.express.iwant.R;
import com.hex.express.iwant.iWantApplication;
import com.hex.express.iwant.activities.LogistcaInforActivity.myAdapter;
import com.hex.express.iwant.fragments.CompanyFragment;
import com.hex.express.iwant.fragments.PersonFragment;
import com.hex.express.iwant.fragments.RangedFragment;
import com.hex.express.iwant.fragments.RegionFragment;
import com.hex.express.iwant.views.LoadingProgressDialog;
import com.hex.express.iwant.views.MyViewpager;
import com.hex.express.iwant.views.TitleBarView;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
/**
 * 
 * @author huyichuan
 *  物流认证界面
 */
public class LogiCertificationActivity extends BaseActivity{
//	@Bind(R.id.btnLefts)
private	Button btnLefts;
//	@Bind(R.id.cerid_person)//个人
	Button cerid_person;
//	@Bind(R.id.cerid_company)//公司
	Button cerid_company;
//	@Bind(R.id.cerid_viewpager)
	MyViewpager cerid_viewpager;
	boolean first = false;
	private ArrayList<Fragment> list;
	
	String loadIndex = "user";//加载Fragment的索引值；
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		iWantApplication.getInstance().addActivity(this);
		setContentView(R.layout.activity_ceridication);
//		ButterKnife.bind(LogiCertificationActivity.this);
		initView();
		initData();
		setOnClick();
	}
	public void show() {
		dialog.show();
	}

	public void dissmiss() {
		dialog.dismiss();
	}
	@Override
	public void onWeightClick(View v) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void initView() {
		// TODO Auto-generated method stub
		btnLefts=(Button)findViewById(R.id.btnLefts);
		cerid_person=(Button)findViewById(R.id.cerid_person);
		cerid_company=(Button)findViewById(R.id.cerid_company);
		cerid_viewpager=(MyViewpager)findViewById(R.id.cerid_viewpager);

		
		
	}
	
	@Override
	public void initData() {
		dialog = new LoadingProgressDialog(LogiCertificationActivity.this);
		list = new ArrayList<Fragment>();
		PersonFragment person = new PersonFragment();//个人
		CompanyFragment company = new CompanyFragment();//公司
		list.add(person);
		list.add(company);
		myAdapter mAdapter = new myAdapter(getSupportFragmentManager());
		cerid_viewpager.setAdapter(mAdapter);
		
		Intent intent = getIntent();//根据索引值来加载
		loadIndex = intent.getStringExtra("loadIndex");
		
//		if ("Escort".equals(loadIndex)) {
////			owner.setTextColor(getResources().getColor(R.color.black));
////			escort.setTextColor(getResources().getColor(R.color.orange1));
//			cerid_viewpager.setCurrentItem(1);
//		}else if("Owner".equals(loadIndex)) {
//			owner.setTextColor(getResources().getColor(R.color.orange1));
//			escort.setTextColor(getResources().getColor(R.color.black));
//			cerid_viewpager.setCurrentItem(0);
//		}
	}

	@Override
	public void setOnClick() {
		// TODO Auto-generated method stub
		cerid_person.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if (first) {
					cerid_person.setTextColor(getResources().getColor(R.color.orange1));
					cerid_person.setTextColor(getResources().getColor(R.color.black));
				}
				first = false;
				cerid_viewpager.setCurrentItem(0);
			}
		});
		cerid_company.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if (!first){
					cerid_company.setTextColor(getResources().getColor(R.color.black));
					cerid_company.setTextColor(getResources().getColor(R.color.orange1));
				}
				first = true;
				cerid_viewpager.setCurrentItem(1);
			}
		});
		btnLefts.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				finish();

			}
		});
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
