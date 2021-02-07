package com.study.mk1.cmp.components;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.study.mk1.cmp.services.ShowOffService;
import com.study.mk1.data.ShowOffInfoDTO;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
@Transactional
public class ShowOffComponentImpl implements ShowOffComponent{

	@Autowired
	ShowOffService showOffService;
	
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
	
	@Override
	public void addShowOffReply(ShowOffInfoDTO dto) throws Exception {
		try {
			showOffService.addShowOffReply(dto);
		}catch(Exception e){
			e.printStackTrace();
			log.error(this.getClass().getName() +".addShowOffReply() [ERROR] --> param : {}",dto);
			throw new RuntimeException(e);
		}
		
	}

	
	
}
