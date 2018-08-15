package com.hex.express.iwant.activities;

import android.view.View;
import android.view.View.OnClickListener;

import java.util.ArrayList;
import java.util.List;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.LocationClientOption.LocationMode;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeOption;
import com.baidu.mapapi.search.poi.PoiSearch;
import com.baidu.mapapi.search.sug.OnGetSuggestionResultListener;
import com.baidu.mapapi.search.sug.SuggestionResult;
import com.baidu.mapapi.search.sug.SuggestionSearch;
import com.baidu.mapapi.search.sug.SuggestionSearchOption;
import com.baidu.mapapi.search.sug.SuggestionResult.SuggestionInfo;
import com.hex.express.iwant.R;
import com.hex.express.iwant.iWantApplication;
import com.hex.express.iwant.activities.BaseActivity;
import com.hex.express.iwant.activities.DepotActivity;
import com.hex.express.iwant.adapters.AddressAdapter;
import com.hex.express.iwant.constance.PreferenceConstants;
import com.hex.express.iwant.utils.PreferencesUtils;
import com.hex.express.iwant.utils.ToastUtil;
import com.hex.express.iwant.views.LoadingProgressDialog;

import android.R.integer;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.ToggleButton;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.CompoundButton.OnCheckedChangeListener;
import butterknife.Bind;
import butterknife.ButterKnife;


public class DepotAddsActivity  extends BaseActivity{
	@Bind(R.id.btnLeft)
	ImageView btnLeft;
	@Bind(R.id.deadd_addse)//搜索的
	EditText deadd_addse;
	@Bind(R.id.deadd_addsetwo)//输入的
	EditText deadd_addsetwo;
	
	@Bind(R.id.tv_my_location)//定位的
	EditText tv_my_location;
	@Bind(R.id.text_queren)//确定
	TextView text_queren;
	@Bind(R.id.btn_posetadd)//确定按钮
	Button btn_posetadd;
	@Bind(R.id.lv)
	ListView lv;
	@Bind(R.id.view)
	View view;
	@Bind(R.id.depot_tog2)//默认地址
	ToggleButton depot_tog2;
	
	private AddressAdapter adapter;
	private SuggestionSearch search;
	private PoiSearch poi;
	private double latitude;
	private double longitude;
	private List<SuggestionInfo> list = new ArrayList<SuggestionInfo>();// 建议信息
	private GeoCoder mSearch;// 反地理位置编码
	private ReverseGeoCodeOption reverseCode;
	private LoadingProgressDialog dialog;
	// 定位的城市
	private String city;
	// 自己选的城市
	private String citytwo;
	private LocationClient client;
	private BDLocation location;
	private InputMethodManager imm;

