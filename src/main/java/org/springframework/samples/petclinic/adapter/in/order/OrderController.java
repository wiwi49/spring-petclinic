package org.springframework.samples.petclinic.adapter.in.order;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.samples.petclinic.adapter.in.order.request.UpdateOrderStatusRequest;
import org.springframework.samples.petclinic.adapter.in.order.response.OrderResponse;
import org.springframework.samples.petclinic.application.order.port.in.CreateOrderUseCase;
import org.springframework.samples.petclinic.application.order.UpdateOrderStatusUseCase;
import org.springframework.samples.petclinic.domain.order.Order;
import org.springframework.samples.petclinic.domain.order.OrderStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final CreateOrderUseCase createOrderUseCase;
    private final UpdateOrderStatusUseCase updateOrderStatusUseCase;

    public OrderController(CreateOrderUseCase createOrderUseCase,
                           UpdateOrderStatusUseCase updateOrderStatusUseCase) {
        this.createOrderUseCase = createOrderUseCase;
        this.updateOrderStatusUseCase = updateOrderStatusUseCase;
    }

    @PostMapping
    public ResponseEntity<OrderResponse> createOrder() {
        Order created = createOrderUseCase.create();
        return ResponseEntity.ok(OrderApiMapper.toResponse(created));
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<OrderResponse> updateStatus(
            @PathVariable("id") Long id,
            @Valid @RequestBody UpdateOrderStatusRequest request
    ) {
        OrderStatus newStatus = parseStatus(request.status());
        Order updated = updateOrderStatusUseCase.updateStatus(id, newStatus);
        return ResponseEntity.ok(OrderApiMapper.toResponse(updated));
    }

    private OrderStatus parseStatus(String rawStatus) {
        return OrderStatus.valueOf(rawStatus.trim().toUpperCase());
    }
}
