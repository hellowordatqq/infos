package com.github.colingan.infos.web.model;

/**
 * Baidu.com Inc. Copyright (c) 2000-2014 All Rights Reserved.
 */
import java.io.Serializable;

import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * @title AbstractBaseModel
 * @author Gan Jia (ganjia@baidu.com)
 * @version 1.0
 */
public abstract class AbstractBaseModel implements Serializable {

  private static final long serialVersionUID = -8147263376864241211L;

  // 基础数据，每个model都包含
  private BasicModel basic;

  public BasicModel getBasic() {
    return basic;
  }

  public void setBasic(BasicModel basic) {
    this.basic = basic;
  }

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }

}
