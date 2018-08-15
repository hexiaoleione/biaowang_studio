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
import java.util.Map;

import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
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

import com.hex.express.iwant.R;
import com.hex.express.iwant.iWantApplication;
import com.hex.express.iwant.newmain.MainTab;
import com.hex.express.iwant.newsmain.NewMainActivity;
import com.hex.express.iwant.utils.StringUtil;
import com.hex.express.iwant.utils.ToastUtil;
import com.hex.express.iwant.views.TitleBarView;
/**
 * 个人信息
 * @author SCHT-40
 *
 */
public class UserInfoActivity extends BaseActivity {
	private Bitmap head;
	private String fileName;
	private EditText edt_userinfoidcardnumber;
	private Button btn_complete;
	private static String path = "/sdcard/myHead/";// sd路径
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_userinfo);
		iWantApplication.getInstance().addActivity(this);
		getData();
		initView();
		initData();
		setOnClick();
	}

	@Override
	public void onWeightClick(View v) {
		final Intent intent = new Intent();
		switch (v.getId()) {
		case R.id.img_headportrait:
			showPopwindow();//显示popupwindow
			break;
		case R.id.btn_complete://完成   //TODO 要加一条图片头像判断
			String infoName = edt_userinfoname.getText().toString().trim();
			String infoIdCardNumber = edt_userinfoidcardnumber.getText().toString().trim();
			if (infoName == null ||TextUtils.isEmpty(infoName)) {//姓名不为空
				ToastUtil.shortToastByRec(getApplicationContext(), R.string.infonameisnotnull);
			} else if ( infoIdCardNumber == null|| TextUtils.isEmpty(infoIdCardNumber)) {//身份证号不为空
				ToastUtil.shortToastByRec(getApplicationContext(), R.string.idcardisnotnull);
			} else if ( !StringUtil.isIDCard(infoIdCardNumber)) {//身份证号格式不正确
				ToastUtil.shortToastByRec(getApplicationContext(), R.string.idcardisnotvolid);
			} else {
//				intent.setClass(UserInfoActivity.this, MainActivity.class);
//				intent.setClass(UserInfoActivity.this, MainTab.class);
				intent.setClass(UserInfoActivity.this, NewMainActivity.class);
				intent.putExtra("isLogin", true);
				startActivity(intent);
			}
			break;
		}
	}

	@Override
	public void initView() {
		//标题
		tbv_show = (TitleBarView) findViewById(R.id.tbv_show);
		this.tbv_show.setTitleText(R.string.userinfotitle);
		//头像
		img_headportrait = (ImageView) findViewById(R.id.img_headportrait);
		//姓名 身份证号
		edt_userinfoname = (EditText) findViewById(R.id.edt_userinfoname);
		edt_userinfoidcardnumber = (EditText) findViewById(R.id.edt_userinfoidcardnumber);
		//按钮
		btn_complete = (Button) findViewById(R.id.btn_complete);
	}

	@Override
	public void initData() {

	}

	@Override
	public void setOnClick() {
		img_headportrait.setOnClickListener(this);
		btn_complete.setOnClickListener(this);
	}

	@Override
	public void getData() {

	}
	/**
	   * 显示popupWindow
	   */
	  private void showPopwindow() {
	    // 利用layoutInflater获得View
	    LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	    View view = inflater.inflate(R.layout.pop_window_lauoutitem, null);
	    // 下面是两种方法得到宽度和高度 getWindow().getDecorView().getWidth()
	    final PopupWindow window = new PopupWindow(view,
	        WindowManager.LayoutParams.MATCH_PARENT,
	        WindowManager.LayoutParams.WRAP_CONTENT);
	    // 设置popWindow弹出窗体可点击，这句话必须添加，并且是true
	    window.setFocusable(true);
	    // 实例化一个ColorDrawable颜色为半透明
	    ColorDrawable dw = new ColorDrawable(R.color.transparent);
	    window.setBackgroundDrawable(dw);
	    window.setOutsideTouchable(false);//这是点击外部不消失
	    // 设置popWindow的显示和消失动画
	    window.setAnimationStyle(R.style.mypopwindow_anim_style);
	    // 在底部显示
	    window.showAtLocation(UserInfoActivity.this.findViewById(R.id.img_headportrait),
	        Gravity.BOTTOM, 0, 0);

	    // 这里检验popWindow里的button是否可以点击
	    TextView tv_camera = (TextView) view.findViewById(R.id.tv_camera);
	    tv_camera.setClickable(true);
	    //点击是拍照
	    tv_camera.setOnClickListener(new OnClickListener() {

	      @Override
	      public void onClick(View v) {
	    	  window.dismiss();
	    	  Intent intent2 = new Intent(
						MediaStore.ACTION_IMAGE_CAPTURE);
				intent2.putExtra(MediaStore.EXTRA_OUTPUT, Uri
						.fromFile(new File(Environment
								.getExternalStorageDirectory(),
								"head.png")));
				//intent2.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
				startActivityForResult(intent2, 2);// 采用ForResult打开
	      }
	    });
	    //从相册弄
	    TextView tv_photofromalbum = (TextView) view.findViewById(R.id.tv_photofromalbum);
	    tv_photofromalbum.setClickable(true);
	    tv_photofromalbum.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				window.dismiss();
				 Intent intent1 = new Intent(Intent.ACTION_PICK, null);
					intent1.setDataAndType(
							MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
							"image/*");
					//intent1.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
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
	    //popWindow消失监听方法
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
					File temp = new File(Environment.getExternalStorageDirectory()
							+ "/head.png");
					cropPhoto(Uri.fromFile(temp));// 裁剪图片
				}
				break;

			// 取得裁剪后的图片
			case 3:
				if (data != null) {
					Bundle extras = data.getExtras();
					head = extras.getParcelable("data");
					if (head != null) {
						/**
						 * 上传服务器代码  //TODO 实现头衔上传
						 */
//						uploadFile();
						setPicToView(head);// 保存在SD卡中
						img_headportrait.setImageBitmap(head);// 用ImageView显示出来
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
			fileName = path + "head.png";
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

		private Thread thread;
		private JSONObject obj2;
		private String idCardFileId;
		private TextView tv_prompt_hint;
		private TextView modify_head;
		private TextView modify_head1;
		private ImageView img_headportrait;
		private TitleBarView tbv_show;
		private EditText edt_userinfoname;
		@SuppressWarnings("static-access")
		/*private void uploadFile() {
			thread = new Thread() {
				public void run() {
					Looper.prepare();
					Map<String, String> map = new HashMap<String, String>();
					map.put("json", "{\"deviceId\":\"" + deviceId
							+ "\",\"fileName\":\"IDCardimage.png\"}");
					File file = new File(fileName);
					Map<String, File> files = new HashMap<String, File>();
					files.put("file", file);
					try {
						String result = post(url1, map, files);
						try {
							obj2 = new JSONObject(result);
							idCardFileId = obj2.getString("errCode");
							// 存储idCardFileId
							// getApplicationContext().getSharedPreferences("config",
							// Context.MODE_PRIVATE).edit().putString("idCardFileId",
							// idCardFileId).commit();
						} catch (JSONException e) {
							e.printStackTrace();
						}
						showDialog("上传成功 !");
					} catch (IOException e) {
						e.printStackTrace();
						showDialog("上传失败,请重新上传");
					}
					Looper.loop();
				};
			};
			try {
				thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			thread.start();
		}*/
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
		public static String post(String actionUrl, Map<String, String> params,
				Map<String, File> files) throws IOException {

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
			conn.setRequestProperty("Content-Type", MULTIPART_FROM_DATA
					+ ";boundary=" + BOUNDARY);

			// 首先组拼文本类型的参数
			StringBuilder sb = new StringBuilder();
			for (Map.Entry<String, String> entry : params.entrySet()) {
				sb.append(PREFIX);
				sb.append(BOUNDARY);
				sb.append(LINEND);
				sb.append("Content-Disposition: form-data; name=\""
						+ entry.getKey() + "\"" + LINEND);
				sb.append("Content-Type: text/plain; charset=" + CHARSET + LINEND);
				sb.append("Content-Transfer-Encoding: 8bit" + LINEND);
				sb.append(LINEND);
				sb.append(entry.getValue());
				sb.append(LINEND);
			}

			DataOutputStream outStream = new DataOutputStream(
					conn.getOutputStream());
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
					sb1.append("Content-Disposition: form-data; name=\"file\"; filename=\""
							+ file.getKey() + "\"" + LINEND);
					sb1.append("Content-Type: application/octet-stream; charset="
							+ CHARSET + LINEND);
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
}
