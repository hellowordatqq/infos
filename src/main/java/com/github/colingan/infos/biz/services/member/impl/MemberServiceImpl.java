package com.github.colingan.infos.biz.services.member.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.Validate;
import org.springframework.stereotype.Service;

import com.github.colingan.infos.biz.services.AbstractBaseService;
import com.github.colingan.infos.biz.services.member.MemberService;
import com.github.colingan.infos.dal.common.CommonOrderBy;
import com.github.colingan.infos.dal.common.CommonOrderBy.OrderByDirection;
import com.github.colingan.infos.dal.common.ComparisonCondition;
import com.github.colingan.infos.dal.common.ComparisonCondition.Operation;
import com.github.colingan.infos.dal.common.Condition;
import com.github.colingan.infos.dal.common.Field;
import com.github.colingan.infos.dal.common.LogicGroupCondition.LogicOperation;
import com.github.colingan.infos.dal.common.OrderBy;
import com.github.colingan.infos.dal.members.MemberDAO;
import com.github.colingan.infos.dal.members.bo.Member;

/**
 * service implements for ones123 members.
 * 
 * @title MemberServiceImpl
 * @author Gan Jia (ganjia@baidu.com)
 * @date 2015年1月20日
 * @version 1.0
 */
@Service
public class MemberServiceImpl extends AbstractBaseService implements MemberService {

  @Resource
  private MemberDAO memberDao;

  @Override
  public Member queryMemberInfoForUser(String userName) {
    if (StringUtils.isEmpty(userName)) {
      throw new IllegalArgumentException("userName is required while query member infos. - "
          + userName);
    }
    List<Member> members = innerQueryMembers(null, userName, Boolean.TRUE, null);
    if (CollectionUtils.isEmpty(members)) {
      // 库里没有
      return null;
    }
    return members.get(0);
  }

  @Override
  public List<Member> queryAllValidateMembers() {
    List<Field> fields = new ArrayList<Field>();
    fields.add(Field.ID);
    fields.add(Field.REAL_NAME);
    fields.add(Field.USER_NAME);
    fields.add(Field.UID);
    fields.add(Field.ROLE_GROUP);
    return this.innerQueryMembers(null, null, Boolean.TRUE, fields);
  }

  private List<Member> innerQueryMembers(Long id, String userName, Boolean validate,
      List<Field> fields) {
    Condition condition = null;
    Condition firstCondition = null;
    if (id != null) {
      firstCondition = new ComparisonCondition(Field.ID, Operation.EQ, id.longValue());
    }
    condition = compoundCondition(condition, firstCondition, LogicOperation.AND);
    if (!StringUtils.isEmpty(userName)) {
      firstCondition = new ComparisonCondition(Field.USER_NAME, Operation.EQ, userName);
    }
    condition = compoundCondition(condition, firstCondition, LogicOperation.AND);
    if (validate != null) {
      firstCondition = new ComparisonCondition(Field.IS_DEL, Operation.EQ, validate ? 0 : 1);
    }
    condition = compoundCondition(condition, firstCondition, LogicOperation.AND);

    // 默认查询字段
    Set<Field> fieldSet = new HashSet<Field>();
    fieldSet.add(Field.ID);
    fieldSet.add(Field.REAL_NAME);
    fieldSet.add(Field.USER_NAME);
    fieldSet.add(Field.UID);
    fieldSet.add(Field.ROLE_GROUP);

    if (CollectionUtils.isNotEmpty(fields)) {
      fieldSet.addAll(fields);
    }

    // id升序
    OrderBy orderBy = new CommonOrderBy(Field.ID, OrderByDirection.ASC);

    return memberDao.getObjects(null, new ArrayList<Field>(fieldSet), condition, orderBy);
  }

  @Override
  public Member addMember(String userName, int role, String realName, String uid) {
    Validate.notEmpty(userName, "userName should not be empty");
    Validate.notEmpty(realName, "realName should not be empty");
    Member member = new Member();
    member.setIsDel(0);
    member.setAddTime(new Date());
    member.setRoleGroup(role);
    member.setUpdateTime(new Date());
    member.setUserName(userName);
    member.setRealName(realName);

    if (uid == null) {
      uid = "";
    }

    member.setUid(uid);
    if (!member.validate()) {
      throw new RuntimeException("member info is not complete - " + member);
    }

    member.setId(this.memberDao.add(member));

    return member;
  }

  @Override
  public boolean updateMember(long id, int role) {
    return this.memberDao.updateMemberRole(id, role);
  }

  @Override
  public boolean delete(long id) {
    return this.memberDao.delete(null, new ComparisonCondition(Field.ID, Operation.EQ, id)) == 1;
  }

}
