package com.hex.express.iwant.bean;

import java.util.ArrayList;
import java.util.List;


public class AgentUserBean  extends BaseBean {
	public List<Data> data = new ArrayList<Data>();
	public List<Data> getData() {
		return data;
	}
	public void setData(List<Data> data) {
		this.data = data;
	}
	public class Data {
		public int userId;
		public String userName;
		public String headPath;
		public String totalNumber;//总单数
		public String totalMoney;//总钱
		public String dayNumber;//今天单数
		public String dayMoney;//今钱
	}

  

}
