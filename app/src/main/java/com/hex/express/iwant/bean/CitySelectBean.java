package com.hex.express.iwant.bean;

import java.io.Serializable;

/**
 * 通用于省份和城市和地区
 * @author bruce
 * @date 2015-12-1
 */
public class CitySelectBean implements Serializable{

	/**
	 * @author bruce
	 */
	private static final long serialVersionUID = 2793613176989319759L;
	
	private String code;
	private String name;
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
}
