package com.github.colingan.infos.dal.slider.bo;

import java.io.Serializable;
import java.util.Date;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

public class Slider implements Serializable {

  private static final long serialVersionUID = -945797619752919794L;

  private long id;
  private int idx;
  private String originName;
  private String destName;
  private int isDel;
  private Date addTime;
  private Date updateTime;

  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }

  public int getIdx() {
    return idx;
  }

  public void setIdx(int idx) {
    this.idx = idx;
  }

  public String getOriginName() {
    return originName;
  }

  public void setOriginName(String originName) {
    this.originName = originName;
  }

  public String getDestName() {
    return destName;
  }

  public void setDestName(String destName) {
    this.destName = destName;
  }

  public int getIsDel() {
    return isDel;
  }

  public void setIsDel(int isDel) {
    this.isDel = isDel;
  }

  public Date getAddTime() {
    return addTime;
  }

  public void setAddTime(Date addTime) {
    this.addTime = addTime;
  }

  public Date getUpdateTime() {
    return updateTime;
  }

  public void setUpdateTime(Date updateTime) {
    this.updateTime = updateTime;
  }

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
  }
}
