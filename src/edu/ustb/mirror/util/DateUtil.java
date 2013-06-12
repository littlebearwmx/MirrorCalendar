package edu.ustb.mirror.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public abstract class DateUtil {

	// 获取当前日期
	public static String getCurrentDateline() {
		DateFormat tempDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return tempDate.format(new java.util.Date());
	}

	// 获取当前年份
	public static String getCurrentYear() {
		DateFormat tempDate = new SimpleDateFormat("yyyy");
		return tempDate.format(new java.util.Date());
	}

	// 将日期装换为用户可读
	public static String getReadableDate(String dateline) {
		SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			Date theDate = f.parse(dateline);

			SimpleDateFormat hourFormat = new SimpleDateFormat("HH:mm");
			Calendar cal = Calendar.getInstance();// 使用默认时区和语言环境获得一个日历。

			Date currentDate = cal.getTime();
			// cal.add(Calendar.DAY_OF_MONTH, -1);
			String tmp = f.format(currentDate);

			tmp = tmp.substring(0, 10) + " 00:00:00";
			// Log.i("TAG", tmp+" VS " +date);
			if (tmp.compareTo(dateline) < 0) {
				return "今天 " + hourFormat.format(theDate);
			}

			cal.setTime(f.parse(tmp));
			cal.add(Calendar.DAY_OF_MONTH, -1);
			tmp = f.format(cal.getTime());

			if (tmp.compareTo(dateline) < 0) {
				return "昨天 " + hourFormat.format(theDate);
			}

			SimpleDateFormat dayFormat = new SimpleDateFormat("M月d日 HH:mm");
			return dayFormat.format(theDate);

		} catch (ParseException e) {
			// TODO Auto-generated catch block
			// e.printStackTrace();
			return "";
		}

	}

	// 获取某天之前的日期
	public static String getBeforeDate(Date date, int days) {
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.set(Calendar.DAY_OF_YEAR, calendar.get(Calendar.DAY_OF_YEAR)
				- days);
		return df.format(calendar.getTime());
	}

	// 获取某天之后的日期
	public static String getAfterDate(Date date, int days) {
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.set(Calendar.DAY_OF_YEAR, calendar.get(Calendar.DAY_OF_YEAR)
				+ days);
		return df.format(calendar.getTime());
	}
}
