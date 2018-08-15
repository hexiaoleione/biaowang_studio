package com.hex.express.iwant.bean;

import java.util.ArrayList;
import java.util.List;

import com.hex.express.iwant.bean.CardBean.Data;

import android.R.integer;

public class LuckyBean {
public List<Data> data = new ArrayList<Data>();
	
	public List<Data> getData() {
		return data;
	}
	public void setData(List<Data> data) {
		this.data = data;
	}
	public class Data{
	
		public int drawLevel;//等级
		public int userId;
		public int recId;
		public String intervalTime;//倒计时
		public String userMobile;
		public String userName;
		public String drawType;
		public String drawMoney;
		public String drawName;
		public String drawTime;
		public String generateTime;
		public int position;//位置
		
	}
	public String message;
	public boolean success;
	public int errCode;
}

