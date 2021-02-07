package com.study.mk1.jpa.mbr;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.study.mk1.data.MbrInfoDTO;

@Repository
public interface MbrJpaRepository extends JpaRepository<MbrJpa, Long>{
	
	/**
	 * mbrId 로 권한과 회원정보 가져오기
	 * @param mbrId
	 * @return
	 */
	MbrJpa findByMbrId(String mbrId);
	
	/**
	 * mbrSeq 로 회원정보 가져오기
	 * @param mbrSeq
	 * @return
	 */
	MbrJpa findByMbrSeq(long mbrSeq);
	
	/**
	 * mbrId 로 갯수 확인
	 * @param mbrId
	 * @return
	 */
	long countByMbrId(String mbrId);
	
	/**
	 * mbrEmail 로 갯수 확인
	 * @param mbrId
	 * @return
	 */
	long countByMbrEmail(String mbrEmail);
	
	/**
	 * mbrEmail 로 갯수 확인
	 * @param mbrId
	 * @return
	 */
	long countByMbrNickNm(String mbrNickNm);
	
	@Modifying
	@Query("UPDATE MbrJpa m "
			+ "SET m.mbrRpstImgUrl = :mbrRpstImgUrl , "
			+ "m.mbrRpstImgNm = :mbrRpstImgNm "
			+ "WHERE 1=1"
			+ "AND m.mbrSeq = :mbrSeq")
	int updateMbrRpstImg(String mbrRpstImgUrl , String mbrRpstImgNm, long mbrSeq);
	
	
	/*@Modifying
	@Query("UPDATE MbrJpa m "
			+ "SET"
			+ " "
			+ "m.mbrId = :#{#dto.mbrId}, "
			+ "m.mbrNm = :#{#dto.mbrNm},"
			+ "m.mbrNickNm = :#{#dto.mbrNickNm},"
			+ "m.mbrEmail = :#{#dto.mbrEmail},"
			+ "WHERE 1=1"
			+ "AND m.mbrSeq = :#{#dto.mbrSeq}")
	int updateMbr(@Param("dto") MbrInfoDTO dto);*/
	
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
