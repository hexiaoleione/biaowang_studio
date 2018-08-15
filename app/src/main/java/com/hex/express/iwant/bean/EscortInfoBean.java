package com.hex.express.iwant.bean;

import java.util.ArrayList;
import java.util.List;

/**
 * 【镖师信息】
 * 
 * @author han
 * 
 */
public class EscortInfoBean extends BaseBean {
	public List<Data> data = new ArrayList<Data>();

	public List<Data> getData() {
		return data;
	}

	public void setData(List<Data> data) {
		this.data = data;
	}

	public class Data {
		public String recId; // 主键
		public String userId; // 司机Id
		public String userHeadPath; // 用户头像路径
		public String userName; // 司机名称
		public String sex; // 司机性别 "男" 或者 "女"
		public String evaluateCount; // 用户评价次数
		public String driverRouteCount;// 接镖次数
		public float synthesisEvaluate;// 综合评价
		public String driverMobile;// 镖师电话

		
	}
}
