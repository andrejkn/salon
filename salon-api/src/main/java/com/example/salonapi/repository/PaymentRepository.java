package com.example.salonapi.repository;

import com.example.salonapi.Payment;
import org.springframework.data.repository.CrudRepository;

public interface PaymentRepository extends CrudRepository<Payment, Long> {
}
