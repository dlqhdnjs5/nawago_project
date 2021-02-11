package com.study.mk1.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;

@Configuration
@PropertySources({
	@PropertySource(value = "file:/etc/tomcat9/properties/config.properties",encoding="utf-8",ignoreResourceNotFound = true),
	@PropertySource(value = "/config.properties",ignoreResourceNotFound = true)
})
//1순위 서버
//2순위 클래스패스

public class GlobalPropertySource {
	
	/*Database resources*/
	@Value("${spring.datasource.driverClassName}")
    private String driverClassName;
    
    @Value("${spring.datasource.url}")
    private String url;
    
    @Value("${spring.datasource.username}")
    private String username;
    
    @Value("${spring.datasource.password}")
    private String password;
    
    /*AWS S3*/
    @Value("${cloud.aws.credentials.access-key}")
    private String accessKey;

    @Value("${cloud.aws.credentials.secret-key}")
    private String secretKey;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

	@Value("${cloud.aws.region.static}")
    private String region;
    
	@Value("${sec.web.token.key}")
    private String secretWebTokenKey;
	
	@Value("${auth.header}")
    private String authHeader;
    
    /*base*/
    @Value("${base.ip}")
    private String baseIp;
    
    
    
    /**
     * email 
     */
    @Value("${spring.mail.host}")
    private String mailHost;
    
    @Value("${spring.mail.port}")
    private String mailPort;
    
    @Value("${spring.mail.username}")
    private String mailId;
    
    @Value("${spring.mail.password}")
    private String mailPw;
    
    /**
     * abandoned
     * @return
     */
    
    @Value("${abandoned.api.key}")
    private String abandonedApiKey;
    
    public String getAbandonedApiKey() {
		return abandonedApiKey;
	}

	public String getAbandonedApiServerUrl() {
		return abandonedApiServerUrl;
	}

	@Value("${abandoned.api.server.url}")
    private String abandonedApiServerUrl;
    
    
    public String getMailHost() {
		return mailHost;
	}

	public String getMailPort() {
		return mailPort;
	}

	public String getMailId() {
		return mailId;
	}

	public String getMailPw() {
		return mailPw;
	}

	public String getAccessKey() {
		return accessKey;
	}

	public String getSecretKey() {
		return secretKey;
	}

	public String getBucket() {
		return bucket;
	}

	public String getRegion() {
		return region;
	}

	public String getDriverClassName() {
        return driverClassName;
    }
 
    public String getUrl() {
        return url;
    }
 
    public String getUsername() {
        return username;
    }
 
    public String getPassword() {
        return password;
    }

    public String getBaseIp() {
    	return baseIp;
    }
    
    public String getBaseUri() {
    	return "http://"+baseIp;
    }
    
    public String getSecretWebTokenKey() {
  		return secretWebTokenKey;
  	}
    
    public String getAuthHeader() {
		return authHeader;
	}


}
