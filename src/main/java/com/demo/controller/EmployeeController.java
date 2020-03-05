/**
 * 
 */
package com.demo.controller;

import java.util.Arrays;

import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;

import com.demo.model.Employee;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * @author Mehul
**/

@Controller
public class EmployeeController {
	
	@RequestMapping(value = "/getEmployees", method = RequestMethod.GET)
    public String getEmployeeInfo() {
        return "get-employees";
    }
	
	@RequestMapping(value = "/showEmployees", method = RequestMethod.GET)
	public String showEmployees(@RequestParam("code") String code, Model m) 
			throws JsonMappingException, JsonProcessingException {
		
		ResponseEntity<String> response = null;
		System.out.println("auth code ------------- "+code);
		
		RestTemplate restTemplate = new RestTemplate();
		
		// According OAuth documentation we need to send the client id and secret key in the header for authentication
		String credentials = "client_id:client_secret";
		String encodedCredentials = new String(Base64.encodeBase64(credentials.getBytes()));
		
		//BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
		//String encodedCredentials = passwordEncoder.encode(credentials);
		
		HttpHeaders headers = new HttpHeaders();
		headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
		headers.add("Authorization", "Basic " + encodedCredentials);
		
		System.out.println("encoded credentials --------------- "+encodedCredentials);
		
		HttpEntity<String> request = new HttpEntity<String>(headers);
		
		String access_token_url = "http://localhost:8080/oauth/token";
		access_token_url += "?code=" + code;
		access_token_url += "&grant_type=authorization_code";
		access_token_url += "&redirect_uri=http://localhost:8090/showEmployees";
		
		System.out.println("access token url --------------- "+access_token_url);
		
		response = restTemplate.exchange(access_token_url, HttpMethod.POST, request, String.class);

		System.out.println("Access Token Response ---------" + response.getBody());
		
		//steps to Get the Access Token From the recieved JSON response
		
		ObjectMapper objectMapper = new ObjectMapper();
		JsonNode jsonNode = objectMapper.readTree(response.getBody());
		String token = jsonNode.path("access_token").asText();
		
		// URL for the resource that we actually want
		
		String url = "http://localhost:8080/user/getEmployeesList";

		// Use the access token for authentication
		
		HttpHeaders header = 	new HttpHeaders();
		header.add("Authorization", "Bearer "+token);
		HttpEntity<String> entity = new HttpEntity<String>(header);
		
		// request to Resource server to get the required resource
		
		ResponseEntity<Employee[]> employees = 
				restTemplate.exchange(url, HttpMethod.GET, entity, Employee[].class);
		
		System.out.println("employees ------------ "+employees);
		
		Employee[] employeesArray = employees.getBody();
		m.addAttribute("employees", Arrays.asList(employeesArray));

		return "show-employees";

	}
	
	
}
