package com.hex.express.iwant.minfragment;


import java.util.ArrayList;
import java.util.List;

import org.apache.http.Header;

import com.google.gson.Gson;
import com.handmark.pulltorefresh.library.ILoadingLayout;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.hex.express.iwant.R;
import com.hex.express.iwant.activities.LogCompanyActivity;
import com.hex.express.iwant.activities.LogistcaInforseActivity;
import com.hex.express.iwant.activities.ProvCityTownAcrivity;
import com.hex.express.iwant.activities.PublicDetailMoreShowActivity;
import com.hex.express.iwant.activities.PublicOtherDetailActivity;
import com.hex.express.iwant.adapters.NearbyAdapter;
import com.hex.express.iwant.adapters.ReceiptCourierAdapter;
import com.hex.express.iwant.bean.PublicCourierBean.Data;
import com.hex.express.iwant.bean.PublicCourierBean;
import com.hex.express.iwant.constance.MCUrl;
import com.hex.express.iwant.constance.PreferenceConstants;
import com.hex.express.iwant.http.AsyncHttpUtils;
import com.hex.express.iwant.http.UrlMap;
import com.hex.express.iwant.utils.PreferencesUtils;
import com.hex.express.iwant.utils.ToastUtil;
import com.hex.express.iwant.views.LoadingProgressDialog;
import com.loopj.android.http.AsyncHttpResponseHandler;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.FrameLayout.LayoutParams;
import butterknife.Bind;
import butterknife.ButterKnife;

public class MinSubFragment3 extends Fragment {

