package com.study.mk1.controllers;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.study.mk1.cmp.components.ShowOffComponent;
import com.study.mk1.common.IOService;
import com.study.mk1.common.JwtTokenProvider;
import com.study.mk1.common.S3service;
import com.study.mk1.common.XSSUtill;
import com.study.mk1.data.ShowOffInfoDTO;
import com.study.mk1.data.ShowOffResult;
import com.study.mk1.enums.ShowOffEnum;
import com.study.mk1.jpa.mbr.MbrJpa;
import com.study.mk1.jpa.showOff.ShowOffJpaCustomRepository;
import com.study.mk1.jpa.showOff.ShowOffJpaRepository;
import com.study.mk1.jpa.showOffAttach.ShowOffAttachJpa;
import com.study.mk1.jpa.showOffLike.ShowOffLikeJpa;
import com.study.mk1.jpa.showOffReply.ShowOffReplyJpa;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Controller
@Slf4j
@RequestMapping("/api/showOff")
@RequiredArgsConstructor
public class ShowOffController {
	private final S3service s3service;
	private final ShowOffComponent showOffComponent;
	private final ShowOffJpaCustomRepository showOffJpaCustomRepository;
	private final IOService ioService;

	@GetMapping("/list")
	@ResponseBody
	public List<ShowOffResult> getShowOffList(HttpServletRequest req, Pageable pageable) {
		MbrJpa mbr  = ioService.getMbrInfoByRequest(req);
		long mbrSeq;
		if(mbr == null) {
			mbrSeq = 0;
		}else {
			mbrSeq = mbr.getMbrSeq();
		}
		
		return showOffJpaCustomRepository.findByShowOffPagingV2(mbrSeq, pageable);
	}
	
	@PostMapping("/file/upload")
	@ResponseBody public ResponseEntity<Object> upload( HttpServletRequest req,	@RequestParam("data") MultipartFile multipartFile) throws IOException {
		try {
			if (!ioService.hasRoleByRequest(req)) return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
		} catch (Exception e) {
			log.error(this.getClass()+".upload 401");
			return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
		}
		
		SimpleDateFormat format1 = new SimpleDateFormat ("yyyy-MM-dd");
		Date time = new Date();
		String time1 = format1.format(time);
		String path = s3service.uploadWithRandomFileNm(multipartFile,"nawago/showOff/"+time1+"/");
		
		return  new ResponseEntity<Object>(path,HttpStatus.OK);
		
	}
	
	@PostMapping("/add")
	@ResponseBody
	public ResponseEntity<String> addShowOff(@RequestBody ShowOffInfoDTO showOffInfoDTO ,HttpServletRequest req) {
		try {
			MbrJpa mbr  = ioService.getMbrInfoByRequest(req);
			if(mbr == null)return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
			
			showOffInfoDTO.getShowOffjpa().setMbrSeq(mbr.getMbrSeq());
			showOffInfoDTO.getShowOffjpa().setShowOffStatCd(ShowOffEnum.StatCd.ACT.toString());
			showOffInfoDTO.getShowOffjpa().setShowOffTpCd(ShowOffEnum.Tpcd.GNRL.toString());
			showOffInfoDTO.getShowOffjpa().setShowOffCont(XSSUtill.stripXSS(showOffInfoDTO.getShowOffjpa().getShowOffCont()));

			List<ShowOffAttachJpa> attachList = new ArrayList<>();
			
			for(String contentsUrl :  showOffInfoDTO.getFileAttachList()) {
				ShowOffAttachJpa obj = new ShowOffAttachJpa();
				obj.setShowOffAttachUrl(ioService.getFileUrl(contentsUrl));
				obj.setShowOffAttachNm(ioService.getFileNm(contentsUrl));
				obj.setShowOffAttachTpCd(ioService.getFileTpCd(contentsUrl));
				attachList.add(obj);
			}

			showOffInfoDTO.setShowOffAttachJpaList(attachList);
			
			showOffComponent.addShowOff(showOffInfoDTO);
		} catch (Exception exception) {
			log.error("error occurred while addShowOff. showOffInfoDTO : {}", showOffInfoDTO, exception);
			throw new RuntimeException(exception);
		}
		
		return  new ResponseEntity<String>(HttpStatus.OK);
	}
	
