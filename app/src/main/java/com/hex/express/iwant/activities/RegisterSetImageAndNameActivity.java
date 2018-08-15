package com.hex.express.iwant.activities;

import java.io.ByteArrayOutputStream;
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
import java.util.List;
import java.util.Map;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import android.R.bool;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
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
import android.os.Looper;
import android.os.Message;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.TextView;
import butterknife.Bind;
import butterknife.ButterKnife;

import com.google.gson.Gson;
import com.hex.express.iwant.R;
import com.hex.express.iwant.bean.IconBean;
import com.hex.express.iwant.bean.RegisterBean;
import com.hex.express.iwant.constance.MCUrl;
import com.hex.express.iwant.constance.MsgConstants;
import com.hex.express.iwant.constance.PreferenceConstants;
import com.hex.express.iwant.http.AsyncHttpUtils;
import com.hex.express.iwant.newmain.MainTab;
import com.hex.express.iwant.newsmain.NewMainActivity;
import com.hex.express.iwant.utils.IDUtils;
import com.hex.express.iwant.utils.Logger;
import com.hex.express.iwant.utils.PreferencesUtils;
import com.hex.express.iwant.utils.StringUtil;
import com.hex.express.iwant.utils.ToastUtil;
import com.hex.express.iwant.views.RoundCornerImageView;
import com.hex.express.iwant.views.TitleBarView;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.squareup.picasso.Picasso;

/**
 * 注册3/3完成
 * 
 * @author Eric
 * 
 */
