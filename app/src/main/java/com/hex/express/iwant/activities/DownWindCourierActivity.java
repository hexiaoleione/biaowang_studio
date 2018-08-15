package com.hex.express.iwant.activities;

import com.hex.express.iwant.R;
import com.hex.express.iwant.iWantApplication;
import com.hex.express.iwant.utils.ToastUtil;
import com.hex.express.iwant.views.TitleBarView;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
/**
 * @author Eric
 *
 */
public class DownWindCourierActivity extends BaseActivity {
	
	private TitleBarView tbv_show;
	private EditText edt_downwindrecaddressinput;
	private EditText edt_downwindsendaddressinput;
	private EditText edt_downwindmathweightinput;
	private Button btn_next;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_downwindcourier);
		iWantApplication.getInstance().addActivity(this);
		getData();
		initView();
		initData();
		setOnClick();
	}

	@Override
	public void onWeightClick(View v) {
		final Intent intent = new Intent();
		switch (v.getId()) {
		case R.id.btn_next:
			String sendAddress = edt_downwindsendaddressinput.getText().toString().trim();
			String recAddress = edt_downwindrecaddressinput.getText().toString().trim();
			String mathWeight = edt_downwindmathweightinput.getText().toString().trim();
			if (sendAddress == null || TextUtils.isEmpty(sendAddress) || sendAddress.length() == 0) {
				ToastUtil.shortToastByRec(getApplicationContext(), R.string.sendaddressisnotnull);
			} else if (recAddress == null || TextUtils.isEmpty(recAddress) || recAddress.length() == 0) {
				ToastUtil.shortToastByRec(getApplicationContext(), R.string.recaddressisnotnull);
			} else if (mathWeight == null || TextUtils.isEmpty(mathWeight) || mathWeight.length() == 0) {
				ToastUtil.shortToastByRec(getApplicationContext(), R.string.mathweightisnotnull);
			} else {
				intent.setClass(DownWindCourierActivity.this, SubmitOrderActivity.class);
				startActivity(intent);
			}
			break;
		}
	}

	@Override
	public void initView() {
		//标题
		tbv_show = (TitleBarView) findViewById(R.id.tbv_show);
		this.tbv_show.setTitleText(R.string.downwindtitle);
		//寄件人地址  收件人地址  重量 下一步
		edt_downwindsendaddressinput = (EditText) findViewById(R.id.edt_downwindsendaddressinput);
		edt_downwindrecaddressinput = (EditText) findViewById(R.id.edt_downwindrecaddressinput);
		edt_downwindmathweightinput = (EditText) findViewById(R.id.edt_downwindmathweightinput);
		btn_next = (Button) findViewById(R.id.btn_next);
	}

	@Override
	public void initData() {

	}

	@Override
	public void setOnClick() {
		btn_next.setOnClickListener(this);
	}

	@Override
	public void getData() {
		
	}
}
