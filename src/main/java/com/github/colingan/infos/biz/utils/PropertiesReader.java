package com.github.colingan.infos.biz.utils;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.MissingResourceException;
import java.util.Properties;
import java.util.ResourceBundle;
import java.util.WeakHashMap;

public class PropertiesReader {
  private static Map<String, Properties> filePropMapping = new WeakHashMap<String, Properties>();

  /**
   * 取得指定properties文件的指定key的value
   * 
   * @param file_name properties文件的名字（没有扩展名）
   * @param key 所指定的key
   * @return 指定key对应的value值
   * @throws MissingResourceException 当没有这个properties文件，或该文件中不存在这个key时
   * @author piggie
   * @date 2008-6-10
   * @version 1.0.0
   */
  public static String getValue(String file_name, String key) throws MissingResourceException {
    final ResourceBundle res = ResourceBundle.getBundle(file_name);
    String value = res.getString(key);
    return value.trim();
  }

  /**
   * 将文件中配置信息填充到properties对象中
   * 
   * @see
   * @param file_name
   * @return
   * @author piggie
   * @date 2008-6-10
   * @version 1.0.0
   * @throws IOException
   * @throws FileNotFoundException
   * @throws IOException
   */
  public static Properties fillProperties(String file_name) throws MissingResourceException {

    if (!file_name.endsWith(".properties")) {
      file_name = file_name + ".properties";
    }
    return getProperties(file_name);
  }

  public static Properties getProperties(String fileName) {

    return getProperties(fileName, PropertiesReader.class.getClassLoader());
  }

  public static Properties getProperties(String fileName, ClassLoader cl) {

    Properties properties = new Properties();

    if (filePropMapping.containsKey(fileName)) {
      properties = filePropMapping.get(fileName);
    } else {
      InputStream is = cl.getResourceAsStream(fileName);
      try {
        try {
          properties.load(is);
        } catch (IOException e) {
          throw new RuntimeException("load properties file error", e);
        }
        filePropMapping.put(fileName, properties);
      } finally {
        try {
          is.close();
        } catch (IOException e) {
          throw new RuntimeException("load properties file error", e);
        }
      }

    }
    return properties;
  }
}
