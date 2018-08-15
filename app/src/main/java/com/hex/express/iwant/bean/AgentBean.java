package com.hex.express.iwant.bean;

import java.util.ArrayList;
import java.util.List;

import com.hex.express.iwant.bean.AdvertiseBean.Data;

public class AgentBean extends BaseBean {
		public List<Data> data = new ArrayList<Data>();
		public List<Data> getData() {
			return data;
		}
		public void setData(List<Data> data) {
			this.data = data;
		}
		public class Data {
			public String expressRecourageLeftMoney;
			public String  perAgentSum;
		}

	  

}