public class RegisterSetImageAndNameActivity extends BaseActivity {
	private String userName;
	private String smsCode;
	private String pwd;
//	private TitleBarView tbv_show;
	private RoundCornerImageView img_headportrait;
	private EditText edt_name;
	private Button btn_complete;
	private Bitmap head;
	private String fileName;
	private static String path = "/sdcard/head/";// sd路径
	private Map map = new HashMap<String, String>();
	private Map map_file = new HashMap<String, File>();
	private String result;
	private String icon = "";
	@Bind(R.id.et_card)
	EditText et_card;
	@Bind(R.id.et_name)
	EditText et_name;
	@Bind(R.id.btnLeft)
	ImageView btnLeft;
	@Bind(R.id.btnRight)
	TextView btnRight;
	private String idcard;
	private String tiaoguoString;
	private boolean uyt;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_registerimgandname);
		ButterKnife.bind(this);
		getData();
		initView();
		initData();
		setOnClick();
	}

	@Override
	public void onWeightClick(View v) {
		switch (v.getId()) {
		case R.id.btn_complete:// 下一步=
			Log.e("obj", et_card.getText().toString());
			if (PreferencesUtils.getString(getApplicationContext(), PreferenceConstants.REALMANAUTH).equals("Y")) {
				int id = PreferencesUtils.getInt(getApplicationContext(), PreferenceConstants.UID);
				JSONObject obj = new JSONObject();
				try {
					
					obj.put("userId", id);
					obj.put("userName", et_name.getText().toString());
					obj.put("idCard", idcard);
					obj.put("idCardPath", PreferencesUtils.getString(getApplicationContext(), PreferenceConstants.ICON_PATH));
					Log.e("address", MCUrl.IMPROVE_INFO + id);
				} catch (JSONException e) {
					e.printStackTrace();
					List list = new ArrayList<String>();
				}
				dialog.show();
				Log.e("obj", obj.toString());
//				AsyncHttpUtils.doPut(getApplicationContext(), MCUrl.IMPROVE_INFO + "/" + id, obj.toString(), null,
//						new AsyncHttpResponseHandler() {
				AsyncHttpUtils.doPut(getApplicationContext(), MCUrl.IMPROVE_IN, obj.toString(), null,
						new AsyncHttpResponseHandler() {
							@Override
							public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
								Log.e("json", new String(arg2));
								dialog.dismiss();
								RegisterBean bean = new Gson().fromJson(new String(arg2), RegisterBean.class);
								if (bean.getErrCode() == 0) {
									PreferencesUtils.putBoolean(getApplicationContext(),
											PreferenceConstants.COMPLETE, bean.getData().get(0).completed);
									PreferencesUtils.putString(getApplicationContext(),
											PreferenceConstants.PASSWORD, bean.getData().get(0).userCode);
									PreferencesUtils.putString(getApplicationContext(), PreferenceConstants.IDCARD,
											bean.getData().get(0).idCard);
									PreferencesUtils.putString(getApplicationContext(),
											PreferenceConstants.ICON_PATH, bean.getData().get(0).idCardPath);
									PreferencesUtils.putString(getApplicationContext(),
											PreferenceConstants.REALMANAUTH, bean.getData().get(0).realManAuth);
									PreferencesUtils.putString(getApplicationContext(),
											PreferenceConstants.USERNAME, bean.getData().get(0).userName);
									ToastUtil.shortToast(RegisterSetImageAndNameActivity.this, bean.getMessage());
									Intent intent = new Intent(RegisterSetImageAndNameActivity.this, NewMainActivity.class);
//									Intent intent = new Intent(RegisterSetImageAndNameActivity.this, MainTab.class);
//									Intent intent = new Intent(RegisterSetImageAndNameActivity.this, MainActivity.class);
									intent.putExtra("type", "1");
									startActivity(intent);
										finish();
								} else {
									Intent intent = new Intent(RegisterSetImageAndNameActivity.this, NewMainActivity.class);
									intent.putExtra("type", "1");
									startActivity(intent);
										finish();
									ToastUtil.shortToast(RegisterSetImageAndNameActivity.this, bean.getMessage());
								}

							}

							@Override
							public void onFailure(int arg0, Header[] arg1, byte[] arg2, Throwable arg3) {
								fileDelete(path + "head.png");
								dialog.dismiss();
							}
						});
			}else {
	
			int a = et_card.getText().toString().indexOf("*****");
			if (a >= 0) {
				idcard = PreferencesUtils.getString(getApplicationContext(), PreferenceConstants.IDCARD);
			} else {
				idcard = et_card.getText().toString();
			}
			if (!uyt) {
				ToastUtil.shortToast(getApplicationContext(), "请上传照片");
				return;
			}
			if (!et_name.getText().toString().equals("") && !et_name.getText().toString().equals("null")) {
				if (IDUtils.IDCardValidate(idcard.toUpperCase()).equals("true")) {
					int id = PreferencesUtils.getInt(getApplicationContext(), PreferenceConstants.UID);
					JSONObject obj = new JSONObject();
					try {
						
						obj.put("userId", id);
						obj.put("userName", et_name.getText().toString());
						obj.put("idCard", idcard);
						obj.put("idCardPath", icon);
						Log.e("address", MCUrl.IMPROVE_INFO + id);
					} catch (JSONException e) {
						e.printStackTrace();
						List list = new ArrayList<String>();
					}
					dialog.show();
					Log.e("obj", obj.toString());
//					AsyncHttpUtils.doPut(getApplicationContext(), MCUrl.IMPROVE_INFO + "/" + id, obj.toString(), null,
//							new AsyncHttpResponseHandler() {
					AsyncHttpUtils.doPut(getApplicationContext(), MCUrl.IMPROVE_IN, obj.toString(), null,
							new AsyncHttpResponseHandler() {
								@Override
								public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
									Log.e("json", new String(arg2));
									dialog.dismiss();
									RegisterBean bean = new Gson().fromJson(new String(arg2), RegisterBean.class);
									if (bean.getErrCode() == 0) {
										PreferencesUtils.putBoolean(getApplicationContext(),
												PreferenceConstants.COMPLETE, bean.getData().get(0).completed);
										PreferencesUtils.putString(getApplicationContext(),
												PreferenceConstants.PASSWORD, bean.getData().get(0).userCode);
										PreferencesUtils.putString(getApplicationContext(), PreferenceConstants.IDCARD,
												bean.getData().get(0).idCard);
										PreferencesUtils.putString(getApplicationContext(),
												PreferenceConstants.ICON_PATH, bean.getData().get(0).idCardPath);
										PreferencesUtils.putString(getApplicationContext(),
												PreferenceConstants.REALMANAUTH, bean.getData().get(0).realManAuth);
										PreferencesUtils.putString(getApplicationContext(),
												PreferenceConstants.USERNAME, bean.getData().get(0).userName);
										ToastUtil.shortToast(RegisterSetImageAndNameActivity.this, bean.getMessage());
										Intent intent = new Intent(RegisterSetImageAndNameActivity.this, NewMainActivity.class);
//										Intent intent = new Intent(RegisterSetImageAndNameActivity.this, MainTab.class);
//										Intent intent = new Intent(RegisterSetImageAndNameActivity.this, MainActivity.class);
										intent.putExtra("type", "1");
										startActivity(intent);
											finish();
									} else {
//										if (bean.getErrCode() == -3) {// 两次机会都用完了
//										AlertDialog.Builder ad = new Builder(RegisterSetImageAndNameActivity.this);
//										ad.setTitle("温馨提示");
//										ad.setMessage(bean.getMessage());
//										ad.setPositiveButton("确认", new DialogInterface.OnClickListener() {
//											public void onClick(DialogInterface dialog, int which) {
////												startActivity(new Intent(RegisterSetImageAndNameActivity.this,
////														MainActivity.class));
//												Intent intent = new Intent(RegisterSetImageAndNameActivity.this, MainActivity.class);
//												intent.putExtra("type", "1");
//												startActivity(intent);
//												dialog.dismiss();
//											}
//										});
//										ad.create().show();
//									} else if (bean.getErrCode() == -4) {// 还有一次修改的机会提示
										ToastUtil.shortToast(RegisterSetImageAndNameActivity.this, bean.getMessage());
//										Intent intent = new Intent(RegisterSetImageAndNameActivity.this, NewMainActivity.class);
//										Intent intent = new Intent(RegisterSetImageAndNameActivity.this, MainTab.class);
//										Intent intent = new Intent(RegisterSetImageAndNameActivity.this, MainActivity.class);
//										intent.putExtra("type", "1");
//										startActivity(intent);
//										finish();
//										PreferencesUtils.putBoolean(getApplicationContext(),
//												PreferenceConstants.COMPLETE, bean.getData().get(0).completed);
//										PreferencesUtils.putString(getApplicationContext(),
//												PreferenceConstants.PASSWORD, bean.getData().get(0).userCode);
//										PreferencesUtils.putString(getApplicationContext(), PreferenceConstants.IDCARD,
//												bean.getData().get(0).idCard);
//										PreferencesUtils.putString(getApplicationContext(),
//												PreferenceConstants.ICON_PATH, bean.getData().get(0).idCardPath);
//										PreferencesUtils.putString(getApplicationContext(),
//												PreferenceConstants.REALMANAUTH, bean.getData().get(0).realManAuth);
//										PreferencesUtils.putString(getApplicationContext(),
//												PreferenceConstants.USERNAME, bean.getData().get(0).userName);
//										AlertDialog.Builder ad = new Builder(RegisterSetImageAndNameActivity.this);
//										ad.setTitle("温馨提示");
//										ad.setMessage(bean.getMessage());
//										ad.setPositiveButton("确认", new DialogInterface.OnClickListener() {
//											public void onClick(DialogInterface dialog, int which) {
////												startActivity(new Intent(RegisterSetImageAndNameActivity.this,
////														MainActivity.class));
//												Intent intent = new Intent(RegisterSetImageAndNameActivity.this, MainActivity.class);
//												intent.putExtra("type", "1");
//												startActivity(intent);
//												finish();
//
//											}
//										});
//										ad.create().show();
									}

								}

								@Override
								public void onFailure(int arg0, Header[] arg1, byte[] arg2, Throwable arg3) {
									fileDelete(path + "head.png");
									dialog.dismiss();
								}
							});
				} else {
					ToastUtil.shortToast(getApplicationContext(), "请输入正确的身份证号");
				}

			} else {
				ToastUtil.shortToast(getApplicationContext(), "请输入姓名");
			}
			
			}
			break;
		case R.id.img_headportrait:// 上传头像
			if (PreferencesUtils.getString(getApplicationContext(), PreferenceConstants.REALMANAUTH).equals("Y")) {
				
			}else {
				showPopwindow();// 显示pupwindown
			}
			
			break;
		}
	}

	@Override
	public void initView() {
		// 标题
//		tbv_show = (TitleBarView) findViewById(R.id.tbv_show);
		// this.tbv_show.setRightBtnText(R.string.jump);
		// 头像
		img_headportrait = (RoundCornerImageView) findViewById(R.id.img_headportrait);
		// 完成按钮
		btn_complete = (Button) findViewById(R.id.btn_complete);
	}

	@Override
	public void initData() {
//		this.tbv_show.setTitleText(R.string.next3);
		tiaoguoString = getIntent().getStringExtra("tiaoguo");
		if (tiaoguoString.equals("1")) {
			btnRight.setText("跳过");
		}
//			tbv_show.getRightBtn().setVisibility(View.VISIBLE);
//			tbv_show.getRightBtn().setText("跳过");
//		} else {
//			tbv_show.getRightBtn().setVisibility(View.GONE);
//		}
//
//		tbv_show.getRightBtn().setTextColor(getResources().getColor(R.color.white));
//		if (PreferencesUtils.getString(getApplicationContext(), PreferenceConstants.IDCARD) != null
//				&& !PreferencesUtils.getString(getApplicationContext(), PreferenceConstants.IDCARD).equals("")) {
		try {
			// 从服务器端获取到的身份证号；
			if (PreferencesUtils.getString(getApplicationContext(), PreferenceConstants.REALMANAUTH).equals("Y")
				) {
				et_card.setText(""+PreferencesUtils.getString(getApplicationContext(), PreferenceConstants.IDCARD));
				et_name.setText(""+PreferencesUtils.getString(getApplicationContext(), PreferenceConstants.USERNAME));
				et_card.setFocusable(false);
				et_name.setFocusable(false);
//				btn_complete.setVisibility(View.GONE);
//				img_headportrait.setClickable(false);
//				btn_complete.setBackgroundColor(R.color.grayviews);//灰色要加的
			} else {
//				btn_complete.setVisibility(View.VISIBLE);
//				img_headportrait.setClickable(true);
				et_card.setFocusable(true);
				et_name.setFocusable(true);
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
			if (PreferencesUtils.getString(getApplicationContext(), PreferenceConstants.ICON_PATH) != null
					&& !PreferencesUtils.getString(getApplicationContext(), PreferenceConstants.ICON_PATH).equals("")) {
//			loader.displayImage(PreferencesUtils.getString(getApplicationContext(), PreferenceConstants.ICON_PATH),img_headportrait, options);
//			loader.displayImage(PreferencesUtils.getString(getApplicationContext(), PreferenceConstants.HEARDER_PATH), img_headportrait,options);
			Log.e("11111", ""+PreferencesUtils.getString(getApplicationContext(), PreferenceConstants.ICON_PATH));
				loader.displayImage(PreferencesUtils.getString(getApplicationContext(), PreferenceConstants.ICON_PATH), img_headportrait);
			img_headportrait.setBackgroundDrawable(null);
			}else {
				img_headportrait.setBackgroundResource(R.drawable.ind_idcar);
			}
//		}
		if (PreferencesUtils.getString(getApplicationContext(), PreferenceConstants.USERNAME) != null
				&& !PreferencesUtils.getString(getApplicationContext(), PreferenceConstants.USERNAME).equals("")) {
			et_name.setText(PreferencesUtils.getString(getApplicationContext(), PreferenceConstants.USERNAME));
		}
		if (PreferencesUtils.getString(getApplicationContext(), PreferenceConstants.IDCARD) != null
				&& !PreferencesUtils.getString(getApplicationContext(), PreferenceConstants.IDCARD).equals("")) {
			String ID_NUMBER = PreferencesUtils.getString(getApplicationContext(), PreferenceConstants.IDCARD);
			Log.e("ID______", PreferencesUtils.getString(getApplicationContext(), PreferenceConstants.IDCARD));

			CharSequence firt4no_ofID = ID_NUMBER.subSequence(0, 4);// 截取身份证号前四位；
			CharSequence last4no_ofID = ID_NUMBER.subSequence(14, 18);// 截取身份证号后四位；
			// 设置身份证号第5位到第14位为密文*；
			et_card.setText(firt4no_ofID + "**********" + last4no_ofID);
		}
	}

	@Override
	public void setOnClick() {
		img_headportrait.setOnClickListener(this);
		btn_complete.setOnClickListener(this);
		btnLeft.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				 if (tiaoguoString.equals("1")) {
				 ToastUtil.shortToast(RegisterSetImageAndNameActivity.this,
				 "注册完成");
				 }
				 Intent intent = new Intent(RegisterSetImageAndNameActivity.this, NewMainActivity.class);
//				Intent intent = new Intent(RegisterSetImageAndNameActivity.this, MainTab.class);
//				Intent intent = new Intent(RegisterSetImageAndNameActivity.this, MainActivity.class);
				intent.putExtra("isLogin", true);
				startActivity(intent);
			}
		});
		//修改手机号
		btnRight.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
