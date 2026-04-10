package com.hospital.service;

import com.hospital.dto.BillingDto;
import com.hospital.entity.Billing;
import com.hospital.entity.MedicalRecord;
import com.hospital.entity.Patient;
import com.hospital.exception.HospitalBusinessException;
import com.hospital.repository.BillingRepository;
import com.hospital.repository.PatientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BillingService {
   private final BillingRepository billingRepository;
   private final PatientRepository patientRepository;

    public List<BillingDto> getAllBillings() {
        List<Billing> billings = billingRepository.findAll();
        if(billings.isEmpty()){
            throw new HospitalBusinessException("no billings found");
        }
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
        Optional<Billing> billing = billingRepository.findById(id);
        if(billing.isEmpty()){
            throw new HospitalBusinessException("no billing found");
        }

            BillingDto billingDto = new BillingDto();
            billingDto.setId(billing.get().getId())
                    .setAmount(billing.get().getAmount())
                    .setPatient_id(billing.get().getPatient().getId())
                    .setCreatedBy(billing.get().getCreatedBy())
                    .setCreatedAt(billing.get().getCreatedAt())
                    .setUpdatedBy(billing.get().getUpdatedBy())
                    .setUpdatedAt(billing.get().getUpdatedAt());
            return billingDto;

    }

    public BillingDto createBilling(BillingDto billingDto) {
       Optional<Patient> patient = patientRepository.findById(billingDto.getPatient_id());
        if (patient.isEmpty()) {
            throw new HospitalBusinessException("no patient found");
        }
        Billing billing = new Billing();
        billing.setAmount(billingDto.getAmount())
                .setPatient(patient.get())
                .setUpdatedAt(LocalDateTime.now())
                .setCreatedAt(LocalDateTime.now())
                .setCreatedBy(billingDto.getCreatedBy())
                .setUpdatedBy(billingDto.getUpdatedBy());
        billingRepository.save(billing);
        return billingDto;
    }

    public BillingDto updateBilling(Long id, BillingDto billingDto) {
       Optional<Patient> patient = patientRepository.findById(billingDto.getPatient_id());
        if (patient.isEmpty()) {
            throw new HospitalBusinessException("no patient found");
        }
        Optional<Billing> billing = billingRepository.findById(id);
        if (billing.isPresent()) {
            Billing dbbilling = billing.get();
            dbbilling.setAmount(billingDto.getAmount())
                    .setPatient(patient.get())
                    .setUpdatedBy(billingDto.getUpdatedBy())
                    .setUpdatedAt(LocalDateTime.now());
            billingRepository.save(dbbilling);
        } else {
            throw new HospitalBusinessException("no billing found");
        }
        return billingDto;
    }

    public void deleteBilling(Long id) {
        Optional<Billing> billing = billingRepository.findById(id);
        if (billing.isEmpty())
            throw new HospitalBusinessException("medicalRecord not found");
        else
            billingRepository.deleteById(id);
    }
}
