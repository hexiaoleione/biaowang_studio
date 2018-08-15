package com.hex.express.iwant.minfragment;


import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.http.Header;

import com.google.gson.Gson;
import com.handmark.pulltorefresh.library.ILoadingLayout;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.hex.express.iwant.R;
import com.hex.express.iwant.activities.DownEscoreDartActivity;
import com.hex.express.iwant.activities.LogCompanyActivity;
import com.hex.express.iwant.activities.LogistcaInforseActivity;
import com.hex.express.iwant.activities.ProvCityTownAcrivity;
import com.hex.express.iwant.adapters.BaseListAdapter;
import com.hex.express.iwant.adapters.NearbyAdapter;
import com.hex.express.iwant.adapters.ReceiptCourierAdapter;
import com.hex.express.iwant.adapters.SendEscortAdapter;
import com.hex.express.iwant.adapters.SendOwnerAdapter;
import com.hex.express.iwant.adapters.BaseListAdapter.ViewHolder;
import com.hex.express.iwant.bean.BaseBean;
import com.hex.express.iwant.bean.DownSpecialBean;
import com.hex.express.iwant.bean.DownSpecialBean;
import com.hex.express.iwant.bean.DownSpecialBean.Data;
import com.hex.express.iwant.bean.PublicCourierBean;
import com.hex.express.iwant.constance.MCUrl;
import com.hex.express.iwant.constance.PreferenceConstants;
import com.hex.express.iwant.http.AsyncHttpUtils;
import com.hex.express.iwant.http.UrlMap;
import com.hex.express.iwant.utils.PreferencesUtils;
import com.hex.express.iwant.utils.ToastUtil;
import com.hex.express.iwant.views.LoadingProgressDialog;
import com.hex.express.iwant.views.MarqueeTextView;
import com.loopj.android.http.AsyncHttpResponseHandler;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.FrameLayout.LayoutParams;
import butterknife.Bind;
import butterknife.ButterKnife;

public class MinFragment1 extends Fragment {

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
	private int num;
	private String cityCode;
	private String entPlaceCityCode;
	private int type=1;
	View view;
	public LoadingProgressDialog dialog;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		if (view==null) {
			view=inflater.inflate(R.layout.fragment_my1, container, false);
		}
		 ViewGroup p = (ViewGroup) view.getParent(); 
         if (p != null) { 
             p.removeAllViewsInLayout(); 
         } 
         ButterKnife.bind(this,view);
         dialog=new LoadingProgressDialog(getActivity());
 		initData();
 		getData();
 	    
