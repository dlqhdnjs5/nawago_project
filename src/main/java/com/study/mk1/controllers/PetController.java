package com.study.mk1.controllers;

import java.io.IOException;
import java.util.ArrayList;
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
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.SdkClientException;
import com.study.mk1.cmp.components.PetComponent;
import com.study.mk1.common.IOService;
import com.study.mk1.common.JwtTokenProvider;
import com.study.mk1.common.S3service;
import com.study.mk1.common.XSSUtill;
import com.study.mk1.data.MbrInfoDTO;
import com.study.mk1.data.PetInfoDTO;
import com.study.mk1.jpa.mbr.MbrJpa;
import com.study.mk1.jpa.pet.PetJpa;
import com.study.mk1.jpa.pet.PetJpaRepository;

import lombok.extern.slf4j.Slf4j;

@Controller
@Slf4j
@RequestMapping("/api/pet")
public class PetController {

	@Autowired
	S3service s3service;
	
	@Autowired
	JwtTokenProvider jwtTokenProvider;
	
	@Autowired
	PetComponent petComponent;
	
	@Autowired
	PetJpaRepository petJpaRepository;
	
	@Autowired
	IOService ioService;
	
	
	@PostMapping("/file/upload") 
	@ResponseBody public String upload(@RequestParam("data") MultipartFile multipartFile) throws IOException { 
		
		String path = s3service.uploadWithRandomFileNm(multipartFile,"nawago/pet/petInfo/");
		
		return path;
	}
	
	
	@PostMapping("/file/delete") 
	@ResponseBody public void delete(HttpServletRequest req,HttpServletResponse res,  @RequestBody PetInfoDTO dto) throws IOException { 
		
		try {
			String filePath = "nawago/pet/petInfo/"+dto.getFileName().toString();
			s3service.delete(filePath);
		} catch (AmazonServiceException e) {
            e.printStackTrace();
        } catch (SdkClientException e) {
            e.printStackTrace();
        }
		
	}
	
	@PostMapping("/tesUpload") 
	@ResponseBody
	public String tesUpload( HttpServletRequest req,HttpServletResponse res,  @RequestBody PetInfoDTO dto) throws IOException { 
		
		try {
			String filePath = "nawago/pet/petInfo/"+dto.getFileName().toString();
			s3service.delete(filePath);
		} catch (AmazonServiceException e) {
            e.printStackTrace();
        } catch (SdkClientException e) {
            e.printStackTrace();
        }
		
		return dto.toString();
	}
	
	@PostMapping("/registPet") 
	@ResponseBody
	public ResponseEntity<Object> registPet( HttpServletRequest req,HttpServletResponse res,  @RequestBody PetInfoDTO dto) throws IOException,Exception { 
		
		String  token = req.getHeader("x-auth");
		if(!"".equals(token)) {
    		boolean tokenValidYn = jwtTokenProvider.validateToken(token);
    		if(!tokenValidYn)return new ResponseEntity<Object>(HttpStatus.UNAUTHORIZED);
    		MbrJpa mbr = jwtTokenProvider.getUserInfo(token);
    		dto.setMbrJpa(mbr);
    		dto.getPetJpa().setPetIntro(XSSUtill.stripXSS(dto.getPetJpa().getPetIntro()));
    		dto.getPetJpa().setPetNm(XSSUtill.stripXSS(dto.getPetJpa().getPetNm()));
    		petComponent.registPet(dto);
    		
    		return new ResponseEntity<Object>(HttpStatus.OK);
    	}else {
    		return new ResponseEntity<Object>(HttpStatus.UNAUTHORIZED);
    	}
		
	}
	
	@GetMapping("/getPetInfo")
	@ResponseBody
	public ResponseEntity<PetJpa> getPetInfo( HttpServletRequest req,HttpServletResponse res,  @RequestParam(value="petSeq") long petSeq) throws Exception {
		
		try {
			
			PetJpa petJpa = petJpaRepository.findByPetSeq(petSeq);
			
			if(petJpa == null) {
				return new ResponseEntity<>(null,HttpStatus.OK);
			}
			
			return new ResponseEntity<PetJpa>(petJpa,HttpStatus.OK);
			
		}catch(Exception e) {
			log.error(e.toString());
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
	}
	
	@PostMapping("/update/petProfilePhoto")
	public ResponseEntity<Object> updateProfilePhoto(HttpServletRequest req,HttpServletResponse res,@RequestBody PetInfoDTO petInfoDTO) throws Exception {
			
		try {
			PetJpa petJpa = new PetJpa();
			petJpa.setPetSeq(petInfoDTO.getPetSeq());
			petJpa.setPetImgUrl(ioService.getFileUrl(petInfoDTO.getAttachFileUrl()));
			petJpa.setPetImgNm(ioService.getFileNm(petInfoDTO.getAttachFileUrl()));
			petComponent.updatePetProfilePhoto(petJpa);
			
			Map<String,String> map = new HashMap<>();
			map.put("mbrRpstImgUrl", petJpa.getPetImgUrl());
			map.put("mbrRpstImgNm", petJpa.getPetImgNm());
			return new ResponseEntity<>( map,  HttpStatus.OK);
			
		}catch(Exception e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
	}
	
	@PostMapping("/update")
	public ResponseEntity<Object> updatePet(HttpServletRequest req,HttpServletResponse res,@RequestBody PetInfoDTO petInfoDTO) throws Exception {
			
		try {
			petInfoDTO.setPetIntro(XSSUtill.stripXSS(petInfoDTO.getPetIntro()));
			petInfoDTO.setPetNm(XSSUtill.stripXSS(petInfoDTO.getPetNm()));
			petComponent.updatePet(petInfoDTO);
			return new ResponseEntity<>( HttpStatus.OK);
			
		}catch(Exception e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
	}
		
}
