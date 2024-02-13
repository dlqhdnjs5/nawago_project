package com.study.mk1.jpa.mbr;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Repository;

import com.mysema.query.jpa.impl.JPAQuery;
import com.mysema.query.jpa.impl.JPAUpdateClause;
import com.study.mk1.data.MbrInfoDTO;
import com.study.mk1.enums.MbrEnum;

@Repository
public class MbrJpaCustomRepositoryImpl implements MbrJpaCustomRepository {
	@PersistenceContext
    private EntityManager em;
	
	@Override
	public void updateMbr(MbrInfoDTO dto) {
		MbrJpa qmbr = MbrJpa.mbrJpa;
		JPAUpdateClause update = new JPAUpdateClause(em, qmbr);
		 update
			.set(qmbr.mbrNm,dto.getMbrNm())
			.set(qmbr.mbrEmail,dto.getMbrEmail())
			.set(qmbr.mbrNickNm,dto.getMbrNickNm())
			.where(qmbr.mbrSeq.eq(dto.getMbrSeq()))
			.execute();
	}
	
	@Override
	public MbrJpa findByMbrId(String mbrId) {
		QMbrJpa qmbr = QMbrJpa.mbrJpa;
		JPAQuery query = new JPAQuery(em);
		return query.from(qmbr)
					.where(qmbr.mbrId.eq(mbrId))
					.where(qmbr.mbrStatCd.eq(MbrEnum.StatCd.ACT.toString()))
					.singleResult(qmbr);
	}
}
