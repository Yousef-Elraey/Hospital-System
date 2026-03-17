package com.hospital.service;

import com.hospital.dto.BillingDto;
import com.hospital.entity.Billing;
import com.hospital.entity.MedicalRecord;
import com.hospital.exception.HospitalBusinessException;
import com.hospital.repository.BillingRepository;
import com.hospital.repository.PatientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class BillingService {
    @Autowired
    BillingRepository billingRepository;
    @Autowired
    PatientRepository patientRepository;

    public List<BillingDto> getAllBillings() {
        List<Billing> billings = billingRepository.findAll();
        List<BillingDto> billingDtos = new ArrayList<>();
        for (Billing billing : billings) {
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
        Billing billing = billingRepository.findById(id).orElse(null);
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
            throw new HospitalBusinessException("billing not found");
        }

    }

    public void createBilling(BillingDto billingDto) {
        if (patientRepository.findById(billingDto.getPatient_id()).isEmpty()) {
            throw new HospitalBusinessException("no patient found");
        }
        Billing billing = new Billing();
        billing.setAmount(billingDto.getAmount())
                .setPatient(patientRepository.findById(billingDto.getPatient_id()).get())
                .setUpdatedAt(LocalDateTime.now())
                .setCreatedAt(LocalDateTime.now())
                .setCreatedBy(billingDto.getCreatedBy())
                .setUpdatedBy(billingDto.getUpdatedBy());
        billingRepository.save(billing);
    }

    public void updateBilling(Long id, BillingDto billingDto) {
        if (patientRepository.findById(billingDto.getPatient_id()).isEmpty()) {
            throw new HospitalBusinessException("no patient found");
        }
        Optional<Billing> billing = billingRepository.findById(id);
        if (billing.isPresent()) {
            Billing dbbilling = billing.get();
            dbbilling.setAmount(billingDto.getAmount())
                    .setPatient(patientRepository.findById(billingDto.getPatient_id()).get())
                    .setUpdatedBy(billingDto.getUpdatedBy())
                    .setUpdatedAt(LocalDateTime.now());
            billingRepository.save(dbbilling);
        } else {
            throw new HospitalBusinessException("no billing found");
        }
    }

    public void deleteBilling(Long id) {
        Optional<Billing> billing = billingRepository.findById(id);
        if (billing.isEmpty())
            throw new HospitalBusinessException("medicalRecord not found");
        else
            billingRepository.deleteById(id);
    }
}
