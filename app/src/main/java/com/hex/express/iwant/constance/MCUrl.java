package com.hex.express.iwant.constance;


public class MCUrl {
    //	http://123.57.239.64:8080/appservice-1.0-SNAPSHOT/webapi
//	public static final String BASE ="http://192.168.1.102:8080/appservice/webapi";//本地服务器
//    public static final String BASE = "http://116.90.87.32:8888/appservice-1.0-SNAPSHOT/webapi";//增宝服务器
	public static final String BASE ="http://www.efamax.com:8888/appservice-1.0-SNAPSHOT/webapi";//线上 服务器
//	public static final String BASE ="http://beta.efamax.com:8888/appservice-1.0-SNAPSHOT/webapi";//beta 服务器

    public static final String REGISTER = BASE + "/users";
    public static final String SMSCODE = BASE + "/sms/smscode";
    public static final String IMPROVE_IN = BASE + "/users/realUser";
    public static final String register = BASE + "/users/register";//注册的新接口
    public static final String IMPROVE_INFO = BASE + "/users";
    public static final String LOGIN = BASE + "/users/userLogin";
    public static final String ADDRESS = BASE + "/address";
    public static final String SENDPERSON = BASE + "/address/list";
    public static final String CORUIERS_LIST = BASE + "/mycourier/list";
    public static final String Add_COURIER = BASE + "/mycourier";
    public static final String EXPRESS = BASE + "/express";
    public static final String COUPON = BASE + "/coupon/list";
    public static final String WALLET = BASE + "/wallet/list";
    public static final String ECOIN = BASE + "/ecoin/list";
    public static final String THIRDLOGIN = BASE + "/users/thirdLogin";
    public static final String MYEXPRESS = BASE + "/express/list";
    public static final String BALANCE = BASE + "/users/user";
    public static final String SYSTEM = BASE + "/system/list";
    public static final String EXPRESS_DEFAULT = BASE + "/express/default";
    public static final String MAPPOINT = BASE + "/mappoint/nearby";
    public static final String UPLOAD_ICON = BASE + "/file/idcard";
    public static final String FIND_CODE = BASE + "/sms/forgetcode";
    public static final String RESET_CODE = BASE + "/users/userPwd";
    public static final String RECEIVER_LIST = BASE + "/address/list/receiver";
    public static final String NEARBY_COURIER = BASE + "/nearbyCourier/list";
    public static final String EXPNO = BASE + "/express/expno";
    public static final String RECEIVE = BASE + "/express/list/receive";
    public static final String EVALUATION = BASE + "/evaluation";
    public static final String WITHDRAW = BASE + "/withdraw";
    public static final String ALIPAYINFO = BASE + "/withdraw/default";//支付宝账号和昵称
    public static final String WITHDRAWMYLEFT = BASE + "/withdraw/myLeft";
    public static final String TRANFERMYLEFT = BASE + "/tranfermoney/myLeft";
    public static final String TRANFERMONEY = BASE + "/tranfermoney";
    public static final String BALANCE_MONEY = BASE + "/express/pay/balance";
    public static final String PAYPARAMS = BASE + "/express/payparams";
    public static final String ARRIVED = BASE + "/express/pay/arrived";
    public static final String MONTH = BASE + "/express/pay/month";
    public static final String FEEDBACK = BASE + "/feedback";
    public static final String VERSION = BASE + "/version";
    public static final String REMOVE = BASE + "/address/remove";
    public static final String GIFT_MESS = BASE + "/luckydraw/daily";
    public static final String GIFT_NUM = BASE + "/luckydraw/draw";
    public static final String MESSAGEREAD = BASE + "/system/read/";
    public static final String MESSAGEDELETE = BASE + "/system/delete/";
    public static final String LOGINAUTH = BASE + "/sms/loginauth";
    public static final String CHECKLOGIN = BASE + "/sms/checkloginauth";
    public static final String LOGINBINDING = BASE + "/users/thirdloginbinding";
    public static final String ADVERTISE = BASE + "/advertise";
    public static final String CROWDFUNDING = BASE + "/crowdfunding/info";
    public static final String CROWDCHECK = BASE + "/crowdfunding/check";
    public static final String COMPANYNEWS = BASE + "/companynews";
    public static final String CROWDFUNDINGIMAGE = BASE + "/crowdfunding/image";
    public static final String ERRLOGUPLOAD = BASE + "/file/errLogUpload";
    public static final String ALIPAYDISCOUNT = BASE + "/alipay/discount";
    public static final String EXPRESSPAYWECHAT = BASE + "/express/pay/wechat/pre/v2";
    public static final String WECHATRECHARGEPRE = BASE + "/wechat/recharge/pre";
    public static final String MAPPOINTKEYWORD = BASE + "/mappoint/keyword";
    public static final String COURIERSPREADMYUNDER = BASE + "/courierspread/myunder";
    public static final String MONEYINLOGRESULT = BASE + "/moneyinlog/result";
    public static final String USERSAUTHENTICATE = BASE + "/users/authenticate/courier";
    public static final String MYPICKUP = BASE + "/express/mypickup/";
    public static final String MYPICKBYDATE = BASE + "/express/mypickbydate/";
    public static final String INCOMEEXPRESSDAYWEEKMONTH = BASE + "/income/express/dayweekmonth/";
    public static final String RACHARGECHANGE = BASE + "/recharge/chance";
    public static final String MYPASSWORD = BASE + "/users/paypassword";
    public static final String eolinker = "http://result.eolinker.com/eGBQb5J99575eb9175dbb71a0d5c2e1c6cb30b3062e49dd?uri=www.eolinker.com/api/demo";
    public static final String RESETPAYPASSWORD = BASE + "/users/resetpaypassword";//重置密码
    public static final String DOWNWINDTASKPUBLISH = BASE + "/downwind/task/publish";//发布顺风任务
    public static final String DOWNWINDTASKPUBLISHS = BASE + "/downwind/task/publishTask";//发布顺风任务新接口
    public static final String FILEDOWNWIND = BASE + "/file/downwind";//顺风照片
    public static final String EXPRESSPAYPARMS = BASE + "/express/payparams/v2";//一键快递多单发布
    public static final String EXPRESSV2 = BASE + "/express/list/childexp";//获取未完成的订单
    public static final String COURIERSCOREANDLV = BASE + "/users/courierScoreAndLV";// 快递员的等级和评分
    public static final String USERCREDIT = BASE + "/credit/userCredit";// 用户信用
    public static final String WALLETINFO = BASE + "/user/balance/walletinfo";// 用户钱包信息
    public static final String AUTHENTICATE = BASE + "/users/authenticate/caravanguardauthenticate";// 用户钱包信息
    public static final String DRIVERROUTE = BASE + "/driver/driverRoute";// 镖师发布顺风行程
    public static final String DRIVERROUTELIST = BASE + "/driver/driverRouteList";// 附近的镖师发布的顺风行程列表
    public static final String DOWNWINDTASKLIST = BASE + "/downwind/task/downwindTask";// 附近的用户发布的镖件列表
    public static final String FindDOWNWINDTASKLIST = BASE + "/downwind/task/driverFindDownwindTask";// 附近的用户发布的镖件列表

//    public static final String DOWNROBORDER = BASE + "/downwind/task/robOrder";// 镖师抢单接口;
    public static final String DOWNROBORDER = BASE + "/downwind/task/robOrderNew";// 镖师抢单接口;
    public static final String DOWNGETTASK = BASE + "/downwind/task/getTask";// 货主发布总单数接口;
    public static final String DRIVERTASKROUTELIST = BASE + "/driver/downwindTaskRouteList";// 镖师接的总单数接口;
    public static final String downwindTaskRouteListType = BASE + "/driver/downwindTaskRouteListType";   //镖师查询所有已接的顺风
    public static final String getTaskByType = BASE + "/downwind/task/getTaskByType";  //货主发布总单数接口;

