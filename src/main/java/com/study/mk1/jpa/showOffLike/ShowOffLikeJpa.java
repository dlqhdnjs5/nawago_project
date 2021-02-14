package com.study.mk1.jpa.showOffLike;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.study.mk1.abstracts.AbstractEntity;
import com.study.mk1.jpa.mbr.MbrJpa;
import com.study.mk1.jpa.mbrAuthMapping.MbrAuthMappingId;
import com.study.mk1.jpa.pet.PetJpa;
import com.study.mk1.jpa.showOff.ShowOffJpa;
import com.sun.istack.NotNull;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "show_off_like")
@Data
@IdClass(ShowOffLikeId.class)
public class ShowOffLikeJpa extends AbstractEntity{
	
	@Id
	@Column(name = "mbr_seq")
	long mbrSeq;
	
	@Id
	@Column(name = "show_off_seq")
	long showOffSeq;
	
	@ManyToOne(fetch = FetchType.LAZY,cascade = {CascadeType.MERGE, CascadeType.PERSIST})
	@JsonIgnore
	@JoinColumn(name = "mbr_seq" , updatable = false, insertable = false)
	MbrJpa mbrJpa;
	
	@ManyToOne(fetch = FetchType.LAZY , cascade = {CascadeType.MERGE, CascadeType.PERSIST})
	@JsonIgnore
	@JoinColumn(name="show_off_seq",updatable = false, insertable = false )
	ShowOffJpa showOffJpa;

}