//				Intent intent = new Intent(RegisterSetImageAndNameActivity.this, RevisePhoneActivity.class);
				Intent intent = new Intent(RegisterSetImageAndNameActivity.this, RevisepersonActivity.class);
//				intent.putExtra("isLogin", true);
				startActivity(intent);
				 if (tiaoguoString.equals("1")) {
					 ToastUtil.shortToast(RegisterSetImageAndNameActivity.this, "注册完成");
//						Intent intents = new Intent(RegisterSetImageAndNameActivity.this, MainActivity.class);
//					 Intent intents = new Intent(RegisterSetImageAndNameActivity.this, MainTab.class);
					 Intent intents = new Intent(RegisterSetImageAndNameActivity.this, NewMainActivity.class);
						intents.putExtra("isLogin", true);
						startActivity(intents);
						finish();
					 }
				
			}
		});
		
		// 点击跳转就回到主界面
//		this.tbv_show.setLeftBtnOnclickListener(new OnClickListener() {
//			@Override
//			public void onClick(View v) {
//				// if (tiaoguoString.equals("1")) {
//				// ToastUtil.shortToast(RegisterSetImageAndNameActivity.this,
//				// "注册完成");
//				// }
//				Intent intent = new Intent(RegisterSetImageAndNameActivity.this, MainActivity.class);
//				intent.putExtra("isLogin", true);
//				startActivity(intent);
//			}
//		});
//		tbv_show.setRightBtnOnclickListener(new OnClickListener() {
//			@Override
//			public void onClick(View arg0) {
//				ToastUtil.shortToast(RegisterSetImageAndNameActivity.this, "注册完成");
//				Intent intent = new Intent(RegisterSetImageAndNameActivity.this, MainActivity.class);
//				intent.putExtra("isLogin", true);
//				startActivity(intent);
//			}
//		});
	}

	@Override
	public void getData() {
		Intent intent = getIntent();
		userName = intent.getStringExtra("userName");
		smsCode = intent.getStringExtra("smsCode");
		pwd = intent.getStringExtra("pwd");
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
		window.showAtLocation(RegisterSetImageAndNameActivity.this.findViewById(R.id.img_headportrait), Gravity.BOTTOM,
				0, 0);

		// 这里检验popWindow里的button是否可以点击
		TextView tv_camera = (TextView) view.findViewById(R.id.tv_camera);
		tv_camera.setClickable(true);
		TextView tv_show = (TextView) view.findViewById(R.id.tv_show);
		tv_show.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				startActivity(new Intent(RegisterSetImageAndNameActivity.this, IdCardActivity.class)
						.putExtra("iconpath", "goodpath"));
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
						Uri.fromFile(new File(Environment.getExternalStorageDirectory(), "head.png")));
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
				File temp = new File(Environment.getExternalStorageDirectory() + "/head.png");
				cropPhoto(Uri.fromFile(temp));// 裁剪图片
			}
			break;

		// 取得裁剪后的图片
		case 3:
