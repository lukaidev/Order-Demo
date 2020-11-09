package com.demo.client.api;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.json.simple.JSONObject;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.client.RestTemplate;


@ExtendWith(SpringExtension.class)
@SpringBootTest
@ActiveProfiles("test")
public class GoogleDirectionsClientTest {
	
	@Autowired
	GoogleDirectionsClient directionsClient;
	
	@MockBean
	RestTemplate restTemplate;
	
	@Test
	@DisplayName("Google directions api cliet")
	public void testGoogleDirectionsApiClient() throws Exception {
		
		JSONObject response = new JSONObject();
		
		GoogleDirectionsClient directionsClientSpy = Mockito.spy(directionsClient);
		Mockito.doReturn(response).when(restTemplate)
		.postForObject(ArgumentMatchers.any(), ArgumentMatchers.any(), ArgumentMatchers.any());
		
		Mockito.doReturn(100).when(directionsClientSpy).scrapeDistanceValue(ArgumentMatchers.any());
		
		Integer result = directionsClientSpy.getDistance("Testing", "Testing");
		
		assertEquals(100, result);
		
		
		
	}
	
	
}
