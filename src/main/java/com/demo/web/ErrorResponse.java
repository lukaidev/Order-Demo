package com.demo.web;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ErrorResponse {
	
	private String error;
	
	public static ErrorResponse message(String message) {
		return ErrorResponse.builder()
				.error(message)
				.build();
	}
	
}
