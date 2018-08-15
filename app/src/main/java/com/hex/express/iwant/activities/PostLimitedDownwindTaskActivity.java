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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
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
import com.baidu.location.LocationClientOption.LocationMode;
import com.google.gson.Gson;
import com.hex.express.iwant.R;
import com.hex.express.iwant.iWantApplication;
import com.hex.express.iwant.bean.AreaBean;
import com.hex.express.iwant.bean.BaseBean;
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
import com.hex.express.iwant.utils.PreferencesUtils;
import com.hex.express.iwant.utils.StringUtil;
import com.hex.express.iwant.utils.ToastUtil;
import com.hex.express.iwant.views.RoundCornerImageView;
import com.hex.express.iwant.views.TimePickerView;
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
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Bitmap.Config;
import android.graphics.PorterDuff.Mode;
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
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.PopupWindow.OnDismissListener;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 【发布限时专递】界面
 * 
 * @author huyichuan
 *
 */
public class PostLimitedDownwindTaskActivity extends BaseActivity {
	@Bind(R.id.tbv_show)
	TitleBarView tbv_show;
	// 寄件人信息
	@Bind(R.id.et_name)
	EditText et_name;//寄件人姓名
	@Bind(R.id.et_tel)
	EditText et_tel;//寄件人电话
	@Bind(R.id.et_address)
	EditText et_address;//寄件人地址
	@Bind(R.id.tv_modifySenderAddress)
	TextView tv_modifySenderAddress;// 修改寄件人地址
	@Bind(R.id.iv_senderList)
	ImageView iv_senderList;// 发件人获取手机通讯录；
	// 收件人信息
	@Bind(R.id.et_add_name)
	EditText et_add_name;
	@Bind(R.id.et_add_tel)
	EditText et_add_tel;
	@Bind(R.id.et_add_address)
	EditText et_add_address;
	@Bind(R.id.et_address_specific)
	EditText et_address_specific; //发件人具体地址
	@Bind(R.id.et_add_address_specific)
	EditText et_add_address_specific; //收件人具体地址
	@Bind(R.id.tv_modifyRecieverAddress)
	TextView tv_modifyRecieverAddress;// 修改收件人地址
	@Bind(R.id.iv_receiverList)
	ImageView iv_receiverList;// 收件人获取手机通讯录；
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
	@Bind(R.id.rl_bannerTip)
	RelativeLayout rl_bannerTip;
	
	// 拍照
	@Bind(R.id.img_headportrait)
	RoundCornerImageView img_headportrait;
	//时间选择
	@Bind(R.id.et_time)
	EditText et_time;
	TimePickerView pvTime;
	// 确认发布
	@Bind(R.id.btn_submit)
	Button btn_submit;
	@Bind(R.id.checkbox_baoss)
	CheckBox checkbox_baoss;
	@Bind(R.id.checkbox_detai)//是否需要大型车
	CheckBox checkbox_detai;
	@Bind(R.id.ll03)
	LinearLayout ll03;
	@Bind(R.id.edt_prices)
	EditText edt_prices;
	@Bind(R.id.v_pps)
	View v_pps;
	@Bind(R.id.tex)
	TextView tex;
	
	private Bitmap imghead;
	private String fileName = path + "imghead.png";
	private static String path = "/sdcard/myHead/";// sd路径
	private Map map = new HashMap<String, String>();
	private Map map_file = new HashMap<String, File>();
	private String result;
	DowInsuraBean	adbean;
	
	//发件人的详细地址
	private String detailLocationOfSender;
	//收件人的详细地址
	private String detailLocationOfReceiver;
	//定位得到的地址
	private String address;
	//选择地址回调得到的地址
	private String return_address;
	// 当前位置经纬度
	private double latitude;
	private double longitude;
	
