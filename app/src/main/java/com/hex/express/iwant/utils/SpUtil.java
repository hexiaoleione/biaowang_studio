package com.hex.express.iwant.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.StreamCorruptedException;
import java.util.ArrayList;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.util.Base64;

public class SpUtil
{
  private static final String NAME = "config";
  public static final String PROPERTY_CITYCODE = "citycode";
  public static final String PROPERTY_FORBIDDENTIME = "forbiddentime";
  public static final String PROPERTY_HAVENEWPUBLISH = "haveNewPublish";
  public static final String PROPERTY_HAVENOTICE = "haveNotice";
  public static final String PROPERTY_ISFIRST = "isFirst";
  public static final String PROPERTY_JPUSHREGID = "jpushregid";
  public static final String PROPERTY_LATITUDE = "latitude";
  public static final String PROPERTY_LOGINERRCOUNT = "loginerrcount";
  public static final String PROPERTY_LONGITUDE = "longitude";
  public static final String PROPERTY_NEWVERSION = "newversion";
  public static final String PROPERTY_VERSION = "version";
  public static final String PROPERTY_VERSIONCODE = "versioncode";
  private static SpUtil instance;
  private SharedPreferences sp;

  private SpUtil(Context paramContext)
  {
    this.sp = paramContext.getSharedPreferences(NAME, 0);
  }

  public static SpUtil getInstance(Context paramContext)
  {
    if (instance == null)
      instance = new SpUtil(paramContext);
    return instance;
  }

  private void setValue(String paramString, int paramInt)
  {
    Editor localEditor = this.sp.edit();
    localEditor.putInt(paramString, paramInt);
    localEditor.commit();
  }

  private void setValue(String paramString, long paramLong)
  {
    Editor localEditor = this.sp.edit();
    localEditor.putLong(paramString, paramLong);
    localEditor.commit();
  }

  private void setValue(String paramString1, String paramString2)
  {
    Editor localEditor = this.sp.edit();
    localEditor.putString(paramString1, paramString2);
    localEditor.commit();
  }

  private void setValue(String paramString, boolean paramBoolean)
  {
    Editor localEditor = this.sp.edit();
    localEditor.putBoolean(paramString, paramBoolean);
    localEditor.commit();
  }

  public String getCityCode()
  {
    return this.sp.getString("citycode", "");
  }

  public long getForbiddenTime()
  {
    return this.sp.getLong("forbiddentime", 0L);
  }

  public double getLatitude()
  {
    return Double.parseDouble(this.sp.getString("latitude", "0"));
  }

  public int getLoginErrCount()
  {
    return this.sp.getInt("loginerrcount", 0);
  }

  public double getLongitude()
  {
    return Double.parseDouble(this.sp.getString("longitude", "0"));
  }

  public String getVersion()
  {
    return this.sp.getString("version", "");
  }

  public int getVersionCode()
  {
    return this.sp.getInt("versioncode", 0);
  }

  public boolean haveNewPublish()
  {
    return this.sp.getBoolean("haveNewPublish", false);
  }

  public boolean haveNewVersion()
  {
    return this.sp.getBoolean("newversion", false);
  }

  public boolean haveNotice()
  {
    return this.sp.getBoolean("haveNotice", false);
  }

  public boolean isFirst()
  {
    return this.sp.getBoolean("isFirst", true);
  }

  public void setCityCode(String paramString)
  {
    setValue("citycode", paramString);
  }

  public void setForbiddenTime(long paramLong)
  {
    setValue("forbiddentime", paramLong);
  }

  public void setHaveNewPublish(boolean paramBoolean)
  {
    setValue("haveNewPublish", paramBoolean);
  }

  public void setHaveNewVersion(boolean paramBoolean)
  {
    setValue("newversion", paramBoolean);
  }

  public void setHaveNotice(boolean paramBoolean)
  {
    setValue("haveNotice", paramBoolean);
  }

  public void setIsFirst(boolean paramBoolean)
  {
    setValue("isFirst", paramBoolean);
  }

//  public void setLatitude(double paramDouble)
//  {
//    setValue("latitude", paramDouble);
//  }

  public void setLoginErrCount(int paramInt)
  {
    setValue("loginerrcount", paramInt);
  }


  public void setVersion(String paramString)
  {
    setValue("version", paramString);
  }

  public void setVersionCode(int paramInt)
  {
    setValue("versioncode", paramInt);
  }
  public static String SceneList2String(ArrayList<String[]> SceneList)
          throws IOException {
    // 实例化一个ByteArrayOutputStream对象，用来装载压缩后的字节文件。
    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
    // 然后将得到的字符数据装载到ObjectOutputStream
    ObjectOutputStream objectOutputStream = new ObjectOutputStream(
            byteArrayOutputStream);
    // writeObject 方法负责写入特定类的对象的状态，以便相应的 readObject 方法可以还原它
    objectOutputStream.writeObject(SceneList);
    // 最后，用Base64.encode将字节文件转换成Base64编码保存在String中
    String SceneListString = new String(Base64.encode(
            byteArrayOutputStream.toByteArray(), Base64.DEFAULT));
    // 关闭objectOutputStream
    objectOutputStream.close();
    return SceneListString;
}

@SuppressWarnings("unchecked")
public static ArrayList<String[]> String2SceneList(String SceneListString)
        throws StreamCorruptedException, IOException,
        ClassNotFoundException {
    byte[] mobileBytes = Base64.decode(SceneListString.getBytes(),
            Base64.DEFAULT);
    ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(
            mobileBytes);
    ObjectInputStream objectInputStream = new ObjectInputStream(
            byteArrayInputStream);
    ArrayList<String[]>  SceneList = (ArrayList<String[]> ) objectInputStream
            .readObject();
    objectInputStream.close();
    return SceneList;
}

}