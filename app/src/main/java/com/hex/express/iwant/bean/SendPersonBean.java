package com.hex.express.iwant.bean;

import java.util.ArrayList;
import java.util.List;

 public class SendPersonBean extends BaseBean {
	 public List<Data> data=new ArrayList<Data>();
	 public class Data{
		 	public Integer addressId; 
			public Integer userId; 
			public String addressType;
			public String cityCode; 
			public String cityName; 
			public String personName;
			public String mobile;
			public String areaName;
			public String address;
			public Double latitude;
			public Double longitude;
			public Boolean ifDefault;
			public Boolean ifDelete;
			@Override
			public String toString() {
				return "Data [addressId=" + addressId + ", userId=" + userId
						+ ", addressType=" + addressType + ", cityCode="
						+ cityCode + ", cityName=" + cityName + ", personName="
						+ personName + ", mobile=" + mobile + ", areaName="
						+ areaName + ", address=" + address + ", latitude="
						+ latitude + ", longitude=" + longitude
						+ ", ifDefault=" + ifDefault + ", ifDelete=" + ifDelete
						+ "]";
			}
	 }
	
}
