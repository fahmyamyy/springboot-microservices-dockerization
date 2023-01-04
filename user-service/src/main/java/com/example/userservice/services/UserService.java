package com.example.userservice.services;


import com.example.userservice.dtos.PaginateUserDTO;
import com.example.userservice.dtos.UserDTO;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface UserService {
    List<UserDTO> getAllUser();
    PaginateUserDTO getAllUserWithPagination(Pageable pageable);
    UserDTO getUserById(Long id);
    UserDTO getUserByUsername(String username);
    void addUser(UserDTO lessonDTO);
    void updateUser(UserDTO lessonDTO, Long id);
    void deleteUser(Long id);
//    void takeUser(String token, Long id);

}
