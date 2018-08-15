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
import com.hex.express.iwant.adapters.BaseListAdapter;
import com.hex.express.iwant.adapters.DrawCardAdapter;
import com.hex.express.iwant.adapters.SendOwnerAdapter;
import com.hex.express.iwant.adapters.BaseListAdapter.ViewHolder;
import com.hex.express.iwant.bean.BaseBean;
import com.hex.express.iwant.bean.DownSpecialBean;
import com.hex.express.iwant.bean.DrawCardBean;
import com.hex.express.iwant.bean.DrawCardBean.Data;
import com.hex.express.iwant.constance.MCUrl;
import com.hex.express.iwant.constance.PreferenceConstants;
import com.hex.express.iwant.http.AsyncHttpUtils;
import com.hex.express.iwant.http.UrlMap;
import com.hex.express.iwant.utils.MyBitmapUtils;
import com.hex.express.iwant.utils.PreferencesUtils;
import com.hex.express.iwant.utils.ToastUtil;
import com.loopj.android.http.AsyncHttpResponseHandler;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ImageView;
import butterknife.Bind;
import butterknife.ButterKnife;

public class NewDrawCardActivity extends BaseActivity{

	@Bind(R.id.view_load_fail)
	LinearLayout view_load_fail;
	@Bind(R.id.listview)
	PullToRefreshListView ptrlv_card;
	@Bind(R.id.null_message)
	View view_null_message;
	private ListView listView;
	@Bind(R.id.btn_Right)
	TextView btn_Right;
	@Bind(R.id.btn_Left)
	ImageView btn_Left;
	@Bind(R.id.webview)
	ImageView webView;
	
	
	
	protected int pageSize = 10;// 表示一页展示多少列
	protected int pageNo = 1;
	private DrawCardBean bean;
	private List<DrawCardBean.Data> mList;
	private List<DrawCardBean.Data> mList2;
	DrawCardAdapter adapter;
	private int num;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_newdrawcard);
		ButterKnife.bind(this);
		initView();
		initData();
		getData();
		setOnClick();
//		showWebview();
		if (!"".equals(getIntent().getStringExtra("gotoUrl"))) {
			new MyBitmapUtils().display(webView, getIntent().getStringExtra("gotoUrl"));
		}
	}
	@Override
	public void onWeightClick(View v) {
		// TODO Auto-generated method stub
		
	}
