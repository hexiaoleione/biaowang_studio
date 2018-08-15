package com.hex.express.iwant.bean;

import java.util.ArrayList;
import java.util.List;

import com.hex.express.iwant.bean.CardBean.Data;

public class ChildExpressBean extends BaseBean {
	public List<Data> data = new ArrayList<Data>();
	public class Data{
	    public Integer recId;//自增主键;
	    public Integer businessId;//总单号;与ExpressHistory的billCode(String)字段对应;与ExpressExpNoParams的billCode(String)字段相对应;
	    public String childExpNo;//子单号;
	    public String childExpPrice;//子单运费;
	    public String expId;
	}
}
