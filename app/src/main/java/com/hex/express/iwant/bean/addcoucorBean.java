package com.hex.express.iwant.bean;

import java.util.ArrayList;
import java.util.List;

public class addcoucorBean extends BaseBean {
	public List<Data> data = new ArrayList<Data>();

	public List<Data> getData() {
		return data;
	}

	public void setData(List<Data> data) {
		this.data = data;
	}

	public class Data {
		public int  recId;//主键
		public int courierId;//快递员id
		public int userId;//用户id

		public String userName;//用户名称
		public String userMobile;//用户手机号
		public String courierName;//快递员名称
		public String courierMobile;//快递员手机号
		public int pointId;//所属网点ID
		public int expId;//快递品牌id
		public String expName;//所属品牌名称
		public String expCode;
		public int pickupScore;//快递员取件评分
		public int sendScore;//快递员送件评分

		public Boolean userDelete;//用户是否删除
		public Boolean ifDefault;//是否默认


		public Double latitude;
		public Double longitude;
		public String cityCode;

	}
}
