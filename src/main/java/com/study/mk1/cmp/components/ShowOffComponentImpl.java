package com.study.mk1.cmp.components;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.study.mk1.cmp.services.ShowOffService;
import com.study.mk1.data.ShowOffInfoDTO;
import com.study.mk1.jpa.showOffLike.ShowOffLikeJpa;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
@Transactional
public class ShowOffComponentImpl implements ShowOffComponent{

	@Autowired
	ShowOffService showOffService;
	
	/**
	 * 스토리 저장
	 */
	@Override
	public void addShowOff(ShowOffInfoDTO dto) throws Exception {
		try {
			showOffService.addShowOff(dto);
		}catch(Exception e){
			e.printStackTrace();
			log.error(this.getClass().getName() +".addShowOff() [ERROR] --> param : {}",dto);
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * 스토리 댓글 저장
	 */
	@Override
	public long addShowOffReply(ShowOffInfoDTO dto) throws Exception {
		try {
			return showOffService.addShowOffReply(dto);
		}catch(Exception e){
			e.printStackTrace();
			log.error(this.getClass().getName() +".addShowOffReply() [ERROR] --> param : {}",dto);
			throw new RuntimeException(e);
		}
		
	}
	
	/**
	 * 스토리 좋아요 업데이트
	 */
	@Override
	public long updateShowOffLike(ShowOffLikeJpa dto) throws Exception {
		
		try {
			return showOffService.updateShowOffLike(dto);
		}catch(Exception e){
			e.printStackTrace();
			log.error(this.getClass().getName() +".addShowOffReply() [ERROR] --> param : {}",dto);
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * 스토리 삭제
	 */
	public void deleteShowOff(ShowOffInfoDTO dto) throws Exception{
		
		try {
			showOffService.deleteShowOff(dto);
		}catch(Exception e){
			e.printStackTrace();
			log.error(this.getClass().getName() +".updateShowOffLike() [ERROR] --> param : {}",dto);
			throw new RuntimeException(e);
		}
		
	}

	
	
}
