package org.springframework.samples.petclinic.adapter.out.order;

import org.springframework.samples.petclinic.domain.order.Order;

final class OrderPersistenceMapper {

    static Order toDomain(OrderJpaEntity entity) {
        return new Order(entity.getId(), entity.getStatus());
    }

    static OrderJpaEntity toEntity(Order order) {
        return new OrderJpaEntity(order.getId(), order.getStatus());
    }

    private OrderPersistenceMapper() {
        // utility class
    }
}
