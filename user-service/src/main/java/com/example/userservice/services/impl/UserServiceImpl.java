package com.example.userservice.services.impl;

import com.example.userservice.dtos.PaginateUserDTO;
import com.example.userservice.dtos.UserDTO;
import com.example.userservice.entities.User;
import com.example.userservice.exception.InvalidPasswordException;
import com.example.userservice.exception.UserNotFoundException;
import com.example.userservice.helpers.PasswordValidator;
import com.example.userservice.repositories.UserRepository;
import com.example.userservice.services.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final PasswordEncoder encoder;

    @Override
    @Transactional(readOnly = false)
    public void addUser(UserDTO userDTO) {
        User user = modelMapper.map(userDTO, User.class);
        if (!PasswordValidator.isValid(user.getPassword())) {
            throw new InvalidPasswordException("error");

        }
        user.setPassword(encoder.encode(user.getPassword()));
        userRepository.save(user);
    }

    @Override
    @Cacheable
    public List<UserDTO> getAllUser() {
        List<User> userList = userRepository.findAll();
        List<UserDTO> userDTOList = new ArrayList<>();
        for (User user : userList) {
            UserDTO userDTO = modelMapper.map(user, UserDTO.class);
            userDTOList.add(userDTO);
        }
        log.info("Getting All Users from databases");
        return userDTOList;
    }
    @Override
    public PaginateUserDTO getAllUserWithPagination(Pageable pageable) {
        Page<User> users = userRepository.findAll(pageable);
        List<UserDTO> userDTOList = new ArrayList<>();
        for (User user : users.getContent()) {
            UserDTO userDTO = modelMapper.map(user, UserDTO.class);
            userDTOList.add(userDTO);
        }
        return PaginateUserDTO.builder().userDTOList(userDTOList).totalOfItems(users.getTotalElements()).totalOfPages(users.getTotalPages()).currentPage(users.getNumber()).build();
    }
    @Override
    public UserDTO getUserById(Long id) {
        Optional<User> userOptional = userRepository.findById(id);
        if (userOptional.isPresent()) {
            UserDTO userDTO = modelMapper.map(userOptional.get(), UserDTO.class);
            log.info("Getting User with id : {} from databases", id);
            return userDTO;
        }
        log.error("User with id : {} not found!", id, new UserNotFoundException("user not found"));
        throw new UserNotFoundException("UserNotFound");
    }

    @Override
    public UserDTO getUserByUsername(String username) {
        Optional<User> userOptional = userRepository.findByUsername(username);
        if (userOptional.isPresent()) {
            UserDTO userDTO = modelMapper.map(userOptional.get(), UserDTO.class);
            log.info("Getting User with username : {} from databases", username);
            return userDTO;
        }
        log.error("User with id : {} not found!", username, new UserNotFoundException("user not found"));
        throw new UserNotFoundException("UserNotFound");
    }
//    @Override
//    @Transactional()
//    @CacheEvict(value = "users", allEntries = true)
//    public void addUser(UserDTO userDTO) {
//        User user = modelMapper.map(userDTO, User.class);
//        userRepository.save(user);
//        log.info("Success add user : {}", userDTO.toString());
//    }
    @Override
    @Transactional()
    @CacheEvict(value = "users", allEntries = true)
    public void updateUser(UserDTO userDTO, Long id) {
        Optional<User> optionalUser = userRepository.findById(id);

        if (optionalUser.isEmpty()) {
            log.error("User with id : {} not found!", id, new UserNotFoundException("user not found"));
            throw new UserNotFoundException("User Not Found");
        }

        User user = optionalUser.get();
        modelMapper.map(userDTO, user);
        userRepository.save(user);
        log.info("Success update user : {}", user.toString());
    }

    @Override
    @Transactional()
    @CacheEvict(value = "users", allEntries = true)
    public void deleteUser(Long id) {
        Optional<User> optionalUser = userRepository.findById(id);

        if (optionalUser.isEmpty()) {
            log.error("User with id : {} not found!", id, new UserNotFoundException("user not found"));
            throw new UserNotFoundException("User Not Found");
        }
        User user = optionalUser.get();
        userRepository.delete(user);
        log.info("success delete user : {}", user.toString());

    }
}
