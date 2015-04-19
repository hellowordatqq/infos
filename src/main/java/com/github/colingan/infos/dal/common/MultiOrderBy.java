package com.github.colingan.infos.dal.common;

import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.Validate;

public class MultiOrderBy implements OrderBy {

  private Set<CommonOrderBy> commons;

  public MultiOrderBy() {
    this.commons = new LinkedHashSet<CommonOrderBy>();
  }

  public void addCommonOrderBy(CommonOrderBy common) {
    Validate.notNull(common, "common order by item should not be null");
    this.commons.add(common);
  }

  @Override
  public String toSqlClause() {
    if (CollectionUtils.isEmpty(commons)) {
      return "";
    }
    StringBuilder sql = new StringBuilder();
    sql.append(" order by ");
    Iterator<CommonOrderBy> it = commons.iterator();
    CommonOrderBy common = it.next();
    sql.append(common.getField().getColumnName()).append(" ")
        .append(common.getDirection().toString().toLowerCase());
    while (it.hasNext()) {
      sql.append(", ");
      common = it.next();
      sql.append(common.getField().getColumnName()).append(" ")
          .append(common.getDirection().toString().toLowerCase());
    }
    return sql.toString();
  }

}
