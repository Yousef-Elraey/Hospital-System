package com.hospital.speciality.repository;

import com.hospital.entity.Speciality;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
            WHERE LOWER(s.nameAr) = LOWER(:name)
               OR LOWER(s.nameEn) = LOWER(:name)
            """)
    Optional<Speciality> findByName(@Param("name") String name);

    @Query("""
            SELECT s FROM Speciality s
            WHERE (:nameEn IS NULL OR LOWER(s.nameEn) = LOWER(:nameEn))
                AND (:nameAr IS NULL OR LOWER(s.nameAr) = LOWER(:nameAr))
            """)
    Page<Speciality> searchSpeciality(@Param("nameEn") String nameEn,
                                      @Param("nameAr") String nameAr,
                                      Pageable pageable);
}
