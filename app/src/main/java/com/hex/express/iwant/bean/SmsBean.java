package com.hex.express.iwant.bean;

import java.util.ArrayList;
import java.util.List;

public class SmsBean extends BaseBean {
	public List<Data> data=new ArrayList<Data>();
	
	public List<Data> getData() {
		return data;
	}

	public void setData(List<Data> data) {
		this.data = data;
	}

	public class Data{
		String message;
		int code;
		public String getMessage() {
			return message;
		}
		public void setMessage(String message) {
			message = message;
		}
		public Integer getCode() {
			return code;
		}
		public void setCode(Integer code) {
			this.code = code;
		}
		
	}
}
