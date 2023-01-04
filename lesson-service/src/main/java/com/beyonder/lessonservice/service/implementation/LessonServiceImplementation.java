package com.beyonder.lessonservice.service.implementation;

import com.beyonder.lessonservice.dto.LessonDTO;
import com.beyonder.lessonservice.dto.PaginateLessonDTO;
import com.beyonder.lessonservice.dto.UserCertsDTO;
import com.beyonder.lessonservice.dto.UserDTO;
import com.beyonder.lessonservice.entity.Lesson;
import com.beyonder.lessonservice.entity.UserLesson;
import com.beyonder.lessonservice.exception.LessonNotFoundException;
import com.beyonder.lessonservice.repository.LessonRepository;
import com.beyonder.lessonservice.repository.UserLessonRepository;
import com.beyonder.lessonservice.service.LessonService;
import com.beyonder.lessonservice.util.GetResponseRestTemplate;
import com.beyonder.lessonservice.util.GetUserDataFromJWT;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@CacheConfig(cacheNames = {"lessons"})
public class LessonServiceImplementation implements LessonService {
    private final LessonRepository lessonRepository;
    private final UserLessonRepository userLessonRepository;
    private final ModelMapper modelMapper;
    private final GetResponseRestTemplate getResponseRestTemplate;
    private final GetUserDataFromJWT getUserDataFromJWT;

    @Override
    @Cacheable
    public List<LessonDTO> getAllLesson() {
        List<Lesson> lessonList = lessonRepository.findAll();
        List<LessonDTO> lessonDTOList = new ArrayList<>();
        for (Lesson lesson : lessonList) {
            LessonDTO lessonDTO = modelMapper.map(lesson, LessonDTO.class);
            lessonDTOList.add(lessonDTO);
        }
        log.info("Getting All Lessons from databases");
        return lessonDTOList;
    }
    @Override
    public PaginateLessonDTO getAllLessonWithPagination(Pageable pageable) {
        Page<Lesson> lessons = lessonRepository.findAll(pageable);
        List<LessonDTO> lessonDTOList = new ArrayList<>();
        for (Lesson lesson : lessons.getContent()) {
            LessonDTO lessonDTO = modelMapper.map(lesson, LessonDTO.class);
            lessonDTOList.add(lessonDTO);
        }
        return PaginateLessonDTO.builder().lessonDTOList(lessonDTOList).totalOfItems(lessons.getTotalElements()).totalOfPages(lessons.getTotalPages()).currentPage(lessons.getNumber()).build();
    }
    @Override
    public LessonDTO getLessonById(Long id) {
        Optional<Lesson> lessonOptional = lessonRepository.findById(id);
        if (lessonOptional.isPresent()) {
            LessonDTO lessonDTO = modelMapper.map(lessonOptional.get(), LessonDTO.class);
            log.info("Getting Lesson with id : {} from databases", id);
            return lessonDTO;
        }
        log.error("Lesson with id : {} not found!", id, new LessonNotFoundException("lesson not found"));
        throw new LessonNotFoundException("LessonNotFound");
    }
    @Override
    @Transactional()
    @CacheEvict(value = "lessons", allEntries = true)
    public void addLesson(LessonDTO lessonDTO) {
        Lesson lesson = modelMapper.map(lessonDTO, Lesson.class);
        lessonRepository.save(lesson);
        log.info("Success add lesson : {}", lessonDTO.toString());
    }
    @Override
    @Transactional()
    @CacheEvict(value = "lessons", allEntries = true)
    public void updateLesson(LessonDTO lessonDTO, Long id) {
        Optional<Lesson> optionalLesson = lessonRepository.findById(id);

        if (optionalLesson.isEmpty()) {
            log.error("Lesson with id : {} not found!", id, new LessonNotFoundException("lesson not found"));
            throw new LessonNotFoundException("Lesson Not Found");
        }

        Lesson lesson = optionalLesson.get();
        modelMapper.map(lessonDTO, lesson);
        lessonRepository.save(lesson);
        log.info("Success update lesson : {}", lesson.toString());
    }

    @Override
    @Transactional()
    @CacheEvict(value = "lessons", allEntries = true)
    public void deleteLesson(Long id) {
        Optional<Lesson> optionalLesson = lessonRepository.findById(id);

        if (optionalLesson.isEmpty()) {
            log.error("Lesson with id : {} not found!", id, new LessonNotFoundException("lesson not found"));
            throw new LessonNotFoundException("Lesson Not Found");
        }
        Lesson lesson = optionalLesson.get();
        lessonRepository.delete(lesson);
        log.info("success delete lesson : {}", lesson.toString());

    }

    @Override
    @Transactional()
    public void takeLesson(String token, Long id) {

        UserDTO userDTO = getUserDataFromJWT.getUserDTO(token);
        UserLesson userLesson = UserLesson.builder().userId(Long.valueOf(userDTO.getId())).lessonId(id).build();
        if (this.isUserTakeLesson(Long.valueOf(userDTO.getId()),id)) {
            log.info("user sudah ambil gan!");
            throw new RuntimeException("User Has been taken this lesson!");
        } else {
            userLessonRepository.save(userLesson);
        }
    }

    @Override
    public UserCertsDTO buildUserCertsDTO(String token, Long id) {
        UserDTO userDTO = getUserDataFromJWT.getUserDTO(token);
        ResponseEntity<Boolean> response = getResponseRestTemplate.getResponse(Boolean.class, "http://localhost:8080/api/v1/userCerts/user/"+userDTO.getId()+"/cert/"+id, token);
        if (response.getBody().booleanValue()) {
            throw new RuntimeException("You have finished this lesson!");
        }
        if (this.isUserTakeLesson(Long.valueOf(userDTO.getId()),id)) {
            return UserCertsDTO.builder().certId(id).userId(Long.valueOf(userDTO.getId())).build();
        }
        throw new RuntimeException("User Has not been taken the lesson yet!");
    }

    @Override
    public Boolean isUserTakeLesson(Long userId, Long lessonId) {
        Optional<UserLesson> userLesson = userLessonRepository.existsByUserIdLessonId(userId, lessonId);
        if (userLesson.isPresent()){
            return true;
        }
        return  false;
    }

//    @Override
//    public Boolean isUserTakeLesson(Long lessonId, String token) {
//        UserDTO userDTO = getUserDataFromJWT.getUserDTO(token);
//        Optional<UserLesson> userLesson = userLessonRepository.existsByUserIdLessonId(userDTO.getId(), lessonId);
//        System.out.println(userDTO.toString());
//        if (userLesson.isPresent()){
//            return true;
//        }
//        return  false;
//    }
}
