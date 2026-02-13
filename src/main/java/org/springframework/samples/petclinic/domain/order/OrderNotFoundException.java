package org.springframework.samples.petclinic.domain.order;

public class OrderNotFoundException extends RuntimeException {

    public OrderNotFoundException(Long orderId) {
        super("Commande introuvable: " + orderId);
    }
}
