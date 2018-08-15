package com.hex.express.iwant.fragments;

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
import com.hex.express.iwant.activities.DownDetialTrackActivity;
import com.hex.express.iwant.activities.DownOwnerDetialsActivity;
import com.hex.express.iwant.activities.MessageActivity;
import com.hex.express.iwant.activities.MyDownwindActivity;
import com.hex.express.iwant.adapters.MessageAdapter;
import com.hex.express.iwant.adapters.SendOwnerAdapter;
import com.hex.express.iwant.bean.BaseBean;
import com.hex.express.iwant.bean.DownSpecialBean;
import com.hex.express.iwant.bean.MessageBean;
import com.hex.express.iwant.bean.DownSpecialBean.Data;
import com.hex.express.iwant.constance.MCUrl;
import com.hex.express.iwant.constance.PreferenceConstants;
import com.hex.express.iwant.http.AsyncHttpUtils;
import com.hex.express.iwant.http.UrlMap;
import com.hex.express.iwant.utils.Logger;
import com.hex.express.iwant.utils.PreferencesUtils;
import com.hex.express.iwant.utils.TestMessage;
import com.hex.express.iwant.utils.ToastUtil;
import com.loopj.android.http.AsyncHttpResponseHandler;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;
import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 【我是货主】Fragment 【我的顺风】界面【1-2】
 * 
 * @author SCHT-50
 * 
 */
