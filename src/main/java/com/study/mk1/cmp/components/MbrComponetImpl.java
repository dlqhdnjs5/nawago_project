package com.study.mk1.cmp.components;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.study.mk1.cmp.services.MbrService;
import com.study.mk1.data.MbrInfoDTO;
import com.study.mk1.jpa.mbr.MbrJpa;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Transactional
@Component
public class MbrComponetImpl implements MbrComponent {
	
	@Autowired
	MbrService mbrService;
	
	/**
	 * 회원가입
	 */
	public void joinMbr(MbrInfoDTO mbrInfoDTO) throws Exception {
		
		mbrService.joinMbr(mbrInfoDTO);
		
	}
	
	/**
	 * 아이디 중복체크
	 */
	public boolean checkMbrId(String mbrId) {
		
		return mbrService.checkMbrId(mbrId);
		
	}
	
	/**
	 * 이메일 중복체크
	 */
	public boolean checkMbrEmail(String mbrEmail) {
		
		return mbrService.checkMbrEmail(mbrEmail);
		
	}
	
	/**
	 * 닉네임 중복체크
	 */
	public boolean countByMbrNickNm(String mbrNickNm) {
		
		return mbrService.countByMbrNickNm(mbrNickNm);
		
	}
	@Override
	public String isExistMbrInfo(MbrInfoDTO mbrInfoDto) throws Exception {
		
		if(!mbrService.checkMbrId(mbrInfoDto.getMbrId())) {
			return "ID";
		}
		
		if(!mbrService.checkMbrEmail(mbrInfoDto.getMbrEmail())) {
			return "EMAIL";
		}
		
		if(!mbrService.countByMbrNickNm(mbrInfoDto.getMbrNickNm())) {
			return "NICKNM";
		}
		
		return "SUCCESS";
	}
	
	@Override
	public String isExistMbrInfoForUpdate(MbrInfoDTO mbrInfoDto) throws Exception {
		
		long mbrSeq = mbrInfoDto.getMbrSeq();
		String bfMbrEmail = mbrInfoDto.getBfMbrEmail();
		String bfMbrNickNm = mbrInfoDto.getBfMbrNickNm();
		
		if(!bfMbrEmail.equals(mbrInfoDto.getMbrEmail() ) ) {
			if(!mbrService.checkMbrNotEmail(mbrInfoDto.getMbrEmail(),mbrSeq)) {
				return "EMAIL";
			}
		}
		if(!bfMbrNickNm.equals(mbrInfoDto.getMbrNickNm() ) ) {
			if(!mbrService.countByMbrNotNickNm(mbrInfoDto.getMbrNickNm(),mbrSeq)) {
				return "NICKNM";
			}
		}
		
		return "SUCCESS";
	}
	
	
	
	@Override
	public void updateProfilePhoto(MbrJpa mbrJpa) throws Exception {
		
		try {
			int result = mbrService.updateProfilePhoto(mbrJpa);
			
			if(result < 1)throw new RuntimeException();
			
		}catch(Exception e) {
			log.info(e.getMessage());
			log.info(this.getClass()+".updateProfilePhoto error ! param -> {}",mbrJpa);
			throw new RuntimeException();
		}
		
	}
	
	@Override
	public void updateMbr(MbrInfoDTO mbrInfoDTO) throws Exception {
		
		try {
			
			mbrService.updateMbr(mbrInfoDTO);
			
		}catch(Exception e) {
			log.info(e.getMessage());
			log.info(this.getClass()+".updateMbr [ERROR] ! param -> {}",mbrInfoDTO);
			throw new RuntimeException(e);
		}
		
	}

}
