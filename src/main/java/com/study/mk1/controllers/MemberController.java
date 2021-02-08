package com.study.mk1.controllers;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.SdkClientException;
import com.study.mk1.cmp.components.MbrComponent;
import com.study.mk1.common.IOService;
import com.study.mk1.common.JwtTokenProvider;
import com.study.mk1.common.S3service;
import com.study.mk1.common.XSSUtill;
import com.study.mk1.data.MbrInfoDTO;
import com.study.mk1.data.MbrInfoResult;
import com.study.mk1.data.PetInfoDTO;
import com.study.mk1.jpa.mbr.MbrJpa;
import com.study.mk1.jpa.mbr.MbrJpaCustomRepository;
import com.study.mk1.jpa.mbr.MbrJpaRepository;
import com.study.mk1.jpa.mbrPetMapping.MbrPetMappingJpa;
import com.study.mk1.jpa.pet.PetJpa;

@Controller
@RequestMapping("/api/member")
public class MemberController {
	
	@Autowired
	S3service s3service;
	
	@Autowired
	IOService ioService;
	
	@Autowired
	JwtTokenProvider jwtTokenProvider;
	
	@Autowired
	MbrJpaCustomRepository mbrJpaCustomRepository;
	
	@Autowired
	MbrJpaRepository mbrJpaRepository;
	
	@Autowired
	MbrComponent mbrComponent;
	
	@GetMapping(value="/checkMbrId")
	@ResponseBody
	public boolean checkMbrId(HttpServletRequest request,@RequestParam(value="mbrId") String mbrId) {
		
		return mbrComponent.checkMbrId(mbrId);
	}
	
	@PostMapping(value="/isExistMbrInfo")
	@ResponseBody
	public String isExistMbrInfo(HttpServletRequest request, @RequestBody MbrInfoDTO mbrInfoDto) throws Exception {
		
		return mbrComponent.isExistMbrInfo(mbrInfoDto);
	}
	
	@PostMapping(value="/isExistMbrInfoForUpdate")
	@ResponseBody
	public String isExistMbrInfoForUpdate(HttpServletRequest request, @RequestBody MbrInfoDTO mbrInfoDto) throws Exception {
		
		return mbrComponent.isExistMbrInfoForUpdate(mbrInfoDto);
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
		
		if(!ioService.checkImgFileExts(multipartFile.getOriginalFilename())) {
			return new ResponseEntity<Object>(HttpStatus.BAD_REQUEST);
		}
		
		String path = s3service.uploadWithRandomFileNm(multipartFile,"nawago/myPage/profile/");
		
		return  new ResponseEntity<Object>(path,HttpStatus.OK);
	}
	
	@PostMapping("/file/delete") 
	@ResponseBody public void delete(HttpServletRequest req,HttpServletResponse res,  @RequestBody PetInfoDTO dto) throws IOException { 
		
		try {
			String filePath = "nawago/myPage/profile/"+dto.getFileName().toString();
			s3service.delete(filePath);
		} catch (AmazonServiceException e) {
            e.printStackTrace();
        } catch (SdkClientException e) {
            e.printStackTrace();
        }
		
	}
	
	@PostMapping("/update/profilePhoto")
	public ResponseEntity<Object> updateProfilePhoto(HttpServletRequest req,HttpServletResponse res,@RequestBody MbrInfoDTO mbrInfoDTO) throws Exception {
			
		try {
			MbrJpa mbrJpa = new MbrJpa();
			mbrJpa.setMbrSeq(mbrInfoDTO.getMbrSeq());
			mbrJpa.setMbrRpstImgUrl(ioService.getFileUrl(mbrInfoDTO.getAttachFileUrl()));
			mbrJpa.setMbrRpstImgNm(ioService.getFileNm(mbrInfoDTO.getAttachFileUrl()));
			mbrComponent.updateProfilePhoto(mbrJpa);
			
			Map<String,String> map = new HashMap<>();
			map.put("mbrRpstImgUrl", mbrJpa.getMbrRpstImgUrl());
			map.put("mbrRpstImgNm", mbrJpa.getMbrRpstImgNm());
			return new ResponseEntity<>( map,  HttpStatus.OK);
			
		}catch(Exception e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
	}
	
	@PostMapping("/update")
	public ResponseEntity<Object> updateMember(HttpServletRequest req,HttpServletResponse res,@RequestBody MbrInfoDTO mbrInfoDTO) throws Exception {
			
		try {
			mbrInfoDTO.setMbrEmail(XSSUtill.stripXSS(mbrInfoDTO.getMbrEmail()));
			mbrInfoDTO.setMbrNickNm(XSSUtill.stripXSS(mbrInfoDTO.getMbrNickNm()));
			mbrInfoDTO.setMbrNm(XSSUtill.stripXSS(mbrInfoDTO.getMbrNm()));
			mbrComponent.updateMbr(mbrInfoDTO);
			return new ResponseEntity<>( HttpStatus.OK);
			
		}catch(Exception e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
	}
	
	@GetMapping("/getUserInfo")
	public ResponseEntity<MbrInfoResult> getUserInfo(HttpServletRequest req,HttpServletResponse res,@RequestParam(value="userId") String userId) throws Exception {
		
		try {
			
			
			
			MbrJpa mbrJpa = mbrJpaCustomRepository.findByMbrId(userId);
			if(mbrJpa == null) {
				return new ResponseEntity<>(null, HttpStatus.OK);
			}
			List<MbrPetMappingJpa> list = mbrJpa.getMbrPetMappingJpa();
			List<PetJpa> pet = new ArrayList<>();
			for(MbrPetMappingJpa obj  :  list) {
				pet.add(obj.getPetJpa());
			}
			MbrInfoResult reulstList = new MbrInfoResult();
			reulstList.setMbrJpa(mbrJpa);
			reulstList.setPetJpaList(pet);
			
			return new ResponseEntity<MbrInfoResult>(reulstList, HttpStatus.OK);
			
		}catch(Exception e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
	}
	
	
}
