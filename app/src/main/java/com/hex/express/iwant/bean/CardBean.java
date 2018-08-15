package com.hex.express.iwant.bean;

import java.util.ArrayList;
import java.util.List;

import com.hex.express.iwant.bean.CardBean.Data;

public class CardBean {
	public List<Data> data = new ArrayList<Data>();
	
	public List<Data> getData() {
		return data;
	}
	public void setData(List<Data> data) {
		this.data = data;
	}
	public class Data{
		public String couponName;
		public String conditions;
		public String coupontime;
		public double money;
		public int userCouponId;
		public int userId;
		public int couponId;
		public boolean ifDelete;
		public boolean ifExpired;
		public boolean ifUsed;
		public boolean isSelected;
	}
	public String message;
	public boolean success;
	public int errCode;
}
