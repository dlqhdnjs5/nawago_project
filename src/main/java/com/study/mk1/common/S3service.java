package com.study.mk1.common;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.SdkClientException;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.study.mk1.config.GlobalPropertySource;

import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import java.io.IOException;

@Service
@NoArgsConstructor
public class S3service {
	
	private AmazonS3 s3Client;

	@Autowired
	GlobalPropertySource globalPropertySource;
	
    @PostConstruct
    public void setS3Client() {
        AWSCredentials credentials = new BasicAWSCredentials(globalPropertySource.getAccessKey(), globalPropertySource.getSecretKey());

        s3Client = AmazonS3ClientBuilder.standard()
                .withCredentials(new AWSStaticCredentialsProvider(credentials))
                .withRegion(globalPropertySource.getRegion())
                .build();
    }

    //s3 업로드  ex)uploadPath  == 'nawago/pet/petInfo/'
    public String upload(MultipartFile file,String uploadPath) throws IOException {
        String fileName = uploadPath+file.getOriginalFilename();
        s3Client.putObject(new PutObjectRequest(globalPropertySource.getBucket(), fileName, file.getInputStream(), null)
                .withCannedAcl(CannedAccessControlList.PublicRead));
        return s3Client.getUrl(globalPropertySource.getBucket(), fileName).toString();
    }
    
    public String uploadWithRandomFileNm(MultipartFile file,String uploadPath) throws IOException {
       
    	String ext = this.getFileExt(file);
        String randomStr = this.getRandomStr(11);
        String fileName = uploadPath+randomStr+ext;
        s3Client.putObject(new PutObjectRequest(globalPropertySource.getBucket(), fileName, file.getInputStream(), null)
                .withCannedAcl(CannedAccessControlList.PublicRead));
        return s3Client.getUrl(globalPropertySource.getBucket(), fileName).toString();
    }
    
    //s3 삭제  ex)uploadPath  == 'nawago/pet/petInfo/sss.jpg'
    public void delete(String fileName) {
        try {
            //Delete 객체 생성
            DeleteObjectRequest deleteObjectRequest = new DeleteObjectRequest(globalPropertySource.getBucket(), fileName);
            //Delete
            this.s3Client.deleteObject(deleteObjectRequest);
            System.out.println(String.format("[%s] deletion complete", fileName));

        } catch (AmazonServiceException e) {
            e.printStackTrace();
        } catch (SdkClientException e) {
            e.printStackTrace();
        }
    }
    
    private  String getRandomStr(int size) {
    	
		char[] tmp = new char[size];
		for(int i=0; i<tmp.length; i++) {
			int div = (int) Math.floor( Math.random() * 2 );
			
			if(div == 0) { // 0이면 숫자로
				tmp[i] = (char) (Math.random() * 10 + '0') ;
			}else { //1이면 알파벳
				tmp[i] = (char) (Math.random() * 26 + 'A') ;
			}
		}
		return new String(tmp);
	}
    
    private String getFileExt(MultipartFile file) {
    	String fileName = file.getOriginalFilename();
    	String extension = StringUtils.getFilenameExtension(fileName); 
    	return "."+extension;
    }

}
