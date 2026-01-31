package com.HospitalManagementSystem.HospitalSystem.service;

import com.HospitalManagementSystem.HospitalSystem.dto.BillingDto;
import com.HospitalManagementSystem.HospitalSystem.entity.Billing;
import com.HospitalManagementSystem.HospitalSystem.repository.BillingRepository;
import com.HospitalManagementSystem.HospitalSystem.repository.PatientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class BillingService {
   @Autowired
    BillingRepository repo;
   @Autowired
   PatientRepository patientRepository;
    public List<BillingDto> getAllBillings() {
       List<Billing> billings = repo.findAll();
       List<BillingDto> billingDtos = new ArrayList<>();
        for (Billing billing:billings) {
            BillingDto billingDto = new BillingDto();
            billingDto.setId(billing.getId())
                    .setAmount(billing.getAmount())
                    .setPatient_id(billing.getPatient().getId())
                    .setCreatedBy(billing.getCreatedBy())
                    .setCreatedAt(billing.getCreatedAt())
                    .setUpdatedBy(billing.getUpdatedBy())
                    .setUpdatedAt(billing.getUpdatedAt());
            billingDtos.add(billingDto);
        }
     return billingDtos;
    }

    public BillingDto getBillingById(Long id) {
       Billing billing = repo.findById(id).orElse(null);
       if (billing != null){
           BillingDto billingDto = new BillingDto();
           billingDto.setId(billing.getId())
                   .setAmount(billing.getAmount())
                   .setPatient_id(billing.getPatient().getId())
                   .setCreatedBy(billing.getCreatedBy())
                   .setCreatedAt(billing.getCreatedAt())
                   .setUpdatedBy(billing.getUpdatedBy())
                   .setUpdatedAt(billing.getUpdatedAt());
           return billingDto;
       }else {
           return null;
       }

    }

    public void createBilling(BillingDto billingDto) {
        Billing billing = new Billing();
        billing.setAmount(billingDto.getAmount())
                .setPatient(patientRepository.findById(billingDto.getPatient_id()).get())
                .setUpdatedAt(LocalDateTime.now())
                .setCreatedAt(LocalDateTime.now())
                .setCreatedBy(billingDto.getCreatedBy())
                .setUpdatedBy(billingDto.getUpdatedBy());
        repo.save(billing);
    }

    public void updateBilling(Long id, BillingDto billingDto) {
       Optional<Billing> billing = repo.findById(id);
       if (billing.isPresent()){
           Billing dbbilling = billing.get();
           dbbilling.setAmount(billingDto.getAmount())
                   .setPatient(patientRepository.findById(billingDto.getPatient_id()).get())
                   .setUpdatedBy(billingDto.getUpdatedBy())
                   .setUpdatedAt(LocalDateTime.now());
           repo.save(dbbilling);
       }else {
           createBilling(billingDto);
       }
    }

    public boolean deleteBilling(Long id) {
        if (repo.findById(id).isPresent()){
            repo.deleteById(id);
            return true;
        }else {
            return false;
        }
    }
}
