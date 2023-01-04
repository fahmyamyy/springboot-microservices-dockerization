package com.beyonder.authservice.service.impl;

import com.beyonder.authservice.dto.UserDTO;
import com.beyonder.authservice.entity.User;
import com.beyonder.authservice.entity.UserSecurity;
import com.beyonder.authservice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class JpaUserDetailService implements UserDetailsService {
    private final UserRepository userRepository;
    private final RestTemplate restTemplate;

    private final ModelMapper modelMapper;
    @Value("${user.service}")
    private String USER_SERVICE;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> user = userRepository.findByUsername(username);
//        restTemplate.
        if(user.isPresent()) {
            return new UserSecurity(user.get());
        }
        throw new UsernameNotFoundException("username not found");
    }

    public List<UserDTO> getAll() {

        List<User> users = userRepository.findAll();
        List<UserDTO> userDTOList = new ArrayList<>();

        for(User user : users) {
            userDTOList.add(modelMapper.map(user, UserDTO.class));
        }

        return userDTOList;
    }


}
