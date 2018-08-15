package com.hex.express.iwant.alipay;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import android.app.Activity;
import android.text.TextUtils;
import android.util.Log;

import com.alipay.sdk.app.PayTask;

/**
 * 说明：支付宝支付工具类
 * 
 * @author lgd
 * @since 2015-3-17 11:11:30
 */
public class AlipayUtils {

	/** 商户PID */
	private static final String PARTNER = "2088121017436645";

	/** 商户收款账号 */
	private static final String SELLER = "1334645946@qq.com";

	/** 公钥 */
//	private static final String RSA_PRIVATE = "MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBAKPRLZAJUw5zTXyDp2RBhjPKqjfcM9AmfG5gkcQ80pz5nllBiFG3pECjRo70IDb7EV3Pj4rdN3aEg7HkEljRQaFgAmGftftjkPXQb09Ab41SY/czLHBFMRg3I67ED/ju5f8kiCx6TM90WSJRFN9tbtJiyRkdv+7VdZ6ivmR7DejVAgMBAAECgYEAjZAkKYaFScWGPoWez9p32KXYNvGJrOS1PsUg8k4FSzXDCyrAXK2vuVPfk4WPmTOZrFHDn6LPXRHipIWM/UfHyARhpO0i9TYtkrxfi+OqnEeaJal6iiQ+Xb7ji3dFse+zdtSNcxXSCoDlbqAKgWiRX0tbxYLd6Li+8YOx9b0ObwECQQDWBDrxt7pLeiB7JRHTNjgbQVbw+pRA8DzqS/ux5waeMpZ2ANX7LMF/V5low0p0grJ5TY2SWilBqx/00uKpHo7hAkEAw/P3L7T2GRSBe5NEmW4FbEOucd+cUp05ObBfJzy2cVSpeZhhgI68aE8TzuaUu4G1oOjeNUDQ6UCd7fTLVTYcdQJAHIqL4diCu7YZxtJ2whgk+12HXrPMOazQMt7XPi51L3vHnQ0XfFvHolfoWZRRxZ/4dTzmeHIVtnA/3sKQ/8TXgQJAFuTlJ3zEGBAORMrjA+4X0htiQUm38OsPqY7SvznXswwKNPu5wJRsrN84WD8Wd4XjG6a5d41YGziCey/TpeQJlQJAWiZ87rd0P6+GNtJlPcYZuw2PHPO7yv/4USKooy1mnaC3i2ttORv6yleCC9NeIOM5VpXf6CYL2jNepKhHsQZP4Q==";
	//private static final String RSA_PRIVATE = "MIICeAIBADANBgkqhkiG9w0BAQEFAASCAmIwggJeAgEAAoGBALPl3QFk+kOOppBJ1ldZWCBlLp7hGW2tIpeRkc2EAyOHCfdcFO+Syo32SHcgw3BIAcrelUnUm8HaFESMxW9p5GeCq+tii9IO6nU7EEAx+8U96845bfrY+syuuIr602DKsUMnCdxqKN2dr4fs/WnvxOgcNs1p271F5E1cc1AMPLSTAgMBAAECgYBI9uE1kJQk1HXgLeJ+mSEyOne3PwtGPaO7H/KoDXQMc9fp+PIQ3VPEUnC/yI0cPEhl1PNt94qZDG+OAh6N+sAFQIXPwq0+d3PvhPz/ATk2ZqcnvvkDDrgU2xNvyj13okkbNiBP3rMKCidOa/AqVZ3VX/gxgYoy2HIeseuVbcmPYQJBAO/K7c3YO59qiNYHgh56c2t8FCKpbJX9uvDgUUaTfSmKJdDFEyQMoFQLykyhPXmSDoBDlnG267iymTGVPFndLvECQQDADpcfSmqZMqKpMmzdsevDpcgSY1c5M8ID4MTziqfGjV8CFGGT0r+LVDPwrSrRIeFiHFz3R6Zjm6vxNsi+xyPDAkEA4SC7XOvrJkMmDxNjhWvfWwVV/ijkinl5/lYL2F/2PX62/CdaThkQmXesSX+EMNQAi0lpQH/FFVWQYC3FSHVRcQJBAL2JaKDLyxm3fAnopsG/RgJGSIXFHh5osu8q0pPa4aq0d+/Qd1/wW3JAPiYmb5wGiKxPe3vabR3eUrjxrPmCBAsCQQCC/I2nsLA7tItkTJ1GeExPP30IY0iZOwAFiaCLEAHnfzDjbKRwjUxg4IL/hnsy7IUOqq+bbZ5xXAL9jYZmNaBo";
	
