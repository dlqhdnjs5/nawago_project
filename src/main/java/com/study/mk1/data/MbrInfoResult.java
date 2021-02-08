package com.study.mk1.data;

import java.util.List;

import com.study.mk1.jpa.mbr.MbrJpa;
import com.study.mk1.jpa.mbrPetMapping.MbrPetMappingJpa;
import com.study.mk1.jpa.pet.PetJpa;

import lombok.Data;

@Data
public class MbrInfoResult {
	
	private MbrJpa mbrJpa;
	
	private List<MbrPetMappingJpa> mbrPetMappingJpaList;
	
	private List<PetJpa> petJpaList;

}
