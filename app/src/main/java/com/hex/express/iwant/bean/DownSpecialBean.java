package com.hex.express.iwant.bean;

import java.util.ArrayList;
import java.util.List;

import android.R.string;

public class DownSpecialBean extends BaseBean {
	public List<Data> data = new ArrayList<Data>();

	public class Data {
		//后台生成
	    public int recId;//主键
	    public String billCode;//流水号
	    public String transferMoney;//运费
//	    public  double transferMoney;
	    public String insureCost;//保险费
	    public String premium;//保额
	    public String distance;//距离 单位km
	    public String status;//状态 0-预发布成功(未支付) 1-支付成功(已支付)  2(已抢单) 3 已取件 4 订单取消()镖师  5 成功  6 删除 7 已评价 8订单取消（用户）
	    public String payType;//支付方式  2-支付宝  3-微信  4-余额支付
	    public String carType;
	    public String dealPassword;//交易密码 六位数字
	    public String publishTime;//发布时间
	    public String limitTime;//用户期望货物被送达的时间
	    public String updateTime;//最近更新时间
	    public String finishTime;//达到时间
	    public Boolean userDelete;//用户删除标识
	    public Boolean driverDelete;//镖师删除
	    //用户第一次创建
	    public String userId;//发件人id
	    public String latitude;//当前纬度
	    public String longitude;//当前经度
	    public String personName;//发件人
	    public String mobile;//发件人手机号
	    public String address;//发件地址
	    public String cityCode;//发件城市code
	    public String fromLatitude;//发件地纬度
	    public String fromLongitude;//发件地经度
	    public String personNameTo;//收件人
	    public String mobileTo;//收件人手机号
	    public String addressTo;//收件地址
	    public String cityCodeTo;//收件城市code
	    public String toLatitude;//收件地纬度
	    public String toLongitude;//收件地经度
	    public String matName;//物品名称
	    public String matImageUrl;//物品图片url
	    public String matRemark;//物品备注
	    public String matWeight;//物品重量
	    public String length; //长
	    public String wide;    //宽
	    public String high;   //高
	    //后面创建
	    public String driverId;//镖师id
	    public String driverName;//镖师名称
	    public String driverMobile;
	    public String transferTypeId;//运送方式 1.快车，2、自行车 3.步行
	    public String transferTypeName;//运送方式名称
	    public String businessRemark;//订单备注
	    public String whether;//判断是否为假单
	    public String readyTime;//判断是否就位
	    public String ifAgree;//判断是
	    public String replaceMoney;//代收货款 
	    public String ifReplaceMoney;//
	    public String ifTackReplace;
	    public String matVolume;
	    public String carLength;
	    public String  useTime;
	    public String cargoSize;
	    public String    townCode;
	    public String      category;
	    
	    public String      payUserId;//区分找人代付id
	    public String     replaceUserId;
	    
	    
	    
	}
}
