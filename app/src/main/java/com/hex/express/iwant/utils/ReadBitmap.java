package com.hex.express.iwant.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
/**
 * 读取图片的工具类
 * @author SCHT-40
 *
 */
public class ReadBitmap {
	/**
	 * 读取byte数据
	 * @param c
	 * @param name
	 * @param indexInt
	 */
	public void readByte(Context c, String name, int indexInt) {
		byte[] b = null;
		int[] intArrat = c.getResources().getIntArray(indexInt);
		try {
			AssetManager am = null;
			am = c.getAssets();
			InputStream is = am.open(name);
			for (int i = 0; i < intArrat.length; i++) {
				b = new byte[intArrat[i]];
				// 读取数据
				is.read(b);
				saveMyBitmap(Bytes2Bimap(b), name + i+ ".png");
			}
			is.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	/**
	 * 把byte数据转换成图片
	 * @param b
	 * @return
	 */
	public static Bitmap Bytes2Bimap(byte[] b) {
		if (b.length != 0) {
			return BitmapFactory.decodeByteArray(b, 0, b.length);
		} else {
			return null;
		}
	}
	/**
	 * 保存图片
	 * @param bmp
	 * @param path
	 * @return
	 */
	public static boolean saveMyBitmap(Bitmap bmp, String path) {
		File f = new File(path);
		try {
			f.createNewFile();
			FileOutputStream fOut = new FileOutputStream(f);
			bmp.compress(Bitmap.CompressFormat.PNG, 100, fOut);
			fOut.flush();
			fOut.close();
			return true;
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return false;
	}
}