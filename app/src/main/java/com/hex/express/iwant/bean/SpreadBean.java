package com.hex.express.iwant.bean;

import java.util.ArrayList;
import java.util.List;

public class SpreadBean extends BaseBean {
	public List<Data>data=new ArrayList<Data>();
	
	public List<Data>getData() {
		return data;
	}

	public void setData(List<Data>data) {
		this.data = data;
	}
	

	public class Data{
		public String high;
		public String length;
		public String price;
		public String matName;
		
	}
}
