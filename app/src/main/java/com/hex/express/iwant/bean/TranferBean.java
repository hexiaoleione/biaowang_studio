package com.hex.express.iwant.bean;

import java.util.ArrayList;
import java.util.List;

public class TranferBean {
	public List<Data> data = new ArrayList<Data>();

	public List<Data> getData() {
		return data;
	}

	public void setData(List<Data> data) {
		this.data = data;
	}

	public class Data {
		public Integer userId;
		public Double transferableMoney;// 转账余额
		public String waitMoney;//余额
		
	}

	public String message;
	public boolean success;
	public int errCode;
}
