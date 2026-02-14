package org.springframework.samples.petclinic.adapter.in.order;

import org.springframework.samples.petclinic.adapter.in.order.response.OrderResponse;
import org.springframework.samples.petclinic.domain.order.Order;

/**
 * Mapper dédié à la transformation des objets du domaine
 * vers des DTOs exposés par l'API.
 *
 * Cette classe :
 *  - appartient à la couche d'adaptation (adapter in)
 *  - évite toute exposition directe des entités du domaine
 *  - centralise la logique de mapping API
 */
final class OrderApiMapper {

	/**
	 * Constructeur privé pour empêcher l'instanciation.
	 * Cette classe est une utility class.
	 */
	private OrderApiMapper() {
	}

	/**
	 * Convertit une entité Order du domaine
	 * en OrderResponse destiné à l'API.
	 *
	 * @param order entité métier
	 * @return DTO de réponse exposé par l'API
	 */
	static OrderResponse toResponse(Order order) {
		return new OrderResponse(
				order.getId(),
				order.getStatus().name()
		);
	}
}
