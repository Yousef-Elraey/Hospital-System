package com.hospital.treatment.service;

import com.hospital.common.exception.HospitalBusinessException;
import com.hospital.diagnose.specification.DiagnoseSpecification;
import com.hospital.dto.PageResponse;
import com.hospital.entity.Diagnose;
import com.hospital.entity.Treatment;
import com.hospital.treatment.dto.request.CreateTreatmentRequest;
import com.hospital.treatment.dto.request.SearchTreatmentRequest;
import com.hospital.treatment.dto.request.UpdateTreatmentRequest;
import com.hospital.treatment.dto.response.CreateTreatmentResponse;
import com.hospital.treatment.dto.response.GetTreatmentResponse;
import com.hospital.treatment.dto.response.UpdateTreatmentResponse;
import com.hospital.treatment.repository.TreatmentRepository;
import com.hospital.treatment.specification.TreatmentSpecification;
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
public class TreatmentService {
    private final TreatmentRepository treatmentRepository;

    public PageResponse<GetTreatmentResponse> getAllTreatments(SearchTreatmentRequest searchTreatmentRequest,
                                                               int page, int size, String sortBy, String direction) {
        Sort sort = direction.equalsIgnoreCase("desc")
                ? Sort.by(sortBy).descending()
                : Sort.by(sortBy).ascending();

        Pageable pageable = PageRequest.of(page,size,sort);

        Specification<Treatment> specification  = Specification.where(null);
        specification = specification
                .and(TreatmentSpecification.hasNameAr(searchTreatmentRequest.getNameAr()))
                .and(TreatmentSpecification.hasNameEn(searchTreatmentRequest.getNameEn()))
                .and(TreatmentSpecification.hasActiveIngredient(searchTreatmentRequest.getActiveIngredient()));

        Page<Treatment> treatmentPage = treatmentRepository.findAll(specification,pageable);
     List<Treatment> treatments = treatmentPage.getContent();
        List<GetTreatmentResponse> treatmentResponses = new ArrayList<>();
        if (!treatments.isEmpty()){
            treatments.forEach(treatment -> {
                GetTreatmentResponse getTreatmentResponse = new GetTreatmentResponse();
                getTreatmentResponse.setId(treatment.getId())
                        .setNameEn(treatment.getNameEn())
                        .setNameAr(treatment.getNameAr())
                        .setActiveIngredient(treatment.getActiveIngredient());
                treatmentResponses.add(getTreatmentResponse);
            });
        }

    return PageResponse.<GetTreatmentResponse>builder()
            .data(treatmentResponses)
            .page(treatmentPage.getNumber())
            .size(treatmentPage.getSize())
            .totalElements(treatmentPage.getTotalElements())
            .totalPages(treatmentPage.getTotalPages())
            .first(treatmentPage.isFirst())
            .last(treatmentPage.isLast())
            .build();
    }

    public GetTreatmentResponse getTreatmentById(Long id) {
       Optional<Treatment> treatment = treatmentRepository.findById(id);
       if (treatment.isEmpty())
           throw new HospitalBusinessException("no treatment found");
       Treatment treatmentDb = treatment.get();
       GetTreatmentResponse getTreatmentResponse = new GetTreatmentResponse();
        getTreatmentResponse.setId(treatmentDb.getId())
                .setNameEn(treatmentDb.getNameEn())
                .setNameAr(treatmentDb.getNameAr())
                .setActiveIngredient(treatmentDb.getActiveIngredient());
        return getTreatmentResponse;
    }

    public CreateTreatmentResponse createTreatment(CreateTreatmentRequest createTreatmentRequest) {
        Treatment treatment = new Treatment();
        treatment.setNameEn(createTreatmentRequest.getNameEn())
                .setNameAr(createTreatmentRequest.getNameAr())
                .setActiveIngredient(createTreatmentRequest.getActiveIngredient());
        treatmentRepository.save(treatment);
        CreateTreatmentResponse treatmentResponse = new CreateTreatmentResponse();
        treatmentResponse.setId(treatment.getId());
        return treatmentResponse;
    }

    public UpdateTreatmentResponse updateTreatment(UpdateTreatmentRequest updateTreatmentRequest) {
     Optional<Treatment> treatment = treatmentRepository.findById(updateTreatmentRequest.getId());
     if (treatment.isPresent()){
         Treatment treatmentDb = treatment.get();
         treatmentDb.setId(updateTreatmentRequest.getId())
                 .setNameEn(updateTreatmentRequest.getNameEn())
                 .setNameAr(updateTreatmentRequest.getNameAr())
                 .setActiveIngredient(updateTreatmentRequest.getActiveIngredient());
         treatmentRepository.save(treatmentDb);

         UpdateTreatmentResponse treatmentResponse = new UpdateTreatmentResponse();
         treatmentResponse.setId(treatmentDb.getId());
         return treatmentResponse;
     }else {
         throw new HospitalBusinessException("no treatment found");
     }
    }


    public void deleteTreatment(Long id) {
       Optional<Treatment> treatment = treatmentRepository.findById(id);
       if(treatment.isPresent())
           treatmentRepository.delete(treatment.get());
       else
           throw new HospitalBusinessException("no treatment found");


    }
}
