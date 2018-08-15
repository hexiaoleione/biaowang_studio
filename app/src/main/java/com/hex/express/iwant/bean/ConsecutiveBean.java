package com.hex.express.iwant.bean;

import java.util.ArrayList;
import java.util.List;

public class ConsecutiveBean extends BaseBean {
	public List<Data> data = new ArrayList<Data>();

	public class Data {
		public String billCode;
		public String needPayMoney;
		public String startBillCode;
		public String endBillCode;
		public String useMoney;
	}
}
