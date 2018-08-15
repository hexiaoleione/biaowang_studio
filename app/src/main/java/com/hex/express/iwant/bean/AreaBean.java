package com.hex.express.iwant.bean;

import com.hex.express.iwant.helper.EntityBase;
import com.lidroid.xutils.db.annotation.Column;

public class AreaBean extends EntityBase{
	public static final String AREA_CODE = "area_code";
	
	public static final String DB_NAME = "area";
	
	@Column(column = AREA_CODE)
	public String area_code ;
	
}

