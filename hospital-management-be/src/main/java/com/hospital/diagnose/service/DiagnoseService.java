package com.hospital.diagnose.service;

import com.hospital.billing.specification.BillingSpecification;
import com.hospital.common.exception.HospitalBusinessException;
import com.hospital.diagnose.dto.request.CreateDiagnoseRequest;
import com.hospital.diagnose.dto.request.SearchDiagnoseRequest;
import com.hospital.diagnose.dto.request.UpdateDiagnoseRequest;
import com.hospital.diagnose.dto.response.CreateDiagnoseResponse;
import com.hospital.diagnose.dto.response.GetDiagnoseResponse;
import com.hospital.diagnose.dto.response.UpdateDiagnoseResponse;
import com.hospital.diagnose.repository.DiagnoseRepository;
import com.hospital.diagnose.specification.DiagnoseSpecification;
import com.hospital.dto.PageResponse;
import com.hospital.entity.Billing;
import com.hospital.entity.Diagnose;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class DiagnoseService {
    private final DiagnoseRepository diagnoseRepository;

    public PageResponse<GetDiagnoseResponse> getAllDiagnoses(SearchDiagnoseRequest searchDiagnoseRequest,
                                                             int page, int size, String sortBy, String direction) {
        Sort sort = direction.equalsIgnoreCase("desc")
                ? Sort.by(sortBy).descending()
                : Sort.by(sortBy).ascending();
        Pageable pageable = PageRequest.of(page,size,sort);

        Specification<Diagnose> specification  = Specification.where(null);
        specification = specification
                .and(DiagnoseSpecification.hasNameAr(searchDiagnoseRequest.getNameAr()))
                .and(DiagnoseSpecification.hasNameEn(searchDiagnoseRequest.getNameEn()));

        Page<Diagnose> diagnosePage = diagnoseRepository.findAll(specification, pageable);
     List<Diagnose> diagnoses = diagnosePage.getContent();

        if (diagnoses.isEmpty()){
            PageResponse.<GetDiagnoseResponse>builder()
                    .data(new ArrayList<>())
                    .page(diagnosePage.getNumber())
                    .size(diagnosePage.getSize())
                    .totalElements(diagnosePage.getTotalElements())
                    .totalPages(diagnosePage.getTotalPages())
                    .first(diagnosePage.isFirst())
                    .last(diagnosePage.isLast())
                    .build();
        }
    List<GetDiagnoseResponse> diagnoseResponses = new ArrayList<>();
    diagnoses.forEach(diagnose -> {
        GetDiagnoseResponse getDiagnoseResponse = new GetDiagnoseResponse();
        getDiagnoseResponse.setId(diagnose.getId())
                .setNameEn(diagnose.getNameEn())
                .setNameAr(diagnose.getNameAr());
        diagnoseResponses.add(getDiagnoseResponse);
    });

        return PageResponse.<GetDiagnoseResponse>builder()
                .data(diagnoseResponses)
                .page(diagnosePage.getNumber())
                .size(diagnosePage.getSize())
                .totalElements(diagnosePage.getTotalElements())
                .totalPages(diagnosePage.getTotalPages())
                .first(diagnosePage.isFirst())
                .last(diagnosePage.isLast())
                .build();
    }

    public GetDiagnoseResponse getDiagnoseById(Long id) {
       Optional<Diagnose> diagnose = diagnoseRepository.findById(id);
       if (diagnose.isEmpty())
           throw new HospitalBusinessException("no diagnose found");
       Diagnose diagnoseDb = diagnose.get();
       GetDiagnoseResponse getDiagnoseResponse = new GetDiagnoseResponse();
        getDiagnoseResponse.setId(diagnoseDb.getId())
                .setNameEn(diagnoseDb.getNameEn())
                .setNameAr(diagnoseDb.getNameAr());
        return getDiagnoseResponse;
    }

    public CreateDiagnoseResponse createDiagnose(CreateDiagnoseRequest createDiagnoseRequest) {
        Diagnose diagnose = new Diagnose();
        diagnose.setNameEn(createDiagnoseRequest.getNameEn())
                .setNameAr(createDiagnoseRequest.getNameAr());
        diagnoseRepository.save(diagnose);
        CreateDiagnoseResponse diagnoseResponse = new CreateDiagnoseResponse();
        diagnoseResponse.setId(diagnose.getId());
        return diagnoseResponse;
    }

    public UpdateDiagnoseResponse updateDiagnose(UpdateDiagnoseRequest updateDiagnoseRequest) {
     Optional<Diagnose> diagnose = diagnoseRepository.findById(updateDiagnoseRequest.getId());
     if (diagnose.isPresent()){
         Diagnose diagnoseDb = diagnose.get();
         diagnoseDb.setId(updateDiagnoseRequest.getId())
                 .setNameEn(updateDiagnoseRequest.getNameEn())
                 .setNameAr(updateDiagnoseRequest.getNameAr());
         diagnoseRepository.save(diagnoseDb);

         UpdateDiagnoseResponse diagnoseResponse = new UpdateDiagnoseResponse();
         diagnoseResponse.setId(diagnoseDb.getId());
         return diagnoseResponse;
     }else {
         throw new HospitalBusinessException("no diagnose found");
     }
    }


    public void deleteDiagnose(Long id) {
       Optional<Diagnose> diagnose = diagnoseRepository.findById(id);
       if(diagnose.isPresent())
           diagnoseRepository.delete(diagnose.get());
       else
           throw new HospitalBusinessException("no diagnose found");


    }
}
