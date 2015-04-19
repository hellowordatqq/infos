package com.github.colingan.infos.biz.services;

import com.github.colingan.infos.dal.common.Condition;
import com.github.colingan.infos.dal.common.LogicGroupCondition;
import com.github.colingan.infos.dal.common.LogicGroupCondition.LogicOperation;

public abstract class AbstractBaseService {

  protected Condition compoundCondition(Condition condition, Condition childCondition,
      LogicOperation logic) {
    if (childCondition == null) {
      return condition;
    }
    if (condition == null) {
      return childCondition;
    }
    return new LogicGroupCondition(condition, logic, childCondition);
  }

}
