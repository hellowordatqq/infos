package com.github.colingan.infos.web.utils;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.Validate;

public class CookieUtil {

  public static final Map<String, String> transCookieToMap(HttpServletRequest request) {
    Validate.notNull(request);
    Map<String, String> rel = new HashMap<String, String>();
    Cookie[] cookies = request.getCookies();
    if (cookies != null) {
      for (Cookie cookie : cookies) {
        rel.put(cookie.getName(), cookie.getValue());
      }
    }
    return rel;
  }
}
