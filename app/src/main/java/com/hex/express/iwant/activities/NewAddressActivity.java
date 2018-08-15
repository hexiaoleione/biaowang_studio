package com.hex.express.iwant.activities;

import java.util.ArrayList;
import java.util.List;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.LocationClientOption.LocationMode;
import com.baidu.mapapi.search.core.PoiInfo;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeOption;
import com.baidu.mapapi.search.poi.OnGetPoiSearchResultListener;
import com.baidu.mapapi.search.poi.PoiCitySearchOption;
import com.baidu.mapapi.search.poi.PoiDetailResult;
import com.baidu.mapapi.search.poi.PoiResult;
import com.baidu.mapapi.search.poi.PoiSearch;
import com.baidu.mapapi.search.poi.PoiSortType;
import com.baidu.mapapi.search.sug.OnGetSuggestionResultListener;
import com.baidu.mapapi.search.sug.SuggestionResult;
import com.baidu.mapapi.search.sug.SuggestionResult.SuggestionInfo;
import com.baidu.mapapi.search.sug.SuggestionSearch;
import com.baidu.mapapi.search.sug.SuggestionSearchOption;
import com.hex.express.iwant.R;
import com.hex.express.iwant.iWantApplication;
import com.hex.express.iwant.adapters.AddressAdapter;
import com.hex.express.iwant.bean.POIBean;
import com.hex.express.iwant.constance.PreferenceConstants;
import com.hex.express.iwant.customer.BaseFragmentActiviy;
import com.hex.express.iwant.utils.PreferencesUtils;
import com.hex.express.iwant.utils.ToastUtil;
import com.hex.express.iwant.views.LoadingProgressDialog;
import com.hex.express.iwant.views.TitleBarView;

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
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class NewAddressActivity extends BaseActivity {
	// public LocationClient mLocationClient = null;
	public String address;
	@Bind(R.id.tv_location)
	TextView tv_location;
	@Bind(R.id.tv_my_location)
	EditText tv_my_location;
//	@Bind(R.id.tbv_show)
//	TitleBarView tbv_show;
	@Bind(R.id.btnLeft)//返回
	ImageView btnLeft;
	@Bind(R.id.btnRight)//返回
	TextView btnRight;
	@Bind(R.id.et_address)
	EditText et_address;
	@Bind(R.id.lv)
	ListView lv;
	@Bind(R.id.view)
	View view;
	@Bind(R.id.text_address)
	TextView text_address;
	private AddressAdapter adapter;
	@Bind(R.id.rl_selectAddress)
	RelativeLayout rl_selectAddress;
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
	private boolean first = false;
	String townaddressd;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_newaddress);
		iWantApplication.getInstance().addActivity(this);
		ButterKnife.bind(this);
		initView();
		initData();
		initGeo();
	}

	private void initGeo() {
		reverseCode = new ReverseGeoCodeOption();
	}

	@OnClick({ R.id.text_address ,R.id.text_queren})
	public void myOnclick(View view) {
		switch (view.getId()) {
		case R.id.text_address:
//			startActivityForResult(new Intent(NewAddressActivity.this, SearchActivity.class), 1);
			startActivityForResult(new Intent(NewAddressActivity.this, NewSearchActivity.class), 1);
			break;
		case R.id.text_queren:
			Builder ad = new Builder(NewAddressActivity.this);
			ad.setTitle("温馨提示");
			ad.setMessage("当前地址为:" + tv_my_location.getText() + " ," + "  是否使用此地址");
			ad.setPositiveButton("确认", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int which) {
					Intent intent = new Intent();
					intent.putExtra("address", tv_my_location.getText().toString());
					intent.putExtra("latitude", latitude);
					intent.putExtra("longitude", longitude);
					intent.putExtra("townaddressd", townaddressd);
					intent.putExtra("city", city);
					setResult(RESULT_OK, intent);
					finish();
				}
			});
			ad.setNegativeButton("取消", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int which) {
					dialog.dismiss();
				}
			});
			ad.create().show();
			break;
		default:
			break;
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		switch (requestCode) {
		case 1:
			if (resultCode == Activity.RESULT_OK) {
				citytwo = data.getStringExtra("citytwo");
				first = true;
				text_address.setText(citytwo);
			}

			break;

		default:
			break;
		}
	}

	public void onWeightClick(View v) {
		// TODO Auto-generated method stub
	}

	public void initView() {
		// TODO Auto-generated method stub
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
		Intent intent = new Intent();
//		Log.e("list", info.toString());
		intent.putExtra("city", text_address.getText());
//		intent.putExtra("townaddressd", info.district);
		intent.putExtra("address", text_address.getText()+""+et_address.getText());
//		intent.putExtra("latitude", info.pt.latitude);
//		intent.putExtra("longitude", info.pt.longitude);
		setResult(RESULT_OK, intent);
		finish();
						}
		});
	}

	public void initData() {
		
		flag = true;
//		tbv_show.setTitleText("地址定位");
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
		 townaddressd = PreferencesUtils.getString(getApplicationContext(), PreferenceConstants.Codedess);
		tv_my_location.setText(addressd);
		tv_my_location.setFocusable(true);
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
//					Log.e("logcitylog", city);
					latitude = arg0.getLatitude();
					longitude = arg0.getLongitude();
					if (flag) {
						text_address.setText(city);
						flag = false;
					}
					// tv_my_location.setText(arg0.getAddrStr());
					// tv_my_location.setSelection(arg0.getAddrStr().length());
				}

			}
		});
		client.start();
		//搜索的输入监听
