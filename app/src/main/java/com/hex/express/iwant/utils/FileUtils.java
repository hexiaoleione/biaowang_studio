package com.hex.express.iwant.utils;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

import android.content.Context;
import android.os.Environment;

/**
 * 文件工具类
 * 
 * @ClassName: FileUtil All rights Reserved, Designed By isoftStone Copyright:
 *             Copyright(C) 2013 Company: isoftStone Holdings Limited
 * @author: Wen Weiquan
 * @version: V1.0
 * @date 2013-11-1 上午11:52:19
 */
public class FileUtils {
//	public static final String WJDX = "/wjdx";
//	public static final String DIST = "dist";
//	public static final String TOOLS = "tools.js";
//	
//
	public static final String IMAGE = "/image";
//	public static final String WWW = "/www";
//	public static final String VENDOR = "/vendor";
//	public static final String VENDOR_INFINITUS = "com.infinitus/vendor";
//	public static final String INFINITUS_JS = "infinitus.js";
//	public static final String HUAKANG_TTF = "huakang.TTF";
//	public static final String YOUYUAN_TTF = "YouYuan.TTF";
//	public static final String THEME = "/theme";
//	public static final String TEMP = "/temp";
//	public static final String NOMEDIA = "/.nomedia";

	/** SD卡根目录下文件缓存的目录 */
	private static String fileCachePath = null;

	/** 得到文件缓存的目录 */
	public static String getFileCachePath() {
		return fileCachePath;
	}
	/**
	 * 初始化应用的临时目录
	 * 
	 * @param context
	 * @param fileName
	 */
	public static void initFileCachePath(Context context) {
		fileCachePath = getCacheDir(context).getAbsolutePath();
		File imageFile = new File(fileCachePath + IMAGE);
		if (!imageFile.exists()) {
			imageFile.mkdir();
		}
	}
	
	public static String getImageCacheFile(){
		return fileCachePath + IMAGE;
	}
	
	public static String getTakePhotoTempPath(){
		return getImageCacheFile() + "/phone" + System.currentTimeMillis() + "temp.jpg";
	}

	/**
	 * 获取应用下的缓存目录
	 * 
	 * @param context
	 * @return
	 */
	private static File getCacheDir(Context context) {
		/*if (android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED))
			return context.getExternalCacheDir();*/
		return context.getCacheDir();
	}