	public static final String RSA_PRIVATE = "MIICeAIBADANBgkqhkiG9w0BAQEFAASCAmIwggJeAgEAAoGBALIiQGr2nJ6Sc7h4qDvv8IbW9Kgj1EIgYPNIFegnr7s7Mgr3qJmkknrIp6D3DcvHJf5g9YU5PY6cnzZQkg5iBvkK6XWzEp93YOiULXUrLWdMFeOPxTHCcyGupadfBF39ZKB4ATe/wirX/x6OT0r2Ztvxeh6VcJ1AzXErbGkh2mQFAgMBAAECgYBJVgqZPsBKFFcp+hqg2m52xOZilqWbN8NSRTonorcZaQx4MW+rGXLos9U3Up7OZ0XADWDnISKzpM1zSgZnf3oIu35EJLgVsdSyrwJ7Y9YNaG+TbAhxDhGT6a8dJxULqmty2I6DGpfR9J24mYqNqr74l6LXFp+A8GbFyvUwLIMW/QJBAOPbdNX793gBIaNE10dmFKtn28kX4agXlDX6hvQIF2XXNBmtQjnueOiTy+Mh/FHa6lnyaAsRj38NacrdEdh+V6sCQQDIIpsJbxnWV/amePTRNy+S4ll0MDEU0e1T5s8G0SxmeBdm5uB++ioR9Y3JcVhFOPnei33wcuNdWWJS4NCFBMMPAkEAzJ5WNJeaKx0w/PKkNMz6ANc6q0KT9aR/B15cdKcTg/QXKHvLvkRmNc4LyYPKmCN0UqP6RP5XE6yS9YdDMFx9EwJBAMICYf7MN+TEUvNgMBucfr7KM+NhUEMZmhKKncjC/2K6Kh1z1M7i3eOZAgD0ophAOmFVIhn0lZoJqzoie6FnU1sCQQCr1m+HZ+1RLl+wUgtTm1eUx+PoIaftalX+PUaBGb893jnb1zoBMwKOqDohLWnBlvNUUXbE6uNpBYXW1rxW5eCT";  
	/** 支付成功 */
	public static final int PAY_RESULT_STATUS_SUC = 1;
	/** 支付失败 */
	public static final int PAY_RESULT_STATUS_FAIL = 2;
	/** 支付结果确认中 */
	public static final int PAY_RESULT_STATUS_WAITTING = 3;
	/** 用户中途取消 */
	public static final int PAY_RESULT_STATUS_CANCEL = 4;
	/** 网络连接出错 */
	public static final int PAY_RESULT_STATUS_HTTP_ERROR = 5;

	/** 订单支付成功 */
	private static final String PAY_STATUS_SUC = "9000";

	/** 正在处理中 */
	private static final String PAY_STATUS_PROCESSING = "8000";

	/** 订单支付失败 */
	private static final String PAY_STATUS_FAIL = "4000";

	/** 用户中途取消 */
	private static final String PAY_STATUS_CANCEL = "6001";

	/** 网络连接出错 */
	private static final String PAY_STATUS_HTTP_ERROR = "6002";

	/**
	 * 支付宝支付,未付款交易的超时时间为30分钟
	 * 
	 * @param activity
	 * @param subject
	 *            商品名称
	 * @param body
	 *            商品详情
	 * @param price
	 *            商品金额
	 * @return
	 */
	public static int pay(Activity activity, String subject, String body, String price, String orderNo) {
		return pay(activity, getOrderInfo(subject, body, price, null, null, null, orderNo));
	}

