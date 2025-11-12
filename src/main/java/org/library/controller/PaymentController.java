package org.library.controller;

import lombok.RequiredArgsConstructor;
import org.library.dto.payment.PaymentRequest;
import org.library.dto.payment.PaymentResponse;
import org.library.service.payment.PaymentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/payments")
public class PaymentController {
    private final PaymentService paymentService;

    @PostMapping()
    public ResponseEntity<?> createPayment(
            @RequestHeader("Idempotency-Key") String idempotencyKey,
            @RequestBody PaymentRequest paymentRequest
    ){
        PaymentResponse paymentResponse = paymentService.makePayment(idempotencyKey, paymentRequest);
        return ResponseEntity.ok(paymentResponse);
    }
}

