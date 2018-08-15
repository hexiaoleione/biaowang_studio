package com.hex.express.iwant.activities;

import com.hex.express.iwant.R;
import com.hex.express.iwant.views.TitleBarView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RelativeLayout;
import butterknife.Bind;
import butterknife.BindBool;
import butterknife.ButterKnife;

/**
 * 
 * @author huyichuan
 *  我的关注
 *   activity_attention
 */
public class LoistattentionActivity extends BaseActivity{
	@Bind(R.id.mylis_show)
	TitleBarView mylis_show;
	@Bind(R.id.attfujin)//	附近货源
	RelativeLayout attfujin;
	@Bind(R.id.attdingdian)//定点货源
	RelativeLayout attdingdian;
	@Bind(R.id.attdiqu) //地区货源
	RelativeLayout attdiqu;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_attention);
		ButterKnife.bind(this);
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
		mylis_show.setTitleText("关注信息");
		// TODO Auto-generated method stub
		
	}

	@Override
	public void initData() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setOnClick() {
		// TODO Auto-generated method stub
//		附近货源
		attfujin.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intent=new Intent();
				intent.putExtra("adds", "1");
				intent.setClass(LoistattentionActivity.this, LoistattentionMyActivity.class);
				startActivity(intent);
			}
		});
		//定点货源
		attdingdian.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intent=new Intent();
//				intent.putExtra("longitude", longitude);
				intent.putExtra("adds", "2");
				intent.setClass(LoistattentionActivity.this, LoistattentionMyActivity.class);
				startActivity(intent);
			}
		});
			//地区货源
			attdiqu.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					Intent intent=new Intent();
//					intent.putExtra("longitude", longitude);
					intent.putExtra("adds", "3");
					intent.setClass(LoistattentionActivity.this, LoistattentionMyActivity.class);
					startActivity(intent);
				}
			});
	}

	@Override
	public void getData() {
		// TODO Auto-generated method stub
		
	}

}
