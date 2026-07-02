package com.hospital.speciality.service;

import com.hospital.common.exception.HospitalBusinessException;
import com.hospital.dto.PageResponse;
import com.hospital.entity.Patient;
import com.hospital.entity.Speciality;
import com.hospital.patient.dto.request.SearchPatientRequest;
import com.hospital.patient.dto.response.GetPatientResponse;
import com.hospital.speciality.dto.request.CreateSpecialityRequest;
import com.hospital.speciality.dto.request.SearchSpecialityRequest;
import com.hospital.speciality.dto.request.UpdateSpecialityRequest;
import com.hospital.speciality.dto.response.CreateSpecialityResponse;
import com.hospital.speciality.dto.response.GetSpecialityResponse;
import com.hospital.speciality.dto.response.UpdateSpecialityResponse;
import com.hospital.speciality.repository.SpecialityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
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
                    .setNameEn(speciality.getNameEn())
                    .setNameAr(speciality.getNameAr());
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
                .setNameEn(specialityDb.getNameEn())
                .setNameAr(specialityDb.getNameAr());
        return getSpecialityResponse;
    }

    public CreateSpecialityResponse createSpeciality(CreateSpecialityRequest createSpecialityRequest) {
        Speciality speciality = new Speciality();
        speciality.setId(createSpecialityRequest.getId())
                .setNameEn(createSpecialityRequest.getNameEn())
                .setNameAr(createSpecialityRequest.getNameAr());
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
                    .setNameEn(updateSpecialityRequest.getName_en())
                    .setNameAr(updateSpecialityRequest.getName_ar());
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
        if (speciality.isPresent())
            specialityRepository.delete(speciality.get());
        else
            throw new HospitalBusinessException("no speciality found");


    }

    public PageResponse<GetSpecialityResponse> searchSpeciality(int page, int size, String sortBy, String direction,
                                                                SearchSpecialityRequest searchSpecialityRequest) {
        String nameEn = searchSpecialityRequest.getNameEn();
        String nameAr = searchSpecialityRequest.getNameAr();

        if (nameEn != null && nameEn.isBlank()) {
            nameEn = null;
        }
        if (nameAr != null && nameAr.isBlank()) {
            nameAr = null;
        }
        Sort sort = direction.equalsIgnoreCase("desc")
                ? Sort.by(sortBy).descending()
                : Sort.by(sortBy).ascending();

        Pageable pageable = PageRequest.of(page, size, sort);
        Page<Speciality> specialityPage = specialityRepository.searchSpeciality(nameEn, nameAr, pageable);
        List<Speciality> specialityList = specialityPage.getContent();
        List<GetSpecialityResponse> responses = new ArrayList<>();

        for (Speciality speciality : specialityList) {
            GetSpecialityResponse getSpecialityResponse = new GetSpecialityResponse();
            getSpecialityResponse
                    .setNameEn(speciality.getNameEn())
                    .setNameAr(speciality.getNameAr());
            responses.add(getSpecialityResponse);
        }

        return PageResponse.<GetSpecialityResponse>builder()
                .data(responses)
                .page(specialityPage.getNumber())
                .size(specialityPage.getSize())
                .totalElements(specialityPage.getTotalElements())
                .totalPages(specialityPage.getTotalPages())
                .first(specialityPage.isFirst())
                .last(specialityPage.isLast())
                .build();
    }
}
