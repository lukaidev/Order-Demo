package com.demo.model;

import javax.validation.constraints.NotEmpty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TakeOrder {

	@NotEmpty
	private String status;
	
	public static TakeOrder success() {
		return TakeOrder.builder()
				.status("SUCCESS")
				.build();
	}

}
