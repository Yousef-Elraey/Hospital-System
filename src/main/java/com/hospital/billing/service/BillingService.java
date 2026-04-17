package com.hospital.billing.service;

import com.hospital.billing.dto.request.CreateBillingRequest;
import com.hospital.billing.dto.request.UpdateBillingRequest;
import com.hospital.billing.dto.response.CreateBillingResponse;
import com.hospital.billing.dto.response.GetBillingResponse;
import com.hospital.billing.dto.response.UpdateBillingResponse;
import com.hospital.entity.Billing;
import com.hospital.entity.Patient;
import com.hospital.common.exception.HospitalBusinessException;
import com.hospital.billing.repository.BillingRepository;
import com.hospital.patient.repository.PatientRepository;
import lombok.RequiredArgsConstructor;
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

    public List<GetBillingResponse> getAllBillings() {
        List<Billing> billings = billingRepository.findAll();
        if(billings.isEmpty()){
            throw new HospitalBusinessException("no billings found");
        }
        List<GetBillingResponse> billingsResponse = new ArrayList<>();
        for (Billing billing : billings) {
            GetBillingResponse billingResponse = new GetBillingResponse();
            billingResponse.setId(billing.getId())
                    .setAmount(billing.getAmount())
                    .setPatient_id(billing.getPatient().getId())
                    .setCreatedBy(billing.getCreatedBy())
                    .setCreatedAt(billing.getCreatedAt())
                    .setUpdatedBy(billing.getUpdatedBy())
                    .setUpdatedAt(billing.getUpdatedAt());
            billingsResponse.add(billingResponse);
        }
     return billingsResponse;
    }

    public GetBillingResponse getBillingById(Long id) {
        Optional<Billing> billing = billingRepository.findById(id);
        if(billing.isEmpty()){
            throw new HospitalBusinessException("no billing found");
        }

            GetBillingResponse billingResponse = new GetBillingResponse();
            billingResponse.setId(billing.get().getId())
                    .setAmount(billing.get().getAmount())
                    .setPatient_id(billing.get().getPatient().getId())
                    .setCreatedBy(billing.get().getCreatedBy())
                    .setCreatedAt(billing.get().getCreatedAt())
                    .setUpdatedBy(billing.get().getUpdatedBy())
                    .setUpdatedAt(billing.get().getUpdatedAt());
            return billingResponse;

    }

    public CreateBillingResponse createBilling(CreateBillingRequest createBillingRequest) {
       Optional<Patient> patient = patientRepository.findById(createBillingRequest.getPatient_id());
        if (patient.isEmpty()) {
            throw new HospitalBusinessException("no patient found");
        }
        Billing billing = new Billing();
        billing.setAmount(createBillingRequest.getAmount())
                .setPatient(patient.get())
                .setUpdatedAt(LocalDateTime.now())
                .setCreatedAt(LocalDateTime.now())
                .setCreatedBy(createBillingRequest.getCreatedBy())
                .setUpdatedBy(createBillingRequest.getUpdatedBy());
        billingRepository.save(billing);
        CreateBillingResponse billingResponse = new CreateBillingResponse();
        billingResponse.setId(billing.getId());
        return billingResponse;
    }

    public UpdateBillingResponse updateBilling(Long id, UpdateBillingRequest billingRequest) {
       Optional<Patient> patient = patientRepository.findById(billingRequest.getPatient_id());
        if (patient.isEmpty()) {
            throw new HospitalBusinessException("no patient found");
        }
        Optional<Billing> billing = billingRepository.findById(id);
        if (billing.isPresent()) {
            Billing dbbilling = billing.get();
            dbbilling.setAmount(billingRequest.getAmount())
                    .setPatient(patient.get())
                    .setUpdatedBy(billingRequest.getUpdatedBy())
                    .setUpdatedAt(LocalDateTime.now());
            billingRepository.save(dbbilling);
            UpdateBillingResponse billingResponse = new UpdateBillingResponse();
            billingResponse.setId(dbbilling.getId());
            return billingResponse;
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
