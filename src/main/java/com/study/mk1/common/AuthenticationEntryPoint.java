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

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class AuthenticationEntryPoint implements org.springframework.security.web.AuthenticationEntryPoint{

	public void commence(HttpServletRequest req , HttpServletResponse res , AuthenticationException e) throws IOException,ServletException {
		log.error("Authentication Denied ", e);
		res.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Access Denied");
	}

}
