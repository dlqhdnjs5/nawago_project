package com.study.mk1.cmp.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.study.mk1.cmp.repositorys.AuthRepository;
import com.study.mk1.cmp.repositorys.MbrRepository;
import com.study.mk1.data.MbrInfoDTO;
import com.study.mk1.jpa.mbr.MbrJpa;
import com.study.mk1.jpa.mbr.MbrJpaCustomRepository;
import com.study.mk1.jpa.mbr.MbrJpaRepository;
import com.study.mk1.jpa.mbrAuthMapping.MbrAuthMappingJpa;
import com.study.mk1.jpa.mbrAuthMapping.MbrAuthMappingJpaRepository;
import com.study.mk1.sequrity.SecurityUserDetailService;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class MbrService {
	
	private static Logger log = LoggerFactory.getLogger(MbrService.class);
	
	@Autowired
	MbrRepository mbrRepository;
	
	@Autowired
	AuthRepository authRepository;
	
	@Autowired
	MbrJpaRepository MbrJpaRepository;
	
	@Autowired
	MbrJpaCustomRepository mbrJpaCustomRepository;
	
	@Autowired
	MbrAuthMappingJpaRepository mbrAuthMappingJpaRepository;
	
	/**
	 * 회원가입
	 */
	public void joinMbr(MbrInfoDTO mbrInfoDTO) {
		
		try {
			
			/*
			 * - mybatis
			 * mbrRepository.insertMbr(mbrInfoDTO.getMbr());
			 *  authRepository.insertMbrAuthMappingUseSelectKey(mbrInfoDTO);
			 *  
			 *  */
			
			MbrJpa mbrJpa = MbrJpaRepository.save(mbrInfoDTO.getMbrJpa());
			MbrAuthMappingJpa mam = new MbrAuthMappingJpa(); 
			mam.setMbrSeq(mbrJpa.getMbrSeq());
			mam.setAuthSeq(mbrInfoDTO.getMbrAuthMappingSeq());
			mbrAuthMappingJpaRepository.save(mam);
			
		}catch(Exception e) {
			e.printStackTrace();
			log.error(this.getClass().getName() +"joinMbr() ERROR --> param : {}",mbrInfoDTO);
			throw new RuntimeException();
		}
		
		
	}
	
	/**
	 * mbrId 로 아이디 중복체크
	 * true  : 중복 없음
	 * false : 중복
	 * @param mbrId
	 * @return
	 */
	public boolean checkMbrId(String mbrId) {
		return MbrJpaRepository.countByMbrId(mbrId) < 1 ? true : false;
	}
	
	
	/**
	 * mbrEmail email중복체크
	 * true  : 중복 없음
	 * false : 중복
	 * @param mbrId
	 * @return
	 */
	public boolean checkMbrEmail(String mbrEmail) {
		return MbrJpaRepository.countByMbrEmail(mbrEmail) < 1 ? true : false;
	}
	
	/**
	 * 닉네임 중복체크
	 * true  : 중복 없음
	 * false : 중복
	 * @param mbrId
	 * @return
	 */
	public boolean countByMbrNickNm(String mbrNickNm) {
		return MbrJpaRepository.countByMbrNickNm(mbrNickNm) < 1 ? true : false;
	}
	
	
	/**
	 * 닉네임 중복체크(본인제외)
	 * true  : 중복 없음
	 * false : 중복
	 * @param mbrId
	 * @return
	 */
	public boolean countByMbrNotNickNm(String mbrNickNm,long mbrSeq) {
		return MbrJpaRepository.countByMbrNotNickNm(mbrNickNm , mbrSeq) < 1 ? true : false;
	}
	
	/**
	 * mbrEmail email중복체크(본인제외)
	 * true  : 중복 없음
	 * false : 중복
	 * @param mbrId
	 * @return
	 */
	public boolean checkMbrNotEmail(String mbrEmail,long mbrSeq) {
		return MbrJpaRepository.countByMbrNotEmail(mbrEmail,mbrSeq) < 1 ? true : false;
	}
	
	
	public int updateProfilePhoto(MbrJpa mbrJpa) throws Exception {
		
		return  MbrJpaRepository.updateMbrRpstImg(mbrJpa.getMbrRpstImgUrl(),mbrJpa.getMbrRpstImgNm(),mbrJpa.getMbrSeq());
		
	}
	
	public void updateMbr(MbrInfoDTO mbrInfoDTO) throws Exception {
		
		 mbrJpaCustomRepository.updateMbr(mbrInfoDTO);
		
	}

}
