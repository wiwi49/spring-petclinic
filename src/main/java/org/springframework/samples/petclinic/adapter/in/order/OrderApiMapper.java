package org.springframework.samples.petclinic.adapter.in.order;

import org.springframework.samples.petclinic.adapter.in.order.response.OrderResponse;
import org.springframework.samples.petclinic.domain.order.Order;

final class OrderApiMapper {

	private OrderApiMapper() {
	}

	static OrderResponse toResponse(Order order) {
		return new OrderResponse(order.getId(), order.getStatus().name());
	}

}
