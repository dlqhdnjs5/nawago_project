package com.study.mk1.jpa.showOff;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.mysema.query.Tuple;
import com.mysema.query.jpa.impl.JPAQuery;
import com.mysema.query.jpa.impl.JPAQueryFactory;
import com.mysema.query.jpa.impl.JPAUpdateClause;
import com.study.mk1.enums.ShowOffEnum;
import com.study.mk1.jpa.showOffReply.QShowOffReplyJpa;
import com.study.mk1.jpa.showOffReply.ShowOffReplyJpa;

@Transactional
@Repository
public class ShowOffJpaCustomRepositoryImpl  implements ShowOffJpaCustomRepository{


	@PersistenceContext    // EntityManagerFactory가 DI 할 수 있도록 어노테이션 설정
    private EntityManager em;
	
	
	@Override
	public List<ShowOffJpa> findByShowOffStatCd() {
		
		QShowOffJpa qShowOffJpa = QShowOffJpa.showOffJpa;
		JPAQuery query = new JPAQuery(em);
		return  query.from(qShowOffJpa)
				.where(qShowOffJpa.showOffStatCd.eq(ShowOffEnum.StatCd.ACT.toString()))
				.orderBy(qShowOffJpa.regDt.desc())
				.list(qShowOffJpa);
		
	}
	
	@Override
	public List<ShowOffJpa> findByShowOffPaging(Pageable pageable) {
		
		QShowOffJpa qShowOffJpa = QShowOffJpa.showOffJpa;
		JPAQuery query = new JPAQuery(em);
		return  query.from(qShowOffJpa)
				.where(qShowOffJpa.showOffStatCd.eq(ShowOffEnum.StatCd.ACT.toString()))
				.orderBy(qShowOffJpa.regDt.desc())
				.limit(pageable.getPageSize())
				.offset(pageable.getOffset())
				.list(qShowOffJpa);
	}
	
	@Override
	public List<ShowOffJpa> findByMbrSeq(long mbrSeq) {
		
		QShowOffJpa qShowOffJpa = QShowOffJpa.showOffJpa;
		JPAQuery query = new JPAQuery(em);
		return query.from(qShowOffJpa)
				.where(qShowOffJpa.mbrSeq.eq(mbrSeq))
				.orderBy(qShowOffJpa.regDt.desc())
				.list(qShowOffJpa);
		
	}
	
	@Override
	public List<ShowOffJpa> findByMbrSeqPaging(long mbrSeq,Pageable pageable) {
		
		QShowOffJpa qShowOffJpa = QShowOffJpa.showOffJpa;
		JPAQuery query = new JPAQuery(em);
		return query.from(qShowOffJpa)
				.where(qShowOffJpa.mbrSeq.eq(mbrSeq))
				.orderBy(qShowOffJpa.regDt.desc())
				.limit(pageable.getPageSize())
				.offset(pageable.getOffset())
				.list(qShowOffJpa);
	}
	
	@Override
	public List<ShowOffReplyJpa> showOffReplyfindByMbrSeq(long showOffSeq) {
		
		QShowOffReplyJpa qShowOffReplyJpa = QShowOffReplyJpa.showOffReplyJpa;
		JPAQuery query = new JPAQuery(em);
		return query.from(qShowOffReplyJpa)
				.where(qShowOffReplyJpa.showOffSeq.eq(showOffSeq))
				.list(qShowOffReplyJpa);
	}


	
}
