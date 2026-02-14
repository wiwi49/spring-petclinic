package org.springframework.samples.petclinic.domain.order;

public class InvalidOrderStatusTransitionException extends RuntimeException {

	public InvalidOrderStatusTransitionException(String message) {
		super(message);
	}

}
