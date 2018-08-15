package com.hex.express.iwant.bean;

import java.util.ArrayList;
import java.util.List;


public class ExerciseBean {
	public List<Data> data = new ArrayList<Data>();

	public List<Data> getData() {
		return data;
	}

	public void setData(List<Data> data) {
		this.data = data;
	}

	public class Data {
		public int activityId;    //活动Id
		public String activityTitle;   // 活动标题
		public String activityDetil;   // 活动详情简介
		public String activityUrl;     // 活动链接
		public String	gotoUrl;  //跳转的web页面
		public String imagesUrl;     //活动图片
		public int activityUrlStatus;    //活动APP跳转链接  1-领取现金券   2-签到    3—抽奖
	    public Boolean valid;   // 是否开放
	}
	public String message;//控制充值奖现金按钮
	public boolean success;//控制现金卷
	public int errCode;
}
