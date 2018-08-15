package com.hex.express.iwant.bean;

import java.util.ArrayList;
import java.util.List;


public class ReleaseBean extends BaseBean {
	public List<Data> data = new ArrayList<Data>();

	public class Data {
		//后台生成
	    public int recId;//主键
	    public String limitPrice;//专送价格
	    public String dowPrice;//顺风价格	
	    public String logisPrice;// 物流价格
	    public String showType;//// 显示方式  1 三种价格全部显示   2 只显示物流
	    public String chitType;// 推荐模式  1 顺风 2 专送  3 物流 4 无
	    public double distance; //运送距离
	    public String weatherMessage;//
	    public String   insureUrl;
	    
	    
	}
}
