package com.study.mk1.jpa.showOff;

import java.util.ArrayList;
import java.util.List;

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
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.study.mk1.abstracts.AbstractEntity;
import com.study.mk1.jpa.mbr.MbrJpa;
import com.study.mk1.jpa.mbrAuthMapping.MbrAuthMappingJpa;
import com.study.mk1.jpa.mbrPetMapping.MbrPetMappingId;
import com.study.mk1.jpa.mbrPetMapping.MbrPetMappingJpa;
import com.study.mk1.jpa.showOffAttach.ShowOffAttachId;
import com.study.mk1.jpa.showOffAttach.ShowOffAttachJpa;
import com.study.mk1.jpa.showOffLike.ShowOffLikeJpa;
import com.study.mk1.jpa.showOffReply.ShowOffReplyJpa;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "show_off")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class ShowOffJpa extends AbstractEntity{
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "show_off_seq", updatable = false, insertable = false)
	long showOffSeq;
	
	@Column(name = "mbr_seq")
	long mbrSeq;
	
	@Column(name = "show_off_tp_cd")
	String showOffTpCd;
	
	@Column(name = "show_off_stat_cd")
	String showOffStatCd;
	
	@Column(name = "show_off_cont")
	String showOffCont;
	
	@Column(name = "regter_id", updatable = false, insertable = false)
	String regterId;
	
	@Column(name = "reg_dt", updatable = false, insertable = false)
	String regDt; 
	
	@Column(name = "udter_id", updatable = false, insertable = false)
	String udterId;
	
	@Column(name = "udt_dt", updatable = false, insertable = false)
	String udtDt;
	
	
	@ManyToOne(fetch = FetchType.EAGER,cascade = CascadeType.ALL)
	@JoinColumn(name = "mbr_seq" , updatable = false, insertable = false)
	MbrJpa mbrJpa;
	
	@OneToMany(fetch = FetchType.EAGER,mappedBy = "showOffJpa")
	List<ShowOffAttachJpa> showOffAttachJpa = new ArrayList<ShowOffAttachJpa>();
	
	@JsonIgnore
	@OneToMany(fetch = FetchType.LAZY,mappedBy = "showOffJpa")
	List<ShowOffLikeJpa> showOffLike = new ArrayList<ShowOffLikeJpa>();
	
	/*@OneToMany(fetch = FetchType.LAZY )
	@JoinColumn(name="show_off_seq")
	List<ShowOffReplyJpa> showOffReplyJpa = new ArrayList<ShowOffReplyJpa>();*/

}
