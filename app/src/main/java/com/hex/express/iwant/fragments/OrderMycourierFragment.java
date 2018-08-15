package com.hex.express.iwant.fragments;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.Header;
import org.json.JSONObject;

import com.google.gson.Gson;
import com.handmark.pulltorefresh.library.ILoadingLayout;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.hex.express.iwant.R;
import com.hex.express.iwant.activities.MessageActivity;
import com.hex.express.iwant.adapters.OrderCourierAdapter;
import com.hex.express.iwant.bean.PublicCourierBean;
import com.hex.express.iwant.bean.MessageBean.Data;
import com.hex.express.iwant.constance.MCUrl;
import com.hex.express.iwant.constance.PreferenceConstants;
import com.hex.express.iwant.http.AsyncHttpUtils;
import com.hex.express.iwant.http.UrlMap;
import com.hex.express.iwant.utils.PreferencesUtils;
import com.hex.express.iwant.utils.ToastUtil;
import com.loopj.android.http.AsyncHttpResponseHandler;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
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
 * 【我的发布】Fragment
 * 【我的快递】--【1-2】
 * @author SCHT-50
 * 
 */

public class OrderMycourierFragment extends BaseItemFragment {
	public View rootView;
	@Bind(R.id.view_load_fail)
	LinearLayout view_load_fail;
	@Bind(R.id.listview)
	PullToRefreshListView ptrlv_card;
	private ListView listView;
//	@Bind(R.id.null_message)
	View view_null_message;
	protected int pageSize = 10;// 表示一页展示多少列
	private int pageNo = 1;// 请求页码
	private PublicCourierBean bean;
	private List<PublicCourierBean.Data> mList;
	private List<PublicCourierBean.Data> mList2;
	OrderCourierAdapter adapter;
	protected int num;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		rootView = inflater.inflate(R.layout.fragment_list, container, false);
		ButterKnife.bind(this, rootView);
		view_null_message=rootView.findViewById(R.id.null_message);
		initData();
		setOnClick();
		return rootView;
	}

	@Override
	public void initData() {
		listView = ptrlv_card.getRefreshableView();
		mList = new ArrayList<PublicCourierBean.Data>();
		mList2 = new ArrayList<PublicCourierBean.Data>();
		getHttpRequst(true, false, 1, false);
	}
	
	private void getHttpRequst(final boolean isFirst, final boolean isRefresh,
			int pageNo, final boolean isPull) {

		String url = UrlMap.getThreeUrl(MCUrl.RECEIVE, "userId", String
				.valueOf(PreferencesUtils.getInt(getActivity()
						.getApplicationContext(), PreferenceConstants.UID)),
				"pageNo", String.valueOf(pageNo), "pageSize", String
						.valueOf(pageSize));
		Log.e("json", url);
		AsyncHttpUtils.doGet(url, null, null, null,
				new AsyncHttpResponseHandler() {

					@Override
					public void onFailure(int arg0, Header[] arg1, byte[] arg2,
							Throwable arg3) {
				

					}

					@Override
					public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
						Log.e("json", "********************" + new String(arg2));
						bean = new Gson().fromJson(new String(arg2),
								PublicCourierBean.class);
						mList2.clear();
						mList = bean.data;
						if (isFirst) {
							if (mList.size() != 0 && mList != null) {
								if (adapter == null) {
									adapter = new OrderCourierAdapter(
											getActivity(), mList);
									listView.setAdapter(adapter);
								}
							} else {
								Log.e("111sdds ", "111111111");
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

				});
	}

	@Override
	public void onWeightClick(View v) {
		// TODO Auto-generated method stub

	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public void setOnClick() {
		// 下拉刷新与上拉加载
		ptrlv_card.setMode(Mode.BOTH);

		ptrlv_card.setOnRefreshListener(new OnRefreshListener2() {

			@Override
			public void onPullDownToRefresh(PullToRefreshBase refreshView) {
				// 刷新 重新加载数据

				getHttpRequst(false, true, 1, false);

			}

			@SuppressWarnings("rawtypes")
			@Override
			public void onPullUpToRefresh(PullToRefreshBase refreshView) {
				pageNo++;
				getHttpRequst(false, false, pageNo, true);
				if (num < pageSize && pageNo > 2) {
					Log.e("dd", num + "");
					ILoadingLayout proxy = refreshView.getLoadingLayoutProxy(
							false, true);
					proxy.setPullLabel("没有更多数据了");
					proxy.setRefreshingLabel("没有更多数据了");
					proxy.setReleaseLabel("没有更多数据了");
					
				}

			}
			
			
		});
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position,
					long arg3) {
//			startActivity(new Intent(getActivity(),abc.class));
			}
		});

	}

	@Override
	public void getData() {
		// TODO Auto-generated method stub

	}

}
