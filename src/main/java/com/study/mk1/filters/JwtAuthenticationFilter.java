package com.study.mk1.filters;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.GenericFilterBean;
import org.springframework.web.filter.OncePerRequestFilter;

import com.study.mk1.common.JwtTokenProvider;
import com.study.mk1.sequrity.SecurityUserDetailService;
import com.study.mk1.sequrity.SecurityUserDetails;

import lombok.RequiredArgsConstructor;


@RequiredArgsConstructor
public class JwtAuthenticationFilter extends GenericFilterBean{
	private final JwtTokenProvider jwtTokenProvider;
	private final SecurityUserDetailService securityUserDetailService;

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		String token = jwtTokenProvider.resolveToken((HttpServletRequest) request);
		if (!StringUtils.isEmpty(token) && jwtTokenProvider.validateToken(token)) {
			Authentication authentication = jwtTokenProvider.getAuthentication(token);
			SecurityContextHolder.getContext().setAuthentication(authentication);

			UserDetails userDetails = securityUserDetailService.loadUserByUsername(jwtTokenProvider.getUserPk(token));

			jwtTokenProvider.createToken(userDetails.getUsername(), userDetails.getAuthorities());
		} else {
			SecurityContextHolder.getContext().setAuthentication(null);
		}

		chain.doFilter(request, response);
	}
}
