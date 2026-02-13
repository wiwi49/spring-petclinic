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

@SpringBootTest
class OrderFlowIntegrationTest {

    @Autowired
    private WebApplicationContext webApplicationContext;

    private MockMvc mockMvc;

    private static final Pattern ID_PATTERN = Pattern.compile("\"id\"\\s*:\\s*(\\d+)");

    @BeforeEach
    void setUp() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @Test
    void shouldFollowFullOrderStatusFlow() throws Exception {
        long orderId = createOrderAndAssertCreated();

        updateStatusAndAssert(orderId, "PAID");
        updateStatusAndAssert(orderId, "SHIPPED");
        updateStatusAndAssert(orderId, "DELIVERED");
    }

    private long createOrderAndAssertCreated() throws Exception {
        MvcResult result = mockMvc.perform(post("/api/orders"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status").value("CREATED"))
                .andExpect(jsonPath("$.id").exists())
                .andReturn();

        String body = result.getResponse().getContentAsString();
        return extractId(body);
    }

    private void updateStatusAndAssert(long orderId, String newStatus) throws Exception {
        String payload = "{\"status\":\"" + newStatus + "\"}";

        MvcResult result = mockMvc.perform(patch("/api/orders/{id}/status", orderId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payload))
                .andReturn();

        int status = result.getResponse().getStatus();
        String body = result.getResponse().getContentAsString();

        // ✅ Debug lisible si ça rate
        assertEquals(200, status,
                "PATCH a échoué. HTTP=" + status + " Body=" + body);

        assertTrue(body.contains("\"status\""),
                "Réponse attendue en JSON avec status. Body=" + body);

        // On garde des assertions stables sans dépendre d’un parseur JSON
        assertTrue(body.contains("\"status\":\"" + newStatus + "\""),
                "Le status renvoyé n'est pas " + newStatus + ". Body=" + body);
        assertTrue(body.contains("\"id\":" + orderId),
                "L'id renvoyé n'est pas " + orderId + ". Body=" + body);
    }

    private long extractId(String jsonBody) {
        Matcher matcher = ID_PATTERN.matcher(jsonBody);
        assertTrue(matcher.find(), "Réponse JSON invalide : id introuvable. Body=" + jsonBody);
        return Long.parseLong(matcher.group(1));
    }
}