	@Bind(R.id.freigh_listview)
	PullToRefreshListView ptrlv_card;
	@Bind(R.id.null_message)
	View view_null_message;
	private ListView listView;
	protected int pageSize = 10;// 表示一页展示多少列
	protected int pageNo = 1;
	private List<Data> mList;
	private List<Data> mList2;
	private ReceiptCourierAdapter adapter;
	public PublicCourierBean bean;
	private int num;
	private String cityCode;
	private String entPlaceCityCode;
	private int type=1;
	View view;
	public LoadingProgressDialog dialog;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		if (view==null) {
			view=inflater.inflate(R.layout.fragment_min3, container, false);
		}
		 ViewGroup p = (ViewGroup) view.getParent(); 
         if (p != null) { 
             p.removeAllViewsInLayout(); 
         } 
         ButterKnife.bind(this,view);
         dialog=new LoadingProgressDialog(getActivity());
 		initData();
 		getData();
		return view;
	}
	
	private void initData(){
		mList = new ArrayList<Data>();
		mList2 = new ArrayList<Data>();
		listView = ptrlv_card.getRefreshableView();
		getHttpRequst(true, false, 1, false);
		
	}
	
	/**
	 * 获取网络数据
	 */
	private void getHttpRequst(final boolean isFirst, final boolean isRefresh, int pageNo, final boolean isPull) {
		String url = UrlMap.getThreeUrl(MCUrl.MYEXPRESS, "userId",
				String.valueOf(PreferencesUtils.getInt(getActivity().getApplicationContext(), PreferenceConstants.UID)),
				"pageNo", String.valueOf(pageNo), "pageSize", String.valueOf(pageSize));
		dialog.show();
		AsyncHttpUtils.doSimGet(url, new AsyncHttpResponseHandler() {
			@Override
			public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
				Log.e("sdds ", new String(arg2));
				dialog.dismiss();
				mList = new ArrayList<Data>();
				mList2= new ArrayList<Data>();
				bean = new Gson().fromJson(new String(arg2), PublicCourierBean.class);
				mList2.clear();
				mList = bean.data;
				if (isFirst) {
					if (mList.size() != 0 && mList != null) {
						if (adapter == null) {
							adapter = new ReceiptCourierAdapter(getActivity(), mList);
							listView.setAdapter(adapter);
						}
					} else {
						Log.e("111sdds ", "111111111");
//						ToastUtil.shortToast(getActivity(), "暂无数据");
						view_null_message.setVisibility(View.VISIBLE);
					}
				} else {
					if (isRefresh && !isPull) {
						if (mList.size() != 0 && mList != null) {
						mList2.clear();
						mList2.addAll(mList);
						adapter.setData(mList2);
						adapter.notifyDataSetChanged();
						ptrlv_card.onRefreshComplete();
						} else {
							view_null_message.setVisibility(View.VISIBLE);
						}
					} else if (!isRefresh && isPull) {
						num = mList.size();
						adapter.addData(mList);
						adapter.notifyDataSetChanged();
						ptrlv_card.onRefreshComplete();
					}
				}

			}

			@Override
			public void onFailure(int arg0, Header[] arg1, byte[] arg2, Throwable arg3) {
				ptrlv_card.setVisibility(View.GONE);
					dialog.dismiss();
			}
		});
	}


	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void getData() {
		ptrlv_card.setMode(Mode.BOTH);
		ptrlv_card.setOnRefreshListener(new OnRefreshListener2() {

			@Override
			public void onPullDownToRefresh(PullToRefreshBase refreshView) {
//				getHttpOwnerRequst(true, false, 1, false, sortType);
				getHttpRequst(false, true, 1, false);
			}

			@Override
			public void onPullUpToRefresh(PullToRefreshBase refreshView) {
				pageNo++;
				getHttpRequst(false, false, pageNo, true);
				if (num < pageSize && pageNo > 2) {
					Log.e("dd", num + "");
					// refreshView.onRefreshComplete();
					ILoadingLayout proxy = refreshView.getLoadingLayoutProxy(false, true);
					proxy.setPullLabel("没有更多数据了");
					proxy.setRefreshingLabel("没有更多数据了");
					proxy.setReleaseLabel("没有更多数据了");
				}
			}
		});
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				Intent intent = new Intent();
				Data data = (Data) adapter.list.get(arg2 - 1);
				// ToastUtil.shortToast(getActivity(), data.personName);
				if (data.status.equals("1") || data.status.equals("4")) {
					intent.setClass(getActivity(), PublicOtherDetailActivity.class);
					intent.putExtra("send_name", data.personName);
					intent.putExtra("send_adddress", data.areaName + data.address);
					intent.putExtra("send_phone", data.mobile);
					intent.putExtra("rece_address", data.areaNameTo + data.addressTo);
					intent.putExtra("rece_name", data.personNameTo);
					intent.putExtra("rece_phone", data.mobileTo);
					intent.putExtra("billCode", data.billCode);
					intent.putExtra("pointStatus", data.pointStatus);
					intent.putExtra("businessId", String.valueOf(data.businessId));
					startActivity(intent);
				} else if (data.status.equals("2")) {
					intent.setClass(getActivity(), PublicDetailMoreShowActivity.class);
					// intent.putExtra("send_name", data.personName);
					// intent.putExtra("send_adddress", data.areaName
					// + data.address);
					// intent.putExtra("send_phone", data.mobile);
					// intent.putExtra("rece_address", data.areaNameTo
					// + data.addressTo);
					// intent.putExtra("rece_name", data.personNameTo);
					// intent.putExtra("rece_phone", data.mobileTo);
					// intent.putExtra("matType", data.matType);
					// intent.putExtra("matName", data.matName);
					// intent.putExtra("weight", data.weight);
					// intent.putExtra("shipMoney", data.shipMoney);
					// intent.putExtra("expName", data.expName);
					// intent.putExtra("insureMoney", data.insureMoney);
					// intent.putExtra("insuranceFee", data.insuranceFee);
					// intent.putExtra("needPayMoney", data.needPayMoney);
					// intent.putExtra("expNo", data.expNo);
					intent.putExtra("send_name", data.personName);
					intent.putExtra("send_adddress", data.areaName + data.address);
					intent.putExtra("send_phone", data.mobile);
					intent.putExtra("businessId", String.valueOf(data.businessId));
					Log.e("businessId", data.businessId + "");
					intent.putExtra("status", data.status);
					intent.putExtra("expName", data.expName);
					startActivity(intent);
				}else if (data.status.equals("3")) {
					ToastUtil.shortToast(getActivity(), "您的单未被快递员抢，不能查看详情！");
				}

			}
		});

	}
}
