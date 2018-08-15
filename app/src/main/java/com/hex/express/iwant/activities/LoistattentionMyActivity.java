package com.hex.express.iwant.activities;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.google.gson.Gson;
import com.handmark.pulltorefresh.library.ILoadingLayout;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.hex.express.iwant.R;
import com.hex.express.iwant.iWantApplication;
import com.hex.express.iwant.adapters.AttentionMyAdapter;
import com.hex.express.iwant.adapters.BaseListAdapter;
import com.hex.express.iwant.adapters.ConcerAdapter;
import com.hex.express.iwant.adapters.BaseListAdapter.ViewHolder;
import com.hex.express.iwant.bean.CardBean;
import com.hex.express.iwant.bean.CityBean;
import com.hex.express.iwant.bean.LogisBean;
import com.hex.express.iwant.bean.LoisattenBean;
import com.hex.express.iwant.bean.CardBean.Data;
import com.hex.express.iwant.constance.MCUrl;
import com.hex.express.iwant.constance.PreferenceConstants;
import com.hex.express.iwant.helper.CheckDbUtils;
import com.hex.express.iwant.helper.CityDbOperation;
import com.hex.express.iwant.helper.DbManager;
import com.hex.express.iwant.http.AsyncHttpUtils;
import com.hex.express.iwant.http.UrlMap;
import com.hex.express.iwant.utils.Logger;
import com.hex.express.iwant.utils.PreferencesUtils;
import com.hex.express.iwant.utils.ToastUtil;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.CompoundButton;
import butterknife.Bind;
import butterknife.ButterKnife;
/**
 * 
 * @author huyichuan
 * g关注定点货源
 */
public class LoistattentionMyActivity extends BaseActivity{
    
	@Bind(R.id.atten_listview)
	PullToRefreshListView atten_listview;
	@Bind(R.id.edt_out)//选择出发城市
	EditText edt_out;
	@Bind(R.id.edt_in)//选择到达城市
	EditText edt_in;
	@Bind(R.id.butemit)
	Button butemit;
	private ListView listView;
	protected int pageSize = 10;// 表示一页展示多少列
	protected int pageNo = 1;
	private List<LoisattenBean.Data> mList;
	private AttentionMyAdapter adapter;
	public LoisattenBean bean;
	private int num;
			// 当前位置经纬度
			private double latitude;
			private double longitude;
			private String city;
			// 寄件人经纬度
			private double mylatitude;// 经度
			private double mylongitude;// 纬度
			private LocationClient client;
			private String cityCode;
			// 收件人经纬度
			private double receiver_longitude;
			private double receiver_latitude;
			private String receiver_citycode;
			private boolean receive;
			private String cityCodes;
			private Calendar c = null;//时间选择
		    private final static int DATE_DIALOG = 0;
		    private final static int DATE = 1; 
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_attention_my);
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
		listView = atten_listview.getRefreshableView();
	}

	@Override
	public void initData() {
		// TODO Auto-generated method stub
		mList = new ArrayList<LoisattenBean.Data>();
		dialog.show();
	String string=getIntent().getStringExtra("adds");
	getHttprequst(true, false, 1, false);
//	附近货源
	if(string.equals("1")){
		
	}//定点货源
	else if (string.equals("2")) {
		
	}
	//地区货源
	else if (string.equals("3")) {
		
	}{
		
	}
	}

	/**
	 * 获取网络数据
	 */
	private void getHttprequst(final boolean isFirst, final boolean isRefresh,
			int pageNo, final boolean isPull) {
		RequestParams params = new RequestParams();
		AsyncHttpUtils.doGet(UrlMap.getThreeUrl(MCUrl.ATTENT_LIST, "userId", String
				.valueOf(PreferencesUtils.getInt(getApplicationContext(),
						PreferenceConstants.UID)), "pageNo", pageNo + "",
				"pageSize", String.valueOf(pageSize)), null, null, params,
				new AsyncHttpResponseHandler() {
					@Override
					public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
						Log.e("111111111json", new String(arg2));
						dialog.dismiss();
						bean = new Gson().fromJson(new String(arg2),
								LoisattenBean.class);
						mList = bean.data;
						if (isFirst) {
							if (mList.size() != 0 && mList != null) {
								if (adapter == null) {
									adapter = new AttentionMyAdapter(
											LoistattentionMyActivity.this, mList);
									listView.setAdapter(adapter);
								}
							} else {
								atten_listview.setVisibility(View.GONE);
							}
						} else {
							if (isRefresh && !isPull) {
								adapter.setData(mList);
								adapter.notifyDataSetChanged();
								atten_listview.onRefreshComplete();
							} else if (!isRefresh && isPull) {
								num = mList.size();
								adapter.addData(mList);
								adapter.notifyDataSetChanged();
								atten_listview.onRefreshComplete();
							}
						}
//
					}

					@Override
					public void onFailure(int arg0, Header[] arg1, byte[] arg2,
							Throwable arg3) {
						dialog.dismiss();
						ToastUtil.shortToast(LoistattentionMyActivity.this, "网络请求加载失败");

					}
				});

	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public void setOnClick() {

		// 下拉刷新与上拉加载
		atten_listview.setMode(Mode.BOTH);
		atten_listview.setOnRefreshListener(new OnRefreshListener2() {

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
		atten_listview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				com.hex.express.iwant.bean.LoisattenBean.Data data = mList.get(arg2 - 1);
				Intent intent = new Intent();
				Log.e("bb", "bbbbb");

			}
		});
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				// TODO Auto-generated method stub
				
			}
		});
		butemit.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				String string=getIntent().getStringExtra("adds");
