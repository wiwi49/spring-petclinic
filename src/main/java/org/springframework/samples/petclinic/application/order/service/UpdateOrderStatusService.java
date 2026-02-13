package org.springframework.samples.petclinic.application.order.service;

import org.springframework.samples.petclinic.application.order.UpdateOrderStatusUseCase;
import org.springframework.samples.petclinic.application.order.port.out.OrderRepositoryPort;
import org.springframework.samples.petclinic.domain.order.Order;
import org.springframework.samples.petclinic.domain.order.OrderNotFoundException;
import org.springframework.samples.petclinic.domain.order.OrderStatus;
import org.springframework.stereotype.Service;

@Service
public class UpdateOrderStatusService implements UpdateOrderStatusUseCase {

    private final OrderRepositoryPort orderRepository;

    public UpdateOrderStatusService(OrderRepositoryPort orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Override
    public Order updateStatus(Long orderId, OrderStatus newStatus) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new OrderNotFoundException(orderId));

        order.changeStatus(newStatus);

        return orderRepository.save(order);
    }
}
