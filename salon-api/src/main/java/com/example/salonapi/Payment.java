package com.example.salonapi;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@ToString
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "payment_id_seq")
    @SequenceGenerator(name = "payment_id_seq", sequenceName = "payment_id_seq", allocationSize = 1)
    private Long id;

    @ManyToOne
    SalonServiceDetail selectedService;

    @OneToOne
    Slot slot;

    Long amount;
    PaymentStatus status;

    @CreatedDate
    LocalDateTime created;

    @LastModifiedDate
    LocalDateTime updated;

    String intentId;
    String clientSecret;

    String firstName;
    String lastName;
    String email;
    String phoneNumber;

    public void setStatusToProcessing() {
        this.setStatus(PaymentStatus.PROCESSING);
    }

    public void setStatusToSuccess() {
        this.setStatus(PaymentStatus.SUCCESS);
    }

    public void setStatusToFailed() {
        this.setStatus(PaymentStatus.FAILED);
    }
}

enum PaymentStatus {
    PROCESSING, SUCCESS, FAILED
}
