package org.springframework.samples.petclinic.domain.order;

public class InvalidOrderStatusTransitionException extends RuntimeException {

    public InvalidOrderStatusTransitionException(OrderStatus from, OrderStatus to) {
        super("Transition invalide de " + from + " vers " + to);
    }
}
