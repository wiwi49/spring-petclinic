package org.springframework.samples.petclinic.application.order.port.out;

import org.springframework.samples.petclinic.domain.order.Order;

import java.util.Optional;

public interface OrderRepositoryPort {
    Optional<Order> findById(Long id);
    Order save(Order order);
}