		return view;
	}
	
	private void initData(){
		mList = new ArrayList<Data>();
		mList2 = new ArrayList<Data>();
		listView = ptrlv_card.getRefreshableView();
		getHttpRequst(true, false, 1, false);
		
	}
	
	/**
	 * 获取网络数据
	 */
	@SuppressWarnings("unchecked")
	private void getHttpRequst(final boolean isFirst, final boolean isRefresh, int pageNo, final boolean isPull) {
		String url = UrlMap.getfour(MCUrl.downwindTaskRouteListType, "userId",
				String.valueOf(PreferencesUtils.getInt(getActivity(), PreferenceConstants.UID)), "pageNo",
				String.valueOf(pageNo), "pageSize", String.valueOf(pageSize),"type","limit");
		dialog.show();
		AsyncHttpUtils.doSimGet(url, new AsyncHttpResponseHandler() {
			@Override
			public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
				Log.e("1111限时sdds ", new String(arg2));
				dialog.dismiss();
				bean = new Gson().fromJson(new String(arg2), DownSpecialBean.class);
				mList2.clear();
				mList = bean.data;
				if (isFirst) {
					if (mList.size() != 0 && mList != null) {
						if (adapter == null) {
							adapter = new SendEscortAdapter(getActivity(), mList);
							listView.setAdapter(adapter);
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
			public void onItemClick(AdapterView<?> arg0, View arg1, int position,
					long arg3) {
				
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
				intent.putExtra("carType", data.carType);//
				intent.putExtra("carLength", data.carLength);
				intent.putExtra("matVolume", data.matVolume);
				intent.putExtra("useTime",  data.useTime);
				
				intent.putExtra("fromLatitude", data.fromLatitude);
				intent.putExtra("fromLongitude", data.fromLongitude);
				intent.putExtra("toLatitude", data.toLatitude);
				intent.putExtra("toLongitude", data.toLongitude);
				intent.putExtra("cargoSize", data.cargoSize);//件数
				intent.putExtra("distance", data.distance);
				intent.putExtra("whether", data.whether);
				intent.putExtra("premium", data.premium);
				
				if (!data.length.equals("0") && !data.wide.equals("0")&& !data.high.equals("0")) {
					intent.putExtra("length", "长："+data.length+" 厘米"+"  宽："+data.wide+" 厘米"+"  高："+data.high+" 厘米");
				}else if (!data.length.equals("0")) {
					if (!data.wide.equals("0")) {
						intent.putExtra("length", "长："+data.length+"厘米  "+"  宽："+data.wide+"厘米  ");
					}else if (!data.high.equals("0")) {
						intent.putExtra("length", "长："+data.length+"厘米  "+"  高："+data.high+"厘米  ");
					}else {
						intent.putExtra("length", "长："+data.length+"厘米  ");
					}
				}else if (!data.wide.equals("0")) {
					if (!data.high.equals("0")) {
						intent.putExtra("length", "宽："+data.wide+"厘米  "+"  高："+data.high+"厘米  ");
					}else {
						intent.putExtra("length", "宽："+data.wide+"厘米  ");
					}
				}else if (!data.high.equals("0")) {
					intent.putExtra("length", "高："+data.high+"厘米  ");
				}else if (data.length.equals("0") && !data.wide.equals("0") &&data.high.equals("0")) {
					intent.putExtra("length", "用户未填写");
					ToastUtil.shortToast(getActivity(), "1111");
				}
//				intent.putExtra("length", "长："+data.length+"  宽："+data.wide+"  高："+data.high);//物品体积
				intent.putExtra("status", data.status);//状态 0-预发布成功(未支付) 1-支付成功(已支付)  2(已抢单) 3 已取件 4 订单取消()镖师  5 成功  6 删除 7 已评价 8订单取消（用户）
				intent.putExtra("ifAgree", data.ifAgree);
//				if(!data.useTime.equals("")){
//					intent.putExtra("useTime", data.useTime);//达到时间
//				}else {
					intent.putExtra("useTime", data.limitTime);//期望到达时间
//				}
//				if(!data.finishTime.equals("")){
//					intent.putExtra("finishTime", data.finishTime);//达到时间
//				}
//				if(!data.limitTime.equals("")){
//					intent.putExtra("limitTime", data.limitTime);//期望到达时间
//				}
//				startActivity(intent);
				startActivityForResult(intent, 11);
//				getActivity().finish();//销毁掉承载它的FragmentActivity，使FragmentActivity在重新加载时能够重新从服务器请求本页面更新完的数据（在点击"取消订单"后）		
			}
		});

	}
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
//		Logger.e("11111s", "requestCode"+requestCode+"resultCode"+resultCode+"data"+data);
		switch (requestCode) {
		case 11:
			if (resultCode == -1) {
				if (data!=null) {
					if (data.getStringExtra("type").equals("1")) {
//						finish();	
						getHttpRequst(false, true, 1, false);	
						
					}
				}
				}
			break;
		}
		
	}
	
	class SendEscortAdapter extends BaseListAdapter {

		public SendEscortAdapter(Context context, List list) {
			super(context, list);
			// TODO Auto-generated constructor stub
		}

		@Override
		public ViewHolder onCreateViewHolder(View itemView) {
			// TODO Auto-generated method stub
			return new OwnerViewHolder(itemView);
		}

		@Override
		public int getLayoutResource() {
			// TODO Auto-generated method stub
			return R.layout.item_newsendfreight;//item_sendfreight  item_newsendfreight
			
		}
		class OwnerViewHolder extends ViewHolder{

			public OwnerViewHolder(View itemView) {
				super(itemView);
				// TODO Auto-generated constructor stub
			}
			@Bind(R.id.text_time)
			TextView text_time;
			@Bind(R.id.iv_img)
			ImageView iv_img;
			@Bind(R.id.text_name)
			TextView text_name;
			@Bind(R.id.text_nameTo)
			TextView text_nameTo;
			@Bind(R.id.text_address)
			MarqueeTextView text_address;
			@Bind(R.id.text_range)
			TextView text_range;
			@Bind(R.id.replaceMoney)
			TextView replaceMoney;
			
			@Bind(R.id.text_state)
			TextView text_state;
			@Bind(R.id.text_quxiao)
			TextView text_quxiao;
			
			
			
			@Bind(R.id.iv_phone)
			ImageView iv_phone;
			@Bind(R.id.iv_details)
			ImageView iv_details;
			@Bind(R.id.iv_distance)
			ImageView iv_distance;
			@Bind(R.id.iv_Order_finish)//押镖成功
			ImageView iv_Order_finish;
			@Bind(R.id.iv_image)
			ImageView iv_image;
			DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			@Override
			public void setData(final int position) {
				// TODO Auto-generated method stub
				super.setData(position);
				final DownSpecialBean bean=new DownSpecialBean();
				bean.data=list;
//				text_time.setTextColor(context.getResources().getColor(R.color.orange1));
				text_time.setText("要求到达时间："+bean.data.get(position).limitTime);
				text_nameTo.setText(bean.data.get(position).personNameTo);
				text_name.setText(bean.data.get(position).matName);
				text_address.setText(bean.data.get(position).addressTo.replace("中国", ""));
				if (bean.data.get(position).ifReplaceMoney.equals("true") && !bean.data.get(position).ifReplaceMoney.equals("")) {
					replaceMoney.setVisibility(View.VISIBLE);
					replaceMoney.setText("代收款："+bean.data.get(position).replaceMoney+"元");
				}else {
					replaceMoney.setVisibility(View.GONE);
				}
				
				if (bean.data.get(position).status.equals("5")||bean.data.get(position).status.equals("7")) {
//					iv_Order_finish.setVisibility(View.VISIBLE);
					iv_image.setVisibility(View.GONE);
				}else {
					iv_image.setVisibility(View.VISIBLE);
					iv_Order_finish.setVisibility(View.GONE);
				}
				if (bean.data.get(position).status.equals("4") && !bean.data.get(position).ifAgree.equals("")) {
//					iv_Order_finish.setVisibility(View.VISIBLE);
					iv_image.setVisibility(View.GONE);
//					iv_Order_finish.setBackgroundResource(R.drawable.huowuweigui);
				} else if(bean.data.get(position).status.equals("4") || bean.data.get(position).status.equals("8") ){
					
//					iv_Order_finish.setVisibility(View.VISIBLE);
					iv_image.setVisibility(View.GONE);
//					iv_Order_finish.setBackgroundResource(R.drawable.bg_quexiao);
					
				}
				// /状态 0-预发布成功(未支付) 1-支付成功(已支付)  2(已抢单) 3 已取件(不需要代收款) 4 订单取消(镖师)  5 成功  6 系统删除 7 已评价 8订单取消(用户)
	            //  9 货物违规状态(镖师点击货物违规按钮后,用户界面出现是否同意的按钮)
				if (bean.data.get(position).status.equals("1")) {
					text_state.setText("取消");
				}else if (bean.data.get(position).status.equals("2")) {
					text_state.setText("未取件");
				} else if (bean.data.get(position).status.equals("3")) {
					text_state.setText("未送达");
				} else if (bean.data.get(position).status.equals("4")) {
					if (!bean.data.get(position).ifAgree.equals(0)) {
						text_quxiao.setText("货物违规取消");
					}else {
						text_quxiao.setText("镖师取消取消");
					}
					
				}else if (bean.data.get(position).status.equals("5")) {
					text_quxiao.setText("已完成");
					text_state.setText("");
				}else if (bean.data.get(position).status.equals("7")) {
					text_quxiao.setText("已完成");
					text_state.setText("");
				}else if (bean.data.get(position).status.equals("8")) {
					text_quxiao.setText("用户取消");
					text_state.setText("");
				}
				else if (bean.data.get(position).status.equals("9")) {
					text_quxiao.setText("货物违规");
				}else if (bean.data.get(position).status.equals("10")) {
					text_quxiao.setText("已完成等待结算运费");
					text_quxiao.setTextColor(getResources().getColor(R.color.red));
					text_state.setText("");
				}
				else if (bean.data.get(position).status.equals("11")) {
					text_state.setText("");
					text_quxiao.setText("已冻结");
					text_quxiao.setTextColor(getResources().getColor(R.color.dahuise));
				}
				
				
				if (bean.data.get(position).status.equals("0") || bean.data.get(position).status.equals("1") 
						|| bean.data.get(position).status.equals("2")  ){
				text_quxiao.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View arg0) {
						// TODO Auto-generated method stub
						Builder ad = new Builder(context);
						ad.setTitle("温馨提示");
						ad.setMessage("确认取消订单吗? 取消订单将会罚款损失");
						ad.setPositiveButton("确认", new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface arg0, int arg1) {
								deleteOrder();
							}
						});
						ad.setNegativeButton("取消", new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface arg0, int arg1) {
								arg0.dismiss();
							}
						});
						ad.create().show();

					}
					private void deleteOrder() {
						AsyncHttpUtils.doSimGet(UrlMap.getTwo(MCUrl.DiverCance, "recId", ""+bean.data.get(position).recId,"type","1"),
								new AsyncHttpResponseHandler() {

									@Override
									public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
										Log.e("msg", new String(arg2));
//										context.sendBroadcast(new Intent().setAction("cancelOrder"));
									
										BaseBean baseBean= new Gson().fromJson(new String(arg2), BaseBean.class);
										BaseBean bean = new Gson().fromJson(new String(arg2), BaseBean.class);
										// Log.e("bean________",
										// bean.getData().get(arg0).toString());
										if (bean.getErrCode() == 0) {// 如果取消订单成功
											ToastUtil.shortToast(getActivity(), "取消订单成功！");
											getHttpRequst(false, true, 1, false);	
//											finish();// 取消订单成功后即销毁此页面,并去启动上一个页面的Fragment
//											startActivity(new Intent(getApplicationContext(), MyDownwindActivity.class)
//													.putExtra("loadIndex", "Escort"));

										} else if (bean.getErrCode() == -1) {
											ToastUtil.shortToast(getActivity(), bean.getMessage());
										} else if (bean.getErrCode() == -2) {
											ToastUtil.shortToast(getActivity(), bean.getMessage());
										} else {
											ToastUtil.shortToast(getActivity(), bean.getMessage());
										}
									}

									@Override
									public void onFailure(int arg0, Header[] arg1, byte[] arg2, Throwable arg3) {
										// TODO Auto-generated method stub

									}
								});

					}
				});
				}
