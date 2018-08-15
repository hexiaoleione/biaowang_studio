package com.hex.express.iwant.bean;

import java.util.ArrayList;
import java.util.List;

import com.hex.express.iwant.bean.AgentUserBean.Data;

public class AgentUBean extends BaseBean {
	public List<Data> data = new ArrayList<Data>();
	public List<Data> getData() {
		return data;
	}
	public void setData(List<Data> data) {
		this.data = data;
	}
	public class Data {
//		public int userId;
		public String type;//类型
		public String pushime;//时间1
		public String money;//钱
		public String userName;//姓名
		public String headPath;//图片
	}

  

}
