package hk.metis.jsonreportreader.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
public class RestfulController {

	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/getResult", method = RequestMethod.POST)
	public List<String> getResult(@RequestBody Map<String, Object> resultMap){
		Map<String, String> credential = (Map<String, String>) resultMap.get("credential");
		List<String> urls = (List<String>) resultMap.get("urls");
		HttpHeaders headers = new HttpHeaders();
		if(credential.get("username") != null) {
			String plainCreds = credential.get("username") + ":" + credential.get("password");
			byte[] plainCredsBytes = plainCreds.getBytes();
			byte[] base64CredsBytes = Base64.encodeBase64(plainCredsBytes);
			String base64Creds = new String(base64CredsBytes);
			headers.add("Authorization", "Basic " + base64Creds);
		}
		List<String> resultList = new ArrayList<String>();
		headers.setContentType(MediaType.APPLICATION_JSON);
		RestTemplate restTemplate = new RestTemplate();
		HttpEntity<String> request = new HttpEntity<String>(headers);
		for(String url : urls){
			System.out.println(url);
			ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, request, String.class);
			resultList.add(response.getBody());
		}
		return resultList;
	}
}
