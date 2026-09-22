package com.hospital.time_slots.specification;

import com.hospital.entity.*;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;
import java.time.LocalTime;

public class TimeSlotsSpecification {

    public static Specification<TimeSlots> hasDoctorId(Long doctorId) {
        return (root, query, cb) ->
                doctorId == null
                        ? null
                        : cb.equal(root.get("doctor").get("id"), doctorId);
    }
    public static Specification<TimeSlots> hasAppointmentType(AppointmentType appointmentType) {
        return (root, query, cb) ->
                appointmentType == null
                        ? null
                        : cb.equal(root.get("appointmentType"), appointmentType);
    }
    public static Specification<TimeSlots> hasTimeSlotsStatus(TimeSlotsStatus timeSlotsStatus) {
        return (root, query, cb) ->
                timeSlotsStatus == null
                        ? null
                        : cb.equal(root.get("timeSlotsStatus"), timeSlotsStatus);
    }
    public static Specification<TimeSlots> hasDay(LocalDate day) {
        return (root, query, cb) ->
                day == null
                        ? null
                        : cb.equal(root.get("day"), day);
    }

    public static Specification<TimeSlots> hasStart(LocalTime start) {
        return (root, query, cb) ->
                start == null
                        ? null
                        : cb.equal(root.get("start"), start);
    }
    public static Specification<TimeSlots> hasEnd(LocalTime end) {
        return (root, query, cb) ->
                end == null
                        ? null
                        : cb.equal(root.get("end"), end);
    }
}
