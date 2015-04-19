package com.github.colingan.infos.biz.services.member;

import java.util.List;

import com.github.colingan.infos.dal.members.bo.Member;

public interface MemberService {

  /**
   * 查询指定用户的角色信息
   * 
   * @param userName 当前登录用户名
   * @return
   */
  Member queryMemberInfoForUser(String userName);

  /**
   * 查询生效的用户列表
   * 
   * @return
   */
  List<Member> queryAllValidateMembers();

  Member addMember(String userName, int role, String realName, String uid);

  boolean updateMember(long id, int role);

  boolean delete(long id);
}
