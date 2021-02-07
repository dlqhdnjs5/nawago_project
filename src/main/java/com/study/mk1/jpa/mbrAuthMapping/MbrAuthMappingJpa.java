package com.study.mk1.jpa.mbrAuthMapping;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.study.mk1.abstracts.AbstractEntity;
import com.study.mk1.entity.Auth;
import com.study.mk1.entity.Mbr;
import com.study.mk1.jpa.auth.AuthJpa;
import com.study.mk1.jpa.mbr.MbrJpa;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "mbr_auth_mapping")
@AllArgsConstructor
@NoArgsConstructor
@Data
@IdClass(MbrAuthMappingId.class)
public class MbrAuthMappingJpa extends AbstractEntity{
	
	/*@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "mbr_auth_seq", updatable = false, insertable = false)
	private long mbrAuthSeq;*/
	
	@Id
	@Column(name = "mbr_seq")
	private long mbrSeq;
	
	@Id
	@Column(name = "auth_seq")
	private long authSeq;
	
	
	@JsonBackReference
	@OneToOne(fetch = FetchType.LAZY,cascade = CascadeType.ALL)
	@JoinColumn(name = "mbr_seq" , updatable = false, insertable = false)
	MbrJpa mbrJpa;
	
	@ManyToOne(fetch = FetchType.EAGER,cascade = CascadeType.ALL)
	@JoinColumn(name = "auth_seq" , updatable = false, insertable = false)
	AuthJpa authJpa;
	
}
