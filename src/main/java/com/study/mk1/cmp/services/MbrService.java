package com.study.mk1.cmp.services;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.study.mk1.cmp.repositorys.AuthRepository;
import com.study.mk1.data.MbrInfoDTO;
import com.study.mk1.jpa.mbr.MbrJpa;
import com.study.mk1.jpa.mbr.MbrJpaCustomRepository;
import com.study.mk1.jpa.mbr.MbrJpaRepository;
import com.study.mk1.jpa.mbrAuthMapping.MbrAuthMappingJpa;
import com.study.mk1.jpa.mbrAuthMapping.MbrAuthMappingJpaRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class MbrService {
	private final AuthRepository authRepository;
	private final MbrJpaRepository MbrJpaRepository;
	private final MbrJpaCustomRepository mbrJpaCustomRepository;
	private final MbrAuthMappingJpaRepository mbrAuthMappingJpaRepository;

	@Transactional
	public void joinMbr(MbrInfoDTO mbrInfoDTO) {
		try {
			MbrJpa mbrJpa = MbrJpaRepository.save(mbrInfoDTO.getMbrJpa());
			MbrAuthMappingJpa mam = new MbrAuthMappingJpa(); 
			mam.setMbrSeq(mbrJpa.getMbrSeq());
			mam.setAuthSeq(mbrInfoDTO.getMbrAuthMappingSeq());
			mbrAuthMappingJpaRepository.save(mam);
		}catch(Exception e) {
			log.error(this.getClass().getName() +"joinMbr() ERROR --> param : {}",mbrInfoDTO);
			throw new RuntimeException("error occre", e);
		}
	}
	
	public boolean checkMbrId(String mbrId) {
		return MbrJpaRepository.countByMbrId(mbrId) < 1;
	}

	public boolean checkMbrEmail(String mbrEmail) {
		return MbrJpaRepository.countByMbrEmail(mbrEmail) < 1;
	}
	
	public boolean countByMbrNickNm(String mbrNickNm) {
		return MbrJpaRepository.countByMbrNickNm(mbrNickNm) < 1;
	}
	
	public boolean countByMbrNotNickNm(String mbrNickNm,long mbrSeq) {
		return MbrJpaRepository.countByMbrNotNickNm(mbrNickNm, mbrSeq) < 1;
	}
	
	public boolean checkMbrNotEmail(String mbrEmail,long mbrSeq) {
		return MbrJpaRepository.countByMbrNotEmail(mbrEmail, mbrSeq) < 1;
	}
	
	public int updateProfilePhoto(MbrJpa mbrJpa) {
		return  MbrJpaRepository.updateMbrRpstImg(mbrJpa.getMbrRpstImgUrl(),mbrJpa.getMbrRpstImgNm(),mbrJpa.getMbrSeq());
	}
	
	public void updateMbr(MbrInfoDTO mbrInfoDTO) {
		 mbrJpaCustomRepository.updateMbr(mbrInfoDTO);
	}

}
