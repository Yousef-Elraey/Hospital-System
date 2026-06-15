package com.hospital.speciality.service;

import com.hospital.common.exception.HospitalBusinessException;
import com.hospital.common.security.JWTService;
import com.hospital.entity.MedicalRecord;
import com.hospital.entity.Patient;
import com.hospital.entity.Speciality;
import com.hospital.medicalRecord.dto.response.GetMedicalRecordResponse;
import com.hospital.medicalRecord.repository.MedicalRecordRepository;
import com.hospital.medicalRecord.service.MedicalRecordService;
import com.hospital.speciality.dto.request.CreateSpecialityRequest;
import com.hospital.speciality.dto.request.UpdateSpecialityRequest;
import com.hospital.speciality.dto.response.CreateSpecialityResponse;
import com.hospital.speciality.dto.response.GetSpecialityResponse;
import com.hospital.speciality.dto.response.UpdateSpecialityResponse;
import com.hospital.speciality.repository.SpecialityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Service
@RequiredArgsConstructor
public class SpecialityService {
    private final SpecialityRepository specialityRepository;

    public List<GetSpecialityResponse> getAllSpecialities() {
        List<Speciality> specialities = specialityRepository.findAll();
        List<GetSpecialityResponse> specialityResponse = new ArrayList<>();
        if (!specialities.isEmpty()) {
            for (Speciality speciality : specialities) {
                GetSpecialityResponse getSpecialityResponse = new GetSpecialityResponse();
                getSpecialityResponse.setId(speciality.getId())
                        .setName_en(speciality.getName_en())
                        .setName_ar(speciality.getName_ar());
                specialityResponse.add(getSpecialityResponse);
            }
        }
        return specialityResponse;
    }

    public GetSpecialityResponse getSpecialityById(Long id) {
        Optional<Speciality> speciality = specialityRepository.findById(id);
        if (speciality.isEmpty()) {
            throw new HospitalBusinessException("no specialities found");
        }
        Speciality specialityDb = speciality.get();
        GetSpecialityResponse specialityResponse = new GetSpecialityResponse();
        specialityResponse.setId(specialityDb.getId())
                .setName_en(specialityDb.getName_en())
                .setName_ar(specialityDb.getName_ar());

        return specialityResponse;
    }


    public CreateSpecialityResponse addSpeciality(CreateSpecialityRequest createSpecialityRequest) {

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

        Optional<Speciality> specialityTemp = specialityRepository.findById(updateSpecialityRequest.getId());
        if (specialityTemp.isPresent()) {
            Speciality specialityDb = specialityTemp.get();
            specialityDb.setId(updateSpecialityRequest.getId())
                    .setName_en(updateSpecialityRequest.getName_en())
                    .setName_ar(updateSpecialityRequest.getName_ar());

            specialityRepository.save(specialityDb);
            UpdateSpecialityResponse specialityResponse = new UpdateSpecialityResponse();
            specialityResponse.setId(specialityDb.getId());
            return specialityResponse;
        } else
            throw new HospitalBusinessException("no speciality found");

    }

    public void deleteSpecialityById(Long id) {
        if (specialityRepository.findById(id).isEmpty())
            throw new HospitalBusinessException("no speciality found");
        else
            specialityRepository.deleteById(id);
    }


}
