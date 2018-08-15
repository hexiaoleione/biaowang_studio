package com.hex.express.iwant.bean;

import java.util.ArrayList;
import java.util.List;

import android.R.integer;


public class DrawPageBean  extends BaseBean {
	public List<Data> data=new ArrayList<Data>();
	
	public class Data{
		public int eCoin;
		public String luckyTime;
	}
}
