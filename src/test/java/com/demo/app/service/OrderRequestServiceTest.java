package com.demo.app.service;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
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

import com.demo.client.api.GoogleDirectionsClient;
import com.demo.domain.Order;
import com.demo.domain.OrderStatus;
import com.demo.domain.service.OrderService;
import com.demo.exception.OrderException;
import com.demo.model.OrderRequest;
import com.demo.model.PlaceOrder;
import com.demo.model.TakeOrder;

import static org.junit.jupiter.api.Assertions.*;


@ExtendWith(SpringExtension.class)
@SpringBootTest
@ActiveProfiles("test")
public class OrderRequestServiceTest {
	
	@Autowired
	OrderRequestService requestService;
	
	@MockBean
	OrderService orderService;
	
	@MockBean
	GoogleDirectionsClient directionsClient; 
	
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
		
	}
	
	@Test
	@DisplayName("Place Order")
	public void testPlaceOrder() throws Exception {
		
		Integer mockDistance = 23743;
		
		Order mockOrder = Order.builder()
				.id(1)
				.origin("14.7002409,121.08754623")
				.destination("14.5568853,121.023532")
				.distance(mockDistance)
				.status(OrderStatus.UNASSIGNED)
				.build();
		
		Mockito.doReturn(mockDistance).when(directionsClient)
		.getDistance("14.7002409,121.08754623","14.5568853,121.023532");
		Mockito.doReturn(mockOrder).when(orderService).save(ArgumentMatchers.any());
		
		OrderRequest result = requestService.placeOrder(placeOrder);
		
		assertEquals(mockOrder.getId(), result.getId());
		assertEquals(mockOrder.getDistance(), result.getDistance());
		assertEquals(mockOrder.getStatus().toString(), result.getStatus());
	}
	
	
	@Test
	@DisplayName("Take order")
	public void testTakeOrder() throws Exception {
		
		Order mockFindOrder = Order.builder()
				.id(1)
				.origin("14.7002409,121.08754623")
				.destination("14.5568853,121.023532")
				.distance(23743)
				.status(OrderStatus.UNASSIGNED)
				.build();
		
		Order mockOrder = Order.builder()
				.id(1)
				.origin("14.7002409,121.08754623")
				.destination("14.5568853,121.023532")
				.distance(23743)
				.status(OrderStatus.TAKEN)
				.build();
		
		
		Mockito.doReturn(mockFindOrder).when(orderService).getOrderById(1);
		
		Mockito.doReturn(mockOrder).when(orderService).save(ArgumentMatchers.any());
		
		TakeOrder takeOrderResult = requestService.takeOrder(1, takeOrder);
		
		assertEquals("SUCCESS", takeOrderResult.getStatus());
	}
	
	@Test
	@DisplayName("Take order - handle order taken")
	public void testTakeOrderHandleOrderTaken() throws Exception {
		
		Order mockOrder = Order.builder()
				.id(1)
				.origin("14.7002409,121.08754623")
				.destination("14.5568853,121.023532")
				.distance(23743)
				.status(OrderStatus.TAKEN)
				.build();
		
		Mockito.doReturn(mockOrder).when(orderService).getOrderById(1);
		
		Exception exception = assertThrows(
				OrderException.class, 
				() -> requestService.takeOrder(1, takeOrder));
		
		assertTrue("Order alredy taken"
				.contains(exception.getMessage()));
	}
	
	
	@Test
	@DisplayName("Order List")
	public void testOrderList() throws Exception {
		
		List<Order> mockOrderList = Arrays.asList(new Order[] {
				Order.builder()
				.id(1)
				.distance(100)
				.origin("123,123")
				.destination("321,321")
				.status(OrderStatus.UNASSIGNED)
				.build(),
				Order.builder()
				.id(2)
				.distance(200)
				.origin("123,123")
				.destination("321,321")
				.status(OrderStatus.UNASSIGNED)
				.build(),
				Order.builder()
				.id(3)
				.distance(300)
				.origin("123,123")
				.destination("321,321")
				.status(OrderStatus.UNASSIGNED)
				.build()
		});
		
		Mockito.doReturn(mockOrderList).when(orderService).getOrderList(
				ArgumentMatchers.anyInt(), 
				ArgumentMatchers.anyInt());
		
		List<OrderRequest> orderRequestListResult = requestService.orderList(1, 3);
		
		assertEquals(1, orderRequestListResult.get(0).getId());
		assertEquals(2, orderRequestListResult.get(1).getId());
		assertEquals(3, orderRequestListResult.get(2).getId());
		
		assertEquals(100, orderRequestListResult.get(0).getDistance());
		assertEquals(200, orderRequestListResult.get(1).getDistance());
		assertEquals(300, orderRequestListResult.get(2).getDistance());
		
		assertEquals("UNASSIGNED", orderRequestListResult.get(0).getStatus());
		assertEquals("UNASSIGNED", orderRequestListResult.get(1).getStatus());
		assertEquals("UNASSIGNED", orderRequestListResult.get(2).getStatus());
	}
	
	@Test
	@DisplayName("Order List - handle page start with zero")
	public void testOrderListHandlePageZero() throws Exception {
		
		Exception exception = assertThrows(
				OrderException.class, 
				() -> requestService.orderList(0, 3));
		
		assertTrue("Page number must starts with 1"
				.contains(exception.getMessage()));
	}

}