//		et_address.addTextChangedListener(new TextWatcher() {
//
//			@Override
//			public void onTextChanged(CharSequence s, int start, int before, int count) {
//				if (s != null && !s.toString().equals("") && city != null) {
//					if (!first) {
////						Log.e("citylog", text_address.getText().toString());
//						search.requestSuggestion(new SuggestionSearchOption().keyword(s.toString()).city(city));
//
//					} else {
////						Log.e("cityloglog", text_address.getText().toString());
//						search.requestSuggestion(new SuggestionSearchOption().keyword(s.toString()).city(citytwo));
//					}
//				}
//			}
//
//			@Override
//			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//
//			}
//
//			@Override
//			public void afterTextChanged(Editable s) {
//				if (s.toString().equals("")) {
//					lv.setVisibility(View.GONE);
//					view.setVisibility(View.VISIBLE);
//				} else {
//					lv.setVisibility(View.VISIBLE);
//					view.setVisibility(View.GONE);
////					tv_my_location.setText("");
//				}
//			}
//		});

//		search.setOnGetSuggestionResultListener(new OnGetSuggestionResultListener() {
//
//			@Override
//			public void onGetSuggestionResult(SuggestionResult arg0) {
//				// Log.e("TAG", arg0.getAllSuggestions().get(1).district);
//				if (arg0 != null && arg0.getAllSuggestions() != null && arg0.getAllSuggestions().size() != 0) {
//					list = arg0.getAllSuggestions();
//					for (int i = 0; i < list.size(); i++) {
//						if (list.get(i).city == null || list.get(i).district == null || list.get(i).key == null
//								|| list.get(i).city.equals("") || list.get(i).district.equals("")
//								|| list.get(i).pt == null) {
//							list.remove(i);
//						}
//					}
//					adapter = new AddressAdapter(getApplicationContext(), list);
//					lv.setAdapter(adapter);
//					lv.setVisibility(View.VISIBLE);
//					view.setVisibility(View.GONE);
//					lv.setOnItemClickListener(new OnItemClickListener() {
//
//						@Override
//						public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//							// TODO Auto-generated method stub
//							SuggestionInfo info = list.get(position);
//							if (info.pt == null) {
//								ToastUtil.shortToast(getApplicationContext(), "选择的地址有误,请重新选择");
//								return;
//							}
////							ToastUtil.longToast(getApplicationContext(), "hashCode"+info.hashCode()+" district"+info.district
////									+" city"+info.city+" key"+info.key);
////							Intent intent = new Intent();
//////							Log.e("list", info.toString());
////							intent.putExtra("city", info.city);
////							intent.putExtra("townaddressd", info.district);
////							intent.putExtra("address", info.city + info.district + info.key);
////							intent.putExtra("latitude", info.pt.latitude);
////							intent.putExtra("longitude", info.pt.longitude);
////							setResult(RESULT_OK, intent);
////							finish();
//						}
//
//					});
//				} else {
//					ToastUtil.shortToast(getApplicationContext(), "输入的地址有误，请重新输入");
//					lv.setVisibility(View.GONE);
//					view.setVisibility(View.VISIBLE);
//				}
//			}
//		});
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

	public void setOnClick() {

	}

	public void getData() {
		// TODO Auto-generated method stub
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
