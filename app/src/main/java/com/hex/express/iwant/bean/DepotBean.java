package com.hex.express.iwant.bean;

import java.util.ArrayList;
import java.util.List;


public class DepotBean {
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
		 public String  cityCode;//城市编码
		 public String locationAddress;//地址{定位获得}
		 public String address;//详细地址{用户输入的}
		 public Boolean ifDefault;//是否默认
		 public String latitude;
		 public String longitude;
		 
	}
	public String message;
	public boolean success;
	public int errCode;
}

