package com.github.colingan.infos.biz;

import gdt.infra.cfg.Configs;

import java.util.regex.Pattern;

import com.typesafe.config.Config;


public class BizConstants {

  private static final String OS = System.getProperty("os", "win");
  private static final Config SYS_CONF = Configs.load("sysconf-" + OS + ".properties");

  public static final String DATE_FORMAT_STRING = "yyyy-MM-dd";

  public static final String CONTENT_FILE_DEST = SYS_CONF.getString("content.file.dest");

  public static final String PRODUCT_ATTACHMENT_DIR = CONTENT_FILE_DEST + "/product";

  public static final String SERVICE_CONTENT_DIR = CONTENT_FILE_DEST + "/service";

  public static final String HELP_CONTENT_DIR = CONTENT_FILE_DEST + "/help";

  public static final String APPLICATION_XOCTET_STREAM = "application/x-octet-stream";

  public static final int AJAX_STATUS_OK = 200;

  public static final int AJAX_STATUS_PARAM_ERROR = 300;

  public static final int AJAX_STATUS_SYS_ERROR = 500;

  public static final String EDITOR_FILE_PATH = CONTENT_FILE_DEST + "/editor/";

  public static final String EDITOR_SAVE_URL = "/editorfile/";
  
  public static final String EDITOR_DOWNLOAD_URL = "/editor/download.do";

  public static final String MEMBER_ROLE_GROUP = "_const_member_role_";

  public static final String UUAP_UNAME_KEY = "_const_cas_assertion_";

  public static final String STATIC_FILE_PREFIX = "/onesfile/";

  public static final String SLASH = "/";

  public static final String CHECKED = "on";

  public static final String EMAIL_REGEXP = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
      + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

  public static final Pattern EMIAL_PATTERN = Pattern.compile(EMAIL_REGEXP);
}
