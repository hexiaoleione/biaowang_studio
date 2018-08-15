package com.hex.express.iwant.newactivity;

import java.util.ArrayList;
import java.util.List;

import com.hex.express.iwant.R;
import com.hex.express.iwant.iWantApplication;
import com.hex.express.iwant.activities.BaseActivity;
import com.hex.express.iwant.views.LoadingProgressDialog;
import com.hex.express.iwant.views.MyViewpager;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 【我的保险】界面
 * @author Eric
 * 
 */
public class NewDepositActivity extends BaseActivity {
	@Bind(R.id.text_title)
	TextView text_title;
	@Bind(R.id.owner)
	ImageView owner;
	@Bind(R.id.escort)
	ImageView escort;
	@Bind(R.id.viewpager)
	MyViewpager viewPager;
	boolean first = false;
	private ArrayList<Fragment> list;
	
	String loadIndex = "user";//加载Fragment的索引值；

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		iWantApplication.getInstance().addActivity(this);
		setContentView(R.layout.activity_newde);
		ButterKnife.bind(NewDepositActivity.this);
		initData();
////		if(DownEscoreDartActivity.instance.ex){}
////		DownEscoreDartActivity.instance.finish();
//		Intent intent = new Intent();
////		  intent.setClassName(com.hex.express.iwant.activities.this, DownEscoreDartActivity.class);
//			  intent.setClassName(getApplicationContext(), "DownEscoreDartActivity");
//			  List list = getPackageManager().queryIntentActivities(intent, 0);
//			  if (list.size() != 0) {   
//			       // 说明系统中存在这个activity
//				  DownEscoreDartActivity.instance.finish();
//			  }
	}

	public void show() {
		dialog.show();
	}

	public void dissmiss() {
		dialog.dismiss();
	}

	@OnClick({ R.id.escort, R.id.owner, R.id.btnLeft })
	public void MyOnclick(View view) {
		switch (view.getId()) {
		case R.id.owner:
			if (first) {
				owner.setBackgroundResource(R.drawable.ktb);
				escort.setBackgroundResource(R.drawable.wbdgang);
			}
			first = false;
			viewPager.setCurrentItem(0);
			break;
		case R.id.escort:
			if (!first){
				owner.setBackgroundResource(R.drawable.ktbgang);
				escort.setBackgroundResource(R.drawable.wbd);
			}
			first = true;
			viewPager.setCurrentItem(1);
			break;
		case R.id.btnLeft:
			finish();
			break;
		default:
			
			break;
		}
	}

	@Override
	public void onWeightClick(View v) {

	}

	@Override
	public void initView() {

	}

	@Override
	public void initData() {
		text_title.setText("投保详情");
		dialog = new LoadingProgressDialog(NewDepositActivity.this);
		list = new ArrayList<Fragment>();
		SendDepositFragment ownerFragment = new SendDepositFragment();//我发Fragment
		EscortDepositFragment escortFragment = new EscortDepositFragment();//我的Fragment
		list.add(ownerFragment);
		list.add(escortFragment);
		myAdapter mAdapter = new myAdapter(getSupportFragmentManager());
		viewPager.setAdapter(mAdapter);
		
//		Intent intent = getIntent();//根据索引值来加载
//		loadIndex = intent.getStringExtra("loadIndex");
//		
//		if ("Escort".equals(loadIndex)) {
//			owner.setTextColor(getResources().getColor(R.color.black));
//			escort.setTextColor(getResources().getColor(R.color.orange1));
//			viewPager.setCurrentItem(1);
//		}else if("Owner".equals(loadIndex)) {
//			owner.setTextColor(getResources().getColor(R.color.orange1));
//			escort.setTextColor(getResources().getColor(R.color.black));
//			viewPager.setCurrentItem(0);
//		}
	}

	@Override
	public void setOnClick() {

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
