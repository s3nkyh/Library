package org.library.dto.payment;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class PaymentResponse {
    private String idempotencyKey;
    private Double amount;
    private String status;
}
