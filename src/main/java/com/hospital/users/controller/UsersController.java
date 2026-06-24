package com.hospital.users.controller;

import com.hospital.dto.PageResponse;
import com.hospital.entity.Users;
import com.hospital.users.dto.request.CreateUserRequest;
import com.hospital.users.dto.request.UpdateUserRequest;
import com.hospital.users.dto.response.CreateUserResponse;
import com.hospital.users.dto.response.GetUserResponse;
import com.hospital.users.dto.response.UpdateUserResponse;
import com.hospital.users.service.UsersService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user")
public class UsersController {
    @Autowired
    private UsersService usersService;

    @GetMapping("/users")
    public ResponseEntity<PageResponse<GetUserResponse>> getAllUsers(@RequestParam(defaultValue = "0")int page,
                                                                     @RequestParam(defaultValue = "10")int size,
                                                                     @RequestParam(defaultValue = "id") String sortBy,
                                                                     @RequestParam(defaultValue = "asc") String direction){
        return new ResponseEntity<>(usersService.getAllUsers(page,size,sortBy,direction),HttpStatus.OK);
    }
    @GetMapping("/{id}")
    public ResponseEntity<GetUserResponse> getUserById(@PathVariable Long id){
        return new ResponseEntity<>(usersService.getUserById(id),HttpStatus.OK);
    }

    @PostMapping("/register")
    public ResponseEntity<CreateUserResponse> register(@Valid @RequestBody CreateUserRequest userRequest){
        return new ResponseEntity<>(usersService.register(userRequest), HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody Users user){
        return new ResponseEntity<>(usersService.verify(user),HttpStatus.OK);
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout(HttpServletRequest request) {
        usersService.logout(request);
        return new ResponseEntity<>("Logged out successfully",HttpStatus.OK);
    }

    @PutMapping("/update")
    public ResponseEntity<UpdateUserResponse> updateUser(@Valid @RequestBody UpdateUserRequest userRequest){
        return new ResponseEntity<>(usersService.updateUser(userRequest),HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteUserById(@PathVariable Long id){
        usersService.deleteUserById(id);
        return new ResponseEntity<>("user deleted successfully",HttpStatus.NO_CONTENT);
    }
}