	private boolean flag;
	boolean top2=false;
	private boolean first = false;
	int  recId;
	private String citys,address;
	double latitudes,longitudes;
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		iWantApplication.getInstance().addActivity(this);
		setContentView(R.layout.activity_depotadd);
		ButterKnife.bind(DepotAddsActivity.this);
		initView();
		initGeo();
		initData();
		setOnClick();
		
	}

	private void initGeo() {
		reverseCode = new ReverseGeoCodeOption();
	}
	@Override
	public void onWeightClick(View v) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void initView() {
		// TODO Auto-generated method stub
		Log.e("11111way  ", getIntent().getStringExtra("way"));
		if (getIntent().getStringExtra("way").equals("1")) {
			btn_posetadd.setText("确定修改");
			try {
				recId=getIntent().getIntExtra("recId", 0);
			if (getIntent().getBooleanExtra("ifDefault", false)) {
				depot_tog2.setChecked(true);
				top2=true;
			}	
			if (!getIntent().getStringExtra("locationAddress").equals("") || getIntent().getStringExtra("locationAddress")!=null) {
				deadd_addse.setText(getIntent().getStringExtra("locationAddress"));
//				address=deadd_addse.getText().toString();
			}
			if (!getIntent().getStringExtra("address").equals("") || getIntent().getStringExtra("way")!=null) {
				deadd_addsetwo.setText(getIntent().getStringExtra("address"));
			}
			
			} catch (Exception e) {
				// TODO: handle exception
			}
		}
		
		flag = true;
		imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		dialog = new LoadingProgressDialog(this);
		dialog.show();
		search = SuggestionSearch.newInstance();
		poi = PoiSearch.newInstance();
		client = new LocationClient(getApplicationContext());
		// 设置
		setLocationParams();
		// String addressd = getIntent().getStringExtra("address");
		String addressd = PreferencesUtils.getString(getApplicationContext(), PreferenceConstants.ADDRESS);
		tv_my_location.setText(addressd);
		
		client.registerLocationListener(new BDLocationListener() {
			@Override
			public void onReceiveLocation(final BDLocation arg0) {
				// TODO Auto-generated method stub
				if (arg0 == null) {
					ToastUtil.shortToast(getApplicationContext(), "定位失败，请检查设置");
					return;
				} else {
					if (dialog != null)
						dialog.dismiss();
					city = arg0.getCity();
//					arg0.g
					Log.e("logcitylog", city);
					latitude = arg0.getLatitude();
					longitude = arg0.getLongitude();
					if (flag) {
//						text_address.setText(city);
						flag = false;
					}
					// tv_my_location.setText(arg0.getAddrStr());
					// tv_my_location.setSelection(arg0.getAddrStr().length());
				}

			}
		});
		client.start();
		//搜索的输入监听
		deadd_addse.addTextChangedListener(new TextWatcher() {

					@Override
					public void onTextChanged(CharSequence s, int start, int before, int count) {
						if (s != null && !s.toString().equals("") && city != null) {
							btn_posetadd.setVisibility(View.VISIBLE);
//							if (!first) {
								search.requestSuggestion(new SuggestionSearchOption().keyword(s.toString()).city(city));
//							} else {
//								search.requestSuggestion(new SuggestionSearchOption().keyword(s.toString()).city(citytwo));
//							}
						}
					}


					@Override
					public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
						// TODO Auto-generated method stub
						
					}
					@Override
					public void afterTextChanged(Editable s) {
						if (s.toString().equals("")) {
							lv.setVisibility(View.GONE);
							view.setVisibility(View.VISIBLE);
						} else {
							lv.setVisibility(View.VISIBLE);
							view.setVisibility(View.GONE);
//							tv_my_location.setText("");
						}
					}
		});
		deadd_addsetwo.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				// TODO Auto-generated method stub
				if (s != null && !s.toString().equals("") && city != null) {
					btn_posetadd.setVisibility(View.VISIBLE);
					}
			} 
			
			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void afterTextChanged(Editable arg0) {
				// TODO Auto-generated method stub
				
			}
		});
	}

	@Override
	public void initData() {
		// TODO Auto-generated method stub
		depot_tog2.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton arg0, boolean isChecked) {
				// TODO Auto-generated method stub
				if (isChecked) {
//					initView();
					btn_posetadd.setVisibility(View.VISIBLE);
					top2=isChecked;
				}else {
					btn_posetadd.setVisibility(View.VISIBLE);
					top2=isChecked;
				}
			}
		});
		
//		deadd_addse.setOnClickListener(new View.OnClickListener() {
//			
//			@Override
//			public void onClick(View arg0) {
//				// TODO Auto-generated method stub
//				startActivityForResult(new Intent(DepotAddsActivity.this, SearchActivity.class), 1);
//			}
//		});
		
		text_queren.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Builder ad = new Builder(DepotAddsActivity.this);
				ad.setTitle("温馨提示");
				ad.setMessage("当前地址为:" + tv_my_location.getText() + " ," + "  是否使用此地址");
				ad.setPositiveButton("确认", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						Intent intent = new Intent();
						intent.putExtra("address", tv_my_location.getText().toString());
						intent.putExtra("latitude", latitude);
						intent.putExtra("longitude", longitude);
						intent.putExtra("deadd_addsetwo", deadd_addsetwo.getText().toString());
						intent.putExtra("way", getIntent().getStringExtra("way"));
						intent.putExtra("city", city);
//						setResult(RESULT_OK, intent);
//						finish();
						address=tv_my_location.getText().toString();
						deadd_addse.setText(tv_my_location.getText().toString());
					}
				});
				ad.setNegativeButton("取消", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
					}
				});
				ad.create().show();
			}
		});
		btn_posetadd.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
//				Log.e("list", info.toString());
//				citys=info.city;
//				address=info.city + info.district + info.key;
//				latitudes=info.pt.latitude;
//				longitudes=info.pt.longitude;
//				if (citys.equals("")) {
//					citys=city;
//				}
//				if (latitudes==0.0) {
//					latitudes=latitude;
//				}
//				if (longitudes==0.0) {
//					longitudes=longitude;
//				}
//				intent.putExtra("city", citys);
//				intent.putExtra("deadd_addsetwo", deadd_addsetwo.getText().toString());
//				intent.putExtra("address", address);
//				intent.putExtra("latitude", latitudes);
//				intent.putExtra("longitude", longitudes);
				intent.putExtra("city", city);
				intent.putExtra("deadd_addsetwo", deadd_addsetwo.getText().toString());
				intent.putExtra("address", deadd_addse.getText().toString());
				intent.putExtra("latitude", latitude);
				intent.putExtra("longitude", longitude);
				intent.putExtra("ifDefault", top2);
