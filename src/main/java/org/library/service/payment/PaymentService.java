package org.library.service.payment;

import org.library.dto.payment.PaymentRequest;
import org.library.dto.payment.PaymentResponse;

public interface PaymentService {
    PaymentResponse makePayment(String idempotencyKey, PaymentRequest paymentRequest);
}