public class SendOwnerFragment extends BaseItemFragment {
	public View rootView;
	@Bind(R.id.view_load_fail)
	LinearLayout view_load_fail;
	@Bind(R.id.listview)
	PullToRefreshListView ptrlv_card;
	@Bind(R.id.null_message)
	View view_null_message;
	protected int pageSize = 10;// 表示一页展示多少列
	private int pageNo = 1;// 请求页码
	private DownSpecialBean bean;
	private List<Data> mList;
	private List<Data> mList2;
	SendOwnerAdapter adapter;
	private ListView listView;
	private MyDownwindActivity DownActivity;
	  protected static final String ACTION = "com.android.broadcast.RECEIVER_ACTION";  
	   LocalBroadcastManager broadcastManager ;
	   BroadcastReceiver bordcastReceiver;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		rootView = inflater.inflate(R.layout.fragment_sendowner, container, false);
		ButterKnife.bind(this, rootView);
		initData();
		setOnClick();
		return rootView;
	}

	@Override
	public void onWeightClick(View v) {
		// TODO Auto-generated method stub

	}

	@Override
	public void initData() {
		listView = ptrlv_card.getRefreshableView();
		mList = new ArrayList<Data>();
		mList2 = new ArrayList<Data>();
		DownActivity = (MyDownwindActivity) getActivity();
		DownActivity.show();
		getHttpRequst(true, false, 1, false);

	}
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		receiver = new MyReceiver();
		IntentFilter filter=new IntentFilter();
		filter.addAction("cancelOrder");
		getActivity().registerReceiver(receiver, filter);
	}
	class MyReceiver extends BroadcastReceiver{

		@Override
		public void onReceive(Context arg0, Intent arg1) {
			Logger.e("[[[[[[[[[[[[[[[[[", "a");
			getHttpRequst(false, true, 1, false);
			
		}
		
	}
	@Override
	public void onDestroyView() {
		// TODO Auto-generated method stub
		super.onDestroyView();
		getActivity().unregisterReceiver(receiver);
	}
	private int num;
	private MyReceiver receiver;
	@SuppressWarnings("unchecked")
	private void getHttpRequst(final boolean isFirst, final boolean isRefresh, int pageNo, final boolean isPull) {
		String url = UrlMap.getThreeUrl(MCUrl.DOWNGETTASK, "userId",
				String.valueOf(PreferencesUtils.getInt(getActivity(), PreferenceConstants.UID)), "pageNo",
				String.valueOf(pageNo), "pageSize", String.valueOf(pageSize));
		AsyncHttpUtils.doSimGet(url, new AsyncHttpResponseHandler() {
			@Override
			public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
				Log.e("11111111", new String(arg2));
				DownActivity.dissmiss();
				mList2.clear();
//				try {
					bean = new Gson().fromJson(new String(arg2), DownSpecialBean.class);
					mList = bean.data;
//				} catch (Exception e) {
//					// TODO: handle exception
//				}
				
				if (isFirst) {
					if (mList.size() != 0 && mList != null) {
						if (adapter == null) {
							adapter = new SendOwnerAdapter(getActivity(), mList);
							listView.setAdapter(adapter);
						}
					} else {
						view_null_message.setVisibility(View.VISIBLE);
						ptrlv_card.setVisibility(View.GONE);
					}
				} else {
					if (isRefresh && !isPull) {
						if (mList.size() != 0 && mList != null) {
								adapter = new SendOwnerAdapter(getActivity(), mList);
								listView.setAdapter(adapter);
								adapter.notifyDataSetChanged();
								ptrlv_card.onRefreshComplete();
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
				DownActivity.dissmiss();
				view_load_fail.setVisibility(View.VISIBLE);
				ptrlv_card.setVisibility(View.GONE);

			}
		});
	}

	@SuppressWarnings("unchecked")
	@Override
	public void setOnClick() {
		// 下拉刷新与上拉加载
		ptrlv_card.setMode(Mode.BOTH);

		ptrlv_card.setOnRefreshListener(new OnRefreshListener2() {

			@SuppressWarnings("rawtypes")
			@Override
			public void onPullDownToRefresh(PullToRefreshBase refreshView) {
				getHttpRequst(false, true, 1, false);
				listView.deferNotifyDataSetChanged();
			}

			@SuppressWarnings("rawtypes")
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
		listView.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
				// TODO Auto-generated method stub
				
//				Log.e("11111", "111111111111");
				Data data = (Data) adapter.list.get(position - 1);
				// 状态 0-预发布成功(未支付) 1-支付成功(已支付) 2-(已抢单) 3-已取件 4-订单取消 5-成功 6-删除
				// 7-已评价 8 用户主动取消订单
				String status = data.status;// 状态；
				final int recId = data.recId;// 状态；
				if (data.status.equals("4") || data.status.equals("5") || data.status.equals("7") || data.status.equals("8")) {
					AlertDialog.Builder builder = new AlertDialog.Builder(
							getActivity());
					builder.setItems(R.array.thread_menu, new OnClickListener() {
						@Override
						public void onClick(DialogInterface arg0, int arg1) {
							// TODO Auto-generated method stub
							detaMessage(recId);
					}
				});
					builder.show();

				}
				
				return true;
			}
		});
		
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {

				Data data = (Data) adapter.list.get(position - 1);
				// 状态 0-预发布成功(未支付) 1-支付成功(已支付) 2-(已抢单) 3-已取件 4-订单取消 5-成功 6-删除
				// 7-已评价 8 用户主动取消订单
				String status = data.status;// 状态；
				int recId = data.recId;// 状态；
				Log.e("json", data.driverId);
				if (!"0".equals(data.driverId)&&!data.driverId.equals("")) {// 如果已被接单，则打开【货物跟踪】界面；
					Intent intent = new Intent(getActivity(), DownDetialTrackActivity.class);
					intent.putExtra("driverId", data.driverId);
					intent.putExtra("userType", "3");
					intent.putExtra("status", data.status);
					intent.putExtra("ifAgree", data.ifAgree);
					intent.putExtra("recId", String.valueOf(recId));
					intent.putExtra("fromLatitude", data.fromLatitude);
					intent.putExtra("fromLongitude", data.fromLongitude);
					intent.putExtra("toLongitude", data.toLongitude);
					intent.putExtra("toLatitude", data.toLatitude);
					intent.putExtra("matImageUrl", data.matImageUrl);
					intent.putExtra("matName", data.matName);
					intent.putExtra("matWeight", data.matWeight);
					if (!data.length.equals("0") && !data.wide.equals("0")&& !data.high.equals("0")) {
						intent.putExtra("length", data.length+" x "+data.wide+" x "+data.high);
					}else {
						intent.putExtra("length", "0");
					}
					intent.putExtra("recId", data.recId);
					intent.putExtra("publishTime", data.publishTime);
					intent.putExtra("personName", data.personName);
					intent.putExtra("mobile", data.mobile);
					intent.putExtra("address", data.address);
					intent.putExtra("personNameTo", data.personNameTo);
					intent.putExtra("mobileTo", data.mobileTo);
					intent.putExtra("addressTo", data.addressTo);
					intent.putExtra("matRemark", data.matRemark);
					intent.putExtra("money", data.transferMoney);
					intent.putExtra("limitTime", data.limitTime);
					intent.putExtra("finishTime", data.finishTime);
					intent.putExtra("driverId", data.driverId);
					
					
					if (data.status.equals("9")) {
						Intent intent1 = new Intent(getActivity(), DownOwnerDetialsActivity.class);
						intent1.putExtra("recId", data.recId);
						intent1.putExtra("matImageUrl", data.matImageUrl);
						intent1.putExtra("matName", data.matName);
						intent1.putExtra("matWeight", data.matWeight);
						intent1.putExtra("ifAgree", data.ifAgree);
						if (!data.length.equals("0") && !data.wide.equals("0")&& !data.high.equals("0")) {
							intent1.putExtra("length", data.length+" x "+data.wide+" x "+data.high);
						}else {
							intent1.putExtra("length", "0");
						}
						intent1.putExtra("publishTime", data.publishTime);
						intent1.putExtra("personName", data.personName);
						intent1.putExtra("mobile", data.mobile);
						intent1.putExtra("address", data.address);
						intent1.putExtra("personNameTo", data.personNameTo);
						intent1.putExtra("mobileTo", data.mobileTo);
						intent1.putExtra("addressTo", data.addressTo);
						intent1.putExtra("matRemark", data.matRemark);
						intent1.putExtra("money", data.transferMoney);
						intent1.putExtra("status", data.status);
						intent1.putExtra("limitTime", data.limitTime);
						intent1.putExtra("finishTime", data.finishTime);
						startActivity(intent1);
					}else {
						startActivity(intent);// 顺风镖师的Id，镖件的状态；
					}

				}else {
					Intent intent = new Intent(getActivity(), DownOwnerDetialsActivity.class);
					intent.putExtra("recId", data.recId);
					intent.putExtra("matImageUrl", data.matImageUrl);
					intent.putExtra("matName", data.matName);
					intent.putExtra("matWeight", data.matWeight);
					intent.putExtra("ifAgree", data.ifAgree);
					if (!data.length.equals("0") && !data.wide.equals("0")&& !data.high.equals("0")) {
						intent.putExtra("length", data.length+" x "+data.wide+" x "+data.high);
					}else {
						intent.putExtra("length", "0");
					}
					intent.putExtra("publishTime", data.publishTime);
					intent.putExtra("personName", data.personName);
					intent.putExtra("mobile", data.mobile);
					intent.putExtra("address", data.address);
					intent.putExtra("personNameTo", data.personNameTo);
					intent.putExtra("mobileTo", data.mobileTo);
					intent.putExtra("addressTo", data.addressTo);
					intent.putExtra("matRemark", data.matRemark);
					intent.putExtra("money", data.transferMoney);
					intent.putExtra("status", data.status);
					intent.putExtra("limitTime", data.limitTime);
					intent.putExtra("finishTime", data.finishTime);
					startActivity(intent);
					
				}
			}
		});

	}
	/**
	 * 删除消息
	 * @param recId
	 */
	private void detaMessage(int recId) {
		AsyncHttpUtils.doGet(UrlMap.getTwo(MCUrl.DeldeteDow, "recId", ""+recId, "type", "1"), null,
				null, null, new AsyncHttpResponseHandler() {
					@Override
					public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
						Log.e("json", "*************************************"+ new String(arg2));
						
						BaseBean		bean = new Gson().fromJson(new String(arg2),
								BaseBean.class);
						if (bean.getErrCode()!=0) {
							ToastUtil.shortToast(getActivity(), bean.getMessage());
						}else {
							getHttpRequst(false, true, 1, false);
						}
					}
					@Override
					public void onFailure(int arg0, Header[] arg1, byte[] arg2,
							Throwable arg3) {
						// TODO Auto-generated method stub

					}
				});

	}
	@Override
	public void getData() {
		// TODO Auto-generated method stub

	}
	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}
	

	public void onStart() {
        super.onStart();
//       EventBus.getDefault().register(this);
        broadcastManager = LocalBroadcastManager.getInstance(getActivity());
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(ACTION);
         bordcastReceiver = new BroadcastReceiver() {
             @Override
            public void onReceive(Context context, Intent intent) {
                  //信息处理
//                  Toast.makeText(context, "信息更新", Toast.LENGTH_SHORT).show(); 
            	getHttpRequst(false, true, 1, false);
              	listView.deferNotifyDataSetChanged();
//              	adapter.notifyDataSetChanged();
            }
       };
    broadcastManager.registerReceiver(bordcastReceiver, intentFilter);
  
    }
    @Override
    public void onDestroy() {
    	// TODO Auto-generated method stub
    	super.onDestroy();
    	broadcastManager.unregisterReceiver(bordcastReceiver);
    }
}
