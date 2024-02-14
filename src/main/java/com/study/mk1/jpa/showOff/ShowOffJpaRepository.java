package com.study.mk1.jpa.showOff;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ShowOffJpaRepository extends JpaRepository<ShowOffJpa, Long>  {
	
	//public void findByShowOffStatCd(String showOffStatCd);
	
	public void deleteByShowOffSeq(long showOffSeq);
	
}
