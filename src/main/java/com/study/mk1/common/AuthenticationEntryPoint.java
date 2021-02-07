package com.study.mk1.common;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.AuthenticationException;

import org.springframework.stereotype.Component;

import com.study.mk1.controllers.DefaultController;



@Component
public class AuthenticationEntryPoint implements org.springframework.security.web.AuthenticationEntryPoint{

	private static Logger log = LoggerFactory.getLogger(AuthenticationEntryPoint.class);
	
	public void commence(HttpServletRequest req , HttpServletResponse res , AuthenticationException e) throws IOException,ServletException {
		
		
		log.info("Authentication [ERROR]");
		res.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Access Denied");
		
	}

}
