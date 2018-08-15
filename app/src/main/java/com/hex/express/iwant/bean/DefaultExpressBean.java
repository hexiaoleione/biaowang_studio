package com.hex.express.iwant.bean;

import java.util.ArrayList;
import java.util.List;

public class DefaultExpressBean extends BaseBean {
	public List<Data> data=new ArrayList<Data>();
	public class Data{
		public String fromCity;
		public String fromCityName;
		public String personName;
		public String mobile;
		public String areaName;
		public String address;
		public Double latitude;
		public Double longitude;
		public Integer courierId;
		public String courierName;
		public String expName;
	}
}
