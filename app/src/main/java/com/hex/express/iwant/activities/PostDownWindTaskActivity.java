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
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.List;
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
import com.hex.express.iwant.bean.AdvertBean;
import com.hex.express.iwant.bean.AreaBean;
import com.hex.express.iwant.bean.CityBean;
import com.hex.express.iwant.bean.DefaultExpressBean;
import com.hex.express.iwant.bean.DowInsuraBean;
import com.hex.express.iwant.bean.DownSpecialBean;
import com.hex.express.iwant.bean.IconBean;
import com.hex.express.iwant.constance.MCUrl;
import com.hex.express.iwant.constance.MsgConstants;
import com.hex.express.iwant.constance.PreferenceConstants;
import com.hex.express.iwant.helper.AreaDboperation;
import com.hex.express.iwant.helper.CheckDbUtils;
import com.hex.express.iwant.helper.CityDbOperation;
import com.hex.express.iwant.helper.DbManager;
import com.hex.express.iwant.http.AsyncHttpUtils;
import com.hex.express.iwant.http.UrlMap;
import com.hex.express.iwant.utils.DataTools;
import com.hex.express.iwant.utils.Logger;
import com.hex.express.iwant.utils.PreferencesUtils;
import com.hex.express.iwant.utils.StringUtil;
import com.hex.express.iwant.utils.ToastUtil;
import com.hex.express.iwant.views.RoundCornerImageView;
import com.hex.express.iwant.views.TitleBarView;
import com.loopj.android.http.AsyncHttpResponseHandler;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Message;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.RelativeLayout;
import android.widget.TextView;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 【发布顺风速递】界面
 * 
 * @author SCHT-50
 * 
 */
public class PostDownWindTaskActivity extends BaseActivity {
	@Bind(R.id.tbv_show)
	TitleBarView tbv_show;
	// 寄件人信息
	@Bind(R.id.iv_add)
	ImageView iv_add;
	@Bind(R.id.et_name)
	EditText et_name;
	@Bind(R.id.et_tel)
	EditText et_tel;
	@Bind(R.id.et_address)
	EditText et_address;
	@Bind(R.id.et_address_specific)// 发件人具体地址
	EditText et_address_specific;
	// 收件人信息
	@Bind(R.id.iv_add_receiver)
	ImageView iv_add_receiver;
	@Bind(R.id.et_add_name)
	EditText et_add_name;
	@Bind(R.id.et_add_tel)
	EditText et_add_tel;
	@Bind(R.id.et_add_address)
	EditText et_add_address;
	@Bind(R.id.et_add_address_specific)//收件人具体地址
	EditText et_add_address_specific;
	
	// 物品信息
	@Bind(R.id.et_goodname)
	EditText et_goodname;
	@Bind(R.id.et_weight)
	EditText et_weight;
	@Bind(R.id.et_good_detail)
	EditText et_good_detail;
	// 物品长宽高
	@Bind(R.id.et_chang)
	EditText et_chang;
	@Bind(R.id.et_kuan)
	EditText et_kuan;
	@Bind(R.id.et_height)
	EditText et_height;
	//广告
	@Bind(R.id.tv_tip)
	TextView tv_tip;
	@Bind(R.id.texs)
	TextView tex;
	
	@Bind(R.id.rl_bannerTip)
	RelativeLayout rl_bannerTip;
	
	// 拍照
	@Bind(R.id.img_headportrait)
	RoundCornerImageView img_headportrait;
	// 确认发布
	@Bind(R.id.btn_submit)
	Button btn_submit;
	//是否买保险
	@Bind(R.id.checkbox_bao)
	CheckBox checkbox_bao;
	//是否需要大型货车
	@Bind(R.id.checkbox_detail)
	CheckBox checkbox_detail;
	@Bind(R.id.lout10)
	LinearLayout lout10;
	@Bind(R.id.edt_price)
	EditText edt_price;
	@Bind(R.id.v_pp)
	View v_pp;
	
