package com.study.mk1.jpa.showOff;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceContexts;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.study.mk1.data.ShowOffResult;
import com.study.mk1.data.ShowOffResultInf;
import com.study.mk1.jpa.mbr.MbrJpa;

@Repository
public interface ShowOffJpaRepository extends JpaRepository<ShowOffJpa, Long>  {
	
	//public void findByShowOffStatCd(String showOffStatCd);
	
	public void deleteByShowOffSeq(long showOffSeq);
	
}
