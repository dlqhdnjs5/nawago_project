package com.study.mk1.jpa.showOffAttach;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.study.mk1.jpa.showOff.ShowOffJpa;

@Repository
public interface ShowOffAttachJpaRepository extends JpaRepository<ShowOffAttachJpa, Long>{
	
	void deleteByShowOffSeq(long showOffSeq);

}
