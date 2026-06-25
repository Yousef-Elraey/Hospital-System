package com.hospital.users.service;

import com.hospital.billing.dto.response.GetBillingResponse;
import com.hospital.common.exception.HospitalBusinessException;
import com.hospital.common.security.JWTService;
import com.hospital.dto.PageResponse;
import com.hospital.entity.Users;
import com.hospital.redis.service.TokenBlackListService;
import com.hospital.users.dto.request.CreateUserRequest;
import com.hospital.users.dto.request.UpdateUserRequest;
import com.hospital.users.dto.response.CreateUserResponse;
import com.hospital.users.dto.response.GetUserResponse;
import com.hospital.users.dto.response.UpdateUserResponse;
import com.hospital.users.repository.UsersRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UsersService {
    private final UsersRepository usersRepository;
   private BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12);
    private final AuthenticationManager authenticationManager;
   private final JWTService jwtService;
    private final TokenBlackListService tokenBlacklistService;


    public PageResponse<GetUserResponse> getAllUsers(int page,int size, String sortBy, String direction) {
        Sort sort = direction.equalsIgnoreCase("desc")
                ? Sort.by(sortBy).descending()
                : Sort.by(sortBy).ascending();

        Pageable pageable = PageRequest.of(page,size,sort);
        Page<Users> usersPage = usersRepository.findAll(pageable);
        List<Users> usersAll = usersPage.getContent();
        if (usersAll.isEmpty()){
            throw new HospitalBusinessException("no users found");
        }
        List<GetUserResponse> userResponseList = new ArrayList<>();

        usersAll.forEach(user->{
            GetUserResponse userResponse = new GetUserResponse();
            userResponse.setId(user.getId())
                    .setUserName(user.getUserName())
                    .setPassword(user.getPassword());
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
        Optional<Users> user = usersRepository.findById(id);
        if (user.isEmpty()){
            throw new HospitalBusinessException("no user found");
        }
        Users userDb = user.get();
        GetUserResponse userResponse = new GetUserResponse();
        userResponse.setId(userDb.getId())
                .setUserName(userDb.getUserName())
                .setPassword(userDb.getPassword());
        return userResponse;
    }

    public CreateUserResponse register(CreateUserRequest userRequest){
        if (usersRepository.findByUserName(userRequest.getUserName()).isPresent()) {
            throw new HospitalBusinessException("user already exist");
        }

        Users userDb = new Users();
        userDb.setUserName(userRequest.getUserName())
                .setPassword(userRequest.getPassword());

        userDb.setPassword(encoder.encode(userDb.getPassword()));
        usersRepository.save(userDb);
        CreateUserResponse userResponse = new CreateUserResponse();
        userResponse.setId(userDb.getId());
        return userResponse;
    }

    public String  verify(Users user) {

        Authentication authentication = authenticationManager
                 .authenticate(new UsernamePasswordAuthenticationToken(user.getUserName(),user.getPassword()));
        if (authentication.isAuthenticated()){
              return jwtService.generateToken(user.getUserName());
        }
        throw new HospitalBusinessException("you are not authenticated");

    }

    public void logout(HttpServletRequest request) {
        String authHeader =
                request.getHeader("Authorization");

        if(authHeader == null ||
                !authHeader.startsWith("Bearer ")) {
            throw new HospitalBusinessException("Token not found");
        }

        String token = authHeader.substring(7);
        Date expiration = jwtService.extractExpiration(token);
        long ttl = expiration.getTime() - System.currentTimeMillis();
        if(ttl > 0) {
            tokenBlacklistService
                    .blacklistToken(
                            token,
                            ttl
                    );
        }
    }

    public UpdateUserResponse updateUser(UpdateUserRequest userRequest) {
       Optional<Users> user = usersRepository.findById(userRequest.getId());
        if (user.isEmpty()){
           throw new HospitalBusinessException("no user found");
        }
        Users userDb = user.get();
        userDb.setUserName(userRequest.getUserName())
                .setPassword(encoder.encode(userRequest.getPassword()));
        usersRepository.save(userDb);
        UpdateUserResponse userResponse  = new UpdateUserResponse();
        userResponse.setId(userDb.getId());
        return userResponse;
    }

    public void deleteUserById(Long id) {
      Optional<Users> user =  usersRepository.findById(id);
      if (user.isEmpty()){
          throw  new HospitalBusinessException("no user found");
      }
      usersRepository.deleteById(id);
    }
}
