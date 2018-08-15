package com.hex.express.iwant.bean;

import java.util.ArrayList;
import java.util.List;

public class MypickMoneyBean extends BaseBean {

	public List<Data> data = new ArrayList<Data>();

	public List<Data> getData() {
		return data;
	}

	public void setData(List<Data> data) {
		this.data = data;
	}

	public class Data {
		public String userId;
		public String dayMount;
		public String dayMoney;
		public String weekMount;
		public String weekMoney;
		public String monthMount;
		public String monthMoney;
		public String totalMoney;
		public String totalMount;

	}

}
