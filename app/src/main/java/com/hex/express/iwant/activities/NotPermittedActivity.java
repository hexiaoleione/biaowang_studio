package com.hex.express.iwant.activities;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.gson.Gson;
import com.handmark.pulltorefresh.library.ILoadingLayout;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.hex.express.iwant.R;
import com.hex.express.iwant.iWantApplication;
import com.hex.express.iwant.adapters.NotPermittedAdapter;
import com.hex.express.iwant.adapters.WalletAdapter;
import com.hex.express.iwant.bean.BaseBean;
import com.hex.express.iwant.bean.NotPermitted;
import com.hex.express.iwant.bean.NotPermitted.Data;
import com.hex.express.iwant.bean.NotPermitted;
import com.hex.express.iwant.constance.MCUrl;
import com.hex.express.iwant.constance.PreferenceConstants;
import com.hex.express.iwant.http.AsyncHttpUtils;
import com.hex.express.iwant.http.UrlMap;
import com.hex.express.iwant.newactivity.NotPermitActivity;
import com.hex.express.iwant.utils.PreferencesUtils;
import com.hex.express.iwant.utils.ToastUtil;
import com.hex.express.iwant.views.TitleBarView;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Telephony.Mms;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Button;
import butterknife.Bind;
import butterknife.ButterKnife;

public class NotPermittedActivity extends BaseActivity{

