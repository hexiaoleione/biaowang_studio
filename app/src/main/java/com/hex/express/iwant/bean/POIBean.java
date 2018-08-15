package com.hex.express.iwant.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class POIBean extends BaseBean implements Serializable{
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
		public Integer userId;//用户id
		public Integer userType;//用户类型：1-普通用户；2-快递员；3-镖师
		public Double latitude;//纬度
		public Double longitude;//经度
		public String recordTime;//记录时间
		@Override
		public String toString() {
			return "Data [useId=" + userId + ", userType=" + userType
					+ ", latitude=" + latitude
					+ ", longitude=" + longitude + ", recordTime=" + recordTime
					+ "]";
		}
		
	}
}
