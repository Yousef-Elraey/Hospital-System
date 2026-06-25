package com.hospital.speciality.service;

import com.hospital.billing.dto.response.GetBillingResponse;
import com.hospital.common.exception.HospitalBusinessException;
import com.hospital.dto.PageResponse;
import com.hospital.entity.Speciality;
import com.hospital.entity.Treatment;
import com.hospital.speciality.dto.request.CreateSpecialityRequest;
import com.hospital.speciality.dto.request.UpdateSpecialityRequest;
import com.hospital.speciality.dto.response.CreateSpecialityResponse;
import com.hospital.speciality.dto.response.GetSpecialityResponse;
import com.hospital.speciality.dto.response.UpdateSpecialityResponse;
import com.hospital.speciality.repository.SpecialityRepository;
import com.hospital.treatment.dto.request.CreateTreatmentRequest;
import com.hospital.treatment.dto.request.UpdateTreatmentRequest;
import com.hospital.treatment.dto.response.CreateTreatmentResponse;
import com.hospital.treatment.dto.response.GetTreatmentResponse;
import com.hospital.treatment.dto.response.UpdateTreatmentResponse;
import com.hospital.treatment.repository.TreatmentRepository;
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
public class SpecialityService {
    private final SpecialityRepository specialityRepository;

    public PageResponse<GetSpecialityResponse> getAllSpecialities(int page, int size, String sortBy, String direction) {
        Sort sort = direction.equalsIgnoreCase("desc")
                ? Sort.by(sortBy).descending()
                : Sort.by(sortBy).ascending();

        Pageable pageable = PageRequest.of(page,size,sort);

        Page<Speciality> specialityPage = specialityRepository.findAll(pageable);
        List<Speciality> specialities = specialityPage.getContent();
        if (specialities.isEmpty())
            throw new HospitalBusinessException("no specialities found");
        List<GetSpecialityResponse> specialityResponses = new ArrayList<>();
        specialities.forEach(speciality -> {
            GetSpecialityResponse getSpecialityResponse = new GetSpecialityResponse();
            getSpecialityResponse.setId(speciality.getId())
                    .setName_en(speciality.getName_en())
                    .setName_ar(speciality.getName_ar());
            specialityResponses.add(getSpecialityResponse);
        });
        return PageResponse.<GetSpecialityResponse>builder()
                .data(specialityResponses)
                .page(specialityPage.getNumber())
                .size(specialityPage.getSize())
                .totalElements(specialityPage.getTotalElements())
                .totalPages(specialityPage.getTotalPages())
                .first(specialityPage.isFirst())
                .last(specialityPage.isLast())
                .build();
    }

    public GetSpecialityResponse getSpecialityById(Long id) {
        Optional<Speciality> speciality = specialityRepository.findById(id);
        if (speciality.isEmpty())
            throw new HospitalBusinessException("no speciality found");
        Speciality specialityDb = speciality.get();
        GetSpecialityResponse getSpecialityResponse = new GetSpecialityResponse();
        getSpecialityResponse.setId(specialityDb.getId())
                .setName_en(specialityDb.getName_en())
                .setName_ar(specialityDb.getName_ar());
        return getSpecialityResponse;
    }

    public CreateSpecialityResponse createSpeciality(CreateSpecialityRequest createSpecialityRequest) {
        Speciality speciality = new Speciality();
        speciality.setId(createSpecialityRequest.getId())
                .setName_en(createSpecialityRequest.getName_en())
                .setName_ar(createSpecialityRequest.getName_ar());
        specialityRepository.save(speciality);
        CreateSpecialityResponse specialityResponse = new CreateSpecialityResponse();
        specialityResponse.setId(speciality.getId());
        return specialityResponse;
    }

    public UpdateSpecialityResponse updateSpeciality(UpdateSpecialityRequest updateSpecialityRequest) {
        Optional<Speciality> speciality = specialityRepository.findById(updateSpecialityRequest.getId());
        if (speciality.isPresent()){
            Speciality specialityDb = speciality.get();
            specialityDb.setId(updateSpecialityRequest.getId())
                    .setName_en(updateSpecialityRequest.getName_en())
                    .setName_ar(updateSpecialityRequest.getName_ar());
            specialityRepository.save(specialityDb);

            UpdateSpecialityResponse specialityResponse = new UpdateSpecialityResponse();
            specialityResponse.setId(specialityDb.getId());
            return specialityResponse;
        }else {
            throw new HospitalBusinessException("no speciality found");
        }
    }


    public void deleteSpecialityById(Long id) {
        Optional<Speciality> speciality = specialityRepository.findById(id);
        if(speciality.isPresent())
            specialityRepository.delete(speciality.get());
        else
            throw new HospitalBusinessException("no speciality found");


    }
}