	@GetMapping("/getMyShowOffList")
	@ResponseBody
	public ResponseEntity<List<ShowOffResult>> getMyShowOffList(@RequestParam(value="params") String mbrSeq, Pageable pageable ) {
		try {
			List<ShowOffResult> list = showOffJpaCustomRepository.findByMbrSeqPagingV2(Long.parseLong(mbrSeq),pageable);
			return new ResponseEntity<List<ShowOffResult>>(list,HttpStatus.OK);
		} catch (Exception exception) {
			log.error("error occurred while getMyShowOffList. mbrSeq : {}", mbrSeq, exception);
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@GetMapping("/getShowOffReplyList")
	@ResponseBody
	public ResponseEntity<List<ShowOffReplyJpa>> getShowOffReplyList(@RequestParam(value="showOffSeq") String showOffSeq ) {
		try {
			List<ShowOffReplyJpa> list = showOffJpaCustomRepository.showOffReplyfindByMbrSeq(Long.parseLong(showOffSeq));
			return new ResponseEntity<List<ShowOffReplyJpa>>(list,HttpStatus.OK);
		} catch (Exception exception) {
			log.error("error occurred while addShowOff. showOffSeq : {}", showOffSeq, exception);
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@PostMapping("/addShowOffReply")
	@ResponseBody
	public ResponseEntity<Long> addShowOffReply(HttpServletRequest req, @RequestBody ShowOffInfoDTO showOffInfoDTO) {
		MbrJpa mbrJpa;
		long showOffReplyCnt;
		try {
			mbrJpa = ioService.getMbrInfoByRequest(req);
			if(mbrJpa == null)return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);

			showOffInfoDTO.getShowOffReplyJpa().setReplyCont(XSSUtill.stripXSS(showOffInfoDTO.getShowOffReplyJpa().getReplyCont()));
			if("".equals(showOffInfoDTO.getShowOffReplyJpa().getReplyCont()) || StringUtils.isEmpty(showOffInfoDTO.getShowOffReplyJpa().getReplyCont()) ) {
				throw new Exception();
			}
			showOffInfoDTO.getShowOffReplyJpa().setMbrSeq(mbrJpa.getMbrSeq());
			showOffInfoDTO.getShowOffReplyJpa().setRegterId(mbrJpa.getMbrId());
			
		
			showOffReplyCnt = showOffComponent.addShowOffReply(showOffInfoDTO);
		} catch (Exception exception) {
			log.error("error occurred while addShowOffReply. showOffInfoDTO : {}", showOffInfoDTO, exception);
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		return new ResponseEntity<>(showOffReplyCnt,HttpStatus.OK);
	}
	
	@PostMapping("/updateShowOffLike")
	@ResponseBody
	public ResponseEntity<Long> updateShowOffLike(HttpServletRequest req, @RequestBody ShowOffLikeJpa showOffLikeJpa) {
		MbrJpa mbrJpa;
		long likeCnt = 0;
		try {
			mbrJpa = ioService.getMbrInfoByRequest(req);
			if(mbrJpa == null)return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);

			showOffLikeJpa.setMbrSeq(mbrJpa.getMbrSeq());
			likeCnt = showOffComponent.updateShowOffLike(showOffLikeJpa);
			
		} catch (Exception exception) {
			log.error("error occurred while addShowOffReply. showOffInfoDTO : {}", showOffLikeJpa, exception);
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		return new ResponseEntity<Long>(likeCnt,HttpStatus.OK);
	}
	
	@PostMapping("/deleteShowOff")
	@ResponseBody
	public ResponseEntity<Object> deleteShowOff(@RequestBody ShowOffInfoDTO showOffInfoDTO) {
		try {
			showOffComponent.deleteShowOff(showOffInfoDTO);
		} catch (Exception exception) {
			log.error("error occurred while addShowOffReply. showOffInfoDTO : {}", showOffInfoDTO, exception);
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		return new ResponseEntity<>(HttpStatus.OK);
	}
	
}
