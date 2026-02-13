package org.springframework.samples.petclinic.domain.order;

import java.util.EnumMap;
import java.util.EnumSet;
import java.util.Map;
import java.util.Set;

/**
 * Règles métier des transitions autorisées.
 * Paquet-private pour rester interne au domaine.
 */
final class OrderStatusTransitions {

    private static final Map<OrderStatus, Set<OrderStatus>> ALLOWED = new EnumMap<>(OrderStatus.class);

    static {
        ALLOWED.put(OrderStatus.CREATED, EnumSet.of(OrderStatus.PAID));
        ALLOWED.put(OrderStatus.PAID, EnumSet.of(OrderStatus.SHIPPED));
        ALLOWED.put(OrderStatus.SHIPPED, EnumSet.of(OrderStatus.DELIVERED));
        ALLOWED.put(OrderStatus.DELIVERED, EnumSet.noneOf(OrderStatus.class));
    }

    static boolean isAllowed(OrderStatus from, OrderStatus to) {
        return ALLOWED.getOrDefault(from, EnumSet.noneOf(OrderStatus.class)).contains(to);
    }

    private OrderStatusTransitions() {
        // utility class
    }
}
