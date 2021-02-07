package com.study.mk1.jpa.showOffReply;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.study.mk1.abstracts.AbstractEntity;
import com.study.mk1.jpa.mbr.MbrJpa;
import com.study.mk1.jpa.showOff.ShowOffJpa;
import com.sun.istack.NotNull;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "show_off_reply")
@Data
public class ShowOffReplyJpa extends AbstractEntity{
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "show_off_reply_seq", updatable = false , insertable = false)
	long showOffReplySeq;
	
	@NotNull
	@Column(name = "mbr_seq")
	long mbrSeq;
	
	@NotNull
	@Column(name = "show_off_seq" )
	long showOffSeq;
	
	@NotNull
	@Column(name = "reply_cont")
	String replyCont;
	
	@Column(name = "reg_dt", updatable = false, insertable = false)
	String regDt; 
	
	@Column(name = "udt_dt", updatable = false, insertable = false)
	String udtDt;
	
	@ManyToOne(fetch = FetchType.EAGER,cascade = CascadeType.ALL)
	@JoinColumn(name = "mbr_seq" , updatable = false, insertable = false)
	MbrJpa mbrJpa;
	
	
	/*@ManyToOne(fetch = FetchType.LAZY,cascade = CascadeType.ALL)
	@JoinColumn(name = "show_off_seq" , updatable = false, insertable = false)
	ShowOffJpa showOffJpa;*/

}
