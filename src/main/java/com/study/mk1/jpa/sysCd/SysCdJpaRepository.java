package com.study.mk1.jpa.sysCd;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface  SysCdJpaRepository extends JpaRepository<SysCdJpa, Long>{
	List<SysCdJpa> findByUpperCd(String upperCd);
}
