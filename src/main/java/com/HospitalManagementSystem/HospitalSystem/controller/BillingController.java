package com.HospitalManagementSystem.HospitalSystem.controller;

import com.HospitalManagementSystem.HospitalSystem.dto.BillingDto;
import com.HospitalManagementSystem.HospitalSystem.service.BillingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/billing")
public class BillingController {
    @Autowired
    BillingService billingService;

    @GetMapping("/billings")
    public ResponseEntity<List<BillingDto>> getAllBillings() {
        return new ResponseEntity<>(billingService.getAllBillings(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<BillingDto> getBillingById(@PathVariable Long id) {
        if (billingService.getBillingById(id) != null) {
            return new ResponseEntity<>(billingService.getBillingById(id), HttpStatus.FOUND);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/billings")
    public ResponseEntity<String> createBilling(@RequestBody BillingDto billingDto) {
        if (billingDto != null) {
            billingService.createBilling(billingDto);
            return new ResponseEntity<>("billing is created", HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>("add an accepted data", HttpStatus.NOT_ACCEPTABLE);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> updateBilling(@PathVariable Long id, @RequestBody BillingDto billingDto) {
        if (billingDto != null) {
            billingService.updateBilling(id, billingDto);
            return new ResponseEntity<>("billing is updated", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("add an accepted data", HttpStatus.NOT_ACCEPTABLE);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteBilling(@PathVariable Long id) {
        if (billingService.deleteBilling(id)) {
            return new ResponseEntity<>("billing is deleted", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("billing not found", HttpStatus.NOT_FOUND);
        }
    }

}
