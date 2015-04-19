package com.github.colingan.infos.web.admin.controller;

import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.Validate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.github.colingan.infos.biz.services.link.LinkService;
import com.github.colingan.infos.dal.common.Field;
import com.github.colingan.infos.dal.link.bo.Link;
import com.github.colingan.infos.web.admin.model.CategoryConsoleModel;
import com.github.colingan.infos.web.admin.model.JTableModel;

@Controller
public class LinkController extends AdminController {

  private static final String LINK_CONSOLE = "admin/link";

  @Resource
  private LinkService linkService;

  @RequestMapping(value = "/admin/link")
  public String link(HttpServletRequest request, HttpServletResponse response) {
    super.baseAdminMemberCheck(request);
    // 设置basic数据
    CategoryConsoleModel model = new CategoryConsoleModel();
    model.setBasic(super.prepareBaseModel(request));

    request.setAttribute(MODEL_NAME, model);
    return LINK_CONSOLE;
  }

  @RequestMapping(value = "/admin/linkList")
  @ResponseBody
  public Map<String, Object> listLink(HttpServletRequest request, HttpServletResponse response) {
    super.baseAdminMemberCheck(request);
    JTableModel model = new JTableModel();
    try {
      model.addAllRecord(this.linkService.queryAllLinks()).success();
    } catch (Exception e) {
      model.fail("友情链接列表查询异常！");
    }
    return model.getDatas();
  }

  @RequestMapping(value = "/admin/delLink")
  @ResponseBody
  public Map<String, Object> deleteLink(HttpServletRequest request, HttpServletResponse response) {
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
    } catch (NumberFormatException nfe) {
      model.fail("记录id非法！");
      return model.getDatas();
    }
    try {
      if (this.linkService.delete(id)) {
        model.success();
      } else {
        model.fail("删除失败！");
      }
    } catch (Exception e) {
      model.fail("系统异常！");
    }
    return model.getDatas();
  }

  @RequestMapping(value = "/admin/addOrUpdateLink")
  @ResponseBody
  public Map<String, Object> addOrUpdateLink(HttpServletRequest request,
      HttpServletResponse response) {
    super.baseAdminMemberCheck(request);
    JTableModel model = new JTableModel();

    String name = request.getParameter(Field.NAME.getKeyName());
    if (StringUtils.isEmpty(name)) {
      model.fail("友情链接名称不能为空！");
      return model.getDatas();
    }
    String link = request.getParameter(Field.LINK.getKeyName());
    if (StringUtils.isEmpty(link)) {
      model.fail("链接不能为空！");
      return model.getDatas();
    }

    String idStr = request.getParameter(Field.ID.getKeyName());
    Long id = null;
    if (!StringUtils.isEmpty(idStr)) {
      // update
      try {
        id = Long.parseLong(idStr);
        Validate.notNull(id);
      } catch (Exception e) {
        model.fail("链接id不合法！");
        return model.getDatas();
      }
    }
    if (id == null) {
      // add
      Link linkObj = null;
      try {
        linkObj = this.linkService.addLink(name, link);
        model.setRecord(linkObj).success();
      } catch (Exception e) {
        model.fail("友情链接添加失败！");
      }
    } else {
      // update
      try {
        if (this.linkService.updateLink(id, name, link)) {
          model.success();
        } else {
          model.fail("友情链接更新失败！ - 未知记录");
        }
      } catch (Exception e) {
        model.fail("系统异常！");
      }
    }
    return model.getDatas();
  }

  @Value("#[logout.url]")
  protected volatile String logout;

  @Override
  protected String getLogout() {
    return this.logout;
  }

}
