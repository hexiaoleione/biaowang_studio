package com.hex.express.iwant.bean;

import java.util.ArrayList;
import java.util.List;


public class DrawCardBean {
	public List<Data> data = new ArrayList<Data>();

	public List<Data> getData() {
		return data;
	}

	public void setData(List<Data> data) {
		this.data = data;
	}

	public class Data {
		public String a;// 
		public String b;// 
		public String c;// 
		public String d;// 
		public String e;// 
		public String couponName;//现金券名字
		public String couponFrom;//现金券来源
		public String conditionId;//现金券来源类型
		public String couponCount;//现金券数量
		public String m;//现金券数量
		public String money;//现金券数量
		
	}
	public String message;
	public boolean success;
	public int errCode;
}
