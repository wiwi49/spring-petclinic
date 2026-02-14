package org.springframework.samples.petclinic.adapter.in.order;

import org.springframework.http.ResponseEntity;
import org.springframework.samples.petclinic.adapter.in.order.request.UpdateOrderStatusRequest;
import org.springframework.samples.petclinic.adapter.in.order.response.OrderResponse;
import org.springframework.samples.petclinic.application.order.UpdateOrderStatusUseCase;
import org.springframework.samples.petclinic.application.order.port.in.CreateOrderUseCase;
import org.springframework.samples.petclinic.domain.order.Order;
import org.springframework.samples.petclinic.domain.order.OrderStatus;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;

/**
 * Contrôleur REST exposant les endpoints liés aux commandes.
 *
 * Ce contrôleur :
 *  - appartient à la couche d'adaptation (adapter in)
 *  - traduit les requêtes HTTP en appels de cas d'usage
 *  - ne contient aucune logique métier
 */
@RestController
@RequestMapping("/api/orders")
public class OrderController {

	/**
	 * Cas d'usage de création de commande.
	 */
	private final CreateOrderUseCase createOrderUseCase;

	/**
	 * Cas d'usage de mise à jour du statut d'une commande.
	 */
	private final UpdateOrderStatusUseCase updateOrderStatusUseCase;

	/**
	 * Injection des cas d'usage via le constructeur.
	 *
	 * @param createOrderUseCase        cas d'usage de création
	 * @param updateOrderStatusUseCase  cas d'usage de mise à jour du statut
	 */
	public OrderController(
			CreateOrderUseCase createOrderUseCase,
			UpdateOrderStatusUseCase updateOrderStatusUseCase) {

		this.createOrderUseCase = createOrderUseCase;
		this.updateOrderStatusUseCase = updateOrderStatusUseCase;
	}

	/**
	 * Endpoint de création d'une nouvelle commande.
	 *
	 * POST /api/orders
	 *
	 * @return la commande créée
	 */
	@PostMapping
	public ResponseEntity<OrderResponse> createOrder() {

		// Appel du cas d'usage applicatif
		Order created = createOrderUseCase.create();

		// Mapping domaine -> DTO API
		return ResponseEntity.ok(OrderApiMapper.toResponse(created));
	}

	/**
	 * Endpoint de mise à jour du statut d'une commande.
	 *
	 * PATCH /api/orders/{id}/status
	 *
	 * @param id      identifiant de la commande
	 * @param request corps de la requête validé
	 * @return la commande mise à jour
	 */
	@PatchMapping("/{id}/status")
	public ResponseEntity<OrderResponse> updateStatus(
			@PathVariable("id") Long id,
			@Valid @RequestBody UpdateOrderStatusRequest request) {

		// Conversion sécurisée du statut reçu en enum métier
		OrderStatus newStatus = parseStatus(request.status());

		// Appel du cas d'usage applicatif
		Order updated = updateOrderStatusUseCase.updateStatus(id, newStatus);

		// Mapping domaine -> DTO API
		return ResponseEntity.ok(OrderApiMapper.toResponse(updated));
	}

	/**
	 * Convertit une chaîne de caractères en OrderStatus.
	 *
	 * Normalise l'entrée utilisateur (trim + uppercase)
	 * afin de rendre l'API plus tolérante.
	 *
	 * @param rawStatus statut brut reçu depuis l'API
	 * @return enum OrderStatus correspondant
	 * @throws IllegalArgumentException si le statut est invalide
	 */
	private OrderStatus parseStatus(String rawStatus) {
		return OrderStatus.valueOf(rawStatus.trim().toUpperCase());
	}
}
