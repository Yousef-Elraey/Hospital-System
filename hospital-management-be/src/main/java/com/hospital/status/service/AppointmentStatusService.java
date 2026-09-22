package com.hospital.status.service;

import com.hospital.common.exception.HospitalBusinessException;
import com.hospital.dto.PageResponse;
import com.hospital.entity.AppointmentStatus;
import com.hospital.entity.Speciality;
import com.hospital.speciality.specification.SpecialitySpecification;
import com.hospital.status.dto.request.SearchAppointmentStatusRequest;
import com.hospital.status.dto.response.StatusResponseDto;
import com.hospital.status.repository.AppointmentStatusRepository;
import com.hospital.status.specification.AppointmentStatusSpecification;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AppointmentStatusService {
  private final AppointmentStatusRepository appointmentStatusRepository;

  public PageResponse<StatusResponseDto> getAllStatus(SearchAppointmentStatusRequest searchAppointmentStatusRequest,int page, int size, String sortBy, String direction) {
    Sort sort = direction.equalsIgnoreCase("desc")
            ? Sort.by(sortBy).descending()
            : Sort.by(sortBy).ascending();
    Pageable pageable = PageRequest.of(page,size,sort);

    Specification<AppointmentStatus> specification  = Specification.where(null);
    specification = specification
            .and(AppointmentStatusSpecification.hasId(searchAppointmentStatusRequest.getId()))
            .and(AppointmentStatusSpecification.hasNameAr(searchAppointmentStatusRequest.getNameAr()))
            .and(AppointmentStatusSpecification.hasNameEn(searchAppointmentStatusRequest.getNameEn()));


    Page<AppointmentStatus> appointmentStatusPage = appointmentStatusRepository.findAll(specification,pageable);

    List<AppointmentStatus> appointmentStatusDb =   appointmentStatusPage.getContent();
    List<StatusResponseDto> statusResponseDtos = new ArrayList<>();

    if (!appointmentStatusDb.isEmpty()){
      appointmentStatusDb.forEach(appointmentStatus -> {
        StatusResponseDto appointmentStatus1 = new StatusResponseDto();
        appointmentStatus1
                .setNameAr(appointmentStatus.getNameAr())
                .setNameEn(appointmentStatus.getNameEn())
                .setId(appointmentStatus.getId());
        statusResponseDtos.add(appointmentStatus1);
      });
    }

    return PageResponse.<StatusResponseDto>builder()
            .data(statusResponseDtos)
            .page(appointmentStatusPage.getNumber())
            .size(appointmentStatusPage.getSize())
            .totalElements(appointmentStatusPage.getTotalElements())
            .totalPages(appointmentStatusPage.getTotalPages())
            .first(appointmentStatusPage.isFirst())
            .last(appointmentStatusPage.isLast())
            .build();
    }
}
