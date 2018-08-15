package com.hex.express.iwant.bean;

import java.util.ArrayList;
import java.util.List;

public class NotPermitted {

	public List<Data> data = new ArrayList<Data>();

	public List<Data> getDataList() {
		return data;
	}

	public void setDataList(List<Data> data) {
		this.data = data;
	}

	
	public class Data {
		public int recId;//报价表主键
		public int quoRecId;//报价表主键
		public int publishUserId;//发件人id
		public int companyUserId;//报价公司或者个人id
		public String cargoName;//货物名称
		public String startPlace;//货物起始地点
		public String entPlace;//货物到达地点
		public String cargoWeight;//货物重量
		public String cargoVolume;//货物体积
		public String publishTime;//发布时间
		public String  playMoneyMin;//物流模块发布报价时   使用余额支付的平台使用费
		public String playMoneyMax;//物流模块发布报价时   使用 微信支付或支付宝支付 的平台使用费
		public String 	billCode;
		
		
	}

	public String message;
	public boolean success;
	public int errCode;
}