	PopupWindow window02;
	PopupWindow window03;
	private Bitmap imghead;
	private String fileName = path + "imghead.png";
	private static String path = "/sdcard/myHead/";// sd路径
	private Map map = new HashMap<String, String>();
	private Map map_file = new HashMap<String, File>();
	private String result;
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
	private String icon;
	private boolean flag = false;// false表示没有上传物品图片，true相反
	private boolean chox=false; 
	private boolean choxdel=false; 
	private String tou;
	DowInsuraBean	adbean;
	private String	townCode,townaddressd;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_down_special);
		ButterKnife.bind(PostDownWindTaskActivity.this);
		initData();
		setOnClick();
	}

	@Override
	public void onWeightClick(View v) {
		// TODO Auto-generated method stub

	}

	@OnClick({ R.id.btn_submit, R.id.img_headportrait, R.id.et_address, R.id.et_add_address, R.id.iv_add,
			R.id.iv_add_receiver,R.id.rl_bannerTip ,R.id.checkbox_bao})
	public void MyOnClick(View view) {
		switch (view.getId()) {
		case R.id.rl_bannerTip://广告点击去镖师认证
			startActivity(new Intent(getApplicationContext(), H5ModelActivity.class));
			break;
		case R.id.btn_submit:
			addPostResult();
//			ceshi();
			break;
		case R.id.img_headportrait:// 添加物品照片
			showPopwindow();
			break;
		case R.id.iv_add:// 从手机通讯录选择发件人name,phone
			Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
			startActivityForResult(intent, 0);
			// startActivityForResult(new Intent(DownWindSpecialActivity.this,
			// SendPersonActivity.class), 4);
			break;
		case R.id.iv_add_receiver:// 从手机通讯录选择收件人name,phone
			Intent intent1 = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
			startActivityForResult(intent1, 9);
			// startActivityForResult(new Intent(DownWindSpecialActivity.this,
			// AddReceiverActivity.class), 5);
			break;
		case R.id.et_address:// 选择寄件人地址
			startActivityForResult(new Intent(PostDownWindTaskActivity.this, AddressActivity.class), 7);
			break;
		case R.id.et_add_address:// 选择收件人地址
			startActivityForResult(new Intent(PostDownWindTaskActivity.this, AddressReceiveActivity.class), 8);
			break;
		default:
			break;
		}
	}
   private void ceshi(){
	   Intent intent = new Intent(PostDownWindTaskActivity.this, DownWindPayActivity.class);
	   intent.putExtra("money","22");
//		intent.putExtra("insureCost", String.valueOf(df.format(bean.data.get(0).insureCost)));
		intent.putExtra("insureCost", "0");
		intent.putExtra("billCode", "72795SF20161101");
//		intent.putExtra("type", 2);
		startActivityForResult(intent, 10);
   }
	private void addPostResult() {
		String money = null;
		JSONObject obj = new JSONObject();
//		|| et_chang.getText().toString().equals("") || et_kuan.getText().toString().equals("")
//		|| et_height.getText().toString().equals("")
		if (!flag) {
			ToastUtil.shortToast(getApplicationContext(), "请上传物品照片");
			return;
		}
		if (!StringUtil.isMobileNO(et_tel.getText().toString())
				|| (et_tel.getText().toString().trim().length() != 11)
						&& !StringUtil.isMobileNO(et_add_tel.getText().toString())
				|| (et_add_tel.getText().toString().trim().length() != 11)) {
			ToastUtil.shortToast(getApplicationContext(), "请输入正确的手机号码");
			return;
		}
		if (et_add_name.getText().toString().equals("") || et_add_tel.getText().toString().equals("")
				|| et_name.getText().toString().equals("") || et_tel.getText().toString().equals("")
				|| et_goodname.getText().toString().equals("") || et_weight.getText().toString().equals("")
				) {
			ToastUtil.shortToast(getApplicationContext(), "请完善信息");
			return;
		}
		if (et_add_address.getText().toString().equals("") || et_address.getText().toString().equals("")
				|| cityCode.equals("") || receiver_citycode.equals("")) {
			ToastUtil.shortToast(getApplicationContext(), "请核对你输入的地址信息");
			return;
		}
		if (choxdel) {
			if ( et_chang.getText().toString().equals("") || et_kuan.getText().toString().equals("")
		    || et_height.getText().toString().equals("")) {
				ToastUtil.shortToast(getApplicationContext(), "请完善信息");
				return;
			}
		}else {
			et_chang.setText("0");
			et_kuan.setText("0");
			et_height.setText("0");
			
		}
	
		try {
			obj.put("userId",
					String.valueOf(PreferencesUtils.getInt(getApplicationContext(), PreferenceConstants.UID)));
			obj.put("latitude", latitude);
			obj.put("longitude", longitude);
			obj.put("cityCode", cityCode);
			obj.put("townCode", townCode);
			obj.put("personName", et_name.getText().toString());
			obj.put("mobile", et_tel.getText().toString());
			obj.put("address", et_address.getText().toString()+et_address_specific.getText().toString());
			obj.put("fromLatitude", mylatitude);
			obj.put("fromLongitude", mylongitude);
			obj.put("personNameTo", et_add_name.getText().toString());
			obj.put("mobileTo", et_add_tel.getText().toString());
			obj.put("addressTo", et_add_address.getText().toString()+et_add_address_specific.getText().toString());
			obj.put("cityCodeTo", receiver_citycode);
			obj.put("toLatitude", receiver_latitude);
			obj.put("toLongitude", receiver_longitude);
			obj.put("publshDeviceId", DataTools.getDeviceId(PostDownWindTaskActivity.this));
			
			obj.put("matName", et_goodname.getText().toString());
			obj.put("matWeight", et_weight.getText().toString());
			obj.put("matImageUrl", icon);
			obj.put("matRemark", et_good_detail.getText().toString());
			obj.put("high", et_height.getText().toString());
			obj.put("wide", et_kuan.getText().toString());
			obj.put("length", et_chang.getText().toString());
			if(chox){
				tou="Y";
				obj.put("whether", tou);
				obj.put("premium", edt_price.getText().toString().trim());
			}else {
				tou="N";
				obj.put("whether", tou);
			}
		
		} catch (JSONException e) {
			e.printStackTrace();
		}
		dialog.show();
		Log.e("查看数据", obj.toString());
		AsyncHttpUtils.doPostJson(PostDownWindTaskActivity.this, MCUrl.DOWNWINDTASKPUBLISHS, obj.toString(),
				new AsyncHttpResponseHandler() {

					@Override
					public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
						Log.e("oppo", new String(arg2));
						dialog.dismiss();
						DownSpecialBean bean = new Gson().fromJson(new String(arg2), DownSpecialBean.class);
						Log.e("oppop", bean.data.toString());
						if (bean.getErrCode() == 0) {
							DecimalFormat df = new DecimalFormat("######0.00");

//							if (!"Y".equals(PreferencesUtils.getString(getApplicationContext(),
//									PreferenceConstants.REALMANAUTH))) {
//								AlertDialog.Builder ad = new Builder(PostDownWindTaskActivity.this);
//								ad.setTitle("温馨提示");
//								ad.setMessage("根据《中华人民共和国反恐怖主义法》规定，发快递须实名制，若您所填信息不真实或不完善，请尽快修改和完善，感谢您的配合。");
//								ad.setNegativeButton("确认", new DialogInterface.OnClickListener() {
//
//									@Override
//									public void onClick(DialogInterface arg0, int arg1) {
//										startActivity(new Intent(PostDownWindTaskActivity.this,
//												RegisterSetImageAndNameActivity.class));
//									}
//								});
//								ad.setPositiveButton("取消", new DialogInterface.OnClickListener() {
//
//									@Override
//									public void onClick(DialogInterface arg0, int arg1) {
//										arg0.dismiss();
//
//									}
//								});
//								ad.create().show();
//
//							} else {
								ToastUtil.shortToast(PostDownWindTaskActivity.this, bean.getMessage());
								Intent intent = new Intent(PostDownWindTaskActivity.this, DownWindPayActivity.class);
								intent.putExtra("money", bean.data.get(0).transferMoney);
//								intent.putExtra("insureCost", String.valueOf(df.format(bean.data.get(0).insureCost)));
								intent.putExtra("insureCost", bean.data.get(0).insureCost);
								intent.putExtra("billCode", bean.data.get(0).billCode);
								Logger.e("billCode数据", bean.data.get(0).billCode);
								startActivityForResult(intent, 10);
//							}

						}else {
							ToastUtil.shortToast(PostDownWindTaskActivity.this, bean.getMessage());
						}

					}

					@Override
					public void onFailure(int arg0, Header[] arg1, byte[] arg2, Throwable arg3) {
						dialog.dismiss();
					}
				});

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
//		Log.e("city", city);
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
			List<AreaBean> selectDataFromDbs = new AreaDboperation()
					.selectDataFromDb("select * from area where area_name='" + townaddressd + "'");
			 
			if (selectDataFromDbs.size()>0 && !selectDataFromDbs.equals("")) {
				townCode=selectDataFromDbs.get(0).area_code;
				Log.e("11111townCode", townCode);
			}
		}
