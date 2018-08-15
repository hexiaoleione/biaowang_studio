package com.hex.express.iwant.bean;

import java.util.ArrayList;
import java.util.List;


public class HaiBean {
public List<Data> data = new ArrayList<Data>();
	
	public List<Data> getData() {
		return data;
	}
	public void setData(List<Data> data) {
		this.data = data;
	}
	public class Data{
		public int	recId;//信息id
		   public int userId;//发件人id
		   public String carNum;//wuliurend 
		   
	}
	public String message;
	public boolean success;
	public int errCode;
}
