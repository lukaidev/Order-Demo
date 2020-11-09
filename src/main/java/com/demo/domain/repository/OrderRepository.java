package com.demo.domain.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.demo.domain.Order;

@Repository
public interface OrderRepository extends JpaRepository<Order, Integer> {
	
	Page<Order> findAll(Pageable pageable);
	
}
