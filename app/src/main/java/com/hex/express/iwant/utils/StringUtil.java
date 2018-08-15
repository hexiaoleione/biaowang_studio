package com.hex.express.iwant.utils;

import java.math.BigDecimal;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.util.Base64;

public class StringUtil {
	/**
	 * 是否是电子邮箱
	 * @param paramString
	 * @return
	 */
	public static boolean isEmail(String paramString) {
		return Pattern
				.compile("\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*")
				.matcher(paramString).matches();
	}
	/**
	 * 是否是身份证号
	 * @param paramString
	 * @return
	 */
	public static boolean isIDCard(String paramString) {
		return Pattern.compile("(\\d{14}[0-9xX])|(\\d{17}[0-9xX])")
				.matcher(paramString).matches();
	}
	/**
	 * 是否符合手机格式
	 * @param paramString
	 * @return
	 */
	public static boolean isMobileNO(String paramString) {
		//return Pattern.compile("^((13[0-9])|(15[^4,\\D])|(18[0,5-9]))\\d{8}$").matcher(p aramString).matches();
		//Pattern p = Pattern.compile("^((13[0-9])|(15[^4,\\D])|(18[0,1,5-9])|(17[6,7,8]))\\d{8}$");
		 String regex = "(\\+\\d+)?1[345678]\\d{9}$"; 
	     return Pattern.matches(regex, paramString); 
		//Matcher m = p.matcher(paramString);
	//	return m.matches();
	}
	/**
	 * 是否为数字
	 * @param paramString
	 * @return
	 */
	public static boolean isNumber(String paramString) {
		try {
			Float.parseFloat(paramString);
			return true;
		} catch (Exception localException) {
		}
		return false;
	}
	/**
	 * 判断车牌号
	 * @param paramString
	 * @return
	 */
	 public static boolean isCarNumber(String paramString) {
		  Pattern pattern = Pattern.compile("^[\u4e00-\u9fa5|WJ]{1}[A-Z0-9]{6}$");
		   Matcher matcher = pattern.matcher(paramString);
		   matcher.matches();
			return true;
		}
	/**
	 * 是否是符合要求的密码格式
	 * @param paramString
	 * @return
	 */
	public static boolean isValidPassword(String paramString) {
		return Pattern.compile("^(?![0-9]+$)(?![a-zA-Z]+$)[0-9A-Za-z]{6,16}$")
				.matcher(paramString).matches();
	}
	/**
	 * 是否是闰年
	 * @param year
	 * @return
	 */
	public static boolean isLeap(int year) {
		if ((year % 4 == 0 && year % 100 != 0) || year % 400 == 0)
			return true;
		else
			return false;
	}
	public static String bytes2Base64(byte[] buff){
        return Base64.encodeToString(buff, Base64.DEFAULT);
    }
	/**
     * 判断给定字符串是否空白串。 空白串是指由空格、制表符、回车符、换行符组成的字符串 若输入字符串为null或空字符串，返回true
     * 
     * @param input
     * @return boolean
     */
    public static boolean isEmpty(String input) {
        if (input == null || "".equals(input) || "null".equals(input))
            return true;

        for (int i = 0; i < input.length(); i++) {
            char c = input.charAt(i);
            if (c != ' ' && c != '\t' && c != '\r' && c != '\n') {
                return false;
            }
        }
        return true;
    }
    /** 
    * * 两个Double数相加 * 
    *  
    * @param v1 * 
    * @param v2 * 
    * @return Double 
    */  
    public static Double add(Double v1, Double v2) {  
       BigDecimal b1 = new BigDecimal(v1.toString());  
       BigDecimal b2 = new BigDecimal(v2.toString());  
       return new Double(b1.add(b2).doubleValue());  
    }  
      
    /** 
    * * 两个Double数相减 * 
    *  
    * @param v1 * 
    * @param v2 * 
    * @return Double 
    */  
    public static Double sub(Double v1, Double v2) {  
		   BigDecimal b1 = new BigDecimal(v1.toString());  
		   BigDecimal b2 = new BigDecimal(v2.toString());  
		   return new Double(b1.subtract(b2).doubleValue());  
		} 
}