package org.springframework.samples.petclinic.domain.order;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

/**
 * Tests unitaires de l'entité Order (couche domaine).
 *
 * Objectif :
 *  - vérifier que les règles métier de transition de statut
 *    sont correctement appliquées
 *  - garantir que l'entité protège ses invariants
 *
 * Ces tests ne dépendent d'aucune infrastructure
 * (ni base de données, ni Spring).
 */
class OrderTest {

	/**
	 * Cas nominal :
	 *  - une commande CREATED peut passer à PAID
	 */
	@Test
	void shouldAllowTransitionCreatedToPaid() {
		// Arrange : commande nouvellement créée
		Order order = new Order(1L, OrderStatus.CREATED);

		// Act : changement de statut autorisé
		order.changeStatus(OrderStatus.PAID);

		// Assert : le statut a bien été mis à jour
		assertEquals(OrderStatus.PAID, order.getStatus());
	}

	/**
	 * Cas d'erreur :
	 *  - une commande PAID ne peut pas revenir à CREATED
	 */
	@Test
	void shouldRejectTransitionPaidToCreated() {
		// Arrange : commande déjà payée
		Order order = new Order(1L, OrderStatus.PAID);

		// Act + Assert : transition interdite => exception
		InvalidOrderStatusTransitionException ex = assertThrows(
				InvalidOrderStatusTransitionException.class,
				() -> order.changeStatus(OrderStatus.CREATED)
		);

		// Assert : le message d'erreur est explicite
		assertTrue(ex.getMessage().contains("Transition invalide"));
	}

	/**
	 * Cas nominal :
	 *  - une commande PAID peut être expédiée
	 */
	@Test
	void shouldAllowTransitionPaidToShipped() {
		// Arrange
		Order order = new Order(1L, OrderStatus.PAID);

		// Act
		order.changeStatus(OrderStatus.SHIPPED);

		// Assert
		assertEquals(OrderStatus.SHIPPED, order.getStatus());
	}

	/**
	 * Cas nominal :
	 *  - une commande SHIPPED peut être livrée
	 */
	@Test
	void shouldAllowTransitionShippedToDelivered() {
		// Arrange
		Order order = new Order(1L, OrderStatus.SHIPPED);

		// Act
		order.changeStatus(OrderStatus.DELIVERED);

		// Assert
		assertEquals(OrderStatus.DELIVERED, order.getStatus());
	}

	/**
	 * Cas d'erreur :
	 *  - il est interdit de sauter des statuts
	 *  - CREATED -> SHIPPED est une transition invalide
	 */
	@Test
	void shouldRejectSkippingStatusesCreatedToShipped() {
		// Arrange
		Order order = new Order(1L, OrderStatus.CREATED);

		// Act + Assert
		InvalidOrderStatusTransitionException ex = assertThrows(
				InvalidOrderStatusTransitionException.class,
				() -> order.changeStatus(OrderStatus.SHIPPED)
		);

		// Assert
		assertTrue(ex.getMessage().contains("Transition invalide"));
	}
}
