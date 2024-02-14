package com.study.mk1.data;

import com.study.mk1.jpa.showOff.ShowOffJpa;

import lombok.Data;

@Data
public class ShowOffResult {
	private ShowOffJpa showOffJpa;
	long replyCnt;
	long likeCnt;
	long myLike;
}
