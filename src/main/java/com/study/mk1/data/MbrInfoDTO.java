package com.study.mk1.data;

import com.study.mk1.abstracts.AbstractEntity;
import com.study.mk1.entity.Auth;
import com.study.mk1.entity.Mbr;
import com.study.mk1.jpa.mbr.MbrJpa;

import lombok.Data;

@Data
public class MbrInfoDTO extends AbstractEntity{
	private Mbr mbr;
	private MbrJpa mbrJpa;
	private Auth auth;
	private String mbrId;
	private String mbrNm;
	private String mbrPw;
	private String mbrEmail;
	private String mbrStatCd;
	private String mbrTpCd;
	private String mbrMobNationNo;
	private String mbrMobAreaNo;
	private String mbrMobTlofNo;
	private String mbrMobTlofLstNo;
	private String mbrGrdCd;
	private String mobilNo;
	private String mbrNickNm;
	private String bfMbrNickNm;
	private String bfMbrEmail;
	private long mbrSeq;
	private long mbrAuthMappingSeq;
}
