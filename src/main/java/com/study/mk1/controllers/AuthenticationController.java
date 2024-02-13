package com.study.mk1.controllers;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.study.mk1.common.IOService;
import com.study.mk1.common.JwtTokenProvider;
import com.study.mk1.sequrity.SecurityUserDetailService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/api")
public class AuthenticationController {
	private final JwtTokenProvider jwtTokenProvider;
	
	@GetMapping(value = "/authenticated")
    public ResponseEntity<Void> authenticated() {
		try {
			if (IOService.hasRole()) {
				return ResponseEntity.ok().build();
			}
		} catch (RuntimeException runtimeException) {
			log.error("error occurred while authenticate");
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
		}

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }
	
	@GetMapping(value= {"/validAuth"})
	public ResponseEntity<Object> validAuth(HttpServletRequest request) throws Exception {
		
		String token = jwtTokenProvider.resolveToken(request);
		if (!StringUtils.isEmpty(token) && jwtTokenProvider.validateToken(token))  {
			return new ResponseEntity<Object>(HttpStatus.OK);
		}

		return new ResponseEntity<Object>(HttpStatus.UNAUTHORIZED);
	}
}
