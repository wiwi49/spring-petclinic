package org.springframework.samples.petclinic.application.order.service;

import org.springframework.samples.petclinic.application.order.port.in.CreateOrderUseCase;
import org.springframework.samples.petclinic.application.order.port.out.OrderRepositoryPort;
import org.springframework.samples.petclinic.domain.order.Order;
import org.springframework.samples.petclinic.domain.order.OrderStatus;
import org.springframework.stereotype.Service;

/**
 * Service applicatif chargé de la création d'une commande.
 *
 * Ce service :
 *  - implémente un cas d'usage métier explicite
 *  - orchestre la création et la persistance de l'entité Order
 *  - ne contient aucune logique métier complexe
 *
 * L'état initial de la commande est défini par le domaine.
 */
@Service
public class CreateOrderService implements CreateOrderUseCase {

	/**
	 * Port de sortie vers la couche de persistance.
	 * Permet de découpler la logique applicative
	 * de l'implémentation technique (JPA, etc.).
	 */
	private final OrderRepositoryPort orderRepositoryPort;

	/**
	 * Injection du repository via le constructeur
	 * (injection de dépendances recommandée).
	 *
	 * @param orderRepositoryPort port de persistance des commandes
	 */
	public CreateOrderService(OrderRepositoryPort orderRepositoryPort) {
		this.orderRepositoryPort = orderRepositoryPort;
	}

	/**
	 * Crée une nouvelle commande dans son état initial.
	 *
	 * Étapes du cas d'usage :
	 *  1. Instanciation de l'entité Order avec son statut initial
	 *  2. Persistance de la commande
	 *
	 * @return la commande nouvellement créée
	 */
	@Override
	public Order create() {

		// Création d'une nouvelle commande avec le statut métier initial
		Order newOrder = new Order(null, OrderStatus.CREATED);
		// (équivalent possible : Order.createNew())

		// Sauvegarde et retour de la commande persistée
		return orderRepositoryPort.save(newOrder);
	}
}
