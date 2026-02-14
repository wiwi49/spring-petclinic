package org.springframework.samples.petclinic.domain.order;

import java.util.Objects;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

/**
 * Entité métier représentant une commande.
 *
 * Cette classe est l'Aggregate Root du domaine "Order" :
 *  - elle porte l'état de la commande
 *  - elle garantit l'application des règles métier
 *  - toute modification du statut passe par elle
 */
@Entity
@Table(name = "petclinic_orders")
public class Order {

	/**
	 * Identifiant technique de la commande.
	 * Généré par la base de données.
	 * Null avant la persistance.
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	/**
	 * Statut courant de la commande.
	 * Stocké en base sous forme de String pour plus de lisibilité
	 * et de robustesse face aux évolutions.
	 */
	@Enumerated(EnumType.STRING)
	@Column(name = "status", nullable = false, length = 30)
	private OrderStatus status;

	/**
	 * Constructeur protégé requis par JPA.
	 * Ne doit pas être utilisé directement dans le code métier.
	 */
	protected Order() {
		// requis par JPA
	}

	/**
	 * Constructeur principal du domaine.
	 *
	 * @param id     identifiant de la commande (peut être null avant save)
	 * @param status statut initial de la commande (obligatoire)
	 */
	public Order(Long id, OrderStatus status) {
		this.id = id;
		this.status = Objects.requireNonNull(status, "status ne peut pas être null");
	}

	/**
	 * Factory method pour créer une nouvelle commande
	 * dans son état initial métier.
	 *
	 * @return une commande avec le statut CREATED
	 */
	public static Order createNew() {
		return new Order(null, OrderStatus.CREATED);
	}

	/**
	 * @return l'identifiant de la commande
	 */
	public Long getId() {
		return id;
	}

	/**
	 * @return le statut courant de la commande
	 */
	public OrderStatus getStatus() {
		return status;
	}

	/**
	 * Change le statut de la commande en respectant
	 * les règles métier de transition.
	 *
	 * Méthode utilisée par les services applicatifs
	 * (ex: UpdateOrderStatusService).
	 *
	 * @param newStatus nouveau statut cible
	 * @throws InvalidOrderStatusTransitionException si la transition est interdite
	 */
	public void changeStatus(OrderStatus newStatus) {
		Objects.requireNonNull(newStatus, "newStatus ne peut pas être null");
		validateTransition(this.status, newStatus);
		this.status = newStatus;
	}

	/**
	 * Valide une transition de statut en s'appuyant
	 * sur les règles métier centralisées.
	 *
	 * @param from statut actuel
	 * @param to   statut cible
	 */
	private void validateTransition(OrderStatus from, OrderStatus to) {
		if (!OrderStatusTransitions.isAllowed(from, to)) {
			throw new InvalidOrderStatusTransitionException(
					"Transition invalide: " + from + " -> " + to
			);
		}
	}
}