	//发件城市
	private String city = "";
	//发件人经纬度
	private double myLatitude;// 纬度
	private double myLongitude;// 经度
	private LocationClient client;
	//发件地址的城市代码
	private String cityCode = "";
	//收件人经纬度
	private double receiverLongitude;
	private double receiverLatitude;
	//收件地址的城市代码
	private String receiver_citycode = "";
	private boolean receive;
	private String icon;
	private boolean flag = false;// false表示没有上传物品图片，true相反
	private boolean frist = false;// 是否第一次获取位置成功
	private boolean chox=false; 
	private boolean choxde=false; 
	PopupWindow window02;
	private String	townCode,townaddressd;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_post_limited_downwindtask);
		ButterKnife.bind(PostLimitedDownwindTaskActivity.this);
		initData();
		setOnClick();
		// 时间选择器
				pvTime = new TimePickerView(this, TimePickerView.Type.MONTH_DAY_HOUR_MIN);
				// 控制时间范围
//				 Calendar calendar = Calendar.getInstance();
//				 pvTime.setRange(calendar.get(Calendar.YEAR) - 20,
//				 calendar.get(Calendar.YEAR));
				pvTime.setTime(new Date());
				pvTime.setCyclic(false);
				pvTime.setCancelable(true);
				// 时间选择后回调
				pvTime.setOnTimeSelectListener(new TimePickerView.OnTimeSelectListener() {

					@Override
					public void onTimeSelect(Date date) {
						   Date now = new Date(); 
						   SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");//日期格式
						   String hehe = dateFormat.format(now); 
						   SimpleDateFormat dfs = new SimpleDateFormat("yyyy-MM-dd HH:mm");     
						   Date begin = null;
						   Date end = null ;
						try {
							begin = dfs.parse(hehe);
					        end = dfs.parse(getTime(date));
						} catch (ParseException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}     
						Log.e("111shijian", ""+getTime(date));
						  long between=(end.getTime()-begin.getTime())/1000;//除以1000是为了转换成秒   
						  if (between>1800) {
							  et_time.setText(getTime(date));
						}else {
							ToastUtil.shortToast(PostLimitedDownwindTaskActivity.this, "请选择30分钟以后的时间！");
						}
						
					}
				});
	}

	
	@Override
	public void onWeightClick(View v) {
		// TODO Auto-generated method stub

	}
	
	//选择时间
	public static String getTime(Date date) {
		SimpleDateFormat format = new SimpleDateFormat("MM-dd HH:mm");
//		SimpleDateFormat formats = new SimpleDateFormat("yyyy");
		Date now = new Date(); 
		  SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy");//日期格式
		   String hehe = dateFormat.format(now); 
		return hehe+"-"+format.format(date);
//		   return dateFormat.format(now);
	}

	@OnClick({R.id.et_time,R.id.btn_submit, R.id.img_headportrait, R.id.et_address, R.id.rl_bannerTip ,R.id.iv_selectSender,R.id.iv_senderList, R.id.tv_modifySenderAddress, R.id.iv_selectReciever, R.id.iv_receiverList,
		R.id.tv_modifyRecieverAddress,R.id.checkbox_baoss,})
	public void MyOnClick(View view) {
		Intent intent = null;
		Bundle bundle = new Bundle();
		switch (view.getId()) {
		case R.id.et_time:// 获取日期
			pvTime.show();
			break;
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
//		case R.id.iv_selectSender:// 从寄件人列表中选择寄件人
//			startActivityForResult(new Intent(PostLimitedDownwindTaskActivity.this, SendPersonActivity.class), 5);
//			break;
//		case R.id.iv_selectReciever:// 从收件人列表中选择收件人
//			startActivityForResult(new Intent(PostLimitedDownwindTaskActivity.this, AddReceiverActivity.class), 6);
//			break;
		case R.id.et_address:// 获取寄件人定位的地点，或者修改
			intent = new Intent(PostLimitedDownwindTaskActivity.this, AddressActivity.class);
			startActivityForResult(intent, 7);
			break;
		case R.id.et_add_address:// 获取收件人定位的地点，或者修改
			intent = new Intent(PostLimitedDownwindTaskActivity.this, AddressActivity.class);
			startActivityForResult(intent, 8);
			break;
		case R.id.tv_modifySenderAddress:// 获取寄件人定位的地点，或者修改
			Log.e("et_address-02", et_address+"-----");
			intent = new Intent(PostLimitedDownwindTaskActivity.this, AddressActivity.class);
			bundle.putString("address", address);
			intent.putExtras(bundle);
			startActivityForResult(intent, 7);
			break;
		case R.id.tv_modifyRecieverAddress:// 获取收件人定位的地点，或者修改
			intent = new Intent(PostLimitedDownwindTaskActivity.this, AddressReceiveActivity.class);
			intent.putExtras(bundle);
			startActivityForResult(intent, 8);
			break;
		case R.id.iv_senderList:// 从手机通讯录里选取寄件人
			intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
			startActivityForResult(intent, 9);
			break;
		case R.id.iv_receiverList:// 从手机通讯录里选取收件人
			intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
			startActivityForResult(intent, 10);
			break;
//		case R.id.checkbox_baos:// 从手机通讯录里选取收件人
//			checkbox_baos.setOnCheckedChangeListener(new OnCheckedChangeListener() {
//				
//				@Override
//				public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
//					// TODO Auto-generated method stub
//					if(arg1){
//						chox=arg1;
//						edt_prices.setVisibility(View.VISIBLE);
//					   	v_pps.setVisibility(View.VISIBLE);
//					}else {
//						chox=arg1;
//						edt_prices.setVisibility(View.GONE);
//					   	v_pps.setVisibility(View.GONE);
//					}
//				}
//			});
//			break;
			
		default:
			break;
		}
	}
	 private void ceshi(){
		   Intent intent = new Intent(PostLimitedDownwindTaskActivity.this, DownWindPayActivity.class);
		   intent.putExtra("money","22");
//			intent.putExtra("insureCost", String.valueOf(df.format(bean.data.get(0).insureCost)));
			intent.putExtra("insureCost", "0");
			intent.putExtra("billCode", "72795SF20161101");
//			intent.putExtra("type", 3);
			startActivityForResult(intent, 11);
	   }
	/**
	 * 发布按钮提交数据
	 */
	private void addPostResult() {
		JSONObject obj = new JSONObject();
		Log.e("111111et_address-03", "-----");
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
		if (choxde) {
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
		
		
		Log.e("et_add_address", et_add_address.getText().toString()+"-----");
		Log.e("et_address", et_address.getText().toString()+"-----");
		Log.e("city", city+"-----");
		Log.e("cityCode", cityCode+"-----");
		Log.e("receiver_citycode", receiver_citycode+"-----");
		if (et_address.getText().toString().equals("") || et_add_address.getText().toString().equals("")|| cityCode.equals("") || receiver_citycode.equals("")) {
			ToastUtil.shortToast(getApplicationContext(), "请核对你输入的地址信息");
			return;
		}
		try {
			obj.put("userId",
					String.valueOf(PreferencesUtils.getInt(getApplicationContext(), PreferenceConstants.UID)));
			obj.put("latitude", latitude);
			obj.put("longitude", longitude);
			obj.put("cityCode", cityCode);
			obj.put("cityCode", cityCode);
			obj.put("townCode", townCode);
			obj.put("personName", et_name.getText().toString());
			obj.put("mobile", et_tel.getText().toString());
			obj.put("address", et_address.getText().toString()+et_address_specific.getText().toString());
			obj.put("fromLatitude", myLatitude);
			obj.put("fromLongitude", myLongitude);
			obj.put("personNameTo", et_add_name.getText().toString());
			obj.put("mobileTo", et_add_tel.getText().toString());
			obj.put("addressTo", et_add_address.getText().toString()+et_add_address_specific.getText().toString());
//			obj.put("addressTo", et_address.getText().toString());
			obj.put("cityCodeTo", receiver_citycode);
			obj.put("toLatitude", receiverLatitude);
			obj.put("toLongitude", receiverLongitude);
//			obj.put("cityCodeTo", cityCode);
//			obj.put("toLatitude", myLatitude);
//			obj.put("toLongitude", myLongitude);
			obj.put("matName", et_goodname.getText().toString());
			obj.put("matWeight", et_weight.getText().toString());
			obj.put("matImageUrl", icon);
			obj.put("matRemark", et_good_detail.getText().toString());
			obj.put("high", et_height.getText().toString());
			obj.put("wide", et_kuan.getText().toString());
			obj.put("length", et_chang.getText().toString());
			obj.put("limitTime", et_time.getText().toString());
			if(chox){
				obj.put("whether", "Y");
				obj.put("premium", edt_prices.getText().toString().trim());
			}else {
				obj.put("whether", "N");
			}
			obj.put("publshDeviceId", DataTools.getDeviceId(PostLimitedDownwindTaskActivity.this));
		} catch (JSONException e) {
			e.printStackTrace();
		}
		dialog.show();
		Log.e("查看数据", obj.toString());
		AsyncHttpUtils.doPostJson(PostLimitedDownwindTaskActivity.this, MCUrl.DOWNWINDTASKPUBLISHS, obj.toString(),
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
//								AlertDialog.Builder ad = new Builder(PostLimitedDownwindTaskActivity.this);
//								ad.setTitle("温馨提示");
//								ad.setMessage("根据《中华人民共和国反恐怖主义法》规定，发快递须实名制，若您所填信息不真实或不完善，请尽快修改和完善，感谢您的配合。");
//								ad.setNegativeButton("确认", new DialogInterface.OnClickListener() {
//
//									@Override
//									public void onClick(DialogInterface arg0, int arg1) {
//										startActivity(new Intent(PostLimitedDownwindTaskActivity.this,
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
								ToastUtil.shortToast(PostLimitedDownwindTaskActivity.this, bean.getMessage());
								Intent intent = new Intent(PostLimitedDownwindTaskActivity.this, DownWindPayActivity.class);
//								intent.putExtra("money", String.valueOf(df.format(bean.data.get(0).transferMoney)));
								intent.putExtra("money", bean.data.get(0).transferMoney);
								intent.putExtra("insureCost", bean.data.get(0).insureCost);
								intent.putExtra("billCode", bean.data.get(0).billCode);
								Log.e("billCode数据",""+ bean.data.get(0).insureCost);
								startActivityForResult(intent, 11);
//							}

						}else {
							ToastUtil.shortToast(PostLimitedDownwindTaskActivity.this, bean.getMessage());
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
		Log.e("city", city);
		if (!city.contains("市")) {
			city = city + "市";
		}
//		List<AreaBean> selectDataFromDb = new AreaDboperation()
//				.selectDataFromDb("select * from area where area_name='" + city + "'");
		List<CityBean> selectDataFromDb = new CityDbOperation()
				.selectDataFromDb("select * from city where city_name='" + city + "'");
	
		if (receive) {
			
			receiver_citycode = selectDataFromDb.get(0).city_code;
			Log.e("citycode", receiver_citycode);
//				ToastUtil.shortToast(PostLimitedDownwindTaskActivity.this, "area_code "+selectDataFromDb.get(0).area_code);
		} else {
//			ToastUtil.shortToast(PostLimitedDownwindTaskActivity.this, "area_code "+selectDataFromDb.get(0).area_code);
			cityCode = selectDataFromDb.get(0).city_code;
			Log.e("citycode", cityCode);
		}
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
				 if (phone==null) {
						return;
					   }
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
                    new Builder(PostLimitedDownwindTaskActivity.this)
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
				cropPhoto(data.getData());// 裁剪图片
			}
			break;
		// 如果是调用相机拍照时
		case 2:
			if (resultCode == RESULT_OK) {
				File temp = new File(Environment.getExternalStorageDirectory() + "/head.png");
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
			
//		case 5:// 从寄件人列表中选择寄件人
//			if (resultCode == Activity.RESULT_OK) {
//				// receiver_city = data.getStringExtra("city");
//				myLatitude = data.getDoubleExtra("latitude", 0);
//				myLongitude = data.getDoubleExtra("longitude", 0);
//				return_address = data.getStringExtra("location").replace("中国", "");
//				et_address.setText(return_address);
//				detailLocationOfSender = data.getStringExtra("address");
//				et_name.setText(data.getStringExtra("name"));
//				et_tel.setText(data.getStringExtra("phone").replaceAll(" ",""));// 选择，并去掉手机号码中的空格；
//				getCityCode(true, data.getStringExtra("city"));
////				city = data.getStringExtra("city");
//				frist = true;
//			}
//
//			break;
//		case 6:// 从收件人列表中选择收件人
//			if (resultCode == Activity.RESULT_OK) {
//				receiverLatitude = data.getDoubleExtra("latitude", 0.0);
//				receiverLongitude = data.getDoubleExtra("longitude", 0.0);
//				et_add_name.setText(data.getStringExtra("name"));
//				et_add_tel.setText(data.getStringExtra("phone"));
//				et_add_address.setText(data.getStringExtra("location"));
//				detailLocationOfReceiver = data.getStringExtra("address");
//				receiver_citycode = data.getStringExtra("citycode");
//				ToastUtil.longToast(getApplicationContext(), receiverLatitude+"/////"+receiverLongitude);
//			}
//			break;
		case 7:// 选择/修改寄件人的寄件地址
			if (resultCode == RESULT_OK) {
				myLatitude = data.getDoubleExtra("latitude", 0);
				myLongitude = data.getDoubleExtra("longitude", 0);
//				city = data.getStringExtra("city");
//				getCityCode(true, city);
//				et_address.setText(data.getStringExtra("address").replace("中国", ""));
//				Log.e("et_address-03", et_address+"-----");
				return_address = data.getStringExtra("address").replace("中国", "");
				et_address.setText(return_address);
					city = data.getStringExtra("city");
					townaddressd= data.getStringExtra("townaddressd");
//					Log.e("11111district", data.getStringExtra("district"));
					getCityCode(true, city);
//					getCityCode(true, data.getStringExtra("district"));
					
					frist = true;
					return;
			}
			Log.e("et_address-04", et_address+"-----");
			break;
		case 8:// 修改收件人的收货地址
			if (resultCode == RESULT_OK) {
				receiverLatitude = data.getDoubleExtra("latitude", 0);
				receiverLongitude = data.getDoubleExtra("longitude", 0);
				city = data.getStringExtra("city");
				Log.e("1111122address", data.getStringExtra("address"));
				receive = true;
				getCityCode();
//				getCityCode(true, city);
				et_add_address.setText(data.getStringExtra("address").replace("中国", ""));
			}
			break;
		case 9:// 从本地通讯录选取寄件人
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
							.getString(phone.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)).replace(" ", "");
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
                    new Builder(PostLimitedDownwindTaskActivity.this)
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
		case 10:// 从本地通讯录选取收件人
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
				// 获得DATA表中的名字
				String name = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
				// 条件为联系人ID
				et_add_name.setText(name);
				String contactId = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
				// 获得DATA表中的电话号码，条件为联系人ID,因为手机号码可能会有多个
				Cursor phone = reContentResolverol.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
						ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = " + contactId, null, null);
				while (phone.moveToNext()) {
					String phone_number = phone
							.getString(phone.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)).replace(" ", "");
					if (!phone_number.equals("")){
						et_add_tel.setText(phone_number);
					}
				
				}
				if(Build.VERSION.SDK_INT < 14) {
				    cursor.close();
				   }
//				phone.close();
				 }else{  
	                    // 没有权限，跳到设置界面，调用Android系统“应用程序信息（Application Info）”界面  
	                    new Builder(PostLimitedDownwindTaskActivity.this)
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
//				 cursor.close();
			}
			
			break;
		case 11:
			if (resultCode == RESULT_OK) {
				finish();
			}
			break;
		}
	};

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

	/**
	 * 获取城市代码
	 * @param flag
	 * @param city
	 */
	private void getCityCode_(boolean flag, String city) {
		boolean isCopySuccess = CheckDbUtils.checkDb();
//		BDLocation bdLocation=new BDLocation();
//		bdLocation.getPoiList().
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
		if (selectDataFromDb.size() > 0 && !selectDataFromDb.equals("")) {
			if (flag) {
				cityCode = selectDataFromDb.get(0).city_code;
				Log.e("citycode", cityCode);
//				ToastUtil.shortToast(getApplicationContext(), "pro_code"+selectDataFromDb.get(0).pro_code+" city_code"+selectDataFromDb.get(0).city_code
//						+" city_name"+selectDataFromDb.get(0).city_name+" param"+selectDataFromDb.get(0).param);
		
			} else {
//				ToastUtil.shortToast(getApplicationContext(), "111  pro_code"+selectDataFromDb.get(0).pro_code+" city_code"+selectDataFromDb.get(0).city_code
//						+" city_name"+selectDataFromDb.get(0).city_name+" param"+selectDataFromDb.get(0).param);
			
				cityCode = selectDataFromDb.get(0).city_code;
				Log.e("cityCode", cityCode);
			}

		}
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
			Log.e("result", result);
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
		myLatitude=getIntent().getDoubleExtra("latitude", 0.000000);
		myLongitude=getIntent().getDoubleExtra("longitude", 0.000000);
		Log.e("myLatitude", myLatitude+"传来的纬度");
		Log.e("myLongitude", myLongitude+"传来的经度");
		
//		if ("NORMAL_USER".equals(getIntent().getStringExtra("downwind"))) {
//			rl_bannerTip.setVisibility(View.VISIBLE);
//		}
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
		tbv_show.setTitleText("发布限时专递");
		tbv_show.setRightBtnText("声明");
		tbv_show.setRightBtnOnclickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intent=new Intent();
				intent.putExtra("name", "1");
				intent.putExtra("url", "http://www.efamax.com/mobile/InsureSF.html");
				intent.setClass(PostLimitedDownwindTaskActivity.this, HWdvertActivity.class);
				startActivity(intent);
			}
		});
		tbv_show.getRightBtn().setTextColor(getResources().getColor(R.color.white));
		latitude=getIntent().getDoubleExtra("latitude", 0);
		longitude=getIntent().getDoubleExtra("longitude", 0);
		et_address.setFocusable(false);
		et_add_address.setFocusable(false);
		client = new LocationClient(getApplicationContext());
		setLocationParams();
		client.registerLocationListener(new BDLocationListener() {

			@Override
			public void onReceiveLocation(BDLocation arg0) {

				if (arg0 == null) {
					ToastUtil.shortToast(getApplicationContext(), "定位失败，请检查定位设置");
					return;
				} else {
					if (dialog != null)
						dialog.dismiss();
					city = arg0.getCity();
//					latitude = arg0.getLatitude();
//					longitude = arg0.getLongitude();
					Log.e("jpppp", latitude + ":::::::::" + longitude);
					address = arg0.getAddrStr();
					townaddressd=arg0.getDistrict();
					getCityCode(true, city);
//					Log.e("PP----address", address);
//					Log.e("PP----latitude", latitude + "");
//					Log.e("PP----longitude", longitude + "");
					if (!address.equals("")) {
						if (frist) {
							et_address.setText(return_address);
							Log.e("et_address-06", et_address.getText().toString()+"-----");
						} else {
							et_address.setText(address);
							Log.e("et_address-07", et_address.getText().toString()+"-----");
						}
					}
				}
			}
		});
		// 初始化定位
		// 打开GPS
		client.start();
		checkbox_baoss.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
				// TODO Auto-generated method stub
				if(arg1){
					chox=arg1;
					edt_prices.setVisibility(View.VISIBLE);
				   	v_pps.setVisibility(View.VISIBLE);
					showPaywindow();
				}else {
					chox=arg1;
					edt_prices.setVisibility(View.GONE);
				   	v_pps.setVisibility(View.GONE);
				}
				}
