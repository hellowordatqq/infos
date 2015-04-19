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
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.github.colingan.infos.biz.services.slider.SliderService;
import com.github.colingan.infos.dal.common.Field;
import com.github.colingan.infos.web.admin.model.CategoryConsoleModel;
import com.github.colingan.infos.web.admin.model.JTableModel;

@Controller
public class SliderController extends AdminController {

  private static final String SLIDER_CONSOLE = "admin/slider";

  @Resource
  private SliderService sliderService;

  @RequestMapping(value = "/admin/slider/imageUpload")
  @ResponseBody
  public Map<String, Object> uploadImage(MultipartHttpServletRequest request,
      HttpServletResponse response) {
    return super.imageUploaduploadImages(request, this.sliderService.getImageFileDirectory(), true);
  }

  @RequestMapping(value = "/admin/slider")
  public String slider(HttpServletRequest request, HttpServletResponse response) {
    super.baseAdminMemberCheck(request);
    // 设置basic数据
    CategoryConsoleModel model = new CategoryConsoleModel();
    model.setBasic(super.prepareBaseModel(request));

    request.setAttribute(MODEL_NAME, model);
    return SLIDER_CONSOLE;
  }

  @RequestMapping(value = "/admin/sliderList")
  @ResponseBody
  public Map<String, Object> listSliders(HttpServletRequest request, HttpServletResponse response) {
    super.baseAdminMemberCheck(request);
    JTableModel model = new JTableModel();
    try {
      model.addAllRecord(this.sliderService.queryAllValidateSliders()).success();
    } catch (Exception e) {
      model.fail("查询轮播图列表失败！");
    }
    return model.getDatas();
  }

  @RequestMapping(value = "/admin/delSlider")
  @ResponseBody
  public Map<String, Object> deleteSlider(HttpServletRequest request, HttpServletResponse response) {
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
      model.fail("记录id值非法！");
      return model.getDatas();
    }

    try {
      if (this.sliderService.deleteSlider(id)) {
        model.success();
      } else {
        model.fail("记录删除失败！");
      }
    } catch (Exception e) {
      model.fail("系统异常！");
    }

    return model.getDatas();
  }

  @RequestMapping(value = "/admin/addOrUpdateSlider")
  @ResponseBody
  public Map<String, Object> addOrUpdateSlider(HttpServletRequest request,
      HttpServletResponse response) {
    super.baseAdminMemberCheck(request);
    JTableModel model = new JTableModel();
    Integer idx = null;
    try {
      idx = Integer.parseInt(request.getParameter(Field.IDX.getKeyName()));
      Validate.notNull(idx);
    } catch (Exception e) {
      model.fail("序号不能为空！");
      return model.getDatas();
    }
    String originName = request.getParameter(Field.ORIGIN_NAME.getKeyName());
    if (StringUtils.isEmpty(originName)) {
      model.fail("文件名不能为空！");
      return model.getDatas();
    }
    String destName = request.getParameter(Field.DEST_NAME.getKeyName());
    if (StringUtils.isEmpty(destName)) {
      model.fail("目标文件名为空，请先上传文件！");
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
      try {
        model.setRecord(this.sliderService.addSlider(idx, originName, destName)).success();
      } catch (Exception e) {
        model.fail("系统异常！");
      }
    } else {
      // update
      try {
        if (this.sliderService.updateSlider(id, idx, originName, destName)) {
          model.success();
        } else {
          model.fail("更新失败！");
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
