package com.demo.client.api;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.demo.exception.OrderException;


@Service
public class GoogleDirectionsClient {
	
	@Value("${google.directions.key}")
	private String directionsKey;

	@Value("${google.directions.url}")
	private String directionsUrl;

	private final RestTemplate restTemplate;

	public GoogleDirectionsClient(RestTemplateBuilder builder) {
		this.restTemplate = builder.build();
	}

	public Integer getDistance(String origin, String destination) {
		UriComponentsBuilder uri = UriComponentsBuilder.fromHttpUrl(directionsUrl);
		
		MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
		params.add("origin", origin);
		params.add("destination", destination);
		params.add("key", directionsKey);
		uri.queryParams(params);
		
		JSONObject reponse = restTemplate.postForObject(uri.toUriString(), null, JSONObject.class);
		String status = (String) reponse.get("status");
		if (!"OK".equals(status)) {
			throw new OrderException("Map status: " + status);
		}
		return scrapeDistanceValue(reponse);
	}
	
	public Integer scrapeDistanceValue(JSONObject reponse) {
		List<?> routesList = (ArrayList<?>) reponse.get("routes");
		Map<?, ?> legs = (LinkedHashMap<?, ?>) routesList.get(0);
		List<?> legsList = (ArrayList<?>) legs.get("legs");
		Map<?, ?> legsMap = (LinkedHashMap<?, ?>) legsList.get(0);
		Map<?, ?> distance = (LinkedHashMap<?, ?>) legsMap.get("distance");
		return (Integer) distance.get("value");
	}

}
