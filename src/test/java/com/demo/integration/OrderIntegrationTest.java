package com.demo.integration;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Arrays;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import com.demo.domain.Order;
import com.demo.domain.OrderStatus;
import com.demo.domain.repository.OrderRepository;
import com.demo.model.PlaceOrder;
import com.demo.model.TakeOrder;
import com.fasterxml.jackson.databind.ObjectMapper;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class OrderIntegrationTest {
	
	@Autowired
	MockMvc mockMvc;
	
	@Autowired
	OrderRepository orderRepository;
	
	PlaceOrder placeOrder;
	TakeOrder takeOrder;
	
	@BeforeEach
	public void init() {
		
		placeOrder = PlaceOrder.builder()
				.origin(Arrays.asList(new String [] { "14.7002409", "121.08754623" }))
				.destination(Arrays.asList(new String [] { "14.5568853", "121.023532" }))
				.build();
		
		takeOrder = TakeOrder.builder()
				.status("TAKEN")
				.build();
		
		orderRepository.saveAll(Arrays.asList(new Order [] {
				Order.builder()
				.origin("14.7002409,121.08754623")
				.destination("14.5568853,121.023532")
				.distance(23743)
				.status(OrderStatus.UNASSIGNED)
				.build(),
				Order.builder()
				.origin("14.7002409,121.08754623")
				.destination("14.5568853,121.023532")
				.distance(23743)
				.status(OrderStatus.TAKEN)
				.build(),
				Order.builder()
				.origin("14.7002409,121.08754623")
				.destination("14.5568853,121.023532")
				.distance(23743)
				.status(OrderStatus.UNASSIGNED)
				.build()
		}));
	}
	
	@Test
	@DisplayName("Place Order - POST /orders")
	public void testPlaceOrder() throws Exception {
		
		// Perform POST request
		mockMvc.perform(post("/orders")
				.contentType(MediaType.APPLICATION_JSON_VALUE)
				.content(new ObjectMapper().writeValueAsString(placeOrder)))
		
		// Validate 200 OK and JSON response type received
		.andExpect(status().isOk())
		.andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
		
		// Validate response body
		.andExpect(jsonPath("$.distance", is(23743)))
		.andExpect(jsonPath("$.status", is("UNASSIGNED")));
	}
	
	@Test
	@DisplayName("Place Order invalid origin format - POST /orders")
	public void testPlaceOrderInvalidOriginFomat() throws Exception {
		
		placeOrder = PlaceOrder.builder()
				.origin(Arrays.asList(new String [] { "START_LATITUDE", "START_LONGITUDE" }))
				.destination(Arrays.asList(new String [] { "14.5568853", "121.023532" }))
				.build();
		
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
	@DisplayName("Place Order invalid origin list size less than 2 - POST /orders")
	public void testPlaceOrderInvalidOriginListSizeLessThanTwo() throws Exception {
		
		placeOrder = PlaceOrder.builder()
				.origin(Arrays.asList(new String [] { "14.7002409" }))
				.destination(Arrays.asList(new String [] { "14.5568853", "121.023532" }))
				.build();
		
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
	@DisplayName("Place Order invalid destination list size more than 2 - POST /orders")
	public void testPlaceOrderInvalidDestinationListSizeMoreThanTwo() throws Exception {
		
		placeOrder = PlaceOrder.builder()
				.origin(Arrays.asList(new String [] { "14.7002409", "121.08754623" }))
				.destination(Arrays.asList(new String [] { "14.5568853", "121.023532", "121.023532" }))
				.build();
		
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
	@DisplayName("Take order that is taken - PATCH /orders/2")
	public void testTakeOrderThatIsTaken() throws Exception {
		
		mockMvc.perform(patch("/orders/2")
				.contentType(MediaType.APPLICATION_JSON_VALUE)
				.content(new ObjectMapper().writeValueAsString(takeOrder)))
		
		// Validate 400 Bad Request and JSON response type received
		.andExpect(status().isBadRequest())
		.andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
		
		// Validate response body
		.andExpect(jsonPath("$.error", is("Order alredy taken")));
	}
	
	@Test
	@DisplayName("Take order that not exist - PATCH /orders/100")
	public void testTakeOrderThatNotExist() throws Exception {
		
		mockMvc.perform(patch("/orders/100")
				.contentType(MediaType.APPLICATION_JSON_VALUE)
				.content(new ObjectMapper().writeValueAsString(takeOrder)))
		
		// Validate 400 Bad Request and JSON response type received
		.andExpect(status().isBadRequest())
		.andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
		
		// Validate response body
		.andExpect(jsonPath("$.error", is("Unable to find order")));
	}
	
	@Test
	@DisplayName("Order list - GET /orders?page=1&limit=3")
	public void testOrderList() throws Exception {
		
		mockMvc.perform(get("/orders")
				.param("page", "1")
				.param("limit", "3")
				.contentType(MediaType.APPLICATION_JSON_VALUE))
		
		// Validate 200 OK and JSON response type received
		.andExpect(status().isOk())
		.andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
		
		// Validate response body
		.andExpect(jsonPath("$[0].distance", is(23743)))
		.andExpect(jsonPath("$[1].distance", is(23743)))
		.andExpect(jsonPath("$[2].distance", is(23743)));
	}
	
	@Test
	@DisplayName("Order list page start with zero - GET /orders?page=0&limit=3")
	public void testOrderListPageStartWithZero() throws Exception {
		
		mockMvc.perform(get("/orders")
				.param("page", "0")
				.param("limit", "3")
				.contentType(MediaType.APPLICATION_JSON_VALUE))
		
		// Validate 400 Bad Request and JSON response type received
		.andExpect(status().isBadRequest())
		.andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
		
		// Validate response body
		.andExpect(jsonPath("$.error", is("Page number must starts with 1")));
	}

}
