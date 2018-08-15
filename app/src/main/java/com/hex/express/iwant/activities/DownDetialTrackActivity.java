package com.hex.express.iwant.activities;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import cn.jpush.android.api.JPushInterface;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.GroundOverlay;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.map.Polyline;
import com.baidu.mapapi.map.PolylineOptions;
import com.baidu.mapapi.map.MyLocationConfiguration.LocationMode;
import com.baidu.mapapi.map.Overlay;
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
import com.baidu.mapapi.search.route.WalkingRouteResult;
import com.baidu.mapapi.utils.DistanceUtil;
import com.google.gson.Gson;
import com.hex.express.iwant.R;
import com.hex.express.iwant.bean.BaseBean;
import com.hex.express.iwant.bean.CurrentLocationBean;
import com.hex.express.iwant.bean.EscortInfoBean;
import com.hex.express.iwant.bean.IconBean;
import com.hex.express.iwant.bean.RegisterBean;
import com.hex.express.iwant.constance.MCUrl;
import com.hex.express.iwant.constance.MsgConstants;
import com.hex.express.iwant.constance.PreferenceConstants;
import com.hex.express.iwant.http.AsyncHttpUtils;
import com.hex.express.iwant.http.UrlMap;
import com.hex.express.iwant.mapapi.overlayutil.DrivingRouteOverlay;
import com.hex.express.iwant.mapapi.overlayutil.TransitRouteOverlay;
import com.hex.express.iwant.mapapi.overlayutil.WalkingRouteOverlay;
import com.hex.express.iwant.utils.AppUtils;
import com.hex.express.iwant.utils.Logger;
import com.hex.express.iwant.utils.MyBitmapUtils;
import com.hex.express.iwant.utils.PreferencesUtils;
import com.hex.express.iwant.utils.ToastUtil;
import com.hex.express.iwant.views.TitleBarView;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

import android.content.Context;
import android.content.Intent;
import android.gesture.GesturePoint;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Bitmap.Config;
import android.graphics.PorterDuff.Mode;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Message;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ZoomControls;
import android.widget.LinearLayout.LayoutParams;
import android.widget.PopupWindow.OnDismissListener;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 【货物跟踪】
 * 
 * @author SCHT-50
 * 
 */
public class DownDetialTrackActivity extends BaseActivity {
	// 标题
//	@Bind(R.id.tbv_show)
//	TitleBarView tbv_show;
	
	@Bind(R.id.btnLeft)
	ImageView btnLeft;
	@Bind(R.id.btnRight)
	TextView btnRight;

	// 头像
	@Bind(R.id.img_header)
	ImageView img_header;
	// 镖师名字
	@Bind(R.id.tv_driverName)
	TextView tv_driverName;
	// 评价服务
	@Bind(R.id.btn_evaluate)
	Button btn_evaluate;
	// 性别图标
	@Bind(R.id.img_sex)
	ImageView img_sex;
	// 接镖次数
	@Bind(R.id.tv_num)
	TextView tv_num;
	// 评分星星
	@Bind(R.id.rab_couriersscore)
	RatingBar rab_couriersscore;
	// 即时通讯图标
	@Bind(R.id.img_message)
	ImageView img_message;
	// 拨打电话图标
	@Bind(R.id.img_phone)
	ImageView img_phone;
	// 地图
	@Bind(R.id.bmapView)
	MapView mapView;
	//点击查看详情
	@Bind(R.id.xiangqing)
	TextView xiangqing;
	BaiduMap map;
	private String driverId;// 镖师的Id
	private String userType;// 派送人的Id = "3"
	Double latitude;// 派送人的纬度；
	Double longitude; // 派送人的经度；
	private EscortInfoBean bean;
	private String status;// 镖件当前的的状态
	private String recId;// 当前镖件的主键
	private String fromLatitude;
	private String fromLongitude;
	private String toLongitude;
	private String toLatitude;
	private RoutePlanSearch routePlanSearch;// 路径规划搜索接口
	private int drivintResultIndex = 0;// 驾车路线方案index
	public LocationMode mCurrrentMode = LocationMode.FOLLOWING;

