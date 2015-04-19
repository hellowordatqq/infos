package com.github.colingan.infos.web.blog.model;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import com.github.colingan.infos.dal.category.bo.Category;
import com.github.colingan.infos.web.model.AbstractBaseModel;

public class BlogListModel extends AbstractBaseModel {

  private static final long serialVersionUID = 5608890407581449360L;

  private long rootCategory;
  private long childCategory;
  private List<Category> categorys;
  private List<Map<String, Object>> blogs;

  private long pageNum;
  private long totalPage;

  public long getRootCategory() {
    return rootCategory;
  }

  public void setRootCategory(long rootCategory) {
    this.rootCategory = rootCategory;
  }

  public long getChildCategory() {
    return childCategory;
  }

  public void setChildCategory(long childCategory) {
    this.childCategory = childCategory;
  }

  public List<Category> getCategorys() {
    return categorys;
  }

  public void setCategorys(List<Category> categorys) {
    this.categorys = categorys;
  }

  public List<Map<String, Object>> getBlogs() {
    return blogs;
  }

  public void setBlogs(List<Map<String, Object>> blogs) {
    this.blogs = blogs;
  }

  public long getPageNum() {
    return pageNum;
  }

  public void setPageNum(long pageNum) {
    this.pageNum = pageNum;
  }

  public long getTotalPage() {
    return totalPage;
  }

  public void setTotalPage(long totalPage) {
    this.totalPage = totalPage;
  }

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
  }
}
