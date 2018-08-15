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
import com.hex.express.iwant.iWantApplication;
import com.hex.express.iwant.bean.BaseBean;
import com.hex.express.iwant.bean.IconBean;
import com.hex.express.iwant.constance.MCUrl;
import com.hex.express.iwant.constance.MsgConstants;
import com.hex.express.iwant.constance.PreferenceConstants;
import com.hex.express.iwant.http.AsyncHttpUtils;
import com.hex.express.iwant.utils.Logger;
import com.hex.express.iwant.utils.PreferencesUtils;
import com.hex.express.iwant.utils.StringUtil;
import com.hex.express.iwant.utils.ToastUtil;
import com.hex.express.iwant.views.RoundCornerImageView;
import com.hex.express.iwant.views.TitleBarView;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import android.app.Activity;
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
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
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
 *  物流个人认证
 */
public class PersonCerActivity extends BaseActivity{
	
	@Bind(R.id.btnLefts)
	Button btnLefts;
	@Bind(R.id.imageViews)
	ImageView imageView1;
	@Bind(R.id.edt_cars)
	EditText edt_cars;//车辆信息
	@Bind(R.id.edt_car_phone)//车牌号
	EditText edt_car_phone;
	@Bind(R.id.edt_drive)//驾驶证号
	EditText edt_drive;
	@Bind(R.id.edt_cyz)//从业资格证
	EditText edt_cyz;
	@Bind(R.id.sumit)
	Button sumit;
	View rootView;
	
	private TitleBarView tbv_show;
	private RoundCornerImageView img_headportrait;
	private TextView btn_complete;
	private Bitmap head;
	private String fileName = path + "peronhead.png";
	private static String path = "/sdcard/myHead/";// sd路径
	private Map map = new HashMap<String, String>();
	private Map map_file = new HashMap<String, File>();
	private String result;
	private String icon="";
	private boolean flag;
	private boolean frist = false;
	private IconBean bean;
	public ImageLoader loader;
	public DisplayImageOptions options;
	String idCardPath,checkName,checkIdCard;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fragment_person);
		iWantApplication.getInstance().addActivity(this);
		ButterKnife.bind(PersonCerActivity.this);
		getData();
		initView();
		initData();
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
		
		Log.e("idCardPath  ", getIntent().getStringExtra("icon"));//照片
		Log.e("checkName   ", getIntent().getStringExtra("et_name"));//姓名
		Log.e("checkIdCard   ", getIntent().getStringExtra("idcard"));//身份证
		idCardPath=getIntent().getStringExtra("icon");
		checkName=getIntent().getStringExtra("et_name");
		checkIdCard=getIntent().getStringExtra("idcard");
	}

	@Override
	public void setOnClick() {
		btnLefts.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				finish();
			}
		});
		// TODO Auto-generated method stub
		sumit.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
//				ToastUtil.shortToast(PersonCerActivity.this, "111111111");
				addPostResult();
			}
		});
		
		imageView1.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				showPopwindow();// 显示pupwindown
//				pupwindown();
				
			}
		});
	}

	@Override
	public void getData() {
		// TODO Auto-generated method stub
		
	}
	private void addPostResult() {
		JSONObject obj = new JSONObject();
		if(edt_cars.getText().toString().trim().equals("") 
				||edt_car_phone.getText().toString().trim().equals("")
				 || edt_drive.getText().toString().trim().equals("")
				 ||	edt_cyz.getText().toString().trim().equals("")){
			ToastUtil.shortToast(PersonCerActivity.this, "请填写完信息");
		}else {
			
		
		if (!StringUtil.isCarNumber(edt_car_phone.getText().toString())) {
			ToastUtil.shortToast(PersonCerActivity.this, "车牌号不正确");
		}else {
			
		
		try {
			obj.put("userId",
					String.valueOf(PreferencesUtils.getInt(PersonCerActivity.this, PreferenceConstants.UID)));
			obj.put("carType", edt_cars.getText().toString());//车辆类型
			obj.put("CarNumber", edt_car_phone.getText().toString());//车牌号
			obj.put("DriverNumber", edt_drive.getText().toString());//驾驶证号
			obj.put("quaCertificate", edt_cyz.getText().toString());//从业资格证
			obj.put("idCardPath", idCardPath);//照片
			obj.put("checkName", checkName);//姓名
			obj.put("checkIdCard", checkIdCard);//身份证
			obj.put("matImageUrl", icon);//司机驾驶证图片url
		} catch (JSONException e) {
			e.printStackTrace();
		}
		dialog.show();
		Log.e("查看数据", obj.toString());
		AsyncHttpUtils.doPostJson(PersonCerActivity.this, MCUrl.PERSON, obj.toString(),
				new AsyncHttpResponseHandler() {

					@Override
					public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
						Log.e("11111111111 wwww   ", new String(arg2));
						dialog.dismiss();
						BaseBean bean = new Gson().fromJson(new String(arg2), BaseBean.class);
						if (bean.getErrCode()==0) {
							ToastUtil.shortToast(PersonCerActivity.this,  bean.getMessage());
							
						}else {
							ToastUtil.shortToast(PersonCerActivity.this,  bean.getMessage());
						}
						
						Log.e("1111111 ", bean.getMessage());

					}

					@Override
					public void onFailure(int arg0, Header[] arg1, byte[] arg2, Throwable arg3) {
						dialog.dismiss();
					}
				});
		}
		}
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
		window.showAtLocation(PersonCerActivity.this.findViewById(R.id.imageViews), Gravity.BOTTOM, 0, 0);

		// 这里检验popWindow里的button是否可以点击
		TextView tv_camera = (TextView) view.findViewById(R.id.tv_camera);
		TextView tv_show = (TextView) view.findViewById(R.id.tv_show);
		tv_show.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				startActivity(new Intent(PersonCerActivity.this, IdCardActivity.class));
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
						Uri.fromFile(new File(Environment.getExternalStorageDirectory(), "peronhead.png")));
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
				File temp = new File(Environment.getExternalStorageDirectory() + "/peronhead.png");
				cropPhoto(Uri.fromFile(temp));// 裁剪图片
			}
			break;

		// 取得裁剪后的图片
		case 3:
//			if (resultCode == RESULT_OK) {
			Log.e("1111tututuutut  ", ""+data);
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
					map.put("fileName", "cardId");
					map_file.put("file", new File(fileName));
					// String result="";
					
					sendEmptyBackgroundMessage(MsgConstants.MSG_01);
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
		intent.putExtra("aspectX", 1);
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
				result = post(MCUrl.DREVER, map, map_file);
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
				Log.e("11111111filePath", bean.data.get(0).filePath);
				ToastUtil.shortToast(PersonCerActivity.this, "上传成功");
				icon = bean.data.get(0).filePath;
//				PreferencesUtils.putString(getApplicationContext(), PreferenceConstants.ICON_PATH, icon);
				imageView1.setBackgroundDrawable(null);
				imageView1.setImageBitmap(head);
//				loader.displayImage(icon, imageView1, options);
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

	// 点击空白区域关闭输入法
//		public boolean onTouchEvent(MotionEvent event) {
//			InputMethodManager inputMethodManager = (InputMethodManager)getActivity(). getSystemService(Context.INPUT_METHOD_SERVICE);
//			inputMethodManager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(),
//					InputMethodManager.HIDE_NOT_ALWAYS);
//			return super.onTouchEvent(event);
//		}
}

