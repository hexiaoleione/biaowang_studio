package com.hex.express.iwant.activities;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.hex.express.iwant.R;
import com.hex.express.iwant.adapters.PublicMoreAdapter;
import com.hex.express.iwant.bean.ChildExpressBean;
import com.hex.express.iwant.bean.ChildExpressBean.Data;
import com.hex.express.iwant.bean.ConsecutiveBean;
import com.hex.express.iwant.constance.MCUrl;
import com.hex.express.iwant.http.AsyncHttpUtils;
import com.hex.express.iwant.http.UrlMap;
import com.hex.express.iwant.iWantApplication;
import com.hex.express.iwant.utils.ToastUtil;
import com.hex.express.iwant.views.MarqueeTextView;
import com.hex.express.iwant.views.TitleBarView;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONException;

import java.text.DecimalFormat;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 【快递详情】
 * 
 * @author SCHT-50
 * 
 */
public class PublicDetailMoreShowActivity extends BaseActivity {
	@Bind(R.id.tv_name)
	TextView tv_name;
	@Bind(R.id.tv_phone)
	TextView tv_phone;
	@Bind(R.id.tv_address)
	MarqueeTextView tv_address;
	@Bind(R.id.tbv_show)
	TitleBarView tbv_show;
	@Bind(R.id.ptrlv_card)
	PullToRefreshListView ptrlv_card;
	@Bind(R.id.tv_needPayMoney)
	TextView tv_needPayMoney;
	@Bind(R.id.btn_submit)
	Button btn_submit;
	ListView listView;
	PublicMoreAdapter adapter;
	List<ChildExpressBean.Data> mlist;
	private String businessId;
	private double sum;
	private double allmoney;
	private String status;
	// 添加连单号
	@Bind(R.id.lout_consecutive_number)
	LinearLayout lout_consecutive_number;
	@Bind(R.id.et_firstnumber)
	TextView et_firstnumber;
	@Bind(R.id.et_endnumber)
	TextView et_endnumber;
	@Bind(R.id.et_money)
	TextView et_money;
	@Bind(R.id.et_allnum)
	TextView et_allnum;
	private DecimalFormat df;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_public_more_evaluate);
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
		listView = ptrlv_card.getRefreshableView();
		df = new DecimalFormat("######0.00");
		this.tbv_show.setTitleText("快递详情");
		tv_name.setText(getIntent().getStringExtra("send_name"));
		tv_address.setText(getIntent().getStringExtra("send_adddress"));
		tv_phone.setText(getIntent().getStringExtra("send_phone"));
		businessId = getIntent().getStringExtra("businessId");
		status = getIntent().getStringExtra("status");
		if (status.equals("2")) {
			btn_submit.setBackgroundColor(getResources().getColor(R.color.grayview));
			btn_submit.setText("已支付");
		}
		getPaying();
	}

	/**
	 * 获取未完成的订单
	 */
	private void getPaying() {
		Log.e("msg", UrlMap.getUrl(MCUrl.EXPRESSV2, "businessId", businessId));
		AsyncHttpUtils.doGet(UrlMap.getUrl(MCUrl.EXPRESSV2, "businessId", businessId), null, null, null,
				new AsyncHttpResponseHandler() {
					private int errCode;
					private String message;

					@Override
					public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
						try {
							org.json.JSONObject object = new org.json.JSONObject(new String(arg2));
							errCode = (Integer) object.opt("errCode");
							message = (String) object.opt("message");
							Log.e("errcode", errCode + "");
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						Log.e("msgfffffff", new String(arg2));
						if (errCode == 0) {
							ptrlv_card.setVisibility(View.VISIBLE);
							lout_consecutive_number.setVisibility(View.GONE);
							ChildExpressBean bean = new Gson().fromJson(new String(arg2), ChildExpressBean.class);
							mlist = bean.data;
							getSummation(mlist);
							adapter = new PublicMoreAdapter(PublicDetailMoreShowActivity.this, mlist);
							listView.setAdapter(adapter);
						} else if (errCode == 1) {
							lout_consecutive_number.setVisibility(View.VISIBLE);
							ptrlv_card.setVisibility(View.GONE);
							ConsecutiveBean bean = new Gson().fromJson(new String(arg2), ConsecutiveBean.class);
							et_firstnumber.setText(bean.data.get(0).startBillCode);
							et_endnumber.setText(bean.data.get(0).endBillCode);
							et_money.setText(bean.data.get(0).useMoney);
							double num = Double.parseDouble(bean.data.get(0).endBillCode)
									- Double.parseDouble(bean.data.get(0).startBillCode)+1;
							et_allnum.setText(num + "");
							Log.e("json", bean.data.get(0).needPayMoney);
							tv_needPayMoney.setText(df.format(Double.parseDouble(bean.data.get(0).needPayMoney))+"");

						} else {
							ToastUtil.shortToast(PublicDetailMoreShowActivity.this, message);
						}
					}

					@Override
					public void onFailure(int arg0, Header[] arg1, byte[] arg2, Throwable arg3) {
						// TODO Auto-generated method stub

					}
				});

	}

	/**
	 * 计算总价格
	 * 
	 * @param mList
	 */
	private void getSummation(List<Data> mList) {
		sum = 0;
		for (int i = 0; i < mlist.size(); i++) {
			allmoney = Double.parseDouble(mlist.get(i).childExpPrice);
			Log.e("allmoney", allmoney + "");
			sum = sum + allmoney;
		}
		tv_needPayMoney.setText(df.format(sum) + "");

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
