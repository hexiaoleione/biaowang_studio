package com.hex.express.iwant.bean;

import java.util.ArrayList;
import java.util.List;

public class IconBean extends BaseBean {
	public List<Data> data=new ArrayList<Data>();
	public class Data{
		public String filePath;
		public Integer userId;
		public String fileName;
		public String internetPath;
	}
	
}
