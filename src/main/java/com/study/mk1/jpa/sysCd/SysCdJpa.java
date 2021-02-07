package com.study.mk1.jpa.sysCd;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.study.mk1.abstracts.AbstractEntity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "sys_cd")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class SysCdJpa extends AbstractEntity {
	
	@Id
	@Column(name = "cd", updatable = false, insertable = false)
	String cd;
	
	@Column(name = "upper_cd")
	String upperCd;
	
	@Column(name = "cd_nm")
	String cdNm;

}
