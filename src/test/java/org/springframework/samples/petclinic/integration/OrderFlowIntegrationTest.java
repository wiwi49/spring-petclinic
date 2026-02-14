package org.springframework.samples.petclinic.integration;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;

import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

/**
 * Test d'intégration complet du flow de commande.
 *
 * Objectif :
 *  - vérifier le fonctionnement global de l'application
 *    (Controller → UseCase → Domaine → Persistance → REST)
 *  - s'assurer que le cycle de vie d'une commande est respecté
 *
 * Ce test démarre le contexte Spring réel (@SpringBootTest)
 * et utilise MockMvc pour simuler des appels HTTP.
 */
@SpringBootTest
class OrderFlowIntegrationTest {

	@Autowired
	private WebApplicationContext webApplicationContext;

	/**
	 * Client HTTP simulé pour tester les endpoints REST.
	 */
	private MockMvc mockMvc;

	/**
	 * Pattern utilisé pour extraire l'id de la commande
	 * depuis la réponse JSON sans dépendre d'un parseur JSON.
	 */
	private static final Pattern ID_PATTERN =
			Pattern.compile("\"id\"\\s*:\\s*(\\d+)");

	/**
	 * Initialisation de MockMvc avec le contexte Spring réel
	 * avant chaque test.
	 */
	@BeforeEach
	void setUp() {
		this.mockMvc = MockMvcBuilders
				.webAppContextSetup(webApplicationContext)
				.build();
	}

	/**
	 * Scénario end-to-end :
	 *  - création d'une commande
	 *  - passage successif des statuts :
	 *      CREATED → PAID → SHIPPED → DELIVERED
	 *
	 * Ce test valide que tout le flux applicatif
	 * fonctionne correctement.
	 */
	@Test
	void shouldFollowFullOrderStatusFlow() throws Exception {

		// Création de la commande
		long orderId = createOrderAndAssertCreated();

		// Enchaînement des transitions de statut
		updateStatusAndAssert(orderId, "PAID");
		updateStatusAndAssert(orderId, "SHIPPED");
		updateStatusAndAssert(orderId, "DELIVERED");
	}

	/**
	 * Crée une nouvelle commande via l'API
	 * et vérifie que :
	 *  - la réponse est OK
	 *  - le statut initial est CREATED
	 *  - un id est bien généré
	 *
	 * @return l'id de la commande créée
	 */
	private long createOrderAndAssertCreated() throws Exception {

		MvcResult result = mockMvc
				.perform(post("/api/orders"))
				.andExpect(status().isOk())
				.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.status").value("CREATED"))
				.andExpect(jsonPath("$.id").exists())
				.andReturn();

		String body = result.getResponse().getContentAsString();
		return extractId(body);
	}

	/**
	 * Met à jour le statut d'une commande et vérifie
	 * que la réponse est cohérente.
	 *
	 * @param orderId   identifiant de la commande
	 * @param newStatus nouveau statut attendu
	 */
	private void updateStatusAndAssert(long orderId, String newStatus) throws Exception {

		String payload = "{\"status\":\"" + newStatus + "\"}";

		MvcResult result = mockMvc
				.perform(
						patch("/api/orders/{id}/status", orderId)
								.contentType(MediaType.APPLICATION_JSON)
								.content(payload)
				)
				.andReturn();

		int status = result.getResponse().getStatus();
		String body = result.getResponse().getContentAsString();

		// Debug lisible si le test échoue
		assertEquals(200, status,
				"PATCH a échoué. HTTP=" + status + " Body=" + body);

		// Vérifications simples et robustes
		assertTrue(body.contains("\"status\""),
				"Réponse attendue en JSON avec status. Body=" + body);

		assertTrue(body.contains("\"status\":\"" + newStatus + "\""),
				"Le status renvoyé n'est pas " + newStatus + ". Body=" + body);

		assertTrue(body.contains("\"id\":" + orderId),
				"L'id renvoyé n'est pas " + orderId + ". Body=" + body);
	}

	/**
	 * Extrait l'identifiant de la commande
	 * depuis une réponse JSON.
	 *
	 * @param jsonBody corps de la réponse
	 * @return id de la commande
	 */
	private long extractId(String jsonBody) {

		Matcher matcher = ID_PATTERN.matcher(jsonBody);

		assertTrue(matcher.find(),
				"Réponse JSON invalide : id introuvable. Body=" + jsonBody);

		return Long.parseLong(matcher.group(1));
	}
}
