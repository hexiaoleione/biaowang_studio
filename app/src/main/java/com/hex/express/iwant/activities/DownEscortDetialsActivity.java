package com.hex.express.iwant.activities;

import java.text.DecimalFormat;
import java.util.List;

import org.apache.http.Header;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationConfiguration.LocationMode;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.route.BikingRouteResult;
import com.baidu.mapapi.search.route.DrivingRoutePlanOption;
import com.baidu.mapapi.search.route.DrivingRouteResult;
import com.baidu.mapapi.search.route.IndoorRouteResult;
import com.baidu.mapapi.search.route.MassTransitRouteResult;
import com.baidu.mapapi.search.route.OnGetRoutePlanResultListener;
import com.baidu.mapapi.search.route.PlanNode;
import com.baidu.mapapi.search.route.RoutePlanSearch;
import com.baidu.mapapi.search.route.TransitRouteResult;
import com.baidu.mapapi.search.route.WalkingRoutePlanOption;
import com.baidu.mapapi.search.route.WalkingRouteResult;
import com.baidu.mapapi.search.route.DrivingRoutePlanOption.DrivingPolicy;
import com.google.gson.Gson;
import com.hex.express.iwant.R;
import com.hex.express.iwant.iWantApplication;
import com.hex.express.iwant.bean.BaseBean;
import com.hex.express.iwant.bean.CityBean;
import com.hex.express.iwant.constance.MCUrl;
import com.hex.express.iwant.constance.PreferenceConstants;
import com.hex.express.iwant.helper.CheckDbUtils;
import com.hex.express.iwant.helper.CityDbOperation;
import com.hex.express.iwant.helper.DbManager;
import com.hex.express.iwant.homfragment.HomSubFragment1;
import com.hex.express.iwant.http.AsyncHttpUtils;
import com.hex.express.iwant.http.UrlMap;
import com.hex.express.iwant.mapapi.overlayutil.DrivingRouteOverlay;
import com.hex.express.iwant.mapapi.overlayutil.TransitRouteOverlay;
import com.hex.express.iwant.mapapi.overlayutil.WalkingRouteOverlay;
import com.hex.express.iwant.utils.AppUtils;
import com.hex.express.iwant.utils.MyBitmapUtils;
import com.hex.express.iwant.utils.PreferencesUtils;
import com.hex.express.iwant.utils.ToastUtil;
import com.hex.express.iwant.views.TitleBarView;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ZoomControls;
import android.widget.LinearLayout.LayoutParams;
import android.widget.PopupWindow.OnDismissListener;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 【货物详情】
 * 
 * @author SCHT-50
 * 
 */
public class DownEscortDetialsActivity extends BaseActivity {
	private View vPopupWindow_recceivedConfirm, vPopupWindow_deliveredConfirm;
	private PopupWindow popupWindow_recceivedConfirm;
	@Bind(R.id.tbv_show)
	TitleBarView tbv_show;
	@Bind(R.id.img_head)
	ImageView img_head;
	@Bind(R.id.img_message)
	ImageView img_message;
	@Bind(R.id.img_phone)
	ImageView img_phone;
	@Bind(R.id.postTime)
	TextView postTime;
	@Bind(R.id.today_boum)
	TextView today_boum;
	@Bind(R.id.today_redboum)
	TextView today_redboum;
	@Bind(R.id.good_detial)
	TextView good_detial;
	@Bind(R.id.tv_size_of_goodse)
	TextView tv_size_of_goodse;
	
