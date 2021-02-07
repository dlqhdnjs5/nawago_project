package com.study.mk1.data;

import java.util.List;

import com.study.mk1.abstracts.AbstractEntity;
import com.study.mk1.jpa.showOff.ShowOffJpa;
import com.study.mk1.jpa.showOffAttach.ShowOffAttachJpa;
import com.study.mk1.jpa.showOffReply.ShowOffReplyJpa;

import lombok.Data;

@Data
public class ShowOffInfoDTO extends AbstractEntity{

	private ShowOffJpa showOffjpa;
	
	private List<ShowOffAttachJpa> showOffAttachJpaList;
	
	private List<String> fileAttachList;
	
	private ShowOffReplyJpa showOffReplyJpa;
	
}
