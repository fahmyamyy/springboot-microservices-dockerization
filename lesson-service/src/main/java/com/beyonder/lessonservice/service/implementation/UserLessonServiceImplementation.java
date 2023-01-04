package com.beyonder.lessonservice.service.implementation;


import com.beyonder.lessonservice.dto.UserLessonDTO;
import com.beyonder.lessonservice.entity.UserLesson;
import com.beyonder.lessonservice.exception.UserLessonNotFoundException;
import com.beyonder.lessonservice.repository.UserLessonRepository;
import com.beyonder.lessonservice.service.UserLessonService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserLessonServiceImplementation implements UserLessonService {
    private final UserLessonRepository userLessonRepository;
    private final ModelMapper modelMapper;
    @Override
    @Cacheable
    public List<UserLessonDTO> getAllUserLesson() {
        List<UserLesson> userLessonList = userLessonRepository.findAll();
        List<UserLessonDTO> userLessonDTOList = new ArrayList<>();
        for (UserLesson userLesson : userLessonList) {
            UserLessonDTO userLessonDTO = modelMapper.map(userLesson, UserLessonDTO.class);
            userLessonDTOList.add(userLessonDTO);
        }
        log.info("Getting All User Lesson from databases");
        return userLessonDTOList;
    }
    @Override
    public UserLessonDTO getUserLessonById(Long id) {
        Optional<UserLesson> userOptional = userLessonRepository.findById(id);
        if (userOptional.isPresent()) {
            UserLessonDTO userLessonDTO = modelMapper.map(userOptional.get(), UserLessonDTO.class);
            log.info("Getting User Lesson with id : {} from databases", id);
            return userLessonDTO;
        }
        log.error("User lesson with id : {} not found!", id, new UserLessonNotFoundException("user lesson not found"));
        throw new UserLessonNotFoundException("UserNotFound");
    }
    @Override
    @Transactional()
    public void addUserLesson(UserLessonDTO userLessonDTO) {
        UserLesson userLesson = modelMapper.map(userLessonDTO, UserLesson.class);
        userLessonRepository.save(userLesson);
        log.info("Success add user : {}", userLessonDTO.toString());
    }

    @Override
    @Transactional()
    public void updateUserLesson(UserLessonDTO userLessonDTO, Long id) {
        Optional<UserLesson> optionalUser = userLessonRepository.findById(id);

        if (optionalUser.isEmpty()) {
            log.error("Error user lesson with id : {}, not found!", id, new UserLessonNotFoundException("user lesson not found"));
            throw new UserLessonNotFoundException("User lesson Not Found");
        }

        UserLesson userLesson = optionalUser.get();
        modelMapper.map(userLessonDTO, userLesson);
        userLessonRepository.save(userLesson);
        log.info("Success update user : {}", userLesson.toString());
    }

    @Override
    @Transactional()
    public void deleteUserLesson(Long id) {
        Optional<UserLesson> optionalUser = userLessonRepository.findById(id);

        if (optionalUser.isEmpty()) {
            log.error("Error, user lesson with id : {} Not Found!", id, new UserLessonNotFoundException("user lesson not found"));
            throw new UserLessonNotFoundException("User lesson Not Found");
        }
        UserLesson userLesson = optionalUser.get();
        userLessonRepository.delete(userLesson);
        log.info("success delete user lesson : {}", userLesson.toString());
    }

    @Override
    public Boolean existsByUserIdLessonId(Long userId, Long lessonId) {
        Optional<UserLesson> userLesson = userLessonRepository.existsByUserIdLessonId(userId, lessonId);
        if (userLesson.isPresent()){
            return Boolean.TRUE;
        }
        return Boolean.FALSE;
    }

}
