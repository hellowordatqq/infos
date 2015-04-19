package com.github.colingan.infos.dal.members.bo;

import java.io.Serializable;
import java.util.Date;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import com.github.colingan.infos.dal.constants.RoleGroup;

public class Member implements Serializable {

  private static final long serialVersionUID = 5248277785702409001L;

  private long id;
  private String realName;
  private String userName;
  private int roleGroup;
  private int isDel;
  private Date addTime;
  private Date updateTime;

  private String uid;

  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }

  public String getRealName() {
    return realName;
  }

  public void setRealName(String realName) {
    this.realName = realName;
  }

  public String getUserName() {
    return userName;
  }

  public void setUserName(String userName) {
    this.userName = userName;
  }

  public int getRoleGroup() {
    return roleGroup;
  }

  public void setRoleGroup(int roleGroup) {
    this.roleGroup = roleGroup;
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

  public String getUid() {

    return uid;
  }

  public void setUid(String uid) {

    this.uid = uid;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + (int) (id ^ (id >>> 32));
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
    Member other = (Member) obj;
    if (id != other.id)
      return false;
    return true;
  }

  public boolean validate() {
    boolean rel = true;
    rel = rel && StringUtils.isNotEmpty(realName);
    rel = rel && StringUtils.isNotEmpty(userName);
    rel = rel && RoleGroup.validateRoleGroupValue(roleGroup);
    rel = rel && (isDel == 0 || isDel == 1);
    return rel;
  }

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
  }
}
