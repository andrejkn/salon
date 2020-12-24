package com.example.salonapi.controller;

import com.example.salonapi.SalonServiceDetail;
import com.example.salonapi.Slot;
import com.example.salonapi.repository.SalonServiceDetailRepository;
import com.example.salonapi.repository.SlotRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(path = "/salon", produces = "application/json")
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

    @GetMapping("/error")
    public String error() {
        return "There is an error!";
    }

    @GetMapping("/slots")
    public List<Slot> getSlots() {
        List<Slot> slots = new ArrayList<>();

        this.slotRepository.findAll().forEach(slots::add);
        return slots;
    }

    @GetMapping("/salonServiceDetails")
    public List<SalonServiceDetail> getSalonServiceDetails() {
        List<SalonServiceDetail> salonServiceDetails = new ArrayList<>();

        this.salonServiceDetailRepository.findAll().forEach(salonServiceDetails::add);
        return salonServiceDetails;
    }
}
