package com.hex.express.iwant.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class MapPointBean extends BaseBean implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public List<Data> data=new ArrayList<Data>();
	public class Data implements Serializable{
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		public Integer recId;
		public String cityCode;
		public String pointName;
		public Double latitude;
		public Double longitude;
		public String address;
		public String streetId;
		public String mobile;
		public String uid;
		public String enableCourier;
		public Integer expId;
		public String expCode;
		public Integer pointId;
		public String manager;
		public String expName;
		public String imagePath;
		@Override
		public String toString() {
			return "Data [recId=" + recId + ", cityCode=" + cityCode
					+ ", pointName=" + pointName + ", latitude=" + latitude
					+ ", longitude=" + longitude + ", address=" + address
					+ ", streetId=" + streetId + ", mobile=" + mobile
					+ ", uid=" + uid + ", enableCourier=" + enableCourier
					+ ", expId=" + expId + ", expCode=" + expCode
					+ ", pointId=" + pointId + ", manager=" + manager
					+ ", expName=" + expName + ", imagePath=" + imagePath + "]";
		}
		
	}
}
