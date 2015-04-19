package com.github.colingan.infos.web.filter;

import java.io.IOException;
import java.util.Map;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.codec.binary.Hex;
import org.apache.commons.lang.Validate;
import org.apache.log4j.Logger;

import com.github.colingan.infos.biz.BizConstants;
import com.github.colingan.infos.common.utils.AESUtil;
import com.github.colingan.infos.common.utils.Constants;
import com.github.colingan.infos.common.utils.JacksonUtil;
import com.github.colingan.infos.web.model.CookieUser;
import com.github.colingan.infos.web.utils.CookieUtil;

public class UserAuthFilter implements Filter {

  private static final Logger LOG = Logger.getLogger(UserAuthFilter.class);

  @Override
  public void destroy() {

  }

  @Override
  public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
      throws IOException, ServletException {

    boolean refreshCookie = false;
    // 先读cookie
    HttpServletRequest req = (HttpServletRequest) request;
    final Map<String, String> cookies = CookieUtil.transCookieToMap(req);
    CookieUser user = getAndCheckCookieUser(cookies);

    if (user == null) {
      LOG.debug("no user info find in cookie, try retrieve from url");
      String id = request.getParameter("id");
      String n = request.getParameter("n");
      String d = request.getParameter("d");
      if (id != null && n != null && d != null) {
        user = new CookieUser();
        user.setId(id);
        user.setN(n);
        user.setD(d);
        refreshCookie = true;
      }
    }

    if (user != null) {
      LOG.debug("user info find: " + user);
      request.setAttribute(BizConstants.UUAP_UNAME_KEY, user);
      if (refreshCookie) {
        this.refreshCookie((HttpServletResponse) response, user);
      }
      chain.doFilter(request, response);
    } else {
      LOG.debug("no user info find.");
      // set nobody user
      request.setAttribute(BizConstants.UUAP_UNAME_KEY, CookieUser.NOBODY);
      chain.doFilter(request, response);
    }
  }

  private CookieUser getAndCheckCookieUser(Map<String, String> cookie) {
    if (cookie == null || cookie.isEmpty()) {
      return null;
    }
    String cuser = cookie.get(Constants.C_UKEY);
    if (cuser == null) {
      return null;
    }

    try {
      // return JacksonUtil.str2Obj(DESUtil.decrypt(cuser, Constants.DES_UKEY), CookieUser.class);
      return JacksonUtil
.str2Obj(
          new String(AESUtil.decrypt(Hex.decodeHex(cuser.toCharArray()), Constants.AES_UKEY),
              "UTF-8"),
              CookieUser.class);
    } catch (Exception e) {
      LOG.warn("unknown cuser in cookie. - " + cuser, e);
      return null;
    }
  }

  private void refreshCookie(HttpServletResponse response, CookieUser user) throws ServletException {
    Validate.notNull(response);
    Validate.notNull(user);

    Cookie cuser;
    try {
      // cuser =
      // new Cookie(Constants.C_UKEY, DESUtil.encrypt(JacksonUtil.obj2Str(user),
      // Constants.DES_UKEY));
      cuser =
          new Cookie(Constants.C_UKEY, Hex.encodeHexString(AESUtil.encrypt(
JacksonUtil
              .obj2Str(user).getBytes("UTF-8"), Constants.AES_UKEY)));
    } catch (Exception e) {
      LOG.error("failed to refresh cookie", e);
      throw new ServletException(e);
    }
    cuser.setMaxAge(-1);
    response.addCookie(cuser);
  }

  @Override
  public void init(FilterConfig arg0) throws ServletException {

  }

}
