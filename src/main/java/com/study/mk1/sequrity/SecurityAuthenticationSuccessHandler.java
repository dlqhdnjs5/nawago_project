package com.study.mk1.sequrity;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.RequestCache;
import org.springframework.security.web.savedrequest.SavedRequest;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.study.mk1.data.SecurityResult;


@Component
public class SecurityAuthenticationSuccessHandler extends  SimpleUrlAuthenticationSuccessHandler{
	
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws ServletException, IOException {
    	
    	ObjectMapper mapper = new ObjectMapper();	
    	
    	SecurityResult securityResult = new SecurityResult();
		securityResult.setCode(HttpStatus.OK);
		securityResult.setStatus(response.SC_OK);
		securityResult.setMessage("로그인 성공");
		securityResult.setSuccess(true);
    	
    	if("".equals(getReturnUrl(request,response))) {
    		securityResult.setPrevPage("/");
    	}else {
    		securityResult.setPrevPage(getReturnUrl(request,response));
    	}

    	response.setCharacterEncoding("UTF-8");
    	response.setContentType("text/plain");
        response.setStatus(HttpServletResponse.SC_OK);
        response.getWriter().print(mapper.writeValueAsString(securityResult));
        response.getWriter().flush();
    }

   private String getReturnUrl(HttpServletRequest request, HttpServletResponse response) {
		RequestCache requestCache = new HttpSessionRequestCache();
		SavedRequest savedRequest = requestCache.getRequest(request, response);
		if (savedRequest == null) {
			return request.getSession().getServletContext().getContextPath();
		}
		return savedRequest.getRedirectUrl();
	}
}
