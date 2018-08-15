package com.hex.express.iwant.bean;

import java.util.ArrayList;
import java.util.List;


public class LoisattenBean {
public List<Data> data = new ArrayList<Data>();
	
	public List<Data> getData() {
		return data;
	}
	public void setData(List<Data> data) {
		this.data = data;
	}
	public class Data{
	
		public String entPlace;
		public String startPlace;
		public int recId;
		public int userId;
		public String startPlaceCityCode;
		public String entPlaceCityCode;
		public boolean ifDelete;
	}
	public String message;
	public boolean success;
	public int errCode;

}
