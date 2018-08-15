package com.hex.express.iwant.bean;

import java.util.ArrayList;
import java.util.List;


public class RegisBean extends BaseBean {
	public List<Data>data=new ArrayList<Data>();
	
	public List<Data>getData() {
		return data;
	}

	public void setData(List<Data>data) {
		this.data = data;
	}
	

	public class Data{
		public int userId;
		public String userCode;
		public String userName;
		public String userType;
		public String mobile;
		public String cityCode;
		public String sex;
		public String inviteCode;
		public String deviceId;
		public String headPath;
		public String password;
		public String payPassword;
		public String idCard;
		public String idCardPath;
		public String birthday;
		public String agreementType;//海南用户标识
		
		public Integer ecoin;
		public Integer  pickupScore;
		public Integer  sendScore;
		public Double balance;
		public Double validBalance;
		public Boolean completed;
		public String lastOnlineTime;
		public Double latitude;
		public Double longitude;
		public String registTime;
		public String courierAuth;
		public String realManAuth;//"y"是实名
		public String driverAuth;
		public String smsCode;
		public String wlid;//物流公司个人判断
		public Boolean temp;
	}
}
