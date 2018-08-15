package com.hex.express.iwant.http;

import java.io.UnsupportedEncodingException;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.entity.ByteArrayEntity;

import android.content.Context;

import com.lidroid.xutils.http.client.multipart.content.ByteArrayBody;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

public class AsyncHttpUtils {
	private final static String TAG = "AsyncHttpUtilClient";
	public static AsyncHttpClient client = new AsyncHttpClient();
	
	public static void doGet(String url, String key, String value,
			RequestParams params, AsyncHttpResponseHandler responseHandler) {
		if (null != value) {
			HeadValue.setHead(key, value);
		}
		client.get(url, params, responseHandler);
	}

	public static void doGets(String url, String key, String value,
			RequestParams params, AsyncHttpResponseHandler responseHandler) {
		if (null != value) {
			HeadValue.setHead(key, value);
		}
		client.get(url, params, responseHandler);
	}
	public static void doSimGet(String url,
			AsyncHttpResponseHandler responseHandler) {
		client.get(url, responseHandler);
	}

	public static void doPost(String url, String headerValue,
			RequestParams params, AsyncHttpResponseHandler responseHandler) {
		if (null != headerValue) {
			// 设置请求头参数 client.addHeader("XX", headerValue);
			/*
			 * client.addHeader("Content-Type",headerValue);
			 * client.addHeader("Content-Length",headerValue.length()+"");
			 */
		}

		client.post(url, params, responseHandler);
	}

	public static void doPut(Context context, String url, String json,
			RequestParams params, AsyncHttpResponseHandler responseHandler) {
		ByteArrayEntity entity = null;
		try {
			entity = new ByteArrayEntity(json.getBytes("UTF-8"));
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		client.put(context, url, entity, "application/json", responseHandler);
	}
	public static void dosPut(Context context, String url, String json,
			AsyncHttpResponseHandler responseHandler) {
		ByteArrayEntity entity = null;
		try {
			entity = new ByteArrayEntity(json.getBytes("UTF-8"));
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		client.put(context, url, entity, "application/json", responseHandler);
	}

	public static void doPostJson(Context context, String url, String json,
			AsyncHttpResponseHandler responseHandler) {
		ByteArrayEntity entity = null;
		try {
			entity = new ByteArrayEntity(json.getBytes("UTF-8"));
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		client.post(context, url, entity, "application/json", responseHandler);
	}

		public static void doPostData(Context context,String url,String json,AsyncHttpResponseHandler responseHandler){
			ByteArrayEntity entity=null;
			try {
				entity = new ByteArrayEntity(json.getBytes("UTF-8"));
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			client.post(context, url, entity, "multipart/form-data", responseHandler);
		}

		
	}