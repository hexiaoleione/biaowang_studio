package com.hex.express.iwant.http;

import com.hex.express.iwant.constance.PreferenceConstants;


public class HeadValue {
	public static void setHead(String key,String value){
		AsyncHttpUtils.client.addHeader(key,value);
	}
}
