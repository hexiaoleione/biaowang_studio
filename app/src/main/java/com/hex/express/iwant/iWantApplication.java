package com.hex.express.iwant;

import java.util.LinkedList;
import java.util.List;

import android.app.Activity;
import android.app.Application;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.support.multidex.MultiDex;

import cn.jpush.android.api.JPushInterface;

import com.baidu.mapapi.SDKInitializer;
import com.hex.express.iwant.helper.DbManager;
import com.hex.express.iwant.utils.CrashHandler;
import com.hex.express.iwant.utils.ToastUtil;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechUtility;
import com.tencent.bugly.crashreport.CrashReport;

public class iWantApplication extends Application {
    // 定义全局变量
    public static String BASE_URL = "http://kd.efamax.com:8080/Exp/ui!";// 基准的urlַ
    private static Handler mainHandler;// 主线程的handler
    static iWantApplication mApplication;// 全局的application
    private BroadcastReceiver connectionReceiver;
    public volatile DbManager mDbManager;
    // 运用list来保存们每一个activity是关键
    private List<Activity> mList = new LinkedList<Activity>();

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        CrashReport.initCrashReport(getApplicationContext(), "ccb6dcb790", false);    //bugly初始化
        JPushInterface.setDebugMode(true);
        JPushInterface.init(this);
        mApplication = this;
        // 在使用 SDK 各组间之前初始化 context 信息，传入 ApplicationContext
        SDKInitializer.initialize(this);
        // 初始化讯飞语音组件
        SpeechUtility.createUtility(this, "appid=" + "5af15334");
    }

    /**
     * 获取全局变量
     *
     * @return
     */
    public static iWantApplication getInstance() {
        return mApplication;
    }

    /**
     * 获取主handler
     *
     * @return
     */
    public static Handler getHandler() {
        return mainHandler;
    }

    /**
     * 实时监听网络连接的广播 注册
     */
    public void registerMessageReceiver() {
        connectionReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                ConnectivityManager connectMgr = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
                NetworkInfo mobNetInfo = connectMgr
                        .getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
                NetworkInfo wifiNetInfo = connectMgr
                        .getNetworkInfo(ConnectivityManager.TYPE_WIFI);
                if (!mobNetInfo.isConnected() && !wifiNetInfo.isConnected()) {
                    ToastUtil.shortToast(mApplication, "当前无网络连接,请连接网络");
                }
            }
        };
        IntentFilter filter = new IntentFilter();
        filter.setPriority(IntentFilter.SYSTEM_HIGH_PRIORITY);
        filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(connectionReceiver, filter);
    }

    /**
     * 取消广播
     */
    public void unRegisterMessageReceiver() {
        unregisterReceiver(connectionReceiver);
    }

    /**
     * add Activity
     *
     * @param activity
     */
    public void addActivity(Activity activity) {
        mList.add(activity);
    }

    /**
     * 关闭每一个list内的activity
     */
    public void exit() {
        try {
            for (Activity activity : mList) {
                if (activity != null)
                    activity.finish();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            System.exit(0);
        }
    }

    /**
     * 杀进程
     */
    public void onLowMemory() {
        super.onLowMemory();
        System.gc();
    }
}
