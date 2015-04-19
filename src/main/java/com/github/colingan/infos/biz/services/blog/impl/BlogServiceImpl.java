package com.github.colingan.infos.biz.services.blog.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.Validate;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.github.colingan.infos.biz.services.blog.BlogService;
import com.github.colingan.infos.common.utils.Constants;
import com.github.colingan.infos.dal.blogs.BlogDAO;
import com.github.colingan.infos.dal.blogs.bo.Blog;
import com.github.colingan.infos.dal.common.CommonOrderBy;
import com.github.colingan.infos.dal.common.CommonOrderBy.OrderByDirection;
import com.github.colingan.infos.dal.common.ComparisonCondition;
import com.github.colingan.infos.dal.common.ComparisonCondition.Operation;
import com.github.colingan.infos.dal.common.Condition;
import com.github.colingan.infos.dal.common.Field;
import com.github.colingan.infos.dal.common.LogicGroupCondition;
import com.github.colingan.infos.dal.common.LogicGroupCondition.LogicOperation;
import com.github.colingan.infos.dal.common.OrderBy;

@Service
public class BlogServiceImpl implements BlogService {

  private static final Logger LOG = Logger.getLogger(BlogService.class);

  @Value("#[blog.dir]")
  protected volatile String blogDir;

  @Resource
  private BlogDAO blogDao;

  @Override
  public boolean deleteBlog(long id) {
    return this.blogDao.delete(null, new ComparisonCondition(Field.ID, Operation.EQ, id)) == 1;
  }

  @Override
  public Blog queryBlogBrief(long id) {
    List<Field> fields = new ArrayList<Field>();
    fields.add(Field.ID);
    fields.add(Field.TITLE);
    fields.add(Field.USER_NAME);
    fields.add(Field.ADD_TIME);
    fields.add(Field.CATEGORY1);
    fields.add(Field.CATEGORY2);
    Condition condition = new ComparisonCondition(Field.ID, Operation.EQ, id);
    List<Blog> blogs = this.blogDao.getObjects(null, fields, condition);
    if (CollectionUtils.isEmpty(blogs)) {
      return null;
    }
    return blogs.get(0);
  }

  @Override
  public List<Blog> getLatestBlogs(int latestCount) {
    Validate.isTrue(latestCount > 0);
    return this.blogDao.getLatestBlogs(latestCount);
  }

  @Override
  public long getBlogSize(long rootCategory, long childCategory) {
    return this.blogDao.count(rootCategory, childCategory);
  }

  @Override
  public long getSearchByTitleSize(String s) {
    Validate.notNull(s);
    s = "%" + s + "%";
    Condition condition = new ComparisonCondition(Field.TITLE, Operation.LK, s);
    return this.blogDao.count(condition);

  }

  @Override
  public List<Blog> queryBlogsBrief(long rootCategory, long childCategory, long pageNum,
      long pageSize) {
    List<Field> fields = new ArrayList<Field>();
    fields.add(Field.ID);
    fields.add(Field.TITLE);
    fields.add(Field.USER_NAME);
    fields.add(Field.ADD_TIME);

    Condition condition =
        new LogicGroupCondition(new ComparisonCondition(Field.CATEGORY2, Operation.EQ,
            childCategory), LogicOperation.AND, new ComparisonCondition(Field.CATEGORY1,
            Operation.EQ, rootCategory));
    OrderBy orderBy = new CommonOrderBy(Field.ID, OrderByDirection.DESC);

    long offset = (pageNum - 1) * pageSize;
    offset = offset < 0 ? 0 : offset;
    return this.blogDao.getObjects(null, fields, condition, orderBy, offset, pageSize);
  }

