package com.hex.express.iwant.activities;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.google.gson.Gson;
import com.handmark.pulltorefresh.library.ILoadingLayout;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.hex.express.iwant.R;
import com.hex.express.iwant.iWantApplication;
import com.hex.express.iwant.activities.MylogisticakActivity.MylisAdapter;
import com.hex.express.iwant.adapters.BaseListAdapter;
import com.hex.express.iwant.bean.BaseBean;
import com.hex.express.iwant.bean.CityBean;
import com.hex.express.iwant.bean.DepotBean;
import com.hex.express.iwant.bean.DepotBean.Data;
import com.hex.express.iwant.constance.MCUrl;
import com.hex.express.iwant.constance.PreferenceConstants;
import com.hex.express.iwant.helper.CheckDbUtils;
import com.hex.express.iwant.helper.CityDbOperation;
import com.hex.express.iwant.helper.DbManager;
import com.hex.express.iwant.http.AsyncHttpUtils;
import com.hex.express.iwant.http.UrlMap;
import com.hex.express.iwant.utils.PreferencesUtils;
import com.hex.express.iwant.utils.ToastUtil;
import com.hex.express.iwant.views.MarqueeTextView;
import com.loopj.android.http.AsyncHttpResponseHandler;

import android.R.integer;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import butterknife.Bind;
import butterknife.ButterKnife;
/**
 * 
 * @author huyichuan
 * 货场地址
 *
 */
public class DepotFreigActivity extends BaseActivity{
	
