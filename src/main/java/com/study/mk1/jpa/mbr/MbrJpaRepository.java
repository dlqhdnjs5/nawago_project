package com.study.mk1.jpa.mbr;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface MbrJpaRepository extends JpaRepository<MbrJpa, Long>{
	
	MbrJpa findByMbrId(String mbrId);
	
	MbrJpa findByMbrSeq(long mbrSeq);

	long countByMbrId(String mbrId);
	
	long countByMbrEmail(String mbrEmail);

	long countByMbrNickNm(String mbrNickNm);
	
	@Modifying
	@Query("UPDATE MbrJpa m "
			+ "SET m.mbrRpstImgUrl = :mbrRpstImgUrl , "
			+ "m.mbrRpstImgNm = :mbrRpstImgNm "
			+ "WHERE 1=1"
			+ "AND m.mbrSeq = :mbrSeq")
	int updateMbrRpstImg(String mbrRpstImgUrl , String mbrRpstImgNm, long mbrSeq);
	
	@Query("SELECT count(*) as cnt "
			+ "FROM MbrJpa m "
			+ "WHERE 1=1 "
			+ "  AND m.mbrSeq <> :mbrSeq "
			+ "  AND m.mbrNickNm = :nickNm")
	int countByMbrNotNickNm(String nickNm,long mbrSeq);
	
	@Query("SELECT count(*) as cnt "
			+ "FROM MbrJpa m "
			+ "WHERE 1=1 "
			+ "  AND m.mbrSeq <> :mbrSeq "
			+ "  AND m.mbrEmail = :mbrEmail")
	int countByMbrNotEmail(String mbrEmail,long mbrSeq);
}
