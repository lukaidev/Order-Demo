package com.demo.domain.service.impl;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.demo.domain.Order;
import com.demo.domain.repository.OrderRepository;
import com.demo.domain.service.OrderService;
import com.demo.exception.OrderException;

@Service
@Transactional(rollbackOn = { Exception.class })
public class OrderServiceImpl implements OrderService{
	
	private final OrderRepository orderRepository;
	
	public OrderServiceImpl(OrderRepository orderRepository) {
		this.orderRepository = orderRepository;
	}

	@Override
	public Order save(Order order) {
		return orderRepository.save(order);
	}

	@Override
	public Order getOrderById(Integer id) {
		return orderRepository.findById(id)
				.orElseThrow(() -> 
				new OrderException("Unable to find order"));
	}

	@Override
	public List<Order> getOrderList(Integer page, Integer limit) {
		Pageable pageable = PageRequest.of(page, limit);
		return orderRepository.findAll(pageable).getContent();
	}

}
