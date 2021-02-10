package com.study.mk1.controllers;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sound.midi.MidiDevice.Info;
import com.study.mk1.cmp.components.ShowOffComponent;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.study.mk1.common.IOService;
import com.study.mk1.common.JwtTokenProvider;
import com.study.mk1.common.S3service;
import com.study.mk1.common.XSSUtill;
import com.study.mk1.data.ShowOffInfoDTO;
import com.study.mk1.data.ShowOffResult;
import com.study.mk1.data.ShowOffResultInf;
import com.study.mk1.enums.ShowOffAttachEnum;
import com.study.mk1.enums.ShowOffEnum;
import com.study.mk1.jpa.mbr.MbrJpa;
import com.study.mk1.jpa.showOff.ShowOffJpa;
import com.study.mk1.jpa.showOff.ShowOffJpaCustomRepository;
import com.study.mk1.jpa.showOff.ShowOffJpaRepository;
import com.study.mk1.jpa.showOffAttach.ShowOffAttachJpa;
import com.study.mk1.jpa.showOffReply.ShowOffReplyJpa;

import lombok.extern.slf4j.Slf4j;

@Controller
@Slf4j
@RequestMapping("/api/showOff")
public class ShowOffController {

	@Autowired
	S3service s3service;
	
	@Autowired
	JwtTokenProvider jwtTokenProvider;
	
	@Autowired
	ShowOffComponent showOffComponent;
	
	@Autowired
	ShowOffJpaRepository showOffJpaRepository;
	
	@Autowired
	ShowOffJpaCustomRepository showOffJpaCustomRepository;
	
	@Autowired
	IOService ioService;
	
	@GetMapping("/list")
	@ResponseBody
	public List<ShowOffResult> getShowOffList(HttpServletRequest req,HttpServletResponse res,Pageable pageable) throws Exception {
		
		List<ShowOffResult> list = showOffJpaCustomRepository.findByShowOffPagingV2(pageable);
		return list;
	}
	
	
	@PostMapping("/file/upload") 
	@ResponseBody public ResponseEntity<Object> upload( HttpServletRequest req,HttpServletResponse res,
			@RequestParam("data") MultipartFile multipartFile) throws IOException { 
		
		String  token = req.getHeader("x-auth");
		if(!"".equals(token)) {
			boolean tokenValidYn = jwtTokenProvider.validateToken(token);
			if(!tokenValidYn)return new ResponseEntity<Object>(HttpStatus.UNAUTHORIZED);
		}else {
			return new ResponseEntity<Object>(HttpStatus.UNAUTHORIZED);
		}
		
		SimpleDateFormat format1 = new SimpleDateFormat ("yyyy-MM-dd");
		Date time = new Date();
		String time1 = format1.format(time);
		
		String path = s3service.uploadWithRandomFileNm(multipartFile,"nawago/showOff/"+time1+"/");
		
		return  new ResponseEntity<Object>(path,HttpStatus.OK);
	}
	
	@PostMapping("/add")
	@ResponseBody
	public ResponseEntity<String> addShowOff( @RequestBody ShowOffInfoDTO dto,HttpServletRequest req,HttpServletResponse res) {
		
		
		try {
			
			String  token = req.getHeader("x-auth");
			if(!"".equals(token)) {
				boolean tokenValidYn = jwtTokenProvider.validateToken(token);
				if(!tokenValidYn)return new ResponseEntity<String>("401",HttpStatus.UNAUTHORIZED);
			}else {
				return new ResponseEntity<String>("401",HttpStatus.UNAUTHORIZED);
			}
			MbrJpa mbr = jwtTokenProvider.getUserInfo(token);
			
			
			//showOff setting
			dto.getShowOffjpa().setMbrSeq(mbr.getMbrSeq());
			dto.getShowOffjpa().setShowOffStatCd(ShowOffEnum.StatCd.ACT.toString());
			dto.getShowOffjpa().setShowOffTpCd(ShowOffEnum.Tpcd.GNRL.toString());
			dto.getShowOffjpa().setShowOffCont(XSSUtill.stripXSS(dto.getShowOffjpa().getShowOffCont()));
			//showOffAttach setting
			List<ShowOffAttachJpa> attachList = new ArrayList<>();
			
			for(String contentsUrl :  dto.getFileAttachList()) {
				ShowOffAttachJpa obj = new ShowOffAttachJpa();
				obj.setShowOffAttachUrl(ioService.getFileUrl(contentsUrl));
				obj.setShowOffAttachNm(ioService.getFileNm(contentsUrl));
				obj.setShowOffAttachTpCd(ioService.getFileTpCd(contentsUrl));
				attachList.add(obj);
			}
			dto.setShowOffAttachJpaList(attachList);
			
			showOffComponent.addShowOff(dto);
			
			
		}catch(Exception e) {
			throw new RuntimeException();
		}
		
		return  new ResponseEntity<String>(HttpStatus.OK);
	}
	
