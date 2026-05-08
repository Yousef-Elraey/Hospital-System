package com.hospital.billing.controller;

import com.hospital.billing.dto.request.CreateBillingRequest;
import com.hospital.billing.dto.request.UpdateBillingRequest;
import com.hospital.billing.dto.response.CreateBillingResponse;
import com.hospital.billing.dto.response.GetBillingResponse;
import com.hospital.billing.dto.response.UpdateBillingResponse;
import com.hospital.billing.service.BillingService;
import jakarta.servlet.http.HttpServletRequest;
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
    public ResponseEntity<List<GetBillingResponse>> getAllBillings() {
        return new ResponseEntity<>(billingService.getAllBillings(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<GetBillingResponse> getBillingById(@PathVariable Long id) {
        return new ResponseEntity<>(billingService.getBillingById(id), HttpStatus.OK);
    }

    @PostMapping("/billings")
    public ResponseEntity<CreateBillingResponse> createBilling(@Valid @RequestBody CreateBillingRequest createBillingRequest) {

        return new ResponseEntity<>(billingService.createBilling(createBillingRequest), HttpStatus.CREATED);
    }

    @PutMapping("/update")
    public ResponseEntity<UpdateBillingResponse> updateBilling(@Valid @RequestBody UpdateBillingRequest billingRequest) {
        return new ResponseEntity<>(billingService.updateBilling(billingRequest), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteBilling(@PathVariable Long id) {
        billingService.deleteBilling(id);
        return new ResponseEntity<>("billing is deleted", HttpStatus.NO_CONTENT);
    }

}
