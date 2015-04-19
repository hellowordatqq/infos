package com.github.colingan.infos.dal.members.impl;

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

import com.github.colingan.infos.dal.common.Condition;
import com.github.colingan.infos.dal.common.GenericDAO;
import com.github.colingan.infos.dal.common.TN;
import com.github.colingan.infos.dal.members.MemberDAO;
import com.github.colingan.infos.dal.members.bo.Member;

public class MemberDAOImpl extends GenericDAO<Member> implements MemberDAO {

  @Override
  public Long add(final Member member) {
    Validate.notNull(member, "member should not be null");
    final StringBuilder sql = new StringBuilder();
    sql.append("insert into ")
        .append(getTableName(DEFAULT_USERID))
        .append(
            " (realname, username, uid, rolegroup, isdel, addtime, updatetime) values (?,?,?,?,?,?,?)");
    KeyHolder holder = new GeneratedKeyHolder();
    getJdbcTemplate(DEFAULT_USERID).update(new PreparedStatementCreator() {

      @Override
      public PreparedStatement createPreparedStatement(Connection conn) throws SQLException {

        PreparedStatement ps =
            conn.prepareStatement(sql.toString(), Statement.RETURN_GENERATED_KEYS);
        int idx = 1;
        ps.setString(idx++, member.getRealName());
        ps.setString(idx++, member.getUserName());
        ps.setString(idx++, member.getUid());
        ps.setInt(idx++, member.getRoleGroup());
        ps.setInt(idx++, member.getIsDel());
        ps.setTimestamp(idx++, new Timestamp(member.getAddTime().getTime()));
        ps.setTimestamp(idx++, new Timestamp(member.getUpdateTime().getTime()));
        return ps;
      }
    }, holder);
    return holder.getKey().longValue();
  }

  @Override
  public boolean updateMemberRole(long id, int role) {
    StringBuilder sql = new StringBuilder();
    sql.append("update ").append(getTableName(DEFAULT_USERID))
        .append(" set rolegroup = ?, updatetime = ? where id = ?");
    List<Object> params = new ArrayList<Object>();
    params.add(role);
    params.add(new Timestamp(System.currentTimeMillis()));
    params.add(id);
    return getJdbcTemplate(DEFAULT_USERID).update(sql.toString(), params.toArray()) == 1;
  }

  @Override
  public int delete(Long userid, Condition condition) {
    if (condition == null) {
      throw new IllegalArgumentException("condition should not be null.");
    }
    StringBuilder sql = new StringBuilder();
    sql.append("update ").append(getTableName(DEFAULT_USERID)).append(" set isdel = 1 where ");
    List<Object> params = new ArrayList<Object>();
    sql.append(condition.toSqlClause(params));
    return getJdbcTemplate(DEFAULT_USERID).update(sql.toString(), params.toArray());
  }

  @Override
  protected String getTableName(Long userid) {
    return TN.TABLE_MEMBERS;
  }

}
