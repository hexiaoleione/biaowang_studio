package com.hex.express.iwant.bean;

import java.util.ArrayList;
import java.util.List;

import com.hex.express.iwant.bean.CardBean.Data;

public class Checnum {
public List<Data> info = new ArrayList<Data>();
	
	
	public List<Data> getInfo() {
	return info;
}


public void setInfo(List<Data> info) {
	this.info = info;
}


	public class Data{
		public String item;
		public String maney;
		public String coupontime;
		
	}
	

}
