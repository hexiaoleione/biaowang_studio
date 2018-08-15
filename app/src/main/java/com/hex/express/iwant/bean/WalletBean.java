package com.hex.express.iwant.bean;

import java.util.ArrayList;
import java.util.List;

public class WalletBean {

	public List<Data> data = new ArrayList<Data>();

	public List<Data> getDataList() {
		return data;
	}

	public void setDataList(List<Data> data) {
		this.data = data;
	}

	
//	"userId":72024,
//	"userName":"",
//	"validBalance":0.0,
//	"holdOnMoney":"","
//	lastTimePercent":"","
//	withdrawableMoney":"","
//	transferableMoney":"","principalMoney":"","
//	unPrincipalMoney":"","
//	yestodayLuckyDrawMoney":0,"
//	yestodayReceiveMoney":0,
//	"yestodayAllowance":0,"
//	yestodayRecommendMoney":0}],"errCode":0,"success":true,"message":"获取成功"}
	
	public class Data {
		public int recordId;//主键
		public int userId;//userId
		public String recordMoney;//金额
		public String recordName;//名字
		public String recordType;//充值类型
		public String recordTime;//充值时间
		public String recordDesc;//内容
		public boolean ifDelete;//删除
		
		public String yestodayRecieveMoney;//昨日总收益
		public String validBalance;//可消费金额
		public String sevendayPercent;//七日年化收益率
		public String interestEveryTenThousand;//每万份收益
		
		public String yestodayAllowance;//昨日补贴
		public String yestodayRecommendMoney;//昨日推广收益
		
		public String userName;
		public String holdOnMoney;
		public String lastTimePercent;
		public String withdrawableMoney;
		public String transferableMoney;
		public String principalMoney;
		public String unPrincipalMoney;
		public String yestodayLuckyDrawMoney;
		public String yestodayReceiveMoney;
		
		
		
		
	}

	public String message;
	public boolean success;
	public int errCode;
}
