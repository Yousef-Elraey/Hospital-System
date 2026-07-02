package com.hospital.billing.controller;

import com.hospital.billing.dto.request.CreateBillingRequest;
import com.hospital.billing.dto.request.SearchBillingRequest;
import com.hospital.billing.dto.request.UpdateBillingRequest;
import com.hospital.billing.dto.response.CreateBillingResponse;
import com.hospital.billing.dto.response.GetBillingResponse;
import com.hospital.billing.dto.response.UpdateBillingResponse;
import com.hospital.billing.service.BillingService;
import com.hospital.dto.PageResponse;
import com.hospital.timeSlots.dto.request.SearchTimeSlotsRequest;
import com.hospital.timeSlots.dto.response.GetTimeSlotsResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/billing")
@RequiredArgsConstructor
public class BillingController {

    private final BillingService billingService;

    @GetMapping("/billings")
    public ResponseEntity<PageResponse<GetBillingResponse>> getAllBillings(@RequestParam(defaultValue = "0")int page,
                                                                           @RequestParam(defaultValue = "10")int size,
                                                                           @RequestParam(defaultValue = "id") String sortBy,
                                                                           @RequestParam(defaultValue = "asc") String direction) {
        return new ResponseEntity<>(billingService.getAllBillings(page,size,sortBy,direction), HttpStatus.OK);
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


    @PostMapping("/search")
    public ResponseEntity<PageResponse<GetBillingResponse>> searchBilling(@RequestBody SearchBillingRequest searchBillingRequest,
                                                                          @RequestParam(defaultValue = "0") int page,
                                                                          @RequestParam(defaultValue = "10") int size,
                                                                          @RequestParam(defaultValue = "id") String sortBy,
                                                                          @RequestParam(defaultValue = "asc") String direction) {
        return new ResponseEntity<>(billingService.searchBilling(page, size, sortBy, direction, searchBillingRequest), HttpStatus.OK);
    }

}
