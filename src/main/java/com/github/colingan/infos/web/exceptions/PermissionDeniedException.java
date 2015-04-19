/**
 * Baidu.com Inc. Copyright (c) 2000-2014 All Rights Reserved.
 */
package com.github.colingan.infos.web.exceptions;

/**
 * 权限拒绝异常
 * 
 * @title PermissionDeniedException
 * @author Gan Jia (ganjia@baidu.com)
 * @date 2014年11月11日
 * @version 1.0
 */
public class PermissionDeniedException extends RuntimeException {

  private static final long serialVersionUID = -7088638370971135141L;

  public PermissionDeniedException() {
    super();
  }

  public PermissionDeniedException(String message, Throwable cause) {
    super(message, cause);
  }

  public PermissionDeniedException(String message) {
    super(message);
  }

}
