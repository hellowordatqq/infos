/**
 * Baidu.com Inc. Copyright (c) 2000-2014 All Rights Reserved.
 */
package com.github.colingan.infos.common.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.Validate;
import org.apache.log4j.Logger;

/**
 * 文件工具类
 * 
 * @title FileUtil
 * @author Gan Jia (ganjia@baidu.com)
 * @date 2014年11月7日
 * @version 1.0
 */
public abstract class FileUtil {

  private static final Logger LOGGER = Logger.getLogger(FileUtil.class);

  /**
   * 获取文件扩展名
   * 
   * @param file 输入的文件名
   * @return 文件扩展名，默认是""
   */
  public static String getExtension(String file) {
    String rel = "";
    int idx = file.lastIndexOf(".");
    if (idx > 0) {
      rel = new String(file.substring(idx + 1));
    }
    return rel.trim();
  }

  /**
   * 读取文件内容，以字符的形式返回
   * 
   * @param path 文件所在目录
   * @param fileName 文件名
   * @return 文件内容的字符串表示
   * @throws FileNotFoundException 文件不存在时抛FileNotFoundException
   * @throws IOException 文件读取失败时抛IOException
   */
  public static String getFileContentAsString(String path, String fileName)
      throws FileNotFoundException, IOException {
    Validate.notEmpty(path, "path should not be empty while read file content.");
    Validate.notEmpty(fileName, "fileName should not be empty while read file content.");
    File file = new File(path, fileName);
    return IOUtils.toString(new FileInputStream(file), Constants.UTF_8);
  }
}
