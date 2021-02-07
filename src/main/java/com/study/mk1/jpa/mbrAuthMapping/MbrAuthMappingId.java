package com.study.mk1.jpa.mbrAuthMapping;

import com.study.mk1.abstracts.AbstractEntity;

import lombok.Data;

@Data
public class MbrAuthMappingId extends AbstractEntity {
	
	long mbrSeq;
	
	long authSeq;

}
