package org.springframework.samples.petclinic.domain.order;

import java.util.Objects;

public class Order {

    private final Long id;
    private OrderStatus status;

    public Order(Long id, OrderStatus status) {
        this.id = Objects.requireNonNull(id, "id ne peut pas être null");
        this.status = Objects.requireNonNull(status, "status ne peut pas être null");
    }

    public Long getId() {
        return id;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public void changeStatus(OrderStatus newStatus) {
        Objects.requireNonNull(newStatus, "newStatus ne peut pas être null");

        if (!OrderStatusTransitions.isAllowed(this.status, newStatus)) {
            throw new InvalidOrderStatusTransitionException(this.status, newStatus);
        }
        this.status = newStatus;
    }
}
