package com.study.mk1.cmp.components;

import com.study.mk1.data.MbrInfoDTO;
import com.study.mk1.jpa.mbr.MbrJpa;

public interface MbrComponent {
	void joinMbr( MbrInfoDTO mbrInfoDTO ) throws Exception;
	
	boolean checkMbrId(String mbrId);
	
	String isExistMbrInfoForUpdate(MbrInfoDTO mbrInfoDto);
	
	void updateProfilePhoto(MbrJpa mbrJpa) throws Exception;
	
	void updateMbr(MbrInfoDTO mbrInfoDTO) throws Exception;
}
