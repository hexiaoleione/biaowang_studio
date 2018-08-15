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
import java.util.HashMap;
import java.util.Map;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.google.gson.Gson;
import com.hex.express.iwant.R;
import com.hex.express.iwant.iWantApplication;
import com.hex.express.iwant.bean.IconBean;
import com.hex.express.iwant.bean.LogisBean;
import com.hex.express.iwant.constance.MCUrl;
import com.hex.express.iwant.constance.MsgConstants;
import com.hex.express.iwant.constance.PreferenceConstants;
import com.hex.express.iwant.http.AsyncHttpUtils;
import com.hex.express.iwant.newactivity.LogisewindActivity;
import com.hex.express.iwant.newsmain.NewMainActivity;
import com.hex.express.iwant.utils.IDUtils;
import com.hex.express.iwant.utils.Logger;
import com.hex.express.iwant.utils.PreferencesUtils;
import com.hex.express.iwant.utils.StringUtil;
import com.hex.express.iwant.utils.ToastUtil;
import com.loopj.android.http.AsyncHttpResponseHandler;

import android.R.string;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Bitmap.Config;
import android.graphics.PorterDuff.Mode;
import android.graphics.drawable.ColorDrawable;
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
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.PopupWindow.OnDismissListener;
import butterknife.Bind;
import butterknife.ButterKnife;

public class CommercialActivity extends BaseActivity{

	
	
	@Bind(R.id.et_addses)
	TextView et_addses;
	
	@Bind(R.id.et_addse)
	EditText et_addse;
	@Bind(R.id.et_homename)
	EditText et_homename;
	@Bind(R.id.et_homephone)
	EditText et_homephone;
	@Bind(R.id.et_tohomephone)
	EditText et_tohomephone;
	@Bind(R.id.delver_img1)//第一张图
	ImageView delver_img1;
	@Bind(R.id.delver_img2)//第二张图
	ImageView delver_img2;
	@Bind(R.id.delver_img3)//第三张图
	ImageView delver_img3;
	@Bind(R.id.sumit)
	Button sumit;
	
	private Bitmap head;
	private String fileName,fileNamet,fileNames;
	private static String path = "/sdcard/myHeads/";// sd路径
	private Map map = new HashMap<String, String>();
	private Map map_file = new HashMap<String, File>();
	private String result;
	private String result2;
	private String result3;
	private String icon = "";
	private String icon2 = "";
	private String icon3 = "";
	private boolean flag1,flag2,flag3;
	
	private String code;
	// 当前位置经纬度
		private double latitude;
		private double longitude;
		private LocationClient client;
		//发件城市
		private String city = "";
		
