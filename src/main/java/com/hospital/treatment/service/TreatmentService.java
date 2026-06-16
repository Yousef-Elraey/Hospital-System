package com.hospital.treatment.service;

import com.hospital.common.exception.HospitalBusinessException;
import com.hospital.entity.Diagnose;
import com.hospital.entity.Treatment;
import com.hospital.treatment.dto.request.CreateTreatmentRequest;
import com.hospital.treatment.dto.request.UpdateTreatmentRequest;
import com.hospital.treatment.dto.response.CreateTreatmentResponse;
import com.hospital.treatment.dto.response.GetTreatmentResponse;
import com.hospital.treatment.dto.response.UpdateTreatmentResponse;
import com.hospital.treatment.repository.TreatmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TreatmentService {
    private final TreatmentRepository treatmentRepository;

    public List<GetTreatmentResponse> getAllTreatments() {
      List<Treatment> treatments = treatmentRepository.findAll();
      if (treatments.isEmpty())
          throw new HospitalBusinessException("no treatments found");
    List<GetTreatmentResponse> treatmentResponses = new ArrayList<>();
        treatments.forEach(treatment -> {
        GetTreatmentResponse getTreatmentResponse = new GetTreatmentResponse();
        getTreatmentResponse.setId(treatment.getId())
                .setNameEn(treatment.getName_en())
                .setNameAr(treatment.getName_ar())
                .setActiveIngredient(treatment.getActiveIngredient());
            treatmentResponses.add(getTreatmentResponse);
    });
    return treatmentResponses;
    }

    public GetTreatmentResponse getTreatmentById(Long id) {
       Optional<Treatment> treatment = treatmentRepository.findById(id);
       if (treatment.isEmpty())
           throw new HospitalBusinessException("no treatment found");
       Treatment treatmentDb = treatment.get();
       GetTreatmentResponse getTreatmentResponse = new GetTreatmentResponse();
        getTreatmentResponse.setId(treatmentDb.getId())
                .setNameEn(treatmentDb.getName_en())
                .setNameAr(treatmentDb.getName_ar())
                .setActiveIngredient(treatmentDb.getActiveIngredient());
        return getTreatmentResponse;
    }

    public CreateTreatmentResponse createTreatment(CreateTreatmentRequest createTreatmentRequest) {
        Treatment treatment = new Treatment();
        treatment.setId(createTreatmentRequest.getId())
                .setName_en(createTreatmentRequest.getNameEn())
                .setName_ar(createTreatmentRequest.getNameAr())
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
                 .setName_en(updateTreatmentRequest.getNameEn())
                 .setName_ar(updateTreatmentRequest.getNameAr())
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
