package com.hex.express.iwant.bean;

import java.util.ArrayList;
import java.util.List;

public class LatesAddressBean extends BaseBean{
public List<Data> data = new ArrayList();

public List<Data> getData() {
	return data;
}

public void setData(List<Data> data) {
	this.data = data;
}
public class Data {
	public String fromAddress;//地址
	public String locationAddress;//地址
	public String address;//地址
	public double fromLatitude;//维度
	public double fromLongitude;//精度
	public String cityCode;//地址
	public String cityName;//地址

//"fromAddress": "河南省许昌市魏都区建安大道东顺河街和建安大道交叉口",
//		"locationAddress": "河南省许昌市魏都区建安大道",
//		"address": "东顺河街和建安大道交叉口",
//		"fromLatitude": 34.040221,
//		"fromLongitude": 113.841555,
//		"cityCode": "411000",
//		"cityName": "许昌市"

}

}
