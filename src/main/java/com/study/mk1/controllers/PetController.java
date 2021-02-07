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
import com.study.mk1.common.JwtTokenProvider;
import com.study.mk1.common.S3service;
import com.study.mk1.common.XSSUtill;
import com.study.mk1.data.PetInfoDTO;
import com.study.mk1.jpa.mbr.MbrJpa;

@Controller
@RequestMapping("/api/pet")
public class PetController {

	@Autowired
	S3service s3service;
	
	@Autowired
	JwtTokenProvider jwtTokenProvider;
	
	@Autowired
	PetComponent petComponent;
	
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
}
