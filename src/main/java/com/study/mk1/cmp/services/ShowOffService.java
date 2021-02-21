package com.study.mk1.cmp.services;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.study.mk1.data.ShowOffInfoDTO;
import com.study.mk1.jpa.showOff.ShowOffJpa;
import com.study.mk1.jpa.showOff.ShowOffJpaRepository;
import com.study.mk1.jpa.showOffAttach.ShowOffAttachJpa;
import com.study.mk1.jpa.showOffAttach.ShowOffAttachJpaRepository;
import com.study.mk1.jpa.showOffLike.ShowOffLikeJpa;
import com.study.mk1.jpa.showOffLike.ShowOffLikeJpaRepository;
import com.study.mk1.jpa.showOffReply.ShowOffReplyJpa;
import com.study.mk1.jpa.showOffReply.ShowOffReplyJpaRepository;

@Service
public class ShowOffService {

	@Autowired
	ShowOffAttachJpaRepository showOffAttachJpaRepository;
	
	@Autowired
	ShowOffJpaRepository showOffJpaRepository;
	
	@Autowired
	ShowOffLikeJpaRepository showOffLikeJpaRepository;
	
	@Autowired
	ShowOffReplyJpaRepository showOffReplyJpaRepository;
	
	@PersistenceContext    // EntityManagerFactory가 DI 할 수 있도록 어노테이션 설정
    private EntityManager em;
	
	/**
	 * 스토리 저장
	 */
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
	
	/**
	 * 스토리 댓글 저장
	 */
	public long addShowOffReply(ShowOffInfoDTO dto) throws Exception {
		
		showOffReplyJpaRepository.save(dto.getShowOffReplyJpa());
		
		return showOffReplyJpaRepository.countByShowOffSeq(dto.getShowOffReplyJpa().getShowOffSeq());
		
	}
	
	/**
	 * 스토리 좋아요 업데이트
	 * @param dto
	 * @throws Exception
	 */
	public long updateShowOffLike(ShowOffLikeJpa dto) throws Exception {
		
		ShowOffLikeJpa result = showOffLikeJpaRepository.findByShowOffSeqAndMbrSeq(dto.getShowOffSeq() , dto.getMbrSeq());
		
		if(result != null) {
			showOffLikeJpaRepository.delete(dto);
		}else {
			showOffLikeJpaRepository.save(dto);
		}
		
		return showOffLikeJpaRepository.countByShowOffSeq(dto.getShowOffSeq());
		
	}
	
	
	/**
	 * 스토리 삭제
	 * @param dto
	 * @throws Exception
	 */
	public void deleteShowOff(ShowOffInfoDTO dto) throws Exception{
		long showOffSeq = dto.getShowOffSeq();
		showOffLikeJpaRepository.deleteByShowOffSeq(showOffSeq);
		showOffAttachJpaRepository.deleteByShowOffSeq(showOffSeq);
		showOffReplyJpaRepository.deleteByShowOffSeq(showOffSeq);
		showOffJpaRepository.deleteByShowOffSeq(showOffSeq);
		
	}
	
}
