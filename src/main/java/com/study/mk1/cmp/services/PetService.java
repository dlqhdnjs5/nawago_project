package com.study.mk1.cmp.services;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.study.mk1.data.PetEnum;
import com.study.mk1.data.PetInfoDTO;
import com.study.mk1.jpa.mbrPetMapping.MbrPetMappingJpa;
import com.study.mk1.jpa.mbrPetMapping.MbrPetMappingJpaRepository;
import com.study.mk1.jpa.pet.PetJpa;
import com.study.mk1.jpa.pet.PetJpaRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PetService {
	private final PetJpaRepository petJpaRepository;
	private final MbrPetMappingJpaRepository mbrPetMappingJpaRepository;

	@Transactional
	public void registPet(PetInfoDTO petInfoDTO) {
		petInfoDTO.getPetJpa().setPetStatCd(PetEnum.petStatCd.ACT.toString());
		PetJpa petJpaReturn = petJpaRepository.save(petInfoDTO.getPetJpa());
		MbrPetMappingJpa mbrPetMappingJpa = new MbrPetMappingJpa();
		mbrPetMappingJpa.setPetSeq(petJpaReturn.getPetSeq());
		mbrPetMappingJpa.setMbrSeq(petInfoDTO.getMbrJpa().getMbrSeq());
		mbrPetMappingJpaRepository.save(mbrPetMappingJpa);
	}
	
	public void updatePetProfilePhoto(PetJpa pet) {
		petJpaRepository.updatePetProfilePhoto(pet.getPetImgUrl(), pet.getPetImgNm(), pet.getPetSeq());

	}
	
	public void updatePet(PetInfoDTO petInfoDTO) {
		petJpaRepository.updateMbr(petInfoDTO.getPetSeq(), petInfoDTO.getPetNm() , petInfoDTO.getPetIntro());
	}
}
