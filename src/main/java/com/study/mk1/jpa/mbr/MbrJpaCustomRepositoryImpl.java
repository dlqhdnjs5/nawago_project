package com.study.mk1.jpa.mbr;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Repository;

import com.mysema.query.jpa.impl.JPAUpdateClause;
import com.study.mk1.data.MbrInfoDTO;

@Repository
public class MbrJpaCustomRepositoryImpl implements MbrJpaCustomRepository {

	@PersistenceContext    // EntityManagerFactory가 DI 할 수 있도록 어노테이션 설정
    private EntityManager em;
	
	@Override
	public void updateMbr(MbrInfoDTO dto) {
		
		QMbrJpa qmbr = QMbrJpa.mbrJpa;
		JPAUpdateClause update = new JPAUpdateClause(em, qmbr);
		 update
			.set(qmbr.mbrNm,dto.getMbrNm())
			.set(qmbr.mbrEmail,dto.getMbrEmail())
			.set(qmbr.mbrNickNm,dto.getMbrNickNm())
			.where(qmbr.mbrSeq.eq(dto.getMbrSeq()))
			.execute();
	}
}