	@Bind(R.id.money_detial)//货物详情
	TextView money_detial;
	@Bind(R.id.tv_weight_of_goods)//货物重量
	TextView tv_weight_of_goods;
	@Bind(R.id.tv_size_of_goods)//货物尺寸
	TextView tv_size_of_goods;
	@Bind(R.id.tv_content_of_goods)//货物备注
	TextView tv_content_of_goods;
	@Bind(R.id.replaceMoney)//代收款
	TextView replaceMoney;
	@Bind(R.id.tv_content_of_usetime)//时间
	TextView tv_content_of_usetime;
	@Bind(R.id.tv_you)//时间
	TextView tv_you;
	
	
	@Bind(R.id.bmapView)
	MapView bmapView;
	BaiduMap map;
	@Bind(R.id.btn_location)
	Button btn_location;
	@Bind(R.id.btn_submit)
	Button btn_submit;
	public LocationMode mCurrrentMode = LocationMode.FOLLOWING;
	private double latitude;// 经度
	private double longitude;// 纬度
	private String cityName;// 城市名字
	private boolean isFirstLoc = true;// 首次定位
	MyLocationListenner myListener;
	private LocationClient client;
	private String cityCode;
	private String city;
	private boolean isFirst = true;
	private String recId;// 货物的主键
	private String mobile;
	private String matImageUrl;
	//起始地经纬度
	private String fromLongitude;
	private String fromLatitude;
	//目的地经纬度
	private String toLatitude;
	private String toLongitude;
	private String transferTypeId;//1快车，2自行车，3步行
	private RoutePlanSearch routePlanSearch;// 路径规划搜索接口
	private int drivintResultIndex = 0;// 驾车路线方案index
	private String high;
	private String wide;
	private String length;
	private  Button btn_ok,btn_exit;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_escort_detial);
		ButterKnife.bind(DownEscortDetialsActivity.this);
		initData();
	}

	@Override
	public void onWeightClick(View v) {
	}

	@Override
	public void initView() {
	}

	@Override
	public void initData() {
		tbv_show.setTitleText("货物详情");
//		tv_content_of_goods.setText(getIntent().getStringExtra("matRemark"));
		today_boum.setText("起始地: "+getIntent().getStringExtra("address"));
		today_redboum.setText("目的地: "+getIntent().getStringExtra("addressTo"));
		String litime=getIntent().getStringExtra("limitTime");
		if (!litime.equals("")) {
			postTime.setText("限定到达时间："+getIntent().getStringExtra("limitTime"));
		}else {
			postTime.setText("发货时间："+getIntent().getStringExtra("publishTime"));
		}
		
		DecimalFormat df = new DecimalFormat("######0.00");
		money_detial.setText(df.format(Double.parseDouble(getIntent().getStringExtra("transferMoney")))+"元");
		good_detial.setText(getIntent().getStringExtra("matName"));
		mobile = getIntent().getStringExtra("mobile");
		recId = getIntent().getStringExtra("recId");
//		high=getIntent().getStringExtra("high");
//		wide=getIntent().getStringExtra("wide");
//		length=getIntent().getStringExtra("length");
//		tv_size_of_goods.setVisibility(View.GONE);
		if (!"".equals(getIntent().getStringExtra("matRemark"))) {
			tv_content_of_goods.setText("备注："+getIntent().getStringExtra("matRemark"));//备注
		}else {
			tv_content_of_goods.setVisibility(View.GONE);
		}
		if (!"".equals(getIntent().getStringExtra("useTime"))) {
			tv_content_of_usetime.setText("取货时间："+getIntent().getStringExtra("useTime"));//备注
		}else {
			tv_content_of_usetime.setVisibility(View.GONE);
		}
		
		if (!"".equals(getIntent().getStringExtra("carLength"))) {
			if(getIntent().getStringExtra("carLength").equals("1")){
				tv_size_of_goods.setText("车长需求：无");
			}else if(getIntent().getStringExtra("carLength").equals("2")){
				tv_size_of_goods.setText("车长需求：1.8米");
			}else if (getIntent().getStringExtra("carLength").equals("3")) {
				tv_size_of_goods.setText("车长需求：2.7米");
			}else if (getIntent().getStringExtra("carLength").equals("4")) {
				tv_size_of_goods.setText("车长需求：4.2米");
			}
		}else {
			tv_size_of_goods.setVisibility(View.GONE);
		}
		if (!"".equals(getIntent().getStringExtra("carLength"))) {
			tv_size_of_goodse.setText("体积："+getIntent().getStringExtra("matVolume") );
		}else {
			tv_size_of_goodse.setVisibility(View.GONE);
		}
	
		tv_weight_of_goods.setText(getIntent().getStringExtra("matWeight")+"公斤");//重量
		Log.e("lkdjll", recId);
		fromLatitude = getIntent().getStringExtra("fromLatitude");
		fromLongitude = getIntent().getStringExtra("fromLongitude");
		toLatitude = getIntent().getStringExtra("toLatitude");
		toLongitude = getIntent().getStringExtra("toLongitude");
		matImageUrl = getIntent().getStringExtra("matImageUrl");
		if (getIntent().getStringExtra("ifReplaceMoney").equals("true") && !"".equals(getIntent().getStringExtra("ifReplaceMoney"))
				&& !"".equals(getIntent().getStringExtra("replaceMoney"))) {
			replaceMoney.setVisibility(View.VISIBLE);
			replaceMoney.setText("代收款："+getIntent().getStringExtra("replaceMoney")+" 元");
		}else {
			replaceMoney.setVisibility(View.GONE);
		}
		// 允许定位
		map = bmapView.getMap();
		map.setMyLocationEnabled(true);
		map.setMapType(BaiduMap.MAP_TYPE_NORMAL);
		map.setMyLocationConfigeration(new MyLocationConfiguration(mCurrrentMode, true, null));
		// 隐藏百度的图标
		View childAt = bmapView.getChildAt(1);
		if (childAt != null && (childAt instanceof ImageView || childAt instanceof ZoomControls)) {
			childAt.setVisibility(View.INVISIBLE);
		}
		// 隐藏比例尺
		bmapView.showScaleControl(false);
		// 隐藏缩放控件
		bmapView.showZoomControls(false);
		client = new LocationClient(getApplicationContext());
		initLocation();
		client.registerLocationListener(new BDLocationListener() {

			@Override
			public void onReceiveLocation(BDLocation arg0) {
				if (arg0 == null || isFirst == false)
					return;
				LatLng cenpt = new LatLng(arg0.getLatitude(), arg0.getLongitude());
				latitude = arg0.getLatitude();
				longitude = arg0.getLongitude();
				Log.e("la", latitude + "");
				city = arg0.getCity();
				getCityCode();
				// 定义地图状态
				MapStatus mMapStatus = new MapStatus.Builder().target(cenpt).zoom(15f).build();
				// 定义MapStatusUpdate对象，以便描述地图状态将要发生的变化
				MapStatusUpdate mMapStatusUpdate = MapStatusUpdateFactory.newMapStatus(mMapStatus);
				// 改变地图状态
				map.setMapStatus(mMapStatusUpdate);
				isFirst = false;
				// 定义Maker坐标点
				LatLng point = new LatLng(latitude, longitude);
				// 构建Marker图标
				BitmapDescriptor bitmap = BitmapDescriptorFactory.fromResource(R.drawable.bnavi_icon_location_fixed);
				// 构建MarkerOption，用于在地图上添加Marker
				OverlayOptions option = new MarkerOptions().position(point).icon(bitmap);
				// 在地图上添加Marker，并显示
				map.addOverlay(option);
			}
		});
		// 初始化定位
		// 打开GPS
		client.start();
//		if (!matImageUrl.equals("")&&!matImageUrl.equals("null")) {
//			loader.displayImage(matImageUrl, img_head, options);
////			new MyBitmapUtils().display(img_head, matImageUrl);
//		}else {
//			img_head.setBackgroundResource(R.drawable.wu_cou);
//		}
		routePlanSearch = RoutePlanSearch.newInstance();
		routePlanSearch.setOnGetRoutePlanResultListener(routePlanResultListener);
		 drivingSearch();
	}

	/**
	 * 驾车线路查询
	 */
	private void drivingSearch() {
		DrivingRoutePlanOption drivingOption = new DrivingRoutePlanOption();
		drivingOption.from(
				PlanNode.withLocation(new LatLng(Double.parseDouble(fromLatitude), Double.parseDouble(fromLongitude))));// 设置起点
		drivingOption
				.to(PlanNode.withLocation(new LatLng(Double.parseDouble(toLatitude), Double.parseDouble(toLongitude))));// 设置终点
		routePlanSearch.drivingSearch(drivingOption);// 发起驾车路线规划
	}

	/**
	 * 步行路线查询
	 */
	private void walkSearch() {
		WalkingRoutePlanOption walkOption = new WalkingRoutePlanOption();
		walkOption.from(
				PlanNode.withLocation(new LatLng(Double.parseDouble(fromLatitude), Double.parseDouble(fromLongitude))));// 设置起点
		walkOption
				.to(PlanNode.withLocation(new LatLng(Double.parseDouble(toLatitude), Double.parseDouble(toLongitude))));// 设置终点
		routePlanSearch.walkingSearch(walkOption);
	}

	/**
	 * 路线规划结果回调
	 */
	OnGetRoutePlanResultListener routePlanResultListener = new OnGetRoutePlanResultListener() {

		/**
		 * 步行路线结果回调
		 */
		@Override
		public void onGetWalkingRouteResult(WalkingRouteResult walkingRouteResult) {
			map.clear();
			if (walkingRouteResult == null || walkingRouteResult.error != SearchResult.ERRORNO.NO_ERROR) {
				Toast.makeText(DownEscortDetialsActivity.this, "抱歉，未找到合适的路线", Toast.LENGTH_SHORT).show();
			}
			if (walkingRouteResult.error == SearchResult.ERRORNO.AMBIGUOUS_ROURE_ADDR) {
				// TODO
				return;
			}
			if (walkingRouteResult.error == SearchResult.ERRORNO.NO_ERROR) {
				WalkingRouteOverlay walkingRouteOverlay = new WalkingRouteOverlay(map);
				walkingRouteOverlay.setData(walkingRouteResult.getRouteLines().get(drivintResultIndex));
				map.setOnMarkerClickListener(walkingRouteOverlay);
				walkingRouteOverlay.addToMap();
				walkingRouteOverlay.zoomToSpan();
				// int totalLine = walkingRouteResult.getRouteLines().size();
				// Toast.makeText(DownEscortDetialsActivity.this,
				// "共查询出" + totalLine + "条符合条件的线路", 1000).show();

			}
		}

		/**
		 * 换成路线结果回调
		 */
		@Override
		public void onGetTransitRouteResult(TransitRouteResult transitRouteResult) {
			map.clear();
			if (transitRouteResult == null || transitRouteResult.error != SearchResult.ERRORNO.NO_ERROR) {
				Toast.makeText(DownEscortDetialsActivity.this, "抱歉，未找到合适的路线", Toast.LENGTH_SHORT).show();
			}
			if (transitRouteResult.error == SearchResult.ERRORNO.AMBIGUOUS_ROURE_ADDR) {
				// 起终点或途经点地址有岐义，通过以下接口获取建议查询信息
				// drivingRouteResult.getSuggestAddrInfo()
				return;
			}
			if (transitRouteResult.error == SearchResult.ERRORNO.NO_ERROR) {
				TransitRouteOverlay transitRouteOverlay = new TransitRouteOverlay(map);
				transitRouteOverlay.setData(transitRouteResult.getRouteLines().get(drivintResultIndex));// 设置一条驾车路线方案
				map.setOnMarkerClickListener(transitRouteOverlay);
				transitRouteOverlay.addToMap();
				transitRouteOverlay.zoomToSpan();
				// int totalLine = transitRouteResult.getRouteLines().size();
				// Toast.makeText(DownEscortDetialsActivity.this, "共查询出" +
				// totalLine + "条符合条件的线路", 1000).show();

			}
		}

		@Override
		public void onGetMassTransitRouteResult(MassTransitRouteResult massTransitRouteResult) {

		}

		/**
		 * 驾车路线结果回调 查询的结果可能包括多条驾车路线方案
		 */
		@Override
		public void onGetDrivingRouteResult(DrivingRouteResult drivingRouteResult) {
			map.clear();
			if (drivingRouteResult == null || drivingRouteResult.error != SearchResult.ERRORNO.NO_ERROR) {
				Toast.makeText(DownEscortDetialsActivity.this, "抱歉，未找到合适的路线", Toast.LENGTH_SHORT).show();
			}
			if (drivingRouteResult.error == SearchResult.ERRORNO.AMBIGUOUS_ROURE_ADDR) {
				// 起终点或途经点地址有岐义，通过以下接口获取建议查询信息
				// drivingRouteResult.getSuggestAddrInfo()
				return;
			}
			if (drivingRouteResult.error == SearchResult.ERRORNO.NO_ERROR) {
				DrivingRouteOverlay drivingRouteOverlay = new DrivingRouteOverlay(map);
				drivingRouteOverlay.setData(drivingRouteResult.getRouteLines().get(drivintResultIndex));// 设置一条驾车路线方案
				map.setOnMarkerClickListener(drivingRouteOverlay);
				drivingRouteOverlay.addToMap();
				drivingRouteOverlay.zoomToSpan();
				// int totalLine = drivingRouteResult.getRouteLines().size();
				// Toast.makeText(DownEscortDetialsActivity.this, "共查询出" +
				// totalLine + "条符合条件的线路", 1000).show();
			}
		}

		@Override
		public void onGetIndoorRouteResult(IndoorRouteResult indoorRouteResult) {

		}

		@Override
		public void onGetBikingRouteResult(BikingRouteResult bikingRouteResult) {

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
		cityCode = selectDataFromDb.get(0).city_code;
		Log.e("citycode", cityCode);
	}

	/**
	 * 百度地图初始化
	 */
	private void initLocation() {
		LocationClientOption option = new LocationClientOption();
		option.setLocationMode(com.baidu.location.LocationClientOption.LocationMode.Hight_Accuracy);// 可选，默认高精度，设置定位模式，高精度，低功耗，仅设备
		option.setCoorType("bd09ll");// 可选，默认gcj02，设置返回的定位结果坐标系
		int span = 60000;
		option.setScanSpan(0);// 可选，默认0，即仅定位一次，设置发起定位请求的间隔需要大于等于1000ms才是有效的
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

	public class MyLocationListenner implements BDLocationListener {

		@Override
		public void onReceiveLocation(BDLocation location) {
			// map view 销毁后不在处理新接收的位置
			if (location == null || bmapView == null)
				return;
			MyLocationData locData = new MyLocationData.Builder().accuracy(location.getRadius())
					// 此处设置开发者获取到的方向信息，顺时针0-360
					.direction(100).latitude(location.getLatitude()).longitude(location.getLongitude()).build();
			map.setMyLocationData(locData);
			if (isFirstLoc) {
				isFirstLoc = false;
				LatLng ll = new LatLng(location.getLatitude(), location.getLongitude());
				MapStatusUpdate u = MapStatusUpdateFactory.newLatLng(ll);
				map.animateMapStatus(u);
			}
		}

		public void onReceivePoi(BDLocation poiLocation) {
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		bmapView.onResume();
	}

	@Override
	protected void onPause() {
		super.onPause();
		bmapView.onPause();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		routePlanSearch.destroy();// 释放检索实例
		bmapView.onDestroy();
	}
//R.id.btn_location,
	@OnClick({ R.id.btn_submit,  R.id.img_message, R.id.img_phone })
	public void MyonClick(View view) {
		switch (view.getId()) {
		case R.id.btn_submit:// 接镖	
			sumit();
//			Intent intent=new Intent();
//			intent.putExtra("type", "1");
//			setResult(RESULT_OK, intent);
//			finish();
//			dialog() ;
			break;
//		case R.id.btn_location:// 定位
//			MapStatus mMapStatus = new MapStatus.Builder().target(new LatLng(latitude, longitude)).zoom(15f).build();
//			// 定义MapStatusUpdate对象，以便描述地图状态将要发生的变化
//			MapStatusUpdate mMapStatusUpdate = MapStatusUpdateFactory.newMapStatus(mMapStatus);
//			// 改变地图状态
//			map.setMapStatus(mMapStatusUpdate);
//			break;
		case R.id.img_message:// 发信息

			break;
		case R.id.img_phone:// 打电话
			AppUtils.intentDial(DownEscortDetialsActivity.this, mobile);
			break;

		default:
			break;
		}
	}
	private void sumit(){
		if ("true".equals(getIntent().getStringExtra("ifReplaceMoney"))) {
			showPaywindowdai();
		}else {
			addEscortreuslt();
		}
		
	}
	// 点击“接单”所弹出的dilog;
	private void dialog() {
		  final Builder builder = new Builder(DownEscortDetialsActivity.this);
		  builder.setMessage("是否要接单吗？");

		  builder.setTitle("确定");
        builder.setPositiveButton("", new DialogInterface.OnClickListener() {
			
			public void onClick(DialogInterface arg0, int arg1) {
				// TODO Auto-generated method stub
				addEscortreuslt();
			}
		});
  builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
			
			public void onClick(DialogInterface arg0, int arg1) {
				// TODO Auto-generated method stub
				builder.create().dismiss();
			}
		});

		  builder.create().show();
		 }
	
	private void showPopupWindow_receivedConfirm() {
		 LinearLayout ll_setChecked;
		
		vPopupWindow_recceivedConfirm = LayoutInflater.from(this).inflate(R.layout.puwindsde, null);
		ll_setChecked = (LinearLayout) vPopupWindow_recceivedConfirm.findViewById(R.id.ll_setChecked);
		btn_ok = (Button) vPopupWindow_recceivedConfirm.findViewById(R.id.btn_recceivedOKs);
		btn_exit = (Button) vPopupWindow_recceivedConfirm.findViewById(R.id.btn_recceivedExit);
		popupWindow_recceivedConfirm = new PopupWindow(vPopupWindow_recceivedConfirm, LayoutParams.MATCH_PARENT,
				LayoutParams.MATCH_PARENT, true);
		// 设置背景 使返回键生效（PopupWindow会拦截触摸事件，包括返回键和菜单键）
		popupWindow_recceivedConfirm.setBackgroundDrawable(new ColorDrawable(0x99999999));

		vPopupWindow_recceivedConfirm.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				System.out.println("1111111111 22 ");
				btn_exit.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View arg0) {
						// TODO Auto-generated method stub
						popupWindow_recceivedConfirm.dismiss();
						
					}
				});
				btn_ok.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View arg0) {
						// TODO Auto-generated method stub
						addEscortreuslt();
						popupWindow_recceivedConfirm.dismiss();
						
					}
				});
				if (vPopupWindow_recceivedConfirm.isShown()) {
					popupWindow_recceivedConfirm.dismiss();
				}
				return false;
			}
		});

	}
	/**
	 * 镖师接镖
	 */
	private void addEscortreuslt() {
		Log.e("url",
				UrlMap.getTwo(MCUrl.DOWNROBORDER, "userId",
						String.valueOf(PreferencesUtils.getInt(getApplicationContext(), PreferenceConstants.UID)),
						"recId", recId));
//		if (getIntent().getStringExtra("whether").equals("A") || getIntent().getStringExtra("whether").equals("B")) {
//		}
		dialog.show();
		AsyncHttpUtils.doGet(UrlMap.getTwo(MCUrl.DOWNROBORDER, "userId", String.valueOf(PreferencesUtils.getInt(getApplicationContext(), PreferenceConstants.UID)), "recId", recId), null, null, null,
				new AsyncHttpResponseHandler() {
					@Override
					public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
						Log.e("msg", new String(arg2));
						dialog.dismiss();
						BaseBean bean = new Gson().fromJson(new String(arg2), BaseBean.class);
						if (bean.getErrCode() == 0) {
							ToastUtil.shortToast(DownEscortDetialsActivity.this, bean.getMessage());
//							sendBroadcast(new Intent().setAction("freight").putExtra("flag", "escort"));//镖师接镖成功后						
							Intent intent=new Intent();
							intent.putExtra("type", "1");
							setResult(RESULT_OK, intent);
							finish();
						}else if (bean.getErrCode() == 2 || bean.getErrCode() == -2) {
							ToastUtil.shortToast(DownEscortDetialsActivity.this, bean.getMessage());
							finish();
						}else if (bean.getErrCode() == 4)  {
							
//							ToastUtil.shortToast(DownEscortDetialsActivity.this, bean.getMessage());
							Builder ad = new Builder(DownEscortDetialsActivity.this);
							ad.setTitle("温馨提示");
							ad.setMessage(bean.getMessage());
							ad.setNegativeButton("去充值", new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface arg0, int arg1) {
//									getHttpMessages(true, false, 1, false);
									Intent intent = new Intent();
									startActivity(intent.setClass(DownEscortDetialsActivity.this, RechargeActivity.class));
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
							ToastUtil.shortToast(DownEscortDetialsActivity.this, bean.getMessage());
						}

					}

					@Override
					public void onFailure(int arg0, Header[] arg1, byte[] arg2, Throwable arg3) {
						dialog.dismiss();
					}
				});

	}
	@Override
	public boolean dispatchKeyEvent(KeyEvent event) {
		if (event.getKeyCode() == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
			finish();
			return true;
		}
		return super.dispatchKeyEvent(event);
	}

	@Override
	public void setOnClick() {

	}

	@Override
	public void getData() {
		// TODO Auto-generated method stub

	}
	/**
	 * 显示投保提示信息
	 */
	private void showPaywindowdai() {
		
		TextView btnsaves_pan,tet_tishi;
		TextView exti;
		// 利用layoutInflater获得View
		LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View view = inflater.inflate(R.layout.popwidndow_daijie, null);
		// 下面是两种方法得到宽度和高度 getWindow().getDecorView().getWidth()
		final PopupWindow window02 = new PopupWindow(view, WindowManager.LayoutParams.MATCH_PARENT,
				WindowManager.LayoutParams.MATCH_PARENT);
		// 设置popWindow弹出窗体可点击，这句话必须添加，并且是true
		window02.setFocusable(true);
		// 实例化一个ColorDrawable颜色为半透明
		// ColorDrawable dw = new ColorDrawable(R.color.transparent01);
		ColorDrawable dw = new ColorDrawable(android.R.color.white);
		window02.setBackgroundDrawable(dw);
		window02.setOutsideTouchable(false);// 这是点击外部不消失
		// 设置popWindow的显示和消失动画
		window02.setAnimationStyle(R.style.mypopwindow_anim_style);
		// 在底部显示
		window02.showAtLocation(DownEscortDetialsActivity.this.findViewById(R.id.btn_submit), Gravity.CENTER, 0, 0);
		btnsaves_pan=(TextView) view.findViewById(R.id.btnsaves_pan);
		exti=(TextView) view.findViewById(R.id.exti);
		btnsaves_pan.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				addEscortreuslt();
				window02.dismiss();
			}
		});
		exti.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				window02.dismiss();
			}
		});
		// popWindow消失监听方法
		window02.setOnDismissListener(new OnDismissListener() {

			@Override
			public void onDismiss() {

			}
		});

	}
}
