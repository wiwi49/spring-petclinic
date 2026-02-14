package org.springframework.samples.petclinic.adapter.out.order;

import org.springframework.samples.petclinic.application.order.port.out.OrderRepositoryPort;
import org.springframework.samples.petclinic.domain.order.Order;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Adapter de persistance implémentant le port OrderRepositoryPort.
 *
 * Cette classe :
 *  - fait le lien entre la couche application et Spring Data JPA
 *  - implémente le contrat défini par le port de sortie
 *  - encapsule toute la logique liée à la persistance
 *
 * Le domaine ne dépend jamais de Spring Data.
 */
@Repository
public class OrderRepositoryAdapter implements OrderRepositoryPort {

	/**
	 * Repository Spring Data JPA.
	 * Strictement technique.
	 */
	private final SpringDataOrderRepository repository;

	/**
	 * Injection du repository Spring Data.
	 *
	 * @param repository repository JPA
	 */
	public OrderRepositoryAdapter(SpringDataOrderRepository repository) {
		this.repository = repository;
	}

	/**
	 * Recherche une commande par son identifiant.
	 *
	 * La conversion JPA → domaine est effectuée via le mapper dédié.
	 *
	 * @param id identifiant de la commande
	 * @return Optional contenant la commande si elle existe
	 */
	@Override
	public Optional<Order> findById(Long id) {
		return repository
				.findById(id)
				.map(OrderPersistenceMapper::toDomain);
	}

	/**
	 * Sauvegarde une commande du domaine.
	 *
	 * Étapes :
	 *  1. Conversion domaine → entité JPA
	 *  2. Persistance via Spring Data
	 *  3. Conversion entité JPA → domaine
	 *
	 * @param order commande métier
	 * @return commande persistée
	 */
	@Override
	public Order save(Order order) {

		OrderJpaEntity saved = repository.save(
				OrderPersistenceMapper.toEntity(order)
		);

		return OrderPersistenceMapper.toDomain(saved);
	}
}
