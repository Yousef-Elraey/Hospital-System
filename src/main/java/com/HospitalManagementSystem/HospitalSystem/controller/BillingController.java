package com.HospitalManagementSystem.HospitalSystem.controller;

import com.HospitalManagementSystem.HospitalSystem.dto.BillingDto;
import com.HospitalManagementSystem.HospitalSystem.entity.Billing;
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
    BillingService service;

 @GetMapping("/getAllBillings")
      public ResponseEntity<List<BillingDto>> getAllBillings(){
        return new ResponseEntity<>(service.getAllBillings(), HttpStatus.OK);
    }
@GetMapping("/getBillingById/{id}")
    public ResponseEntity<BillingDto> getBillingById(@PathVariable Long id){
        if (service.getBillingById(id) != null){
            return new ResponseEntity<>(service.getBillingById(id),HttpStatus.FOUND);
        }else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
}
@PostMapping("/createBilling")
    public ResponseEntity<String> createBilling(@RequestBody BillingDto billingDto){
     if (billingDto != null) {
         service.createBilling(billingDto);
         return new ResponseEntity<>("billing is created", HttpStatus.CREATED);
     }else {
         return new ResponseEntity<>("add an accepted data",HttpStatus.NOT_ACCEPTABLE);
     }
}
@PutMapping("/updateBilling/{id}")
    public ResponseEntity<String> updateBilling(@PathVariable Long id, @RequestBody BillingDto billingDto){
     if (billingDto != null){
         service.updateBilling(id,billingDto);
         return new ResponseEntity<>("billing is updated",HttpStatus.OK);
     }else {
         return new ResponseEntity<>("add an accepted data",HttpStatus.NOT_ACCEPTABLE);
     }
}
@DeleteMapping("/deleteBilling/{id}")
    public ResponseEntity<String> deleteBilling(@PathVariable Long id){
     if (service.deleteBilling(id)){
         return new ResponseEntity<>("billing is deleted",HttpStatus.OK);
     }else {
         return new ResponseEntity<>("billing not found",HttpStatus.NOT_FOUND);
     }
}

}