//				if (!bean.data.get(position).matImageUrl.equals("")) {
//					new MyBitmapUtils().display(iv_img, bean.data.get(position).matImageUrl);
//				}else {
//					iv_img.setBackgroundResource(R.drawable.ren3);
//				}
				// 将图片设置成圆角；
				if (!bean.data.get(position).limitTime.equals("")) {
//					new MyBitmapUtils().display(iv_imgOfGood, bean.data.get(position).matImageUrl);
					iv_img.setBackgroundResource(R.drawable.xianshim);
				}else {
					iv_img.setBackgroundResource(R.drawable.shunfengm);
				} 
				if (!"".equals(bean.data.get(position).carLength)) {
					if(bean.data.get(position).carLength.equals("1")){
						iv_img.setBackgroundResource(R.drawable.shunfengm);
					}else if(bean.data.get(position).carLength.equals("2")){
						iv_img.setBackgroundResource(R.drawable.yimi);
					}else if (bean.data.get(position).carLength.equals("3")) {
						iv_img.setBackgroundResource(R.drawable.liangmi);
					}else if (bean.data.get(position).carLength.equals("4")) {
						iv_img.setBackgroundResource(R.drawable.simi);
					}
				}
					try
					{
					Date date=new Date();
					 DateFormat format=new SimpleDateFormat("yyyy-MM-dd HH:mm");
					 String time=format.format(date);
					  Date d1 = df.parse(bean.data.get(position).limitTime);
					  Date d2 = df.parse(time);
					  long diff = d1.getTime() - d2.getTime();//这样得到的差值是微秒级别
					  long days = diff / (1000 * 60 * 60 * 24);
					  long hours = (diff-days*(1000 * 60 * 60 * 24))/(1000* 60 * 60);
					  long minutes = (diff-days*(1000 * 60 * 60 * 24)-hours*(1000* 60 * 60))/(1000* 60);
					  System.out.println(""+days+"天"+hours+"小时"+minutes+"分");
		         if(minutes<0){
//		        	 iv_Order_finish.setVisibility(View.VISIBLE);
//		 	         iv_Order_finish.setBackgroundResource(R.drawable.chao_nosmill);
		        	 text_quxiao.setText("送单超时");
		              }
					}
					catch (Exception e)
					{
					}
				
			}
			
		}
				}
		
		
}