//			}
		});
     checkbox_detai.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
				// TODO Auto-generated method stub
				if (arg1) {
					choxde=arg1;
					ll03.setVisibility(View.VISIBLE);
					tex.setVisibility(View.VISIBLE);
				}else {
					choxde=arg1;
					ll03.setVisibility(View.GONE);
					tex.setVisibility(View.GONE);
				}
			}
		});
		getDefaultMessage();
	}

	/**
	 * 获取寄件人默认信息
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
								et_address.setText(bean.data.get(0).areaName.replace("中国", ""));
								detailLocationOfSender = bean.data.get(0).address;
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
		window.showAtLocation(PostLimitedDownwindTaskActivity.this.findViewById(R.id.img_headportrait), Gravity.BOTTOM, 0, 0);

		// 这里检验popWindow里的button是否可以点击
		TextView tv_camera = (TextView) view.findViewById(R.id.tv_camera);
		TextView tv_show = (TextView) view.findViewById(R.id.tv_show);
		tv_show.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				startActivity(new Intent(PostLimitedDownwindTaskActivity.this, IdCardActivity.class).putExtra("iconpath", "goodpath"));
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
						Uri.fromFile(new File(Environment.getExternalStorageDirectory(), "head.png")));
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
				Intent intent1 = new Intent(Intent.ACTION_PICK, null);
				intent1.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
				// intent1.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
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
		intent.putExtra("outputX", 350);
		intent.putExtra("outputY", 350);
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
		inputMethodManager.hideSoftInputFromWindow(PostLimitedDownwindTaskActivity.this.getCurrentFocus().getWindowToken(),
				InputMethodManager.HIDE_NOT_ALWAYS);
		return super.onTouchEvent(event);
	}
	
	
/**
 * 将图片裁成圆角
 * @param bitmap
 * @param roundPx
 * @return
 */
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
	 * 获取城市代码
	 * @param flag
	 * @param city
	 */
	private void getCityCode(boolean flag, String city) {
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
		}// area  area_name  取县的code
