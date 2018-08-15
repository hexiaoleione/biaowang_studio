package com.hex.express.iwant.fragments;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.gson.Gson;
import com.handmark.pulltorefresh.library.ILoadingLayout;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.hex.express.iwant.R;
import com.hex.express.iwant.activities.MyCourierActivity;
import com.hex.express.iwant.activities.PublicDetailMoreShowActivity;
import com.hex.express.iwant.activities.PublicDetailShowActivity;
import com.hex.express.iwant.activities.PublicOtherDetailActivity;
import com.hex.express.iwant.adapters.ReceiptCourierAdapter;
import com.hex.express.iwant.bean.PublicCourierBean;
import com.hex.express.iwant.bean.PublicCourierBean.Data;
import com.hex.express.iwant.constance.MCUrl;
import com.hex.express.iwant.constance.PreferenceConstants;
import com.hex.express.iwant.http.AsyncHttpUtils;
import com.hex.express.iwant.http.UrlMap;
import com.hex.express.iwant.utils.PreferencesUtils;
import com.hex.express.iwant.utils.ToastUtil;
import com.hex.express.iwant.views.LoadingProgressDialog;
import com.loopj.android.http.AsyncHttpResponseHandler;

import android.R.integer;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 【我的发布】Fragment 【我的快递】--【1-2】
 * 
 * @author SCHT-50
 * 
 */
public class ReceiptMycourierFragment extends BaseItemFragment {
	public View rootView;
	@Bind(R.id.view_load_fail)
	LinearLayout view_load_fail;
	@Bind(R.id.listview)
	PullToRefreshListView ptrlv_card;
	@Bind(R.id.null_message)
	View view_null_message;
	protected int pageSize = 10;// 表示一页展示多少列
	private int pageNo = 1;// 请求页码
	private PublicCourierBean bean;
	private List<Data> mList;
	private List<Data> mList2;
	LoadingProgressDialog dialog;
	ReceiptCourierAdapter adapter;
	private ListView listView;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		rootView = inflater.inflate(R.layout.fragment_list, container, false);
		ButterKnife.bind(this, rootView);
		initData();
		setOnClick();
		return rootView;

	}

	@Override
	public void initData() {
		listView = ptrlv_card.getRefreshableView();
		myCourierActivity = (MyCourierActivity) getActivity();
		myCourierActivity.show();
		getHttpRequst(true, false, 1, false);
		delete();
	}

	/**
	 * 删除
	 */
	public void delete() {
		listView.setOnItemLongClickListener(new OnItemLongClickListener() {

			private int deleter;

			@Override
			public boolean onItemLongClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				Data data = (Data) adapter.list.get(arg2 - 1);
				final String billCode = data.billCode;
				if (data.status.equals("2")||data.status.equals("1")||data.status.equals("0")) {
					deleter = R.array.thread_menu;
				} else if (data.status.equals("4")||data.status.equals("3")){
					deleter=R.array.thread_cancle;
				}
				AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
				builder.setItems(deleter, new OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						Log.e("111json", MCUrl.EXPRESSDELETE + billCode);
						JSONObject obj = new JSONObject();
						try {
							obj.put("billCode", billCode);
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						AsyncHttpUtils.doPut(getActivity(), MCUrl.EXPRESSDELETE, obj.toString(), null,
								new AsyncHttpResponseHandler() {

									@Override
									public void onFailure(int arg0, Header[] arg1, byte[] arg2, Throwable arg3) {
										// TODO Auto-generated method stub
										Log.e("msg111", new String(arg2));
									}

									@Override
									public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
										Log.e("msg222", new String(arg2));
										getHttpRequst(false, true, 1, false);
										adapter.notifyDataSetChanged();
									}

								});
					}
				});
				builder.show();
				return true;
			}
		});
	}

	private int num;

	@SuppressWarnings("unchecked")
	private void getHttpRequst(final boolean isFirst, final boolean isRefresh, int pageNo, final boolean isPull) {
		String url = UrlMap.getThreeUrl(MCUrl.MYEXPRESS, "userId",
				String.valueOf(PreferencesUtils.getInt(getActivity().getApplicationContext(), PreferenceConstants.UID)),
				"pageNo", String.valueOf(pageNo), "pageSize", String.valueOf(pageSize));
		AsyncHttpUtils.doSimGet(url, new AsyncHttpResponseHandler() {
			@Override
			public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
				myCourierActivity.dissmiss();
				Log.e("sdds ", new String(arg2));
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
						ptrlv_card.setVisibility(View.GONE);
					}
				} else {
					if (isRefresh && !isPull) {
						mList2.clear();
						mList2.addAll(mList);
						adapter.setData(mList2);
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
			public void onFailure(int arg0, Header[] arg1, byte[] arg2, Throwable arg3) {
				myCourierActivity.dissmiss();
				view_load_fail.setVisibility(View.VISIBLE);
				ptrlv_card.setVisibility(View.GONE);

			}
		});
	}

	@Override
	public void onWeightClick(View v) {

	}

	private ListView lv;
	private MyCourierActivity myCourierActivity;

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

	@Override
	public void getData() {
		// TODO Auto-generated method stub

	}

}
