package com.demo.app.service.impl;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.demo.app.service.OrderRequestService;
import com.demo.client.api.GoogleDirectionsClient;
import com.demo.domain.Order;
import com.demo.domain.service.OrderService;
import com.demo.exception.OrderException;
import com.demo.model.OrderRequest;
import com.demo.model.PlaceOrder;
import com.demo.model.TakeOrder;

import lombok.extern.log4j.Log4j2;

@Service
@Log4j2
public class OrderRequestServiceImpl implements OrderRequestService {
	
	private static final Integer PAGE_MIN_SIZE = 1;
	
	private final OrderService orderService;
	private final GoogleDirectionsClient directionsClient;
	
	public OrderRequestServiceImpl(OrderService orderService, GoogleDirectionsClient directionsClient) {
		this.orderService = orderService;
		this.directionsClient = directionsClient;
	}

	@Override
	public OrderRequest placeOrder(PlaceOrder placeOrder) {
		log.info("Start OrderRequestService -> placeOrder");
		
		String origin = Optional.of(placeOrder)
				.map(p -> p.getOrigin().stream()
						.collect(Collectors.joining(",")))
				.get();
		
		String destination = Optional.of(placeOrder)
				.map(p -> p.getDestination().stream()
						.collect(Collectors.joining(",")))
				.get();
		
		OrderRequest orderRequest = Optional.of(
				directionsClient.getDistance(origin, destination))
				.map(distance -> orderService.save(
						Order.placeOrder(
								origin, 
								destination, 
								distance)))
				.map(order -> OrderRequest.builder()
						.id(order.getId())
						.distance(order.getDistance())
						.status(order.getStatus().name())
						.build())
				.get();
		
		log.info("End OrderRequestService -> placeOrder");
		return orderRequest;
	}

	@Override
	public TakeOrder takeOrder(Integer id, TakeOrder takeOrder) {
		log.info("Start OrderRequestService -> takeOrder");
		
		Order order = orderService.getOrderById(id);
		if(order.getStatus().name().equals(takeOrder.getStatus())) {
			throw new OrderException("Order alredy taken");
		}
		
		order.takeOrder();
		TakeOrder takeOrderResponse = Optional.of(orderService.save(order))
				.map(o -> TakeOrder.success())
				.get();
		
		log.info("End OrderRequestService -> takeOrder");
		return takeOrderResponse;
	}

	@Override
	public List<OrderRequest> orderList(Integer page, Integer limit) {
		log.info("Start OrderRequestService -> orderList");
		if(PAGE_MIN_SIZE > page) {
			throw new OrderException("Page number must starts with 1");
		}
		
		List<OrderRequest> orderListResult = orderService.getOrderList(page - 1, limit)
				.stream().map(order -> OrderRequest.builder()
						.id(order.getId())
						.distance(order.getDistance())
						.status(order.getStatus().name())
						.build())
				.collect(Collectors.toList());
		
		log.info("End OrderRequestService -> orderList");
		return orderListResult;
	}

}
