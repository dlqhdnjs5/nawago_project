package com.study.mk1.cmp.components;

import org.springframework.stereotype.Component;

import com.study.mk1.data.ShowOffInfoDTO;

@Component
public interface ShowOffComponent {
	
	public void addShowOff(ShowOffInfoDTO dto) throws Exception;
	
	public void addShowOffReply(ShowOffInfoDTO dto) throws Exception;

}
