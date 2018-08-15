package com.hex.express.iwant;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;

/**
 * 创建一个ActivityCollector.java，此类用作收集和销毁activity的公共类
 * @author han
 *
 */
public class ActivityCollector {  
	  
    public static List<Activity> activities = new ArrayList<Activity>();  
  
    public static void addActivity(Activity activity) {  
        activities.add(activity);  
    }  
  
    public static void removeActivity(Activity activity) {  
        activities.remove(activity);  
    }  
  
    public static void finishAll() {  
        for (Activity activity : activities) {  
            if (!activity.isFinishing()) {  
                activity.finish();  
            }  
        }  
    }  
  
}  
