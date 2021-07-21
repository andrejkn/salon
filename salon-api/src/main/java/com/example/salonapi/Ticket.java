package com.example.salonapi;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;

@Entity
@Data
@NoArgsConstructor
@ToString
public class Ticket {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ticket_id_seq")
    @SequenceGenerator(name = "ticket_id_seq", sequenceName = "ticket_id_seq", allocationSize = 1)
    private Long id;

    @OneToOne
    Payment payment;

    private TicketStatus ticketStatus;

    public void setTicketStatusToBooked() {
        this.ticketStatus = TicketStatus.BOOKED;
    }
}

enum TicketStatus {
    OPEN, BOOKED
}
