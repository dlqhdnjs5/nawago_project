package com.study.mk1.cmp.services;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import com.study.mk1.data.ShowOffInfoDTO;
import com.study.mk1.jpa.showOff.ShowOffJpa;
import com.study.mk1.jpa.showOff.ShowOffJpaRepository;
import com.study.mk1.jpa.showOffAttach.ShowOffAttachJpa;
import com.study.mk1.jpa.showOffAttach.ShowOffAttachJpaRepository;
import com.study.mk1.jpa.showOffLike.ShowOffLikeJpa;
import com.study.mk1.jpa.showOffLike.ShowOffLikeJpaRepository;
import com.study.mk1.jpa.showOffReply.ShowOffReplyJpaRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ShowOffService {
	private final ShowOffAttachJpaRepository showOffAttachJpaRepository;
	private final ShowOffJpaRepository showOffJpaRepository;
	private final ShowOffLikeJpaRepository showOffLikeJpaRepository;
	private final ShowOffReplyJpaRepository showOffReplyJpaRepository;

	@Transactional
	public void addShowOff(ShowOffInfoDTO dto) {
		ShowOffJpa showOffJpa = showOffJpaRepository.save(dto.getShowOffjpa());
		int showOffTurn = 1;

		for(ShowOffAttachJpa showOffAttachJpaObj : dto.getShowOffAttachJpaList()) {
			showOffAttachJpaObj.setShowOffSeq(showOffJpa.getShowOffSeq());
			showOffAttachJpaObj.setShowOffTurn(showOffTurn);
			showOffTurn++;
		}
		showOffAttachJpaRepository.saveAll(dto.getShowOffAttachJpaList());
	}

	@Transactional
	public long addShowOffReply(ShowOffInfoDTO dto) {
		showOffReplyJpaRepository.save(dto.getShowOffReplyJpa());
		return showOffReplyJpaRepository.countByShowOffSeq(dto.getShowOffReplyJpa().getShowOffSeq());
	}
	
	public long updateShowOffLike(ShowOffLikeJpa dto) {
		ShowOffLikeJpa result = showOffLikeJpaRepository.findByShowOffSeqAndMbrSeq(dto.getShowOffSeq() , dto.getMbrSeq());
		
		if(!ObjectUtils.isEmpty(result)) {
			showOffLikeJpaRepository.delete(dto);
		}else {
			showOffLikeJpaRepository.save(dto);
		}
		
		return showOffLikeJpaRepository.countByShowOffSeq(dto.getShowOffSeq());
	}
	
	@Transactional
	public void deleteShowOff(ShowOffInfoDTO dto) {
		long showOffSeq = dto.getShowOffSeq();
		showOffLikeJpaRepository.deleteByShowOffSeq(showOffSeq);
		showOffAttachJpaRepository.deleteByShowOffSeq(showOffSeq);
		showOffReplyJpaRepository.deleteByShowOffSeq(showOffSeq);
		showOffJpaRepository.deleteByShowOffSeq(showOffSeq);
	}
}