	/**
	 * 支付宝支付
	 * 
	 * @param activity
	 * @param orderInfo
	 *            订单信息，考虑安全因素，可能订单生成由用户服务器生成
	 * @return
	 */
	public static int pay(Activity activity, String orderInfo) {

		Log.e("Log", orderInfo);

		// 对订单做RSA 签名
		String sign = sign(orderInfo);
		try {
			// 仅需对sign 做URL编码
			sign = URLEncoder.encode(sign, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

		// 完整的符合支付宝参数规范的订单信息
		final String payInfo = orderInfo + "&sign=\"" + sign + "\"&" + getSignType();
		Log.e("payINf", payInfo);
		// 构造PayTask 对象
		PayTask alipay = new PayTask(activity);

		// 调用支付接口，获取支付结果
		PayResult payResult = new PayResult(alipay.pay(payInfo));
		Log.e("result",payResult.getResult());

		if (TextUtils.equals(payResult.getResultStatus(), PAY_STATUS_SUC))
			return PAY_RESULT_STATUS_SUC;
		else if (TextUtils.equals(payResult.getResultStatus(), PAY_STATUS_PROCESSING))
			return PAY_RESULT_STATUS_WAITTING;
		else if (TextUtils.equals(payResult.getResultStatus(), PAY_STATUS_FAIL))
			return PAY_RESULT_STATUS_FAIL;
		else if (TextUtils.equals(payResult.getResultStatus(), PAY_STATUS_CANCEL))
			return PAY_RESULT_STATUS_CANCEL;
		else if (TextUtils.equals(payResult.getResultStatus(), PAY_STATUS_HTTP_ERROR))
			return PAY_RESULT_STATUS_HTTP_ERROR;
		return PAY_RESULT_STATUS_HTTP_ERROR;
	}

	/**
	 * 查询终端设备是否存在支付宝认证账户
	 */
	public static boolean checkAccountIfExist(Activity activity) {
		// 构造PayTask 对象
		PayTask payTask = new PayTask(activity);
		// 调用查询接口，获取查询结果
		return payTask.checkAccountIfExist();
	}
	/**
	 * 
	 * @param subject
	 *            商品名称
	 * @param body
	 *            商品详情
	 * @param price
	 *            商品金额
	 * @param notifyUrl
	 *            服务器异步通知页面路径
	 * @param timeout
	 *            未付款交易的超时时间,默认30分钟，一旦超时，该笔交易就会自动被关闭。 取值范围：1m～15d。
	 *            m-分钟，h-小时，d-天，1c-当天（无论交易何时创建，都在0点关闭）。
	 *            该参数数值不接受小数点，如1.5h，可转换为90m。
	 * @param returnUrl
	 *            支付宝处理完请求后，当前页面跳转到商户指定页面的路径，可空
	 * @return
	 */
	public static String getOrderInfo(String subject, String body, String price, String notifyUrl, String timeout, String returnUrl, String orderNo) {
		// 签约合作者身份ID
		String orderInfo = "partner=" + "\"" + PARTNER + "\"";

		// 签约卖家支付宝账号
		orderInfo += "&seller_id=" + "\"" + SELLER + "\"";

		// 商户网站唯一订单号
		orderInfo += "&out_trade_no=" + "\"" + orderNo + "\"";

		// 商品名称
		orderInfo += "&subject=" + "\"" + subject + "\"";

		// 商品详情
		orderInfo += "&body=" + "\"" + body + "\"";

		// 商品金额
		orderInfo += "&total_fee=" + "\"" + price + "\"";

		if (!TextUtils.isEmpty(notifyUrl))
			// 服务器异步通知页面路径
			orderInfo += "&notify_url=" + "\"" + notifyUrl + "\"";

		// 服务接口名称， 固定值
		orderInfo += "&service=\"mobile.securitypay.pay\"";

		// 支付类型， 固定值
		orderInfo += "&payment_type=\"1\"";

		// 参数编码， 固定值
		orderInfo += "&_input_charset=\"utf-8\"";

		// 设置未付款交易的超时时间
		// 默认30分钟，一旦超时，该笔交易就会自动被关闭。
		// 取值范围：1m～15d。
		// m-分钟，h-小时，d-天，1c-当天（无论交易何时创建，都在0点关闭）。
		// 该参数数值不接受小数点，如1.5h，可转换为90m。

		if (!TextUtils.isEmpty(timeout))
			orderInfo += "&it_b_pay=\"" + timeout + "\"";

		// extern_token为经过快登授权获取到的alipay_open_id,带上此参数用户将使用授权的账户进行支付
		// orderInfo += "&extern_token=" + "\"" + extern_token + "\"";

		if (TextUtils.isEmpty(returnUrl))
			returnUrl = "http://m.alipay.com";

		if (!TextUtils.isEmpty(returnUrl))
			// 支付宝处理完请求后，当前页面跳转到商户指定页面的路径，可空
			orderInfo += "&return_url=\"" + returnUrl + "\"";

		// 调用银行卡支付，需配置此参数，参与签名， 固定值 （需要签约《无线银行卡快捷支付》才能使用）
		// orderInfo += "&paymethod=\"expressGateway\"";
		String sign = sign(orderInfo);
		try {
			// 仅需对sign 做URL编码
			sign = URLEncoder.encode(sign, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

		// 完整的符合支付宝参数规范的订单信息
		final String payInfo = orderInfo + "&sign=\"" + sign + "\"&" + getSignType();
		Log.e("payINf", payInfo);

		return payInfo;
	}

	/**
	 * 对订单信息进行签名
	 * 
	 * @param content
	 *            待签名订单信息
	 */
	private static String sign(String content) {
		return SignUtils.sign(content, RSA_PRIVATE);
	}

	/**
	 * 获取签名方式
	 */
	private static String getSignType() {
		return "sign_type=\"RSA\"";
	}
}