	private View vPopupWindow_complain;
	private PopupWindow popupWindow_complain;
	private EditText	edit_comp;
	private ImageView	btn_comp_image;
	private Button	btn_comp_sumit;
	private TextView exit;
	private Bitmap head;
	private String fileName = path + "dart.png";
	private static String path = "/sdcard/myHead/";// sd路径
	private Map map1 = new HashMap<String, String>();
	private Map map_file = new HashMap<String, File>();
	private Bitmap head1;
	private String fileName1 = path + "compse.png";
	private String result;
	private String result1;
	private String icon="";
	private String icon1="";
	private boolean flag;
	private boolean frist = false;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_down_track);
		ButterKnife.bind(DownDetialTrackActivity.this);
		initData();// 嗯
		getData();
		initView();
	}

	@Override
	public void onWeightClick(View v) {
		// TODO Auto-generated method stub

	}

	@Override
	public void initView() {
		// TODO Auto-generated method stub
		btnLeft.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				finish();
			}
		});
		btnRight.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				showPopup_complain();
			}
		});
	}

	@OnClick({ R.id.img_header, R.id.btn_evaluate, R.id.img_phone, R.id.img_message, R.id.rab_couriersscore 
		,R.id.xiangqing})
	public void MyonClick(View view) {
		switch (view.getId()) {
		case R.id.img_header:
			startActivity(new Intent(DownDetialTrackActivity.this, EscortMessageActivity.class).putExtra("driverId",
					driverId));
			break;
		case R.id.btn_submit:
			startActivity(new Intent(DownDetialTrackActivity.this, EscoreDartEvaluteActivity.class));
			break;
		case R.id.img_phone:
			AppUtils.intentDial(DownDetialTrackActivity.this, bean.data.get(0).driverMobile);
			break;
		case R.id.img_message:
			ToastUtil.shortToast(DownDetialTrackActivity.this, "即时通信");
			break;
		case R.id.btn_evaluate:
			startActivity(new Intent(DownDetialTrackActivity.this, EscoreDartEvaluteActivity.class)
					.putExtra("driverId", driverId).putExtra("recId", getIntent().getIntExtra("recId", 0))
					.putExtra("userHeadPath", bean.data.get(0).userHeadPath));
			finish();
			break;
		case R.id.rab_couriersscore:

			break;
		case R.id.xiangqing:
			Intent intent = new Intent(this, DownOwnerDetialsActivity.class);
			intent.putExtra("matImageUrl", getIntent().getStringExtra("matImageUrl"));
			intent.putExtra("matName", getIntent().getStringExtra("matName"));
			intent.putExtra("matWeight", getIntent().getStringExtra("matWeight"));
			intent.putExtra("length", getIntent().getStringExtra("length"));
			intent.putExtra("publishTime", getIntent().getStringExtra("publishTime"));
			intent.putExtra("personName", getIntent().getStringExtra("personName"));
			intent.putExtra("mobile", getIntent().getStringExtra("mobile"));
			intent.putExtra("address", getIntent().getStringExtra("address"));
			intent.putExtra("personNameTo", getIntent().getStringExtra("personNameTo"));
			intent.putExtra("mobileTo", getIntent().getStringExtra("mobileTo"));
			intent.putExtra("addressTo", getIntent().getStringExtra("addressTo"));
			intent.putExtra("matRemark", getIntent().getStringExtra("matRemark"));
			intent.putExtra("money", getIntent().getStringExtra("money"));
			intent.putExtra("status", getIntent().getStringExtra("status"));
			intent.putExtra("recId", getIntent().getIntExtra("recId", 0));
			intent.putExtra("limitTime", getIntent().getStringExtra("limitTime"));
			intent.putExtra("finishTime", getIntent().getStringExtra("finishTime"));
			intent.putExtra("replaceMoney", getIntent().getStringExtra("replaceMoney"));
			intent.putExtra("ifReplaceMoney", getIntent().getStringExtra("ifReplaceMoney"));
			intent.putExtra("ifTackReplace", getIntent().getStringExtra("ifTackReplace"));
			intent.putExtra("carType", getIntent().getStringExtra("carType"));
			intent.putExtra("insureCost", getIntent().getStringExtra("insureCost"));
			intent.putExtra("premium", getIntent().getStringExtra("premium"));
			intent.putExtra("carLength", getIntent().getStringExtra("carLength"));
			intent.putExtra("matVolume", getIntent().getStringExtra("matVolume"));
			intent.putExtra("useTime", getIntent().getStringExtra("useTime"));
			intent.putExtra("cargoSize",  getIntent().getStringExtra("cargoSize"));
			intent.putExtra("townCode",   getIntent().getStringExtra("townCode"));
			intent.putExtra("cityCode",   getIntent().getStringExtra("cityCode"));
			intent.putExtra("fromLatitude",   getIntent().getStringExtra("fromLatitude"));
			intent.putExtra("fromLongitude",   getIntent().getStringExtra("fromLongitude"));
			intent.putExtra("cityCodeTo",   getIntent().getStringExtra("cityCodeTo"));
			
			intent.putExtra("toLatitude",   getIntent().getStringExtra("toLatitude"));
			intent.putExtra("toLongitude",   getIntent().getStringExtra("toLongitude"));
			intent.putExtra("cityCode",   getIntent().getStringExtra("cityCode"));
			intent.putExtra("whether",  getIntent().getStringExtra("whether"));
			intent.putExtra("category", getIntent().getStringExtra("category"));
			startActivity(intent);
			finish();
			break;
		default:
			break;
		}
	}

	@Override
	public void initData() {
		map = mapView.getMap();
		map.setMyLocationEnabled(true);
		map.setMapType(BaiduMap.MAP_TYPE_NORMAL);
		map.setMyLocationConfigeration(new MyLocationConfiguration(mCurrrentMode, true, null));
		// 隐藏百度的图标
		View childAt = mapView.getChildAt(1);
		if (childAt != null && (childAt instanceof ImageView || childAt instanceof ZoomControls)) {
			childAt.setVisibility(View.INVISIBLE);
		}
		// 隐藏比例尺
		mapView.showScaleControl(false);
		// 隐藏缩放控件
		mapView.showZoomControls(false);
		// 接收到从上一个界面发来的镖师Id；
		// 上一个界面SendOwnerFragment；
		Intent intent = getIntent();
		driverId = intent.getStringExtra("driverId");
		userType = intent.getStringExtra("userType");
		// 状态 0-预发布成功(未支付) 1-支付成功(已支付) 2-(已抢单) 3-已取件 4-订单取消 5-成功 6-删除
		// 7-已评价
		status = intent.getStringExtra("status");
		recId = intent.getStringExtra("recId");
		fromLatitude = intent.getStringExtra("fromLatitude");
		fromLongitude = intent.getStringExtra("fromLongitude");
		toLongitude = intent.getStringExtra("toLongitude");
		toLatitude = intent.getStringExtra("toLatitude");
		Log.e("*********************--", "-----" + fromLatitude);
		Log.e("*******recId**************--", "-----" + recId);
//		tbv_show.setTitleText("货物跟踪");
		// // 如果镖件押镖已成功，则设置“评价服务”可点击；
//		Toast.makeText(DownDetialTrackActivity.this, "评价     "+status, Toast.LENGTH_SHORT).show(); 
		if ( status.equals("5")) {
			btn_evaluate.setBackgroundColor(getResources().getColor(R.color.orange1));
			btn_evaluate.setClickable(true);
		} else {
			btn_evaluate.setBackgroundColor(getResources().getColor(R.color.grayview));
			btn_evaluate.setClickable(false);
		}
		if (status.equals("3") || status.equals("4")) {
			if (getIntent().getStringExtra("ifAgree").equals("1")) {
				btnRight.setVisibility(View.GONE);
			}else {
				btnRight.setVisibility(View.VISIBLE);
			}
		}
//		else if (status.equals("9")) {
//			btnRight.setVisibility(View.GONE);
//		}
		routePlanSearch = RoutePlanSearch.newInstance();
		routePlanSearch.setOnGetRoutePlanResultListener(routePlanResultListener);
		drivingSearch();// 驾车线路查询
//		getLatestLocationOfDeliveryman();// 获取派送者的经纬度信息
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
	 * 路线规划结果回调
	 */
	OnGetRoutePlanResultListener routePlanResultListener = new OnGetRoutePlanResultListener() {

		/**
		 * 步行路线结果回调
		 */
		@Override
		public void onGetWalkingRouteResult(WalkingRouteResult walkingRouteResult) {
			if (walkingRouteResult == null || walkingRouteResult.error != SearchResult.ERRORNO.NO_ERROR) {
				Toast.makeText(DownDetialTrackActivity.this, "抱歉，未找到适合的路线", Toast.LENGTH_SHORT).show();
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
			if (transitRouteResult == null || transitRouteResult.error != SearchResult.ERRORNO.NO_ERROR) {
				Toast.makeText(DownDetialTrackActivity.this, "抱歉，未找到适合的路线", Toast.LENGTH_SHORT).show();
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
			if (drivingRouteResult == null || drivingRouteResult.error != SearchResult.ERRORNO.NO_ERROR) {
				Toast.makeText(DownDetialTrackActivity.this, "抱歉，未找到适合的路线", Toast.LENGTH_SHORT).show();
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

	@Override
	public void setOnClick() {
		// TODO Auto-generated method stub

	}

	/**
	 * 表77 获取镖师信息 /driver/driverDetail
	 */
	@Override
	public void getData() {
		Log.e("-----------------", "-----" + UrlMap.getUrl(MCUrl.DRIVERDETAIL, "driverId", driverId));
		// GET方法获取数据(镖师的基本信息)
		AsyncHttpUtils.doSimGet(UrlMap.getUrl(MCUrl.DRIVERDETAIL, "driverId", driverId),
				new AsyncHttpResponseHandler() {
					@Override
					public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
						// TODO Auto-generated method stub
						Log.e("ccc", new String(arg2));
						bean = new Gson().fromJson(new String(arg2), EscortInfoBean.class);
						if (bean.getErrCode() == 0) {// 如果获取成功
							if (!bean.data.equals("") && bean.data.size() > 0) {
								setData();
							}

						}
						if (bean.getErrCode() == -1) {
							ToastUtil.shortToast(getApplicationContext(), bean.getMessage());
						}
					}

					@Override
					public void onFailure(int arg0, Header[] arg1, byte[] arg2, Throwable arg3) {

					}
				});
	}

	public void setData() {
		tv_driverName.setText(bean.getData().get(0).userName);// 设置镖师的名字

		if ("男".equals(bean.getData().get(0).sex)) {

			img_sex.setImageResource(R.drawable.escort_man);// 设置性别图片【男】

		} else {
			img_sex.setImageResource(R.drawable.escort_woment);// 设置性别图片【女】
		}

		tv_num.setText(bean.getData().get(0).driverRouteCount);// 设置接单次数

		rab_couriersscore.setRating(bean.getData().get(0).synthesisEvaluate);// 设置评分
		if (!bean.getData().get(0).userHeadPath.equals("")) {
			loader.displayImage(bean.getData().get(0).userHeadPath, img_header, options);
		} else {
			img_header.setBackgroundResource(R.drawable.wu_cou);
		}

	}

	@Override
	protected void onResume() {
		super.onResume();
		mapView.onResume();
		}

	@Override
	protected void onPause() {
		super.onPause();
		mapView.onPause();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		routePlanSearch.destroy();// 释放检索实例
		mapView.onDestroy();
//		map.clear();
	}

	/**
	 * 获取派送者的经纬度信息；
	 */
	private void getLatestLocationOfDeliveryman() {
		Log.e("失败原因4555", "" + UrlMap.getUrl(MCUrl.DRIVERROUTEPATH, "driverId", driverId));
		AsyncHttpUtils.doGet(UrlMap.getUrl(MCUrl.DRIVERROUTEPATH, "driverId", driverId), null, null, null,
				new AsyncHttpResponseHandler() {
					private Object OverlayOptionsooPolyline;
					@Override
					public void onFailure(int arg0, Header[] arg1, byte[] arg2, Throwable arg3) {
						Log.e("失败原因", "" + new String(arg2) + "---------------");
					}
					@Override
					public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
						// TODO Auto-generated method stub
						Log.e("json______getLatestLocationOfDeliveryman", "" + new String(arg2) + "---------------");
						CurrentLocationBean bean = new Gson().fromJson(new String(arg2), CurrentLocationBean.class);
						if (bean.data.size() != 0 && bean.data != null) {
							userType = bean.data.get(0).userType;						
//							LatLng point = new LatLng(bean.data.get(0).latitude, bean.data.get(0).longitude);
//							Log.e("longitude3", point + "-------------");
//							// 构建Marker图标
//							BitmapDescriptor bitmap1 = null;
//							if ("3".equals(userType) || "2".equals(userType)) {
//								bitmap1 = BitmapDescriptorFactory.fromResource(R.drawable.driver_icon);
//							}
//							// 构建MarkerOption，用于在地图上添加Marker
//							OverlayOptions option1 = new MarkerOptions().position(point).icon(bitmap1);
//							// 在地图上添加Marker，并显示
//							map.addOverlay(option1);
							List<LatLng> points = new ArrayList<LatLng>();
							for (int i = 0; i < bean.data.size(); i++) {
								LatLng point = new LatLng(bean.data.get(i).latitude, bean.data.get(i).longitude);
								points.add(point);
							}
							List<LatLng> mList = new ArrayList<LatLng>();
							for (int i = 0; i < points.size() - 1; i++) {
								double distance = DistanceUtil.getDistance(points.get(i), points.get(i + 1));
								Log.e("distan", distance + "");
								if (distance<2 && distance > 1000) {
									mList.add(points.get(i));
								}
							}
							Log.e("mlistmsg", mList.size()+"++++++++++++++++++++");
							// 构造对象
							OverlayOptions ooPolyline = new PolylineOptions().width(8).color(0xAAFF0000).points(mList).visible(false);
							// 添加到地图
							map.addOverlay(ooPolyline);
							//设置线的宽度
//							 polylineOptions.width(10);
//							 //设置线的颜色
//							 polylineOptions.color(Color.RED);
//							 //设置线是否可见
//							 polylineOptions.visible(true);

						}
					}

				});
	}

	public class Myoverlinde extends Overlay {
		public Myoverlinde(Drawable drawable) {
			super();

		}
	}
	

	private void showPopup_complain(){
	vPopupWindow_complain = LayoutInflater.from(this).inflate(R.layout.popupwindow_complain, null);
//	ll_report = (LinearLayout) vPopupWindow_report.findViewById(R.id.ll_setChecked);
	btn_comp_image = (ImageView) vPopupWindow_complain.findViewById(R.id.btn_comp_image);
	edit_comp = (EditText) vPopupWindow_complain.findViewById(R.id.edit_comp);
	btn_comp_sumit = (Button) vPopupWindow_complain.findViewById(R.id.btn_comp_sumit);
	exit = (TextView) vPopupWindow_complain.findViewById(R.id.exit);
	popupWindow_complain = new PopupWindow(vPopupWindow_complain, LayoutParams.MATCH_PARENT,
			LayoutParams.MATCH_PARENT, true);
	popupWindow_complain.setAnimationStyle(R.style.mypopwindow_anim_style);
	popupWindow_complain.showAtLocation(DownDetialTrackActivity.this.findViewById(R.id.btn_evaluate), Gravity.CENTER, 0, 0);
	// 设置背景 使返回键生效（PopupWindow会拦截触摸事件，包括返回键和菜单键）
	popupWindow_complain.setBackgroundDrawable(new ColorDrawable(0x99999999));
	//上传照片
	btn_comp_image.setOnClickListener(new View.OnClickListener() {
		
		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			showPopwindowcomp();
		}
	});
	//提交
	btn_comp_sumit.setOnClickListener(new View.OnClickListener() {
		
		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			getmegsetousu();
//			popupWindow_complain.dismiss();
		}
	});
	exit.setOnClickListener(new View.OnClickListener() {
		
		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			popupWindow_complain.dismiss();
		}
	});
	popupWindow_complain.setOnDismissListener(new OnDismissListener() {

		@Override
		public void onDismiss() {
			popupWindow_complain.dismiss();
		}
	});
}
	/**
	 * t投诉
	 */
	private void getmegsetousu(){
		JSONObject obj = new JSONObject();
		if (edit_comp.getText().toString().equals("")) {
			ToastUtil.shortToast(getApplicationContext(), "请输入投诉内容");
			return;
		}
//		if (!frist) {
//			ToastUtil.shortToast(getApplicationContext(), "上传照片");
//			return;
//		}
		try {
			obj.put("userId",
					String.valueOf(PreferencesUtils.getInt(getApplicationContext(), PreferenceConstants.UID)));
			obj.put("toUserId", getIntent().getStringExtra("driverId"));
			obj.put("type", "1");
			obj.put("details",edit_comp.getText().toString());
			obj.put("toRecId",recId);
			obj.put("imageUrl", icon1);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		dialog.show();
		Log.e("查看数据", obj.toString());
		//PublishNew    LOGISTICS
		AsyncHttpUtils.doPostJson(DownDetialTrackActivity.this, MCUrl.Accusations, obj.toString(),
				new AsyncHttpResponseHandler() {
			@Override
			public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
				dialog.dismiss();
				popupWindow_complain.dismiss();
				BaseBean baseBean=new Gson().fromJson(new String(arg2), BaseBean.class);
				if (baseBean.getErrCode()==0) {
				}else {
					ToastUtil.shortToast(DownDetialTrackActivity.this, baseBean.getMessage());
				}
			}
			@Override
			public void onFailure(int arg0, Header[] arg1, byte[] arg2, Throwable arg3) {
				dialog.dismiss();
			}
		});
	}
	/**
	 * 显示投诉popupWindow
	 */
	private void showPopwindowcomp(){
		// 利用layoutInflater获得View
		LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View view = inflater.inflate(R.layout.pop_window_lauoutitem, null);
		// 下面是两种方法得到宽度和高度 getWindow().getDecorView().getWidth()
		final PopupWindow window = new PopupWindow(view, WindowManager.LayoutParams.MATCH_PARENT,
				WindowManager.LayoutParams.WRAP_CONTENT);
		// 设置popWindow弹出窗体可点击，这句话必须添加，并且是true
		window.setFocusable(true);
		// 实例化一个ColorDrawable颜色为半透明
		ColorDrawable dw = new ColorDrawable(R.color.transparent);
		window.setBackgroundDrawable(dw); 
		window.setOutsideTouchable(false);// 这是点击外部不消失
		// 设置popWindow的显示和消失动画
		window.setAnimationStyle(R.style.mypopwindow_anim_style);
		// 在底部显示
		window.showAtLocation(DownDetialTrackActivity.this.findViewById(R.id.btn_evaluate), Gravity.BOTTOM, 0, 0);

		// 这里检验popWindow里的button是否可以点击
		TextView tv_camera = (TextView) view.findViewById(R.id.tv_camera);
		TextView tv_show = (TextView) view.findViewById(R.id.tv_show);
		tv_show.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				startActivity(new Intent(DownDetialTrackActivity.this, IdCardActivity.class));
				window.dismiss();
			}
		});
		tv_camera.setClickable(true);
		// 点击是拍照
		tv_camera.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				window.dismiss();
				Intent intent2 = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
				intent2.putExtra(MediaStore.EXTRA_OUTPUT,
						Uri.fromFile(new File(Environment.getExternalStorageDirectory(), "compse.png")));
				// intent2.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
				startActivityForResult(intent2, 22);// 采用ForResult打开
			}
		});
		// 从相册弄
		TextView tv_photofromalbum = (TextView) view.findViewById(R.id.tv_photofromalbum);
		tv_photofromalbum.setClickable(true);
		tv_photofromalbum.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				window.dismiss();
				Intent intent1 = new Intent(Intent.ACTION_PICK, null);
				intent1.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
				// intent1.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
				startActivityForResult(intent1, 11);
			}
		});
		TextView tv_cancel = (TextView) view.findViewById(R.id.tv_cancel);
		tv_cancel.setClickable(true);
		tv_cancel.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				window.dismiss();
			}
		});
		// popWindow消失监听方法
		window.setOnDismissListener(new OnDismissListener() {

			@Override
			public void onDismiss() {
				

			}
		});

	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
		case 11:
			if (resultCode == RESULT_OK) {
				cropPhototwo(data.getData());// 裁剪图片
			}
			break;
		// 如果是调用相机拍照时
		case 22:
			if (resultCode == RESULT_OK) {
				File temp = new File(Environment.getExternalStorageDirectory() + "/compse.png");
				cropPhototwo(Uri.fromFile(temp));// 裁剪图片
			}
			break;

		// 取得裁剪后的图片
		case 33:
//			if (resultCode == RESULT_OK) {
			Log.e("1111tututuutut  ", ""+data);
////			}
			if (data != null) {
				Bundle extras = data.getExtras();
				head1 = extras.getParcelable("data");
				Log.e("img.1111111"  ,""+head1);
				if (head1 != null) {
					/**
					 * 上传服务器代码 //TODO 实现头衔上传
					 */
					Logger.e("img.1111111");
					setPicToViewtwo(head1);
					map1.put("userId",
							String.valueOf(PreferencesUtils.getInt(getApplicationContext(), PreferenceConstants.UID)));
					map1.put("fileName1", "idcard");
					map_file.put("file", new File(fileName1));
					// String result="";
					sendEmptyBackgroundMessage(MsgConstants.MSG_02);
					btn_comp_image.setBackgroundDrawable(null);
					btn_comp_image.setImageBitmap(head);
				}
			}
			break;
		default:
			break;
		}
		super.onActivityResult(requestCode, resultCode, data);
	};
	/***
	 * 裁剪图片方法实现
	 * 
	 * @param uri
	 */
	public void cropPhoto(Uri uri) {
		Intent intent = new Intent("com.android.camera.action.CROP");
		intent.setDataAndType(uri, "image/*");
		intent.putExtra("crop", "true");
		// aspectX aspectY 是宽高的比例
		intent.putExtra("aspectX", 1.5);
		intent.putExtra("aspectY", 1);
		// outputX outputY 是裁剪图片宽高
		intent.putExtra("outputX", 350);
		intent.putExtra("outputY", 350);
		intent.putExtra("return-data", true);
		startActivityForResult(intent, 3);
	}
	/***
	 * 裁剪图片方法实现
	 * 
	 * @param uri
	 */
	public void cropPhototwo(Uri uri) {
		Intent intent = new Intent("com.android.camera.action.CROP");
		intent.setDataAndType(uri, "image/*");
		intent.putExtra("crop", "true");
		// aspectX aspectY 是宽高的比例
		intent.putExtra("aspectX", 1.5);
		intent.putExtra("aspectY", 1);
		// outputX outputY 是裁剪图片宽高
		intent.putExtra("outputX", 350);
		intent.putExtra("outputY", 350);
		intent.putExtra("return-data", true);
		startActivityForResult(intent, 33);
	}
	
	@Override
	public void handleBackgroundMessage(Message msg) {
		super.handleBackgroundMessage(msg);
		switch (msg.what) {
		case MsgConstants.MSG_02:
			try {
				result1 = post(MCUrl.Accusation, map1, map_file);
				sendEmptyUiMessage(MsgConstants.MSG_02);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			break;
		default:
			break;
		}
	}
	@Override
	public void handleUiMessage(Message msg) {
		// TODO Auto-generated method stub
		super.handleUiMessage(msg);
		switch (msg.what) {
		case MsgConstants.MSG_02:
			Log.e("result", result1);
			IconBean bean1 = new Gson().fromJson(result1, IconBean.class);
			if (bean1.getErrCode()==0) {
			if (bean1.data.size() != 0) {
				frist=true;
				Log.e("filePath", bean1.data.get(0).filePath);
				ToastUtil.shortToast(DownDetialTrackActivity.this, "上传成功");
				icon1 = bean1.data.get(0).filePath;
//				PreferencesUtils.putString(getApplicationContext(), PreferenceConstants.ICON_PATH, icon);
//				loader.displayImage(icon, com_img, options);
				btn_comp_image.setBackgroundDrawable(null);
				btn_comp_image.setImageBitmap(head1);
			}
			} else if (bean1.getErrCode()==-2) {
				
				ToastUtil.shortToast(DownDetialTrackActivity.this, "请勿重复上传");
			}else if (bean1.getErrCode()==-3) {
				ToastUtil.shortToast(DownDetialTrackActivity.this, "请勿重复上传");
			}
			break;
		default:
			break;
		}
	}
	
	/**
	 * 保存裁剪之后的图片数据
	 * 
	 * @param head2
	 */

	private void setPicToView(Bitmap mBitmap) {
		String sdStatus = Environment.getExternalStorageState();
		if (!sdStatus.equals(Environment.MEDIA_MOUNTED)) { // 检测sd是否可用
			return;
		}
		FileOutputStream b = null;
		File file = new File(path);
		file.mkdirs();// 创建文件夹
		try {
			b = new FileOutputStream(fileName);
			mBitmap.compress(Bitmap.CompressFormat.PNG, 100, b);// 把数据写入文件
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} finally {
			try {
				// 关闭流
				b.flush();
				b.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	/**
	 * 保存裁剪之后的图片数据
	 * 
	 * @param head2
	 */

	private void setPicToViewtwo(Bitmap mBitmap) {
		String sdStatus = Environment.getExternalStorageState();
		if (!sdStatus.equals(Environment.MEDIA_MOUNTED)) { // 检测sd是否可用
			return;
		}
		FileOutputStream b = null;
		File file = new File(path);
		file.mkdirs();// 创建文件夹
		fileName = path + "compse.png";
		try {
			b = new FileOutputStream(fileName);
			mBitmap.compress(Bitmap.CompressFormat.PNG, 100, b);// 把数据写入文件
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} finally {
			try {
				// 关闭流
				b.flush();
				b.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 通过拼接的方式构造请求内容，实现参数传输以及文件传输
	 * 
	 * @param actionUrl
	 *            访问的服务器URL
	 * @param params
	 *            普通参数
	 * @param files
	 *            文件参数
	 * @return
	 * @return
	 * @throws IOException
	 */
	public static String post(String actionUrl, Map<String, String> params, Map<String, File> files)
			throws IOException {

		StringBuilder sb2 = new StringBuilder();

		String BOUNDARY = java.util.UUID.randomUUID().toString();
		String PREFIX = "--", LINEND = "\r\n";
		String MULTIPART_FROM_DATA = "multipart/form-data";
		String CHARSET = "UTF-8";

		URL uri = new URL(actionUrl);
		HttpURLConnection conn = (HttpURLConnection) uri.openConnection();
		conn.setReadTimeout(5 * 1000); // 缓存的最长时间
		conn.setDoInput(true);// 允许输入
		conn.setDoOutput(true);// 允许输出
		conn.setUseCaches(false); // 不允许使用缓存
		conn.setRequestMethod("POST");
		conn.setRequestProperty("connection", "keep-alive");
		conn.setRequestProperty("Charsert", "UTF-8");
		conn.setRequestProperty("Content-Type", MULTIPART_FROM_DATA + ";boundary=" + BOUNDARY);

		// 首先组拼文本类型的参数
		StringBuilder sb = new StringBuilder();
		for (Map.Entry<String, String> entry : params.entrySet()) {
			sb.append(PREFIX);
			sb.append(BOUNDARY);
			sb.append(LINEND);
			sb.append("Content-Disposition: form-data; name=\"" + entry.getKey() + "\"" + LINEND);
			sb.append("Content-Type: text/plain; charset=" + CHARSET + LINEND);
			sb.append("Content-Transfer-Encoding: 8bit" + LINEND);
			sb.append(LINEND);
			sb.append(entry.getValue());
			sb.append(LINEND);
		}

		DataOutputStream outStream = new DataOutputStream(conn.getOutputStream());
		outStream.write(sb.toString().getBytes());
		InputStream in = null;
		// 发送文件数据
		if (files != null) {
			for (Map.Entry<String, File> file : files.entrySet()) {
				StringBuilder sb1 = new StringBuilder();
				sb1.append(PREFIX);
				sb1.append(BOUNDARY);
				sb1.append(LINEND);
				// name是post中传参的键 filename是文件的名称
				sb1.append(
						"Content-Disposition: form-data; name=\"file\"; filename=\"" + file.getKey() + "\"" + LINEND);
				sb1.append("Content-Type: application/octet-stream; charset=" + CHARSET + LINEND);
				sb1.append(LINEND);
				outStream.write(sb1.toString().getBytes());
				InputStream is = new FileInputStream(file.getValue());
				byte[] buffer = new byte[1024];
				int len = 0;
				while ((len = is.read(buffer)) != -1) {
					outStream.write(buffer, 0, len);
				}
				is.close();
				outStream.write(LINEND.getBytes());
			}

			// 请求结束标志
			byte[] end_data = (PREFIX + BOUNDARY + PREFIX + LINEND).getBytes();
			outStream.write(end_data);
			outStream.flush();
			// 得到响应码
			int res = conn.getResponseCode();
			if (res == 200) {
				in = conn.getInputStream();
				int ch;

				while ((ch = in.read()) != -1) {
					sb2.append((char) ch);
				}
			}
			outStream.close();
			conn.disconnect();
		}
		return sb2.toString();
	}

	public InputStream getStream(File file) {
		// 第2步、通过子类实例化父类对象
		// File f= new File("d:" + File.separator + "test.txt") ; // 声明File对象
		// 第2步、通过子类实例化父类对象
		InputStream input = null; // 准备好一个输入的对象
		try {
			input = new FileInputStream(file); // 通过对象多态性，进行实例化
			// 第3步、进行读操作
			// byte b[] = new byte[input..available()] ; 跟使用下面的代码是一样的
			byte b[] = new byte[(int) file.length()]; // 数组大小由文件决定
			int len = input.read(b); // 读取内容
			// 第4步、关闭输出流

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				input.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return input;
	}

	private boolean fileIsExist(String path) {
		File file = new File(path);
		if (file.exists()) {
			return true;
		}
		return false;
	}

	private void fileDelete(String path) {

		if (fileIsExist(path)) {
			File file = new File(path);
			file.delete();
		}
	}
	public Bitmap getRoundedCornerBitmap(Bitmap bitmap, float roundPx) {

		Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Config.ARGB_8888);
		Canvas canvas = new Canvas(output);

		final int color = 0xff424242;
		final Paint paint = new Paint();
		final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
		final RectF rectF = new RectF(rect);

		paint.setAntiAlias(true);
		canvas.drawARGB(0, 0, 0, 0);
		paint.setColor(color);
		canvas.drawRoundRect(rectF, roundPx, roundPx, paint);

		paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
		canvas.drawBitmap(bitmap, rect, rect, paint);

		return output;
	}

}