//	private void showWebview() {
//	String	url = getIntent().getStringExtra("gotoUrl");
////		url = "http://www.efamax.com/mobile/recharge.html";
//		webView.loadUrl(url);
//		webView.setBackgroundColor(2);
//		webView.setWebViewClient(new WebViewClient() {
//			@Override
//			public boolean shouldOverrideUrlLoading(WebView view, String url) {
//				// TODO Auto-generated method stub
//				view.loadUrl(url);
//				return true;
//
//			}
//		});
//
//	}
	@Override
	public void initView() {
		// TODO Auto-generated method stub
		mList = new ArrayList<DrawCardBean.Data>();
		mList2 = new ArrayList<DrawCardBean.Data>();
		listView = ptrlv_card.getRefreshableView();
		getHttpRequst(true, false, 1, false);
	}

	@Override
	public void initData() {
		// TODO Auto-generated method stub
		btn_Left.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				finish();
			}
		});
		btn_Right.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intent=new Intent();
				intent.putExtra("url", "http://www.efamax.com/mobile/explain/couponExplain.html");
				intent.setClass(NewDrawCardActivity.this, HAdvertActivity.class);
				startActivity(intent);
				
			}
		});
	}

	@Override
	public void setOnClick() {
		// TODO Auto-generated method stub
		
	}

	/**
	 * 获取网络数据  
	 */
	@SuppressWarnings("unchecked")
	private void getHttpRequst(final boolean isFirst, final boolean isRefresh, int pageNo, final boolean isPull) {
		String url = UrlMap.getUrl(MCUrl.getReceiveList, "userId",
				String.valueOf(PreferencesUtils.getInt(NewDrawCardActivity.this, PreferenceConstants.UID)));
		dialog.show();
		AsyncHttpUtils.doSimGet(url, new AsyncHttpResponseHandler() {
			@Override
			public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
				Log.e("sdds ", new String(arg2));
				dialog.dismiss();
				bean = new Gson().fromJson(new String(arg2), DrawCardBean.class);
				mList2.clear();
				mList = bean.data;
				if (isFirst) {
					if (mList.size() != 0 && mList != null) {
						if (adapter == null) {
							adapter = new DrawCardAdapter(NewDrawCardActivity.this, mList);
							listView.setAdapter(adapter);
							view_null_message.setVisibility(View.GONE);
						}
					} else {
						Log.e("111sdds ", "111111111");
//						ToastUtil.shortToast(getActivity(), "暂无数据");
						view_null_message.setVisibility(View.VISIBLE);
					}
				} else {
					if (isRefresh && !isPull) {
						if (mList.size() != 0 && mList != null) {
						mList2.clear();
						mList2.addAll(mList);
						adapter.setData(mList2);
						adapter.notifyDataSetChanged();
						ptrlv_card.onRefreshComplete();
						view_null_message.setVisibility(View.GONE);
						}else {
							view_null_message.setVisibility(View.VISIBLE);
						}
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
				ptrlv_card.setVisibility(View.GONE);
				dialog.dismiss();
			}
		});
	}


	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void getData() {
		ptrlv_card.setMode(Mode.BOTH);
		ptrlv_card.setOnRefreshListener(new OnRefreshListener2() {

			@Override
			public void onPullDownToRefresh(PullToRefreshBase refreshView) {
//				getHttpOwnerRequst(true, false, 1, false, sortType);
				getHttpRequst(false, true, 1, false);
			}

			@Override
			public void onPullUpToRefresh(PullToRefreshBase refreshView) {
				pageNo++;
				getHttpRequst(false, false, pageNo, true);
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
		
		
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
				// TODO Auto-generated method stub

				Data data = (Data) adapter.list.get(position - 1);
				// 7-已评价 8 用户主动取消订单
//				String status = data.status;// 状态；
//				int recId = data.recId;// 状态；
//				Log.e("json", data.driverId);
				}
		});
	}
	public class DrawCardAdapter extends BaseListAdapter{

		public DrawCardAdapter(Context context, List list) {
			super(context, list);
			// TODO Auto-generated constructor stub
		}


		@Override
		public ViewHolder onCreateViewHolder(View itemView) {
			return new OwnerViewHolder(itemView);
		}

		@Override
		public int getLayoutResource() {
			return R.layout.item_newdrawcard;//item_newdrawcard

		}

		class OwnerViewHolder extends ViewHolder {

			public OwnerViewHolder(View itemView) {
				super(itemView);
			}

			@Bind(R.id.img_title)
			TextView img_title;
			@Bind(R.id.textView1)
			TextView textView1;
			@Bind(R.id.img_left)
			TextView img_left;
			@Bind(R.id.ll_a)
			RelativeLayout ll_a;

			
			private DrawCardBean bean;

			@Override
			public void setData(final int position) {
				super.setData(position);
				bean = new DrawCardBean();
				bean.data = list;
				Log.e("BEAM", bean.data.get(position).toString());
//				img_title.setText(bean.getData().get(position).couponName);//money
				img_title.setText(bean.getData().get(position).money);//money
				textView1.setText(bean.getData().get(position).couponFrom);
				img_left.setText("领取*"+bean.getData().get(position).couponCount);
				if ("0".equals(bean.getData().get(position).couponCount)) {
					img_left.setText("领取*0");
//					ll_a.setBackgroundDrawable(null);
//					ll_a.setBackgroundResource(R.drawable.newdraw_bgs);
				}
//			}
				img_left.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View arg0) {
						// TODO Auto-generated method stub
						AsyncHttpUtils.doGet(UrlMap.getTwo(MCUrl.RecriveCoupon, "userId", String
								.valueOf(PreferencesUtils.getInt(context,
										PreferenceConstants.UID)), "conditionId", String
								.valueOf(bean.getData().get(position).conditionId)), null,
								null, null, new AsyncHttpResponseHandler() {
									@Override
									public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
										Log.e("json", "*************************************"
												+ new String(arg2));
								BaseBean		drabsean = new Gson().fromJson(new String(arg2),
										BaseBean.class);
								if (drabsean.getErrCode()==0) {
									ToastUtil.shortToast(context, drabsean.getMessage());
									getHttpRequst(false, true, 1, false);
								}
								ToastUtil.shortToast(context, drabsean.getMessage());
									}
									@Override
									public void onFailure(int arg0, Header[] arg1, byte[] arg2,
											Throwable arg3) {
										// TODO Auto-generated method stub

									}
								});
					}
				});
			}
		}

	}

}
