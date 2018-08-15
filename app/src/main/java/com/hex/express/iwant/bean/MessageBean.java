package com.hex.express.iwant.bean;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MessageBean {
	public List<Data> data = new ArrayList<Data>();

	public List<Data> getData() {
		return data;
	}

	public void setData(List<Data> data) {
		this.data = data;
	}

	public class Data {
		public String messageId;// id
		public Integer userId;// 用户id
		public String userName;// 用户姓名
		public String userMobile;// 用户mobile
		public String messageType;// 消息类型 1-系统消息(默认) 2-个人通知
		public String messageTitle;// 消息标题
		public String messageDesc;// 消息描述
		public String messageUrl;// 推广url
		public Integer sendAdminId;// 管理员id
		public String sendAdminName;// 管理员名称
		public Boolean ifReaded;// 是否已读
		public Boolean ifDelete;// 是否删除
		public String sendTime;
	}
	public String message;
	public boolean success;
	public int errCode;
}
