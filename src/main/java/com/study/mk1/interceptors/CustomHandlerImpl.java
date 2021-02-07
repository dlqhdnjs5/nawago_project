package com.study.mk1.interceptors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.study.NawagoMk2Application;

public class CustomHandlerImpl implements HandlerInterceptor {

	private static Logger log = LoggerFactory.getLogger(CustomHandlerImpl.class);
	
	public boolean preHandle(HttpServletRequest request,HttpServletResponse response, Object handler) {
		
		log.info("CustomHandlerImpl.preHandle");
		
		return true;
	}
	
	 // 컨트롤러가 실행된 후 호출된다.
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView view)
            throws Exception {
    	
    	log.info("CustomHandlerImpl.postHandle");
 
    }
 
    // 모든작업이 다 완료된 후에 실행한다
    @Override
    public void afterCompletion(HttpServletRequest request,HttpServletResponse response, Object handler, Exception ex)
            throws Exception {
    	
    	log.info("CustomHandlerImpl.afterCompletion");
    	
 
    }





}
