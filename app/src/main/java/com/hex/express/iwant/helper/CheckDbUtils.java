package com.hex.express.iwant.helper;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import com.hex.express.iwant.iWantApplication;
import com.hex.express.iwant.utils.FileUtils;
/**
 * 查询数据库的文件
 * @author SCHT-40
 *
 */
public class CheckDbUtils {

	private final static String DB_NAME = "data.db";
	private final static String DB_PATH = "/data/data/"
			+ iWantApplication.getInstance().getPackageName() + "/" + "databases";

	private static boolean copyToDb() {
		File file = new File(DB_PATH + "/" + DB_NAME);
		InputStream in = null;
		FileOutputStream fos = null;
		try {
			in = iWantApplication.getInstance().getAssets().open(DB_NAME);
			fos = new FileOutputStream(file, false);
			byte[] buffer = new byte[1024];
			int count = 0;
			while ((count = (in.read(buffer, 0, 1024))) > 0) {
				fos.write(buffer, 0, count);
			}
			fos.flush();
			return true;
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (in != null) {
					in.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
			try {
				if (fos != null) {
					fos.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
			in = null;
			fos = null;
		}
		return false;
	}

	public static boolean checkDb() {
		if (!FileUtils.isExist(DB_PATH + "/" + DB_NAME)) {
			FileUtils.mkdir(DB_PATH, DB_NAME);
			return copyToDb();
		}else{
			return true;
		}
	}


}
