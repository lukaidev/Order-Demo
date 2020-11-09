package com.demo.app.service;

import java.util.List;

import com.demo.model.OrderRequest;
import com.demo.model.PlaceOrder;
import com.demo.model.TakeOrder;

public interface OrderRequestService {
	
	OrderRequest placeOrder(PlaceOrder placeOrder);
	
	TakeOrder takeOrder(Integer id, TakeOrder takeOrder);
	
	List<OrderRequest> orderList(Integer page, Integer limit);

}
