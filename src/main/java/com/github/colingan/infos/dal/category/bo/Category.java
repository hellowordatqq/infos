package com.github.colingan.infos.dal.category.bo;

import java.io.Serializable;
import java.util.Date;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import com.github.colingan.infos.dal.constants.CategoryLevel;

/**
 * blog category for ones123
 * 
 * @title Category
 * @author Gan Jia (ganjia@baidu.com)
 * @date 2015年1月19日
 * @version 1.0
 */
public class Category implements Serializable {

  private static final long serialVersionUID = 2193179169570223853L;

  private long id;
  private String name;
  private int level;
  private long parentCategory;
  private int isDel;
  private Date addTime;
  private Date updateTime;
  private String updateuser;

  public Category() {}

  public Category(long id) {
    this.id = id;
  }

  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public int getLevel() {
    return level;
  }

  public void setLevel(int level) {
    this.level = level;
  }

  public long getParentCategory() {
    return parentCategory;
  }

  public void setParentCategory(long parentCategory) {
    this.parentCategory = parentCategory;
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

  public String getUpdateuser() {
    return updateuser;
  }

  public void setUpdateuser(String updateuser) {
    this.updateuser = updateuser;
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
    Category other = (Category) obj;
    if (id != other.id)
      return false;
    return true;
  }

  public boolean validate() {
    boolean rel = true;
    rel = rel && StringUtils.isNotEmpty(name);
    rel = rel && CategoryLevel.validateCategoryLevel(level);
    rel = rel && (parentCategory >= 0);
    rel = rel && (isDel == 0 || isDel == 1);
    rel = rel && StringUtils.isNotEmpty(updateuser);

    return rel;
  }

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
  }
}
