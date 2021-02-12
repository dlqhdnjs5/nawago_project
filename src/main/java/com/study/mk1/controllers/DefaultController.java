package com.study.mk1.controllers;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.security.core.GrantedAuthority;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.study.mk1.cmp.repositorys.MbrRepository;
import com.study.mk1.common.IOService;
import com.study.mk1.common.JwtTokenProvider;
import com.study.mk1.common.S3service;
import com.study.mk1.common.S3uploader;
import com.study.mk1.config.GlobalPropertySource;
import com.study.mk1.data.MbrInfoDTO;
import com.study.mk1.entity.Mbr;
import com.study.mk1.enums.MbrEnum;
import com.study.mk1.interceptors.CustomHandlerImpl;
import com.study.mk1.jpa.mbr.MbrJpa;
import com.study.mk1.jpa.mbr.MbrJpaRepository;
import com.study.mk1.jpa.mbrPetMapping.MbrPetMappingJpa;
import com.study.mk1.jpa.pet.PetJpa;
import com.study.mk1.sequrity.SecurityUserDetailService;
import com.study.mk1.sequrity.SecurityUserDetails;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping("/api")
public class DefaultController {
	
	private static Logger log = LoggerFactory.getLogger(DefaultController.class);
	
	@Autowired
	MbrRepository mbrRepository;
	
	@Autowired
	MbrJpaRepository mbrJpaRepository;
	
	@Autowired
	S3service s3service;
	
	@Autowired
	GlobalPropertySource gp;
	
	@Autowired
    private JavaMailSenderImpl mailSender;

	
	@Autowired
	SecurityUserDetailService securityUserDetailService;
	
	private final PasswordEncoder passwordEncoder;
	private final JwtTokenProvider jwtTokenProvider;
	
	
	@RequestMapping(value= {"/home","/"})
	public String home(HttpServletRequest rquest , HttpServletResponse response)  {
		
		log.info("home");
		//TODO : 로그인 만든후 회원가입 만들기
		return "index";
		
	}
	
	/*@RequestMapping(value= {"/login"})
	public String login(HttpServletRequest rquest , HttpServletResponse response ,
			@RequestParam(value= "error",required=false) String error)  {
		
		if(error != null) {
			return "display/loginFail";
		}else {
			return "display/login";
			
		}
		
	}*/
	
	// 로그인
    @PostMapping("/loginProcesse")
    @ResponseBody
    public ResponseEntity<String> login(@RequestBody Map<String, String> user) {
    	log.info("_____________________________________[LOGIN]_____________________________________________");
    	MbrJpa mbrJpa = mbrJpaRepository.findByMbrId(user.get("email"));
    	
    	if(mbrJpa == null) {
    		return new ResponseEntity<String>("회원정보가 존재하지 않습니다.",HttpStatus.UNAUTHORIZED);
    	}else {
    		SecurityUserDetails userDetail = new SecurityUserDetails(mbrJpa, mbrJpa.getMam().getAuthJpa());
        	if(userDetail == null) {
        		return new ResponseEntity<String>("회원정보가 존재하지 않습니다.",HttpStatus.UNAUTHORIZED);
        	}
        	if (!passwordEncoder.matches(user.get("password"), userDetail.getPassword())) {
        		return new ResponseEntity<String>("올바른 비밀번호를 입력해 주세요.",HttpStatus.UNAUTHORIZED);
        	}
        	if(mbrJpa.getMbrStatCd().equals(MbrEnum.StatCd.FSCSI.toString())) {
        		return new ResponseEntity<String>("너는시발럼아 부적절한 행위로 강제탈퇴 되었습니다.알겠니? 꺼져",HttpStatus.UNAUTHORIZED);
        	}
        	return new ResponseEntity<String>(jwtTokenProvider.createToken(userDetail.getUsername(), userDetail.getAuthorities()), HttpStatus.OK);
    	}
    }
    
    
    @GetMapping("/getMbrInfo")
    @ResponseBody
    public ResponseEntity<Object> getMbrInfo(HttpServletRequest req, HttpServletResponse rs) {
    	log.info("_____________________________________[getUser]_____________________________________________");
    	String  token = req.getHeader("x-auth");
    	/*hasRole()*/
    	if(!"".equals(token)) {
    		boolean tokenValidYn = jwtTokenProvider.validateToken(token);
    		if(!tokenValidYn)return new ResponseEntity<Object>(HttpStatus.UNAUTHORIZED);
    		MbrJpa mbr = jwtTokenProvider.getUserInfo(token);
    		Map mbrMap = new HashMap<String,String>();
    		mbrMap.put("mbrId", mbr.getMbrId());
    		mbrMap.put("mbrEmail", mbr.getMbrEmail());
    		mbrMap.put("mbrGrdCd", mbr.getMbrGrdCd());
    		mbrMap.put("mbrMobileNo", mbr.getMbrMobAreaNo() +"-"+ mbr.getMbrMobTlofLstNo() +"-"+ mbr.getMbrMobTlofNo());
    		mbrMap.put("mbrNm", mbr.getMbrNm());
    		mbrMap.put("mbrNickNm", mbr.getMbrNickNm());
    		mbrMap.put("mbrTpCd", mbr.getMbrTpCd());
    		mbrMap.put("mbrRpstImgUrl", mbr.getMbrRpstImgUrl());
    		mbrMap.put("mbrRpstImgNm", mbr.getMbrRpstImgNm());
    		mbrMap.put("mbrSeq", mbr.getMbrSeq());
    		List<MbrPetMappingJpa> list = mbr.getMbrPetMappingJpa();
    		List<PetJpa> pet = new ArrayList<>();
    		for(MbrPetMappingJpa obj  :  list) {
    			pet.add(obj.getPetJpa());
    		}
    		mbrMap.put("mbrPet", pet);
    		
    		return new ResponseEntity<Object>(mbrMap,HttpStatus.OK);
    	}else {
    		return new ResponseEntity<Object>(HttpStatus.UNAUTHORIZED);
    	}
    }
	
