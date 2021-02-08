package com.study.mk1.cmp.components;

import org.springframework.stereotype.Component;

import com.study.mk1.data.MbrInfoDTO;
import com.study.mk1.data.PetInfoDTO;
import com.study.mk1.jpa.mbr.MbrJpa;
import com.study.mk1.jpa.pet.PetJpa;

@Component
public interface PetComponent {

	/**
	 * 반려동물 등록
	 * @param petInfoDTO
	 */
	public void registPet(PetInfoDTO petInfoDTO);
	
	/**
	 * 프로필 사진 변경
	 * @param mbrJpa
	 * @throws Exception
	 */
	public void updatePetProfilePhoto(PetJpa pet) throws Exception;
	
	/**
	 * 펫 정보 변경
	 * @param mbrInfoDTO
	 * @throws Exception
	 */
	public void updatePet(PetInfoDTO mbrInfoDTO) throws Exception;
}
