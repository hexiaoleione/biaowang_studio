package com.hex.express.iwant.bean;

import java.util.ArrayList;
import java.util.List;

public class CrowdCheckBean extends BaseBean {
	public List<Data> data = new ArrayList<Data>();

	public List<Data> getData() {
		return data;
	}

	public void setData(List<Data> data) {
		this.data = data;
	}

	public class Data {
		public int recId;
		public int userId;// 用户id
		public String userName;// 用户姓名
		public String userMobile;// 用户手机号

		public Double fundingMoney;// 众筹金额
		public Double fundingPercent;// 众筹所占股份
		public int fundingTimes;// 众筹次数
		public int fundingRanking;// 排名
	}

}
