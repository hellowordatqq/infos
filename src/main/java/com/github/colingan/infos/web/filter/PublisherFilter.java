package com.github.colingan.infos.web.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import com.github.colingan.infos.biz.BizConstants;
import com.github.colingan.infos.dal.constants.RoleGroup;
import com.github.colingan.infos.dal.members.bo.Member;
import com.github.colingan.infos.web.exceptions.PermissionDeniedException;

public class PublisherFilter implements Filter {

  @Override
  public void destroy() {

  }

  @Override
  public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
      throws IOException, ServletException {
    // get member info from request
    Member member = (Member) request.getAttribute(BizConstants.MEMBER_ROLE_GROUP);
    if (member == null || member.getRoleGroup() < RoleGroup.READ_WRITE.getValue()) {
      throw new PermissionDeniedException("you are not allowed to visit publisher pages.");
    }
    chain.doFilter(request, response);
  }

  @Override
  public void init(FilterConfig arg0) throws ServletException {}

}
