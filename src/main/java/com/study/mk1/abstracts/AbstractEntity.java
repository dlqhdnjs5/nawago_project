package com.study.mk1.abstracts;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.io.Serializable;
import java.lang.*;

import javax.persistence.Column;

import org.springframework.util.StringUtils;

import com.study.mk1.enums.ShowOffAttachEnum;

@Data
public class AbstractEntity implements Serializable {
	@Column(name = "regter_id", updatable = false, insertable = false)
	String regterId;

	@Column(name = "reg_dt", updatable = false, insertable = false)
	String regDt;

	@Column(name = "udter_id", updatable = false, insertable = false)
	String udterId;

	@Column(name = "udt_dt", updatable = false, insertable = false)
	String udtDt;

	String fileName;
	String attachFileUrl;
	
}
