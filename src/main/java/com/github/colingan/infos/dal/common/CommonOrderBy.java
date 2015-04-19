package com.github.colingan.infos.dal.common;

import org.apache.commons.lang.Validate;

public class CommonOrderBy implements OrderBy {

  private Field field;
  private OrderByDirection direction;

  public CommonOrderBy(Field field, OrderByDirection direction) {
    Validate.notNull(field, "field should not be null in CommonOrderBy");
    Validate.notNull(direction, "direction should not be null in CommonOrderBy");
    this.field = field;
    this.direction = direction;
  }

  @Override
  public String toSqlClause() {
    StringBuilder sql = new StringBuilder();
    sql.append(" order by ").append(field.getColumnName()).append(" ")
        .append(direction.toString().toLowerCase()).append(" ");
    return sql.toString();
  }

  public Field getField() {
    return field;
  }

  public OrderByDirection getDirection() {
    return direction;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((field == null) ? 0 : field.hashCode());
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    CommonOrderBy other = (CommonOrderBy) obj;
    if (field != other.field)
      return false;
    return true;
  }

  public static enum OrderByDirection {
    ASC, // 升序
    DESC; // 降序
  }

}
