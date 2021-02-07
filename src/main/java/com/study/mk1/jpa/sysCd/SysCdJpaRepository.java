package com.study.mk1.jpa.sysCd;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface  SysCdJpaRepository extends JpaRepository<SysCdJpa, Long>{
	
	
	/**
	 * upperCd 로 cd 조회
	 * @param upperCd
	 * @return
	 */
	List<SysCdJpa> findByUpperCd(String upperCd);
}
