package com.study.mk1.controllers;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;


@Controller
public class CustomErrorController /*implements ErrorController */{
	
	private final String VIEW_PATH = "/error";

	@GetMapping(value = VIEW_PATH) 
	public String handleError(HttpServletRequest request,HttpServletResponse response) {
		return "index";
	}
}
