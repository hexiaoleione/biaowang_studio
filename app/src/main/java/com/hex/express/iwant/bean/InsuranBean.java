package com.hex.express.iwant.bean;

import java.util.ArrayList;
import java.util.List;

import com.hex.express.iwant.bean.LoOfferBean.Data;

public class InsuranBean {

public List<Data> data = new ArrayList<Data>();
	
	public List<Data> getData() {
		return data;
	}
	public void setData(List<Data> data) {
		this.data = data;
	}
	public class Data{
	
		public int recId;//主键
		public int userId;//发件人id
		public String cargoName;//货物名称
		public String startPlace;//货物起始地点
	
	}
	public String message;
	public boolean success;
	public int errCode;
}
