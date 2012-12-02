package org.dolphinboy.birdway.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;


public class DateUtils {
	/**
	 * 日期格式:yyyy-MM-dd
	 */
	public static final SimpleDateFormat DATE_FMT_D = new SimpleDateFormat("yyyy-MM-dd");
	/**
	 * 日期格式:yyyy-MM-dd HH:mm:ss
	 */
	public static final SimpleDateFormat DATE_FMT_HMS = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	/**
	 * 日期格式:yyyy-MM-dd HH:mm:ss zzz
	 */
	public static final SimpleDateFormat DATE_FMT_HMSS = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss SSS");
	public static final SimpleDateFormat DATE_FMT_HMSS_UU = new SimpleDateFormat("yyyy-MM-dd_HHmmss_SSS");
	/**
	 * 星期格式:yyyy-MM-dd-EEE
	 */
	public static final SimpleDateFormat DATE_FMT_WEEK = new SimpleDateFormat("yyyy-MM-dd-EEE");
	
	/**
	 * 日期时间格式化方法
	 * 此方法有待完善
	 * @param date
	 * @param sdf
	 * @return
	 * @throws ParseException
	 */
	public static String formatDate(Date date, SimpleDateFormat sdf) throws ParseException {
		return sdf.format(date);
	}
	public static Date formatDate(String date, SimpleDateFormat sdf) throws ParseException {
		String fdate = sdf.format(date);
		return sdf.parse(fdate);
	}
}
