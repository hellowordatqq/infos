/**
 * Baidu.com Inc. Copyright (c) 2000-2014 All Rights Reserved.
 */
package com.github.colingan.infos.common.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.lang.Validate;

/**
 * 时间日期工具类
 * 
 * @title DateTimeUtil
 * @author Gan Jia (ganjia@baidu.com)
 * @date 2014年11月7日
 * @version 1.0
 */
public abstract class DateTimeUtil {

  private static final String FORMAT_SECOND = "yyyy-MM-dd HH:mm:ss";
  private static final String FORMAT_MINUTE = "yyyy-MM-dd HH:mm";
  private static final String FORMAT_DAY = "yyyy-MM-dd";

  public static final long ONE_DAY_IN_MILLISECOND = 86400000;

  /**
   * 日期对象转成字符，精确到秒，格式：yyyy-MM-dd HH:mm:ss
   * 
   * @param date 待转换的日期实例
   * @return 转换后的字符串
   */
  public static final String dateToSecond(Date date) {
    return generalDateFormat(date, FORMAT_SECOND);
  }

  /**
   * 日期对象转成字符，精确到分，格式：yyyy-MM-dd HH:mm
   * 
   * @param date 待转换的日期实例
   * @return 转换后的字符串
   */
  public static final String dateToMiute(Date date) {
    return generalDateFormat(date, FORMAT_MINUTE);
  }

  /**
   * 日期对象转成字符，精确到填，格式：yyyy-MM-dd
   * 
   * @param date 待转换的日期实例
   * @return 转换后的字符串
   */
  public static final String dateToDay(Date date) {
    return generalDateFormat(date, FORMAT_DAY);
  }

  /**
   * 按指定的格式将日期对象转换成字符串
   * 
   * @param date 待转换的日期实例
   * @param format 格式
   * @return 转换后的字符串
   */
  public static final String generalDateFormat(Date date, String format) {
    SimpleDateFormat f = new SimpleDateFormat(format);
    return f.format(date);
  }

  public static int daysBetween(Date date1, Date date2) {
    Validate.notNull(date1);
    Validate.notNull(date2);

    SimpleDateFormat format = new SimpleDateFormat(FORMAT_DAY);
    try {
      long time1 = format.parse(dateToDay(date1)).getTime();
      long time2 = format.parse(dateToDay(date2)).getTime();
      return (int) ((time1 - time2) / ONE_DAY_IN_MILLISECOND);
    } catch (Exception e) {
      // not posible
      return -1;
    }
  }
}
