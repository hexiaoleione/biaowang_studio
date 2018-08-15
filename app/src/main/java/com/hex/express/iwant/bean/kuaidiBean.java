package com.hex.express.iwant.bean;

import com.hex.express.iwant.helper.EntityBase;
import com.lidroid.xutils.db.annotation.Column;

public class kuaidiBean extends EntityBase {
	public static final String EXPCODE = "expCode";
	public static  final String EXPNAME = "expName";
	public static final String NAMEWORD = "nameWord";
	public static final String FAVORITE = "favorite";
	public static final String EXPID = "expId";
	public static final String DB_NAME = "kuaidi";
	public static final String SHOWORDER="showorder";
	@Column(column = EXPCODE)
	public String expCode ;
	
	@Column(column = EXPNAME)
	public String expName ;
	
	@Column(column = NAMEWORD)
	public String nameWord ;
	
	@Column(column = FAVORITE)
	public String favorite ;
	
	@Column(column = EXPID)
	public int expId ;
	@Column(column=SHOWORDER)
	public int orderId;
}
