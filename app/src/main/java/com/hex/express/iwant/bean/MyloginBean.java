package com.hex.express.iwant.bean;

import java.util.ArrayList;
import java.util.List;


public class MyloginBean {
public List<Data> data = new ArrayList<Data>();
	
	public List<Data> getData() {
		return data;
	}
	public void setData(List<Data> data) {
		this.data = data;
	}
	public class Data{
		public int	recId;//信息id
		   public int userId;//发件人id
		   public String userToId;//wuliurend 
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
		   
		    public String publishTime;  //推送时间
		    public String status;//是否发布
		    public String   quotationNumber;
		    public String  billCode;
		    public String  whether;
		    public String  premium;
		    public String  category;
		    public String  insurance;
		    public String  cargoCost;  //价值
		    public double insureCost;//保险费
		    public String  sendName;
		    public String  sendMobile;  
		    public String   carNumImg;
		    public String sendPerson;    //普通版发件人姓名
		    public String sendPhone;    //普通版发件人电话
		    public String pdfURL; 
		    public String remark;//保单号
		    public String cargoNumber;//
		    public String  appontSpace;
		    
		    public String  length;
		    public String  wide;
		    public String  high;
		    public String  weight;
		    public String  cargoSize;
		    public String  carType;
		    public String  carName;
		    public String  tem;
		    
	}
	public String message;
	public boolean success;
	public int errCode;
}
