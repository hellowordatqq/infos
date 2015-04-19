package com.github.colingan.infos.web.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import com.github.colingan.infos.biz.BizConstants;
import com.github.colingan.infos.biz.services.member.MemberService;
import com.github.colingan.infos.biz.utils.ApplicationContextProvier;
import com.github.colingan.infos.dal.constants.RoleGroup;
import com.github.colingan.infos.dal.members.bo.Member;
import com.github.colingan.infos.web.model.CookieUser;

public class MemberRoleQueryFilter implements Filter {

	private MemberService memberService;

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {

	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {
		if (this.memberService == null) {
			this.initMemberService(request);
		}
		Member member = null;
		CookieUser user = (CookieUser) request
				.getAttribute(BizConstants.UUAP_UNAME_KEY);
		if (user == CookieUser.NOBODY) {
			member = new Member();
			member.setUserName(user.getN());
			member.setRoleGroup(RoleGroup.ANOY.getValue());
		} else {
			member = memberService.queryMemberInfoForUser(user.getN());
			if (member == null) {
				member = new Member();
				member.setUserName(user.getN());
				// 只读权限
				member.setRoleGroup(RoleGroup.ANOY.getValue());
			}
		}
		request.setAttribute(BizConstants.MEMBER_ROLE_GROUP, member);

		// continue
		chain.doFilter(request, response);
	}

	@Override
	public void destroy() {

	}

	private synchronized void initMemberService(ServletRequest request) {
		if (memberService == null) {
			memberService = ApplicationContextProvier.getApplicationContext()
					.getBean(MemberService.class);
			System.out.println("memberService : " + memberService);
		}
	}

	protected String getUserName(HttpServletRequest request) {
		CookieUser user = (CookieUser) request
				.getAttribute(BizConstants.UUAP_UNAME_KEY);
		return user.getN();
	}

}
