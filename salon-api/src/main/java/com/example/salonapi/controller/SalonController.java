package com.example.salonapi.controller;

import com.example.salonapi.Payment;
import com.example.salonapi.PaymentRequest;
import com.example.salonapi.SalonServiceDetail;
import com.example.salonapi.Slot;
import com.example.salonapi.Ticket;
import com.example.salonapi.repository.PaymentRepository;
import com.example.salonapi.repository.SalonServiceDetailRepository;
import com.example.salonapi.repository.SlotRepository;
import com.example.salonapi.repository.TicketRepository;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import com.stripe.param.PaymentIntentCreateParams;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(path = "/api", produces = "application/json")
@CrossOrigin
public class SalonController {
    private final SlotRepository slotRepository;
    private final SalonServiceDetailRepository salonServiceDetailRepository;
    private final PaymentRepository paymentRepository;
    private final TicketRepository ticketRepository;

    @Autowired
    public SalonController(SlotRepository slotRepository,
                           SalonServiceDetailRepository salonServiceDetailRepository,
                           PaymentRepository paymentRepository,
                           TicketRepository ticketRepository) {
        this.slotRepository = slotRepository;
        this.salonServiceDetailRepository = salonServiceDetailRepository;
        this.paymentRepository = paymentRepository;
        this.ticketRepository = ticketRepository;
    }

    @GetMapping("/services/slots")
    public List<Slot> getSlots() {
        List<Slot> slots = new ArrayList<>();

        this.slotRepository.findAll().forEach(slots::add);
        return slots;
    }

    @GetMapping("/services/availableSlots/{salonServiceId}/{formattedDate}")
    public List<Slot> getAvailableSlots(@PathVariable("salonServiceId") Long salonServiceId,
                                       @PathVariable("formattedDate") String formattedDate) {
        List<Slot> slots = slotRepository.findAvailableSlotsByServiceIdAndSlotFor(salonServiceId, formattedDate);
        return slots;
    }

    @GetMapping("/services/salonServiceDetails")
    public List<SalonServiceDetail> getSalonServiceDetails() {
        List<SalonServiceDetail> salonServiceDetails = new ArrayList<>();

        this.salonServiceDetailRepository.findAll().forEach(salonServiceDetails::add);
        return salonServiceDetails;
    }

    @PostMapping("/payments/initiate")
    public Payment initiatePayment(@RequestBody PaymentRequest paymentRequest) throws StripeException {
        Payment initialPayment = new Payment();

        // Verify if the Slot Id and Salon Service Detail Id are valid
        Optional<Slot> slot = slotRepository.findById(paymentRequest.getSlotId());
        Optional<SalonServiceDetail> salonServiceDetail =
                salonServiceDetailRepository.findById(paymentRequest.getSalonServiceDetailId());

        if (!slot.isPresent()) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "The slot with id = " + paymentRequest.getSlotId() + " does not exist"
            );
        }

        if (!salonServiceDetail.isPresent()) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "The salon service detail with id = " + paymentRequest.getSalonServiceDetailId() +
                            " does not exist"
            );
        }

        Slot retrievedSlot = slot.get();
        SalonServiceDetail retrievedSalonServiceDetail = salonServiceDetail.get();

        initialPayment.setFirstName(paymentRequest.getFirstName());
        initialPayment.setLastName(paymentRequest.getLastName());
        initialPayment.setEmail(paymentRequest.getEmail());
        initialPayment.setPhoneNumber(paymentRequest.getPhone());
        initialPayment.setSlot(retrievedSlot);
        initialPayment.setSelectedService(retrievedSalonServiceDetail);
        initialPayment.setStatusToProcessing();
        Payment savedPayment = paymentRepository.save(initialPayment);

        Stripe.apiKey =
                "sk_test_51I2mOkAgBbEa1gJVXKTuhYrEJ5iDYL91JpJJKstk3uBCyGPwHs5XlkU06aH5A19dZmMYfL87rD2omcdzGvJDdI7r00mjRhlVD5";

        PaymentIntentCreateParams params = PaymentIntentCreateParams.builder()
                .setCurrency("cad")
                .setAmount(retrievedSalonServiceDetail.getPrice())
                .putMetadata("integration_check", "accept_a_payment")
                .build();

        PaymentIntent intent = PaymentIntent.create(params);

        initialPayment.setAmount(retrievedSalonServiceDetail.getPrice());
        initialPayment.setCreated(LocalDateTime.now());

        savedPayment.setIntentId(intent.getId());
        paymentRepository.save(savedPayment);

        retrievedSlot.setStatusToLocked();
        retrievedSlot.setLockedAt(LocalDateTime.now());
        slotRepository.save(retrievedSlot);

        return savedPayment;
    }

    @PutMapping("/payments/confirm/{paymentID}")
    public Ticket confirmPayment(@PathVariable Long paymentID) {
        Optional<Payment> payment = paymentRepository.findById(paymentID);

        if (payment.isPresent()) {
            Payment retrievedPayment = payment.get();
            try {
                Stripe.apiKey =
                        "sk_test_51I2mOkAgBbEa1gJVXKTuhYrEJ5iDYL91JpJJKstk3uBCyGPwHs5XlkU06aH5A19dZmMYfL87rD2omcdzGvJDdI7r00mjRhlVD5";
                PaymentIntent intent = PaymentIntent.retrieve(retrievedPayment.getIntentId());
                retrievedPayment.setStatusToSuccess();
                retrievedPayment.setUpdated(LocalDateTime.now());
                retrievedPayment.setClientSecret(intent.getClientSecret());
                retrievedPayment.getSlot().setStatusToConfirmed();
                retrievedPayment.getSlot().setConfirmedAt(LocalDateTime.now());
                Payment savedPayment = paymentRepository.save(retrievedPayment);

                Ticket ticket = new Ticket();
                ticket.setPayment(savedPayment);
                ticket.setTicketStatusToBooked();

                ticketRepository.save(ticket);
                return ticket;
            } catch (StripeException e) {
                e.printStackTrace();
            }
        }

        return null;
    }
}
