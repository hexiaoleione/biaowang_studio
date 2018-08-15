package com.hex.express.iwant.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateUtil {
	
	 private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

	 private Calendar calendar;
	 
	public DateUtil() {
		// TODO Auto-generated constructor stub
	}

	/**
     * 获取几天前的日期字符串
     * @param days
     * @return
     */
    public String getDateStrDaysBefore(String date,Integer days){
    	calendar = Calendar.getInstance();
    	try {
			calendar.setTime(sdf.parse(date));
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
        calendar.add(Calendar.DATE,days);
        Date dateStr = calendar.getTime();
        return sdf.format(dateStr);

    }
    /**
     * 获取几天后的日期字符串
     * @param days
     * @return
     */
    public String getDateStrDaysafter(String date,Integer days){
    	calendar = Calendar.getInstance();
    	try {
			calendar.setTime(sdf.parse(date));
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
        calendar.add(Calendar.DATE,+days);
        Date dateStr = calendar.getTime();
        return sdf.format(dateStr);

    }
	
}
