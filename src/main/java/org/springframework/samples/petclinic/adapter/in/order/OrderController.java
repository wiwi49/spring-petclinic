package org.springframework.samples.petclinic.adapter.in.order;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.samples.petclinic.adapter.in.order.request.UpdateOrderStatusRequest;
import org.springframework.samples.petclinic.adapter.in.order.response.OrderResponse;
import org.springframework.samples.petclinic.application.order.UpdateOrderStatusUseCase;
import org.springframework.samples.petclinic.domain.order.Order;
import org.springframework.samples.petclinic.domain.order.OrderStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final UpdateOrderStatusUseCase updateOrderStatusUseCase;

    public OrderController(UpdateOrderStatusUseCase updateOrderStatusUseCase) {
        this.updateOrderStatusUseCase = updateOrderStatusUseCase;
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<OrderResponse> updateStatus(
            @PathVariable Long id,
            @Valid @RequestBody UpdateOrderStatusRequest request
    ) {
        OrderStatus newStatus = OrderStatus.valueOf(request.status().trim().toUpperCase());
        Order updated = updateOrderStatusUseCase.updateStatus(id, newStatus);
        return ResponseEntity.ok(OrderApiMapper.toResponse(updated));
    }
}
