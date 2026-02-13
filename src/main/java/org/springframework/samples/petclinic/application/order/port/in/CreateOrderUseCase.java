package org.springframework.samples.petclinic.application.order.port.in;

import org.springframework.samples.petclinic.domain.order.Order;

public interface CreateOrderUseCase {
    Order create();
}
