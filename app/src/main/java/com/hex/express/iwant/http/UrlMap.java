package com.hex.express.iwant.http;

public class UrlMap {
	public static String getUrl(String url, String key, String value) {
		return url + "?" + key + "=" + value;
	}

	public static String getThreeUrl(String url, String key_one,
			String value_one, String key_two, String value_two,
			String key_three, String value_three) {
		return getUrl(url, key_one, value_one) + "&" + key_two + "="
				+ value_two + "&" + key_three + "=" + value_three;
	}

	public static String getTwo(String url, String key_one, String value_one,
			String key_two, String value_two) {
		return getUrl(url, key_one, value_one) + "&" + key_two + "="
				+ value_two;
	}

	public static String getfour(String url, String key_one, String value_one,
			String key_two, String value_two, String key_three,
			String value_three, String key_four, String value_four) {
		return getUrl(url, key_one, value_one) + "&" + key_two + "="
				+ value_two + "&" + key_three + "=" + value_three + "&"
				+ key_four + "=" + value_four;
	}
	public static String getfive(String url, String key_one, String value_one,
			String key_two, String value_two, String key_three,
			String value_three, String key_four, String value_four, String key_five, String value_five) {
		return getUrl(url, key_one, value_one) + "&" + key_two + "="
				+ value_two + "&" + key_three + "=" + value_three + "&"
				+ key_four + "=" + value_four+ "&"
				+ key_five + "=" + value_five;
	}
	public static String getfsix(String url, String key_one, String value_one,
			String key_two, String value_two, String key_three,
			String value_three, String key_four, String value_four, String key_five, String value_five,String key_six, String value_six) {
		return getUrl(url, key_one, value_one) + "&" + key_two + "="
				+ value_two + "&" + key_three + "=" + value_three + "&"
				+ key_four + "=" + value_four+ "&"
				+ key_five + "=" + value_five+ "&"
				+ key_six + "=" + value_six;
	}
	public static String getseven(String url, String key_one, String value_one,
			String key_two, String value_two, String key_three,
			String value_three, String key_four, String value_four, 
			String key_five, String value_five,String key_six, String value_six,
			String key_seven, String value_seven) {
		return getUrl(url, key_one, value_one) + "&" + key_two + "="
				+ value_two + "&" + key_three + "=" + value_three + "&"
				+ key_four + "=" + value_four+ "&"
				+ key_five + "=" + value_five+ "&"
				+ key_six + "=" + value_six+ "&"
				+ key_seven + "=" + value_seven;
	}
	public static String getnine(String url, String key_one, String value_one,
			String key_two, String value_two, String key_three,
			String value_three, String key_four, String value_four, 
			String key_five, String value_five,String key_six, String value_six,
			String key_seven, String value_seven,
			String key_eight, String value_eight,
			String key_nine, String value_nine) {
		return getUrl(url, key_one, value_one) + "&" + key_two + "="
				+ value_two + "&" + key_three + "=" + value_three + "&"
				+ key_four + "=" + value_four+ "&"
				+ key_five + "=" + value_five+ "&"
				+ key_six + "=" + value_six+ "&"
				+ key_seven + "=" + value_seven+ "&"
				+ key_eight + "=" + value_eight+ "&"
				+ key_nine + "=" + value_nine;
	}
	
}
