package org.springframework.samples.petclinic.adapter.out.order;

import org.springframework.samples.petclinic.application.order.port.out.OrderRepositoryPort;
import org.springframework.samples.petclinic.domain.order.Order;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class OrderRepositoryAdapter implements OrderRepositoryPort {

    private final SpringDataOrderRepository repository;

    public OrderRepositoryAdapter(SpringDataOrderRepository repository) {
        this.repository = repository;
    }

    @Override
    public Optional<Order> findById(Long id) {
        return repository.findById(id).map(OrderPersistenceMapper::toDomain);
    }

    @Override
    public Order save(Order order) {
        OrderJpaEntity saved = repository.save(OrderPersistenceMapper.toEntity(order));
        return OrderPersistenceMapper.toDomain(saved);
    }
}
