package com.study.mk1.jpa.showOff;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.hibernate.query.criteria.internal.expression.function.AggregationFunction.COUNT;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.mysema.query.Tuple;
import com.mysema.query.jpa.JPAExpressions;
import com.mysema.query.jpa.JPASubQuery;
import com.mysema.query.jpa.impl.JPAQuery;
import com.mysema.query.jpa.impl.JPAQueryFactory;
import com.mysema.query.jpa.impl.JPAUpdateClause;
import com.mysema.query.types.Projections;
import com.study.mk1.data.ShowOffResult;
import com.study.mk1.enums.MbrEnum;
import com.study.mk1.enums.ShowOffEnum;
import com.study.mk1.jpa.mbr.QMbrJpa;
import com.study.mk1.jpa.showOffLike.QShowOffLikeJpa;
import com.study.mk1.jpa.showOffReply.QShowOffReplyJpa;
import com.study.mk1.jpa.showOffReply.ShowOffReplyJpa;

@Transactional
@Repository
public class ShowOffJpaCustomRepositoryImpl  implements ShowOffJpaCustomRepository{


	@PersistenceContext    // EntityManagerFactory가 DI 할 수 있도록 어노테이션 설정
    private EntityManager em;
	
	
	/**
	 * 스토리 리스트 조회 
	 * TODO : 삭제예정
	 */
	@Override
	public List<ShowOffJpa> findByShowOffStatCd() {
		
		QShowOffJpa qShowOffJpa = QShowOffJpa.showOffJpa;
		JPAQuery query = new JPAQuery(em);
		return  query.from(qShowOffJpa)
				.where(qShowOffJpa.showOffStatCd.eq(ShowOffEnum.StatCd.ACT.toString()))
				.orderBy(qShowOffJpa.regDt.desc())
				.list(qShowOffJpa);
		
	}
	
	/**
	 * 스토리 리스트 조회 
	 * TODO : 삭제예정
	 */
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
	
	
	/**
	 * 스토리 리스트 페이징 조회
	 */
	@Override
	public List<ShowOffResult> findByShowOffPagingV2(long mbrSeq,Pageable pageable) {
		
		QShowOffJpa qShowOffJpa = QShowOffJpa.showOffJpa;
		QShowOffReplyJpa qShowOffReplyJpa = QShowOffReplyJpa.showOffReplyJpa;
		QShowOffLikeJpa qShowOffLikeJpa = QShowOffLikeJpa.showOffLikeJpa;
		QMbrJpa qMbrJpa = QMbrJpa.mbrJpa;
		JPAQuery query = new JPAQuery(em);
		
		
		return  query.from(qShowOffJpa)
					.where(qShowOffJpa.mbrSeq.in(
													new JPASubQuery()
													.from(qMbrJpa)
													.where(qMbrJpa.mbrStatCd.eq(MbrEnum.StatCd.ACT.toString()))
													.list(qMbrJpa.mbrSeq))
					.and(qShowOffJpa.showOffStatCd.eq(ShowOffEnum.StatCd.ACT.toString()))
					)
					.orderBy(qShowOffJpa.regDt.desc())
					.limit(pageable.getPageSize())
					.offset(pageable.getOffset())
					.list(Projections.bean(ShowOffResult.class,qShowOffJpa.as("showOffJpa")
							,new JPASubQuery()
							.from(qShowOffReplyJpa)
							.where(qShowOffJpa.showOffSeq.eq(qShowOffReplyJpa.showOffSeq))
							.count().as("replyCnt")
							,new JPASubQuery()
							.from(qShowOffLikeJpa)
							.where(qShowOffJpa.showOffSeq.eq(qShowOffLikeJpa.showOffSeq))
							.count().as("likeCnt")
							,new JPASubQuery()
							.from(qShowOffLikeJpa)
							.where(qShowOffLikeJpa.mbrSeq.eq(mbrSeq).and(qShowOffJpa.showOffSeq.eq(qShowOffLikeJpa.showOffSeq)))
							.count().as("myLike")
						)
					);
	}
	
	/**
	 * 마이 스토리 리스트 페이징 조회
	 */
	@Override
	public List<ShowOffResult> findByMbrSeqPagingV2(long mbrSeq, Pageable pageable) {
		
		QShowOffJpa qShowOffJpa = QShowOffJpa.showOffJpa;
		QShowOffReplyJpa qShowOffReplyJpa = QShowOffReplyJpa.showOffReplyJpa;
		QShowOffLikeJpa qShowOffLikeJpa = QShowOffLikeJpa.showOffLikeJpa;
		JPAQuery query = new JPAQuery(em);
		
		return  query.from(qShowOffJpa)
				.where(qShowOffJpa.showOffStatCd.eq(ShowOffEnum.StatCd.ACT.toString())
				.and(qShowOffJpa.mbrSeq.eq(mbrSeq)))
				.orderBy(qShowOffJpa.regDt.desc())
				.limit(pageable.getPageSize())
				.offset(pageable.getOffset())
				.list(Projections.bean(ShowOffResult.class,qShowOffJpa.as("showOffJpa")
						,new JPASubQuery()
						.from(qShowOffReplyJpa)
						.where(qShowOffJpa.showOffSeq.eq(qShowOffReplyJpa.showOffSeq))
						.count().as("replyCnt")
						,new JPASubQuery()
						.from(qShowOffLikeJpa)
						.where(qShowOffJpa.showOffSeq.eq(qShowOffLikeJpa.showOffSeq))
						.count().as("likeCnt")
						,new JPASubQuery()
						.from(qShowOffLikeJpa)
						.where(qShowOffLikeJpa.mbrSeq.eq(mbrSeq).and(qShowOffJpa.showOffSeq.eq(qShowOffLikeJpa.showOffSeq)))
						.count().as("myLike")
					)
				);
		
	}

	/**
	 * 마이 스토리 리스트 페이징 조회 
	 * TODO : 삭제 예정
	 */
	@Override
	public List<ShowOffJpa> findByMbrSeq(long mbrSeq) {
		
		QShowOffJpa qShowOffJpa = QShowOffJpa.showOffJpa;
		JPAQuery query = new JPAQuery(em);
		return query.from(qShowOffJpa)
				.where(qShowOffJpa.mbrSeq.eq(mbrSeq))
				.orderBy(qShowOffJpa.regDt.desc())
				.list(qShowOffJpa);
		
	}
	
	/**
	 * 마이 스토리 리스트 페이징 조회 
	 * TODO : 삭제 예정
	 */
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
	
	@Override
	public void deleteShowOff(ShowOffJpa showOffJpa) {
		
		
	}


	
}
