package org.schic.common.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class WorkDayUtils {

	public static void main(String[] args) {
		try {
			String strDateStart = "2013-08-01";
			String strDateEnd = "2014-08-31";

			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			Date dateStart = sdf.parse(strDateStart);
			Date dateEnd = sdf.parse(strDateEnd);
			WorkDayUtils app = new WorkDayUtils();
			Calendar calStart = Calendar.getInstance();
			Calendar calEnd = Calendar.getInstance();
			calStart.setTime(dateStart);
			calEnd.setTime(dateEnd);
			System.out.println("开始日：" + calStart.get(Calendar.YEAR) + "-"
					+ (calStart.get(Calendar.MONTH) + 1) + "-"
					+ calStart.get(Calendar.DAY_OF_MONTH) + " "
					+ app.getChineseWeek(calStart));
			System.out.println("结束日：" + calEnd.get(Calendar.YEAR) + "-"
					+ (calEnd.get(Calendar.MONTH) + 1) + "-"
					+ calEnd.get(Calendar.DAY_OF_MONTH) + " "
					+ app.getChineseWeek(calEnd));
			System.out.println("工作日：" + app.getWorkingDay(calStart, calEnd));
			System.out.println("休息日：" + app.getHolidays(calStart, calEnd));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 获取日期之间的天数
	 * @param d1
	 * @param d2
	 * @return
	 */
	public int getDaysBetween(Calendar d1, Calendar d2) {
		if (d1.after(d2)) { // swap dates so that d1 is start and d2 is end
			Calendar swap = d1;
			d1 = d2;
			d2 = swap;
		}
		int days = d2.get(Calendar.DAY_OF_YEAR) - d1.get(Calendar.DAY_OF_YEAR);
		int y2 = d2.get(Calendar.YEAR);
		if (d1.get(Calendar.YEAR) != y2) {
			d1 = (Calendar) d1.clone();
			do {
				days += d1.getActualMaximum(Calendar.DAY_OF_YEAR);
				d1.add(Calendar.YEAR, 1);
			} while (d1.get(Calendar.YEAR) != y2);
		}
		return days;
	}

	/**
	 * 获取工作日
	 * @param d1
	 * @param d2
	 * @return
	 */
	public int getWorkingDay(Calendar d1, Calendar d2) {
		int result = -1;
		if (d1.after(d2)) { // swap dates so that d1 is start and d2 is end
			Calendar swap = d1;
			d1 = d2;
			d2 = swap;
		}
		int chargeStartDate = 0;// 开始日期的日期偏移量
		int chargeEndDate = 0;// 结束日期的日期偏移量
		// 日期不在同一个日期内
		int stmp;
		int etmp;
		stmp = 7 - d1.get(Calendar.DAY_OF_WEEK);
		etmp = 7 - d2.get(Calendar.DAY_OF_WEEK);
		if (stmp != 0 && stmp != 6) {// 开始日期为星期六和星期日时偏移量为0
			chargeStartDate = stmp - 1;
		}
		if (etmp != 0 && etmp != 6) {// 结束日期为星期六和星期日时偏移量为0
			chargeEndDate = etmp - 1;
		}
		result = (getDaysBetween(this.getNextMonday(d1), this.getNextMonday(d2))
				/ 7) * 5 + chargeStartDate - chargeEndDate;
		return result;
	}

	/**
	 * 获取中文日期
	 * @param date
	 * @return
	 */
	public String getChineseWeek(Calendar date) {
		final String[] dayNames = {"星期日", "星期一", "星期二", "星期三", "星期四", "星期五",
				"星期六"};
		int dayOfWeek = date.get(Calendar.DAY_OF_WEEK);
		return dayNames[dayOfWeek - 1];
	}

	/**
	 * 获得日期的下一个星期一的日期
	 * @param date
	 * @return
	 */
	public Calendar getNextMonday(Calendar date) {
		Calendar result = null;
		result = date;
		do {
			result = (Calendar) result.clone();
			result.add(Calendar.DATE, 1);
		} while (result.get(Calendar.DAY_OF_WEEK) != 2);
		return result;
	}

	/**
	 * 获取休息日
	 * @param d1
	 * @param d2
	 * @return
	 */
	public int getHolidays(Calendar d1, Calendar d2) {
		return this.getDaysBetween(d1, d2) - this.getWorkingDay(d1, d2);
	}

}
