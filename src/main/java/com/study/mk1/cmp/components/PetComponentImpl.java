package com.study.mk1.cmp.components;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.study.mk1.cmp.services.PetService;
import com.study.mk1.controllers.DefaultController;
import com.study.mk1.data.MbrInfoDTO;
import com.study.mk1.data.PetInfoDTO;
import com.study.mk1.jpa.pet.PetJpa;

@Transactional
@Component
public class PetComponentImpl implements PetComponent{
		
		private static Logger log = LoggerFactory.getLogger(PetComponentImpl.class);
		
		
		@Autowired
		PetService petService;
		
		/**
		 * 반려동물 등록
		 * @param petInfoDTO
		 */
		public void registPet(PetInfoDTO petInfoDTO) {
			try {
				petService.registPet(petInfoDTO);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				log.info(e.toString());
				throw new RuntimeException(e);
			}
		}
		
		/**
		 * 프로필 사진 변경
		 * @param mbrJpa
		 * @throws Exception
		 */
		public void updatePetProfilePhoto(PetJpa pet) throws Exception{
			
			try {
				petService.updatePetProfilePhoto(pet);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				log.info(e.toString());
				throw new RuntimeException(e);
			}
			
		}
		
		@Override
		public void updatePet(PetInfoDTO petInfoDTO) throws Exception {
			
			try {
				
				petService.updatePet(petInfoDTO);
				
			}catch(Exception e) {
				log.info(e.getMessage());
				log.info(this.getClass()+".updatePet [ERROR] ! param -> {}",petInfoDTO);
				throw new RuntimeException(e);
			}
			
		}
		
}
