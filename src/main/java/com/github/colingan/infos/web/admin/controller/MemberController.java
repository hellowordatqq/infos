package com.github.colingan.infos.web.admin.controller;

import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.github.colingan.infos.biz.services.category.UserNotFindException;
import com.github.colingan.infos.biz.services.member.MemberService;
import com.github.colingan.infos.dal.common.Field;
import com.github.colingan.infos.dal.constants.RoleGroup;
import com.github.colingan.infos.dal.members.bo.Member;
import com.github.colingan.infos.web.admin.model.CategoryConsoleModel;
import com.github.colingan.infos.web.admin.model.JTableModel;

@Controller
public class MemberController extends AdminController {

  private static final String MEMBER_CONSOLE = "admin/member";

  @Resource
  private MemberService memberService;

  @RequestMapping(value = "/admin/member")
  public String member(HttpServletRequest request, HttpServletResponse response) {
    super.baseAdminMemberCheck(request);
    // 设置basic数据
    CategoryConsoleModel model = new CategoryConsoleModel();
    model.setBasic(super.prepareBaseModel(request));

    request.setAttribute(MODEL_NAME, model);
    return MEMBER_CONSOLE;
  }

  @RequestMapping(value = "/admin/memberList")
  @ResponseBody
  public Map<String, Object> listMembers(HttpServletRequest request, HttpServletResponse response) {
    super.baseAdminMemberCheck(request);
    JTableModel model = new JTableModel();
    try {
      model.addAllRecord(this.memberService.queryAllValidateMembers()).success();
    } catch (Exception e) {
      model.fail("查询用户列表失败！" + e.getMessage());
    }
    return model.getDatas();
  }

  @RequestMapping(value = "/admin/delMember")
  @ResponseBody
  public Map<String, Object> deleteMember(HttpServletRequest request, HttpServletResponse response) {
    super.baseAdminMemberCheck(request);
    JTableModel model = new JTableModel();
    String idStr = request.getParameter(Field.ID.getKeyName());
    if (StringUtils.isEmpty(idStr)) {
      model.fail("记录id为空！");
      return model.getDatas();
    }
    Long id = null;
    try {
      id = Long.parseLong(idStr);
    } catch (NumberFormatException e) {
      model.fail("记录id不合法！");
      return model.getDatas();
    }
    try {
      if (this.memberService.delete(id)) {
        model.success();
      } else {
        model.fail("删除失败！");
      }
    } catch (Exception e) {
      model.fail("系统异常！");
    }
    return model.getDatas();
  }

  @RequestMapping(value = "/admin/updateMember")
  @ResponseBody
  public Map<String, Object> updateMember(HttpServletRequest request, HttpServletResponse response) {
    super.baseAdminMemberCheck(request);
    JTableModel model = new JTableModel();
    String idStr = request.getParameter(Field.ID.getKeyName());
    if (StringUtils.isEmpty(idStr)) {
      model.fail("记录id为空！");
      return model.getDatas();
    }
    Long id = null;
    try {
      id = Long.parseLong(idStr);
    } catch (NumberFormatException e) {
      model.fail("记录id非法！");
      return model.getDatas();
    }
    String roleGroup = request.getParameter(Field.ROLE_GROUP.getKeyName());
    if (StringUtils.isEmpty(roleGroup)) {
      model.fail("请选择用户权限！");
      return model.getDatas();
    }
    Integer role = null;
    try {
      role = Integer.parseInt(roleGroup);
      if (!RoleGroup.validateRoleGroupValue(role)) {
        // 非法值
        throw new NumberFormatException("未知权限取值！ - " + role);
      }
    } catch (NumberFormatException nfe) {
      model.fail("用户权限值非法！");
      return model.getDatas();
    }
    try {
      if (this.memberService.updateMember(id, role)) {
        model.success();
      } else {
        model.fail("更新失败！");
      }
    } catch (Exception e) {
      model.fail("系统异常！");
    }
    return model.getDatas();
  }

  @RequestMapping(value = "/admin/addMember")
  @ResponseBody
  public Map<String, Object> addMember(HttpServletRequest request, HttpServletResponse response) {
    super.baseAdminMemberCheck(request);
    JTableModel model = new JTableModel();
    String userName = request.getParameter(Field.USER_NAME.getKeyName());
    if (StringUtils.isEmpty(userName)) {
      model.fail("用户名必填！");
      return model.getDatas();
    }
    String roleGroup = request.getParameter(Field.ROLE_GROUP.getKeyName());
    if (StringUtils.isEmpty(roleGroup)) {
      model.fail("请选择用户权限！");
      return model.getDatas();
    }
    Integer role = null;
    try {
      role = Integer.parseInt(roleGroup);
    } catch (NumberFormatException nfe) {
      model.fail("用户权限值非法！");
      return model.getDatas();
    }
    // realName
    String realName = request.getParameter(Field.REAL_NAME.getKeyName());
    if (StringUtils.isEmpty(realName)) {
      realName = userName;
    }
    // uid
    String uid = request.getParameter(Field.UID.getKeyName());

    Member member = null;
    try {
      member = this.memberService.addMember(userName, role, realName, uid);
    } catch (UserNotFindException unfe) {
      model.fail("未知用户！");
      return model.getDatas();
    } catch (Exception e) {
      model.fail("用户添加失败！");
      return model.getDatas();
    }

    model.setRecord(member).success();
    return model.getDatas();
  }

  @Value("#[logout.url]")
  protected volatile String logout;

  @Override
  protected String getLogout() {
    return this.logout;
  }
}
