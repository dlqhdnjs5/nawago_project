package com.study.mk1.jpa.showOffReply;

import org.springframework.data.jpa.repository.JpaRepository;


public interface ShowOffReplyJpaRepository extends JpaRepository<ShowOffReplyJpa, Long> {
	
	long countByShowOffSeq(long showOffSeq);
}
