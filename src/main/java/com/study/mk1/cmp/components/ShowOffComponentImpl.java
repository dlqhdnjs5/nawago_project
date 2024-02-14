package com.study.mk1.cmp.components;

import org.springframework.stereotype.Component;

import com.study.mk1.cmp.services.ShowOffService;
import com.study.mk1.data.ShowOffInfoDTO;
import com.study.mk1.jpa.showOffLike.ShowOffLikeJpa;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@Component
@Slf4j
public class ShowOffComponentImpl implements ShowOffComponent{
	private final ShowOffService showOffService;
	
	@Override
	public void addShowOff(ShowOffInfoDTO dto) {
		showOffService.addShowOff(dto);
	}
	
	@Override
	public long addShowOffReply(ShowOffInfoDTO dto) {
		return showOffService.addShowOffReply(dto);
	}
	
	@Override
	public long updateShowOffLike(ShowOffLikeJpa dto) {
		return showOffService.updateShowOffLike(dto);
	}
	
	public void deleteShowOff(ShowOffInfoDTO dto) {
		showOffService.deleteShowOff(dto);
	}
}
