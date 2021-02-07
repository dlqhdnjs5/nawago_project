package com.study.mk1.data;

import org.springframework.beans.factory.annotation.Autowired;

import com.study.mk1.abstracts.AbstractEntity;
import com.study.mk1.common.IOService;
import com.study.mk1.entity.Auth;
import com.study.mk1.entity.Mbr;
import com.study.mk1.jpa.mbr.MbrJpa;

import lombok.Data;

@Data
public class MbrInfoDTO extends AbstractEntity{

	@Autowired
	IOService ioService;
	
	Mbr mbr;
	
	MbrJpa mbrJpa;
	
	Auth auth;
	
	String mbrId;
	
	String mbrNm;
	
	String mbrPw;
	
	String mbrEmail;
	
	String mbrStatCd;
	
	String mbrTpCd;
	
	String mbrMobNationNo;
	
	String mbrMobAreaNo;
	
	String mbrMobTlofNo;
	
	String mbrMobTlofLstNo;
	
	String mbrGrdCd;
	
	String mobilNo;
	
	String mbrNickNm;
	
	//mbrUpdate
	//변경이전
	String bfMbrNickNm;
	String bfMbrEmail;
	
	long mbrSeq;
	
	private long mbrAuthMappingSeq;
	

}
