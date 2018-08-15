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
import com.hex.express.iwant.activities.MylogisticakActivity.MylisAdapter;
import com.hex.express.iwant.adapters.BaseListAdapter;
import com.hex.express.iwant.bean.FreigBean.Data;
import com.hex.express.iwant.bean.BaseBean;
import com.hex.express.iwant.bean.FreigBean;
import com.hex.express.iwant.constance.MCUrl;
import com.hex.express.iwant.constance.PreferenceConstants;
import com.hex.express.iwant.http.AsyncHttpUtils;
import com.hex.express.iwant.http.UrlMap;
import com.hex.express.iwant.utils.PreferencesUtils;
import com.hex.express.iwant.utils.ToastUtil;
import com.hex.express.iwant.views.MarqueeTextView;
import com.hex.express.iwant.views.TitleBarView;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import android.R.integer;
import android.app.Activity;
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
 * @author huyichuan
 * 物流货运（公司）
 */
public class LogisticalFreightActivity extends BaseActivity{
	@Bind(R.id.freigh_listview)
	PullToRefreshListView freigh_listview;
	@Bind(R.id.btnLeft)
	ImageView btnLeft;
	@Bind(R.id.btnRight)
	TextView btnRight;
	private ListView listView;
	@Bind(R.id.null_message)
	View view_null_message;
	protected int pageSize = 10;// 表示一页展示多少列
	protected int pageNo = 1;
	private List<FreigBean.Data> mList;
	private List<FreigBean.Data> mList2;
//	private MylisFreighAdapter adapter;
	private MylositaAdapter adapter;
	public FreigBean bean;
	private int num;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.logistiofreigh);
		ButterKnife.bind(this);
		initView();
		initData();
		setOnClick();
		getData();
	}
	@Override
	public void onWeightClick(View v) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void initView() {
		// TODO Auto-generated method stub
//		freigh_show.setTitleText("我的物流");
		listView = freigh_listview.getRefreshableView();
		btnLeft.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				finish();
			}
		});
		btnRight.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intent=new Intent();
				intent.setClass(LogisticalFreightActivity.this, DepotFreigActivity.class);
				startActivity(intent);
				
			}
		});
	}

	@Override
	public void initData() {
		// TODO Auto-generated method stub
		mList = new ArrayList<FreigBean.Data>();
		mList2 = new ArrayList<FreigBean.Data>();
		dialog.show();
		getHttprequst(true, false, 1, false);
	}
	/**
	 * 获取网络数据
	 */
	private void getHttprequst(final boolean isFirst, final boolean isRefresh,
			int pageNo, final boolean isPull) {
		RequestParams params = new RequestParams();
		Log.e("数据", UrlMap.getThreeUrl(MCUrl.TASKFIND, "userId", String
				.valueOf(PreferencesUtils.getInt(getApplicationContext(),
						PreferenceConstants.UID)), "pageNo", pageNo + "",
				"pageSize", String.valueOf(pageSize)));
		
		AsyncHttpUtils.doGet(UrlMap.getThreeUrl(MCUrl.TASKFIND, "userId", String
				.valueOf(PreferencesUtils.getInt(getApplicationContext(),
						PreferenceConstants.UID)), "pageNo", pageNo + "",
				"pageSize", String.valueOf(pageSize)), null, null, params,
				new AsyncHttpResponseHandler() {
					@Override
					public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
						Log.e("11111111json", new String(arg2));
						dialog.dismiss();
						mList2.clear();
						bean = new Gson().fromJson(new String(arg2),
								FreigBean.class);
						mList = bean.data;
//						ToastUtil.shortToast(LogisticalFreightActivity.this, bean.message);
						if (isFirst) {
							if (mList.size() != 0 && mList != null) {
								if (adapter == null) {
									adapter = new MylositaAdapter(
											LogisticalFreightActivity.this, mList);
									listView.setAdapter(adapter);
								}
							} else {
								view_null_message.setVisibility(View.VISIBLE);
								freigh_listview.setVisibility(View.GONE);
							}
						} else {
							if (isRefresh && !isPull) {
								mList = bean.data;
								if (mList.size() != 0 && mList != null) {
									   freigh_listview.onRefreshComplete();
										adapter = new MylositaAdapter(
												LogisticalFreightActivity.this, mList);
										listView.setAdapter(adapter);
										
								} else {
									view_null_message.setVisibility(View.VISIBLE);
									freigh_listview.setVisibility(View.GONE);
								}
//								if (mList.size() != 0 && mList != null) {
//								mList2.clear();
//								mList2.addAll(mList);
//								adapter.setData(mList2);
//								adapter.notifyDataSetChanged();
//								freigh_listview.onRefreshComplete();
//								}
							} else if (!isRefresh && isPull) {
								num = mList.size();
								adapter.addData(mList);
								adapter.notifyDataSetChanged();
								freigh_listview.onRefreshComplete();
							}
						}

					}

					@Override
					public void onFailure(int arg0, Header[] arg1, byte[] arg2,
							Throwable arg3) {
						dialog.dismiss();
						ToastUtil.shortToast(LogisticalFreightActivity.this, "网络请求加载失败");

					}
				});

	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public void setOnClick() {

		// 下拉刷新与上拉加载
		freigh_listview.setMode(Mode.BOTH);
		freigh_listview.setOnRefreshListener(new OnRefreshListener2() {

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
					ILoadingLayout proxy = refreshView.getLoadingLayoutProxy(
							false, true);
					proxy.setPullLabel("没有更多数据了");
					proxy.setRefreshingLabel("没有更多数据了");
					proxy.setReleaseLabel("没有更多数据了");
				}
			}
		});
		freigh_listview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				Data data = mList.get(arg2 - 1);
				Intent intent = new Intent();
				Log.e("bb", "bbbbb");

			}
		});
			listView.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
					// TODO Auto-generated method stub
					Intent intent=new Intent();
					intent.putExtra("downwind", "escort");
			///			LogCompanyDeliveryActivity      LogisticalFreightActivity
					intent.setClass(LogisticalFreightActivity.this, LogCompanyDeliveryActivity.class);//公司
					startActivity(intent);
				}
			});
	}

	private boolean gift;

	@Override
	public void getData() {
		gift = getIntent().getBooleanExtra("gift", false);
	}

	/**
	 * 内部类 adapter
	 */
	
	class MylositaAdapter extends BaseListAdapter{

		public MylositaAdapter(Context context, List list) {
			super(context, list);
			// TODO Auto-generated constructor stub
		}

		@Override
		public ViewHolder onCreateViewHolder(View itemView) {
			// TODO Auto-generated method stub
			return new MysViewHolder(itemView);
		}

		@Override
		public int getLayoutResource() {
			// TODO Auto-generated method stub
			return R.layout.mylosit_itemse;//mylosita_itemse// mylosit_itemse
		}

		class MysViewHolder extends ViewHolder {

			public MysViewHolder(View itemView) {
				super(itemView);
			}
			@Bind(R.id.infor_title_juli)//距离、、、、未付款
			TextView infor_title_juli;
			@Bind(R.id.infor_title_time)//时间
			TextView infor_title_time;
			@Bind(R.id.infor_title_wupin)//物品名字
			TextView infor_title_wupin;
			@Bind(R.id.infor_title_zhongliang)// 重量
			TextView infor_title_zhongliang;
			@Bind(R.id.infor_title_tiji)//体积
			TextView infor_title_tiji;
			@Bind(R.id.infor_title_jiazhi)//价值
			TextView infor_title_jiazhi;
			@Bind(R.id.infor_title_songhuodizhi)//送货地址
			MarqueeTextView infor_title_songhuodizhi;
			@Bind(R.id.infor_title_quhuo)//取货操作
			TextView infor_title_quhuo;
//			@Bind(R.id.infor_title_quhuo_time)//取货时间
//			TextView infor_title_quhuo_time;
			
			@Bind(R.id.infor_title_daodadizhi)//达到地址
			MarqueeTextView infor_title_daodadizhi;
			@Bind(R.id.infor_title_songhuo)//送货操作
			TextView infor_title_songhuo;
			@Bind(R.id.infor_title_songhuo_time)//送货时间
			TextView infor_title_songhuo_time;
			@Bind(R.id.btn_zb)//是否中标
			Button btn_zb;
			@Bind(R.id.btn_zbdian)//中标后面的点
			Button btn_zbdian;
			@Bind(R.id.btn_fh)//已发货
			Button btn_fh;
			@Bind(R.id.btn_fhdian)//已发货后面的点
			Button btn_fhdian;
			@Bind(R.id.btn_dd)//已完成
			Button btn_dd;
//		
//status;//物流状态  0 已发布(还没有支付手续费), 1 已报价, 2 已支付, 3 已接货, 4 已送达, 5 已评价  6 用户已支付发布时的手续费
			private FreigBean bean;

			@Override
			public void setData(final int position) {
				super.setData(position);
				bean = new FreigBean();
				bean.data = list;
				Log.e("BEAM", bean.data.get(position).toString());
			
				infor_title_time.setText(bean.getData().get(position).publishTime);
				infor_title_wupin.setText("物品："+bean.getData().get(position).cargoName);
				infor_title_zhongliang.setText("重量："+bean.getData().get(position).weight);
				infor_title_tiji.setText("体积："+bean.getData().get(position).cargoVolume+"方");
//				infor_title_jiazhi.setText("价值："+bean.getData().get(position).cargoCost+"元");
				infor_title_songhuodizhi.setText(bean.getData().get(position).startPlace);
//				infor_title_quhuo_time.setText(bean.getData().get(position).takeTime);
				infor_title_songhuo_time.setText(bean.getData().get(position).arriveTime);
//				infor_title_quhuo.setText(bean.getData().get(position).entPlace);
				if (bean.getData().get(position).takeCargo) {
					infor_title_quhuo.setText("上门取货：是");
				}else {
					infor_title_quhuo.setText("上门取货：否");
////					infor_title_quhuo.setVisibility(View.GONE);
//					infor_title_quhuo.setTextColor(R.color.graywhite);
				}
				if (bean.getData().get(position).sendCargo) {
					infor_title_songhuo.setText("送货上门：是");
				}else {
					infor_title_songhuo.setText("送货上门：否");
//					infor_title_songhuo.setTextColor(R.color.graywhite);
//					infor_title_songhuo.setVisibility(View.GONE);
				}
				infor_title_daodadizhi.setText(bean.getData().get(position).entPlace);
//				infor_title_songhuo.setText(bean.getData().get(position).distance);
	//status;//物流状态  0 已发布(还没有支付手续费), 1 已报价, 2 已支付, 3 已接货, 4 已送达, 5 已评价  6 用户已支付发布时的手续费
//				if (bean.getData().get(position).status.equals("3")) {
//				}
				if (bean.getData().get(position).status==9 || bean.getData().get(position).status==7) {
					infor_title_juli.setVisibility(View.VISIBLE);
					infor_title_juli.setText("非担保交易");
//					btn_bj..setVisibility(View.INVISIBLE);
					btn_zb.setBackgroundResource(R.drawable.logisyzbs);
					btn_fh.setVisibility(View.INVISIBLE);
					btn_dd.setVisibility(View.INVISIBLE);
					btn_zbdian.setVisibility(View.INVISIBLE);
					btn_fhdian.setVisibility(View.INVISIBLE);
				}else {
					btn_fh.setVisibility(View.VISIBLE);
					btn_dd.setVisibility(View.VISIBLE);
					btn_zbdian.setVisibility(View.VISIBLE);
					btn_fhdian.setVisibility(View.VISIBLE);
				}
				if (bean.getData().get(position).status==2 ||
						bean.getData().get(position).status==3 ||
						bean.getData().get(position).status==4 ||
						bean.getData().get(position).status==6) {
					infor_title_juli.setVisibility(View.VISIBLE);
					infor_title_juli.setText("担保交易");
				}else {
					infor_title_juli.setVisibility(View.GONE);
				}
					
				if (bean.getData().get(position).status==2) {
					btn_zb.setBackgroundResource(R.drawable.logisyzbs);
					btn_zbdian.setBackgroundResource(R.drawable.greensearchimg_13880);
					btn_fh.setBackgroundResource(R.drawable.logiswfh);
					btn_dd.setBackgroundResource(R.drawable.logiswdd);
					btn_fhdian.setBackgroundResource(R.drawable.grayseach_13880);
				}else 
				if (bean.getData().get(position).status==3) {   
					btn_zb.setBackgroundResource(R.drawable.logisyzbs);
					btn_fh.setBackgroundResource(R.drawable.logisyquhuo);
					btn_zbdian.setBackgroundResource(R.drawable.greensearchimg_13880);
					btn_fhdian.setBackgroundResource(R.drawable.greensearchimg_13880);
					btn_dd.setBackgroundResource(R.drawable.logiswdd);
				}else 
				if (bean.getData().get(position).status==4) {
					btn_zb.setBackgroundResource(R.drawable.logisyzbs);
					btn_fh.setBackgroundResource(R.drawable.logisyquhuo);
					btn_dd.setBackgroundResource(R.drawable.logisydd);
					btn_zbdian.setBackgroundResource(R.drawable.greensearchimg_13880);
					btn_fhdian.setBackgroundResource(R.drawable.greensearchimg_13880);
				}else 
				if (bean.getData().get(position).status==8) {
					btn_zb.setBackgroundResource(R.drawable.logisyzbs);
					btn_zbdian.setBackgroundResource(R.drawable.greensearchimg_13880);
					btn_fh.setBackgroundResource(R.drawable.logiswfh);
					btn_dd.setBackgroundResource(R.drawable.logiswdd);
					btn_fhdian.setBackgroundResource(R.drawable.grayseach_13880);
				}
				
				//报价详情
				btn_zb.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View arg0) {
						// TODO Auto-generated method stub
						Intent intent=new Intent();
						intent.putExtra("recId", bean.getData().get(position).recId);//int recId;//镖件主键
//						
						intent.setClass(context,	LogCompanyFreActivity.class);
						startActivity(intent);
						
					}
				});
				
				//收货界面
				btn_fh.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View arg0) {
						// TODO Auto-generated method stub
						if (bean.getData().get(position).status!=3) {
							Intent intent=new Intent();
							intent.putExtra("recId", bean.getData().get(position).recId);//int recId;//镖件主键
							intent.putExtra("userId", bean.getData().get(position).userId);//发件人id
							intent.putExtra("cargoName", bean.getData().get(position).cargoName);//货物名称
							intent.putExtra("startPlace", bean.getData().get(position).startPlace);//物品起发地址
							intent.putExtra("entPlace", bean.getData().get(position).entPlace);//物品到达地址
							intent.putExtra("cargoWeight", bean.getData().get(position).weight);//物品重量
							intent.putExtra("cargoVolume", bean.getData().get(position).cargoVolume);//物品体积
							intent.putExtra("takeTime", bean.getData().get(position).takeTime);//取货时间
							intent.putExtra("arriveTime", bean.getData().get(position).arriveTime);//到达时间
							intent.putExtra("takeName", bean.getData().get(position).takeName);//收货人姓名
							intent.putExtra("takeMobile", bean.getData().get(position).takeMobile);//收货人地址
							intent.putExtra("remark", bean.getData().get(position).remark);//备注
							intent.putExtra("status", bean.getData().get(position).status);//
							intent.putExtra("takeCargo", bean.getData().get(position).takeCargo);//是否要取货
							intent.putExtra("sendCargo", bean.getData().get(position).sendCargo);//是否要送货
							intent.putExtra("sendName", bean.getData().get(position).sendName);//发货人
							intent.putExtra("sendMobile", bean.getData().get(position).sendMobile);//发货人手机号
							intent.putExtra("sendNumber", bean.getData().get(position).sendNumber);//发货次数
							intent.putExtra("publishTime", bean.getData().get(position).publishTime);//发布时间
							intent.putExtra("transferMoney", bean.getData().get(position).transferMoney);//
							intent.putExtra("luMessage", bean.getData().get(position).luMessage);//
							intent.putExtra("cargoCost", bean.getData().get(position).cargoCost);//价值
							intent.putExtra("distance", bean.getData().get(position).distance);//j距离
							intent.putExtra("firstPicture", bean.getData().get(position).firstPicture);//照片一
							intent.putExtra("secondPicture", bean.getData().get(position).secondPicture);//照片二
							intent.putExtra("thirdPicture", bean.getData().get(position).thirdPicture);//照片三
							intent.putExtra("cargoSize", bean.getData().get(position).cargoSize);//
							intent.putExtra("way", "1");//c照片
							intent.setClass(context,LogCompanyDeliveryActivity.class);
							 startActivityForResult(intent,10);	
							}else {
						Intent intent=new Intent();
						intent.putExtra("recId", bean.getData().get(position).recId);//int recId;//镖件主键
						intent.putExtra("userId", bean.getData().get(position).userId);//发件人id
						intent.putExtra("cargoName", bean.getData().get(position).cargoName);//货物名称
						intent.putExtra("startPlace", bean.getData().get(position).startPlace);//物品起发地址
						intent.putExtra("entPlace", bean.getData().get(position).entPlace);//物品到达地址
						intent.putExtra("cargoWeight", bean.getData().get(position).weight);//物品重量
						intent.putExtra("cargoVolume", bean.getData().get(position).cargoVolume);//物品体积
						intent.putExtra("takeTime", bean.getData().get(position).takeTime);//取货时间
						intent.putExtra("arriveTime", bean.getData().get(position).arriveTime);//到达时间
						intent.putExtra("takeName", bean.getData().get(position).takeName);//收货人姓名
						intent.putExtra("takeMobile", bean.getData().get(position).takeMobile);//收货人地址
						intent.putExtra("remark", bean.getData().get(position).remark);//备注
						intent.putExtra("status", bean.getData().get(position).status);//
						intent.putExtra("takeCargo", bean.getData().get(position).takeCargo);//是否要取货
						intent.putExtra("sendCargo", bean.getData().get(position).sendCargo);//是否要送货
						intent.putExtra("sendName", bean.getData().get(position).sendName);//发货人
						intent.putExtra("sendMobile", bean.getData().get(position).sendMobile);//发货人手机号
						intent.putExtra("sendNumber", bean.getData().get(position).sendNumber);//发货次数
						intent.putExtra("publishTime", bean.getData().get(position).publishTime);//发布时间
						intent.putExtra("transferMoney", bean.getData().get(position).transferMoney);//
						intent.putExtra("luMessage", bean.getData().get(position).luMessage);//
						intent.putExtra("cargoCost", bean.getData().get(position).cargoCost);//价值
						intent.putExtra("distance", bean.getData().get(position).distance);//j距离
						intent.putExtra("firstPicture", bean.getData().get(position).firstPicture);//照片一
						intent.putExtra("secondPicture", bean.getData().get(position).secondPicture);//照片二
						intent.putExtra("thirdPicture", bean.getData().get(position).thirdPicture);//照片三
						intent.putExtra("way", "2");//c照片
						intent.putExtra("cargoSize", bean.getData().get(position).cargoSize);//
						intent.setClass(context,LogCompanyDeliveryActivity.class);
//						 startActivity(intent);
						 startActivityForResult(intent,10);							}
					}
				});
				
				btn_dd.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View arg0) {
						// TODO Auto-generated method stub
						if (bean.getData().get(position).status==3) {
							Builder ad = new Builder(LogisticalFreightActivity.this);
							ad.setTitle("温馨提示");
							ad.setMessage("是否确认货物已送达");
							ad.setNegativeButton("确认", new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface arg0, int arg1) {
//									getMessages(bean.getData().get(position).recId);
									// 输入支付密码:LogisticaPayActivity   CompanyDeliveryPayActivity
									startActivity(new Intent(getApplicationContext(), LogisticaPayActivity.class)
											.putExtra("recId",bean.getData().get(position).recId));
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
							if (bean.getData().get(position).status==4) {
								ToastUtil.shortToast(context, "您已完成");
								return;
							  }
							ToastUtil.shortToast(context, "您到达目的后才能使用");
						
						}
						
					}
				});
			}

		}
		}
	public void getMessages(final int recid){

		RequestParams params = new RequestParams();
		Log.e("数据", UrlMap.getUrl(MCUrl.ARRIVE, "recId", recid+""));
		AsyncHttpUtils.doGet(UrlMap.getUrl(MCUrl.ARRIVE, "recId", recid+""), null, null, params,
				new AsyncHttpResponseHandler() {
					@Override
					public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
						Log.e("11111111json", new String(arg2));
						dialog.dismiss();
						BaseBean		beans = new Gson().fromJson(new String(arg2),
								BaseBean.class);
						if (beans.getErrCode()==0) {
							ToastUtil.shortToast(LogisticalFreightActivity.this, bean.message);
							// 输入支付密码:LogisticaPayActivity   CompanyDeliveryPayActivity
							startActivity(new Intent(getApplicationContext(), LogisticaPayActivity.class)
									.putExtra("recId",recid));
						}else {
							ToastUtil.shortToast(LogisticalFreightActivity.this, bean.message);
						}
						

					}

					@Override
					public void onFailure(int arg0, Header[] arg1, byte[] arg2,
							Throwable arg3) {
						dialog.dismiss();
						ToastUtil.shortToast(LogisticalFreightActivity.this, "网络请求加载失败");

					}
				});
	}
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
//		Log.e("11111111  ", resultCode+" ssss   "+data);
		if (requestCode==10) {
			if (resultCode == Activity.RESULT_OK) {
				getHttprequst(false, true, 1, false);
			}
		}
	}
}