	@RequestMapping(value= {"/logout"})
	public String logout(HttpServletRequest request , HttpServletResponse response) throws Exception {
		
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();

		if(auth != null){
			new SecurityContextLogoutHandler().logout(request, response, auth);
		}
		
		return "redirect:/login";
		
	}
	
	@RequestMapping(value= {"/signUpPage"})
	public String joinPage(HttpServletRequest request , HttpServletResponse response) throws Exception {
		
		return "display/signUpPage";
		
	}
	
	@RequestMapping(value="/join")
	public ResponseEntity<Void> joinMbr(HttpServletRequest request , HttpServletResponse response, @RequestBody MbrInfoDTO mbrInfoDto) throws Exception {
		
		try {
			
			if(mbrInfoDto != null && mbrInfoDto.getMbrJpa() != null) {
				mbrInfoDto.getMbrJpa().setMbrPw(mbrInfoDto.getMbrPw());//@JsonIgnore 으로인한 dto 로 대체
				securityUserDetailService.joinMbr(mbrInfoDto);
				
			}else {
				NullPointerException e = new NullPointerException();
				throw e;
			}
			
		}catch(NullPointerException e) {
			log.warn("NullPointerException :  {}",mbrInfoDto);
		}catch(Exception e) {
			throw new Exception();
		}
		
		return ResponseEntity.status(HttpStatus.OK).build();
	}
	
	@PostMapping("/sendAuthEmail")
	@ResponseBody 
	public ResponseEntity<String> sendAuthEmail(HttpServletRequest request , HttpServletResponse response , @RequestBody MbrInfoDTO mbrInfoDto)throws Exception {
		
		
		log.info(this.getClass()+".sendAuthEmail [INFO] dto -> {}",mbrInfoDto);
		String key=  "";
		try {
			key= this.getRandomStr(8);  //인증번호 
			String nawagoEmail = gp.getMailId();
			MimeMessage mail = mailSender.createMimeMessage();
			
			String sendMsg = new StringBuilder().append("<h2>Nawago<h2><br>")
					.append("<br>")
					.append("인증번호를 입력한뒤 회원가입을 마무리 해주세요!")
					.append("<br>")
					.append("인증 번호 : "+key)
					.toString();
			
			mail.setSubject("[Nawago] 인증 메일");
			mail.setText(sendMsg,"utf-8","html");
			mail.addRecipient(Message.RecipientType.TO,  new InternetAddress(mbrInfoDto.getMbrEmail()));
			mailSender.send(mail);
			
		} catch (MessagingException e) {
	            e.printStackTrace();
	            log.error(e.toString());
	            log.info(this.getClass()+".sendAuthEmail [ERROR] dto -> {}",mbrInfoDto);
	            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}catch(Exception e) {
	    	e.printStackTrace();
            log.error(e.toString());
            log.info(this.getClass()+".sendAuthEmail [ERROR] dto -> {}",mbrInfoDto);
            throw new RuntimeException(e);
	    }
		
		
		return new ResponseEntity<String>(key,HttpStatus.OK);
	}
	
	
	private  String getRandomStr(int size) {
    	
		char[] tmp = new char[size];
		for(int i=0; i<tmp.length; i++) {
			int div = (int) Math.floor( Math.random() * 2 );
			
			if(div == 0) { // 0이면 숫자로
				tmp[i] = (char) (Math.random() * 10 + '0') ;
			}else { //1이면 알파벳
				tmp[i] = (char) (Math.random() * 26 + 'A') ;
			}
		}
		return new String(tmp);
	}

}
