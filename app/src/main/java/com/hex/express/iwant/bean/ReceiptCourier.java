package com.hex.express.iwant.bean;

import java.util.ArrayList;
import java.util.List;

/**
 * 我的快递_我的发布Bean
 * 
 * @author SCHT-50
 * 
 */
public class ReceiptCourier {
	public List<Data> data = new ArrayList<Data>();

	public List<Data> getData() {
		return data;
	}

	public void setData(List<Data> data) {
		this.data = data;
	}

	public class Data {
		public int billCode;// 流水账号
		public int businessId;// 主键
		public String personNameTo;// 收件人
		public String addressTo;// 收件详细地址
		public String matName;// 物品名称
		public String publishTime;// 发布时间
		public String status;// 状态code：0-已发布，1-已接单，2-已取件(已支付),6-已到货,7-已评价,8-用户中途取消，9-已退件
		public String statusName;// 状态名称
		public String courierName;// 快递员名称
		public String showLevel;// 显示级别 00-显示快递员/状态/时间 01-显示收件人/目的地/物品/状态/时间

	}

	public String message;
	public boolean success;
	public int errCode;
}
