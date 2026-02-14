package org.springframework.samples.petclinic.domain.order;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

class OrderTest {

	@Test
	void shouldAllowTransitionCreatedToPaid() {
		Order order = new Order(1L, OrderStatus.CREATED);

		order.changeStatus(OrderStatus.PAID);

		assertEquals(OrderStatus.PAID, order.getStatus());
	}

	@Test
	void shouldRejectTransitionPaidToCreated() {
		Order order = new Order(1L, OrderStatus.PAID);

		InvalidOrderStatusTransitionException ex = assertThrows(InvalidOrderStatusTransitionException.class,
				() -> order.changeStatus(OrderStatus.CREATED));

		assertTrue(ex.getMessage().contains("Transition invalide"));
	}

	@Test
	void shouldAllowTransitionPaidToShipped() {
		Order order = new Order(1L, OrderStatus.PAID);

		order.changeStatus(OrderStatus.SHIPPED);

		assertEquals(OrderStatus.SHIPPED, order.getStatus());
	}

	@Test
	void shouldAllowTransitionShippedToDelivered() {
		Order order = new Order(1L, OrderStatus.SHIPPED);

		order.changeStatus(OrderStatus.DELIVERED);

		assertEquals(OrderStatus.DELIVERED, order.getStatus());
	}

	@Test
	void shouldRejectSkippingStatusesCreatedToShipped() {
		Order order = new Order(1L, OrderStatus.CREATED);

		InvalidOrderStatusTransitionException ex = assertThrows(InvalidOrderStatusTransitionException.class,
				() -> order.changeStatus(OrderStatus.SHIPPED));

		assertTrue(ex.getMessage().contains("Transition invalide"));
	}

}
