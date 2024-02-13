package com.study.mk1.cmp.components;

import org.springframework.stereotype.Component;

import com.study.mk1.data.MbrInfoDTO;
import com.study.mk1.data.PetInfoDTO;
import com.study.mk1.jpa.mbr.MbrJpa;
import com.study.mk1.jpa.pet.PetJpa;

@Component
public interface PetComponent {
	void registPet(PetInfoDTO petInfoDTO);
	
	void updatePetProfilePhoto(PetJpa pet);

	void updatePet(PetInfoDTO mbrInfoDTO);
}
