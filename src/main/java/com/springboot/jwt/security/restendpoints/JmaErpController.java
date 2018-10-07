package com.springboot.jwt.security.restendpoints;

import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.json.JSONObject;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/erp")
public class JmaErpController {

	@CrossOrigin(origins = "*")
	@RequestMapping(value = "/addBolb", method = RequestMethod.POST, consumes = { "application/json" }, 
	produces = { "application/json" })
	public String addBlob(@RequestBody String inputJson, HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		
		String entityKey = null;	
		long entityid = 0;
		
		
		JSONObject jsonObject = new JSONObject();
		try {
			Map<String, String> jsonMap = new HashMap<String, String>();
			
			final String entity_name = jsonMap.get("entity_name");
			final String imagestring = jsonMap.get("imagestring");
			entityKey = jsonMap.get("entity_key_id");
			
		
			
			
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		
		return jsonObject.toString();
		
				
		 
	}
	
}