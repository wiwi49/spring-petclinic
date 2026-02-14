package org.springframework.samples.petclinic.application.order.service;

import org.springframework.samples.petclinic.application.order.UpdateOrderStatusUseCase;
import org.springframework.samples.petclinic.application.order.port.out.OrderRepositoryPort;
import org.springframework.samples.petclinic.domain.order.Order;
import org.springframework.samples.petclinic.domain.order.OrderNotFoundException;
import org.springframework.samples.petclinic.domain.order.OrderStatus;
import org.springframework.stereotype.Service;

/**
 * Service applicatif responsable de la mise à jour du statut d'une commande.
 *
 * Ce service :
 *  - implémente un cas d'usage applicatif
 *  - orchestre les appels au domaine
 *  - ne contient aucune règle métier
 *
 * Toute la logique métier (validation des transitions)
 * est déléguée à l'entité Order.
 */
@Service
public class UpdateOrderStatusService implements UpdateOrderStatusUseCase {

	/**
	 * Port de sortie vers la couche de persistance.
	 * Permet de découpler le domaine de l'implémentation technique
	 * (JPA, JDBC, API externe, etc.).
	 */
	private final OrderRepositoryPort orderRepository;

	/**
	 * Injection du repository via le constructeur
	 * (injection de dépendances recommandée).
	 *
	 * @param orderRepository port de persistance des commandes
	 */
	public UpdateOrderStatusService(OrderRepositoryPort orderRepository) {
		this.orderRepository = orderRepository;
	}

	/**
	 * Met à jour le statut d'une commande existante.
	 *
	 * Étapes du cas d'usage :
	 *  1. Chargement de la commande
	 *  2. Validation et application du changement d'état (domaine)
	 *  3. Persistance de la commande mise à jour
	 *
	 * @param orderId   identifiant de la commande
	 * @param newStatus nouveau statut à appliquer
	 * @return la commande mise à jour
	 *
	 * @throws OrderNotFoundException si la commande n'existe pas
	 * @throws InvalidOrderStatusTransitionException si la transition est interdite
	 */
	@Override
	public Order updateStatus(Long orderId, OrderStatus newStatus) {

		// Récupération de la commande ou exception si inexistante
		Order order = orderRepository
				.findById(orderId)
				.orElseThrow(() -> new OrderNotFoundException(orderId));

		// Application du changement de statut (logique métier dans le domaine)
		order.changeStatus(newStatus);

		// Sauvegarde et retour de la commande mise à jour
		return orderRepository.save(order);
	}
}
