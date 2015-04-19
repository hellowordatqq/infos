/**
 * Baidu.com Inc. Copyright (c) 2000-2014 All Rights Reserved.
 */
package com.github.colingan.infos.web.main.model;

import java.util.List;
import java.util.Map.Entry;

import com.github.colingan.infos.dal.blogs.bo.Blog;
import com.github.colingan.infos.dal.category.bo.Category;
import com.github.colingan.infos.dal.link.bo.Link;
import com.github.colingan.infos.web.model.AbstractBaseModel;

/**
 * @title MainModel
 * @author Gan Jia (ganjia@baidu.com)
 * @version 1.0
 */
public class MainModel extends AbstractBaseModel {

  private static final long serialVersionUID = -8517449613990835650L;
  private String[] banner;
  private List<Entry<Category, List<Entry<Category, List<Blog>>>>> newBlogs;

  private List<Link> links;

  public String[] getBanner() {
    return banner;
  }

  public void setBanner(String[] banner) {
    this.banner = banner;
  }

  public List<Entry<Category, List<Entry<Category, List<Blog>>>>> getNewBlogs() {
    return newBlogs;
  }

  public void setNewBlogs(List<Entry<Category, List<Entry<Category, List<Blog>>>>> newBlogs) {
    this.newBlogs = newBlogs;
  }

  public List<Link> getLinks() {
    return links;
  }

  public void setLinks(List<Link> links) {
    this.links = links;
  }

}
