package org.springframework.samples.petclinic.application.order.service;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import org.springframework.samples.petclinic.application.order.port.out.OrderRepositoryPort;
import org.springframework.samples.petclinic.domain.order.InvalidOrderStatusTransitionException;
import org.springframework.samples.petclinic.domain.order.Order;
import org.springframework.samples.petclinic.domain.order.OrderNotFoundException;
import org.springframework.samples.petclinic.domain.order.OrderStatus;

/**
 * Tests unitaires du service applicatif UpdateOrderStatusService.
 *
 * Objectif :
 *  - vérifier l'orchestration du cas d'usage (find → changeStatus → save)
 *  - vérifier la gestion des erreurs attendues (not found, transition invalide)
 *
 * Le repository est mocké (Mockito) afin de tester uniquement la logique du service,
 * sans dépendre de la base de données.
 */
class UpdateOrderStatusServiceTest {

	/**
	 * Cas nominal :
	 *  - une commande existe
	 *  - la transition CREATED -> PAID est autorisée
	 *  - la commande est sauvegardée
	 */
	@Test
	void shouldUpdateStatusAndSaveWhenTransitionIsValid() {
		// Arrange : création du mock repository + service à tester
		OrderRepositoryPort repository = mock(OrderRepositoryPort.class);
		UpdateOrderStatusService service = new UpdateOrderStatusService(repository);

		// Arrange : commande existante en base (simulée)
		Order existing = new Order(1L, OrderStatus.CREATED);

		// Arrange : le repository retourne la commande quand on la cherche par id
		when(repository.findById(1L)).thenReturn(Optional.of(existing));

		// Arrange : le save retourne l'objet sauvegardé (ici on renvoie l'argument)
		when(repository.save(any(Order.class))).thenAnswer(inv -> inv.getArgument(0));

		// Act : exécution du cas d’usage
		Order updated = service.updateStatus(1L, OrderStatus.PAID);

		// Assert : le statut a bien été mis à jour
		assertEquals(OrderStatus.PAID, updated.getStatus());

		// Assert : vérification des interactions avec le repository
		verify(repository).findById(1L);
		verify(repository).save(existing);

		// Assert : aucune interaction supplémentaire inattendue
		verifyNoMoreInteractions(repository);
	}

	/**
	 * Cas d'erreur :
	 *  - la commande n'existe pas
	 *  - le service doit lever OrderNotFoundException
	 *  - aucune sauvegarde ne doit être tentée
	 */
	@Test
	void shouldThrowOrderNotFoundWhenOrderDoesNotExist() {
		// Arrange
		OrderRepositoryPort repository = mock(OrderRepositoryPort.class);
		UpdateOrderStatusService service = new UpdateOrderStatusService(repository);

		// Arrange : aucune commande trouvée
		when(repository.findById(99L)).thenReturn(Optional.empty());

		// Act + Assert : l'exception attendue est levée
		OrderNotFoundException ex = assertThrows(
				OrderNotFoundException.class,
				() -> service.updateStatus(99L, OrderStatus.PAID)
		);

		// Assert : le message d'erreur contient l'information attendue
		assertTrue(ex.getMessage().contains("Commande introuvable"));

		// Assert : le repository a été appelé uniquement pour le findById
		verify(repository).findById(99L);
		verifyNoMoreInteractions(repository);
	}

	/**
	 * Cas d'erreur :
	 *  - la commande existe mais la transition est interdite
	 *  - exemple : PAID -> CREATED (retour en arrière)
	 *  - le service doit lever InvalidOrderStatusTransitionException
	 *  - aucune sauvegarde ne doit être effectuée
	 */
	@Test
	void shouldThrowInvalidTransitionWhenRuleIsViolated() {
		// Arrange
		OrderRepositoryPort repository = mock(OrderRepositoryPort.class);
		UpdateOrderStatusService service = new UpdateOrderStatusService(repository);

		// Arrange : commande existante déjà payée
		Order existing = new Order(1L, OrderStatus.PAID);
		when(repository.findById(1L)).thenReturn(Optional.of(existing));

		// Act + Assert : transition invalide => exception attendue
		InvalidOrderStatusTransitionException ex = assertThrows(
				InvalidOrderStatusTransitionException.class,
				() -> service.updateStatus(1L, OrderStatus.CREATED)
		);

		// Assert : le message indique bien une transition invalide
		assertTrue(ex.getMessage().contains("Transition invalide"));

		// Assert : on a bien recherché la commande
		verify(repository).findById(1L);

		// Assert : on ne doit jamais sauvegarder si la règle métier échoue
		verify(repository, never()).save(any());

		// Assert : aucune interaction supplémentaire inattendue
		verifyNoMoreInteractions(repository);
	}

}
