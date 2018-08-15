package com.hex.express.iwant.bean;

import java.util.ArrayList;
import java.util.List;

public class WechatBean extends BaseBean {
	public List<Data> data = new ArrayList<Data>();

	public List<Data> getData() {
		return data;
	}

	public void setData(List<Data> data) {
		this.data = data;
	}
	public class Data {
		public String billCode;
		public String appId;
		public String partnerId;
		public String prepayid;
		public String package_;
		public String nonceStr;
		public String timestamp;
		public String sign;
		
	}
}
