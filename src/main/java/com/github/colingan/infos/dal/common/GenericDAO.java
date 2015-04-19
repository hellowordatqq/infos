/**
 * Baidu.com Inc. Copyright (c) 2000-2014 All Rights Reserved.
 */
package com.github.colingan.infos.dal.common;

import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;
import org.springframework.jdbc.core.JdbcTemplate;

import com.github.colingan.infos.dal.utils.SqlBuilderHelper;

/**
 * DAO基础接口的实现
 * 
 * @title GenericDAO
 * @author Gan Jia (ganjia@baidu.com)
 * @date 2014年11月6日
 * @version 1.0
 */
public abstract class GenericDAO<T> implements IGenericDAO<T> {

  protected static final Logger LOGGER = Logger.getLogger(GenericDAO.class);
  protected static final Long DEFAULT_USERID = 0L;
  protected JdbcTemplate template;
  private Class<T> clazz;

  public GenericDAO() {
    clazz =
        (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
  }

  @Override
  public List<T> getObjects(Long userid, List<Field> fields) {
    return getObjects(userid, fields, null, null, null, null);
  }

  @Override
  public List<T> getObjects(Long userid, List<Field> fields, OrderBy orderBy) {
    return getObjects(userid, fields, null, orderBy, null, null);
  }

  @Override
  public List<T> getObjects(Long userid, List<Field> fields, Condition condition) {
    return getObjects(userid, fields, condition, null, null, null);
  }

  @Override
  public List<T> getObjects(Long userid, List<Field> fields, Condition condition, OrderBy orderBy) {
    return getObjects(userid, fields, condition, orderBy, null, null);
  }

  @Override
  public List<T> getObjects(Long userid, List<Field> fields, Condition condition, OrderBy orderBy,
      Long offset, Long pageSize) {
    if (userid == null) {
      userid = DEFAULT_USERID;
    }
    StringBuilder sql = new StringBuilder();
    List<Object> params = new ArrayList<Object>();
    sql.append("select ").append(SqlBuilderHelper.getSelectSqlStr(fields)).append(" from ")
        .append(getTableName(userid)).append(" ");
    if (condition != null) {
      sql.append(" where ").append(condition.toSqlClause(params));
    }

    if (orderBy != null) {
      sql.append(" ").append(orderBy.toSqlClause());
    }

    if (offset != null && pageSize != null) {
      sql.append(" limit ?, ?");
      params.add(offset);
      params.add(pageSize);
    }

    if (CollectionUtils.isNotEmpty(params)) {
      return trans(template.queryForList(sql.toString(), params.toArray()));
    }
    return trans(template.queryForList(sql.toString()));
  }

  @Override
  public int delete(Long userid, Condition condition) {
    if (condition == null) {
      throw new IllegalArgumentException("delete operation without condition is not allowed. - "
          + condition);
    }
    if (userid == null) {
      userid = DEFAULT_USERID;
    }
    StringBuilder sql = new StringBuilder();
    List<Object> params = new ArrayList<Object>();
    sql.append("delete from ").append(getTableName(userid)).append(" where ")
        .append(condition.toSqlClause(params));

    return template.update(sql.toString(), params.toArray());
  }

  abstract protected String getTableName(Long userid);

  protected List<T> trans(List<Map<String, Object>> records) {
    List<T> datas = new ArrayList<T>();
    for (Map<String, Object> record : records) {
      datas.add((T) SqlBuilderHelper.trans2(clazz, record));
    }
    return datas;
  }

  public void setTemplate(JdbcTemplate template) {
    this.template = template;
  }

  protected JdbcTemplate getJdbcTemplate(long userid) {
    return template;
  }

}
