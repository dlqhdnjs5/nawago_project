package com.study.mk1.cmp.components;

import org.springframework.stereotype.Component;

import com.study.mk1.data.MbrInfoDTO;
import com.study.mk1.jpa.mbr.MbrJpa;

public interface MbrComponent {
	
	/**
	 * 회원가입 Mybatis , JPA 분기
	 * @param mbrInfoDTO
	 * @throws Exception
	 */
	public void joinMbr( MbrInfoDTO mbrInfoDTO ) throws Exception;
	
	/**
	 * 아이디 중복체크
	 * @param mbrId
	 * @return
	 */
	public boolean checkMbrId(String mbrId);
	
	/**
	 * 존재 유효성 체크
	 * @param mbrInfoDto
	 * @return
	 */
	public String isExistMbrInfo(MbrInfoDTO mbrInfoDto) throws Exception;
	
	/**
	 * 존재 유효성 체크
	 * @param mbrInfoDto
	 * @return
	 */
	public String isExistMbrInfoForUpdate(MbrInfoDTO mbrInfoDto) throws Exception;
	
	/**
	 * 프로필 사진 변경
	 * @param mbrJpa
	 * @throws Exception
	 */
	public void updateProfilePhoto(MbrJpa mbrJpa) throws Exception;
	
	
	/**
	 * 회원 정보 변경
	 * @param mbrInfoDTO
	 * @throws Exception
	 */
	public void updateMbr(MbrInfoDTO mbrInfoDTO) throws Exception;
	
}
