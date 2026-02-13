package org.springframework.samples.petclinic.application.order;

import org.springframework.samples.petclinic.domain.order.Order;
import org.springframework.samples.petclinic.domain.order.OrderStatus;

public interface UpdateOrderStatusUseCase {
    Order updateStatus(Long orderId, OrderStatus newStatus);
}
