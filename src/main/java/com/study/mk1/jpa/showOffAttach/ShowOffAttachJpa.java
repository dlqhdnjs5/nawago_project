package com.study.mk1.jpa.showOffAttach;

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
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.study.mk1.abstracts.AbstractEntity;
import com.study.mk1.jpa.mbr.MbrJpa;
import com.study.mk1.jpa.showOff.ShowOffJpa;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "show_off_attach")
@AllArgsConstructor
@NoArgsConstructor
@Data
@IdClass(ShowOffAttachId.class)
public class ShowOffAttachJpa extends AbstractEntity{

	@Id
	@Column(name = "show_off_seq", updatable = false)
	long showOffSeq;
	
	@Id
	@Column(name = "show_off_turn")
	long showOffTurn;	
	
	@Column(name = "show_off_attach_url")
	String showOffAttachUrl;
	
	@Column(name = "show_off_attach_nm")
	String showOffAttachNm;
	
	@Column(name = "show_off_attach_tp_cd")
	String showOffAttachTpCd;
	
	@ManyToOne(fetch = FetchType.LAZY,cascade = CascadeType.ALL)
	@JsonIgnore
	@JoinColumn(name = "show_off_seq" , updatable = false, insertable = false)
	ShowOffJpa  showOffJpa;
}