	private TitleBarView tbv_show;
	@Bind(R.id.ptrl_wallet)
	PullToRefreshListView ptrlv_card;
	@Bind(R.id.null_message)
	View view_null_message;
	private ListView listView;
	@Bind(R.id.button1)
	Button button1;
	@Bind(R.id.dannumber)
	TextView dannumber;
	private List<NotPermitted.Data> mList2;
	private List<NotPermitted.Data> mList;
	protected int pageSize = 10;// 表示一页展示多少列
	protected int pageNo = 1;
	private NotPermittedAdapter adapter;
	private NotPermitted walletBean;
	int mnumber,mnumb;
	int  Mms;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.notpermitted_activity);
		iWantApplication.getInstance().addActivity(this);
		ButterKnife.bind(NotPermittedActivity.this);
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
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
//		getHttprequst(true,false, 1, false);
//		getHttprequsts(true,false, 1, false);
	}

	@Override
	public void initView() {
		// TODO Auto-generated method stub
		tbv_show = (TitleBarView) findViewById(R.id.tbv_show);
		this.tbv_show.setTitleText("未缴费单件");
		// 用于展示数据的listview
		listView = ptrlv_card.getRefreshableView();
		mList2=new ArrayList<NotPermitted.Data>();
		mList=new ArrayList<NotPermitted.Data>();
		getHttprequst(true,false, 1, false);
		getHttprequsts(true,false, 1, false);
	}
	/**
	 * 异步请求数据
	 */
	private void getHttprequst(final boolean isFirst,final boolean isRefresh, int pageNo,
			final boolean isPull) {
		RequestParams params = new RequestParams();
		Log.e("json", UrlMap.getThreeUrl(MCUrl.WaitPayList, "userId", String
				.valueOf(PreferencesUtils.getInt(getApplicationContext(),
						PreferenceConstants.UID)), "pageNo", String
				.valueOf(pageNo), "pageSize", String.valueOf(pageSize)));
		mList.clear();
		AsyncHttpUtils.doGet(UrlMap.getUrl(MCUrl.WaitPayList, "userId", String
				.valueOf(PreferencesUtils.getInt(getApplicationContext(),
						PreferenceConstants.UID))), null, null, params,
				new AsyncHttpResponseHandler() {

					@Override
					public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
						// TODO Auto-generated method stub
						mList2.clear();
						dialog.dismiss();
						Log.e("1111sjson", new String(arg2));
						walletBean = new Gson().fromJson(new String(arg2),
								NotPermitted.class);
						mList = walletBean.data;
						if (isFirst) {
							if (mList.size() != 0 && mList != null) {
								if (adapter == null) {
									adapter = new NotPermittedAdapter(
											NotPermittedActivity.this, mList);
									listView.setAdapter(adapter);
								}
							} else {
								view_null_message.setVisibility(View.VISIBLE);
								ptrlv_card.setVisibility(View.GONE);
							}
						} else {
							
							if (isRefresh && !isPull) {
								if (mList.size() != 0 && mList != null) {
									adapter = new NotPermittedAdapter(
											NotPermittedActivity.this, mList);
									listView.setAdapter(adapter);
									adapter.notifyDataSetChanged();
									ptrlv_card.onRefreshComplete();
								}
							} else if (!isRefresh && isPull) {
								Mms = mList.size();
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
						ToastUtil.shortToast(NotPermittedActivity.this, "网络请求失败");

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
				listView.deferNotifyDataSetChanged();
//				getrequstBalance();
			}

			@SuppressWarnings("deprecation")
			@Override
			public void onPullUpToRefresh(PullToRefreshBase refreshView) {
//				getrequstBalance();
				pageNo++;
				
				if (num < Mms && pageNo > 2) {
					getHttprequst(false,false, pageNo, true);
					Log.e("dd", num + "");
					// refreshView.onRefreshComplete();
					ILoadingLayout proxy = refreshView.getLoadingLayoutProxy(
							false, true);
					proxy.setPullLabel("没有更多数据了");
					proxy.setRefreshingLabel("没有更多数据了");
					proxy.setReleaseLabel("没有更多数据了");
				}else {
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
			public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
				// TODO 此处实现item点击获取数据的逻辑
				Data data = (Data) adapter.list.get(position-1);
				Intent intent = new Intent(NotPermittedActivity.this, NotPermitActivity.class);
			 		intent.putExtra("quoRecId", data.quoRecId);//物流单号
			 		startActivity(intent);
			}
		});
	}
	/**
	 * 异步请求数据
	 */
	private void getHttprequsts(final boolean isFirst,final boolean isRefresh, int pageNo,
			final boolean isPull) {
		RequestParams params = new RequestParams();
		Log.e("json", UrlMap.getThreeUrl(MCUrl.OnePay, "userId", String
				.valueOf(PreferencesUtils.getInt(getApplicationContext(),
						PreferenceConstants.UID)), "pageNo", String
				.valueOf(pageNo), "pageSize", String.valueOf(pageSize)));

		AsyncHttpUtils.doGet(UrlMap.getUrl(MCUrl.OnePay, "userId", String
				.valueOf(PreferencesUtils.getInt(getApplicationContext(),
						PreferenceConstants.UID))), null, null, params,
				new AsyncHttpResponseHandler() {

					@Override
					public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
						// TODO Auto-generated method stub
						Log.e("1111json", new String(arg2));
						dialog.dismiss();
						
						String string=new String(arg2);
						JSONObject jObject;
						try {
							jObject = new JSONObject(string);
							JSONArray jArray=jObject.optJSONArray("data");
							jArray.opt(0);
							mnumb=(Integer) jArray.opt(0);
							mnumber=(Integer) jArray.opt(1);
							dannumber.setText(""+jArray.opt(0)+"单");
							Log.e("122222s   ",  ""+jArray.opt(0));
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
//						walletBean = new Gson().fromJson(new String(arg2),
//								NotPermitted.class);
//						mList = walletBean.data;
					}
					@Override
					public void onFailure(int arg0, Header[] arg1, byte[] arg2,
							Throwable arg3) {
						dialog.dismiss();
						ToastUtil.shortToast(NotPermittedActivity.this, "网络请求失败");

					}
				});

	}
	@Override
	public void initData() {
		// TODO Auto-generated method stub
		button1.setOnClickListener( new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if (mnumb!=0) {
				
				Builder ad = new Builder(NotPermittedActivity.this);
				ad.setTitle("温馨提示");
				ad.setMessage("是否确定余额支付："+mnumber+"元");
				ad.setNegativeButton("确认", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface arg0, int arg1) {
						getMessages();
					}
				});
				ad.setPositiveButton("取消", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface arg0, int arg1) {
						arg0.dismiss();
						
					}
				});
				ad.create().show();
				
				}
			}
		});
		
	}

	private void getMessages(){
		RequestParams params = new RequestParams();

		AsyncHttpUtils.doGet(UrlMap.getThreeUrl(MCUrl.OneKeyPay, "userId", String
				.valueOf(PreferencesUtils.getInt(getApplicationContext(),
						PreferenceConstants.UID)), "money", ""+mnumber, "count", ""+mnumb), null, null, params,
				new AsyncHttpResponseHandler() {

					@Override
					public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
						// TODO Auto-generated method stub
						Log.e("1111json", new String(arg2));
						dialog.dismiss();
						BaseBean sBean = new Gson().fromJson(new String(arg2),
								BaseBean.class);
						if (sBean.getErrCode()==0) {
							ToastUtil.shortToast(NotPermittedActivity.this, sBean.getMessage());
							finish();
						}else {
							finish();
							ToastUtil.shortToast(NotPermittedActivity.this, sBean.getMessage());
						}
					}
					@Override
					public void onFailure(int arg0, Header[] arg1, byte[] arg2,
							Throwable arg3) {
						dialog.dismiss();
						ToastUtil.shortToast(NotPermittedActivity.this, "网络请求失败");

					}
				});

	}

	@Override
	public void getData() {
		// TODO Auto-generated method stub
		
	}

}
