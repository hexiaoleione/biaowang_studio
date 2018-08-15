package com.hex.express.iwant.bean;

import java.util.ArrayList;
import java.util.List;

public class GiftBean extends BaseBean {
	public List<Data> data=new ArrayList<Data>();
	
	public class Data{
		public List<String> drawList=new ArrayList<String>();
		public double allMoney;
		public String luckyNum;
		public double leftMoney;
		public int drawCount;
	}
}
