package com.hex.express.iwant.bean;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class CrowdNewBean extends BaseBean {
	public List<Data> data = new ArrayList<Data>();

	public List<Data> getData() {
		return data;
	}

	public void setData(List<Data> data) {
		this.data = data;
	}

	public class Data {
		public int recId;// 主键
		public String newsName;// 新闻名称
		public String newsContent;// 新闻内容
		public String publishTime;// 发布时间
		public int adminId;// 发布管理员id
		public String adminName;// 发布管理员姓名
		public String pictureUrl;// 图片地址
		public String newsUrl;// 新闻对应地址
		public boolean ifDelete;// 是否被删除
	}

}
