package com.hex.express.iwant.bean;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class CurrentLocationBean {

	public List<Data> data = new ArrayList<Data>();

	public List<Data> getDataList() {
		return data;
	}

	public void setDataList(List<Data> data) {
		this.data = data;
	}
	
	//
	public class Data {
		public String userId;//userId 用户类型：2-为快递员，3-为镖师
		public String userType;//userType
		public Double latitude;//纬度；
		public Double longitude;//经度；
		public String recordTime;//记录时间
	}

	public String message;
	public boolean success;
	public int errCode; //0 -成功； -1 -失败
}
