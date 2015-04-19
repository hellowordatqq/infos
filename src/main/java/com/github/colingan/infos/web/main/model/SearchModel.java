
package com.github.colingan.infos.web.main.model;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import com.github.colingan.infos.web.model.AbstractBaseModel;

public class SearchModel extends AbstractBaseModel {


  private static final long serialVersionUID = -5326523220064748466L;

  private List<Map<String, Object>> blogs;

  private String s;
  private long pageNum;
  private long totalPage;

  public String getS() {

    return s;
  }

  public void setS(String s) {

    this.s = s;
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

