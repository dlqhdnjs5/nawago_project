package com.study.mk1.jpa.mbr;

import com.study.mk1.data.MbrInfoDTO;

public interface MbrJpaCustomRepository {
	
	void updateMbr(MbrInfoDTO dto);
	
	MbrJpa findByMbrId(String mbrId);

}
