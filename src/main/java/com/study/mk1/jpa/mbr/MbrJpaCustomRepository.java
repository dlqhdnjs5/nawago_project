package com.study.mk1.jpa.mbr;

import com.study.mk1.data.MbrInfoDTO;

public interface MbrJpaCustomRepository {
	
	public void updateMbr(MbrInfoDTO dto);
	
	public MbrJpa findByMbrId(String mbrId);

}
