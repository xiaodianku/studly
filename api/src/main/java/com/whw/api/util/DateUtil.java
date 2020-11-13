package com.whw.api.util;


import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * Date Util
 *
 * @author qrz 2018/12/7 10:12
 */
public class DateUtil {

	/**
	 * exact to day
	 *
	 * @param date need to parse
	 * @return Date instance like 'yyyy-MM-dd'
	 * @author wc 2018年7月5日下午3:31:46
	 */
	public static Date exact2Day(Date date) {
		if (null == date) {
			return null;
		}
		SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
		try {
			return sf.parse(sf.format(date));
		} catch (ParseException e) {
			return null;
		}
	}

	/**
	 * exact to day
	 *
	 * @param date need to parse
	 * @return Long value for date time like 'yyyy-MM-dd'
	 * @author wc 2018年7月5日下午3:31:46
	 */
	public static Long exact2DayTime(Date date) {
		if (null == date) {
			return null;
		}
		SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
		try {
			return sf.parse(sf.format(date)).getTime();
		} catch (ParseException e) {
			return null;
		}
	}
	
	/**
	 * format date to yyyy-MM-dd HH:mm:ss String
	 *
	 * @param date need to format
	 * @return String instance for date like 'yyyy-MM-dd'
	 * @author wc 2018年7月6日下午2:36:41
	 */
	public static String Date2yyyyMMddHHmmss(Date date) {
		if (null == date) {
			return null;
		}
		SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return sf.format(date);
	}

	/**
	 * format date to yyyy-MM-dd String
	 *
	 * @param date need to format
	 * @return String instance for date like 'yyyy-MM-dd'
	 * @author wc 2018年7月6日下午2:36:41
	 */
	public static String Date2yyyyMMdd(Date date) {
		if (null == date) {
			return null;
		}
		SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
		return sf.format(date);
	}


	/**
	 * format date to yyyy-MM-dd String
	 *
	 * @param date need to format
	 * @return String instance for date like 'yyyy-MM-dd'
	 * @author wc 2018年7月6日下午2:36:41
	 */
	public static String Date2yyyyMMddHH(Date date) {
		if (null == date) {
			return null;
		}
		SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH");
		return sf.format(date);
	}

	/**
	 * add days
	 *
	 * @param date The date need to add days.
	 * @param days Can be Positive number or negative number.
	 * @return Date instance.
	 * @author wc 2018年7月5日下午4:53:15
	 */
	public static Date addDay(Date date, int days) {
		if (null == date) {
			return null;
		}
		return new Date(date.getTime() + (days * 86400000));
	}

	/**
	 * day of week(0-6. 0 is sunday)
	 *
	 * @param date The date need to parse
	 * @return day of week(0-6. 0 is sunday)
	 * @author wc 2018年7月9日下午2:09:35
	 */
	public static int dayOfWeek(Date date) {
		Calendar cal = new GregorianCalendar();
		cal.setTime(date);
		return cal.get(Calendar.DAY_OF_WEEK) - 1;
	}

	/**
	 * Timestamp 转yyyy
	 * @param time
	 * @return
	 */
	public static String dateToStr(java.sql.Timestamp time) {
		SimpleDateFormat sf = new SimpleDateFormat("yyyy");
		String str = sf.format(time);
		return str;
	}

	/**
	 * Timestamp 转yyyy
	 * @param time
	 * @return
	 */
	public static String dateToStrYYYYMMDDHHMM(java.sql.Timestamp time) {
		SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		String str = sf.format(time);
		return str;
	}
	/**
	 * Timestamp 转yyyy
	 * @param time
	 * @return
	 */
	public static String dateToStrYYYYMMDDHHMMSS(java.sql.Timestamp time) {
		if(time == null) return "";
		SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String str = sf.format(time);
		return str;
	}


	public static String getYYYYMMDDHHMMSS() {
		SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String str = sf.format(new Date());
		return str;
	}

	public static String getYYYYMMDD() {
		SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
		String str = sf.format(new Date());
		return str;
	}

	public static String dateToStrHHMM(java.sql.Timestamp time) {
		SimpleDateFormat sf = new SimpleDateFormat("HH:mm");
		String str = sf.format(time);
		return str;
	}

	/**
	 * Timestamp 转yyyy
	 * @param time
	 * @return
	 */
	public static String dateToStrMMDDHHMM(java.sql.Timestamp time) {
		SimpleDateFormat sf = new SimpleDateFormat("MM-dd HH:mm");
		String str = sf.format(time);
		return str;
	}

	/**
	 * 给时间加上几个小时
	 * @param day 当前时间 格式：yyyy-MM-dd HH:mm:ss
	 * @param hour 需要加的时间
	 * @return
	 */
	public static String addDateMinut(String day, int hour){
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH");
		Date date = null;
		try {
			date = format.parse(day);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		if (date == null)
			return "";
//		System.out.println("front:" + format.format(date)); //显示输入的日期
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.HOUR, hour);// 24小时制
		date = cal.getTime();
//		System.out.println("after:" + format.format(date));  //显示更新后的日期
		cal = null;
		return format.format(date);

	}

	/**
	 * 给时间加上几个小时
	 * @param day 当前时间 格式：yyyy-MM-dd
	 * @param addday 需要加的时间
	 * @return
	 */
	public static String addDateDay(String day, int addday){
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		Date date = null;
		try {
			date = format.parse(day);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		if (date == null)
			return "";
//		System.out.println("front:" + format.format(date)); //显示输入的日期
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.DATE, addday);// 24小时制
		date = cal.getTime();
//		System.out.println("after:" + format.format(date));  //显示更新后的日期
		cal = null;
		return format.format(date);

	}


	public static void main(String[] args) {
		System.out.println(addDateDay("2019-08-01",0));
		System.out.println(addDateMinut("2019-08-01",0));
	}

}
