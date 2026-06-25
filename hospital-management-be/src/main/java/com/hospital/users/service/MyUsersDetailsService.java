package com.hospital.users.service;

import com.hospital.entity.Users;
import com.hospital.users.MyUserDetails;
import com.hospital.users.repository.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class MyUsersDetailsService implements UserDetailsService {
    @Autowired
    private UsersRepository usersRepository;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<Users> user =  usersRepository.findByUserName(username);
        if (user.isEmpty()){
            throw new UsernameNotFoundException("user not found");
        }
        return new MyUserDetails(user.get());

    }
}
