package com.hex.express.iwant.bean;

import java.util.ArrayList;
import java.util.List;

public class PublicCourierBean extends BaseBean {
	public List<Data> data = new ArrayList<Data>();

	public class Data {
		public String businessId;// 主键
		public String billCode;// 流水号 // TODO
		public String userId;// 发件用户id
		public String userName;// 用户名7
		public String expNo;// 快递单号
		public String userIdTo;// 收件人Id // TODO

		/** 发件人收件人信息 **/
		public String personName;// 发件人
		public String personNameTo;// 收件人
		public String mobile;// 发件人手机号
		public String mobileTo;// 收件人手机号
		public String areaName;// 发件地区
		public String areaNameTo;// 收件地区
		public String address;// 发件详细地址
		public String addressTo;// 收件详细地址
		public Double latitude;// 纬度
		public Double longitude;// 经度
		public String fromCity;// 发件城市code
		public String fromCityName;// 起始城市名称
		public String toCity;// 收件城市code
		public String toCityName;// 收件城市名称

		/*** 物品信息 ****/
		public String matType;// 物品类型
		public String matName;// 物品名称
		public Float weight;// 重量
		public String weightRange;// 重量范围
		public String imagePath;// 图片路径
		public String counts;// 件数

		/*** 快递公司信息 ***/
		public String expId;// 快递公司id
		public String expName;// 快递公司名称
		public String pointId;// 快递网点Id
		public String pointName;// 快递网点名称
		public String courierId;// 快递员id
		public String courierName;// 快递员名称
		public String courierMobile;// 快递员手机号

		public String publishTime;// 发布时间
		public String contendTime;// 抢单时间
		public String payTime;// 支付时间
		public String pickTime;// 取件时间
		public String receiveTime;// 收件时间
		public String planFinishDate;// 如果未取件,自动默认取件时间

		/** 费用和支付信息 **/
		public Double shipMoney;// 运费
		public Double insureMoney;// 保价费
		public Double insuranceFee;// 保额
		public Boolean payed;// 是否支付
		public Integer payPrepared;// 支付方式，0-未确定，1-现付，3-协议付（月结），2-到付
		public String payType;// 支付类型，1-现金支付，2-支付宝，3-微信支付，4-余额支付
		public String ecoin;// 使用E币数
		public Double ecoinMoney;// E币抵扣金额
		public Integer userCouponId;// 使用现金券id
		public String userCouponName;// 使用现金券名称
		public String couponMoney;// 现金券抵扣金额
		public String needPayMoney;// 需要支付的总金额数

		public String status; // 状态：0-已发布，1-已接单，2-已取件(已支付),  3 抢单发布未被抢 4 快递员已抢单,6-已到货,7-已评价,8-用户中途取消，9-已退件
		public String statusName;
		public String assigned;// 指定类型 N-未指定(抢单) U-用户指定 P-网点指定
		public Boolean userDelete;// 用户删除标记
		public Boolean courierDelete;// 快递员删除标记

		public String pointStatus;// 该网点的快递员能否使用在线支付 1==已开通在线支付 ,未开通!=1

	}

}
