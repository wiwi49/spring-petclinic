package org.springframework.samples.petclinic.adapter.out.order;

import jakarta.persistence.*;
import org.springframework.samples.petclinic.domain.order.OrderStatus;

/**
 * Entité JPA représentant la commande côté persistance.
 *
 * Cette classe :
 *  - appartient à la couche d'adaptation de sortie (adapter out)
 *  - est strictement technique
 *  - ne contient aucune règle métier
 *
 * Elle permet de mapper le domaine Order
 * vers la structure de la base de données.
 */
@Entity
@Table(name = "orders")
public class OrderJpaEntity {

	/**
	 * Identifiant technique généré par la base de données.
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	/**
	 * Statut de la commande persisté en base.
	 *
	 * Stocké sous forme de String pour :
	 *  - la lisibilité
	 *  - la robustesse face aux évolutions de l'enum
	 */
	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private OrderStatus status;

	/**
	 * Constructeur protégé requis par JPA.
	 */
	protected OrderJpaEntity() {
		// JPA
	}

	/**
	 * Constructeur utilisé pour la création
	 * d'une nouvelle entité persistante.
	 *
	 * @param status statut initial de la commande
	 */
	public OrderJpaEntity(OrderStatus status) {
		this.status = status;
	}

	/**
	 * Constructeur complet utilisé pour le mapping
	 * domaine ↔ persistance.
	 *
	 * @param id     identifiant technique
	 * @param status statut de la commande
	 */
	public OrderJpaEntity(Long id, OrderStatus status) {
		this.id = id;
		this.status = status;
	}

	/**
	 * @return identifiant technique
	 */
	public Long getId() {
		return id;
	}

	/**
	 * @return statut courant de la commande
	 */
	public OrderStatus getStatus() {
		return status;
	}

	/**
	 * Met à jour le statut côté persistance.
	 *
	 * @param status nouveau statut
	 */
	public void setStatus(OrderStatus status) {
		this.status = status;
	}
}