//		if (townaddressd.equals("")) {
//			townaddressd = PreferencesUtils.getString(getApplicationContext(), PreferenceConstants.Codedess);
//		}
	
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		switch (requestCode) {
		case 0:// 增加手机寄件人联系人的回调
			if (data == null) {
				return;
			}
			if (resultCode == Activity.RESULT_OK) {
				ContentResolver reContentResolverol = getContentResolver();
				Uri contactData = data.getData();
				// 查询就是输入URI等参数,其中URI是必须的,其他是可选的,如果系统能找到URI对应的ContentProvider将返回一个Cursor对象.
				Cursor cursor = managedQuery(contactData, null, null, null, null);
				cursor.moveToFirst();
				if (cursor.moveToFirst()) {
					
				
				// 获得DATA表中的名字
				String name = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
				// 条件为联系人ID
				et_name.setText(name);
				String contactId = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
				// 获得DATA表中的电话号码，条件为联系人ID,因为手机号码可能会有多个
				Cursor phone = reContentResolverol.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
						ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = " + contactId, null, null);
				while (phone.moveToNext()) {
					String phone_number = phone
							.getString(phone.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
					if (!phone_number.equals("")){
						et_tel.setText(phone_number);
					}
				}
				if(Build.VERSION.SDK_INT < 14) {
				    cursor.close();
				   }
//				phone.close();
				}else{  
	                // 没有权限，跳到设置界面，调用Android系统“应用程序信息（Application Info）”界面  
	                new Builder(PostDownWindTaskActivity.this)
	                .setMessage("app需要开启读取联系人权限")  
	                .setPositiveButton("设置", new DialogInterface.OnClickListener() {  
	                    @Override  
	                    public void onClick(DialogInterface dialogInterface, int i) {  
	                        Intent intent = new Intent(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS);  
	                        intent.setData(Uri.parse("package:" + getPackageName()));  
	                        startActivity(intent);  
	                    }  
	                })  
	                .setNegativeButton("取消", null)  
	                .create()  
	                .show();  
	                  
	            }   
//			 cursor.close();
			}

			break;
		// 如果是直接从相册获取
		case 1:
			if (resultCode == RESULT_OK) {
//				String path = "";
//				if(data.getData().getPath().startsWith("/external")){
//				String[] proj = {MediaStore.Images.Media.DATA};
//				Cursor cursor = mContext.getContentResolver().query(data.getData(), proj, null, null, null);
//				cursor.moveToFirst();
//				int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
//				path = cursor.getString(column_index);
//				}else{
//				path = data.getData().getPath();
//				}
//				int width = getResources().getDimensionPixelOffset(R.dimen.avatar_width);
//				int height = getResources().getDimensionPixelOffset(R.dimen.avatar_height);
//				cropImage(imageUri.fromFile(new File(path)), width, height, IMAGE_CAPTURE_CROP);
				if (data != null) {
				cropPhoto(data.getData());// 裁剪图片
				}
			}
			break;
		// 如果是调用相机拍照时
		case 2:
			if (resultCode == RESULT_OK) {
				File temp = new File(Environment.getExternalStorageDirectory() + "/goodsphoto.png");
				cropPhoto(Uri.fromFile(temp));// 裁剪图片
			}
			break;

		// 取得裁剪后的图片
		case 3:
			if (data != null) {
				Bundle extras = data.getExtras();
				imghead = extras.getParcelable("data");
				if (imghead != null) {
					/**
					 * 上传服务器代码 //TODO 实现头衔上传
					 */
					setPicToView(imghead);
					map.put("userId",
							String.valueOf(PreferencesUtils.getInt(getApplicationContext(), PreferenceConstants.UID)));
					map.put("fileName", "顺风物品");
					map_file.put("file", new File(fileName));
					sendEmptyBackgroundMessage(MsgConstants.MSG_01);
				}
			}
			break;
		case 4:
			if (resultCode == Activity.RESULT_OK) {
				et_name.setText(data.getStringExtra("name"));
				et_tel.setText(data.getStringExtra("phone"));
				et_address.setText(data.getStringExtra("address"));
				mylatitude = data.getDoubleExtra("latitude", 0);
				mylongitude = data.getDoubleExtra("longitude", 0);
				city = data.getStringExtra("city");
//				getCityCode();
				Log.e("PPPPPPPPPPPPPPPPPPPP",
						data.getStringExtra("name") + data.getStringExtra("phone") + data.getStringExtra("address"));
			}
			break;

		case 5:
			if (resultCode == Activity.RESULT_OK) {
				// receiver_city = data.getStringExtra("city");
				receiver_latitude = data.getDoubleExtra("latitude", 0);
				receiver_longitude = data.getDoubleExtra("longitude", 0);
				Log.e("收件人", receiver_latitude + "++++++" + receiver_longitude);
				receiver_citycode = data.getStringExtra("citycode");
				et_add_name.setText(data.getStringExtra("name"));
				et_add_tel.setText(data.getStringExtra("phone"));
				et_add_address.setText(data.getStringExtra("location") + data.getStringExtra("address"));
			}

			break;
		case 7:
			if (resultCode == RESULT_OK) {
				mylatitude = data.getDoubleExtra("latitude", 0);
				mylongitude = data.getDoubleExtra("longitude", 0);
				city = data.getStringExtra("city");
				townaddressd =data.getStringExtra("townaddressd");
				getCityCode();
				et_address.setText(data.getStringExtra("address").replace("中国", ""));
			}
			break;
		case 8:
			if (data == null) {
				return;
			}
			if (resultCode == RESULT_OK) {
				receiver_latitude = data.getDoubleExtra("latitude", 0);
				receiver_longitude = data.getDoubleExtra("longitude", 0);
				city = data.getStringExtra("city"); 
		       String		townadd =data.getStringExtra("townaddressd");
				receive = true;
				getCityCode();
				et_add_address.setText(data.getStringExtra("address").replace("中国", ""));
			}
			break;
		case 9:// 增加手机收件人联系人的回调
			if (data == null) {
				return;
			}
			if (resultCode == Activity.RESULT_OK) {
				ContentResolver reContentResolverol = getContentResolver();
				Uri contactData = data.getData();
				// 查询就是输入URI等参数,其中URI是必须的,其他是可选的,如果系统能找到URI对应的ContentProvider将返回一个Cursor对象.
				Cursor cursor = managedQuery(contactData, null, null, null, null);
//				cursor.moveToFirst();
				if (cursor.moveToFirst()) {
					try {
						
				// 获得DATA表中的名字
				String name = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
				// 条件为联系人ID
				et_add_name.setText(name);
				String contactId = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
				// 获得DATA表中的电话号码，条件为联系人ID,因为手机号码可能会有多个
				Cursor phone = reContentResolverol.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
						ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = " + contactId, null, null);
//			   if (phone==null) {et_add_tel
//				return;
//			   }
				while (phone.moveToNext()) {
					String phone_number = phone
							.getString(phone.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
					if (!phone_number.equals("")){
						et_add_tel.setText(phone_number);
					}
				}

					} catch (Exception e) {
						// TODO: handle exception
					}
//				phone.close();
					if(Build.VERSION.SDK_INT < 14) {
					    cursor.close();
					   }
			}else{  
                // 没有权限，跳到设置界面，调用Android系统“应用程序信息（Application Info）”界面  et_add_tel.setText(phone_number);
                new Builder(PostDownWindTaskActivity.this)
                .setMessage("app需要开启读取联系人权限")  
                .setPositiveButton("设置", new DialogInterface.OnClickListener() {  
                    @Override  
                    public void onClick(DialogInterface dialogInterface, int i) {  
                        Intent intent = new Intent(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS);  
                        intent.setData(Uri.parse("package:" + getPackageName()));  
                        startActivity(intent);  
                    }  
                })  
                .setNegativeButton("取消", null)  
                .create()  
                .show();  
                  
            }   
//		 cursor.close();
			}

			break;
		case 10:
			if (resultCode == RESULT_OK) {
				finish();
			}
			break;
		}
	};

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

	@Override
	public void handleBackgroundMessage(Message msg) {
		// TODO Auto-generated method stub
		super.handleBackgroundMessage(msg);
		switch (msg.what) {
		case MsgConstants.MSG_01:
			try {
				result = post(MCUrl.FILEDOWNWIND, map, map_file);
				sendEmptyUiMessage(MsgConstants.MSG_01);
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
			Log.e("111111result", result);
			IconBean bean = new Gson().fromJson(result, IconBean.class);
			if (bean.data.size() != 0) {
				flag = true;
				icon = bean.data.get(0).filePath;
				PreferencesUtils.putString(getApplicationContext(), PreferenceConstants.GOODPATH, icon);
				setPicToView(imghead);
				img_headportrait.setBackgroundDrawable(null);// 切换图片前先置空
				img_headportrait.setImageBitmap(imghead);
			}
			break;

		default:
			break;
		}
	}

	@Override
	public void initView() {
		// TODO Auto-generated method stub

	}

	@Override
	public void initData() {
		
		if (PreferencesUtils.getString(getApplicationContext(), PreferenceConstants.USERTYPE).equals("1")) {
			rl_bannerTip.setVisibility(View.VISIBLE);
		}
		else{
			rl_bannerTip.setVisibility(View.GONE);	
		}
		if (PreferencesUtils.getString(getApplicationContext(), PreferenceConstants.USERNAME) != null
				&& !PreferencesUtils.getString(getApplicationContext(), PreferenceConstants.USERNAME).equals("")) {
			et_name.setText(PreferencesUtils.getString(getApplicationContext(), PreferenceConstants.USERNAME));
		}
		if (!PreferencesUtils.getString(getApplicationContext(), PreferenceConstants.MOBILE).equals("")
				&& PreferencesUtils.getString(getApplicationContext(), PreferenceConstants.MOBILE) != null) {
			et_tel.setText(PreferencesUtils.getString(getApplicationContext(), PreferenceConstants.MOBILE));
		}
		img_headportrait.setBackgroundResource(R.drawable.camre_img);
		tbv_show.setTitleText("发布顺风速递");
		tbv_show.setRightBtnText("声明");
		tbv_show.getRightBtn().setTextColor(getResources().getColor(R.color.white));
		tbv_show.setRightBtnOnclickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intent=new Intent();
				intent.putExtra("url", "http://www.efamax.com/mobile/InsureSF.html");
				intent.putExtra("name", "1");
				intent.setClass(PostDownWindTaskActivity.this, HWdvertActivity.class);
				startActivity(intent);
			}
		});
		et_address.setFocusable(false);
		// getDefaultMessage();
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
		checkbox_bao.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
				// TODO Auto-generated method stub
			if(arg1){
				chox=arg1;
				edt_price.setVisibility(View.VISIBLE);
			   	v_pp.setVisibility(View.VISIBLE);
//			   	if (!"Y".equals(PreferencesUtils.getString(getApplicationContext(),
//						PreferenceConstants.REALMANAUTH))) {
//					AlertDialog.Builder ad = new Builder(PostDownWindTaskActivity.this);
//					ad.setTitle("温馨提示");
//					ad.setMessage("根据《中华人民共和国反恐怖主义法》规定，发快递须实名制，若您所填信息不真实或不完善，请尽快修改和完善，感谢您的配合。");
//					ad.setNegativeButton("确认", new DialogInterface.OnClickListener() {
//
//						@Override
//						public void onClick(DialogInterface arg0, int arg1) {
//							startActivity(new Intent(PostDownWindTaskActivity.this,
//									RegisterSetImageAndNameActivity.class));
//						}
//					});
//					ad.setPositiveButton("取消", new DialogInterface.OnClickListener() {
//
//						@Override
//						public void onClick(DialogInterface arg0, int arg1) {
//							arg0.dismiss();
//
//						}
//					});
//					ad.create().show();
//
//				} else {
			   			showPaywindow();
//			}
			}else {
				chox=arg1;
				edt_price.setVisibility(View.GONE);
			   	v_pp.setVisibility(View.GONE);
			}
			}
		});
	}

	/**
	 * 获取默认寄件人信息
	 */
	private void getDefaultMessage() {
		dialog.show();
		AsyncHttpUtils.doSimGet(
				UrlMap.getfour(MCUrl.EXPRESS_DEFAULT, "userId",
						PreferencesUtils.getInt(getApplicationContext(), PreferenceConstants.UID) + "", "latitude",
						latitude + "", "longitude", longitude + "", "cityCode", cityCode),
				new AsyncHttpResponseHandler() {
					@Override
					public void onFailure(int arg0, Header[] arg1, byte[] arg2, Throwable arg3) {
						dialog.dismiss();

					}

					@Override
					public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
						if (dialog != null)
							dialog.dismiss();
						if (arg2 == null || arg2.length == 0)
							return;
						Log.e("nn", new String(arg2));
						DefaultExpressBean bean = new Gson().fromJson(new String(arg2), DefaultExpressBean.class);
						if (bean.getErrCode() == 0) {
							if (!bean.data.get(0).address.equals("")) {
								et_name.setText(bean.data.get(0).personName);
								et_tel.setText(bean.data.get(0).mobile);
								et_address.setText(bean.data.get(0).address);
							} else {
								et_name.setText(PreferencesUtils.getString(getApplicationContext(),
										PreferenceConstants.USERNAME));
								et_tel.setText(PreferencesUtils.getString(getApplicationContext(),
										PreferenceConstants.MOBILE));
							}
						}

					}
				});

	}

	@Override
	public void setOnClick() {
		checkbox_detail.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
				// TODO Auto-generated method stub
				if (arg1) {
					choxdel=arg1;
					lout10.setVisibility(View.VISIBLE);
					tex.setVisibility(View.VISIBLE);
				}else {
					choxdel=arg1;
					lout10.setVisibility(View.GONE);
					tex.setVisibility(View.GONE);
				}
			}
		});
		// TODO Auto-generated method stub
		AsyncHttpUtils.doSimGet(MCUrl.DowInsura, new AsyncHttpResponseHandler() {
			@Override
			public void onFailure(int arg0, Header[] arg1, byte[] arg2, Throwable arg3) {
				
			}
			@Override
			public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
				Log.e("111111", new String(arg2));
					adbean = new Gson().fromJson(new String(arg2), DowInsuraBean.class);
				
				
			}
		});
	}

	@Override
	public void getData() {
		// TODO Auto-generated method stub

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
		window.showAtLocation(PostDownWindTaskActivity.this.findViewById(R.id.img_headportrait), Gravity.BOTTOM, 0, 0);

		// 这里检验popWindow里的button是否可以点击
		TextView tv_camera = (TextView) view.findViewById(R.id.tv_camera);
		TextView tv_show = (TextView) view.findViewById(R.id.tv_show);
		tv_show.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				startActivity(new Intent(PostDownWindTaskActivity.this, IdCardActivity.class).putExtra("iconpath", "goodpath"));
				window.dismiss();
			}
		});
		tv_camera.setClickable(true);
		// 点击是拍照
		tv_camera.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				window.dismiss();
				Intent intent2 = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
				intent2.putExtra(MediaStore.EXTRA_OUTPUT,
						Uri.fromFile(new File(Environment.getExternalStorageDirectory(), "goodsphoto.png")));
				// intent2.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
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
				// 相册选择
				Intent intent1 = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
				startActivityForResult(intent1, 1);

