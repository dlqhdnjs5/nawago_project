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
import com.study.mk1.jpa.showOffLike.ShowOffLikeJpa;
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
	
	/**
	 * 스토리 목록 조회
	 */
	@GetMapping("/list")
	@ResponseBody
	public List<ShowOffResult> getShowOffList(HttpServletRequest req,HttpServletResponse res,Pageable pageable) throws Exception {
		
		long mbrSeq;
		MbrJpa mbrJpa = new MbrJpa();
		String token = req.getHeader("x-auth");
		if(!"".equals(token) && !"null".equals(token) ) {
			mbrJpa = jwtTokenProvider.getUserInfo(token);
			mbrSeq = mbrJpa.getMbrSeq();
		}else {
			mbrSeq = 0; //0 인 mbrSeq 는 없음.
		}
		
		List<ShowOffResult> list = showOffJpaCustomRepository.findByShowOffPagingV2(mbrSeq,pageable);
		return list;
	}
	
	
	/**
	 * 스토리 미디어 파일 s3 저장
	 */
	@PostMapping("/file/upload") 
	@ResponseBody public ResponseEntity<Object> upload( HttpServletRequest req,HttpServletResponse res,
			@RequestParam("data") MultipartFile multipartFile) throws IOException { 
		
		String  token = req.getHeader("x-auth");
		if(!"".equals(token) && !"null".equals(token) ) {
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
	
	/**
	 * 스토리 저장
	 */
	@PostMapping("/add")
	@ResponseBody
	public ResponseEntity<String> addShowOff( @RequestBody ShowOffInfoDTO dto,HttpServletRequest req,HttpServletResponse res) {
		
		
		try {
			
			String  token = req.getHeader("x-auth");
			if(!"".equals(token) && !"null".equals(token) ) {
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
	
	/**
	 * 마이페이지 스토리 목록 조회
	 */
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
	
	/**
	 * 스토리 댓글 저장
	 */
	@PostMapping("/addShowOffReply")
	@ResponseBody
	public ResponseEntity<Long> addShowOffReply(HttpServletRequest req,HttpServletResponse res, @RequestBody ShowOffInfoDTO dto) {
		
		String token = req.getHeader("x-auth");
		MbrJpa mbrJpa = new MbrJpa();
		long showOffReplyCnt;
		try {
			
			if(!"".equals(token) && !"null".equals(token) ) {
				boolean tokenValidYn = jwtTokenProvider.validateToken(token);
				mbrJpa = jwtTokenProvider.getUserInfo(token);
				if(!tokenValidYn)return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
			}else {
				return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
			}
			
			dto.getShowOffReplyJpa().setReplyCont(XSSUtill.stripXSS(dto.getShowOffReplyJpa().getReplyCont()));
			if("".equals(dto.getShowOffReplyJpa().getReplyCont()) || StringUtils.isEmpty(dto.getShowOffReplyJpa().getReplyCont()) ) {
				throw new Exception();
			}
			dto.getShowOffReplyJpa().setMbrSeq(mbrJpa.getMbrSeq());
			dto.getShowOffReplyJpa().setRegterId(mbrJpa.getMbrId());
			
		
			showOffReplyCnt = showOffComponent.addShowOffReply(dto);
			
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
			// TODO Auto-generated catch block
		}
		
		return new ResponseEntity<Long>(showOffReplyCnt,HttpStatus.OK);
	}
	
	
	/**
	 * 좋아요 버튼 update
	 * @param req
	 * @param res
	 * @param dto
	 * @return
	 */
	@PostMapping("/updateShowOffLike")
	@ResponseBody
	public ResponseEntity<Long> updateShowOffLike(HttpServletRequest req,HttpServletResponse res, @RequestBody ShowOffLikeJpa dto) {
		
		String token = req.getHeader("x-auth");
		MbrJpa mbrJpa = new MbrJpa();
		long likeCnt;
		try {
			
			if(!"".equals(token) && !"null".equals(token) ) {
				boolean tokenValidYn = jwtTokenProvider.validateToken(token);
				mbrJpa = jwtTokenProvider.getUserInfo(token);
				if(!tokenValidYn)return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
			}else {
				return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
			}
			
			dto.setMbrSeq(mbrJpa.getMbrSeq());
			likeCnt = showOffComponent.updateShowOffLike(dto);
			
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
			// TODO Auto-generated catch block
		}
		
		return new ResponseEntity<Long>(likeCnt,HttpStatus.OK);
	}
	
}