    public static final String EXPRESSDELETE = BASE + "/express/delete";// 用户端我的接单删除接口;
    public static final String EXPRESSDELETECOURIER = BASE + "/express/delete/courier";// 快递端我的接单删除接口;
    public static final String DRIVERDETAIL = BASE + "/driver/driverDetail";// 镖师的详细信息;
    public static final String DRIVERISTASK = BASE + "/driver/driverIsTake";// 镖师确定取货发送确认密码;
    public static final String DRIVERTRUETASK = BASE + "/driver/driverTrueTake";// 镖师完成镖件核对密码;
    public static final String DRIVERREFUNDREIMBURSE = BASE + "/driver/refundReimburse";// 顺风镖师退款接;
    public static final String DiverCance = BASE + "/driver/diverCance";// 顺风镖师退款接(新);
    public static final String ReplayMoney = BASE + "/driver/tackReplayMoney";// 顺风镖师代收款;

    public static final String DRIVEREVALUATE = BASE + "/evaluation/driverEvaluate";// 镖师的被评价信息;
    public static final String ESCOREEVALUATE = BASE + "/evaluation/addEvaluateToDriver";//添加镖师评价;
    public static final String GETUNREADSYSMSG = BASE + "/system/getUnReadSysMsg";//查看是否有未读信息;
    public static final String DRIVERROUTETOONE = BASE + "/driver/driverRouteToOne";//镖师发布的一条行程;
    public static final String CANCLEDRIVERROUTETOONE = BASE + "/driver/backOfDriverRoute";//取消镖师发布的一条行程;
    public static final String EXPRESSLIST = BASE + "/express/expressList";//快递员抢单大厅接口;
    public static final String GRABEXPRESSLIST = BASE + "/express/grabExpress";//快递员抢单大厅接口;
    public static final String NEWEXPRESS = BASE + "/express/newExpress";//用户的抢单发布	;
    public static final String IDCDHEAD = BASE + "/file/head";//用户上传头像
    public static final String CHECKADDCHECKINFO = BASE + "/check/addCheckInfo";//用户上传身份证实名认证的接口
    public static final String UPLOADMYLOCATION = BASE + "/location/upload";//用户上传自己的位置
    public static final String CANCELDOWNWINDTASK = BASE + "/downwind/task/cancelDownwindtask";//货主取消订单接口
    public static final String POI_OF_NEARBY = BASE + "/location/nearby";//获取附近的快递员/镖师
    public static final String DRIVERROUTEPATH = BASE + "/driver/driverRoutePath";//获取该镖件/快递的镖师/快递员的最新的坐标
    public static final String DOWNWINDTASKLIMITTIME = BASE + "/downwind/task/downwindTaskLimitTime";//
    public static final String DOWNWINDTASKLIMITTIMEse = BASE + "/downwind/task/driverFindLimitDowWind";//

