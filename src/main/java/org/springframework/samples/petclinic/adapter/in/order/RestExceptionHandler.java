package org.springframework.samples.petclinic.adapter.in.order;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.samples.petclinic.domain.order.InvalidOrderStatusTransitionException;
import org.springframework.samples.petclinic.domain.order.OrderNotFoundException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * Gestionnaire global des exceptions REST.
 *
 * Cette classe :
 *  - intercepte les exceptions levées dans les controllers
 *  - traduit les erreurs métier et techniques en réponses HTTP adaptées
 *  - garantit une API cohérente et lisible côté client
 */
@RestControllerAdvice
public class RestExceptionHandler {

	/**
	 * Gestion du cas où une commande n'existe pas.
	 *
	 * @param ex exception métier levée par le domaine
	 * @return réponse HTTP 404 avec message d'erreur
	 */
	@ExceptionHandler(OrderNotFoundException.class)
	public ResponseEntity<Map<String, String>> handleNotFound(OrderNotFoundException ex) {
		return ResponseEntity
				.status(HttpStatus.NOT_FOUND)
				.body(Map.of("error", ex.getMessage()));
	}

	/**
	 * Gestion des transitions de statut invalides.
	 *
	 * @param ex exception métier liée aux règles de transition
	 * @return réponse HTTP 400 (Bad Request)
	 */
	@ExceptionHandler(InvalidOrderStatusTransitionException.class)
	public ResponseEntity<Map<String, String>> handleInvalidTransition(
			InvalidOrderStatusTransitionException ex) {

		return ResponseEntity
				.status(HttpStatus.BAD_REQUEST)
				.body(Map.of("error", ex.getMessage()));
	}

	/**
	 * Gestion des erreurs de requête génériques.
	 *
	 * Exemple :
	 *  - valeur de statut inconnue (OrderStatus.valueOf)
	 *
	 * @param ex exception levée lors du parsing ou du mapping
	 * @return réponse HTTP 400 avec message explicite
	 */
	@ExceptionHandler(IllegalArgumentException.class)
	public ResponseEntity<Map<String, String>> handleBadRequest(IllegalArgumentException ex) {

		return ResponseEntity
				.status(HttpStatus.BAD_REQUEST)
				.body(Map.of("error", "Requête invalide: " + ex.getMessage()));
	}

	/**
	 * Gestion des erreurs de validation des DTOs (@Valid).
	 *
	 * Exemple :
	 *  - champ obligatoire manquant
	 *  - champ vide ou invalide
	 *
	 * @param ex exception levée par le mécanisme de validation
	 * @return réponse HTTP 400 avec message générique
	 */
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<Map<String, String>> handleValidation(MethodArgumentNotValidException ex) {

		return ResponseEntity
				.status(HttpStatus.BAD_REQUEST)
				.body(Map.of("error", "Validation échouée"));
	}
}