	String IDENTIFY_CODE;
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_commercial);
		iWantApplication.getInstance().addActivity(this);
		ButterKnife.bind(this);
		initData();
		initView();
		setOnClick();
		inding();
		
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
		
	}

	@Override
	public void setOnClick() {
		// TODO Auto-generated method stub
		et_addses.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				startActivityForResult(new Intent(CommercialActivity. this,ProvCityActivity.class),101);
				
			}
		});
		delver_img1.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
						showPopwindow();
			}
		});
	 delver_img2.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
					showPopwindowtow();
				
			}
		});
	  delver_img3.setOnClickListener(new OnClickListener() {
		
		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			     showPopwindowsend();
		}
	});
	  sumit.setOnClickListener(new OnClickListener() {
		
		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			addPostResult();
		}
	});
	}
	private void inding(){
		client = new LocationClient(CommercialActivity.this);
		setLocationParams();
		client.registerLocationListener(new BDLocationListener() {
			@Override
			public void onReceiveLocation(BDLocation arg0) {
				if (arg0 == null) {
					ToastUtil.shortToast(CommercialActivity.this, "定位失败，请检查定位设置");
					return;
				} else {
					city = arg0.getCity();
					latitude = arg0.getLatitude();
					longitude = arg0.getLongitude();
					Log.e("jpppp", latitude + ":::::::::" + longitude);
//					address = arg0.getAddrStr();
//					townaddress2=arg0.getDistrict();
//					}
				}
			}
		});
		// 初始化定位
		// 打开GPS
		client.start();
	}
	/**
	 * 百度地图定位设置
	 */
	private void setLocationParams() {
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
	@Override
	public void getData() {
		// TODO Auto-generated method stub
		
	}
	private void addPostResult() {
		JSONObject obj = new JSONObject();
//		if(!"".equals(icon) || !"".equals(icon2) || !"".equals(icon3)){
			if(!flag1){
				ToastUtil.shortToast(getApplicationContext(), "请上传照片");
				return;
			}			
		if(!flag2){
			ToastUtil.shortToast(getApplicationContext(), "请上传照片");
			return;
		}
		if(!flag3){
			ToastUtil.shortToast(getApplicationContext(), "请上传照片");
			return;
		}
			if ("".equals(et_addses.getText().toString()) || "".equals(et_addse.getText().toString())) {
				ToastUtil.shortToast(CommercialActivity.this, "请查看地址是否填写");
				return;
			}
			if (et_homename.getText().toString().equals("") ) {
				ToastUtil.shortToast(CommercialActivity.this, "请查看店名是否填写");
				return;
			}
//			String string =et_tohomephone.getText().toString();
//			  String tmpstr=string.replace(" ","");
//			if (!StringUtil.isMobileNO(tmpstr)
//					|| (tmpstr.length() != 11)
//							) {
//				ToastUtil.shortToast(CommercialActivity.this, "请输入正确的手机号码");
//				return;
//			}
			String  idcard=et_homephone.getText().toString();
			if (IDUtils.IDCardValidate(idcard.toUpperCase()).equals("false")) {
				ToastUtil.shortToast(CommercialActivity.this, "请输入正确的身份证号");
				return;
			}
			if ("".equals(et_homephone.getText().toString())) {
				ToastUtil.shortToast(CommercialActivity.this, "请输入身份证号");
				return;
			}
		try {
			obj.put("userId",
					String.valueOf(PreferencesUtils.getInt(getApplicationContext(), PreferenceConstants.UID)));
			
			obj.put("latitude", latitude);
			obj.put("longitude", longitude);
			obj.put("site", et_addses.getText().toString());//三级地址
			obj.put("address", et_addse.getText().toString());//详细地址
			obj.put("shopName", et_homename.getText().toString()); //店名
			obj.put("idCard", et_homephone.getText().toString()); //身份证号		
			obj.put("inviteMobile", et_tohomephone.getText().toString()); //推荐人手机号
			obj.put("cityCode", code); //城市代号
			obj.put("shopDoorURL", icon);//图片1recId
			obj.put("shopLicenseURL", icon2);//图片2
			obj.put("shopIdCardURL", icon3);//图片3
//			obj.put("carNumImg", mInput.getText().toString());
//			obj.put("remark", spec_edt_yuan.getText().toString().trim());//报价元
		} catch (JSONException e) {
			e.printStackTrace();
		}
		dialog.show();
		Log.e("查看数据", obj.toString());//LOGISCORECIVE  LOGISTICS
		AsyncHttpUtils.doPostJson(CommercialActivity.this, MCUrl.addChapman, obj.toString(),
				new AsyncHttpResponseHandler() {

					@Override
					public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
						Log.e("11111111111 wwww   ", new String(arg2));
						dialog.dismiss();
						LogisBean bean = new Gson().fromJson(new String(arg2), LogisBean.class);
//						Log.e("oppop", bean.data.toString());
//						ToastUtil.shortToast(getApplicationContext(), bean.getMessage());
						if (bean.getErrCode()==0) {
							Intent intent = new Intent(CommercialActivity.this, NewMainActivity.class);
							intent.putExtra("type", "1");
							startActivity(intent);
							ToastUtil.shortToast(getApplicationContext(), bean.getMessage());
						     finish();
						}else {
//							Intent intent = new Intent();
//							setResult(RESULT_OK, intent);
//								finish();
							ToastUtil.shortToast(getApplicationContext(), bean.getMessage());
						}
						
					}

					@Override
					public void onFailure(int arg0, Header[] arg1, byte[] arg2, Throwable arg3) {
						dialog.dismiss();
					}
				});
		
//		}
//		else {
//			ToastUtil.shortToast(getApplicationContext(), "请上传照片");
//		}
	}
	
	/**
	 * 显示popupWindow
	 */
	private void showPopwindow() {
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
		window.showAtLocation(CommercialActivity.this.findViewById(R.id.delver_img1), Gravity.BOTTOM,
				0, 0);

		// 这里检验popWindow里的button是否可以点击
		TextView tv_camera = (TextView) view.findViewById(R.id.tv_camera);
		tv_camera.setClickable(true);
		TextView tv_show = (TextView) view.findViewById(R.id.tv_show);
		tv_show.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				startActivity(new Intent(CommercialActivity.this, IdCardActivity.class)
						.putExtra("iconpath", "iconpath"));
				window.dismiss();
			}
		});
		// 点击是拍照
		tv_camera.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				window.dismiss();
				Intent intent2 = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
				intent2.putExtra(MediaStore.EXTRA_OUTPUT,
						Uri.fromFile(new File(Environment.getExternalStorageDirectory(), "loghea.png")));
				startActivityForResult(intent2, 2);// 采用ForResult打开
			}
		});
		// 从相册弄
		TextView tv_photofromalbum = (TextView) view.findViewById(R.id.tv_photofromalbum);
		tv_photofromalbum.setClickable(true);
		tv_photofromalbum.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				window.dismiss();
				Intent intent1 = new Intent(Intent.ACTION_PICK, null);
				intent1.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
				// intent1.addFlags(Intent.[_ACTIVITY_SINGLE_TOP);
				startActivityForResult(intent1, 1);
			}
		});
		TextView tv_cancel = (TextView) view.findViewById(R.id.tv_cancel);
		tv_cancel.setClickable(true);
		tv_cancel.setOnClickListener(new OnClickListener() {

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
	/**
	 * 显示2popupWindow
	 */
	private void showPopwindowtow() {
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
		window.showAtLocation(CommercialActivity.this.findViewById(R.id.delver_img1), Gravity.BOTTOM,
				0, 0);

		// 这里检验popWindow里的button是否可以点击
		TextView tv_camera = (TextView) view.findViewById(R.id.tv_camera);
		tv_camera.setClickable(true);
		TextView tv_show = (TextView) view.findViewById(R.id.tv_show);
		tv_show.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				startActivity(new Intent(CommercialActivity.this, IdCardActivity.class)
						.putExtra("iconpath", "iconpath"));
				window.dismiss();
			}
		});
		// 点击是拍照
		tv_camera.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				window.dismiss();
				Intent intent2 = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
				intent2.putExtra(MediaStore.EXTRA_OUTPUT,
						Uri.fromFile(new File(Environment.getExternalStorageDirectory(), "logheas.png")));
				startActivityForResult(intent2, 22);// 采用ForResult打开
			}
		});
		// 从相册弄
		TextView tv_photofromalbum = (TextView) view.findViewById(R.id.tv_photofromalbum);
		tv_photofromalbum.setClickable(true);
		tv_photofromalbum.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				window.dismiss();
				Intent intent1 = new Intent(Intent.ACTION_PICK, null);
				intent1.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
				// intent1.addFlags(Intent.[_ACTIVITY_SINGLE_TOP);
				startActivityForResult(intent1, 11);
			}
		});
		TextView tv_cancel = (TextView) view.findViewById(R.id.tv_cancel);
		tv_cancel.setClickable(true);
		tv_cancel.setOnClickListener(new OnClickListener() {

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
	/**
	 * 显示2popupWindow
	 */
	private void showPopwindowsend() {
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
		window.showAtLocation(CommercialActivity.this.findViewById(R.id.delver_img1), Gravity.BOTTOM,
				0, 0);

		// 这里检验popWindow里的button是否可以点击
		TextView tv_camera = (TextView) view.findViewById(R.id.tv_camera);
		tv_camera.setClickable(true);
		TextView tv_show = (TextView) view.findViewById(R.id.tv_show);
		tv_show.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				startActivity(new Intent(CommercialActivity.this, IdCardActivity.class)
						.putExtra("iconpath", "iconpath"));
				window.dismiss();
			}
		});
		// 点击是拍照
		tv_camera.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				window.dismiss();
				Intent intent2 = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
				intent2.putExtra(MediaStore.EXTRA_OUTPUT,
						Uri.fromFile(new File(Environment.getExternalStorageDirectory(), "loghease.png")));
				startActivityForResult(intent2, 222);// 采用ForResult打开
			}
		});
		// 从相册弄
		TextView tv_photofromalbum = (TextView) view.findViewById(R.id.tv_photofromalbum);
		tv_photofromalbum.setClickable(true);
		tv_photofromalbum.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				window.dismiss();
				Intent intent1 = new Intent(Intent.ACTION_PICK, null);
				intent1.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
				// intent1.addFlags(Intent.[_ACTIVITY_SINGLE_TOP);
				startActivityForResult(intent1, 111);
			}
		});
		TextView tv_cancel = (TextView) view.findViewById(R.id.tv_cancel);
		tv_cancel.setClickable(true);
		tv_cancel.setOnClickListener(new OnClickListener() {

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
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		switch (requestCode) {
		case 1:
			if (resultCode == RESULT_OK) {
				cropPhoto(data.getData());// 裁剪图片
			}
			break;
		// 如果是调用相机拍照时
		case 2:
			if (resultCode == RESULT_OK) {
				File temp = new File(Environment.getExternalStorageDirectory() + "/loghea.png");
				cropPhoto(Uri.fromFile(temp));// 裁剪图片
			}
			break;

		// 取得裁剪后的图片
		case 3:
			Log.e("111111444    ", ""+data);
			if (data != null) {
				Bundle extras = data.getExtras();
				head = extras.getParcelable("data");
//				Logger.e("img.1111111"  +head);
				if (head != null) {
					/**
					 * 上传服务器代码 //TODO 实现头衔上传
					 */
					setPicToView(head);
					map.put("userId",
							String.valueOf(PreferencesUtils.getInt(getApplicationContext(), PreferenceConstants.UID)));
					map.put("fileName", "物流公司物品1");
					map_file.put("file", new File(fileName));
					// String result="";
//					delver_img1.setBackgroundDrawable(null);
//					delver_img1.setImageBitmap(head);
					
					sendEmptyBackgroundMessage(MsgConstants.MSG_01);
				}
			}
			break;
		// 如果是直接从相册获取
			case 11:
						if (resultCode == RESULT_OK) {
							cropPhototow(data.getData());// 裁剪图片
						}
						break;
					// 如果是调用相机拍照时
					case 22:
						if (resultCode == RESULT_OK) {
							File temp = new File(Environment.getExternalStorageDirectory() + "/logheas.png");
							cropPhototow(Uri.fromFile(temp));// 裁剪图片
						}
						break;

					// 取得裁剪后的图片
					case 33:
//						Log.e("1111112222    ", ""+data);
						if (data != null) {
							Bundle extras = data.getExtras();
							head = extras.getParcelable("data");
							Logger.e("img.11111112222"  +head);
							if (head != null) {
								/**
								 * 上传服务器代码 //TODO 实现头衔上传
								 */
								setPicToViewtwo(head);
								map.put("userId",
										String.valueOf(PreferencesUtils.getInt(getApplicationContext(), PreferenceConstants.UID)));
								map.put("fileName", "物流公司物品2");
								map_file.put("file", new File(fileNamet));
								// String result="";
								sendEmptyBackgroundMessage(MsgConstants.MSG_02);
							}
						}
						break;
						// 如果是直接从相册获取
					case 111:
								if (resultCode == RESULT_OK) {
									cropPhotosend(data.getData());// 裁剪图片
								}
								break;
							// 如果是调用相机拍照时
							case 222:
								if (resultCode == RESULT_OK) {
									File temp = new File(Environment.getExternalStorageDirectory() + "/loghease.png");
									cropPhotosend(Uri.fromFile(temp));// 裁剪图片
								}
								break;

							// 取得裁剪后的图片
							case 333:
								Log.e("1111113333    ", ""+data);
								if (data != null) {
									Bundle extras = data.getExtras();
									head = extras.getParcelable("data");
									Logger.e("img.11111113333"  +head);
									if (head != null) {
										/**
										 * 上传服务器代码 //TODO 实现头衔上传
										 */
										setPicToViewsen(head);
										map.put("userId",
												String.valueOf(PreferencesUtils.getInt(getApplicationContext(), PreferenceConstants.UID)));
										map.put("fileName", "物流公司物品3");
										map_file.put("file", new File(fileNames));
										// String result="";
										sendEmptyBackgroundMessage(MsgConstants.MSG_03);
									}
								}
								break;
		case 101:
			if (data==null) {
				return;
			}
		Log.e("22222  ", data.getStringExtra("code"));
		Log.e("222223name  ", data.getStringExtra("name"));
		code=data.getStringExtra("code");
		et_addses.setText(""+data.getStringExtra("name"));
			break;

		default:
			break;
		}
	}
	@Override
	public void handleBackgroundMessage(Message msg) {
		super.handleBackgroundMessage(msg);
		switch (msg.what) {
		case MsgConstants.MSG_01:
			try {
				result = post(MCUrl.shopDoor, map, map_file);
				sendEmptyUiMessage(MsgConstants.MSG_01);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			break;
		case MsgConstants.MSG_02:
			try {
				result2 = post(MCUrl.shopLicense, map, map_file);
				sendEmptyUiMessage(MsgConstants.MSG_02);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			break;
		case MsgConstants.MSG_03:
			try {
				result3 = post(MCUrl.shopIdCard, map, map_file);
				sendEmptyUiMessage(MsgConstants.MSG_03);
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
		case MsgConstants.MSG_01:
			Log.e("11111result", result);
			IconBean bean = new Gson().fromJson(result, IconBean.class);
			if (bean.getErrCode()== 0) {
				flag1=true;
				Log.e("filePath", bean.data.get(0).filePath);
				ToastUtil.shortToast(CommercialActivity.this, "第一张上传成功");
				icon = bean.data.get(0).filePath;
//				PreferencesUtils.putString(getApplicationContext(), PreferenceConstants.ICON_PATH, icon);
				Log.e("11111touxiang", icon);
				delver_img1.setBackgroundDrawable(null);
				delver_img1.setImageBitmap(head);
				
			}else {
				ToastUtil.shortToast(CommercialActivity.this, ""+bean.getMessage());
			}
			break;
		case MsgConstants.MSG_02:
			Log.e("11111222result", result2);
			IconBean bean1 = new Gson().fromJson(result2, IconBean.class);
			Log.e("11111222result", ""+bean1.data.size());
			if (bean1.getErrCode()== 0) {
				flag2=true;
				Log.e("filePath", bean1.data.get(0).filePath);
				ToastUtil.shortToast(CommercialActivity.this, "第二张上传成功");
				icon2 = bean1.data.get(0).filePath;
//				PreferencesUtils.putString(getApplicationContext(), PreferenceConstants.ICON_PATH, icon);
				Log.e("111112222touxiang", icon2);
				delver_img2.setBackgroundDrawable(null);
				delver_img2.setImageBitmap(head);
			}
			else {
				ToastUtil.shortToast(CommercialActivity.this, ""+bean1.getMessage());
			}
			break;
		case MsgConstants.MSG_03:
			Log.e("11111333result", result3);
			IconBean bean11 = new Gson().fromJson(result3, IconBean.class);
			if (bean11.getErrCode()== 0) {
				flag3=true;
				Log.e("filePath", bean11.data.get(0).filePath);
				ToastUtil.shortToast(CommercialActivity.this, "第三张上传成功");
				icon3 = bean11.data.get(0).filePath;
//				PreferencesUtils.putString(getApplicationContext(), PreferenceConstants.ICON_PATH, icon);
				Log.e("11111333touxiang", icon3);
				delver_img3.setBackgroundDrawable(null);
				delver_img3.setImageBitmap(head);
			}else {
				ToastUtil.shortToast(CommercialActivity.this, ""+bean11.getMessage());
			}
			break;
		default:
			break;
		}
	}
	/***
	 * 裁剪图片方法实现1
	 * 
	 * @param uri1
	 */
	public void cropPhoto(Uri uri) {
		Intent intent = new Intent("com.android.camera.action.CROP");
		intent.setDataAndType(uri, "image/*");
		//设置裁剪
		intent.putExtra("crop", true);
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
	 * 裁剪图片方法实现2
	 * 
	 * @param uri
	 */
	public void cropPhototow(Uri uri) {
		Intent intent = new Intent("com.android.camera.action.CROP");
		intent.setDataAndType(uri, "image/*");
		//设置裁剪
		intent.putExtra("crop", true);
		// aspectX aspectY 是宽高的比例
		intent.putExtra("aspectX", 1.5);
		intent.putExtra("aspectY", 1);
		// outputX outputY 是裁剪图片宽高
		intent.putExtra("outputX", 350);
		intent.putExtra("outputY", 350);
		intent.putExtra("return-data", true);
		startActivityForResult(intent, 33);
	}
	
	/***
	 * 裁剪图片方法实现2
	 * 
	 * @param uri
	 */
	public void cropPhotosend(Uri uri) {
		Intent intent = new Intent("com.android.camera.action.CROP");
		intent.setDataAndType(uri, "image/*");
		//设置裁剪
		intent.putExtra("crop", true);
		// aspectX aspectY 是宽高的比例
		intent.putExtra("aspectX", 1.5);
		intent.putExtra("aspectY", 1);
		// outputX outputY 是裁剪图片宽高
		intent.putExtra("outputX", 350);
		intent.putExtra("outputY", 350);
		intent.putExtra("return-data", true);
		startActivityForResult(intent, 333);
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
		fileName = path + "loghea.png";
		
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
	private void setPicToViewtwo(Bitmap mBitmap) {
		String sdStatus = Environment.getExternalStorageState();
		if (!sdStatus.equals(Environment.MEDIA_MOUNTED)) { // 检测sd是否可用
			return;
		}
		FileOutputStream b = null;
		File file = new File(path);
		file.mkdirs();// 创建文件夹
		fileNamet = path + "logheas.png";
		try {
			b = new FileOutputStream(fileNamet);
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
	private void setPicToViewsen(Bitmap mBitmap) {
		String sdStatus = Environment.getExternalStorageState();
		if (!sdStatus.equals(Environment.MEDIA_MOUNTED)) { // 检测sd是否可用
			return;
		}
		FileOutputStream b = null;
		File file = new File(path);
		file.mkdirs();// 创建文件夹
		fileNames = path + "loghease.png";
		try {
			b = new FileOutputStream(fileNames);
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

	/* 显示Dialog的method */
	private void showDialog(String mess) {
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