	@Bind(R.id.btnLeft)
	ImageView btnLeft;
	@Bind(R.id.depot_listview)
	PullToRefreshListView  depot_listview;
	@Bind(R.id.btn_poset)
	Button btn_poset;
	@Bind(R.id.null_message)
	View view_null_message;
	@Bind(R.id.view_load_fail)
	LinearLayout view_load_fail;
	private ListView listView;
	protected int pageSize = 20;// 表示一页展示多少列
	private int pageNo = 1;// 请求页码
	private DepotBean bean;
	private List<DepotBean.Data> mList;
	private List<DepotBean.Data> mList2;
	DepotAdapter adapter;
	// 当前位置经纬度
		private double latitude;
		private double longitude;
		private String city;
	private double mylatitude;// 经度
	private double mylongitude;// 纬度
	private LocationClient client;
	private String cityCode;
	private boolean receive;
	private String receiver_citycode,et_address,deadd_addsetwo,way;
	boolean ifDefault;
	int  recId;
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		iWantApplication.getInstance().addActivity(this);
		//activity_logistical_xiangqing   activity_logistical_xiqing
		setContentView(R.layout.activity_depot);
		ButterKnife.bind(DepotFreigActivity.this);
		initView();
		initData();
		setOnClick();
	}

	@Override
	public void onWeightClick(View v) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void initView() {
		// TODO Auto-generated method stub
		mList2= new ArrayList<DepotBean.Data>();
		mList = new ArrayList<DepotBean.Data>();
		listView = depot_listview.getRefreshableView();
		client = new LocationClient(getApplicationContext());
		initLocation();
		client.registerLocationListener(new BDLocationListener() {

			@Override
			public void onReceiveLocation(BDLocation arg0) {
				latitude = arg0.getLatitude();
				longitude = arg0.getLongitude();
				Log.e("jpppp", latitude + ":::::::::" + longitude);
			}
		});
		// 初始化定位
		// 打开GPS
		client.start();
		
	}

	@Override
	public void initData() {
		// TODO Auto-generated method stub
		getHttpRequst(true, false, 1, false);
	}

	@Override
	public void setOnClick() {
		// TODO Auto-generated method stub
		btnLeft.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				finish();
			}
		});
		
		btn_poset.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(DepotFreigActivity.this, DepotAddsActivity.class);
				intent.putExtra("way", "2");
		 		startActivityForResult(intent, 1);
		 		finish();
				
			}
		});
		// 下拉刷新与上拉加载
		depot_listview.setMode(Mode.BOTH);
		depot_listview.setOnRefreshListener(new OnRefreshListener2() {

					@Override
					public void onPullDownToRefresh(PullToRefreshBase refreshView) {
						getHttpRequst(false, true, 1, false);
					}

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
//		listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//
//			@Override
//			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
//					long arg3) {
//				
//			      	Data data = mList.get(arg2 - 1);
//			      	Log.e("11111", "1111");
//			      	finish();
//
//			}
//		});
	}

	@Override
	public void getData() {
		// TODO Auto-generated method stub
		
	}
	private int num;
	//获取已接的镖件
	@SuppressWarnings("unchecked")
	private void getHttpRequst(final boolean isFirst, final boolean isRefresh,
			int pageNo, final boolean isPull) {
		String url = UrlMap.getThreeUrl(MCUrl.LogComAddList, "userId",String.valueOf(PreferencesUtils.getInt(DepotFreigActivity.this, PreferenceConstants.UID)),
				"pageNo", String.valueOf(pageNo), "pageSize", String.valueOf(pageSize));
		Log.e("huochang", url);
		AsyncHttpUtils.doSimGet(url, new AsyncHttpResponseHandler() {
			@Override
			public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
				Log.e("huochang", new String(arg2));
			
				try {
					bean = new Gson().fromJson(new String(arg2),
							DepotBean.class);
					mList = bean.data;
//					ToastUtil.shortToast(LogistcaInforseActivity.this, ""+bean.message);
				} catch (Exception e) {
					// TODO: handle exception
				}
				
				if (isFirst) {
					if (mList.size() != 0 && mList != null) {
						if (adapter == null) {
							mList2.addAll(mList);
							adapter = new DepotAdapter(DepotFreigActivity.this,
									mList2);
							listView.setAdapter(adapter);
						}
					} else {
						view_null_message.setVisibility(View.VISIBLE);
						depot_listview.setVisibility(View.GONE);
					}
				} else {
					if (isRefresh && !isPull) {
						mList = bean.data;
						if (mList.size() != 0 && mList != null) {
							depot_listview.onRefreshComplete();
								adapter = new DepotAdapter(DepotFreigActivity.this, mList);
								listView.setAdapter(adapter);
								
						} else {
							view_null_message.setVisibility(View.VISIBLE);
							depot_listview.setVisibility(View.GONE);
						}
						}
				else if (!isRefresh && isPull) {
						num = mList.size();
						adapter.addData(mList);
						adapter.notifyDataSetChanged();
						depot_listview.onRefreshComplete();
					}
				}

			}

			@Override
			public void onFailure(int arg0, Header[] arg1, byte[] arg2,
					Throwable arg3) {
				view_load_fail.setVisibility(View.VISIBLE);
				depot_listview.setVisibility(View.GONE);

			}
		});
	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		switch (requestCode) {
		case 1:
			if (resultCode == RESULT_OK) {
				mylatitude = data.getDoubleExtra("latitude", 0);
				mylongitude = data.getDoubleExtra("longitude", 0);
				city = data.getStringExtra("city");
				way= data.getStringExtra("way");
				et_address=data.getStringExtra("address").replace("中国", "");
				deadd_addsetwo=data.getStringExtra("deadd_addsetwo");
				ifDefault=data.getBooleanExtra("ifDefault", false);
				recId=data.getIntExtra("recId", 0);
				getCityCode();
				Log.e("11111address  ",""+data.getStringExtra("address"));
				if (way.equals("2")) {//添加新地址
					addmesge();
				}else if (way.equals("1")) {//修改地址
					revisemesge();
				}
			}
			break;
		}
		};
	private void getCityCode() {
		boolean isCopySuccess = CheckDbUtils.checkDb();
		// 成功的将数据库copy到data 中
		if (isCopySuccess) {
			iWantApplication.getInstance().mDbManager = new DbManager(iWantApplication.getInstance());
		}
		if (city == null || city.equals("")) {
			ToastUtil.shortToast(getApplicationContext(), "请输入完整信息");
			return;
		}
		Log.e("city", city);
		if (!city.contains("市")) {
			city = city + "市";
		}
		List<CityBean> selectDataFromDb = new CityDbOperation()
				.selectDataFromDb("select * from city where city_name='" + city + "'");
		if (receive) {
			receiver_citycode = selectDataFromDb.get(0).city_code;
			Log.e("citycode", receiver_citycode);

		} else {
			cityCode = selectDataFromDb.get(0).city_code;
			Log.e("citycode", cityCode);
		}
	}
	private void initLocation() {
		LocationClientOption option = new LocationClientOption();
		option.setLocationMode(com.baidu.location.LocationClientOption.LocationMode.Hight_Accuracy);// 可选，默认高精度，设置定位模式，高精度，低功耗，仅设备
		option.setCoorType("bd09ll");// 可选，默认gcj02，设置返回的定位结果坐标系
		int span = 0;
		option.setScanSpan(span);// 可选，默认0，即仅定位一次，设置发起定位请求的间隔需要大于等于1000ms才是有效的
		option.setIsNeedAddress(true);// 可选，设置是否需要地址信息，默认不需要
		option.setOpenGps(false);// 可选，默认false,设置是否使用gps
		option.setLocationNotify(false);// 可选，默认false，设置是否当gps有效时按照1S1次频率输出GPS结果
		option.setIsNeedLocationDescribe(true);// 可选，默认false，设置是否需要位置语义化结果，可以在BDLocation.getLocationDescribe里得到，结果类似于“在北京天安门附近”
		option.setIsNeedLocationPoiList(true);// 可选，默认false，设置是否需要POI结果，可以在BDLocation.getPoiList里得到
		option.setIgnoreKillProcess(false);// 可选，默认true，定位SDK内部是一个SERVICE，并放到了独立进程，设置是否在stop的时候杀死这个进程，默认不杀死
		option.SetIgnoreCacheException(false);// 可选，默认false，设置是否收集CRASH信息，默认收集
		option.setEnableSimulateGps(false);// 可选，默认false，设置是否需要过滤gps仿真结果，默认需要
		client.setLocOption(option);
	}
	//添加
   private void addmesge(){
	   JSONObject obj = new JSONObject();
		try {
			obj.put("userId",
					String.valueOf(PreferencesUtils.getInt(getApplicationContext(), PreferenceConstants.UID)));
			obj.put("cityCode", cityCode);
			obj.put("locationAddress", et_address);
			obj.put("address", deadd_addsetwo);
			obj.put("latitude", mylatitude);
			obj.put("longitude", mylongitude);
			obj.put("ifDefault", ifDefault);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		AsyncHttpUtils.doPostJson(DepotFreigActivity.this, MCUrl.AddComAdd, obj.toString(),
				new AsyncHttpResponseHandler() {

					@Override
					public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
						Log.e("oppo", new String(arg2));
						dialog.dismiss();
						BaseBean bean = new Gson().fromJson(new String(arg2), BaseBean.class);
						if (bean.getErrCode() == 0) {
							getHttpRequst(false, true, 1, false);
						}else {
							ToastUtil.shortToast(DepotFreigActivity.this, bean.getMessage());
						}

					}

					@Override
					public void onFailure(int arg0, Header[] arg1, byte[] arg2, Throwable arg3) {
						dialog.dismiss();
					}
				});
   }
   //修改
   private void revisemesge(){
	   JSONObject obj = new JSONObject();
		try {
			obj.put("userId",
					String.valueOf(PreferencesUtils.getInt(getApplicationContext(), PreferenceConstants.UID)));
			obj.put("cityCode", cityCode);
			
			obj.put("recId", recId);
			obj.put("locationAddress", et_address);
			obj.put("address", deadd_addsetwo);
			obj.put("latitude", mylatitude);
			obj.put("longitude", mylongitude);
			obj.put("ifDefault", ifDefault);
			obj.put("ifDelete", false);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		AsyncHttpUtils.doPostJson(DepotFreigActivity.this, MCUrl.UpdateComAdd, obj.toString(),
				new AsyncHttpResponseHandler() {

					@Override
					public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
						Log.e("oppo", new String(arg2));
						dialog.dismiss();
						BaseBean bean = new Gson().fromJson(new String(arg2), BaseBean.class);
						if (bean.getErrCode() == 0) {
							getHttpRequst(false, true, 1, false);
						}else {
							ToastUtil.shortToast(DepotFreigActivity.this, bean.getMessage());
						}
					}
					@Override
					public void onFailure(int arg0, Header[] arg1, byte[] arg2, Throwable arg3) {
						dialog.dismiss();
					}
				});
   }
  
   
   
   class DepotAdapter extends BaseListAdapter{
		public DepotAdapter(Context context, List list) {
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
			return R.layout.depot_item;
		}
		class OwnerViewHolder extends ViewHolder {

			public OwnerViewHolder(View itemView) {
				super(itemView);
			}
			@Bind(R.id.depot_title)//市
			MarqueeTextView depot_title;
			@Bind(R.id.depot_context)//具体地址
//			TextView depot_context;
			MarqueeTextView depot_context;
			@Bind(R.id.depot_compile)
			Button depot_compile;//修改
			@Bind(R.id.depot_delet)
			Button depot_delet;//删除
			@Bind(R.id.cb_xuan)//选择是否默认
			CheckBox cb_xuan;
			@Bind(R.id.depot_lin)
			LinearLayout depot_lin;
			private DepotBean depobean;
		
		
		public void setData(final int position) {
			super.setData(position);
			depobean = new DepotBean();
			depobean.data = list;
			depot_title.setText(depobean.data.get(position).locationAddress);
			depot_context.setText(depobean.data.get(position).address);
			
			if (depobean.getData().get(position).ifDefault) {
				cb_xuan.setChecked(true);
//				cb_xuan.setClickable(false);
			}else {
//				cb_xuan.setClickable(false);
				cb_xuan.setText("设为默认");
				cb_xuan.setOnCheckedChangeListener(new OnCheckedChangeListener() {
					
					@Override
					public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
						// TODO Auto-generated method stub
						if (arg1) {
							
						
						 JSONObject obj = new JSONObject();
							try {
								obj.put("userId",
										String.valueOf(PreferencesUtils.getInt(getApplicationContext(), PreferenceConstants.UID)));
								obj.put("cityCode", depobean.data.get(position).cityCode);
								
								obj.put("recId", depobean.data.get(position).recId);
								obj.put("locationAddress", depobean.data.get(position).locationAddress);
								obj.put("address", depobean.data.get(position).address);
								obj.put("latitude", depobean.data.get(position).latitude);
								obj.put("longitude", depobean.data.get(position).longitude);
								obj.put("ifDefault", arg1);
								obj.put("ifDelete", false);
							} catch (JSONException e) {
								e.printStackTrace();
							}
							AsyncHttpUtils.doPostJson(DepotFreigActivity.this, MCUrl.UpdateComAdd, obj.toString(),
									new AsyncHttpResponseHandler() {

										@Override
										public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
											Log.e("oppo", new String(arg2));
											dialog.dismiss();
											BaseBean bean = new Gson().fromJson(new String(arg2), BaseBean.class);
											if (bean.getErrCode() == 0) {
												getHttpRequst(false, true, 1, false);
											}else {
												ToastUtil.shortToast(DepotFreigActivity.this, bean.getMessage());
											}
										}
										@Override
										public void onFailure(int arg0, Header[] arg1, byte[] arg2, Throwable arg3) {
											dialog.dismiss();
										}
									});
						}else {
							JSONObject obj = new JSONObject();
							try {
								obj.put("userId",
										String.valueOf(PreferencesUtils.getInt(getApplicationContext(), PreferenceConstants.UID)));
								obj.put("cityCode", depobean.data.get(position).cityCode);
								
								obj.put("recId", depobean.data.get(position).recId);
								obj.put("locationAddress", depobean.data.get(position).locationAddress);
								obj.put("address", depobean.data.get(position).address);
								obj.put("latitude", depobean.data.get(position).latitude);
								obj.put("longitude", depobean.data.get(position).longitude);
								obj.put("ifDefault", arg1);
								obj.put("ifDelete", false);
							} catch (JSONException e) {
								e.printStackTrace();
							}
							AsyncHttpUtils.doPostJson(DepotFreigActivity.this, MCUrl.UpdateComAdd, obj.toString(),
									new AsyncHttpResponseHandler() {

										@Override
										public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
											Log.e("oppo", new String(arg2));
											dialog.dismiss();
											BaseBean bean = new Gson().fromJson(new String(arg2), BaseBean.class);
											if (bean.getErrCode() == 0) {
												getHttpRequst(false, true, 1, false);
											}else {
												ToastUtil.shortToast(DepotFreigActivity.this, bean.getMessage());
											}
										}
										@Override
										public void onFailure(int arg0, Header[] arg1, byte[] arg2, Throwable arg3) {
											dialog.dismiss();
										}
									});
							
						}
					}
				});
			}
			depot_compile.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					Intent intent=new Intent();
					intent.putExtra("locationAddress", depobean.data.get(position).locationAddress);
					intent.putExtra("address", depobean.data.get(position).address);
					intent.putExtra("cityCode", depobean.data.get(position).cityCode);
					intent.putExtra("recId", depobean.data.get(position).recId);
					intent.putExtra("ifDefault", depobean.data.get(position).ifDefault);
					intent.putExtra("way", "1");
					intent.setClass(context,	DepotAddsActivity.class);//
					startActivityForResult(intent, 1);
