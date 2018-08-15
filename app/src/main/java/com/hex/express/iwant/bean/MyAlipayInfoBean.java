package com.hex.express.iwant.bean;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MyAlipayInfoBean extends BaseBean {
	public List<Data>data=new ArrayList<Data>();
	
	public List<Data>getData() {
		return data;
	}

	public void setData(List<Data>data) {
		this.data = data;
	}
	

	public class Data{
//		public String userId;
//		public String recId;
//		public String withdrawType;
//		public String applyTime;
//		public String applyMoney;
//		public String status;
//		public String statusName;
//		public String dealTime;
		public String aliPayNickName;
		public String aliPayAccount;
	}
}
