package com.hex.express.iwant.activities;

import butterknife.Bind;
import butterknife.ButterKnife;

import com.hex.express.iwant.R;
import com.hex.express.iwant.iWantApplication;
import com.hex.express.iwant.views.TitleBarView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

/**
 * 发单物品详情
 * 
 * @author SCHT-50
 * 
 */
public class PublicDetailShowActivity extends BaseActivity {
	@Bind(R.id.tv_name)
	TextView tv_name;
	@Bind(R.id.tv_phone)
	TextView tv_phone;
	@Bind(R.id.tv_address)
	TextView tv_address;
	@Bind(R.id.tv_re_name)
	TextView tv_re_name;
	@Bind(R.id.tv_re_phone)
	TextView tv_re_phone;
	@Bind(R.id.tv_re_address)
	TextView tv_re_address;
	@Bind(R.id.et_name)
	TextView et_name;
	@Bind(R.id.et_weight)
	TextView et_weight;
	@Bind(R.id.et_freight)
	TextView et_freight;
	@Bind(R.id.et_value)
	TextView et_value;
	@Bind(R.id.et_keep)
	TextView et_keep;
	@Bind(R.id.et_total)
	TextView et_total;
	@Bind(R.id.et_number)
	TextView et_number;
	@Bind(R.id.btn_wuliu)
	Button btn_wuliu;
	@Bind(R.id.tbv_show)
	TitleBarView tbv_show;
	@Bind(R.id.ll_rece)
	LinearLayout ll_rece;
	@Bind(R.id.rb_warp)
	RadioButton rb_warp;
	@Bind(R.id.rb_file)
	RadioButton rb_file;
	private String expName;
	private String expNo;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_public_evaluate);
		iWantApplication.getInstance().addActivity(this);
		ButterKnife.bind(this);
		initData();
	}

	@Override
	public void onWeightClick(View v) {
		// TODO Auto-generated method stub

	}

	@Override
	public void initView() {
		// TODO Auto-generated method stub

	}

	@Override
	public void initData() {
		this.tbv_show.setTitleText("快递详情");
		tv_name.setText(getIntent().getStringExtra("send_name"));
		tv_address.setText(getIntent().getStringExtra("send_adddress"));
		tv_phone.setText(getIntent().getStringExtra("send_phone"));
		tv_re_address.setText(getIntent().getStringExtra("rece_address"));
		tv_re_name.setText(getIntent().getStringExtra("rece_name"));
		tv_re_phone.setText(getIntent().getStringExtra("rece_phone"));
		if (tv_re_address.getText().equals("")) {
			ll_rece.setVisibility(View.GONE);
		}
		et_freight.setFocusable(false);
		et_name.setFocusable(false);
		et_keep.setFocusable(false);
		et_total.setFocusable(false);
		et_value.setFocusable(false);
		et_weight.setFocusable(false);
		et_number.setFocusable(false);
		et_freight.setTextColor(getResources().getColor(
				R.color.black));	
		et_name.setTextColor(getResources().getColor(
				R.color.black));
		et_keep.setTextColor(getResources().getColor(
				R.color.black));
		et_total.setTextColor(getResources().getColor(
				R.color.black));
		et_value.setTextColor(getResources().getColor(
				R.color.black));
		et_weight.setTextColor(getResources().getColor(
				R.color.black));
		et_number.setTextColor(getResources().getColor(
				R.color.black));
		et_name.setText(getIntent().getStringExtra("matName"));
		et_freight.setText(getIntent().getDoubleExtra("shipMoney", 0)+"元");
		et_keep.setText(getIntent().getDoubleExtra("insuranceFee",0)+"元");
		et_total.setText(getIntent().getDoubleExtra("needPayMoney",0)+"元");
		et_value.setText(getIntent().getDoubleExtra("insureMoney",0)+"元");
		et_weight.setText(getIntent().getFloatExtra("weight",0)+"kg");
		if (getIntent().getStringExtra("matType").equals("1")) {
			rb_warp.setEnabled(true);
			rb_warp.setChecked(true);
			rb_file.setEnabled(false);
		}
		else {
			rb_warp.setEnabled(false);
			rb_file.setEnabled(true);
			rb_file.setChecked(true);
		}
		
		expNo = getIntent().getStringExtra("expNo");
		expName = getIntent().getStringExtra("expName");
		et_number.setText(expNo);
		btn_wuliu.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				if (!expName.equals("") && !expNo.equals("")) {
					intent.setClass(PublicDetailShowActivity.this, ExpressCheckActivity.class);
					intent.putExtra("expName", expName);
					intent.putExtra("expNo", expNo);
					startActivity(intent);
					finish();
				}
			
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
