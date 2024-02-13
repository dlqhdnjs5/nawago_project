package com.study.mk1.jpa.showOff;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.study.mk1.data.ShowOffResult;
import com.study.mk1.jpa.showOffReply.ShowOffReplyJpa;

public interface ShowOffJpaCustomRepository {
	List<ShowOffResult> findByMbrSeqPagingV2(long mbrSeq,Pageable pageable);
	List<ShowOffReplyJpa> showOffReplyfindByMbrSeq(long showOffSeq);
	List<ShowOffResult> findByShowOffPagingV2(long mbrSeq,Pageable pageable);
	void deleteShowOff(ShowOffJpa showOffJpa);
	
	
}
