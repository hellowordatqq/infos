package com.github.colingan.infos.web.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.github.colingan.infos.biz.BizConstants;
import com.github.colingan.infos.dal.constants.RoleGroup;
import com.github.colingan.infos.dal.members.bo.Member;

public class ViewCheckFilter implements Filter {

	@Override
	public void destroy() {
		// TODO Auto-generated method stub

	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {
		HttpServletRequest req = (HttpServletRequest) request;
		if (req.getRequestURI().startsWith("/blog")) {
			// get member info from request
			Member member = (Member) request
					.getAttribute(BizConstants.MEMBER_ROLE_GROUP);
			if (member == null
					|| member.getRoleGroup() < RoleGroup.READ_ONLY.getValue()) {
				((HttpServletResponse) response).sendRedirect("/error");
				return;
			}
		}
		chain.doFilter(request, response);
	}

	@Override
	public void init(FilterConfig arg0) throws ServletException {
		// TODO Auto-generated method stub

	}

}
