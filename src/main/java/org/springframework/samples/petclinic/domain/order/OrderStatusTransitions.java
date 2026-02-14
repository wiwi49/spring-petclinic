package org.springframework.samples.petclinic.domain.order;

import java.util.EnumMap;
import java.util.EnumSet;
import java.util.Map;
import java.util.Set;

/**
 * Définit les règles métier des transitions autorisées entre les statuts
 * d'une commande.
 *
 * Classe package-private afin de rester interne au domaine
 * (non exposée à l'extérieur).
 *
 * Cette implémentation centralise les règles de transition pour :
 *  - garantir la cohérence des changements d'état
 *  - éviter les transitions illégales
 *  - faciliter la maintenance et l'évolution des règles métier
 */
final class OrderStatusTransitions {

	/**
	 * Map associant chaque statut à l'ensemble des statuts vers lesquels
	 * une transition est autorisée.
	 *
	 * EnumMap est utilisé pour :
	 *  - de meilleures performances
	 *  - une consommation mémoire réduite
	 */
	private static final Map<OrderStatus, Set<OrderStatus>> ALLOWED =
			new EnumMap<>(OrderStatus.class);

	static {
		// Une commande nouvellement créée peut uniquement être payée
		ALLOWED.put(OrderStatus.CREATED, EnumSet.of(OrderStatus.PAID));

		// Une commande payée peut être expédiée
		ALLOWED.put(OrderStatus.PAID, EnumSet.of(OrderStatus.SHIPPED));

		// Une commande expédiée peut être livrée
		ALLOWED.put(OrderStatus.SHIPPED, EnumSet.of(OrderStatus.DELIVERED));

		// Une commande livrée est un état final (aucune transition possible)
		ALLOWED.put(OrderStatus.DELIVERED, EnumSet.noneOf(OrderStatus.class));
	}

	/**
	 * Vérifie si une transition entre deux statuts est autorisée.
	 *
	 * @param from statut actuel de la commande
	 * @param to   statut cible
	 * @return true si la transition est valide, false sinon
	 */
	static boolean isAllowed(OrderStatus from, OrderStatus to) {
		return ALLOWED
				.getOrDefault(from, EnumSet.noneOf(OrderStatus.class))
				.contains(to);
	}

	/**
	 * Constructeur privé pour empêcher l'instanciation.
	 * Cette classe est une utility class.
	 */
	private OrderStatusTransitions() {
		// utility class
	}
}
