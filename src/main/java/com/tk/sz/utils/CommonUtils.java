package com.tk.sz.utils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public  class CommonUtils {

	
	/**
	 * 比较时间大小
	 * @param nowTime
	 * @param beginTime
	 * @param endTime
	 * @return
	 */
	public static boolean belongCalendar(Date nowTime, Date beginTime, Date endTime) {
	    Calendar date = Calendar.getInstance();
	    date.setTime(nowTime);
	    Calendar begin = Calendar.getInstance();
	    begin.setTime(beginTime);
	    Calendar end = Calendar.getInstance();
	    end.setTime(endTime);
	    if (date.after(begin) && date.before(end)) {
	        return true;
	    }else if(nowTime.compareTo(beginTime)==0 || nowTime.compareTo(endTime) == 0 ){
	    	return true;
	    }else {
	        return false;
	    }
	}
	
	
	//获取年龄
		public static int getAge(Date birthDay , Date yearMonth) throws Exception {
			Calendar cal = Calendar.getInstance();
			cal.setTime(yearMonth);
			
			if (cal.before(birthDay)) { // 出生日期晚于计算时间，无法计算
				throw new IllegalArgumentException("The birthDay is before Now.It's unbelievable!");
			}
			
			int yearNow = cal.get(Calendar.YEAR); // 计算时年份
			int monthNow = cal.get(Calendar.MONTH); // 计算时月份
			int dayOfMonthNow = cal.get(Calendar.DAY_OF_MONTH); // 计算时日期
			
			cal.setTime(birthDay);
			int yearBirth = cal.get(Calendar.YEAR);
			int monthBirth = cal.get(Calendar.MONTH);
			int dayOfMonthBirth = cal.get(Calendar.DAY_OF_MONTH);
			
			int age = yearNow - yearBirth; // 计算整岁数
			if (monthNow <= monthBirth) {
				if (monthNow == monthBirth) {
					if (dayOfMonthNow < dayOfMonthBirth)
						age--;// 当前日期在生日之前，年龄减一
				} else {
					age--;// 当前月份在生日之前，年龄减一

				}
			}
			return age;
		}
		
		public static int getAge2(Date birthDay) throws Exception {
			Calendar cal = Calendar.getInstance();
			if (cal.before(birthDay)) { // 出生日期晚于当前时间，无法计算
				throw new IllegalArgumentException("The birthDay is before Now.It's unbelievable!");
			}
			int yearNow = cal.get(Calendar.YEAR); // 当前年份
			int monthNow = cal.get(Calendar.MONTH); // 当前月份
			int dayOfMonthNow = cal.get(Calendar.DAY_OF_MONTH); // 当前日期
			cal.setTime(birthDay);
			int yearBirth = cal.get(Calendar.YEAR);
			int monthBirth = cal.get(Calendar.MONTH);
			int dayOfMonthBirth = cal.get(Calendar.DAY_OF_MONTH);
			int age = yearNow - yearBirth; // 计算整岁数
			if (monthNow <= monthBirth) {
				if (monthNow == monthBirth) {
					if (dayOfMonthNow < dayOfMonthBirth)
						age--;// 当前日期在生日之前，年龄减一
				} else {
					age--;// 当前月份在生日之前，年龄减一

				}
			}
			return age;
		}
		
		/**
		 * 获取当前日期字符串
		 * @return
		 */
		public static String getNowData() {
			Calendar c = Calendar.getInstance();// 可以对每个时间域单独修改
			int year = c.get(Calendar.YEAR);
			int month = c.get(Calendar.MONTH) + 1;
			int date = c.get(Calendar.DATE);
			int hour = c.get(Calendar.HOUR_OF_DAY);
			int minute = c.get(Calendar.MINUTE);
			int second = c.get(Calendar.SECOND);
			System.out.println(year + "/" + month + "/" + date + " " + hour + ":" + minute + ":" + second);
			return year + "" + timeAdd0(month) + "" + timeAdd0(date) + "" + timeAdd0(hour) + "" + timeAdd0(minute) + "" + timeAdd0(second);
		}

		public static String getYearMonth() {
			Calendar c = Calendar.getInstance();// 可以对每个时间域单独修改
			int year = c.get(Calendar.YEAR);
			int month = c.get(Calendar.MONTH) + 1;
			return year + "" + timeAdd0(month);
		}

		public static String timeAdd0(int str) {
			String restult = String.valueOf(str);
			if (restult.length() <= 1) {
				restult = '0' + restult;
			}
			return restult;
		}
		

		/**
		 * 比较日期大小
		 * @param DATE1
		 * @param DATE2
		 * @return
		 */
		public static int compare_date(String DATE1, String DATE2) {
			DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
			try {
				Date dt1 = df.parse(DATE1);
				Date dt2 = df.parse(DATE2);
				if (dt1.getTime() >= dt2.getTime()) {
					return 1;
				} else if (dt1.getTime() < dt2.getTime()) {
					return -1;
				} else {
					return 0;
				}
			} catch (Exception exception) {
				exception.printStackTrace();
			}
			return 0;
		}
		
		
		/**
		 * 获取两个日期月份差
		 * @param d1
		 * @param d2
		 * @return
		 */
		public static int getMonthDiff(Date d1, Date d2) {
			Calendar c1 = Calendar.getInstance();
			Calendar c2 = Calendar.getInstance();
			c1.setTime(d1);
			c2.setTime(d2);
			if (c1.getTimeInMillis() < c2.getTimeInMillis())
				return 0;
			int year1 = c1.get(Calendar.YEAR);
			int year2 = c2.get(Calendar.YEAR);
			int month1 = c1.get(Calendar.MONTH);
			int month2 = c2.get(Calendar.MONTH);
			int day1 = c1.get(Calendar.DAY_OF_MONTH);
			int day2 = c2.get(Calendar.DAY_OF_MONTH);
			// 获取年的差值 假设 d1 = 2015-8-16 d2 = 2011-9-30
			int yearInterval = year1 - year2;
			// 如果 d1的 月-日 小于 d2的 月-日 那么 yearInterval-- 这样就得到了相差的年数
			if (month1 < month2 || month1 == month2 && day1 < day2)
				yearInterval--;
			// 获取月数差值
			int monthInterval = (month1 + 12) - month2;
			if (day1 < day2)
				monthInterval--;
			monthInterval %= 12;
			return yearInterval * 12 + monthInterval;
		}
		

		
		public static double getStr2Double(String value) {
			String result = value.split("%")[0];
			return Double.valueOf(result);
		}
		
		
		private static Date strFormatDate(String ld) throws Exception {
			DateFormat format = new SimpleDateFormat("yyyy-MM");
			Date lendDate = format.parse(ld);
			System.out.println(lendDate);
			return lendDate;
		}
		
		private static Date strFormatDate2day(String ld) throws Exception {
			DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
			Date lendDate = format.parse(ld);
			System.out.println(lendDate);
			return lendDate;
		}
		
		
		/**
		 * 获取两个日期差的月份集合
		 * @param startTime
		 * @param endTime
		 * @return
		 */
	public static List<String> getMonthList(String startTime, String endTime) {

		List<String> days = new ArrayList<String>();

		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM");
		try {
			Date start = dateFormat.parse(startTime);
			Date end = dateFormat.parse(endTime);

			Calendar tempStart = Calendar.getInstance();
			tempStart.setTime(start);

			Calendar tempEnd = Calendar.getInstance();
			tempEnd.setTime(end);
			tempEnd.add(Calendar.MONTH, +1);// 日期加1(包含结束)
			while (tempStart.before(tempEnd)) {
				days.add(dateFormat.format(tempStart.getTime()));
				tempStart.add(Calendar.MONTH, 1);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return days;
	}
		
		public static void main(String[] args) {
			List<String> days =	getMonthList ("2019-02-20" ,  "2019-06-01") ;
			System.out.println(days);
		}

}
