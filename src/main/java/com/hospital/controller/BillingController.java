package com.hospital.controller;

import com.hospital.dto.BillingDto;
import com.hospital.service.BillingService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/billing")
@RequiredArgsConstructor
public class BillingController {

    private final BillingService billingService;

    @GetMapping("/billings")
    public ResponseEntity<List<BillingDto>> getAllBillings() {
        return new ResponseEntity<>(billingService.getAllBillings(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<BillingDto> getBillingById(@PathVariable Long id) {
        return new ResponseEntity<>(billingService.getBillingById(id), HttpStatus.OK);
    }

    @PostMapping("/billings")
    public ResponseEntity<BillingDto> createBilling(@Valid @RequestBody BillingDto billingDto) {

            return new ResponseEntity<>(billingService.createBilling(billingDto), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<BillingDto> updateBilling(@PathVariable Long id,@Valid @RequestBody BillingDto billingDto) {


            return new ResponseEntity<>(billingService.updateBilling(id, billingDto), HttpStatus.NO_CONTENT);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteBilling(@PathVariable Long id) {
          billingService.deleteBilling(id);
            return new ResponseEntity<>("billing is deleted", HttpStatus.NO_CONTENT);
    }

}
