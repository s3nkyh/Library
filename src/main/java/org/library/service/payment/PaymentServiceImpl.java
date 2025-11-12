package org.library.service.payment;

import lombok.RequiredArgsConstructor;
import org.library.dto.payment.PaymentRequest;
import org.library.dto.payment.PaymentResponse;
import org.library.model.payment.Payment;
import org.library.repo.jpa.payment.PaymentRepo;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {
    private final PaymentRepo paymentRepo;

    @Override
    public PaymentResponse makePayment(String idempotencyKey, PaymentRequest paymentRequest) {
        if (paymentRepo.existsById(idempotencyKey)) {
            throw new RuntimeException("Payment already exists");
        }

        Payment payment = paymentRepo.save(Payment.builder()
                .idempotencyKey(idempotencyKey)
                .amount(paymentRequest.getAmount())
                .status("COMPLETED")
                .build());

        return PaymentResponse.builder()
                .idempotencyKey(payment.getIdempotencyKey())
                .amount(payment.getAmount())
                .status(payment.getStatus())
                .build();
    }
}
