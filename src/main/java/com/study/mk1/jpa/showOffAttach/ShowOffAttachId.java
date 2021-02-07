package com.study.mk1.jpa.showOffAttach;

import javax.persistence.EmbeddedId;

import com.study.mk1.abstracts.AbstractEntity;

import lombok.Data;

@Data
public class ShowOffAttachId extends AbstractEntity{

	long showOffSeq;
	
	long showOffTurn;
}
