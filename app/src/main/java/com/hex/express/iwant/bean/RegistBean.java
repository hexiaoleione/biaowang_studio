package com.hex.express.iwant.bean;

import java.util.ArrayList;
import java.util.List;


public class RegistBean {
	public List<Data> data = new ArrayList<Data>();

	public List<Data> getData() {
		return data;
	}

	public void setData(List<Data> data) {
		this.data = data;
	}

	public class Data {
		public String endSignTime;//最后签到的时间
		public int days;//领奖签到的天数,每一个周期清零
		public int number;//连续签到的天数,累加
		public String ecoin;//一个周期末赠送的E币数(未用)
		public Boolean ifSign;//是否签到
		public  String needSignDays;//距离领奖的连签天数
		public String keepDay;  // 连续签到天数
		public int allEcoin;////通过签到获得的总E币数
		public int everyDayEcoin;// 每次签到奖励E币数量
		public int dayEcoin;//今日已获得的E币数量
		public int userEcoin;
	}
	public String message;
	public boolean success;
	public int errCode;
}