//		List<AreaBean> selectDataFromDb = new AreaDboperation()
//				.selectDataFromDb("select * from area where area_name='" + city + "'");
		List<CityBean> selectDataFromDb = new CityDbOperation()
				.selectDataFromDb("select * from city where city_name='" + city + "'");
		
		if (selectDataFromDb.size() > 0 && !selectDataFromDb.equals("")) {
//			ToastUtil.longToast(PostLimitedDownwindTaskActivity.this, "11111area_name  "+selectDataFromDb.get(0).area_code);
			if (flag) {
//				ToastUtil.shortToast(PostLimitedDownwindTaskActivity.this, "area_code "+selectDataFromDb.get(0).area_code);
				cityCode = selectDataFromDb.get(0).city_code;
				Log.e("11111city_codee", selectDataFromDb.get(0).city_code);
				
				List<AreaBean> selectDataFromDbs = new AreaDboperation()
						.selectDataFromDb("select * from area where area_name='" + townaddressd + "'");
				if (selectDataFromDbs.size()>0 && !selectDataFromDbs.equals("")) {
					townCode=selectDataFromDbs.get(0).area_code;
//					Log.e("11111ptownCodee", townCode);
				}
			} else {
//				ToastUtil.shortToast(PostLimitedDownwindTaskActivity.this, "area_code23s "+selectDataFromDb.get(0).area_code);
				receiver_citycode = selectDataFromDb.get(0).city_code;
				Log.e("11111pro_code", receiver_citycode);
			}

		}
//		if (townaddressd.equals("")) {
//			townaddressd = PreferencesUtils.getString(getApplicationContext(), PreferenceConstants.Codedess);
//		}
//		 if (!townaddressd.equals("")) {
//			
//		List<AreaBean> selectDataFromDbs = new AreaDboperation()
//				.selectDataFromDb("select * from area where area_name='" + townaddressd + "'");
//		if (selectDataFromDbs.size()>0 && !selectDataFromDbs.equals("")) {
//			townCode=selectDataFromDbs.get(0).area_code;
////			Log.e("11111ptownCodee", townCode);
//		}
//		 }
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
		window02.showAtLocation(PostLimitedDownwindTaskActivity.this.findViewById(R.id.checkbox_baoss), Gravity.CENTER, 0, 0);
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
