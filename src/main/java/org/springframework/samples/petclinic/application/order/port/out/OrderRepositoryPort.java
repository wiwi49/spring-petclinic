package org.springframework.samples.petclinic.application.order.port.out;

import java.util.Optional;

import org.springframework.samples.petclinic.domain.order.Order;

/**
 * Port de sortie pour l'accès aux données des commandes.
 *
 * Cette interface définit le contrat de persistance du domaine Order
 * sans dépendre d'une technologie spécifique (JPA, JDBC, API externe, etc.).
 *
 * Elle permet :
 *  - de découpler la couche application du stockage
 *  - de faciliter les tests (mock, in-memory repository)
 *  - d'assurer l'évolutivité de l'architecture
 */
public interface OrderRepositoryPort {

	/**
	 * Recherche une commande par son identifiant.
	 *
	 * @param id identifiant de la commande
	 * @return un Optional contenant la commande si elle existe,
	 *         ou vide sinon
	 */
	Optional<Order> findById(Long id);

	/**
	 * Sauvegarde une commande.
	 *
	 * Peut correspondre à :
	 *  - une création si l'id est null
	 *  - une mise à jour si l'id existe déjà
	 *
	 * @param order commande à persister
	 * @return la commande persistée
	 */
	Order save(Order order);
}
