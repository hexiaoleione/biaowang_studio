package com.hex.express.iwant.bean;

import java.util.ArrayList;
import java.util.List;

public class EscoreAuthenticateBean extends BaseBean {
	public List<Data> data = new ArrayList<Data>();

	public List<Data> getData() {
		return data;
	}

	public void setData(List<Data> data) {
		this.data = data;
	}

	public class Data {
		public int userId;// 用户id
		public String userName;// 用户姓名
		public String userType;// 用户类型
		public String idCard;// 身份证号
		public String idCardPath;// 身份证号
		public String bankName;// 银行名称
		public String bankCard;// 银行卡卡号
		public int status;// 认证状态 0-未认证 1-已驳回 2-已通过
		public String statusName;// 状态名称
		public String applyTime;// 申请时间
		public String updateTime;// 更新时间
	}
}
