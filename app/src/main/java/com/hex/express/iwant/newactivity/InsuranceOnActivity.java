package com.hex.express.iwant.newactivity;

import com.hex.express.iwant.R;
import com.hex.express.iwant.iWantApplication;
import com.hex.express.iwant.activities.BaseActivity;
import com.hex.express.iwant.activities.HAdvertActivity;

import android.content.ComponentName;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import butterknife.Bind;
import butterknife.ButterKnife;

public class InsuranceOnActivity extends BaseActivity{
	@Bind(R.id.btnLeft)
	ImageView btnleft;//返回键
	@Bind(R.id.btnRight)
	TextView btnRight;
	@Bind(R.id.xiaer)
	Button xiaer;// 
	@Bind(R.id.xiayi)
	Button xiayi;// 
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.loginsuranceon_activity);//loginsurance_activity
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
			intent.setClass(InsuranceOnActivity.this, HAdvertActivity.class);//公司
			startActivity(intent);
	}
});
xiayi.setOnClickListener(new OnClickListener() {
	
	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
//		if (!"".equals( bean.data.get(position).pdfURL)) {
			
			 Intent intent1 = new Intent();
			intent1.setAction(Intent.ACTION_VIEW);
			intent1.setData(Uri.parse("http://www.efamax.com/mobile/images/lipeishenqingshu.pdf"));
			// 注意此处的判断intent.resolveActivity()可以返回显示该Intent的Activity对应的组件名
			// 官方解释 : Name of the component implementing an activity that can display the intent
			if (intent1.resolveActivity(InsuranceOnActivity.this.getPackageManager()) != null) {    
			   ComponentName componentName = intent1.resolveActivity(InsuranceOnActivity.this.getPackageManager()); 
			  // 打印Log   ComponentName到底是什么
//			  Log.e("1111", "componentName = " + bean.data.get(0).advertiseHtmlUrl);
			  startActivity(Intent.createChooser(intent1, "请选择浏览器"));
			} else {    
			  Toast.makeText(InsuranceOnActivity.this.getApplicationContext(), "没有匹配的程序", Toast.LENGTH_SHORT).show();
			}
//		}else {
//			  Toast.makeText(InsuranceOnActivity.this.getApplicationContext(), "暂无下载地址", Toast.LENGTH_SHORT).show();
//		}
		
	}
});
xiaer.setOnClickListener(new OnClickListener() {
	
	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		 Intent intent1 = new Intent();
			intent1.setAction(Intent.ACTION_VIEW);
			intent1.setData(Uri.parse("http://www.efamax.com/mobile/images/lipeixuzhi.pdf"));
			// 注意此处的判断intent.resolveActivity()可以返回显示该Intent的Activity对应的组件名
			// 官方解释 : Name of the component implementing an activity that can display the intent
			if (intent1.resolveActivity(InsuranceOnActivity.this.getPackageManager()) != null) {    
			   ComponentName componentName = intent1.resolveActivity(InsuranceOnActivity.this.getPackageManager()); 
			  // 打印Log   ComponentName到底是什么
//			  Log.e("1111", "componentName = " + bean.data.get(0).advertiseHtmlUrl);
			  startActivity(Intent.createChooser(intent1, "请选择浏览器"));
			} else {    
			  Toast.makeText(InsuranceOnActivity.this.getApplicationContext(), "没有匹配的程序", Toast.LENGTH_SHORT).show();
			}
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