    public static final String DOWNWINDTASKLIMpice = BASE + "/downwind/task/dowWindPrice";//
    public static final String PUblishTaskNew = BASE + "/downwind/task/publishTaskNew";//

    public static final String DeleteAll = BASE + "/system/deleteAll/";//  删除所有消息
    public static final String LOGISTICS = BASE + "/logistics/task/publish";//  物流发货
    public static final String LOGISTICSLIST = BASE + "/logistics/task/list";//  我的物流
    public static final String LOGISTICSLIST_DEKETE = BASE + "/logistics/task/delete/";//  删除我的报价
    public static final String COMPANY = BASE + "/quotation/publish";//  物流报价
    public static final String COMPANYUP = BASE + "/quotation/update";//  物流从新报价
    public static final String DeleteLog = BASE + "/logistics/task/deleteLog";//

    public static final String PERSON = BASE + "/logistics/register/person";//  物流个人认证
    public static final String PERSONCOMOANY = BASE + "/logistics/register/company";//  物流公司认证
    public static final String OFFer = BASE + "/quotation/list";//  货运报价
    public static final String RestartQuo = BASE + "/logistics/task/restartQuo";//  重选报价

    public static final String NEARBY = BASE + "/logistics/task/look/nearBy";//  公司附近货源
    public static final String AREA = BASE + "/logistics/task/look/area";//  公司地区货源
    public static final String LINE = BASE + "/logistics/task/look/line";//  公司长途货源
    public static final String TASKFIND = BASE + "/logistics/task/find";//  公司物流显示

    public static final String ATTENT_LIST = BASE + "/logistics/line/list";//  查看路线
    public static final String ATTENT_NEARBY = BASE + "/logistics/task/look/nearBy";//  查看路线 附近
    public static final String ATTENT_DIQU = BASE + "/logistics/task/look/area";//查看路线 地区
    public static final String ATTENT_ADD = BASE + "/logistics/line/add";//  添加路线
    public static final String ATTENT_Delete = BASE + "/logistics/line/delete";//  删除路线
    public static final String LOGISCOMPANIMGFIRST = BASE + "/file/first";//  图片1
    public static final String LOGISCOMPANIMGSECOND = BASE + "/file/second";//  图片2
    public static final String LOGISCOMPANIMGTHIRD = BASE + "/file/third";//  图片3
    public static final String LOGISCORECIVE = BASE + "/logistics/task/recive";//  图片上传

    public static final String THINKCHANGE = BASE + "/system/agentNew/scanLogin";//  扫二维码登录
    public static final String BALANCEPAY = BASE + "/logistics/task/balancePay";//  用户发物流支付
    public static final String BALANCEPAYCOMPAY = BASE + "/quotation/balancePay";//  公司报价支付
    public static final String AgentCount = BASE + "/users/perAgentCount";// 人人代理推广人数
    public static final String UserBalance = BASE + "/user/balance/getUserBalance";// 人人代理推广钱
    public static final String AgentUserList = BASE + "/users/perAgentUserList";// 人人代理推广列表
    public static final String AgentUserListnew = BASE + "/users/perAgentUserListNew";// 人人代理推广新接口

