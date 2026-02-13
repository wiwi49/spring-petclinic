package org.springframework.samples.petclinic.application.order.service;

import org.springframework.samples.petclinic.application.order.port.in.CreateOrderUseCase;
import org.springframework.samples.petclinic.application.order.port.out.OrderRepositoryPort;
import org.springframework.samples.petclinic.domain.order.Order;
import org.springframework.samples.petclinic.domain.order.OrderStatus;
import org.springframework.stereotype.Service;

@Service
public class CreateOrderService implements CreateOrderUseCase {

    private final OrderRepositoryPort orderRepositoryPort;

    public CreateOrderService(OrderRepositoryPort orderRepositoryPort) {
        this.orderRepositoryPort = orderRepositoryPort;
    }

    @Override
    public Order create() {
        Order newOrder = new Order(null, OrderStatus.CREATED);
        return orderRepositoryPort.save(newOrder);
    }
}