//				Intent intent1 = new Intent(Intent.ACTION_PICK, null);
//				intent1.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
//				// intent1.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
//				
				
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
		intent.putExtra("aspectX", 1);
		intent.putExtra("aspectY", 1);
		// outputX outputY 是裁剪图片宽高
		intent.putExtra("outputX", 150);
		intent.putExtra("outputY", 150);
		intent.putExtra("return-data", true);
		startActivityForResult(intent, 3);
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

	// 点击空白区域关闭输入法
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		inputMethodManager.hideSoftInputFromWindow(PostDownWindTaskActivity.this.getCurrentFocus().getWindowToken(),
				InputMethodManager.HIDE_NOT_ALWAYS);
		return super.onTouchEvent(event);
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
	/**
	 * 显示投保提示信息
	 */
	private void showPaywindow() {
		
		TextView btnsaves_pan,tet_tishi;
		// 利用layoutInflater获得View
		LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View view = inflater.inflate(R.layout.popwidndow_baojia, null);
		// 下面是两种方法得到宽度和高度 getWindow().getDecorView().getWidth()
		window02 = new PopupWindow(view, WindowManager.LayoutParams.MATCH_PARENT,
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
		window02.showAtLocation(PostDownWindTaskActivity.this.findViewById(R.id.checkbox_bao), Gravity.CENTER, 0, 0);
		btnsaves_pan=(TextView) view.findViewById(R.id.btnsaves_pan);
		tet_tishi=(TextView) view.findViewById(R.id.tet_tishi);
		tet_tishi.setText(adbean.getMessage());
		btnsaves_pan.setOnClickListener(new OnClickListener() {
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
