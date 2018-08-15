package com.hex.express.iwant.bean;

import java.util.ArrayList;
import java.util.List;


public class FreigBean {
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
		public String distance;//距离
	    public String startPlace;//货物起始地点
	    public String entPlace;//货物到达地点
	    public boolean takeCargo;//是否需要取货
	    public boolean sendCargo;//是否需要送货
	    public String cargoVolume;//货物体积
	    public String  cargoCost;//价值
	    public String takeTime;//取货时间
	    public String arriveTime;//到达时间
	    public String takeName;//收货人姓名
	    public String takeMobile;//收货电话
	    public String remark;//备注
	    public int status;//物流状态
	    public String publishTime;//发布时间
	    public String payType;//支付方式  2-支付宝  3-微信  4-余额支付
	    public String billCode;//流水号
	    public String ifDelete;// 是否删除
	    public String latitude;//发件地纬度
	    public String longitude;//发件地经度
	    public String startPlaceCityCode;//货物起始地点城市编码
	    public String entPlaceCityCode;//货物到达地点城市编码
	    public String sendName;//发货人姓名
	    public String sendMobile;//发货人电话
	    public String sendNumber;//发货次数
	    public String transferMoney;//报价运费
	    public String luMessage;//留言
	    public String  firstPicture;       //第一张货物图片url
	    public String  secondPicture;       //第二张货物图片url
	    public String  thirdPicture;       //第三张货物图片url
	    public String  sendCargoMoney; //s送货费
		 public String  takeCargoMoney;//上门取货费
		 public String  cargoTotal;//运费
		 public String endPlaceName;
		 public String sendPerson;
		 public String sendPhone;
		 public String 	 carNumImg ;//车牌号
		 public String 		 appontSpace;
		 public String 	 length;
		 public String     wide;
		 public String     high;
		 public String    weight;
		 public String   cargoSize;
		 public String    carType;
		 public String   carName;
		 public String 	 cargoWeight;
		 public String   tem;
	}
	public String message;
	public boolean success;
	public int errCode;
}
