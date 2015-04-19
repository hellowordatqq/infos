/**
 * Baidu.com Inc. Copyright (c) 2000-2014 All Rights Reserved.
 */
package com.github.colingan.infos.web.controller;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.Validate;
import org.apache.log4j.Logger;

import com.github.colingan.infos.biz.BizConstants;
import com.github.colingan.infos.biz.services.category.CategoryService;
import com.github.colingan.infos.common.utils.Constants;
import com.github.colingan.infos.dal.category.bo.Category;
import com.github.colingan.infos.dal.common.Field;
import com.github.colingan.infos.dal.members.bo.Member;
import com.github.colingan.infos.web.model.BasicModel;
import com.github.colingan.infos.web.model.CookieUser;

/**
 * 控制器基础类，负责基础数据模型的获取及公共权限需求
 * 
 * @title BaseController
 * @author Gan Jia (ganjia@baidu.com)
 * @date 2014年11月11日
 * @version 1.0
 */
public abstract class BaseController {

  protected static final Logger LOGGER = Logger.getLogger(BaseController.class);
  protected static final String MODEL_NAME = "model";

  protected static final Long DEFAULT_PAGE_SIZE = 10L;
  protected static final Long DEFAULT_PAGE = 1L;
  protected static final String PARAM_PAGE_SIZE = "pageSize";
  protected static final String PARAM_PAGE_NUMBER = "pageNumber";

  @Resource
  protected CategoryService categoryService;

  protected BasicModel prepareBaseModel(HttpServletRequest request) {
    Validate.notNull(request, "request should not be null while doing basic prepare.");
    BasicModel model = new BasicModel();
    String userName = this.getUserName(request);
    model.setUserName(userName);

    this.innerFullfilBasicModel(model, request);
    return model;
  }

  private void innerFullfilBasicModel(BasicModel model, HttpServletRequest request) {
    model.setLogout(getLogout());
    Map<Category, List<Category>> datas = categoryService.queryAllValidCategoryBriefs();
    Map<Category, List<Map<String, Object>>> categorys =
        new LinkedHashMap<Category, List<Map<String, Object>>>();
    if (datas != null && datas.size() > 0) {
      for (Entry<Category, List<Category>> entry : datas.entrySet()) {
        categorys.put(entry.getKey(), new ArrayList<Map<String, Object>>());
        if (CollectionUtils.isNotEmpty(entry.getValue())) {
          for (Category category : entry.getValue()) {
            Map<String, Object> innerMap = new HashMap<String, Object>();
            innerMap.put(Field.ID.getKeyName(), category.getId());
            innerMap.put(Field.NAME.getKeyName(), category.getName());
            categorys.get(entry.getKey()).add(innerMap);
          }
        }
      }
    }
    model.setNav(new ArrayList<Entry<Category, List<Map<String, Object>>>>(categorys.entrySet()));
    // role level
    model.setRoleLevel(((Member) request.getAttribute(BizConstants.MEMBER_ROLE_GROUP))
        .getRoleGroup());
    // tag
    model.setTag(request.getRequestURI());
  }

  /**
   * 通用文件输出接口
   * 
   * @param response http响应
   * @param path 文件目录
   * @param fileName 文件名
   * @throws IOException http响应输出失败抛IO异常
   * @throws IllegalArgumentException 下载的文件不存在时抛参数异常
   */
  protected void generalFileDownload(HttpServletRequest request, HttpServletResponse response,
      String path, String fileName) throws IOException {
    Validate.notNull(path);
    Validate.notNull(fileName);
    Validate.notNull(response);
    File file = new File(path, fileName);
    if (!file.exists()) {
      throw new IllegalArgumentException("no file find. - " + fileName);
    }
    response.reset();
    OutputStream os = response.getOutputStream();
    response.setContentType("application/octet-stream; charset=utf-8");
    response
        .setHeader("Content-Disposition",
            "attachment;filename="
                + new String(fileName.getBytes(Constants.UTF_8), Constants.ISO_8859));
    os.write(IOUtils.toByteArray(new BufferedInputStream(new FileInputStream(file))));
    os.flush();
    os.close();
  }

  /**
   * 获取登录用户名
   * 
   * @param request http请求
   * @return 用户名（邮箱前缀）
   */
  protected String getUserName(HttpServletRequest request) {
    CookieUser user = (CookieUser) request.getAttribute(BizConstants.UUAP_UNAME_KEY);
    return user.getN();
  }

  public void setCategoryService(CategoryService categoryService) {
    this.categoryService = categoryService;
  }

  protected abstract String getLogout();

}
