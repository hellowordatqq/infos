package com.github.colingan.infos.dal.common;

import java.util.List;

import org.apache.commons.lang.Validate;

public class LogicGroupCondition implements Condition {

  private Condition first;
  private LogicOperation operation;
  private Condition second;

  public LogicGroupCondition(Condition first, LogicOperation operation, Condition second) {
    super();
    Validate.notNull(first, "first should not be null in LogicGroupCondition.");
    Validate.notNull(operation, "operation should not be null in LogicGroupCondition.");
    Validate.notNull(second, "second should not be null in LogicGroupCondition.");
    this.first = first;
    this.operation = operation;
    this.second = second;
  }

  @Override
  public String toSqlClause(List<Object> params) {
    Validate.notNull(params, "params should not be null in LogicGroupCondition.");
    StringBuilder sql = new StringBuilder();
    sql.append(" (").append(first.toSqlClause(params)).append(") ")
        .append(operation.getSqlClause()).append(" (").append(second.toSqlClause(params))
        .append(") ");
    return sql.toString();
  }

  public static enum LogicOperation {
    AND, // 与操作
    OR // 或操作
    ;

    private String sqlClause;

    private LogicOperation() {
      this.sqlClause = this.toString().toLowerCase();
    }

    private LogicOperation(String sqlClause) {
      this.sqlClause = sqlClause;
    }

    public String getSqlClause() {
      return sqlClause;
    }
  }
}
