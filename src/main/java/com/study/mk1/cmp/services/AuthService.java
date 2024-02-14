package com.study.mk1.cmp.services;

import org.springframework.stereotype.Service;

import com.study.mk1.jpa.mbr.MbrJpa;
import com.study.mk1.jpa.mbr.MbrJpaRepository;
import com.study.mk1.sequrity.SecurityUserDetails;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthService {
	private final MbrJpaRepository mbrJpaRepository;
	
	public SecurityUserDetails getSecurityUserDetails(String userId) {
		MbrJpa mbrJpa  = mbrJpaRepository.findByMbrId(userId);
		SecurityUserDetails userDetail = new SecurityUserDetails(mbrJpa,mbrJpa.getMam().getAuthJpa());
		
		return userDetail;
	}
}
