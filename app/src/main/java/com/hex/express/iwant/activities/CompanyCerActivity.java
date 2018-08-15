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

import com.google.gson.Gson;
import com.hex.express.iwant.R;
import com.hex.express.iwant.iWantApplication;
import com.hex.express.iwant.bean.IconBean;
import com.hex.express.iwant.constance.MCUrl;
import com.hex.express.iwant.constance.MsgConstants;
import com.hex.express.iwant.constance.PreferenceConstants;
import com.hex.express.iwant.utils.IDUtils;
import com.hex.express.iwant.utils.Logger;
import com.hex.express.iwant.utils.PreferencesUtils;
import com.hex.express.iwant.utils.StringUtil;
import com.hex.express.iwant.utils.ToastUtil;
import com.hex.express.iwant.views.RoundCornerImageView;
import com.hex.express.iwant.views.TitleBarView;

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
 *  物流认证公司
 */
public class CompanyCerActivity extends BaseActivity{
	@Bind(R.id.btnLefts)
	Button btnLefts;
		@Bind(R.id.com_img)//上传照片
	ImageView com_img;
	@Bind(R.id.editText1)//公司名称
	EditText editText1;
//	@Bind(R.id.editText2)//法人姓名
//	EditText editText2;
	@Bind(R.id.editText3)//联系方式
	EditText editText3;
	@Bind(R.id.editText4)//地址
	EditText editText4;
	@Bind(R.id.editText5)//地址
	EditText editText5;
	@Bind(R.id.com_next)//下一步
	TextView com_next;
	
	private TitleBarView tbv_show;
	private RoundCornerImageView img_headportrait;
	private TextView btn_complete;
	private Bitmap head;
	private String fileName = path + "companynes.png";
	private static String path = "/sdcard/myHead/";// sd路径
	private Map map = new HashMap<String, String>();
	private Map map_file = new HashMap<String, File>();
	private String result;
	private String icon="";
	private boolean flag;
	private boolean frist = false;
	private IconBean bean;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fragment_company);
		iWantApplication.getInstance().addActivity(this);
		ButterKnife.bind(CompanyCerActivity.this);
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
		com_next.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intent=new Intent();
//				intent.setClass(CompanyCerActivity.this, LogisticalRegistrationActivity.class);
//				startActivity(intent);
//				String  idcard=editText5.getText().toString();
//				if (IDUtils.IDCardValidate(idcard.toUpperCase()).equals("true")) {
					if (!StringUtil.isMobileNO(editText3.getText().toString()) || (editText3.getText().toString().length() != 11)) { // 判断是否是正确的手机号码
						// Log.e("bbb",use)
						ToastUtil.shortToastByRec(CompanyCerActivity.this,
								R.string.mobileisnotvolid);
						return;
						}
				if(!editText1.getText().toString().trim().equals("") 
						&& !editText3.getText().toString().trim().equals("")
						&&	!editText4.getText().toString().trim().equals("")
						&& !editText5.getText().toString().trim().equals("")){
					intent.putExtra("editText1", editText1.getText().toString());
					intent.putExtra("editText3", editText3.getText().toString());
					intent.putExtra("editText4", editText4.getText().toString());
					intent.putExtra("editText5", editText5.getText().toString());
					intent.putExtra("icon", icon);
					
					intent.setClass(CompanyCerActivity.this, LogisticalRegistrationActivity.class);
					if ( editText5.getText().toString().length()==18) {
						startActivity(intent);
						finish();
					}else {
						ToastUtil.shortToast(CompanyCerActivity.this, "请检查证件号码");
					}
				
					
				}else {
					ToastUtil.shortToast(CompanyCerActivity.this, "请填完善信息");
				}
//			} else {
//				ToastUtil.shortToast(getApplicationContext(), "请输入正确的身份证号");
//			}
//				btn_complete.setClickable(true);
			}
		});
		com_img.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				showPopwindow();
			}
		});
	}

	@Override
	public void setOnClick() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void getData() {
		// TODO Auto-generated method stub
		btnLefts.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				finish();
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
		window.showAtLocation(CompanyCerActivity.this.findViewById(R.id.com_img), Gravity.BOTTOM, 0, 0);

		// 这里检验popWindow里的button是否可以点击
		TextView tv_camera = (TextView) view.findViewById(R.id.tv_camera);
		TextView tv_show = (TextView) view.findViewById(R.id.tv_show);
		tv_show.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				startActivity(new Intent(CompanyCerActivity.this, IdCardActivity.class));
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
						Uri.fromFile(new File(Environment.getExternalStorageDirectory(), "companynes.png")));
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
				File temp = new File(Environment.getExternalStorageDirectory() + "/companynes.png");
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
				Log.e("img.1111111"  ,""+head);
				if (head != null) {
					/**
					 * 上传服务器代码 //TODO 实现头衔上传
					 */
					Logger.e("img.1111111");
					setPicToView(head);
					map.put("userId",
							String.valueOf(PreferencesUtils.getInt(getApplicationContext(), PreferenceConstants.UID)));
					map.put("fileName", "idcard");
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
				result = post(MCUrl.IDCARIDCom, map, map_file);
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
			if (bean.getErrCode()==0) {
			if (bean.data.size() != 0) {
				Log.e("filePath", bean.data.get(0).filePath);
				ToastUtil.shortToast(CompanyCerActivity.this, "上传成功");
				icon = bean.data.get(0).filePath;
//				PreferencesUtils.putString(getApplicationContext(), PreferenceConstants.ICON_PATH, icon);
//				loader.displayImage(icon, com_img, options);
				com_img.setBackgroundDrawable(null);
				com_img.setImageBitmap(head);
			}
			} else if (bean.getErrCode()==-2) {
				
				ToastUtil.shortToast(CompanyCerActivity.this, "您的信息正在审核,请勿重复上传");
			}else if (bean.getErrCode()==-3) {
				ToastUtil.shortToast(CompanyCerActivity.this, "您的信息已经审核通过,请勿重复上传");
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

