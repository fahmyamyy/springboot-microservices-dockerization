package com.example.userservice.controllers;

import com.example.userservice.dtos.PaginateUserDTO;
import com.example.userservice.dtos.ResponseDTO;
import com.example.userservice.dtos.UserDTO;
import com.example.userservice.services.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/user")
@Slf4j
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping
    public ResponseEntity<ResponseDTO<List<UserDTO>>> getAllUser(){
//        log.info("Hit controller for getting all lessons");
        List<UserDTO> lessonDTOList = userService.getAllUser();
        return new ResponseEntity<ResponseDTO<List<UserDTO>>>(ResponseDTO
                .<List<UserDTO>>builder()
                .httpStatus(HttpStatus.OK)
                .message("success get all users")
                .data(lessonDTOList).build(), HttpStatus.OK);
    }

    @GetMapping("/pagination")
    public ResponseEntity<ResponseDTO<PaginateUserDTO>> getAllUsersWithPagination(
            @RequestParam(name = "page", defaultValue = "1") int page,
            @RequestParam(name = "limit", defaultValue = "5") int limit
    ) {
        int offset = page - 1;
        Pageable pageable = PageRequest.of(offset, limit);

        PaginateUserDTO paginateUserDTO = userService.getAllUserWithPagination(pageable);
        return new ResponseEntity<ResponseDTO<PaginateUserDTO>>(ResponseDTO
                .<PaginateUserDTO>builder()
                .httpStatus(HttpStatus.OK)
                .message("success get all lessons")
                .data(paginateUserDTO).build(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseDTO<UserDTO>> getUserById(@PathVariable Long id) {
        log.info("Hit controller for getting lesson by id");
        UserDTO userDTO = userService.getUserById(id);
        return new ResponseEntity<ResponseDTO<UserDTO>>(ResponseDTO.<UserDTO>builder()
                .httpStatus(HttpStatus.OK)
                .message("success get lesson with id " + id)
                .data(userDTO).build(), HttpStatus.OK);
    }

    @GetMapping("username/{username}")
//    public ResponseEntity<ResponseDTO<UserDTO>> getUserByUsername(@RequestParam String username) {
    public ResponseEntity<ResponseDTO<UserDTO>> getUserByUsername(@PathVariable String username) {
        log.info("Hit controller for getting lesson by username");
        UserDTO userDTO = userService.getUserByUsername(username);
        return new ResponseEntity<ResponseDTO<UserDTO>>(ResponseDTO.<UserDTO>builder()
                .httpStatus(HttpStatus.OK)
                .message("success get lesson with username " + username)
                .data(userDTO).build(), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<ResponseDTO> addUser(@RequestBody UserDTO lessonDTO) {
        log.info("Hit controller for adding lesson");
        userService.addUser(lessonDTO);
        return new ResponseEntity<ResponseDTO>(ResponseDTO.builder()
                .httpStatus(HttpStatus.CREATED)
                .message("success add lesson")
                .data(lessonDTO).build(), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ResponseDTO<UserDTO>> updateUser(@RequestBody UserDTO userDTO, @PathVariable Long id) {
        log.info("Hit controller for updating lesson by id");
        userService.updateUser(userDTO, id);
        return new ResponseEntity<ResponseDTO<UserDTO>>(ResponseDTO.<UserDTO>builder()
                .httpStatus(HttpStatus.OK)
                .message("success update lesson with id " + id)
                .data(userDTO)
                .build(), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseDTO> deleteUser(@PathVariable Long id) {
        log.info("Hit controller for deleting lesson by id");
        userService.deleteUser(id);
        return new ResponseEntity<ResponseDTO>(ResponseDTO.builder()
                .httpStatus(HttpStatus.ACCEPTED)
                .message("success delete lesson with id " + id)
                .build(), HttpStatus.ACCEPTED);
    }
}
