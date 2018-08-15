package com.hex.express.iwant.bean;

import com.hex.express.iwant.helper.EntityBase;
import com.lidroid.xutils.db.annotation.Column;
import com.lidroid.xutils.db.annotation.Table;

@Table(name = "provice")
public class ProviceBean extends EntityBase{
	
	
	public static final String PRO_CODE = "pro_code";
	public static  final String PRO_NAME = "pro_name";
	public static final String DBNAME = "provice";
	@Column(column = PRO_CODE)
	public String pro_code ;
	@Column(column = PRO_NAME)
	public String pro_name ;
}
