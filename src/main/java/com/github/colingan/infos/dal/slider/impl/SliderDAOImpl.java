package com.github.colingan.infos.dal.slider.impl;

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
import com.github.colingan.infos.dal.slider.SliderDAO;
import com.github.colingan.infos.dal.slider.bo.Slider;

public class SliderDAOImpl extends GenericDAO<Slider> implements SliderDAO {

  @Override
  public int update(long id, Slider slider) {
    Validate.notNull(slider);

    StringBuilder sql = new StringBuilder();
    sql.append("update ").append(getTableName(DEFAULT_USERID))
        .append(" set idx = ?, originname = ?, destname = ?, updatetime = ? where id = ?");
    List<Object> params = new ArrayList<Object>();
    params.add(slider.getIdx());
    params.add(slider.getOriginName());
    params.add(slider.getDestName());
    params.add(new Timestamp(slider.getUpdateTime().getTime()));
    params.add(id);
    return getJdbcTemplate(DEFAULT_USERID).update(sql.toString(), params.toArray());
  }

  @Override
  public long addSlider(final Slider slider) {
    Validate.notNull(slider);
    final StringBuilder sql = new StringBuilder();
    sql.append("insert into ").append(getTableName(DEFAULT_USERID))
        .append(" (idx, originname, destname, isdel, addtime, updatetime) values (?,?,?,?,?,?)");
    KeyHolder holder = new GeneratedKeyHolder();
    getJdbcTemplate(DEFAULT_USERID).update(new PreparedStatementCreator() {

      @Override
      public PreparedStatement createPreparedStatement(Connection conn) throws SQLException {

        PreparedStatement ps =
            conn.prepareStatement(sql.toString(), Statement.RETURN_GENERATED_KEYS);
        int idx = 1;
        ps.setInt(idx++, slider.getIdx());
        ps.setString(idx++, slider.getOriginName());
        ps.setString(idx++, slider.getDestName());
        ps.setInt(idx++, slider.getIsDel());
        ps.setTimestamp(idx++, new Timestamp(slider.getAddTime().getTime()));
        ps.setTimestamp(idx++, new Timestamp(slider.getUpdateTime().getTime()));
        return ps;
      }
    }, holder);
    return holder.getKey().longValue();
  }

  @Override
  public int delete(Long userid, Condition condition) {
    Validate.notNull(condition, "condition should not be null.");
    StringBuilder sql = new StringBuilder();
    sql.append("update ").append(getTableName(DEFAULT_USERID))
        .append(" set isdel = 1, updatetime = ? where ");
    List<Object> params = new ArrayList<Object>();
    params.add(new Timestamp(System.currentTimeMillis()));
    sql.append(condition.toSqlClause(params));

    return getJdbcTemplate(DEFAULT_USERID).update(sql.toString(), params.toArray());
  }

  @Override
  protected String getTableName(Long userid) {
    return TN.TABLE_SLIDER;
  }

}
