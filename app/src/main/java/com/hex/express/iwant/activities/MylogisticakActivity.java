package com.hex.express.iwant.activities;

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
import com.hex.express.iwant.adapters.BaseListAdapter;
import com.hex.express.iwant.bean.BaseBean;
import com.hex.express.iwant.bean.LogisBean;
import com.hex.express.iwant.bean.MyloginBean;
import com.hex.express.iwant.bean.MyloginBean.Data;
import com.hex.express.iwant.constance.MCUrl;
import com.hex.express.iwant.constance.PreferenceConstants;
import com.hex.express.iwant.http.AsyncHttpUtils;
import com.hex.express.iwant.http.UrlMap;
import com.hex.express.iwant.newmain.MainTab;
import com.hex.express.iwant.newsmain.NewMainActivity;
import com.hex.express.iwant.utils.PreferencesUtils;
import com.hex.express.iwant.utils.ToastUtil;
import com.hex.express.iwant.views.MarqueeTextView;
import com.hex.express.iwant.views.TitleBarView;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
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
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import butterknife.Bind;
import butterknife.ButterKnife;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
/**
 * 
 * @author huyichuan
 *  我的物流界面
 */

public class MylogisticakActivity extends BaseActivity {

	@Bind(R.id.mylis_listview)
	PullToRefreshListView mylis_listview;
	@Bind(R.id.mylis_show)
	TitleBarView mylis_show;
	
	 ListView listView;
	@Bind(R.id.null_message)
	View view_null_message;
	protected int pageSize = 10;// 表示一页展示多少列
	protected int pageNo = 1;
	private List<MyloginBean.Data> mList;
	private List<MyloginBean.Data> mList2;
	private MylisAdapter adapter;
	private int num;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_down_mylogistical);
		ButterKnife.bind(MylogisticakActivity.this);
		initView();
		initData();
		setOnClick();
		delete();
	}
	@Override
	public void onWeightClick(View v) {
		// TODO Auto-generated method stub
		
	}
	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		initView();
		initData();
		setOnClick();
		delete();
	}
