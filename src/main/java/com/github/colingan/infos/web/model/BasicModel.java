/**
 * Baidu.com Inc. Copyright (c) 2000-2014 All Rights Reserved.
 */
package com.github.colingan.infos.web.model;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.github.colingan.infos.dal.category.bo.Category;

/**
 * @title BasicModel
 * @author Gan Jia (ganjia@baidu.com)
 * @version 1.0
 */
public class BasicModel implements Serializable {

  private static final long serialVersionUID = -1432625858105356881L;
  private String userName;
  private String logout;
  private List<Entry<Category, List<Map<String, Object>>>> nav;
  private String role;
  private String tag;
  private List<Map<String, Object>> categorys;
  private int roleLevel;

  public String getUserName() {
    return userName;
  }

  public void setUserName(String userName) {
    this.userName = userName;
  }

  public String getLogout() {
    return logout;
  }

  public void setLogout(String logout) {
    this.logout = logout;
  }

  public List<Entry<Category, List<Map<String, Object>>>> getNav() {

    return nav;
  }

  public void setNav(List<Entry<Category, List<Map<String, Object>>>> nav) {

    this.nav = nav;
  }

  public String getRole() {
    return role;
  }

  public void setRole(String role) {
    this.role = role;
  }

  public String getTag() {
    return tag;
  }

  public void setTag(String tag) {
    this.tag = tag;
  }

  public List<Map<String, Object>> getCategorys() {
    return categorys;
  }

  public void setCategorys(List<Map<String, Object>> categorys) {
    this.categorys = categorys;
  }

  public int getRoleLevel() {
    return roleLevel;
  }

  public void setRoleLevel(int roleLevel) {
    this.roleLevel = roleLevel;
  }

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }
}
