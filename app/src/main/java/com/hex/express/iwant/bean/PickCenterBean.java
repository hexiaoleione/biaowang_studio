package com.hex.express.iwant.bean;

import java.util.ArrayList;
import java.util.List;

public class PickCenterBean extends BaseBean {
	public List<Data>data =new ArrayList<Data>();
	public class Data{
		public String businessId;//主键
	    public String billCode;//流水号
	    public String userId;//发件用户id
	    public String userName;//用户名
	    public String expNo;//快递单号
	    public String userIdTo;//收件人Id
	    public Boolean joinNo;  //是否连号

	    public String personName;//发件人
	    public String personNameTo;//收件人
	    public String mobile;//发件人手机号
	    public String mobileTo;//收件人手机号
	    public String areaName;//发件地区
	    public String areaNameTo;//收件地区
	    public String address;//发件详细地址
	    public String addressTo;//收件详细地址
	    public String latitude;//纬度
	    public String longitude;//经度
	    public String fromCity;//发件城市code
	    public String fromCityName;//起始城市名称
	    public String toCity;//收件城市code
	    public String toCityName;//收件城市名称
	    public Integer distance; //距离

	    public String matType;//物品类型 1-包裹 2-文件
	    public String matName;//物品名称
	    public String weight;//重量
	    public String weightRange;//重量范围
	    public String imagePath;//图片路径
	    public String counts;//件数
	    public String publishTime;//发布时间
	    
	}
}
