package com.study.mk1.controllers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

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
import com.study.mk1.jpa.mbrPetMapping.MbrPetMappingJpa;
import com.study.mk1.jpa.pet.PetJpa;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/api/member")
public class MemberController {
	private static final String NAWAGO_PROFILE_PATH = "nawago/myPage/profile/";

	private final S3service s3service;
	private final IOService ioService;
	private final JwtTokenProvider jwtTokenProvider;
	private final MbrJpaCustomRepository mbrJpaCustomRepository;
	private final MbrComponent mbrComponent;
	
	@GetMapping(value="/checkMbrId")
	@ResponseBody
	public boolean checkMbrId(@RequestParam(value="mbrId") String mbrId) {
		return mbrComponent.checkMbrId(mbrId);
	}
	
	@PostMapping(value="/isExistMbrInfo")
	@ResponseBody
	public String isExistMbrInfo(@RequestBody MbrInfoDTO mbrInfoDto) {
		try {
			return mbrComponent.isExistMbrInfoForUpdate(mbrInfoDto);
		} catch (Exception exception) {
			log.error("error occurred while get isExistMbrInfo. MbrInfoDTO: {}", mbrInfoDto, exception);
			throw new RuntimeException(exception);
		}
	}
	
	@PostMapping(value="/isExistMbrInfoForUpdate")
	@ResponseBody
	public String isExistMbrInfoForUpdate(@RequestBody MbrInfoDTO mbrInfoDto) {
		try {
			return mbrComponent.isExistMbrInfoForUpdate(mbrInfoDto);
		} catch (Exception exception) {
			log.error("error occurred while get isExistMbrInfoForUpdate. MbrInfoDTO: {}", mbrInfoDto, exception);
			throw new RuntimeException(exception);
		}
	}
	
	@PostMapping("/file/upload") 
	@ResponseBody public ResponseEntity<Object> upload(HttpServletRequest req,	@RequestParam("data") MultipartFile multipartFile) throws IOException {
		String token = req.getHeader("x-auth");
		if (StringUtils.isEmpty(token)) {
			return new ResponseEntity<Object>(HttpStatus.UNAUTHORIZED);
		}

		boolean tokenValidYn = jwtTokenProvider.validateToken(token);
		if (!tokenValidYn) return new ResponseEntity<Object>(HttpStatus.UNAUTHORIZED);
		
		if(!ioService.checkImgFileExts(multipartFile.getOriginalFilename())) {
			return new ResponseEntity<Object>(HttpStatus.BAD_REQUEST);
		}

		String path = s3service.uploadWithRandomFileNm(multipartFile,NAWAGO_PROFILE_PATH);

		return new ResponseEntity<Object>(path,HttpStatus.OK);
	}
	
	@PostMapping("/file/delete") 
	@ResponseBody public void delete(@RequestBody PetInfoDTO petInfoDTO) {
		try {
			String filePath = NAWAGO_PROFILE_PATH + petInfoDTO.getFileName().toString();
			s3service.delete(filePath);
		} catch (Exception exception) {
			log.error("error occurred while update profilePhoto PetInfoDTO: {}", petInfoDTO, exception);
			throw new RuntimeException(exception);
		}
	}
	
	@PostMapping("/update/profilePhoto")
	public ResponseEntity<Object> updateProfilePhoto(@RequestBody MbrInfoDTO mbrInfoDTO) throws Exception {
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
			
		} catch (Exception e) {
			log.error("error occurred while update profilePhoto mbrInfoDto: {}", mbrInfoDTO, e);
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
	}
	
	@PostMapping("/update")
	public ResponseEntity<Object> updateMember(@RequestBody MbrInfoDTO mbrInfoDTO) throws Exception {
		try {
			mbrInfoDTO.setMbrEmail(XSSUtill.stripXSS(mbrInfoDTO.getMbrEmail()));
			mbrInfoDTO.setMbrNickNm(XSSUtill.stripXSS(mbrInfoDTO.getMbrNickNm()));
			mbrInfoDTO.setMbrNm(XSSUtill.stripXSS(mbrInfoDTO.getMbrNm()));
			mbrComponent.updateMbr(mbrInfoDTO);
			return new ResponseEntity<>( HttpStatus.OK);
		} catch(Exception e) {
			log.error("error occurred while update  MbrInfoDTO: {}", mbrInfoDTO, e);
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
	}
	
	@GetMapping("/getUserInfo")
	public ResponseEntity<MbrInfoResult> getUserInfo(@RequestParam(value="userId") String userId) throws Exception {
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
		} catch (Exception e) {
			log.error("error occurred while getUserInfo  userId: {}", userId, e);
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
}
