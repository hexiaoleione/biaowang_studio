package com.hex.express.iwant.bean;

import java.util.ArrayList;
import java.util.List;

public class EcoinBean {
	public List<Data> data = new ArrayList<Data>();
	
	
	public List<Data> getList() {
		return data;
	}
	public void setList(List<Data> list) {
		this.data = data;
	}
	public class Data {
		public String ecoinMoney;
		public String ecoinName;
		public String ecoinTime;
		public boolean ifDelete;
		public int recordId;
		public int userId;
	}

	public String message;
	public boolean success;
	public int errCode;
}