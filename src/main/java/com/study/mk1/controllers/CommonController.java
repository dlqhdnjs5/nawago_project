package com.study.mk1.controllers;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.study.mk1.jpa.sysCd.SysCdJpa;
import com.study.mk1.jpa.sysCd.SysCdJpaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping("/api/common")
public class CommonController {
	private final SysCdJpaRepository sysCdJpaRepository;
	
	@GetMapping("/getCommonCd")
	@ResponseBody
	public List<SysCdJpa> getCommonCd(@RequestParam(value="upperCd") String upperCd) throws Exception{
		return sysCdJpaRepository.findByUpperCd(upperCd);
	}
}
