package com.study.mk1.cmp.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.study.mk1.data.MbrInfoDTO;
import com.study.mk1.data.PetEnum;
import com.study.mk1.data.PetInfoDTO;
import com.study.mk1.jpa.mbr.MbrJpa;
import com.study.mk1.jpa.mbrPetMapping.MbrPetMappingJpa;
import com.study.mk1.jpa.mbrPetMapping.MbrPetMappingJpaRepository;
import com.study.mk1.jpa.pet.PetJpa;
import com.study.mk1.jpa.pet.PetJpaRepository;

@Service
public class PetService {
	
	@Autowired
	PetJpaRepository petJpaRepository;
	
	@Autowired
	MbrPetMappingJpaRepository mbrPetMappingJpaRepository;
	
	public void registPet(PetInfoDTO petInfoDTO) throws Exception{
		petInfoDTO.getPetJpa().setPetStatCd(PetEnum.petStatCd.ACT.toString());
		PetJpa petJpaReturn = petJpaRepository.save(petInfoDTO.getPetJpa());
		MbrPetMappingJpa mbrPetMappingJpa = new MbrPetMappingJpa();
		mbrPetMappingJpa.setPetSeq(petJpaReturn.getPetSeq());
		mbrPetMappingJpa.setMbrSeq(petInfoDTO.getMbrJpa().getMbrSeq());
		mbrPetMappingJpaRepository.save(mbrPetMappingJpa);
		
		
	}
	
	public int updatePetProfilePhoto(PetJpa pet) throws Exception {
		
		return  petJpaRepository.updatePetProfilePhoto(pet.getPetImgUrl(),pet.getPetImgNm(),pet.getPetSeq());
		
	}
	
	public void updatePet(PetInfoDTO petInfoDTO) throws Exception {
		
		petJpaRepository.updateMbr(petInfoDTO.getPetSeq(), petInfoDTO.getPetNm() , petInfoDTO.getPetIntro());
		
	}

}
