package com.hex.express.iwant.bean;

import java.util.ArrayList;
import java.util.List;

public class VersionBean extends BaseBean {
	public List<Data> data = new ArrayList<Data>();
	public class Data {
		public Integer recId;
		public String androidVersion;
		public String androidApkPath;
		public String iosVersion;
		public String updateTime;
		public String updateContent;
		public String  ifUpdate;
	}
}
