package com.github.colingan.infos.dal.blogs;

import java.util.List;

import com.github.colingan.infos.dal.blogs.bo.Blog;
import com.github.colingan.infos.dal.common.Condition;
import com.github.colingan.infos.dal.common.IGenericDAO;

public interface BlogDAO extends IGenericDAO<Blog> {

  long addBlog(Blog newBlog);

  int updateBlog(Blog blog);

  long count(long rootCategory, long childCategory);

  List<Blog> getLatestBlogs(int latestCount);

  long count(Condition condition);

}
