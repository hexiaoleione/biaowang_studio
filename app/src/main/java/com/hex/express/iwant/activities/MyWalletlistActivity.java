package com.hex.express.iwant.activities;

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
import com.hex.express.iwant.iWantApplication;
import com.hex.express.iwant.adapters.WalletAdapter;
import com.hex.express.iwant.bean.WalletBean;
import com.hex.express.iwant.constance.MCUrl;
import com.hex.express.iwant.constance.PreferenceConstants;
import com.hex.express.iwant.http.AsyncHttpUtils;
import com.hex.express.iwant.http.UrlMap;
import com.hex.express.iwant.utils.PreferencesUtils;
import com.hex.express.iwant.utils.ToastUtil;
import com.hex.express.iwant.views.TitleBarView;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;
import butterknife.Bind;
import butterknife.ButterKnife;

public class MyWalletlistActivity extends BaseActivity{

	private TitleBarView tbv_show;
	@Bind(R.id.ptrl_wallet)
	PullToRefreshListView ptrlv_card;
	@Bind(R.id.null_message)
	View view_null_message;
	private ListView listView;
	private List<WalletBean.Data> mList2;
	private List<WalletBean.Data> mList;
	protected int pageSize = 10;// 表示一页展示多少列
	protected int pageNo = 1;
	private WalletAdapter adapter;
	private WalletBean walletBean;
	boolean flag=true;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.mywalletlist_activity);
		iWantApplication.getInstance().addActivity(this);
		ButterKnife.bind(this);
		getData();
		initView();
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
		// 标题
				tbv_show = (TitleBarView) findViewById(R.id.tbv_show);
				this.tbv_show.setTitleText("明细");
				// 用于展示数据的listview
				listView = ptrlv_card.getRefreshableView();
	}

	@Override
	public void initData() {
		// TODO Auto-generated method stub
		mList = new ArrayList<WalletBean.Data>();
		mList2= new ArrayList<WalletBean.Data>();
		dialog.show();
		getHttprequst(true,false, 1, false);
	}
	/**
	 * 异步请求数据
	 */
	private void getHttprequst(final boolean isFirst,final boolean isRefresh, int pageNo,
			final boolean isPull) {
		RequestParams params = new RequestParams();
		Log.e("json", UrlMap.getThreeUrl(MCUrl.WALLET, "userId", String
				.valueOf(PreferencesUtils.getInt(getApplicationContext(),
						PreferenceConstants.UID)), "pageNo", String
				.valueOf(pageNo), "pageSize", String.valueOf(pageSize)));

		AsyncHttpUtils.doGet(UrlMap.getThreeUrl(MCUrl.WALLET, "userId", String
				.valueOf(PreferencesUtils.getInt(getApplicationContext(),
						PreferenceConstants.UID)), "pageNo", pageNo + "",
				"pageSize", String.valueOf(pageSize)), null, null, params,
				new AsyncHttpResponseHandler() {

					@Override
					public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
						// TODO Auto-generated method stub
						mList2.clear();
						dialog.dismiss();
						Log.e("json", new String(arg2));
						walletBean = new Gson().fromJson(new String(arg2),
								WalletBean.class);
						mList = walletBean.data;
						if (isFirst) {
							if (mList.size() != 0 && mList != null) {
								if (adapter == null) {
									adapter = new WalletAdapter(
											MyWalletlistActivity.this, mList);
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
						ToastUtil.shortToast(MyWalletlistActivity.this, "网络请求失败");

					}
				});

	}

	private int num;

	@SuppressWarnings({ "rawtypes", "unchecked" })
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
//				getrequstBalance();
			}

			@SuppressWarnings("deprecation")
			@Override
			public void onPullUpToRefresh(PullToRefreshBase refreshView) {
//				getrequstBalance();
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
		// TODO Auto-generated method stub
		
	}

}
