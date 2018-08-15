package com.hex.express.iwant.bean;

import java.util.ArrayList;
import java.util.List;


public class GuardBean extends BaseBean {
	public List<Data>data=new ArrayList<Data>();
	
	public List<Data>getData() {
		return data;
	}

	public void setData(List<Data>data) {
		this.data = data;
	}
	

	public class Data{
		public String driverCount;
		public String	activityMoney;
		public boolean buttunSwitch;
		public boolean ifReciveMoney;
		public String driverRouteCount;//接单次数
		public String	activityCount;//周期次数
	
		public String	recordMoney;//周期次数recordMoney
		public String	ifBuyInsure;//
		
		
		public String ifPass;
//		public String matName;
		
	}
}
