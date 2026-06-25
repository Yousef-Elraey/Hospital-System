package com.hospital.diagnose.service;

import com.hospital.billing.dto.response.GetBillingResponse;
import com.hospital.common.exception.HospitalBusinessException;
import com.hospital.diagnose.dto.request.CreateDiagnoseRequest;
import com.hospital.diagnose.dto.request.UpdateDiagnoseRequest;
import com.hospital.diagnose.dto.response.CreateDiagnoseResponse;
import com.hospital.diagnose.dto.response.GetDiagnoseResponse;
import com.hospital.diagnose.dto.response.UpdateDiagnoseResponse;
import com.hospital.diagnose.repository.DiagnoseRepository;
import com.hospital.dto.PageResponse;
import com.hospital.entity.Diagnose;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class DiagnoseService {
    private final DiagnoseRepository diagnoseRepository;

    public PageResponse<GetDiagnoseResponse> getAllDiagnoses(int page,int size, String sortBy, String direction) {
        Sort sort = direction.equalsIgnoreCase("desc")
                ? Sort.by(sortBy).descending()
                : Sort.by(sortBy).ascending();

        Pageable pageable = PageRequest.of(page,size,sort);
        Page<Diagnose> diagnosePage = diagnoseRepository.findAll(pageable);
     List<Diagnose> diagnoses = diagnosePage.getContent();
        if (diagnoses.isEmpty())
          throw new HospitalBusinessException("no diagnoses found");
    List<GetDiagnoseResponse> diagnoseResponses = new ArrayList<>();
    diagnoses.forEach(diagnose -> {
        GetDiagnoseResponse getDiagnoseResponse = new GetDiagnoseResponse();
        getDiagnoseResponse.setId(diagnose.getId())
                .setNameEn(diagnose.getName_en())
                .setNameAr(diagnose.getName_ar());
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
                .setNameEn(diagnoseDb.getName_en())
                .setNameAr(diagnoseDb.getName_ar());
        return getDiagnoseResponse;
    }

    public CreateDiagnoseResponse createDiagnose(CreateDiagnoseRequest createDiagnoseRequest) {
        Diagnose diagnose = new Diagnose();
        diagnose.setId(createDiagnoseRequest.getId())
                .setName_en(createDiagnoseRequest.getNameEn())
                .setName_ar(createDiagnoseRequest.getNameAr());
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
                 .setName_en(updateDiagnoseRequest.getNameEn())
                 .setName_ar(updateDiagnoseRequest.getNameAr());
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
