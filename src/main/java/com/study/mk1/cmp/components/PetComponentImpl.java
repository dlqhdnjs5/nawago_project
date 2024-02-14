package com.study.mk1.cmp.components;

import org.springframework.stereotype.Component;

import com.study.mk1.cmp.services.PetService;
import com.study.mk1.data.PetInfoDTO;
import com.study.mk1.jpa.pet.PetJpa;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class PetComponentImpl implements PetComponent{
		private final PetService petService;
		
		public void registPet(PetInfoDTO petInfoDTO) {
			petService.registPet(petInfoDTO);
		}
		
		public void updatePetProfilePhoto(PetJpa pet) {
			petService.updatePetProfilePhoto(pet);
		}
		
		@Override
		public void updatePet(PetInfoDTO petInfoDTO) {
			petService.updatePet(petInfoDTO);
		}
		
}
