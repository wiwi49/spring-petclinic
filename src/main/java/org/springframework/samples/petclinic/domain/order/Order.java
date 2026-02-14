package org.springframework.samples.petclinic.domain.order;

import java.util.Objects;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "petclinic_orders")
public class Order {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id; // null avant save()

	@Enumerated(EnumType.STRING)
	@Column(name = "status", nullable = false, length = 30)
	private OrderStatus status;

	protected Order() {
		// requis par JPA
	}

	public Order(Long id, OrderStatus status) {
		this.id = id; // id peut être null
		this.status = Objects.requireNonNull(status, "status ne peut pas être null");
	}

	public static Order createNew() {
		return new Order(null, OrderStatus.CREATED);
	}

	public Long getId() {
		return id;
	}

	public OrderStatus getStatus() {
		return status;
	}

	// Méthode attendue par tes tests + UpdateOrderStatusService
	public void changeStatus(OrderStatus newStatus) {
		Objects.requireNonNull(newStatus, "newStatus ne peut pas être null");
		validateTransition(this.status, newStatus);
		this.status = newStatus;
	}

	private void validateTransition(OrderStatus from, OrderStatus to) {
		boolean ok = (from == OrderStatus.CREATED && to == OrderStatus.PAID)
				|| (from == OrderStatus.PAID && to == OrderStatus.SHIPPED)
				|| (from == OrderStatus.SHIPPED && to == OrderStatus.DELIVERED);

		if (!ok) {
			throw new InvalidOrderStatusTransitionException("Transition invalide: " + from + " -> " + to);
		}
	}

}
