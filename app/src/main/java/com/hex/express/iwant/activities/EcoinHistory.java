package com.hex.express.iwant.activities;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.google.gson.Gson;
import com.handmark.pulltorefresh.library.ILoadingLayout;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.hex.express.iwant.R;
import com.hex.express.iwant.adapters.EcoinHistoryAdapter;
import com.hex.express.iwant.bean.EcoinBean;
import com.hex.express.iwant.constance.MCUrl;
import com.hex.express.iwant.constance.PreferenceConstants;
import com.hex.express.iwant.http.AsyncHttpUtils;
import com.hex.express.iwant.http.UrlMap;
import com.hex.express.iwant.iWantApplication;
import com.hex.express.iwant.utils.PreferencesUtils;
import com.hex.express.iwant.utils.ToastUtil;
import com.hex.express.iwant.views.TitleBarView;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * E币历史
 * 
 * @author SCHT-50
 * 
 */
public class EcoinHistory extends BaseActivity {
	@Bind(R.id.tbv_show)
	TitleBarView tbv_show;
	@Bind(R.id.ptrlv_card)
	PullToRefreshListView ptrlv_card;
	@Bind(R.id.null_message)
	View view_null_message;

	private List<EcoinBean.Data> mList;
	private List<EcoinBean.Data> mList2;
	private EcoinBean bean;
	private EcoinHistoryAdapter adapter;
	private ListView listView;
	protected int pageSize = 100;// 表示一页展示多少列
	protected int pageNo = 1;
	protected int num;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activty_ecoinhistory);
		iWantApplication.getInstance().addActivity(this);
		ButterKnife.bind(this);
		getData();
		initView();
		initData();
		setOnClick();
	}

	@Override
	public void onWeightClick(View v) {

	}

	@Override
	public void initView() {
		// 标题
		this.tbv_show.setTitleText("积分历史");
		listView = ptrlv_card.getRefreshableView();

	}

	@Override
	public void initData() {
		mList = new ArrayList<EcoinBean.Data>();
		mList2 = new ArrayList<EcoinBean.Data>();
		dialog.show();
		getHttprequst(true,false, 1, false);
	}

	/**
	 * 获取网路数据
	 */
	private void getHttprequst(final boolean isFirst,final boolean isRefresh, int pageNo,
			final boolean isPull) {
		RequestParams params = new RequestParams();
		Log.e("json",
				""
						+ UrlMap.getThreeUrl(MCUrl.ECOIN, "userId", String
								.valueOf(PreferencesUtils.getInt(
										getApplicationContext(),
										PreferenceConstants.UID)), "pagerNum",
								String.valueOf(pageNo), "pager", String
										.valueOf(pageSize)));
		AsyncHttpUtils.doGet(UrlMap.getThreeUrl(MCUrl.ECOIN, "userId", String
				.valueOf(PreferencesUtils.getInt(getApplicationContext(),
						PreferenceConstants.UID)), "pageNo", String
				.valueOf(pageNo), "pageSize", String.valueOf(pageSize)), null,
				null, params, new AsyncHttpResponseHandler() {

					@Override
					public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
						dialog.dismiss();
						bean = new Gson().fromJson(new String(arg2),
								EcoinBean.class);
						mList2.clear();
						mList = bean.data;
						if (isFirst) {
							if (mList.size() != 0 && mList != null) {
								if (adapter == null) {
									adapter = new EcoinHistoryAdapter(
											EcoinHistory.this, mList);
									listView.setAdapter(adapter);
								}
							} else {
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
					public void onFailure(int arg0, Header[] arg1, byte[] arg2,
							Throwable arg3) {
						dialog.dismiss();
						ToastUtil.shortToast(EcoinHistory.this, "网络请求失败");
					}
				});

	}

	@SuppressWarnings({ "deprecation", "rawtypes", "unchecked" })
	@Override
	public void setOnClick() {
		// 下拉刷新与上拉加载
		ptrlv_card.setMode(Mode.BOTH);
		ptrlv_card.setOnRefreshListener(new OnRefreshListener2() {

			@Override
			public void onPullDownToRefresh(PullToRefreshBase refreshView) {
				Log.e("vv", "vvv");
				// 刷新 重新加载数据
				getHttprequst(false,true, 1, false);
			}

			@SuppressWarnings("deprecation")
			@Override
			public void onPullUpToRefresh(PullToRefreshBase refreshView) {
				pageNo++;
				getHttprequst(false,false, pageNo, true);
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
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO 此处实现item点击获取数据的逻辑
			}
		});
	}

	@Override
	public void getData() {

	}
}
