package com.study.mk1.data;

public class PetEnum {
	public enum petStatCd {
		
		ACT, //사용중
		ABNDN, //유기
		LOST,//실종
		SCSI;
	}
	
	private final String text;
	private PetEnum(final String text) {
		this.text = text;
	}

	@Override
	public String toString() {
		return text;
	}
}
