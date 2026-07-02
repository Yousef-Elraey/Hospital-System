package com.hospital.doctor.repository;

import com.hospital.entity.Doctor;
import com.hospital.entity.Speciality;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DoctorRepository extends JpaRepository<Doctor, Long> {
    Optional<Doctor> findByContactNumber(String contactNumber);


    @Query("""
                SELECT d
                FROM Doctor d
                WHERE (:name IS NULL OR LOWER(d.name) LIKE LOWER(CONCAT('%', :name, '%')))
                      AND (:specialityId IS NULL OR d.speciality.id = :specialityId)
                      AND (:contactNumber IS NULL OR d.contactNumber = :contactNumber)
            """)
    Page<Doctor> searchDoctors(@Param("name") String name,
                               @Param("specialityId") Long specialityID,
                               @Param("contactNumber") String contactNumber, Pageable pageable);


}
