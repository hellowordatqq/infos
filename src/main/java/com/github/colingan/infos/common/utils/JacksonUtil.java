/**
 * Baidu.com Inc. Copyright (c) 2000-2014 All Rights Reserved.
 */
package com.github.colingan.infos.common.utils;

import java.io.IOException;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.SerializationConfig;
import org.codehaus.jackson.type.TypeReference;

/**
 * jackson工具类
 * 
 * @title JacksonUtil
 * @author Gan Jia (ganjia@baidu.com)
 * @date 2014年11月7日
 * @version 1.0
 */
public abstract class JacksonUtil {
  // can reuse, share globally
  private static final ObjectMapper mapper;

  static {
    mapper = new ObjectMapper();
    mapper.configure(SerializationConfig.Feature.FAIL_ON_EMPTY_BEANS, false);
    mapper.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
  }

  /**
   * json转成对象obj
   * 
   * @param s json串
   * @param valueType 对象类型
   * @return 转换后的对象实例
   * @throws JsonParseException
   * @throws JsonMappingException
   * @throws IOException
   */
  public static final <T> T str2Obj(String s, Class<T> valueType) throws JsonParseException,
      JsonMappingException, IOException {
    return mapper.readValue(s, valueType);
  }

  /**
   * json串转换成java对象
   * 
   * @param s 待转换的json串
   * @param typeReference 需转换的java类型
   * @return 转换后的对象实例
   * @throws JsonParseException
   * @throws JsonMappingException
   * @throws IOException
   */
  public static final <T> T str2Obj(String s, TypeReference<T> typeReference)
      throws JsonParseException, JsonMappingException, IOException {
    return mapper.readValue(s, typeReference);
  }

  public static final String obj2Str(Object obj) throws JsonGenerationException,
      JsonMappingException, IOException {
    return mapper.writeValueAsString(obj);
  }
}
