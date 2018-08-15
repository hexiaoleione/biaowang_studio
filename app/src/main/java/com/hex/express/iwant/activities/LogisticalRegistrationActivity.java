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

import com.google.gson.Gson;
import com.hex.express.iwant.R;
import com.hex.express.iwant.bean.BaseBean;
import com.hex.express.iwant.bean.IconBean;
import com.hex.express.iwant.constance.MCUrl;
import com.hex.express.iwant.constance.MsgConstants;
import com.hex.express.iwant.constance.PreferenceConstants;
import com.hex.express.iwant.http.AsyncHttpUtils;
import com.hex.express.iwant.newmain.MainTab;
import com.hex.express.iwant.newsmain.NewMainActivity;
import com.hex.express.iwant.utils.Logger;
import com.hex.express.iwant.utils.PreferencesUtils;
import com.hex.express.iwant.utils.ToastUtil;
import com.loopj.android.http.AsyncHttpResponseHandler;

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


/**
 * 
 * @author huyichuan
 * 物流公司注册2
 *
 */
public class LogisticalRegistrationActivity extends BaseActivity{
	
	@Bind(R.id.idcardz)
	ImageView idcardz;
	@Bind(R.id.idcardf)
	ImageView idcardf;
	@Bind(R.id.idcardr)
	ImageView idcardr;
	@Bind(R.id.comps_emit)
	Button comps_emit;//提交
	@Bind(R.id.btnLe)
	Button btnLe;//返回
	
	String compname,farname,number,addess,icons,idcard;
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
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.companys);
		ButterKnife.bind(this);
		initData();
		initView();
		setOnClick();
		
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
		compname=getIntent().getStringExtra("editText1");
		number=getIntent().getStringExtra("editText3");
		addess=getIntent().getStringExtra("editText4");
		icons=getIntent().getStringExtra("icon");
		idcard=getIntent().getStringExtra("editText5");
		Log.e("111", compname+"  "+farname+"   "+icon);
		
		
	}

	@Override
	public void setOnClick() {
		// TODO Auto-generated method stub
		btnLe.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				finish();
				
			}
		});
		comps_emit.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
