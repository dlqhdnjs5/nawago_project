package com.study.mk1.sequrity;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;

import com.study.mk1.controllers.DefaultController;

public class LogoutSuccessHandler implements org.springframework.security.web.authentication.logout.LogoutSuccessHandler{

	private static Logger log = LoggerFactory.getLogger(LogoutSuccessHandler.class);
	@Override
	public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication)
			throws IOException, ServletException {

		log.info("_____________________________________[LOGOUT]_____________________________________________");

		response.setStatus(HttpServletResponse.SC_OK);
		response.setHeader("x-auth","");
		response.sendRedirect("/");
	}
	
}
