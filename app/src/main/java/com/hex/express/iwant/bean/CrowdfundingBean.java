package com.hex.express.iwant.bean;

import java.util.ArrayList;
import java.util.List;

public class CrowdfundingBean extends BaseBean {
	public List<Data> data = new ArrayList<Data>();

	public List<Data> getData() {
		return data;
	}

	public void setData(List<Data> data) {
		this.data = data;
	}

	public class Data {
		public String fundingName;// 众筹项目名称

		public int fundingUserMount;// 已参与用户数量
		public Double limitMinMoney;// 起投金额
		public Double userFundingMoney;// 众筹金额
		public Double userFundingPercent;// 众筹所占股份
		public Double targetTotalMoney;// 目标金额
		public Double targetTotalPercent;// 目标股份
		public Double fundingMoney;// 已筹金额
		public Double fundingPercent;// 已筹股份
		public int userId;// 用户id
		public String userName;// 用户姓名
		public String userMobile;// 用户手机号

		public int userFundingTimes;// 众筹次数
		public int userFundingRanking;// 排名
	}
}
