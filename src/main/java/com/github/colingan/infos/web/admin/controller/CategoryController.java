package com.github.colingan.infos.web.admin.controller;

import java.util.List;
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
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.github.colingan.infos.biz.services.category.CategoryService;
import com.github.colingan.infos.dal.category.bo.Category;
import com.github.colingan.infos.dal.common.Field;
import com.github.colingan.infos.web.admin.model.CategoryConsoleModel;
import com.github.colingan.infos.web.admin.model.JTableModel;

@Controller
public class CategoryController extends AdminController {

  private static final String CATEGORY_CONSOLE = "admin/category";

  @Resource
  private CategoryService categoryService;

  @RequestMapping(value = "/admin")
  public String admin(HttpServletRequest request, HttpServletResponse response) {
    this.baseAdminMemberCheck(request);

    // 默认重定向到分类管理
    return "redirect:/admin/category";
  }

  @RequestMapping(value = "/admin/category/imageUpload")
  @ResponseBody
  public Map<String, Object> uploadImage(MultipartHttpServletRequest request,
      HttpServletResponse response) {
    return super.imageUploaduploadImages(request, this.categoryService.getIconFileDirectory(),
        false);
  }

  @RequestMapping(value = "/category/second/query")
  @ResponseBody
  public Map<String, Object> queryChildCategory(HttpServletRequest request,
      HttpServletResponse response) {
    JTableModel model = new JTableModel();
    String parentCategory = request.getParameter(Field.PARENT_CATEGORY.getKeyName());
    if (StringUtils.isEmpty(parentCategory)) {
      model.fail("一级分类id未指定！");
      return model.getDatas();
    }
    Long pid = null;
    try {
      pid = Long.parseLong(parentCategory);
    } catch (NumberFormatException nfe) {
      model.fail("一级分类id不合法！");
      return model.getDatas();
    }
    try {
      List<Category> datas = this.categoryService.getValidSecondChildLevelContent(pid);
      model.success().addAllRecord(datas);
    } catch (Exception e) {
      model.fail("查询二级分类失败！");
    }
    return model.getDatas();
  }

  @RequestMapping(value = "/admin/category")
  public String categoryConsole(HttpServletRequest request, HttpServletResponse response) {
    this.baseAdminMemberCheck(request);

    // 设置basic数据
    CategoryConsoleModel model = new CategoryConsoleModel();
    model.setBasic(super.prepareBaseModel(request));

    request.setAttribute(MODEL_NAME, model);
    return CATEGORY_CONSOLE;
  }

  @RequestMapping(value = "/admin/rootCategoryList")
  @ResponseBody
  public Map<String, Object> getRootCategoryList(HttpServletRequest request,
      HttpServletResponse responser) {
    this.baseAdminMemberCheck(request);

    JTableModel model = new JTableModel();
    try {
      List<Category> datas = this.categoryService.getValidFirstLevelContent();
      model.success().addAllRecord(datas);
    } catch (Exception e) {
      model.fail("查询一级分类失败！");
    }

    return model.getDatas();
  }

  @RequestMapping(value = "/admin/childCategoryList")
  @ResponseBody
  public Map<String, Object> getChildCategoryList(HttpServletRequest request,
      HttpServletResponse response) {
    this.baseAdminMemberCheck(request);

    return queryChildCategory(request, response);
  }

  @RequestMapping(value = "/admin/addCategory")
  @ResponseBody
  public Map<String, Object> addCategory(HttpServletRequest request, HttpServletResponse response) {
    this.baseAdminMemberCheck(request);
    JTableModel model = new JTableModel();
    String name = request.getParameter(Field.NAME.getKeyName());
    if (StringUtils.isEmpty(name)) {
      model.fail("分类名称不能为空！");
      return model.getDatas();
    }
    Integer level = null;
    try {
      level = Integer.parseInt(request.getParameter(Field.LEVEL.getKeyName()));
      Validate.notNull(level);
    } catch (Exception e) {
      model.fail("level不能为空！");
      return model.getDatas();
    }
    Long parentCategory = null;
    try {
      parentCategory = Long.parseLong(request.getParameter(Field.PARENT_CATEGORY.getKeyName()));
      Validate.notNull(parentCategory);
    } catch (Exception e) {
      model.fail("父类id不能为空！");
      return model.getDatas();
    }

    try {
      Category category =
          this.categoryService.addCategory(super.getUserName(request), name, level,
              parentCategory);
      model.success().setRecord(category);
    } catch (Exception e) {
      model.fail("系统异常！");
    }

    return model.getDatas();
  }

  @RequestMapping(value = "/admin/updateCategory")
  @ResponseBody
  public Map<String, Object> updateCategory(HttpServletRequest request, HttpServletResponse response) {
    this.baseAdminMemberCheck(request);
    JTableModel model = new JTableModel();
    Long id = null;
    try {
      id = Long.parseLong(request.getParameter(Field.ID.getKeyName()));
      Validate.notNull(id);
    } catch (Exception e) {
      model.fail("更新失败，id为空！");
      return model.getDatas();
    }
    String name = request.getParameter(Field.NAME.getKeyName());
    if (StringUtils.isEmpty(name)) {
      model.fail("更新失败，分类名称不能为空！");
      return model.getDatas();
    }
    try {
      if (this.categoryService.updateCategory(super.getUserName(request), id, name)) {
        model.success();
      } else {
        model.fail("更新失败！");
      }
    } catch (Exception e) {
      model.fail("系统异常！");
    }
    return model.getDatas();
  }

  @RequestMapping(value = "/admin/delCategory")
  @ResponseBody
  public Map<String, Object> deleteCategory(HttpServletRequest request, HttpServletResponse response) {
    this.baseAdminMemberCheck(request);

    JTableModel model = new JTableModel();
    Long id = null;
    try {
      id = Long.parseLong(request.getParameter(Field.ID.getKeyName()));
      Validate.notNull(id);
    } catch (Exception e) {
      model.fail("操作失败，分类id为空！");
      return model.getDatas();
    }

    try {
      if (this.categoryService.deleteCategoryCascade(id)) {
        model.success();
      } else {
        model.fail("删除失败，未知分类id");
      }
    } catch (Exception e) {
      model.fail("系统异常！");
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
