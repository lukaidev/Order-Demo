package com.demo.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "orders")
public class Order {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Integer id;

	@NotEmpty(message = "origin must not be empty")
	@Column(name = "origin")
	private String origin;

	@NotEmpty(message = "destination must not be empty")
	@Column(name = "destination")
	private String destination;
	
	@NotNull(message = "destination must not be null")
	@Column(name = "distance")
	private Integer distance;
	
	@Enumerated(EnumType.STRING)
	private OrderStatus status;
	
	public static Order placeOrder(String origin, String destination, Integer distance) {
		return Order.builder()
				.origin(origin)
				.destination(destination)
				.distance(distance)
				.status(OrderStatus.UNASSIGNED)
				.build();
	}
	
	public void takeOrder() {
		this.status = OrderStatus.TAKEN;
	}

}
