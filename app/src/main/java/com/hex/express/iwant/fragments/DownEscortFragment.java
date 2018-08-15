package com.hex.express.iwant.fragments;//package com.hex.express.iwant.fragments;
//
//import java.util.ArrayList;
//import java.util.List;
//
//import org.apache.http.Header;
//
//import android.content.Intent;
//import android.os.Bundle;
//import android.util.Log;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.View.OnClickListener;
//import android.view.ViewGroup;
//import android.widget.AdapterView;
//import android.widget.AdapterView.OnItemClickListener;
//import android.widget.Button;
//import android.widget.LinearLayout;
//import android.widget.ListView;
//import butterknife.Bind;
//import butterknife.ButterKnife;
//
//import com.google.gson.Gson;
//import com.handmark.pulltorefresh.library.ILoadingLayout;
//import com.handmark.pulltorefresh.library.PullToRefreshBase;
//import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
//import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
//import com.handmark.pulltorefresh.library.PullToRefreshListView;
//import com.hex.express.iwant.R;
//import com.hex.express.iwant.activities.DownWindActivity;
//import com.hex.express.iwant.activities.DownWindStrokeActivity;
//import com.hex.express.iwant.activities.DownEscortDetialsActivity;
//import com.hex.express.iwant.adapters.DownwindEscortAdapter;
//import com.hex.express.iwant.bean.DownSpecialBean;
//import com.hex.express.iwant.bean.DownSpecialBean.Data;
//import com.hex.express.iwant.constance.MCUrl;
//import com.hex.express.iwant.constance.PreferenceConstants;
//import com.hex.express.iwant.http.AsyncHttpUtils;
//import com.hex.express.iwant.http.UrlMap;
//import com.hex.express.iwant.utils.PreferencesUtils;
//import com.loopj.android.http.AsyncHttpResponseHandler;
//
////我是镖师的fragment
//public class DownEscortFragment extends BaseItemFragment {
//	public View rootView;
//	@Bind(R.id.view_load_fail)
//	LinearLayout view_load_fail;
//	@Bind(R.id.listview)
//	PullToRefreshListView ptrlv_card;
//	@Bind(R.id.btn_escort)
//	Button btn_escort;
//	@Bind(R.id.null_message)
//	View view_null_message;
//	protected int pageSize = 10;// 表示一页展示多少列
//	private int pageNo = 1;// 请求页码
//	private DownSpecialBean bean;
//	private List<DownSpecialBean.Data> mList;
//	DownwindEscortAdapter adapter;
//	private ListView listView;
//	private DownWindActivity DownActivity;
//
//	@Override
//	public View onCreateView(LayoutInflater inflater, ViewGroup container,
//			Bundle savedInstanceState) {
//		rootView = inflater.inflate(R.layout.fragment_list, container, false);
//		ButterKnife.bind(this, rootView);
//		initData();
//		setOnClick();
//		return rootView;
//
//	}
//
//	@Override
//	public void initData() {
//		btn_escort.setVisibility(View.VISIBLE);
//		listView = ptrlv_card.getRefreshableView();
//		DownActivity = (DownWindActivity) getActivity();
//		DownActivity.show();
//		getHttpRequst(true, false, 1, false);
//	}
//
//	private int num;
//
//	@SuppressWarnings("unchecked")
//	private void getHttpRequst(final boolean isFirst, final boolean isRefresh,
//			int pageNo, final boolean isPull) {
//		String url = UrlMap.getThreeUrl(MCUrl.DOWNWINDTASKLIST, "userId", String
//				.valueOf(PreferencesUtils.getInt(getActivity()
//						.getApplicationContext(), PreferenceConstants.UID)),
//				"pageNo", String.valueOf(pageNo), "pageSize", String
//						.valueOf(pageSize));
//		AsyncHttpUtils.doSimGet(url, new AsyncHttpResponseHandler() {
//			@Override
//			public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
//				DownActivity.dissmiss();
//				Log.e("pkdkoooooooodjfjdkj", new String(arg2));
//				mList = new ArrayList<DownSpecialBean.Data>();
//				bean = new Gson().fromJson(new String(arg2),
//						DownSpecialBean.class);
//				mList = bean.data;
//				if (isFirst) {
//					if (mList.size() != 0 && mList != null) {
//						if (adapter == null) {
//							adapter = new DownwindEscortAdapter(getActivity(), mList);
//							listView.setAdapter(adapter);
//						}
//					} else {
//						view_null_message.setVisibility(View.VISIBLE);
//						ptrlv_card.setVisibility(View.GONE);
//					}
//				} else {
//					if (isRefresh && !isPull) {
//						adapter.setData(mList);
//						adapter.notifyDataSetChanged();
//						ptrlv_card.onRefreshComplete();
//					} else if (!isRefresh && isPull) {
//						num = mList.size();
//						adapter.addData(mList);
//						adapter.notifyDataSetChanged();
//						ptrlv_card.onRefreshComplete();
//					}
//				}
//
//			}
//
//			@Override
//			public void onFailure(int arg0, Header[] arg1, byte[] arg2,
//					Throwable arg3) {
//				DownActivity.dissmiss();
//				view_load_fail.setVisibility(View.VISIBLE);
//				ptrlv_card.setVisibility(View.GONE);
//
//			}
//		});
//	}
//
//	@Override
//	public void onViewCreated(View view, Bundle savedInstanceState) {
//		super.onViewCreated(view, savedInstanceState);
//	}
//
//	@Override
//	public void onWeightClick(View v) {
//		// TODO Auto-generated method stub
//
//	}
//
//	@SuppressWarnings({ "rawtypes", "unchecked" })
//	@Override
//	public void setOnClick() {
//		// 下拉刷新与上拉加载
//		ptrlv_card.setMode(Mode.BOTH);
//
//		ptrlv_card.setOnRefreshListener(new OnRefreshListener2() {
//
//			@Override
//			public void onPullDownToRefresh(PullToRefreshBase refreshView) {
//				getHttpRequst(false, true, 1, false);
//			}
//
//			@Override
//			public void onPullUpToRefresh(PullToRefreshBase refreshView) {
//				pageNo++;
//				getHttpRequst(false, false, pageNo, true);
//				if (num < pageSize && pageNo > 2) {
//					Log.e("dd", num + "");
//					// refreshView.onRefreshComplete();
//					ILoadingLayout proxy = refreshView.getLoadingLayoutProxy(
//							false, true);
//					proxy.setPullLabel("没有更多数据了");
//					proxy.setRefreshingLabel("没有更多数据了");
//					proxy.setReleaseLabel("没有更多数据了");
//				}
//			}
//		});
//		listView.setOnItemClickListener(new OnItemClickListener() {
//
//			@Override
//			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
//					long arg3) {
//				Data data = (Data) adapter.list.get(arg2-1);
//				Intent intent = new Intent(getActivity(), DownEscortDetialsActivity.class);
//				intent.putExtra("recId", String.valueOf(data.recId));
//				intent.putExtra("publishTime", data.publishTime);
//				intent.putExtra("mobile", data.mobile);
//				intent.putExtra("address", data.address);
//				intent.putExtra("addressTo", data.addressTo);
//				intent.putExtra("matName", data.matName);
//				intent.putExtra("transferMoney", String.valueOf(data.transferMoney));
//				intent.putExtra("matRemark", data.matRemark);
//				intent.putExtra("matImageUrl", data.matImageUrl);
//				intent.putExtra("fromLatitude",String.valueOf(data.fromLatitude) );
//				intent.putExtra("fromLongitude", String.valueOf(data.fromLongitude));
//				intent.putExtra("toLatitude", String.valueOf(data.toLatitude));
//				intent.putExtra("toLongitude",String.valueOf( data.toLongitude));
//				Log.e("recId", data.recId+"");
//				startActivity(intent);
//				
//			}
//		});
//		btn_escort.setOnClickListener(new OnClickListener() {
//
//			@Override
//			public void onClick(View v) {
//				startActivity(new Intent(getActivity(),
//						DownWindStrokeActivity.class));
//
//			}
//		});
//
//	}
//
//	@Override
//	public void getData() {
//		// TODO Auto-generated method stub
//
//	}
//
//}
