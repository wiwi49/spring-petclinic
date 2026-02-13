package org.springframework.samples.petclinic.adapter.in.order.request;

import jakarta.validation.constraints.NotNull;

public record UpdateOrderStatusRequest(
        @NotNull String status
) {}
