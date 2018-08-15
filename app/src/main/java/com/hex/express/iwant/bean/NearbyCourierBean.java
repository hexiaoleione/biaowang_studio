package com.hex.express.iwant.bean;

import java.util.ArrayList;
import java.util.List;

public class NearbyCourierBean extends BaseBean {
	public List<Data> data=new ArrayList<Data>();
	public class Data{
		public Integer courierId;
		public String courierName;
		public String courierMobile;
		public Integer pointId;
		public Integer expId;
		public String expName;
		public Integer pickupScore;
		public Integer sendScore;
	}
}
