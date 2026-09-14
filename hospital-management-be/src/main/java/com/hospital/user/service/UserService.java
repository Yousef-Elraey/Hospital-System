package com.hospital.user.service;

import com.hospital.common.exception.HospitalBusinessException;
import com.hospital.common.security.JwtService;
import com.hospital.dto.PageResponse;
import com.hospital.entity.User;
import com.hospital.redis.service.TokenBlackListService;
import com.hospital.user.dto.request.UpdateUserRequest;
import com.hospital.user.dto.request.UserSearchRequest;
import com.hospital.user.dto.response.GetUserResponse;
import com.hospital.user.dto.response.UpdateUserResponse;
import com.hospital.user.repository.UserRepository;
import com.hospital.user.specification.UserSpecification;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder encoder;


    public PageResponse<GetUserResponse> getAllUsers(UserSearchRequest userSearchRequest,
                                                     int page, int size, String sortBy,
                                                     String direction) {
        Sort sort = direction.equalsIgnoreCase("desc")
                ? Sort.by(sortBy).descending()
                : Sort.by(sortBy).ascending();
        Pageable pageable = PageRequest.of(page, size, sort);

        Specification<User> specification = Specification.where(null);
        specification = specification
                .and(UserSpecification.hasPhone(userSearchRequest.getPhone()))
                .and(UserSpecification.hasId(userSearchRequest.getId()))
                .and(UserSpecification.hasUserName(userSearchRequest.getUserName()))
                .and(UserSpecification.hasEmail(userSearchRequest.getEmail()))
                .and(UserSpecification.hasFullName(userSearchRequest.getFullName()))
                .and(UserSpecification.hasRole(userSearchRequest.getRole()))
                .and(UserSpecification.hasPassword(userSearchRequest.getPassword()))
                .and(UserSpecification.hasActive(userSearchRequest.getActive()));


        Page<User> usersPage = userRepository.findAll(specification, pageable);

        List<User> usersList = usersPage.getContent();
        if (usersList.isEmpty()) {
            return PageResponse.<GetUserResponse>builder()
                    .data(new ArrayList<>())
                    .page(usersPage.getNumber())
                    .size(usersPage.getSize())
                    .totalElements(usersPage.getTotalElements())
                    .totalPages(usersPage.getTotalPages())
                    .first(usersPage.isFirst())
                    .last(usersPage.isLast())
                    .build();
        }

        List<GetUserResponse> userResponseList = new ArrayList<>();

        usersList.forEach(user -> {
            GetUserResponse userResponse = new GetUserResponse();
            userResponse.setUserName(user.getUserName())
                    .setPhone(user.getPhone())
                    .setEmail(user.getEmail())
                    .setFullName(user.getFullName())
                    .setAddress(user.getAddress())
                    .setSummary(user.getSummary())
                    .setRole(user.getRole())
                    .setActive(user.getActive())
                    .setStatus(user.getStatus());
            userResponseList.add(userResponse);
        });
        return PageResponse.<GetUserResponse>builder()
                .data(userResponseList)
                .page(usersPage.getNumber())
                .size(usersPage.getSize())
                .totalElements(usersPage.getTotalElements())
                .totalPages(usersPage.getTotalPages())
                .first(usersPage.isFirst())
                .last(usersPage.isLast())
                .build();
    }

    public GetUserResponse getUserById(Long id) {
        Optional<User> user = userRepository.findById(id);
        if (user.isEmpty()) {
            throw new HospitalBusinessException("no user found");
        }
        User userDb = user.get();
        GetUserResponse userResponse = new GetUserResponse();
        userResponse.setUserName(userDb.getUserName())
                .setPhone(userDb.getPhone())
                .setEmail(userDb.getEmail())
                .setFullName(userDb.getFullName())
                .setAddress(userDb.getAddress())
                .setSummary(userDb.getSummary())
                .setRole(userDb.getRole())
                .setActive(userDb.getActive())
                .setStatus(userDb.getStatus());
        ;
        return userResponse;
    }


    public UpdateUserResponse updateUser(UpdateUserRequest userRequest) {
        Optional<User> user = userRepository.findById(userRequest.getId());
        if (user.isEmpty()) {
            throw new HospitalBusinessException("no user found");
        }
        User userDb = user.get();
        userDb.setUserName(userRequest.getUserName())
                .setPassword(encoder.encode(userRequest.getPassword()));
        userRepository.save(userDb);
        UpdateUserResponse userResponse = new UpdateUserResponse();
        userResponse.setId(userDb.getId());
        return userResponse;
    }

    public void deleteUserById(Long id) {
        Optional<User> user = userRepository.findById(id);
        if (user.isEmpty()) {
            throw new HospitalBusinessException("no user found");
        }
        userRepository.deleteById(id);
    }
}
