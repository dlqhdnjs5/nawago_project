package com.study.mk1.controllers;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
public class CustomErrorController /*implements ErrorController */{
	
	private final String VIEW_PATH = "/error";

	@GetMapping(value = VIEW_PATH) 
	public String handleError(HttpServletRequest request,HttpServletResponse response) {
		return "index";
	}

	/*@Override
	public String getErrorPath() {
		// TODO Auto-generated method stub
		return VIEW_PATH;
	}*/

}
