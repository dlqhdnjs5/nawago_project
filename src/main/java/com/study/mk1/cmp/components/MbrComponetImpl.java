package com.study.mk1.cmp.components;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.study.mk1.cmp.services.MbrService;
import com.study.mk1.data.MbrInfoDTO;
import com.study.mk1.jpa.mbr.MbrJpa;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Transactional
@Component
@RequiredArgsConstructor
public class MbrComponetImpl implements MbrComponent {
	private final MbrService mbrService;
	
	public void joinMbr(MbrInfoDTO mbrInfoDTO) throws Exception {
		mbrService.joinMbr(mbrInfoDTO);
	}
	
	public boolean checkMbrId(String mbrId) {
		return mbrService.checkMbrId(mbrId);
	}

	@Override
	public String isExistMbrInfo(MbrInfoDTO mbrInfoDto) {
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
	public String isExistMbrInfoForUpdate(MbrInfoDTO mbrInfoDto) {
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
	public void updateProfilePhoto(MbrJpa mbrJpa) {
		int result = mbrService.updateProfilePhoto(mbrJpa);
		if(result < 1) throw new RuntimeException();
	}
	
	@Override
	public void updateMbr(MbrInfoDTO mbrInfoDTO) {
		mbrService.updateMbr(mbrInfoDTO);
	}

}