//			if (resultCode == RESULT_OK) {
//			Log.e("1111tututuutut  ", ""+resultCode);
////			}
			if (data != null) {
				Bundle extras = data.getExtras();
				head = extras.getParcelable("data");
				Logger.e("img.1111111"  +head);
				if (head != null) {
					/**
					 * 上传服务器代码 //TODO 实现头衔上传
					 */
					Logger.e("img.1111111");
					setPicToView(head);
					map.put("userId",
							String.valueOf(PreferencesUtils.getInt(getApplicationContext(), PreferenceConstants.UID)));
					map.put("fileName", "idCard_HEADPORTRAIT");
					map_file.put("file", new File(fileName));
					// String result="";
					sendEmptyBackgroundMessage(MsgConstants.MSG_01);
					img_headportrait.setBackgroundDrawable(null);// 将原背景图片置空，避免与新图片叠加显示，尤其是在上面图层圆角或者有透明度时；
					img_headportrait.setImageBitmap(head);
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
	@Override
	public void handleBackgroundMessage(Message msg) {
		super.handleBackgroundMessage(msg);
		switch (msg.what) {
		case MsgConstants.MSG_01:
			try {
				// IDCDHEAD  UPLOAD_ICON
				result = post(MCUrl.UPLOAD_ICON, map, map_file);
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
				uyt=true;
				Log.e("filePath", bean.data.get(0).filePath);
				ToastUtil.shortToast(RegisterSetImageAndNameActivity.this, "上传成功");
				icon = bean.data.get(0).filePath;
				PreferencesUtils.putString(getApplicationContext(), PreferenceConstants.ICON_PATH, icon);
//				loader.displayImage(icon, img_headportrait, options);
//				img_headportrait.setBackgroundDrawable(null);// 将原背景图片置空，避免与新图片叠加显示，尤其是在上面图层圆角或者有透明度时；
//				img_headportrait.setImageBitmap(head);
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
		fileName = path + "head.png";
		try {
			b = new FileOutputStream(fileName);
			mBitmap.compress(Bitmap.CompressFormat.PNG, 100, b);// 把数据写入文件
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} finally {
			try {
		      if(b != null) 
	          b.close();
				// 关闭流
//				b.close();	
//				b.flush();
//				b.close();
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
