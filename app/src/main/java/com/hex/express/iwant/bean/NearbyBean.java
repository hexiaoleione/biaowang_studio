package com.hex.express.iwant.bean;

import java.util.ArrayList;
import java.util.List;


public class NearbyBean {

public List<Data> data = new ArrayList<Data>();
	
	public List<Data> getData() {
		return data;
	}
	public void setData(List<Data> data) {
		this.data = data;
	}
	public class Data{
		public int recId;//主键
		 public int userId;//发件人id
		 public String cargoName;//货物名称
		 public String startPlace;//货物起始地点
		 public String entPlace;//货物到达地点
		 public Boolean takeCargo;//是否需要取货
		 public Boolean sendCargo;//是否需要送货
		 public String cargoWeight;//货物重量
		 public String cargoVolume;//货物体积
		 public String takeTime;//取货时间
		 public String arriveTime;//到达时间
		 public String takeName;//收货人姓名
		 public String takeMobile;//收货电话
		 public String remark;//备注
		 public String status;//物流状态
		 public String publishTime;//发布时间
		 public String payType;//支付方式  2-支付宝  3-微信  4-余额支付
		 public String billCode;//流水号
		 public Boolean ifDelete;// 是否删除
		 public Double latitude;//发件地纬度
		 public Double longitude;//发件地经度
		 public String startPlaceCityCode;//货物起始地点城市编码
		 public String entPlaceCityCode;//货物到达地点城市编码
		 public String sendName;
		 public String  sendMobile;
		 public String  sendNumber;
		 public String  transferMoney;
		 public String  luMessage;
		 public String  distance;
		 public String  sendCargoMoney; //s送货费
		 public String  takeCargoMoney;//上门取货费
		 public String  cargoTotal;//运费
		 public String  cargoCost;//货物价值
		 public String  yardAddress;//
		 public Boolean  ifQuotion;// 是否报价
		 public String sendPerson;    //普通版发件人姓名
		 public String sendPhone;    //普通版发件人电话
		 public String endPlaceName;
		 public String cargoNumber;
		 public String appontSpace;
		    public String matWeight;//物品重量
		    public String   cargoSize;
		    public String length; //长
		    public String wide;    //宽
		    public String high;   //;
		    public String     weight;
		    public String carType;
		    public String carName;
		    public String tem;
		    
//		 public String userToId;//个人或公司   userID
//		 public String evaluationStatus;//用户评价的状态  0->未评价 1->已评价
//		 public String evaluationScore;    //用户对(司机/公司)评价分数
//		 public String evaluationContent;   //用户对(司机/公司)评价内容
//		 public String evaluationTag;       //(司机/公司)标签
//		 public String updateTime;//用户评价时间
//		 public int quotationNumber;//报价次数
	}
	public String message;
	public boolean success;
	public int errCode;
}
