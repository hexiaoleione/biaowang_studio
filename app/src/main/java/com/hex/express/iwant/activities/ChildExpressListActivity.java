package com.hex.express.iwant.activities;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.Header;
import org.json.JSONException;

import com.google.gson.Gson;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.hex.express.iwant.R;
import com.hex.express.iwant.adapters.PublicChildExpressAdapter;
import com.hex.express.iwant.bean.ChildExpressBean;
import com.hex.express.iwant.bean.ChildExpressBean.Data;
import com.hex.express.iwant.bean.ConsecutiveBean;
import com.hex.express.iwant.constance.MCUrl;
import com.hex.express.iwant.http.AsyncHttpUtils;
import com.hex.express.iwant.http.UrlMap;
import com.loopj.android.http.AsyncHttpResponseHandler;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
/**
 * 快递员我的接单点击详情
 * @author huyichuan
 *
 */
public class ChildExpressListActivity extends BaseActivity {
	PublicChildExpressAdapter adapter;
	List<ChildExpressBean.Data> mlist=new ArrayList<ChildExpressBean.Data>();
	private String businessId;

	private int errCode;
	private String message;
	private double sum;
	private double allmoney;
//	普通多单
	TextView tv_number;
	TextView tv_totalPrice;
	PullToRefreshListView ptrlv_card;
	ListView listView;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		businessId = getIntent().getStringExtra("businessId");
		getPaying();
	}
	/**
	 * 获取未完成的订单
	 */
	private void getPaying() {
		Log.e("msg", UrlMap.getUrl(MCUrl.EXPRESSV2, "businessId", businessId));
		AsyncHttpUtils.doGet(UrlMap.getUrl(MCUrl.EXPRESSV2, "businessId", businessId), null, null, null,
				new AsyncHttpResponseHandler() {

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
						Log.e("msg", new String(arg2));
						if (errCode == 0) {
							setContentView(R.layout.popwinds_expressdetail_2);
							ptrlv_card=(PullToRefreshListView) findViewById(R.id.ptrlv_card);
							tv_totalPrice=(TextView) findViewById(R.id.tv_totalPrice);
							ImageView img_cancel03 = (ImageView) findViewById(R.id.img_cancel03);
							tv_number=(TextView) findViewById(R.id.tv_number);
							listView=ptrlv_card.getRefreshableView();
							img_cancel03.setOnClickListener(new OnClickListener() {
								
								@Override
								public void onClick(View arg0) {
									finish();
									
								}
							});
							ChildExpressBean bean = new Gson().fromJson(new String(arg2), ChildExpressBean.class);
							mlist = bean.data;
							getSummation(mlist);
							Log.e("mlis", mlist.size()+"");
							tv_number.setText("单数:  "+mlist.size());
							adapter = new PublicChildExpressAdapter(ChildExpressListActivity.this, mlist);
							listView.setAdapter(adapter);
						}
							else if (errCode == 1) {
								setContentView(R.layout.popwinds_expressdetail_1);
								ImageView img_cancel03 = (ImageView) findViewById(R.id.img_cancel03);
								TextView tv_number=(TextView) findViewById(R.id.tv_number);
								TextView tv_expNo_start=(TextView) findViewById(R.id.tv_expNo_start);
								TextView tv_expNo_end=(TextView) findViewById(R.id.tv_expNo_end);
								TextView tv_price=(TextView) findViewById(R.id.tv_price);
								TextView tv_totalPrice=(TextView) findViewById(R.id.tv_totalPrice);
								ConsecutiveBean bean = new Gson().fromJson(new String(arg2), ConsecutiveBean.class);
								tv_expNo_start.setText("开始单号:   "+bean.data.get(0).startBillCode);
								tv_expNo_end.setText("结束单号:   "+bean.data.get(0).endBillCode);
								tv_price.setText("每单金额: "+bean.data.get(0).useMoney+"元");
								tv_totalPrice.setText("总金额: "+bean.data.get(0).needPayMoney+"元");
								double num = Double.parseDouble(bean.data.get(0).endBillCode)
										- Double.parseDouble(bean.data.get(0).startBillCode)+1;
								tv_number.setText("单数： "+num);
								
								img_cancel03.setOnClickListener(new OnClickListener() {
									
									@Override
									public void onClick(View arg0) {
										finish();
										
									}
								});
							}
							
							

//						} else {
//							ToastUtil.shortToast(PublicDetailMoreShowActivity.this, message);
//						}
					}

					@Override
					public void onFailure(int arg0, Header[] arg1, byte[] arg2, Throwable arg3) {
						// TODO Auto-generated method stub

					}
				});

	}
	@Override
	public void onWeightClick(View v) {

	}

	@Override
	public void initView() {

	}

	@Override
	public void initData() {

	}
	/**
	 * 计算总价格
	 * 
	 * @param mList
	 */
	private void getSummation(List<Data> mList) {
		sum = 0;
		DecimalFormat df = new DecimalFormat("######0.00");
		for (int i = 0; i < mlist.size(); i++) {
			allmoney = Double.parseDouble(mlist.get(i).childExpPrice);
			Log.e("allmoney", allmoney + "");
			sum = sum + allmoney;
		}
		tv_totalPrice.setText("总金额:  "+df.format(sum) +"元");

	}


	@Override
	public void getData() {

	}
	@Override
	public void setOnClick() {
		// TODO Auto-generated method stub
		
	}

}
