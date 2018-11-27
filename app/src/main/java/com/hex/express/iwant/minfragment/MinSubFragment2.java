package com.hex.express.iwant.minfragment;


import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.handmark.pulltorefresh.library.ILoadingLayout;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.hex.express.iwant.R;
import com.hex.express.iwant.activities.DownDetialTrackActivity;
import com.hex.express.iwant.activities.DownOwnerDetialsActivity;
import com.hex.express.iwant.activities.DownWindPayActivity;
import com.hex.express.iwant.adapters.BaseListAdapter;
import com.hex.express.iwant.bean.BaseBean;
import com.hex.express.iwant.bean.DownSpecialBean;
import com.hex.express.iwant.bean.DownSpecialBean.Data;
import com.hex.express.iwant.constance.MCUrl;
import com.hex.express.iwant.constance.PreferenceConstants;
import com.hex.express.iwant.http.AsyncHttpUtils;
import com.hex.express.iwant.http.UrlMap;
import com.hex.express.iwant.newsmain.NewMainActivity;
import com.hex.express.iwant.utils.AppUtils;
import com.hex.express.iwant.utils.PreferencesUtils;
import com.hex.express.iwant.utils.ToastUtil;
import com.hex.express.iwant.views.LoadingProgressDialog;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.apache.http.Header;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MinSubFragment2 extends Fragment {

	@Bind(R.id.view_load_fail)
	LinearLayout view_load_fail;
	@Bind(R.id.listview2)
	PullToRefreshListView ptrlv_card;
	@Bind(R.id.null_message)
	View view_null_message;
	private ListView listView;
	protected int pageSize = 10;// 表示一页展示多少列
	protected int pageNo = 1;
	private DownSpecialBean bean;
	private List<Data> mList;
	private List<Data> mList2;
	SendOwnerAdapter adapter;
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
			view=inflater.inflate(R.layout.fragment_min2, container, false);
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
	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		getHttpRequst(true, false, 1, false);
	}
	/**
	 * 获取网络数据
	 */
	@SuppressWarnings("unchecked")
		private void getHttpRequst(final boolean isFirst, final boolean isRefresh, int pageNo, final boolean isPull) {
			String url = UrlMap.getfour(MCUrl.getTaskByType, "userId",
					String.valueOf(PreferencesUtils.getInt(getActivity(), PreferenceConstants.UID)), "pageNo",
					String.valueOf(pageNo), "pageSize", String.valueOf(pageSize),"type","wind");
		dialog.show();
		AsyncHttpUtils.doSimGet(url, new AsyncHttpResponseHandler() {
			@Override
			public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
				Log.e("sdds ", new String(arg2));
				dialog.dismiss();
				bean = new Gson().fromJson(new String(arg2), DownSpecialBean.class);
				mList2.clear();
				mList = bean.data;
				if (isFirst) {
					if (mList.size() != 0 && mList != null) {
						if (adapter == null) {
							adapter = new SendOwnerAdapter(getActivity(), mList);
							listView.setAdapter(adapter);
						}
						view_null_message.setVisibility(View.GONE);
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
						} else {
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
		listView.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
				// TODO Auto-generated method stub
				Data data = (Data) adapter.list.get(position - 1);
				// 状态 0-预发布成功(未支付) 1-支付成功(已支付) 2-(已抢单) 3-已取件 4-订单取消 5-成功 6-删除
				// 7-已评价 8 用户主动取消订单
				String status = data.status;// 状态；
				final int recId = data.recId;// 状态；
				if (data.status.equals("0") || data.status.equals("4") || data.status.equals("5") || data.status.equals("7") || data.status.equals("8")) {
					Builder builder = new Builder(getActivity());
					builder.setTitle("温馨提示");
					builder.setMessage("是否要删除");
					builder.setNegativeButton("确认", new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface arg0, int arg1) {
							detaMessage(recId);
						}
					});
					builder.setPositiveButton("取消", new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface arg0, int arg1) {
							arg0.dismiss();
							
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
				// TODO Auto-generated method stub

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
					intent.putExtra("carLength", data.carLength);
					intent.putExtra("matVolume", data.matVolume);
					if (!data.length.equals("0") && !data.wide.equals("0")&& !data.high.equals("0")) {
						intent.putExtra("length", "长："+data.length+" 厘米 "+"  宽："+data.wide+" 厘米 "+" 高："+data.high+" 厘米");
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
						intent.putExtra("length", "用户未填写 ");
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
					intent.putExtra("replaceMoney", data.replaceMoney);
					intent.putExtra("ifReplaceMoney", data.ifReplaceMoney);
					intent.putExtra("ifTackReplace", data.ifTackReplace);
					intent.putExtra("carType", data.carType);
					intent.putExtra("insureCost", data.insureCost);
					intent.putExtra("premium", data.premium);
					intent.putExtra("useTime",  data.useTime);
					intent.putExtra("cargoSize",  data.cargoSize);
					
					intent.putExtra("townCode",  data.townCode);
					intent.putExtra("cityCode",  data.cityCode);
					intent.putExtra("fromLatitude",  data.fromLatitude);
					intent.putExtra("fromLongitude",  data.fromLongitude);
					intent.putExtra("cityCodeTo",  data.cityCodeTo);
					intent.putExtra("toLatitude",  data.toLatitude);
					intent.putExtra("toLongitude",  data.toLongitude);
					intent.putExtra("whether",  data.whether);
					intent.putExtra("category",  data.category);
					if (data.status.equals("9")) {
						Intent intent1 = new Intent(getActivity(), DownOwnerDetialsActivity.class);
						intent1.putExtra("recId", data.recId);
						intent1.putExtra("matImageUrl", data.matImageUrl);
						intent1.putExtra("matName", data.matName);
						intent1.putExtra("matWeight", data.matWeight);
						intent1.putExtra("ifAgree", data.ifAgree);
						if (!data.length.equals("0") && !data.wide.equals("0")&& !data.high.equals("0")) {
							intent1.putExtra("length", "长："+data.length+" 厘米 "+"  宽："+data.wide+" 厘米 "+" 高："+data.high+" 厘米");
						}else if (!data.length.equals("0")) {
							if (!data.wide.equals("0")) {
								intent1.putExtra("length", "长："+data.length+"厘米  "+"  宽："+data.wide+"厘米  ");
							}else if (!data.high.equals("0")) {
								intent1.putExtra("length", "长："+data.length+"厘米  "+"  高："+data.high+"厘米  ");
							}else {
								intent1.putExtra("length", "长："+data.length+"厘米  ");
							}
						}else if (!data.wide.equals("0")) {
							if (!data.high.equals("0")) {
								intent1.putExtra("length", "宽："+data.wide+"厘米  "+"  高："+data.high+"厘米  ");
							}else {
								intent1.putExtra("length", "宽："+data.wide+"厘米  ");
							}
						}else if (!data.high.equals("0")) {
							intent1.putExtra("length", "高："+data.high+"厘米  ");
						}else if (data.length.equals("0") && !data.wide.equals("0") &&data.high.equals("0")) {
							intent1.putExtra("length", "用户未填写 ");
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
						intent1.putExtra("replaceMoney", data.replaceMoney);
						intent1.putExtra("ifReplaceMoney", data.ifReplaceMoney);
						intent1.putExtra("ifTackReplace", data.ifTackReplace);
						intent1.putExtra("carType", data.carType);
						intent1.putExtra("insureCost", data.insureCost);
						intent1.putExtra("premium", data.premium);
						intent1.putExtra("carLength", data.carLength);
						intent1.putExtra("matVolume", data.matVolume);
						intent1.putExtra("useTime",  data.useTime);
						intent1.putExtra("cargoSize",  data.cargoSize);
						
						intent1.putExtra("townCode",  data.townCode);
						intent1.putExtra("cityCode",  data.cityCode);
						intent1.putExtra("fromLatitude",  data.fromLatitude);
						intent1.putExtra("fromLongitude",  data.fromLongitude);
						intent1.putExtra("cityCodeTo",  data.cityCodeTo);
						intent1.putExtra("toLatitude",  data.toLatitude);
						intent1.putExtra("toLongitude",  data.toLongitude);
						intent1.putExtra("whether",  data.whether);
						intent1.putExtra("category",  data.category);
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
						intent.putExtra("length", "长："+data.length+" 厘米 "+"  宽："+data.wide+" 厘米 "+" 高："+data.high+" 厘米");
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
						intent.putExtra("length", "用户未填写 ");
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
					intent.putExtra("replaceMoney", data.replaceMoney);
					intent.putExtra("ifReplaceMoney", data.ifReplaceMoney);
					intent.putExtra("ifTackReplace", data.ifTackReplace);
					intent.putExtra("carType", data.carType);
					intent.putExtra("insureCost", data.insureCost);
					intent.putExtra("premium", data.premium);
					intent.putExtra("carLength", data.carLength);
					intent.putExtra("matVolume", data.matVolume);
					intent.putExtra("useTime",  data.useTime);
					intent.putExtra("cargoSize",  data.cargoSize);
					intent.putExtra("townCode",  data.townCode);
					intent.putExtra("cityCode",  data.cityCode);
					intent.putExtra("fromLatitude",  data.fromLatitude);
					intent.putExtra("fromLongitude",  data.fromLongitude);
					intent.putExtra("cityCodeTo",  data.cityCodeTo);
					intent.putExtra("toLatitude",  data.toLatitude);
					intent.putExtra("toLongitude",  data.toLongitude);
					intent.putExtra("whether",  data.whether);
					intent.putExtra("category",  data.category);
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
						
						BaseBean	bean = new Gson().fromJson(new String(arg2),
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
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
//		Logger.e("11111s", "requestCode"+requestCode+"resultCode"+resultCode+"data"+data);
		switch (requestCode) {
		case 10:
			if (resultCode == -1) {
				if (data!=null) {
//						finish();	
						getHttpRequst(false, true, 1, false);	
						if (data!=null) {
							if (data.getStringExtra("type").equals("3")) {
								shoudairen();
							}
						}
				}
				}
			break;
		case 11:
			if (resultCode == -1) {
				
					if (!data.getStringExtra("type").equals("1")) {
//						finish();	
						getHttpRequst(false, true, 1, false);	
					}
				}
			break;
		}
	
	}
	/**
	 * 显示投保提示信息
	 */
	private void shoudairen() {

	        LayoutInflater inflater = LayoutInflater.from(getActivity());  
	        final View textEntryView = inflater.inflate(  
	                R.layout.popwidndow_daifu, null);  
	        final EditText edtInput=(EditText)textEntryView.findViewById(R.id.edtInput);  
	        final Builder builder = new Builder(getActivity());
	        builder.setCancelable(false);  
	        builder.setTitle("请输入代付人手机号");  
	        builder.setView(textEntryView);  
	        builder.setPositiveButton("确认",  
	                new DialogInterface.OnClickListener() {  
	                    public void onClick(DialogInterface dialog, int whichButton) {  
//	                        setTitle(edtInput.getText());  
	                        getHttpRequst(edtInput.getText().toString());
	                    }  
	                });  
	        builder.setNegativeButton("取消",  
	                new DialogInterface.OnClickListener() {  
	                    public void onClick(DialogInterface dialog, int whichButton) {  
//	                        setTitle("");  
	                    }  
	                });  
	        builder.show();  
		
	}
	/**
	 * 获取网络数据  
	 */
	@SuppressWarnings("unchecked")
	private void getHttpRequst(String phone) {
		String url = UrlMap.getTwo(MCUrl.replace, "recId", ""+bean.data.get(0).recId,
				"mobile", phone);
//		dialog.show();
		AsyncHttpUtils.doSimGet(url, new AsyncHttpResponseHandler() {
			@Override
			public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
				Log.e("sdds ", new String(arg2));
//				dialog.dismiss();
			BaseBean baseBean=new Gson().fromJson(new String(arg2), BaseBean.class);
			if (baseBean.getErrCode()==0) {
				ToastUtil.shortToast(getActivity(), baseBean.getMessage());
				Intent intent=new Intent();
				intent.setClass(getActivity(), NewMainActivity.class);
				startActivity(intent);
			}else {
				ToastUtil.shortToast(getActivity(), baseBean.getMessage());
			}

			}

			@Override
			public void onFailure(int arg0, Header[] arg1, byte[] arg2, Throwable arg3) {
			}
		});
	}
class SendOwnerAdapter extends BaseListAdapter {
		

		public SendOwnerAdapter(Context context, List list) {
			super(context, list);
			// TODO Auto-generated constructor stub
		}

		@Override
		public ViewHolder onCreateViewHolder(View itemView) {
			return new OwnerViewHolder(itemView);
		}

		@Override
		public int getLayoutResource() {
			return R.layout.item_newsendownerd;//item_sendownerd  item_newsendownerd

		}

		class OwnerViewHolder extends ViewHolder {

			public OwnerViewHolder(View itemView) {
				super(itemView);
			}

			@Bind(R.id.tv_status)
			TextView tv_status;
			@Bind(R.id.text1)
			TextView text1;
			@Bind(R.id.text_name)
			TextView text_name;

			@Bind(R.id.text_time)
			TextView text_time;

			@Bind(R.id.text_peoplename)
			TextView text_peoplename;

			@Bind(R.id.iv_message)
			ImageView img_message;

			@Bind(R.id.iv_phone)
			ImageView img_phone;

			@Bind(R.id.img_fukuai)//代付款按钮
			ImageView img_fukuai;
			

			@Bind(R.id.img_status)
			ImageView img_status;

			@Bind(R.id.iv_address)
			ImageView img_address;

			@Bind(R.id.delete_order) // 取消订单
			TextView delete_order;

			// 接镖的，和取件的才能显示
			@Bind(R.id.iv_imgview)
			ImageView iv_imgview;
			private DownSpecialBean bean;

			@Override
			public void setData(final int position) {
				super.setData(position);
				bean = new DownSpecialBean();
				bean.data = list;
				Log.e("BEAM", bean.data.get(position).toString());
				text_time.setText("要求到达时间："+bean.data.get(position).useTime);
				text_peoplename.setText(bean.data.get(position).driverName);
				text_name.setText(bean.data.get(position).matName);
//				iv_imgview.setVisibility(View.VISIBLE);
				
				img_phone.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						AppUtils.intentDial(context, bean.data.get(position).mobile);

					}
				});
				if (bean.data.get(position).status.equals("0")) {
					img_fukuai.setVisibility(View.VISIBLE);
					img_status.setVisibility(View.GONE);
					delete_order.setVisibility(View.VISIBLE);
					tv_status.setVisibility(View.VISIBLE);
					delete_order.setText("取消");
					tv_status.setText("未付款");
				}else {
					img_fukuai.setVisibility(View.GONE);
					img_status.setVisibility(View.VISIBLE);
					delete_order.setVisibility(View.VISIBLE);
				}
				img_fukuai.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
//						ToastUtil.shortToast(context, "即时通信");
						Intent intent = new Intent(getActivity(), DownWindPayActivity.class);
						intent.putExtra("recId", bean.data.get(position).recId);
						intent.putExtra("money", bean.data.get(position).transferMoney);
//						intent.putExtra("insureCost", String.valueOf(df.format(bean.data.get(0).insureCost)));
						intent.putExtra("insureCost", bean.data.get(position).insureCost);
						intent.putExtra("billCode", bean.data.get(position).billCode);
						intent.putExtra("distance", bean.data.get(position).distance);
						intent.putExtra("baofei", bean.data.get(position).premium);
						intent.putExtra("daishou",bean.data.get(position).replaceMoney);
						intent.putExtra("payUserId", bean.data.get(position).replaceUserId);
						intent.putExtra("tyy","顺路送");
//						Logger.e("billCode数据", bean.data.get(0).billCode);
						startActivityForResult(intent, 10);
					}
				});
				img_address.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						ToastUtil.shortToast(context, "获取当前地理位置");

					}
				});
				delete_order.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View arg0) {
						if (bean.data.get(position).status.equals("0")) {
							detaMessage(bean.data.get(position).recId);
						}else {
							
						String string = null;
						Builder ad = new Builder(context);
						if (bean.data.get(position).status.equals("1") || bean.data.get(position).status.equals("0")
								|| bean.data.get(position).status.equals("2")) {
							ad.setTitle("温馨提示");
				            ad.setMessage("确认取消订单吗");
						}else {
						try
						{
						DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");
						Date date=new Date();
						 DateFormat format=new SimpleDateFormat("yyyy-MM-dd HH:mm");
						 String time=format.format(date);
						  Date d1 = df.parse(bean.data.get(position).publishTime);
						  Date d2 = df.parse(time);
						  long diff = d1.getTime() - d2.getTime();//这样得到的差值是微秒级别
						  long days = diff / (1000 * 60 * 60 * 24);
						  long hours = (diff-days*(1000 * 60 * 60 * 24))/(1000* 60 * 60);
						  long minutes = (diff-days*(1000 * 60 * 60 * 24)-hours*(1000* 60 * 60))/(1000* 60);
						  System.out.println(""+days+"天"+hours+"小时"+minutes+"分");
			         if(diff<1800){
			        	 string="因镖师已接单，取消订单将会罚款损失";
			        	 ad.setTitle("温馨提示");
			             ad.setMessage("因镖师已接单，取消订单将会罚款");
			              }else {
			            string="确认取消订单吗";
			            ad.setTitle("温馨提示");

			            ad.setMessage("确认取消订单吗");
						}
						}
						catch (Exception e)
						{
						}
						}
						
												
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
					}

					private void deleteOrder() {
						AsyncHttpUtils.doGet(
								UrlMap.getUrl(MCUrl.CANCELDOWNWINDTASK, "recId",
										String.valueOf(bean.data.get(position).recId)),
								null, null, null, new AsyncHttpResponseHandler() {

									@Override
									public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
										Log.e("msg", new String(arg2));
//										context.sendBroadcast(new Intent().setAction("cancelOrder"));
									
										BaseBean baseBean= new Gson().fromJson(new String(arg2), BaseBean.class);
										if (baseBean.getErrCode()==0) {
											ToastUtil.shortToast(getActivity(), baseBean.getMessage());
											getHttpRequst(false, true, 1, false);
										}else {
											ToastUtil.shortToast(getActivity(), baseBean.getMessage());
										}
									}

									@Override
									public void onFailure(int arg0, Header[] arg1, byte[] arg2, Throwable arg3) {
										// TODO Auto-generated method stub

									}
								});

					}
					
				});
				
				img_status.setBackgroundResource(R.drawable.zhuangtai1);
