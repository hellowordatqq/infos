package com.github.colingan.infos.dal.common;

import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang.Validate;

public class ComparisonCondition implements Condition {

  private Field field;
  private Operation operation;
  private Object value;

  public ComparisonCondition(Field field, Operation operation, Object value) {
    super();
    Validate.notNull(field, "field should not be null for condition object");
    Validate.notNull(operation, "operation should not be null for condition object.");
    Validate.notNull(value, "value should not be null for condition object.");
    this.field = field;
    this.operation = operation;
    this.value = value;
  }

  @Override
  public String toSqlClause(List<Object> params) {
    Validate.notNull(params, "params holder should not be null while compile sql clause.");
    switch (operation) {
      case EQ:
      case GT:
      case GE:
      case LT:
      case LE:
      case NE:
      case LK:
        return singleComparationSqlClause(params);
      case IN:
      case NI:
        return multiComparationSqlClause(params);
    }
    throw new IllegalArgumentException("unknown operation find.");
  }

  private String singleComparationSqlClause(List<Object> params) {
    StringBuilder sql = new StringBuilder();
    sql.append(field.getColumnName()).append(operation.getSqlClause()).append("? ");
    params.add(value);
    return sql.toString();
  }

  private String multiComparationSqlClause(List<Object> params) {
    if (value instanceof Iterable) {
      Iterator it = ((Iterable) value).iterator();
      StringBuilder sql = new StringBuilder();
      sql.append(field.getColumnName()).append(" ").append(operation.getSqlClause()).append(" (");
      Object item = it.next();
      sql.append("?");
      params.add(item);
      while (it.hasNext()) {
        item = it.next();
        sql.append(", ?");
        params.add(item);
      }
      sql.append(")");
      return sql.toString();
    }
    throw new RuntimeException("invalid value type find for multi comparation : "
        + value.getClass());
  }

  public static enum Operation {

    EQ("="), // 相等
    GT(">"), // 大于
    GE(">="), // 不小于
    LT("<"), // 小于
    LE("<="), // 不大于
    NE("!="), // 不等于
    IN("in"), // in操作
    NI("not in"), // not in 操作
    LK(" like "), // like操作
    ;

    private String sqlClause;

    private Operation(String sqlClause) {
      this.sqlClause = sqlClause;
    }

    public String getSqlClause() {
      return sqlClause;
    }
  }

}
