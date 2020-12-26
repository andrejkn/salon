package com.example.salonapi.controller;

import com.example.salonapi.SalonServiceDetail;
import com.example.salonapi.Slot;
import com.example.salonapi.repository.SalonServiceDetailRepository;
import com.example.salonapi.repository.SlotRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(path = "/api/services", produces = "application/json")
@CrossOrigin
public class SalonController {
    private final SlotRepository slotRepository;
    private final SalonServiceDetailRepository salonServiceDetailRepository;

    @Autowired
    public SalonController(SlotRepository slotRepository,
                           SalonServiceDetailRepository salonServiceDetailRepository) {
        this.slotRepository = slotRepository;
        this.salonServiceDetailRepository = salonServiceDetailRepository;
    }

    @GetMapping("/slots")
    public List<Slot> getSlots() {
        List<Slot> slots = new ArrayList<>();

        this.slotRepository.findAll().forEach(slots::add);
        return slots;
    }

    @GetMapping("/availableSlots/{salonServiceId}/{formattedDate}")
    public List<Slot> getAvailableSlots(@PathVariable("salonServiceId") Long salonServiceId,
                                       @PathVariable("formattedDate") String formattedDate) {
        List<Slot> slots = slotRepository.findAvailableSlotsByServiceIdAndSlotFor(salonServiceId, formattedDate);
        return slots;
    }

    @GetMapping("/salonServiceDetails")
    public List<SalonServiceDetail> getSalonServiceDetails() {
        List<SalonServiceDetail> salonServiceDetails = new ArrayList<>();

        this.salonServiceDetailRepository.findAll().forEach(salonServiceDetails::add);
        return salonServiceDetails;
    }
}
