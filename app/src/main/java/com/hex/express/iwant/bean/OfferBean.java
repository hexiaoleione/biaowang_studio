package com.hex.express.iwant.bean;

import java.util.ArrayList;
import java.util.List;


public class OfferBean {
	public List<Data> data = new ArrayList<Data>();
	
	public List<Data> getData() {
		return data;
	}
	public void setData(List<Data> data) {
		this.data = data;
	}
	public class Data{
		public String transferMoney;//报价运费
		public String takeCargoMoney;//上门取货运费
		public String sendCargoMoney;//送货上门运费
		public String cargoTotal;//运费
		public String companyName;//物流公司名称
		public String distance;//距离
		public String shipNumber;//运货次数
		public String address;//公司地址
		public String yardAddress;//公司地址
		
		public int userId;
		public int wlid;
		public float evaluation;//用户评价
		public String matImageUrl;//公司图片url
		public String realManAuth;//是否实名制认证
		public String  publishTime; //报价时间
		public String  mobile; //电话
		public String  luMessage; //留言
		
	}
	public String message;
	public boolean success;
	public int errCode;
}
