package com.hospital.speciality.repository;

import com.hospital.entity.Patient;
import com.hospital.entity.Speciality;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SpecialityRepository extends JpaRepository<Speciality,Long> {

}
