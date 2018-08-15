package com.hex.express.iwant.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.Bind;
import butterknife.ButterKnife;

import com.hex.express.iwant.R;
import com.hex.express.iwant.iWantApplication;
/**
 * 信息详情界面
 * @author SCHT-50
 *
 */
public class MessageDetails extends BaseActivity {
	@Bind(R.id.messageDesc)
	TextView text;
	@Bind(R.id.closes)
	ImageView close;
	@Bind(R.id.text)
	TextView tv_title;
	@Bind(R.id.endtimes)
	TextView endtimes;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		iWantApplication.getInstance().addActivity(this);
		setContentView(R.layout.activity_messagedetails);
		ButterKnife.bind(this);
		initView();
		initData();
	}

	@Override
	public void onWeightClick(View v) {
		// TODO Auto-generated method stub

	}

	@SuppressWarnings({ "unused", "unused" })
	@Override
	public void initView() {

	}

	@Override
	public void initData() {
		Intent intent = getIntent();
		String messageUrl = intent.getStringExtra("messageUrl");
		String messageDesc = intent.getStringExtra("messageDesc");
		String title=intent.getStringExtra("title");
		String endtimeString=intent.getStringExtra("sendTime");
		Log.e("111111",endtimeString);
		endtimes.setText(endtimeString);
		text.setText(messageDesc);
		tv_title.setText(title);
		close.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				finish();
				
			}
		});

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
