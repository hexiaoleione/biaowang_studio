package com.hex.express.iwant.activities;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.TextView;

import com.hex.express.iwant.R;
import com.hex.express.iwant.utils.AppUtils;
import com.hex.express.iwant.views.ClipImageView;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

public class CropActivity extends Activity {

	private ClipImageView image;
	private String path;
	// 裁切后是否返回路径，默认是返回bitmap字节数据，如果需要返回路径，就需要先生成图片再返回
	private boolean backPath;
	private ImageLoader loader;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);// 去掉标题栏
		/*this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);// 去掉信息栏
*/		setContentView(R.layout.activity_crop);
		loader=ImageLoader.getInstance();
		loader.init(ImageLoaderConfiguration.createDefault(this));
		image = (ClipImageView) findViewById(R.id.iv);
		path = getIntent().getStringExtra("path");
		backPath = getIntent().getBooleanExtra("backPath", false);
		if (!path.startsWith("content"))
			loader.displayImage("file://" + path, image);
			//Picasso.with(this).load("file://" + path).into(image);
		else
			loader.displayImage(path, image);
		//loader.load(path).into(image);

		TextView right = (TextView) findViewById(R.id.tv_right);
		right.setText("确定");
		right.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				ok();
			}
		});
	}

	public void ok() {
		// 此处获取剪裁后的bitmap
		if (!backPath) {
			Bitmap bitmap = image.clip();

			// 由于Intent传递bitmap不能超过40k,此处使用二进制数组传递
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
			byte[] bitmapByte = baos.toByteArray();

			Intent intent = new Intent();
			intent.putExtra("result", bitmapByte);
			setResult(RESULT_OK, intent);
			finish();
		} else
			loadPath();
	}

	public void loadPath() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				Bitmap bitmap = image.clip();
				File cacheDir = null;
				AppUtils.saveBitmap(bitmap, null);
				if (android.os.Environment.getExternalStorageState().equals(
						android.os.Environment.MEDIA_MOUNTED)) {
					cacheDir = getExternalCacheDir();
				} else {
					cacheDir = getCacheDir();
				}
				File file = new File(cacheDir.getAbsolutePath() + "/"
						+ System.currentTimeMillis() + ".jpg");
				saveBitmap(bitmap, file);
				Intent intent = new Intent();
				intent.putExtra("result", file.getAbsolutePath());
				setResult(RESULT_OK, intent);
				finish();
			}
		}).start();
	}

	/** 将bitmap保存 */
	private boolean saveBitmap(Bitmap bitmap, File file) {
		if (bitmap != null && file != null) {
			FileOutputStream out = null;
			try {
				out = new FileOutputStream(file);
				/** quality，质量，0-100 */
				return bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				if (out != null) {
					try {
						out.flush();
						out.close();
					} catch (Exception e2) {
						e2.printStackTrace();
					}
				}
			}
		}
		return false;
	}
}
