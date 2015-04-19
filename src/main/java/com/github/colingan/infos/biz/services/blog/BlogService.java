package com.github.colingan.infos.biz.services.blog;

import java.util.List;

import com.github.colingan.infos.dal.blogs.bo.Blog;

public interface BlogService {

  long addNewBlog(String userName, String title, long category1, long category2, String content);

  Blog queryBlogDetail(long id);

  boolean updateBlog(long blogId, String title, String content);

  List<Blog> queryBlogsBrief(long rootCategory, long childCategory, long pageNum, long pageSize);

  long getBlogSize(long rootCategory, long childCategory);

  List<Blog> getLatestBlogs(int latestCount);

  Blog queryBlogBrief(long id);

  boolean deleteBlog(long id);

  List<Blog> searchByTitle(String s, long pageNum, long pageSize);

  long getSearchByTitleSize(String s);

}