    public static final String Balance = BASE + "/logistics/task/pay/balance";// 物流用户支付
    public static final String WPRE = BASE + "/logistics/task/pay/wechat/pre";// w微信预支付
    public static final String CARID = BASE + "/file/cardId";// 个人物流驾驶证
    public static final String DREVER = BASE + "/file/driverLicense";// 个人物流驾驶证
    public static final String IDCARIDCom = BASE + "/file/company";// 个人物流驾驶证
    public static final String Modift = BASE + "/users/modifyMobile";// 修改手机号
    public static final String NewMobile = BASE + "/sms/newMobileCode";// 修改手机的验证码
    public static final String CHECK = BASE + "/logistics/task/check";//
    public static final String ARRIVE = BASE + "/logistics/task/arrive";//
    public static final String Advertise = BASE + "/advertise/getAdvice";// 广告页
    public static final String getAdvert = BASE + "/activity/getAdvert";// 广告页2
    public static final String getAdvertNew = BASE + "/activity/getAdvertNew";// 广告页2
    public static final String LoginCoun = BASE + "/users/loginCount";// 登录次数
    public static final String ValidRules = BASE + "/activity/getValidRules";// 广告列表
    public static final String ValidRulesNew = BASE + "/activity/getValidRulesNew";// 广告列表\
    public static final String validRulesNew = BASE + "/activity/validRulesNew";// 广告列表xin

    public static final String Recrive = BASE + "/coupon/receiveList";// 获取现金卷
    public static final String RecriveCoupon = BASE + "/coupon/receiveCoupon";// 领取现金卷
    public static final String Singnlist = BASE + "/ecoin/signlist";//
    public static final String Singn = BASE + "/ecoin/sign";//
    public static final String LuckyDial = BASE + "/luckydraw/luckyDial";// 获取抽奖的概率
    public static final String DrawPage = BASE + "/luckydraw/drawPage";// 获抽奖信息
    public static final String Draw = BASE + "/luckydraw/award";// 领奖
    public static final String QuotationInfo = BASE + "/logistics/task/quotationInfo";// 查看个人物流信息
    public static final String info = BASE + "/logistics/task/info";// 查看个人物流信息

    public static final String Chose = BASE + "/logistics/task/chose";//
    public static final String AndUserId = BASE + "/moneyinlog/findPayResultAndUserId";// 物流公司报价费微信和支付宝支付结果moneyinlog/findPayResultAndUserId

    public static final String LOGISCOMPANIMGFIRSTOpen = BASE + "/file/idCardOpen";//  图片1身份证正面
    public static final String LOGISCOMPANIMGSECONDOpens = BASE + "/file/idCardObverse";//  图片2身份证反面
    public static final String LOGISCOMPANIMGTHIRDLice = BASE + "/file/businessLicense";//  图片3 营业执照
    public static final String DowInsura = BASE + "/downwind/task/dowInsurance";//  获取保费率
    public static final String Loin = BASE + "/logistics/task/logInsurance";//  获取保费率
    public static final String Driver = BASE + "/file/driverLicense";//上传驾驶证照片
    public static final String CarNumber = BASE + "/file/getCarNumber";//上传车牌号照片
    public static final String Driving = BASE + "/file/getDrivingLicense";//上传行驶证照片
    public static final String LogComAddList = BASE + "/logistics/task/logComAddList";//获取货场地址
    public static final String AddComAdd = BASE + "/logistics/task/addComAdd";//  添加 货场 地址
    public static final String UpdateComAdd = BASE + "/logistics/task/updateComAdd";//      修改 货场 地址
    public static final String DefaultComAdd = BASE + "/logistics/task/defaultComAdd";//   获取默认货场地址
    public static final String WlCarNum = BASE + "/file/wlCarNum";//   上传车牌号照片
    public static final String LogAddInsurance = BASE + "/logistics/task/logAddInsurance";//   上传货物价值
    public static final String PublishHn = BASE + "/logistics/task/publishHn";// 海南专属
    public static final String EndPolicy = BASE + "/logistics/task/EndPolicy";// 海南改单

