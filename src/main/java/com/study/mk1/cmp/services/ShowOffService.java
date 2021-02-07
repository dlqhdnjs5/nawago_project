package com.study.mk1.cmp.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.study.mk1.data.ShowOffInfoDTO;
import com.study.mk1.jpa.showOff.ShowOffJpa;
import com.study.mk1.jpa.showOff.ShowOffJpaRepository;
import com.study.mk1.jpa.showOffAttach.ShowOffAttachJpa;
import com.study.mk1.jpa.showOffAttach.ShowOffAttachJpaRepository;
import com.study.mk1.jpa.showOffReply.ShowOffReplyJpaRepository;

@Service
public class ShowOffService {

	@Autowired
	ShowOffAttachJpaRepository showOffAttachJpaRepository;
	
	@Autowired
	ShowOffJpaRepository showOffJpaRepository;
	
	@Autowired
	ShowOffReplyJpaRepository showOffReplyJpaRepository;
	
	public void addShowOff(ShowOffInfoDTO dto) throws Exception{
		
		ShowOffJpa showOffJpa = showOffJpaRepository.save(dto.getShowOffjpa());
		int showOffTurn = 1;
		ShowOffAttachJpa showOffAttachJpa = new ShowOffAttachJpa();
		
		
		for(ShowOffAttachJpa showOffAttachJpaObj : dto.getShowOffAttachJpaList()) {
			showOffAttachJpaObj.setShowOffSeq(showOffJpa.getShowOffSeq());
			showOffAttachJpaObj.setShowOffTurn(showOffTurn);
			showOffTurn++;
		}
		showOffAttachJpaRepository.saveAll(dto.getShowOffAttachJpaList());
		
	}
	
	public void addShowOffReply(ShowOffInfoDTO dto) throws Exception {
		
		showOffReplyJpaRepository.save(dto.getShowOffReplyJpa());
		
	}
	
}