//				delete_order.setVisibility(View.VISIBLE);
//				tv_status.setText("");
				if (bean.data.get(position).status.equals("9")) {
					delete_order.setVisibility(View.VISIBLE);
					delete_order.setText("货物违规");
					tv_status.setText("");
					delete_order.setClickable(false);
					img_status.setBackgroundResource(R.drawable.zhuangtai6);
				} else {
					delete_order.setClickable(true);
				}
				// public String status;//状态 0-预发布成功(未支付) 1-支付成功(已支付)-待接镖 2-待取镖
				// 3-押镖中 4-取消押镖 5-押镖完成（） 6-删除 7-已评价   8订单取消(用户)   9货物违规状态(镖师点击货物违规按钮后,用户界面出现是否同意的按钮)
				if (bean.data.get(position).status.equals("1")) {
					tv_status.setText("等待接单");
					tv_status.setTextColor(context.getResources().getColor(R.color.red));
					text1.setText("收件人:");
					text_peoplename.setText(bean.data.get(position).personNameTo);
					delete_order.setVisibility(View.VISIBLE);
					img_status.setBackgroundResource(R.drawable.zhuangtai1);
				} else if (bean.data.get(position).status.equals("2")) {
					tv_status.setText("等待取件");
					text1.setText("镖师:");
					tv_status.setTextColor(context.getResources().getColor(R.color.red));
					delete_order.setVisibility(View.VISIBLE);
					img_status.setBackgroundResource(R.drawable.zhuangtai5);
				} else if (bean.data.get(position).status.equals("3")) {
					tv_status.setText("送件中");
					text1.setText("镖师:");
					tv_status.setTextColor(context.getResources().getColor(R.color.red));
					delete_order.setVisibility(View.GONE);
					img_status.setBackgroundResource(R.drawable.zhuangtai2);
				} else if (bean.data.get(position).status.equals("4")) {
					if (!bean.data.get(position).ifAgree.equals("")) {
						tv_status.setText("货物违规取消");
						delete_order.setVisibility(View.GONE);
						text1.setText("镖师:");
						tv_status.setTextColor(context.getResources().getColor(R.color.red));
						img_status.setBackgroundResource(R.drawable.zhuangtai6);
					} else {
						delete_order.setVisibility(View.GONE);
						tv_status.setText("镖师取消送单");
						text1.setText("镖师:");
						tv_status.setTextColor(context.getResources().getColor(R.color.red));
						img_status.setBackgroundResource(R.drawable.zhuangtai6);
					}

				} else if (bean.data.get(position).status.equals("5")) {
					tv_status.setText("  押单完成(待评价）");
					text1.setText("镖师:");
					delete_order.setVisibility(View.GONE);
					tv_status.setTextColor(context.getResources().getColor(R.color.red));
					img_status.setBackgroundResource(R.drawable.zhuangtai3);
				} else if (bean.data.get(position).status.equals("6")) {
					tv_status.setTextColor(context.getResources().getColor(R.color.red));
					tv_status.setText("过期（已退款）");
					text1.setText("收件人:");
					img_status.setBackgroundResource(R.drawable.zhuangtai);
//					setBackgroundResource(R.drawable.simi);
					delete_order.setVisibility(View.GONE);
				}  else if (bean.data.get(position).status.equals("7")) {
					tv_status.setTextColor(context.getResources().getColor(R.color.orange1));
					tv_status.setText("已评价");
					text1.setText("镖师:");
					delete_order.setVisibility(View.GONE);
					img_status.setBackgroundResource(R.drawable.zhuangtai3);
				} else if (bean.data.get(position).status.equals("8")) {
					tv_status.setTextColor(context.getResources().getColor(R.color.red));
					tv_status.setText("已取消发布");
					text1.setText("收件人:");
					text_peoplename.setText(bean.data.get(position).personNameTo);
					delete_order.setVisibility(View.GONE);
					img_status.setBackgroundResource(R.drawable.zhuangtai);
				} else if (bean.data.get(position).status.equals("10")) {
					tv_status.setText("  已送达");
					text1.setText("镖师:");
					delete_order.setVisibility(View.GONE);
					tv_status.setTextColor(context.getResources().getColor(R.color.red));
					img_status.setBackgroundResource(R.drawable.zhuangtai3);
				} else if (bean.data.get(position).status.equals("11")) {
					tv_status.setText("  已退款");
					text1.setText("镖师:");
					delete_order.setVisibility(View.GONE);
					tv_status.setTextColor(context.getResources().getColor(R.color.red));
					img_status.setBackgroundResource(R.drawable.zhuangtai3);
				}
				
				
//				if(!bean.data.get(position).limitTime.equals("")){
//					img_message.setVisibility(View.VISIBLE);
//				}
//	           if (img_message) {
//	        	   img_message.setVisibility(View.VISIBLE);
//			}
			}
		}
	}
}
