package com.study.mk1.jpa.showOff;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.study.mk1.data.ShowOffResult;
import com.study.mk1.jpa.showOffReply.ShowOffReplyJpa;

public interface ShowOffJpaCustomRepository {

	public List<ShowOffJpa> findByShowOffStatCd();
	
	public List<ShowOffJpa> findByShowOffPaging(Pageable pageable);
	
	public List<ShowOffJpa> findByMbrSeq(long mbrSeq);
	
	public List<ShowOffJpa> findByMbrSeqPaging(long mbrSeq,Pageable pageable);
	
	public List<ShowOffResult> findByMbrSeqPagingV2(long mbrSeq,Pageable pageable);
	
	public List<ShowOffReplyJpa> showOffReplyfindByMbrSeq(long showOffSeq);

	public List<ShowOffResult> findByShowOffPagingV2(long mbrSeq,Pageable pageable);
	
	
}
