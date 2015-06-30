package com.aliyun.findme.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.SimpleTimeZone;

/** 
 * @ClassName: DateUtil 
 * @Description: Date的常用工具
 * @author mingfeng.lmf 
 *  
 */
public class DateUtil {
	
	/** 
	* 
	* Description: 获取当前时间的GMT格式日期  
	* 
	* @return 按照GMT日期格式，返回String字符
	*/
	public static String getGMTTime() {
		 Date d = new Date(System.currentTimeMillis() + 0);
		 SimpleDateFormat dateFormat = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss z", Locale.ENGLISH);
		 dateFormat.setTimeZone(new SimpleTimeZone(0, "GMT"));
		 return dateFormat.format(d);
	}
	
	/**
	 * Description: 获取当前日期
	 * @return yyyy-mm-dd
	 */
	public static String getDate() {
		Date d = new Date();
		String date = DateFormat.getDateInstance().format(d);
		
		return date;
	}
	
	/**
	 * Description: 获取当前日期，自定义格式
	 *   
	 * @param format
	 * @return date
	 */
	public static String getDate(String format) {
		Date d = new Date();
		SimpleDateFormat dateFormat = new SimpleDateFormat(format);
		
		return dateFormat.format(d);
	}
	
	/** 
	* 
	* Description: 将Date类型的时间转换为指定格式的String 
	* @param date	Date类型参数
	* @param pattern 指定的日期类型；null或""为默认格式："yyyy-MM-dd HH:mm:ss.SSS"；
	* @return 按照pattern中指定的日期类型返回String字符
	*/
	public static String dateToStr(Date date,String pattern) {
		if(pattern == null||pattern.trim().equals("")){
			pattern = "yyyy-MM-dd HH:mm:ss.SSS";
		}
		DateFormat ymdhmsFormat = new SimpleDateFormat(pattern);
		
		return ymdhmsFormat.format(date);
	}
	
	/**
	 * 将指定格式的字符串转换为Date类型的时间
	 * 
	 * @param str  指定格式的日期字符串
	 * @param pattern 日期字符串指定的格式；null或"",为默认格式："yyyy-MM-dd HH:mm:ss.SSS"；
	 * @return
	 * 
	 */
	public static Date strToDate(String str,String pattern){
		if(pattern == null||pattern.trim().equals("")){
			pattern = "yyyy-MM-dd HH:mm:ss.SSS";
		}
		DateFormat ymdhmsFormat = new SimpleDateFormat(pattern);
		Date date=null;
		try {
			date = ymdhmsFormat.parse(str);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return date;
	}
	
	/**
	 * 将日期格式的字符串转换为时间戳
	 * 时间戳：the milliseconds since January 1, 1970, 00:00:00 GMT.
	 * @param str 指定格式的日期字符串
	 * @param pattern 日期字符串指定的格式；null或"",为默认格式："yyyy-MM-dd HH:mm:ss.SSS"；
	 * @return long
	 */
	public static long strToTimestamp(String str,String pattern){
		if(pattern == null||pattern.trim().equals("")){
			pattern = "yyyy-MM-dd HH:mm:ss.SSS";
		}
		DateFormat ymdhmsFormat = new SimpleDateFormat(pattern);
		Date date=null;
		long time = -1;
		try {
			date = ymdhmsFormat.parse(str);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(date!=null){
			time = date.getTime();
		}
		return time;
	}
	
}
