package com.study.mk1.common;

import java.util.Collection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.security.core.context.SecurityContext;
import com.study.mk1.controllers.DefaultController;
import com.study.mk1.enums.ShowOffAttachEnum;
import com.study.mk1.sequrity.SecurityUserDetails;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class IOService {
	
	private static Logger log = LoggerFactory.getLogger(IOService.class);
	
	/* 롤을 가지고 있는지 체크*/
	@SuppressWarnings("unchecked")
	public static boolean hasRole() {
		boolean hasRole = false;
		try {
			if (SecurityContextHolder.getContext() != null && SecurityContextHolder.getContext().getAuthentication() != null
					&& SecurityContextHolder.getContext().getAuthentication().getAuthorities() != null) {
				Collection<GrantedAuthority> authorities = (Collection<GrantedAuthority>) SecurityContextHolder.getContext()
						.getAuthentication().getAuthorities();

				for (GrantedAuthority authority : authorities) {
					hasRole = authority.getAuthority().equals("ROLE_USER");
					if (hasRole) {
						break;
					}
				}
			}
		} catch (Exception e) {
			log.info("=======================================");
			log.info("hasRole() error occour :: " + e.getMessage());
			log.info("=======================================");
		}
		return hasRole;
	}
	
	/* 스프링 시큐리티로 로그인된 사용자 세션정보를 얻는다. */
	public static Object getCurrentUserDetailPrincipal() {
		SecurityContext context = SecurityContextHolder.getContext();
		if (context == null || context.getAuthentication() == null) {
			return null;
		} else {
			return context.getAuthentication().getPrincipal();
		}
	}
	
	/* 스프링 시큐리티로 로그인된 사용자 세션정보를 얻는다. */
	public static SecurityUserDetails getCurrentUserDetail() {
		SecurityContext context = SecurityContextHolder.getContext();
		if (context == null || context.getAuthentication() == null) {
			return null;
		} else {
			return  (SecurityUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		}
	}
	
	public boolean checkImgFileExts(String fileUrl) {
		
		String extension = StringUtils.getFilenameExtension(fileUrl);
    	extension = extension.toLowerCase();
    	
    	String imgExts = "jpg,png,jpeg,pdf";
    	if(imgExts.contains(extension)) {
    		return true;
    	}else {
    		return false;
    	}
		
	}
	
	public String getFileNm(String fileUrl) {
		String fileName = "";
		try {
			if(!"".equals(fileUrl) ) {
				int slshIndex = fileUrl.lastIndexOf( "/" );
				fileName = fileUrl.substring( slshIndex + 1 );
			}else {
				return null;
			}
			
		}catch(Exception e) {
			log.info(this.getClass() + ".getFileNm [ERROR]");
		}
		return fileName;
	}
	
	public String getFileUrl(String fileUrl) {
		String url  = "";
		try {
			if(!"".equals(fileUrl) ) {
				int slshIndex = fileUrl.lastIndexOf( "/" );
				url = fileUrl.substring(0, slshIndex  );
			}else {
				return null;
			}
		}catch(Exception e){
			log.info(this.getClass() + ".getFileUrl [ERROR]");
		}
		return url;
	}
	
	public String getFileTpCd(String fileUrl) {
    	String extension = StringUtils.getFilenameExtension(fileUrl);
    	extension = extension.toLowerCase();
    	
    	String imgExts = "jpg,png,jpeg,pdf";
    	String movExts = "mp4,mov,avi,webm,ogg";
    	
    	if(movExts.contains(extension)) {
    		return ShowOffAttachEnum.TpCd.MOV.toString();
    	}else if(imgExts.contains(extension)) {
    		return ShowOffAttachEnum.TpCd.IMG.toString();
    	}else {
    		return null;
    	}
    	
    }

}
