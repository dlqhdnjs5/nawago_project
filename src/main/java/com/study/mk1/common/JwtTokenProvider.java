package com.study.mk1.common;

import java.util.Base64;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

import com.study.mk1.config.GlobalPropertySource;
import com.study.mk1.jpa.mbr.MbrJpa;
import com.study.mk1.jpa.mbr.MbrJpaRepository;
import com.study.mk1.sequrity.SecurityUserDetailService;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Component
public class JwtTokenProvider {
	
	//TODO : 설정파일로 뺄것
	private  String SECRETKEY;
	private  long EXPIRATION_MS = 1000 * 60 * 60 ; 
	
	private final UserDetailsService userDetailsService;
	
	@Autowired
	private SecurityUserDetailService securityUserDetailService;
	
	@Autowired
	private MbrJpaRepository mbrJpaRepository;
	
	@Autowired
	GlobalPropertySource gp;
	
	 // 객체 초기화, secretKey를 Base64로 인코딩한다.
	@PostConstruct
	protected void init() {	
		SECRETKEY = Base64.getEncoder().encodeToString(gp.getSecretKey().getBytes());
	}

	 // JWT 토큰 생성
	public String createToken(String userPk, Collection<? extends GrantedAuthority> roles) {
		Claims claims = Jwts.claims().setSubject(userPk); // JWT payload 에 저장되는 정보단위
		claims.put("roles", roles); // 정보는 key / value 쌍으로 저장된다.
		Date now = new Date();
		
		return Jwts.builder()
		.setClaims(claims) // 정보 저장
		.setIssuedAt(now) // 토큰 발행 시간 정보
		.setExpiration(new Date(now.getTime() + EXPIRATION_MS)) 
		.signWith(SignatureAlgorithm.HS256, SECRETKEY) 
		.compact();
	}
	
	// JWT 토큰에서 인증 정보 조회
	public Authentication getAuthentication(String token) {
		UserDetails userDetails = securityUserDetailService.loadUserByUsername(this.getUserPk(token));
		return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
	}
	
	// JWT 토큰에서 인증 정보 조회
	public Authentication getAuthenticationById(String id) {
		UserDetails userDetails = securityUserDetailService.loadUserByUsername(id);
		return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
	}

	 // 토큰에서 회원 정보 추출
	public String getUserPk(String token) {
		return Jwts.parser().setSigningKey(SECRETKEY).parseClaimsJws(token).getBody().getSubject();
	}
	
	public MbrJpa getUserInfo(String token) {
		String mbrId = Jwts.parser().setSigningKey(SECRETKEY).parseClaimsJws(token).getBody().getSubject();
		return  mbrJpaRepository.findByMbrId(mbrId);
	}
	
	public MbrJpa getFamilyInfo(String token) {
		String mbrId = Jwts.parser().setSigningKey(SECRETKEY).parseClaimsJws(token).getBody().getSubject();
		return  mbrJpaRepository.findByMbrId(mbrId);
	}
	
	public String resolveToken(HttpServletRequest request) {
		return request.getHeader(gp.getAuthHeader());
	}

    // 토큰의 유효성 + 만료일자 확인
	public boolean validateToken(String jwtToken) {
		try {
			Jws<Claims> claims = Jwts.parser().setSigningKey(SECRETKEY).parseClaimsJws(jwtToken);
			return !claims.getBody().getExpiration().before(new Date());
		} catch (Exception e) {
			return false;
		}
	}
}
