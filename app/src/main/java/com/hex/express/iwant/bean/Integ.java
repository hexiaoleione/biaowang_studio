package com.hex.express.iwant.bean;

import java.util.ArrayList;
import java.util.List;

import com.hex.express.iwant.bean.LoOfferBean.Data;

public class Integ {

public List<Data> data = new ArrayList<Data>();
	
	public List<Data> getData() {
		return data;
	}
	public void setData(List<Data> data) {
		this.data = data;
	}
	public class Data{
	
		public int moneyMin;//价格
		public int moneyMax;//价格3
		public int countMin;//积分数量一
		public int countMax;//积分数量2
		public String stringMin;
		public String stringMax;
	}
	public String message;
	public boolean success;
	public int errCode;
}
