package com.demo.model;

import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
public class PlaceOrder {

	@Valid
	@Size(min = 2, max = 2, message = "origin must have 2 items")
	private 
	List<
	@Valid
	@NotEmpty(message = "origin must not be empty")
	@Pattern(regexp = "[-+]?[0-9]*\\.?[0-9]+", message = "Invalid origin latitude / longitude format")
	String> origin;
	
	@Valid
	@Size(min = 2, max = 2, message = "destination must have 2 items")
	private 
	List<
	@Valid 
	@NotEmpty(message = "destination must not be empty")
	@Pattern(regexp = "[-+]?[0-9]*\\.?[0-9]+", message = "Invalid destination latitude / longitude format")
	String> destination;
	
}
