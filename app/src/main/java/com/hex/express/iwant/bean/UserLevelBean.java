package com.hex.express.iwant.bean;

import java.util.ArrayList;
import java.util.List;

public class UserLevelBean extends BaseBean {
	public List<Data> data = new ArrayList<Data>();
	public List<Data> getData() {
		return data;
	}
	public void setData(List<Data> data) {
		this.data = data;
	}
	public class Data {
		public String userId;
		public String userName;
		public String userMobile;
		public int creditScore;
		public String updateTime;
		public int creditLV;
	}
}