    public static final String HPublishHn = BASE + "/file/wlCarNumHn";// 海南专属
    public static final String FindHn = BASE + "/logistics/task/findHn";// 海南专属
    public static final String PublishNew = BASE + "/logistics/task/publishNew";// 发布物流新接口
    public static final String CustomerChoose = BASE + "/driver/customerChoose";// 用户是否同意
    public static final String OnReady = BASE + "/downwind/task/onReady";// 就位
    public static final String RemoveDow = BASE + "/driver/removeDow";// h货物违规
    public static final String Illegal = BASE + "/file/illegal";// h货物违规
    public static final String Accusation = BASE + "/file/accusation";// 投诉照片
    public static final String Accusations = BASE + "/feedback/accusation";// 投诉照
    public static final String HaiCarNumber = BASE + "/logistics/task/carNumber";// 海南查看车牌号
    public static final String HaiDeleteCarNum = BASE + "/logistics/task/deleteCarNum";// 海南删除查看车牌号
    public static final String DeldeteDow = BASE + "/downwind/task/deldeteDow";// 删除查
    public static final String WaitPayList = BASE + "/quotation/waitPayList";// 未缴费
    public static final String OnePay = BASE + "/quotation/onePay";// yijiaan缴费
    public static final String OneKeyPay = BASE + "/quotation/oneKeyPay";// yijiaan缴费
    public static final String buyEcoinRule = BASE + "/ecoin/buyEcoinRule";// y
    public static final String buyEcoin = BASE + "/ecoin/buyEcoin";// y
    public static final String exchangeCoupon = BASE + "/ecoin/exchangeCoupon";// y

    public static final String getReceiveList = BASE + "/coupon/getReceiveList";// 新的现金券
    public static final String getExtend = BASE + "/wallet/getExtend";// 推广钱
    public static final String driverActivity = BASE + "/activity/driverActivity";// 镖师推广
    public static final String driverActivityReceive = BASE + "/activity/driverActivityReceive";// 镖师推广提现
    public static final String loginRewardInfo = BASE + "/activity/loginRewardInfo";// 登录奖励
    public static final String getLoginReward = BASE + "/activity/getLoginReward";// 登录奖励领取
    public static final String afterPublish = BASE + "/downwind/task/afterPublish";// 推送

    public static final String getPriceByType = BASE + "/downwind/task/getPriceByType";// 新的接口
    public static final String replacePay = BASE + "/downwind/task/replacePay";// 我的代付列表
    public static final String replace = BASE + "/downwind/task/replace";// 我的代付
    public static final String carType = BASE + "/downwind/task/carType";// 车型类型
    public static final String getCagoType = BASE + "/logistics/task/getCagoType";// 货物类型

    public static final String trueTake = BASE + "/driver/trueTake";// 无密码完成
    public static final String coupon = BASE + "/downwind/task/coupon";// 无密码完成
    public static final String publishInsure = BASE + "/logistics/task/publishInsure";// 发布物流新直接投保

    public static final String addInsurance = BASE + "/logistics/task/addInsurance";// 添加投保信息
    public static final String payInsure = BASE + "/logistics/task/payInsure";// 余额支付
    public static final String publishInsureNew = BASE + "/logistics/task/publishInsureNew";// z直接投保
    public static final String smscodeSend = BASE + "/sms/smscodeSend";// 新的短信验证
    public static final String compare = BASE + "/sms/compare";// 新的短信验证校验
    public static final String shopDoor = BASE + "/file/shopDoor";//  商户 门头照
    public static final String shopLicense = BASE + "/file/shopLicense";//  商户 营业执照
    public static final String shopIdCard = BASE + "/file/shopIdCard";//  商户 身份证照片
    public static final String addChapman = BASE + "/check/addChapman";//  商户 认证
    public static final String getChapman = BASE + "/check/getChapman";//  商户信息
    public static final String driverMoney = BASE + "/driver/driverMoney";//  押金钱
    public static final String driverRefund = BASE + "/driver/driverRefund";//  押金钱

    public static final String driveInfo = BASE + "/driver/driveInfo";//
    public static final String insureList = BASE + "/logistics/task/insureList";//
    public static final String driveSafe = BASE + "/driver/driveSafe";//

    public static final String getDriverSafe = BASE + "/driver/getDriverSafe";//
    public static final String getSmscode = BASE + "/users/getSmscode";// 新的短信验证
    public static final String upDatePaypassword = BASE + "/users/upDatePaypassword";// 新的短信验证

    public static final String getIfHaveAnswerRecord = BASE + "/system/question/getIfHaveAnswerRecord";// 查询镖师接单时是否需要先完成答题培训
    public static final String saveAnswerResult = BASE + "/system/question/saveAnswerResult";// 答完培训题目后保存接口地址   （post请求）
    public static final String getLatesAddress = BASE + "/system/question/getLatesAddress";// 获取用户最近一次发单地址
    public static final String updateIfHaveBuyInsure = BASE + "/system/question/updateIfHaveBuyInsure";// 是否购买意外险



}
