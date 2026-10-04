package com.hospital.billing.service;

import com.hospital.appointment.specification.AppointmentSpecification;
import com.hospital.billing.dto.request.CreateBillingRequest;
import com.hospital.billing.dto.request.SearchBillingRequest;
import com.hospital.billing.dto.request.UpdateBillingRequest;
import com.hospital.billing.dto.response.CreateBillingResponse;
import com.hospital.billing.dto.response.GetBillingResponse;
import com.hospital.billing.dto.response.SearchBillingResponse;
import com.hospital.billing.dto.response.UpdateBillingResponse;
import com.hospital.billing.repository.BillingRepository;
import com.hospital.billing.specification.BillingSpecification;
import com.hospital.common.exception.ErrorCode;
import com.hospital.common.exception.HospitalBusinessException;
import com.hospital.common.security.JwtService;
import com.hospital.dto.PageResponse;
import com.hospital.entity.Appointment;
import com.hospital.entity.Billing;
import com.hospital.entity.Patient;
import com.hospital.patient.repository.PatientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
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
    private final JwtService jwtService;

    public PageResponse<GetBillingResponse> searchBilling(SearchBillingRequest searchBillingRequest,
                                                           int page, int size, String sortBy, String direction) {
        Sort sort = direction.equalsIgnoreCase("desc")
                ? Sort.by(sortBy).descending()
                : Sort.by(sortBy).ascending();
        Pageable pageable = PageRequest.of(page, size, sort);

        Specification<Billing> specification  = Specification.where(null);
        specification = specification
                .and(BillingSpecification.hasPatientId(searchBillingRequest.getPatientId()))
                .and(BillingSpecification.hasAmount(searchBillingRequest.getAmount()));


        Page<Billing> billingPage = billingRepository.findAll(specification, pageable);
        List<Billing> billings = billingPage.getContent();
        if (billings.isEmpty()) {
            PageResponse.<GetBillingResponse>builder()
                    .data(new ArrayList<>())
                    .page(billingPage.getNumber())
                    .size(billingPage.getSize())
                    .totalElements(billingPage.getTotalElements())
                    .totalPages(billingPage.getTotalPages())
                    .first(billingPage.isFirst())
                    .last(billingPage.isLast())
                    .build();
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
     return PageResponse.<GetBillingResponse>builder()
             .data(billingsResponse)
             .page(billingPage.getNumber())
             .size(billingPage.getSize())
             .totalElements(billingPage.getTotalElements())
             .totalPages(billingPage.getTotalPages())
             .first(billingPage.isFirst())
             .last(billingPage.isLast())
             .build();
    }

    public GetBillingResponse getBillingById(Long id) {
        Optional<Billing> billing = billingRepository.findById(id);
        if(billing.isEmpty()){
            throw new HospitalBusinessException(HttpStatus.NOT_FOUND, ErrorCode.BILLING_NOT_FOUND.name(),"billing with id ("+id+") not found");
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
            throw new HospitalBusinessException(HttpStatus.NOT_FOUND,ErrorCode.PATIENT_NOT_FOUND.name()
                    ,"patient with id ("+createBillingRequest.getPatient_id()+") not found");
        }
        Billing billing = new Billing();
        billing.setAmount(createBillingRequest.getAmount())
                .setPatient(patient.get())
                .setUpdatedAt(LocalDateTime.now())
                .setCreatedAt(LocalDateTime.now());
        billingRepository.save(billing);
        CreateBillingResponse billingResponse = new CreateBillingResponse();
        billingResponse.setId(billing.getId());
        return billingResponse;
    }

    public UpdateBillingResponse updateBilling(UpdateBillingRequest billingRequest) {

        Optional<Patient> patient = patientRepository.findById(billingRequest.getPatientId());
        if (patient.isEmpty()) {
            throw new HospitalBusinessException(HttpStatus.NOT_FOUND,ErrorCode.PATIENT_NOT_FOUND.name()
                    ,"patient with id ("+billingRequest.getPatientId()+") not found");
        }
        Optional<Billing> billing = billingRepository.findById(billingRequest.getId());
        if (billing.isPresent()) {
            Billing dbbilling = billing.get();
            dbbilling.setAmount(billingRequest.getAmount())
                    .setPatient(patient.get())
                    .setUpdatedAt(LocalDateTime.now());
            billingRepository.save(dbbilling);
            UpdateBillingResponse billingResponse = new UpdateBillingResponse();
            billingResponse.setId(dbbilling.getId());
            return billingResponse;
        } else {
            throw new HospitalBusinessException(HttpStatus.NOT_FOUND,ErrorCode.BILLING_NOT_FOUND.name()
                    ,"billing with id ("+billingRequest.getId()+") not found");
        }
    }

    public void deleteBilling(Long id) {
        Optional<Billing> billing = billingRepository.findById(id);
        if (billing.isEmpty())
            throw new HospitalBusinessException(HttpStatus.NOT_FOUND,ErrorCode.BILLING_NOT_FOUND.name()
                    ,"billing with id ("+id+") not found");
        else
            billingRepository.deleteById(id);
    }
}
