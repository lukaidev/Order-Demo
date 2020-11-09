package com.demo.web.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.demo.app.service.OrderRequestService;
import com.demo.model.OrderRequest;
import com.demo.model.PlaceOrder;
import com.demo.model.TakeOrder;

import lombok.extern.log4j.Log4j2;

@RestController
@RequestMapping("/orders")
@Log4j2
public class OrderController {

	private final OrderRequestService orderService;

	public OrderController(OrderRequestService orderService) {
		this.orderService = orderService;
	}

	@PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<OrderRequest> placeOrder(@RequestBody @Valid PlaceOrder placeOrder) {
		log.info("START -> placeOrder");
		OrderRequest orderRespose = orderService.placeOrder(placeOrder);
		log.info("END -> placeOrder");
		return ResponseEntity.ok(orderRespose);
	}

	@PatchMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<TakeOrder> takeOrder(@PathVariable(required = true) Integer id,
			@RequestBody @Valid TakeOrder takeOrder) {
		log.info("START -> takeOrder");
		TakeOrder takeOrderRespose = orderService.takeOrder(id, takeOrder);
		log.info("END -> takeOrder");
		return ResponseEntity.ok(takeOrderRespose);
	}

	@GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<OrderRequest>> orderList(
			@RequestParam(required = true) Integer page,
			@RequestParam(required = true) Integer limit) {
		log.info("START -> orderList");
		List<OrderRequest> orderListResponse = orderService.orderList(page, limit);
		log.info("END -> orderList");
		return ResponseEntity.ok(orderListResponse);
	}

}
