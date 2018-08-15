package com.hex.express.iwant.fragments;//package com.hex.express.iwant.fragments;
//
//import java.util.ArrayList;
//import java.util.List;
//
//import org.apache.http.Header;
//
//import butterknife.Bind;
//import butterknife.ButterKnife;
//
//import com.google.gson.Gson;
//import com.handmark.pulltorefresh.library.ILoadingLayout;
//import com.handmark.pulltorefresh.library.PullToRefreshBase;
//import com.handmark.pulltorefresh.library.PullToRefreshListView;
//import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
//import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
//import com.hex.express.iwant.R;
//import com.hex.express.iwant.activities.CrowdFundingActivity;
//import com.hex.express.iwant.activities.DownWindActivity;
//import com.hex.express.iwant.activities.DownWindSpecialActivity;
//import com.hex.express.iwant.activities.MyCourierActivity;
//import com.hex.express.iwant.adapters.DownwindOwnerAdapter;
//import com.hex.express.iwant.adapters.ReceiptCourierAdapter;
//import com.hex.express.iwant.bean.CardBean;
//import com.hex.express.iwant.bean.DownBean;
//import com.hex.express.iwant.bean.DownSpecialBean;
//import com.hex.express.iwant.bean.DownStrokeBean;
//import com.hex.express.iwant.bean.DownStrokeBean.Data;
//import com.hex.express.iwant.bean.PublicCourierBean;
//import com.hex.express.iwant.constance.MCUrl;
//import com.hex.express.iwant.constance.PreferenceConstants;
//import com.hex.express.iwant.http.AsyncHttpUtils;
//import com.hex.express.iwant.http.UrlMap;
//import com.hex.express.iwant.utils.PreferencesUtils;
//import com.hex.express.iwant.views.LoadingProgressDialog;
//import com.lidroid.xutils.view.annotation.event.OnItemClick;
//import com.loopj.android.http.AsyncHttpResponseHandler;
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
//
///**
// * 【我是货主】Fragment
// * 【顺风专递】界面【1-2】
// * 
// * @author han
// *
// */
//public class DownOwnerFragment extends BaseItemFragment {
//	public View rootView;
//	@Bind(R.id.view_load_fail)
//	LinearLayout view_load_fail;
//	@Bind(R.id.listview)
//	PullToRefreshListView ptrlv_card;
//	@Bind(R.id.btn_owner)
//	Button btn_owner;
//	@Bind(R.id.null_message)
//	View view_null_message;
//	protected int pageSize = 10;// 表示一页展示多少列
//	private int pageNo = 1;// 请求页码
//	private DownStrokeBean bean;
//	private List<DownStrokeBean.Data> mList;
//	DownwindOwnerAdapter adapter;
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
//	@Override
//	public void initData() {
//		btn_owner.setVisibility(View.VISIBLE);
//		listView = ptrlv_card.getRefreshableView();
//		DownActivity = (DownWindActivity) getActivity();
//		DownActivity.show();
//		
//		getHttpRequst(true, false, 1, false);
//	}
//
//	private int num;
//
//	@SuppressWarnings("unchecked")
//	private void getHttpRequst(final boolean isFirst, final boolean isRefresh,
//			int pageNo, final boolean isPull) {
//		String url = UrlMap.getThreeUrl(MCUrl.DRIVERROUTELIST, "userId", String
//				.valueOf(PreferencesUtils.getInt(getActivity()
//						.getApplicationContext(), PreferenceConstants.UID)),
//				"pageNo", String.valueOf(pageNo), "pageSize", String
//						.valueOf(pageSize));
//		AsyncHttpUtils.doSimGet(url, new AsyncHttpResponseHandler() {
//			@Override
//			public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
//				DownActivity.dissmiss();
//				Log.e("pkdkdggggggggggggggggggjfjdkj", new String(arg2));
//				mList = new ArrayList<DownStrokeBean.Data>();
//				bean = new Gson().fromJson(new String(arg2),
//						DownStrokeBean.class);
//				mList = bean.data;
//				if (isFirst) {
//					if (mList.size() != 0 && mList != null) {
//						if (adapter == null) {
//							adapter = new DownwindOwnerAdapter(getActivity(), mList);
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
//	public void onWeightClick(View v) {
//		// TODO Auto-generated method stub
//
//	}
//
//	@SuppressWarnings("unchecked")
//	@Override
//	public void setOnClick() {
//		// 下拉刷新与上拉加载
//		ptrlv_card.setMode(Mode.BOTH);
//
//		ptrlv_card.setOnRefreshListener(new OnRefreshListener2() {
//
//			@SuppressWarnings("rawtypes")
//			@Override
//			public void onPullDownToRefresh(PullToRefreshBase refreshView) {
//				getHttpRequst(false, true, 1, false);
//			}
//
//			@SuppressWarnings("rawtypes")
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
//				
//				
//				startActivity(new Intent(getActivity(),
//						DownWindSpecialActivity.class));
//				
//			}
//		});
//		btn_owner.setOnClickListener(new OnClickListener() {
//
//			@Override
//			public void onClick(View v) {
//				startActivity(new Intent(getActivity(),
//						DownWindSpecialActivity.class));
//
//				
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
