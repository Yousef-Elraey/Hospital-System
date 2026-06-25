package com.hospital.speciality.repository;

import com.hospital.entity.Speciality;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SpecialityRepository extends JpaRepository<Speciality, Long> {
    @Query("""
            SELECT s
            FROM Speciality s
            WHERE LOWER(s.name_ar) = LOWER(:name)
               OR LOWER(s.name_en) = LOWER(:name)
            """)
    Optional<Speciality> findByName(@Param("name") String name);
}
