package com.hex.express.iwant.http;

import java.io.File;
import java.io.FileNotFoundException;

import android.content.Context;

import com.hex.express.iwant.constance.PreferenceConstants;
import com.hex.express.iwant.utils.PreferencesUtils;
import com.loopj.android.http.RequestParams;

public class HttpRequestParams{
	/**
	 * 
	 * @return 根据请求的参数返回不同的params
	 */
	
	public static RequestParams getParams(){
		RequestParams params=new RequestParams();
		params.put("key", "value");
		try {
			params.put("file", new File(""));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return params;
	}
	public static RequestParams getSMSParams(Context context){
		RequestParams params=new RequestParams();
		params.put("mobile",PreferencesUtils.getString(context,PreferenceConstants.MOBILE));
		return params;
	}
}
