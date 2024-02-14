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
	private String SECRETKEY;
	private long EXPIRATION_MS = 1000 * 60 * 60 ;

	private final SecurityUserDetailService securityUserDetailService;
	private final MbrJpaRepository mbrJpaRepository;
	private final GlobalPropertySource gp;
	
	@PostConstruct
	protected void init() {	
		SECRETKEY = Base64.getEncoder().encodeToString(gp.getSecretKey().getBytes());
	}

	public String createToken(String userPk, Collection<? extends GrantedAuthority> roles) {
		Claims claims = Jwts.claims().setSubject(userPk);
		claims.put("roles", roles);
		Date now = new Date();
		
		return Jwts.builder()
		.setClaims(claims)
		.setIssuedAt(now)
		.setExpiration(new Date(now.getTime() + EXPIRATION_MS)) 
		.signWith(SignatureAlgorithm.HS256, SECRETKEY) 
		.compact();
	}
	
	public Authentication getAuthentication(String token) {
		UserDetails userDetails = securityUserDetailService.loadUserByUsername(this.getUserPk(token));
		return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
	}

	public String getUserPk(String token) {
		return Jwts.parser().setSigningKey(SECRETKEY).parseClaimsJws(token).getBody().getSubject();
	}
	
	public MbrJpa getUserInfo(String token) {
		String mbrId = Jwts.parser().setSigningKey(SECRETKEY).parseClaimsJws(token).getBody().getSubject();
		return  mbrJpaRepository.findByMbrId(mbrId);
	}
	
	public String resolveToken(HttpServletRequest request) {
		return request.getHeader(gp.getAuthHeader());
	}

	public boolean validateToken(String jwtToken) {
		try {
			Jws<Claims> claims = Jwts.parser().setSigningKey(SECRETKEY).parseClaimsJws(jwtToken);
			return !claims.getBody().getExpiration().before(new Date());
		} catch (Exception e) {
			return false;
		}
	}
}