  @Override
  public List<Blog> searchByTitle(String s, long pageNum, long pageSize) {
    Validate.notNull(s);
    List<Field> fields = new ArrayList<Field>();
    fields.add(Field.ID);
    fields.add(Field.TITLE);
    fields.add(Field.USER_NAME);
    fields.add(Field.ADD_TIME);

    s = "%" + s + "%";

    Condition condition = new ComparisonCondition(Field.TITLE, Operation.LK, s);
    OrderBy orderBy = new CommonOrderBy(Field.ID, OrderByDirection.DESC);
    long offset = (pageNum - 1) * pageSize;
    offset = offset < 0 ? 0 : offset;
    return this.blogDao.getObjects(null, fields, condition, orderBy, offset, pageSize);

  }

  @Override
  public Blog queryBlogDetail(long id) {
    List<Field> fields = new ArrayList<Field>();
    fields.add(Field.ID);
    fields.add(Field.TITLE);
    fields.add(Field.USER_NAME);
    fields.add(Field.CATEGORY1);
    fields.add(Field.CATEGORY2);
    fields.add(Field.CONTENT);
    fields.add(Field.ADD_TIME);

    Condition condition = new ComparisonCondition(Field.ID, Operation.EQ, id);
    List<Blog> blogs = this.blogDao.getObjects(null, fields, condition);
    if (CollectionUtils.isEmpty(blogs) || blogs.size() != 1) {
      throw new RuntimeException("unknown blog id or dirty id find.");
    }
    Blog blog = blogs.get(0);
    // read blog content
    File file = new File(blogDir, blog.getContent());
    try {
      blog.setContent(IOUtils.toString(new FileInputStream(file), Constants.UTF_8));
    } catch (Exception e) {
      throw new RuntimeException("failed to read blog content file.");
    }

    return blog;
  }

  @Override
  public boolean updateBlog(long blogId, String title, String content) {
    Validate.notEmpty(title, "title should not be empty.");
    Validate.notEmpty(content, "content should not be empty.");
    // query blog first
    List<Field> fields = new ArrayList<Field>();
    fields.add(Field.ID);
    fields.add(Field.CONTENT);
    Condition condition = new ComparisonCondition(Field.ID, Operation.EQ, blogId);
    List<Blog> blogs = this.blogDao.getObjects(null, fields, condition);
    Validate.isTrue(CollectionUtils.isNotEmpty(blogs) && blogs.size() == 1);
    Blog blog = blogs.get(0);
    // update content file
    boolean rel = this.innerWriteBlogContent(blog.getContent(), content);
    if (!rel) {
      return rel;
    }
    // update db record
    blog.setTitle(title);
    return this.blogDao.updateBlog(blog) == 1;
  }

  @Override
  public long addNewBlog(String userName, String title, long category1, long category2,
      String content) {
    Validate.notEmpty(userName, "user name should not empty.");
    Validate.notEmpty(title, "title should not be empty.");
    Validate.notEmpty(content, "content should not be empty.");
    Blog newBlog = new Blog();
    newBlog.setAddTime(new Date());
    newBlog.setCategory1(category1);
    newBlog.setCategory2(category2);
    newBlog.setContent(this.newBlogFileName(userName));
    newBlog.setTitle(title);
    newBlog.setUpdateTime(new Date());
    newBlog.setUserName(userName);

    // write content file
    if (this.innerWriteBlogContent(newBlog.getContent(), content)) {
      return this.blogDao.addBlog(newBlog);
    }
    return 0;
  }

  private boolean innerWriteBlogContent(String fileName, String content) {
    Validate.notEmpty(fileName);
    Validate.notEmpty(content);

    File path = new File(blogDir);
    if (!path.exists()) {
      path.mkdirs();
    }
    File file = new File(path, fileName);
    try {
      IOUtils.write(content, new FileOutputStream(file), Constants.UTF_8);
      return true;
    } catch (Exception e) {
      LOG.error("failed to write blog file", e);
      try {
        // delete useless file.
        if (file.exists()) {
          file.delete();
        }
      } finally {
        return false;
      }
    }
  }

  private String newBlogFileName(String userName) {
    StringBuilder sb = new StringBuilder();
    sb.append(userName).append(System.currentTimeMillis());
    return DigestUtils.md5Hex(sb.toString());
  }

}
