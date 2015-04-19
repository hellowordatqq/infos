package com.github.colingan.infos.dal.link.impl;

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

import com.github.colingan.infos.dal.common.GenericDAO;
import com.github.colingan.infos.dal.common.TN;
import com.github.colingan.infos.dal.link.LinkDAO;
import com.github.colingan.infos.dal.link.bo.Link;

public class LinkDAOImpl extends GenericDAO<Link> implements LinkDAO {

  @Override
  public Long addLink(final Link link) {
    Validate.notNull(link, "link should not be null.");
    final StringBuilder sql = new StringBuilder();
    sql.append("insert into ").append(getTableName(DEFAULT_USERID))
        .append(" (linkname, link, addtime, updatetime) values (?,?,?,?)");
    KeyHolder keyHolder = new GeneratedKeyHolder();

    getJdbcTemplate(DEFAULT_USERID).update(new PreparedStatementCreator() {

      @Override
      public PreparedStatement createPreparedStatement(Connection conn) throws SQLException {

        PreparedStatement ps =
            conn.prepareStatement(sql.toString(), Statement.RETURN_GENERATED_KEYS);
        int idx = 1;
        ps.setString(idx++, link.getName());
        ps.setString(idx++, link.getLink());
        ps.setTimestamp(idx++, new Timestamp(link.getAddTime().getTime()));
        ps.setTimestamp(idx++, new Timestamp(link.getUpdateTime().getTime()));
        return ps;
      }
    }, keyHolder);
    return keyHolder.getKey().longValue();
  }

  @Override
  public boolean update(long id, String name, String link) {
    Validate.notEmpty(name, "name should not be empty.");
    Validate.notEmpty(link, "link should not be empty.");
    StringBuilder sql = new StringBuilder();
    sql.append("update ").append(getTableName(DEFAULT_USERID))
        .append(" set linkname = ?, link = ?, updatetime = ? where id = ?");
    List<Object> params = new ArrayList<Object>();
    params.add(name);
    params.add(link);
    params.add(new Timestamp(System.currentTimeMillis()));
    params.add(id);
    return getJdbcTemplate(DEFAULT_USERID).update(sql.toString(), params.toArray()) == 1;
  }

  @Override
  protected String getTableName(Long userid) {
    return TN.TABLE_LINKS;
  }

}
