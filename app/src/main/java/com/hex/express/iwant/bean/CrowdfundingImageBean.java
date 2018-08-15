package com.hex.express.iwant.bean;

import java.util.ArrayList;
import java.util.List;

public class CrowdfundingImageBean extends BaseBean {
	public List<Data> data = new ArrayList<Data>();

	public List<Data> getData() {
		return data;
	}

	public void setData(List<Data> data) {
		this.data = data;
	}

	public class Data {

		public String topImageUrl;
		public String bottomImageUrl;

	}
}