//				ToastUtil.shortToast(DepotAddsActivity.this, "address"+deadd_addse.getText().toString());
				intent.putExtra("way", getIntent().getStringExtra("way"));
				if (getIntent().getStringExtra("way").equals("1")) {
					intent.putExtra("recId", recId);
				}
				if (!deadd_addse.getText().toString().equals("") ) {
					setResult(RESULT_OK, intent);
					finish();
				}
				
			}
		});
		search.setOnGetSuggestionResultListener(new OnGetSuggestionResultListener() {
			@Override
			public void onGetSuggestionResult(SuggestionResult arg0) {
				// Log.e("TAG", arg0.getAllSuggestions().get(1).district);
				if (arg0 != null && arg0.getAllSuggestions() != null && arg0.getAllSuggestions().size() != 0) {
					list = arg0.getAllSuggestions();
					for (int i = 0; i < list.size(); i++) {
						if (list.get(i).city == null || list.get(i).district == null || list.get(i).key == null
								|| list.get(i).city.equals("") || list.get(i).district.equals("")
								|| list.get(i).pt == null) {
							list.remove(i);
						}
					}
					adapter = new AddressAdapter(getApplicationContext(), list);
					lv.setAdapter(adapter);
					lv.setVisibility(View.VISIBLE);
					view.setVisibility(View.GONE);
					lv.setOnItemClickListener(new OnItemClickListener() {

						@Override
						public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
							// TODO Auto-generated method stub
							SuggestionInfo info = list.get(position);
							if (info.pt == null) {
								ToastUtil.shortToast(getApplicationContext(), "选择的地址有误,请重新选择");
								return;
							}
//							ToastUtil.longToast(getApplicationContext(), "hashCode"+info.hashCode()+" district"+info.district
//									+" city"+info.city+" key"+info.key);
//							Intent intent = new Intent();
							Log.e("list", info.toString());
							city=info.city;
							address=info.city + info.district + info.key;
							latitude=info.pt.latitude;
							longitude=info.pt.longitude;
							deadd_addse.setText(""+address);
//							intent.putExtra("city", info.city);
////							intent.putExtra("city", info.district);
//							intent.putExtra("address", info.city + info.district + info.key);
//							intent.putExtra("latitude", info.pt.latitude);
//							intent.putExtra("longitude", info.pt.longitude);
//							setResult(RESULT_OK, intent);
//							finish();
						}

					});
				} else {
//					ToastUtil.shortToast(getApplicationContext(), "输入的地址有误，请重新输入");
					lv.setVisibility(View.GONE);
					view.setVisibility(View.VISIBLE);
				}
			}
		});
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
	}

	@Override
	public void getData() {
		// TODO Auto-generated method stub
		
	}

	private void setLocationParams() {
		// TODO Auto-generated method stub
		Log.e("iiiii", "9");
		LocationClientOption option = new LocationClientOption();
		option.setLocationMode(LocationMode.Hight_Accuracy);// 可选，默认高精度，设置定位模式，高精度，低功耗，仅设备
		option.setCoorType("bd09ll");// 可选，默认gcj02，设置返回的定位结果坐标系
		int span = 60000;
		option.setScanSpan(span);// 可选，默认0，即仅定位一次，设置发起定位请求的间隔需要大于等于1000ms才是有效的
		option.setIsNeedAddress(true);// 可选，设置是否需要地址信息，默认不需要
		option.setOpenGps(true);// 可选，默认false,设置是否使用gps
		option.setLocationNotify(false);// 可选，默认false，设置是否当gps有效时按照1S1次频率输出GPS结果
		option.setIsNeedLocationDescribe(true);// 可选，默认false，设置是否需要位置语义化结果，可以在BDLocation.getLocationDescribe里得到，结果类似于“在北京天安门附近”
		option.setIsNeedLocationPoiList(true);// 可选，默认false，设置是否需要POI结果，可以在BDLocation.getPoiList里得到
		option.setIgnoreKillProcess(false);// 可选，默认true，定位SDK内部是一个SERVICE，并放到了独立进程，设置是否在stop的时候杀死这个进程，默认不杀死
		option.SetIgnoreCacheException(false);// 可选，默认false，设置是否收集CRASH信息，默认收集
		option.setEnableSimulateGps(false);// 可选，默认false，设置是否需要过滤gps仿真结果，默认需要
		client.setLocOption(option);
	}
	
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		switch (requestCode) {
		case 1:
			if (resultCode == Activity.RESULT_OK) {
				citytwo = data.getStringExtra("citytwo");
				first = true;
				deadd_addse.setText(citytwo);
			}

			break;

		default:
			break;
		}
	}
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		if (mSearch != null)
			mSearch.destroy();
		/*
		 * if(poi!=null) poi.destroy();
		 */
		if (poi != null)
			poi.destroy();
	}
}
