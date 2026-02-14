package org.springframework.samples.petclinic.application.order;

import org.springframework.samples.petclinic.domain.order.Order;
import org.springframework.samples.petclinic.domain.order.OrderStatus;

/**
 * Cas d’usage applicatif pour la mise à jour du statut d’une commande.
 *
 * Cette interface représente une frontière d’entrée (port d’entrée) :
 *  - exposée à la couche de présentation (REST, UI, etc.)
 *  - indépendante de toute implémentation technique
 *
 * Elle décrit ce que l’application sait faire,
 * sans expliquer comment c’est fait.
 */
public interface UpdateOrderStatusUseCase {

	/**
	 * Met à jour le statut d’une commande existante.
	 *
	 * @param orderId   identifiant de la commande
	 * @param newStatus nouveau statut à appliquer
	 * @return la commande mise à jour
	 *
	 * @throws org.springframework.samples.petclinic.domain.order.OrderNotFoundException
	 *         si la commande n'existe pas
	 * @throws org.springframework.samples.petclinic.domain.order.InvalidOrderStatusTransitionException
	 *         si la transition est interdite par les règles métier
	 */
	Order updateStatus(Long orderId, OrderStatus newStatus);

}
