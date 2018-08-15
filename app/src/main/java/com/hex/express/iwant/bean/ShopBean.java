package com.hex.express.iwant.bean;

import java.util.ArrayList;
import java.util.List;


public class ShopBean extends BaseBean {
	public List<Data>data=new ArrayList<Data>();
	
	public List<Data>getData() {
		return data;
	}

	public void setData(List<Data>data) {
		this.data = data;
	}
	

	public class Data{
		public Integer userId;
		public String userCode;
		public String site;
		public String address;
		public String shopName;
		public String inviteMobile;
		public String cityCode;
		public String mobile;
	}
}
