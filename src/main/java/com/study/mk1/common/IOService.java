package com.study.mk1.common;

import java.util.Collection;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.security.core.context.SecurityContext;
import com.study.mk1.controllers.DefaultController;
import com.study.mk1.enums.ShowOffAttachEnum;
import com.study.mk1.jpa.mbr.MbrJpa;
import com.study.mk1.sequrity.SecurityUserDetails;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class IOService {
	private final JwtTokenProvider jwtTokenProvider;
	private static final String ROLE_USER = "ROLE_USER";

	public static boolean hasRole() {
		boolean hasRole = false;
		if (SecurityContextHolder.getContext() != null && SecurityContextHolder.getContext().getAuthentication() != null
				&& SecurityContextHolder.getContext().getAuthentication().getAuthorities() != null) {
			Collection<GrantedAuthority> authorities = (Collection<GrantedAuthority>) SecurityContextHolder.getContext()
					.getAuthentication().getAuthorities();

			for (GrantedAuthority authority : authorities) {
				hasRole = authority.getAuthority().equals(ROLE_USER);
				if (hasRole) {
					break;
				}
			}
		}

		return hasRole;
	}

	public boolean checkImgFileExts(String fileUrl) {
		String extension = StringUtils.getFilenameExtension(fileUrl);
    	extension = extension.toLowerCase();
    	
    	String imgExts = "jpg,png,jpeg,pdf";
		return imgExts.contains(extension);
		
	}
	
	public String getFileNm(String fileUrl) {
		String fileName = "";

		if(StringUtils.isEmpty(fileUrl)) {
			return null;
		}

		int slshIndex = fileUrl.lastIndexOf( "/" );
		return fileUrl.substring( slshIndex + 1 );
	}
	
	public String getFileUrl(String fileUrl) {
		if(StringUtils.isEmpty(fileUrl)) {
			return null;
		}

		int slshIndex = fileUrl.lastIndexOf( "/" );
		return fileUrl.substring(0, slshIndex  );
	}
	
	public String getFileTpCd(String fileUrl) {
    	String extension = StringUtils.getFilenameExtension(fileUrl);
    	extension = extension.toLowerCase();
    	
    	String imgExts = "jpg,png,jpeg,pdf";
    	String movExts = "mp4,mov,avi,webm,ogg";
    	
    	if(movExts.contains(extension)) {
    		return ShowOffAttachEnum.TpCd.MOV.toString();
    	}

    	if (imgExts.contains(extension)) {
    		return ShowOffAttachEnum.TpCd.IMG.toString();
    	}

    	return null;
    }
	
	public MbrJpa getMbrInfoByRequest(HttpServletRequest req) {
		String token = jwtTokenProvider.resolveToken(req);
		if(StringUtils.isEmpty(token)) {
			return null;
		}

		boolean tokenValidYn = jwtTokenProvider.validateToken(token);
		if (!tokenValidYn) {
			return null;
		}

		return jwtTokenProvider.getUserInfo(token);
	}
	
	public boolean hasRoleByRequest(HttpServletRequest req) throws Exception {
		String token = jwtTokenProvider.resolveToken((HttpServletRequest) req);		
		boolean hasRole = false;
		if(!"".equals(token) && !"null".equals(token) ) {
			boolean tokenValidYn = jwtTokenProvider.validateToken(token);
			if(!tokenValidYn) {
				return false;
			}else {
				Authentication auth = jwtTokenProvider.getAuthentication(token);
					
				for (GrantedAuthority authority : auth.getAuthorities()) {
					hasRole = authority.getAuthority().equals("ROLE_USER");
					if (hasRole) {
						return true;
					}
				}
				return false;
			}
		}else {
			return false;
		}
		
	}

}
