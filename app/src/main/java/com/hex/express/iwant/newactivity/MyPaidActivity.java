package com.hex.express.iwant.newactivity;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.Header;
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
import com.hex.express.iwant.activities.BaseActivity;
import com.hex.express.iwant.activities.LogCompanyActivity;
import com.hex.express.iwant.activities.RechargeActivity;
import com.hex.express.iwant.adapters.BaseListAdapter;
import com.hex.express.iwant.adapters.NearbyAdapter;
import com.hex.express.iwant.adapters.BaseListAdapter.ViewHolder;
import com.hex.express.iwant.adpter.MyPaidAdapter;
import com.hex.express.iwant.bean.BaseBean;
import com.hex.express.iwant.bean.MyPaidBean;
import com.hex.express.iwant.constance.MCUrl;
import com.hex.express.iwant.constance.PreferenceConstants;
import com.hex.express.iwant.http.AsyncHttpUtils;
import com.hex.express.iwant.http.UrlMap;
import com.hex.express.iwant.subfragment.SubFragment3;
import com.hex.express.iwant.utils.PreferencesUtils;
import com.hex.express.iwant.utils.ToastUtil;
import com.hex.express.iwant.views.DialogUtils;
import com.hex.express.iwant.views.TitleBarView;
import com.loopj.android.http.AsyncHttpResponseHandler;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 
 * 我的代付
 */
public class MyPaidActivity extends BaseActivity{

	
	@Bind(R.id.btnLeft)
	ImageView btnLeft;

