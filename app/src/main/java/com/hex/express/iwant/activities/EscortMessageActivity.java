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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

import com.google.gson.Gson;
import com.handmark.pulltorefresh.library.ILoadingLayout;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.hex.express.iwant.R;
import com.hex.express.iwant.adapters.EscortMessageAdapter;
import com.hex.express.iwant.bean.EscortInfoBean;
import com.hex.express.iwant.bean.EvaluationOfEscortBean;
import com.hex.express.iwant.constance.MCUrl;
import com.hex.express.iwant.constance.PreferenceConstants;
import com.hex.express.iwant.http.AsyncHttpUtils;
import com.hex.express.iwant.http.UrlMap;
import com.hex.express.iwant.utils.AppUtils;
import com.hex.express.iwant.utils.MyBitmapUtils;
import com.hex.express.iwant.utils.PreferencesUtils;
import com.hex.express.iwant.utils.ToastUtil;
import com.hex.express.iwant.views.TitleBarView;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

/**
 * 【镖师信息】 镖师的信息和镖师的用户评价
 * 
 * @author SCHT-50
 * 
 */
public class EscortMessageActivity extends BaseActivity {
	@Bind(R.id.tbv_show)
	// 标题
	TitleBarView tbv_show;
	@Bind(R.id.img_header)
	// 头像
	ImageView img_header;
	@Bind(R.id.tv_driverName)
	// 镖师名字
	TextView tv_driverName;
	@Bind(R.id.img_sex)
	// 性别图标
	ImageView img_sex;
	@Bind(R.id.tv_num)
	// 接镖次数
	TextView tv_num;
	@Bind(R.id.rab_couriersscore)
	// 评分星星
	RatingBar rab_couriersscore;
	@Bind(R.id.ptrl_wallet)
	PullToRefreshListView ptrlv_card;
	@Bind(R.id.null_message)
	View view_null_message;
	private ListView listView;
	protected int pageSize = 10;// 表示一页展示多少列
	protected int pageNo = 1;
	private List<EvaluationOfEscortBean.Data> mList;
	private EscortMessageAdapter adapter;
	public EscortInfoBean eBean;
	public EvaluationOfEscortBean bean;
	private String driverId;// 镖师的Id

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_escort_message);
		ButterKnife.bind(EscortMessageActivity.this);
		initView();
		initData();
	}

	@OnClick({ R.id.img_phone })
	public void MyOnClick(View view) {
		switch (view.getId()) {
		case R.id.img_phone:// 拨打电话
			AppUtils.intentDial(EscortMessageActivity.this, eBean.data.get(0).driverMobile);
			break;

		default:
			break;
		}
	}

	@Override
	public void onWeightClick(View v) {
		// TODO Auto-generated method stub

	}

	@Override
	public void initView() {

		// 接收到从上一个界面发来的镖师I
		driverId = getIntent().getStringExtra("driverId");
		Log.e("driverId", driverId);
		getData();
		tbv_show.setTitleText("镖师信息");
		listView = ptrlv_card.getRefreshableView();

	}

	@Override
	public void initData() {
		mList = new ArrayList<EvaluationOfEscortBean.Data>();

		getHttprequst(true, false, 1, false);

	}

	/**
	 * 获取镖师的评价信息
	 */
	private void getHttprequst(final boolean isFirst, final boolean isRefresh, int pageNo, final boolean isPull) {
		RequestParams params = new RequestParams();
		dialog.show();
		Log.e("urlopmg", UrlMap.getThreeUrl(MCUrl.DRIVEREVALUATE, "driverId", driverId, "pageNo", pageNo + "",
				"pageSize", String.valueOf(pageSize)));
		AsyncHttpUtils.doGet(UrlMap.getThreeUrl(MCUrl.DRIVEREVALUATE, "driverId", driverId, "pageNo", pageNo + "",
				"pageSize", String.valueOf(pageSize)), null, null, params, new AsyncHttpResponseHandler() {
					@Override
					public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
						Log.e("json", new String(arg2));
						dialog.dismiss();
						bean = new Gson().fromJson(new String(arg2), EvaluationOfEscortBean.class);
						mList = bean.data;
						if (isFirst) {
							if (bean.getErrCode() == 0) {
								if (mList.size() != 0 && mList != null) {
									if (adapter == null) {
										adapter = new EscortMessageAdapter(EscortMessageActivity.this, mList);
										listView.setAdapter(adapter);
									}
								} else {
									view_null_message.setVisibility(View.VISIBLE);
									ptrlv_card.setVisibility(View.GONE);
								}
							} else {
								ToastUtil.shortToast(EscortMessageActivity.this, bean.getMessage());
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
					public void onFailure(int arg0, Header[] arg1, byte[] arg2, Throwable arg3) {
						dialog.dismiss();
						ToastUtil.shortToast(EscortMessageActivity.this, "网络请求加载失败");

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
				getHttprequst(false, true, 1, false);
			}

			@Override
			public void onPullUpToRefresh(PullToRefreshBase refreshView) {
				pageNo++;
				getHttprequst(false, false, pageNo, true);
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
		ptrlv_card.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {

			}
		});

	}

	/**
	 * 表77 获取镖师信息 /driver/driverDetail
	 */
	@Override
	public void getData() {
		Log.e("-----------------", "-----" + UrlMap.getUrl(MCUrl.DRIVERDETAIL, "driverId", driverId));
		// GET方法获取数据(镖师的基本信息)
		AsyncHttpUtils.doSimGet(UrlMap.getUrl(MCUrl.DRIVERDETAIL, "driverId", driverId),
				new AsyncHttpResponseHandler() {
					@Override
					public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
						// TODO Auto-generated method stub
						Log.e("xinklsd", new String(arg2));
						eBean = new Gson().fromJson(new String(arg2), EscortInfoBean.class);
						if (eBean.getErrCode() == 0) {// 如果获取成功
							if (!eBean.data.equals("") && eBean.data.size() > 0) {
								tv_driverName.setText(eBean.getData().get(0).userName);// 设置镖师姓名
								if ("男".equals(eBean.getData().get(0).sex)) {
									img_sex.setImageResource(R.drawable.escort_man);// 设置性别图片【男】
								} else {
									img_sex.setImageResource(R.drawable.escort_woment);// 设置性别图片【女】
								}
								tv_num.setText(eBean.getData().get(0).driverRouteCount);// 设置接单次数
								rab_couriersscore.setRating(eBean.getData().get(0).synthesisEvaluate);// 设置评分
								if (!eBean.data.get(0).userHeadPath.equals("")) {
									loader.displayImage(eBean.data.get(0).userHeadPath, img_header, options);
								} else {
									img_header.setBackgroundResource(R.drawable.moren);
								}
							} else {
								ToastUtil.shortToast(getApplicationContext(), eBean.getMessage());
							}

						} else if (eBean.getErrCode() == -1) {
							ToastUtil.shortToast(getApplicationContext(), eBean.getMessage());
						}
					}

					@Override
					public void onFailure(int arg0, Header[] arg1, byte[] arg2, Throwable arg3) {
						Log.e("ccc", arg0 + "");
					}
				});
	}
}
