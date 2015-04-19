package com.github.colingan.infos.dal.common;

import java.util.List;

public interface Condition {

  String toSqlClause(List<Object> params);
}
