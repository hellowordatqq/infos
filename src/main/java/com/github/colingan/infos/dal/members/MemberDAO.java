package com.github.colingan.infos.dal.members;

import com.github.colingan.infos.dal.common.IGenericDAO;
import com.github.colingan.infos.dal.members.bo.Member;

public interface MemberDAO extends IGenericDAO<Member> {

  Long add(Member member);

  boolean updateMemberRole(long id, int role);
}
