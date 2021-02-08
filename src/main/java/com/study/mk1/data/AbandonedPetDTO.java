package com.study.mk1.data;

import com.study.mk1.abstracts.AbstractEntity;

import lombok.Data;

@Data
public class AbandonedPetDTO  extends AbstractEntity {
	
	
	/**
	 * 공고 종료일
	 */
	private String noticeEdt; 
	
	/**
	 * 이미지
	 */
	private String popfile;
	
	/**
	 * 상태
	 */
	private String processState; 
	
	/**
	 * 성별
	 */
	private String sexCd; 
	
	/**
	 * 중성화 여부
	 */
	private String neuterYn;
	
	/**
	 * 특징
	 */
	private String specialMark;
	
	/**
	 * 보호소 이름
	 */
	private String careNm;
	
	/**
	 * 보호소 전화번호
	 */
	private String careTel;
	
	/**
	 * 보호 장소
	 */
	private String careAddr;
	
	/**
	 * 담당자
	 */
	private String chargeNm;
	
	/**
	 * 담당자 연락처
	 */
	private String officetel;
	
	/**
	 * 특이사항
	 */
	private String noticeComment;
	
	/**
	 * 유기번호
	 */
	private String desertionNo;
	
	/**
	 * 발견장소
	 */
	private String happenPlace;
	
	/**
	 * 품종
	 */
	private String kindCd;
	
	/**
	 * 색상
	 */
	private String colorCd;
	
	/**
	 * 나이
	 */
	private String age;
	
	/**
	 * 체중
	 */
	private String weight;
	
	/**
	 * 공고 시작일
	 */
	private String noticeSdt;
	

}
