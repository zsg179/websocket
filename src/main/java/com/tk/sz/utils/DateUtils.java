package com.tk.sz.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

/**
 * Function：
 *
 * @Auth：zhouwenjun
 * @Date: 2018-3-14 9:44
 */
public class DateUtils {

	private static String DEFAULT_YEAR_MONTH_DAY = "2018-03-13 ";

	private static String DEFAULT_SECOND = ":00 000";

	private static String DATE_PATTERN = "yyyy-MM-dd HH:mm:ss";

	/**
	 * 根据时间戳，提取出来小时和分钟，例如：13:00
	 * 
	 * @param timestamp
	 * @return
	 */
	public static String getHourAndMinuteFromTimestamp(long timestamp) {

		Date date = new Date(timestamp);

		DateFormat dateFormat = new SimpleDateFormat(DATE_PATTERN);

		String time = dateFormat.format(date); // eg.2018-03-13 09:00:00

		String hourAndMinute = time.substring(10, 16);

		return hourAndMinute;
	}

	/**
	 * 根据小时和分钟，提取出来时间戳
	 * 
	 * @param hourAndMinute,例如：13:00
	 * @return
	 */
	public static long getTimestampFromHourAndMinute(String hourAndMinute) throws ParseException {

		String time = DEFAULT_YEAR_MONTH_DAY + hourAndMinute + DEFAULT_SECOND;

		DateFormat dateFormat = new SimpleDateFormat(DATE_PATTERN);

		Date date = dateFormat.parse(time);

		return date.getTime();
	}

	/**
	 * 获取当前日期，设置时区， 防止出现差8个小时时差
	 * @return
	 */
	public static String getDateTime() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		sdf.setTimeZone(TimeZone.getTimeZone("GMT+8"));// 如果不指定时区，在有些机器上会出现时间误差。
		return sdf.format(new Date());
	}

}
