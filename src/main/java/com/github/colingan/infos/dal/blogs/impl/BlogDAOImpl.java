package com.github.colingan.infos.dal.blogs.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.Validate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import com.github.colingan.infos.dal.blogs.BlogDAO;
import com.github.colingan.infos.dal.blogs.bo.Blog;
import com.github.colingan.infos.dal.common.Condition;
import com.github.colingan.infos.dal.common.GenericDAO;
import com.github.colingan.infos.dal.common.TN;

public class BlogDAOImpl extends GenericDAO<Blog> implements BlogDAO {

  @Override
  public List<Blog> getLatestBlogs(int latestCount) {
    StringBuilder sql = new StringBuilder();

    sql.append(
        "select a.id as id, a.username as userName, a.category1 as category1, a.category2 as category2, a.title as title, a.addtime as addTime, a.updatetime as updateTime from ")
        .append(getTableName(DEFAULT_USERID))
        .append(" a where ? > (select count(1) from ")
        .append(getTableName(DEFAULT_USERID))
        .append(" where category1 = a.category1 and id > a.id) order by a.category1 asc, a.id desc");

    List<Blog> rel =
        trans(getJdbcTemplate(DEFAULT_USERID).queryForList(sql.toString(), latestCount));

    return rel;
  }

  @Override
  public long count(long rootCategory, long childCategory) {
    StringBuilder sql = new StringBuilder();
    sql.append("select count(id) from ").append(getTableName(DEFAULT_USERID))
        .append(" where category1 = ? and category2 = ?");

    return getJdbcTemplate(DEFAULT_USERID).queryForInt(sql.toString(), rootCategory, childCategory);
  }

  @Override
  public long count(Condition condition) {
    if (condition == null) {
      throw new IllegalArgumentException("condition is required for count operation. - "
          + condition);
    }
    StringBuilder sql = new StringBuilder();
    List<Object> params = new ArrayList<Object>();
    sql.append("select count(id) from ").append(getTableName(DEFAULT_USERID)).append(" where ")
        .append(condition.toSqlClause(params));
    return getJdbcTemplate(DEFAULT_USERID).queryForInt(sql.toString(), params.toArray());

  }

  @Override
  public int updateBlog(Blog blog) {
    Validate.notNull(blog);
    // only title, updatetime is updatable
    StringBuilder sql = new StringBuilder();
    sql.append("update ").append(getTableName(DEFAULT_USERID))
        .append(" set title = ?, updatetime = ? where id = ?");
    List<Object> params = new ArrayList<Object>();
    params.add(blog.getTitle());
    params.add(new Timestamp(System.currentTimeMillis()));
    params.add(blog.getId());
    return getJdbcTemplate(DEFAULT_USERID).update(sql.toString(), params.toArray());
  }

  @Override
  public long addBlog(final Blog newBlog) {
    Validate.notNull(newBlog);
    final StringBuilder sql = new StringBuilder();
    sql.append("insert into ")
        .append(getTableName(DEFAULT_USERID))
        .append(
            " (username, category1, category2, title, content, addtime, updatetime) values (?,?,?,?,?,?,?) ");
    KeyHolder holder = new GeneratedKeyHolder();
    getJdbcTemplate(DEFAULT_USERID).update(new PreparedStatementCreator() {

      @Override
      public PreparedStatement createPreparedStatement(Connection conn) throws SQLException {

        PreparedStatement ps =
            conn.prepareStatement(sql.toString(), Statement.RETURN_GENERATED_KEYS);
        int idx = 1;
        ps.setString(idx++, newBlog.getUserName());
        ps.setLong(idx++, newBlog.getCategory1());
        ps.setLong(idx++, newBlog.getCategory2());
        ps.setString(idx++, newBlog.getTitle());
        ps.setString(idx++, newBlog.getContent());
        ps.setTimestamp(idx++, new Timestamp(newBlog.getAddTime().getTime()));
        ps.setTimestamp(idx++, new Timestamp(newBlog.getUpdateTime().getTime()));
        return ps;
      }
    }, holder);
    return holder.getKey().longValue();
  }

  @Override
  protected String getTableName(Long userid) {
    return TN.TABLE_BLOGS;
  }

}