//					context. startActivity(intent);
				}
			});
			
//			depot_lin.setOnClickListener(new OnClickListener() {
//				@Override
//				public void onClick(View arg0) {
//					// TODO Auto-generated method stub
//					Intent intent=new Intent();
//					intent.putExtra("locationAddress", depobean.data.get(position).locationAddress);
//					intent.putExtra("address", depobean.data.get(position).address);
////					intent.putExtra("cityCode", depobean.data.get(position).cityCode);
////					intent.putExtra("way", "1");
//					intent.setClass(context,	LogCompanyActivity.class);
//					setResult(RESULT_OK, intent);
//					finish();
//				}
//			});
			
			depot_delet.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					
					JSONObject obj = new JSONObject();
					try {
						obj.put("userId",
								String.valueOf(PreferencesUtils.getInt(context, PreferenceConstants.UID)));
						obj.put("recId", depobean.data.get(position).recId);
						obj.put("cityCode", depobean.data.get(position).cityCode);
						obj.put("locationAddress", depobean.data.get(position).locationAddress);
						obj.put("address", depobean.data.get(position).address);
						obj.put("latitude", depobean.data.get(position).latitude);
						obj.put("longitude", depobean.data.get(position).longitude);
						obj.put("ifDelete", true);
						obj.put("ifDefault", depobean.data.get(position).ifDefault);
					} catch (JSONException e) {
						e.printStackTrace();
					}
					AsyncHttpUtils.doPostJson(context, MCUrl.UpdateComAdd, obj.toString(),
							new AsyncHttpResponseHandler() {

								@Override
								public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
									Log.e("oppo", new String(arg2));
									BaseBean bean = new Gson().fromJson(new String(arg2), BaseBean.class);
									if (bean.getErrCode() == 0) {
										getHttpRequst(false, true, 1, false);
									}else {
										ToastUtil.shortToast(context, bean.getMessage());
									}
								}
								@Override
								public void onFailure(int arg0, Header[] arg1, byte[] arg2, Throwable arg3) {
								}
							});
				}
			});
		}
		}
   }
}
