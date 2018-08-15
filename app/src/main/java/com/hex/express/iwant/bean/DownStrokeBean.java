package com.hex.express.iwant.bean;

import java.util.ArrayList;
import java.util.List;

public class DownStrokeBean extends BaseBean {
	public List<Data> data = new ArrayList<Data>();
	public class Data {
		public String recId; // 主键
		public Integer userId; // 司机Id
		public String userHeadPath; // 用户头像路径
		public String userName; // 司机名称
//		public String driverMobile; // 司机电话
		public String cityCodeTo; // 到达城市代码
		public String cityCode; // 出发城市代码
		public String latitude; // 当前纬度 前端
		public String longitude; // 当前经度 前端
		public Integer distance; // 距离
		public String address; // 出发地址 前端
		public String addressTo; // 到达地址 前端
		public String leaveTime; // 离开时间 前端
		public String transportationId;// 交通工具id （1：快车 2：自行车 3：步行） 前端
		public String transportationName;// 交通工具名称 前端
		public String publishTime; // 发布时间
		public String remark; // 备注 前端
		public String driverMobile; // 镖师电话
		public String fromLatitude; // 出发纬度 前端
		public String toLatitude; // 到达纬度 前端
		public String fromLongitude; // 出发经度 前端
		public String toLongitude; // 到达经度 前端
	}

}
