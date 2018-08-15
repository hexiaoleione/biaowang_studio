package com.hex.express.iwant.bean;

import java.util.ArrayList;
import java.util.List;

public class EvaluationOfEscortBean extends BaseBean {
	public List<Data> data = new ArrayList<Data>();

	public class Data {
		private String recId;     //(顺丰表主键)关联
		private String driverId;     //司机Id
		private String userId;         //用户Id
		private String driverScore;   //司机评价分数
		private String driverContent;  //司机评价内容
		private String driverTag;      //标签
		private String userScore;     //用户评价分数
		private String userContent;    //用户评价内容
		private String userTag;        //用户标签
		private String updateTime;       //更新时间
		public String getRecId() {
			return recId;
		}
		public void setRecId(String recId) {
			this.recId = recId;
		}
		public String getDriverId() {
			return driverId;
		}
		public void setDriverId(String driverId) {
			this.driverId = driverId;
		}
		public String getUserId() {
			return userId;
		}
		public void setUserId(String userId) {
			this.userId = userId;
		}
		public String getDriverScore() {
			return driverScore;
		}
		public void setDriverScore(String driverScore) {
			this.driverScore = driverScore;
		}
		public String getDriverContent() {
			return driverContent;
		}
		public void setDriverContent(String driverContent) {
			this.driverContent = driverContent;
		}
		public String getDriverTag() {
			return driverTag;
		}
		public void setDriverTag(String driverTag) {
			this.driverTag = driverTag;
		}
		public String getUserScore() {
			return userScore;
		}
		public void setUserScore(String userScore) {
			this.userScore = userScore;
		}
		public String getUserContent() {
			return userContent;
		}
		public void setUserContent(String userContent) {
			this.userContent = userContent;
		}
		public String getUserTag() {
			return userTag;
		}
		public void setUserTag(String userTag) {
			this.userTag = userTag;
		}
		public String getUpdateTime() {
			return updateTime;
		}
		public void setUpdateTime(String updateTime) {
			this.updateTime = updateTime;
		}

	}

}
