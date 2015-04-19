package com.github.colingan.infos.web.model;

import java.io.Serializable;
import java.util.List;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

public class BaseAjaxResponse<T> implements Serializable {

  private static final long serialVersionUID = 4183165277961293781L;

  private int status;
  private List<T> datas;

  public int getStatus() {
    return status;
  }

  public void setStatus(int status) {
    this.status = status;
  }

  public List<T> getDatas() {
    return datas;
  }

  public void setDatas(List<T> datas) {
    this.datas = datas;
  }

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
  }
}
