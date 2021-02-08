package com.study.mk1.controllers;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.study.mk1.common.IOService;
import com.study.mk1.common.JwtTokenProvider;
import com.study.mk1.sequrity.SecurityUserDetailService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping("/api")
public class AuthenticationController {
	
	@Autowired
	SecurityUserDetailService securityUserDetailService;
	
	private final PasswordEncoder passwordEncoder;
	private final JwtTokenProvider jwtTokenProvider;
	
	@GetMapping(value = "/authenticated")
    public ResponseEntity<Void> authenticated() {
        if (IOService.hasRole()) {
            return ResponseEntity.ok().build();
        } 
        else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }
	
	@GetMapping(value= {"/validAuth"})
	public ResponseEntity<Object> validAuth(HttpServletRequest request , HttpServletResponse response) throws Exception {
		
		String token = jwtTokenProvider.resolveToken((HttpServletRequest) request);		
		if (token != null && jwtTokenProvider.validateToken(token))  {
			return new ResponseEntity<Object>(HttpStatus.OK);
		}else {
			return new ResponseEntity<Object>(HttpStatus.UNAUTHORIZED);
		}
		
	}
	
	@GetMapping(value= {"/validAuth2"})
	@ResponseBody
	public boolean validAuth2(HttpServletRequest request , HttpServletResponse response) throws Exception {
		
		try {
			String token = jwtTokenProvider.resolveToken((HttpServletRequest) request);		
			if (token != null && jwtTokenProvider.validateToken(token))  {
				return true;
			}else {
				return false;
			}
		}catch(Exception e) {
			throw new Exception(e);
		}
		
		
	}
	
	

}
