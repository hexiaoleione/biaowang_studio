package com.hex.express.iwant.activities;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.Header;

import com.google.gson.Gson;
import com.handmark.pulltorefresh.library.ILoadingLayout;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.hex.express.iwant.R;
import com.hex.express.iwant.iWantApplication;
import com.hex.express.iwant.adapters.PickCenterAdapter;
import com.hex.express.iwant.bean.PickCenterBean;
import com.hex.express.iwant.constance.MCUrl;
import com.hex.express.iwant.constance.PreferenceConstants;
import com.hex.express.iwant.http.AsyncHttpUtils;
import com.hex.express.iwant.http.UrlMap;
import com.hex.express.iwant.utils.PreferencesUtils;
import com.hex.express.iwant.views.TitleBarView;
import com.loopj.android.http.AsyncHttpResponseHandler;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import butterknife.Bind;
import butterknife.ButterKnife;
/**
 * 抢单大厅
 * @author Eric
 *
 */
public class PickCenterActivity extends BaseActivity {
	@Bind(R.id.tbv_show)
	TitleBarView tbv_show;
	@Bind(R.id.ptrlv_card)
	PullToRefreshListView ptrlv_card;
	@Bind(R.id.null_message)
	View view_null_message;
	@Bind(R.id.view_load_fail)
	LinearLayout view_load_fail;
	ListView listview;
	protected int pageSize = 10;// 表示一页展示多少列
	private int pageNo = 1;// 请求页码
	private int escorePageNo = 1;// 请求页码
	private PickCenterBean bean;
	private List<PickCenterBean.Data> mList;
	private List<PickCenterBean.Data> mList2;
	PickCenterAdapter adapter;
	private double latitude;
	private double longitude;
	private String cityCode;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_pick_center);//TODO 设置layout
		iWantApplication.getInstance().addActivity(this);
		ButterKnife.bind(PickCenterActivity.this);
		initView();
		initData();
		setOnClick();
	}

	@Override
	public void onWeightClick(View v) {

	}

	@Override
	public void initView() {
		mList = new ArrayList<PickCenterBean.Data>();
		mList2 = new ArrayList<PickCenterBean.Data>();
		handler = new Handler(){
			@Override
			public void handleMessage(Message msg) {
				// TODO Auto-generated method stub
				super.handleMessage(msg);
				switch (msg.what) {
				case 1:
					finish();
					
					break;

				default:
					break;
				}
			}
		};
	}

	@Override
	public void initData() {
		latitude = Double.parseDouble(PreferencesUtils.getString(getApplicationContext(), PreferenceConstants.LATITUDE));
		longitude =Double.parseDouble(PreferencesUtils.getString(getApplicationContext(), PreferenceConstants.LONGITUDE));
		cityCode = PreferencesUtils.getString(getApplicationContext(), PreferenceConstants.CITYCODE);
		tbv_show.setTitleText("抢单大厅");
		listview=ptrlv_card.getRefreshableView();
		getHttpRequst(true,false ,1,false);
	}
	/**
	 * 获取我是镖师发的行程记录
	 * 
	 * @param isFirst
	 * @param isRefresh
	 * @param pageNo
	 * @param isPull
	 * @param sortType
	 */
	@SuppressWarnings("unchecked")
	private void getHttpRequst(final boolean isFirst, final boolean isRefresh, int pageNo, final boolean isPull
		) {
		String url = UrlMap.getfive(MCUrl.EXPRESSLIST, "latitude",
				String.valueOf(latitude),"longitude",String.valueOf(longitude),"cityCode",String.valueOf(cityCode), "pageNo",
				String.valueOf(pageNo), "pageSize", String.valueOf(pageSize));
		Log.e("mgsdkl", url);
		AsyncHttpUtils.doSimGet(url, new AsyncHttpResponseHandler() {
			@Override
			public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
				dialog.dismiss();
				
				bean = new Gson().fromJson(new String(arg2), PickCenterBean.class);
				mList2.clear();
				mList = bean.data;
				if (isFirst) {
					if (mList.size() != 0 && mList != null) {
							adapter = new PickCenterAdapter(PickCenterActivity.this, mList,handler);
							listview.setAdapter(adapter);
							adapter.notifyDataSetChanged();
						
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
			public void onFailure(int arg0, Header[] arg1, byte[] arg2, Throwable arg3) {
				dialog.dismiss();
				view_load_fail.setVisibility(View.VISIBLE);
				ptrlv_card.setVisibility(View.GONE);

			}
		});
	}
	private int num;
	private Handler handler;
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public void setOnClick() {
		// 下拉刷新与上拉加载
		ptrlv_card.setMode(Mode.BOTH);

		ptrlv_card.setOnRefreshListener(new OnRefreshListener2() {

			@SuppressWarnings("rawtypes")
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
					ILoadingLayout proxy = refreshView.getLoadingLayoutProxy(false, true);
					proxy.setPullLabel("没有更多数据了");
					proxy.setRefreshingLabel("没有更多数据了");
					proxy.setReleaseLabel("没有更多数据了");
				}
			}
		});
		listview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {

//				startActivity(new Intent(PickCenterActivity.this, DownWindSpecialActivity.class));

			}
		});
		


	}

	@Override
	public void getData() {
		// TODO Auto-generated method stub
		
	}
}
