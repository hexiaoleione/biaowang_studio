package com.hex.express.iwant.bean;

import java.util.ArrayList;
import java.util.List;

public class CourierWalletBean extends BaseBean {
	public List<Data> data = new ArrayList<Data>();

	public List<Data> getDataList() {
		return data;
	}

	public void setDataList(List<Data> data) {
		this.data = data;
	}

	public class Data {
		public String userId;
		public String userName;
		public String validBalance;
		public String holdOnMoney;
		public String lastTimePercent;
		public String withdrawableMoney;
		public String transferableMoney;
		public String principalMoney;
		public String unPrincipalMoney;
		public String yestodayLuckyDrawMoney;
		public String yestodayReceiveMoney;
		public String yestodayAllowance;
		public String yestodayRecommendMoney;
		public String sevendayPercent;
		public String interestEveryTenThousand;
		public String yestodayRechargeRecourageMoney;
		public String courierMessage;
		public String passedExpressMoney;//已判定金额
	}
}
