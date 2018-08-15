package com.hex.express.iwant.bean;

import com.hex.express.iwant.helper.EntityBase;
import com.lidroid.xutils.db.annotation.Column;

public class CityBean extends EntityBase{
	public static final String PRO_CODE = "pro_code";
	public static  final String CITY_NAME = "city_name";
	public static final String CITY_CODE = "city_code";
	public static final String PARAM = "param";
	
	public static final String DB_NAME = "city";
	
	@Column(column = PRO_CODE)
	public String pro_code ;
	
	@Column(column = CITY_NAME)
	public String city_name ;
	
	@Column(column = CITY_CODE)
	public String city_code ;
	
	@Column(column = PARAM)
	public String param ;
	
}
