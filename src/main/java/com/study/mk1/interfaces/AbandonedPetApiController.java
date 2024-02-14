package com.study.mk1.interfaces;

import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.study.mk1.config.GlobalPropertySource;
import com.study.mk1.data.AbandonedPetDTO;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@RequestMapping("/api/abandoned")
@RestController
@Slf4j
public class AbandonedPetApiController {
	private final GlobalPropertySource gp;
	private static String KEY;
	private static String SERVICE_URL;
	private static final String pagePerView = "8";
	
	
	@PostConstruct
	protected void init() {	
		SERVICE_URL = gp.getAbandonedApiServerUrl();
		KEY = gp.getAbandonedApiKey();
	}
	
	@RequestMapping("/getAbandonedPetInfo")
	public ResponseEntity<List<AbandonedPetDTO>> getAbandonedPetInfo(@RequestParam(value="page") String pageNo) {
		List<AbandonedPetDTO> abandonedPetDTOList = new ArrayList<>();

		try {
			StringBuilder urlBuilder = new StringBuilder(SERVICE_URL);
			urlBuilder.append("?" + URLEncoder.encode("ServiceKey","UTF-8") + "=" + KEY);
			urlBuilder.append("&" + URLEncoder.encode("numOfRows","UTF-8") + "=" + pagePerView);
			urlBuilder.append("&" + URLEncoder.encode("pageNo","UTF-8") + "=" + pageNo);
			URL url = new URL(urlBuilder.toString());
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();

			Document doc = dBuilder.parse(url.toString());
			doc.getDocumentElement().normalize();
			NodeList nList = doc.getElementsByTagName("item");

			for (int index = 0; index < nList.getLength(); index++) {
				Node nNode = nList.item(index);
				if(nNode.getNodeType() == Node.ELEMENT_NODE){
					AbandonedPetDTO abandonedPetDTO = new AbandonedPetDTO();
					Element eElement = (Element) nNode;

					abandonedPetDTO.setNoticeEdt(getTagValue("noticeEdt", eElement));
					abandonedPetDTO.setNoticeSdt(getTagValue("noticeSdt", eElement));
					abandonedPetDTO.setPopfile(getTagValue("popfile", eElement));
					abandonedPetDTO.setProcessState(getTagValue("processState", eElement));
					abandonedPetDTO.setSexCd(getTagValue("sexCd", eElement));
					abandonedPetDTO.setNeuterYn(getTagValue("neuterYn", eElement));
					abandonedPetDTO.setSpecialMark(getTagValue("specialMark", eElement));
					abandonedPetDTO.setCareNm(getTagValue("careNm", eElement));
					abandonedPetDTO.setCareTel(getTagValue("careTel", eElement));
					abandonedPetDTO.setCareAddr(getTagValue("careAddr", eElement));
					abandonedPetDTO.setChargeNm(getTagValue("chargeNm", eElement));
					abandonedPetDTO.setOfficetel(getTagValue("officetel", eElement));
					abandonedPetDTO.setNoticeComment(getTagValue("noticeComment", eElement));
					abandonedPetDTO.setDesertionNo(getTagValue("desertionNo", eElement));
					abandonedPetDTO.setHappenPlace(getTagValue("happenPlace", eElement));
					abandonedPetDTO.setKindCd(getTagValue("kindCd", eElement));
					abandonedPetDTO.setColorCd(getTagValue("colorCd", eElement));
					abandonedPetDTO.setAge(getTagValue("age", eElement));
					abandonedPetDTO.setWeight(getTagValue("weight", eElement));
					abandonedPetDTOList.add(abandonedPetDTO);
				}
			}
        
        } catch (Exception exception) {
        	log.error("error occured while getAbandonedPetInfo", exception);
        	return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<>(abandonedPetDTOList, HttpStatus.OK);
	}
	
	private static String getTagValue(String tag, Element eElement) {
		Node node = eElement.getElementsByTagName(tag).item(0);

		if (node == null) {
			return null;
		}

	    NodeList nlList = eElement.getElementsByTagName(tag).item(0).getChildNodes();
	    Node nValue = (Node) nlList.item(0);
	    if (nValue == null)  return null;
	    return nValue.getNodeValue();
	}
}
