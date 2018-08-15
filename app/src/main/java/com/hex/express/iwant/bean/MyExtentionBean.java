package com.hex.express.iwant.bean;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class MyExtentionBean extends BaseBean {
	public List<Data> data = new ArrayList();

	public List<Data> getData() {
		return data;
	}
	public void setData(List<Data> data) {
		this.data = data;
	}
	public class Data {
		public Integer courierId;//快递员id
		public String courierName;//快递员名字
		public String courierHeadPath;//快递员的 头像地址
		public Integer allCount;//总单
		public BigDecimal allMoney;//总收入
		public Integer todayCount;//今日单
		public BigDecimal todayMoney;//今日收入
		public String expName;//快递公司名称
		public Integer rankingOrder;//排序字段
	}
}
