package com.github.colingan.infos.web.blog.model;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import com.github.colingan.infos.web.model.AbstractBaseModel;

public class BlogModel extends AbstractBaseModel {

  private static final long serialVersionUID = -8920715687591724091L;
  protected List<Map<String, Object>> breadcrumb;
  protected String title;
  protected String author;
  protected String addTime;
  protected String content;
  protected long rootCategory;

  public List<Map<String, Object>> getBreadcrumb() {
    return breadcrumb;
  }

  public void setBreadcrumb(List<Map<String, Object>> breadcrumb) {
    this.breadcrumb = breadcrumb;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public String getAuthor() {
    return author;
  }

  public void setAuthor(String author) {
    this.author = author;
  }

  public String getAddTime() {
    return addTime;
  }

  public void setAddTime(String addTime) {
    this.addTime = addTime;
  }

  public String getContent() {
    return content;
  }

  public void setContent(String content) {
    this.content = content;
  }

  public long getRootCategory() {
    return rootCategory;
  }

  public void setRootCategory(long rootCategory) {
    this.rootCategory = rootCategory;
  }

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
  }
}
