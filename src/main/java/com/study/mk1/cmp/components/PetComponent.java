package com.study.mk1.cmp.components;

import org.springframework.stereotype.Component;

import com.study.mk1.data.PetInfoDTO;

@Component
public interface PetComponent {

	/**
	 * 반려동물 등록
	 * @param petInfoDTO
	 */
	public void registPet(PetInfoDTO petInfoDTO);
}