//	public void charse(){
//		ToastUtil.shortToast(MylogisticakActivity.this, "11111111");
//		adapter.notifyDataSetChanged();
//		getHttprequst(false, true, 1, false);
//		
//	}
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		initView();
		initData();
		setOnClick();
		delete();
	}

	@Override
	public void initView() {
		// TODO Auto-generated method stub
		mylis_show.setTitleText("我的物流");
		listView = mylis_listview.getRefreshableView();
		
	}

	@Override
	public void initData() {
		// TODO Auto-generated method stub
		mList = new ArrayList<MyloginBean.Data>();
		mList2=new ArrayList<MyloginBean.Data>();
		dialog.show();
		getHttprequst(true, false, 1, false);
	}
	/**
	 * 获取网络数据
	 */
	private void getHttprequst(final boolean isFirst, final boolean isRefresh,
			int pageNo, final boolean isPull) {
		RequestParams params = new RequestParams();
		Log.e("11111", UrlMap.getThreeUrl(MCUrl.LOGISTICSLIST, "userId", String
				.valueOf(PreferencesUtils.getInt(getApplicationContext(),
						PreferenceConstants.UID)), "pageNo", pageNo + "",
				"pageSize", String.valueOf(pageSize)));
		AsyncHttpUtils.doGet(UrlMap.getThreeUrl(MCUrl.LOGISTICSLIST, "userId", String
				.valueOf(PreferencesUtils.getInt(getApplicationContext(),
						PreferenceConstants.UID)), "pageNo", pageNo + "",
				"pageSize", String.valueOf(pageSize)), null, null, params,
				new AsyncHttpResponseHandler() {
					@Override
					public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
						Log.e("json", new String(arg2));
						dialog.dismiss();
						
						mList2.clear();
						MyloginBean bean = new Gson().fromJson(new String(arg2),
								MyloginBean.class);
						mList = bean.data;
						if (isFirst) {
							if (mList.size() != 0 && mList != null) {
								if (adapter == null) {
									adapter = new MylisAdapter(MylogisticakActivity.this, mList);
									listView.setAdapter(adapter);
								}
							} else {
								view_null_message.setVisibility(View.VISIBLE);
								mylis_listview.setVisibility(View.GONE);
							}
						} else {
							if (isRefresh && !isPull) {
//								mList.clear();
								mList = bean.data;
									if (mList.size() != 0 && mList != null) {
											mylis_listview.onRefreshComplete();
											adapter = new MylisAdapter(MylogisticakActivity.this, mList);
											listView.setAdapter(adapter);
											
									} else {
										view_null_message.setVisibility(View.VISIBLE);
										mylis_listview.setVisibility(View.GONE);
									}
//								mList2.clear();
//								mList2.addAll(mList);
//								adapter.setData(mList2);
//								adapter.notifyDataSetInvalidated();//这个方法会重新刷新listview的界面
//								adapter.notifyDataSetChanged();
//								mylis_listview.onRefreshComplete();
							} else if (!isRefresh && isPull) {
								num = mList.size();
//								mList2.clear();
//								mList2.addAll(mList);
								adapter.setData(mList);
								adapter.notifyDataSetChanged();
								mylis_listview.onRefreshComplete();
							}
						}

					}

					@Override
					public void onFailure(int arg0, Header[] arg1, byte[] arg2,
							Throwable arg3) {
						dialog.dismiss();
						view_null_message.setVisibility(View.VISIBLE);
						mylis_listview.setVisibility(View.GONE);
						ToastUtil.shortToast(MylogisticakActivity.this, "网络请求加载失败");

					}
				});

	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public void setOnClick() {

		// 下拉刷新与上拉加载
		mylis_listview.setMode(Mode.BOTH);
		mylis_listview.setOnRefreshListener(new OnRefreshListener2() {

			@Override
			public void onPullDownToRefresh(PullToRefreshBase refreshView) {
				getHttprequst(false, true, 1, false);
//				getHttprequst(true, false, 1, false);
			}

			@Override
			public void onPullUpToRefresh(PullToRefreshBase refreshView) {
				pageNo++;
				getHttprequst(false, false, pageNo, true);
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
//		mylis_listview.setOnItemClickListener(new OnItemClickListener() {
//
//			@Override
//			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
//					long arg3) {
////				Data data = mList.get(arg2 - 1);
////				Intent intent = new Intent();
//				Log.e("bb", "bbbbb");
////				if (!data.ifUsed && gift == true) {
////					if (data.money != 0 && data.userCouponId != 0) {
////						intent.putExtra("money", data.money);
////						intent.putExtra("userCouponId", data.userCouponId);
////						Log.e("vvv", "ggggg");
////						setResult(RESULT_OK, intent);
////						finish();
////					}
////				}
//
//			}
//		});

//		mylis_listview.setOnItemClickListener(new OnItemClickListener() {
//
//			@Override
//			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
//				// TODO Auto-generated method stub
////				Data data = (Data) adapter.list.get(arg2 - 1);
////				Intent intent = new Intent(MylogisticakActivity.this, LogisticalQuoteActivity.class);
////				startActivity(intent);
//				// TODO Auto-generated method stub
//				Data data = (Data) adapter.deleteData().get(arg2 - 1);
//				final int recId = data.recId;
//				Log.e("111111", recId+"");
//			}
//		});
	}

	private boolean gift;
	//长按删除事件
		public void delete() {
//			listView.setOnItemLongClickListener(new OnItemLongClickListener() {
//				@Override
//				public boolean onItemLongClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
//					// TODO Auto-generated method stub
//					Data data = (Data) adapter.deleteData().get(arg2 - 1);
//					final int recId = data.recId;
//					Log.e("111111", recId+"");
////					if (!data.status.equals("3") && !data.status.equals("4") && !data.status.equals("5")) {
//					
////					AlertDialog.Builder builder = new AlertDialog.Builder(
////							MylogisticakActivity.this);
////						builder.setItems(R.array.thread_menu, new DialogInterface.OnClickListener() {
////							
////							public void onClick(DialogInterface dialog, int which) {
////								Log.e("json", MCUrl.LOGISTICSLIST_DEKETE + recId);
////								JSONObject obj = new JSONObject();
////								AsyncHttpUtils.doPut(MylogisticakActivity.this,
////										MCUrl.LOGISTICSLIST_DEKETE +String.valueOf(recId),
////										obj.toString(), null,new AsyncHttpResponseHandler() {
////
////											@Override
////											public void onFailure(int arg0,
////													Header[] arg1, byte[] arg2,
////													Throwable arg3) {
////												// TODO Auto-generated method stub
////											}
////											@Override
////											public void onSuccess(int arg0,
////													Header[] arg1, byte[] arg2) {
////												Log.e("JSON", adapter.deleteData().size()+"");
////												getHttprequst(false, true, 1, false);
////												Log.e("JSONtttttttt", adapter.deleteData().size()+"");
////											}
////
////										});
////							}
////						});
////						builder.show();
////					}
//						return true;
//				}
//			});
		
		}
	@Override
	public void getData() {
		
		gift = getIntent().getBooleanExtra("gift", false);
	}

	 class MylisAdapter extends BaseListAdapter{

			public MylisAdapter(Context context, List list) {
				super(context, list);
				// TODO Auto-generated constructor stub
			}

			@Override
			public ViewHolder onCreateViewHolder(View itemView) {
				// TODO Auto-generated method stub
				return new MylisViewHoder(itemView);
			}

			@Override
			public int getLayoutResource() {
				// TODO Auto-generated method stub   mylogist_item  mylogist_itemes
				return R.layout.mylogist_itemes;  //mylogistical_item
			}
			class MylisViewHoder extends ViewHolder{

				public MylisViewHoder(View itemView) {
					super(itemView);
					// TODO Auto-generated constructor stub
				}

				@Bind(R.id.item_wupin)
				TextView item_wupin;
				@Bind(R.id.item_zhongliang)
				TextView item_zhongliang;
				@Bind(R.id.item_tiji)
				TextView item_tiji;
//				@Bind(R.id.item_dizhi1)
//				TextView item_dizhi1;
//				@Bind(R.id.item_quhuo)
//				TextView item_quhuo;
				@Bind(R.id.item_dizhi2)
				MarqueeTextView item_dizhi2;
//				@Bind(R.id.item_songhuo)
//				TextView item_songhuo;
				@Bind(R.id.item_name)
				TextView item_name;
				@Bind(R.id.item_phone)
				TextView item_phone;
				@Bind(R.id.item_time)
				TextView item_time;
				@Bind(R.id.item_xin)//
				TextView item_xin;
				@Bind(R.id.item_weizhifu)//未支付图片
				ImageView item_weizhifu;
				
				@Bind(R.id.button1)
				Button button1;
				@Bind(R.id.button2)
				Button button2;
				
				@Bind(R.id.butt_wan)//中间的
				Button butt_wan;
				@Bind(R.id.textView1)//  报价图标
				TextView textView1;
				@Bind(R.id.item_time2)
				TextView item_time2;
				@Bind(R.id.linearLayout1)
				LinearLayout linearLayout1;
				private MyloginBean bean;
       //status;//物流状态  0 已发布(还没有支付手续费), 1 已报价, 2 已支付, 3 已接货, 4 已送达, 5 已评价  6 用户已支付发布时的手续费
				//  7  9是分担保交易  8就选它了
				
				@Override
				public void setData(final int position) {
					// TODO Auto-generated method stub
					super.setData(position);
					bean = new MyloginBean();
					bean.data = list;
					item_wupin.setText("物品："+bean.data.get(position).cargoName);
					item_zhongliang.setText("重量："+bean.data.get(position).cargoWeight);
					item_tiji.setText("体积："+bean.data.get(position).cargoVolume+"立方米");
//					item_dizhi1.setText(""+bean.getData().get(position).startPlace);
					item_dizhi2.setText(""+bean.data.get(position).entPlace);
					item_name.setText("收货人："+bean.data.get(position).takeName);
					item_phone.setText("手机号："+bean.data.get(position).takeMobile);
					item_time.setText(""+bean.data.get(position).publishTime);
					item_xin.setText("+"+bean.data.get(position).quotationNumber);
					
					//中间的按钮
//					butt_wan.setVisibility(View.GONE);
//					item_weizhifu.setTag(tag);
					if (bean.data.get(position).status.equals("1")) {
						item_weizhifu.setVisibility(View.VISIBLE);
						item_weizhifu.setBackgroundResource(R.drawable.status_1sx);
						button1.setVisibility(View.VISIBLE);
						button2.setVisibility(View.VISIBLE);
						butt_wan.setVisibility(View.GONE);
						item_xin.setVisibility(View.VISIBLE);
						textView1.setVisibility(View.VISIBLE);
					}else 
						if(bean.data.get(position).status.equals("6")) {
						item_weizhifu.setVisibility(View.VISIBLE);
						item_weizhifu.setBackgroundResource(R.drawable.status_1x);
						button1.setVisibility(View.VISIBLE);
						button2.setVisibility(View.VISIBLE);
						butt_wan.setVisibility(View.GONE);
						item_xin.setVisibility(View.VISIBLE);
						textView1.setVisibility(View.VISIBLE);
					}else
					if (bean.data.get(position).status.equals("2")) {
						item_weizhifu.setVisibility(View.VISIBLE);
						item_weizhifu.setBackgroundResource(R.drawable.status_2x);
						button1.setVisibility(View.GONE);
						button2.setVisibility(View.GONE);
						item_xin.setVisibility(View.GONE);
						textView1.setVisibility(View.GONE);
//						butt_wan.setBackgroundResource(R.drawable.danbao);
//						item_time.setVisibility(View.GONE);
						butt_wan.setVisibility(View.VISIBLE);
					}else
					if (bean.data.get(position).status.equals("8")) {
						item_weizhifu.setVisibility(View.VISIBLE);
						item_weizhifu.setBackgroundResource(R.drawable.status_8x);
						button1.setBackgroundResource(R.drawable.xianxia);
						button2.setBackgroundResource(R.drawable.danbao);
						button1.setVisibility(View.VISIBLE);
						button2.setVisibility(View.VISIBLE);
						item_xin.setVisibility(View.GONE);
						butt_wan.setVisibility(View.GONE);
						textView1.setVisibility(View.INVISIBLE);
						
					}else
					if (bean.data.get(position).status.equals("4") ) {
						item_weizhifu.setVisibility(View.VISIBLE);
						item_weizhifu.setBackgroundResource(R.drawable.status_7xs);
						button1.setVisibility(View.GONE);
						button2.setVisibility(View.GONE);
						item_xin.setVisibility(View.GONE);
						textView1.setVisibility(View.INVISIBLE);
//						item_time.setVisibility(View.GONE);
						butt_wan.setVisibility(View.VISIBLE);
//						item_time2.setVisibility(View.VISIBLE);
//						item_time2.setText(""+bean.getData().get(position).publishTime);
					}else
					if (bean.data.get(position).status.equals("5")) {
						item_weizhifu.setVisibility(View.VISIBLE);
						item_weizhifu.setBackgroundResource(R.drawable.status_7xs);
						button1.setVisibility(View.GONE);
						button2.setVisibility(View.GONE);
						item_xin.setVisibility(View.GONE);
						textView1.setVisibility(View.INVISIBLE);
//						item_time.setVisibility(View.GONE);
						butt_wan.setVisibility(View.VISIBLE);
//						item_time2.setVisibility(View.VISIBLE);
//						item_time2.setText(""+bean.getData().get(position).publishTime);
					}else
					if (bean.data.get(position).status.equals("7")) {
						item_weizhifu.setVisibility(View.VISIBLE);
						item_weizhifu.setBackgroundResource(R.drawable.status_7x);
						button1.setVisibility(View.GONE);
						button2.setVisibility(View.GONE);
						item_xin.setVisibility(View.GONE);
						textView1.setVisibility(View.INVISIBLE);
						butt_wan.setBackgroundResource(R.drawable.danbao);
//						item_time.setVisibility(View.GONE);
						butt_wan.setVisibility(View.VISIBLE);
//						item_time2.setVisibility(View.VISIBLE);
//						item_time2.setText(""+bean.getData().get(position).publishTime);
					}else
					if (bean.data.get(position).status.equals("3")) {
//						button1.setBackgroundResource(R.drawable.search_bar_dui);
						item_weizhifu.setVisibility(View.VISIBLE);
						button1.setVisibility(View.GONE);
						button2.setVisibility(View.GONE);
						item_xin.setVisibility(View.GONE);
						textView1.setVisibility(View.INVISIBLE);
						butt_wan.setBackgroundResource(R.drawable.search_barkan);
						item_weizhifu.setBackgroundResource(R.drawable.status_3x);
						butt_wan.setVisibility(View.VISIBLE);
//						item_time2.setVisibility(View.VISIBLE);
//						item_time2.setText(""+bean.getData().get(position).publishTime);
					}else
					if (bean.data.get(position).status.equals("9")) {
							item_weizhifu.setVisibility(View.VISIBLE);
							item_weizhifu.setBackgroundResource(R.drawable.status_7x);
							button1.setVisibility(View.GONE);
							button2.setVisibility(View.GONE);
							textView1.setVisibility(View.INVISIBLE);
							item_xin.setVisibility(View.GONE);
							butt_wan.setVisibility(View.VISIBLE);
//							item_time2.setVisibility(View.VISIBLE);
//							item_time2.setText(""+bean.getData().get(position).publishTime);
						}else {
							butt_wan.setVisibility(View.GONE);
						}
//					if (bean.getData().get(position).status.equals("4")) {
////						button1.setBackgroundResource(R.drawable.search_bar_dui);
//						button1.setVisibility(View.GONE);
//						button2.setVisibility(View.GONE);
//						textView1.setVisibility(View.INVISIBLE);
//						item_time.setVisibility(View.GONE);
//						item_xin.setVisibility(View.INVISIBLE);
//						butt_wan.setBackgroundResource(R.drawable.search_barwancheng);
//						butt_wan.setVisibility(View.VISIBLE);
//						item_time2.setVisibility(View.VISIBLE);
//						item_time2.setText(""+bean.getData().get(position).publishTime);
//					}
					button1.setOnClickListener(new OnClickListener() {
						@Override
						public void onClick(View arg0) {
							
							// TODO Auto-generated method stub
							
							if (bean.data.get(position).status.equals("8")) {
								Builder ad = new Builder(MylogisticakActivity.this);
								ad.setTitle("温馨提示");
								ad.setMessage("确定选择线下交易吗？");
								ad.setNegativeButton("确认", new DialogInterface.OnClickListener() {
									@Override
									public void onClick(DialogInterface arg0, int arg1) {
											if (!bean.data.get(position).userToId.equals("")) {
												int  iid=bean.data.get(position).recId;
												String  userToId=bean.data.get(position).userToId;
//												addPostResult(iid);
//												ToastUtil.shortToast(context, "查看报价");;
												getaddsurplus(iid,userToId);
											}
										
									}
								});
								ad.setPositiveButton("取消", new DialogInterface.OnClickListener() {
									@Override
									public void onClick(DialogInterface arg0, int arg1) {
										arg0.dismiss();
										
									}
								});
								ad.create().show();
							}else {
							
							Builder ad = new Builder(MylogisticakActivity.this);
							ad.setTitle("温馨提示");
							ad.setMessage("是否要删除");
							ad.setNegativeButton("确认", new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface arg0, int arg1) {
									int  iid=bean.data.get(position).recId;
									addPostResult(iid);
//									ToastUtil.shortToast(context, "查看报价");;
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
					
					linearLayout1.setOnLongClickListener(new View.OnLongClickListener() {
						
						@Override
						public boolean onLongClick(View arg0) {
							// TODO Auto-generated method stub
					if (!bean.data.get(position).status.equals("3") && !bean.data.get(position).status.equals("4") && !bean.data.get(position).status.equals("5") && !bean.data.get(position).status.equals("9")) {
							Builder ad = new Builder(MylogisticakActivity.this);
							ad.setTitle("温馨提示");
							ad.setMessage("是否取消发布");
							ad.setNegativeButton("确认", new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface arg0, int arg1) {
									int  iid=bean.data.get(position).recId;
									addPostResult(iid);
//									ToastUtil.shortToast(context, "查看报价");;
								}
							});
							ad.setPositiveButton("取消", new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface arg0, int arg1) {
									arg0.dismiss();
								}
							});
							ad.create().show();
							}else {
								
							
					if(bean.data.get(position).status.equals("4") || bean.data.get(position).status.equals("5") || bean.data.get(position).status.equals("9") || bean.data.get(position).status.equals("7")){
						Builder ad = new Builder(MylogisticakActivity.this);
						ad.setTitle("温馨提示");
						ad.setMessage("是否删除此条记录");
						ad.setNegativeButton("确认", new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface arg0, int arg1) {
								int  iid=bean.data.get(position).recId;
								addPostResult(iid);
//								ToastUtil.shortToast(context, "查看报价");;
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
							return true;
						}
					});
					butt_wan.setOnClickListener(new OnClickListener() {
						@Override
						public void onClick(View arg0) {
							Intent intent=new Intent();
							// TODO Auto-generated method stub
//							ToastUtil.shortToast(context, "查看报价人员");
//							intent.putExtra("cityCode", cityCode);
//							intent.putExtra("latitude", latitude);
							intent.putExtra("cargoName", bean.data.get(position).cargoName);
							intent.putExtra("cargoWeight", bean.data.get(position).cargoWeight);
							intent.putExtra("cargoVolume", bean.data.get(position).cargoVolume);
							intent.putExtra("startPlace", bean.data.get(position).startPlace);
							intent.putExtra("entPlace", bean.data.get(position).entPlace);
							intent.putExtra("takeName", bean.data.get(position).takeName);
							intent.putExtra("takeMobile", bean.data.get(position).takeMobile);
							intent.putExtra("publishTime", bean.data.get(position).publishTime);
							intent.putExtra("takeCargo", bean.data.get(position).takeCargo);
							intent.putExtra("sendCargo", bean.data.get(position).sendCargo);
							intent.putExtra("remark", bean.data.get(position).remark);
							intent.putExtra("takeTime", bean.data.get(position).takeTime);
							intent.putExtra("arriveTime", bean.data.get(position).arriveTime);
							intent.putExtra("recId", bean.data.get(position).recId);
							intent.putExtra("billCode", bean.data.get(position).billCode);
							intent.putExtra("whether", bean.data.get(position).whether);
							intent.putExtra("premium", bean.data.get(position).premium);
							intent.putExtra("status", bean.data.get(position).status);
							intent.putExtra("cargoCost", bean.data.get(position).cargoCost);
							intent.putExtra("sendPerson", bean.data.get(position).sendPerson);
							intent.putExtra("sendPhone", bean.data.get(position).sendPhone);
							
							intent.putExtra("cargoNumber", bean.data.get(position).cargoNumber);
//							intent.setClass(MainActivity.this, MyDownwindActivity.class);
//							intent.setClass(context, LogistiOfferActivity.class);//
							intent.setClass(context, LogistioffActivity.class);//
//							if (!bean.getData().get(position).status.equals("2")) {
								context.startActivity(intent);
//							}else {
//								ToastUtil.shortToast(context, "此单已完成");
//							}
							
						}
					});
					button2.setOnClickListener(new OnClickListener() {
						@Override
						public void onClick(View arg0) {
							Intent intent=new Intent();
							// TODO Auto-generated method stub
//							ToastUtil.shortToast(context, "查看报价人员");
//							intent.putExtra("cityCode", cityCode);
//							intent.putExtra("latitude", latitude);
							intent.putExtra("cargoName", bean.data.get(position).cargoName);
							intent.putExtra("cargoWeight", bean.data.get(position).cargoWeight);
							intent.putExtra("cargoVolume", bean.data.get(position).cargoVolume);
							intent.putExtra("startPlace", bean.data.get(position).startPlace);
							intent.putExtra("entPlace", bean.data.get(position).entPlace);
							intent.putExtra("takeName", bean.data.get(position).takeName);
							intent.putExtra("takeMobile", bean.data.get(position).takeMobile);
							intent.putExtra("publishTime", bean.data.get(position).publishTime);
							intent.putExtra("takeCargo", bean.data.get(position).takeCargo);
							intent.putExtra("sendCargo", bean.data.get(position).sendCargo);
							intent.putExtra("remark", bean.data.get(position).remark);
							intent.putExtra("takeTime", bean.data.get(position).takeTime);
							intent.putExtra("arriveTime", bean.data.get(position).arriveTime);
							intent.putExtra("recId", bean.data.get(position).recId);
							intent.putExtra("billCode", bean.data.get(position).billCode);
							intent.putExtra("whether", bean.data.get(position).whether);
							intent.putExtra("premium", bean.data.get(position).premium);
							intent.putExtra("status", bean.data.get(position).status);
							intent.putExtra("sendPerson", bean.data.get(position).sendPerson);
							intent.putExtra("sendPhone", bean.data.get(position).sendPhone);

							intent.putExtra("cargoNumber", bean.data.get(position).cargoNumber);
//							intent.putExtra("cargoCost", bean.data.get(position).cargoCost);//价值
//							intent.putExtra("insureCost", bean.data.get(position).insureCost);//保费
//							intent.setClass(MainActivity.this, MyDownwindActivity.class);
							if (bean.data.get(position).status.equals("2") || bean.data.get(position).status.equals("8")) {
								intent.setClass(context, LogistioffActivity.class);
								context.startActivity(intent);
							}else {
								intent.setClass(context, LogistiOfferActivity.class);//用
								context.startActivity(intent);
//								finish();
							}
							
						}
					});
			
				}

				}
			private void addPostResult(int  messageId) {
				JSONObject obj = new JSONObject();
				
//				try {
//					obj.put("messageId",String.valueOf(messageId));
//				} catch (JSONException e) {
//					e.printStackTrace();
//				}
				Log.e("查看数据", obj.toString());//doPostJson     dosPut
				Log.e("111", MCUrl.LOGISTICSLIST_DEKETE + String.valueOf(messageId));
				AsyncHttpUtils.doPut(context,
						MCUrl.LOGISTICSLIST_DEKETE +String.valueOf(messageId),
						obj.toString(), null,new AsyncHttpResponseHandler() {
//				AsyncHttpUtils.dosPut(context, MCUrl.LOGISTICSLIST_DEKETE, obj.toString(),
//						new AsyncHttpResponseHandler() {
							@Override
							public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
								Log.e("11111111111 wwww   ", new String(arg2));
								LogisBean bean = new Gson().fromJson(new String(arg2), LogisBean.class);
//								Log.e("oppop", bean.data.toString());
								ToastUtil.shortToast(context, bean.getMessage());
								adapter.notifyDataSetChanged();
								getHttprequst(false, true, 1, false);
							}

							@Override
							public void onFailure(int arg0, Header[] arg1, byte[] arg2, Throwable arg3) {
							}
						});

			}
			 /**
			 * 非担保交易
			 */
			public void getaddsurplus(int recid,String userToId) {
//				if (getIntent().getStringExtra("billCode").equals("") || getIntent().getStringExtra("transferMoney").equals("")) {
//					ToastUtil.shortToast(getApplicationContext(), "该订单有问题");
//					return;
//				}
				JSONObject obj = new JSONObject();
				try {
					obj.put("userId",getIntent().getIntExtra("userId", 0));
					obj.put("billCode", getIntent().getStringExtra("billCode"));
					obj.put("whether", ""+0);
					obj.put("premium", getIntent().getStringExtra("premium"));
					obj.put("WLBId", getIntent().getIntExtra("recId", 0));
//					obj.put("WLBId", getIntent().getIntExtra("recId", 0));
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				Log.e("obj", UrlMap.getfive(MCUrl.Balance, "userId",""+getIntent().getIntExtra("userId", 0),"WLBId",""+getIntent().getIntExtra("WLBId", 0), 
						"whether",""+false,"warrant",""+0,"transferMoney", getIntent().getStringExtra("transferMoney")));
				RequestParams params = new RequestParams();
				dialog.show();
//				AsyncHttpUtils.doGet(UrlMap.getfive(MCUrl.Balance, "userId",""+getIntent().getIntExtra("userId", 0),"WLBId",""+getIntent().getIntExtra("WLBId", 0), 
//						"whether",""+0,"warrant",""+0,"transferMoney", getIntent().getStringExtra("transferMoney")), null, null, params,
//						new AsyncHttpResponseHandler() {
					AsyncHttpUtils.doGet(UrlMap.getfive(MCUrl.Balance, "userId",""+userToId,"WLBId",""+recid, 
							"whether",""+0,"warrant",""+0,"transferMoney",""+0), null, null, params,
							new AsyncHttpResponseHandler() {
							@Override
							public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
								// TODO Auto-generated method
								dialog.dismiss();
								Intent intent=new Intent();
								if (arg2 == null)
									return;
								Log.e("msg", new String(arg2));
								BaseBean bean = new Gson().fromJson(new String(arg2), BaseBean.class);
								if (bean.getErrCode() == 0) {
									ToastUtil.shortToast(MylogisticakActivity.this, bean.getMessage());
//									sendBroadcast(new Intent().setAction("freight").putExtra("flag", "owner"));//启动SendFreightActivity界面	
//									intent.setClass(MylogisticakActivity.this, MainActivity.class);//个人
//									intent.setClass(MylogisticakActivity.this, MainTab.class);//个人
									intent.setClass(MylogisticakActivity.this, NewMainActivity.class);//个人
									
									startActivity(intent);
									finish();
								}
							}

							@Override
							public void onFailure(int arg0, Header[] arg1, byte[] arg2, Throwable arg3) {
								// TODO Auto-generated method
								// stub
								Log.e("1111111", ""+arg0);
								dialog.dismiss();
							}
						});
			}
		}
	

}
