package com.hex.express.iwant.bean;

import java.util.ArrayList;
import java.util.List;

import com.hex.express.iwant.bean.LoisattenBean.Data;

public class LoOfferBean {
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
		public String takeCargo;//是否需要取货
		public String sendCargo;//是否需要送货
		public String cargoWeight;//货物重量
		public String cargoVolume;//货物体积
		public String takeTime;//取货时间
		public String arriveTime;//到达时间
		public String takeName;//收货人姓名
		public String takeMobile;//收货电话
		public String remark;//备注
		public String status;//物流状态
		public String publishTime;//发布时间
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
		public String  firstPicture;       //第一张货物图片url
		public String  secondPicture;       //第二张货物图片url
		public String  thirdPicture;       //第三张货物图片url
		public String takeCargoMoney;//取货费
		public String sendCargoMoney;//送货费
		public String cargoTotal;//运费
		public String cargoCost;//货物价值,用来计算保费
		public String luMessage;//留言
		public Integer userToId;//公司或个人  id
		public String reportTime;//报价时间
		public String companyName;//物流公司名称
		public String shipNumber;//运货次数
		public String address;//公司地址
		public Double evaluation;//用户评价
		public String matImageUrl;//公司图片url
		public String realManAuth;//实名认证
		public String companyMobile;//公司手机号
		public String	yardAddress;
		public Double	insureCost;
		public String	category;
		public String	insurance;
		 public String sendPerson;    //普通版发件人姓名
		 public String sendPhone;    //普通版发件人电话
		 public String cargoNumber;  
		 public String appontSpace;
		 public String 	 length;
		 public String 	 wide;
		 public String 	 high;
		 public String 	 pdfURL;
		 public String 	 weight;
		 public String 	 cargoSize;
		 public String 	 wlid;
		 public String 	 payTime;
		 public String 	 whether;
		 public String 	 latitudeTo;
		 public String 	 longitudeTo;
		 public String 	 endPlaceName;
		 
		 public String 	 carType;
		 public String 	 carName;
		  public String tem;
	}
	public String message;
	public boolean success;
	public int errCode;

}
