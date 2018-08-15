package com.hex.express.iwant.bean;

import java.util.ArrayList;
import java.util.List;


public class DowInsuBeanse  extends BaseBean {
	public List<Data> data=new ArrayList<Data>();
	public class Data{
		public double smallTruckPrice;//小货车价格
		public double  middleTruckPrice;//中货车价格
		public double   smallMinibusPrice;//小面包价格
		public double  middleMinibusPrice;//中面包价格
	}
}