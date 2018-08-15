package com.hex.express.iwant.newactivity;

import com.hex.express.iwant.R;
import com.hex.express.iwant.iWantApplication;
import com.hex.express.iwant.activities.BaseActivity;
import com.hex.express.iwant.activities.HAdvertActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.Bind;
import butterknife.ButterKnife;

public class InsuranceOffActivity extends BaseActivity{

	@Bind(R.id.btnLeft)
	ImageView btnleft;//返回键
	@Bind(R.id.btnRight)
	TextView btnRight;
//	@Bind(R.id.btntuinsu)
//	Button btntuinsu;// 
//	@Bind(R.id.btntuinsure)
//	Button btntuinsure;// 
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.loginsuranceoff_activity);//loginsurance_activity
		iWantApplication.getInstance().addActivity(this);
		 ButterKnife.bind(this);
		 initView();
		 initData();
		 setOnClick();
	}
	@Override
	public void onWeightClick(View v) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void initView() {
		// TODO Auto-generated method stub
btnleft.setOnClickListener(new OnClickListener() {
			
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
		
		   String url="http://www.efamax.com/mobile/explain/driverSafeExplain.html";
		    Intent intent=new Intent();
			intent.putExtra("url", url);
			intent.setClass(InsuranceOffActivity.this, HAdvertActivity.class);//公司
			startActivity(intent);
	}
});
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
