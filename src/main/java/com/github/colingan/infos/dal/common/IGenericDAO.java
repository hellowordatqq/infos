/**
 * Baidu.com Inc. Copyright (c) 2000-2014 All Rights Reserved.
 */
package com.github.colingan.infos.dal.common;

import java.util.List;

/**
 * DAO基础接口，主要处理简单的ORM和查询、分页需求
 * 
 * @title IGenericDAO
 * @author Gan Jia (ganjia@baidu.com)
 * @date 2014年11月6日
 * @version 1.0
 */
public interface IGenericDAO<T> {

  /**
   * 查询表中指定字段的“所有记录”
   * 
   * @param userid 账户id 兼容按id分库表的需求，estart一期暂无
   * @param fields 要查询的列
   * @return 符合查询需求的记录对象列表
   */
  List<T> getObjects(Long userid, List<Field> fields);

  /**
   * 查询表中指定字段的“所有记录”且按照指定的要求排序
   * 
   * @param userid 账户id 兼容按id分库表的需求，estart一期暂无
   * @param fields 要查询的列
   * @param orderBy 排序规则
   * @return 符合查询和排序需求的记录对象列表
   */
  List<T> getObjects(Long userid, List<Field> fields, OrderBy orderBy);

  /**
   * 查询表中指定字段满足检索条件按的“所有记录”
   * 
   * @param userid 账户id 兼容按id分库表的需求，estart一期暂无
   * @param fields 要查询的列
   * @param condition 查询条件
   * @return 符合查询条件的记录对象列表
   */
  List<T> getObjects(Long userid, List<Field> fields, Condition condition);

  /**
   * 查询表中指定字段的满足检索条件的“所有记录”并按要求排序
   * 
   * @param userid 账户id 兼容按id分库表的需求，estart一期暂无
   * @param fields 要查询的列
   * @param condition 查询条件
   * @param orderBy 排序规则
   * @return 符合查询条件和排序需求的记录对象列表
   */
  List<T> getObjects(Long userid, List<Field> fields, Condition condition, OrderBy orderBy);

  /**
   * 查询表中指定字段的满足检索条件、排序规则的指定范围的记录
   * 
   * @param userid 账户id 兼容按id分库表的需求，estart一期暂无
   * @param fields 要查询的列
   * @param condition 查询条件
   * @param orderBy 排序规则
   * @param offset 起始偏移量
   * @param pageSize 需要的记录条数
   * @return 符合查询条件、排序需求和分页需求的记录对象列表
   */
  List<T> getObjects(Long userid, List<Field> fields, Condition condition, OrderBy orderBy,
      Long offset, Long pageSize);

  /**
   * 删除给定条件的记录
   * 
   * @param userid 账户id 兼容按id分库表的需求，estart一期暂无
   * @param condition 指定的删除条件
   * @return 删除指定条件的记录
   */
  int delete(Long userid, Condition condition);
}
