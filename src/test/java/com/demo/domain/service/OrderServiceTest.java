package com.demo.domain.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.demo.domain.Order;
import com.demo.domain.OrderStatus;
import com.demo.domain.repository.OrderRepository;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@ActiveProfiles("test")
public class OrderServiceTest {
	
	@Autowired
	OrderService orderService;
	
	@MockBean
	OrderRepository orderRepository;
	
	Optional<Order> mockOrder;
	
	Page<Order> mockOrderList;
	
	@BeforeEach
	public void init() {
		
		mockOrder = Optional.of(Order.builder()
				.id(1)
				.origin("14.7002409,121.08754623")
				.destination("14.5568853,121.023532")
				.distance(23743)
				.status(OrderStatus.UNASSIGNED)
				.build());
		
		mockOrderList = new PageImpl<>(Arrays.asList(new Order[] {
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
		}));
	}
	
	@Test
	@DisplayName("Order - save")
	public void testOrderSave() throws Exception {
		
		Order order = Order.placeOrder(
				"14.7002409,121.08754623",
				"14.5568853,121.023532", 
				23743);
		
		Mockito.doReturn(mockOrder.get()).when(orderRepository).save(order);
		
		Order response = orderService.save(order);
		
		assertNotNull(response.getId());
		assertEquals(mockOrder.get().getOrigin(), response.getOrigin());
		assertEquals(mockOrder.get().getDestination(), response.getDestination());
		assertEquals(mockOrder.get().getDistance(), response.getDistance());
		assertEquals(mockOrder.get().getStatus(), response.getStatus());
		
	}
	
	@Test
	@DisplayName("Order - get order by id")
	public void testOrderGetOrderById() throws Exception {
		
		Mockito.doReturn(mockOrder).when(orderRepository)
		.findById(ArgumentMatchers.anyInt());
		
		Order response = orderService.getOrderById(1);
		
		assertEquals(mockOrder.get().getId(), response.getId());
		assertEquals(mockOrder.get().getOrigin(), response.getOrigin());
		assertEquals(mockOrder.get().getDestination(), response.getDestination());
		assertEquals(mockOrder.get().getDistance(), response.getDistance());
		assertEquals(mockOrder.get().getStatus(), response.getStatus());
		
	}
	
	@Test
	@DisplayName("Order - get order list")
	public void testOrderGetOrderList() throws Exception {
		
		Pageable pageable = PageRequest.of(0, 3);
		Mockito.doReturn(mockOrderList).when(orderRepository)
		.findAll(pageable);
		
		List<Order> response = orderService.getOrderList(0, 3);
		
		assertEquals(1, response.get(0).getId());
		assertEquals(2, response.get(1).getId());
		assertEquals(3, response.get(2).getId());
		
	}
	
}
