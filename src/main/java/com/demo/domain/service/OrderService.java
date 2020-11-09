package com.demo.domain.service;

import java.util.List;

import com.demo.domain.Order;

public interface OrderService {

	Order save(Order order);

	Order getOrderById(Integer id);

	List<Order> getOrderList(Integer page, Integer limit);

}
