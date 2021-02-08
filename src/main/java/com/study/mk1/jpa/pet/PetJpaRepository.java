package com.study.mk1.jpa.pet;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;


@Repository
public interface  PetJpaRepository extends JpaRepository<PetJpa, Long>{
	
	/**
	 * pet 정보 조회
	 * @param petSeq
	 * @return
	 */
	public PetJpa findByPetSeq(long petSeq);
	
	
	@Modifying
	@Query("UPDATE PetJpa m "
			+ "SET m.petImgUrl = :petImgUrl , "
			+ "m.petImgNm = :petImgNm "
			+ "WHERE 1=1"
			+ "AND m.petSeq = :petSeq")
	public int updatePetProfilePhoto(String petImgUrl , String petImgNm, long petSeq);
	
	
	@Modifying
	@Query("UPDATE PetJpa m "
			+ "SET m.petNm = :petNm , "
			+ "m.petIntro = :petIntro "
			+ "WHERE 1=1"
			+ "AND m.petSeq = :petSeq")
	public void updateMbr(long petSeq , String petNm, String petIntro);

}
