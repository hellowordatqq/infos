package com.github.colingan.infos.web.admin.controller;

import java.io.File;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.Validate;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.github.colingan.infos.biz.BizConstants;
import com.github.colingan.infos.common.utils.FileUtil;
import com.github.colingan.infos.dal.common.Field;
import com.github.colingan.infos.dal.constants.RoleGroup;
import com.github.colingan.infos.dal.members.bo.Member;
import com.github.colingan.infos.web.admin.model.JTableModel;
import com.github.colingan.infos.web.admin.model.JTableModel.ErrorCode;
import com.github.colingan.infos.web.controller.BaseController;

/**
 * Controller for administration functions.
 * 
 * @title AdminController
 * @author Gan Jia (ganjia@baidu.com)
 * @date 2015年1月20日
 * @version 1.0
 */
public abstract class AdminController extends BaseController {

  protected void baseAdminMemberCheck(HttpServletRequest request) {
    Member member = (Member) request.getAttribute(BizConstants.MEMBER_ROLE_GROUP);
    Validate.notNull(member, "member is required.");
    Validate.isTrue(member.getRoleGroup() >= RoleGroup.ADMIN.getValue(),
        "member role should greater than administrator");
  }

  protected Map<String, Object> imageUploaduploadImages(MultipartHttpServletRequest request,
      String destFileDir, boolean needRandomName) {

    Validate.notNull(request);
    Validate.notEmpty(destFileDir);

    this.baseAdminMemberCheck(request);
    JTableModel model = new JTableModel();
    model.jsonRpcVersion(JTableModel.RPC_2_0);

    MultipartFile file = request.getFile(Field.FILE.getKeyName());
    if (file == null) {
      model.jsonRpcFail(new ErrorCode(101, "附件为空！"));
      return model.getDatas();
    }

    File dest = null;

    if (needRandomName) {
      String ext = FileUtil.getExtension(file.getOriginalFilename());
      if (StringUtils.isNotEmpty(ext)) {
        ext = "." + ext;
      }
      dest = new File(destFileDir, UUID.randomUUID().toString() + ext);
    } else {
      dest = new File(destFileDir, file.getOriginalFilename());
    }

    try {
      if (!needRandomName && fileExist(dest)) {
        model.jsonRpcFail(new ErrorCode(102, "文件名已存在！"));
        return model.getDatas();
      }
      if (!fileExist(dest)) {
        dest.createNewFile();
      }
      file.transferTo(dest);
    } catch (Exception e) {
      e.printStackTrace();
      model.jsonRpcFail(new ErrorCode(103, "文件存储失败！"));
      try {
        if (dest != null) {
          dest.delete();
        }
      } finally {
        return model.getDatas();
      }
    }

    model.jsonRpcResult("success");
    model.jsonRpcId(dest.getName());
    return model.getDatas();
  }

  private boolean fileExist(File dest) {
    Validate.notNull(dest);
    if (!dest.getParentFile().exists()) {
      dest.getParentFile().mkdirs();
    }
    return dest.exists();
  }

}