	@Bind(R.id.freigh_listviewse)
	PullToRefreshListView freigh_listview;
	@Bind(R.id.null_message)
	View view_null_message;
	private ListView listView;
	protected int pageSize = 10;// 表示一页展示多少列
	protected int pageNo = 1;
	private List<MyPaidBean.Data> mList;
	private List<MyPaidBean.Data> mList2;
	private MyPaidAdapter adapter;
	public MyPaidBean bean;
	private int num;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.mypaid_activity);
		iWantApplication.getInstance().addActivity(this);
		 ButterKnife.bind(this);
		initData();
		getData();
	}
	@Override
	public void onWeightClick(View v) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void initView() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void initData() {
		// TODO Auto-generated method stub
		mList = new ArrayList<MyPaidBean.Data>();
		mList2 = new ArrayList<MyPaidBean.Data>();
		listView = freigh_listview.getRefreshableView();
		getHttprequst(true, false, 1, false,"");
		btnLeft.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				finish();
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
	private void getHttprequst(final boolean isFirst, final boolean isRefresh,
			int pageNo, final boolean isPull ,String search) {
		String url = UrlMap.getThreeUrl(MCUrl.replacePay, "userId",String.valueOf(PreferencesUtils.getInt(MyPaidActivity.this, PreferenceConstants.UID)),
				"pageNo", String.valueOf(pageNo), "pageSize", String
						.valueOf(pageSize));
		Log.e("11111fjin", url);
		dialog.show();
		mList.clear();
		AsyncHttpUtils.doSimGet(url, new AsyncHttpResponseHandler() {
			@Override
			public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
				dialog.dismiss();
				mList2.clear();
				Log.e("11111daifu", new String(arg2));
				
				try {
					bean = new Gson().fromJson(new String(arg2),
							MyPaidBean.class);
					mList = bean.data;
				} catch (Exception e) {
					// TODO: handle exception
				}
				if (isFirst) {
					if (mList.size() != 0 && mList != null) {
//						if (adapter == null) {adapter = new NearbyAdapter(
//									getActivity(), mList);
						     adapter = new MyPaidAdapter(MyPaidActivity.this, mList);
							listView.setAdapter(adapter);
							view_null_message.setVisibility(View.GONE);
							freigh_listview.setVisibility(View.VISIBLE);
//						}
					} else {
						view_null_message.setVisibility(View.VISIBLE);
						freigh_listview.setVisibility(View.GONE);
					}
				} else {
					if (isRefresh && !isPull) {
						mList2.clear();
//						mList2 = bean.data;
						if (mList.size() != 0 && mList != null) {
//							mList2.addAll(mList);
//							Log.e("111111  ", mList2.size()+"");
								adapter = new MyPaidAdapter(
										MyPaidActivity.this, mList);
								listView.setAdapter(adapter);
								adapter.notifyDataSetChanged();
								freigh_listview.onRefreshComplete();
								view_null_message.setVisibility(View.GONE);
								freigh_listview.setVisibility(View.VISIBLE);
						} else {
							view_null_message.setVisibility(View.VISIBLE);
							freigh_listview.setVisibility(View.GONE);
						}
					} else if (!isRefresh && isPull) {
						num = mList.size();
						adapter.addData(mList);
						adapter.notifyDataSetChanged();
						freigh_listview.onRefreshComplete();
					}
				}

			}

			@Override
			public void onFailure(int arg0, Header[] arg1, byte[] arg2, Throwable arg3) {
				freigh_listview.setVisibility(View.GONE);
				view_null_message.setVisibility(View.VISIBLE);
				dialog.dismiss();
			}
		});
	}
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void getData() {
		freigh_listview.setMode(Mode.BOTH);
		freigh_listview.setOnRefreshListener(new OnRefreshListener2() {

			@Override
			public void onPullDownToRefresh(PullToRefreshBase refreshView) {
//				getHttpOwnerRequst(true, false, 1, false, sortType);
				getHttprequst(false, true, 1, false,"");
			}

			@Override
			public void onPullUpToRefresh(PullToRefreshBase refreshView) {
				pageNo++;
				getHttprequst(false, false, pageNo, true,"");
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
//				Data data = (Data) adapter.list.get(position - 1);
//				ToastUtil.shortToast(MyPaidActivity.this, "11111");
			
			}

		});

	}
	class MyPaidAdapter extends BaseListAdapter {

		public MyPaidAdapter(Context context, List list) {
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
			//informat_newitem   informat_item  新的改的
			return R.layout.item_mypaid;//informat_item  information_item  
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
			Button img_fukuai;
			

			@Bind(R.id.img_status)
			ImageView img_status;

			@Bind(R.id.iv_address)
			ImageView img_address;

			@Bind(R.id.delete_order) // 取消订单
			TextView delete_order;
			@Bind(R.id.te_yifukuai) // 已付款
			TextView te_yifukuai;
			@Bind(R.id.tv_zhao) // title
			TextView tv_zhao;
			
			

			// 接镖的，和取件的才能显示
			@Bind(R.id.iv_imgview)
			ImageView iv_imgview;
			private MyPaidBean bean;

			@Override
			public void setData(final int position) {
				super.setData(position);
				bean = new MyPaidBean();
				bean.data = list;
//				Log.e("BEAM", bean.data.get(position).toString());
				text_time.setText("发布时间："+bean.data.get(position).publishTime);
				text_peoplename.setText(bean.data.get(position).driverName);
				text_name.setText(bean.data.get(position).matName);
//				img_status.setBackgroundResource(R.drawable.newfabu);
				delete_order.setText("代付金额："+bean.data.get(position).transferMoney+"元");
				tv_zhao.setText(""+bean.data.get(position).personName+"("+bean.data.get(position).mobile+")"+"找你代付");
				if (bean.data.get(position).status.equals("0")) {
					img_fukuai.setVisibility(View.VISIBLE);
					te_yifukuai.setVisibility(View.GONE);
				}else {
					te_yifukuai.setVisibility(View.VISIBLE);
					img_fukuai.setVisibility(View.INVISIBLE);
				}
//				iv_imgview.setVisibility(View.VISIBLE);
				
//				img_phone.setOnClickListener(new OnClickListener() {
	//
//					@Override
//					public void onClick(View v) {
//						AppUtils.intentDial(context, bean.data.get(position).mobile);
	//
//					}
////				});
//				try {
//					double num1 = 0.0;
//					double num2 = 0.0;
//					num1 = Double.valueOf(bean.data.get(position).transferMoney);
//					num2 = Double.valueOf(bean.data.get(position).cargoCost);
//					DecimalFormat df = new DecimalFormat("###.00");  
//					if (!"".equals(getIntent().getStringExtra("baofei"))) {
//						delete_order.setText(""+num2+ " 元"+"(保额"+getIntent().getStringExtra("baofei")+"元)");
//					}else {
//						delete_order.setText(""+num2+ " 元");
//					}
//				
//					
//					delete_order.setText(""+df.format(sub(num1,num2))+" 元");
//					catch (Exception e) {
//						// TODO: handle exception
//					}
				
				img_fukuai.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						JSONObject obj = new JSONObject();
						try {
							obj.put("billCode", bean.data.get(position).billCode);
							obj.put("matName", null);
							obj.put("userId",  String
									.valueOf(PreferencesUtils.getInt(getApplicationContext(),
											PreferenceConstants.UID)));
							obj.put("matType", null);
							obj.put("insuranceFee", null);
							obj.put("insureMoney", null);
							obj.put("shipMoney", null);
							obj.put("needPayMoney",  bean.data.get(position).transferMoney);
							obj.put("weight", null);
							obj.put("userCouponId", "");
							// }

						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						Log.e("1111   余额obj", obj.toString());
						AsyncHttpUtils.doPut(context, MCUrl.BALANCE_MONEY, obj.toString(), null,
								new AsyncHttpResponseHandler() {
									@Override
									public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
										// TODO Auto-generated method
										// stub
										if (arg2 == null)
											return;
										Log.e("msg", new String(arg2));
										BaseBean bean = new Gson().fromJson(new String(arg2), BaseBean.class);
										if (bean.getErrCode() == 0) {
											ToastUtil.shortToast(context, bean.getMessage());
//											sendBroadcast(new Intent().setAction("freight").putExtra("flag", "owner"));//启动SendFreightActivity界面	
//											Intent intent=new Intent();
//											intent.putExtra("type", "2");
//											setResult(RESULT_OK, intent);
											finish();
											
										}
										if (bean.getErrCode() == -2) {
											DialogUtils.createAlertDialogTwo(context, "", bean.getMessage(), 0, "去充值",
													"更换支付方式", new DialogInterface.OnClickListener() {

														@Override
														public void onClick(DialogInterface dialog, int which) {

															context.startActivity(new Intent(context, RechargeActivity.class));
														}

													}).show();
										}
									}

									@Override
									public void onFailure(int arg0, Header[] arg1, byte[] arg2, Throwable arg3) {
										// TODO Auto-generated method
										// stub
									}
								});

					}
				});
				
			}
		}
			
		}
		
}
