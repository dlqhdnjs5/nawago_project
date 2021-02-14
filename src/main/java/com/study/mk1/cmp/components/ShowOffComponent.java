package com.study.mk1.cmp.components;

import org.springframework.stereotype.Component;

import com.study.mk1.data.ShowOffInfoDTO;
import com.study.mk1.jpa.showOffLike.ShowOffLikeJpa;

@Component
public interface ShowOffComponent {
	/**
	 * 스토리 저장
	 */
	public void addShowOff(ShowOffInfoDTO dto) throws Exception;
	/**
	 * 스토리 댓글 저장
	 */
	public long addShowOffReply(ShowOffInfoDTO dto) throws Exception;
	/**
	 * 스토리 좋아요 업데이트
	 */
	public long updateShowOffLike(ShowOffLikeJpa dto) throws Exception;

}
