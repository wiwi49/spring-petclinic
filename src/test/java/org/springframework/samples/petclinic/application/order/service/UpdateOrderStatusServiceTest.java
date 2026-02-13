package org.springframework.samples.petclinic.application.order.service;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import org.springframework.samples.petclinic.application.order.port.out.OrderRepositoryPort;
import org.springframework.samples.petclinic.domain.order.InvalidOrderStatusTransitionException;
import org.springframework.samples.petclinic.domain.order.Order;
import org.springframework.samples.petclinic.domain.order.OrderNotFoundException;
import org.springframework.samples.petclinic.domain.order.OrderStatus;

class UpdateOrderStatusServiceTest {

    @Test
    void shouldUpdateStatusAndSaveWhenTransitionIsValid() {
        OrderRepositoryPort repository = mock(OrderRepositoryPort.class);
        UpdateOrderStatusService service = new UpdateOrderStatusService(repository);

        Order existing = new Order(1L, OrderStatus.CREATED);
        when(repository.findById(1L)).thenReturn(Optional.of(existing));
        when(repository.save(any(Order.class))).thenAnswer(inv -> inv.getArgument(0));

        Order updated = service.updateStatus(1L, OrderStatus.PAID);

        assertEquals(OrderStatus.PAID, updated.getStatus());
        verify(repository).findById(1L);
        verify(repository).save(existing);
        verifyNoMoreInteractions(repository);
    }

    @Test
    void shouldThrowOrderNotFoundWhenOrderDoesNotExist() {
        OrderRepositoryPort repository = mock(OrderRepositoryPort.class);
        UpdateOrderStatusService service = new UpdateOrderStatusService(repository);

        when(repository.findById(99L)).thenReturn(Optional.empty());

        OrderNotFoundException ex = assertThrows(
                OrderNotFoundException.class,
                () -> service.updateStatus(99L, OrderStatus.PAID)
        );

        assertTrue(ex.getMessage().contains("Commande introuvable"));

        verify(repository).findById(99L);
        verifyNoMoreInteractions(repository);
    }

    @Test
    void shouldThrowInvalidTransitionWhenRuleIsViolated() {
        OrderRepositoryPort repository = mock(OrderRepositoryPort.class);
        UpdateOrderStatusService service = new UpdateOrderStatusService(repository);

        Order existing = new Order(1L, OrderStatus.PAID);
        when(repository.findById(1L)).thenReturn(Optional.of(existing));

        InvalidOrderStatusTransitionException ex = assertThrows(
                InvalidOrderStatusTransitionException.class,
                () -> service.updateStatus(1L, OrderStatus.CREATED)
        );

        assertTrue(ex.getMessage().contains("Transition invalide"));

        verify(repository).findById(1L);
        verify(repository, never()).save(any());
        verifyNoMoreInteractions(repository);
    }
}
