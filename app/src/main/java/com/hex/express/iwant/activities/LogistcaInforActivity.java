package com.hex.express.iwant.activities;

import java.util.ArrayList;

import com.hex.express.iwant.R;
import com.hex.express.iwant.iWantApplication;
import com.hex.express.iwant.activities.MyDownwindActivity.myAdapter;
import com.hex.express.iwant.bean.CityBean;
import com.hex.express.iwant.fragments.NearbyFragment;
import com.hex.express.iwant.fragments.RangedFragment;
import com.hex.express.iwant.fragments.RegionFragment;
import com.hex.express.iwant.fragments.SendEscortFragment;
import com.hex.express.iwant.fragments.SendOwnerFragment;
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
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.Bind;
import butterknife.ButterKnife;
/**
 * 
 * 
 * @author huyichuan
 * 物流货源附近，地区, 长途
 */
public class LogistcaInforActivity extends BaseActivity{
//	@Bind(R.id.infor_show)
	ImageView btnLeft,btnRight;
//	@Bind(R.id.changtu)
	Button changtu;
//	@Bind(R.id.fujin)
	Button fujin;
//	@Bind(R.id.diqu)
	Button diqu;
	@Bind(R.id.infor_viewpager)
	MyViewpager infor_viewpager;
	boolean first = false;
	private ArrayList<Fragment> list;
	String string ;
	
	String loadIndex = "user";//加载Fragment的索引值；
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		iWantApplication.getInstance().addActivity(this);
		setContentView(R.layout.activity_information);
		ButterKnife.bind(LogistcaInforActivity.this);
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
		Intent intent = getIntent();//根据索引值来加载
//		string = intent.getStringExtra("cityCode");
//		System.out.println("111111111    "+string);
		// TODO Auto-generated method stub
		btnRight=(ImageView) findViewById(R.id.btnRight);
		btnLeft=(ImageView) findViewById(R.id.btnLeft);
		changtu=(Button) findViewById(R.id.changtu);
		fujin=(Button) findViewById(R.id.fujin);
		diqu=(Button) findViewById(R.id.diqu);
	}

	@Override
	public void initData() {
		dialog = new LoadingProgressDialog(LogistcaInforActivity.this);
		list = new ArrayList<Fragment>();
		NearbyFragment nearbyFragment = new NearbyFragment();//附近Fragment
		RangedFragment rangeFragment = new RangedFragment();//长途Fragment
		RegionFragment regionFragment = new  RegionFragment();//地区Fragment
		list.add(nearbyFragment);
		list.add(rangeFragment);
		list.add(regionFragment);
		myAdapter mAdapter = new myAdapter(getSupportFragmentManager());
		infor_viewpager.setAdapter(mAdapter);
		
		
		
	}

	@Override
	public void setOnClick() {
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
				Intent intent=new Intent();
				intent.setClass(LogistcaInforActivity.this, LoistattentionActivity.class);//公司
				startActivity(intent);
			}
		});
		// TODO Auto-generated method stub
		changtu.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				infor_viewpager.setCurrentItem(0);
				changtu.setTextColor(getResources().getColor(R.color.orange1));
				fujin.setTextColor(getResources().getColor(R.color.black));
				diqu.setTextColor(getResources().getColor(R.color.black));
				
				
			}
		});
		fujin.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				infor_viewpager.setCurrentItem(1);
				changtu.setTextColor(getResources().getColor(R.color.black));
				fujin.setTextColor(getResources().getColor(R.color.orange1));
				diqu.setTextColor(getResources().getColor(R.color.black));
				
			}
		});
		diqu.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					infor_viewpager.setCurrentItem(2);
					changtu.setTextColor(getResources().getColor(R.color.black));
					fujin.setTextColor(getResources().getColor(R.color.black));
					diqu.setTextColor(getResources().getColor(R.color.orange1));
					
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
