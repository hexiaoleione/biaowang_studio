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
import com.hex.express.iwant.adapters.MylisFreighAdapter;
import com.hex.express.iwant.adapters.MylositaAdapter;
import com.hex.express.iwant.bean.CardBean;
import com.hex.express.iwant.bean.CardBean.Data;
import com.hex.express.iwant.constance.MCUrl;
import com.hex.express.iwant.constance.PreferenceConstants;
import com.hex.express.iwant.http.AsyncHttpUtils;
import com.hex.express.iwant.http.UrlMap;
import com.hex.express.iwant.utils.PreferencesUtils;
import com.hex.express.iwant.utils.ToastUtil;
import com.hex.express.iwant.views.TitleBarView;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;
import butterknife.Bind;
import butterknife.ButterKnife;
/**
 * 
 * @author huyichuan
 * 公司 我的物流
 */
public class LoistcalMyActivity extends BaseActivity{
	@Bind(R.id.myli_listview)
	PullToRefreshListView freigh_listview;
	@Bind(R.id.my_show)
	TitleBarView freigh_show;
	private ListView listView;
	protected int pageSize = 10;// 表示一页展示多少列
	protected int pageNo = 1;
	private List<CardBean.Data> mList;
	private MylositaAdapter adapter;
	public CardBean bean;
	private int num;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.logistio_my);
		ButterKnife.bind(this);
		initView();
		initData();
		setOnClick();
		getData();
	}
	@Override
	public void onWeightClick(View v) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void initView() {
		// TODO Auto-generated method stub
		freigh_show.setTitleText("物流货运");
		listView = freigh_listview.getRefreshableView();
		
	}

	@Override
	public void initData() {
		// TODO Auto-generated method stub
		mList = new ArrayList<CardBean.Data>();
//		dialog.show();
//		getHttprequst(true, false, 1, false);
	}
	/**
	 * 获取网络数据
	 */
	private void getHttprequst(final boolean isFirst, final boolean isRefresh,
			int pageNo, final boolean isPull) {
		RequestParams params = new RequestParams();
		AsyncHttpUtils.doGet(UrlMap.getThreeUrl(MCUrl.COUPON, "userId", String
				.valueOf(PreferencesUtils.getInt(getApplicationContext(),
						PreferenceConstants.UID)), "pageNo", pageNo + "",
				"pageSize", String.valueOf(pageSize)), null, null, params,
				new AsyncHttpResponseHandler() {
					@Override
					public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
						Log.e("json", new String(arg2));
						dialog.dismiss();
						bean = new Gson().fromJson(new String(arg2),
								CardBean.class);
						mList = bean.data;
						if (isFirst) {
							if (mList.size() != 0 && mList != null) {
								if (adapter == null) {
									adapter = new MylositaAdapter(
											LoistcalMyActivity.this, mList);
									listView.setAdapter(adapter);
								}
							} else {
								freigh_listview.setVisibility(View.GONE);
							}
						} else {
							if (isRefresh && !isPull) {
								adapter.setData(mList);
								adapter.notifyDataSetChanged();
								freigh_listview.onRefreshComplete();
							} else if (!isRefresh && isPull) {
								num = mList.size();
								adapter.addData(mList);
								adapter.notifyDataSetChanged();
								freigh_listview.onRefreshComplete();
							}
						}

					}

					@Override
					public void onFailure(int arg0, Header[] arg1, byte[] arg2,
							Throwable arg3) {
						dialog.dismiss();
						ToastUtil.shortToast(LoistcalMyActivity.this, "网络请求加载失败");

					}
				});

	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public void setOnClick() {

		// 下拉刷新与上拉加载
		freigh_listview.setMode(Mode.BOTH);
		freigh_listview.setOnRefreshListener(new OnRefreshListener2() {

			@Override
			public void onPullDownToRefresh(PullToRefreshBase refreshView) {
				getHttprequst(false, true, 1, false);
			}

			@Override
			public void onPullUpToRefresh(PullToRefreshBase refreshView) {
				pageNo++;
				getHttprequst(false, false, pageNo, true);
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
		freigh_listview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				Data data = mList.get(arg2 - 1);
				Intent intent = new Intent();
				Log.e("bb", "bbbbb");
				if (!data.ifUsed && gift == true) {
					if (data.money != 0 && data.userCouponId != 0) {
						intent.putExtra("money", data.money);
						intent.putExtra("userCouponId", data.userCouponId);
						Log.e("vvv", "ggggg");
						setResult(RESULT_OK, intent);
						finish();
					}
				}

			}
		});
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				// TODO Auto-generated method stub
				
			}
		});
	}

	private boolean gift;

	@Override
	public void getData() {
		gift = getIntent().getBooleanExtra("gift", false);
	}

}
