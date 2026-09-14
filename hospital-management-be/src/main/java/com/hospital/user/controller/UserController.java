package com.hospital.user.controller;

import com.hospital.dto.PageResponse;
import com.hospital.user.dto.request.UpdateUserRequest;
import com.hospital.user.dto.request.UserSearchRequest;
import com.hospital.user.dto.response.GetUserResponse;
import com.hospital.user.dto.response.UpdateUserResponse;
import com.hospital.user.service.UserService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
@SecurityRequirement(name = "Bearer Authentication")
public class UserController {
    @Autowired
    private UserService userService;

    @GetMapping("/users")
    public ResponseEntity<PageResponse<GetUserResponse>> getAllUsers(@ParameterObject
                                                                     UserSearchRequest userSearchRequest,
                                                                     @RequestParam(defaultValue = "0") int page,
                                                                     @RequestParam(defaultValue = "10") int size,
                                                                     @RequestParam(defaultValue = "id") String sortBy,
                                                                     @RequestParam(defaultValue = "asc") String direction) {
        return new ResponseEntity<>(userService.getAllUsers(userSearchRequest, page, size, sortBy, direction), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<GetUserResponse> getUserById(@PathVariable Long id) {
        return new ResponseEntity<>(userService.getUserById(id), HttpStatus.OK);
    }


    @PutMapping("/update")
    public ResponseEntity<UpdateUserResponse> updateUser(@Valid @RequestBody UpdateUserRequest userRequest) {
        return new ResponseEntity<>(userService.updateUser(userRequest), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteUserById(@PathVariable Long id) {
        userService.deleteUserById(id);
        return new ResponseEntity<>("user deleted successfully", HttpStatus.NO_CONTENT);
    }
}
