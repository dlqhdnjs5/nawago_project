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
import org.springframework.util.StringUtils;
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

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Controller
@Slf4j
@RequestMapping("/api/pet")
@RequiredArgsConstructor
public class PetController {
	private final static String PET_INFO_PATH = "nawago/pet/petInfo/";

	private final S3service s3service;
	private final JwtTokenProvider jwtTokenProvider;
	private final PetComponent petComponent;
	private final PetJpaRepository petJpaRepository;
	private final IOService ioService;
	
	
	@PostMapping("/file/upload") 
	@ResponseBody public String upload(@RequestParam("data") MultipartFile multipartFile) {
		try {
			return s3service.uploadWithRandomFileNm(multipartFile,PET_INFO_PATH);
		} catch (Exception exception) {
			log.error("error occurred while file upload.", exception);
			throw new RuntimeException(exception);
		}

	}
	
	
	@PostMapping("/file/delete") 
	@ResponseBody public void delete(@RequestBody PetInfoDTO dto) throws IOException {
		try {
			String filePath = "nawago/pet/petInfo/"+dto.getFileName().toString();
			s3service.delete(filePath);
		} catch (Exception exception) {
			log.error("error occurred while delete file. PetInfoDTO: {}", dto, exception);
            throw exception;
        }

	}

	@PostMapping("/registPet") 
	@ResponseBody
	public ResponseEntity<Object> registPet( HttpServletRequest req, @RequestBody PetInfoDTO dto) {
		try {
			String token = req.getHeader("x-auth");

			if(StringUtils.isEmpty(token)) {
				return new ResponseEntity<Object>(HttpStatus.UNAUTHORIZED);
			}

			boolean tokenValidYn = jwtTokenProvider.validateToken(token);
			if(!tokenValidYn) return new ResponseEntity<Object>(HttpStatus.UNAUTHORIZED);

			MbrJpa mbr = jwtTokenProvider.getUserInfo(token);
			dto.setMbrJpa(mbr);
			dto.getPetJpa().setPetIntro(XSSUtill.stripXSS(dto.getPetJpa().getPetIntro()));
			dto.getPetJpa().setPetNm(XSSUtill.stripXSS(dto.getPetJpa().getPetNm()));
			petComponent.registPet(dto);
		} catch (RuntimeException exception) {
			log.error("error occurred while registPet. PetInfoDTO: {}", dto, exception);
			throw exception;
		}

		return new ResponseEntity<>(HttpStatus.OK);
	}
	
	@GetMapping("/getPetInfo")
	@ResponseBody
	public ResponseEntity<PetJpa> getPetInfo(@RequestParam(value="petSeq") long petSeq) {
		try {
			PetJpa petJpa = petJpaRepository.findByPetSeq(petSeq);
			
			if(petJpa == null) {
				return new ResponseEntity<>(null,HttpStatus.OK);
			}
			
			return new ResponseEntity<>(petJpa,HttpStatus.OK);
		} catch(RuntimeException runtimeException) {
			log.error("error occurred while getPetInfo. petSeq : {}", petSeq, runtimeException);
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@PostMapping("/update/petProfilePhoto")
	public ResponseEntity<Object> updateProfilePhoto(@RequestBody PetInfoDTO petInfoDTO) {
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
			
		} catch (RuntimeException runtimeException) {
			log.error("error occurred while updatePet. PetInfoDTO : {}", petInfoDTO, runtimeException);
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@PostMapping("/update")
	public ResponseEntity<Object> updatePet(@RequestBody PetInfoDTO petInfoDTO) {
		try {
			petInfoDTO.setPetIntro(XSSUtill.stripXSS(petInfoDTO.getPetIntro()));
			petInfoDTO.setPetNm(XSSUtill.stripXSS(petInfoDTO.getPetNm()));
			petComponent.updatePet(petInfoDTO);
			return new ResponseEntity<>( HttpStatus.OK);
			
		} catch (RuntimeException runtimeException) {
			log.error("error occurred while updatePet. PetInfoDTO : {}", petInfoDTO, runtimeException);
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
		
}
