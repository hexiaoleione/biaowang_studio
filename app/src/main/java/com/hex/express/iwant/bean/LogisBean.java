package com.hex.express.iwant.bean;

import java.util.ArrayList;
import java.util.List;

import com.hex.express.iwant.bean.CardBean.Data;

public class LogisBean extends BaseBean{
public List<Data> data = new ArrayList();

public List<Data> getData() {
	return data;
}

public void setData(List<Data> data) {
	this.data = data;
}
public class Data {
	public int recId;
	public Integer courierId;//快递员id
	public Integer userId;
	public String billCode;//快递单号
	public String evaluationStatus;//余额支付时的价格
	public String evaluationScore;//其它支付的价格
	public String playMoneyMin;//余额支付时的价格
	public String playMoneyMax;//其它支付的价格
	public String insureCost;//其
	public String remark;//其
	
}

}
