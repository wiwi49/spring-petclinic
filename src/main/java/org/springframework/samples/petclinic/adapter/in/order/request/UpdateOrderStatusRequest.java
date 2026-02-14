package org.springframework.samples.petclinic.adapter.in.order.request;

import jakarta.validation.constraints.NotBlank;

/**
 * DTO de requête utilisé pour la mise à jour du statut d'une commande.
 *
 * Ce record :
 *  - représente les données entrantes provenant de l'API (HTTP / JSON)
 *  - est indépendant du domaine métier
 *  - porte les règles de validation côté entrée
 *
 * Exemple de payload JSON attendu :
 * {
 *   "status": "PAID"
 * }
 */
public record UpdateOrderStatusRequest(

	/**
	 * Nouveau statut de la commande sous forme de chaîne de caractères.
	 *
	 * La validation @NotBlank garantit :
	 *  - la présence du champ
	 *  - l'absence de chaîne vide ou composée uniquement d'espaces
	 *
	 * La conversion vers OrderStatus (enum)
	 * est effectuée dans la couche d'adaptation.
	 */
	@NotBlank
	String status
) {
}
