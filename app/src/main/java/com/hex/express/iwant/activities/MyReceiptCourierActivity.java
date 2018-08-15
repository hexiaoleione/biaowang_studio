package com.hex.express.iwant.activities;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.Header;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import butterknife.Bind;
import butterknife.ButterKnife;

import com.google.gson.Gson;
import com.handmark.pulltorefresh.library.ILoadingLayout;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.hex.express.iwant.R;
import com.hex.express.iwant.adapters.ReceiptCourierAdapter;
import com.hex.express.iwant.bean.PublicCourierBean;
import com.hex.express.iwant.bean.PublicCourierBean.Data;
import com.hex.express.iwant.constance.MCUrl;
import com.hex.express.iwant.constance.PreferenceConstants;
import com.hex.express.iwant.http.AsyncHttpUtils;
import com.hex.express.iwant.http.UrlMap;
import com.hex.express.iwant.utils.PreferencesUtils;
import com.hex.express.iwant.views.TitleBarView;
import com.loopj.android.http.AsyncHttpResponseHandler;

public class MyReceiptCourierActivity extends BaseActivity {
	@Bind(R.id.view_load_fail)
	LinearLayout view_load_fail;
	@Bind(R.id.ptrl_wallet)
	PullToRefreshListView ptrlv_card;
	@Bind(R.id.null_message)
	View view_null_message;
	@Bind(R.id.tbv_show)
	TitleBarView tbv_show;
	protected int pageSize = 10;// 表示一页展示多少列
	private int pageNo = 1;// 请求页码
	private PublicCourierBean bean;
	private List<PublicCourierBean.Data> mList;
	ReceiptCourierAdapter adapter;	
	private ListView listView;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_receipt_courier);
		ButterKnife.bind(MyReceiptCourierActivity.this);
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

	}

	@Override
	public void initData() {
		listView=ptrlv_card.getRefreshableView();
	tbv_show.setTitleText("我的快递");
	dialog.show();
	getHttpRequst(true, false, 1, false);

	}
	private int num;

	@SuppressWarnings("unchecked")
	private void getHttpRequst(final boolean isFirst, final boolean isRefresh,
			int pageNo, final boolean isPull) {
		String url = UrlMap.getThreeUrl(MCUrl.MYEXPRESS, "userId", String
				.valueOf(PreferencesUtils.getInt(MyReceiptCourierActivity.this
						.getApplicationContext(), PreferenceConstants.UID)),
				"pageNo", String.valueOf(pageNo), "pageSize", String
						.valueOf(pageSize));
		AsyncHttpUtils.doSimGet(url, new AsyncHttpResponseHandler() {
			@Override
			public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
				dialog.dismiss();
				mList = new ArrayList<PublicCourierBean.Data>();
				bean = new Gson().fromJson(new String(arg2),
						PublicCourierBean.class);
				mList = bean.data;
				if (isFirst) {
					if (mList.size() != 0 && mList != null) {
						if (adapter == null) {
							adapter = new ReceiptCourierAdapter(
									MyReceiptCourierActivity.this, mList);
							listView.setAdapter(adapter);
						}
					} else {
						view_null_message.setVisibility(View.VISIBLE);
						ptrlv_card.setVisibility(View.GONE);
					}
				} else {
					if (isRefresh && !isPull) {
						adapter.setData(mList);
						adapter.notifyDataSetChanged();
						ptrlv_card.onRefreshComplete();
					} else if (!isRefresh && isPull) {
						num = mList.size();
						adapter.addData(mList);
						adapter.notifyDataSetChanged();
						ptrlv_card.onRefreshComplete();
					}
				}

			}
			
			@Override
			public void onFailure(int arg0, Header[] arg1, byte[] arg2,
					Throwable arg3) {
				dialog.dismiss();
				view_load_fail.setVisibility(View.VISIBLE);
				ptrlv_card.setVisibility(View.GONE);

			}
		});
	}

	@SuppressWarnings({ "unchecked", "rawtypes", "deprecation" })
	@Override
	public void setOnClick() {
		// 下拉刷新与上拉加载
		ptrlv_card.setMode(Mode.BOTH);

		ptrlv_card.setOnRefreshListener(new OnRefreshListener2() {

			@Override
			public void onPullDownToRefresh(PullToRefreshBase refreshView) {
				getHttpRequst(false, true, 1, false);
			}

			@SuppressWarnings("rawtypes")
			@Override
			public void onPullUpToRefresh(PullToRefreshBase refreshView) {
				pageNo++;
				getHttpRequst(false, false, pageNo, true);
				if (num < pageSize && pageNo > 2) {
					Log.e("dd", num + "");
					// refreshView.onRefreshComplete();
					ILoadingLayout proxy = refreshView.getLoadingLayoutProxy(
							false, true);
					proxy.setPullLabel("没有更多数据了");
					proxy.setRefreshingLabel("没有更多数据了");
					proxy.setReleaseLabel("没有更多数据了");
				}
			}
		});
		// lv=ptrlv_card.getRefreshableView();
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				Intent intent = new Intent();
				PublicCourierBean.Data data = (Data) adapter.list.get(arg2 - 1);
				// ToastUtil.shortToast(getActivity(), data.personName);
				if (data.status.equals("1")) {
					intent.setClass(MyReceiptCourierActivity.this, PublicOtherDetailActivity.class);
					intent.putExtra("send_name", data.personName);
					intent.putExtra("send_adddress", data.areaName
							+ data.address);
					intent.putExtra("send_phone", data.mobile);
					intent.putExtra("rece_address", data.areaNameTo
							+ data.addressTo);
					intent.putExtra("rece_name", data.personNameTo);
					intent.putExtra("rece_phone", data.mobileTo);
					intent.putExtra("billCode", data.billCode);
					intent.putExtra("pointStatus", data.pointStatus);
					intent.putExtra("businessId",
							String.valueOf(data.businessId));

				} else {
					intent.setClass(MyReceiptCourierActivity.this,
							PublicDetailShowActivity.class);
					intent.putExtra("send_name", data.personName);
					intent.putExtra("send_adddress", data.areaName
							+ data.address);
					intent.putExtra("send_phone", data.mobile);
					intent.putExtra("rece_address", data.areaNameTo
							+ data.addressTo);
					intent.putExtra("rece_name", data.personNameTo);
					intent.putExtra("rece_phone", data.mobileTo);
					intent.putExtra("matType", data.matType);
					intent.putExtra("matName", data.matName);
					intent.putExtra("weight", data.weight);
					intent.putExtra("shipMoney", data.shipMoney);
					intent.putExtra("expName", data.expName);
					intent.putExtra("insureMoney", data.insureMoney);
					intent.putExtra("insuranceFee", data.insuranceFee);
					intent.putExtra("needPayMoney", data.needPayMoney);
					intent.putExtra("expNo", data.expNo);
				}
				startActivity(intent);
			}
		});

	}


	@Override
	public void getData() {
		// TODO Auto-generated method stub

	}

}