//				ToastUtil.shortToast(LogisticalRegistrationActivity.this, "111111");
				addPostResult();
			}
		});
		idcardz.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				showPopwindow();
			}
		});
		idcardf.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				showPopwindowtow();
			}
		});
		idcardr.setOnClickListener(new OnClickListener() {
	
	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		  showPopwindowsend();
			}
		});
		
	}
	@Override
	public void getData() {
		// TODO Auto-generated method stub
	}
	private void addPostResult() {
		JSONObject obj = new JSONObject();
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
		try {
			obj.put("userId",
					String.valueOf(PreferencesUtils.getInt(LogisticalRegistrationActivity.this, PreferenceConstants.UID)));
			obj.put("companyName", compname);//物流公司名称
//			obj.put("bossName", farname);//物流公司法人姓名
			obj.put("bossPhone", number);//物流公司法人联系方式
			obj.put("comLogoUrl", icons);//物流公司照片
			obj.put("address", addess);//物流公司照片
			obj.put("businessLicense", idcard);//身份证
			obj.put("idCardOpenPath", icon);//物流公司地址
			obj.put("idCardObversePath", icon2);//物流公司地址
			obj.put("businessLicensePath", icon3);//物流公司地址
			
//			obj.put("status", "1");//认证状态
//			obj.put("matImageUrl", "http://imgtech.gmw.cn/attachement/jpg/site2/20160910/3820504555972022206.jpg");//司机驾驶证图片url
		} catch (JSONException e) {
			e.printStackTrace();
		}
		Log.e("查看数据", obj.toString());
		dialog.show();
		AsyncHttpUtils.doPostJson(LogisticalRegistrationActivity.this, MCUrl.PERSONCOMOANY, obj.toString(),
				new AsyncHttpResponseHandler() {
					@Override
					public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
						Log.e("11111111111 wwww   ", new String(arg2));
						dialog.dismiss();
						BaseBean bean = new Gson().fromJson(new String(arg2), BaseBean.class);
//						Log.e("oppop", bean.data.toString());
						if (bean.getErrCode()==0) {
							ToastUtil.shortToast(LogisticalRegistrationActivity.this, bean.getMessage());
//							Intent intent = new Intent(LogisticalRegistrationActivity.this, MainTab.class);
							Intent intent = new Intent(LogisticalRegistrationActivity.this, NewMainActivity.class);
							
							intent.putExtra("type", "1");
							startActivity(intent);
						     finish();
						}else {
							ToastUtil.shortToast(LogisticalRegistrationActivity.this, bean.getMessage());
//							Intent intent = new Intent(LogisticalRegistrationActivity.this, MainTab.class);
							Intent intent = new Intent(LogisticalRegistrationActivity.this, NewMainActivity.class);
							intent.putExtra("type", "1");
							startActivity(intent);
						     finish();
						}
						
					}

					@Override
					public void onFailure(int arg0, Header[] arg1, byte[] arg2, Throwable arg3) {
						dialog.dismiss();
					}
				});
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
		window.showAtLocation(LogisticalRegistrationActivity.this.findViewById(R.id.comps_emit), Gravity.BOTTOM,
				0, 0);

		// 这里检验popWindow里的button是否可以点击
		TextView tv_camera = (TextView) view.findViewById(R.id.tv_camera);
		tv_camera.setClickable(true);
		TextView tv_show = (TextView) view.findViewById(R.id.tv_show);
		tv_show.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				startActivity(new Intent(LogisticalRegistrationActivity.this, IdCardActivity.class)
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
						Uri.fromFile(new File(Environment.getExternalStorageDirectory(), "loghead.png")));
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
		window.showAtLocation(LogisticalRegistrationActivity.this.findViewById(R.id.comps_emit), Gravity.BOTTOM,
				0, 0);

		// 这里检验popWindow里的button是否可以点击
		TextView tv_camera = (TextView) view.findViewById(R.id.tv_camera);
		tv_camera.setClickable(true);
		TextView tv_show = (TextView) view.findViewById(R.id.tv_show);
		tv_show.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				startActivity(new Intent(LogisticalRegistrationActivity.this, IdCardActivity.class)
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
						Uri.fromFile(new File(Environment.getExternalStorageDirectory(), "logheads.png")));
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
		window.showAtLocation(LogisticalRegistrationActivity.this.findViewById(R.id.comps_emit), Gravity.BOTTOM,
				0, 0);

		// 这里检验popWindow里的button是否可以点击
		TextView tv_camera = (TextView) view.findViewById(R.id.tv_camera);
		tv_camera.setClickable(true);
		TextView tv_show = (TextView) view.findViewById(R.id.tv_show);
		tv_show.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				startActivity(new Intent(LogisticalRegistrationActivity.this, IdCardActivity.class)
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
						Uri.fromFile(new File(Environment.getExternalStorageDirectory(), "logheade.png")));
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
		switch (requestCode) {
		// 如果是直接从相册获取
		case 1:
			if (resultCode == RESULT_OK) {
				cropPhoto(data.getData());// 裁剪图片
			}
			break;
		// 如果是调用相机拍照时
		case 2:
			if (resultCode == RESULT_OK) {
				File temp = new File(Environment.getExternalStorageDirectory() + "/loghead.png");
				cropPhoto(Uri.fromFile(temp));// 裁剪图片
			}
			break;

		// 取得裁剪后的图片
		case 3:
			Log.e("111111444    ", ""+data);
			if (data != null) {
				Bundle extras = data.getExtras();
				head = extras.getParcelable("data");
				Logger.e("img.1111111"  +head);
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
							File temp = new File(Environment.getExternalStorageDirectory() + "/logheads.png");
							cropPhototow(Uri.fromFile(temp));// 裁剪图片
						}
						break;

					// 取得裁剪后的图片
					case 33:
						Log.e("1111112222    ", ""+data);
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
									File temp = new File(Environment.getExternalStorageDirectory() + "/logheadse.png");
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
		default:
			break;
		}
		super.onActivityResult(requestCode, resultCode, data);
	};

	@Override
	public void handleBackgroundMessage(Message msg) {
		super.handleBackgroundMessage(msg);
		switch (msg.what) {
		case MsgConstants.MSG_01:
			try {
				result = post(MCUrl.LOGISCOMPANIMGFIRSTOpen, map, map_file);
				sendEmptyUiMessage(MsgConstants.MSG_01);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			break;
		case MsgConstants.MSG_02:
			try {
				result2 = post(MCUrl.LOGISCOMPANIMGSECONDOpens, map, map_file);
				sendEmptyUiMessage(MsgConstants.MSG_02);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			break;
		case MsgConstants.MSG_03:
			try {
				result3 = post(MCUrl.LOGISCOMPANIMGTHIRDLice, map, map_file);
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
			if (bean.getErrCode()==0) {
			if (bean.data.size() != 0) {
				flag1=true;
				Log.e("filePath", bean.data.get(0).filePath);
				ToastUtil.shortToast(LogisticalRegistrationActivity.this, "第一张上传成功");
				icon = bean.data.get(0).filePath;
//				PreferencesUtils.putString(getApplicationContext(), PreferenceConstants.ICON_PATH, icon);
				Log.e("11111touxiang", icon);
				idcardz.setBackgroundDrawable(null);
				idcardz.setImageBitmap(head);
			}
			}else if (bean.getErrCode()==-2) {
				
				ToastUtil.shortToast(LogisticalRegistrationActivity.this, "您的信息正在审核,请勿重复上传");	
				}else if (bean.getErrCode()==-3) {
					ToastUtil.shortToast(LogisticalRegistrationActivity.this, "您的信息已经审核通过,请勿重复上传");	
				}
			break;
		case MsgConstants.MSG_02:
			Log.e("11111222result", result2);
			IconBean bean1 = new Gson().fromJson(result2, IconBean.class);
			Log.e("11111222result", ""+bean1.data.size());
			if (bean1.getErrCode()==0) {
			if (bean1.data.size() != 0) {
				flag2=true;
				Log.e("filePath", bean1.data.get(0).filePath);
				ToastUtil.shortToast(LogisticalRegistrationActivity.this, "第二张上传成功");
				icon2 = bean1.data.get(0).filePath;
//				PreferencesUtils.putString(getApplicationContext(), PreferenceConstants.ICON_PATH, icon);
				Log.e("111112222touxiang", icon2);
				idcardf.setBackgroundDrawable(null);
				idcardf.setImageBitmap(head);
			}
		}else if (bean1.getErrCode()==-2) {
			
			ToastUtil.shortToast(LogisticalRegistrationActivity.this, "您的信息正在审核,请勿重复上传");	
			}else if (bean1.getErrCode()==-3) {
				ToastUtil.shortToast(LogisticalRegistrationActivity.this, "您的信息已经审核通过,请勿重复上传");	
			}
			break;
		case MsgConstants.MSG_03:
			Log.e("11111333result", result3);
			IconBean bean11 = new Gson().fromJson(result3, IconBean.class);
			if (bean11.getErrCode()==0) {
			if (bean11.data.size() != 0) {
				flag3=true;
				Log.e("filePath", bean11.data.get(0).filePath);
				ToastUtil.shortToast(LogisticalRegistrationActivity.this, "第三张上传成功");
				icon3 = bean11.data.get(0).filePath;
//				PreferencesUtils.putString(getApplicationContext(), PreferenceConstants.ICON_PATH, icon);
				Log.e("11111333touxiang", icon3);
				idcardr.setBackgroundDrawable(null);
				idcardr.setImageBitmap(head);
			}
	}else if (bean11.getErrCode()==-2) {
		
		ToastUtil.shortToast(LogisticalRegistrationActivity.this, "您的信息正在审核,请勿重复上传");	
		}else if (bean11.getErrCode()==-3) {
			ToastUtil.shortToast(LogisticalRegistrationActivity.this, "您的信息已经审核通过,请勿重复上传");	
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
		fileName = path + "loghead.png";
		
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
		fileNamet = path + "logheads.png";
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
		fileNames = path + "logheadse.png";
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
