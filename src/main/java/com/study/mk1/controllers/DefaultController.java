package com.study.mk1.controllers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.study.mk1.cmp.repositorys.MbrRepository;
import com.study.mk1.common.IOService;
import com.study.mk1.common.JwtTokenProvider;
import com.study.mk1.common.S3service;
import com.study.mk1.config.GlobalPropertySource;
import com.study.mk1.data.MbrInfoDTO;
import com.study.mk1.enums.MbrEnum;
import com.study.mk1.jpa.mbr.MbrJpa;
import com.study.mk1.jpa.mbr.MbrJpaRepository;
import com.study.mk1.jpa.mbrPetMapping.MbrPetMappingJpa;
import com.study.mk1.jpa.pet.PetJpa;
import com.study.mk1.sequrity.SecurityUserDetailService;
import com.study.mk1.sequrity.SecurityUserDetails;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Controller
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api")
public class DefaultController {
	private final MbrJpaRepository mbrJpaRepository;
	private final JavaMailSenderImpl mailSender;
	private final IOService ioService;
	private final SecurityUserDetailService securityUserDetailService;
	private final PasswordEncoder passwordEncoder;
	private final JwtTokenProvider jwtTokenProvider;

	/**
	 * 로그인
	 * @param user
	 * @return
	 */
    @PostMapping("/loginProcesse")
    @ResponseBody
    public ResponseEntity<String> login(@RequestBody Map<String, String> user) {
    	MbrJpa mbrJpa = mbrJpaRepository.findByMbrId(user.get("email"));
    	
    	if (ObjectUtils.isEmpty(mbrJpa)) {
    		return new ResponseEntity<String>("회원정보가 존재하지 않습니다.",HttpStatus.UNAUTHORIZED);
    	}

		SecurityUserDetails userDetail = new SecurityUserDetails(mbrJpa, mbrJpa.getMam().getAuthJpa());
		if (!passwordEncoder.matches(user.get("password"), userDetail.getPassword())) {
			return new ResponseEntity<String>("올바른 비밀번호를 입력해 주세요.",HttpStatus.UNAUTHORIZED);
		}

		if(mbrJpa.getMbrStatCd().equals(MbrEnum.StatCd.SCSI.toString())) {
			return new ResponseEntity<String>("비활성화된 계정 입니다.",HttpStatus.UNAUTHORIZED);
		}

		if(mbrJpa.getMbrStatCd().equals(MbrEnum.StatCd.FSCSI.toString())) {
			return new ResponseEntity<String>("부적절한 행위로 강제탈퇴 되었습니다.",HttpStatus.UNAUTHORIZED);
		}

		return new ResponseEntity<String>(jwtTokenProvider.createToken(userDetail.getUsername(), userDetail.getAuthorities()), HttpStatus.OK);

    }
    
    /**
     * 유저 기본 정보 조회
     * @param req
     * @param rs
     * @return
     */
    @GetMapping("/getMbrInfo")
    @ResponseBody
    public ResponseEntity<Object> getMbrInfo(HttpServletRequest req, HttpServletResponse rs) {
    	try {
			if(ioService.hasRoleByRequest(req)) {
				MbrJpa mbr =  ioService.getMbrInfoByRequest(req);
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
				for (MbrPetMappingJpa obj  :  list) {
					pet.add(obj.getPetJpa());
				}
				mbrMap.put("mbrPet", pet);
				
				return new ResponseEntity<Object>(mbrMap,HttpStatus.OK);
			} else {
				return new ResponseEntity<Object>(HttpStatus.UNAUTHORIZED);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			log.error(this.getClass()+".getMbrInfo");
			return new ResponseEntity<Object>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
    }
	
	@RequestMapping(value="/join")
	public ResponseEntity<Void> joinMbr(@RequestBody MbrInfoDTO mbrInfoDto) throws Exception {
		try {
			if(!ObjectUtils.isEmpty(mbrInfoDto) && !ObjectUtils.isEmpty(mbrInfoDto.getMbrJpa())) {
				mbrInfoDto.getMbrJpa().setMbrPw(mbrInfoDto.getMbrPw());
				securityUserDetailService.joinMbr(mbrInfoDto);
			}else {
				throw new NullPointerException();
			}
		} catch(Exception e) {
			throw new Exception("Error occured while join member ", e);
		}
		
		return ResponseEntity.status(HttpStatus.OK).build();
	}
	
	@PostMapping("/sendAuthEmail")
	@ResponseBody 
	public ResponseEntity<String> sendAuthEmail(@RequestBody MbrInfoDTO mbrInfoDto)throws Exception {
		String key=  "";
		try {
			key= this.getRandomStr(8);
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
			
			if(div == 0) {
				tmp[i] = (char) (Math.random() * 10 + '0') ;
			}else {
				tmp[i] = (char) (Math.random() * 26 + 'A') ;
			}
		}
		return new String(tmp);
	}

}
