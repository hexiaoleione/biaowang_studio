package com.hex.express.iwant.fragments;

import java.text.DecimalFormat;
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
import com.hex.express.iwant.activities.DownEscoreDartActivity;
import com.hex.express.iwant.activities.MyDownwindActivity;
import com.hex.express.iwant.adapters.SendEscortAdapter;
import com.hex.express.iwant.bean.BaseBean;
import com.hex.express.iwant.bean.DownSpecialBean;
import com.hex.express.iwant.bean.DownSpecialBean.Data;
import com.hex.express.iwant.bean.DownStrokeBean;
import com.hex.express.iwant.constance.MCUrl;
import com.hex.express.iwant.constance.PreferenceConstants;
import com.hex.express.iwant.http.AsyncHttpUtils;
import com.hex.express.iwant.http.UrlMap;
import com.hex.express.iwant.utils.PreferencesUtils;
import com.hex.express.iwant.utils.ToastUtil;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 【我接的镖】Fragment
 * 【我的顺风】界面【2-2】
 * @author SCHT-50
 *
 */
public class SendEscortFragment extends BaseItemFragment {
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
	SendEscortAdapter adapter;
	private ListView listView;
	private MyDownwindActivity DownActivity;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		rootView = inflater.inflate(R.layout.fragment_sendescort, container, false);
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
	
	
	private int num;

	//获取已接的镖件
	@SuppressWarnings("unchecked")
	private void getHttpRequst(final boolean isFirst, final boolean isRefresh,
			int pageNo, final boolean isPull) {
		String url = UrlMap.getThreeUrl(MCUrl.DRIVERTASKROUTELIST, "userId",
				String.valueOf(PreferencesUtils.getInt(getActivity(), PreferenceConstants.UID)),
				"pageNo", String.valueOf(pageNo), "pageSize", String
						.valueOf(pageSize));
		AsyncHttpUtils.doSimGet(url, new AsyncHttpResponseHandler() {
			@Override
			public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
				DownActivity.dissmiss();
				Log.e("11111我接的镖jfjdkj", new String(arg2));
				mList2.clear();
//				try {
					bean = new Gson().fromJson(new String(arg2),
							DownSpecialBean.class);
					mList = bean.data;
//				} catch (Exception e) {
//					// TODO: handle exception
//				}
				
				if (isFirst) {
					if (mList.size() != 0 && mList != null) {
						if (adapter == null) {
							adapter = new SendEscortAdapter(getActivity(),
									mList);
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
			}

			@SuppressWarnings("rawtypes")
			@Override
			public void onPullUpToRefresh(PullToRefreshBase refreshView) {
				pageNo++;
				getHttpRequst(false, false, pageNo, true);
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
				if (status.equals("4") || status.equals("5") || status.equals("7") || status.equals("8")) {
					AlertDialog.Builder builder = new AlertDialog.Builder(
							getActivity());
					builder.setItems(R.array.thread_menu, new DialogInterface.OnClickListener() {

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
			public void onItemClick(AdapterView<?> arg0, View arg1, int position,
					long arg3) {
//				if(DownEscoreDartActivity.instance != null){}
//				DownEscoreDartActivity.instance.finish();
//				Intent intent = new Intent();
////				  intent.setClassName(com.hex.express.iwant.activities.this, DownEscoreDartActivity.class);
//					  intent.setClassName(getApplicationContext(), "DownEscoreDartActivity");
//					  List list = getPackageManager().queryIntentActivities(intent, 0);
//					  if (list.size() != 0) {   
//					       // 说明系统中存在这个activity
//						  DownEscoreDartActivity.instance.finish();
//					  }
				Data data = (Data) adapter.list.get(position - 1);
				Intent intent = new Intent(getActivity(), DownEscoreDartActivity.class);
				intent.putExtra("recId", String.valueOf(data.recId));//int recId;//镖件主键
				intent.putExtra("toUserId", data.userId);//String publishTime;//发布时间
				intent.putExtra("publishTime", data.publishTime);//String publishTime;//发布时间
				intent.putExtra("personName", data.personName);//String personName;//发件人
				intent.putExtra("mobile", data.mobile);//String mobile;//发件人手机号
				intent.putExtra("address", data.address.replace("中国", ""));//String address;//发件地址
				intent.putExtra("personNameTo", data.personNameTo);//String personNameTo;//收件人
				intent.putExtra("matWeight", data.matWeight);//String personNameTo;//重量
				intent.putExtra("mobileTo", data.mobileTo);//String mobileTo;//收件人手机号
				intent.putExtra("addressTo", data.addressTo.replace("中国", ""));//String addressTo;//收件地址
				intent.putExtra("matRemark", data.matRemark);//String matRemark;//物品备注
				intent.putExtra("matName", data.matName);//String matRemark;//物品名字
				intent.putExtra("transferMoney",String.valueOf(data.transferMoney));//String matRemark;//物品价格
				intent.putExtra("readyTime", data.readyTime);//String readyTime
				intent.putExtra("replaceMoney", data.replaceMoney);//
				intent.putExtra("ifReplaceMoney", data.ifReplaceMoney);//
				intent.putExtra("ifTackReplace", data.ifTackReplace);//
				
				if (!data.length.equals("0") && !data.wide.equals("0")&& !data.high.equals("0")) {
					intent.putExtra("length", "长："+data.length+"  宽："+data.wide+"  高："+data.high);
				}else {
					intent.putExtra("length", "0");
				}
//				intent.putExtra("length", "长："+data.length+"  宽："+data.wide+"  高："+data.high);//物品体积
				intent.putExtra("status", data.status);//状态 0-预发布成功(未支付) 1-支付成功(已支付)  2(已抢单) 3 已取件 4 订单取消()镖师  5 成功  6 删除 7 已评价 8订单取消（用户）
				intent.putExtra("ifAgree", data.ifAgree);
				if(!data.finishTime.equals("")){
					intent.putExtra("finishTime", data.finishTime);//达到时间
				}
				if(!data.limitTime.equals("")){
					intent.putExtra("limitTime", data.limitTime);//期望到达时间
				}
				startActivity(intent);
				getActivity().finish();//销毁掉承载它的FragmentActivity，使FragmentActivity在重新加载时能够重新从服务器请求本页面更新完的数据（在点击"取消订单"后）		
			}
		});

	}
	/**
	 * 删除消息
	 * @param recId
	 */
	private void detaMessage(int recId) {
		AsyncHttpUtils.doGet(UrlMap.getTwo(MCUrl.DeldeteDow, "recId", ""+recId, "type", "2"), null,
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

}
