package com.demo.web.controller;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import com.demo.app.service.OrderRequestService;
import com.demo.exception.OrderException;
import com.demo.model.OrderRequest;
import com.demo.model.PlaceOrder;
import com.demo.model.TakeOrder;
import com.fasterxml.jackson.databind.ObjectMapper;

import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.doReturn;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class OrderControllerTest {

	@Autowired
	private MockMvc mockMvc;
	
	@MockBean
	OrderRequestService orderRequestService;
	
	PlaceOrder placeOrder;
	TakeOrder takeOrder;
	
	@BeforeEach
	public void init() {
		placeOrder = PlaceOrder.builder()
				.origin(Arrays.asList(new String [] { "14.7002409", "121.08754623" }))
				.destination(Arrays.asList(new String [] { "14.5568853", "121" }))
				.build();
		
		takeOrder = TakeOrder.builder()
				.status("TAKEN")
				.build();
	}
	
	@Test
	@DisplayName("Place Order - POST /orders")
	public void testPlaceOrder() throws Exception {
		
		// Mock Data
		OrderRequest mockResponse = OrderRequest.builder()
				.id(1)
				.distance(100)
				.status("SAMPLE_STAT")
				.build();
		// Prepare mock service method
		doReturn(mockResponse).when(orderRequestService).placeOrder(placeOrder);
		
		// Perform POST request
		mockMvc.perform(post("/orders")
				.contentType(MediaType.APPLICATION_JSON_VALUE)
				.content(new ObjectMapper().writeValueAsString(placeOrder)))
		
		// Validate 200 OK and JSON response type received
		.andExpect(status().isOk())
		.andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
		
		// Validate response body
		.andExpect(jsonPath("$.id", is(1)))
		.andExpect(jsonPath("$.distance", is(100)))
		.andExpect(jsonPath("$.status", is("SAMPLE_STAT")));
		
	}
	
	@Test
	@DisplayName("Place Order Handle OrderException - POST /orders")
	public void testPlaceOrderHandleOrderException() throws Exception {
		
		// Prepare mock service method
		Mockito.doThrow(new OrderException("Test Exception")).when(orderRequestService).placeOrder(placeOrder);
		
		// Perform POST request
		mockMvc.perform(post("/orders")
				.contentType(MediaType.APPLICATION_JSON_VALUE)
				.content(new ObjectMapper().writeValueAsString(placeOrder)))
				
		// Validate 400 BAD REQUEST and JSON response type received
		.andExpect(status().isBadRequest())
		.andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
				
		// Validate response body
		.andExpect(jsonPath("$.error", is("Test Exception")));
	}
	
	@Test
	@DisplayName("Place Order invalid origin format - POST /orders")
	public void testPlaceOrderInvalidOriginFomat() throws Exception {
		
		placeOrder = PlaceOrder.builder()
				.origin(Arrays.asList(new String [] { "START_LATITUDE", "START_LONGITUDE" }))
				.destination(Arrays.asList(new String [] { "14.5568853", "121.023532" }))
				.build();
		
		// Prepare mock service method
		Mockito.doThrow(new OrderException("Test Exception")).when(orderRequestService).placeOrder(placeOrder);
		
		// Perform POST request
		mockMvc.perform(post("/orders")
				.contentType(MediaType.APPLICATION_JSON_VALUE)
				.content(new ObjectMapper().writeValueAsString(placeOrder)))
				
		// Validate 400 BAD REQUEST and JSON response type received
		.andExpect(status().isBadRequest())
		.andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
				
		// Validate response body
		.andExpect(jsonPath("$.error", is("Invalid origin latitude / longitude format")));
	}
	
	@Test
	@DisplayName("Place Order invalid origin list size - POST /orders")
	public void testPlaceOrderInvalidOriginListSize() throws Exception {
		
		placeOrder = PlaceOrder.builder()
				.origin(Arrays.asList(new String [] { "14.7002409" }))
				.destination(Arrays.asList(new String [] { "14.5568853", "121.023532" }))
				.build();
		
		// Prepare mock service method
		Mockito.doThrow(new OrderException("Test Exception")).when(orderRequestService).placeOrder(placeOrder);
		
		// Perform POST request
		mockMvc.perform(post("/orders")
				.contentType(MediaType.APPLICATION_JSON_VALUE)
				.content(new ObjectMapper().writeValueAsString(placeOrder)))
				
		// Validate 400 BAD REQUEST and JSON response type received
		.andExpect(status().isBadRequest())
		.andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
				
		// Validate response body
		.andExpect(jsonPath("$.error", is("origin must have 2 items")));
	}
	
	@Test
	@DisplayName("Place Order invalid destination format - POST /orders")
	public void testPlaceOrderInvalidDestinationFomat() throws Exception {
		
		placeOrder = PlaceOrder.builder()
				.origin(Arrays.asList(new String [] { "14.7002409", "121.08754623" }))
				.destination(Arrays.asList(new String [] { "END_LATITUDE", "END_LONGITUDE" }))
				.build();
		
		// Prepare mock service method
		Mockito.doThrow(new OrderException("Test Exception")).when(orderRequestService).placeOrder(placeOrder);
		
		// Perform POST request
		mockMvc.perform(post("/orders")
				.contentType(MediaType.APPLICATION_JSON_VALUE)
				.content(new ObjectMapper().writeValueAsString(placeOrder)))
				
		// Validate 400 BAD REQUEST and JSON response type received
		.andExpect(status().isBadRequest())
		.andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
				
		// Validate response body
		.andExpect(jsonPath("$.error", is("Invalid destination latitude / longitude format")));
	}
	
	@Test
	@DisplayName("Place Order invalid destination list size - POST /orders")
	public void testPlaceOrderInvalidDestinationListSize() throws Exception {
		
		placeOrder = PlaceOrder.builder()
				.origin(Arrays.asList(new String [] { "14.7002409", "121.08754623" }))
				.destination(Arrays.asList(new String [] { "14.5568853", "121.023532", "121.023532" }))
				.build();
		
		// Prepare mock service method
		Mockito.doThrow(new OrderException("Test Exception")).when(orderRequestService).placeOrder(placeOrder);
		
		// Perform POST request
		mockMvc.perform(post("/orders")
				.contentType(MediaType.APPLICATION_JSON_VALUE)
				.content(new ObjectMapper().writeValueAsString(placeOrder)))
				
		// Validate 400 BAD REQUEST and JSON response type received
		.andExpect(status().isBadRequest())
		.andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
				
		// Validate response body
		.andExpect(jsonPath("$.error", is("destination must have 2 items")));
	}
	
	@Test
	@DisplayName("Take order - PATCH /orders/1")
	public void testTakeOrder() throws Exception {
		
		// Mock Data
		TakeOrder mockTakeOrderResponse = TakeOrder.builder()
				.status("SUCCESS")
				.build();
		
		// Prepare mock service method
		Mockito.doReturn(mockTakeOrderResponse).when(orderRequestService).takeOrder(1, takeOrder);
		
		mockMvc.perform(patch("/orders/1")
				.contentType(MediaType.APPLICATION_JSON_VALUE)
				.content(new ObjectMapper().writeValueAsString(takeOrder)))
		
		// Validate 200 OK and JSON response type received
		.andExpect(status().isOk())
		.andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
		
		// Validate response body
		.andExpect(jsonPath("$.status", is("SUCCESS")));
	}
	
	@Test
	@DisplayName("Take order Handle OrderException - PATCH /orders/1")
	public void testTakeOrderHandleOrderException() throws Exception {
		
		// Prepare mock service method
		Mockito.doThrow(new OrderException("Test Exception")).when(orderRequestService).takeOrder(1, takeOrder);
		
		// Perform POST request
		mockMvc.perform(patch("/orders/1")
				.contentType(MediaType.APPLICATION_JSON_VALUE)
				.content(new ObjectMapper().writeValueAsString(takeOrder)))
						
		// Validate 400 BAD REQUEST and JSON response type received
		.andExpect(status().isBadRequest())
		.andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
						
		// Validate response body
		.andExpect(jsonPath("$.error", is("Test Exception")));
		
	}
	
	@Test
	@DisplayName("Order list - GET /orders?page=1&limit=5")
	public void testOrderList() throws Exception {

		// Mock Data
		List<OrderRequest> mockOrderList = Arrays.asList(new OrderRequest[] {
				OrderRequest.builder().id(1).distance(100).status("SAMPLE_STAT").build(),
				OrderRequest.builder().id(2).distance(200).status("SAMPLE_STAT").build(),
				OrderRequest.builder().id(3).distance(300).status("SAMPLE_STAT").build(),
				OrderRequest.builder().id(4).distance(400).status("SAMPLE_STAT").build(),
				OrderRequest.builder().id(5).distance(500).status("SAMPLE_STAT").build()
		});
		
		// Prepare mock service method
		Mockito.doReturn(mockOrderList).when(orderRequestService).orderList(1, 5);
		
		mockMvc.perform(get("/orders")
				.param("page", "1")
				.param("limit", "5")
				.contentType(MediaType.APPLICATION_JSON_VALUE))
		
		// Validate 200 OK and JSON response type received
		.andExpect(status().isOk())
		.andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
		
		// Validate response body
		.andExpect(jsonPath("$[0].id", is(1)))
		.andExpect(jsonPath("$[1].id", is(2)))
		.andExpect(jsonPath("$[2].id", is(3)))
		.andExpect(jsonPath("$[3].id", is(4)))
		.andExpect(jsonPath("$[4].id", is(5)));
	}
	
	@Test
	@DisplayName("Order list Handle OrderException - GET /orders?page=1&limit=5")
	public void testOrderListHandleOrderException() throws Exception {
		
		// Prepare mock service method
		Mockito.doThrow(new OrderException("Test Exception")).when(orderRequestService).orderList(1, 5);
		
		// Perform POST request
		mockMvc.perform(get("/orders")
				.param("page", "1")
				.param("limit", "5")
				.contentType(MediaType.APPLICATION_JSON_VALUE))
						
		// Validate 400 BAD REQUEST and JSON response type received
		.andExpect(status().isBadRequest())
		.andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
						
		// Validate response body
		.andExpect(jsonPath("$.error", is("Test Exception")));
		
	}
	

}
