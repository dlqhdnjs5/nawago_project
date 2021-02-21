package com.study.mk1.jpa.showOffLike;

import org.springframework.data.jpa.repository.JpaRepository;


public interface ShowOffLikeJpaRepository extends JpaRepository<ShowOffLikeJpa, Long> {
	
	/**
	 * 스토리 시퀀스와 회원 번호로 조회
	 * @param showOffSeq
	 * @param mbrSeq
	 * @return
	 */
	ShowOffLikeJpa findByShowOffSeqAndMbrSeq(long showOffSeq , long mbrSeq);
	
	/**
	 * 스토리 좋아요 카운트 구하기
	 * @param showOffSeq
	 * @return
	 */
	long countByShowOffSeq(long showOffSeq);
	
	void deleteByShowOffSeq(long showOffSeq);
}
