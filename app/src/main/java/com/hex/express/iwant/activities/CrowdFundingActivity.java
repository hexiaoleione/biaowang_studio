package com.hex.express.iwant.activities;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.Header;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import butterknife.Bind;
import butterknife.ButterKnife;

import com.google.gson.Gson;
import com.handmark.pulltorefresh.library.ILoadingLayout;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.hex.express.iwant.R;
import com.hex.express.iwant.adapters.CrowdFundingAdapter;
import com.hex.express.iwant.bean.CrowdNewBean;
import com.hex.express.iwant.bean.CrowdfundingBean;
import com.hex.express.iwant.bean.PublicCourierBean;
import com.hex.express.iwant.constance.MCUrl;
import com.hex.express.iwant.constance.PreferenceConstants;
import com.hex.express.iwant.http.AsyncHttpUtils;
import com.hex.express.iwant.http.UrlMap;
import com.hex.express.iwant.utils.PreferencesUtils;
import com.hex.express.iwant.utils.ToastUtil;
import com.hex.express.iwant.views.TitleBarView;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

/**
 * 【我要融资】股东界面
 * 
 * @author SCHT-50
 * 
 */
public class CrowdFundingActivity extends BaseActivity {
	@Bind(R.id.money_detial)
	TextView money_detial;
	@Bind(R.id.percent_detial)
	TextView percent_detial;
	@Bind(R.id.money_Mydetial)
	TextView money_Mydetial;
	@Bind(R.id.percent_Mydetial)
	TextView percent_Mydetial;
	@Bind(R.id.tbv_show)
	TitleBarView tbv_show;
	@Bind(R.id.ptrlv_card)
	PullToRefreshListView ptrlv_card;
	@Bind(R.id.btn_submit)
	Button btn_submit;
	@Bind(R.id.btn_sure)
	Button btn_sure;
	private ListView listView;
	protected int pageSize = 10;// 表示一页展示多少列
	protected int pageNo = 1;
	private List<CrowdNewBean.Data> mList;
	private CrowdFundingAdapter adapter;
	public CrowdNewBean bean;
	@Bind(R.id.null_message)
	View view_null_message;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_crowd_funding);
		ButterKnife.bind(CrowdFundingActivity.this);
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

	/**
	 * 获取总的目标金额
	 */
	private void getrequstFunding() {
		RequestParams params = new RequestParams();
		Log.e("json",
				MCUrl.CROWDFUNDING
						+ "/"
						+ String.valueOf(PreferencesUtils.getInt(
								getApplicationContext(),
								PreferenceConstants.UID)));
		AsyncHttpUtils.doGet(
				MCUrl.CROWDFUNDING
						+ "/"
						+ String.valueOf(PreferencesUtils.getInt(
								getApplicationContext(),
								PreferenceConstants.UID)), null, null, params,
				new AsyncHttpResponseHandler() {

					@Override
					public void onFailure(int arg0, Header[] arg1, byte[] arg2,
							Throwable arg3) {
						ToastUtil.shortToast(CrowdFundingActivity.this,
								bean.getMessage());
					}

					@Override
					public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
						// TODO Auto-generated method stub
						Log.e("$$$$$$$$$$$$$$", new String(arg2));
						CrowdfundingBean bean = new Gson().fromJson(new String(
								arg2), CrowdfundingBean.class);
						DecimalFormat df = new DecimalFormat("######0.0");
						DecimalFormat dt = new DecimalFormat("######0.00");
						DecimalFormat dd = new DecimalFormat("######0");
						money_detial.setText(dd.format(bean.data.get(0).targetTotalMoney));
						money_Mydetial.setText(dd.format(bean.data.get(0).userFundingMoney));
						double targetTotalPercent = bean.data.get(0).targetTotalPercent;
						double userFundingPercent = bean.data.get(0).userFundingPercent;
						if (targetTotalPercent * 100 < 1) {
							if (String.valueOf(userFundingPercent).length() > 5) {
								percent_Mydetial.setText(dt
										.format(userFundingPercent * 100) + "%");
							} else {
								percent_detial.setText(df
										.format(targetTotalPercent * 100) + "%");
							}
						} else {
							percent_detial.setText(dd
									.format(targetTotalPercent * 100) + "%");
						}
						if (userFundingPercent * 100 < 1) {
							if (String.valueOf(userFundingPercent).length() > 5) {
								percent_Mydetial.setText(dt
										.format(userFundingPercent * 100) + "%");
							} else {

								Log.e("lllll++++++++++KKKKKKK",
										bean.data.get(0).userFundingPercent
												+ "");
								percent_Mydetial.setText(df
										.format(userFundingPercent * 100) + "%");
							}
						} else {
							percent_Mydetial.setText(dd
									.format(userFundingPercent * 100) + "%");
						}

					}

				});
	}

	@Override
	public void initData() {
		tbv_show.setTitleText("镖王融资");
		listView = ptrlv_card.getRefreshableView();
		mList = new ArrayList<CrowdNewBean.Data>();
		getrequstFunding();
		dialog.show();
		getHttprequst(true, false, 1, false);
	}

	/**
	 * 获取网络数据
	 */
	private void getHttprequst(final boolean isFirst, final boolean isRefresh,
			int pageNo, final boolean isPull) {
		RequestParams params = new RequestParams();
		Log.e("jsontttttt", MCUrl.COMPANYNEWS);
		AsyncHttpUtils.doGet(MCUrl.COMPANYNEWS, null, null, params,
				new AsyncHttpResponseHandler() {
					@Override
					public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
						Log.e("jsouuuuuun", new String(arg2));
						dialog.dismiss();
						bean = new Gson().fromJson(new String(arg2),
								CrowdNewBean.class);
						mList = bean.data;
						if (isFirst) {
							if (mList.size() != 0 && mList != null) {
								if (adapter == null) {
									adapter = new CrowdFundingAdapter(
											CrowdFundingActivity.this, mList);
									listView.setAdapter(adapter);
								}
							} else {
								view_null_message.setVisibility(View.VISIBLE);
								ptrlv_card.setVisibility(View.GONE);
							}
						} 
//						else {
//							if (isRefresh && !isPull) {
//								adapter.setData(mList);
//								adapter.notifyDataSetChanged();
//								ptrlv_card.onRefreshComplete();
//							} else if (!isRefresh && isPull) {
//								num = mList.size();
//								adapter.addData(mList);
//								adapter.notifyDataSetChanged();
//								ptrlv_card.onRefreshComplete();
//							}
//						}

					}

					@Override
					public void onFailure(int arg0, Header[] arg1, byte[] arg2,
							Throwable arg3) {
						dialog.dismiss();
						ToastUtil.shortToast(CrowdFundingActivity.this,
								"网络请求加载失败");

					}
				});

	}

	private int num;

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public void setOnClick() {
//		// 下拉刷新与上拉加载
//		ptrlv_card.setMode(Mode.BOTH);
//		ptrlv_card.setOnRefreshListener(new OnRefreshListener2() {
//
//			@Override
//			public void onPullDownToRefresh(PullToRefreshBase refreshView) {
//				getHttprequst(false, true, 1, false);
//			}
//
//			@Override
//			public void onPullUpToRefresh(PullToRefreshBase refreshView) {
//				pageNo++;
//				getHttprequst(false, false, pageNo, true);
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
//
//		ptrlv_card.setOnItemClickListener(new OnItemClickListener() {
//
//			@Override
//			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
//					long arg3) {
//				Intent intent = new Intent();
//				startActivity(intent.setClass(CrowdFundingActivity.this,
//						CrowdWelcomeActivity.class));
//
//			}
//		});
		btn_submit.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				startActivity(intent.setClass(CrowdFundingActivity.this,
						CrowdConsultActivity.class));

			}
		});
		btn_sure.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				startActivity(intent.setClass(CrowdFundingActivity.this,
						CrowdConsultActivity.class));

			}
		});

	}

	@Override
	public void getData() {
		// TODO Auto-generated method stub

	}

}