//				附近货源
				if(string.equals("1")){
					
				}//定点货源
				else if (string.equals("2")) {
					
				}
				//地区货源
				else if (string.equals("3")) {
					
				}{
					
				}

				addPostResult();
			}
		});
		edt_in.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				startActivityForResult(new Intent(LoistattentionMyActivity.this, AddressActivity.class), 8);
				
			}
		});
		edt_out.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				startActivityForResult(new Intent(LoistattentionMyActivity.this, AddressActivity.class), 7);
				
			}
		});
	}

	private boolean gift;

	@Override
	public void getData() {
		gift = getIntent().getBooleanExtra("gift", false);
	}
	private void addPostResult() {
		JSONObject obj = new JSONObject();
//		if(!edt_out.getText().toString().trim().equals("") ||
//				!edt_in.getText().toString().trim().equals("")){
//			return;
//		}else {
//			ToastUtil.shortToast(this, "信息不能为空");
//		}
		try {
			obj.put("userId",
					String.valueOf(PreferencesUtils.getInt(getApplicationContext(), PreferenceConstants.UID)));
			obj.put("latitude", latitude);
			obj.put("longitude", longitude);
			obj.put("startPlaceCityCode", cityCode);
			obj.put("startPlace", edt_out.getText().toString().trim());//edt_out.getText().toString().trim()
			obj.put("entPlace", edt_in.getText().toString().trim());//edt_in.getText().toString().trim()
			obj.put("entPlaceCityCode", cityCodes);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		dialog.show();
		Log.e("查看数据", obj.toString());
		AsyncHttpUtils.doPostJson(LoistattentionMyActivity.this, MCUrl.ATTENT_ADD, obj.toString(),
				new AsyncHttpResponseHandler() {

					@Override
					public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
						Log.e("11111111111 wwww   ", new String(arg2));
						dialog.dismiss();
						LogisBean bean = new Gson().fromJson(new String(arg2), LogisBean.class);
						Log.e("oppop", bean.getMessage());
						ToastUtil.shortToast(getApplicationContext(), bean.getMessage());

					}

					@Override
					public void onFailure(int arg0, Header[] arg1, byte[] arg2, Throwable arg3) {
						dialog.dismiss();
					}
				});

	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		Log.e("11111111  ", resultCode+" ssss   "+data);
		switch (requestCode) {
			case 7:
				if (resultCode == RESULT_OK) {
					mylatitude = data.getDoubleExtra("latitude", 0);
					mylongitude = data.getDoubleExtra("longitude", 0);
					city = data.getStringExtra("city");
					getCityCode();
					edt_out.setText(data.getStringExtra("address").replace("中国", ""));
				}
				break;
			case 8:
				if (resultCode == RESULT_OK) {
					mylatitude = data.getDoubleExtra("latitude", 0);
					mylongitude = data.getDoubleExtra("longitude", 0);
					city = data.getStringExtra("city");
					getCityCodes();
					Log.e("111111     ",""+data.getStringExtra("address").replace("中国", ""));
					edt_in.setText(data.getStringExtra("address").replace("中国", ""));
				}
				break;

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
	private void getCityCodes() {
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
			cityCodes = selectDataFromDb.get(0).city_code;
			Log.e("citycode", cityCode);
		}
	}

	
	class AttentionMyAdapter extends BaseListAdapter{

		public AttentionMyAdapter(Context context, List list) {
			super(context, list);
			// TODO Auto-generated constructor stub
		}

		@Override
		public ViewHolder onCreateViewHolder(View itemView) {
			// TODO Auto-generated method stub
			return new MylisOfferViewHoder(itemView);
		}

		@Override
		public int getLayoutResource() {
			// TODO Auto-generated method stub
			return R.layout.item_attent;
		}

		class MylisOfferViewHoder extends ViewHolder{

			public MylisOfferViewHoder(View itemView) {
				super(itemView);
				// TODO Auto-generated constructor stub
			}

			@Bind(R.id.attent_delete)//删除按钮
			TextView attent_delete;
			@Bind(R.id.attent_end)//到达地址
			TextView attent_end;
			@Bind(R.id.attent_start)//起发地址
			TextView attent_start;
			private LoisattenBean cardBean;

			@Override
			public void setData(final int position) {
				// TODO Auto-generated method stub
				super.setData(position);
				cardBean = new LoisattenBean();
				cardBean.data = list;
				attent_start.setText(cardBean.getData().get(position).startPlace);
				attent_end.setText(cardBean.getData().get(position).entPlace);
				attent_delete.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View arg0) {
						// TODO Auto-generated method stub
						Log.e("11111", ""+cardBean.getData().get(position).recId);
						addPostResult(cardBean.getData().get(position).recId);
						
					}
				});
			}
			private void addPostResult(int  recId) {
				JSONObject obj = new JSONObject();
//				try {
////					obj.put("recId", String.valueOf(recId));
//				} catch (JSONException e) {
					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
				
				Log.e("查看数据", obj.toString());//doPostJson     dosPut
				Log.e("111", MCUrl.ATTENT_Delete + String.valueOf(recId));
				AsyncHttpUtils.doPut(context,
						MCUrl.ATTENT_Delete +String.valueOf(recId),
						obj.toString(), null,new AsyncHttpResponseHandler() {
//				AsyncHttpUtils.dosPut(context, MCUrl.ATTENT_Delete, obj.toString(),
//						new AsyncHttpResponseHandler() {
							@Override
							public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
								Log.e("11111111111 wwww   ", new String(arg2));
								LogisBean bean = new Gson().fromJson(new String(arg2), LogisBean.class);
//								Log.e("oppop", bean.data.toString());
								ToastUtil.shortToast(context, bean.getMessage());
//								getHttprequst(false, true, 1, false);
							}

							@Override
							public void onFailure(int arg0, Header[] arg1, byte[] arg2, Throwable arg3) {
							}
						});

			}
		}

			}
	
}