	/**
	 * 保存首页配置文件
	 */
	public static void saveHomeSetFile(Context context, String data) {
		initFileCachePath(context);
		try {
			printlnFile("home_set.xml", data);
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public static void saveHomeUserSetFile(Context context, String data) {
		initFileCachePath(context);
		try {
			printlnFile("userhome_set.xml", data);
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	private static void printlnFile(String filename, String msg) throws IOException {
		File f = new File(fileCachePath, filename);
		if (!f.getParentFile().exists()) {
			f.getParentFile().mkdirs();
		}
		f.createNewFile();
		PrintWriter p = new PrintWriter(new OutputStreamWriter(new FileOutputStream(f), "utf-8"));
		p.println(msg);
		p.flush();
		p.close();
	}

	/**
	 * 获取用户菜单缓存
	 * 
	 * @param context
	 * @return
	 */
	public String getUserFuncListFile(Context context) {
		String data = null;
		String name = "funclist_User";
		File file = new File(fileCachePath, name);

		try {
			FileInputStream in = new FileInputStream(file);
			data = readTextInputStream(in);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return data;
	}

	public static String readTextInputStream(InputStream is) throws IOException {
		StringBuffer strbuffer = new StringBuffer();
		String line;
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new InputStreamReader(is));
			while ((line = reader.readLine()) != null) {
				strbuffer.append(line);
			}
		} finally {
			if (reader != null) {
				reader.close();
			}
		}
		return strbuffer.toString();
	}

	public static boolean DeleteFolder(String sPath) {
		boolean flag = false;
		if (sPath != null) {
			File file = new File(sPath);
			// 判断目录或文件是否存在
			if (!file.exists()) { // 不存在返回 false
				return flag;
			} else {
				// 判断是否为文件
				if (file.isFile()) { // 为文件时调用删除文件方法
					return deleteFile(sPath);
				} else { // 为目录时调用删除目录方法
					return deleteDirectory(sPath);
				}
			}
		}
		return flag;
	}

	private static boolean deleteFile(String sPath) {
		boolean flag = false;
		File file = new File(sPath);
		// 路径为文件且不为空则进行删除
		if (file.isFile() && file.exists()) {
			file.delete();
			flag = true;
		}
		return flag;
	}

	public static boolean deleteDirectory(String sPath) {
		// 如果sPath不以文件分隔符结尾，自动添加文件分隔符
		if (!sPath.endsWith(File.separator)) {
			sPath = sPath + File.separator;
		}
		File dirFile = new File(sPath);
		// 如果dir对应的文件不存在，或者不是一个目录，则退出
		if (!dirFile.exists() || !dirFile.isDirectory()) {
			return false;
		}
		boolean flag = true;
		// 删除文件夹下的所有文件(包括子目录)
		File[] files = dirFile.listFiles();
		for (int i = 0; i < files.length; i++) {
			// 删除子文件
			if (files[i].isFile()) {
				flag = deleteFile(files[i].getAbsolutePath());
				if (!flag)
					break;
			} // 删除子目录
			else {
				flag = deleteDirectory(files[i].getAbsolutePath());
				if (!flag)
					break;
			}
		}
		if (!flag)
			return false;
		// 删除当前目录
		if (dirFile.delete()) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 移动目录
	 * 
	 * @param srcDirName
	 *            源目录完整路径
	 * @param destDirName
	 *            目的目录完整路径
	 * @return 目录移动成功返回true，否则返回false
	 */
	public static boolean moveDirectory(String srcDirName, String destDirName) {

		File srcDir = new File(srcDirName);
		if (!srcDir.exists() || !srcDir.isDirectory())
			return false;

		File destDir = new File(destDirName);
		if (!destDir.exists())
			destDir.mkdirs();

		/**
		 * 如果是文件则移动，否则递归移动文件夹。删除最终的空源文件夹 注意移动文件夹时保持文件夹的树状结构
		 */
		File[] sourceFiles = srcDir.listFiles();
		for (File sourceFile : sourceFiles) {
			if (sourceFile.isFile())
				moveFile(sourceFile.getAbsolutePath(), destDir.getAbsolutePath());
			else if (sourceFile.isDirectory())
				moveDirectory(sourceFile.getAbsolutePath(),
						destDir.getAbsolutePath() + File.separator + sourceFile.getName());
			else
				;
		}
		return srcDir.delete();
	}

	/**
	 * 移动文件
	 * 
	 * @param srcFileName
	 *            源文件完整路径
	 * @param destDirName
	 *            目的目录完整路径
	 * @return 文件移动成功返回true，否则返回false
	 */
	public static boolean moveFile(String srcFileName, String destDirName) {

		File srcFile = new File(srcFileName);
		if (!srcFile.exists() || !srcFile.isFile())
			return false;

		File destDir = new File(destDirName);
		if (!destDir.exists())
			destDir.mkdirs();

		return srcFile.renameTo(new File(destDirName + File.separator + srcFile.getName()));
	}

	/**
	 * 复制Assets 里的整个文件夹到SD卡里
	 * 
	 * @param assetDir
	 * @param dir
	 */
	public static void copyAssets(Context context, String assetDir, String dir) {
		String[] files;
		try {
			// 获得Assets一共有几多文件
			files = context.getResources().getAssets().list(assetDir);
		} catch (IOException e1) {
			return;
		}
		File mWorkingPath = new File(dir);
		// 如果文件路径不存在
		if (!mWorkingPath.exists()) {
			// 创建文件夹
			if (!mWorkingPath.mkdirs()) {
				// 文件夹创建不成功时调用
			}
		}

		for (int i = 0; i < files.length; i++) {
			try {
				// 获得每个文件的名字
				String fileName = files[i];
				// 根据路径判断是文件夹还是文件
				if (!fileName.contains(".")) {
					if (0 == assetDir.length()) {
						copyAssets(context, fileName, dir + fileName + "/");
					} else {
						copyAssets(context, assetDir + "/" + fileName, dir + "/" + fileName + "/");
					}
					continue;
				}
				File outFile = new File(mWorkingPath, fileName);
				if (outFile.exists()) {
					outFile.delete();
				}
				InputStream in = null;
				if (0 != assetDir.length()) {
					in = context.getAssets().open(assetDir + "/" + fileName);
				} else {
					in = context.getAssets().open(fileName);
				}
				OutputStream out = new FileOutputStream(outFile);

				// Transfer bytes from in to out
				byte[] buf = new byte[1024];
				int len;
				while ((len = in.read(buf)) > 0) {
					out.write(buf, 0, len);
				}

				in.close();
				out.close();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 单个文件复制,dir的格式如“/sdcard/js/”+filePath
	 * 
	 * @param filePath
	 * @param dir
	 * @param fileName
	 * @throws IOException
	 */
	public static void copyOneFileToSDCard(Context context, String filePath, String dir, String fileName) {
		try {
			InputStream is = context.getAssets().open(filePath);
			BufferedInputStream inBuff = null;
			inBuff = new BufferedInputStream(is);

			byte[] buffer = new byte[1024];
			if (isHaveSDCard()) {
				File f = new File(dir);
				if (!f.exists()) {
					f.mkdirs();
				}

				FileOutputStream fos = new FileOutputStream(dir + "/" + fileName);
				int len = inBuff.read(buffer);
				while (len > 0) {
					fos.write(buffer, 0, len);
					len = inBuff.read(buffer);
				}
				fos.close();
				inBuff.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 判断SDCard是否存在
	 * 
	 * @return
	 */
	private static boolean isHaveSDCard() {
		String status = Environment.getExternalStorageState();
		if (status.equals(Environment.MEDIA_MOUNTED)) {
			return true;
		} else {
			return false;
		}
	}
	public static void mkdir(String db_path, String db_path2) {
        try {
            File file = new File(db_path + "/" + db_path2);
            if (!file.getParentFile().exists()) {
                file.getParentFile().mkdir();
            }
            if (!file.exists()) {
                file.createNewFile();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return;
    }
    /**
     * 鍒涘缓鏂囦欢
     * @param path
     */
    public static void mkdir(String path) {
        try {
            File file = new File(path);
            if (!file.getParentFile().exists()) {
                mkdir(file.getParentFile().getPath());
            }
            if (!file.exists()) {
                file.mkdir();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return;
    }
    /**
     * 鍒ゆ柇鏂囦欢鏄惁瀛樺湪
     * @param path
     * @return
     */
    public static boolean isExist(String path) {
        File file = new File(path);
        try {
            if (file.getParentFile().exists()) {
                if (file.exists()) {
                    return true;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean judeSize(String string, int size) {
        return false;
    }
	public static void isFolderExists(String string) {
		// TODO Auto-generated method stub
		
	}
	public static String getDir() {
		// TODO Auto-generated method stub
		return null;
	}


}
