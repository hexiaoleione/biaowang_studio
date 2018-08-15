package com.hex.express.iwant.bean;

import java.util.ArrayList;
import java.util.List;


public class DeposBean extends BaseBean {
	public List<Data>data=new ArrayList<Data>();
	
	public List<Data>getData() {
		return data;
	}

	public void setData(List<Data>data) {
		this.data = data;
	}
	

	public class Data{
		public int userId;
		public String driverMoney;// 镖师押金     0 默认      1    已充值    2  退款中   3  已退款
		public String money;
		
		
	}
}
