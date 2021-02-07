package com.study.mk1.jpa.pet;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface  PetJpaRepository extends JpaRepository<PetJpa, Long>{

}
