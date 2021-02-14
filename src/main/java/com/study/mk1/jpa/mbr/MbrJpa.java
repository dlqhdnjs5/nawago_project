package com.study.mk1.jpa.mbr;


import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.study.mk1.abstracts.AbstractEntity;
import com.study.mk1.jpa.mbrAuthMapping.MbrAuthMappingJpa;
import com.study.mk1.jpa.mbrPetMapping.MbrPetMappingJpa;
import com.study.mk1.jpa.showOff.ShowOffJpa;
import com.study.mk1.jpa.showOffLike.ShowOffLikeJpa;
import com.study.mk1.jpa.showOffReply.ShowOffReplyJpa;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Entity
@Table(name = "mbr")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class  MbrJpa extends AbstractEntity{
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "mbr_seq", updatable = false, insertable = false)
	long mbrSeq;
	
	@Column(name = "mbr_id")
	String mbrId;
	
	@Column(name = "mbr_nm")
	String mbrNm;
	
	@Column(name = "mbr_nick_nm")
	String mbrNickNm;

	@JsonIgnore
	@Column(name = "mbr_pw")
	String mbrPw;

	@Column(name = "mbr_email")
	String mbrEmail;
	
	@JsonIgnore
	@Column(name = "mbr_stat_cd")
	String mbrStatCd;
	
	@Column(name = "mbr_tp_cd")
	String mbrTpCd;

	@Column(name = "mbr_mob_nation_no")
	String mbrMobNationNo;

	@Column(name = "mbr_mob_area_no")
	String mbrMobAreaNo;

	@Column(name = "mbr_mob_tlof_no")
	String mbrMobTlofNo;

	@Column(name = "mbr_mob_tlof_lst_no")
	String mbrMobTlofLstNo;

	@Column(name = "mbr_grd_cd")
	String mbrGrdCd;
	
	@Column(name = "mbr_rpst_img_url")
	String mbrRpstImgUrl;
	
	@Column(name = "mbr_rpst_img_nm")
	String mbrRpstImgNm;
	
	@JsonIgnore
	@OneToOne(fetch = FetchType.EAGER , mappedBy="mbrJpa")
	MbrAuthMappingJpa mam;
	
	@JsonIgnore
	@OneToMany(fetch = FetchType.LAZY,mappedBy = "mbrJpa")
	List<MbrPetMappingJpa> mbrPetMappingJpa = new ArrayList<MbrPetMappingJpa>();
	
	
	@JsonBackReference
	@OneToMany(fetch = FetchType.LAZY,mappedBy = "mbrJpa")
	List<ShowOffJpa> showOffJpa = new ArrayList<ShowOffJpa>();
	
	@JsonIgnore
	@OneToMany(fetch = FetchType.LAZY,mappedBy = "mbrJpa")
	List<ShowOffReplyJpa> showOffReplyJpa = new ArrayList<ShowOffReplyJpa>();
	
	@JsonIgnore
	@OneToMany(fetch = FetchType.LAZY,mappedBy = "mbrJpa")
	List<ShowOffLikeJpa> showOffLike = new ArrayList<ShowOffLikeJpa>();
	
}
