package com.hospital.timeSlots.service;

import com.hospital.common.exception.HospitalBusinessException;
import com.hospital.doctor.repository.DoctorRepository;
import com.hospital.dto.PageResponse;
import com.hospital.entity.*;
import com.hospital.patient.dto.response.GetPatientResponse;
import com.hospital.timeSlots.dto.request.CreateTimeSlotsRequest;
import com.hospital.timeSlots.dto.request.GenerateTimeSlotsRequest;
import com.hospital.timeSlots.dto.request.SearchTimeSlotsRequest;
import com.hospital.timeSlots.dto.request.UpdateTimeSlotsRequest;
import com.hospital.timeSlots.dto.response.CreateTimeSlotsResponse;
import com.hospital.timeSlots.dto.response.GetTimeSlotsResponse;
import com.hospital.timeSlots.dto.response.UpdateTimeSlotsResponse;
import com.hospital.timeSlots.repository.TimeSlotsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TimeSlotsService {
    private final TimeSlotsRepository timeSlotsRepository;
    private final DoctorRepository doctorRepository;

    public PageResponse<GetTimeSlotsResponse> getAllTimeSlots(int page, int size, String sortBy, String direction) {
        Sort sort = direction.equalsIgnoreCase("desc")
                ? Sort.by(sortBy).descending()
                : Sort.by(sortBy).ascending();
        Pageable pageable = PageRequest.of(page,size,sort);
        Page<TimeSlots> timeSlotsPage = timeSlotsRepository.findAll(pageable);

        List<TimeSlots> timeSlotsList = timeSlotsPage.getContent();

        if (timeSlotsList.isEmpty()) {
            throw new HospitalBusinessException("no timeSlots found");
        }
        List<GetTimeSlotsResponse> getTimeSlotsResponses = new ArrayList<>();
        for (TimeSlots timeSlots: timeSlotsList) {
            GetTimeSlotsResponse getTimeSlotsResponse = new GetTimeSlotsResponse();
            getTimeSlotsResponse.setId(timeSlots.getId())
                    .setDoctorId(timeSlots.getDoctor().getId())
                    .setDay(timeSlots.getDay())
                    .setStart(timeSlots.getStart())
                    .setEnd(timeSlots.getEnd())
                    .setStatus(timeSlots.getStatus())
                    .setAppointmentType(timeSlots.getAppointmentType());
            getTimeSlotsResponses.add(getTimeSlotsResponse);
        }
        return PageResponse.<GetTimeSlotsResponse>builder()
                .data(getTimeSlotsResponses)
                .page(timeSlotsPage.getNumber())
                .size(timeSlotsPage.getSize())
                .totalElements(timeSlotsPage.getTotalElements())
                .totalPages(timeSlotsPage.getTotalPages())
                .first(timeSlotsPage.isFirst())
                .last(timeSlotsPage.isLast())
                .build();
    }

    public GetTimeSlotsResponse getTimeSlotsById(Long id) {
        Optional<TimeSlots> timeSlots = timeSlotsRepository.findById(id);
        if (timeSlots.isEmpty()){
            throw new HospitalBusinessException("no time slots found");
        }
        TimeSlots timeSlotsDb = timeSlots.get();
        GetTimeSlotsResponse getTimeSlotsResponse = new GetTimeSlotsResponse();
        getTimeSlotsResponse.setId(timeSlotsDb.getId())
                .setDoctorId(timeSlotsDb.getDoctor().getId())
                .setDay(timeSlotsDb.getDay())
                .setStart(timeSlotsDb.getStart())
                .setEnd(timeSlotsDb.getEnd())
                .setStatus(timeSlotsDb.getStatus())
                .setAppointmentType(timeSlotsDb.getAppointmentType());

        return getTimeSlotsResponse;
    }


    public CreateTimeSlotsResponse createTimeSlots(CreateTimeSlotsRequest createTimeSlotsRequest) {
        Optional<Doctor> doctorOp = doctorRepository.findById(createTimeSlotsRequest.getDoctorId());
        if (doctorOp.isEmpty()){
            throw new HospitalBusinessException("no doctor found");
        }
        TimeSlots timeSlots = new TimeSlots();
        timeSlots.setId(createTimeSlotsRequest.getId())
                .setDoctor(doctorOp.get())
                .setDay(createTimeSlotsRequest.getDay())
                .setStart(createTimeSlotsRequest.getStart())
                .setEnd(createTimeSlotsRequest.getEnd())
                .setStatus(createTimeSlotsRequest.getStatus())
                .setAppointmentType(createTimeSlotsRequest.getAppointmentType());

        timeSlotsRepository.save(timeSlots);
        CreateTimeSlotsResponse createTimeSlotsResponse = new CreateTimeSlotsResponse();
        createTimeSlotsResponse.setId(timeSlots.getId());
        return createTimeSlotsResponse;
    }


    public UpdateTimeSlotsResponse updateTimeSlots(UpdateTimeSlotsRequest updateTimeSlotsRequest) {
       Optional<TimeSlots> timeSlotsOp = timeSlotsRepository.findById(updateTimeSlotsRequest.getId());
      Optional<Doctor> doctorOp = doctorRepository.findById(updateTimeSlotsRequest.getDoctorId());
       if (timeSlotsOp.isEmpty()){
           throw new HospitalBusinessException("no time slots found");
       }
       if(doctorOp.isEmpty()){
           throw new HospitalBusinessException("no doctor found");
       }
       TimeSlots timeSlots = timeSlotsOp.get();
        timeSlots
                .setDoctor(doctorOp.get())
                .setDay(updateTimeSlotsRequest.getDay())
                .setStart(timeSlots.getStart())
                .setEnd(timeSlots.getEnd())
                .setStatus(updateTimeSlotsRequest.getStatus())
                .setAppointmentType(updateTimeSlotsRequest.getAppointmentType());

        timeSlotsRepository.save(timeSlots);
       UpdateTimeSlotsResponse updateTimeSlotsResponse = new UpdateTimeSlotsResponse();
        updateTimeSlotsResponse.setId(timeSlots.getId());
        return updateTimeSlotsResponse;
    }

    public void deleteTimeSlots(Long id) {
       if(timeSlotsRepository.findById(id).isEmpty()){
           throw new HospitalBusinessException("no time slots found");
       }else {
           timeSlotsRepository.deleteById(id);
       }
    }

    public void generateTimeSlots(GenerateTimeSlotsRequest request) {
      LocalDate currentDay = request.getDayStart();
      LocalDate dayEnd = request.getDayEnd();
      Optional<Doctor> doctorOp = doctorRepository.findById(request.getDoctorId());
     if (doctorOp.isEmpty()){
         throw new HospitalBusinessException("doctor not found");
     }
      Doctor doctor = doctorOp.get();
       List<String> specificDays = request.getDays().stream()
                                    .map(String::toUpperCase)
                                    .toList();
      while(!currentDay.isAfter(dayEnd)){
       String dayName = currentDay.getDayOfWeek().name();
        if (specificDays.contains(dayName)){
           LocalTime slotStart = request.getStart();
           LocalTime limitEnd = request.getEnd();
           Long duration = request.getDuration();
            while(!slotStart.plusMinutes(duration).isAfter(limitEnd)){
               LocalTime slotEnd = slotStart.plusMinutes(duration);
               TimeSlots timeSlots = new TimeSlots();
               timeSlots
                       .setDoctor(doctor)
                       .setAppointmentType(request.getAppointmentType())
                       .setDay(currentDay)
                       .setStart(slotStart)
                       .setEnd(slotEnd)
                       .setStatus(request.getStatus());
                timeSlotsRepository.save(timeSlots);
                slotStart = slotEnd;
           }

        }
        currentDay = currentDay.plusDays(1);
      }


    }

    public List<GetTimeSlotsResponse> getAvailableTimeSlots(Long doctorId) {
        List<TimeSlots> AvailableTimeSlots = timeSlotsRepository.getAvailableTimeSlots(doctorId);
       if (AvailableTimeSlots.isEmpty()){
           throw new HospitalBusinessException("no time slots available");
       }
       List<GetTimeSlotsResponse> responses = new ArrayList<>();
        for (TimeSlots timeSlots: AvailableTimeSlots) {
            GetTimeSlotsResponse getTimeSlotsResponse = new GetTimeSlotsResponse();
            getTimeSlotsResponse.setId(timeSlots.getId())
                    .setDoctorId(timeSlots.getDoctor().getId())
                    .setDay(timeSlots.getDay())
                    .setStart(timeSlots.getStart())
                    .setEnd(timeSlots.getEnd())
                    .setStatus(timeSlots.getStatus())
                    .setAppointmentType(timeSlots.getAppointmentType());
            responses.add(getTimeSlotsResponse);
        }

        return responses;
    }

    public PageResponse<GetTimeSlotsResponse> searchTimeSlots(int page, int size, String sortBy, String direction, SearchTimeSlotsRequest searchTimeSlotsRequest) {
        Long doctorId = searchTimeSlotsRequest.getDoctorId();
        TimeSlotsStatus timeSlotsStatus = searchTimeSlotsRequest.getTimeSlotsStatus();
        LocalDate from = searchTimeSlotsRequest.getFrom();
        LocalDate to = searchTimeSlotsRequest.getTo();


        Sort sort = direction.equalsIgnoreCase("desc")
                ? Sort.by(sortBy).descending()
                : Sort.by(sortBy).ascending();

        Pageable pageable = PageRequest.of(page, size, sort);
        Page<TimeSlots> timeSlotsPage = timeSlotsRepository.searchTimeSlots(doctorId, timeSlotsStatus, from, to, pageable);
        List<TimeSlots> timeSlotsList = timeSlotsPage.getContent();
        List<GetTimeSlotsResponse> responses = new ArrayList<>();

        for (TimeSlots timeSlots : timeSlotsList) {
            GetTimeSlotsResponse getTimeSlotsResponse = new GetTimeSlotsResponse();
            getTimeSlotsResponse
                    .setDoctorId(timeSlots.getDoctor().getId())
                    .setDay(timeSlots.getDay())
                    .setStart(timeSlots.getStart())
                    .setEnd(timeSlots.getEnd())
                    .setStatus(timeSlots.getStatus())
                    .setAppointmentType(timeSlots.getAppointmentType());
            responses.add(getTimeSlotsResponse);
        }

        return PageResponse.<GetTimeSlotsResponse>builder()
                .data(responses)
                .page(timeSlotsPage.getNumber())
                .size(timeSlotsPage.getSize())
                .totalElements(timeSlotsPage.getTotalElements())
                .totalPages(timeSlotsPage.getTotalPages())
                .first(timeSlotsPage.isFirst())
                .last(timeSlotsPage.isLast())
                .build();
    }
}
