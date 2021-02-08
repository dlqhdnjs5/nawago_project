package com.study.mk1.interfaces;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.study.mk1.data.AbandonedPetDTO;
import com.study.mk1.jpa.showOffReply.ShowOffReplyJpa;

import lombok.extern.slf4j.Slf4j;

@RequestMapping("/api/abandoned")
@RestController
@Slf4j
public class AbandonedPetApiController {
	
	private static final String key = "dmvr4zhgUd1LgEmKvbN%2BME8PEOBi1O2WlWtocqqgHwuNrTrOedgWIhUfD8EOrptDCqpRMn0VfDWRQ8qSfSqJqA%3D%3D";

	private static final String serviceUrl = "http://openapi.animal.go.kr/openapi/service/rest/abandonmentPublicSrvc/abandonmentPublic";
	
	private static final String pagePerView = "8";
	
	
	@RequestMapping("/getAbandonedPetInfo")
	public ResponseEntity<List<AbandonedPetDTO>> getAbandonedPetInfo(HttpServletRequest rq, HttpServletResponse rs,@RequestParam(value="page") String pageNo) throws Exception {
	
        
        List<Map<String,String>> resultList  = new ArrayList<>();
        StringBuilder urlBuilder = new StringBuilder(serviceUrl); 
        urlBuilder.append("?" + URLEncoder.encode("ServiceKey","UTF-8") + "="+key);
        urlBuilder.append("&" + URLEncoder.encode("numOfRows","UTF-8") + "="+pagePerView);
        urlBuilder.append("&" + URLEncoder.encode("pageNo","UTF-8") + "="+pageNo);
        URL url = new URL(urlBuilder.toString());

        DocumentBuilderFactory dbFactoty = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbFactoty.newDocumentBuilder();
        Document doc = dBuilder.parse(url.toString());
        
        doc.getDocumentElement().normalize();
        NodeList nList = doc.getElementsByTagName("item");
        System.out.println("파싱할 리스트 수 : "+ nList.getLength()); 
        
        List<AbandonedPetDTO> dto = new ArrayList<>();
        
        
        for(int temp = 0; temp < nList.getLength(); temp++){		
        	Node nNode = nList.item(temp);
        	if(nNode.getNodeType() == Node.ELEMENT_NODE){
        		AbandonedPetDTO obj = new AbandonedPetDTO();
        		Map result = new HashMap();
        		
        		Element eElement = (Element) nNode;
        		
        		log.info("날짜 : " +getTagValue("age", eElement));
        		log.info("시도명 : " +getTagValue("careAddr", eElement));
        		log.info("확진자수 : "  +getTagValue("careNm", eElement));
        		log.info("사망자수 : "+getTagValue("careTel", eElement));
        		log.info("격리자 수 : "  +getTagValue("chargeNm", eElement));
        		log.info("격리 해제 수 : "  +getTagValue("colorCd", eElement));
        		log.info("전일대비 증감수 : "+getTagValue("desertionNo", eElement));
        		log.info("##############################");
        		
        		obj.setNoticeEdt(getTagValue("noticeEdt", eElement));
        		obj.setNoticeSdt(getTagValue("noticeSdt", eElement));
        		obj.setPopfile(getTagValue("popfile", eElement));
        		obj.setProcessState(getTagValue("processState", eElement));
        		obj.setSexCd(getTagValue("sexCd", eElement));
        		obj.setNeuterYn(getTagValue("neuterYn", eElement));
        		obj.setSpecialMark(getTagValue("specialMark", eElement));
        		obj.setCareNm(getTagValue("careNm", eElement));
        		obj.setCareTel(getTagValue("careTel", eElement));
        		obj.setCareAddr(getTagValue("careAddr", eElement));
        		obj.setChargeNm(getTagValue("chargeNm", eElement));
        		obj.setOfficetel(getTagValue("officetel", eElement));
        		obj.setNoticeComment(getTagValue("noticeComment", eElement));
        		obj.setDesertionNo(getTagValue("desertionNo", eElement));
        		obj.setHappenPlace(getTagValue("happenPlace", eElement));
        		obj.setKindCd(getTagValue("kindCd", eElement));
        		obj.setColorCd(getTagValue("colorCd", eElement));
        		obj.setAge(getTagValue("age", eElement));
        		obj.setWeight(getTagValue("weight", eElement));
        		
        		dto.add(obj);
        		
    
        	}	
        }
        
        return new ResponseEntity<List<AbandonedPetDTO>>(dto,HttpStatus.OK);
        
	}
	
	private static String getTagValue(String tag, Element eElement) {
		Node node = eElement.getElementsByTagName(tag).item(0);
		if(node == null) {
			return null;
		}
	    NodeList nlList = eElement.getElementsByTagName(tag).item(0).getChildNodes();
	    Node nValue = (Node) nlList.item(0);
	    if(nValue == null) 
	        return null;
	    return nValue.getNodeValue();
	}

}
