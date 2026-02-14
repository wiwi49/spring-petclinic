package org.springframework.samples.petclinic.adapter.out.order;

import org.springframework.samples.petclinic.domain.order.Order;

/**
 * Mapper de persistance responsable de la conversion
 * entre les entités JPA et les objets du domaine.
 *
 * Cette classe :
 *  - appartient à la couche adapter out
 *  - empêche toute dépendance directe du domaine vers JPA
 *  - centralise la logique de mapping persistance ↔ domaine
 */
final class OrderPersistenceMapper {

	/**
	 * Convertit une entité JPA en entité du domaine.
	 *
	 * @param entity entité JPA issue de la base de données
	 * @return entité du domaine Order
	 */
	static Order toDomain(OrderJpaEntity entity) {
		return new Order(
				entity.getId(),
				entity.getStatus()
		);
	}

	/**
	 * Convertit une entité du domaine en entité JPA.
	 *
	 * @param order entité métier
	 * @return entité JPA prête à être persistée
	 */
	static OrderJpaEntity toEntity(Order order) {
		return new OrderJpaEntity(
				order.getId(),
				order.getStatus()
		);
	}

	/**
	 * Constructeur privé pour empêcher l'instanciation.
	 * Cette classe est une utility class.
	 */
	private OrderPersistenceMapper() {
		// utility class
	}
}
