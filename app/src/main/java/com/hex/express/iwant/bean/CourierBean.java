package com.hex.express.iwant.bean;

import java.util.ArrayList;
import java.util.List;

public class CourierBean extends BaseBean {
	public List<Data> data = new ArrayList();

	public List<Data> getData() {
		return data;
	}
	public void setData(List<Data> data) {
		this.data = data;
	}
	public class Data {
		public Integer recId;
		public Integer courierId;//快递员id
		public Integer userId;
		public String userName;
		public String userMobile;
		public String courierName;//快递员名字
		public String courierMobile;//快递员手机号
		public Integer pointId;
		public Integer expId;
		public String expName;//快递公司名称
		public Integer pickupScore;
		public Integer sendScore;
		public Boolean userDelete;
		public String expCode;
		public double latitude;
		public double longitude;
		public String cityCode;
	}
}