	@GetMapping("/getMyShowOffList")
	@ResponseBody
	public ResponseEntity<List<ShowOffResult>> getMyShowOffList( HttpServletRequest req,HttpServletResponse res,
			 @RequestParam(value="params") String mbrSeq, Pageable pageable ) {
		
		try {
			List<ShowOffResult> list = showOffJpaCustomRepository.findByMbrSeqPagingV2(Long.parseLong(mbrSeq),pageable);
			return new ResponseEntity<List<ShowOffResult>>(list,HttpStatus.OK);
		}catch(Exception e) {
			log.info(this.getClass()+".getMyShowOffList() [ERROR] --> mbrSeq : {}",mbrSeq); 
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		
	}
	
	@GetMapping("/getShowOffReplyList")
	@ResponseBody
	public ResponseEntity<List<ShowOffReplyJpa>> getShowOffReplyList( HttpServletRequest req,HttpServletResponse res,
			 @RequestParam(value="showOffSeq") String showOffSeq ) {
		try {
			
			List<ShowOffReplyJpa> list = showOffJpaCustomRepository.showOffReplyfindByMbrSeq(Long.parseLong(showOffSeq));
			return new ResponseEntity<List<ShowOffReplyJpa>>(list,HttpStatus.OK);
		
		
		}catch(Exception e) {
			log.info(this.getClass()+".getMyShowOffList() [ERROR] --> mbrSeq : {}",showOffSeq); 
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		
	}
	
	@PostMapping("/addShowOffReply")
	@ResponseBody
	public ResponseEntity<Object> addShowOffReply(HttpServletRequest req,HttpServletResponse res, @RequestBody ShowOffInfoDTO dto) {
		
		String token = req.getHeader("x-auth");
		MbrJpa mbrJpa = new MbrJpa();
		
		try {
			
			if(!"".equals(token)) {
				boolean tokenValidYn = jwtTokenProvider.validateToken(token);
				mbrJpa = jwtTokenProvider.getUserInfo(token);
				if(!tokenValidYn)return new ResponseEntity<Object>(HttpStatus.UNAUTHORIZED);
			}else {
				return new ResponseEntity<Object>(HttpStatus.UNAUTHORIZED);
			}
			
			
//			dto.getShowOffReplyjpa().(XSSUtill.stripXSS(dto.getShowOffjpa().getShowOffCont()));
			
			dto.getShowOffReplyJpa().setReplyCont(XSSUtill.stripXSS(dto.getShowOffReplyJpa().getReplyCont()));
			if("".equals(dto.getShowOffReplyJpa().getReplyCont()) || StringUtils.isEmpty(dto.getShowOffReplyJpa().getReplyCont()) ) {
				throw new Exception();
			}
			dto.getShowOffReplyJpa().setMbrSeq(mbrJpa.getMbrSeq());
			dto.getShowOffReplyJpa().setRegterId(mbrJpa.getMbrId());
			
		
			showOffComponent.addShowOffReply(dto);
			
		} catch (Exception e) {
			return new ResponseEntity<Object>(HttpStatus.INTERNAL_SERVER_ERROR);
			// TODO Auto-generated catch block
		}
		
		return new ResponseEntity<>(HttpStatus.OK);
	}
	
}
