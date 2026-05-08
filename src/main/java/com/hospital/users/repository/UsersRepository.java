package com.hospital.users.repository;

import com.hospital.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UsersRepository extends JpaRepository<Users,Long> {
    public Optional<Users> findByUserName(String userName);

}
