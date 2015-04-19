/**
 * Baidu.com Inc. Copyright (c) 2000-2014 All Rights Reserved.
 */
package com.github.colingan.infos.dal.utils;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.util.StringUtils;

import com.github.colingan.infos.common.utils.JacksonUtil;
import com.github.colingan.infos.dal.common.Field;

/**
 * sql构造辅助类
 * 
 * @title SqlBuilderHelper
 * @author Gan Jia (ganjia@baidu.com)
 * @date 2014年11月6日
 * @version 1.0
 */
public abstract class SqlBuilderHelper {

  /**
   * Fields转sql
   * 
   * @param fields 字段列表
   * @return 转换后的sql
   * @throws IllegalArgumentException: fields为空或存在null时抛IllegalArgumentException.
   */
  public static String getSelectSqlStr(Collection<Field> fields) {
    if (CollectionUtils.isEmpty(fields)) {
      throw new IllegalArgumentException("query fields should not be empty.");
    }
    StringBuilder sb = new StringBuilder();
    Iterator<Field> iterator = fields.iterator();
    Field field = iterator.next();
    if (field == null) {
      throw new IllegalArgumentException("field should not be empty. - "
          + StringUtils.collectionToCommaDelimitedString(fields));
    }
    sb.append(field.getColumnName()).append(" AS ").append(field.getBoName());
    while (iterator.hasNext()) {
      sb.append(",");
      field = iterator.next();
      if (field == null) {
        throw new IllegalArgumentException("field should not be empty. - "
            + StringUtils.collectionToCommaDelimitedString(fields));
      }
      sb.append(field.getColumnName()).append(" AS ").append(field.getBoName());
    }
    return sb.toString();
  }

  /**
   * db记录map转换成bo
   * 
   * @param clazz 转换后的对象类型
   * @param record db记录
   * @return 转换后的对象实例
   * @throws RuntimeException jackson转换失败时抛运行时异常
   */
  public static Object trans(Class<?> clazz, Map<String, Object> record) {
    try {
      return JacksonUtil.str2Obj(JacksonUtil.obj2Str(record), clazz);
    } catch (Exception e) {
      throw new RuntimeException("failed to translate record to object. class - " + clazz
          + ", record - " + record, e);
    }
  }

  // for access
  public static Object trans2(Class<?> clazz, Map<String, Object> record) {
    try {
      Map<String, Object> newRecord = new HashMap<String, Object>(record.size());
      for (Entry<String, Object> entry : record.entrySet()) {
        Field check = Field.queryFieldByBoName(entry.getKey());
        newRecord.put(check == null ? entry.getKey() : check.getBoName(), entry.getValue());
      }
      return JacksonUtil.str2Obj(JacksonUtil.obj2Str(newRecord), clazz);
    } catch (Exception e) {
      throw new RuntimeException("failed to translate record to object. class - " + clazz
          + ", record - " + record, e);
    }
  }
}
