package com.study.mk1.data;

import com.study.mk1.abstracts.AbstractEntity;
import com.study.mk1.jpa.mbr.MbrJpa;
import com.study.mk1.jpa.pet.PetJpa;

import lombok.Data;

@Data
public class PetInfoDTO extends AbstractEntity {

	private PetJpa petJpa;
	
	private MbrJpa mbrJpa;
}
