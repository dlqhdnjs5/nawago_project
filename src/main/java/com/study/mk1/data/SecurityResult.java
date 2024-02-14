package com.study.mk1.data;

import org.springframework.http.HttpStatus;

import lombok.Data;

@Data
public class SecurityResult {
	private HttpStatus code;
	private int status;
	private String message;
	private boolean isSuccess;
	private String prevPage;

}
