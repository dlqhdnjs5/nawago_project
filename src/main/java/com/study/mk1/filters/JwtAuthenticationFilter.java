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
import org.springframework.web.filter.GenericFilterBean;
import org.springframework.web.filter.OncePerRequestFilter;

import com.study.mk1.common.JwtTokenProvider;
import com.study.mk1.sequrity.SecurityUserDetailService;
import com.study.mk1.sequrity.SecurityUserDetails;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class JwtAuthenticationFilter extends GenericFilterBean{

	private final JwtTokenProvider jwtTokenProvider;
	
	@Autowired
	SecurityUserDetailService securityUserDetailService;

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		// 헤더에서 JWT 를 받아옵니다.
		String token = jwtTokenProvider.resolveToken((HttpServletRequest) request);
		// 유효한 토큰인지 확인합니다.
		if (token != null && jwtTokenProvider.validateToken(token)) {
			// 토큰이 유효하면 토큰으로부터 유저 정보를 받아옵니다.
			Authentication authentication = jwtTokenProvider.getAuthentication(token);
			// SecurityContext 에 Authentication 객체를 저장합니다.
			SecurityContextHolder.getContext().setAuthentication(authentication);
			
			UserDetails userDetails = securityUserDetailService.loadUserByUsername(jwtTokenProvider.getUserPk(token));
			
			jwtTokenProvider.createToken(userDetails.getUsername(), userDetails.getAuthorities());
			
			
		}else {
			SecurityContextHolder.getContext().setAuthentication(null);
			
		}
		chain.doFilter(request, response);
	}
}
