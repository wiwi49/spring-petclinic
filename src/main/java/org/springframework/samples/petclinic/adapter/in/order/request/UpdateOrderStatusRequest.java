package org.springframework.samples.petclinic.adapter.in.order.request;

import jakarta.validation.constraints.NotBlank;

public record UpdateOrderStatusRequest(@NotBlank String status) {
}
