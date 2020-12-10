package com.refinement.util;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import com.refinement.http.PageResult;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.*;
import java.util.Date;
import java.util.List;

public class CommonUtils {

	public static Date localDateTime2Date( LocalDateTime localDateTime) {
		ZoneId zoneId = ZoneId.systemDefault();
		ZonedDateTime zdt = localDateTime.atZone(zoneId);//Combines this date-time with a time-zone to create a  ZonedDateTime.
		return Date.from(zdt.toInstant());
	}

	public static Date localDateToDate(LocalDate localDate) {
		ZoneId zone = ZoneId.systemDefault();
		Instant instant = localDate.atStartOfDay().atZone(zone).toInstant();
		return Date.from(instant);
	}

	/**
	 * PageHelper 分页
	 * @param pages 分页数据
	 * @param page 页
	 * @return PageInfo
	 */
	public static <T> PageResult pageInfo(Page<T> pages, Integer page) {
		//分页结果
		List<T> result = pages.getResult();
		//PageInfo
		PageInfo<T> pageInfo = new PageInfo<>(result);
		//初始化PageInfo
		PageResult pageResult = new PageResult();
		// 当前页
		pageResult.setCurrent(Integer.parseInt(page.toString()));
		// 当前页大小
		pageResult.setSize(pageInfo.getSize());
		// 总记录数
		pageResult.setTotal(pages.getTotal());
		return pageResult;
	}

	/**
	 * 判断字符串是否未指定格式
	 *
	 * @param str 字符串
	 * @return boolean
	 */
	public static boolean checkDate(String str) {
		boolean convertSuccess = true;
		// 指定日期格式为四位年/两位月份/两位日期，注意yyyy/MM/dd区分大小写；
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM");
		try {
		// 设置lenient为false. 否则SimpleDateFormat会比较宽松地验证日期，比如2007/02/29会被接受，并转换成2007/03/01
			format.setLenient(false);
			format.parse(str);
		} catch (ParseException e) {
			// e.printStackTrace();
		// 如果throw java.text.ParseException或者NullPointerException，就说明格式不对
			convertSuccess = false;
		}
		return !convertSuccess;
	}

	public static LocalDate dateToLocalDate(Date date) {
		Instant instant = date.toInstant();
		ZoneId zone = ZoneId.systemDefault();
		LocalDateTime localDateTime = LocalDateTime.ofInstant(instant, zone);
		return localDateTime.toLocalDate();
	}

	/**
	 * 通过时间秒毫秒数判断两个时间的间隔
	 * @param date1 日期一
	 * @param date2 日期二
	 * @return int
	 */
	public static int differDay(Date date1, Date date2) {
		return (int) ((date2.getTime() - date1.getTime()) / (1000*3600*24));
	}
}
