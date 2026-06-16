package com.hospital.diagnose.repository;

import com.hospital.entity.Diagnose;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DiagnoseRepository extends JpaRepository<Diagnose,Long> {

}
