package org.springframework.samples.petclinic.adapter.out.order;

import jakarta.persistence.*;
import org.springframework.samples.petclinic.domain.order.OrderStatus;

@Entity
@Table(name = "orders")
public class OrderJpaEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private OrderStatus status;

	protected OrderJpaEntity() {
		// JPA
	}

	public OrderJpaEntity(OrderStatus status) {
		this.status = status;
	}

	public OrderJpaEntity(Long id, OrderStatus status) {
		this.id = id;
		this.status = status;
	}

	public Long getId() {
		return id;
	}

	public OrderStatus getStatus() {
		return status;
	}

	public void setStatus(OrderStatus status) {
		this.status = status;
	}

}
